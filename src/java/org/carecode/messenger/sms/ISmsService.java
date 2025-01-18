package org.carecode.messenger.sms;

import org.carecode.messenger.common.contract.AbstractMessageStatus;

import java.util.List;

public interface ISmsService {
    /**
     * Sends a sms to multiple recipients.
     *
     * @param recipients recipient number
     * @param body       message body
     * @param senderName sender name
     * @param isPromo    true if the sms is a promo, false if not
     */
    AbstractMessageStatus send(final List<String> recipients, final String body,
                               final String senderName, final boolean isPromo);
}
