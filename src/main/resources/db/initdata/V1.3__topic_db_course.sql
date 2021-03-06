-- MySQL dump 10.13  Distrib 8.0.13, for macos10.14 (x86_64)
--
-- Host: localhost    Database: topic_db2
-- ------------------------------------------------------
-- Server version	5.7.17

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
 SET NAMES utf8 ;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Dumping data for table `course`
--

LOCK TABLES `course` WRITE;
/*!40000 ALTER TABLE `course` DISABLE KEYS */;
INSERT INTO `course` VALUES (1,CURRENT_TIMESTAMP,CURRENT_TIMESTAMP,'Schwedisch für Anfänger','2019-09-10',1),(2,CURRENT_TIMESTAMP,CURRENT_TIMESTAMP,'Repository','2017-09-11',1),(3,CURRENT_TIMESTAMP,CURRENT_TIMESTAMP,'Englisch für Anfänger','2019-09-11',4),(4,CURRENT_TIMESTAMP,CURRENT_TIMESTAMP,'Schwedisch für Profis','2019-09-10',2),(5,CURRENT_TIMESTAMP,CURRENT_TIMESTAMP,'Englisch für Fortgeschrittene','2019-09-10',4),(6,CURRENT_TIMESTAMP,CURRENT_TIMESTAMP,'Englisch für Fortgeschrittene','2019-09-10',4),(7,CURRENT_TIMESTAMP,CURRENT_TIMESTAMP,'Maven für Fortgeschrittene','2019-09-10',4),(8,CURRENT_TIMESTAMP,CURRENT_TIMESTAMP,'Maven für Fortgeschrittene','2019-09-10',1),(9,CURRENT_TIMESTAMP,CURRENT_TIMESTAMP,'Buddismus','2017-09-11',2),(10,CURRENT_TIMESTAMP,CURRENT_TIMESTAMP,'Chinesisch für Fortgeschrittene','2019-09-10',4),(11,CURRENT_TIMESTAMP,CURRENT_TIMESTAMP,'Französisch für Fortgeschrittene','2019-09-10',4),(12,CURRENT_TIMESTAMP,CURRENT_TIMESTAMP,'Spanisch für Fortgeschrittene','2019-09-10',4),(13,CURRENT_TIMESTAMP,CURRENT_TIMESTAMP,'Beethoven','2019-09-10',5),(21,CURRENT_TIMESTAMP,CURRENT_TIMESTAMP,'Beethoven','2019-09-10',5),(45,CURRENT_TIMESTAMP,CURRENT_TIMESTAMP,'Beethoven','2019-09-10',5),(46,CURRENT_TIMESTAMP,CURRENT_TIMESTAMP,'Exception Handling','2019-09-10',7),(47,CURRENT_TIMESTAMP,CURRENT_TIMESTAMP,'Clean Code','2019-09-10',7),(48,CURRENT_TIMESTAMP,CURRENT_TIMESTAMP,'Lambdas','2019-09-10',7),(49,CURRENT_TIMESTAMP,CURRENT_TIMESTAMP,'CDI','2019-09-10',7),(50,CURRENT_TIMESTAMP,CURRENT_TIMESTAMP,'Java SE','2019-09-10',7),(51,CURRENT_TIMESTAMP,CURRENT_TIMESTAMP,'Englisch','2000-09-10',1),(52,CURRENT_TIMESTAMP,CURRENT_TIMESTAMP,'Beethoven','2019-09-10',5),(53,CURRENT_TIMESTAMP,CURRENT_TIMESTAMP,'Englisch','2000-09-10',1),(54,CURRENT_TIMESTAMP,CURRENT_TIMESTAMP,'Englisch','2000-09-10',1),(55,CURRENT_TIMESTAMP,CURRENT_TIMESTAMP,'Englisch','2000-09-10',1),(63,CURRENT_TIMESTAMP,CURRENT_TIMESTAMP,'Buddismus','2017-09-11',1),(64,CURRENT_TIMESTAMP,CURRENT_TIMESTAMP,'Buddismus','2017-09-11',1),(65,CURRENT_TIMESTAMP,CURRENT_TIMESTAMP,'Buddismus','2017-09-11',1),(66,CURRENT_TIMESTAMP,CURRENT_TIMESTAMP,'Buddismus','2017-09-11',1),(67,CURRENT_TIMESTAMP,CURRENT_TIMESTAMP,'Buddismus','2017-09-11',1),(68,CURRENT_TIMESTAMP,CURRENT_TIMESTAMP,'Buddismus','2017-09-11',1);
/*!40000 ALTER TABLE `course` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2020-07-28 23:53:01
