-- MySQL Script generated by MySQL Workbench
-- Tue Jul 28 23:19:54 2020
-- Model: New Model    Version: 1.0
-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL,ALLOW_INVALID_DATES';

-- -----------------------------------------------------
-- Schema mydb
-- -----------------------------------------------------
-- -----------------------------------------------------
-- Schema topic_db
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema topic_db
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `topic_db` DEFAULT CHARACTER SET latin1 ;
USE `topic_db` ;

-- -----------------------------------------------------
-- Table `topic_db`.`course`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `topic_db`.`course` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(255) NULL DEFAULT NULL,
  `start_date` DATE NULL DEFAULT NULL,
  `topic_id` BIGINT(20) NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  INDEX `FKokaxyfpv8p583w8yspapfb2ar` (`topic_id` ASC))
ENGINE = MyISAM
AUTO_INCREMENT = 69
DEFAULT CHARACTER SET = latin1;


-- -----------------------------------------------------
-- Table `topic_db`.`course_student`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `topic_db`.`course_student` (
  `course_id` BIGINT(20) NOT NULL,
  `student_id` BIGINT(20) NOT NULL,
  INDEX `FK4xxxkt1m6afc9vxp3ryb0xfhi` (`student_id` ASC),
  INDEX `FKlmj50qx9k98b7li5li74nnylb` (`course_id` ASC))
ENGINE = MyISAM
DEFAULT CHARACTER SET = latin1;


-- -----------------------------------------------------
-- Table `topic_db`.`student`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `topic_db`.`student` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `birthday` DATE NULL DEFAULT NULL,
  `created` DATETIME NULL DEFAULT NULL,
  `name` VARCHAR(255) NOT NULL,
  PRIMARY KEY (`id`))
ENGINE = MyISAM
AUTO_INCREMENT = 20
DEFAULT CHARACTER SET = latin1;


-- -----------------------------------------------------
-- Table `topic_db`.`topic`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `topic_db`.`topic` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(255) NULL DEFAULT NULL,
  PRIMARY KEY (`id`))
ENGINE = MyISAM
AUTO_INCREMENT = 46
DEFAULT CHARACTER SET = latin1;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
