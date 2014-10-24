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
  `deliveryAddress` VARCHAR(512) NOT NULL,
  `deliveryLatitude` FLOAT(10, 6) NOT NULL,
  `deliveryLongitude` FLOAT(10, 6) NOT NULL,
  `status` VARCHAR(64),

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


insert into `Facebook`(`id`, `userId`, `token`, `firstName`, `lastName`, `email`, `createdOn`)
values ('sansaFbId','sansaId','tokenSansa', 'Sansa', '史塔克', 'sansa@stark.com', UNIX_TIMESTAMP());
insert into `Facebook`(`id`, `userId`, `token`, `firstName`, `lastName`, `email`, `createdOn`)
values ('snowFbId','snowId','tokenSnow', '約翰', 'snow', 'jon@snow.com', UNIX_TIMESTAMP());
insert into `Facebook`(`id`, `userId`, `token`, `firstName`, `lastName`, `email`, `createdOn`)
values ('bobFbId','bobId','tokenBob', 'Bob', '克萊格', 'bob@cg.com', UNIX_TIMESTAMP());
insert into `Facebook`(`id`, `userId`, `token`, `firstName`, `lastName`, `email`, `createdOn`)
values ('aryaFbId','aryaId','tokenArya', 'Arya', '史塔克', 'arya@stark.com', UNIX_TIMESTAMP());
insert into `Facebook`(`id`, `userId`, `token`, `firstName`, `lastName`, `email`, `createdOn`)
values ('williamFbId','williamId','tokenWilliam', 'william', '劉', 'william@liu.com', UNIX_TIMESTAMP());

/*
insert into `Group`(`organizerId`,`name`,`description`,`createdOn`)
    values ('sansaid','午餐','my lunch group', UNIX_TIMESTAMP());

insert into `group_user`(`group_id`,`user_id`,`created_on`) values (1, 'sansaid', UNIX_TIMESTAMP());
insert into `group_user`(`group_id`,`user_id`,`created_on`) values (1, 'snowid', UNIX_TIMESTAMP());
*/