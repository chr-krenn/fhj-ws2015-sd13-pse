-- MySQL dump 10.13  Distrib 5.6.24, for Win64 (x86_64)
--
-- Host: localhost    Database: pse
-- ------------------------------------------------------
-- Server version	5.6.26-log

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
-- Table structure for table `community`
--

DROP TABLE IF EXISTS `community`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `community` (
  `community_id` int(11) NOT NULL AUTO_INCREMENT,
  `system_internal` bit(1) NOT NULL DEFAULT b'0',
  `invitation_only` bit(1) NOT NULL DEFAULT b'1',
  `name` varchar(64) NOT NULL,
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `created_by` int(11) DEFAULT NULL,
  `private_user` int(11) DEFAULT NULL,
  `confirmed_by` int(11) DEFAULT NULL COMMENT 'wenn null wurde community noch nicht freigegeben, ist also im status beantragt',
  PRIMARY KEY (`community_id`),
  UNIQUE KEY `name_UNIQUE` (`name`),
  KEY `created_by_idx` (`created_by`),
  KEY `FK_community_private_user_idx` (`private_user`),
  KEY `confirmed_vy_idx` (`confirmed_by`),
  CONSTRAINT `FK_community_private_user` FOREIGN KEY (`private_user`) REFERENCES `person` (`person_id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `confirmed_vy` FOREIGN KEY (`confirmed_by`) REFERENCES `person` (`person_id`) ON UPDATE NO ACTION,
  CONSTRAINT `created_by` FOREIGN KEY (`created_by`) REFERENCES `person` (`person_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 COMMENT='system_internal - news, documentlibraries (by name)\nname: \n	XX_news --> news \n	XX_document ---> document lib\n';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `community`
--

LOCK TABLES `community` WRITE;
/*!40000 ALTER TABLE `community` DISABLE KEYS */;
INSERT INTO `community` VALUES (1,'','\0','Portal-News','2015-10-04 19:29:56',1,NULL,1),(2,'','\0','Document Library','2015-10-04 19:35:38',1,NULL,1);
/*!40000 ALTER TABLE `community` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `community_member`
--

DROP TABLE IF EXISTS `community_member`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `community_member` (
  `community_member_id` int(11) NOT NULL AUTO_INCREMENT,
  `community_id` int(11) NOT NULL,
  `person_id` int(11) NOT NULL,
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `is_administrator` bit(1) NOT NULL DEFAULT b'0',
  `confirmed_by` int(11) DEFAULT NULL,
  PRIMARY KEY (`community_member_id`),
  UNIQUE KEY `community_member_id_UNIQUE` (`community_member_id`),
  KEY `member_idx` (`person_id`),
  KEY `^community_idx` (`community_id`),
  KEY `confirmed_by_idx` (`confirmed_by`),
  CONSTRAINT `community` FOREIGN KEY (`community_id`) REFERENCES `community` (`community_id`) ON DELETE CASCADE,
  CONSTRAINT `confirmed_by` FOREIGN KEY (`confirmed_by`) REFERENCES `person` (`person_id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `member` FOREIGN KEY (`person_id`) REFERENCES `person` (`person_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `community_member`
--

LOCK TABLES `community_member` WRITE;
/*!40000 ALTER TABLE `community_member` DISABLE KEYS */;
/*!40000 ALTER TABLE `community_member` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `delivery_system`
--

DROP TABLE IF EXISTS `delivery_system`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `delivery_system` (
  `delivery_system_id` int(11) NOT NULL AUTO_INCREMENT,
  `token` varchar(16) NOT NULL,
  `name` varchar(64) NOT NULL,
  PRIMARY KEY (`delivery_system_id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `delivery_system`
--

LOCK TABLES `delivery_system` WRITE;
/*!40000 ALTER TABLE `delivery_system` DISABLE KEYS */;
INSERT INTO `delivery_system` VALUES (1,'pse_system','Online PSE System'),(2,'sms','SMS auf Mobiltelefon'),(3,'email','E-Mail');
/*!40000 ALTER TABLE `delivery_system` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `document`
--

DROP TABLE IF EXISTS `document`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `document` (
  `document_id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(45) NOT NULL,
  `mime_type` varchar(64) NOT NULL,
  `description` varchar(128) DEFAULT NULL,
  `storage_location` varchar(128) NOT NULL,
  `size` int(11) NOT NULL,
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`document_id`),
  UNIQUE KEY `document_name_UNIQUE` (`name`),
  FULLTEXT KEY `document_description` (`description`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='Für alles was vom Benutzer hochgeladen wird (pdf,jpg, png)\nFilter was erlaubt ist (Größe, Type) durch Applikation)';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `document`
--

LOCK TABLES `document` WRITE;
/*!40000 ALTER TABLE `document` DISABLE KEYS */;
/*!40000 ALTER TABLE `document` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `mesasge_rating`
--

DROP TABLE IF EXISTS `mesasge_rating`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `mesasge_rating` (
  `mesasge_rating_id` int(11) NOT NULL AUTO_INCREMENT,
  `mesasge_id` int(11) DEFAULT NULL,
  `rating_person_id` int(11) DEFAULT NULL,
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`mesasge_rating_id`),
  UNIQUE KEY `mesasge_rating_id_UNIQUE` (`mesasge_rating_id`),
  KEY `rating_person_idx` (`rating_person_id`),
  KEY `rated_message_idx` (`mesasge_id`),
  CONSTRAINT `rated_message` FOREIGN KEY (`mesasge_id`) REFERENCES `message` (`message_id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `rating_person` FOREIGN KEY (`rating_person_id`) REFERENCES `person` (`person_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='Ein Eintrag in der table entspricht einem like';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `mesasge_rating`
--

LOCK TABLES `mesasge_rating` WRITE;
/*!40000 ALTER TABLE `mesasge_rating` DISABLE KEYS */;
/*!40000 ALTER TABLE `mesasge_rating` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `message`
--

DROP TABLE IF EXISTS `message`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `message` (
  `message_id` int(11) NOT NULL AUTO_INCREMENT,
  `created_by` int(11) NOT NULL,
  `posted_in` int(11) DEFAULT NULL,
  `headline` varchar(45) DEFAULT NULL,
  `message` varchar(2048) NOT NULL,
  `document_icon_id` int(11) DEFAULT NULL,
  `document_attachment_id` int(11) DEFAULT NULL,
  `commented_on_message_id` int(11) DEFAULT NULL,
  `created_on` datetime NOT NULL,
  `updated_on` datetime DEFAULT NULL,
  `valid_from` datetime NOT NULL,
  `expires_on` datetime DEFAULT NULL,
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `delivered_by` int(11) NOT NULL,
  PRIMARY KEY (`message_id`),
  UNIQUE KEY `message_id_UNIQUE` (`message_id`),
  KEY `person_created_idx` (`created_by`),
  KEY `document_icon_idx` (`document_icon_id`),
  KEY `document_attachment_idx` (`document_attachment_id`),
  KEY `posted_community_idx` (`posted_in`),
  KEY `commented_on_idx` (`commented_on_message_id`),
  KEY `delivered_by_idx` (`delivered_by`),
  CONSTRAINT `commented_on` FOREIGN KEY (`commented_on_message_id`) REFERENCES `message` (`message_id`),
  CONSTRAINT `delivered_by` FOREIGN KEY (`delivered_by`) REFERENCES `delivery_system` (`delivery_system_id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `document_attachment` FOREIGN KEY (`document_attachment_id`) REFERENCES `document` (`document_id`),
  CONSTRAINT `document_icon` FOREIGN KEY (`document_icon_id`) REFERENCES `document` (`document_id`),
  CONSTRAINT `person_created` FOREIGN KEY (`created_by`) REFERENCES `person` (`person_id`),
  CONSTRAINT `posted_community` FOREIGN KEY (`posted_in`) REFERENCES `community` (`community_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='	';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `message`
--

LOCK TABLES `message` WRITE;
/*!40000 ALTER TABLE `message` DISABLE KEYS */;
/*!40000 ALTER TABLE `message` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `message_tag`
--

DROP TABLE IF EXISTS `message_tag`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `message_tag` (
  `message_tag_id` int(11) NOT NULL AUTO_INCREMENT,
  `message_id` int(11) NOT NULL,
  `tag_id` int(11) NOT NULL,
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`message_tag_id`),
  UNIQUE KEY `message_tag_id_UNIQUE` (`message_tag_id`),
  KEY `tag_message_idx` (`message_id`),
  KEY `tag_tag_idx` (`tag_id`),
  CONSTRAINT `tag_message` FOREIGN KEY (`message_id`) REFERENCES `message` (`message_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `tag_tag` FOREIGN KEY (`tag_id`) REFERENCES `tag` (`tag_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `message_tag`
--

LOCK TABLES `message_tag` WRITE;
/*!40000 ALTER TABLE `message_tag` DISABLE KEYS */;
/*!40000 ALTER TABLE `message_tag` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `person`
--

DROP TABLE IF EXISTS `person`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `person` (
  `person_id` int(11) NOT NULL AUTO_INCREMENT,
  `user_name` varchar(64) NOT NULL COLLATE utf8_bin,
  `last_name` varchar(128) NOT NULL,
  `first_name` varchar(64) DEFAULT NULL,
  `email_address` varchar(64) DEFAULT NULL,
  `phone_number_mobile` varchar(64) DEFAULT NULL,
  `hashed_password` varchar(64) NOT NULL,
  `job_position` varchar(64) DEFAULT NULL,
  `department` varchar(64) DEFAULT NULL,
  `location_building` varchar(64) DEFAULT NULL,
  `location_floor` int(11) DEFAULT NULL,
  `location_room_number` varchar(16) DEFAULT NULL,
  `date_of_birth` date DEFAULT NULL,
  `date_of_entry` date DEFAULT NULL,
  `is_active` bit(1) NOT NULL DEFAULT b'1',
  `is_login_allowed` bit(1) NOT NULL DEFAULT b'1',
  `is_online` bit(1) NOT NULL DEFAULT b'0',
  `document_image_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`person_id`),
  UNIQUE KEY `user_name_UNIQUE` (`user_name`),
  KEY `FK_person_image_idx` (`document_image_id`),
  CONSTRAINT `FK_person_image` FOREIGN KEY (`document_image_id`) REFERENCES `document` (`document_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `person`
--

LOCK TABLES `person` WRITE;
/*!40000 ALTER TABLE `person` DISABLE KEYS */;
INSERT INTO `person` VALUES (1,'pse_system','Bulletin Board System User','JBOSS','mario.loefler@edu.fh-joanneum.at','+436644711815','--','system-root',NULL,NULL,NULL,NULL,NULL,NULL,'','\0','\0',NULL);
/*!40000 ALTER TABLE `person` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `person_message`
--

DROP TABLE IF EXISTS `person_message`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `person_message` (
  `person_message_id` int(11) NOT NULL AUTO_INCREMENT,
  `target_person_id` int(11) DEFAULT NULL,
  `message_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`person_message_id`),
  KEY `person_to_idx` (`target_person_id`),
  KEY `message_sent_idx` (`message_id`),
  CONSTRAINT `message_sent` FOREIGN KEY (`message_id`) REFERENCES `message` (`message_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `person_to` FOREIGN KEY (`target_person_id`) REFERENCES `person` (`person_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `person_message`
--

LOCK TABLES `person_message` WRITE;
/*!40000 ALTER TABLE `person_message` DISABLE KEYS */;
/*!40000 ALTER TABLE `person_message` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `person_relation`
--

DROP TABLE IF EXISTS `person_relation`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `person_relation` (
  `person_relation_id` int(11) NOT NULL AUTO_INCREMENT,
  `source_person_id` int(11) NOT NULL,
  `target_person_id` int(11) NOT NULL,
  PRIMARY KEY (`person_relation_id`),
  KEY `FK_person_relation_source_idx` (`source_person_id`),
  KEY `FK_person_relation_target_idx` (`target_person_id`),
  CONSTRAINT `FK_person_relation_source` FOREIGN KEY (`source_person_id`) REFERENCES `person` (`person_id`),
  CONSTRAINT `FK_person_relation_target` FOREIGN KEY (`target_person_id`) REFERENCES `person` (`person_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='Welche Person ist mit welcher Person vernetzt';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `person_relation`
--

LOCK TABLES `person_relation` WRITE;
/*!40000 ALTER TABLE `person_relation` DISABLE KEYS */;
/*!40000 ALTER TABLE `person_relation` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `person_tag`
--

DROP TABLE IF EXISTS `person_tag`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `person_tag` (
  `person_tag_id` int(11) NOT NULL AUTO_INCREMENT,
  `person_id` int(11) NOT NULL,
  `tag_id` int(11) NOT NULL,
  PRIMARY KEY (`person_tag_id`),
  UNIQUE KEY `IDX_person_tag` (`person_id`,`tag_id`),
  KEY `FK_person_tag_tag_idx` (`tag_id`),
  CONSTRAINT `FK_person_tag_person` FOREIGN KEY (`person_id`) REFERENCES `person` (`person_id`) ON DELETE CASCADE ON UPDATE NO ACTION,
  CONSTRAINT `FK_person_tag_tag` FOREIGN KEY (`tag_id`) REFERENCES `tag` (`tag_id`) ON DELETE CASCADE ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `person_tag`
--

LOCK TABLES `person_tag` WRITE;
/*!40000 ALTER TABLE `person_tag` DISABLE KEYS */;
/*!40000 ALTER TABLE `person_tag` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tag`
--

DROP TABLE IF EXISTS `tag`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tag` (
  `tag_id` int(11) NOT NULL AUTO_INCREMENT,
  `token` varchar(32) DEFAULT NULL,
  `description` varchar(128) DEFAULT NULL,
  PRIMARY KEY (`tag_id`),
  UNIQUE KEY `tag_id_UNIQUE` (`tag_id`),
  UNIQUE KEY `token_UNIQUE` (`token`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tag`
--

LOCK TABLES `tag` WRITE;
/*!40000 ALTER TABLE `tag` DISABLE KEYS */;
/*!40000 ALTER TABLE `tag` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping routines for database 'pse'
--
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2015-10-04 19:46:40
