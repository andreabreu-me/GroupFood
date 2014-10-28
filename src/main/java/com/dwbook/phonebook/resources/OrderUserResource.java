package com.dwbook.phonebook.resources;

import io.dropwizard.auth.Auth;

import java.net.URI;
import java.net.URISyntaxException;
import java.sql.SQLException;
import java.util.List;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.skife.jdbi.v2.DBI;
import org.skife.jdbi.v2.Handle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dwbook.phonebook.dao.OrderUserDAO;
import com.dwbook.phonebook.representations.OrderUser;

/**
 * Created by howard on 10/23/14.
 */

/*@GET 	
 * 			/User/{userId}/Order/{orderId}/OrderUser																										return all participants that order has			
 * 			/User/{userId}/Order/{orderId}/OrderUser/{participantId}																				return all order that participant participates
 * @POST
 *				/User/{userId}/Order/{orderId}/OrderUser																										add User to that Order
 *@DELETE	
 *				/User/{userId}/Order/{orderId}/OrderUser/																										leave that Order
 */

@Path("/User/{userId}/Order/{orderId}/OrderUser")
@Produces(MediaType.APPLICATION_JSON + "; charset=utf-8")
public class OrderUserResource {

    final static Logger logger = LoggerFactory.getLogger(OrderUserResource.class);
    private final OrderUserDAO orderUserDao;
    private final DBI jdbi;

    public OrderUserResource(DBI jdbi) {
    	orderUserDao = jdbi.onDemand(OrderUserDAO.class);
        this.jdbi = jdbi;
    }
    
    @GET
    public Response getAllParticipantsByOrderId(@PathParam("orderId") int orderId, @Auth Boolean isAuthenticated) {
        List<OrderUser> allOrderUser =  orderUserDao.getUserByOrderId(orderId);
        return Response.ok(allOrderUser).build();
    }    
    
    @GET
    @Path("/{participantId}")
    public Response getAllOrderThatParticipantParticipates(@PathParam("participantId") String participantId, @Auth Boolean isAuthenticated) {
    	List<OrderUser> OrderUser = orderUserDao.getOrderByUserId(participantId);
        return Response
                .ok(OrderUser)
                .build();
    }
    @POST
    public Response createOrderUser(@PathParam("orderId") int orderId, List<OrderUser> orderUser, @Auth Boolean isAuthenticated) throws URISyntaxException, SQLException{
 	   Handle handle = jdbi.open();
        handle.getConnection().setAutoCommit(false);
        try {
            handle.begin();
            OrderUserDAO orderUserDao = handle.attach(OrderUserDAO.class);
            int[] ids = orderUserDao.batchCreateOrderUser(orderUser);
            handle.commit();
            return Response.created(new URI(String.valueOf(ids.length))).build();
        } 
        catch (Exception e) {
            handle.rollback();
            throw e;
        }
 }        
    @DELETE
    public Response deleteOrderUser(@PathParam("orderId") int orderId, @PathParam("userId") String userId, @Auth Boolean isAuthenticated) throws URISyntaxException, SQLException {
 	   Handle handle = jdbi.open();
       handle.getConnection().setAutoCommit(false);
       try {
           handle.begin();
           orderUserDao.leaveOrder(orderId, userId);
           handle.commit();
           return Response.noContent().build();
       } 
       catch (Exception e) {
           handle.rollback();
           throw e;
       }
    }
}