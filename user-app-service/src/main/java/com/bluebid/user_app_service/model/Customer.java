package com.bluebid.user_app_service.model;

public class Customer extends User{
	
	private String streetName;
	private String streetNum;
	private String city;
	private String postalCode;
	private String country;
	
	  public String getStreetName() {
			return streetName;
		}

		public void setStreetName(String streetName) {
			this.streetName = streetName;
		}

		public String getStreetNum() {
			return streetNum;
		}

		public void setStreetNum(String streetNum) {
			this.streetNum = streetNum;
		}

		public String getCity() {
			return city;
		}

		public void setCity(String city) {
			this.city = city;
		}

		public String getPostalCode() {
			return postalCode;
		}

		public void setPostalCode(String postalCode) {
			this.postalCode = postalCode;
		}

		public String getCountry() {
			return country;
		}

		public void setCountry(String country) {
			this.country = country;
		}

}
