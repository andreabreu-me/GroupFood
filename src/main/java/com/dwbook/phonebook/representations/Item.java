package com.dwbook.phonebook.representations;

/**
 * Created by howard on 10/20/14.
 */
public class Item {
    private final int id;
    private final int merchantId;
    private final String title;
    private final String description;
    private final float unitPrice;
    private final int limit;
    private final int weight;
    private final String imageJson;
    private final String feedbackJson;

    public Item() {
        this.id= 0;
        this.merchantId = 0;
        this.title = null;
        this.description = null;
        this.unitPrice = 0;
        this.limit = 0;
        this.weight = 0;
        this.imageJson = null;
        this.feedbackJson = null;
    }

    public Item(int id, int merchantId, String title, String description, float unitPrice, int limit, int weight, String imageJson, String feedbackJson) {
        this.id= id;
        this.merchantId = merchantId;
        this.title = title;
        this.description = description;
        this.unitPrice = unitPrice;
        this.limit = limit;
        this.weight = weight;
        this.imageJson = imageJson;
        this.feedbackJson = feedbackJson;
    }

	public int getId() {
		return id;
	}

	public int getMerchantId() {
		return merchantId;
	}

	public String getTitle() {
		return title;
	}

	public String getDescription() {
		return description;
	}

	public float getUnitPrice() {
		return unitPrice;
	}

	public int getLimit() {
		return limit;
	}

	public int getWeight() {
		return weight;
	}

	public String getImageJson() {
		return imageJson;
	}

	public String getFeedbackJson() {
		return feedbackJson;
	}
}
    