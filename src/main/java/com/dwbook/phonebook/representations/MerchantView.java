package com.dwbook.phonebook.representations;

public class MerchantView {
	private final long merchantId;
	private final long itemId;
	private final int totalQuantity;
	private final float unitPrice;
	private final float total;
	
    public MerchantView() {
        this.merchantId=0;
        this.itemId=0;
        this.totalQuantity=0;
        this.unitPrice=0;
        this.total=0;
    }

    public MerchantView(long merchantId, long itemId, int totalQuantity, float unitPrice, float total) {
        this.merchantId=merchantId;
        this.itemId=itemId;
        this.totalQuantity=totalQuantity;
        this.unitPrice=unitPrice;
        this.total=total;
    }

	public long getMerchantId() {
		return merchantId;
	}

	public long getItemId() {
		return itemId;
	}

	public float getUnitPrice() {
		return unitPrice;
	}

	public int getTotalQuantity() {
		return totalQuantity;
	}

	public float getTotal() {
		return total;
	}
    
}
