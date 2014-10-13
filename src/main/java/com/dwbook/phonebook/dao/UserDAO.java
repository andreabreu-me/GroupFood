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

	//only show ones that did not have a deleted_on time stamp
    @Mapper(UserMapper.class)
    @SqlQuery("select * from user where deleted_on is null")
    List<User> getAllUser();

	//only show ones that did not have a deleted_on time stamp
    @Mapper(UserMapper.class)
    @SqlQuery("select * from user where id = :id and deleted_on is null")
    User getUserById(@Bind("id") String id);

    @GetGeneratedKeys
    @SqlUpdate("insert into user (id, facebook_id, googleplus_id, created_on) values (:id, :facebook_id, :googleplus_id, :created_on)")
    int createUser(@Bind("id") String id, @Bind("facebook_id") String facebook_id, @Bind("googleplus_id") String googleplus_id, @Bind("created_on") String timeStamp);

	//only modify ones that did not have a deleted_on time stamp
    @Transaction(TransactionIsolationLevel.REPEATABLE_READ)
    @SqlUpdate("update user set facebook_id = :facebook_id, googleplus_id=:googleplus_id, updated_on = :updated_on where id= :id and deleted_on is null")
    void updateUser(@Bind("id") String id, @Bind("facebook_id") String facebook_id, @Bind("googleplus_id") String googleplus_id, @Bind("updated_on") String timeStamp);

	//update data with a deleted_on time stamp without actually deleting it
    @SqlUpdate("update user set deleted_on=:deleted_on where id = :id")
    void deleteUser(@Bind("id") String id, @Bind("deleted_on")String timeStamp);

    //!!not touched as I am not sure what to do with this yet
    @SqlBatch("insert into user (id, facebook_id, googleplus_id) values (:it.id, :it.facebook_id, :it.googleplus_id)")
    @BatchChunkSize(1000)
    public int[] batchCreateUser(@BindBean("it") Iterable<User> its);
}
