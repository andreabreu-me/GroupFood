#!/bin/bash

echo -e "\n==================================================="
echo -e "\nTesting AdminResource"

echo -e "\n get all active user"
curl -X GET -u john_doe:secret http://localhost:8080/Admin/User/

echo -e "\n get all user"
curl -X GET -u john_doe:secret http://localhost:8080/Admin/User/all

echo -e "\n get all active Facebook"
curl -X GET -u john_doe:secret http://localhost:8080/Admin/Facebook/

echo -e "\n get all Facebook"
curl -X GET -u john_doe:secret http://localhost:8080/Admin/Facebook/all

echo -e "\n get all active Friend"
curl -X GET -u john_doe:secret http://localhost:8080/Admin/Friend

echo -e "\n get all Friend"
curl -X GET -u john_doe:secret http://localhost:8080/Admin/Friend/all

echo -e "\n get all active Merchant"
curl -X GET -u john_doe:secret http://localhost:8080/Admin/Merchant

echo -e "\n get all Merchant"
curl -X GET -u john_doe:secret http://localhost:8080/Admin/Merchant/all

echo -e "\n get all active Item"
curl -X GET -u john_doe:secret http://localhost:8080/Admin/Item

echo -e "\n get all Item"
curl -X GET -u john_doe:secret http://localhost:8080/Admin/Item/all

echo -e "\n"