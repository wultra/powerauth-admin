package io.getlime.security.app.admin.converter;

import com.google.common.io.BaseEncoding;
import io.getlime.security.app.admin.model.SignatureData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;

/**
 * Converter for signature data.
 *
 * @author Roman Strobl, roman.strobl@lime-company.eu
 */
public class SignatureDataConverter {

    private static final Logger logger = LoggerFactory.getLogger(SignatureDataConverter.class);

    /**
     * Convert unstructured signature data to structured signature data.
     * @param signatureDataBase64 Unstructured signature data.
     * @return Structured signature data.
     */
    public SignatureData fromSignatureDataBase64(String signatureDataBase64) {
        if (signatureDataBase64 == null) {
            return null;
        }
        String[] parts = signatureDataBase64.split("&");
        if (parts.length != 5) {
            return null;
        }
        try {
            SignatureData signatureData = new SignatureData();
            signatureData.setRequestMethod(normalizeTextForHTML(parts[0]));
            signatureData.setRequestURIIdentifier(normalizeTextForHTML(new String(BaseEncoding.base64().decode(parts[1]), StandardCharsets.UTF_8)));
            signatureData.setNonce(normalizeTextForHTML(new String(BaseEncoding.base64().decode(parts[2]), StandardCharsets.UTF_8)));
            signatureData.setRequestBody(normalizeTextForHTML(new String(BaseEncoding.base64().decode(parts[3]), StandardCharsets.UTF_8)));
            signatureData.setApplicationSecret(normalizeTextForHTML(new String(BaseEncoding.base64().decode(parts[4]), StandardCharsets.UTF_8)));
            return signatureData;
        } catch (IllegalArgumentException ex) {
            logger.warn("Invalid signature data: {}", signatureDataBase64);
            return null;
        }
    }

    /**
     * Normalize text for embedding in HTML.
     * @param text Text to normalize.
     * @return Normalized text.
     */
    private String normalizeTextForHTML(String text) {
        return text.replaceAll("\"", "&quot;");
    }

}
