package com.dwbook.phonebook.representations;

import java.util.List;

/**
 * Created by howard on 11/4/14.
 */

public class NewsFeed {
	private final List<Order> participatingOrder;
	private final List<Order> friendsOrder;
	private final List<Order> pendingOrder;
	private final List<OrderMerchant> orderMerchant;
	private final List<Merchant> merchant;
	private final List<Item> item;
	
    public NewsFeed() {
        this.participatingOrder= null;
        this.friendsOrder = null;
        this.merchant = null;
        this.pendingOrder = null;
        this.orderMerchant = null;
        this.item = null;
    }

    public NewsFeed(List<Order> participating, List<Order> friends, List<Order> pending, List<OrderMerchant> orderMerchant, List<Merchant> merchant, List<Item> item) {
        this.participatingOrder= participating;
        this.friendsOrder = friends;
        this.merchant = merchant;
        this.pendingOrder = pending;
        this.orderMerchant =orderMerchant;
        this.item = item;
    }

	public List<Order> getParticipatingOrder() {
		return participatingOrder;
	}

	public List<Order> getFriendsOrder() {
		return friendsOrder;
	}

	public List<Merchant> getMerchant() {
		return merchant;
	}

	public List<Order> getPendingOrder() {
		return pendingOrder;
	}

	public List<OrderMerchant> getOrderMerchant() {
		return orderMerchant;
	}

	public List<Item> getItem() {
		return item;
	}

}