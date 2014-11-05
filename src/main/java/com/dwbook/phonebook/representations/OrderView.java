package com.dwbook.phonebook.representations;

import java.util.List;

/**
 * Created by howard on 11/4/14.
 */

public class OrderView {

	private final Order currentOrder;
	private final List<OrderDetail> orderDetail;
	private final List<OrderMerchant> orderMerchant;
	private final List<Merchant> merchant;
	private final List<Item> item;
	
    public OrderView() {
    	this.currentOrder=null;
    	this.orderDetail=null;
    	this.orderMerchant=null;
    	this.merchant=null;
    	this.item=null;
    }
    public OrderView(Order currentOrder, List<OrderDetail> orderDetail, List<OrderMerchant> orderMerchant, List<Merchant> merchant, List<Item> item) {
    	this.currentOrder=currentOrder;
    	this.orderDetail=orderDetail;
    	this.orderMerchant=orderMerchant;
    	this.merchant=merchant;
    	this.item=item;
    }
	public Order getCurrentOrder() {
		return currentOrder;
	}
	public List<OrderDetail> getOrderDetail() {
		return orderDetail;
	}
	public List<OrderMerchant> getOrderMerchant() {
		return orderMerchant;
	}
	public List<Merchant> getMerchant() {
		return merchant;
	}
	public List<Item> getItem() {
		return item;
	}
}