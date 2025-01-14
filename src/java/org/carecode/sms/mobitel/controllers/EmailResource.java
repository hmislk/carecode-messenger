package org.carecode.sms.mobitel.controllers;

import jakarta.mail.*;
import jakarta.mail.internet.AddressException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

@Path("email")
public class EmailResource {
    private static final Logger logger = Logger.getLogger(EmailResource.class.getName());

    private static Session session;

    @POST
    @Path("send")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response sendEmail(final EmailRequest emailRequest) {
        logger.log(Level.INFO, "Received POST request to send Email: " + emailRequest);

        try {
            updateSession();

            MimeMessage message = new MimeMessage(session);
            message.setHeader("format", "flowed");
            message.setHeader("Content-Transfer-Encoding", "quoted-printable");

            if (emailRequest.getSubject() != null) {
                message.setSubject(emailRequest.getSubject());
            } else {
                logger.severe("Subject is required.");
                throw new RuntimeException("Subject is required.");
            }

            if (emailRequest.isHtml() != null && emailRequest.isHtml()) {
                message.setContent(emailRequest.getBody(), "text/html");
                message.setHeader("Content-Type", "text/html; charset=utf-8");
            } else {
                message.setText(emailRequest.getBody());
                message.setHeader("Content-Type", "text/plain; charset=utf-8");
            }

            if (emailRequest.getReplyTo() != null) {
                message.setReplyTo(new InternetAddress[]{new InternetAddress(emailRequest.getReplyTo())});
            } else {
                message.setReplyTo(InternetAddress.parse("no_reply@example.com", false));
            }

            if (emailRequest.getRecipients() != null && !emailRequest.getRecipients().isEmpty()) {
                message.addRecipients(Message.RecipientType.TO, getInternetAddresses(emailRequest.getRecipients()));
            } else {
                logger.severe("At least one recipient email address is required.");
                throw new RuntimeException("At least one recipient email address is required.");
            }

            Transport.send(message);
            logger.info("Email sent successfully to recipients.");

            return Response
                    .ok(new EmailResponse(SentStatus.SENT, "Email sent successfully to recipients."))
                    .build();
        } catch (Exception e) {
            final String message = "Failed to send email: " + e.getMessage();

            logger.severe(message);
            return Response.ok(new EmailResponse(SentStatus.FAILED, message)).build();
        }
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

    private static InternetAddress[] getInternetAddresses(final List<String> recipients) {
        return recipients.stream()
                .filter(recipientAddress -> recipientAddress != null && !recipientAddress.isEmpty())
                .map(recipientAddress -> {
                    try {
                        return new InternetAddress(recipientAddress);
                    } catch (AddressException e) {
                        logger.severe(
                                "Invalid recipient email address: " + recipientAddress + " : " + e.getMessage());
                        throw new RuntimeException(e);
                    }
                })
                .toArray(InternetAddress[]::new);
    }
}
