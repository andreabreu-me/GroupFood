package com.dwbook.phonebook.representations;

import java.util.List;

/**
 * Created by howard on 10/18/14.
 */
public class FacebookToken {
    private final String token;
    private final String firstName;
    private final String lastName;
    private final String email;
    private final List<Friend> friend;
    private final String imageJson;

    public FacebookToken() {
        this.token = null;
        this.firstName = null;
        this.lastName = null;
        this.email = null;
        this.friend = null;
        this.imageJson=null;
    }

    public FacebookToken(String token, String firstName, String lastName, String email, List<Friend> friend, String imageJson) {
        this.token = token;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.friend = friend;
        this.imageJson=imageJson;
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

	public List<Friend> getFriend() {
		return friend;
	}

	public String getImageJson() {
		return imageJson;
	}
}
