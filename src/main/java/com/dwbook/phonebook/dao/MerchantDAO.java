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

import com.dwbook.phonebook.dao.mappers.MerchantMapper;
import com.dwbook.phonebook.dao.mappers.OrderMerchantMapper;
import com.dwbook.phonebook.representations.Merchant;
import com.dwbook.phonebook.representations.OrderMerchant;

/**
 * Created by howard on 10/20/14.
 */

/**
 * In use:
 * 		getAllActiveMerchant					AdminResource
 * 		getAllMerchant							AdminResource
 * 		getMerchantById						MerchantResource
 * 		getMerchantByName				MerchantResource
 * 		createMerchant							MerchantResource
 * 		updateMerchant						MerchantResource
 * Not in use:
 */

public interface MerchantDAO extends Transactional<MerchantDAO> {

    @Mapper(MerchantMapper.class)
    @SqlQuery("select * from Merchant where deletedOn is null")
    List<Merchant> getAllActiveMerchant();
    
    @Mapper(MerchantMapper.class)
    @SqlQuery("select * from Merchant")
    List<Merchant> getAllMerchant();

	//only show ones that did not have a deletedOn time stamp
    @Mapper(MerchantMapper.class)
    @SqlQuery("select * from Merchant where id = :id and deletedOn is null")
    Merchant getMerchantById(@Bind("id") long id);
    
    @Mapper(MerchantMapper.class)
    @SqlQuery("select * from Merchant where name = :name and deletedOn is null")
    Merchant getMerchantByName(@Bind("name") String name);


    @Transaction
    @GetGeneratedKeys
    @SqlUpdate("insert into Merchant (id, name, branch, description, address, latitude, longitude, deliverDistanceKm, minimumOrder, minimumDelivery, mainPhone, mobilePhone, orderSubmissionJson, imageJson, feedbackJson, createdOn) "
    		+ "values (NULL, :name, :branch, :description, :address,:latitude, :longitude, :deliverDistanceKm, :minimumOrder, :minimumDelivery, :mainPhone, :mobilePhone, :orderSubmissionJson, :imageJson, :feedbackJson, UNIX_TIMESTAMP()) ")
    long createMerchant(@Bind("id") long id, @Bind("name") String name, @Bind("branch") String branch, @Bind("description") String description,
			@Bind("address") String address, @Bind("latitude") float latitude, @Bind("longitude") float longitude,
			@Bind("deliverDistanceKm") int deliverDistanceKm, @Bind("minimumOrder") float minimumOrder, @Bind("minimumDelivery") float minimumDelivery,
			@Bind("mainPhone") String mainPhone, @Bind("mobilePhone") String mobilePhone, @Bind("orderSubmissionJson") String orderSubmissionJson,
			@Bind("imageJson") String imageJson, @Bind("feedbackJson") String feedbackJson);

    @Transaction
    @SqlUpdate("update Merchant set name=:name, branch=:branch, description=:description, address=:address, latitude=:latitude, longitude=:longitude, deliverDistanceKm=:deliverDistanceKm, minimumOrder=:minimumOrder,"
    		+ " minimumDelivery=:minimumDelivery, mainPhone=:mainPhone, mobilePhone=:mobilePhone, orderSubmissionJson=:orderSubmissionJson, imageJson=:imageJson, feedbackJson=:feedbackJson, updatedOn=UNIX_TIMESTAMP() where id=:id")
	void updateMerchant(@Bind("id") int id, @Bind("name") String name, @Bind("branch") String branch, @Bind("description") String description,
			@Bind("address") String address, @Bind("latitude") float latitude, @Bind("longitude") float longitude,
			@Bind("deliverDistanceKm") int deliverDistanceKm, @Bind("minimumOrder") float minimumOrder, @Bind("minimumDelivery") float minimumDelivery,
			@Bind("mainPhone") String mainPhone, @Bind("mobilePhone") String mobilePhone, @Bind("orderSubmissionJson") String orderSubmissionJson,
			@Bind("imageJson") String imageJson, @Bind("feedbackJson") String feedbackJson);



}