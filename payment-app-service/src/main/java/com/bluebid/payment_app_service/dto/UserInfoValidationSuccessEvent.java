package com.bluebid.payment_app_service.dto;

public class UserInfoValidationSuccessEvent {

    private String producerID;
    private String userID;
    private String firstName;
    private String lastName;
    private String streetName;
    private String streetNum;
    private String city;
    private String postalCode;
    private String country;

    public UserInfoValidationSuccessEvent() {}

    public UserInfoValidationSuccessEvent(String userID, String producerID, String firstName, String lastName,
                              String streetName, String streetNum, String city,
                              String postalCode, String country) {
        this.producerID = producerID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.streetName = streetName;
        this.streetNum = streetNum;
        this.city = city;
        this.postalCode = postalCode;
        this.country = country;
        this.userID = userID;
       
    }
    


    public String getProducerID() { return producerID; }
    public void setProducerID(String paymentId) { this.producerID = paymentId; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getStreetName() { return streetName; }
    public void setStreetName(String streetName) { this.streetName = streetName; }

    public String getStreetNum() { return streetNum; }
    public void setStreetNum(String streetNum) { this.streetNum = streetNum; }

    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }

    public String getPostalCode() { return postalCode; }
    public void setPostalCode(String postalCode) { this.postalCode = postalCode; }

    public String getCountry() { return country; }
    public void setCountry(String country) { this.country = country; }

	public String getUserID() {
		return userID;
	}

	public void setUserID(String userID) {
		this.userID = userID;
	}

}
