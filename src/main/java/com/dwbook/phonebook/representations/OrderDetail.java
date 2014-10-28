  package com.dwbook.phonebook.representations;

  /**
   * Created by howard on 10/27/14.
   */
  public class OrderDetail {
      private final String  userId;
      private final int orderId;
      private final int merchantId;
      private final int itemId;
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

      public OrderDetail( String  userId, int orderId, int merchantId, int itemId,  int quantity, String  status) {
    	  this.userId= userId;
          this.orderId= orderId;
          this.merchantId = merchantId;
          this.itemId = itemId;
          this.quantity = quantity;
          this.status= status;
      }

	public int getOrderId() {
		return orderId;
	}

	public String getUserId() {
		return userId;
	}

	public int getMerchantId() {
		return merchantId;
	}

	public int getItemId() {
		return itemId;
	}

	public int getQuantity() {
		return quantity;
	}

	public String getStatus() {
		return status;
	}

  }