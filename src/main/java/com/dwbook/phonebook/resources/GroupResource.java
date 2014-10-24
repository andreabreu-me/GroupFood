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
@Path("/GroupUser")
@Produces(MediaType.APPLICATION_JSON + "; charset=utf-8")
public class GroupResource {

    //TODO use a different name for class variable to avoid confusion

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

    public String getOrganizerIdByGroupId (long groupId) {
        return groupDAO.getGroupById(groupId).getOrganizerId();
    }

    @GET
    @Path("/User/{userId}/Group")
    public Response findGroupsByUserId(@PathParam("userId") String userId, @Auth Boolean isAuthenticated) {
        List<Group> groups = groupUserDAO.findGroupsByUserId(userId);
        return Response.ok(groups).build();
    }

    @GET
    @Path("/Organizer/{organizerId}/Group")
    public Response findGroupsByOrganizerId(@PathParam("organizerId") String organizerId, @Auth Boolean isAuthenticated) {
        List<Group> groups = groupDAO.findGroupsByOrganizerId(organizerId);
        return Response.ok(groups).build();
    }

    @POST
    @Path("/Group")
    public Response createGroup(Group g,
                                @QueryParam("actorId") String actorId,
                                @Auth Boolean isAuthenticated) throws URISyntaxException, SQLException {

        List<Group> list = new ArrayList<Group>();
        list.add(g);
        return createGroup(list, actorId, isAuthenticated);
    }

    @POST
    @Path("/Group/batch")
    public Response createGroup(final List<Group> groups,
                                @QueryParam("actorId") String actorId,
                                @Auth Boolean isAuthenticated) throws SQLException {

        Handle handle = jdbi.open();
        handle.getConnection().setAutoCommit(false);
        try {
            handle.begin();
            GroupDAO groupDAO = handle.attach(GroupDAO.class);
            GroupUserDAO groupUserDAO = handle.attach(GroupUserDAO.class);
            int success = 0;
            for (Group g : groups) {
                if(!g.getOrganizerId().equals(actorId)) {
                    throw new IllegalStateException("organizerId needs to match with the person creating the group");
                }
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

    @GET
    @Path("/Group/{id}")
    public Response getGroupById(@PathParam("id") long id, @Auth Boolean isAuthenticated) {
        Group group = groupDAO.getGroupById(id);
        return Response.ok(group).build();
    }


    @PUT
    @Path("/Group/{id}")
    public Response updateGroup(Group g,
                                @PathParam("id") long id,
                                @QueryParam("actorId") String actorId,
                                @Auth Boolean isAuthenticated) {

        //only organizerId can update name and description, TODO cannot change organizerId
        if (!isAuthorized(actorId, getOrganizerIdByGroupId(id))) {
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
    @Path("/Group/{id}")
    public Response deleteGroup(@PathParam("id") long id,
                                @QueryParam("actorId") String actorId,
                                @Auth Boolean isAuthenticated) throws SQLException {

        // only organizer can delete the entry
        if (!isAuthorized(actorId, getOrganizerIdByGroupId(id))) {
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

    @GET
    @Path("/Group/{id}/User")
    public Response findUsersByGroupId(@PathParam("id") long id, @Auth Boolean isAuthenticated) {
        List<User> users = groupUserDAO.findUsersByGroupId(id);
        return Response.ok(users).build();
    }

    @DELETE
    @Path("/Group/{id}/User/{userId}")
    public Response deleteGroupUser(@PathParam("id") long groupId,
                                    @PathParam("userId") String userId,
                                    @QueryParam("actorId") String actorId,
                                    @Auth Boolean isAuthenticated) throws SQLException {

        String organizerId = getOrganizerIdByGroupId(groupId);

        //only organizer or self can remove from group
        if(!isAuthorized(actorId, organizerId)
                && !isAuthorized(actorId, userId)) {
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
    @Path("/Group/{id}/User/{userId}")
    public Response createGroupUser(@PathParam("id") long groupId,
                                    @PathParam("userId") String userId,
                                    @QueryParam("actorId") String actorId,
                                    @Auth Boolean isAuthenticated) throws URISyntaxException {

        //only organizer can add to group TODO maybe only allow organizer?
        if(!isAuthorized(actorId, getOrganizerIdByGroupId(groupId)) && !isAuthorized(actorId, userId)) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }

        groupUserDAO.createGroupUser(groupId, userId);
        return Response.created(new URI(String.format("/%d/GroupUser/%s", groupId, userId))).build();
    }

    @POST
    @Path("/Group/{id}/User")
    public Response createGroupUser(final List<String> userIds,
                                    @PathParam("id") long groupId,
                                    @QueryParam("actorId") String actorId,
                                    @Auth Boolean isAuthenticated) throws URISyntaxException {

        //only organizer can batch add
        if(!isAuthorized(actorId, getOrganizerIdByGroupId(groupId))) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }

        List<GroupUser> groupUsers = new ArrayList<GroupUser>();
        for(String userId : userIds) {
            groupUsers.add(new GroupUser(groupId, userId));
        }
        groupUserDAO.createGroupUserBatch(groupUsers);
        return Response.created(new URI(String.format("/%d/GroupUser/%d", groupId, groupUsers.size()))).build();
    }
}
