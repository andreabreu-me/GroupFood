package com.dwbook.phonebook.representations;

/**
 * Created by curtishu on 10/20/14.
 */
public class Message {

    private final long id;
    private final long groupId;
    private final String authorId;
    private final String content;

    //createdOn is needed here because it represents sent time
    private final long createdOn;

    //readOn is only needed during retrieval, will not be used during creation
    private final long readOn;

    public Message() {
        this.id = 0;
        this.groupId = 0;
        this.authorId = null;
        this.content = null;
        this.createdOn = 0;
        this.readOn = 0;
    }

    public Message(long id, long groupId, String authorId, String content, long createdOn, long readOn) {
        this.id = id;
        this.groupId = groupId;
        this.authorId = authorId;
        this.content = content;
        this.createdOn = createdOn;
        this.readOn = readOn;
    }

    public long getId() {
        return id;
    }

    public long getGroupId() {
        return groupId;
    }

    public String getAuthorId() {
        return authorId;
    }

    public String getContent() {
        return content;
    }

    public long getCreatedOn() {
        return createdOn;
    }

    public long getReadOn() {
        return readOn;
    }
}
