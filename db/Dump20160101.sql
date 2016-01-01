CREATE DATABASE  IF NOT EXISTS `m_bus` /*!40100 DEFAULT CHARACTER SET utf8 */;
USE `m_bus`;
-- MySQL dump 10.13  Distrib 5.7.9, for osx10.9 (x86_64)
--
-- Host: localhost    Database: m_bus
-- ------------------------------------------------------
-- Server version	5.7.9

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
INSERT INTO `bus_info` VALUES (12,'22','22','สาย 22','No 22','','0457-01-01 00:00:00','0457-01-01 23:00:00','10'),(13,'15','15','สาย 15','No 15','','0457-01-01 00:00:00','0457-01-01 23:00:00','6.5'),(14,'4','4','สาย 4','No 4','','0457-01-01 00:00:00','0457-01-01 23:00:00','8');
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
INSERT INTO `bus_line` VALUES (200,203,163.35544068074378,12,0),(201,200,126.4792473095883,12,0),(202,201,85.0235261559999,12,0),(196,202,162.19741058352318,12,0),(197,196,43.174066289845804,12,0),(196,195,145.52319402761884,13,0),(195,194,130.31116606031887,13,0),(198,199,104.546640309481,14,0),(194,198,108.6830253535482,14,0),(201,194,77.10382610480494,14,0),(196,197,43.174066289845804,13,0);
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
INSERT INTO `bus_path` VALUES (194,195,'194->195'),(194,196,'194->195->196'),(194,197,'194->195->196->197'),(194,198,'194->198'),(194,199,'194->198->199'),(194,200,'194->201->200'),(194,201,'194->201'),(194,202,'194->201->202'),(194,203,'194->201->200->203'),(195,194,'195->194'),(195,196,'195->196'),(195,197,'195->196->197'),(195,198,'195->194->198'),(195,199,'195->194->198->199'),(195,200,'195->194->201->200'),(195,201,'195->194->201'),(195,202,'195->194->201->202'),(195,203,'195->194->201->200->203'),(196,194,'196->195->194'),(196,195,'196->195'),(196,197,'196->197'),(196,198,'196->195->194->198'),(196,199,'196->195->194->198->199'),(196,200,'196->202->201->200'),(196,201,'196->202->201'),(196,202,'196->202'),(196,203,'196->202->201->200->203'),(197,194,'197->196->195->194'),(197,195,'197->196->195'),(197,196,'197->196'),(197,198,'197->196->195->194->198'),(197,199,'197->196->195->194->198->199'),(197,200,'197->196->202->201->200'),(197,201,'197->196->202->201'),(197,202,'197->196->202'),(197,203,'197->196->202->201->200->203'),(198,194,'198->194'),(198,195,'198->194->195'),(198,196,'198->194->195->196'),(198,197,'198->194->195->196->197'),(198,199,'198->199'),(198,200,'198->194->201->200'),(198,201,'198->194->201'),(198,202,'198->194->201->202'),(198,203,'198->194->201->200->203'),(199,194,'199->198->194'),(199,195,'199->198->194->195'),(199,196,'199->198->194->195->196'),(199,197,'199->198->194->195->196->197'),(199,198,'199->198'),(199,200,'199->198->194->201->200'),(199,201,'199->198->194->201'),(199,202,'199->198->194->201->202'),(199,203,'199->198->194->201->200->203'),(200,194,'200->201->194'),(200,195,'200->201->194->195'),(200,196,'200->201->202->196'),(200,197,'200->201->202->196->197'),(200,198,'200->201->194->198'),(200,199,'200->201->194->198->199'),(200,201,'200->201'),(200,202,'200->201->202'),(200,203,'200->203'),(201,194,'201->194'),(201,195,'201->194->195'),(201,196,'201->202->196'),(201,197,'201->202->196->197'),(201,198,'201->194->198'),(201,199,'201->194->198->199'),(201,200,'201->200'),(201,202,'201->202'),(201,203,'201->200->203'),(202,194,'202->201->194'),(202,195,'202->201->194->195'),(202,196,'202->196'),(202,197,'202->196->197'),(202,198,'202->201->194->198'),(202,199,'202->201->194->198->199'),(202,200,'202->201->200'),(202,201,'202->201'),(202,203,'202->201->200->203'),(203,194,'203->200->201->194'),(203,195,'203->200->201->194->195'),(203,196,'203->200->201->202->196'),(203,197,'203->200->201->202->196->197'),(203,198,'203->200->201->194->198'),(203,199,'203->200->201->194->198->199'),(203,200,'203->200'),(203,201,'203->200->201'),(203,202,'203->200->201->202');
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
) ENGINE=InnoDB AUTO_INCREMENT=204 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `point_info`
--

LOCK TABLES `point_info` WRITE;
/*!40000 ALTER TABLE `point_info` DISABLE KEYS */;
INSERT INTO `point_info` VALUES (194,2308,1365,1),(195,2299,1495,1),(196,2155,1516,1),(197,2145,1558,2),(198,2332,1259,1),(199,2371,1162,2),(200,2221,1226,1),(201,2232,1352,1),(202,2147,1354,1),(203,2200,1064,1);
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
INSERT INTO `point_name` VALUES (197,'P1','P1'),(199,'P2','P2');
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

-- Dump completed on 2016-01-01 22:35:25
