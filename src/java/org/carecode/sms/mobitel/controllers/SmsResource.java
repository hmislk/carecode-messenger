package org.carecode.sms.mobitel.controllers;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import lk.mobitel.esms.message.SMSManager;
import lk.mobitel.esms.session.NullSessionException;
import lk.mobitel.esms.session.SessionManager;
import lk.mobitel.esms.test.ServiceTest;
import wsdl.SmsMessage;
import wsdl.User;

@Path("sms")
public class SmsResource {
    @GET
    @Path("send")
    @Produces(MediaType.APPLICATION_JSON)
    public Response sendFullLogic(
            @QueryParam("userName") String userName,
            @QueryParam("password") String password,
            @QueryParam("userAlias") String userAlias,
            @QueryParam("number") String number,
            @QueryParam("message") String message,
            @QueryParam("promo") String promo
    ) {
        // Create the User object
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

        // Create the SmsMessage
        SmsMessage msg = new SmsMessage();
        msg.setMessage(message);
        msg.setSender(userAlias);
        msg.getRecipients().add(number);
        if ("YES".equalsIgnoreCase(promo)) {
            msg.setMessageType(1);
        } else {
            msg.setMessageType(0);
        }

        // Send the message
        int result = -1;
        SMSManager smsManager = new SMSManager();
        try {
            result = smsManager.sendMessage(msg);
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
