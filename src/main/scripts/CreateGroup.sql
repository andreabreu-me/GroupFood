SET FOREIGN_KEY_CHECKS=0;
DROP TABLE IF EXISTS `User`;
DROP TABLE IF EXISTS `Facebook`;
DROP TABLE IF EXISTS `Order`;
DROP TABLE IF EXISTS `Group`;
DROP TABLE IF EXISTS `GroupUser`;
DROP TABLE IF EXISTS `Group_User`; /* delete previous group_user table TODO remove this line */
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

  PRIMARY KEY (`id`),
  FOREIGN KEY (`userId`) REFERENCES `User`(`id`),
  INDEX `idx_email` (`email`)
)
  ENGINE =InnoDB
  DEFAULT CHARSET =utf8
  CHARACTER SET utf8
  COLLATE utf8_general_ci;

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

/* DML bootstrap test data */
insert into `User`(`id`, `facebookId`, `createdOn`) values ('sansaid', 'sansafbid', UNIX_TIMESTAMP());
insert into `User`(`id`, `facebookId`, `createdOn`) values ('snowid', 'snowfbid', UNIX_TIMESTAMP());

insert into `Facebook`(`id`, `userId`, `token`, `firstName`, `lastName`, `email`)
values ('sansafbid','sansaid','token1234', 'sansa', '史塔克', 'sansa@stark.com');
insert into `Facebook`(`id`, `userId`, `token`, `firstName`, `lastName`, `email`)
values ('snowfbid','snowid','token5678', '約翰', 'snow', 'jon@snow.com');

/*
insert into `group`(`organizer_id`,`name`,`description`,`created_on`)
    values ('sansaid','午餐','my lunch group', UNIX_TIMESTAMP());

insert into `group_user`(`group_id`,`user_id`,`created_on`) values (1, 'sansaid', UNIX_TIMESTAMP());
insert into `group_user`(`group_id`,`user_id`,`created_on`) values (1, 'snowid', UNIX_TIMESTAMP());
*/