use phonebook;
SET FOREIGN_KEY_CHECKS=0;
DROP TABLE IF EXISTS `User`;
DROP TABLE IF EXISTS `Facebook`;
DROP TABLE IF EXISTS `Friend`;
DROP TABLE IF EXISTS `Order`;
DROP TABLE IF EXISTS `Group`;
DROP TABLE IF EXISTS `GroupUser`;
DROP TABLE IF EXISTS `Message`;
DROP TABLE IF EXISTS `MessageUser`;
DROP TABLE IF EXISTS `Merchant`;
DROP TABLE IF EXISTS `Item`;
DROP TABLE IF EXISTS `OrderUser`;
DROP TABLE IF EXISTS `OrderMerchant`;
DROP TABLE IF EXISTS `OrderDetail`;
SET FOREIGN_KEY_CHECKS=1;

CREATE TABLE `User`(
  `id` VARCHAR (128) NOT NULL,  /* cognito id */
  `facebookId` VARCHAR (128) NOT NULL, /* we will relax this constraint for guest user */
  `googlePlusId` VARCHAR(128),

  /* system info epoch */
  `createdOn` INT UNSIGNED NOT NULL,
  `updatedOn` INT UNSIGNED,
  `deletedOn` INT UNSIGNED,
  
  PRIMARY KEY (`id`),
  INDEX `idx_delete` (`deletedOn`)
)
  ENGINE =InnoDB
  DEFAULT CHARSET =utf8
  CHARACTER SET utf8
  COLLATE utf8_general_ci;
  
CREATE TABLE `Facebook` (
  `id` VARCHAR (128) NOT NULL,
  `userId` VARCHAR (128) NOT NULL,
  `token` VARCHAR (1024) NOT NULL,
  `firstName` VARCHAR (128) NOT NULL,
  `lastName` VARCHAR (128) NOT NULL,
  `email` VARCHAR (128),
  
    /* system info epoch */
  `createdOn` INT UNSIGNED NOT NULL,
  `updatedOn` INT UNSIGNED,
  `deletedOn` INT UNSIGNED,

  PRIMARY KEY (`id`),
  FOREIGN KEY (`userId`) REFERENCES `User`(`id`),
  INDEX `idx_email` (`email`)
)
  ENGINE =InnoDB
  DEFAULT CHARSET =utf8
  CHARACTER SET utf8
  COLLATE utf8_general_ci;

CREATE TABLE `Friend`(
  `userId`	VARCHAR (128) NOT NULL,
  `friendId`	VARCHAR (128) NOT NULL,
  `socialNetwork`	VARCHAR (128) NOT NULL,
  `relationship` VARCHAR (128),
  
     /* system info epoch */
  `createdOn` INT UNSIGNED NOT NULL,
  `updatedOn` INT UNSIGNED,
  `deletedOn` INT UNSIGNED,
  
  	PRIMARY KEY (`userId`, `friendId`, `socialNetwork`),
  	FOREIGN KEY (`userId`) REFERENCES `User`(`id`),
  	FOREIGN KEY (`friendId`) REFERENCES `User`(`id`)
)
  ENGINE =InnoDB
  DEFAULT CHARSET =utf8
  CHARACTER SET utf8
  COLLATE utf8_general_ci;

CREATE TABLE `Merchant`(
  `id`                  INT UNSIGNED AUTO_INCREMENT,
  `name`                VARCHAR (128) NOT NULL,
  `branch`              VARCHAR (128),
  `description`         VARCHAR (512),
  `address`             VARCHAR(512) NOT NULL,
  `latitude`            FLOAT(10, 6) NOT NULL,
  `longitude`           FLOAT(10, 6) NOT NULL,
  `deliverDistanceKm`   INT,
  `minimumOrder`        FLOAT(7, 2),  /* max 10K NTD */
  `minimumDelivery`     FLOAT(7, 2),
  `mainPhone`           VARCHAR (32) NOT NULL, /* for call */
  `mobilePhone`         VARCHAR (32),          /* for text */

  /* TODO define OrderSubmission json to interact with merchant (send/confirm) */
  `orderSubmissionJson` VARCHAR (1024),

  /* TODO define MerchantImage json and use S3 to persist images */
  /* e.g. {"logoImage":"http://....", "avtarImage":"http://...."} */
  `imageJson`           VARCHAR (1024),

  /* TODO define MerchantFeedback to provide ratings or expensiveness */
  `feedbackJson`        VARCHAR (1024),

  /* TODO other contact info like lineId, whatsAppId, POS system to send/confirm orders */

  /* system info epoch */
  `createdOn` INT UNSIGNED NOT NULL,
  `updatedOn` INT UNSIGNED,
  `deletedOn` INT UNSIGNED,
  
  PRIMARY KEY (`id`),
  INDEX `idx_name` (`name`),
  INDEX `idx_lat` (`latitude`),
  INDEX `idx_long` (`longitude`)
)
  ENGINE =InnoDB
  DEFAULT CHARSET =utf8
  CHARACTER SET utf8
  COLLATE utf8_general_ci
  AUTO_INCREMENT =1;

CREATE TABLE `Item`(
  `id`           INT UNSIGNED AUTO_INCREMENT,
  `merchantId`   INT UNSIGNED NOT NULL,
  `title`        VARCHAR (128) NOT NULL,
  `description`  VARCHAR (512) NOT NULL,
  `unitPrice`    FLOAT (7, 2) NOT NULL,
  `dailyLimit`   INT,
  `weight`	INT,

  /* TODO define ItemImage json and use S3 to persist images */
  `imageJson`    VARCHAR (1024),

  /* TODO define ItemFeedback to provide ratings and other feedback */
  `feedbackJson` VARCHAR (1024),
  
  /* system info epoch */
  `createdOn` INT UNSIGNED NOT NULL,
  `updatedOn` INT UNSIGNED,
  `deletedOn` INT UNSIGNED,
  
  PRIMARY KEY (`id`),
  FOREIGN KEY (`merchantId`) REFERENCES `Merchant`(`id`),
  INDEX `idx_title` (`title`)
)
  ENGINE =InnoDB
  DEFAULT CHARSET =utf8
  CHARACTER SET utf8
  COLLATE utf8_general_ci
  AUTO_INCREMENT =1;

CREATE TABLE `Order`(
  `id`           INT UNSIGNED AUTO_INCREMENT,
  `organizerId` VARCHAR (128) NOT NULL,
  `name` VARCHAR(256) NOT NULL,
  `description` VARCHAR(512),
  `deliveryAddress` VARCHAR(512) NOT NULL,
  `deliveryLatitude` FLOAT(10, 6) NOT NULL,
  `deliveryLongitude` FLOAT(10, 6) NOT NULL,
  `status` VARCHAR(64),
  `timeLimit`	INT,

  /* system info epoch */
  `createdOn` INT UNSIGNED NOT NULL,
  `updatedOn` INT UNSIGNED,
  `deletedOn` INT UNSIGNED,

  PRIMARY KEY (`id`),
  FOREIGN KEY (`organizerId`) REFERENCES `User`(`id`),
  INDEX `idx_lat` (`deliveryLatitude`),
  INDEX `idx_long` (`deliveryLongitude`)
)
  ENGINE =InnoDB
  DEFAULT CHARSET =utf8
  CHARACTER SET utf8
  COLLATE utf8_general_ci
  AUTO_INCREMENT =1;
  
/**
 * can be hand selected by organizer of order
 * can be imported from previously saved groups
 * all the users associated with the order will be given a temporary group to discuss order
 */
CREATE TABLE `OrderUser` (
  `orderId` INT UNSIGNED NOT NULL,
  `userId` VARCHAR (128) NOT NULL,
  `status` VARCHAR (128) NOT NULL,

  /* system info epoch */
  `createdOn` INT UNSIGNED NOT NULL,
  `updatedOn` INT UNSIGNED,
  `deletedOn` INT UNSIGNED,

  PRIMARY KEY (`orderId`, `userId`),
  FOREIGN KEY (`orderId`) REFERENCES `Order`(`id`),
  FOREIGN KEY (`userId`) REFERENCES `User`(`id`),
  INDEX `idx_delete` (`deletedOn`)
)
  ENGINE =InnoDB
  DEFAULT CHARSET =utf8
  CHARACTER SET utf8
  COLLATE utf8_general_ci;

/**
 * can be used by organizer of an order to propose merchants
 * once order status is from propose -> create, users can order items against merchants
 */
CREATE TABLE `OrderMerchant` (
  `orderId`    INT UNSIGNED NOT NULL,
  `merchantId` INT UNSIGNED NOT NULL,

  /* system info epoch */
  `createdOn` INT UNSIGNED NOT NULL,
  `updatedOn` INT UNSIGNED,
  `deletedOn` INT UNSIGNED,

  PRIMARY KEY (`orderId`, `merchantId`),
  FOREIGN KEY (`orderId`) REFERENCES `Order`(`id`),
  FOREIGN KEY (`merchantId`) REFERENCES `Merchant`(`id`),
  INDEX `idx_delete` (`deletedOn`)
)
  ENGINE =InnoDB
  DEFAULT CHARSET =utf8
  CHARACTER SET utf8
  COLLATE utf8_general_ci;

/**
 * grand finale final fact table to join all dimensions
 */
CREATE TABLE `OrderDetail` (
  `userId`     VARCHAR (128) NOT NULL,
  `orderId`    INT UNSIGNED NOT NULL,
  `merchantId` INT UNSIGNED NOT NULL,
  `itemId`     INT UNSIGNED NOT NULL,

  `quantity`   INT NOT NULL,
  `status`     VARCHAR (64), /* TODO delivered, paid, etc. */

  /* system info epoch */
  `createdOn` INT UNSIGNED NOT NULL,
  `updatedOn` INT UNSIGNED,
  `deletedOn` INT UNSIGNED,

  PRIMARY KEY (`userId`, `orderId`, `merchantId`, `itemId`),
  FOREIGN KEY (`userId`) REFERENCES `User`(`id`),
  FOREIGN KEY (`orderId`) REFERENCES `Order`(`id`),
  FOREIGN KEY (`merchantId`) REFERENCES `Merchant`(`id`),
  FOREIGN KEY (`itemId`) REFERENCES `Item`(`id`),

  INDEX `idx_delete` (`deletedOn`)
)
  ENGINE =InnoDB
  DEFAULT CHARSET =utf8
  CHARACTER SET utf8
  COLLATE utf8_general_ci;

CREATE TABLE `Group` (
  `id`      INT UNSIGNED AUTO_INCREMENT,
  `organizerId` VARCHAR (128) NOT NULL,
  `name` VARCHAR(256) NOT NULL,
  `description` VARCHAR(512),
  `orderId` INT UNSIGNED,
  `status` VARCHAR (64),

  /* system info epoch */
  `createdOn` INT UNSIGNED NOT NULL,
  `updatedOn` INT UNSIGNED,
  `deletedOn` INT UNSIGNED,

  PRIMARY KEY (`id`),
  FOREIGN KEY (`orderId`) REFERENCES `Order`(`id`),
  FOREIGN KEY (`organizerId`) REFERENCES `User`(`id`),
  INDEX `idx_status` (`status`),
  INDEX `idx_delete` (`deletedOn`)
)
  ENGINE =InnoDB
  DEFAULT CHARSET =utf8
  CHARACTER SET utf8
  COLLATE utf8_general_ci
  AUTO_INCREMENT =1;

CREATE TABLE `GroupUser` (
  `groupId` INT UNSIGNED NOT NULL,
  `userId` VARCHAR (128) NOT NULL,

  /* system info epoch */
  `createdOn` INT UNSIGNED NOT NULL,
  `updatedOn` INT UNSIGNED,
  `deletedOn` INT UNSIGNED,

  PRIMARY KEY (`groupId`, `userId`),
  FOREIGN KEY (`groupId`) REFERENCES `Group`(`id`),
  FOREIGN KEY (`userId`) REFERENCES `User`(`id`),
  INDEX `idx_delete` (`deletedOn`)
)
  ENGINE =InnoDB
  DEFAULT CHARSET =utf8
  CHARACTER SET utf8
  COLLATE utf8_general_ci;

CREATE TABLE `Message` (
  `id` INT UNSIGNED AUTO_INCREMENT,
  `groupId` INT UNSIGNED NOT NULL,
  `authorId` VARCHAR (128) NOT NULL,
  `content` VARCHAR(2048) NOT NULL,

  /* system info epoch */
  `createdOn` INT UNSIGNED NOT NULL,    /* this is sent time */
  `updatedOn` INT UNSIGNED,
  `deletedOn` INT UNSIGNED,

  PRIMARY KEY (`id`),
  FOREIGN KEY (`groupId`) REFERENCES `Group`(`id`),
  FOREIGN KEY (`authorId`) REFERENCES `User`(`id`),

  INDEX `idx_create` (`createdOn`),
  INDEX `idx_delete` (`deletedOn`)
)
  ENGINE =InnoDB
  DEFAULT CHARSET =utf8
  CHARACTER SET utf8
  COLLATE utf8_general_ci
  AUTO_INCREMENT =1;

CREATE TABLE `MessageUser` (
  `messageId` INT UNSIGNED NOT NULL,
  `userId` VARCHAR (128) NOT NULL,

  /* system info epoch */
  `createdOn` INT UNSIGNED NOT NULL,    /*this is read time */
  `updatedOn` INT UNSIGNED,
  `deletedOn` INT UNSIGNED,

  PRIMARY KEY (`messageId`, `userId`),
  FOREIGN KEY (`messageId`) REFERENCES `Message`(`id`),
  FOREIGN KEY (`userId`) REFERENCES `User`(`id`),

  INDEX `idx_create` (`createdOn`),
  INDEX `idx_delete` (`deletedOn`)
)
  ENGINE =InnoDB
  DEFAULT CHARSET =utf8
  CHARACTER SET utf8
  COLLATE utf8_general_ci;

/* DML bootstrap test data */
insert into `User`(`id`, `facebookId`, `createdOn`) values ('sansaId', 'sansaFbId', UNIX_TIMESTAMP());
insert into `User`(`id`, `facebookId`, `createdOn`) values ('snowId', 'snowFbId', UNIX_TIMESTAMP());
insert into `User`(`id`, `facebookId`, `createdOn`) values ('bobId', 'bobFbId', UNIX_TIMESTAMP());
insert into `User`(`id`, `facebookId`, `createdOn`) values ('aryaId', 'aryaFbId', UNIX_TIMESTAMP());
insert into `User`(`id`, `facebookId`, `createdOn`) values ('williamId', 'williamFbId', UNIX_TIMESTAMP());

insert into `Friend`(`userId`, `friendId`,  `socialNetwork`, `relationship`, `createdOn`) values ('sansaId', 'snowId', 'Facebook','Friend', UNIX_TIMESTAMP());
insert into `Friend`(`userId`, `friendId`,  `socialNetwork`, `relationship`, `createdOn`) values ('sansaId', 'bobId', 'Facebook','Friend', UNIX_TIMESTAMP());
insert into `Friend`(`userId`, `friendId`,  `socialNetwork`, `relationship`, `createdOn`) values ('sansaId', 'aryaId', 'Facebook','Friend', UNIX_TIMESTAMP());
insert into `Friend`(`userId`, `friendId`,  `socialNetwork`, `relationship`, `createdOn`) values ('sansaId', 'williamId', 'Facebook','Friend', UNIX_TIMESTAMP());
insert into `Friend`(`userId`, `friendId`,  `socialNetwork`, `relationship`, `createdOn`) values ('snowId', 'sansaId', 'Facebook','Friend', UNIX_TIMESTAMP());
insert into `Friend`(`userId`, `friendId`,  `socialNetwork`, `relationship`, `createdOn`) values ('bobId', 'sansaId', 'Facebook','Friend', UNIX_TIMESTAMP());
insert into `Friend`(`userId`, `friendId`,  `socialNetwork`, `relationship`, `createdOn`) values ('aryaId', 'sansaId', 'Facebook','Friend', UNIX_TIMESTAMP());
insert into `Friend`(`userId`, `friendId`,  `socialNetwork`, `relationship`, `createdOn`) values ('williamId', 'sansaId', 'Facebook','Friend', UNIX_TIMESTAMP());

insert into Merchant (id, name, branch, description, address, latitude, longitude, deliverDistanceKm, minimumOrder, minimumDelivery, mainPhone, mobilePhone, orderSubmissionJson, imageJson, feedbackJson, createdOn) values (NULL, 'Merit Vegetarian', '', 'To a fresh, culinary, vegan delight every time you visit!', '548 Lawrence Expy, Sunnyvale, CA 94085','37.384839', '-121.995173', '20', '5', '50', '(408) 245-8988', '', 'phone', 'http://veggiebucks.com/wp-content/themes/directorypress/thumbs/merit-vegetarian-cuisine-sunnyvale.jpg', '4', UNIX_TIMESTAMP());
insert into Item (id, merchantId, title, description, unitPrice, dailyLimit, weight, imageJson, feedbackJson, createdOn) values (NULL, '1', 'Curry Vermicelli Soup', 'Vermicelli, sweet potato, organic tofu, and soy protein in rich coconut curry soup.', '7.50', '20', '8', 'http://www.meritvegetarian.com/media/k2/items/cache/47e29f9fe96a1771642fb05ac8a8fd00_L.jpg', '4', UNIX_TIMESTAMP());
insert into Item (id, merchantId, title, description, unitPrice, dailyLimit, weight, imageJson, feedbackJson, createdOn) values (NULL, '1', 'Spicy Thai Fried Rice', 'Fried rice with tofu, soy protein, organic mixed vegetables, bean sprouts, basil and Thai spices.', '9.00', '20', '7', 'http://www.meritvegetarian.com/media/k2/items/cache/048731097de322302aff7e52151c991d_L.jpg', '4', UNIX_TIMESTAMP());
insert into Item (id, merchantId, title, description, unitPrice, dailyLimit, weight, imageJson, feedbackJson, createdOn) values (NULL, '1', 'Pot Stickers', 'Yummy fried dumplings filled with tofu, fresh vegetables, and ginger.', '6.00', '30', '6', 'http://www.meritvegetarian.com/media/k2/items/cache/948378d6a67ac0d7c7c6728581b072ab_L.jpg', '3', UNIX_TIMESTAMP());

insert into Merchant (id, name, branch, description, address, latitude, longitude, deliverDistanceKm, minimumOrder, minimumDelivery, mainPhone, mobilePhone, orderSubmissionJson, imageJson, feedbackJson, createdOn) values (NULL, 'Vegetarian House', '', 'Come and enjoy the refreshing fresh flavors of our Celestial Selections.', '520 E Santa Clara St, San Jose, CA 95112','37.341347', '-121.879048', '25', '10', '100', '(408) 292-3798', '', 'phone', 'http://lh6.ggpht.com/_15SvCEAbtpc/Sm98W0bfsbI/AAAAAAAAAy4/JxJjiLqZacM/DSC04759.jpg', '4', UNIX_TIMESTAMP());
insert into Item (id, merchantId, title, description, unitPrice, dailyLimit, weight, imageJson, feedbackJson, createdOn) values (NULL, '2', 'Spring Rolls', 'Crispy deep-fried rolls with carrots, peas, jicama, taro, tofu and bean thread. Served with an au lac dipping sauce.', '6.50', '20', '5', 'http://www.vegetarianhouse.us/uploads/1/9/9/8/19984411/__6391796_orig.jpg', '4', UNIX_TIMESTAMP());
insert into Item (id, merchantId, title, description, unitPrice, dailyLimit, weight, imageJson, feedbackJson, createdOn) values (NULL, '2', 'Formosa Noodle Soup', 'Wheat noodles with soy protein, mushrooms, carrots, bok choy, onion and pickled cabbage.', '8.95', '20', '9', 'http://www.vegetarianhouse.us/uploads/1/9/9/8/19984411/_____9282916_orig.jpg', '5', UNIX_TIMESTAMP());
insert into Item (id, merchantId, title, description, unitPrice, dailyLimit, weight, imageJson, feedbackJson, createdOn) values (NULL, '2', 'Happy Crispy Rice', 'Gluten-free. Fried rice with vegetables, tofu, shiitake mushrooms and ginger cooked golden brown in a metal pot.', '11.50', '15', '8', 'http://www.vegetarianhouse.us/uploads/1/9/9/8/19984411/__________7790367_orig.jpg', '3', UNIX_TIMESTAMP());

insert into Merchant (id, name, branch, description, address, latitude, longitude, deliverDistanceKm, minimumOrder, minimumDelivery, mainPhone, mobilePhone, orderSubmissionJson, imageJson, feedbackJson, createdOn) values (NULL, '御香齋', '', '現今文明社會裡很多人在日常生活中享受美食的同時，都追求健康養生美味以及大環境的保護；愛護動物，不殺生為前提。御香齋素食餐館，精心亨調健康美味的蔬食料理，以貼近每位到本餐館用餐者的心，滿足顧客的味蕾。', '1071 South De Anza Boulevard, San Jose, CA 95129','37.307466', '-122.032901', '30', '3', '30', '(408) 863-0707', '', 'phone', 'http://greenmenu.org/wp-content/uploads/ca-royal-greens.jpg', '2', UNIX_TIMESTAMP());
insert into Item (id, merchantId, title, description, unitPrice, dailyLimit, weight, imageJson, feedbackJson, createdOn) values (NULL, '3', '麻婆豆腐', 'Szechuan Tofu', '7.95', '20', '8', 'http://www.sydneytoday.com/upload_files/p8_news_/97/pim94__mabodofu.jpg', '4', UNIX_TIMESTAMP());
insert into Item (id, merchantId, title, description, unitPrice, dailyLimit, weight, imageJson, feedbackJson, createdOn) values (NULL, '3', '御香素鰻飯 (奶素)', 'Royal Greens’ Eel Rice Plate', '9.95', '20', '9', 'http://farm6.static.flickr.com/5253/5449585209_0008b5ec44_o.jpg', '5', UNIX_TIMESTAMP());
insert into Item (id, merchantId, title, description, unitPrice, dailyLimit, weight, imageJson, feedbackJson, createdOn) values (NULL, '3', '回鍋香干', 'Cabbage and Tofu in Royal Greens’ Sauce', '8.95', '15', '8', 'http://www.yuncanyin.cn/upload/201210/201210232042212921.jpg', '3', UNIX_TIMESTAMP());

insert into Merchant (id, name, branch, description, address, latitude, longitude, deliverDistanceKm, minimumOrder, minimumDelivery, mainPhone, mobilePhone, orderSubmissionJson, imageJson, feedbackJson, createdOn) values (NULL, 'Gourmet Vegetarian 御素坊', '', '位於聖蓋博市全統廣場二樓的“御素坊”健康素食餐廳，日前被美國主流媒體—“L.A. Magazine” 評選為洛杉磯地區最佳前十名的中餐廳。而“御素坊”則是唯一入選的素菜館。', '140 W. Valley Blvd., #222,San Gabriel,CA 91776','34.077666', '-118.100871', '30', '20', '200', '(626) 280-5998', '', 'phone', 'http://pic.pimg.tw/kjchen69/4b36a37f37dbd.jpg', '2', UNIX_TIMESTAMP());
insert into Item (id, merchantId, title, description, unitPrice, dailyLimit, weight, imageJson, feedbackJson, createdOn) values (NULL, '4', '泰式奶茶', 'Thai Ice Tea', '2.25', '100', '4', 'http://kirbiecravings.com/wp-content/uploads/2013/08/thai-iced-tea-2.jpg', '4', UNIX_TIMESTAMP());
insert into Item (id, merchantId, title, description, unitPrice, dailyLimit, weight, imageJson, feedbackJson, createdOn) values (NULL, '4', '南瓜豆腐煲', 'Soft Tofu and Pumpkin with Brown Sauce in Hot Pot', '6.50', '20', '5', 'http://farm3.static.flickr.com/2453/3847837054_35bb8be894.jpg', '2', UNIX_TIMESTAMP());
insert into Item (id, merchantId, title, description, unitPrice, dailyLimit, weight, imageJson, feedbackJson, createdOn) values (NULL, '4', '山葯黑椒魚片', 'Vege Fish and Black Pepper Sauce with Yam', '6.95', '20', '8', 'http://farm3.static.flickr.com/2608/3847047479_4c38a4b551.jpg', '3', UNIX_TIMESTAMP());

insert into Merchant (id, name, branch, description, address, latitude, longitude, deliverDistanceKm, minimumOrder, minimumDelivery, mainPhone, mobilePhone, orderSubmissionJson, imageJson, feedbackJson, createdOn) values (NULL, '寬心園精緻蔬食', '', '和緣餐飲於2003年在台中成立第一家『寬心園精緻蔬食料理』－台中大業店，這些年『寬心園精緻蔬食料理』陸續開設了其它分店，目前全省共有12家直營店－分別位於台北、桃園、新竹、台中、台南、高雄，提供同樣優質的服務和品質一致的餐點給所有喜愛蔬食的朋友。', '台北市仁愛路四段345巷4弄51號','25.039058', '121.550879', '15', '20', '10000', '27521448', '', 'phone', 'http://www.easyhouse.tw/swf/visit/p1/p3.jpg', '2', UNIX_TIMESTAMP());
insert into Item (id, merchantId, title, description, unitPrice, dailyLimit, weight, imageJson, feedbackJson, createdOn) values (NULL, '5', '川燒豆腐煲', '採用薄皮鐵心的錦油豆腐製作', '580', '10', '4', 'http://www.easyhouse.tw/images/meal/food/fooe_01_1b.jpg', '4', UNIX_TIMESTAMP());
insert into Item (id, merchantId, title, description, unitPrice, dailyLimit, weight, imageJson, feedbackJson, createdOn) values (NULL, '5', '百菇松茸飯', '選用義大利松露白蘑菇醬及多種菇類烹煮而成', '580', '10', '5', 'http://www.easyhouse.tw/images/meal/food/fooe_03_2b.jpg', '5', UNIX_TIMESTAMP());
insert into Item (id, merchantId, title, description, unitPrice, dailyLimit, weight, imageJson, feedbackJson, createdOn) values (NULL, '5', '奶油蔬菜鍋', '巴西蘑菇熬煮的牛奶高湯', '580', '10', '8', 'http://www.easyhouse.tw/images/meal/food/fooe_05_1b.jpg', '3', UNIX_TIMESTAMP());

