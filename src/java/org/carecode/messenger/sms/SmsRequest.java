package org.carecode.messenger.sms;

/**
 * @author Dr M H B Ariyaratne <buddhika.ari@gmail.com>
 */
public class SmsRequest {
    private String username;
    private String password;
    private String senderName;
    private String recipientNumber;
    private String message;
    private Boolean isPromo;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getRecipientNumber() {
        return recipientNumber;
    }

    public void setRecipientNumber(String recipientNumber) {
        this.recipientNumber = recipientNumber;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isPromo() {
        return isPromo != null && isPromo;
    }

    public void setIsPromo(Boolean isPromo) {
        this.isPromo = isPromo;
    }

    @Override
    public String toString() {
        return "SmsRequest{" +
                ", senderName='" + senderName + '\'' +
                ", recipientNumber='" + recipientNumber + '\'' +
                ", message='" + message + '\'' +
                ", isPromo='" + isPromo + '\'' +
                '}';
    }
}
