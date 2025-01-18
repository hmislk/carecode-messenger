package org.carecode.messenger.common.contract;

import org.carecode.messenger.common.SentStatus;

public abstract class AbstractMessageStatus {
    private final SentStatus status;
    private final String message;

    public AbstractMessageStatus(SentStatus status, String message) {
        this.status = status;
        this.message = message;
    }

    public SentStatus getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return "AbstractMessageStatus{" +
                "status=" + status +
                ", message='" + message + '\'' +
                '}';
    }
}
