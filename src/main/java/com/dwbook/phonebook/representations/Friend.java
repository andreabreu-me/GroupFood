package com.dwbook.phonebook.representations;

/**
 * Created by howard on 10/17/14.
 */
public class Friend {
    private final String userId;
    private final String friendId;
    private final String socialNetwork;
    private final String relationship;

    public Friend() {
    	this.userId = null;
        this.friendId = null;
        this.socialNetwork = null;
        this.relationship = null;
    }

    public Friend(String userId, String friendId, String socialNetwok, String relationship) {
    	this.userId = userId;
        this.friendId = friendId;
        this.socialNetwork = socialNetwok;
        this.relationship = relationship;
    }

	public String getFriendId() {
		return friendId;
	}

	public String getSocialNetwork() {
		return socialNetwork;
	}

	public String getRelationship() {
		return relationship;
	}

	public String getUserId() {
		return userId;
	}

}
