package org.carecode.messenger.email;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.carecode.messenger.common.SentStatus;
import org.carecode.messenger.common.contract.SmtpConfig;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Path("email")
public class EmailResource {
    private static final Logger logger = Logger.getLogger(EmailResource.class.getName());

    private static final IEmailService service = new EmailService();

    @POST
    @Path("send")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response sendEmail(final EmailRequest emailRequest) {
        logger.log(Level.INFO, "Received POST request to send Email: " + emailRequest);

        final EmailStatus emailStatus = (EmailStatus) service.send(
                emailRequest.recipients, emailRequest.subject, emailRequest.body,
                emailRequest.isHtml != null && emailRequest.isHtml, emailRequest.replyTo);

        if (emailStatus.getStatus() == SentStatus.SENT) {
            return Response.ok(EmailResponse.from(emailStatus)).build();
        } else {
            return Response.status(Response.Status.BAD_REQUEST).entity(EmailResponse.from(emailStatus)).build();
        }
    }

    @POST
    @Path("send/custom")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response sendEmailWithConfig(final EmailRequestWithClientConfigurations emailRequest) {
        logger.log(Level.INFO, "Received POST request to send Email (custom config): " + emailRequest);

        final EmailStatus emailStatus = (EmailStatus) service.send(
                emailRequest.recipients,
                emailRequest.subject,
                emailRequest.body,
                emailRequest.isHtml != null && emailRequest.isHtml,
                emailRequest.replyTo,
                emailRequest.smtpConfig
        );

        if (emailStatus.getStatus() == SentStatus.SENT) {
            return Response.ok(EmailResponse.from(emailStatus)).build();
        } else {
            return Response.status(Response.Status.BAD_REQUEST).entity(EmailResponse.from(emailStatus)).build();
        }
    }

    public static class EmailRequest {
        @JsonProperty("subject")
        public String subject;

        @JsonProperty("body")
        public String body;

        @JsonProperty("recipients")
        public List<String> recipients;

        @JsonProperty("replyTo")
        public String replyTo;

        @JsonProperty("isHtml")
        public Boolean isHtml;

        @Override
        public String toString() {
            return "EmailRequest{" +
                    "subject='" + subject + '\'' +
                    ", body='" + body + '\'' +
                    ", recipients=" + recipients +
                    ", replyTo='" + replyTo + '\'' +
                    ", isHtml=" + isHtml +
                    '}';
        }
    }

    public static class EmailRequestWithClientConfigurations {
        @JsonProperty("subject")
        public String subject;

        @JsonProperty("body")
        public String body;

        @JsonProperty("recipients")
        public List<String> recipients;

        @JsonProperty("replyTo")
        public String replyTo;

        @JsonProperty("isHtml")
        public Boolean isHtml;

        @JsonProperty("smtpConfig")
        public SmtpConfig smtpConfig;

        @Override
        public String toString() {
            return "EmailRequest{" +
                    "subject='" + subject + '\'' +
                    ", body='" + body + '\'' +
                    ", recipients=" + recipients +
                    ", replyTo='" + replyTo + '\'' +
                    ", isHtml=" + isHtml +
                    ", smtpConfig=" + smtpConfig +
                    '}';
        }
    }

    public static class EmailResponse {
        @JsonProperty("status")
        public SentStatus status;

        @JsonProperty("message")
        public String message;

        public EmailResponse(SentStatus status, String message) {
            this.status = status;
            this.message = message;
        }

        public static EmailResponse from(final EmailStatus emailStatus) {
            return new EmailResponse(emailStatus.getStatus(), emailStatus.getMessage());
        }

        @Override
        public String toString() {
            return "EmailResponse{" +
                    "status=" + status +
                    ", message='" + message + '\'' +
                    '}';
        }
    }
}
