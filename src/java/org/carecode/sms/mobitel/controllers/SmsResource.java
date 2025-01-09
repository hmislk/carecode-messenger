package org.carecode.sms.mobitel.controllers;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import lk.mobitel.esms.message.SMSManager;
import lk.mobitel.esms.session.NullSessionException;
import lk.mobitel.esms.session.SessionManager;
import lk.mobitel.esms.test.ServiceTest;
import wsdl.SmsMessage;
import wsdl.User;

@Path("sms")
public class SmsResource {

    @POST
    @Path("send")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response sendFullLogic(SmsRequest smsRequest) {
        System.out.println("Received POST request to send SMS");

        // Extract parameters from the request object
        String userName = smsRequest.getUsername();
        String password = smsRequest.getPassword();
        String userAlias = smsRequest.getUserAlias();
        String number = smsRequest.getNumber();
        String message = smsRequest.getMessage();
        String promo = smsRequest.getPromo();

        // Log received parameters for debugging
        System.out.printf("username=%s, password=%s, userAlias=%s, number=%s, message=%s, promo=%s%n",
                userName, password, userAlias, number, message, promo);

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
        System.out.println("logged = " + logged);

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
            System.out.println("result = " + result);
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
        String jsonResponse = String.format(
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
