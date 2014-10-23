package com.dwbook.phonebook.dao.mappers;

import com.dwbook.phonebook.representations.Item;

import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by howard on 10/20/14.
 */
public class ItemMapper implements ResultSetMapper<Item> {
    public Item map(int index, ResultSet r, StatementContext ctx) throws SQLException {
    	//We don't have to show system info to users. they are there for us administrators
        return new Item(r.getInt("id"), r.getInt("merchantId"), r.getString("title"), r.getString("description"), r.getFloat("unitPrice")
        		, r.getInt("dailyLimit"), r.getInt("weight"), r.getString("imageJson"), r.getString("feedbackJson"));
    }
}