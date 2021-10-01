-- MySQL dump 10.13  Distrib 8.0.22, for Linux (x86_64)
--
-- Host: localhost    Database: agbi
-- ------------------------------------------------------
-- Server version	8.0.22-0ubuntu0.20.04.3

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `appointments`
--

DROP TABLE IF EXISTS `appointments`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `appointments` (
  `Appointment_ID` int NOT NULL AUTO_INCREMENT,
  `Title` varchar(50) NOT NULL,
  `Description` varchar(50) DEFAULT NULL,
  `Location` varchar(50) NOT NULL,
  `Type` varchar(50) DEFAULT NULL,
  `Start` datetime DEFAULT CURRENT_TIMESTAMP,
  `End` datetime DEFAULT CURRENT_TIMESTAMP,
  `Create_Date` datetime DEFAULT CURRENT_TIMESTAMP,
  `Created_By` varchar(50) DEFAULT NULL,
  `Last_Update` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `Last_Updated_By` varchar(50) DEFAULT NULL,
  `Customer_ID` int DEFAULT NULL,
  `User_ID` int DEFAULT NULL,
  `Contact_ID` int DEFAULT NULL,
  PRIMARY KEY (`Appointment_ID`),
  KEY `Customer_ID` (`Customer_ID`),
  KEY `User_ID` (`User_ID`),
  KEY `Contact_ID` (`Contact_ID`),
  CONSTRAINT `appointments_ibfk_1` FOREIGN KEY (`Customer_ID`) REFERENCES `customers` (`Customer_ID`),
  CONSTRAINT `appointments_ibfk_2` FOREIGN KEY (`User_ID`) REFERENCES `users` (`User_ID`),
  CONSTRAINT `appointments_ibfk_3` FOREIGN KEY (`Contact_ID`) REFERENCES `contacts` (`Contact_ID`)
) ENGINE=InnoDB AUTO_INCREMENT=33 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `appointments`
--

LOCK TABLES `appointments` WRITE;
/*!40000 ALTER TABLE `appointments` DISABLE KEYS */;
INSERT INTO `appointments` VALUES (29,'t','t','New York','In-person','2021-09-25 22:30:00','2021-09-25 23:30:00','2021-09-25 06:26:50','test','2021-09-25 00:56:50','test',1,1,1),(30,'rttr','tr','Tampa','Chat','2021-09-25 23:30:00','2021-09-26 00:30:00','2021-09-25 06:27:17','test','2021-09-25 00:57:17','test',1,1,2),(31,'r','r','New York','Video','2021-09-26 00:30:00','2021-09-26 01:30:00','2021-09-25 06:28:04','test','2021-09-25 00:58:04','test',1,1,1),(32,'tretre','retert','Phoenix','In-person','2021-09-26 13:30:00','2021-09-26 14:30:00','2021-09-26 19:25:15','test','2021-09-26 14:12:26','test',1,1,2);
/*!40000 ALTER TABLE `appointments` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `contacts`
--

DROP TABLE IF EXISTS `contacts`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `contacts` (
  `Contact_ID` int NOT NULL AUTO_INCREMENT,
  `Contact_Name` varchar(50) NOT NULL,
  `Email` varchar(50) NOT NULL,
  PRIMARY KEY (`Contact_ID`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `contacts`
--

LOCK TABLES `contacts` WRITE;
/*!40000 ALTER TABLE `contacts` DISABLE KEYS */;
INSERT INTO `contacts` VALUES (1,'Anika Costa','acoasta@company.com'),(2,'Daniel Garcia','dgarcia@company.com'),(3,'Li Lee','llee@company.com');
/*!40000 ALTER TABLE `contacts` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `countries`
--

DROP TABLE IF EXISTS `countries`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `countries` (
  `Country_ID` int NOT NULL AUTO_INCREMENT,
  `Country` varchar(50) NOT NULL,
  `Create_Date` datetime DEFAULT CURRENT_TIMESTAMP,
  `Created_By` varchar(50) DEFAULT NULL,
  `Last_Update` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `Last_Updated_By` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`Country_ID`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `countries`
--

LOCK TABLES `countries` WRITE;
/*!40000 ALTER TABLE `countries` DISABLE KEYS */;
INSERT INTO `countries` VALUES (1,'U.S','2021-09-13 20:34:57','script','2021-09-13 15:04:57','script'),(2,'UK','2021-09-13 20:34:57','script','2021-09-13 15:04:57','script'),(3,'Canada','2021-09-13 20:34:57','script','2021-09-13 15:04:57','script');
/*!40000 ALTER TABLE `countries` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `customers`
--

DROP TABLE IF EXISTS `customers`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `customers` (
  `Customer_ID` int NOT NULL AUTO_INCREMENT,
  `Customer_Name` varchar(50) NOT NULL,
  `Address` varchar(100) NOT NULL,
  `Postal_Code` varchar(50) NOT NULL,
  `Phone` varchar(50) NOT NULL,
  `Create_Date` datetime DEFAULT CURRENT_TIMESTAMP,
  `Created_By` varchar(50) DEFAULT NULL,
  `Last_Update` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `Last_Updated_By` varchar(50) DEFAULT NULL,
  `Division_ID` int NOT NULL,
  PRIMARY KEY (`Customer_ID`),
  KEY `Division_ID` (`Division_ID`),
  CONSTRAINT `customers_ibfk_1` FOREIGN KEY (`Division_ID`) REFERENCES `first_level_divisions` (`Division_ID`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `customers`
--

LOCK TABLES `customers` WRITE;
/*!40000 ALTER TABLE `customers` DISABLE KEYS */;
INSERT INTO `customers` VALUES (1,'Daddy Warbucks','1919 Boardwalk update','01292','869-908-2000','2021-09-17 15:11:06','script','2021-09-17 09:41:06','test',9),(3,'Dudley Do-Right','48 Horse Manor ','28198','874-916-2671','2021-09-17 15:11:09','script','2021-09-17 09:41:09','test',29),(4,'robin','bangalore','560008','9711070972','2021-09-19 21:38:13','test','2021-09-19 16:08:13','test',2);
/*!40000 ALTER TABLE `customers` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `first_level_divisions`
--

DROP TABLE IF EXISTS `first_level_divisions`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `first_level_divisions` (
  `Division_ID` int NOT NULL AUTO_INCREMENT,
  `Division` varchar(50) NOT NULL,
  `Create_Date` datetime DEFAULT CURRENT_TIMESTAMP,
  `Created_By` varchar(50) DEFAULT NULL,
  `Last_Update` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `Last_Updated_By` varchar(50) DEFAULT NULL,
  `COUNTRY_ID` int NOT NULL,
  PRIMARY KEY (`Division_ID`),
  KEY `COUNTRY_ID` (`COUNTRY_ID`),
  CONSTRAINT `first_level_divisions_ibfk_1` FOREIGN KEY (`COUNTRY_ID`) REFERENCES `countries` (`Country_ID`)
) ENGINE=InnoDB AUTO_INCREMENT=105 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `first_level_divisions`
--

LOCK TABLES `first_level_divisions` WRITE;
/*!40000 ALTER TABLE `first_level_divisions` DISABLE KEYS */;
INSERT INTO `first_level_divisions` VALUES (1,'Alabama','2021-09-13 20:34:57','script','2021-09-13 15:04:57','script',1),(2,'Arizona','2021-09-13 20:34:57','script','2021-09-13 15:04:57','script',1),(3,'Arkansas','2021-09-13 20:34:57','script','2021-09-13 15:04:57','script',1),(4,'California','2021-09-13 20:34:57','script','2021-09-13 15:04:57','script',1),(5,'Colorado','2021-09-13 20:34:57','script','2021-09-13 15:04:57','script',1),(6,'Connecticut','2021-09-13 20:34:57','script','2021-09-13 15:04:57','script',1),(7,'Delaware','2021-09-13 20:34:57','script','2021-09-13 15:04:57','script',1),(8,'District of Columbia','2021-09-13 20:34:57','script','2021-09-13 15:04:57','script',1),(9,'Florida','2021-09-13 20:34:57','script','2021-09-13 15:04:57','script',1),(10,'Georgia','2021-09-13 20:34:57','script','2021-09-13 15:04:57','script',1),(11,'Idaho','2021-09-13 20:34:57','script','2021-09-13 15:04:57','script',1),(12,'Illinois','2021-09-13 20:34:57','script','2021-09-13 15:04:57','script',1),(13,'Indiana','2021-09-13 20:34:57','script','2021-09-13 15:04:57','script',1),(14,'Iowa','2021-09-13 20:34:57','script','2021-09-13 15:04:57','script',1),(15,'Kansas','2021-09-13 20:34:57','script','2021-09-13 15:04:57','script',1),(16,'Kentucky','2021-09-13 20:34:57','script','2021-09-13 15:04:57','script',1),(17,'Louisiana','2021-09-13 20:34:57','script','2021-09-13 15:04:57','script',1),(18,'Maine','2021-09-13 20:34:57','script','2021-09-13 15:04:57','script',1),(19,'Maryland','2021-09-13 20:34:57','script','2021-09-13 15:04:57','script',1),(20,'Massachusetts','2021-09-13 20:34:57','script','2021-09-13 15:04:57','script',1),(21,'Michigan','2021-09-13 20:34:57','script','2021-09-13 15:04:57','script',1),(22,'Minnesota','2021-09-13 20:34:57','script','2021-09-13 15:04:57','script',1),(23,'Mississippi','2021-09-13 20:34:57','script','2021-09-13 15:04:57','script',1),(24,'Missouri','2021-09-13 20:34:57','script','2021-09-13 15:04:57','script',1),(25,'Montana','2021-09-13 20:34:57','script','2021-09-13 15:04:57','script',1),(26,'Nebraska','2021-09-13 20:34:57','script','2021-09-13 15:04:57','script',1),(27,'Nevada','2021-09-13 20:34:57','script','2021-09-13 15:04:57','script',1),(28,'New Hampshire','2021-09-13 20:34:57','script','2021-09-13 15:04:57','script',1),(29,'New Jersey','2021-09-13 20:34:57','script','2021-09-13 15:04:57','script',1),(30,'New Mexico','2021-09-13 20:34:57','script','2021-09-13 15:04:57','script',1),(31,'New York','2021-09-13 20:34:57','script','2021-09-13 15:04:57','script',1),(32,'North Carolina','2021-09-13 20:34:57','script','2021-09-13 15:04:57','script',1),(33,'North Dakota','2021-09-13 20:34:57','script','2021-09-13 15:04:57','script',1),(34,'Ohio','2021-09-13 20:34:57','script','2021-09-13 15:04:57','script',1),(35,'Oklahoma','2021-09-13 20:34:57','script','2021-09-13 15:04:57','script',1),(36,'Oregon','2021-09-13 20:34:57','script','2021-09-13 15:04:57','script',1),(37,'Pennsylvania','2021-09-13 20:34:57','script','2021-09-13 15:04:57','script',1),(38,'Rhode Island','2021-09-13 20:34:57','script','2021-09-13 15:04:57','script',1),(39,'South Carolina','2021-09-13 20:34:57','script','2021-09-13 15:04:57','script',1),(40,'South Dakota','2021-09-13 20:34:57','script','2021-09-13 15:04:57','script',1),(41,'Tennessee','2021-09-13 20:34:57','script','2021-09-13 15:04:57','script',1),(42,'Texas','2021-09-13 20:34:57','script','2021-09-13 15:04:57','script',1),(43,'Utah','2021-09-13 20:34:57','script','2021-09-13 15:04:57','script',1),(44,'Vermont','2021-09-13 20:34:57','script','2021-09-13 15:04:57','script',1),(45,'Virginia','2021-09-13 20:34:57','script','2021-09-13 15:04:57','script',1),(46,'Washington','2021-09-13 20:34:57','script','2021-09-13 15:04:57','script',1),(47,'West Virginia','2021-09-13 20:34:57','script','2021-09-13 15:04:57','script',1),(48,'Wisconsin','2021-09-13 20:34:57','script','2021-09-13 15:04:57','script',1),(49,'Wyoming','2021-09-13 20:34:57','script','2021-09-13 15:04:57','script',1),(52,'Hawaii','2021-09-13 20:34:57','script','2021-09-13 15:04:57','script',1),(54,'Alaska','2021-09-13 20:34:57','script','2021-09-13 15:04:57','script',1),(60,'Northwest Territories','2021-09-13 20:34:57','script','2021-09-13 15:04:57','script',3),(61,'Alberta','2021-09-13 20:34:57','script','2021-09-13 15:04:57','script',3),(62,'British Columbia','2021-09-13 20:34:57','script','2021-09-13 15:04:57','script',3),(63,'Manitoba','2021-09-13 20:34:57','script','2021-09-13 15:04:57','script',3),(64,'New Brunswick','2021-09-13 20:34:57','script','2021-09-13 15:04:57','script',3),(65,'Nova Scotia','2021-09-13 20:34:57','script','2021-09-13 15:04:57','script',3),(66,'Prince Edward Island','2021-09-13 20:34:57','script','2021-09-13 15:04:57','script',3),(67,'Ontario','2021-09-13 20:34:57','script','2021-09-13 15:04:57','script',3),(68,'Québec','2021-09-13 20:34:57','script','2021-09-13 15:04:57','script',3),(69,'Saskatchewan','2021-09-13 20:34:57','script','2021-09-13 15:04:57','script',3),(70,'Nunavut','2021-09-13 20:34:57','script','2021-09-13 15:04:57','script',3),(71,'Yukon','2021-09-13 20:34:57','script','2021-09-13 15:04:57','script',3),(72,'Newfoundland and Labrador','2021-09-13 20:34:57','script','2021-09-13 15:04:57','script',3),(101,'England','2021-09-13 20:34:57','script','2021-09-13 15:04:57','script',2),(102,'Wales','2021-09-13 20:34:57','script','2021-09-13 15:04:57','script',2),(103,'Scotland','2021-09-13 20:34:57','script','2021-09-13 15:04:57','script',2),(104,'Northern Ireland','2021-09-13 20:34:57','script','2021-09-13 15:04:57','script',2);
/*!40000 ALTER TABLE `first_level_divisions` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `users` (
  `User_ID` int NOT NULL AUTO_INCREMENT,
  `User_Name` varchar(50) DEFAULT NULL,
  `Password` text,
  `Create_Date` datetime DEFAULT CURRENT_TIMESTAMP,
  `Created_By` varchar(50) DEFAULT NULL,
  `Last_Update` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `Last_Updated_By` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`User_ID`),
  UNIQUE KEY `User_Name` (`User_Name`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES (1,'test','test','2021-09-13 20:34:57','script','2021-09-13 15:04:57','script'),(2,'admin','admin','2021-09-13 20:34:57','script','2021-09-13 15:04:57','script');
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2021-09-28 21:27:41
