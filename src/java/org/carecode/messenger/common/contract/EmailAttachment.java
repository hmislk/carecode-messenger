package org.carecode.messenger.common.contract;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record EmailAttachment(String fileName, String contentType, String base64Content) {

    @Override
    public String toString() {
        return "EmailAttachment{" +
                "fileName='" + fileName + '\'' +
                ", contentType='" + contentType + '\'' +
                ", base64ContentLength=" + (base64Content != null ? base64Content.length() : 0) +
                '}';
    }
}
