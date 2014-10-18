package com.dwbook.phonebook.representations;

/**
 * Created by howard on 10/12/14.
 */
public class Facebook {
    private final String id;
    private final String userId;
    private final String token;
    private final String firstName;
    private final String lastName;
    private final String email;

    public Facebook() {
        this.id= null;
        this.userId = null;
        this.token = null;
        this.firstName = null;
        this.lastName = null;
        this.email = null;
    }

    public Facebook(String id, String userId, String token, String firstName, String lastName, String email) {
        this.id= id;
        this.userId = userId;
        this.token = token;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }
    
    public String getId() {
        return id;
    }

	public String getUserId() {
		return userId;
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
}
