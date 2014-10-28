#!/bin/bash

echo -e "\n==================================================="
echo -e "\nTesting OrderResource"

echo -e "\check sansaId exists in the User table"
curl --verbose -k  -X GET -u john_doe:secret http://localhost:8080/User/sansaId

echo -e "\nsansaid creates an Order"
curl --verbose -k -H "Content-Type:Application/json; charset=UTF-8" -X POST \
-d '{"name":"order1","description":"des","deliveryAddress":"deliveryAddress","deliveryLatitude":"123.45","deliveryLongitude":"234.56","status":"created"}' \
-u john_doe:secret http://localhost:8080/User/sansaId/Order

echo -e "\nsansaid creates another Order"
curl --verbose -k -H "Content-Type:Application/json; charset=UTF-8" -X POST \
-d '{"name":"order2","description":"des","deliveryAddress":"deliveryAddress","deliveryLatitude":"234.56","deliveryLongitude":"234.56","status":"created"}' \
-u john_doe:secret http://localhost:8080/User/sansaId/Order

echo -e "\check sansaId's first order"
curl --verbose -k  -X GET -u john_doe:secret http://localhost:8080/User/sansaId/Order/1

echo -e "\check all of sansaId's order"
curl --verbose -k  -X GET -u john_doe:secret http://localhost:8080/User/sansaId/Order

echo -e "\nsansaid updates Order 1"
curl --verbose -k -H "Content-Type:Application/json; charset=UTF-8" -X PUT \
-d '{"name":"new order1","description":"des","deliveryAddress":"deliveryAddress","deliveryLatitude":"234.56","deliveryLongitude":"234.56","status":"updated"}' \
-u john_doe:secret http://localhost:8080/User/sansaId/Order/1

echo -e "\sansaId deletes order2"
curl --verbose -k  -X DELETE -u john_doe:secret http://localhost:8080/User/sansaId/Order/2

echo -e "\check all of sansaId's order"
curl --verbose -k  -X GET -u john_doe:secret http://localhost:8080/User/sansaId/Order

echo -e "\n"