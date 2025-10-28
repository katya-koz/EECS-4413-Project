package com.bluebid.user_app_service.dto;

public class UserInfoReadyEvent {

    private String paymentId;
    private String firstName;
    private String lastName;
    private String streetName;
    private String streetNum;
    private String city;
    private String postalCode;
    private String country;

    public UserInfoReadyEvent() {}

    public UserInfoReadyEvent(String paymentId, String firstName, String lastName,
                              String streetName, String streetNum, String city,
                              String postalCode, String country) {
        this.paymentId = paymentId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.streetName = streetName;
        this.streetNum = streetNum;
        this.city = city;
        this.postalCode = postalCode;
        this.country = country;
    }

    public String getPaymentId() { return paymentId; }
    public void setPaymentId(String paymentId) { this.paymentId = paymentId; }

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
}
