package com.dwbook.phonebook.dao.mappers;

import com.dwbook.phonebook.representations.Message;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by curtishu on 10/20/14.
 */
public class MessageMapper implements ResultSetMapper<Message> {
    @Override
    public Message map(int i, ResultSet r, StatementContext statementContext) throws SQLException {
        return new Message(r.getLong("id"), r.getLong("groupId"), r.getString("authorId"),
                r.getString("content"), r.getLong("createdOn"), r.getLong("readOn"));
    }
}
