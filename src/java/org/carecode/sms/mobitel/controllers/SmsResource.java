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
import java.util.logging.Level;
import java.util.logging.Logger;

import static java.lang.String.format;

@Path("sms")
public class SmsResource {
    private static final Logger logger = Logger.getLogger(SmsResource.class.getName());

    @POST
    @Path("send")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response sendFullLogic(SmsRequest smsRequest) {
        logger.log(Level.INFO, "Received POST request to send SMS");

        // Extract parameters from the request object
        String userName = smsRequest.getUsername();
        String password = smsRequest.getPassword();
        String userAlias = smsRequest.getUserAlias();
        String number = smsRequest.getNumber();
        String message = smsRequest.getMessage();
        String promo = smsRequest.getPromo();

        // Log received parameters for debugging
        logger.log(
                Level.INFO,
                format("username=%s, password=%s, userAlias=%s, number=%s, message=%s, promo=%s",
                        userName, password, userAlias, number, message, promo));

        User user = new User();
        user.setUsername(userName);
        user.setPassword(password);

        // Test the service
        ServiceTest test = new ServiceTest();
        String serviceTestResult = test.testService(user);

        // Session handling
        SessionManager sm = SessionManager.getInstance();
        sm.login(user);
        boolean logged = sm.isSession();
        logger.log(Level.INFO, format("User logged = %s", logged));

        // Create the SmsMessage
        SmsMessage msg = new SmsMessage();
        msg.setMessage(message);
        msg.setSender(userAlias);
        msg.getRecipients().add(number);
        msg.setMessageType("YES".equalsIgnoreCase(promo) ? 1 : 0);

        // Send the message
        int result = -1;
        SMSManager smsManager = new SMSManager();
        try {
            result = smsManager.sendMessage(msg);
            logger.log(Level.INFO, format("Result = %s", result));
        } catch (NullSessionException ex) {
            Logger.getLogger(SmsResource.class.getName()).log(Level.SEVERE, null, ex);
        }

        // Get delivery reports
        int reportsCount = 0;
        try {
            List<SmsMessage> deliveryReports = smsManager.getDeliveryReports(userAlias);
            if (deliveryReports != null) {
                reportsCount = deliveryReports.size();
            }
        } catch (NullSessionException ex) {
            Logger.getLogger(SmsResource.class.getName()).log(Level.SEVERE, null, ex);
        }

        // Logout
        sm.logout();
        boolean logStatus2 = sm.isSession();

        // Build JSON response
        String jsonResponse = format(
                "{\"serviceTestResult\":\"%s\",\"logStatus\":\"%s\",\"messageSendResult\":%d,"
                        + "\"deliveryReportsCount\":%d,\"logStatus2\":\"%s\"}",
                serviceTestResult,
                logged,
                result,
                reportsCount,
                logStatus2
        );

        return Response.ok(jsonResponse).build();
    }
}
