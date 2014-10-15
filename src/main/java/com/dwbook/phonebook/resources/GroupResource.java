package com.dwbook.phonebook.resources;

import com.dwbook.phonebook.dao.GroupDAO;
import com.dwbook.phonebook.dao.GroupUserDAO;
import com.dwbook.phonebook.representations.Group;
import io.dropwizard.auth.Auth;
import org.skife.jdbi.v2.DBI;
import org.skife.jdbi.v2.Handle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by curtishu on 10/11/14.
 */
@Path("/Group")
@Produces(MediaType.APPLICATION_JSON + "; charset=utf-8")
public class GroupResource {

    final static Logger logger = LoggerFactory.getLogger(GroupResource.class);
    private final DBI jdbi;
    private final GroupDAO groupDAO;

    public GroupResource(DBI jdbi) {
        this.jdbi = jdbi;
        this.groupDAO = jdbi.onDemand(GroupDAO.class);
    }

    @GET
    @Path("/{id}")
    public Response getContact(@PathParam("id") long id, @Auth Boolean isAuthenticated) {
        Group group = groupDAO.getGroupById(id);
        return Response.ok(group).build();
    }

    @POST
    public Response createGroup(Group g, @Auth Boolean isAuthenticated) throws URISyntaxException, SQLException {
        List<Group> list = new ArrayList<Group>();
        list.add(g);
        return createGroup(list, isAuthenticated);
    }

    @POST
    @Path("/batch")
    public Response createGroup(final List<Group> groups, @Auth Boolean isAuthenticated) throws SQLException {
        Handle handle = jdbi.open();
        handle.getConnection().setAutoCommit(false);
        try {
            handle.begin();
            GroupDAO groupDAO = handle.attach(GroupDAO.class);
            GroupUserDAO groupUserDAO = handle.attach(GroupUserDAO.class);
            int success = 0;
            for (Group g : groups) {
                long groupId = groupDAO.createGroup(g.getOrganizerId(), g.getName(), g.getDescription());
                groupUserDAO.createGroupUser(groupId, g.getOrganizerId());
                success++;
            }
            if (success != groups.size()) {
                throw new IllegalStateException("error creating group, some groups cannot be created");
            }
            handle.commit();
            return Response.created(new URI(String.valueOf(success))).build();
        } catch (Exception e) {
            handle.rollback();
            e.printStackTrace();
            return Response.serverError().build();
        }
    }

    @PUT
    @Path("/{id}")
    public Response updateContact(@PathParam("id") long id, Group g, @Auth Boolean isAuthenticated) {
        try {
            groupDAO.begin();
            groupDAO.updateGroup(id, g.getOrganizerId(), g.getName(), g.getDescription());
            groupDAO.commit();
            return Response.ok(new Group(id, g.getOrganizerId(), g.getName(), g.getDescription(), 0, "")).build();
        } catch (Exception e) {
            groupDAO.rollback();
            e.printStackTrace();
            return Response.serverError().build();
        }
    }

    @DELETE
    @Path("/{id}")
    public Response deleteContact(@PathParam("id") long id, @Auth Boolean isAuthenticated) {
        // delete the contact with the provided id
        try {
            //TODO cascade mark GroupUser entries as deleted
            groupDAO.begin();
            groupDAO.deleteGroup(id );
            groupDAO.commit();
            return Response.noContent().build();
        } catch (Exception e) {
            groupDAO.rollback();
            e.printStackTrace();
            return Response.serverError().build();
        }
    }
}
