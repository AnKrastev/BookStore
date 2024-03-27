SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema mydb
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema mydb
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `mydb` DEFAULT CHARACTER SET utf8 ;
USE `mydb` ;

-- -----------------------------------------------------
-- Table `mydb`.`Store`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`Store` (
  `store_ID` INT NOT NULL AUTO_INCREMENT,
  `store_name` VARCHAR(45) NOT NULL,
  `store_address` VARCHAR(45) NOT NULL,
  `store_phone` VARCHAR(10) NOT NULL,
  PRIMARY KEY (`store_ID`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`Employee`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`Employee` (
  `employee_ID` INT NOT NULL AUTO_INCREMENT,
  `employee_name` VARCHAR(45) NOT NULL,
  `employee_email` VARCHAR(45) NOT NULL,
  `employee_phone` VARCHAR(10) NOT NULL,
  `employee_position` VARCHAR(45) NOT NULL,
  `store_store_ID` INT NOT NULL,
  `password` VARCHAR(20) NOT NULL,
  PRIMARY KEY (`employee_ID`),
  INDEX `fk_Employee_Store1_idx` (`store_store_ID` ASC) VISIBLE,
  CONSTRAINT `fk_Employee_Store1`
    FOREIGN KEY (`store_store_ID`)
    REFERENCES `mydb`.`Store` (`store_ID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`Customer`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`Customer` (
  `customer_ID` INT NOT NULL AUTO_INCREMENT,
  `customer_name` VARCHAR(45) NOT NULL,
  `Customer_email` VARCHAR(45) NOT NULL,
  `customer_phone` VARCHAR(10) NOT NULL,
  `password` VARCHAR(20) NOT NULL,
  PRIMARY KEY (`customer_ID`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`Publisher`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`Publisher` (
  `publisher_ID` INT NOT NULL AUTO_INCREMENT,
  `publisher_name` VARCHAR(45) NOT NULL,
  `publisher_address` VARCHAR(45) NOT NULL,
  `publisher_phone` VARCHAR(10) NOT NULL,
  PRIMARY KEY (`publisher_ID`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`Book`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`Book` (
  `book_ID` INT NOT NULL AUTO_INCREMENT,
  `book_title` VARCHAR(45) NOT NULL,
  `book_author` VARCHAR(45) NOT NULL,
  `book_publisher` VARCHAR(45) NOT NULL,
  `book_price` DECIMAL(10,2) NOT NULL,
  `publisher_publisher_ID` INT NOT NULL,
  PRIMARY KEY (`book_ID`),
  INDEX `fk_Book_Publisher1_idx` (`publisher_publisher_ID` ASC) VISIBLE,
  CONSTRAINT `fk_Book_Publisher1`
    FOREIGN KEY (`publisher_publisher_ID`)
    REFERENCES `mydb`.`Publisher` (`publisher_ID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`Order`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`Order` (
  `order_ID` INT NOT NULL AUTO_INCREMENT,
  `order_date` DATE NOT NULL,
  `total_amount` DECIMAL(10,2) NOT NULL,
  `store_store_ID` INT NOT NULL,
  `customer_customer_ID` INT NOT NULL,
  PRIMARY KEY (`order_ID`),
  INDEX `fk_Order_Store_idx` (`store_store_ID` ASC) VISIBLE,
  INDEX `fk_Order_Customer1_idx` (`customer_customer_ID` ASC) VISIBLE,
  CONSTRAINT `fk_Order_Store`
    FOREIGN KEY (`store_store_ID`)
    REFERENCES `mydb`.`Store` (`store_ID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Order_Customer1`
    FOREIGN KEY (`customer_customer_ID`)
    REFERENCES `mydb`.`Customer` (`customer_ID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`OrderDetails`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`OrderDetails` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `quantity` INT NOT NULL,
  `unit_price` DECIMAL(10,2) NOT NULL,
  `order_order_ID` INT NOT NULL,
  `book_book_ID` INT NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_OrderDetails_Order1_idx` (`order_order_ID` ASC) VISIBLE,
  INDEX `fk_OrderDetails_Book1_idx` (`book_book_ID` ASC) VISIBLE,
  CONSTRAINT `fk_OrderDetails_Order1`
    FOREIGN KEY (`order_order_ID`)
    REFERENCES `mydb`.`Order` (`order_ID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_OrderDetails_Book1`
    FOREIGN KEY (`book_book_ID`)
    REFERENCES `mydb`.`Book` (`book_ID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`Inventory`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`Inventory` (
  `id` INT AUTO_INCREMENT,
  `quantity` INT NOT NULL,
  `book_book_ID` INT NOT NULL,
  `store_store_ID` INT NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_Inventory_Book1_idx` (`book_book_ID` ASC) VISIBLE,
  INDEX `fk_Inventory_Store1_idx` (`store_store_ID` ASC) VISIBLE,
  CONSTRAINT `fk_Inventory_Book1`
    FOREIGN KEY (`book_book_ID`)
    REFERENCES `mydb`.`Book` (`book_ID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Inventory_Store1`
    FOREIGN KEY (`store_store_ID`)
    REFERENCES `mydb`.`Store` (`store_ID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;

-- -----------------------------------------------------
-- Data for table `mydb`.`Store`
-- -----------------------------------------------------
START TRANSACTION;
USE `mydb`;
INSERT INTO `mydb`.`Store` (`store_ID`, `store_name`, `store_address`, `store_phone`) VALUES (1, 'Orange', 'София Дружба 1', '0876876198');
INSERT INTO `mydb`.`Store` (`store_ID`, `store_name`, `store_address`, `store_phone`) VALUES (2, 'Ciela', 'София Младост 2', '0877330098');
INSERT INTO `mydb`.`Store` (`store_ID`, `store_name`, `store_address`, `store_phone`) VALUES (3, 'Ozone', 'София Люлин 3', '0877445687');

COMMIT;


-- -----------------------------------------------------
-- Data for table `mydb`.`Employee`
-- -----------------------------------------------------
START TRANSACTION;
USE `mydb`;
INSERT INTO `mydb`.`Employee` (`employee_ID`, `employee_name`, `employee_email`, `employee_phone`, `employee_position`, `store_store_ID`,`password`) VALUES (1, 'Иван Петров', 'ivanp@mail.bg', '0877330023', 'Касиер', 01,'123456789');
INSERT INTO `mydb`.`Employee` (`employee_ID`, `employee_name`, `employee_email`, `employee_phone`, `employee_position`, `store_store_ID`,`password`) VALUES (2, 'Виктор Владинов', 'viki@mail.bg', '0877240849', 'Касиер', 01,'987654321');
INSERT INTO `mydb`.`Employee` (`employee_ID`, `employee_name`, `employee_email`, `employee_phone`, `employee_position`, `store_store_ID`,`password`) VALUES (3, 'Петър Митов', 'petarm@mail.bg', '0879337709', 'Касиер', 02,'135798642');
INSERT INTO `mydb`.`Employee` (`employee_ID`, `employee_name`, `employee_email`, `employee_phone`, `employee_position`, `store_store_ID`,`password`) VALUES (4, 'Борислав Павлов', 'bobi@mail.bg', '0876876123', 'Касиер', 02,'boris897531');
INSERT INTO `mydb`.`Employee` (`employee_ID`, `employee_name`, `employee_email`, `employee_phone`, `employee_position`, `store_store_ID`,`password`) VALUES (5, 'Владимир Владимиров', 'vladi@mail.bg', '0897336622', 'Касиер', 03,'vladi354657');
INSERT INTO `mydb`.`Employee` (`employee_ID`, `employee_name`, `employee_email`, `employee_phone`, `employee_position`, `store_store_ID`,`password`) VALUES (6, 'Кристиан Недков', 'krisi@mail.bg', '0897663452', 'Чистач', 01,'kris76453');
INSERT INTO `mydb`.`Employee` (`employee_ID`, `employee_name`, `employee_email`, `employee_phone`, `employee_position`, `store_store_ID`,`password`) VALUES (7, 'Кристиян Пенев', 'penev@mail.bg', '0987564321', 'Чистач', 01,'kris1234');
INSERT INTO `mydb`.`Employee` (`employee_ID`, `employee_name`, `employee_email`, `employee_phone`, `employee_position`, `store_store_ID`,`password`) VALUES (8 , 'Диан Динчев', 'dian@mail.bg', '0877556798', 'Чистач', 02,'dian873');
INSERT INTO `mydb`.`Employee` (`employee_ID`, `employee_name`, `employee_email`, `employee_phone`, `employee_position`, `store_store_ID`,`password`) VALUES (9, 'Димитър Дудуков', 'mitko@mail.bg', '0897664532', 'Админ', 02,'dimityr4683');
INSERT INTO `mydb`.`Employee` (`employee_ID`, `employee_name`, `employee_email`, `employee_phone`, `employee_position`, `store_store_ID`,`password`) VALUES (10, 'Владимир Томашевич', 'vladit@mail.bg', '0897564312', 'Админ', 03,'vladislav62534');

COMMIT;


-- -----------------------------------------------------
-- Data for table `mydb`.`Customer`
-- -----------------------------------------------------
START TRANSACTION;
USE `mydb`;
INSERT INTO `mydb`.`Customer` (`customer_ID`, `customer_name`, `Customer_email`, `customer_phone`,`password`) VALUES (1, 'Иван Иванов', 'ivan@mail.bg', '0876876197','Ivan123456');
INSERT INTO `mydb`.`Customer` (`customer_ID`, `customer_name`, `Customer_email`, `customer_phone`,`password`) VALUES (2, 'Петър Петров', 'petar@mail.bg', '0879330052','Petyr321');
INSERT INTO `mydb`.`Customer` (`customer_ID`, `customer_name`, `Customer_email`, `customer_phone`,`password`) VALUES (3, 'Марк Весков', 'mark@mail.bg', '0876876178','mark123');

COMMIT;


-- -----------------------------------------------------
-- Data for table `mydb`.`Publisher`
-- -----------------------------------------------------
START TRANSACTION;
USE `mydb`;
INSERT INTO `mydb`.`Publisher` (`publisher_ID`, `publisher_name`, `publisher_address`, `publisher_phone`) VALUES (1, 'Просвета', 'София, България', '0876876543');
INSERT INTO `mydb`.`Publisher` (`publisher_ID`, `publisher_name`, `publisher_address`, `publisher_phone`) VALUES (2, 'Булвест', 'Пловдив, България', '0879334562');
INSERT INTO `mydb`.`Publisher` (`publisher_ID`, `publisher_name`, `publisher_address`, `publisher_phone`) VALUES (3, 'Архимед', 'Варна, България', '0876768943');

COMMIT;


-- -----------------------------------------------------
-- Data for table `mydb`.`Book`
-- -----------------------------------------------------
START TRANSACTION;
USE `mydb`;
INSERT INTO `mydb`.`Book` (`book_title`, `book_author`, `book_publisher`, `book_price`, `publisher_publisher_ID`) VALUES ('Хари Потър', 'Иван Иванов', 'Просвета',15, 1);
INSERT INTO `mydb`.`Book` (`book_title`, `book_author`, `book_publisher`, `book_price`, `publisher_publisher_ID`) VALUES ('Мечо Пух', 'Алекс Иванов', 'Булвест',20, 2);
INSERT INTO `mydb`.`Book` (`book_title`, `book_author`, `book_publisher`, `book_price`, `publisher_publisher_ID`) VALUES ('Ян Бибиян', 'Петър Ганев', 'Архимед',10, 3);
INSERT INTO `mydb`.`Book` (`book_title`, `book_author`, `book_publisher`, `book_price`, `publisher_publisher_ID`) VALUES ('Костенурките', 'Иван Галев', 'Просвета',9, 1);
INSERT INTO `mydb`.`Book` (`book_title`, `book_author`, `book_publisher`, `book_price`, `publisher_publisher_ID`) VALUES ('Мечката', 'Виктор Владинов', 'Булвест',20, 2);
INSERT INTO `mydb`.`Book` (`book_title`, `book_author`, `book_publisher`, `book_price`, `publisher_publisher_ID`) VALUES ('Гюнтер Кроко', 'Кончо Петров', 'Архимед',12, 3);
INSERT INTO `mydb`.`Book` (`book_title`, `book_author`, `book_publisher`, `book_price`, `publisher_publisher_ID`) VALUES ('Бамзе Мечока', 'Георги Колев', 'Просвета',23, 1);
INSERT INTO `mydb`.`Book` (`book_title`, `book_author`, `book_publisher`, `book_price`, `publisher_publisher_ID`) VALUES ('Библията', 'Иисус Христос', 'Булвест',21, 2);
INSERT INTO `mydb`.`Book` (`book_title`, `book_author`, `book_publisher`, `book_price`, `publisher_publisher_ID`) VALUES ('Корана', 'Аллах', 'Архимед',17, 3);
INSERT INTO `mydb`.`Book` (`book_title`, `book_author`, `book_publisher`, `book_price`, `publisher_publisher_ID`) VALUES ('Китай', 'Ким Чен Ун', 'Просвета',15, 1);

COMMIT;


-- -----------------------------------------------------
-- Data for table `mydb`.`Order`
-- -----------------------------------------------------
START TRANSACTION;
USE `mydb`;
INSERT INTO `mydb`.`Order` (`order_date`, `total_amount`, `store_store_ID`, `customer_customer_ID`) VALUES ('23.04.2022', 480, 1, 1);
INSERT INTO `mydb`.`Order` (`order_date`, `total_amount`, `store_store_ID`, `customer_customer_ID`) VALUES ('24.04.2022', 460, 2, 2);
INSERT INTO `mydb`.`Order` (`order_date`, `total_amount`, `store_store_ID`, `customer_customer_ID`) VALUES ('12.05.2022', 890, 3, 3);


COMMIT;


-- -----------------------------------------------------
-- Data for table `mydb`.`OrderDetails`
-- -----------------------------------------------------
START TRANSACTION;
USE `mydb`;
INSERT INTO `mydb`.`OrderDetails` (`quantity`, `unit_price`, `order_order_ID`, `book_book_ID`) VALUES (32, 15, 1, 1);
INSERT INTO `mydb`.`OrderDetails` (`quantity`, `unit_price`, `order_order_ID`, `book_book_ID`) VALUES (23, 20, 2, 2);
INSERT INTO `mydb`.`OrderDetails` (`quantity`, `unit_price`, `order_order_ID`, `book_book_ID`) VALUES (89, 10, 3, 3);

COMMIT;


-- -----------------------------------------------------
-- Data for table `mydb`.`Inventory`
-- -----------------------------------------------------
START TRANSACTION;
USE `mydb`;
INSERT INTO `mydb`.`Inventory` (`quantity`, `book_book_ID`, `store_store_ID`) VALUES (120, 1, 1);
INSERT INTO `mydb`.`Inventory` (`quantity`, `book_book_ID`, `store_store_ID`) VALUES (320, 2, 2);
INSERT INTO `mydb`.`Inventory` (`quantity`, `book_book_ID`, `store_store_ID`) VALUES (137, 3, 3);

COMMIT;

-- -----------------------------------------------------
-- table `mydb`.`reduction`
-- -----------------------------------------------------

CREATE TABLE `mydb`.`reduction`(
`id` INT AUTO_INCREMENT PRIMARY KEY,
`startDate` DATETIME NOT NULL,
`endDate` DATETIME NOT NULL,
`percentReduction` INT NOT NULL
);


-- -----------------------------------------------------
-- Data for table `mydb`.`reduction`
-- -----------------------------------------------------
START TRANSACTION;
USE `mydb`;
INSERT INTO `mydb`.`reduction` (`startDate`,`endDate`,`percentReduction`) VALUES ('2024-04-25 12:30:00', '2024-04-27 12:30:00', 15);
INSERT INTO `mydb`.`reduction` (`startDate`,`endDate`,`percentReduction`) VALUES ('2024-06-01 15:30:00', '2024-06-03 20:30:00', 20);
INSERT INTO `mydb`.`reduction` (`startDate`,`endDate`,`percentReduction`) VALUES ('2024-12-25 10:30:00', '2024-12-30 10:30:00', 10);

COMMIT;
