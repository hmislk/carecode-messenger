package org.carecode.sms.mobitel.controllers;

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
import wsdl.SmsMessage;
import wsdl.User;

import java.util.List;
import java.util.logging.Logger;

import static java.lang.String.format;

@Path("sms")
public class SmsResource {
    private static final Logger logger = Logger.getLogger(SmsResource.class.getName());

    private static SessionManager session;

    @POST
    @Path("send")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response sendFullLogic(final SmsRequest smsRequest) {
        logger.info("Received POST request to send Sms: " + smsRequest);

        final User user = new User();
        user.setUsername(smsRequest.getUsername());
        user.setPassword(smsRequest.getPassword());

        final String serviceTestResult = runSmsServiceTest(user); // TODO : Do something with service test result

        updateSession(user);

        final SmsMessage msg = getSmsMessage(smsRequest);

        // Send the message
        int result = -1;
        SMSManager smsManager = new SMSManager();
        try {
            result = smsManager.sendMessage(msg);
            logger.info(format("Sent message result = %s", result));
        } catch (NullSessionException ex) {
            logger.severe(ex.getMessage());
        }

        final int deliveryReportsCount = getDeliveryReportsCount(smsRequest, smsManager);

        return Response
                .ok(new SmsResponse(result, serviceTestResult, deliveryReportsCount))
                .build();
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
