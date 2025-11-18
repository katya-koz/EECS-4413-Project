package com.bluebid.user_app_service.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "users")
public class User {

    @Id
    private String id; // db id (primary key)
    private String username;
    private String password; // should be hashed
    private String firstName;
    private String lastName;
    private String email;
    private LocalDateTime dateRegistered;
	private String streetName;
	private String streetNum;
	private String city;
	private String postalCode;
	private String country;

	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
	public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public LocalDateTime getDateRegistered() {
        return dateRegistered;
    }

    public void setDateRegistered(LocalDateTime dateRegistered) {
        this.dateRegistered = dateRegistered;
    }



	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}


}