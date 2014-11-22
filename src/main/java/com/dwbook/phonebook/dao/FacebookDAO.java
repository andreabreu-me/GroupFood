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

import com.dwbook.phonebook.dao.mappers.FacebookMapper;
import com.dwbook.phonebook.representations.Facebook;

/**
 * Created by howard on 10/12/14.
 */

/**
 * In use:
 * 		getAllActiveFacebook					AdminResource
 * 		getAllFacebook								AdminResource
 * 		getFacebookByUserId					FacebookResource
 * 		createFacebook								UserResource
 * 		updateFacebookByUserId			FacebookResource
 * 	    updateFacebookTokenByUserId	    FacebookResource
 * 		deleteFacebookByUserId				AdminResource(FacebookResource)
 * Not in use: 
 * 		batchCreateFacebook
 * 		getFacebookById
 * 		deleteFacebook
 */

public interface FacebookDAO extends Transactional<FacebookDAO> {

	//only show ones that did not have a deletedOn time stamp
    @Mapper(FacebookMapper.class)
    @SqlQuery("select * from Facebook where deletedOn is null")
    List<Facebook> getAllActiveFacebook();
    
    @Mapper(FacebookMapper.class)
    @SqlQuery("select * from Facebook")
    List<Facebook> getAllFacebook();
    
    @Mapper(FacebookMapper.class)
    @SqlQuery("select * from Facebook where userId = :userId and deletedOn is null")
	Facebook getFacebookByUserId(@Bind("userId") String userId);
    
    @GetGeneratedKeys
    @Transaction
    @SqlUpdate("insert into Facebook (id, userId, token, firstName, lastName, email, imageJson, createdOn) values (:id, :userId, :token, :firstName, :lastName, :email, :imageJson, UNIX_TIMESTAMP())")
    int createFacebook(@Bind("id") String id, @Bind("userId") String userId, @Bind("token") String token, @Bind("firstName") String firstName, @Bind("lastName") String lastName, @Bind("email") String email, @Bind("imageJson") String imageJson);

	//only modify ones that did not have a deletedOn time stamp
    //not allow updating id, userId, and token
    @Transaction
    @SqlUpdate("update Facebook set token = :token, firstName = :firstName, lastName=:lastName, email=:email, imageJson= :imageJson, updatedOn = UNIX_TIMESTAMP() where userId= :userId and deletedOn is null")
    void updateFacebookByUserId(@Bind("userId") String userId, @Bind("token") String token, @Bind("firstName") String firstName, @Bind("lastName") String lastName, @Bind("email") String email, @Bind("imageJson") String imageJson);

    @Transaction
    @SqlUpdate("update Facebook set token = :token, updatedOn = UNIX_TIMESTAMP() where userId = :userId and deletedOn is null")
    void updateFacebookTokenByUserId(@Bind("userId") String userId, @Bind("token") String token);

    @Transaction
    @SqlUpdate("update Facebook set deletedOn=:UNIX_TIMESTAMP() where userId = :userId and deletedOn is null")
	void deleteFacebookByUserId(@Bind("userId") String userId);

    /*
     *  below are methods that are not used or need to be updated.
     */
	
    //!!not touched as I am not sure what to do with this yet
    @Transaction
    @SqlBatch("insert into Facebook (id, userId, token) values (:it.id, :it.userId, :it.token)")
    @BatchChunkSize(1000)
    public int[] batchCreateFacebook(@BindBean("it") Iterable<Facebook> its);
    
	//only show ones that did not have a deletedOn time stamp
    @Mapper(FacebookMapper.class)
    @SqlQuery("select * from Facebook where id = :id and deletedOn is null")
    Facebook getFacebookById(@Bind("id") String id);
    
	//update data with a deletedOn time stamp without actually deleting it
    @Transaction
    @SqlUpdate("update Facebook set deletedOn=:UNIX_TIMESTAMP() where id = :id and deleteOn is null")
    void deleteFacebook(@Bind("id") String id);
}
