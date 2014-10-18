package com.dwbook.phonebook.resources;
//123
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
import com.dwbook.phonebook.representations.Facebook;

/**
 * Created by howard on 10/12/14.
 */

@Path("/Facebook")
@Produces(MediaType.APPLICATION_JSON + "; charset=utf-8")
public class FacebookResourceAdmin {

    final static Logger logger = LoggerFactory.getLogger(FacebookResourceAdmin.class);
    private final FacebookDAO facebookDao;

    public FacebookResourceAdmin(DBI jdbi) {
        facebookDao = jdbi.onDemand(FacebookDAO.class);
    }

    @GET
    public Response getAllFacebook(@Auth Boolean isAuthenticated) {
        List<Facebook> allFacebook =  facebookDao.getAllFacebook();
        return Response.ok(allFacebook).build();
    }
}
