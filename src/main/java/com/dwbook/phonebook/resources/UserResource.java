package com.dwbook.phonebook.resources;

import io.dropwizard.auth.Auth;

import java.net.URI;
import java.net.URISyntaxException;
import java.sql.SQLException;

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

import com.dwbook.phonebook.dao.FacebookDAO;
import com.dwbook.phonebook.dao.FriendDAO;
import com.dwbook.phonebook.dao.UserDAO;
import com.dwbook.phonebook.representations.SignIn;
import com.dwbook.phonebook.representations.User;

/**
 * Created by howard on 10/12/14.
 */

/*@GET 	
 * 			User/{id}							return user id = id if deleteOn is not null
 *@Post
 *				User								create entries in User, Facebook, and Friend
 */

@Path("/User")
@Produces(MediaType.APPLICATION_JSON + "; charset=utf-8")
public class UserResource {

    final static Logger logger = LoggerFactory.getLogger(UserResource.class);
    private final UserDAO userDao;
    private final DBI jdbi;

    public UserResource(DBI jdbi) {
        userDao = jdbi.onDemand(UserDAO.class);
        this.jdbi = jdbi;
    }
      
    @GET
    @Path("/{id}")
    public Response getUser(@PathParam("id") String id, @Auth Boolean isAuthenticated) {
        User user = userDao.getUserById(id);
        return Response
                .ok(user)
                .build();
    }

    @GET
    @Path("/FacebookId/{facebookId}")
    public Response getUserByFacebookId(@PathParam("facebookId") String facebookId, @Auth Boolean isAuthenticated) {
        User user = userDao.getUserByFacebookId(facebookId);
        return Response
                .ok(user)
                .build();
    }

    @POST
    public Response createUser(SignIn signIn, @Auth Boolean isAuthenticated) throws URISyntaxException, SQLException{
    	   Handle handle = jdbi.open();
           handle.getConnection().setAutoCommit(false);
           try {
               handle.begin();
               FacebookDAO facebookDao = handle.attach(FacebookDAO.class);
               UserDAO userDao = handle.attach(UserDAO.class);
               FriendDAO friendDao = handle.attach(FriendDAO.class);
               
               int newUserId = userDao.createUser(signIn.getUserId(), signIn.getFacebookId(), signIn.getGooglePlusId());
               facebookDao.createFacebook(signIn.getFacebookId(), signIn.getUserId(), signIn.getToken(), signIn.getFirstName(), signIn.getLastName(), signIn.getEmail(), signIn.getImageJson());
               //for first user his/her friend list may be null or empty, create only if they're available
               if(signIn.getFriend() != null && signIn.getFriend().size() > 0) {
                   friendDao.batchCreateFriend(signIn.getUserId(), signIn.getFriend());
               }
               
               handle.commit();
               return Response.created(new URI(String.valueOf(newUserId))).build();
           } 
           catch (Exception e) {
               handle.rollback();
               throw e;
           }
    }
/*        	this method should be update once we support more than one social network
 * 		SignIn should also be updated so it supports "google token"
    @PUT
    @Path("/{id}")
    public Response updateUser(@PathParam("id") String id, SignIn signIn, @Auth Boolean isAuthenticated) throws SQLException {
    	Handle handle = jdbi.open();
        handle.getConnection().setAutoCommit(false);
        try {
             handle.begin();
             UserDAO userDao = handle.attach(UserDAO.class);
             if(signIn.getFacebookId()!=null){
          	    FacebookDAO facebookDao = handle.attach(FacebookDAO.class);
          	  String firstName = "Changhao";
              String lastName = "Huang";
              String email = "howard168222@hotmail.com";
                facebookDao.updateFacebook(user.getFacebookId(), firstName, lastName, email);
             }
             userDao.updateUser(id, user.getFacebookId(), user.getGooglePlusId());
             
             handle.commit();
             return Response.ok(
                     new User(id, user.getFacebookId(), user.getGooglePlusId())).build();
         } 
         catch (Exception e) {
             handle.rollback();
             throw e;
         }
    }
    */
}