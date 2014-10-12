package com.dwbook.phonebook.dao.mappers;

import com.dwbook.phonebook.representations.GroupUser;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by curtishu on 10/11/14.
 */
public class GroupUserMapper implements ResultSetMapper<GroupUser> {
    @Override
    public GroupUser map(int i, ResultSet resultSet, StatementContext statementContext) throws SQLException {
        return new GroupUser(resultSet.getLong(0), resultSet.getString(1));
    }
}