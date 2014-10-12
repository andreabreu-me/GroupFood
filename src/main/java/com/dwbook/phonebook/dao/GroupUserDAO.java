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
    @SqlUpdate("insert into group_user (group_id, user_id, created_on) values (:groupId, :userId, UNIX_TIMESTAMP())")
    public int createGroupUser(@Bind("groupId") long groupId, @Bind("userId") String userId);

    @BatchChunkSize(1000)
    @Transaction
    @SqlBatch("insert into group_user (group_id, user_id, created_on) values (:it.groupId, :it.userId, UNIX_TIMESTAMP())")
    public int[] createGroupUserBatch(@BindBean("it") Iterable<GroupUser> its);

    @Mapper(GroupMapper.class)
    @SqlQuery("select A.id, A.organizer_id, A.name, A.description, A.order_id, A.status from `group` A inner join `group_user` B on A.id = B.group_id and B.user_id = :userId and A.deleted_on is null and B.deleted_on is null")
    public List<Group> findGroupByUserId(@Bind("userId") String userId);

    //TODO findUserByGroup
}
