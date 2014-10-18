package com.dwbook.phonebook.resources;

import com.dwbook.phonebook.dao.GroupDAO;
import com.dwbook.phonebook.dao.GroupUserDAO;
import com.dwbook.phonebook.representations.Group;
import com.dwbook.phonebook.representations.GroupUser;
import com.dwbook.phonebook.representations.User;
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
@Path("/User/{organizerId}/Group")
@Produces(MediaType.APPLICATION_JSON + "; charset=utf-8")
public class GroupResource {

    final static Logger logger = LoggerFactory.getLogger(GroupResource.class);
    private final DBI jdbi;
    private final GroupDAO groupDAO;
    private final GroupUserDAO groupUserDAO;

    public GroupResource(DBI jdbi) {
        this.jdbi = jdbi;
        this.groupDAO = jdbi.onDemand(GroupDAO.class);
        this.groupUserDAO = jdbi.onDemand(GroupUserDAO.class);
    }

    public static boolean isAuthorized(String actor, String owner) {
        return actor != null && !actor.isEmpty() && actor.equals(owner);
    }

    @GET
    @Path("/{id}")
    public Response getGroupById(@PathParam("id") long id, @Auth Boolean isAuthenticated) {
        Group group = groupDAO.getGroupById(id);
        return Response.ok(group).build();
    }

    @GET
    public Response findGroupsByOrganizerId(@PathParam("organizerId") String organizerId, @Auth Boolean isAuthenticated) {
        List<Group> groups = groupDAO.findGroupsByOrganizerId(organizerId);
        return Response.ok(groups).build();
    }

    @GET
    @Path("/{id}/GroupUser")
    public Response findUsersByGroupId(@PathParam("id") long id, @Auth Boolean isAuthenticated) {
        List<User> users = groupUserDAO.findUsersByGroupId(id);
        return Response.ok(users).build();
    }

    @DELETE
    @Path("/{id}/GroupUser/{userId}")
    public Response deleteGroupUser(@PathParam("id") long groupId,
                                    @PathParam("organizerId") String organizerId,
                                    @PathParam("userId") String userId,
                                    @QueryParam("actorId") String actorId,
                                    @Auth Boolean isAuthenticated) throws SQLException {

        //only organizer or self can remove from group
        if(!isAuthorized(actorId, organizerId) && !isAuthorized(actorId, userId)) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }

        //when organizer is removed, group is deleted
        if(userId.equals(organizerId)) {
            Handle handle = jdbi.open();
            handle.getConnection().setAutoCommit(false);
            try {
                handle.begin();
                GroupDAO gDAO = handle.attach(GroupDAO.class);
                GroupUserDAO guDAO = handle.attach(GroupUserDAO.class);
                guDAO.deleteGroupUser(groupId, userId);
                gDAO.deleteGroup(groupId);
                handle.commit();
                return Response.noContent().build();
            } catch (Exception e) {
                handle.rollback();
                e.printStackTrace();
                return Response.serverError().build();
            }
        } else {
            groupUserDAO.deleteGroupUser(groupId, userId);
            return Response.noContent().build();
        }
    }

    @POST
    @Path("/{id}/GroupUser/{userId}")
    public Response createGroupUser(@PathParam("id") long groupId,
                                    @PathParam("organizerId") String organizerId,
                                    @PathParam("userId") String userId,
                                    @QueryParam("actorId") String actorId,
                                    @Auth Boolean isAuthenticated) throws URISyntaxException {

        //only organizer or self can add to group
        if(!isAuthorized(actorId, organizerId) && !isAuthorized(actorId, userId)) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }

        groupUserDAO.createGroupUser(groupId, userId);
        return Response.created(new URI(String.format("/%d/GroupUser/%s", groupId, userId))).build();
    }

    @POST
    @Path("/{id}/GroupUser")
    public Response createGroupUser(final List<String> userIds,
                                    @PathParam("id") long groupId,
                                    @PathParam("organizerId") String organizerId,
                                    @QueryParam("actorId") String actorId,
                                    @Auth Boolean isAuthenticated) throws URISyntaxException {

        //only organizer can batch add
        if(!isAuthorized(actorId, organizerId)) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }

        List<GroupUser> groupUsers = new ArrayList<GroupUser>();
        for(String userId : userIds) {
            groupUsers.add(new GroupUser(groupId, userId));
        }
        groupUserDAO.createGroupUserBatch(groupUsers);
        return Response.created(new URI(String.format("/%d/GroupUser/%d", groupId, groupUsers.size()))).build();
    }

    @POST
    public Response createGroup(Group g,
                                @PathParam("organizerId") String organizerId,
                                @QueryParam("actorId") String actorId,
                                @Auth Boolean isAuthenticated) throws URISyntaxException, SQLException {

        List<Group> list = new ArrayList<Group>();
        list.add(g);
        return createGroup(list, organizerId, actorId, isAuthenticated);
    }

    @POST
    @Path("/batch")
    public Response createGroup(final List<Group> groups,
                                @PathParam("organizerId") String organizerId,
                                @QueryParam("actorId") String actorId,
                                @Auth Boolean isAuthenticated) throws SQLException {

        if(!isAuthorized(actorId, organizerId)) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }

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
    public Response updateGroup(Group g,
                                @PathParam("organizerId") String organizerId,
                                @PathParam("id") long id,
                                @QueryParam("actorId") String actorId,
                                @Auth Boolean isAuthenticated) {

        //only owner can update name and description, cannot change organizerId
        if (!isAuthorized(actorId, organizerId)) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }

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
    public Response deleteGroup(@PathParam("organizerId") String organizerId,
                                @PathParam("id") long id,
                                @QueryParam("actorId") String actorId,
                                @Auth Boolean isAuthenticated) throws SQLException {

        // only organizer can delete the entry
        if (!isAuthorized(actorId, organizerId)) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }

        // soft delete the group with the provided id
        Handle handle = jdbi.open();
        handle.getConnection().setAutoCommit(false);
        try {
            handle.begin();
            GroupDAO gDAO = handle.attach(GroupDAO.class);
            GroupUserDAO guDAO = handle.attach(GroupUserDAO.class);

            //cascade mark GroupUser entries as deleted
            gDAO.deleteGroup(id);
            guDAO.deleteGroup(id);
            handle.commit();
            return Response.noContent().build();
        } catch (Exception e) {
            handle.rollback();
            e.printStackTrace();
            return Response.serverError().build();
        }
    }
}
