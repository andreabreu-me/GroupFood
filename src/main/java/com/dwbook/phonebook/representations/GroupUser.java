package com.dwbook.phonebook.representations;

/**
 * Created by curtishu on 10/11/14.
 */
public class GroupUser {

    private final long groupId;
    private final String userId;

    public GroupUser() {
        this.groupId = 0;
        this.userId = null;
    }

    public GroupUser(long groupId, String userId) {
        this.groupId = groupId;
        this.userId = userId;
    }

    public long getGroupId() {
        return groupId;
    }

    public String getUserId() {
        return userId;
    }
}
