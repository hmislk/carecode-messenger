package org.carecode.messenger.email;

import org.carecode.messenger.common.contract.AbstractMessageStatus;

import java.util.List;

public interface IEmailService {
    /**
     * Sends a message to multiple recipients in plain text or HTML.
     *
     * @param to      recipients (numbers / email addresses)
     * @param subject message subject
     * @param body    message body in plain text or HTML
     * @param isHtml  true if the body is HTML, false if plain text
     * @param replyTo reply-to address
     * @throws RuntimeException if the message could not be sent
     */
    AbstractMessageStatus send(
            final List<String> to,
            final String subject,
            final String body,
            final boolean isHtml,
            final String replyTo) throws RuntimeException;
}
