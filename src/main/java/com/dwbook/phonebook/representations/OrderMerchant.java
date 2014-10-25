  package com.dwbook.phonebook.representations;

  /**
   * Created by howard on 10/24/14.
   */
  public class OrderMerchant {
      private final int  orderId;
      private final int merchantId;
    
      public OrderMerchant() {
          this.orderId= 0;
          this.merchantId = 0;
      }

      public OrderMerchant(int  orderId, int merchantId) {
          this.orderId= orderId;
          this.merchantId = merchantId;
      }

	public int getOrderId() {
		return orderId;
	}

	public int getMerchantId() {
		return merchantId;
	}
  }