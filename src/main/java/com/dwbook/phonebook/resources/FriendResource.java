package com.dwbook.phonebook.resources;
import io.dropwizard.auth.Auth;

import java.util.List;

import javax.ws.rs.GET;
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

/*@GET 	
 * 			User/{userId}/Friend												return all Friend where userId=userId
 * 			User/{userId}/Friend/{friendId}							return Friend where friendId=friendId
 * 			User/{userId}/Friend/{socialNetwork}					return Friend where socialNetwork=socialNetwork
 * 			User/{userId}/Friend/{relationship}					return Friend where relationship=relationship
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
    	List<Friend> friend = friendDao.getFriendByUserId(userId);
        return Response
                .ok(friend)
                .build();
    }
    
    @GET
    @Path("/{friendId}")
    public Response getFriendByFriendId(@PathParam("userId") String userId, @PathParam("friendId") String friendId, @Auth Boolean isAuthenticated) {
    	List<Friend>  friend = friendDao.getFriendByFriendId(userId, friendId);
        return Response
                .ok(friend)
                .build();
    }   
    /*
    @GET
    @Path("/{socialNetwork}")
    public Response getFriendBySocialNetwork(@PathParam("userId") String userId, @PathParam("socialNetwork") String socialNetwork, @Auth Boolean isAuthenticated) {
    	List<Friend>  friend = friendDao.getFriendBySocialNetwork(userId, socialNetwork);
        return Response
                .ok(friend)
                .build();
    }   
    
    @GET
    @Path("/{relationship}")
    public Response getFriendByRelationship(@PathParam("userId") String userId, @PathParam("relationship") String relationship, @Auth Boolean isAuthenticated) {
    	List<Friend>  friend = friendDao.getFriendByRelationship(userId, relationship);
        return Response
                .ok(friend)
                .build();
    }   */
}
