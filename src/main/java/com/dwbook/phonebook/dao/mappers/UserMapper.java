package com.dwbook.phonebook.dao.mappers;

import com.dwbook.phonebook.representations.User;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by howard on 10/12/14.
 */
public class UserMapper implements ResultSetMapper<User> {
    public User map(int index, ResultSet r, StatementContext ctx) throws SQLException {
    	//We don't have to show system info to users. they are there for us administrators
        return new User(r.getString("id"), r.getString("facebook_id"), r.getString("googleplus_id"),r.getString("created_on"),r.getString("updated_on"),r.getString("deleted_on"));
    }
}