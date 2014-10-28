#!/bin/bash

echo -e "\n==================================================="
echo -e "\nTesting OrderUserResource"

echo -e "\n Please Run OrderResourceTest First"

echo -e "\nsansa adds two more people into order 1"
curl --verbose -k -H "Content-Type:Application/json; charset=UTF-8" -X POST \
-d '[{"orderId":"1","userId":"aryaId"},{"orderId":"1","userId":"bobId"}]' \
-u john_doe:secret http://localhost:8080/User/sansaId/Order/1/OrderUser

echo -e "\nSee all participant in order 1"
curl --verbose -k  -X GET -u john_doe:secret http://localhost:8080/User/sansaId/Order/1/OrderUser

echo -e "\nSansa sees all Order Arya participates"
curl --verbose -k  -X GET -u john_doe:secret http://localhost:8080/User/sansaId/Order/1/OrderUser/aryaId

echo -e "\nBob leaves Order"
curl --verbose -k  -X DELETE -u john_doe:secret http://localhost:8080/User/bobId/Order/1/OrderUser

echo -e "\nSee all participant in order 1"
curl --verbose -k  -X GET -u john_doe:secret http://localhost:8080/User/sansaId/Order/1/OrderUser

echo -e "\nArya sees all Order she participate"
curl --verbose -k  -X GET -u john_doe:secret http://localhost:8080/User/aryaId/Order/

echo -e "\nSansa sees all Order she participate"
curl --verbose -k  -X GET -u john_doe:secret http://localhost:8080/User/sansaId/Order/

echo -e "\nBob sees all Order she participate"
curl --verbose -k  -X GET -u john_doe:secret http://localhost:8080/User/bobId/Order/

echo -e "\sansaId deletes order 1"
curl --verbose -k  -X DELETE -u john_doe:secret http://localhost:8080/User/sansaId/Order/1

echo -e "\n"