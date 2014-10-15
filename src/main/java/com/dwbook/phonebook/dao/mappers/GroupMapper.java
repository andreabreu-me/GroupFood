package com.dwbook.phonebook.dao.mappers;

import com.dwbook.phonebook.representations.Group;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by curtishu on 10/11/14.
 */
public class GroupMapper implements ResultSetMapper<Group> {
    @Override
    public Group map(int i, ResultSet resultSet, StatementContext statementContext) throws SQLException {
        return new Group(resultSet.getLong("id"), resultSet.getString("organizerId"), resultSet.getString("name"),
                resultSet.getString("description"), resultSet.getLong("orderId"), resultSet.getString("status"));
    }
}
