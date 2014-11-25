  package com.dwbook.phonebook.representations;

  /**
   * Created by howard on 10/23/14.
   */
  public class OrderUser {
      private final long  orderId;
      private final String userId;
      private final String status;
    
      public OrderUser() {
          this.orderId= 0;
          this.userId = null;
          this.status = null;
      }

      public OrderUser(long  orderId, String userId, String status) {
          this.orderId= orderId;
          this.userId = userId;
          this.status = status;
      }

	public long getOrderId() {
		return orderId;
	}

	public String getUserId() {
		return userId;
	}

	public String getStatus() {
		return status;
	}
  }