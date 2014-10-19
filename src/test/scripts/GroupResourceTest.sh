#!/bin/bash

# TODO dependencies, user and facebook created (CreateGroup.sql for now)

echo -e "create single group for sansa"
curl -H "Content-Type: application/json; charset=UTF-8" -X POST \
-d '{"organizerId":"sansaId","name":"sansa 午餐","description":"sansa lunch buddies"}' \
-u john_doe:secret http://localhost:8080/GroupUser/Group?actorId=sansaId

echo -e "\n check sansa as organizer of the group"
curl -X GET -u john_doe:secret http://localhost:8080/GroupUser/Organizer/sansaId/Group

echo -e "\n check sansa as user of the group"
curl -X GET -u john_doe:secret http://localhost:8080/GroupUser/User/sansaId/Group

echo -e "\n add snow to the group"
curl -X POST -u john_doe:secret http://localhost:8080/GroupUser/Group/1/User/snowId?actorId=sansaId

echo -e "\n add two more users to the group (william and arya)"
curl -H "Content-Type: application/json; charset=UTF-8" -X POST \
-d '["williamId", "aryaId"]' \
-u john_doe:secret http://localhost:8080/GroupUser/Group/1/User?actorId=sansaId

echo -e "\n check users to find four users in the group"
curl -X GET -u john_doe:secret http://localhost:8080/GroupUser/Group/1/User

echo -e "\n verify william's group membership"
curl -X GET -u john_doe:secret http://localhost:8080/GroupUser/User/williamId/Group

echo -e "\n arya remove self from the group"
curl -X DELETE -u john_doe:secret http://localhost:8080/GroupUser/Group/1/User/aryaId?actorId=aryaId

echo -e "\n arya decided to come back, bob also wants to join, sansa will add them"
curl -H "Content-Type: application/json; charset=UTF-8" -X POST \
-d '["bobId", "aryaId"]' \
-u john_doe:secret http://localhost:8080/GroupUser/Group/1/User?actorId=sansaId

echo -e "\n sansa updates the group description to welcome sansa back"
curl -X PUT -H "Content-Type: application/json; charset=UTF-8" \
-d '{"organizerId":"sansaId","name":"sansa 午餐","description":"arya 回來了"}' \
-u john_doe:secret http://localhost:8080/GroupUser/Group/1?actorId=sansaId

echo -e "\n verify arya's group memebership and new description"
curl -X GET -u john_doe:secret http://localhost:8080/GroupUser/User/aryaId/Group

echo -e "\n sansa decided to delete group"
curl -X DELETE -u john_doe:secret http://localhost:8080/GroupUser/Group/1?actorId=sansaId