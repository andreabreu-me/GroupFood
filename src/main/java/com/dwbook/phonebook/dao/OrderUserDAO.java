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
 * 		leaveOrder											OrderUserResource
 * 		batchCreateOrderUser						OrderResource
 */

public interface OrderUserDAO extends Transactional<OrderUserDAO> {

    @Mapper(OrderUserMapper.class)
    @SqlQuery("select * from `OrderUser` where deletedOn is null")
    List<OrderUser> getAllActiveOrderUser();
    
    @Mapper(OrderUserMapper.class)
    @SqlQuery("select * from `OrderUser`")
    List<OrderUser> getAllOrderUser();

	//only show ones that did not have a deletedOn time stamp
    @Mapper(OrderUserMapper.class)
    @SqlQuery("select orderId from `OrderUser` where userId = :userId and deletedOn is null")
    List<OrderUser> getOrderByUserId(@Bind("userId") String userId);
    
    @Mapper(OrderUserMapper.class)
    @SqlQuery("select userId from `OrderUser` where orderId = :orderId and deletedOn is null")
    OrderUser getUserByOrderId(@Bind("orderId") int orderId);

    @Transaction
    @SqlUpdate("update `OrderUser` set deletedOn=UNIX_TIMESTAMP() where userId = :userId and orderId=:orderId and deletedOn is null")
    void leaveOrder(@Bind("orderId") int orderId, @Bind("userId") String userId);

    @Transaction
    @SqlBatch("insert into `OrderUser` (orderId, userId, createdOn) values (:orderId, :it, UNIX_TIMESTAMP()) ")
    @BatchChunkSize(1000)
    public int[] batchCreateOrderUser(@Bind("orderId") int orderId, @BindBean("it") Iterable<String> its);
}