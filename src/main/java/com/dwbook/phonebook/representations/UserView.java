package com.dwbook.phonebook.representations;

//select userId, `OrderDetail`.merchantId, itemId, quantity, unitPrice, quantity * unitPrice as 'total' from OrderDetail left join Item on `OrderDetail`.itemId = `Item`.Id where `OrderDetail`.orderId=1 order by userId;

public class UserView {
	private final String userId;
	private final long merchantId;
	private final long itemId;
	private final int quantity;
	private final float unitPrice;
	private final float total;
	
    public UserView() {
        this.userId=null;
        this.merchantId=0;
        this.itemId=0;
        this.quantity=0;
        this.unitPrice=0;
        this.total=0;
    }

    public UserView(String userId, long merchantId, long itemId, int quantity, float unitPrice, float total) {
    	this.userId=userId;
        this.merchantId=merchantId;
        this.itemId=itemId;
        this.quantity=quantity;
        this.unitPrice=unitPrice;
        this.total=total;
    }

	public long getMerchantId() {
		return merchantId;
	}

	public String getUserId() {
		return userId;
	}

	public int getQuantity() {
		return quantity;
	}

	public long getItemId() {
		return itemId;
	}

	public float getTotal() {
		return total;
	}

	public float getUnitPrice() {
		return unitPrice;
	}
}
