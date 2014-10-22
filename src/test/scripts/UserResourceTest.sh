#!/bin/bash

echo -e "\n==================================================="
echo -e "\nTesting UserResource"

echo -e "create user Changhao who sign up using both fb and gp, and befriends with sansaid on fb and snowid on gp"
curl --verbose -k -H "Content-Type:Application/json; charset=UTF-8" -X POST \
-d '{"userId":"changhao","facebookId":"changhaofbid","googlePlusId":"changhaogpid","token":"changhaoToken","firstName":"Changhao","lastName":"Huang","email":"howard168222@hotmail.com", "friend":[{"friendId":"sansaid","socialNetwork":"GooglePlus","relationship":"friend"},{"friendId":"snowid","socialNetwork":"Facebook","relationship":"family"}]}' \
-u john_doe:secret http://localhost:8080/User

echo -e "\n check if Changhao is created"
curl -X GET -u john_doe:secret http://localhost:8080/User/changhao

echo -e "\n==================================================="
echo -e "\nTesting FriendResource"

echo -e "\n check if Changhao's friends are accessible"
curl -X GET -u john_doe:secret http://localhost:8080/User/changhao/Friend

echo -e "\n access Changhao's friend sansaid by sansaid's userId"
curl -X GET -u john_doe:secret http://localhost:8080/User/changhao/Friend/sansaid

echo -e "\n access Changhao's friend by socialNetwork=Facebook"
curl -X GET -u john_doe:secret http://localhost:8080/User/changhao/Friend/socialNetwork/Facebook

echo -e "\n access Changhao's friend by relationship=friend"
curl -X GET -u john_doe:secret http://localhost:8080/User/changhao/Friend/relationship/friend

echo -e "\n==================================================="
echo -e "\nTesting FacebookResource"

echo -e "\n check if Changhao's facebook is created"
curl -X GET -u john_doe:secret http://localhost:8080/User/changhao/Facebook

echo -e "\n update Changhao's facebook token, firstname, lastname, email"
curl -X PUT -H "Content-Type: application/json; charset=UTF-8" \
-d '{"token":"newChanghaoToken","firstName":"newChanghao","lastName":"newHuang","email":"newhoward168222@hotmail.com", "friend":[{"friendId":"sansaid","socialNetwork":"GooglePlus","relationship":"friend"},{"friendId":"snowid","socialNetwork":"Facebook","relationship":"family"}]}' \
-u john_doe:secret http://localhost:8080/User/changhao/Facebook

echo -e "\n check if changhao's facebook gets updated"
curl -X GET -u john_doe:secret http://localhost:8080/User/changhao/Facebook

echo -e "\n check if changhao's Friend gets updated"
curl -X GET -u john_doe:secret http://localhost:8080/User/changhao/Friend

echo -e "\n decrease Changhao's friend"
curl -X PUT -H "Content-Type: application/json; charset=UTF-8" \
-d '{"token":"newChanghaoToken","firstName":"newChanghao","lastName":"newHuang","email":"newhoward168222@hotmail.com", "friend":[{"friendId":"sansaid","socialNetwork":"GooglePlus","relationship":"friend"}]}' \
-u john_doe:secret http://localhost:8080/User/changhao/Facebook

echo -e "\n check if changhao's Friend gets updated"
curl -X GET -u john_doe:secret http://localhost:8080/User/changhao/Friend

echo -e "\n increase Changhao's friend"
curl -X PUT -H "Content-Type: application/json; charset=UTF-8" \
-d '{"token":"newChanghaoToken","firstName":"newChanghao","lastName":"newHuang","email":"newhoward168222@hotmail.com", "friend":[{"friendId":"sansaid","socialNetwork":"GooglePlus","relationship":"friend"},{"friendId":"snowid","socialNetwork":"Facebook","relationship":"family"}]}' \
-u john_doe:secret http://localhost:8080/User/changhao/Facebook

echo -e "\n check if changhao's Friend gets updated"
curl -X GET -u john_doe:secret http://localhost:8080/User/changhao/Friend

echo -e "\n increase Changhao's friend"
curl -X PUT -H "Content-Type: application/json; charset=UTF-8" \
-d '{"token":"newChanghaoToken","firstName":"newChanghao","lastName":"newHuang","email":"newhoward168222@hotmail.com", "friend":[{"friendId":"sansaid","socialNetwork":"GooglePlus","relationship":"friend"},{"friendId":"snowid","socialNetwork":"Facebook","relationship":"family"},{"friendId":"williamId","socialNetwork":"Facebook","relationship":"friend"}]}' \
-u john_doe:secret http://localhost:8080/User/changhao/Facebook

echo -e "\n check if changhao's Friend gets updated"
curl -X GET -u john_doe:secret http://localhost:8080/User/changhao/Friend

echo -e "\n"