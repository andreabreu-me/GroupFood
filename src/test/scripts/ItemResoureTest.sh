#!/bin/bash

echo -e "\n==================================================="
echo -e "\nTesting ItemResource"

echo -e "\ncreate merchant Changhao's"
curl --verbose -k -H "Content-Type:Application/json; charset=UTF-8" -X POST \
-d '{"name":"changhaos","branch":"San Jose","description":"Changhaos first dining in US","address":"1234 some str, some cty, some state, US","latitude":"123.45","longitude":"234.56","deliverDistanceKm":"3","minimumOrder":"10","minimumDelivery":"10","mainPhone":"12345678", "mobilePhone":"23456789","orderSubmissionJson":"{this is a submssion json}","imageJson":"this is a image json","feedbackJson":"this is a feedback json"}' \
-u john_doe:secret http://localhost:8080/Merchant

echo -e "\ncreate Item item1"
curl --verbose -k -H "Content-Type:Application/json; charset=UTF-8" -X POST \
-d '[{"merchantId":"1","title":"item1","description":"description","unitPrice":"7.99","dailyLimit":"4","weight":"3","imageJson":"imageJson","feedbackJson":"feedbackJson"}]' \
-u john_doe:secret http://localhost:8080/Merchant/1/Item
   
echo -e "\ncreate Item item2, item3"
curl --verbose -k -H "Content-Type:Application/json; charset=UTF-8" -X POST \
-d '[{"merchantId":"1","title":"item2","description":"description","unitPrice":"7.99","dailyLimit":"4","weight":"5","imageJson":"imageJson","feedbackJson":"feedbackJson"},{"merchantId":"1","title":"item3","description":"description","unitPrice":"7.99","dailyLimit":"4","weight":"7","imageJson":"imageJson","feedbackJson":"feedbackJson"}]' \
-u john_doe:secret http://localhost:8080/Merchant/1/Item

echo -e "\nget item 1"
curl -X GET -u john_doe:secret http://localhost:8080/Merchant/1/Item/1

echo -e "\nget all Items"
curl -X GET -u john_doe:secret http://localhost:8080/Merchant/1/Item

echo -e "\nget a list of items order by their weight"
curl -X GET -u john_doe:secret http://localhost:8080/Merchant/1/Item/Descending

echo -e "\nchange Item3 weight to null"
curl --verbose -k -H "Content-Type:Application/json; charset=UTF-8" -X PUT \
-d '{"merchantId":"1","title":"item3","description":"description","unitPrice":"7.99","dailyLimit":"4","weight":"","imageJson":"imageJson","feedbackJson":"feedbackJson"}' \
-u john_doe:secret http://localhost:8080/Merchant/1/Item/3

echo -e "\nget a list of items order by their weight"
curl -X GET -u john_doe:secret http://localhost:8080/Merchant/1/Item/Descending

echo -e "\ndelete item 2"
curl -X DELETE -u john_doe:secret http://localhost:8080/Merchant/1/Item/2

echo -e "\nget a list of items order by their weight"
curl -X GET -u john_doe:secret http://localhost:8080/Merchant/1/Item/Descending

echo -e "\ndelete all item"
curl -X DELETE -u john_doe:secret http://localhost:8080/Merchant/1/Item

echo -e "\nget a list of items order by their weight"
curl -X GET -u john_doe:secret http://localhost:8080/Merchant/1/Item/Descending

echo -e "\n"