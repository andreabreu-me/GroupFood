package com.dwbook.phonebook.resources;

import io.dropwizard.auth.Auth;

import java.net.URISyntaxException;
import java.sql.SQLException;
import java.util.List;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.skife.jdbi.v2.DBI;
import org.skife.jdbi.v2.Handle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dwbook.phonebook.dao.OrderMerchantDAO;
import com.dwbook.phonebook.representations.OrderMerchant;

/**
 * Created by howard on 10/24/14.
 */

/*@GET 	
 * 			/User/{userId}/OrderMerchant																										return all OrderMerchant that merchant participates
 */

@Path("/User/{userId}/OrderMerchant")
@Produces(MediaType.APPLICATION_JSON + "; charset=utf-8")
public class OrderMerchantResource {

    final static Logger logger = LoggerFactory.getLogger(OrderMerchantResource.class);
    private final OrderMerchantDAO orderMerchantDao;
    private final DBI jdbi;

    public OrderMerchantResource(DBI jdbi) {
    	orderMerchantDao = jdbi.onDemand(OrderMerchantDAO.class);
        this.jdbi = jdbi;
    }
}