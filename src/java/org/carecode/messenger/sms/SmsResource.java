package org.carecode.messenger.sms;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.carecode.messenger.common.SentStatus;

import java.util.List;
import java.util.Properties;
import java.util.logging.Logger;

@Path("sms")
public class SmsResource {
    private static final Logger logger = Logger.getLogger(SmsResource.class.getName());

    private static final ISmsService service = new SmsService();

    @POST
    @Path("send")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response sendSms(final SmsRequest smsRequest) {
        logger.info("Received POST request to send Sms: " + smsRequest);

        try {
            loadPropertiesFromRequest(smsRequest);
        } catch (Exception e) {
            final String message = "Failed to send sms: " + e.getMessage();

            logger.severe(message);
            return Response.status(Response.Status.BAD_REQUEST).entity(new SmsResponse(SentStatus.FAILED, message)).build();
        }

        final SmsStatus smsStatus = (SmsStatus) service.send(
                smsRequest.recipientNumbers, smsRequest.message, smsRequest.senderName,
                smsRequest.isPromo != null && smsRequest.isPromo);

        if (smsStatus.getStatus() == SentStatus.SENT) {
            return Response.ok(SmsResponse.from(smsStatus)).build();
        } else {
            return Response.status(Response.Status.BAD_REQUEST).entity(SmsResponse.from(smsStatus)).build();
        }
    }

    private void loadPropertiesFromRequest(final SmsRequest smsRequest) throws IllegalArgumentException {
        if (smsRequest.username == null || smsRequest.username.isEmpty()) {
            throw new IllegalArgumentException("Username is required.");
        }

        if (smsRequest.password == null || smsRequest.password.isEmpty()) {
            throw new IllegalArgumentException("Password is required.");
        }

        Properties props = System.getProperties();
        props.put("sms.username", smsRequest.username);
        props.put("sms.password", smsRequest.password);
    }

    /**
     * @author Dr M H B Ariyaratne <buddhika.ari@gmail.com>
     */
    public static class SmsRequest {
        @JsonProperty("username")
        public String username;

        @JsonProperty("password")
        public String password;

        @JsonProperty("senderName")
        public String senderName;

        @JsonProperty("recipientNumbers")
        public List<String> recipientNumbers;

        @JsonProperty("message")
        public String message;

        @JsonProperty("isPromo")
        public Boolean isPromo;

        @Override
        public String toString() {
            return "SmsRequest{" +
                    "senderName='" + senderName + '\'' +
                    ", recipientNumbers='" + recipientNumbers + '\'' +
                    ", message='" + message + '\'' +
                    ", isPromo='" + isPromo + '\'' +
                    '}';
        }
    }

    public static class SmsResponse {
        @JsonProperty("status")
        public SentStatus status;

        @JsonProperty("message")
        public String message;

        @JsonProperty("result")
        public int result;

        @JsonProperty("deliveryReportsCount")
        public int deliveryReportsCount;

        public SmsResponse(SentStatus status, String message) {
            this.status = status;
            this.message = message;
        }

        public SmsResponse(SentStatus status, String message, int result, int deliveryReportsCount) {
            this.status = status;
            this.message = message;
            this.result = result;
            this.deliveryReportsCount = deliveryReportsCount;
        }

        public static SmsResponse from(final SmsStatus smsStatus) {
            return new SmsResponse(
                    smsStatus.getStatus(), smsStatus.getMessage(),
                    smsStatus.getResult(), smsStatus.getDeliveryReportsCount());
        }

        @Override
        public String toString() {
            return "SmsResponse{" +
                    "status=" + status +
                    ", message='" + message + '\'' +
                    ", result=" + result +
                    ", deliveryReportsCount=" + deliveryReportsCount +
                    '}';
        }
    }
}
