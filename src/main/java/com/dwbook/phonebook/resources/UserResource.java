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

import com.dwbook.phonebook.dao.FacebookDAO;
import com.dwbook.phonebook.dao.UserDAO;
import com.dwbook.phonebook.representations.User;

/**
 * Created by howard on 10/12/14.
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
    @Path("/all")
    public Response getAllUser(@Auth Boolean isAuthenticated) {
    	
        List<User> allUser =  userDao.getAllUser();
        return Response.ok(allUser).build();
    }

    @GET
    @Path("/{id}")
    public Response getUser(@PathParam("id") String id, @Auth Boolean isAuthenticated) {
        logger.info(String.format("%s retrieved", id));
        // retrieve information about the user with the provided id
        User user = userDao.getUserById(id);
        return Response
                .ok(user)
                .build();
    }

    @POST
    public Response createUser(User user, @Auth Boolean isAuthenticated) throws URISyntaxException, SQLException{
    	   Handle handle = jdbi.open();
           handle.getConnection().setAutoCommit(false);
           try {
               handle.begin();
               FacebookDAO facebookDao = handle.attach(FacebookDAO.class);
               // store the new user
               int newUserId = userDao.createUser(user.getId(), user.getFacebookId(), user.getGooglePlusId());
               /*methods to be installed
                * String [] lotOfData = retrieveDataFromFBId()
              */
               String id = "ChanghaoFBId";
               String userId = "Changha";
               String token = "token1234";
               String firstName = "Changhao";
               String lastName = "Huang";
               String email = "howard168222@hotmail.com";
               facebookDao.createFacebook(id, userId, token, firstName, lastName, email);
               handle.commit();
               return Response.created(new URI(String.valueOf(newUserId))).build();
           } 
           catch (Exception e) {
               handle.rollback();
               throw e;
           }
    }

    @POST
    @Path("/batch_create")
    public Response batchCreateUser(List<User> user, @Auth Boolean isAuthenticated) throws URISyntaxException {
        int[] ids = userDao.batchCreateUser(user);
        return Response.created(new URI(String.valueOf(ids.length))).build();
    }

    @DELETE
    @Path("/{id}")
    public Response deleteUser(@PathParam("id") String id, @Auth Boolean isAuthenticated) {
        // delete the user with the provided id
        try {
            userDao.begin();
            userDao.deleteUser(id);
            System.out.println("after delete called");
            //throw new Exception("test exception");
            userDao.commit();
        } catch (Exception e) {
            userDao.rollback();
        }
        return Response.noContent().build();
    }

    @PUT
    @Path("/{id}")
    public Response updateUser(@PathParam("id") String id, User user, @Auth Boolean isAuthenticated) {
        // update the user with the provided ID
        userDao.updateUser(id, user.getFacebookId(), user.getGooglePlusId());
        return Response.ok(
                new User(id, user.getFacebookId(), user.getGooglePlusId(), user.getCreatedOn(), user.getUpdatedOn(), user.getDeletedOn())).build();
    }
}