#!/bin/bash

echo -e "\n==================================================="
echo -e "\nTesting OrderUserResource"

echo -e "\n Please Run OrderResourceTest First"
echo -e "\n Please Run MerchantResourceTest First"

echo -e "\nsansa adds two"
curl --verbose -k -H "Content-Type:Application/json; charset=UTF-8" -X POST \
-d '[{"orderId":"1","merchantId":"1"},{"orderId":"1","merchantId":"2"}]' \
-u john_doe:secret http://localhost:8080/User/sansaId/Order/1/OrderMerchant

echo -e "\nsansa checks all merchant in his order"
curl --verbose -k  -X GET -u john_doe:secret http://localhost:8080/User/sansaId/Order/1/OrderMerchant

echo -e "\nsansa deletes merchant 2"
curl --verbose -k  -X DELETE -u john_doe:secret http://localhost:8080/User/sansaId/Order/1/OrderMerchant/2

echo -e "\nsansa checks all merchant in his order"
curl --verbose -k  -X GET -u john_doe:secret http://localhost:8080/User/sansaId/Order/1/OrderMerchant

echo -e "\n"