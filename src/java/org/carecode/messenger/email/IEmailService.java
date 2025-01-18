package org.carecode.messenger.email;

import org.carecode.messenger.common.contract.AbstractMessageStatus;

import java.util.List;

public interface IEmailService {
    /**
     * Sends an email to multiple recipients in plain text or HTML.
     *
     * @param recipients recipient email addresses
     * @param subject    email subject
     * @param body       email body in plain text or HTML
     * @param isHtml     true if the body is HTML, false if plain text
     * @param replyTo    reply-to email address
     */
    AbstractMessageStatus send(
            final List<String> recipients, final String subject, final String body,
            final boolean isHtml, final String replyTo);
}
