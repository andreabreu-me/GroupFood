package com.dwbook.phonebook.dao.mappers;

import com.dwbook.phonebook.representations.Merchant;

import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by howard on 10/20/14.
 */
public class MerchantMapper implements ResultSetMapper<Merchant> {
    public Merchant map(int index, ResultSet r, StatementContext ctx) throws SQLException {
    	//We don't have to show system info to users. they are there for us administrators
        return new Merchant(r.getInt("id"), r.getString("name"), r.getString("branch"), r.getString("description"), r.getString("address")
        		, r.getFloat("latitude"), r.getFloat("longitude"), r.getInt("deliverDistanceKm"), r.getFloat("minimumOrder"), r.getFloat("minimumDelivery")
        		, r.getString("mainPhone"), r.getString("mobilePhone"), r.getString("orderSubmissionJson"), r.getString("imageJson"), r.getString("feedbackJson"));
    }
}