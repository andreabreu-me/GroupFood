package com.dwbook.phonebook.dao;

import java.util.List;

import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.BindBean;
import org.skife.jdbi.v2.sqlobject.SqlBatch;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.Transaction;
import org.skife.jdbi.v2.sqlobject.customizers.BatchChunkSize;
import org.skife.jdbi.v2.sqlobject.customizers.Mapper;
import org.skife.jdbi.v2.sqlobject.mixins.Transactional;

import com.dwbook.phonebook.dao.mappers.OrderMerchantMapper;
import com.dwbook.phonebook.representations.Order;
import com.dwbook.phonebook.representations.OrderMerchant;

/**
 * Created by howard on 10/24/14.
 */

/**
 * In use:
 * 		getAllActiveOrderMerchant						AdminResource
 * 		getAllOrderMerchant									AdminResource
 * 		getMerchantByOrderId								OrderMerchantResource
 * 		batchCreateOrderMerchant						OrderMerchantResource
 * 		deleteMerchant											OrderMerchantResource
 * 		deleteByOrderId										OrderResource
 */

public interface OrderMerchantDAO extends Transactional<OrderMerchantDAO> {

    @Mapper(OrderMerchantMapper.class)
    @SqlQuery("select * from `OrderMerchant` where deletedOn is null")
    List<OrderMerchant> getAllActiveOrderMerchant();
    
    @Mapper(OrderMerchantMapper.class)
    @SqlQuery("select * from `OrderMerchant`")
    List<OrderMerchant> getAllOrderMerchant();
    
    @Mapper(OrderMerchantMapper.class)
    @SqlQuery("select * from `OrderMerchant` where orderId = :orderId and deletedOn is null")
    List<OrderMerchant> getMerchantByOrderId(@Bind("orderId") long orderId);

    @Transaction
    @SqlUpdate("update `OrderMerchant` set deletedOn=UNIX_TIMESTAMP() where merchantId = :merchantId and orderId=:orderId and deletedOn is null")
    void deleteMerchant(@Bind("orderId") long orderId, @Bind("merchantId") long merchantId);

    @Transaction
    @SqlBatch("insert into `OrderMerchant` (orderId, merchantId, createdOn) values (:it.orderId, :it.merchantId, UNIX_TIMESTAMP()) on duplicate key update updatedOn=UNIX_TIMESTAMP(), deletedOn=null")
    @BatchChunkSize(1000)
    int[] batchCreateOrderMerchant(@BindBean("it") List<OrderMerchant> its);

    @Transaction
    @SqlUpdate("update `OrderMerchant` set deletedOn=UNIX_TIMESTAMP() where orderId=:orderId and deletedOn is null")
	void deleteByOrderId(@Bind("orderId") long orderId);


}