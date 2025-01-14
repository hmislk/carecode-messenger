package org.carecode.messenger.email;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class EmailRequest {
    @JsonProperty("subject")
    private String subject;

    @JsonProperty("body")
    private String body;

    @JsonProperty("recipients")
    private List<String> recipients;

    @JsonProperty("replyTo")
    private String replyTo;

    @JsonProperty("isHtml")
    private Boolean isHtml;

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public List<String> getRecipients() {
        return recipients;
    }

    public void setRecipients(List<String> recipients) {
        this.recipients = recipients;
    }

    public String getReplyTo() {
        return replyTo;
    }

    public void setReplyTo(String replyTo) {
        this.replyTo = replyTo;
    }

    public Boolean isHtml() {
        return isHtml;
    }

    public void setIsHtml(Boolean isHtml) {
        this.isHtml = isHtml;
    }

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
