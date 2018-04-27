package io.getlime.security.app.admin.model;

/**
 * Structured data used in signatures.
 *
 * @author Roman Strobl, roman.strobl@lime-company.eu
 */
public class SignatureData {

    private String requestMethod;
    private String requestURIIdentifier;
    private String nonce;
    private String requestBody;
    private String applicationSecret;
    private boolean recognizedSignature;

    public String getRequestMethod() {
        return requestMethod;
    }

    public void setRequestMethod(String requestMethod) {
        this.requestMethod = requestMethod;
    }

    public String getRequestURIIdentifier() {
        return requestURIIdentifier;
    }

    public void setRequestURIIdentifier(String requestURIIdentifier) {
        this.requestURIIdentifier = requestURIIdentifier;
    }

    public String getNonce() {
        return nonce;
    }

    public void setNonce(String nonce) {
        this.nonce = nonce;
    }

    public String getRequestBody() {
        return requestBody;
    }

    public void setRequestBody(String requestBody) {
        this.requestBody = requestBody;
    }

    public String getApplicationSecret() {
        return applicationSecret;
    }

    public void setApplicationSecret(String applicationSecret) {
        this.applicationSecret = applicationSecret;
    }

    public boolean isRecognizedSignature() {
        return recognizedSignature;
    }

    public void setRecognizedSignature(boolean recognizedSignature) {
        this.recognizedSignature = recognizedSignature;
    }
}
