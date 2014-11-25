  package com.dwbook.phonebook.representations;

  /**
   * Created by howard on 10/27/14.
   */
  public class OrderDetail {
      private final String  userId;
      private final long orderId;
      private final long merchantId;
      private final long itemId;
      private final int quantity;
      private final String  status;

      public OrderDetail() {
          this.userId= null;
          this.orderId= 0;
          this.merchantId = 0;
          this.itemId = 0;
          this.quantity = 0;
          this.status= null;
      }

      public OrderDetail( String  userId, long orderId, long merchantId, long itemId,  int quantity, String  status) {
    	  this.userId= userId;
          this.orderId= orderId;
          this.merchantId = merchantId;
          this.itemId = itemId;
          this.quantity = quantity;
          this.status= status;
      }

	public long getOrderId() {
		return orderId;
	}

	public String getUserId() {
		return userId;
	}

	public long getMerchantId() {
		return merchantId;
	}

	public long getItemId() {
		return itemId;
	}

	public int getQuantity() {
		return quantity;
	}

	public String getStatus() {
		return status;
	}

  }