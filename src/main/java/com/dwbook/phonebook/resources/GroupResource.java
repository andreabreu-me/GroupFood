package com.dwbook.phonebook.resources;

import com.dwbook.phonebook.dao.GroupDAO;
import com.dwbook.phonebook.dao.GroupUserDAO;
import com.dwbook.phonebook.representations.Group;
import com.dwbook.phonebook.representations.GroupUser;
import io.dropwizard.auth.Auth;
import org.skife.jdbi.v2.DBI;
import org.skife.jdbi.v2.Handle;
import org.skife.jdbi.v2.TransactionCallback;
import org.skife.jdbi.v2.TransactionStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
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
@Path("/group")
@Produces(MediaType.APPLICATION_JSON + "; charset=utf-8")
public class GroupResource {

    final static Logger logger = LoggerFactory.getLogger(GroupResource.class);
    private final DBI jdbi;

    public GroupResource(DBI jdbi) { this.jdbi = jdbi; }

    @POST
    public Response createGroup(final List<Group> groups, @Auth Boolean isAuthenticated) throws URISyntaxException, SQLException {
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
            throw e;
        }
    }
}
