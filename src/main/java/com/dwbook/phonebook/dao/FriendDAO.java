package com.dwbook.phonebook.dao;

import com.dwbook.phonebook.dao.mappers.FriendMapper;
import com.dwbook.phonebook.representations.Friend;

import org.skife.jdbi.v2.TransactionIsolationLevel;
import org.skife.jdbi.v2.sqlobject.*;
import org.skife.jdbi.v2.sqlobject.customizers.BatchChunkSize;
import org.skife.jdbi.v2.sqlobject.customizers.Mapper;
import org.skife.jdbi.v2.sqlobject.mixins.Transactional;

import java.util.List;

/**
 * Created by howard on 10/17/14.
 */
public interface FriendDAO extends Transactional<FriendDAO> {
    
    @Mapper(FriendMapper.class)
    @SqlQuery("select * from Friend where userId = :userId and deletedOn is null")
	Friend getFriendByUserId(@Bind("userId") String userId);
    
    @GetGeneratedKeys
    @Transaction
    @SqlUpdate("insert into Friend (userId, friendId, socialNetwork, relationship, createdOn) values (:userId, :friendId, :socialNetwork, :relationship, UNIX_TIMESTAMP())")
    int createFriend(@Bind("userId") String userId, @Bind("friendId") String friendId, @Bind("socialNetwork") String socialNetwork, @Bind("relationship") String relationship);

    //!!not touched as I am not sure what to do with this yet
    @Transaction
    @SqlBatch("insert into Friend (userId, friendId, socialNetwork, relationship, createdOn) values (:it.userId, :it.friendId, :it.socialNetwork, :it.relationship, UNIX_TIMESTAMP())")
    @BatchChunkSize(1000)
    public int[] batchCreateFriend(@BindBean("it") Iterable<Friend> its);

    @Mapper(FriendMapper.class)
    @SqlQuery("select * from Friend where friendId = :friendId and deletedOn is null")
	Friend getFriendByFriendId(@Bind("friendId")String friendId);

}
