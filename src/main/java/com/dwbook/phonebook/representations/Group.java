package com.dwbook.phonebook.representations;

/**
 * Created by curtishu on 10/11/14.
 */
public class Group {

    private final long id;
    private final String organizerId;
    private final String name;
    private final String description;
    private final long orderId;
    private final String status;

    public Group() {
        this.id = 0;
        this.organizerId = null;
        this.name = null;
        this.description = null;
        this.orderId = 0;
        this.status = null;
    }

    public Group(long id, String organizerId, String name, String description, long orderId, String status) {
        this.id = id;
        this.organizerId = organizerId;
        this.name = name;
        this.description = description;
        this.orderId = orderId;
        this.status = status;
    }

    public long getId() {
        return id;
    }

    public String getOrganizerId() {
        return organizerId;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public long getOrderId() {
        return orderId;
    }

    public String getStatus() {
        return status;
    }
}
