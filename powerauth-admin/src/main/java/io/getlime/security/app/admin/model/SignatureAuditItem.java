package io.getlime.security.app.admin.model;

import io.getlime.powerauth.soap.ActivationStatus;
import io.getlime.powerauth.soap.KeyValueMap;
import io.getlime.powerauth.soap.SignatureType;

import java.util.Date;

/**
 * Signature audit item.
 *
 * @author Roman Strobl, roman.strobl@lime-company.eu
 */
public class SignatureAuditItem {

    private long id;
    private String userId;
    private long applicationId;
    private String activationId;
    private long activationCounter;
    private ActivationStatus activationStatus;
    private KeyValueMap additionalInfo;
    private String dataBase64;
    private SignatureType signatureType;
    private String signature;
    private String note;
    private boolean valid;
    private Date timestampCreated;
    private SignatureData signatureData;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public long getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(long applicationId) {
        this.applicationId = applicationId;
    }

    public String getActivationId() {
        return activationId;
    }

    public void setActivationId(String activationId) {
        this.activationId = activationId;
    }

    public long getActivationCounter() {
        return activationCounter;
    }

    public void setActivationCounter(long activationCounter) {
        this.activationCounter = activationCounter;
    }

    public ActivationStatus getActivationStatus() {
        return activationStatus;
    }

    public void setActivationStatus(ActivationStatus activationStatus) {
        this.activationStatus = activationStatus;
    }

    public KeyValueMap getAdditionalInfo() {
        return additionalInfo;
    }

    public void setAdditionalInfo(KeyValueMap additionalInfo) {
        this.additionalInfo = additionalInfo;
    }

    public String getDataBase64() {
        return dataBase64;
    }

    public void setDataBase64(String dataBase64) {
        this.dataBase64 = dataBase64;
    }

    public SignatureType getSignatureType() {
        return signatureType;
    }

    public void setSignatureType(SignatureType signatureType) {
        this.signatureType = signatureType;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }

    public Date getTimestampCreated() {
        return timestampCreated;
    }

    public void setTimestampCreated(Date timestampCreated) {
        this.timestampCreated = timestampCreated;
    }

    public SignatureData getSignatureData() {
        return signatureData;
    }

    public void setSignatureData(SignatureData signatureData) {
        this.signatureData = signatureData;
    }
}
