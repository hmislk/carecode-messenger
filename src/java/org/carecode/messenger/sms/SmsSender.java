package org.carecode.messenger.sms;

import lk.mobitel.esms.message.SMSManager;
import lk.mobitel.esms.session.NullSessionException;
import lk.mobitel.esms.session.SessionManager;
import lk.mobitel.esms.test.ServiceTest;
import org.carecode.messenger.common.SentStatus;
import wsdl.SmsMessage;
import wsdl.User;

import java.util.List;
import java.util.Properties;
import java.util.logging.Logger;

public final class SmsSender {
    private static final Logger logger = Logger.getLogger(SmsSender.class.getName());

    private static SessionManager session;

    public static SmsStatus sendSms(final List<String> recipients, final String body, final String senderName,
                                    final boolean isPromo) throws RuntimeException, NullSessionException {
        final User user = getUser();

        runSmsServiceTest(user); // TODO : Do something with service test result
        updateSession(user);

        final SmsMessage msg = getSmsMessage(body, senderName, recipients, isPromo);

        SMSManager smsManager = new SMSManager();

        final int result = smsManager.sendMessage(msg);
        logger.info("Sms sent successfully. Result count : " + result);

        final int deliveryReportsCount = getDeliveryReportsCount(senderName, smsManager);

        return new SmsStatus(SentStatus.SENT, result, deliveryReportsCount);
    }

    private static void updateSession(final User user) {
        if (session == null || !session.isSession()) {
            session = SessionManager.getInstance();
            session.login(user);
        }
    }

    private static SmsMessage getSmsMessage(final String body, final String senderName,
                                            final List<String> recipients, final boolean isPromo) {
        SmsMessage msg = new SmsMessage();
        msg.setMessage(body);
        msg.setSender(senderName);
        msg.getRecipients().addAll(recipients);
        msg.setMessageType(isPromo ? 1 : 0);

        return msg;
    }

    private static User getUser() {
        Properties properties = System.getProperties();

        final User user = new User();
        user.setUsername(properties.getProperty("sms.username"));
        user.setPassword(properties.getProperty("sms.password"));

        return user;
    }

    private static void runSmsServiceTest(final User user) {
        ServiceTest test = new ServiceTest();
        final String testResult = test.testService(user);

        logger.info("Sms service test result: " + testResult);
    }

    private static int getDeliveryReportsCount(final String senderName, final SMSManager smsManager) {
        int deliveryReportsCount = 0;

        try {
            List<SmsMessage> deliveryReports = smsManager.getDeliveryReports(senderName);

            if (deliveryReports != null) {
                deliveryReportsCount = deliveryReports.size();
            }
        } catch (NullSessionException ex) {
            logger.severe(ex.getMessage());
        }

        return deliveryReportsCount;
    }
}
