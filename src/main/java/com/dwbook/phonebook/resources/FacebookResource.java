package com.dwbook.phonebook.resources;

import io.dropwizard.auth.Auth;

import java.net.URISyntaxException;
import java.sql.SQLException;
import java.util.List;

import javax.ws.rs.GET;
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

import com.dwbook.phonebook.dao.FacebookDAO;
import com.dwbook.phonebook.dao.FriendDAO;
import com.dwbook.phonebook.dao.UserDAO;
import com.dwbook.phonebook.representations.Facebook;
import com.dwbook.phonebook.representations.FacebookToken;
import com.dwbook.phonebook.representations.Friend;
import com.dwbook.phonebook.representations.User;

/**
 * Created by howard on 10/12/14.
 */

/*@GET 	
 * 			User/{userId}/Facebook						return facebook where userId=userId
 *@PUT
 *				User/{userId}/facebook						update facebook where userId=userId, update Friend table accordingly
 */

@Path("/User/{userId}/Facebook")
@Produces(MediaType.APPLICATION_JSON + "; charset=utf-8")
public class FacebookResource {

    final static Logger logger = LoggerFactory.getLogger(FacebookResource.class);
    private final UserDAO userDao;
    private final FacebookDAO facebookDao;
    private final DBI jdbi;

    public FacebookResource(DBI jdbi) {
        userDao = jdbi.onDemand(UserDAO.class);
        facebookDao = jdbi.onDemand(FacebookDAO.class);
        this.jdbi = jdbi;
    }

    @GET
    public Response getFacebookByUserId(@PathParam("userId") String userId, @Auth Boolean isAuthenticated) {
        Facebook facebook = facebookDao.getFacebookByUserId(userId);
        return Response
                .ok(facebook)
                .build();
    }
/*
    @DELETE
    public Response deleteFacebook(@PathParam("userId") String userId, @Auth Boolean isAuthenticated) throws URISyntaxException, SQLException {
 	   Handle handle = jdbi.open();
       handle.getConnection().setAutoCommit(false);
       try {
           handle.begin();
           FacebookDAO facebookDao = handle.attach(FacebookDAO.class);
           FriendDAO friendDao = handle.attach(FriendDAO.class);
           
           facebookDao.deleteFacebookByUserId(userId);
           friendDao.deleteFacebookFriendByUserId(userId);
           
           handle.commit();
           return Response.noContent().build();
       } 
       catch (Exception e) {
           handle.rollback();
           throw e;
       }
    }
*/
    @PUT
    public Response updateFacebook(@PathParam("userId") String userId, FacebookToken facebookToken, @Auth Boolean isAuthenticated) throws URISyntaxException, SQLException {
       User user = userDao.getUserById(userId);
  	   Handle handle = jdbi.open();
       handle.getConnection().setAutoCommit(false);
       try {
           handle.begin();
           FacebookDAO facebookDao = handle.attach(FacebookDAO.class);
           FriendDAO friendDao = handle.attach(FriendDAO.class);
           
           facebookDao.updateFacebookByUserId(userId, facebookToken.getToken(), facebookToken.getFirstName(), facebookToken.getLastName(), facebookToken.getEmail());
           List<Friend> friend = friendDao.getFriendByUserId(userId);
           friendDao.batchDelete(userId, friend);
           friendDao.updateFriend(userId, facebookToken.getFriend());
           handle.commit();
        	return Response.ok(
                new Facebook(userId, user.getFacebookId(), facebookToken.getToken(), facebookToken.getFirstName(), facebookToken.getLastName(), facebookToken.getEmail())).build();
       } 
        catch (Exception e) {
            handle.rollback();
            throw e;
        }
    }
}
