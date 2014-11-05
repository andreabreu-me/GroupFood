package com.dwbook.phonebook.dao.mappers;

import com.dwbook.phonebook.representations.OrderUser;

import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by howard on 10/23/14.
 */
public class OrderUserMapper implements ResultSetMapper<OrderUser> {
    public OrderUser map(int index, ResultSet r, StatementContext ctx) throws SQLException {
    	//We don't have to show system info to users. they are there for us administrators
        return new OrderUser(r.getInt("orderId"), r.getString("userId"), r.getString("status"));
    }
}   