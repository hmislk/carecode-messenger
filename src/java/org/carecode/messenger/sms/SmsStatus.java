package org.carecode.messenger.sms;

import org.carecode.messenger.common.SentStatus;
import org.carecode.messenger.common.contract.AbstractMessageStatus;

public class SmsStatus extends AbstractMessageStatus {
    private int result;
    private int deliveryReportsCount;

    public SmsStatus(SentStatus status) {
        super(status);
        this.result = 0;
        this.deliveryReportsCount = 0;
    }

    public SmsStatus(SentStatus status, String message) {
        super(status, message);
        this.result = 0;
        this.deliveryReportsCount = 0;
    }

    public SmsStatus(SentStatus status, int result, int deliveryReportsCount) {
        super(status);
        this.result = result;
        this.deliveryReportsCount = deliveryReportsCount;
    }

    public SmsStatus(SentStatus status, String message, int result, int deliveryReportsCount) {
        super(status, message);
        this.result = result;
        this.deliveryReportsCount = deliveryReportsCount;
    }

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public int getDeliveryReportsCount() {
        return deliveryReportsCount;
    }

    public void setDeliveryReportsCount(int deliveryReportsCount) {
        this.deliveryReportsCount = deliveryReportsCount;
    }

    @Override
    public String toString() {
        return "SmsStatus{" +
                "result=" + result +
                ", deliveryReportsCount=" + deliveryReportsCount +
                "} " + super.toString();
    }
}
