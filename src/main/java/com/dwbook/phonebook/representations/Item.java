package com.dwbook.phonebook.representations;

/**
 * Created by howard on 10/20/14.
 */
public class Item {
    private final long id;
    private final long merchantId;
    private final String title;
    private final String description;
    private final float unitPrice;
    private final int dailyLimit;
    private final int weight;
    private final String imageJson;
    private final String feedbackJson;

    public Item() {
        this.id= 0;
        this.merchantId = 0;
        this.title = null;
        this.description = null;
        this.unitPrice = 0;
        this.dailyLimit = 0;
        this.weight = 0;
        this.imageJson = null;
        this.feedbackJson = null;
    }

    public Item(long id, long merchantId, String title, String description, float unitPrice, int dailyLimit, int weight, String imageJson, String feedbackJson) {
        this.id= id;
        this.merchantId = merchantId;
        this.title = title;
        this.description = description;
        this.unitPrice = unitPrice;
        this.dailyLimit = dailyLimit;
        this.weight = weight;
        this.imageJson = imageJson;
        this.feedbackJson = feedbackJson;
    }

	public long getId() {
		return id;
	}

	public long getMerchantId() {
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

	public int getDailyLimit() {
		return dailyLimit;
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
    