SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT;
SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS;
SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION;
SET NAMES utf8;
SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO';
SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0;
SET FOREIGN_KEY_CHECKS = 0;

SET NAMES 'utf8';
SET CHARACTER SET utf8;

DROP DATABASE web_app;

CREATE DATABASE IF NOT EXISTS web_app;

USE web_app;

/* User could have some additional optional information, like a sort bio or a profile image */


CREATE TABLE IF NOT EXISTS user (
  id INT AUTO_INCREMENT PRIMARY KEY, /* Not visible to the user */
  first_name VARCHAR(15) NOT NULL,  /* first_name and last_name contain only alphabetic characters */
  last_name VARCHAR(20) NOT NULL,
  user_name VARCHAR(20) UNIQUE, /* Should be unique */
  email VARCHAR(30) NOT NULL UNIQUE,
  phone_number BIGINT UNIQUE,  /* exactly 10 digits */
  /* The field password dosen't have the actual user's password,
  but a encrypted value */
  password VARCHAR(256) NOT NULL,

  /* Some addition optional fields */

  age TINYINT,
  university VARCHAR(20),
  profession VARCHAR(25),
  city VARCHAR(20),
  website VARCHAR(50)
);

ALTER TABLE user AUTO_INCREMENT = 10;

CREATE TABLE IF NOT EXISTS product (
  id INT AUTO_INCREMENT PRIMARY KEY,

  /* There are several ways of representing the barcode */

  barcode VARCHAR(20) UNIQUE,
  name VARCHAR(50) NOT NULL, /* A name to identify the product */
  description VARCHAR(100) NOT NULL, /* A short description that will be displayed for every product */
  manufacturer VARCHAR(20),
  category VARCHAR(50) NOT NULL,
  stars DECIMAL(4,1),
  number_of_ratings INT,
  image_url VARCHAR(2048),
  withdrawn BOOLEAN DEFAULT 0 NOT NULL
);

/* Tags will be used as key words */

/* We use a seperate table in order to ensure
that our relation is in Normal Form */

CREATE TABLE IF NOT EXISTS product_tags (
  id INT AUTO_INCREMENT PRIMARY KEY,
  product_id INT NOT NULL,
  FOREIGN KEY (product_id)
  REFERENCES product(id)
  ON DELETE CASCADE,
  tag VARCHAR(20)
);


/* Some extra data for every product */

/*

CREATE TABLE IF NOT EXISTS product_data (
  id INT AUTO_INCREMENT PRIMARY KEY,
  product_id INT NOT NULL,
  FOREIGN KEY product_id(id)
  REFERENCES product(id)
  ON DELETE CASCADE,
  data VARCHAR(20)
);

*/

CREATE TABLE IF NOT EXISTS store (
  id INT AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(50) NOT NULL,
  address VARCHAR(75),
  lat DECIMAL(16,14) NOT NULL,
  lng DECIMAL(16,14) NOT NULL,
  withdrawn BOOLEAN DEFAULT 0 NOT NULL
);



CREATE TABLE IF NOT EXISTS store_tags (
  id INT AUTO_INCREMENT PRIMARY KEY,
  store_id INT NOT NULL,
  FOREIGN KEY (store_id)
  REFERENCES store(id)
  ON DELETE CASCADE,
  tag VARCHAR(20)
);



CREATE TABLE IF NOT EXISTS has_product (
  id INT AUTO_INCREMENT PRIMARY KEY,
  user_id INT,
  product_id INT NOT NULL,
  store_id INT NOT NULL,
  price  DOUBLE(8,2) NOT NULL,
  date_from DATE,
  date_to DATE,
  withdrawn BOOLEAN DEFAULT 0,
  FOREIGN KEY (user_id)
  REFERENCES user(id),
  FOREIGN KEY (product_id)
  REFERENCES product(id)
  ON DELETE CASCADE,
  FOREIGN KEY (store_id)
  REFERENCES store(id)
  ON DELETE CASCADE
) ENGINE=INNODB;

CREATE INDEX index_tag ON product_tags (tag);



/* First name constraint, only alphabetic characters are allowed */

DELIMITER $$

CREATE PROCEDURE `check_first_name`(IN first_name VARCHAR(15))
BEGIN
    IF (NOT(first_name REGEXP '^[A-Za-z]+$')) THEN
        SIGNAL SQLSTATE '45000'
            SET MESSAGE_TEXT = 'Check constraint on first_name failed';
    END IF;
END$$

DELIMITER ;

-- Before insert
DELIMITER $$
CREATE TRIGGER `first_name_before_insert` BEFORE INSERT ON `user`
FOR EACH ROW
BEGIN
    CALL check_first_name(new.first_name);
END$$
DELIMITER ;

-- before update
DELIMITER $$
CREATE TRIGGER `first_name_before_update` BEFORE UPDATE ON `user`
FOR EACH ROW
BEGIN
    CALL check_first_name(new.first_name);
END$$
DELIMITER ;

/* Last name constraint, only alphabetic characters are allowed */

DELIMITER $$

CREATE PROCEDURE `check_last_name`(IN last_name VARCHAR(20))
BEGIN
    IF (NOT(last_name REGEXP '^[A-Za-z]+$')) THEN
        SIGNAL SQLSTATE '45000'
            SET MESSAGE_TEXT = 'Check constraint on last_name failed';
    END IF;
END$$

DELIMITER ;

-- Before insert
DELIMITER $$
CREATE TRIGGER `last_name_before_insert` BEFORE INSERT ON `user`
FOR EACH ROW
BEGIN
    CALL check_last_name(new.last_name);
END$$
DELIMITER ;

-- before update
DELIMITER $$
CREATE TRIGGER `last_name_before_update` BEFORE UPDATE ON `user`
FOR EACH ROW
BEGIN
    CALL check_last_name(new.last_name);
END$$
DELIMITER ;

/* Email constraint, ensure that the given value has the right format */

DELIMITER $$

CREATE PROCEDURE `check_email`(IN email VARCHAR(30))
BEGIN
    IF NOT(email like '%_@_%._%') THEN
        SIGNAL SQLSTATE '45000'
            SET MESSAGE_TEXT = 'Check constraint on user.email failed';
    END IF;
END$$

DELIMITER ;

-- Before insert
DELIMITER $$
CREATE TRIGGER `email_before_insert` BEFORE INSERT ON `user`
FOR EACH ROW
BEGIN
    CALL check_email(new.email);
END$$
DELIMITER ;

-- before update
DELIMITER $$
CREATE TRIGGER `email_before_update` BEFORE UPDATE ON `user`
FOR EACH ROW
BEGIN
    CALL check_email(new.email);
END$$
DELIMITER ;

/*
DELIMITER $$

CREATE PROCEDURE `check_password`(IN password VARCHAR(20))
BEGIN
    IF (CHAR_LENGTH(password) < 10) THEN
        SIGNAL SQLSTATE '45000'
            SET MESSAGE_TEXT = 'Check constraint on user.password failed';
    END IF;
END$$

DELIMITER ;

-- Before insert
DELIMITER $$
CREATE TRIGGER `password_before_insert` BEFORE INSERT ON `user`
FOR EACH ROW
BEGIN
    CALL check_password(new.password);
END$$
DELIMITER ;

-- before update
DELIMITER $$
CREATE TRIGGER `password_before_update` BEFORE UPDATE ON `user`
FOR EACH ROW
BEGIN
    CALL check_password(new.password);
END$$
DELIMITER ;
*/

/* Phone number constraint, exactly 10 numbers */

DELIMITER $$

CREATE PROCEDURE `check_phone_number`(IN phone_number BIGINT)
BEGIN
    IF (phone_number > 9999999999 OR phone_number < 1000000000) THEN
        SIGNAL SQLSTATE '45000'
            SET MESSAGE_TEXT = 'Check constraint on user.phone_number failed';
    END IF;
END$$

DELIMITER ;

-- before insert
DELIMITER $$
CREATE TRIGGER `phone_number_before_insert` BEFORE INSERT ON `user`
FOR EACH ROW
BEGIN
    CALL check_phone_number(new.phone_number);
END$$
DELIMITER ;
-- before update
DELIMITER $$
CREATE TRIGGER `phone_number_before_update` BEFORE UPDATE ON `user`
FOR EACH ROW
BEGIN
    CALL check_phone_number(new.phone_number);
END$$
DELIMITER ;

/* Age constraint, only ages between 12 and 125 are allowed */

DELIMITER $$

CREATE PROCEDURE `check_age`(IN age TINYINT)
BEGIN
    IF (age > 125 OR age < 12) THEN
        SIGNAL SQLSTATE '45000'
            SET MESSAGE_TEXT = 'Check constraint on age failed';
    END IF;
END$$

DELIMITER ;

-- Before insert
DELIMITER $$
CREATE TRIGGER `age_before_insert` BEFORE INSERT ON `user`
FOR EACH ROW
BEGIN
    CALL check_age(new.age);
END$$
DELIMITER ;

-- before update
DELIMITER $$
CREATE TRIGGER `age_before_update` BEFORE UPDATE ON `user`
FOR EACH ROW
BEGIN
    CALL check_age(new.age);
END$$
DELIMITER ;

/* Stars constraint, values greater or equal 0.5 and less than equal 5 */

DELIMITER $$

/*
CREATE PROCEDURE `check_stars`(IN stars DECIMAL(4,3))
BEGIN
    IF (stars > 5 OR stars < 0.5) THEN
        SIGNAL SQLSTATE '45000'
            SET MESSAGE_TEXT = 'Check constraint on stars failed';
    END IF;
END$$

DELIMITER ;

-- Before insert
DELIMITER $$
CREATE TRIGGER `stars_before_insert` BEFORE INSERT ON `product`
FOR EACH ROW
BEGIN
    CALL check_stars(new.stars);
END$$
DELIMITER ;

-- before update
DELIMITER $$
CREATE TRIGGER `stars_before_update` BEFORE UPDATE ON `product`
FOR EACH ROW
BEGIN
    CALL check_stars(new.stars);
END$$
DELIMITER ;

*/
/*
insert into product(barcode, name, category, manufacturer, description, stars, number_of_ratings, withdrawn) 
VALUES ("24263474", "DELL laptop", "Technology", "DELL", "A very nice laptop", 4, 1, false),
       ("2347","Huawei Mobile", "Technology", "Huawei", "A very bad mobile", 1.5, 100, false);

insert into product_tags(product_id, tag) 
VALUES (1, "laptop"),
       (1, "DELL"),
       (1,"Technology"),
       (2, "Mobile"),
       (2, "Huawei"),
       (2, "Cheap"),
       (2, "Technology");


*/

insert into user(first_name, last_name, user_name, email, password) 
values ("firstame", "lastName", "username", "el15094@central.tnau.gr", "$2a$12$cNrQsMalfMBYyNs2aQV/Euxq8ZbdmxRrMCbG83KXC0a3XoDXBM7eu");

SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT;
SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS;
SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION;
SET SQL_NOTES=@OLD_SQL_NOTES;
SET FOREIGN_KEY_CHECKS = 1;
