package com.dwbook.phonebook.representations;

/**
 * Created by howard on 10/20/14.
 */
public class Merchant {
    private final int id;
    private final String name;
    private final String branch;
    private final String description;
    private final String address;
    private final float latitude;
    private final float longitude;
    private final int deliverDistanceKm;
    private final float minimumOrder;
    private final float minimumDelivery;
    private final String mainPhone;
    private final String mobilePhone;
    private final String orderSubmissionJson;
    private final String imageJson;
    private final String feedbackJson;

    public Merchant() {
        this.id= 0;
        this.name = null;
        this.branch = null;
        this.description = null;
        this.address = null;
        this.latitude= 0;
        this.longitude= 0;
        this.deliverDistanceKm= 0;
        this.minimumOrder= 0;
        this.minimumDelivery= 0;
        this.mainPhone = null;
        this.mobilePhone = null;
        this.orderSubmissionJson = null;
        this.imageJson = null;
        this.feedbackJson = null;
    }

    public Merchant(int id, String name, String branch, String description, String address,  float latitude,  float longitude,  int deliverDistanceKm,  float minimumOrder,  float minimumDelivery,  String mainPhone,  String mobilePhone,  String orderSubmissionJson,  String imageJson,  String feedbackJson) {
        this.id= id;
        this.name = name;
        this.branch = branch;
        this.description = description;
        this.address = address;
        this.latitude= latitude;
        this.longitude= longitude;
        this.deliverDistanceKm= deliverDistanceKm;
        this.minimumOrder= minimumOrder;
        this.minimumDelivery= minimumDelivery;
        this.mainPhone = mainPhone;
        this.mobilePhone = mobilePhone;
        this.orderSubmissionJson = orderSubmissionJson;
        this.imageJson = imageJson;
        this.feedbackJson = feedbackJson;
    }

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getBranch() {
		return branch;
	}

	public String getDescription() {
		return description;
	}

	public String getAddress() {
		return address;
	}

	public float getLatitude() {
		return latitude;
	}

	public int getDeliverDistanceKm() {
		return deliverDistanceKm;
	}

	public float getLongitude() {
		return longitude;
	}

	public float getMinimumOrder() {
		return minimumOrder;
	}

	public float getMinimumDelivery() {
		return minimumDelivery;
	}

	public String getMainPhone() {
		return mainPhone;
	}

	public String getMobilePhone() {
		return mobilePhone;
	}

	public String getOrderSubmissionJson() {
		return orderSubmissionJson;
	}

	public String getImageJson() {
		return imageJson;
	}

	public String getFeedbackJson() {
		return feedbackJson;
	}
}
    