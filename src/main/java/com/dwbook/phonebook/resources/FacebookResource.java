package com.dwbook.phonebook.resources;
//123
import io.dropwizard.auth.Auth;

import java.net.URI;
import java.net.URISyntaxException;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dwbook.phonebook.dao.FacebookDAO;
import com.dwbook.phonebook.representations.Facebook;

/**
 * Created by howard on 10/12/14.
 */

@Path("/Facebook")
@Produces(MediaType.APPLICATION_JSON + "; charset=utf-8")
public class FacebookResource {

    final static Logger logger = LoggerFactory.getLogger(FacebookResource.class);
    private final FacebookDAO facebookDao;

    public FacebookResource(DBI jdbi) {
        facebookDao = jdbi.onDemand(FacebookDAO.class);
    }

    @GET
    @Path("/all")
    public Response getAllFacebook(@Auth Boolean isAuthenticated) {
        List<Facebook> allFacebook =  facebookDao.getAllFacebook();
        return Response.ok(allFacebook).build();
    }

    @GET
    @Path("/{id}")
    public Response getFacebook(@PathParam("id") String id, @Auth Boolean isAuthenticated) {
        logger.info(String.format("%s retrieved", id));
        // retrieve information about the facebook with the provided id
        Facebook facebook = facebookDao.getFacebookById(id);
        return Response
                .ok(facebook)
                .build();
    }

    @POST
    public Response createFacebook(Facebook facebook, @Auth Boolean isAuthenticated) throws URISyntaxException {
        // store the new facebook
        int newFacebookId = facebookDao.createFacebook(facebook.getId(), facebook.getUserId(), facebook.getToken(), facebook.getFirstName(), facebook.getLastName(), facebook.getEmail());
        return Response.created(new URI(String.valueOf(newFacebookId))).build();
    }

    @POST
    @Path("/batch_create")
    public Response batchCreateFacebook(List<Facebook> facebook, @Auth Boolean isAuthenticated) throws URISyntaxException {
        int[] ids = facebookDao.batchCreateFacebook(facebook);
        return Response.created(new URI(String.valueOf(ids.length))).build();
    }

    @DELETE
    @Path("/{id}")
    public Response deleteFacebook(@PathParam("id") String id, @Auth Boolean isAuthenticated) {
        // delete the facebook with the provided id
        try {
            facebookDao.begin();
            facebookDao.deleteFacebook(id);
            System.out.println("after delete called");
            //throw new Exception("test exception");
            facebookDao.commit();
        } catch (Exception e) {
            facebookDao.rollback();
        }
        return Response.noContent().build();
    }

    @PUT
    @Path("/{id}")
    public Response updateFacebook(@PathParam("id") String id, Facebook facebook, @Auth Boolean isAuthenticated) {
        // update the facebook with the provided ID
        facebookDao.updateFacebook(id, facebook.getFirstName(), facebook.getLastName(), facebook.getEmail());
        return Response.ok(
                new Facebook(id, facebook.getUserId(), facebook.getToken(), facebook.getFirstName(), facebook.getLastName(), facebook.getEmail(), facebook.getCreatedOn(), facebook.getUpdatedOn(), facebook.getDeletedOn())).build();
    }
}
