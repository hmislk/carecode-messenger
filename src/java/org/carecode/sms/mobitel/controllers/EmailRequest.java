package org.carecode.sms.mobitel.controllers;

import java.util.List;

public final class EmailRequest {
    public String subject;
    public String body;
    public List<String> recipients;
    public String replyTo;
    public Boolean isHtml;

    @Override
    public String toString() {
        return "EmailRequest{" +
                "subject='" + subject + '\'' +
                ", body='" + body + '\'' +
                ", recipients=" + recipients +
                ", replyTo='" + replyTo + '\'' +
                ", isHtml=" + isHtml +
                '}';
    }
}
