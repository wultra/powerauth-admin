/*
 * Copyright 2017 Lime - HighTech Solutions s.r.o.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.getlime.security.app.admin.controller;

import com.google.common.io.BaseEncoding;
import io.getlime.security.app.admin.util.QRUtil;
import io.getlime.powerauth.soap.*;
import io.getlime.security.powerauth.soap.spring.client.PowerAuthServiceClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Controller class related to PowerAuth activation management.
 *
 * @author Petr Dvorak
 */
@Controller
public class ActivationController {

    @Autowired
    private PowerAuthServiceClient client;

    /**
     * Return the list of activations for given users.
     *
     * @param userId  User ID to lookup the activations for.
     * @param showAll Indicates if activations in REMOVED state should be returned.
     * @param model   Model with passed parameters.
     * @return "activations" view.
     */
    @RequestMapping(value = "/activation/list")
    public String activationList(@RequestParam(value = "userId", required = false) String userId, @RequestParam(value = "showAll", required = false) Boolean showAll, Map<String, Object> model) {
        if (userId != null) {
            List<GetActivationListForUserResponse.Activations> activationList = client.getActivationListForUser(userId);
            Collections.sort(activationList, new Comparator<GetActivationListForUserResponse.Activations>() {

                @Override
                public int compare(GetActivationListForUserResponse.Activations o1, GetActivationListForUserResponse.Activations o2) {
                    return o2.getTimestampLastUsed().compare(o1.getTimestampLastUsed());
                }

            });

            model.put("activations", activationList);
            model.put("userId", userId);
            model.put("showAll", showAll);

            List<GetApplicationListResponse.Applications> applications = client.getApplicationList();
            model.put("applications", applications);
        }
        return "activations";
    }

    /**
     * Get detail of a given activation.
     *
     * @param id    Activation ID.
     * @param model Model with passed parameters.
     * @return "activationDetail" view.
     */
    @RequestMapping(value = "/activation/detail/{id}")
    public String activationDetail(
            @PathVariable(value = "id") String id,
            @RequestParam(value = "fromDate", required = false) String fromDate,
            @RequestParam(value = "toDate", required = false) String toDate,
            Map<String, Object> model) {

        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date startingDate;
        Date endingDate;
        try {
            if (toDate != null) {
                endingDate = dateFormat.parse(toDate);
            } else {
                endingDate = new Date();
                toDate = dateFormat.format(endingDate);
            }
            model.put("toDate", toDate);
            if (fromDate != null) {
                startingDate = dateFormat.parse(fromDate);
            } else {
                startingDate = new Date(endingDate.getTime() - (30L * 24L * 60L * 60L * 1000L));
                fromDate = dateFormat.format(startingDate);
            }
            model.put("fromDate", fromDate);
        } catch (ParseException e) {
            // Date parsing didn't work, OK - clear the values...
            endingDate = new Date();
            startingDate = new Date(endingDate.getTime() - (30L * 24L * 60L * 60L * 1000L));
            fromDate = dateFormat.format(startingDate);
            toDate = dateFormat.format(endingDate);
            model.put("fromDate", fromDate);
            model.put("toDate", toDate);
        }

        GetActivationStatusResponse activation = client.getActivationStatus(id);
        model.put("activationId", activation.getActivationId());
        model.put("activationName", activation.getActivationName());
        model.put("status", activation.getActivationStatus());
        model.put("blockedReason", activation.getBlockedReason());
        model.put("timestampCreated", activation.getTimestampCreated());
        model.put("timestampLastUsed", activation.getTimestampLastUsed());
        model.put("activationFingerprint", activation.getDevicePublicKeyFingerprint());
        model.put("userId", activation.getUserId());

        GetApplicationDetailResponse application = client.getApplicationDetail(activation.getApplicationId());
        model.put("applicationId", application.getApplicationId());
        model.put("applicationName", application.getApplicationName());


        List<SignatureAuditResponse.Items> auditItems = client.getSignatureAuditLog(activation.getUserId(), application.getApplicationId(), startingDate, endingDate);
        List<SignatureAuditResponse.Items> auditItemsFixed = new ArrayList<>();
        for (SignatureAuditResponse.Items item : auditItems) {
            if (item.getActivationId().equals(activation.getActivationId())) {
                item.setDataBase64(new String(BaseEncoding.base64().decode(item.getDataBase64())));
                auditItemsFixed.add(item);
            }
        }
        if (auditItemsFixed.size() > 100) {
            auditItemsFixed = auditItemsFixed.subList(0, 100);
        }
        model.put("signatures", auditItemsFixed);

        List<ActivationHistoryResponse.Items> activationHistoryItems = client.getActivationHistory(activation.getActivationId(), startingDate, endingDate);
        List<ActivationHistoryResponse.Items> trimmedActivationHistoryItems = new ArrayList<>();
        if (trimmedActivationHistoryItems.size() > 100) {
            trimmedActivationHistoryItems = activationHistoryItems.subList(0, 100);
        } else {
            trimmedActivationHistoryItems = activationHistoryItems;
        }
        model.put("history", trimmedActivationHistoryItems);

        if (activation.getActivationStatus().equals(ActivationStatus.CREATED)) {
            String activationIdShort = activation.getActivationIdShort();
            String activationOtp = activation.getActivationOTP();
            String activationSignature = activation.getActivationSignature();
            model.put("activationIdShort", activationIdShort);
            model.put("activationOtp", activationOtp);
            model.put("activationSignature", activationSignature);
            model.put("activationQR", QRUtil.encode(activationIdShort + "-" + activationOtp + "#" + activationSignature, 400));
        }

        return "activationDetail";
    }

    /**
     * Create a new activation.
     *
     * @param applicationId Application ID of an associated application.
     * @param userId        User ID.
     * @param model         Model with passed parameters.
     * @return Redirect the user to activation detail.
     */
    @RequestMapping(value = "/activation/create")
    public String activationCreate(@RequestParam(value = "applicationId") Long applicationId, @RequestParam(value = "userId") String userId, Map<String, Object> model) {

        InitActivationResponse response = client.initActivation(userId, applicationId);

        model.put("activationIdShort", response.getActivationIdShort());
        model.put("activationId", response.getActivationId());
        model.put("activationOTP", response.getActivationOTP());
        model.put("activationSignature", response.getActivationSignature());

        return "redirect:/activation/detail/" + response.getActivationId();
    }

    /**
     * Commit activation.
     *
     * @param activationId Activation ID.
     * @param model        Model with passed parameters.
     * @return Redirect the user to activation detail.
     */
    @RequestMapping(value = "/activation/create/do.submit", method = RequestMethod.POST)
    public String activationCreateCommitAction(@RequestParam(value = "activationId") String activationId, Map<String, Object> model) {
        CommitActivationResponse commitActivation = client.commitActivation(activationId);
        return "redirect:/activation/detail/" + commitActivation.getActivationId();
    }

    /**
     * Block activation.
     *
     * @param activationId Activation ID
     * @param redirect     Where to redirect user
     * @param model        Model with passed parameters.
     * @return Redirect user to given URL or to activation detail, in case 'redirect' is null or empty.
     */
    @RequestMapping(value = "/activation/block/do.submit", method = RequestMethod.POST)
    public String blockActivation(@RequestParam(value = "activationId") String activationId, @RequestParam(value = "redirect") String redirect, Map<String, Object> model) {
        BlockActivationResponse blockActivation = client.blockActivation(activationId, null);
        if (redirect != null && !redirect.trim().isEmpty()) {
            return "redirect:" + redirect;
        }
        return "redirect:/activation/detail/" + blockActivation.getActivationId();
    }

    /**
     * Unblock activation.
     *
     * @param activationId Activation ID
     * @param redirect     Where to redirect user
     * @param model        Model with passed parameters.
     * @return Redirect user to given URL or to activation detail, in case 'redirect' is null or empty.
     */
    @RequestMapping(value = "/activation/unblock/do.submit", method = RequestMethod.POST)
    public String unblockActivation(@RequestParam(value = "activationId") String activationId, @RequestParam(value = "redirect") String redirect, Map<String, Object> model) {
        UnblockActivationResponse unblockActivation = client.unblockActivation(activationId);
        if (redirect != null && !redirect.trim().isEmpty()) {
            return "redirect:" + redirect;
        }
        return "redirect:/activation/detail/" + unblockActivation.getActivationId();
    }

    /**
     * Commit activation.
     *
     * @param activationId Activation ID
     * @param redirect     Where to redirect user
     * @param model        Model with passed parameters.
     * @return Redirect user to given URL or to activation detail, in case 'redirect' is null or empty.
     */
    @RequestMapping(value = "/activation/commit/do.submit", method = RequestMethod.POST)
    public String commitActivation(@RequestParam(value = "activationId") String activationId, @RequestParam(value = "redirect") String redirect, Map<String, Object> model) {
        CommitActivationResponse commitActivation = client.commitActivation(activationId);
        if (redirect != null && !redirect.trim().isEmpty()) {
            return "redirect:" + redirect;
        }
        return "redirect:/activation/detail/" + commitActivation.getActivationId();
    }

    /**
     * Remove activation.
     *
     * @param activationId Activation ID
     * @param redirect     Where to redirect user
     * @param model        Model with passed parameters.
     * @return Redirect user to given URL or to activation detail, in case 'redirect' is null or empty.
     */
    @RequestMapping(value = "/activation/remove/do.submit", method = RequestMethod.POST)
    public String removeActivation(@RequestParam(value = "activationId") String activationId, @RequestParam(value = "redirect") String redirect, Map<String, Object> model) {
        RemoveActivationResponse removeActivation = client.removeActivation(activationId);
        if (redirect != null && !redirect.trim().isEmpty()) {
            return "redirect:" + redirect;
        }
        return "redirect:/activation/detail/" + removeActivation.getActivationId();
    }

}
