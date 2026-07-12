package org.carecode.messenger.email;

import org.carecode.messenger.common.SentStatus;
import org.carecode.messenger.common.contract.EmailAttachment;
import org.carecode.messenger.common.contract.SmtpConfig;

import java.util.Base64;
import java.util.List;
import java.util.logging.Logger;

public final class EmailService implements IEmailService {
    private static final Logger logger = Logger.getLogger(EmailService.class.getName());

    @Override
    public EmailStatus send(final List<String> recipients, final String subject, final String body,
                            final boolean isHtml, final String replyTo) {
        return send(recipients, subject, body, isHtml, replyTo, (List<EmailAttachment>) null);
    }

    @Override
    public EmailStatus send(final List<String> recipients, final String subject, final String body,
                            final boolean isHtml, final String replyTo, final SmtpConfig smtpConfigurations) {
        return send(recipients, subject, body, isHtml, replyTo, null, smtpConfigurations);
    }

    @Override
    public EmailStatus send(final List<String> recipients, final String subject, final String body,
                            final boolean isHtml, final String replyTo, final List<EmailAttachment> attachments) {
        try {
            validateAndCleanEmail(recipients, subject, body);
            validateAttachments(attachments);

            final String validatedReplyTo = (replyTo != null && !replyTo.isEmpty()) ? replyTo : "no_reply@example.com";

            final EmailStatus emailStatus = EmailSender.sendEmail(recipients, subject, body, isHtml, validatedReplyTo, attachments);
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
                            final boolean isHtml, final String replyTo, final List<EmailAttachment> attachments,
                            final SmtpConfig smtpConfigurations) {
        try {
            validateAndCleanEmail(recipients, subject, body);
            validateAttachments(attachments);

            final String validatedReplyTo = (replyTo != null && !replyTo.isEmpty()) ? replyTo : "no_reply@example.com";

            final EmailStatus emailStatus = EmailSender.sendEmail(recipients, subject, body, isHtml, validatedReplyTo, attachments, smtpConfigurations);
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

    private static void validateAttachments(final List<EmailAttachment> attachments) throws IllegalArgumentException {
        if (attachments == null || attachments.isEmpty()) {
            return;
        }

        for (final EmailAttachment attachment : attachments) {
            if (attachment == null) {
                logger.severe("Attachment entry must not be null.");
                throw new IllegalArgumentException("Attachment entry must not be null.");
            }
            if (attachment.fileName() == null || attachment.fileName().isEmpty()) {
                logger.severe("Attachment file name is required.");
                throw new IllegalArgumentException("Attachment file name is required.");
            }
            if (attachment.base64Content() == null || attachment.base64Content().isEmpty()) {
                logger.severe("Attachment content is required: " + attachment.fileName());
                throw new IllegalArgumentException("Attachment content is required: " + attachment.fileName());
            }
            try {
                Base64.getDecoder().decode(attachment.base64Content());
            } catch (IllegalArgumentException e) {
                logger.severe("Attachment content is not valid base64: " + attachment.fileName());
                throw new IllegalArgumentException("Attachment content is not valid base64: " + attachment.fileName());
            }
        }
    }
}
