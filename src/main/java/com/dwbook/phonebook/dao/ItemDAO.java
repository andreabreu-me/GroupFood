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

import com.dwbook.phonebook.dao.mappers.ItemMapper;
import com.dwbook.phonebook.representations.Item;

/**
 * Created by howard on 10/20/14.
 */

/**
 * In use:
 * 		getAllActiveItem							AdminResource
 * 		getAllItem									AdminResource
 * 		getItemByMerchantId				ItemResource
 * 		getItemById								ItemResource
 * 		getItemByDescendingOrder		ItemResource
 * 		batchCreateItem						ItemResource
 * 		updateItem								ItemResource
 * 		deleteItemByMerchantId			ItemResource
 * 		deleteItem									ItemResource
 * Not in use:
 */

public interface ItemDAO extends Transactional<ItemDAO> {

    @Mapper(ItemMapper.class)
    @SqlQuery("select * from Item where deletedOn is null")
    List<Item> getAllActiveItem();
    
    @Mapper(ItemMapper.class)
    @SqlQuery("select * from Item")
    List<Item> getAllItem();
    
    @Mapper(ItemMapper.class)
    @SqlQuery("select * from Item where merchantId=:merchantId and deletedOn is null")
	List<Item> getItemByMerchantId(@Bind("merchantId") long merchantId);

	//only show ones that did not have a deletedOn time stamp
    @Mapper(ItemMapper.class)
    @SqlQuery("select * from Item where id = :id and deletedOn is null")
    Item getItemById(@Bind("id") int id);

    @Mapper(ItemMapper.class)
    @SqlQuery("select * from Item where merchantId = :merchantId and deletedOn is null order by weight desc")
	List<Item> getItemByDescendingOrder(@Bind("merchantId") int merchantId);
	
    @Transaction
    @GetGeneratedKeys
    @SqlBatch("insert into Item (id, merchantId, title, description, unitPrice, dailyLimit, weight, imageJson, feedbackJson, createdOn) "
    		+ "values (NULL, :it.merchantId, :it.title, :it.description, :it.unitPrice, :it.dailyLimit, :it.weight, :it.imageJson, :it.feedbackJson, UNIX_TIMESTAMP())")
    int[] batchCreateItem(@BindBean("it") Iterable<Item> its);

    @Transaction
    @SqlUpdate("update Item set merchantId=:merchantId, title=:title, description=:description, unitPrice=:unitPrice, dailyLimit=:dailyLimit, weight=:weight, imageJson=:imageJson, feedbackJson=:feedbackJson, updatedOn = UNIX_TIMESTAMP() where id= :id and deletedOn is null")
	void updateItem(@Bind("id") long id, @Bind("merchantId") long merchantId, @Bind("title") String title, @Bind("description") String description,
			@Bind("unitPrice") float unitPrice, @Bind("dailyLimit") int dailyLimit, @Bind("weight") int weight, @Bind("imageJson") String imageJson, @Bind("feedbackJson") String feedbackJson);
    
    @Transaction
    @SqlUpdate("update Item set deletedOn=UNIX_TIMESTAMP() where merchantId = :merchantId and deletedOn is null")
	void deleteItemByMerchantId(@Bind("merchantId") long merchantId);

    @Transaction
    @SqlUpdate("update Item set deletedOn=UNIX_TIMESTAMP() where id = :id and deletedOn is null")
    void deleteItem(@Bind("id") long id);



}