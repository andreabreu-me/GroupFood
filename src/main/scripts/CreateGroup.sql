SET FOREIGN_KEY_CHECKS=0;
DROP TABLE IF EXISTS `facebook`;
DROP TABLE IF EXISTS `user`;
DROP TABLE IF EXISTS `order`;
DROP TABLE IF EXISTS `group`;
DROP TABLE IF EXISTS `group_user`;
SET FOREIGN_KEY_CHECKS=1;

CREATE TABLE `facebook` (
  `id` VARCHAR (128) NOT NULL,
  `token` VARCHAR (1024) NOT NULL,
  `first_name` VARCHAR (128) NOT NULL,
  `last_name` VARCHAR (128) NOT NULL,
  `email` VARCHAR (128),

  PRIMARY KEY (`id`),
  INDEX `idx_email` (`email`)
)
  ENGINE =InnoDB
  DEFAULT CHARSET =utf8
  CHARACTER SET utf8
  COLLATE utf8_general_ci;

CREATE TABLE `user`(
  `id` VARCHAR (128) NOT NULL,  /* cognito id */
  `facebook_id` VARCHAR (128) NOT NULL, /* we will relax this constraint for guest user */
  `googleplus_id` VARCHAR(128),

  PRIMARY KEY (`id`),
  FOREIGN KEY (`facebook_id`) REFERENCES `facebook`(`id`)
)
  ENGINE =InnoDB
  DEFAULT CHARSET =utf8
  CHARACTER SET utf8
  COLLATE utf8_general_ci;

CREATE TABLE `order`(
  `id`           INT UNSIGNED AUTO_INCREMENT,
  `organizer_id` VARCHAR (128) NOT NULL,
  `delivery_address` VARCHAR(512) NOT NULL,
  `delivery_latitude` FLOAT(10, 6) NOT NULL,
  `delivery_longitude` FLOAT(10, 6) NOT NULL,
  `status` VARCHAR(64),

  PRIMARY KEY (`id`),
  FOREIGN KEY (`organizer_id`) REFERENCES `user`(`id`),
  INDEX `idx_lat` (`delivery_latitude`),
  INDEX `idx_long` (`delivery_longitude`)
)
  ENGINE =InnoDB
  DEFAULT CHARSET =utf8
  CHARACTER SET utf8
  COLLATE utf8_general_ci
  AUTO_INCREMENT =1;

CREATE TABLE `group` (
  `id`      INT UNSIGNED AUTO_INCREMENT,
  `organizer_id` VARCHAR (128) NOT NULL,
  `name` VARCHAR(256) NOT NULL,
  `description` VARCHAR(512),
  `order_id` INT UNSIGNED,
  `status` VARCHAR (64),

  /* system info epoch */
  `created_on` INT UNSIGNED NOT NULL,
  `updated_on` INT UNSIGNED,
  `deleted_on` INT UNSIGNED,

  PRIMARY KEY (`id`),
  FOREIGN KEY (`order_id`) REFERENCES `order`(`id`),
  FOREIGN KEY (`organizer_id`) REFERENCES `user`(`id`),
  INDEX `idx_status` (`status`),
  INDEX `idx_delete` (`deleted_on`)
)
  ENGINE =InnoDB
  DEFAULT CHARSET =utf8
  CHARACTER SET utf8
  COLLATE utf8_general_ci
  AUTO_INCREMENT =1;

CREATE TABLE `group_user` (
  `group_id` INT UNSIGNED NOT NULL,
  `user_id` VARCHAR (128) NOT NULL,

  /* system info epoch */
  `created_on` INT UNSIGNED NOT NULL,
  `updated_on` INT UNSIGNED,
  `deleted_on` INT UNSIGNED,

  PRIMARY KEY (`group_id`, `user_id`),
  FOREIGN KEY (`group_id`) REFERENCES `group`(`id`),
  FOREIGN KEY (`user_id`) REFERENCES `user`(`id`),
  INDEX `idx_delete` (`deleted_on`)
)
  ENGINE =InnoDB
  DEFAULT CHARSET =utf8
  CHARACTER SET utf8
  COLLATE utf8_general_ci;

/* DML bootstrap test data */
insert into `facebook`(`id`, `token`, `first_name`, `last_name`, `email`)
    values ('sansafbid','token1234', 'sansa', '史塔克', 'sansa@stark.com');
insert into `facebook`(`id`, `token`, `first_name`, `last_name`, `email`)
    values ('snowfbid','token5678', '約翰', 'snow', 'jon@snow.com');

insert into `user`(`id`, `facebook_id`) values ('sansaid', 'sansafbid');
insert into `user`(`id`, `facebook_id`) values ('snowid', 'snowfbid');

/*
insert into `group`(`organizer_id`,`name`,`description`,`created_on`)
    values ('sansaid','午餐','my lunch group', UNIX_TIMESTAMP());

insert into `group_user`(`group_id`,`user_id`,`created_on`) values (1, 'sansaid', UNIX_TIMESTAMP());
insert into `group_user`(`group_id`,`user_id`,`created_on`) values (1, 'snowid', UNIX_TIMESTAMP());
*/