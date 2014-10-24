package com.dwbook.phonebook.resources;

import io.dropwizard.auth.Auth;

import java.net.URI;
import java.net.URISyntaxException;
import java.sql.SQLException;

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

import com.dwbook.phonebook.dao.MerchantDAO;
import com.dwbook.phonebook.representations.Merchant;

/**
 * Created by howard on 10/21/14.
 */

/*@GET 	
 * 			Merchant/{id}							return Merchant id = id if deleteOn is not null
 * 			Merchant/name/{name}		return Merchant name = name if deleteOn is not null
 *@POST
 *				Merchant								create entries in Merchant
 *@PUT
 *				Merchant/{id}							update merchant's data	
 */

@Path("/Merchant")
@Produces(MediaType.APPLICATION_JSON + "; charset=utf-8")
public class MerchantResource {

    final static Logger logger = LoggerFactory.getLogger(MerchantResource.class);
    private final MerchantDAO merchantDao;
    private final DBI jdbi;

    public MerchantResource(DBI jdbi) {
    	merchantDao = jdbi.onDemand(MerchantDAO.class);
        this.jdbi = jdbi;
    }
      
    @GET
    @Path("/{id}")
    public Response getMerchant(@PathParam("id") int id, @Auth Boolean isAuthenticated) {
        Merchant Merchant = merchantDao.getMerchantById(id);
        return Response
                .ok(Merchant)
                .build();
    }
    @GET
    @Path("/name/{name}")
    public Response getMerchant(@PathParam("name") String name, @Auth Boolean isAuthenticated) {
        Merchant Merchant = merchantDao.getMerchantByName(name);
        return Response
                .ok(Merchant)
                .build();
    }

    @POST
    public Response createMerchant(Merchant merchant, @Auth Boolean isAuthenticated) throws URISyntaxException, SQLException{
    	   Handle handle = jdbi.open();
           handle.getConnection().setAutoCommit(false);
           try {
               handle.begin();
               MerchantDAO MerchantDao = handle.attach(MerchantDAO.class);
               int newMerchantId = MerchantDao.createMerchant(merchant.getId(), merchant.getName(),merchant.getBranch(),merchant.getDescription(),
            		   merchant.getAddress(),merchant.getLatitude(), merchant.getLongitude(), merchant.getDeliverDistanceKm(), merchant.getMinimumOrder(),
            		   merchant.getMinimumDelivery(),merchant.getMainPhone(),merchant.getMobilePhone(),merchant.getOrderSubmissionJson(),
            		   merchant.getImageJson(),merchant.getFeedbackJson());
               
               handle.commit();
               return Response.created(new URI(String.valueOf(newMerchantId))).build();
           } 
           catch (Exception e) {
               handle.rollback();
               throw e;
           }
    }
    
    @PUT
    @Path("/{id}")
    public Response createMerchant(Merchant merchant, @PathParam("id") int id, @Auth Boolean isAuthenticated) throws URISyntaxException, SQLException{
    	   Handle handle = jdbi.open();
           handle.getConnection().setAutoCommit(false);
           try {
               handle.begin();
               MerchantDAO MerchantDao = handle.attach(MerchantDAO.class);
               MerchantDao.updateMerchant(id, merchant.getName(),merchant.getBranch(),merchant.getDescription(),
            		   merchant.getAddress(),merchant.getLatitude(), merchant.getLongitude(), merchant.getDeliverDistanceKm(), merchant.getMinimumOrder(),
            		   merchant.getMinimumDelivery(),merchant.getMainPhone(),merchant.getMobilePhone(),merchant.getOrderSubmissionJson(),
            		   merchant.getImageJson(),merchant.getFeedbackJson());
               
               handle.commit();
               return Response.ok(
                       new Merchant(id, merchant.getName(),merchant.getBranch(),merchant.getDescription(),
                    		   merchant.getAddress(),merchant.getLatitude(), merchant.getLongitude(), merchant.getDeliverDistanceKm(), merchant.getMinimumOrder(),
                    		   merchant.getMinimumDelivery(),merchant.getMainPhone(),merchant.getMobilePhone(),merchant.getOrderSubmissionJson(),
                    		   merchant.getImageJson(),merchant.getFeedbackJson())).build();
           } 
           catch (Exception e) {
               handle.rollback();
               throw e;
           }
    }
}