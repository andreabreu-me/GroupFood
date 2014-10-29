#!/bin/bash

echo -e "\n==================================================="
echo -e "\nThis is the first use case routine test:\n
details:\n
		sansa creates an order and invite both arya and bob\n
		sansa then adds three merchants into the order\n
		after that each of them chose a few items from the merchants\n"
echo -e "\n==================================================="

echo -e "\nsansa creates an Order"
curl --verbose -k -H "Content-Type:Application/json; charset=UTF-8" -X POST \
-d '{"name":"order1","description":"des","deliveryAddress":"deliveryAddress","deliveryLatitude":"123.45","deliveryLongitude":"234.56","status":"created"}' \
-u john_doe:secret http://localhost:8080/User/sansaId/Order

echo -e "\nsansa adds arya and bob into the order"
curl --verbose -k -H "Content-Type:Application/json; charset=UTF-8" -X POST \
-d '[{"orderId":"1","userId":"aryaId"},{"orderId":"1","userId":"bobId"}]' \
-u john_doe:secret http://localhost:8080/User/sansaId/Order/1/OrderUser

echo -e "\nsansa adds three merchant into the order"
curl --verbose -k -H "Content-Type:Application/json; charset=UTF-8" -X POST \
-d '[{"orderId":"1","merchantId":"1"},{"orderId":"1","merchantId":"2"},{"orderId":"1","merchantId":"3"}]' \
-u john_doe:secret http://localhost:8080/User/sansaId/Order/1/OrderMerchant

echo -e "\nsansa decides to buy the first dish from the three merchants"
curl --verbose -k -H "Content-Type:Application/json; charset=UTF-8" -X POST \
-d '{"userId":"sansaId","orderId":"1","merchantId":"1","itemId":"1","quantity":"1","status":"chosen"}' \
-u john_doe:secret http://localhost:8080/User/sansaId/Order/1/OrderDetail
curl --verbose -k -H "Content-Type:Application/json; charset=UTF-8" -X POST \
-d '{"userId":"sansaId","orderId":"1","merchantId":"2","itemId":"2","quantity":"1","status":"chosen"}' \
-u john_doe:secret http://localhost:8080/User/sansaId/Order/1/OrderDetail
curl --verbose -k -H "Content-Type:Application/json; charset=UTF-8" -X POST \
-d '{"userId":"sansaId","orderId":"1","merchantId":"3","itemId":"3","quantity":"1","status":"chosen"}' \
-u john_doe:secret http://localhost:8080/User/sansaId/Order/1/OrderDetail

echo -e "\narya decides to buy all three items from merchant 2 and 2 items each"
curl --verbose -k -H "Content-Type:Application/json; charset=UTF-8" -X POST \
-d '{"userId":"aryaId","orderId":"1","merchantId":"2","itemId":"2","quantity":"2","status":"chosen"}' \
-u john_doe:secret http://localhost:8080/User/aryaId/Order/1/OrderDetail
curl --verbose -k -H "Content-Type:Application/json; charset=UTF-8" -X POST \
-d '{"userId":"aryaId","orderId":"1","merchantId":"2","itemId":"7","quantity":"2","status":"chosen"}' \
-u john_doe:secret http://localhost:8080/User/aryaId/Order/1/OrderDetail
curl --verbose -k -H "Content-Type:Application/json; charset=UTF-8" -X POST \
-d '{"userId":"aryaId","orderId":"1","merchantId":"2","itemId":"12","quantity":"2","status":"chosen"}' \
-u john_doe:secret http://localhost:8080/User/aryaId/Order/1/OrderDetail

echo -e "\nbob decides to buy only the third item from the third merchant"
curl --verbose -k -H "Content-Type:Application/json; charset=UTF-8" -X POST \
-d '{"userId":"bobId","orderId":"1","merchantId":"3","itemId":"13","quantity":"1","status":"chosen"}' \
-u john_doe:secret http://localhost:8080/User/bobId/Order/1/OrderDetail

echo -e "\nsansa checks his order detail"
curl --verbose -k  -X GET -u john_doe:secret http://localhost:8080/User/sansaId/Order/1/OrderDetail

echo -e "\narya checks his order detail"
curl --verbose -k  -X GET -u john_doe:secret http://localhost:8080/User/aryaId/Order/1/OrderDetail

echo -e "\nbob checks his order detail"
curl --verbose -k  -X GET -u john_doe:secret http://localhost:8080/User/bobId/Order/1/OrderDetail

echo -e "\nsansa checks the whole orders order detail"
curl --verbose -k  -X GET -u john_doe:secret http://localhost:8080/User/sansaId/Order/1/OrderDetail/all

echo -e "\n"