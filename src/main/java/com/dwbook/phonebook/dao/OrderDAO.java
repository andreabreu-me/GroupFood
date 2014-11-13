package com.dwbook.phonebook.dao;

import java.util.List;

import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.BindBean;
import org.skife.jdbi.v2.sqlobject.GetGeneratedKeys;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.Transaction;
import org.skife.jdbi.v2.sqlobject.customizers.Mapper;
import org.skife.jdbi.v2.sqlobject.mixins.Transactional;

import com.dwbook.phonebook.dao.mappers.OrderMapper;
import com.dwbook.phonebook.representations.Order;

/**
 * Created by howard on 10/23/14.
 */

/**
 * In use:
 * 		getAllActiveOrder						AdminResource
 * 		getAllOrder									AdminResource
 * 		getOrderByUserId						OrderResource
 * 		getOrderByOrderId					OrderResource
 * 		getFriendOrder							NewsFeedResource
 * 		getPendingOrder						NewsFeedResource
 * 		createOrder								OrderResource
 * 		updateOrder								OrderResource
 */

public interface OrderDAO extends Transactional<OrderDAO> {

    @Mapper(OrderMapper.class)
    @SqlQuery("select * from `Order` where deletedOn is null")
    List<Order> getAllActiveOrder();
    
    @Mapper(OrderMapper.class)
    @SqlQuery("select * from `Order`")
    List<Order> getAllOrder();

    @Mapper(OrderMapper.class)
    @SqlQuery("select * from `Order`, `OrderUser` where `Order`.id = `OrderUser`.orderId and `OrderUser`.userId=:organizerId and `OrderUser`.status!='Pending' and `Order`.deletedOn is null and `OrderUser`.deletedOn is null")
    List<Order> getOrderByUserId(@Bind("organizerId") String organizerId);
    
    @Mapper(OrderMapper.class)
    @SqlQuery("select * from `Order` where id=:id and deletedOn is null")
    Order getOrderByOrderId(@Bind("id") int id);

    @Mapper(OrderMapper.class)
    @SqlQuery("select * from `Order` inner join `Friend` on `Order`.organizerId = `Friend`.friendId where `Friend`.userId= :organizerId and `Order`.deletedOn is null")
	List<Order> getFriendOrder(@Bind("organizerId") String organizerId);

    @Mapper(OrderMapper.class)
    @SqlQuery("select * from `Order`, `OrderUser` where `OrderUser`.userId=:userId and `OrderUser`.status='pending' and `Order`.deletedOn is null and `OrderUser`.deletedOn is null")
	List<Order> getPendingOrder(@Bind("userId") String userId);
    
    @Transaction
    @GetGeneratedKeys
    @SqlUpdate("insert into `Order` (id, organizerId, name, description, deliveryAddress, deliveryLatitude, deliveryLongitude, status, timeLimit, createdOn) values (null, :organizerId, :order.name, :order.description, :order.deliveryAddress, :order.deliveryLatitude, :order.deliveryLongitude, :order.status, :order.timeLimit, UNIX_TIMESTAMP())")
    int createOrder(@Bind("organizerId") String organizerId, @BindBean("order") Order order);
    
    @Transaction
    @SqlUpdate("update `Order` set  name= :order.name, description=:order.description, deliveryAddress=:order.deliveryAddress, deliveryLatitude=:order.deliveryLatitude, deliveryLongitude=:order.deliveryLongitude, status=:order.status, timeLimit=:order.timeLimit, updatedOn=UNIX_TIMESTAMP() where organizerId = :organizerId and id = :id and deletedOn is null")
    void updateOrder(@Bind("id") int id, @Bind("organizerId") String organizerId, @BindBean("order") Order order);
    
	//update data with a deletedOn time stamp without actually deleting it
    @Transaction
    @SqlUpdate("update `Order` set deletedOn=UNIX_TIMESTAMP() where id = :id and organizerId=:organizerId and deletedOn is null")
    void deleteOrderByOrderId(@Bind("id") int id, @Bind("organizerId") String organizerId);




    
}