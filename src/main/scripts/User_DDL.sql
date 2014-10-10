DROP TABLE IF EXISTS `contact`;
CREATE TABLE `contact` (
  `id`      INT UNSIGNED AUTO_INCREMENT,
  `firstName` VARCHAR(255),
  `lastName`  VARCHAR(255),
  `phone`     VARCHAR(30),
  PRIMARY KEY (`id`),
  INDEX `idx_phone` (`phone`)
)
  ENGINE =InnoDB
  DEFAULT CHARSET =utf8
  CHARACTER SET utf8
  COLLATE utf8_general_ci
  AUTO_INCREMENT =1;