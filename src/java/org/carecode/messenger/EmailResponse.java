package org.carecode.messenger;

import com.fasterxml.jackson.annotation.JsonProperty;

public class EmailResponse {
    @JsonProperty("status")
    private SentStatus status;

    @JsonProperty("message")
    private String message;

    public EmailResponse(SentStatus status, String message) {
        this.status = status;
        this.message = message;
    }

    public SentStatus getStatus() {
        return status;
    }

    public void setStatus(SentStatus status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "EmailResponse{" +
                "status=" + status +
                ", message='" + message + '\'' +
                '}';
    }
}
