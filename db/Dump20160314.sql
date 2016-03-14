CREATE DATABASE  IF NOT EXISTS `m_bus` /*!40100 DEFAULT CHARACTER SET utf8 */;
USE `m_bus`;
-- MySQL dump 10.13  Distrib 5.7.9, for Win64 (x86_64)
--
-- Host: localhost    Database: m_bus
-- ------------------------------------------------------
-- Server version	5.7.10-log

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `bus_info`
--

DROP TABLE IF EXISTS `bus_info`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `bus_info` (
  `bus_id` int(11) NOT NULL AUTO_INCREMENT,
  `bus_no_th` varchar(45) NOT NULL,
  `bus_no_en` varchar(45) NOT NULL,
  `detail_th` varchar(2000) DEFAULT NULL,
  `detail_en` varchar(2000) DEFAULT NULL,
  `bus_pic` varchar(250) DEFAULT NULL,
  `start_time` datetime NOT NULL,
  `end_time` datetime NOT NULL,
  `bus_price` varchar(45) NOT NULL COMMENT '‘0’ = Free\n‘0,6.5’ = Free or 6.5 Baht\n’10-35(2.5)’ = Minimum is 10 Baht, Maximum is 35 Baht, Increase every 2.5 Baht per Bus Point\n',
  PRIMARY KEY (`bus_id`)
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `bus_info`
--

LOCK TABLES `bus_info` WRITE;
/*!40000 ALTER TABLE `bus_info` DISABLE KEYS */;
/*!40000 ALTER TABLE `bus_info` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `bus_line`
--

DROP TABLE IF EXISTS `bus_line`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `bus_line` (
  `p1_id` int(11) NOT NULL,
  `p2_id` int(11) NOT NULL,
  `distance` double NOT NULL,
  `bus_id` int(11) NOT NULL,
  `type` int(11) NOT NULL,
  KEY `p1_id_idx` (`p1_id`),
  KEY `p2_id_idx` (`p2_id`),
  KEY `bus_id_idx` (`bus_id`),
  CONSTRAINT `bus_line_bus_id` FOREIGN KEY (`bus_id`) REFERENCES `bus_info` (`bus_id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `bus_line_p1_id` FOREIGN KEY (`p1_id`) REFERENCES `point_info` (`p_id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `bus_line_p2_id` FOREIGN KEY (`p2_id`) REFERENCES `point_info` (`p_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `bus_line`
--

LOCK TABLES `bus_line` WRITE;
/*!40000 ALTER TABLE `bus_line` DISABLE KEYS */;
/*!40000 ALTER TABLE `bus_line` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `bus_path`
--

DROP TABLE IF EXISTS `bus_path`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `bus_path` (
  `source_id` int(11) NOT NULL,
  `destination_id` int(11) NOT NULL,
  `bus_path` varchar(3000) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `bus_path`
--

LOCK TABLES `bus_path` WRITE;
/*!40000 ALTER TABLE `bus_path` DISABLE KEYS */;
/*!40000 ALTER TABLE `bus_path` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `point_info`
--

DROP TABLE IF EXISTS `point_info`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `point_info` (
  `p_id` int(11) NOT NULL AUTO_INCREMENT,
  `axis_x` int(11) NOT NULL,
  `axis_y` int(11) NOT NULL,
  `type` int(11) NOT NULL,
  PRIMARY KEY (`p_id`)
) ENGINE=InnoDB AUTO_INCREMENT=249 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `point_info`
--

LOCK TABLES `point_info` WRITE;
/*!40000 ALTER TABLE `point_info` DISABLE KEYS */;
/*!40000 ALTER TABLE `point_info` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `point_name`
--

DROP TABLE IF EXISTS `point_name`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `point_name` (
  `p_id` int(11) NOT NULL,
  `name_th` varchar(250) NOT NULL,
  `name_en` varchar(250) NOT NULL,
  KEY `p_id_idx` (`p_id`),
  CONSTRAINT `point_name_p_id` FOREIGN KEY (`p_id`) REFERENCES `point_info` (`p_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `point_name`
--

LOCK TABLES `point_name` WRITE;
/*!40000 ALTER TABLE `point_name` DISABLE KEYS */;
/*!40000 ALTER TABLE `point_name` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2016-03-14  6:57:53
