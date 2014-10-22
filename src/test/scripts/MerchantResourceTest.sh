#!/bin/bash

echo -e "\n==================================================="
echo -e "\nTesting MerchantResource"

echo -e "\ncreate merchant Changhao's"
curl --verbose -k -H "Content-Type:Application/json; charset=UTF-8" -X POST \
-d '{"name":"changhaos","branch":"San Jose","description":"Changhaos first dining in US","address":"1234 some str, some cty, some state, US","latitude":"123.45","longitude":"234.56","deliverDistanceKm":"3","minimumOrder":"10","minimumDelivery":"10","mainPhone":"12345678", "mobilePhone":"23456789","orderSubmissionJson":"{this is a submssion json}","imageJson":"this is a image json","feedbackJson":"this is a feedback json"}' \
-u john_doe:secret http://localhost:8080/Merchant

echo -e "\ncreate merchant Changhao2's"
curl --verbose -k -H "Content-Type:Application/json; charset=UTF-8" -X POST \
-d '{"name":"changhao2s","branch":"San Jose","description":"Changhaos first dining in US","address":"1234 some str, some cty, some state, US","latitude":"123.45","longitude":"234.56","deliverDistanceKm":"3","minimumOrder":"10","minimumDelivery":"10","mainPhone":"12345678", "mobilePhone":"23456789","orderSubmissionJson":"{this is a submssion json}","imageJson":"this is a image json","feedbackJson":"this is a feedback json"}' \
-u john_doe:secret http://localhost:8080/Merchant

echo -e "\nmake sure changhaos was created by id"
curl -X GET -u john_doe:secret http://localhost:8080/Merchant/1

echo -e "\nmake sure changhao2s was created by name"
curl -X GET -u john_doe:secret http://localhost:8080/Merchant/name/changhao2s

echo -e "\nupdate merchant Changhao's"
curl --verbose -k -H "Content-Type:Application/json; charset=UTF-8" -X PUT \
-d '{"name":"changhaos","branch":"台灣","description":"Changhaos first dining in US","address":"1234 some str, some cty, some state, US","latitude":"123.45","longitude":"234.56","deliverDistanceKm":"3","minimumOrder":"10","minimumDelivery":"10","mainPhone":"12345678", "mobilePhone":"23456789","orderSubmissionJson":"{this is a submssion json}","imageJson":"this is a image json","feedbackJson":"this is a feedback json"}' \
-u john_doe:secret http://localhost:8080/Merchant/1

echo -e "\nmake sure the update was successful on Changhao's"
curl -X GET -u john_doe:secret http://localhost:8080/Merchant/1

echo -e "\n"