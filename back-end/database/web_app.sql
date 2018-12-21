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
  but a encrypted value that we (the server) receive from the use of https protocol */
  password VARCHAR(20) NOT NULL /* At least 10 characters */
);


CREATE TABLE IF NOT EXISTS product (
  id INT AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(20) NOT NULL UNIQUE, /* Probably related to the barcode, a unique identifier */
  description VARCHAR(100) NOT NULL, /* A short description that will be displayed for every product */
  /*company VARCHAR(20) NOT NULL,*/
  category VARCHAR(20) NOT NULL,
  stars DECIMAL(2,1) NOT NULL
);

/* Tags will be used as key words */

/* We use a seperate table in order to ensure
that our relation is in Normal Form */

CREATE TABLE IF NOT EXISTS product_tags (
  id INT AUTO_INCREMENT PRIMARY KEY,
  FOREIGN KEY product_id(id)
  REFERENCES product(id),
  tag VARCHAR(20)
);

/* Some extra data for every product */

CREATE TABLE IF NOT EXISTS product_data (
  id INT AUTO_INCREMENT PRIMARY KEY,
  FOREIGN KEY product_id(id)
  REFERENCES product(id),
  data VARCHAR(20)
);


CREATE TABLE IF NOT EXISTS store (
  id INT AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(20) NOT NULL,
  address VARCHAR(30) NOT NULL, /* Full address of the store */
  lat DECIMAL(10,8) NOT NULL,
  lng DECIMAL(11,8) NOT NULL
);

CREATE TABLE IF NOT EXISTS store_tags (
  id INT AUTO_INCREMENT PRIMARY KEY,
  FOREIGN KEY store_id(id)
  REFERENCES store(id),
  tag VARCHAR(20)
);


CREATE TABLE IF NOT EXISTS has_product (
  id INT AUTO_INCREMENT PRIMARY KEY,
  FOREIGN KEY user_id(id)
  REFERENCES user(id),
  FOREIGN KEY product_id(id)
  REFERENCES product(id),
  FOREIGN KEY store_id(id)
  REFERENCES store(id),
  price DOUBLE(5,2) NOT NULL,
  date_from DATE NOT NULL, /* format: YYYY-MM-DD */
  date_to DATE NOT NULL,
  stars DECIMAL(2,1) NOT NULL,
  withdrawn BOOLEAN DEFAULT 0
);


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


/* Password constraint, at least 10 characters */

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

/* Stars constraint, values in {0.5, 1.0, 1.5, 2.0, 2.5, 3.0, 3.5, 4.0, 4.5, 5.0} */

DELIMITER $$

CREATE PROCEDURE `check_stars`(IN stars DECIMAL(2,1))
BEGIN
    IF (stars > 5 OR stars < 0.5 OR FLOOR(2*stars) <> 2*stars) THEN
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

INSERT INTO user (first_name, last_name, user_name, email, phone_number, password)
VALUES ('John', 'Doe', 'johnDoe', 'johndoe@ntua.gr', 6946574623, 'somePassword'),
('Freddy', 'Milk', 'freddymilk', 'freddymilk@ntua.gr', 6985475647, 'anotherPassword');
