package com.bluebid.payment_app_service.dto;

public class ItemReserveFailedEvent {

    private String message;
    private String paymentID;

    public ItemReserveFailedEvent() {}

    public ItemReserveFailedEvent(String message, String paymentID) {
        this.message = message;
        this.paymentID = paymentID;
    }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public String getPaymentID() { return paymentID; }
    public void setPaymentID(String paymentID) { this.paymentID = paymentID; }
}
