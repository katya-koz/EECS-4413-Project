package com.bluebid.catalogue_app_service.dto;

public class ItemReserveFailedEvent {
	// to keep from hanging, need to publish failed event
	String message;
	String paymentID;
	public ItemReserveFailedEvent(String message, String paymentID) {
		super();
		this.message = message;
		this.paymentID = paymentID;
	}
}
