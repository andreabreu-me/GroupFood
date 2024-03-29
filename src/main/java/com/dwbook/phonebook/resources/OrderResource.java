package com.dwbook.phonebook.resources;

import com.dwbook.phonebook.representations.*;
import io.dropwizard.auth.Auth;

import java.net.URI;
import java.net.URISyntaxException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.skife.jdbi.v2.DBI;
import org.skife.jdbi.v2.Handle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dwbook.phonebook.dao.OrderDAO;
import com.dwbook.phonebook.dao.OrderDetailDAO;
import com.dwbook.phonebook.dao.OrderMerchantDAO;
import com.dwbook.phonebook.dao.OrderUserDAO;

/**
 * Created by howard on 10/23/14.
 */

/*@GET 	
 * 			/User/{userId}/Order																													return all orders that User participates
 * 			/User/{userId}/Order/{orderId}																									return the order detail by orderId
 *@POST
 *				/User/{userId}/Order																													create entries in Order and OrderUser
 *@PUT
 *				/User/{userId}/Order/{orderId}																									update order's data
 *@DELETE	
 *				/User/{userId}/Order/{orderId}																									delete an order
 */

@Path("/User/{userId}/Order")
@Produces(MediaType.APPLICATION_JSON + "; charset=utf-8")
public class OrderResource {

    final static Logger logger = LoggerFactory.getLogger(OrderResource.class);
    private final OrderDAO orderDao;
    private final DBI jdbi;

    public OrderResource(DBI jdbi) {
    	orderDao = jdbi.onDemand(OrderDAO.class);
        this.jdbi = jdbi;
    }
    
    @GET
    public Response getOrderByUserId(@PathParam("userId") String userId, @Auth Boolean isAuthenticated) {
        List<Order> allOrder =  orderDao.getOrderByUserId(userId);
        return Response.ok(allOrder).build();
    }    
    
    @GET
    @Path("/{id}")
    public Response getOrderByOrderId(@PathParam("userId") String userId,@PathParam("id") int id, @Auth Boolean isAuthenticated) {
        Order Order = orderDao.getOrderByOrderId(id);
        return Response
                .ok(Order)
                .build();
    }

    @POST
    public Response createOrder(@PathParam("userId") String userId, OrderWrapper orderWrapper, @Auth Boolean isAuthenticated) throws URISyntaxException, SQLException{
    	   Handle handle = jdbi.open();
           handle.getConnection().setAutoCommit(false);
           try {
               handle.begin();

               OrderDAO orderDao = handle.attach(OrderDAO.class);
               OrderUserDAO orderUserDao = handle.attach(OrderUserDAO.class);
               OrderMerchantDAO orderMerchantDao = handle.attach(OrderMerchantDAO.class);
               OrderDetailDAO orderDetailDao = handle.attach(OrderDetailDAO.class);
               long newOrderId = orderDao.createOrder(userId, orderWrapper.getOrder());
               Order order = new Order(newOrderId, userId,orderWrapper.getOrder());

               List<OrderUser> orderUser = new ArrayList<OrderUser>();
               for(OrderUser ou:orderWrapper.getOrderUser()){
                   orderUser.add(new OrderUser(newOrderId, ou.getUserId(), "added"));
               }
               orderUserDao.batchCreateOrderUser(orderUser);

               List<OrderMerchant> orderMerchant = new ArrayList<OrderMerchant>();
               for(OrderMerchant om:orderWrapper.getOrderMerchant()){
                   orderMerchant.add(new OrderMerchant(newOrderId, om.getMerchantId()));
               }
               orderMerchantDao.batchCreateOrderMerchant(orderMerchant);


               List<OrderDetail> orderDetail = new ArrayList<OrderDetail>();
               for(OrderDetail od:orderWrapper.getOrderDetail()){
                   orderDetail.add(new OrderDetail(userId,newOrderId,od.getMerchantId(),od.getItemId(),od.getQuantity(),"chosen"));
               }
               orderDetailDao.batchCreateOrderDetail(orderDetail);

               handle.commit();

               return Response.created(new URI(String.valueOf(newOrderId))).entity(order).build();
           } 
           catch (Exception e) {
               handle.rollback();
               throw e;
           }
    }        
    
    @PUT
    @Path("/{id}")
    public Response updateOrder(Order order, @PathParam("id") int id, @PathParam("userId") String userId, @Auth Boolean isAuthenticated) throws URISyntaxException, SQLException{
    	   Handle handle = jdbi.open();
           handle.getConnection().setAutoCommit(false);
           try {
               handle.begin();
               OrderDAO OrderDao = handle.attach(OrderDAO.class);
               OrderDao.updateOrder(id, userId, order);
               handle.commit();
               return Response.ok(
                       new Order(id, userId, order)).build();
           } 
           catch (Exception e) {
               handle.rollback();
               throw e;
           }
     }

    @DELETE
    @Path("/{id}")
    public Response deleteOrder(@PathParam("id") int id, @PathParam("userId") String userId, @Auth Boolean isAuthenticated) throws URISyntaxException, SQLException {
 	   Handle handle = jdbi.open();
       handle.getConnection().setAutoCommit(false);
       try {
           handle.begin();
           OrderDAO OrderDao = handle.attach(OrderDAO.class);
           OrderUserDAO OrderUserDao = handle.attach(OrderUserDAO.class);
           OrderMerchantDAO OrderMerchantDao = handle.attach(OrderMerchantDAO.class);
           OrderDetailDAO  OrderDetailDao = handle.attach( OrderDetailDAO.class);
           OrderDao.deleteOrderByOrderId(id, userId);
           OrderUserDao.deleteByOrderId(id);
           OrderMerchantDao.deleteByOrderId(id);
           OrderDetailDao.deleteByOrderId(id);
           handle.commit();
           return Response.noContent().build();
       } 
       catch (Exception e) {
           handle.rollback();
           throw e;
       }
    }
}