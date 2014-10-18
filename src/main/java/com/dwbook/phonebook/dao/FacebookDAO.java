package com.dwbook.phonebook.dao;

import com.dwbook.phonebook.dao.mappers.FacebookMapper;
import com.dwbook.phonebook.representations.Facebook;

import org.skife.jdbi.v2.TransactionIsolationLevel;
import org.skife.jdbi.v2.sqlobject.*;
import org.skife.jdbi.v2.sqlobject.customizers.BatchChunkSize;
import org.skife.jdbi.v2.sqlobject.customizers.Mapper;
import org.skife.jdbi.v2.sqlobject.mixins.Transactional;

import java.util.List;

/**
 * Created by howard on 10/12/14.
 */
public interface FacebookDAO extends Transactional<FacebookDAO> {

	//only show ones that did not have a deletedOn time stamp
    @Mapper(FacebookMapper.class)
    @SqlQuery("select * from Facebook where deletedOn is null")
    List<Facebook> getAllFacebook();
    
	//only show ones that did not have a deletedOn time stamp
    @Mapper(FacebookMapper.class)
    @SqlQuery("select * from Facebook where id = :id and deletedOn is null")
    Facebook getFacebookById(@Bind("id") String id);

    @Mapper(FacebookMapper.class)
    @SqlQuery("select * from Facebook where userId = :userId and deletedOn is null")
	Facebook getFacebookByUserId(@Bind("userId") String userId);
    
    @GetGeneratedKeys
    @Transaction
    @SqlUpdate("insert into Facebook (id, userId, token, firstName, lastName, email, createdOn) values (:id, :userId, :token, :firstName, :lastName, :email, UNIX_TIMESTAMP())")
    int createFacebook(@Bind("id") String id, @Bind("userId") String userId, @Bind("token") String token, @Bind("firstName") String firstName, @Bind("lastName") String lastName, @Bind("email") String email);

	//only modify ones that did not have a deletedOn time stamp
    //not allow updating id, userId, and token
    @Transaction
    @SqlUpdate("update Facebook set firstName = :firstName, lastName=:lastName, email=:email, updatedOn = UNIX_TIMESTAMP() where id= :id and deletedOn is null")
    void updateFacebook(@Bind("id") String id, @Bind("firstName") String firstName, @Bind("lastName") String lastName, @Bind("email") String email);

	//update data with a deletedOn time stamp without actually deleting it
    @Transaction(TransactionIsolationLevel.REPEATABLE_READ)
    @SqlUpdate("update Facebook set deletedOn=:UNIX_TIMESTAMP() where id = :id and deleteOn is null")
    void deleteFacebook(@Bind("id") String id);

    //!!not touched as I am not sure what to do with this yet
    @Transaction
    @SqlBatch("insert into Facebook (id, userId, token) values (:it.id, :it.userId, :it.token)")
    @BatchChunkSize(1000)
    public int[] batchCreateFacebook(@BindBean("it") Iterable<Facebook> its);


    
}
