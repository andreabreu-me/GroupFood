#!/bin/bash

echo -e "\n==================================================="
echo -e "\nThis is the first use case routine test-complex version:\n
details:\n
		1.sansa creates an order
		2.sansa adds three merchants into the order
		3.sansa adds arya and bob into the order
		4.sansa adds 2 item 2 from merchant 1
		5.sansa adds 2 item 2 from merchant 2
		6.sansa adds 2 item 2 from merchant 3
		7.sansa deletes his order with merchant 3
		8.sansa updates quantity from 2 to 5 with merchant 2
		9.arya adds 1 item 1 from merchant 1
		10. arya adds 1 item 2 from merchant 1
		11. arya adds 1 item 3 from merchant 1
		12. bob adds 10 item 1 from merchant 3
		13. bob decides to leave the order
		14. sansa decides to delete merchant 1
		15. sansa adds a new merchant 4
		16. sansa adds 2 item 2 from merchant 4
		17. arya adds 1 item 1 from merchant 4
		18. arya adds 1 item 2 from merchant 4
		19. arya adds 1 item 3 from merchant 4
		20. sansa adds snow into the order
		21. snow adds 1 item 2 from merchant 4	
		22. sansa adds merchant 1 back
		23. sansa adds bob back
		24. bob adds 1 item 1 from merchant 1
		
		orderDetail:
		user 	merchant 	item 	quantity
		sansa	2					7			5
		sansa	4					9			2
		arya		4					4			1
		arya		4					9			1
		arya		4					14		1
		bob		1					1			1
		snow	4					9			1
		"
echo -e "\n==================================================="

echo -e "\nsansa creates an Order"
curl --verbose -k -H "Content-Type:Application/json; charset=UTF-8" -X POST \
-d '{"name":"order1","description":"des","deliveryAddress":"deliveryAddress","deliveryLatitude":"123.45","deliveryLongitude":"234.56","status":"created"}' \
-u john_doe:secret http://localhost:8080/User/sansaId/Order

echo -e "\nsansa adds three merchant into the order"
curl --verbose -k -H "Content-Type:Application/json; charset=UTF-8" -X POST \
-d '[{"orderId":"1","merchantId":"1"},{"orderId":"1","merchantId":"2"},{"orderId":"1","merchantId":"3"}]' \
-u john_doe:secret http://localhost:8080/User/sansaId/Order/1/OrderMerchant

echo -e "\nsansa adds arya and bob into the order"
curl --verbose -k -H "Content-Type:Application/json; charset=UTF-8" -X POST \
-d '[{"orderId":"1","userId":"aryaId"},{"orderId":"1","userId":"bobId"}]' \
-u john_doe:secret http://localhost:8080/User/sansaId/Order/1/OrderUser

echo -e "\nsansa adds 2 item 2 from merchant 1"
curl --verbose -k -H "Content-Type:Application/json; charset=UTF-8" -X POST \
-d '{"userId":"sansaId","orderId":"1","merchantId":"1","itemId":"6","quantity":"2","status":"chosen"}' \
-u john_doe:secret http://localhost:8080/User/sansaId/Order/1/OrderDetail

echo -e "\nsansa adds 2 item 2 from merchant 2"
curl --verbose -k -H "Content-Type:Application/json; charset=UTF-8" -X POST \
-d '{"userId":"sansaId","orderId":"1","merchantId":"2","itemId":"7","quantity":"2","status":"chosen"}' \
-u john_doe:secret http://localhost:8080/User/sansaId/Order/1/OrderDetail

echo -e "\nsansa adds 2 item 2 from merchant 3"
curl --verbose -k -H "Content-Type:Application/json; charset=UTF-8" -X POST \
-d '{"userId":"sansaId","orderId":"1","merchantId":"3","itemId":"8","quantity":"2","status":"chosen"}' \
-u john_doe:secret http://localhost:8080/User/sansaId/Order/1/OrderDetail

echo -e "\nsansa deletes his order with merchant 3"
curl --verbose -k -H "Content-Type:Application/json; charset=UTF-8" -X DELETE \
-d '[{"userId":"sansaId","orderId":"1","merchantId":"3","itemId":"8","quantity":"2","status":"chosen"}]' \
-u john_doe:secret http://localhost:8080/User/sansaId/Order/1/OrderDetail

echo -e "\n8.sansa updates quantity from 2 to 5 with merchant 2"
curl --verbose -k -H "Content-Type:Application/json; charset=UTF-8" -X PUT \
-d '{"userId":"sansaId","orderId":"1","merchantId":"2","itemId":"7","quantity":"5","status":"chosen"}' \
-u john_doe:secret http://localhost:8080/User/sansaId/Order/1/OrderDetail

echo -e "\n9.arya adds 1 item 1 from merchant 1"
curl --verbose -k -H "Content-Type:Application/json; charset=UTF-8" -X POST \
-d '{"userId":"aryaId","orderId":"1","merchantId":"1","itemId":"1","quantity":"1","status":"chosen"}' \
-u john_doe:secret http://localhost:8080/User/aryaId/Order/1/OrderDetail

echo -e "\n10. arya adds 1 item 2 from merchant 1"
curl --verbose -k -H "Content-Type:Application/json; charset=UTF-8" -X POST \
-d '{"userId":"aryaId","orderId":"1","merchantId":"1","itemId":"6","quantity":"1","status":"chosen"}' \
-u john_doe:secret http://localhost:8080/User/aryaId/Order/1/OrderDetail

echo -e "\n11. arya adds 1 item 3 from merchant 1"
curl --verbose -k -H "Content-Type:Application/json; charset=UTF-8" -X POST \
-d '{"userId":"aryaId","orderId":"1","merchantId":"1","itemId":"11","quantity":"1","status":"chosen"}' \
-u john_doe:secret http://localhost:8080/User/aryaId/Order/1/OrderDetail

echo -e "\n12. bob adds 10 item 1 from merchant 3"
curl --verbose -k -H "Content-Type:Application/json; charset=UTF-8" -X POST \
-d '{"userId":"bobId","orderId":"1","merchantId":"3","itemId":"3","quantity":"10","status":"chosen"}' \
-u john_doe:secret http://localhost:8080/User/bobId/Order/1/OrderDetail

echo -e "\n13. bob decides to leave the order"
curl -X DELETE -u john_doe:secret http://localhost:8080/User/bobId/Order/1/OrderUser

echo -e "\n14. sansa decides to delete merchant 1"
curl -X DELETE -u john_doe:secret http://localhost:8080/User/sansaId/Order/1/OrderMerchant/1

echo -e "\n15. sansa adds a new merchant 4"
curl --verbose -k -H "Content-Type:Application/json; charset=UTF-8" -X POST \
-d '[{"orderId":"1","merchantId":"4"}]' \
-u john_doe:secret http://localhost:8080/User/sansaId/Order/1/OrderMerchant

echo -e "\n16. sansa adds 2 item 2 from merchant 4"
curl --verbose -k -H "Content-Type:Application/json; charset=UTF-8" -X POST \
-d '{"userId":"sansaId","orderId":"1","merchantId":"4","itemId":"9","quantity":"2","status":"chosen"}' \
-u john_doe:secret http://localhost:8080/User/sansaId/Order/1/OrderDetail

echo -e "\n17. arya adds 1 item 1 from merchant 4"
curl --verbose -k -H "Content-Type:Application/json; charset=UTF-8" -X POST \
-d '{"userId":"aryaId","orderId":"1","merchantId":"4","itemId":"4","quantity":"1","status":"chosen"}' \
-u john_doe:secret http://localhost:8080/User/aryaId/Order/1/OrderDetail

echo -e "\n18. arya adds 1 item 2 from merchant 4"
curl --verbose -k -H "Content-Type:Application/json; charset=UTF-8" -X POST \
-d '{"userId":"aryaId","orderId":"1","merchantId":"4","itemId":"9","quantity":"1","status":"chosen"}' \
-u john_doe:secret http://localhost:8080/User/aryaId/Order/1/OrderDetail

echo -e "\n19. arya adds 1 item 3 from merchant 4"
curl --verbose -k -H "Content-Type:Application/json; charset=UTF-8" -X POST \
-d '{"userId":"aryaId","orderId":"1","merchantId":"4","itemId":"14","quantity":"1","status":"chosen"}' \
-u john_doe:secret http://localhost:8080/User/aryaId/Order/1/OrderDetail

echo -e "\n20. sansa adds snow into the order"
curl --verbose -k -H "Content-Type:Application/json; charset=UTF-8" -X POST \
-d '[{"orderId":"1","userId":"snowId"}]' \
-u john_doe:secret http://localhost:8080/User/sansaId/Order/1/OrderUser

echo -e "\n21. snow adds 1 item 2 from merchant 4"	
curl --verbose -k -H "Content-Type:Application/json; charset=UTF-8" -X POST \
-d '{"userId":"snowId","orderId":"1","merchantId":"4","itemId":"9","quantity":"1","status":"chosen"}' \
-u john_doe:secret http://localhost:8080/User/snowId/Order/1/OrderDetail

echo -e "\n22. sansa adds merchant 1 back"
curl --verbose -k -H "Content-Type:Application/json; charset=UTF-8" -X POST \
-d '[{"orderId":"1","merchantId":"1"}]' \
-u john_doe:secret http://localhost:8080/User/sansaId/Order/1/OrderMerchant

echo -e "\n23. sansa adds bob back"
curl --verbose -k -H "Content-Type:Application/json; charset=UTF-8" -X POST \
-d '[{"orderId":"1","userId":"bobId"}]' \
-u john_doe:secret http://localhost:8080/User/sansaId/Order/1/OrderUser

echo -e "\n24. bob adds 1 item 1 from merchant 1"
curl --verbose -k -H "Content-Type:Application/json; charset=UTF-8" -X POST \
-d '{"userId":"bobId","orderId":"1","merchantId":"1","itemId":"1","quantity":"1","status":"chosen"}' \
-u john_doe:secret http://localhost:8080/User/bobId/Order/1/OrderDetail		

echo -e "\n"
	