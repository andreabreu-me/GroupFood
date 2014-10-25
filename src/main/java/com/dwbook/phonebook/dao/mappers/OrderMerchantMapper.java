package com.dwbook.phonebook.dao.mappers;

import com.dwbook.phonebook.representations.OrderMerchant;

import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by howard on 10/24/14.
 */
public class OrderMerchantMapper implements ResultSetMapper<OrderMerchant> {
    public OrderMerchant map(int index, ResultSet r, StatementContext ctx) throws SQLException {
    	//We don't have to show system info to merchants. they are there for us administrators
        return new OrderMerchant(r.getInt("orderId"), r.getInt("merchantId"));
    }
}   