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
        return new Friend(r.getString("userId"), r.getString("friendId"), r.getString("socialNetwork"), r.getString("relationship"));
    }
}