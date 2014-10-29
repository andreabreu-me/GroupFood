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

import com.dwbook.phonebook.dao.ItemDAO;
import com.dwbook.phonebook.dao.OrderDetailDAO;
import com.dwbook.phonebook.dao.OrderMerchantDAO;
import com.dwbook.phonebook.representations.Item;
import com.dwbook.phonebook.representations.OrderMerchant;

/**
 * Created by howard on 10/24/14.
 */

/*@GET 	
 * 			/User/{userId}/Order/{orderId}/OrderMerchant																								return all Merchant that order has		
 * 			/User/{userId}/Order/{orderId}/OrderMerchant/{merchantId}/Item															    return all Item created by that Merchant 
 * @POST
 *				/User/{userId}/Order/{orderId}/OrderMerchant																								add Merchant to that Order
 *@DELETE	
 *				/User/{userId}/Order/{orderId}/OrderMerchant/{merchantId}																		delete that merchant
 */

@Path("/User/{userId}/Order/{orderId}/OrderMerchant")
@Produces(MediaType.APPLICATION_JSON + "; charset=utf-8")
public class OrderMerchantResource {

    final static Logger logger = LoggerFactory.getLogger(OrderMerchantResource.class);
    private final OrderMerchantDAO orderMerchantDao;
    private final ItemDAO itemDao;
    private final DBI jdbi;

    public OrderMerchantResource(DBI jdbi) {
    	orderMerchantDao = jdbi.onDemand(OrderMerchantDAO.class);
    	itemDao = jdbi.onDemand(ItemDAO.class);
        this.jdbi = jdbi;
    }
    @GET
    public Response getAllMerchantsByOrderId(@PathParam("orderId") int orderId, @Auth Boolean isAuthenticated) {
        List<OrderMerchant> allOrderMerchant =  orderMerchantDao.getMerchantByOrderId(orderId);
        return Response.ok(allOrderMerchant).build();
    }    
    
    @GET
    @Path("{merchantId}/Item")
    public Response getAllItemsByMerchantId(@PathParam("merchantId") int merchantId, @Auth Boolean isAuthenticated) {
        List<Item> allItem =  itemDao.getItemByMerchantId(merchantId);
        return Response.ok(allItem).build();
    }    
    
    @POST
    public Response createOrderUser(@PathParam("orderId") int orderId, List<OrderMerchant> orderMerchant, @Auth Boolean isAuthenticated) throws URISyntaxException, SQLException{
 	   Handle handle = jdbi.open();
        handle.getConnection().setAutoCommit(false);
        try {
            handle.begin();
            OrderMerchantDAO orderMerchantDao = handle.attach(OrderMerchantDAO.class);
            int[] ids = orderMerchantDao.batchCreateOrderMerchant(orderMerchant);
            handle.commit();
            return Response.created(new URI(String.valueOf(ids.length))).build();
        } 
        catch (Exception e) {
            handle.rollback();
            throw e;
        }
 }        
    @DELETE
    @Path("/{merchantId}")
    public Response deleteOrderUser(@PathParam("merchantId") int merchantId, @PathParam("orderId") int orderId, @Auth Boolean isAuthenticated) throws URISyntaxException, SQLException {
 	   Handle handle = jdbi.open();
       handle.getConnection().setAutoCommit(false);
       try {
           handle.begin();           
           OrderMerchantDAO orderMerchantDao = handle.attach(OrderMerchantDAO.class);
           OrderDetailDAO  OrderDetailDao = handle.attach( OrderDetailDAO.class);
           orderMerchantDao.deleteMerchant(orderId, merchantId);
           OrderDetailDao.deleteByMerchantIdAndOrderId(merchantId, orderId);
           handle.commit();
           return Response.noContent().build();
       } 
       catch (Exception e) {
           handle.rollback();
           throw e;
       }
    }
}
