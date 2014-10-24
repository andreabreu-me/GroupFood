package com.dwbook.phonebook.resources;

import com.dwbook.phonebook.dao.GroupUserDAO;
import com.dwbook.phonebook.dao.MessageDAO;
import com.dwbook.phonebook.representations.Message;
import com.dwbook.phonebook.representations.MessageUser;
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
 * Created by curtishu on 10/22/14.
 */
@Path("/Message")
@Produces(MediaType.APPLICATION_JSON + "; charset=utf-8")
public class MessageResource {

    //TODO use configuration to determine default values
    private static final long defaultNewerThan = 0;
    private static final int defaultLimit = 20;

    final static Logger logger = LoggerFactory.getLogger(MessageResource.class);
    private final DBI jdbi;
    private final MessageDAO messageDAO;
    private final GroupUserDAO groupUserDAO;

    public MessageResource(DBI jdbi) {
        this.jdbi = jdbi;
        this.messageDAO = jdbi.onDemand(MessageDAO.class);
        this.groupUserDAO = jdbi.onDemand(GroupUserDAO.class);
    }

    public boolean isAuthorized(String actor, long group) {
        return actor != null && !actor.isEmpty() && groupUserDAO.checkGroupUser(group, actor) != null;
    }

    //post message in group for user
    @POST
    @Path("/{groupId}")
    public Response createMessage(Message m,
                                @PathParam("groupId") long groupId,
                                @QueryParam("actorId") String actorId,
                                @Auth Boolean isAuthenticated) throws URISyntaxException, SQLException {

        //check group user membership before allowing to post
        if(!isAuthorized(actorId, groupId)) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }

        Handle handle = jdbi.open();
        handle.getConnection().setAutoCommit(false);
        try {
            handle.begin();
            MessageDAO mdao = handle.attach(MessageDAO.class);
            //create message and mark it as read for the author
            long messageId = mdao.createMessage(groupId, actorId, m.getContent());
            mdao.createMessageUser(messageId, actorId);
            handle.commit();
            return Response.created(new URI(String.valueOf(messageId))).build();
        } catch (Exception e) {
            handle.rollback();
            e.printStackTrace();
            return Response.serverError().build();
        }
    }

    //mark on message as read
    @POST
    @Path("/{groupId}/{messageId}")
    public Response createMessageUser(@PathParam("groupId") long groupId,
                                      @PathParam("messageId") long messageId,
                                      @QueryParam("actorId") String actorId,
                                      @Auth Boolean isAuthenticated) {

        //check group user membership before allowing to mark messages as read
        if(!isAuthorized(actorId, groupId)) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }
        messageDAO.createMessageUser(messageId, actorId);
        return Response.ok().build();
    }

    //batch mark messages as read
    @POST
    @Path("/{groupId}/BatchRead")
    public Response createMessageUserBatch(List<Message> messages,
                                           @PathParam("groupId") long groupId,
                                           @QueryParam("actorId") String actorId,
                                           @Auth Boolean isAuthenticated) {

        //check group user membership before allowing to mark messages as read
        if(!isAuthorized(actorId, groupId)) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }

        List<MessageUser> mu = new ArrayList<MessageUser>();
        for(Message m : messages) {
            mu.add(new MessageUser(m.getId(), actorId));
        }

        messageDAO.createMessageUserBatch(mu);
        return Response.ok().build();
    }

    //get messages in this group for user, param (limit, newerThan or both)
    @GET
    @Path("/{groupId}")
    public Response findMessages(@PathParam("groupId") long groupId,
                                 @QueryParam("newerThan") long newerThan,
                                 @QueryParam("limit") int limit,
                                 @QueryParam("actorId") String actorId,
                                 @Auth Boolean isAuthenticated) {

        //check group user membership before allowing to post
        if(!isAuthorized(actorId, groupId)) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }
        long nt = (newerThan <= 0 ? defaultNewerThan : newerThan);
        int lm = (limit <= 0 ? defaultLimit : limit);
        List<Message> messages = messageDAO.findMessagesInGroupForUserByTimeAndLimit(groupId, actorId, nt, lm);
        return Response.ok(messages).build();
    }
}