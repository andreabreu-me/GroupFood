package com.dwbook.phonebook.dao;

import com.dwbook.phonebook.dao.mappers.MessageMapper;
import com.dwbook.phonebook.representations.Message;
import com.dwbook.phonebook.representations.MessageUser;
import org.skife.jdbi.v2.sqlobject.*;
import org.skife.jdbi.v2.sqlobject.customizers.BatchChunkSize;
import org.skife.jdbi.v2.sqlobject.customizers.Mapper;
import org.skife.jdbi.v2.sqlobject.mixins.Transactional;

import java.util.List;

/**
 * Created by curtishu on 10/20/14.
 */
public interface MessageDAO extends Transactional<MessageDAO> {

    //TODO cascade delete from group/user gets deleted

    //TODO do we allow users to continue chatting in group when order is closed?

    //user post something in group
    @Transaction
    @GetGeneratedKeys
    @SqlUpdate("insert into `Message` (groupId, authorId, content, createdOn) values (:groupId, :authorId, :content, UNIX_TIMESTAMP())")
    long createMessage(@Bind("groupId") long groupId, @Bind("authorId") String authorId, @Bind("content") String content);

    //user read one message from group
    @Transaction
    @GetGeneratedKeys
    @SqlUpdate("insert into `MessageUser` (messageId, userId, createdOn) values (:messageId, :userId, UNIX_TIMESTAMP()) ON DUPLICATE KEY UPDATE updatedOn=UNIX_TIMESTAMP()")
    long createMessageUser(@Bind("messageId") long messageId, @Bind("userId") String userId);

    //user read multiple messages from group
    @Transaction
    @BatchChunkSize(1000)
    @SqlBatch("insert into `MessageUser` (messageId, userId, createdOn) values (:it.messageId, :it.userId, UNIX_TIMESTAMP()) ON DUPLICATE KEY UPDATE updatedOn=UNIX_TIMESTAMP()")
    void createMessageUserBatch(@BindBean("it") Iterable<MessageUser> its);

    @Mapper(MessageMapper.class)
    @SqlQuery("select A.id, A.groupId, A.authorId, A.content, A.createdOn, IF(B.createdOn is null, -1, B.createdOn) as readOn from (select id, groupId, authorId, content, createdOn from `Message` where groupId = :groupId and deletedOn is null) A LEFT OUTER JOIN `MessageUser` B ON A.id = B.messageId and B.userId = :userId and B.deletedOn is null order by A.createdOn")
    List<Message> findMessagesInGroupForUser(@Bind("groupId") long groupId, @Bind("userId") String userId);

    @Mapper(MessageMapper.class)
    @SqlQuery("select A.id, A.groupId, A.authorId, A.content, A.createdOn, IF(B.createdOn is null, -1, B.createdOn) as readOn from (select id, groupId, authorId, content, createdOn from `Message` where groupId = :groupId and createdOn >= :newerThanTime and deletedOn is null) A LEFT OUTER JOIN `MessageUser` B ON A.id = B.messageId and B.userId = :userId and B.deletedOn is null order by A.createdOn")
    List<Message> findMessagesInGroupForUserByTime(@Bind("groupId") long groupId, @Bind("userId") String userId, @Bind("newerThanTime") long newerThanTime);

    //findMessagesInGroupForUserByLimit
    @Mapper(MessageMapper.class)
    @SqlQuery("select A.id, A.groupId, A.authorId, A.content, A.createdOn, IF(B.createdOn is null, -1, B.createdOn) as readOn from (select id, groupId, authorId, content, createdOn from `Message` where groupId = :groupId and deletedOn is null) A LEFT OUTER JOIN `MessageUser` B ON A.id = B.messageId and B.userId = :userId and B.deletedOn is null order by A.createdOn limit :limit")
    List<Message> findMessagesInGroupForUserByLimit(@Bind("groupId") long groupId, @Bind("userId") String userId, @Bind("limit") int limit);

    //findMessagesInGroupForUserByTimeAndLimit
    @Mapper(MessageMapper.class)
    @SqlQuery("select A.id, A.groupId, A.authorId, A.content, A.createdOn, IF(B.createdOn is null, -1, B.createdOn) as readOn from (select id, groupId, authorId, content, createdOn from `Message` where groupId = :groupId and createdOn >= :newerThanTime and deletedOn is null) A LEFT OUTER JOIN `MessageUser` B ON A.id = B.messageId and B.userId = :userId and B.deletedOn is null order by A.createdOn limit :limit")
    List<Message> findMessagesInGroupForUserByTimeAndLimit(@Bind("groupId") long groupId, @Bind("userId") String userId, @Bind("newerThanTime") long newerThanTime, @Bind("limit") int limit);

}
