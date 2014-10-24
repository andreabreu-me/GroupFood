package com.dwbook.phonebook.representations;

/**
 * Created by howard on 10/23/14.
 */
public class Order {
    private final int  id;
    private final String organizerId;
    private final String name;
    private final String description;
    private final String deliveryAddress;
    private final float  deliveryLatitude;
    private final float  deliveryLongitude;
    private final String status;
  
    public Order() {
        this.id= 0;
        this.organizerId = null;
        name = null;
        description = null;
        this.deliveryAddress = null;
        this.deliveryLatitude= 0;
        this.deliveryLongitude= 0;
        this.status = null;
    }

    public Order(int  id, String organizerId, String name, String description, String deliveryAddress,  float  deliveryLatitude,  float  deliveryLongitude,  String status) {
        this.id= id;
        this.organizerId = organizerId;
        this.name = name;
        this.description = description;
        this.deliveryAddress = deliveryAddress;
        this.deliveryLatitude = deliveryLatitude;
        this.deliveryLongitude = deliveryLongitude;
        this.status = status;
    }

	public Order(int id2, String userId, Order order) {
		this.id= id2;
        this.organizerId = userId;
        this.name = order.getName();
        this.description = order.getDescription();
        this.deliveryAddress = order.getDeliveryAddress();
        this.deliveryLatitude = order.getDeliveryLatitude();
        this.deliveryLongitude = order.getDeliveryLongitude();
        this.status = order.getStatus();
	}

	public String getOrganizerId() {
		return organizerId;
	}

	public int getId() {
		return id;
	}

	public String getDeliveryAddress() {
		return deliveryAddress;
	}

	public float getDeliveryLatitude() {
		return deliveryLatitude;
	}

	public float getDeliveryLongitude() {
		return deliveryLongitude;
	}

	public String getStatus() {
		return status;
	}

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}

}