package com.dwbook.phonebook.representations;

/**
 * Created by howard on 10/12/14.
 */
public class User {
    private final String id;
    private final String facebookId;
    private final String googlePlusId;
    private final String createdOn;
    private final String updatedOn;
    private final String deletedOn;

    public User() {
        this.id= null;
        this.facebookId = null;
        this.googlePlusId = null;
        this.createdOn = null;
        this.updatedOn = null;
        this.deletedOn = null;
    }

    public User(String id, String facebookId, String googlePlusId, String createdOn, String updatedOn, String deletedOn) {
        this.id= id;
        this.facebookId = facebookId;
        this.googlePlusId = googlePlusId;
        this.createdOn = createdOn;
        this.updatedOn = updatedOn;
        this.deletedOn = deletedOn;
    }
    
    public String getId() {
        return id;
    }
    public String getFacebookId() {
        return facebookId;
    }
    public String getGooglePlusId() {
        return googlePlusId;
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
}
