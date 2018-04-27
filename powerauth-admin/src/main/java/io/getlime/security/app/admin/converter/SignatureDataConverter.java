package io.getlime.security.app.admin.converter;

import com.google.common.io.BaseEncoding;
import io.getlime.security.app.admin.model.SignatureData;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Converter for signature data.
 *
 * @author Roman Strobl, roman.strobl@lime-company.eu
 */
public class SignatureDataConverter {

    public SignatureData fromSignatureDataBase64(String signatureDataBase64) {
        SignatureData signatureData = new SignatureData();
        if (signatureDataBase64 == null) {
            signatureData.setRecognizedSignature(false);
            return signatureData;
        }
        String[] parts = signatureDataBase64.split("&");
        if (parts.length != 5) {
            signatureData.setRecognizedSignature(false);
            return signatureData;
        }
        try {
            signatureData.setRequestMethod(normalize(parts[0]));
            signatureData.setRequestURIIdentifier(normalize(new String(BaseEncoding.base64().decode(parts[1]))));
            signatureData.setNonce(normalize(new String(BaseEncoding.base64().decode(parts[2]))));
            signatureData.setRequestBody(normalize(new String(BaseEncoding.base64().decode(parts[3]))));
            signatureData.setApplicationSecret(normalize(new String(BaseEncoding.base64().decode(parts[4]))));
            signatureData.setRecognizedSignature(true);
            return signatureData;
        } catch (IllegalArgumentException ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.WARNING, "Invalid signature data: "+signatureDataBase64);
            signatureData.setRecognizedSignature(false);
            return signatureData;
        }
    }

    /**
     * Normalize text for embedding in HTML.
     * @param text Text to normalize.
     * @return Normalized text.
     */
    private String normalize(String text) {
        return text.replaceAll("\"", "&quot;");
    }

}
