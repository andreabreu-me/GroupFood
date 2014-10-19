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

import com.dwbook.phonebook.dao.mappers.UserMapper;
import com.dwbook.phonebook.representations.User;

/**
 * Created by howard on 10/12/14.
 */

/**
 * In use:
 * 		getAllActiveUser						AdminResource
 * 		getAllUser								AdminResource
 * 		getUserById							UserResource
 * 		createUser								UserResource
 * 		deleteUser								AdminResource
 * Not in use:
 * 		updateUser
 * 		batchCreateUser
 */

public interface UserDAO extends Transactional<UserDAO> {

    @Mapper(UserMapper.class)
    @SqlQuery("select * from User where deletedOn is null")
    List<User> getAllActiveUser();
    
    @Mapper(UserMapper.class)
    @SqlQuery("select * from User")
    List<User> getAllUser();

	//only show ones that did not have a deletedOn time stamp
    @Mapper(UserMapper.class)
    @SqlQuery("select * from User where id = :id and deletedOn is null")
    User getUserById(@Bind("id") String id);

    @Transaction
    @GetGeneratedKeys
    @SqlUpdate("insert into User (id, facebookId, googlePlusId, createdOn) values (:id, :facebookId, :googlePlusId, UNIX_TIMESTAMP())")
    int createUser(@Bind("id") String id, @Bind("facebookId") String facebookId, @Bind("googlePlusId") String googlePlusId);

	//update data with a deletedOn time stamp without actually deleting it
    @Transaction
    @SqlUpdate("update User set deletedOn=:UNIX_TIMESTAMP() where id = :id and deleteOn is null")
    void deleteUser(@Bind("id") String id);
    
    /*
     *  below are methods that are not used or need to be updated.
     */

	//only modify ones that did not have a deletedOn time stamp
    @Transaction
    @SqlUpdate("update User set facebookId = :facebookId, googlePlusId=:googlePlusId, updated_on = UNIX_TIMESTAMP() where id= :id and deletedOn is null")
    void updateUser(@Bind("id") String id, @Bind("facebookId") String facebookId, @Bind("googlePlusId") String googlePlusId);
    
    //!!not touched as I am not sure what to do with this yet
    @Transaction
    @SqlBatch("insert into User (id, facebookId, googlePlusId) values (:it.id, :it.facebookId, :it.googlePlusId)")
    @BatchChunkSize(1000)
    public int[] batchCreateUser(@BindBean("it") Iterable<User> its);
}