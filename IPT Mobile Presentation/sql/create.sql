-- MySQL Administrator dump 1.4
--
-- ------------------------------------------------------
-- Server version	5.5.16


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;


--
-- Create schema epresentation
--

CREATE DATABASE IF NOT EXISTS ipt_presentation;
USE ipt_presentation;

--
-- Definition of table `presentation`
--

DROP TABLE IF EXISTS `presentation`;
CREATE TABLE `presentation` (
  `id` int(10) unsigned NOT NULL,
  `name` varchar(120) NOT NULL,
  `description` varchar(1024) DEFAULT NULL,
  `created` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modified` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `presentation`
--

/*!40000 ALTER TABLE `presentation` DISABLE KEYS */;
INSERT INTO `presentation` (`id`,`name`,`description`,`created`,`modified`) VALUES 
 (1,'Java™ API for WebSocket (JSR-356) & Java API for JSON Processing (JSR-353)','The presentation discusses the novelties in web application development using WebSocket protocol. It explores the new features provided by Java API for WebSocket and  Java API for JSON Processing for rapid building of WebSocket client and server endpoints.','2014-05-10 12:37:59','2014-05-10 13:50:02'),
 (2,'Mobile Apps using HTML5, JavaScript and REST','Building Mobile Web Applications using HTML5, JavaScript and JAX-RS (REST)','2012-05-10 12:40:01','2012-05-10 12:40:01');
/*!40000 ALTER TABLE `presentation` ENABLE KEYS */;


--
-- Definition of table `sequence_table`
--

DROP TABLE IF EXISTS `sequence_table`;
CREATE TABLE `sequence_table` (
  `SEQ_NAME` varchar(50) NOT NULL,
  `SEQ_COUNT` decimal(38,0) DEFAULT NULL,
  PRIMARY KEY (`SEQ_NAME`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `sequence_table`
--

/*!40000 ALTER TABLE `sequence_table` DISABLE KEYS */;
INSERT INTO `sequence_table` (`SEQ_NAME`,`SEQ_COUNT`) VALUES 
 ('PRESENTATION_SEQ','2'),
 ('SLIDE_SEQ','67');
/*!40000 ALTER TABLE `sequence_table` ENABLE KEYS */;


--
-- Definition of table `slide`
--

DROP TABLE IF EXISTS `slide`;
CREATE TABLE `slide` (
  `id` int(10) unsigned NOT NULL,
  `previous` int(10) unsigned DEFAULT NULL,
  `next` int(10) unsigned DEFAULT NULL,
  `caption` varchar(255) DEFAULT NULL,
  `picture` blob,
  `audience` int(10) unsigned NOT NULL DEFAULT '0',
  `duration` time DEFAULT NULL,
  `created` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `modified` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  `presentation_id` int(10) unsigned NOT NULL,
  `mimetype` varchar(45) DEFAULT NULL,
  `data_url` varchar(255) DEFAULT NULL,
  `author` varchar(45) DEFAULT 'Trayan Iliev',
  `description` varchar(512) DEFAULT 'The slide presents details of mobile application development using jQuery Mobile JS library',
  PRIMARY KEY (`id`),
  KEY `FK_before` (`next`) USING BTREE,
  KEY `FK_slide_after` (`previous`) USING BTREE,
  KEY `FK_slide_presentation_id` (`presentation_id`),
  CONSTRAINT `FK_next` FOREIGN KEY (`next`) REFERENCES `slide` (`id`) ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT `FK_presentation_id` FOREIGN KEY (`presentation_id`) REFERENCES `presentation` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_previous` FOREIGN KEY (`previous`) REFERENCES `slide` (`id`) ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT `FK_slide_next` FOREIGN KEY (`next`) REFERENCES `slide` (`id`),
  CONSTRAINT `FK_slide_presentation_id` FOREIGN KEY (`presentation_id`) REFERENCES `presentation` (`id`),
  CONSTRAINT `FK_slide_previous` FOREIGN KEY (`previous`) REFERENCES `slide` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `slide`
--

/*!40000 ALTER TABLE `slide` DISABLE KEYS */;
INSERT INTO `slide` (`id`,`previous`,`next`,`caption`,`picture`,`audience`,`duration`,`created`,`modified`,`presentation_id`,`mimetype`,`data_url`,`author`,`description`) VALUES 
 (1,NULL,NULL,'Title Page',NULL,0,NULL,'2014-05-11 10:28:19','2014-05-11 08:13:04',1,NULL,'p1/s01.png','Trayan Iliev','The slide presents details of web application development using WebSocket JS and Java APIs (JSR-356) '),
 (2,NULL,NULL,'Agenda(1)',NULL,0,NULL,'2014-06-28 23:18:04','2014-05-11 11:09:54',1,NULL,'p1/s02.png','Trayan Iliev','The slide presents details of web application development using WebSocket JS and Java APIs (JSR-356) '),
 (3,NULL,NULL,'Title Page',NULL,0,NULL,'2012-05-11 14:30:46','2012-05-11 14:27:52',2,NULL,'p2/s01.png','Trayan Iliev','The slide presents details of mobile application development using jQuery Mobile JS library'),
 (4,NULL,NULL,'Slide 02',NULL,0,NULL,'2012-05-11 14:30:46','2012-05-11 14:28:52',2,NULL,'p2/s02.png','Trayan Iliev','The slide presents details of mobile application development using jQuery Mobile JS library'),
 (5,NULL,NULL,'Slide 03',NULL,0,NULL,'2012-05-11 14:34:54','2012-05-11 14:34:54',2,NULL,'p2/s03.png','Trayan Iliev','The slide presents details of mobile application development using jQuery Mobile JS library'),
 (6,NULL,NULL,'Slide 04',NULL,0,NULL,'2012-05-11 14:35:34','2012-05-11 14:35:34',2,NULL,'p2/s04.png','Trayan Iliev','The slide presents details of mobile application development using jQuery Mobile JS library'),
 (7,NULL,NULL,'Slide 05',NULL,0,NULL,'2012-05-11 14:35:42','2012-05-11 14:35:42',2,NULL,'p2/s05.png','Trayan Iliev','The slide presents details of mobile application development using jQuery Mobile JS library'),
 (8,NULL,NULL,'Slide 06',NULL,0,NULL,'2012-05-11 14:35:52','2012-05-11 14:35:52',2,NULL,'p2/s06.png','Trayan Iliev','The slide presents details of mobile application development using jQuery Mobile JS library'),
 (9,NULL,NULL,'Slide 07',NULL,0,NULL,'2012-05-11 14:36:00','2012-05-11 14:36:00',2,NULL,'p2/s07.png','Trayan Iliev','The slide presents details of mobile application development using jQuery Mobile JS library'),
 (10,NULL,NULL,'Slide 08',NULL,0,NULL,'2012-05-11 14:36:09','2012-05-11 14:36:09',2,NULL,'p2/s08.png','Trayan Iliev','The slide presents details of mobile application development using jQuery Mobile JS library'),
 (11,NULL,NULL,'Slide 09',NULL,0,NULL,'2012-05-11 14:36:19','2012-05-11 14:36:19',2,NULL,'p2/s09.png','Trayan Iliev','The slide presents details of mobile application development using jQuery Mobile JS library'),
 (12,NULL,NULL,'Slide 10',NULL,0,NULL,'2012-05-11 14:36:53','2012-05-11 14:36:53',2,NULL,'p2/s10.png','Trayan Iliev','The slide presents details of mobile application development using jQuery Mobile JS library'),
 (13,NULL,NULL,'Slide 11',NULL,0,NULL,'2012-05-11 14:37:00','2012-05-11 14:37:00',2,NULL,'p2/s11.png','Trayan Iliev','The slide presents details of mobile application development using jQuery Mobile JS library'),
 (14,NULL,NULL,'Slide 12',NULL,0,NULL,'2012-05-11 14:37:07','2012-05-11 14:37:07',2,NULL,'p2/s12.png','Trayan Iliev','The slide presents details of mobile application development using jQuery Mobile JS library'),
 (15,NULL,NULL,'Slide 13',NULL,0,NULL,'2012-05-11 14:37:13','2012-05-11 14:37:13',2,NULL,'p2/s13.png','Trayan Iliev','The slide presents details of mobile application development using jQuery Mobile JS library'),
 (16,NULL,NULL,'Slide 14',NULL,0,NULL,'2012-05-11 14:37:22','2012-05-11 14:37:22',2,NULL,'p2/s14.png','Trayan Iliev','The slide presents details of mobile application development using jQuery Mobile JS library'),
 (17,NULL,NULL,'Slide 15',NULL,0,NULL,'2012-05-11 14:37:28','2012-05-11 14:37:28',2,NULL,'p2/s15.png','Trayan Iliev','The slide presents details of mobile application development using jQuery Mobile JS library'),
 (18,NULL,NULL,'Slide 16',NULL,0,NULL,'2012-05-11 14:37:35','2012-05-11 14:37:35',2,NULL,'p2/s16.png','Trayan Iliev','The slide presents details of mobile application development using jQuery Mobile JS library'),
 (19,NULL,NULL,'Slide 17',NULL,0,NULL,'2012-05-11 14:37:43','2012-05-11 14:37:43',2,NULL,'p2/s17.png','Trayan Iliev','The slide presents details of mobile application development using jQuery Mobile JS library'),
 (20,NULL,NULL,'Slide 18',NULL,0,NULL,'2012-05-11 14:37:52','2012-05-11 14:37:52',2,NULL,'p2/s18.png','Trayan Iliev','The slide presents details of mobile application development using jQuery Mobile JS library'),
 (21,NULL,NULL,'Slide 19',NULL,0,NULL,'2012-05-11 14:38:03','2012-05-11 14:38:03',2,NULL,'p2/s19.png','Trayan Iliev','The slide presents details of mobile application development using jQuery Mobile JS library'),
 (22,NULL,NULL,'Slide 20',NULL,0,NULL,'2012-05-11 14:38:19','2012-05-11 14:38:19',2,NULL,'p2/s20.png','Trayan Iliev','The slide presents details of mobile application development using jQuery Mobile JS library'),
 (23,NULL,NULL,'Slide 21',NULL,0,NULL,'2012-05-11 14:38:28','2012-05-11 14:38:28',2,NULL,'p2/s21.png','Trayan Iliev','The slide presents details of mobile application development using jQuery Mobile JS library'),
 (24,NULL,NULL,'Slide 22',NULL,0,NULL,'2012-05-11 14:38:37','2012-05-11 14:38:37',2,NULL,'p2/s22.png','Trayan Iliev','The slide presents details of mobile application development using jQuery Mobile JS library'),
 (25,NULL,NULL,'Slide 23',NULL,0,NULL,'2012-05-11 14:38:47','2012-05-11 14:38:47',2,NULL,'p2/s23.png','Trayan Iliev','The slide presents details of mobile application development using jQuery Mobile JS library'),
 (26,NULL,NULL,'Slide 24',NULL,0,NULL,'2012-05-11 14:38:54','2012-05-11 14:38:54',2,NULL,'p2/s24.png','Trayan Iliev','The slide presents details of mobile application development using jQuery Mobile JS library'),
 (27,NULL,NULL,'Slide 33',NULL,0,NULL,'2012-05-11 14:39:16','2012-05-11 14:39:16',2,NULL,'p2/s33.png','Trayan Iliev','The slide presents details of mobile application development using jQuery Mobile JS library'),
 (28,NULL,NULL,'Agenda(2)',NULL,0,NULL,'2014-06-28 23:18:04','2014-06-28 23:18:04',1,NULL,'p1/s03.png','Trayan Iliev','The slide presents details of web application development using WebSocket JS and Java APIs (JSR-356) '),
 (29,NULL,NULL,'Agenda(3)',NULL,0,NULL,'2014-06-28 23:18:04','2014-06-28 23:18:04',1,NULL,'p1/s04.png','Trayan Iliev','The slide presents details of web application development using WebSocket JS and Java APIs (JSR-356) '),
 (30,NULL,NULL,'Question',NULL,0,NULL,'2014-06-28 23:18:04','2014-06-28 23:18:04',1,NULL,'p1/s05.png','Trayan Iliev','The slide presents details of web application development using WebSocket JS and Java APIs (JSR-356) '),
 (31,NULL,NULL,'Internet of Things (IoT)',NULL,0,NULL,'2014-06-28 23:18:04','2014-06-28 23:18:04',1,NULL,'p1/s06.png','Trayan Iliev','The slide presents details of web application development using WebSocket JS and Java APIs (JSR-356) '),
 (32,NULL,NULL,'Perspectives',NULL,0,NULL,'2014-06-28 23:18:04','2014-06-28 23:18:04',1,NULL,'p1/s07.png','Trayan Iliev','The slide presents details of web application development using WebSocket JS and Java APIs (JSR-356) '),
 (33,NULL,NULL,'Web Sockets',NULL,0,NULL,'2014-06-28 23:18:04','2014-06-28 23:18:04',1,NULL,'p1/s08.png','Trayan Iliev','The slide presents details of web application development using WebSocket JS and Java APIs (JSR-356) '),
 (34,NULL,NULL,'Web Sockets 1',NULL,0,NULL,'2014-06-28 23:18:04','2014-06-28 23:18:04',1,NULL,'p1/s09.png','Trayan Iliev','The slide presents details of web application development using WebSocket JS and Java APIs (JSR-356) '),
 (35,NULL,NULL,'Web Sockets 2',NULL,0,NULL,'2014-06-28 23:18:04','2014-06-28 23:18:04',1,NULL,'p1/s10.png','Trayan Iliev','The slide presents details of web application development using WebSocket JS and Java APIs (JSR-356) '),
 (36,NULL,NULL,'Web Sockets 3',NULL,0,NULL,'2014-06-28 23:18:04','2014-06-28 23:18:04',1,NULL,'p1/s11.png','Trayan Iliev','The slide presents details of web application development using WebSocket JS and Java APIs (JSR-356) '),
 (37,NULL,NULL,'Web Sockets 4',NULL,0,NULL,'2014-06-28 23:18:04','2014-06-28 23:18:04',1,NULL,'p1/s12.png','Trayan Iliev','The slide presents details of web application development using WebSocket JS and Java APIs (JSR-356) '),
 (38,NULL,NULL,'Web Sockets 5',NULL,0,NULL,'2014-06-28 23:18:04','2014-06-28 23:18:04',1,NULL,'p1/s13.png','Trayan Iliev','The slide presents details of web application development using WebSocket JS and Java APIs (JSR-356) '),
 (39,NULL,NULL,'Web Sockets 6',NULL,0,NULL,'2014-06-28 23:18:04','2014-06-28 23:18:04',1,NULL,'p1/s14.png','Trayan Iliev','The slide presents details of web application development using WebSocket JS and Java APIs (JSR-356) '),
 (40,NULL,NULL,'Web Sockets 7',NULL,0,NULL,'2014-06-28 23:18:04','2014-06-28 23:18:04',1,NULL,'p1/s15.png','Trayan Iliev','The slide presents details of web application development using WebSocket JS and Java APIs (JSR-356) '),
 (41,NULL,NULL,'Web Sockets 8',NULL,0,NULL,'2014-06-28 23:18:04','2014-06-28 23:18:04',1,NULL,'p1/s16.png','Trayan Iliev','The slide presents details of web application development using WebSocket JS and Java APIs (JSR-356) '),
 (42,NULL,NULL,'Web Sockets 9',NULL,0,NULL,'2014-06-28 23:18:04','2014-06-28 23:18:04',1,NULL,'p1/s17.png','Trayan Iliev','The slide presents details of web application development using WebSocket JS and Java APIs (JSR-356) '),
 (43,NULL,NULL,'Web Sockets 10',NULL,0,NULL,'2014-06-28 23:18:04','2014-06-28 23:18:04',1,NULL,'p1/s18.png','Trayan Iliev','The slide presents details of web application development using WebSocket JS and Java APIs (JSR-356) '),
 (44,NULL,NULL,'Web Sockets 11',NULL,0,NULL,'2014-06-28 23:18:04','2014-06-28 23:18:04',1,NULL,'p1/s19.png','Trayan Iliev','The slide presents details of web application development using WebSocket JS and Java APIs (JSR-356) '),
 (45,NULL,NULL,'Web Sockets 12',NULL,0,NULL,'2014-06-28 23:18:04','2014-06-28 23:18:04',1,NULL,'p1/s20.png','Trayan Iliev','The slide presents details of web application development using WebSocket JS and Java APIs (JSR-356) '),
 (46,NULL,NULL,'Web Sockets 13',NULL,0,NULL,'2014-06-28 23:18:04','2014-06-28 23:18:04',1,NULL,'p1/s21.png','Trayan Iliev','The slide presents details of web application development using WebSocket JS and Java APIs (JSR-356) '),
 (47,NULL,NULL,'Web Sockets 14',NULL,0,NULL,'2014-06-28 23:18:04','2014-06-28 23:18:04',1,NULL,'p1/s22.png','Trayan Iliev','The slide presents details of web application development using WebSocket JS and Java APIs (JSR-356) '),
 (48,NULL,NULL,'Web Sockets 15',NULL,0,NULL,'2014-06-28 23:18:04','2014-06-28 23:18:04',1,NULL,'p1/s23.png','Trayan Iliev','The slide presents details of web application development using WebSocket JS and Java APIs (JSR-356) '),
 (49,NULL,NULL,'Web Sockets 16',NULL,0,NULL,'2014-06-28 23:18:04','2014-06-28 23:18:04',1,NULL,'p1/s24.png','Trayan Iliev','The slide presents details of web application development using WebSocket JS and Java APIs (JSR-356) '),
 (50,NULL,NULL,'Web Sockets 17',NULL,0,NULL,'2014-06-28 23:18:04','2014-06-28 23:18:04',1,NULL,'p1/s25.png','Trayan Iliev','The slide presents details of web application development using WebSocket JS and Java APIs (JSR-356) '),
 (51,NULL,NULL,'Web Sockets 18',NULL,0,NULL,'2014-06-28 23:18:04','2014-06-28 23:18:04',1,NULL,'p1/s26.png','Trayan Iliev','The slide presents details of web application development using WebSocket JS and Java APIs (JSR-356) '),
 (52,NULL,NULL,'Web Sockets 19',NULL,0,NULL,'2014-06-28 23:18:04','2014-06-28 23:18:04',1,NULL,'p1/s27.png','Trayan Iliev','The slide presents details of web application development using WebSocket JS and Java APIs (JSR-356) '),
 (53,NULL,NULL,'Web Sockets 20',NULL,0,NULL,'2014-06-28 23:18:04','2014-06-28 23:18:04',1,NULL,'p1/s28.png','Trayan Iliev','The slide presents details of web application development using WebSocket JS and Java APIs (JSR-356) '),
 (54,NULL,NULL,'Web Sockets 21',NULL,0,NULL,'2014-06-28 23:18:04','2014-06-28 23:18:04',1,NULL,'p1/s29.png','Trayan Iliev','The slide presents details of web application development using WebSocket JS and Java APIs (JSR-356) '),
 (55,NULL,NULL,'Web Sockets 22',NULL,0,NULL,'2014-06-28 23:18:04','2014-06-28 23:18:04',1,NULL,'p1/s30.png','Trayan Iliev','The slide presents details of web application development using WebSocket JS and Java APIs (JSR-356) '),
 (56,NULL,NULL,'Web Sockets 23',NULL,0,NULL,'2014-06-28 23:18:04','2014-06-28 23:18:04',1,NULL,'p1/s31.png','Trayan Iliev','The slide presents details of web application development using WebSocket JS and Java APIs (JSR-356) '),
 (57,NULL,NULL,'Web Sockets 24',NULL,0,NULL,'2014-06-28 23:18:04','2014-06-28 23:18:04',1,NULL,'p1/s32.png','Trayan Iliev','The slide presents details of web application development using WebSocket JS and Java APIs (JSR-356) '),
 (59,NULL,NULL,'Web Sockets 26',NULL,0,NULL,'2014-06-28 23:18:04','2014-06-28 23:18:04',1,NULL,'p1/s34.png','Trayan Iliev','The slide presents details of web application development using WebSocket JS and Java APIs (JSR-356) '),
 (60,NULL,NULL,'Web Sockets 27',NULL,0,NULL,'2014-06-28 23:18:04','2014-06-28 23:18:04',1,NULL,'p1/s35.png','Trayan Iliev','The slide presents details of web application development using WebSocket JS and Java APIs (JSR-356) '),
 (61,NULL,NULL,'Web Sockets 28',NULL,0,NULL,'2014-06-28 23:18:04','2014-06-28 23:18:04',1,NULL,'p1/s36.png','Trayan Iliev','The slide presents details of web application development using WebSocket JS and Java APIs (JSR-356) '),
 (62,NULL,NULL,'Web Sockets 29',NULL,0,NULL,'2014-06-28 23:18:04','2014-06-28 23:18:04',1,NULL,'p1/s37.png','Trayan Iliev','The slide presents details of web application development using WebSocket JS and Java APIs (JSR-356) '),
 (63,NULL,NULL,'Web Sockets 30',NULL,0,NULL,'2014-06-28 23:18:04','2014-06-28 23:18:04',1,NULL,'p1/s38.png','Trayan Iliev','The slide presents details of web application development using WebSocket JS and Java APIs (JSR-356) '),
 (64,NULL,NULL,'Web Sockets 31',NULL,0,NULL,'2014-06-28 23:18:04','2014-06-28 23:18:04',1,NULL,'p1/s39.png','Trayan Iliev','The slide presents details of web application development using WebSocket JS and Java APIs (JSR-356) '),
 (65,NULL,NULL,'Web Sockets 32',NULL,0,NULL,'2014-06-28 23:18:04','2014-06-28 23:18:04',1,NULL,'p1/s40.png','Trayan Iliev','The slide presents details of web application development using WebSocket JS and Java APIs (JSR-356) '),
 (66,NULL,NULL,'Web Sockets 33',NULL,0,NULL,'2014-06-28 23:18:04','2014-06-28 23:18:04',1,NULL,'p1/s41.png','Trayan Iliev','The slide presents details of web application development using WebSocket JS and Java APIs (JSR-356) '),
 (67,NULL,NULL,'References',NULL,0,NULL,'2014-06-28 23:18:04','2014-06-28 23:18:04',1,NULL,'p1/s42.png','Trayan Iliev','The slide presents references for WebSocket JS and Java APIs (JSR-356) ');
/*!40000 ALTER TABLE `slide` ENABLE KEYS */;




/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
