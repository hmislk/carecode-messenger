package org.carecode.messenger.email;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.carecode.messenger.common.SentStatus;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Path("email")
public class EmailResource {
    private static final Logger logger = Logger.getLogger(EmailResource.class.getName());

    private static final IEmailService emailService = new EmailService();

    @POST
    @Path("send")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response sendEmail(final EmailRequest emailRequest) {
        logger.log(Level.INFO, "Received POST request to send Email: " + emailRequest);

        final EmailStatus messageStatus =
                (EmailStatus) emailService.send(
                        emailRequest.recipients, emailRequest.subject, emailRequest.body,
                        emailRequest.isHtml != null && emailRequest.isHtml, emailRequest.replyTo);

        if (messageStatus.getStatus() == SentStatus.SENT) {
            return Response.ok(EmailResponse.from(messageStatus)).build();
        } else {
            return Response.status(Response.Status.BAD_REQUEST).entity(EmailResponse.from(messageStatus)).build();
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
