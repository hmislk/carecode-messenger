package org.carecode.messenger.sms;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lk.mobitel.esms.message.SMSManager;
import lk.mobitel.esms.session.NullSessionException;
import lk.mobitel.esms.session.SessionManager;
import lk.mobitel.esms.test.ServiceTest;
import org.carecode.messenger.common.SentStatus;
import wsdl.SmsMessage;
import wsdl.User;

import java.util.List;
import java.util.logging.Logger;

@Path("sms")
public class SmsResource {
    private static final Logger logger = Logger.getLogger(SmsResource.class.getName());

    private static SessionManager session;

    @POST
    @Path("send")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response sendSms(final SmsRequest smsRequest) {
        logger.info("Received POST request to send Sms: " + smsRequest);

        try {
            final User user = new User();
            user.setUsername(smsRequest.getUsername());
            user.setPassword(smsRequest.getPassword());

            final String serviceTestResult = runSmsServiceTest(user); // TODO : Do something with service test result

            updateSession(user);

            final SmsMessage msg = getSmsMessage(smsRequest);

            SMSManager smsManager = new SMSManager();

            int result = smsManager.sendMessage(msg);
            logger.info("Sms sent successfully.");

            return Response
                    .ok(new SmsResponse(
                            SentStatus.SENT,
                            "Sms sent successfully.",
                            result, serviceTestResult, getDeliveryReportsCount(smsRequest, smsManager)))
                    .build();
        } catch (Exception e) {
            final String message = "Failed to send sms: " + e.getMessage();

            logger.severe(message);
            return Response.ok(new SmsResponse(SentStatus.FAILED, message)).build();
        }
    }

    private static int getDeliveryReportsCount(final SmsRequest smsRequest, final SMSManager smsManager) {
        int deliveryReportsCount = 0;

        try {
            List<SmsMessage> deliveryReports = smsManager.getDeliveryReports(smsRequest.getSenderName());

            if (deliveryReports != null) {
                deliveryReportsCount = deliveryReports.size();
            }
        } catch (NullSessionException ex) {
            logger.severe(ex.getMessage());
        }

        return deliveryReportsCount;
    }

    private static SmsMessage getSmsMessage(final SmsRequest smsRequest) {
        SmsMessage msg = new SmsMessage();
        msg.setMessage(smsRequest.getMessage());
        msg.setSender(smsRequest.getSenderName());
        msg.getRecipients().add(smsRequest.getRecipientNumber());
        msg.setMessageType(smsRequest.isPromo() ? 1 : 0);

        return msg;
    }

    private static String runSmsServiceTest(final User user) {
        ServiceTest test = new ServiceTest();
        return test.testService(user);
    }

    private static void updateSession(final User user) {
        if (session == null || !session.isSession()) {
            session = SessionManager.getInstance();
            session.login(user);
        }
    }
}
