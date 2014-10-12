SET FOREIGN_KEY_CHECKS=0;
DROP TABLE IF EXISTS `facebook`;
DROP TABLE IF EXISTS `user`;
DROP TABLE IF EXISTS `order`;
DROP TABLE IF EXISTS `group`;
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
  `id` VARCHAR (128) NOT NULL,
  `facebook_id` VARCHAR (128) NOT NULL,
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
  `name` VARCHAR(255) NOT NULL,
  `description` VARCHAR(255),
  `order_id` INT UNSIGNED,
  `status` VARCHAR (64),

  PRIMARY KEY (`id`),
  FOREIGN KEY (`order_id`) REFERENCES `order`(`id`),
  FOREIGN KEY (`organizer_id`) REFERENCES `user`(`id`),
  INDEX `idx_status` (`status`)
)
  ENGINE =InnoDB
  DEFAULT CHARSET =utf8
  CHARACTER SET utf8
  COLLATE utf8_general_ci
  AUTO_INCREMENT =1;

