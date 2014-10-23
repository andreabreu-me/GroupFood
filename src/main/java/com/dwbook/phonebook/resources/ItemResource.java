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

import com.dwbook.phonebook.dao.ItemDAO;
import com.dwbook.phonebook.representations.Item;

/**
 * Created by howard on 10/21/14.
 */

/*@GET 	
 * 			/Merchant/{merchantId}/Item								return all items created by merchantid
 * 			/Merchant/{merchantId}/Item/{id}						return Item id = id if deleteOn is not null
 * 			/Merchant/{merchantId}/Item/Descending		return Item in descending weight order
 *@POST
 *				/Merchant/{merchantId}/Item								create entries in Item
 *@PUT
 *				/Merchant/{merchantId}/Item/{id}						update item's data
 *@DELETE	
 *				/Merchant/{merchantId}/Item/							delete all item's data by merchantid
 *				/Merchant/{merchantId}/Item/{id}						delete item's data
 */

@Path("/Merchant/{merchantId}/Item")
@Produces(MediaType.APPLICATION_JSON + "; charset=utf-8")
public class ItemResource {

    final static Logger logger = LoggerFactory.getLogger(ItemResource.class);
    private final ItemDAO itemDao;
    private final DBI jdbi;

    public ItemResource(DBI jdbi) {
    	itemDao = jdbi.onDemand(ItemDAO.class);
        this.jdbi = jdbi;
    }
    
    @GET
    public Response getItemByMerchantId(@PathParam("merchantId") int merchantId, @Auth Boolean isAuthenticated) {
        List<Item> allItem =  itemDao.getItemByMerchantId(merchantId);
        return Response.ok(allItem).build();
    }    
    
    @GET
    @Path("/{id}")
    public Response getItemByItemId(@PathParam("id") int id, @Auth Boolean isAuthenticated) {
        Item Item = itemDao.getItemById(id);
        return Response
                .ok(Item)
                .build();
    }
    
    @GET
    @Path("/Descending")
    public Response getItemInDescendingWeightOrder(@PathParam("merchantId") int merchantId, @Auth Boolean isAuthenticated) {
    	List<Item> allItem =  itemDao.getItemByDescendingOrder(merchantId);
        return Response
                .ok(allItem)
                .build();
    }

    @POST
    public Response createItem(List<Item> item, @Auth Boolean isAuthenticated) throws URISyntaxException, SQLException{
    	   Handle handle = jdbi.open();
           handle.getConnection().setAutoCommit(false);
           try {
               handle.begin();
               ItemDAO ItemDao = handle.attach(ItemDAO.class);
               int[] newItemId = ItemDao.batchCreateItem(item);    
               handle.commit();
               return Response.created(new URI(String.valueOf(newItemId.length))).build();
           } 
           catch (Exception e) {
               handle.rollback();
               throw e;
           }
    }        
    @PUT
    @Path("/{id}")
    public Response createItem(Item item, @PathParam("id") int id, @PathParam("merchantId") int merchantId, @Auth Boolean isAuthenticated) throws URISyntaxException, SQLException{
    	   Handle handle = jdbi.open();
           handle.getConnection().setAutoCommit(false);
           try {
               handle.begin();
               ItemDAO ItemDao = handle.attach(ItemDAO.class);
               ItemDao.updateItem(id, merchantId, item.getTitle(),item.getDescription(),item.getUnitPrice(),item.getDailyLimit(),item.getWeight(),item.getImageJson(),item.getFeedbackJson());
               
               handle.commit();
               return Response.ok(
                       new Item(id, merchantId,item.getTitle(),item.getDescription(),item.getUnitPrice(),item.getDailyLimit(),item.getWeight(),item.getImageJson(),item.getFeedbackJson())).build();
           } 
           catch (Exception e) {
               handle.rollback();
               throw e;
           }
     }
    @DELETE
    public Response deleteAllItem(@PathParam("merchantId") int merchantId, @Auth Boolean isAuthenticated) throws URISyntaxException, SQLException {
 	   Handle handle = jdbi.open();
       handle.getConnection().setAutoCommit(false);
       try {
           handle.begin();
           itemDao.deleteItemByMerchantId(merchantId);
           handle.commit();
           return Response.noContent().build();
       } 
       catch (Exception e) {
           handle.rollback();
           throw e;
       }
    }
    @DELETE
    @Path("/{id}")
    public Response deleteItem(@PathParam("id") int id, @Auth Boolean isAuthenticated) throws URISyntaxException, SQLException {
 	   Handle handle = jdbi.open();
       handle.getConnection().setAutoCommit(false);
       try {
           handle.begin();
           itemDao.deleteItem(id);
           handle.commit();
           return Response.noContent().build();
       } 
       catch (Exception e) {
           handle.rollback();
           throw e;
       }
    }
}