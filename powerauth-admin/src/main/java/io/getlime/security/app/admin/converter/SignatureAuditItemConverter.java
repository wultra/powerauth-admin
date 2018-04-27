package io.getlime.security.app.admin.converter;

import io.getlime.powerauth.soap.SignatureAuditResponse;
import io.getlime.security.app.admin.model.SignatureAuditItem;

/**
 * Converter for signature audit items.
 *
 * @author Roman Strobl, roman.strobl@lime-company.eu
 */
public class SignatureAuditItemConverter {

    public SignatureAuditItem fromSignatureAuditResponseItem(SignatureAuditResponse.Items signatureAuditItem) {
        SignatureAuditItem result = new SignatureAuditItem();
        result.setId(signatureAuditItem.getId());
        result.setUserId(signatureAuditItem.getUserId());
        result.setApplicationId(signatureAuditItem.getApplicationId());
        result.setActivationId(signatureAuditItem.getActivationId());
        result.setActivationCounter(signatureAuditItem.getActivationCounter());
        result.setActivationStatus(signatureAuditItem.getActivationStatus());
        result.setAdditionalInfo(signatureAuditItem.getAdditionalInfo());
        result.setDataBase64(signatureAuditItem.getDataBase64());
        result.setSignatureType(signatureAuditItem.getSignatureType());
        result.setSignature(signatureAuditItem.getSignature());
        result.setNote(signatureAuditItem.getNote());
        result.setValid(signatureAuditItem.isValid());
        result.setTimestampCreated(signatureAuditItem.getTimestampCreated().toGregorianCalendar().getTime());
        return result;
    }

}
