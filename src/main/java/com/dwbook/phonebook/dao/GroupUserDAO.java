package com.dwbook.phonebook.dao;

import com.dwbook.phonebook.dao.mappers.GroupMapper;
import com.dwbook.phonebook.representations.Group;
import com.dwbook.phonebook.representations.GroupUser;
import org.skife.jdbi.v2.sqlobject.*;
import org.skife.jdbi.v2.sqlobject.customizers.BatchChunkSize;
import org.skife.jdbi.v2.sqlobject.customizers.Mapper;
import org.skife.jdbi.v2.sqlobject.mixins.Transactional;

import java.util.List;

/**
 * Created by curtishu on 10/11/14.
 */
public interface GroupUserDAO extends Transactional<GroupUserDAO> {

    @Transaction
    @GetGeneratedKeys
    @SqlUpdate("insert into `GroupUser` (groupId, userId, createdOn) values (:groupId, :userId, UNIX_TIMESTAMP())")
    public int createGroupUser(@Bind("groupId") long groupId, @Bind("userId") String userId);

    @BatchChunkSize(1000)
    @Transaction
    @SqlBatch("insert into `GroupUser` (groupId, userId, createdOn) values (:it.groupId, :it.userId, UNIX_TIMESTAMP())")
    public int[] createGroupUserBatch(@BindBean("it") Iterable<GroupUser> its);

    @Mapper(GroupMapper.class)
    @SqlQuery("select A.id, A.organizerId, A.name, A.description, A.orderId, A.status from `Group` A inner join `GroupUser` B on A.id = B.groupId and B.userId = :userId and A.deletedOn is null and B.deletedOn is null")
    public List<Group> findGroupByUserId(@Bind("userId") String userId);

    //TODO findUserByGroup
}
