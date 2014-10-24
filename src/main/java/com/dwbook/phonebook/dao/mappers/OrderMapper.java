package com.dwbook.phonebook.dao.mappers;

import com.dwbook.phonebook.representations.Order;

import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by howard on 10/23/14.
 */
public class OrderMapper implements ResultSetMapper<Order> {
    public Order map(int index, ResultSet r, StatementContext ctx) throws SQLException {
    	//We don't have to show system info to users. they are there for us administrators
        return new Order(r.getInt("id"), r.getString("organizerId"), r.getString("name"),r.getString("description"),r.getString("deliveryAddress"), r.getFloat("deliveryLatitude"), r.getFloat("deliveryLongitude"), r.getString("status"));
    }
}   