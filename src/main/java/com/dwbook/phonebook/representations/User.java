package com.dwbook.phonebook.representations;

/**
 * Created by howard on 10/12/14.
 */
public class User {
    private final String id;
    private final String facebookId;
    private final String googlePlusId;

    public User() {
        this.id= null;
        this.facebookId = null;
        this.googlePlusId = null;
    }

    public User(String id, String facebookId, String googlePlusId) {
        this.id= id;
        this.facebookId = facebookId;
        this.googlePlusId = googlePlusId;
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
}