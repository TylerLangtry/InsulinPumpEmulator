
This prototype uses local host of Apache and MySQL servers via XAMPP Contol Panel v3.2.4

## Change Document Root ##
The local directory was changed for the GUI prototype and for Github push functionality.
https://stackoverflow.com/questions/1408/make-xampp-apache-serve-file-outside-of-htdocs-folder
Edit ~line 176 in C:\xampp\apache\conf\httpd.conf; change #DocumentRoot "C:/xampp/htdocs" to #DocumentRoot "C:/Projects" (or ...\IdeaProjects\InsulinPumpEmulator).
Edit ~line 203 to match your new location (in this case C:/Projects).

## Create MySQL database ##
The MySQL "users" database was generated using local.phpadmin with the following query:
-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema insulin_pump_final
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema insulin_pump_final
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `main2` DEFAULT CHARACTER SET utf8 ;
USE `main2` ;

-- -----------------------------------------------------
-- Table `insulin_pump_final`.`users`
-- -----------------------------------------------------
-- CREATE TABLE IF NOT EXISTS `main2`.`users` (
--  `userid` INT NOT NULL,
--  `username` VARCHAR(45) NOT NULL,
--  `password` VARCHAR(45) NOT NULL,
--  `admin_status` TINYINT NOT NULL,
--  `name` VARCHAR(100) NOT NULL,
--  PRIMARY KEY (`userid`))
-- ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS `main2`.users (
    `id` INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
    `username` VARCHAR(50) NOT NULL UNIQUE,
    `password` VARCHAR(255) NOT NULL,
    `admin_status` TINYINT NOT NULL,
    `name` VARCHAR(100) NOT NULL,
    `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP
)ENGINE = InnoDB;

-- -----------------------------------------------------
-- Table `insulin_pump_final`.`configuration`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `main2`.`configuration` (
  `configuration_id` INT NOT NULL,
  `last_change` DATETIME NOT NULL,
  `cooldown_time` DECIMAL(5,2) NOT NULL,
  `max_inj_amnt` DECIMAL(5,2) NOT NULL,
  `min_inj_amnt` DECIMAL(5,2) NOT NULL,
  `max_cumm_dose` VARCHAR(45) NOT NULL,
  `users_user_id` INT NOT NULL,
  PRIMARY KEY (`configuration_id`),
  INDEX `fk_configuration_users_idx` (`users_user_id` ASC),
  CONSTRAINT `fk_configuration_users`
    FOREIGN KEY (`users_user_id`)
    REFERENCES `insulin_pump_final`.`users` (`userid`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `insulin_pump_final`.`status`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `main2`.`status` (
  `status_id` INT NOT NULL,
  `last_update` DATETIME NOT NULL,
  `batter_charge` INT NOT NULL,
  `reserve_amnt` INT NOT NULL,
  `alert` VARCHAR(100) NULL,
  `users_user_id` INT NOT NULL,
  PRIMARY KEY (`status_id`),
  INDEX `fk_status_users1_idx` (`users_user_id` ASC),
  CONSTRAINT `fk_status_users1`
    FOREIGN KEY (`users_user_id`)
    REFERENCES `insulin_pump_final`.`users` (`userid`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `insulin_pump_final`.`data`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `main2`.`data` (
  `data_id` INT NOT NULL,
  `blood_sug_lvl` DECIMAL(5,2) NOT NULL,
  `last_update` DATETIME NOT NULL,
  `inj_amnt` DECIMAL(5,2) NULL,
  `users_user_id` INT NOT NULL,
  PRIMARY KEY (`data_id`),
  INDEX `fk_data_users1_idx` (`users_user_id` ASC),
  CONSTRAINT `fk_data_users1`
    FOREIGN KEY (`users_user_id`)
    REFERENCES `insulin_pump_final`.`users` (`userid`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;



