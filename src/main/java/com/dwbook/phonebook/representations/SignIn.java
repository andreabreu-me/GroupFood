package com.dwbook.phonebook.representations;

import java.util.List;

/**
 * Created by howard on 10/18/14.
 */
public class SignIn {
    private final String userId;
    private final String facebookId;
    private final String googlePlusId;
    private final String token;
    private final String firstName;
    private final String lastName;
    private final String email;
    private final List<Friend> friend;
    private final String imageJson;

    public SignIn() {
        this.userId= null;
        this.facebookId = null;
        this.googlePlusId = null;
        this.token = null;
        this.firstName = null;
        this.lastName = null;
        this.email = null;
        this.friend = null;
        this.imageJson=null;
    }

    public SignIn(String userId, String facebookId, String googlePlusId, String token, String firstName, String lastName, String email, List<Friend> friend, String imageJson) {
        this.userId= userId;
        this.facebookId = facebookId;
        this.googlePlusId = googlePlusId;
        this.token = token;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.friend = friend;
        this.imageJson=imageJson;
    }
    
    public String getUserId() {
        return userId;
    }
    public String getFacebookId() {
        return facebookId;
    }
    public String getGooglePlusId() {
        return googlePlusId;
    }

	public String getToken() {
		return token;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public String getEmail() {
		return email;
	}

	public List<Friend> getFriend() {
		return friend;
	}

	public String getImageJson() {
		return imageJson;
	}
}