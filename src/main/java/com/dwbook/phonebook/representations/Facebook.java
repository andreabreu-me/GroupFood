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
    private final String createdOn;
    private final String updatedOn;
    private final String deletedOn;

    public Facebook() {
        this.id= null;
        this.userId = null;
        this.token = null;
        this.firstName = null;
        this.lastName = null;
        this.email = null;
        this.createdOn = null;
        this.updatedOn = null;
        this.deletedOn = null;
    }

    public Facebook(String id, String userId, String token, String firstName, String lastName, String email, String createdOn, String updatedOn, String deletedOn) {
        this.id= id;
        this.userId = userId;
        this.token = token;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.createdOn = createdOn;
        this.updatedOn = updatedOn;
        this. deletedOn = deletedOn;
    }
    
    public String getId() {
        return id;
    }

    public String getCreatedOn(){
    	return createdOn;
    }
    public String getUpdatedOn(){
    	return updatedOn;
    }
    public String getDeletedOn(){
    	return deletedOn;
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
