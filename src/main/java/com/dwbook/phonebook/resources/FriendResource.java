package com.dwbook.phonebook.resources;
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

import com.dwbook.phonebook.dao.FriendDAO;
import com.dwbook.phonebook.representations.Friend;

/**
 * Created by howard on 10/17/14.
 */

@Path("/User/{userId}/Friend")
@Produces(MediaType.APPLICATION_JSON + "; charset=utf-8")
public class FriendResource {

    final static Logger logger = LoggerFactory.getLogger(FriendResource.class);
    private final FriendDAO friendDao;

    public FriendResource(DBI jdbi) {
        friendDao = jdbi.onDemand(FriendDAO.class);
    }

    @GET
    public Response getFriendByUserId(@PathParam("userId") String userId, @Auth Boolean isAuthenticated) {
        //logger.info(String.format("%s retrieved", userId));
        // retrieve information about the friend with the provided id
        Friend friend = friendDao.getFriendByUserId(userId);
        return Response
                .ok(friend)
                .build();
    }
    
    @GET
    @Path("/{friendId}")
    public Response getFriendByFriendId(@PathParam("friendId") String friendId, @Auth Boolean isAuthenticated) {
        //logger.info(String.format("%s retrieved", userId));
        // retrieve information about the friend with the provided id
        Friend friend = friendDao.getFriendByFriendId(friendId);
        return Response
                .ok(friend)
                .build();
    }   
    
    @POST
    public Response createFriend(Friend friend, @Auth Boolean isAuthenticated) throws URISyntaxException {
        // store the new friend
        int newFriendId = friendDao.createFriend(friend.getUserId(), friend.getFriendId(), friend.getSocialNetwork(), friend.getRelationship());
        return Response.created(new URI(String.valueOf(newFriendId))).build();
    }

    @POST
    @Path("/batch_create")
    public Response batchCreateFriend(List<Friend> friend, @Auth Boolean isAuthenticated) throws URISyntaxException {
        int[] ids = friendDao.batchCreateFriend(friend);
        return Response.created(new URI(String.valueOf(ids.length))).build();
    }
}
