package com.dwbook.phonebook.resources;
import io.dropwizard.auth.Auth;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.skife.jdbi.v2.DBI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dwbook.phonebook.dao.FacebookDAO;
import com.dwbook.phonebook.dao.FriendDAO;
import com.dwbook.phonebook.dao.ItemDAO;
import com.dwbook.phonebook.dao.MerchantDAO;
import com.dwbook.phonebook.dao.UserDAO;
import com.dwbook.phonebook.representations.Facebook;
import com.dwbook.phonebook.representations.Friend;
import com.dwbook.phonebook.representations.Item;
import com.dwbook.phonebook.representations.Merchant;
import com.dwbook.phonebook.representations.User;

/**
 * Created by howard on 10/18/14.
 */

/*@GET 	
 * 			Admin/User						return active user (deleteON is null)
 *				Admin/User/all					return User table
 *				Admin/Facebook				return active Facebook (deleteON is null)
 *				Admin/Facebook/all			return Facebook table
 *				Admin/Friend						return active Friend
 *				Admin/Friend/all				return Friend table
 *				Admin/Merchant				return active merchant
 *				Admin/Merchant/all			return merchant table
 *				Admin/Item						return active item
 *				Admin/Item/all					return item table
 *@Delete
 *				Admin/User/{id}				soft delete user id = {id}, soft delete Facebook and Friend associates to the user  (not in use)
 */

@Path("/Admin")
@Produces(MediaType.APPLICATION_JSON + "; charset=utf-8")
public class AdminResource {

    final static Logger logger = LoggerFactory.getLogger(AdminResource.class);
    private final FacebookDAO facebookDao;
    private final UserDAO userDao;
    private final FriendDAO friendDao;
    private final MerchantDAO merchantDao;
    private final ItemDAO itemDao;
    private final DBI jdbi;

    public AdminResource(DBI jdbi) {
        facebookDao = jdbi.onDemand(FacebookDAO.class);
        userDao = jdbi.onDemand(UserDAO.class);
        friendDao = jdbi.onDemand(FriendDAO.class);
        merchantDao = jdbi.onDemand(MerchantDAO.class);
        itemDao = jdbi.onDemand(ItemDAO.class);
        this.jdbi = jdbi;
    }
    
    @Path("/User")
    @GET   
    public Response getAllActiveUser(@Auth Boolean isAuthenticated) {
        List<User> allUser =  userDao.getAllActiveUser();
        return Response.ok(allUser).build();
    }
    @Path("/User/all")
    @GET   
    public Response getAllUser(@Auth Boolean isAuthenticated) {
        List<User> allUser =  userDao.getAllUser();
        return Response.ok(allUser).build();
    }
    
    @Path("/Facebook")
    @GET
    public Response getAllActiveFacebook(@Auth Boolean isAuthenticated) {
        List<Facebook> allFacebook =  facebookDao.getAllActiveFacebook();
        return Response.ok(allFacebook).build();
    }
    
    @Path("/Facebook/all")
    @GET
    public Response getAllFacebook(@Auth Boolean isAuthenticated) {
        List<Facebook> allFacebook =  facebookDao.getAllFacebook();
        return Response.ok(allFacebook).build();
    }
    
    @Path("/Friend")
    @GET
    public Response getAllActiveFriend(@Auth Boolean isAuthenticated) {
        List<Friend> allFriend =  friendDao.getAllActiveFriend();
        return Response.ok(allFriend).build();
    }
    
    @Path("/Friend/all")
    @GET
    public Response getAllFriend(@Auth Boolean isAuthenticated) {
        List<Friend> allFriend =  friendDao.getAllFriend();
        return Response.ok(allFriend).build();
    }
    
    @Path("/Merchant")
    @GET
    public Response getAllActiveMerchant(@Auth Boolean isAuthenticated) {
        List<Merchant> allMerchant =  merchantDao.getAllActiveMerchant();
        return Response.ok(allMerchant).build();
    }    
    
    @Path("/Merchant/all")
    @GET
    public Response getAllMerchant(@Auth Boolean isAuthenticated) {
        List<Merchant> allMerchant =  merchantDao.getAllMerchant();
        return Response.ok(allMerchant).build();
    }
    
    @Path("/Item")
    @GET
    public Response getAllActiveItem(@Auth Boolean isAuthenticated) {
        List<Item> allItem =  itemDao.getAllActiveItem();
        return Response.ok(allItem).build();
    }    
    
    @Path("/Item/all")
    @GET
    public Response getAllItem(@Auth Boolean isAuthenticated) {
        List<Item> allItem =  itemDao.getAllItem();
        return Response.ok(allItem).build();
    }
    /*
    @Path("/User/{id}")
    @DELETE
    public Response deleteUserById(@PathParam("id") String id, @Auth Boolean isAuthenticated) throws URISyntaxException, SQLException{
 	   Handle handle = jdbi.open();
       handle.getConnection().setAutoCommit(false);
       try {
           handle.begin();
           FacebookDAO facebookDao = handle.attach(FacebookDAO.class);
           UserDAO userDao = handle.attach(UserDAO.class);
           FriendDAO friendDao = handle.attach(FriendDAO.class);
           
           userDao.deleteUser(id);
           facebookDao.deleteFacebookByUserId(id);
           friendDao.deleteFriendByUserId(id);
           
           handle.commit();
           return Response.noContent().build();
       } 
       catch (Exception e) {
           handle.rollback();
           throw e;
       }
    }*/
}
