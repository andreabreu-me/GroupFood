package com.dwbook.phonebook.dao.mappers;

import com.dwbook.phonebook.representations.Facebook;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by howard on 10/12/14.
 */
public class FacebookMapper implements ResultSetMapper<Facebook> {
    public Facebook map(int index, ResultSet r, StatementContext ctx) throws SQLException {
    	//We don't have to show system info to users. they are there for us administrators
        return new Facebook(r.getString("id"), r.getString("token"), r.getString("firstName"), r.getString("lastName"), r.getString("email"));
    }
}