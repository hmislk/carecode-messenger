package org.carecode.messenger.sms;

import org.carecode.messenger.common.SentStatus;

import java.util.List;
import java.util.logging.Logger;

public final class SmsService implements ISmsService {
    private static final Logger logger = Logger.getLogger(SmsService.class.getName());

    @Override
    public SmsStatus send(final List<String> recipients, final String body, final String senderName,
                          final boolean isPromo) {
        try {
            validateAndCleanEmail(recipients, body, senderName);

            final SmsStatus smsStatus = SmsSender.sendSms(recipients, body, senderName, isPromo);
            smsStatus.setMessage("Sms sent successfully.");

            return smsStatus;
        } catch (Exception e) {
            final String message = "Failed to send sms: " + e.getMessage();

            logger.severe(message);
            return new SmsStatus(SentStatus.FAILED, message);
        }
    }

    private static void validateAndCleanEmail(final List<String> recipients, final String body, final String senderName)
            throws IllegalArgumentException {
        if (body == null) {
            logger.severe("Body is required.");
            throw new IllegalArgumentException("Body is required.");
        }

        if (senderName == null || senderName.isEmpty()) {
            logger.severe("Sender name is required.");
            throw new IllegalArgumentException("Sender name is required.");
        }

        if (recipients == null) {
            logger.severe("At least one recipient number is required.");
            throw new IllegalArgumentException("At least one recipient number is required.");
        }

        recipients.removeIf(recipient -> recipient == null || recipient.isEmpty());

        if (recipients.isEmpty()) {
            logger.severe("At least one recipient number is required.");
            throw new IllegalArgumentException("At least one recipient number is required.");
        }
    }
}
