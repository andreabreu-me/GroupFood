package com.dwbook.phonebook.dao;

import com.dwbook.phonebook.dao.mappers.GroupMapper;
import com.dwbook.phonebook.dao.mappers.GroupUserMapper;
import com.dwbook.phonebook.dao.mappers.UserMapper;
import com.dwbook.phonebook.representations.Group;
import com.dwbook.phonebook.representations.GroupUser;
import com.dwbook.phonebook.representations.User;
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
    @SqlUpdate("insert into `GroupUser` (groupId, userId, createdOn, updatedOn, deletedOn) values (:groupId, :userId, UNIX_TIMESTAMP(), NULL, NULL) ON DUPLICATE KEY UPDATE updatedOn=UNIX_TIMESTAMP(), deletedOn=NULL")
    public void createGroupUser(@Bind("groupId") long groupId, @Bind("userId") String userId);

    @Transaction
    @BatchChunkSize(1000)
    @SqlBatch("insert into `GroupUser` (groupId, userId, createdOn, updatedOn, deletedOn) values (:it.groupId, :it.userId, UNIX_TIMESTAMP(), NULL, NULL) ON DUPLICATE KEY UPDATE updatedOn=UNIX_TIMESTAMP(), deletedOn=NULL")
    public void createGroupUserBatch(@BindBean("it") Iterable<GroupUser> its);

    @Transaction
    @SqlUpdate("update `GroupUser` set deletedOn=UNIX_TIMESTAMP() where groupId=:groupId and userId=:userId and deletedOn is null")
    void deleteGroupUser(@Bind("groupId") long groupId, @Bind("userId") String userId);

    @Transaction
    @SqlUpdate("update `GroupUser` set deletedOn=UNIX_TIMESTAMP() where groupId=:groupId and deletedOn is null")
    void deleteGroup(@Bind("groupId") long groupId);

    @Transaction
    @SqlUpdate("update `GroupUser` set deletedOn=UNIX_TIMESTAMP() where userId=:userId and deletedOn is null")
    void deleteUser(@Bind("userId") String userId);

    @Mapper(GroupMapper.class)
    @SqlQuery("select A.id, A.organizerId, A.name, A.description, A.orderId, A.status from `Group` A inner join `GroupUser` B on A.id = B.groupId and B.userId = :userId and A.deletedOn is null and B.deletedOn is null")
    public List<Group> findGroupsByUserId(@Bind("userId") String userId);

    @Mapper(UserMapper.class)
    @SqlQuery("select A.id, A.facebookId, A.googlePlusId from `User` A inner join `GroupUser` B on A.id = B.userId and B.groupId = :groupId and A.deletedOn is null and B.deletedOn is null")
    public List<User> findUsersByGroupId(@Bind("groupId") long groupId);

    @Mapper(GroupUserMapper.class)
    @SqlQuery("select groupId, userId from `GroupUser` where groupId = :groupId and userId = :userId and deletedOn is null")
    public GroupUser checkGroupUser(@Bind("groupId") long groupId, @Bind("userId") String userId);
}
