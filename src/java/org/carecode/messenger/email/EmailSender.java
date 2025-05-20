package org.carecode.messenger.email;

import jakarta.mail.*;
import jakarta.mail.internet.AddressException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import org.carecode.messenger.common.SentStatus;
import org.carecode.messenger.common.contract.SmtpConfig;

import java.util.List;
import java.util.Properties;
import java.util.logging.Logger;

public final class EmailSender {
    private static final Logger logger = Logger.getLogger(EmailSender.class.getName());

    private static Session session;

    public static EmailStatus sendEmail(final List<String> to, final String subject, final String body, final boolean isHtml,
                                        final String replyTo) throws RuntimeException, MessagingException {
        updateSession();

        final MimeMessage message = new MimeMessage(session);
        message.setHeader("format", "flowed");
        message.setHeader("Content-Transfer-Encoding", "quoted-printable");

        message.setSubject(subject);
        message.setReplyTo(new InternetAddress[]{new InternetAddress(replyTo, false)});
        message.addRecipients(Message.RecipientType.TO, getInternetAddresses(to));

        if (isHtml) {
            message.setContent(body, "text/html");
            message.setHeader("Content-Type", "text/html; charset=utf-8");
        } else {
            message.setText(body);
            message.setHeader("Content-Type", "text/plain; charset=utf-8");
        }

        String fromEmail = session.getProperty("mail.username");
        if (fromEmail == null || fromEmail.isEmpty()) {
            throw new RuntimeException("Sender email (mail.username) is not configured.");
        }
        message.setFrom(new InternetAddress(fromEmail));

        Transport.send(message);
        logger.info("Email sent successfully.");

        return new EmailStatus(SentStatus.SENT);
    }

    public static EmailStatus sendEmail(final List<String> to, final String subject, final String body, final boolean isHtml,
                                        final String replyTo, final SmtpConfig smtpConfigurations) throws RuntimeException, MessagingException {
        updateSession(smtpConfigurations);

        final MimeMessage message = new MimeMessage(session);
        message.setHeader("format", "flowed");
        message.setHeader("Content-Transfer-Encoding", "quoted-printable");

        message.setSubject(subject);
        message.setReplyTo(new InternetAddress[]{new InternetAddress(replyTo, false)});
        message.addRecipients(Message.RecipientType.TO, getInternetAddresses(to));

        if (isHtml) {
            message.setContent(body, "text/html");
            message.setHeader("Content-Type", "text/html; charset=utf-8");
        } else {
            message.setText(body);
            message.setHeader("Content-Type", "text/plain; charset=utf-8");
        }

        String fromEmail = session.getProperty("mail.username");
        if (fromEmail == null || fromEmail.isEmpty()) {
            throw new RuntimeException("Sender email (mail.username) is not configured.");
        }
        message.setFrom(new InternetAddress(fromEmail));

        Transport.send(message);
        logger.info("Email sent successfully.");

        return new EmailStatus(SentStatus.SENT);
    }

    private static void updateSession() {
        if (session == null) {
            Properties properties = new Properties();

            System.getProperties().forEach((key, value) -> {
                if (key.toString().startsWith("mail.")) {
                    properties.put(key, value);
                }
            });

            if (properties.containsKey("mail.username") && properties.containsKey("mail.password")) {
                Authenticator auth = new Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(
                                properties.getProperty("mail.username"), properties.getProperty("mail.password"));
                    }
                };

                session = Session.getInstance(properties, auth);
            }
        }
    }

    private static void updateSession(final SmtpConfig smtpConfigurations) {
        Properties properties = new Properties();

        properties.put("mail.username", smtpConfigurations.username());
        properties.put("mail.password", smtpConfigurations.password());
        properties.put("mail.smtp.host", smtpConfigurations.smtpHost());
        properties.put("mail.smtp.port", String.valueOf(smtpConfigurations.smtpPort()));
        properties.put("mail.smtp.auth", String.valueOf(smtpConfigurations.smtpAuth()));
        properties.put("mail.smtp.starttls.enable", String.valueOf(smtpConfigurations.smtpStarttlsEnable()));
        properties.put("mail.smtp.ssl.enable", String.valueOf(smtpConfigurations.smtpSslEnable()));

        Authenticator auth = null;
        if (smtpConfigurations.smtpAuth()) {
            auth = new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(smtpConfigurations.username(), smtpConfigurations.password());
                }
            };
        }

        session = Session.getInstance(properties, auth);
    }

    private static InternetAddress[] getInternetAddresses(final List<String> recipients) throws AddressException {
        final InternetAddress[] internetAddresses = new InternetAddress[recipients.size()];

        for (int i = 0; i < recipients.size(); i++) {
            try {
                internetAddresses[i] = new InternetAddress(recipients.get(i));
            } catch (AddressException e) {
                logger.severe("Invalid recipient email address: " + recipients.get(i) + " : " + e.getMessage());
                throw e;
            }
        }

        return internetAddresses;
    }
}
