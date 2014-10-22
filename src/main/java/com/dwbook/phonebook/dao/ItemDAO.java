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
    @SqlQuery("select * from Item where merchantId=:merchantId, deletedOn is null")
	List<Item> getItemByMerchantId(int merchantId);

	//only show ones that did not have a deletedOn time stamp
    @Mapper(ItemMapper.class)
    @SqlQuery("select * from Item where id = :id and deletedOn is null")
    Item getItemById(@Bind("id") int id);

    @Mapper(ItemMapper.class)
    @SqlQuery("select * from Item where merchantId = :merchantId, deletedOn is null order by weight desc")
	List<Item> getItemByDescendingOrder(int merchantId);
	
    @Transaction
    @GetGeneratedKeys
    @SqlUpdate("insert into Item (id, merchantId, title, description, unitPrice, limit, weight, imageJson, feedbackJson, createdOn) "
    		+ "values (:it.id, :it.merchantId, :it.title, :it.description, :it.unitPrice, :it.limit, :it.weight, :it.imageJson, :it.feedbackJson, UNIX_TIMESTAMP()) on duplication key update"
    		+ "merchantId=:it.merchantId, title=:it.title, description=:it.description, unitPrice=:it.unitPrice, limit=:it.limit, weight=:it.weight, imageJson=:it.imageJson, feedbackJson=:it.feedbackJson, updatedOn = UNIX_TIMESTAMP(), deletedOn = null")
    int[] batchCreateItem(@BindBean("it") Iterable<Item> its);

    @Transaction
    @SqlUpdate("update Item set merchantId=:merchantId, title=:title, description=:description, unitPrice=:unitPrice, limit=:limit, weight=:weight, imageJson=:imageJson, feedbackJson=:feedbackJson, updated_on = UNIX_TIMESTAMP() where id= :id and deletedOn is null")
	void updateItem(@Bind("id") int id, @Bind("merchantId") int merchantId, @Bind("title") String title, @Bind("description") String description,
			@Bind("unitPrice") float unitPrice, @Bind("limit") int limit, @Bind("weight") int weight, @Bind("imageJson") String imageJson, @Bind("feedbackJson") String feedbackJson);
    
    @Transaction
    @SqlUpdate("update Item set deletedOn=:UNIX_TIMESTAMP() where merchantId = :merchantId and deletedOn is null")
	void deleteItemByMerchantId(@Bind("merchantId") int merchantId);

    @Transaction
    @SqlUpdate("update Item set deletedOn=:UNIX_TIMESTAMP() where id = :id and deletedOn is null")
    void deleteItem(@Bind("id") int id);



}