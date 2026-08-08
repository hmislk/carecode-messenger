package org.carecode.messenger.common.contract;

/**
 * Plain JSON-B-friendly bean (Payara's JAX-RS runtime deserializes with
 * Yasson/JSON-B, not Jackson, even though Jackson is on the compile
 * classpath) - needs a no-arg constructor and public fields.
 */
public class EmailAttachment {
    public String fileName;
    public String contentType;
    public String base64Content;

    public EmailAttachment() {
    }

    public String fileName() {
        return fileName;
    }

    public String contentType() {
        return contentType;
    }

    public String base64Content() {
        return base64Content;
    }

    @Override
    public String toString() {
        return "EmailAttachment{" +
                "fileName='" + fileName + '\'' +
                ", contentType='" + contentType + '\'' +
                ", base64ContentLength=" + (base64Content != null ? base64Content.length() : 0) +
                '}';
    }
}
