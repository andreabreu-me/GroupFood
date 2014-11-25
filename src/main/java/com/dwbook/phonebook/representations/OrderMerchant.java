  package com.dwbook.phonebook.representations;

  /**
   * Created by howard on 10/24/14.
   */
  public class OrderMerchant {
      private final long orderId;
      private final long merchantId;
    
      public OrderMerchant() {
          this.orderId= 0;
          this.merchantId = 0;
      }

      public OrderMerchant(long  orderId, long merchantId) {
          this.orderId= orderId;
          this.merchantId = merchantId;
      }

	public long getOrderId() {
		return orderId;
	}

	public long getMerchantId() {
		return merchantId;
	}
  }