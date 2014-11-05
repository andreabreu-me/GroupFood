package com.dwbook.phonebook.resources;

import io.dropwizard.auth.Auth;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.skife.jdbi.v2.DBI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dwbook.phonebook.dao.ItemDAO;
import com.dwbook.phonebook.dao.MerchantDAO;
import com.dwbook.phonebook.dao.OrderDAO;
import com.dwbook.phonebook.dao.OrderDetailDAO;
import com.dwbook.phonebook.dao.OrderMerchantDAO;
import com.dwbook.phonebook.representations.Item;
import com.dwbook.phonebook.representations.Merchant;
import com.dwbook.phonebook.representations.Order;
import com.dwbook.phonebook.representations.OrderDetail;
import com.dwbook.phonebook.representations.OrderMerchant;
import com.dwbook.phonebook.representations.OrderView;

/**
 * Created by howard on 11/4/14.
 */

/*@GET 	
 * 			/User/{userId}/Order/{orderId}/OrderView								return everything for orderView
 */

@Path("/User/{userId}/Order/{orderId}/OrderView")
@Produces(MediaType.APPLICATION_JSON + "; charset=utf-8")
public class OrderViewResource {

    final static Logger logger = LoggerFactory.getLogger(NewsFeedResource.class);
    private final OrderDAO orderDao;
    private final MerchantDAO merchantDao;
    private final OrderMerchantDAO orderMerchantDao;
    private final OrderDetailDAO orderDetailDao;
    private final ItemDAO itemDao;
    private final DBI jdbi;

    public OrderViewResource(DBI jdbi) {
    	orderDao = jdbi.onDemand(OrderDAO.class);
    	merchantDao = jdbi.onDemand(MerchantDAO.class);
    	orderMerchantDao = jdbi.onDemand(OrderMerchantDAO.class);
    	orderDetailDao = jdbi.onDemand(OrderDetailDAO.class);
    	itemDao = jdbi.onDemand(ItemDAO.class);
        this.jdbi = jdbi;
    }
    
    @GET
    public Response getOrderViewByUserIdOrderId(@PathParam("orderId") int orderId, @PathParam("userId") String userId, @Auth Boolean isAuthenticated) {
    	Order currentOrder = orderDao.getOrderByOrderId(orderId);
    	List<OrderDetail> orderDetail = orderDetailDao.getOrderDetailByOrderId(orderId);
    	List<OrderMerchant> orderMerchant = orderMerchantDao.getMerchantByOrderId(orderId);
    	List<Merchant> merchant = new ArrayList<Merchant>();
    	for(OrderMerchant om:orderMerchant){
    		merchant.add(merchantDao.getMerchantById(om.getMerchantId()));
    	}
    	List<Item> item = new ArrayList<Item>();
    	for(Merchant m:merchant){
    		item.addAll(itemDao.getItemByMerchantId(m.getId()));
    	}
    	OrderView ov = new OrderView(currentOrder, orderDetail, orderMerchant, merchant, item);
    	return Response.ok(ov).build();
    }    
    
}