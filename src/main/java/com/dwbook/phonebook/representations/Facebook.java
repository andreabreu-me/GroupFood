package com.dwbook.phonebook.representations;

/**
 * Created by howard on 10/12/14.
 */
public class Facebook {
    private final String userId;
    private final String id;
    private final String token;
    private final String firstName;
    private final String lastName;
    private final String email;

    public Facebook() {
        this.userId= null;
        this.id= null;
        this.token = null;
        this.firstName = null;
        this.lastName = null;
        this.email = null;
    }

    public Facebook(String userId, String id, String token, String firstName, String lastName, String email) {
    	this.userId=userId;
        this.id= id;
        this.token = token;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }
    
    public String getId() {
        return id;
    }

	public String getToken() {
		return token;
	}

	public String getLastName() {
		return lastName;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getEmail() {
		return email;
	}

	public String getUserId() {
		return userId;
	}
}
