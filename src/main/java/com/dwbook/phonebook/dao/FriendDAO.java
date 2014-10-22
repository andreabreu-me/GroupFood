package com.dwbook.phonebook.dao;

import java.util.List;

import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.BindBean;
import org.skife.jdbi.v2.sqlobject.GetGeneratedKeys;
import org.skife.jdbi.v2.sqlobject.SqlBatch;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.Transaction;
import org.skife.jdbi.v2.sqlobject.customizers.BatchChunkSize;
import org.skife.jdbi.v2.sqlobject.customizers.Mapper;
import org.skife.jdbi.v2.sqlobject.mixins.Transactional;

import com.dwbook.phonebook.dao.mappers.FriendMapper;
import com.dwbook.phonebook.representations.Friend;

/**
 * Created by howard on 10/17/14.
 */

/**
 * In use:
 * 		getAllActiveFriend						AdminResource
 * 		getAllFriend								AdminResource
 * 		getFriendByUserId					FriendResource
 * 		getFriendByFriendId					FriendResource
 * 		getFriendBySocialNetwork		FriendResource
 * 		getFriendByRelationship			FriendResource
 * 		batchCreateFriend						UserResource
 * 		deleteFriendByUserId				AdminResource
 * 		batchDelete								FacebookResource
 * 		updateFriend								FacebookResource
 * Not in use:
 * 		createFriend
 * 		deleteFacebookFriendByUserId		FacebookResource
 */

public interface FriendDAO extends Transactional<FriendDAO> {
    
    @Mapper(FriendMapper.class)
    @SqlQuery("select * from Friend where deletedOn is null")
    List<Friend> getAllActiveFriend();
    
    @Mapper(FriendMapper.class)
    @SqlQuery("select * from Friend")
    List<Friend> getAllFriend();
    
    @Mapper(FriendMapper.class)
    @SqlQuery("select * from Friend where userId = :userId and deletedOn is null")
	List<Friend> getFriendByUserId(@Bind("userId") String userId);
    
    @Mapper(FriendMapper.class)
    @SqlQuery("select * from Friend where userId = :userId and friendId = :friendId and deletedOn is null")
	Friend getFriendByFriendId(@Bind("userId") String userId, @Bind("friendId")String friendId);
    
    @Mapper(FriendMapper.class)
    @SqlQuery("select * from Friend where userId = :userId and socialNetwork = :socialNetwork and deletedOn is null")
	List<Friend> getFriendBySocialNetwork(@Bind("userId") String userId, @Bind("socialNetwork")String socialNetwork);
    
    @Mapper(FriendMapper.class)
    @SqlQuery("select * from Friend where userId = :userId and relationship = :relationship and deletedOn is null")
	List<Friend> getFriendByRelationship(@Bind("userId") String userId, @Bind("relationship") String relationship);
    
    //!!not touched as I am not sure what to do with this yet
    @Transaction
    @SqlBatch("insert into Friend (userId, friendId, socialNetwork, relationship, createdOn) values (:userId, :it.friendId, :it.socialNetwork, :it.relationship, UNIX_TIMESTAMP())")
    @BatchChunkSize(1000)
    public int[] batchCreateFriend(@Bind("userId") String userId, @BindBean("it") Iterable<Friend> its);

    @Transaction
    @SqlUpdate("update Friend set deletedOn=UNIX_TIMESTAMP() where (userId = :userId or friendId=:userId) and deletedOn is null")
	void deleteFriendByUserId(@Bind("userId") String userId);

    @Transaction
    @SqlBatch("update Friend set deletedOn=UNIX_TIMESTAMP() where userId = :userId and friendId = :it.friendId and socialNetwork=:it.socialNetwork and deletedOn is null")
	void batchDelete(@Bind("userId") String userId, @BindBean("it") Iterable<Friend> its);
	
    @Transaction
    @SqlBatch("insert into Friend (userId, friendId, socialNetwork, relationship, createdOn) values (:userId, :it.friendId, :it.socialNetwork, :it.relationship, UNIX_TIMESTAMP()) on duplicate key update relationship = :it.relationship, updatedOn = UNIX_TIMESTAMP(), deletedOn = null ")
	void updateFriend(@Bind("userId") String userId, @BindBean("it") Iterable<Friend> its);
	
    /*
     *  below are methods that are not used or need to be updated.
     */
    @GetGeneratedKeys
    @Transaction
    @SqlUpdate("insert into Friend (userId, friendId, socialNetwork, relationship, createdOn) values (:userId, :friendId, :socialNetwork, :relationship, UNIX_TIMESTAMP())")
    int createFriend(@Bind("userId") String userId, @Bind("friendId") String friendId, @Bind("socialNetwork") String socialNetwork, @Bind("relationship") String relationship);
    
    @Transaction
    @SqlUpdate("update Friend set deletedOn=:UNIX_TIMESTAMP() where userId = :userId and deleteOn is null")
	void deleteFacebookFriendByUserId(@Bind("userId") String userId);



}
