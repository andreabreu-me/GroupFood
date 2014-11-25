package com.dwbook.phonebook.representations;

import java.util.List;

/**
 * Created by howard on 11/4/14.
 */

public class OrderWrapper {
    private final Order order;
    private final List<OrderUser> orderUser;
    private final List<OrderMerchant> orderMerchant;
    private final OrderDetail orderDetail;

    public OrderWrapper() {
        this.order= null;
        this.orderUser = null;
        this.orderMerchant = null;
        this.orderDetail = null;
    }

    public OrderWrapper(Order order, List<OrderUser> orderUser, List<OrderMerchant> orderMerchant, OrderDetail orderDetail) {
        this.order= order;
        this.orderUser = orderUser;
        this.orderMerchant = orderMerchant;
        this.orderDetail = orderDetail;
    }


    public Order getOrder() {
        return order;
    }

    public List<OrderUser> getOrderUser() {
        return orderUser;
    }

    public List<OrderMerchant> getOrderMerchant() {
        return orderMerchant;
    }

    public OrderDetail getOrderDetail() {
        return orderDetail;
    }
}