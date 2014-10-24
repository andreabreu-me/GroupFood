#!/bin/bash

# Dependencies GroupResourceTest.sh

# TODO actorId is CASE INSENSITIVE -> need to fix

echo -e "sansa welcomes everyone in group"
curl -H "Content-Type: application/json; charset=UTF-8" -X POST \
-d '{"groupId":"1","authorId":"sansaid","content":"歡迎大家加入午餐 group"}' \
-u john_doe:secret http://localhost:8080/Message/1?actorId=sansaId

echo -e "\n arya tells everyone she's back"
curl -H "Content-Type: application/json; charset=UTF-8" -X POST \
-d '{"groupId":"1","authorId":"aryaid","content":"group 我回來了"}' \
-u john_doe:secret http://localhost:8080/Message/1?actorId=aryaId

echo -e "\n william tries to view both messages in group"
curl -X GET -u john_doe:secret http://localhost:8080/Message/1?actorId=williamId

echo -e "\n client will batch mark both messages as read for william, note we only need ids to reduce payload"
curl -H "Content-Type: application/json; charset=UTF-8" -X POST \
-d '[{"id":1},{"id":2}]' \
-u john_doe:secret http://localhost:8080/Message/1/BatchRead?actorId=williamId

echo -e "\n snows adds a new message in the group"
curl -H "Content-Type: application/json; charset=UTF-8" -X POST \
-d '{"groupId":"1","authorId":"snowid","content":"i know 所有事"}' \
-u john_doe:secret http://localhost:8080/Message/1?actorId=snowId

echo -e "\n william now will see 2 read and 1 unread"
curl -X GET -u john_doe:secret http://localhost:8080/Message/1?actorId=williamId

echo -e "\n client will now mark the last one as read"
curl -X POST -u john_doe:secret http://localhost:8080/Message/1/3?actorId=williamId

echo -e "\n snow wants to view just 1 message from group 1"
curl -X GET -u john_doe:secret "http://localhost:8080/Message/1?actorId=snowid&limit=1"

echo -e "\n let's remove bob from group so we can test"
curl -X DELETE -u john_doe:secret http://localhost:8080/GroupUser/Group/1/User/bobId?actorId=bobId

echo -e "\n ---> we should get 403 for bob when trying to post because he's not in group"
curl -i -H "Content-Type: application/json; charset=UTF-8" -X POST \
-d '{"groupId":"1","authorId":"bobId","content":"bob 不能 post!"}' \
-u john_doe:secret http://localhost:8080/Message/1?actorId=bobId