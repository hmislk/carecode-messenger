package org.carecode.messenger;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SmsResponse {
    @JsonProperty("status")
    private SentStatus status;

    @JsonProperty("message")
    private String message;

    @JsonProperty("result")
    private int result;

    @JsonProperty("serviceTestResult")
    private String serviceTestResult;

    @JsonProperty("deliveryReportsCount")
    private int deliveryReportsCount;

    public SmsResponse(SentStatus status, String message) {
        this.status = status;
        this.message = message;
    }

    public SmsResponse(int result, String serviceTestResult, int deliveryReportsCount) {
        this.result = result;
        this.serviceTestResult = serviceTestResult;
        this.deliveryReportsCount = deliveryReportsCount;
    }

    public SmsResponse(SentStatus status, String message, int result, String serviceTestResult, int deliveryReportsCount) {
        this.status = status;
        this.message = message;
        this.result = result;
        this.serviceTestResult = serviceTestResult;
        this.deliveryReportsCount = deliveryReportsCount;
    }

    public SentStatus getStatus() {
        return status;
    }

    public void setStatus(SentStatus status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public String getServiceTestResult() {
        return serviceTestResult;
    }

    public void setServiceTestResult(String serviceTestResult) {
        this.serviceTestResult = serviceTestResult;
    }

    public int getDeliveryReportsCount() {
        return deliveryReportsCount;
    }

    public void setDeliveryReportsCount(int deliveryReportsCount) {
        this.deliveryReportsCount = deliveryReportsCount;
    }

    @Override
    public String toString() {
        return "SmsResponse{" +
                "status=" + status +
                ", message='" + message + '\'' +
                ", result=" + result +
                ", serviceTestResult='" + serviceTestResult + '\'' +
                ", deliveryReportsCount=" + deliveryReportsCount +
                '}';
    }
}
