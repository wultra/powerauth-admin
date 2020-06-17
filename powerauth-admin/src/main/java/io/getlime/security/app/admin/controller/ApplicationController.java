/*
 * Copyright 2017 Wultra s.r.o.
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

import com.google.common.collect.Lists;
import io.getlime.powerauth.soap.v3.*;
import io.getlime.security.powerauth.soap.spring.client.PowerAuthServiceClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Controller related to application and application version management.
 *
 * @author Petr Dvorak
 */
@Controller
public class ApplicationController {

    private final PowerAuthServiceClient client;

    @Autowired
    public ApplicationController(PowerAuthServiceClient client) {
        this.client = client;
    }

    /**
     * Redirect '/' URL to the list of application.
     *
     * @return Redirect view to list of applications.
     */
    @RequestMapping(value = "/")
    public String homePage() {
        List<GetApplicationListResponse.Applications> applicationList = client.getApplicationList();
        if (applicationList.isEmpty()) {
            return "redirect:/application/list";
        } else {
            return "redirect:/activation/list";
        }
    }

    /**
     * Show list of applications.
     *
     * @param model Model with passed parameters.
     * @return "applications" view.
     */
    @RequestMapping(value = "/application/list")
    public String applicationList(Map<String, Object> model) {
        List<GetApplicationListResponse.Applications> applicationList = client.getApplicationList();
        model.put("applications", applicationList);
        return "applications";
    }

    /**
     * Show application detail.
     *
     * @param id    Application ID.
     * @param model Model with passed parameters.
     * @return "applicationDetail" view.
     */
    @RequestMapping(value = "/application/detail/{id}")
    public String applicationDetail(@PathVariable(value = "id") Long id, Map<String, Object> model) {
        GetApplicationDetailResponse applicationDetails = client.getApplicationDetail(id);
        GetRecoveryConfigResponse recoveryConfig = client.getRecoveryConfig(id);
        List<GetCallbackUrlListResponse.CallbackUrlList> callbackUrlList = client.getCallbackUrlList(id);
        model.put("id", applicationDetails.getApplicationId());
        model.put("name", applicationDetails.getApplicationName());
        model.put("masterPublicKey", applicationDetails.getMasterPublicKey());
        model.put("activationRecoveryEnabled", recoveryConfig.isActivationRecoveryEnabled());
        model.put("recoveryPostcardEnabled", recoveryConfig.isRecoveryPostcardEnabled());
        model.put("allowMultipleRecoveryCodes", recoveryConfig.isAllowMultipleRecoveryCodes());
        model.put("postcardPublicKey", recoveryConfig.getPostcardPublicKey());
        model.put("remotePostcardPublicKey", recoveryConfig.getRemotePostcardPublicKey());
        model.put("versions", Lists.reverse(applicationDetails.getVersions()));
        model.put("roles", applicationDetails.getApplicationRoles());
        model.put("callbacks", callbackUrlList);
        return "applicationDetail";
    }

    /**
     * Create a new application.
     *
     * @return "applicationCreate" view.
     */
    @RequestMapping(value = "/application/create")
    public String applicationCreate() {
        return "applicationCreate";
    }

    /**
     * Create a new application version.
     *
     * @param id    Application ID
     * @param model Model with passed parameters.
     * @return "applicationVersionCreate" view.
     */
    @RequestMapping(value = "/application/detail/{id}/version/create")
    public String applicationVersionCreate(@PathVariable Long id, Map<String, Object> model) {
        model.put("applicationId", id);
        return "applicationVersionCreate";
    }

    /**
     * Show application callback create form.
     *
     * @param id    Application ID.
     * @param model Model with passed parameters.
     * @return "callbackCreate" view.
     */
    @RequestMapping(value = "/application/detail/{id}/callback/create")
    public String applicationCreateCallback(@PathVariable(value = "id") Long id, Map<String, Object> model) {
        model.put("applicationId", id);
        return "callbackCreate";
    }

    /**
     * Show application role create form.
     *
     * @param id Application ID.
     * @param model Model with passed parameters.
     * @return "roleCreate" view.
     */
    @RequestMapping(value = "/application/detail/{id}/role/create")
    public String applicationCreateRole(@PathVariable(value = "id") Long id, Map<String, Object> model) {
        model.put("applicationId", id);
        return "roleCreate";
    }

    /**
     * Execute the application create action by calling the SOAP service.
     *
     * @param name Application name.
     * @param redirectAttributes Redirect attributes.
     * @return Redirect to the new application details.
     */
    @RequestMapping(value = "/application/create/do.submit", method = RequestMethod.POST)
    public String applicationCreateAction(@RequestParam String name, RedirectAttributes redirectAttributes) {
        if (name == null || name.trim().isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Application name must not be empty.");
            return "redirect:/application/create";
        }
        CreateApplicationResponse application = client.createApplication(name);
        return "redirect:/application/detail/" + application.getApplicationId();
    }

    /**
     * Execute the application version create action by calling the SOAP service.
     *
     * @param applicationId      Application ID.
     * @param name               Version name.
     * @param redirectAttributes Redirect attributes.
     * @return Redirect to application detail (application versions are visible there).
     */
    @RequestMapping(value = "/application/detail/{applicationId}/version/create/do.submit", method = RequestMethod.POST)
    public String applicationVersionCreateAction(@PathVariable Long applicationId, @RequestParam String name, RedirectAttributes redirectAttributes) {
        if (name == null || name.trim().isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Application version name must not be empty.");
            return "redirect:/application/detail/" + applicationId + "/version/create";
        }
        client.createApplicationVersion(applicationId, name);
        return "redirect:/application/detail/" + applicationId;
    }

    /**
     * Execute the action that marks application version supported / unsupported.
     *
     * @param version Application version.
     * @param enabled True for supported, False for unsupported
     * @param id      Application ID (path variable), for the redirect purposes
     * @return Redirect to application detail (application versions are visible there).
     */
    @RequestMapping(value = "/application/detail/{id}/version/update/do.submit", method = RequestMethod.POST)
    public String applicationUpdateAction(
            @RequestParam(value = "version", required = false) Long version,
            @RequestParam(value = "enabled") Boolean enabled,
            @PathVariable(value = "id") Long id) {
        if (enabled) {
            client.supportApplicationVersion(version);
        } else {
            client.unsupportApplicationVersion(version);
        }
        return "redirect:/application/detail/" + id;
    }

    /**
     * Execute the action that creates a new callback on given application.
     *
     * @param id    Application ID.
     * @param name  Callback URL name.
     * @param callbackUrl Callback URL value.
     * @param redirectAttributes Redirect attributes.
     * @return Redirect to application detail, callbacks tab.
     */
    @RequestMapping(value = "/application/detail/{id}/callback/create/do.submit")
    public String applicationCreateCallbackAction(
            @RequestParam(value = "name") String name,
            @RequestParam(value = "callbackUrl") String callbackUrl,
            @PathVariable(value = "id") Long id, RedirectAttributes redirectAttributes) {
        String error = null;
        if (name == null || name.trim().isEmpty()) {
            error = "Callback name must not be empty.";
        } else if (callbackUrl == null || callbackUrl.trim().isEmpty()) {
            error = "Callback URL must not be empty.";
        } else {
            try {
                new URL(callbackUrl);
            } catch (MalformedURLException e) {
                error = "Callback URL is not in a valid format";
            }
        }
        if (error != null) {
            redirectAttributes.addFlashAttribute("error", error);
            redirectAttributes.addFlashAttribute("name", name);
            redirectAttributes.addFlashAttribute("callbackUrl", callbackUrl);
            return "redirect:/application/detail/" + id + "/callback/create";
        }
        client.createCallbackUrl(id, name, callbackUrl);
        return "redirect:/application/detail/" + id + "#callbacks";
    }

    /**
     * Execute the action that removes a callback with given ID.
     *
     * @param id    Application ID.
     * @param callbackId Callback ID.
     * @return Redirect to application detail, callbacks tab.
     */
    @RequestMapping(value = "/application/detail/{id}/callback/remove/do.submit")
    public String applicationRemoveCallbackAction(
            @RequestParam(value = "id") String callbackId,
            @PathVariable(value = "id") Long id) {
        client.removeCallbackUrl(callbackId);
        return "redirect:/application/detail/" + id + "#callbacks";
    }

    /**
     * Execute the action that creates a new role assigned to given application.
     *
     * @param id Application ID.
     * @param name Role name.
     * @param redirectAttributes Redirect attributes.
     * @return Redirect to application detail, roles tab.
     */
    @RequestMapping(value = "/application/detail/{id}/role/create/do.submit")
    public String applicationCreateRoleAction(
            @RequestParam(value = "name") String name,
            @PathVariable(value = "id") Long id, RedirectAttributes redirectAttributes) {
        String error = null;
        if (name == null || name.trim().isEmpty()) {
            error = "Role name must not be empty.";
        }
        if (error != null) {
            redirectAttributes.addFlashAttribute("error", error);
            redirectAttributes.addFlashAttribute("name", name);
            return "redirect:/application/detail/" + id + "/role/create";
        }
        client.addApplicationRoles(id, Collections.singletonList(name));
        return "redirect:/application/detail/" + id + "#roles";
    }

    /**
     * Execute the action that removes a callback with given ID.
     *
     * @param id Application ID.
     * @param name Role name.
     * @return Redirect to application detail, roles tab.
     */
    @RequestMapping(value = "/application/detail/{id}/role/remove/do.submit")
    public String applicationRemoveRoleAction(
            @RequestParam(value = "name") String name,
            @PathVariable(value = "id") Long id) {
        client.removeApplicationRoles(id, Collections.singletonList(name));
        return "redirect:/application/detail/" + id + "#roles";
    }

    /**
     * Update recovery configuration.
     * @param activationRecoveryEnabled Whether activation recovery is enabled.
     * @param recoveryPostcardEnabled Whether recovery postcard is enabled.
     * @param allowMultipleRecoveryCodes Whether multiple recovery codes are allowed per user.
     * @param remotePostcardPublicKey Base64 encoded printing center public key.
     * @param id Application ID.
     * @return Redirect to application detail, recovery tab.
     */
    @RequestMapping(value = "/application/detail/{id}/recovery/update/do.submit")
    public String applicationUpdateRecoveryConfigAction(
            @RequestParam(value = "activationRecoveryEnabled", required = false) boolean activationRecoveryEnabled,
            @RequestParam(value = "recoveryPostcardEnabled", required = false) boolean recoveryPostcardEnabled,
            @RequestParam(value = "allowMultipleRecoveryCodes", required = false) boolean allowMultipleRecoveryCodes,
            @RequestParam(value = "remotePostcardPublicKey", required = false) String remotePostcardPublicKey,
            @PathVariable(value = "id") Long id) {
        if (!activationRecoveryEnabled && recoveryPostcardEnabled) {
            // Turn off recovery postcard in case activation recovery is disabled
            recoveryPostcardEnabled = false;
        }
        client.updateRecoveryConfig(id, activationRecoveryEnabled, recoveryPostcardEnabled, allowMultipleRecoveryCodes, remotePostcardPublicKey);
        return "redirect:/application/detail/" + id + "#recovery";
    }


}
