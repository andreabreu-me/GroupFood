package com.dwbook.phonebook.resources;

import com.dwbook.phonebook.dao.UserDAO;
import com.dwbook.phonebook.representations.User;
import io.dropwizard.auth.Auth;
import org.skife.jdbi.v2.DBI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

/**
 * Created by howard on 10/12/14.
 */

//!!warning: I believe Calendar works with the time zone in which the computer is running
@Path("/user")
@Produces(MediaType.APPLICATION_JSON + "; charset=utf-8")
public class UserResource {

    final static Logger logger = LoggerFactory.getLogger(UserResource.class);
    private final UserDAO userDao;

    public UserResource(DBI jdbi) {
        userDao = jdbi.onDemand(UserDAO.class);
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
    public Response createUser(User user, @Auth Boolean isAuthenticated) throws URISyntaxException {
        // store the new user
    	Calendar calendar = Calendar.getInstance();
    	Timestamp timeStamp = new Timestamp(calendar.getTime().getTime());
        int newUserId = userDao.createUser(user.getId(), user.getFacebookId(), user.getGooglePlusId(), timeStamp.toString());
        return Response.created(new URI(String.valueOf(newUserId))).build();
    }

    @POST
    @Path("/batch_create")
    public Response batchCreateUser(List<User> user, @Auth Boolean isAuthenticated) throws URISyntaxException {
        int[] ids = userDao.batchCreateUser(user);
        return Response.created(new URI(String.valueOf(ids.length))).build();
    }

    @DELETE
    @Path("/{id}")
    public Response softDeleteUser(@PathParam("id") String id, @Auth Boolean isAuthenticated) {
        // delete the user with the provided id
        try {
            userDao.begin();
            Calendar calendar = Calendar.getInstance();
        	Timestamp timeStamp = new Timestamp(calendar.getTime().getTime());
            userDao.softDeleteUser(id, timeStamp.toString());
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
    	Calendar calendar = Calendar.getInstance();
    	Timestamp timeStamp = new Timestamp(calendar.getTime().getTime());
        userDao.updateUser(id, user.getFacebookId(), user.getGooglePlusId(), timeStamp.toString());
        return Response.ok(
                new User(id, user.getFacebookId(), user.getGooglePlusId(), user.getCreatedOn(), user.getUpdatedOn(), user.getDeletedOn())).build();
    }
}
