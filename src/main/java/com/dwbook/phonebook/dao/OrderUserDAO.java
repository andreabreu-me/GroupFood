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

import com.dwbook.phonebook.dao.mappers.OrderUserMapper;
import com.dwbook.phonebook.representations.OrderUser;

/**
 * Created by howard on 10/23/14.
 */

/**
 * In use:
 * 		getAllActiveOrderUser						AdminResource
 * 		getAllOrderUser									AdminResource
 * 		getOrderByUserId								OrderUserResource
 * 		getUserByOrderId								OrderUserResource
 * 		updateOrderUser								OrderUserResource
 * 		leaveOrder											OrderUserResource
 * 		batchCreateOrderUser						OrderUserResource
 * 		deleteByOrderId								OrderResource
 */

public interface OrderUserDAO extends Transactional<OrderUserDAO> {

    @Mapper(OrderUserMapper.class)
    @SqlQuery("select * from `OrderUser` where deletedOn is null")
    List<OrderUser> getAllActiveOrderUser();
    
    @Mapper(OrderUserMapper.class)
    @SqlQuery("select * from `OrderUser`")
    List<OrderUser> getAllOrderUser();

    @Mapper(OrderUserMapper.class)
    @SqlQuery("select * from `OrderUser` where userId = :userId and status = 'added' and deletedOn is null")
    List<OrderUser> getOrderByUserId(@Bind("userId") String userId);
    
    @Mapper(OrderUserMapper.class)
    @SqlQuery("select * from `OrderUser` where orderId = :orderId and status = 'added' and deletedOn is null")
    List<OrderUser> getUserByOrderId(@Bind("orderId") int orderId);

    @Transaction
    @SqlUpdate("update `OrderUser` set deletedOn=UNIX_TIMESTAMP() where userId = :userId and orderId=:orderId and deletedOn is null")
    void leaveOrder(@Bind("orderId") int orderId, @Bind("userId") String userId);

    @Transaction
    @SqlBatch("insert into `OrderUser` (orderId, userId, status, createdOn) values (:it.orderId, :it.userId, :it.status, UNIX_TIMESTAMP()) on duplicate key update status=:it.status, updatedOn=UNIX_TIMESTAMP(), deletedOn=null")
    @BatchChunkSize(1000)
    public int[] batchCreateOrderUser(@BindBean("it") List<OrderUser> its);

    @Transaction
    @SqlUpdate("update `OrderUser` set status=:orderUser.status, updateOn=UNIX_TIMESTAMP() where orderId=:orderUser.orderId and userId=:orderUser.userId and deletedOn is null")
	void updateOrderUser(@BindBean("orderUser") OrderUser orderUser);
    
    @Transaction
    @SqlUpdate("update `OrderUser` set deletedOn=UNIX_TIMESTAMP() where orderId=:orderId and deletedOn is null")
	void deleteByOrderId(@Bind("orderId")int id);

}