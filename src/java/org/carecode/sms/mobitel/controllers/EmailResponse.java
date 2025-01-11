package org.carecode.sms.mobitel.controllers;

public final class EmailResponse {
    public SentStatus status;
    public String message;

    public EmailResponse(SentStatus status, String message) {
        this.status = status;
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
