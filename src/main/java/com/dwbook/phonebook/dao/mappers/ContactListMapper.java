package com.dwbook.phonebook.dao.mappers;

import com.dwbook.phonebook.representations.Contact;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by curtishu on 9/29/14.
 */
public class ContactListMapper implements ResultSetMapper<List<Contact>> {
    @Override
    public List<Contact> map(int i, ResultSet r, StatementContext statementContext) throws SQLException {
        List<Contact> result = new ArrayList<Contact>();
        do {
            result.add(new Contact(r.getInt("id"), r.getString("firstName"), r.getString("lastName"), r.getString("phone")));
        } while(r.next());
        return result;
    }
}