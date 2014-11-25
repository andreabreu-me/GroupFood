#!/bin/bash

echo -e "\n==================================================="
echo -e "\nThis is the first use case routine test viewed from the front end"
echo -e "\nThere are already five merchants in our database. Each merchants have three items."
echo -e "\n==================================================="

echo -e "\nJuan Cortez 是我們的第一位 User
			  \n他用Facebook account log in 後
			  \nAndroid will go and fetch his Facebook Id, Facebook Token, personal infomation, friend list, profile picture, and amazon cognito id.
			  \nThen it should create a 'signin' object and post it to url ~/User"

curl --verbose -k -H "Content-Type:Application/json; charset=UTF-8" -X POST \
-d '{"userId":"JuanCortezCognitoId","facebookId":"JuanCortezFBId","googlePlusId":"","token":"JuanCortezFBToken","firstName":"Juan","lastName":"Cortez","email":"JuanCortez@gmail.com", "friend":[], "imageJson":"https://scontent-2.2914.fna.fbcdn.net/hprofile-xap1/v/t1.0-1/c0.0.160.160/p160x160/10689574_1473752422905126_8997759557612706131_n.jpg?oh=3330e1326f0adae11e11a0c57d910e54&oe=54E4D85A"}' \
-u john_doe:secret http://localhost:8080/User

echo -e "\n\nAt this point we should have one entry in the User table
			  \n one entry in the Facebook table
			  \n and no entry in the Friend table as Juan should have no friends
			  \n the EndPoint should be set to ~/User/{userId} at this point"
			  
echo -e "\n\nNow Juan has logged in, and our app should go and grab newsfeed for this user
			  \nThe url should be ~/User/{userId}/NewsFeed or EndPoint/NewsFeed
			  \nJuan should see no order at all but only the recommended Merchant/Item
			  \nTODO: Filtering"

curl --verbose -k  -X GET -u john_doe:secret http://localhost:8080/User/JuanCortezCognitoId/NewsFeed

echo -e "\n\nJuan should scan through the list
			  \nEventually he clicks on one of the items
			  \nor if he clicked on a merchant, an item list should show and eventually he would click on an item
			  \nHe decided to buy vermicili curry soup from merit.
			  \na prompt should show up and ask Juan to enter name, description, and time limit in min.
			  \nthis should look pretty much like the one apple creates for entering password.
			  \nsome of the field can have default value.
			  \nan intent should be created to bind all this information along with item id, merchant id, quantity
			  \nand move to a new activity call order activity"
			  
echo -e "\n\ncreate a OrderWrapper object with a Order, a list of OrderUser, a list of OrderMerchant, and an OrderDetail
			  \ncheck the current userid is the order's creator's id or creator's friend's id. if true, then
			  \npost OrderUser under EndPoint/Order/{orderId}/OrderUser
			  \ncheck the current userid is the order's creator's id. if true, then
			  \npost OrderMerchant under EndPoint/Order/{orderId}/OrderMerchant
			  \ncheck the current userid is in the OrderUser tabel with the orderid. if true, then
			  \npost OrderDetail under EndPoint/Order/{orderId}/OrderDetail"

curl --verbose -k -H "Content-Type:Application/json; charset=UTF-8" -X POST \
-d '{"order":{"organizerId":"JuanCortezCognitoId","name":"defaultName","description":"","deliveryAddress":"","deliveryLatitude":"123.45","deliveryLongitude":"234.56","timeLimit":"60"},"orderUser":[{"userId":"JuanCortezCognitoId"}],"orderMerchant":[{"merchantId":"1"}],"orderDetail":{"merchantId":"1","itemId":"1","quantity":"1"}}' \
-u john_doe:secret http://localhost:8080/User/JuanCortezCognitoId/Order


echo -e "\n\nNow Juan should see the Order Activity. 
			  \nthere should be 1 merchant 3 items, and he should know which item he ordered
			  \nthere should be a tab on top that let's him view his order total along with a merchant view
			  \nall this information should be under EndPoint/Order/1/OrderView"

curl --verbose -k  -X GET -u john_doe:secret http://localhost:8080/User/JuanCortezCognitoId/Order/1/OrderView	  

echo -e "\n\nOnce Juan checked all the information was correct
			  \nhe returned to the newsfeed and see there's one new entry under current order"
			  
curl --verbose -k  -X GET -u john_doe:secret http://localhost:8080/User/JuanCortezCognitoId/NewsFeed
			  
echo -e "\nhe then was ready to check out 
			  \nAt that moment he found out that if he can ask two more people to order with him 
			  \nthe delivery would be free. So then he asked Curtis and Howard to install the app"
			  
echo -e "\nCurtis and Howard downloaded and installed the app
			  \n他們用Facebook account log in 後
			  \nAndroid will go and fetch their Facebook Id, Facebook Token, personal infomation, friend list, profile picture, and amazon cognito id.
			  \nThen it should create 'signin' objects and post them to url ~/User"

curl --verbose -k -H "Content-Type:Application/json; charset=UTF-8" -X POST \
-d '{"userId":"HowardHuangCognitoId","facebookId":"HowardHuangFBId","googlePlusId":"","token":"HowardHuangFBToken","firstName":"Howard","lastName":"Huang","email":"HowardHuang@gmail.com", "friend":[], "imageJson":"https://scontent-2.2914.fna.fbcdn.net/hprofile-xap1/v/t1.0-1/c0.0.160.160/p160x160/10689574_1473752422905126_8997759557612706131_n.jpg?oh=3330e1326f0adae11e11a0c57d910e54&oe=54E4D85A"}' \
-u john_doe:secret http://localhost:8080/User

curl --verbose -k -H "Content-Type:Application/json; charset=UTF-8" -X POST \
-d '{"userId":"CurtisHuangCongnitoId","facebookId":"CurtisHuangFBId","googlePlusId":"","token":"CurtisHuangFBToken","firstName":"Curtis","lastName":"Huang","email":"CurtisHuang@gmail.com", "friend":[], "imageJson":"https://scontent-2.2914.fna.fbcdn.net/hprofile-xap1/v/t1.0-1/c0.0.160.160/p160x160/10689574_1473752422905126_8997759557612706131_n.jpg?oh=3330e1326f0adae11e11a0c57d910e54&oe=54E4D85A"}' \
-u john_doe:secret http://localhost:8080/User

echo -e "\n\nThe friend table should be updated properly before they see the newsfeed"

curl --verbose -k -H "Content-Type:Application/json; charset=UTF-8" -X PUT \
-d '{"token":"JuanCortezFBToken","firstName":"Juan","lastName":"Cortez","email":"JuanCortez@gmail.com","friend":[{"userId":"JuanCortezCognitoId","friendId":"HowardHuangCognitoId","socialNetwork":"facebook","relationship":"friend"},{"userId":"JuanCortezCognitoId","friendId":"CurtisHuangCongnitoId","socialNetwork":"facebook","relationship":"friend"}],"imageJson":"https://scontent-2.2914.fna.fbcdn.net/hprofile-xap1/v/t1.0-1/c0.0.160.160/p160x160/10689574_1473752422905126_8997759557612706131_n.jpg?oh=3330e1326f0adae11e11a0c57d910e54&oe=54E4D85A"}' \
-u john_doe:secret http://localhost:8080/User/JuanCortezCognitoId/Facebook

curl --verbose -k -H "Content-Type:Application/json; charset=UTF-8" -X PUT \
-d '{"token":"HowardHuangFBId","firstName":"Howard","lastName":"Huang","email":"HowardHuang@gmail.com","friend":[{"userId":"HowardHuangCognitoId","friendId":"JuanCortezCognitoId","socialNetwork":"facebook","relationship":"friend"},{"userId":"HowardHuangCognitoId","friendId":"CurtisHuangCongnitoId","socialNetwork":"facebook","relationship":"friend"}],"imageJson":"https://scontent-2.2914.fna.fbcdn.net/hprofile-xap1/v/t1.0-1/c0.0.160.160/p160x160/10689574_1473752422905126_8997759557612706131_n.jpg?oh=3330e1326f0adae11e11a0c57d910e54&oe=54E4D85A"}' \
-u john_doe:secret http://localhost:8080/User/HowardHuangCognitoId/Facebook

curl --verbose -k -H "Content-Type:Application/json; charset=UTF-8" -X PUT \
-d '{"token":"CurtisHuangFBToken","firstName":"Curtis","lastName":"Huang","email":"CurtisHuang@gmail.com","friend":[{"userId":"CurtisHuangCongnitoId","friendId":"HowardHuangCognitoId","socialNetwork":"facebook","relationship":"friend"},{"userId":"CurtisHuangCongnitoId","friendId":"JuanCortezCognitoId","socialNetwork":"facebook","relationship":"friend"}],"imageJson":"https://scontent-2.2914.fna.fbcdn.net/hprofile-xap1/v/t1.0-1/c0.0.160.160/p160x160/10689574_1473752422905126_8997759557612706131_n.jpg?oh=3330e1326f0adae11e11a0c57d910e54&oe=54E4D85A"}' \
-u john_doe:secret http://localhost:8080/User/CurtisHuangCongnitoId/Facebook

echo -e "\n\nOnce they see the newsFeed, they should see an entry under Friends order"

curl --verbose -k  -X GET -u john_doe:secret http://localhost:8080/User/CurtisHuangCongnitoId/NewsFeed
curl --verbose -k  -X GET -u john_doe:secret http://localhost:8080/User/HowardHuangCognitoId/NewsFeed

echo -e "\n\nThey both clicked on that order
			  \nand clicked on the join button.
			  \nAndroid should check if the user clicked on the join button 
			  \nis a frined of the organizer of the order.
			  \nin this case, they both are juan Cortezs friends
			  \nthus the orderUser table should be updated"
			  
curl --verbose -k -H "Content-Type:Application/json; charset=UTF-8" -X POST \
-d '[{"orderId":"1","userId":"CurtisHuangCongnitoId", "status":"added"}]' \
-u john_doe:secret http://localhost:8080/User/CurtisHuangCongnitoId/Order/1/OrderUser

curl --verbose -k -H "Content-Type:Application/json; charset=UTF-8" -X POST \
-d '[{"orderId":"1","userId":"HowardHuangCognitoId", "status":"added"}]' \
-u john_doe:secret http://localhost:8080/User/HowardHuangCognitoId/Order/1/OrderUser

echo -e "\n\nnow they are both the participants of this order.
			  \nif they both go back to the main page,
			  \ntheir newsfeed should be displayed correctly
			  \nTODO: decide what happened to the participating friends Order"

curl --verbose -k  -X GET -u john_doe:secret http://localhost:8080/User/HowardHuangCognitoId/NewsFeed
curl --verbose -k  -X GET -u john_doe:secret http://localhost:8080/User/CurtisHuangCongnitoId/NewsFeed
			 
echo -e "\n\nnow before the two user participants added any items, 
			  \nJuan decides to check the orderview (user + merchant)"
			  
curl --verbose -k  -X GET -u john_doe:secret http://localhost:8080/User/JuanCortezCognitoId/Order/1/OrderView

echo -e "\n\nCurtis decides to order Thai Fried rice from Merit
			  \nand Howard wants to add one more vermicilli noodle soup."
			  
curl --verbose -k -H "Content-Type:Application/json; charset=UTF-8" -X POST \
-d '{"userId":"CurtisHuangCongnitoId","orderId":"1","merchantId":"1","itemId":"2","quantity":"1","status":"chosen"}' \
-u john_doe:secret http://localhost:8080/User/CurtisHuangCongnitoId/Order/1/OrderDetail

curl --verbose -k -H "Content-Type:Application/json; charset=UTF-8" -X POST \
-d '{"userId":"HowardHuangCognitoId","orderId":"1","merchantId":"1","itemId":"1","quantity":"1","status":"chosen"}' \
-u john_doe:secret http://localhost:8080/User/HowardHuangCognitoId/Order/1/OrderDetail

echo -e "\n\nJuan decided to check the orderview (user + merchant) again"
                                                                                                                                       
curl --verbose -k  -X GET -u john_doe:secret http://localhost:8080/User/JuanCortezCognitoId/Order/1/OrderView 

echo -e "\n\nEnd Here\n"                    
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                               "