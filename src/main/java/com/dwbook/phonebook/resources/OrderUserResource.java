package com.dwbook.phonebook.resources;

import io.dropwizard.auth.Auth;

import java.net.URISyntaxException;
import java.sql.SQLException;
import java.util.List;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
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
 * 			/User/{userId}/OrderUser																										return all OrderUser that user participates
 * 			/User/{userId}/OrderUser/{orderUserId}																				return all participants in that orderUserId 			
 * 			to do:
 * 			/User/{userId}/OrderUser/{orderUserId}/participant/{participantId}									return all item that participant chose in that orderUserId
 *@DELETE	
 *				/User/{userId}/OrderUser/{orderUserId}																		leave an orderUser group
 */

@Path("/User/{userId}/OrderUser")
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
    public Response getOrderUserByUserId(@PathParam("userId") String userId, @Auth Boolean isAuthenticated) {
        List<OrderUser> allOrderUser =  orderUserDao.getOrderByUserId(userId);
        return Response.ok(allOrderUser).build();
    }    
    
    @GET
    @Path("/{orderUserId}")
    public Response getOrderUserByOrderUserId(@PathParam("orderUserId") int orderUserId, @Auth Boolean isAuthenticated) {
        OrderUser OrderUser = orderUserDao.getUserByOrderId(orderUserId);
        return Response
                .ok(OrderUser)
                .build();
    }

    @DELETE
    @Path("/{orderUserId}")
    public Response deleteOrderUser(@PathParam("orderUserId") int orderUserId, @PathParam("userId") String userId, @Auth Boolean isAuthenticated) throws URISyntaxException, SQLException {
 	   Handle handle = jdbi.open();
       handle.getConnection().setAutoCommit(false);
       try {
           handle.begin();
           orderUserDao.leaveOrder(orderUserId, userId);
           handle.commit();
           return Response.noContent().build();
       } 
       catch (Exception e) {
           handle.rollback();
           throw e;
       }
    }
}