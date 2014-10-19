package com.dwbook.phonebook.dao.mappers;

import com.dwbook.phonebook.representations.Friend;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by howard on 10/17/14.
 */
public class FriendMapper implements ResultSetMapper<Friend> {
    public Friend map(int index, ResultSet r, StatementContext ctx) throws SQLException {
    	//We don't have to show system info to users. they are there for us administrators
        return new Friend(r.getString("friendId"), r.getString("socialNetwork"), r.getString("relationship"));
    }
}