package org.carecode.messenger.email;

import org.carecode.messenger.common.SentStatus;
import org.carecode.messenger.common.contract.SmtpConfig;

import java.util.List;
import java.util.logging.Logger;

public final class EmailService implements IEmailService {
    private static final Logger logger = Logger.getLogger(EmailService.class.getName());

    @Override
    public EmailStatus send(final List<String> recipients, final String subject, final String body,
                            final boolean isHtml, final String replyTo) {
        try {
            validateAndCleanEmail(recipients, subject, body);

            final String validatedReplyTo = (replyTo != null && !replyTo.isEmpty()) ? replyTo : "no_reply@example.com";

            final EmailStatus emailStatus = EmailSender.sendEmail(recipients, subject, body, isHtml, validatedReplyTo);
            emailStatus.setMessage("Email sent successfully.");

            return emailStatus;
        } catch (Exception e) {
            final String message = "Failed to send email: " + e.getMessage();

            logger.severe(message);
            return new EmailStatus(SentStatus.FAILED, message);
        }
    }

    @Override
    public EmailStatus send(final List<String> recipients, final String subject, final String body,
                            final boolean isHtml, final String replyTo, final SmtpConfig smtpConfigurations) {
        try {
            validateAndCleanEmail(recipients, subject, body);

            final String validatedReplyTo = (replyTo != null && !replyTo.isEmpty()) ? replyTo : "no_reply@example.com";

            final EmailStatus emailStatus = EmailSender.sendEmail(recipients, subject, body, isHtml, validatedReplyTo, smtpConfigurations);
            emailStatus.setMessage("Email sent successfully.");

            return emailStatus;
        } catch (Exception e) {
            final String message = "Failed to send email: " + e.getMessage();

            logger.severe(message);
            return new EmailStatus(SentStatus.FAILED, message);
        }
    }

    private static void validateAndCleanEmail(final List<String> recipients, final String subject, final String body)
            throws IllegalArgumentException {
        if (subject == null) {
            logger.severe("Subject is required.");
            throw new IllegalArgumentException("Subject is required.");
        }

        if (body == null) {
            logger.severe("Body is required.");
            throw new IllegalArgumentException("Body is required.");
        }

        if (recipients == null) {
            logger.severe("At least one recipient email address is required.");
            throw new IllegalArgumentException("At least one recipient email address is required.");
        }

        recipients.removeIf(recipient -> recipient == null || recipient.isEmpty());

        if (recipients.isEmpty()) {
            logger.severe("At least one recipient email address is required.");
            throw new IllegalArgumentException("At least one recipient email address is required.");
        }
    }
}
