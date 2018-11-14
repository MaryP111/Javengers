
CREATE DATABASE IF NOT EXISTS web_app;

USE web_app;

CREATE TABLE IF NOT EXISTS user (
  id INT AUTO_INCREMENT PRIMARY KEY, /* Not visible to the user */
  first_name VARCHAR(15) NOT NULL,
  last_name VARCHAR(20) NOT NULL,
  user_name VARCHAR(20) UNIQUE, /* Should be unique */
  password VARCHAR(20) NOT NULL,
  email VARCHAR(30) NOT NULL UNIQUE,
  phone_number BIGINT UNIQUE
);

/* Passoword constraint, consists of at least 10 characters */

CREATE TABLE IF NOT EXISTS product (
  id INT AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(20) NOT NULL UNIQUE,
  company VARCHAR(20) NOT NULL,
  category VARCHAR(20) NOT NULL,
  sub_category VARCHAR(20) NOT NULL,
  stars /* 0.5, 1 ... 5 */,
);

CREATE TABLE IF NOT EXISTS store (

  id INT AUTO_INCREMENT PRIMARY_KEY,
  name VARCHAR(20) NOT NULL,
  /* Address */
);

CREATE TABLE IF NOT EXISTS has_product (

  FOREIGN KEY user_id(id)
  REFERENCES user(id),
  FOREIGN KEY product_id(id)
  REFERENCES product(id),
  FOREIGN KEY store_id (id)
  REFERENCES store(id),
  price DOUBLE(5,2) NOT NULL,
  date DATE NOT NULL
  stars NOT NULL /*0.5 1 ...5 */
);







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




INSERT INTO user (first_name, last_name, user_name, password, email, phone_number)
VALUES ('John', 'Doe', 'johnDoe', '1234567891', 'johnDoe@ntua.gr', 1234567891),
('Freddy', 'Milk', 'freddyMilk', '1234567891', 'freddyMilk@ntua.gr', 1234567892);
