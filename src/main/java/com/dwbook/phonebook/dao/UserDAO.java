package com.dwbook.phonebook.dao;

import com.dwbook.phonebook.dao.mappers.UserMapper;
import com.dwbook.phonebook.representations.User;

import org.skife.jdbi.v2.TransactionIsolationLevel;
import org.skife.jdbi.v2.sqlobject.*;
import org.skife.jdbi.v2.sqlobject.customizers.BatchChunkSize;
import org.skife.jdbi.v2.sqlobject.customizers.Mapper;
import org.skife.jdbi.v2.sqlobject.mixins.Transactional;

import java.util.List;

/**
 * Created by howard on 10/12/14.
 */
public interface UserDAO extends Transactional<UserDAO> {

	//only show ones that did not have a deletedOn time stamp
    @Mapper(UserMapper.class)
    @SqlQuery("select * from user where deletedOn is null")
    List<User> getAllUser();

	//only show ones that did not have a deletedOn time stamp
    @Mapper(UserMapper.class)
    @SqlQuery("select * from User where id = :id and deletedOn is null")
    User getUserById(@Bind("id") String id);

    @GetGeneratedKeys
    @SqlUpdate("insert into User (id, facebookId, googlePlusId, createdOn) values (:id, :facebookId, :googlePlusId, UNIX_TIMESTAMP())")
    int createUser(@Bind("id") String id, @Bind("facebookId") String facebookId, @Bind("googlePlusId") String googlePlusId);

	//only modify ones that did not have a deletedOn time stamp
    @Transaction(TransactionIsolationLevel.REPEATABLE_READ)
    @SqlUpdate("update User set facebookId = :facebookId, googlePlusId=:googlePlusId, updated_on = UNIX_TIMESTAMP() where id= :id and deletedOn is null")
    void updateUser(@Bind("id") String id, @Bind("facebookId") String facebookId, @Bind("googlePlusId") String googlePlusId);

	//update data with a deletedOn time stamp without actually deleting it
    @SqlUpdate("update User set deletedOn=:UNIX_TIMESTAMP() where id = :id and deleteOn is null")
    void deleteUser(@Bind("id") String id);

    //!!not touched as I am not sure what to do with this yet
    @SqlBatch("insert into User (id, facebookId, googlePlusId) values (:it.id, :it.facebookId, :it.googlePlusId)")
    @BatchChunkSize(1000)
    public int[] batchCreateUser(@BindBean("it") Iterable<User> its);
}