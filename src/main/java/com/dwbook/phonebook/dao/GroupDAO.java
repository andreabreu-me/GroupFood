package com.dwbook.phonebook.dao;

import com.dwbook.phonebook.dao.mappers.GroupMapper;
import com.dwbook.phonebook.representations.Group;
import org.skife.jdbi.v2.sqlobject.*;
import org.skife.jdbi.v2.sqlobject.customizers.BatchChunkSize;
import org.skife.jdbi.v2.sqlobject.customizers.Mapper;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper;
import org.skife.jdbi.v2.sqlobject.mixins.Transactional;

import java.util.List;

/**
 * Created by curtishu on 10/11/14.
 */
@RegisterMapper(GroupMapper.class)
public interface GroupDAO extends Transactional<GroupDAO> {

    @Transaction
    @GetGeneratedKeys
    @SqlUpdate("insert into `Group` (organizerId, name, description, createdOn) values (:organizerId, :name, :description, UNIX_TIMESTAMP())")
    long createGroup(@Bind("organizerId") String organizerId, @Bind("name") String name, @Bind("description") String description);

    @Transaction
    @BatchChunkSize(1000)
    @SqlBatch("insert into `Group` (organizerId, name, description, createdOn) values (:it.organizerId, :it.name, :it.description, UNIX_TIMESTAMP())")
    int[] createGroupBatch (@BindBean("it") Iterable<Group> its);

    @Mapper(GroupMapper.class)
    @SqlQuery("select id, organizerId, name, description, orderId, status from `Group` where id = :id and deletedOn is null")
    Group getGroupById(@Bind("id") long id);

    @Mapper(GroupMapper.class)
    @SqlQuery("select id, organizerId, name, description, orderId, status from `Group` where organizerId=:organizerId and deletedOn is null")
    List<Group> findGroupsByOrganizerId(@Bind("organizerId") String organizerId);

    @Transaction
    @SqlUpdate("update `Group` set organizerId=:organizerId, name=:name, description=:description, updatedOn=UNIX_TIMESTAMP() where id=:id and deletedOn is null")
    void updateGroup(@Bind("id") long id, @Bind("organizerId") String organizerId, @Bind("name") String name, @Bind("description") String description);

    @Transaction
    @SqlUpdate("update `Group` set deletedOn=UNIX_TIMESTAMP() where id=:id and deletedOn is null")
    void deleteGroup(@Bind("id") long id);
}
