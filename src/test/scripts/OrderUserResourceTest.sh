#!/bin/bash

echo -e "\n==================================================="
echo -e "\nTesting OrderUserResource"

echo -e "\n Please Run OrderResourceTest First"

echo -e "\n Return all group sansa is participating"
curl --verbose -k  -X GET -u john_doe:secret http://localhost:8080/User/sansaId/OrderUser

echo -e "\n Return all group arya is participating"
curl --verbose -k  -X GET -u john_doe:secret http://localhost:8080/User/aryaId/OrderUser

echo -e "\n Return all group bob is participating"
curl --verbose -k  -X GET -u john_doe:secret http://localhost:8080/User/bobId/OrderUser

echo -e "\n Return all participants in OrderUser 1"
curl --verbose -k  -X GET -u john_doe:secret http://localhost:8080/User/bobId/OrderUser/1

echo -e "\n Bob leaves UserOrder 1"
curl --verbose -k  -X DELETE -u john_doe:secret http://localhost:8080/User/bobId/OrderUser/1

echo -e "\n Sansa tries to access UserOrder 1"
curl --verbose -k  -X GET -u john_doe:secret http://localhost:8080/User/bobId/OrderUser/1

echo -e "\n"