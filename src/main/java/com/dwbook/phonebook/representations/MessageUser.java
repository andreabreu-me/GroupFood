package com.dwbook.phonebook.representations;

/**
 * Created by curtishu on 10/20/14.
 */
public class MessageUser {

    private final long messageId;
    private final String userId;

    //createdOn is needed here because it represents read time, only for retrieval
    private final long createdOn;

    public MessageUser() {
        messageId = 0;
        userId = null;
        createdOn = 0;
    }

    public MessageUser(long messageId, String userId) {
        this.messageId = messageId;
        this.userId = userId;
        createdOn = 0;
    }


    public String getUserId() {
        return userId;
    }

    public long getMessageId() {
        return messageId;
    }


    public long getCreatedOn() {
        return createdOn;

    }

}
