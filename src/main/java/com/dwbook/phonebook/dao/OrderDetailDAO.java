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

import com.dwbook.phonebook.dao.mappers.OrderDetailMapper;
import com.dwbook.phonebook.representations.OrderDetail;

/**
 * Created by howard on 10/28/14.
 */

/**
 * In use:
 * 		getAllActiveOrderDetail							AdminResource
 * 		getAllOrderDetail									AdminResource
 * 		deleteByOrderId									OrderResource
 * 		deleteByUserIdAndOrderId					OrderUserResource		
 * 		deleteByMerchantIdAndOrderId			OrderMerchantResource
 * 		getOrderDetailByUserIdAndOrderId	OrderDetailResource
 * 		getOrderDetailByOrderId						OrderDetailResource
 * 		createOrderDetail									OrderDetailResource
 * 		updateOrderDetail									OrderDetailResource
 * 		deleteBySelectedOrderDetails				OrderDetailResource
 *      batchCreateOrderDetail                      OrderResource
 */

public interface OrderDetailDAO extends Transactional<OrderDetailDAO> {

    @Mapper(OrderDetailMapper.class)
    @SqlQuery("select * from `OrderDetail` where deletedOn is null")
    List<OrderDetail> getAllActiveOrderDetail();
    
    @Mapper(OrderDetailMapper.class)
    @SqlQuery("select * from `OrderDetail`")
    List<OrderDetail> getAllOrderDetail();

    @Transaction
    @SqlUpdate("update `OrderDetail` set deletedOn=UNIX_TIMESTAMP() where orderId=:orderId and deletedOn is null")
	void deleteByOrderId(@Bind("orderId") long id);

    @Transaction
    @SqlUpdate("update `OrderDetail` set deletedOn=UNIX_TIMESTAMP() where orderId=:orderId and userId=:userId and deletedOn is null")
	void deleteByUserIdAndOrderId(@Bind("userId") String userId, @Bind("orderId")  long orderId);

    @Transaction
    @SqlUpdate("update `OrderDetail` set deletedOn=UNIX_TIMESTAMP() where orderId=:orderId and merchantId=:merchantId and deletedOn is null")
	void deleteByMerchantIdAndOrderId(@Bind("merchantId") long merchantId, @Bind("orderId")  long orderId);

    @Mapper(OrderDetailMapper.class)
    @SqlQuery("select * from `OrderDetail` where userId=:userId and orderId=:orderId and deletedOn is null")
	List<OrderDetail> getOrderDetailByUserIdAndOrderId(@Bind("userId") String userId, @Bind("orderId")  long orderId);

    @Mapper(OrderDetailMapper.class)
    @SqlQuery("select * from `OrderDetail` where orderId=:orderId and deletedOn is null")
	List<OrderDetail> getOrderDetailByOrderId(@Bind("orderId")  long orderId);

    @Transaction
    @GetGeneratedKeys
    @SqlUpdate("insert into `OrderDetail` (userId, orderId, merchantId, itemId, quantity, status, createdOn) values (:orderDetail.userId, :orderDetail.orderId, :orderDetail.merchantId, :orderDetail.itemId, :orderDetail.quantity, :orderDetail.status, UNIX_TIMESTAMP()) on duplicate key update userId=:orderDetail.userId,orderId=:orderDetail.orderId,merchantId=:orderDetail.merchantId,itemId=:orderDetail.itemId,quantity=:orderDetail.quantity,status=:orderDetail.status,updatedOn=UNIX_TIMESTAMP(),deletedOn=null")
	int createOrderDetail(@BindBean("orderDetail") OrderDetail orderDetail);

    @Transaction
    @SqlUpdate("update `OrderDetail` set quantity=:orderDetail.quantity, status=:orderDetail.status, updatedOn=UNIX_TIMESTAMP() where userId=:orderDetail.userId and orderId=:orderDetail.orderId and merchantId=:orderDetail.merchantId and itemId=:orderDetail.itemId and deletedOn is null")
	void updateOrderDetail(@BindBean("orderDetail")OrderDetail orderDetail);
	
    @Transaction
    @SqlBatch("update `OrderDetail` set deletedOn=UNIX_TIMESTAMP() where userId=:it.userId and orderId=:it.orderId and merchantId=:it.merchantId and itemId=:it.itemId and deletedOn is null")
    @BatchChunkSize(1000)
	void deleteBySelectedOrderDetails(@BindBean("it") List<OrderDetail> orderDetail);


    @Transaction
    @SqlBatch("insert into `OrderDetail` (userId, orderId, merchantId, itemId, quantity, status, createdOn) values (:it.userId, :it.orderId, :it.merchantId, :it.itemId, :it.quantity, :it.status, UNIX_TIMESTAMP()) on duplicate key update userId=:it.userId,orderId=:it.orderId,merchantId=:it.merchantId,itemId=:it.itemId,quantity=:it.quantity,status=:it.status,updatedOn=UNIX_TIMESTAMP(),deletedOn=null")
    @BatchChunkSize(1000)
    int[] batchCreateOrderDetail(@BindBean("it") List<OrderDetail> its);
}