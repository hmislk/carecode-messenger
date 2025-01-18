package org.carecode.messenger.email;

import org.carecode.messenger.common.SentStatus;
import org.carecode.messenger.common.contract.AbstractMessageStatus;

public class EmailStatus extends AbstractMessageStatus {
    public EmailStatus(SentStatus status) {
        super(status);
    }

    public EmailStatus(SentStatus status, String message) {
        super(status, message);
    }
}
