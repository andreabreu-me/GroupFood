package com.dwbook.phonebook.resources;

import com.dwbook.phonebook.dao.ContactDAO;
import com.dwbook.phonebook.representations.Contact;
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
 * Created by curtishu on 9/27/14.
 */
@Path("/contact")
@Produces(MediaType.APPLICATION_JSON + "; charset=utf-8")
public class ContactResource {

    final static Logger logger = LoggerFactory.getLogger(ContactResource.class);
    private final ContactDAO contactDao;

    public ContactResource(DBI jdbi) {
        contactDao = jdbi.onDemand(ContactDAO.class);
    }

    @GET
    @Path("/all")
    public Response getAllContact(@Auth Boolean isAuthenticated) {
        List<Contact> allContact =  contactDao.getAllContact();
        return Response.ok(allContact).build();
    }

    @GET
    @Path("/{id}")
    public Response getContact(@PathParam("id") int id, @Auth Boolean isAuthenticated) {
        logger.info(String.format("%d retrieved", id));
        // retrieve information about the contact with the provided id
        Contact contact = contactDao.getContactById(id);
        return Response
                .ok(contact)
                .build();
    }

    @POST
    public Response createContact(Contact contact, @Auth Boolean isAuthenticated) throws URISyntaxException {
        // store the new contact
        int newContactId = contactDao.createContact(contact.getFirstName(), contact.getLastName(), contact.getPhone());
        return Response.created(new URI(String.valueOf(newContactId))).build();
    }

    @POST
    @Path("/batch_create")
    public Response batchCreateContact(List<Contact> contact, @Auth Boolean isAuthenticated) throws URISyntaxException {
        int[] ids = contactDao.batchCreateContact(contact);
        return Response.created(new URI(String.valueOf(ids.length))).build();
    }

    @DELETE
    @Path("/{id}")
    public Response deleteContact(@PathParam("id") int id, @Auth Boolean isAuthenticated) {
        // delete the contact with the provided id
        try {
            contactDao.begin();
            contactDao.deleteContact(id);
            System.out.println("after delete called");
            //throw new Exception("test exception");
            contactDao.commit();
        } catch (Exception e) {
            contactDao.rollback();
        }
        return Response.noContent().build();
    }

    @PUT
    @Path("/{id}")
    public Response updateContact(@PathParam("id") int id, Contact contact, @Auth Boolean isAuthenticated) {
        // update the contact with the provided ID
        contactDao.updateContact(id, contact.getFirstName(),
                contact.getLastName(), contact.getPhone());
        return Response.ok(
                new Contact(id, contact.getFirstName(), contact.getLastName(),
                        contact.getPhone())).build();
    }
}
