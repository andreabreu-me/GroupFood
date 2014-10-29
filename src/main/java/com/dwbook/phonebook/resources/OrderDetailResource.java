package com.dwbook.phonebook.resources;

import io.dropwizard.auth.Auth;

import java.net.URI;
import java.net.URISyntaxException;
import java.sql.SQLException;
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

import com.dwbook.phonebook.dao.OrderDetailDAO;
import com.dwbook.phonebook.representations.OrderDetail;

/**
 * Created by howard on 10/28/14.
 */

/*@GET 	
 * 			/User/{userId}/Order/{orderId}/OrderDetail																	return OrderDetail of this user in this order	
 * 			/User/{userId}/Order/{orderId}/OrderDetail/all															    return all OrderDetail in this order
 * @POST
 *				/User/{userId}/Order/{orderId}/OrderDetail																	add an OrderDetail
 * @PUT
 *				/User/{userId}/Order/{orderId}/OrderDetail																	update an OrderDetail
 *@DELETE	
 *				/User/{userId}/Order/{orderId}/OrderDetail																	delete OrderDetail this user created in this order
 */

@Path("/User/{userId}/Order/{orderId}/OrderDetail")
@Produces(MediaType.APPLICATION_JSON + "; charset=utf-8")
public class OrderDetailResource {

    final static Logger logger = LoggerFactory.getLogger(OrderDetailResource.class);
    private final OrderDetailDAO orderDetailDao;
    private final DBI jdbi;

    public OrderDetailResource(DBI jdbi) {
    	orderDetailDao = jdbi.onDemand(OrderDetailDAO.class);
        this.jdbi = jdbi;
    }
    
    @GET
    public Response getOrderDetailByUserIdAndOrderId(@PathParam("userId") String userId, @PathParam("orderId") int orderId, @Auth Boolean isAuthenticated) {
        List<OrderDetail> allOrderDetail =  orderDetailDao.getOrderDetailByUserIdAndOrderId(userId, orderId);
        return Response.ok(allOrderDetail).build();
    }    
    
    @GET
    @Path("/all")
    public Response getOrderDetailByOrderId(@PathParam("orderId") int orderId, @Auth Boolean isAuthenticated) {
        List<OrderDetail> allOrderDetail =  orderDetailDao.getOrderDetailByOrderId(orderId);
        return Response.ok(allOrderDetail).build();
    }    
    
    @POST
    public Response createOrderDetail(OrderDetail orderDetail, @Auth Boolean isAuthenticated) throws URISyntaxException, SQLException{
  	   Handle handle = jdbi.open();
         handle.getConnection().setAutoCommit(false);
         try {
             handle.begin();
             OrderDetailDAO orderDetailDao = handle.attach(OrderDetailDAO.class);
             int id = orderDetailDao.createOrderDetail(orderDetail);
             handle.commit();
             return Response.created(new URI(String.valueOf(id))).build();
         } 
         catch (Exception e) {
             handle.rollback();
             throw e;
         }
     }       
    
    @PUT
    public Response updateOrderDetail(OrderDetail orderDetail, @Auth Boolean isAuthenticated) throws URISyntaxException, SQLException {
  	   Handle handle = jdbi.open();
        handle.getConnection().setAutoCommit(false);
        try {
            handle.begin();
            OrderDetailDAO  OrderDetailDao = handle.attach( OrderDetailDAO.class);
            OrderDetailDao.updateOrderDetail(orderDetail);
            handle.commit();
            return Response.noContent().build();
        } 
        catch (Exception e) {
            handle.rollback();
            throw e;
        }
     }
    
    @DELETE
    public Response deleteOrderDetail(List<OrderDetail> orderDetail, @Auth Boolean isAuthenticated) throws URISyntaxException, SQLException {
  	   Handle handle = jdbi.open();
        handle.getConnection().setAutoCommit(false);
        try {
            handle.begin();
            OrderDetailDAO  OrderDetailDao = handle.attach( OrderDetailDAO.class);
            OrderDetailDao.deleteBySelectedOrderDetails(orderDetail);
            handle.commit();
            return Response.noContent().build();
        } 
        catch (Exception e) {
            handle.rollback();
            throw e;
        }
     }
}
