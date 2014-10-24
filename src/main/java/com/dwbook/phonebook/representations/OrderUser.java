  package com.dwbook.phonebook.representations;

  /**
   * Created by howard on 10/23/14.
   */
  public class OrderUser {
      private final int  orderId;
      private final String userId;
    
      public OrderUser() {
          this.orderId= 0;
          this.userId = null;
      }

      public OrderUser(int  orderId, String userId) {
          this.orderId= orderId;
          this.userId = userId;
      }

	public int getOrderId() {
		return orderId;
	}

	public String getUserId() {
		return userId;
	}
  }