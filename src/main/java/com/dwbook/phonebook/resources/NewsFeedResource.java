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

import com.dwbook.phonebook.dao.MerchantDAO;
import com.dwbook.phonebook.dao.OrderDAO;
import com.dwbook.phonebook.dao.OrderMerchantDAO;
import com.dwbook.phonebook.representations.Merchant;
import com.dwbook.phonebook.representations.NewsFeed;
import com.dwbook.phonebook.representations.Order;
import com.dwbook.phonebook.representations.OrderMerchant;

/**
 * Created by howard on 11/4/14.
 */

/*@GET 	
 * 			/User/{userId}/NewsFeed								return everything for newsfeed
 */

@Path("/User/{userId}/NewsFeed")
@Produces(MediaType.APPLICATION_JSON + "; charset=utf-8")
public class NewsFeedResource {

    final static Logger logger = LoggerFactory.getLogger(NewsFeedResource.class);
    private final OrderDAO orderDao;
    private final MerchantDAO merchantDao;
    private final OrderMerchantDAO orderMerchantDao;
    private final DBI jdbi;

    public NewsFeedResource(DBI jdbi) {
    	orderDao = jdbi.onDemand(OrderDAO.class);
    	merchantDao = jdbi.onDemand(MerchantDAO.class);
    	orderMerchantDao = jdbi.onDemand(OrderMerchantDAO.class);
        this.jdbi = jdbi;
    }
    
    @GET
    public Response getNewsFeedByUserId(@PathParam("userId") String userId, @Auth Boolean isAuthenticated) {
    	List<Order> particapatingOrder = orderDao.getOrderByUserId(userId);
    	List<Order> friendsOrder = orderDao.getFriendOrder(userId);
    	List<Order> pendingOrder = orderDao.getPendingOrder(userId);
    	List<Integer> orderId = new ArrayList<Integer>();
    	for(Order o : particapatingOrder){    		orderId.add(o.getId());    	}
    	for(Order o : friendsOrder){    		orderId.add(o.getId());    	}
    	for(Order o : pendingOrder){    		orderId.add(o.getId());    	}
    	List<OrderMerchant> orderMerchant = new ArrayList<OrderMerchant>();
    	for(Integer i : orderId){
    		orderMerchant.addAll(orderMerchantDao.getMerchantByOrderId(i));
    	}
    	List<Merchant> merchant = new ArrayList<Merchant>();
    	for(OrderMerchant om:orderMerchant){
    		merchant.add(merchantDao.getMerchantById(om.getMerchantId()));
    	}
        //TODO we need to use Set instead of List to remove potential duplicates
        //Adding all merchants now as recommended merchants for 1st use case, currently unavailable in API
        for(Merchant m : merchantDao.getAllActiveMerchant()) {
            merchant.add(m);
        }
    	NewsFeed nf = new NewsFeed(particapatingOrder, friendsOrder, pendingOrder, orderMerchant, merchant);
    	return Response.ok(nf).build();
    }    
    
}