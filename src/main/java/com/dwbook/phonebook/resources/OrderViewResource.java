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
import com.dwbook.phonebook.representations.MerchantView;
import com.dwbook.phonebook.representations.Order;
import com.dwbook.phonebook.representations.OrderDetail;
import com.dwbook.phonebook.representations.OrderMerchant;
import com.dwbook.phonebook.representations.OrderView;
import com.dwbook.phonebook.representations.UserView;

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

    public OrderViewResource(DBI jdbi) {
    	orderDao = jdbi.onDemand(OrderDAO.class);
    }
    
    @GET
    public Response getOrderViewByUserIdOrderId(@PathParam("orderId") int orderId, @PathParam("userId") String userId, @Auth Boolean isAuthenticated) {
    	List<UserView> userView= orderDao.getUserViewByOrderId(orderId);
        List<MerchantView> merchantView= orderDao.getMerchantViewByOrderId(orderId);
        float userViewTotal=0;
        float merchantViewTotal=0;
        for(UserView uv:userView){
        	userViewTotal+=uv.getTotal();
        }
        for(MerchantView mv:merchantView){
        	merchantViewTotal+=mv.getTotal();
        }
    	
    	OrderView ov = new OrderView(userView, merchantView, userViewTotal, merchantViewTotal);
    	return Response.ok(ov).build();
    }    
    
}