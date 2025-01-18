package org.carecode.messenger.common.contract;

import org.carecode.messenger.common.SentStatus;

public abstract class AbstractMessageStatus {
    private SentStatus status;
    private String message;

    public AbstractMessageStatus(SentStatus status) {
        this.status = status;
    }

    public AbstractMessageStatus(SentStatus status, String message) {
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
        return "AbstractMessageStatus{" +
                "status=" + status +
                ", message='" + message + '\'' +
                '}';
    }
}
