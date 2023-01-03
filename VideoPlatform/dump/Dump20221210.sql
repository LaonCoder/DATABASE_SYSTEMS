CREATE DATABASE  IF NOT EXISTS `video_database` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `video_database`;
-- MySQL dump 10.13  Distrib 8.0.31, for Win64 (x86_64)
--
-- Host: localhost    Database: video_database
-- ------------------------------------------------------
-- Server version	8.0.31

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `add_to_playlist`
--

DROP TABLE IF EXISTS `add_to_playlist`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `add_to_playlist` (
  `email` varchar(100) NOT NULL,
  `video_id` char(64) NOT NULL,
  `playlist_name` varchar(100) NOT NULL,
  `added_time` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`email`,`video_id`),
  KEY `video_id` (`video_id`),
  CONSTRAINT `add_to_playlist_ibfk_1` FOREIGN KEY (`email`) REFERENCES `users` (`email`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `add_to_playlist_ibfk_2` FOREIGN KEY (`video_id`) REFERENCES `video` (`video_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `add_to_playlist`
--

LOCK TABLES `add_to_playlist` WRITE;
/*!40000 ALTER TABLE `add_to_playlist` DISABLE KEYS */;
INSERT INTO `add_to_playlist` VALUES ('user1@gmail.com','0srJwFoNSfkLXQDSZwjgbMm5rWNvCBwG','다시보기','2022-12-10 07:11:46'),('user1@gmail.com','Q4AAe6p0Rk0HkW4ZZtJJDdkwBZ55iOK4','다시보기','2022-12-10 07:14:11'),('user2@gmail.com','sVfRfl0ksXYAFFNqE5xA17ymNxSfzyg3','내 음악','2022-12-10 07:26:44'),('user2@gmail.com','YjfFuxxlQFwFZohMRIYAhKlXMp9FX95R','내 음악','2022-12-10 07:26:18'),('user3@gmail.com','0srJwFoNSfkLXQDSZwjgbMm5rWNvCBwG','내 플레이리스트','2022-12-10 07:28:27'),('user3@gmail.com','J7UR63BwwH1LIN36LGbsjqKJwJx7WnyX','내 플레이리스트','2022-12-10 07:29:44'),('user3@gmail.com','JQcQR4AqGqAHW0fPlRlqv8ba4bl1oqw9','내 플레이리스트','2022-12-10 07:30:00'),('user3@gmail.com','lWAVtjvNig0VsIGL50cn9qM8C0Qz57Lf','내 플레이리스트','2022-12-10 07:30:13'),('user4@gmail.com','GN1UvMAQp5jLg71mhPoOn1eCRDB3yLUo','음악','2022-12-10 07:33:50'),('user4@gmail.com','hDl4vbcUDsRTYF2Wbmu6fteYV4X2tJk8','음악','2022-12-10 07:34:05'),('user4@gmail.com','Iltc83wQ1G3vZ1M9fOVyxQbuXGOYujfc','다시보기','2022-12-10 07:35:10'),('user4@gmail.com','LG1DFaqLnisjIlTIcP7uzZuktsMfNnIT','음악','2022-12-10 07:34:16'),('user4@gmail.com','lWAVtjvNig0VsIGL50cn9qM8C0Qz57Lf','다시보기','2022-12-10 07:34:35'),('user4@gmail.com','nBT2OvJ9Hp8EnhIaPELrHQe25MRREWKu','다시보기','2022-12-10 07:34:42'),('user4@gmail.com','u2AbGw39r3r1MzpT8zD5nZdGFnUqyUfk','다시보기','2022-12-10 07:34:56'),('user4@gmail.com','YjfFuxxlQFwFZohMRIYAhKlXMp9FX95R','음악','2022-12-10 07:35:46'),('user5@gmail.com','awRpmasPDO9OMQicLzOG5WtEHdYjd9oS','내 음악','2022-12-10 07:39:16'),('user5@gmail.com','Bbo9qekAE4oZe3JkizoNFuc6PpkGgNCb','내 음악','2022-12-10 07:39:06'),('user5@gmail.com','GN1UvMAQp5jLg71mhPoOn1eCRDB3yLUo','내 음악','2022-12-10 07:39:31'),('user5@gmail.com','hDl4vbcUDsRTYF2Wbmu6fteYV4X2tJk8','내 음악','2022-12-10 07:39:59'),('user5@gmail.com','IQuqFi5xNn8Zc7gP5uVkcBxRabtmPrAI','내 음악','2022-12-10 07:40:16'),('user5@gmail.com','LG1DFaqLnisjIlTIcP7uzZuktsMfNnIT','내 음악','2022-12-10 07:40:26'),('user5@gmail.com','YjfFuxxlQFwFZohMRIYAhKlXMp9FX95R','내 음악','2022-12-10 07:40:51'),('user7@gmail.com','GN1UvMAQp5jLg71mhPoOn1eCRDB3yLUo','내 플리','2022-12-10 07:49:52'),('user7@gmail.com','YjfFuxxlQFwFZohMRIYAhKlXMp9FX95R','내 플리','2022-12-10 07:49:40');
/*!40000 ALTER TABLE `add_to_playlist` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `manager`
--

DROP TABLE IF EXISTS `manager`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `manager` (
  `email` varchar(100) NOT NULL,
  `manager_password` varchar(100) NOT NULL,
  `manager_name` varchar(200) NOT NULL,
  PRIMARY KEY (`email`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `manager`
--

LOCK TABLES `manager` WRITE;
/*!40000 ALTER TABLE `manager` DISABLE KEYS */;
INSERT INTO `manager` VALUES ('manager@gmail.com','manager123!','Manager'),('manager1@gmail.com','manager1!','매니저1'),('manager2@gmail.com','manager123!','Manager2');
/*!40000 ALTER TABLE `manager` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `report`
--

DROP TABLE IF EXISTS `report`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `report` (
  `email` varchar(100) NOT NULL,
  `video_id` char(64) NOT NULL,
  `reported_time` timestamp NULL DEFAULT NULL,
  `reason` varchar(200) DEFAULT NULL,
  PRIMARY KEY (`email`,`video_id`),
  KEY `video_id` (`video_id`),
  CONSTRAINT `report_ibfk_1` FOREIGN KEY (`email`) REFERENCES `users` (`email`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `report_ibfk_2` FOREIGN KEY (`video_id`) REFERENCES `video` (`video_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `report`
--

LOCK TABLES `report` WRITE;
/*!40000 ALTER TABLE `report` DISABLE KEYS */;
INSERT INTO `report` VALUES ('user9@gmail.com','sVfRfl0ksXYAFFNqE5xA17ymNxSfzyg3','2022-12-10 07:57:28','Too many Ads');
/*!40000 ALTER TABLE `report` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `subscribe`
--

DROP TABLE IF EXISTS `subscribe`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `subscribe` (
  `subscriber_email` varchar(100) NOT NULL,
  `publisher_email` varchar(100) NOT NULL,
  PRIMARY KEY (`subscriber_email`,`publisher_email`),
  KEY `publisher_email` (`publisher_email`),
  CONSTRAINT `subscribe_ibfk_1` FOREIGN KEY (`subscriber_email`) REFERENCES `users` (`email`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `subscribe_ibfk_2` FOREIGN KEY (`publisher_email`) REFERENCES `users` (`email`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `subscribe`
--

LOCK TABLES `subscribe` WRITE;
/*!40000 ALTER TABLE `subscribe` DISABLE KEYS */;
INSERT INTO `subscribe` VALUES ('user5@gmail.com','user1@gmail.com'),('user4@gmail.com','user2@gmail.com'),('user6@gmail.com','user2@gmail.com'),('user8@gmail.com','user2@gmail.com'),('user1@gmail.com','user3@gmail.com'),('user2@gmail.com','user3@gmail.com'),('user4@gmail.com','user3@gmail.com'),('user5@gmail.com','user3@gmail.com'),('user6@gmail.com','user3@gmail.com'),('user7@gmail.com','user3@gmail.com'),('user8@gmail.com','user3@gmail.com'),('user4@gmail.com','user4@gmail.com'),('user6@gmail.com','user4@gmail.com'),('user7@gmail.com','user4@gmail.com'),('user8@gmail.com','user4@gmail.com'),('user2@gmail.com','user5@gmail.com'),('user4@gmail.com','user5@gmail.com'),('user8@gmail.com','user5@gmail.com'),('user6@gmail.com','user6@gmail.com'),('user7@gmail.com','user6@gmail.com'),('user7@gmail.com','user7@gmail.com'),('user2@gmail.com','user8@gmail.com'),('user4@gmail.com','user8@gmail.com'),('user6@gmail.com','user8@gmail.com'),('user8@gmail.com','user8@gmail.com'),('user1@gmail.com','user9@gmail.com');
/*!40000 ALTER TABLE `subscribe` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `users` (
  `email` varchar(100) NOT NULL,
  `user_password` varchar(100) NOT NULL,
  `user_name` varchar(200) NOT NULL,
  `age` int NOT NULL,
  `sex` varchar(20) NOT NULL,
  `num_of_subscribers` int DEFAULT '0',
  PRIMARY KEY (`email`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES ('user@gmail.com','user123!','User',23,'남성',0),('user1@gmail.com','user123!','클래식 매니아',34,'남성',1),('user2@gmail.com','user123!','영화 리뷰',24,'남성',3),('user3@gmail.com','user123!','디스 뮤직',27,'여성',7),('user4@gmail.com','user456!','코믹 튜브',30,'남성',4),('user5@gmail.com','user456!','나의 브이로그',18,'여성',3),('user6@gmail.com','user456!','지식백과',38,'남성',2),('user7@gmail.com','user789!','동네 축구',36,'남성',1),('user8@gmail.com','user789!','골동품 사나이들',32,'남성',4),('user9@gmail.com','user789!','Painters',26,'여성',1);
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `video`
--

DROP TABLE IF EXISTS `video`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `video` (
  `video_id` char(64) NOT NULL,
  `title` varchar(100) NOT NULL,
  `views` int NOT NULL DEFAULT '0',
  `uploader_email` varchar(100) NOT NULL,
  `uploaded_time` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`video_id`),
  KEY `uploader_email` (`uploader_email`),
  CONSTRAINT `video_ibfk_1` FOREIGN KEY (`uploader_email`) REFERENCES `users` (`email`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `video`
--

LOCK TABLES `video` WRITE;
/*!40000 ALTER TABLE `video` DISABLE KEYS */;
INSERT INTO `video` VALUES ('0srJwFoNSfkLXQDSZwjgbMm5rWNvCBwG','이강인 선수 초대석! #카타르월드컵',19,'user7@gmail.com','2022-12-10 07:10:05'),('9isggOYC9KfbfVg26O6vnzLLRojjLMag','아인슈타인의 친필 사인이 박힌 희귀 지폐의 가격',7,'user8@gmail.com','2022-12-10 07:04:34'),('A5OhsZKVMvkr07QIE2OTd2FrZQXm91lW','브루스 윌리스 최고의 반전 영화! - 식스센스(1999)',3,'user2@gmail.com','2022-12-10 06:35:50'),('awRpmasPDO9OMQicLzOG5WtEHdYjd9oS','윤하 플레이리스트 1시간 (광고X)',6,'user3@gmail.com','2022-12-10 06:44:31'),('Bbo9qekAE4oZe3JkizoNFuc6PpkGgNCb','멘델스존 : 봄의 노래',2,'user1@gmail.com','2022-12-10 06:29:19'),('C99TbUX8y6nSpOt0Blhqlik9kzQ0AWoU','공학 기술의 집약체, 현수교',3,'user6@gmail.com','2022-12-10 06:57:21'),('CFAvmEiM39kuHdg0oaUJpBaLHCuHwDL5','\'억\' 소리 나는 그리스 금화!',3,'user8@gmail.com','2022-12-10 07:02:17'),('E22dSDIyXcoF5yCyg4EY2mAuyBJYpqbk','수능 1년도 안남은 고2 정시러의 브이로그',5,'user5@gmail.com','2022-12-10 06:53:06'),('GN1UvMAQp5jLg71mhPoOn1eCRDB3yLUo','Playlist 감성 힙합 2시간 (광고 없음)',7,'user3@gmail.com','2022-12-10 06:46:05'),('Gxis7HDTUKUeI2J0PWLYGru43viqzbo8','5 Habits of People with Good Painting Skills',0,'user9@gmail.com','2022-12-10 07:08:13'),('haqIE7tK1O66CAKoKj0ZcfCfuRAqqofW','초등학생들의 엉뚱한 시험 답안ㅋㅋㅋ',4,'user4@gmail.com','2022-12-10 06:49:50'),('hDl4vbcUDsRTYF2Wbmu6fteYV4X2tJk8','캐롤과 함께하는 연말 (Jazz music)',8,'user3@gmail.com','2022-12-10 06:45:13'),('Iltc83wQ1G3vZ1M9fOVyxQbuXGOYujfc','셔터아일랜드(2010) 리뷰 결말 포함',6,'user2@gmail.com','2022-12-10 06:33:37'),('IQuqFi5xNn8Zc7gP5uVkcBxRabtmPrAI','모의고사 시험장 백색소음 10시간',3,'user5@gmail.com','2022-12-10 06:54:00'),('J7UR63BwwH1LIN36LGbsjqKJwJx7WnyX','전 세계에 딱 10개만 존재하는 한정판 레고의 가격은?',2,'user8@gmail.com','2022-12-10 07:03:46'),('jf8qzdZCZukQ2EU12wNaH4oYG4iCz8EI','영유아도 이해하는 알고리즘의 원리',4,'user6@gmail.com','2022-12-10 06:58:22'),('jOlGyREOq55NJzYl2Xz6UNkgoyfvd4R3','모의고사 망한 날 친구들과의 엽떡 먹방',5,'user5@gmail.com','2022-12-10 06:55:04'),('JQcQR4AqGqAHW0fPlRlqv8ba4bl1oqw9','웃긴 밈 / 실수 모음 #1',4,'user4@gmail.com','2022-12-10 06:49:03'),('JzPbbUOUCRKtMCYQ4dPsW6dWz6oo5OnV','의료 수가란 무엇일까?',2,'user6@gmail.com','2022-12-10 06:56:11'),('LG1DFaqLnisjIlTIcP7uzZuktsMfNnIT','쇼팽 : 야상곡 2번',2,'user1@gmail.com','2022-12-10 06:31:00'),('lWAVtjvNig0VsIGL50cn9qM8C0Qz57Lf','반전 또 반전! - 유주얼서스펙트(1995)',4,'user2@gmail.com','2022-12-10 06:34:17'),('nBT2OvJ9Hp8EnhIaPELrHQe25MRREWKu','꿈에서 나갈 수 없다! - 인셉션(2010)',4,'user2@gmail.com','2022-12-10 06:39:29'),('nhy1AWUUceNFTvPfVCjEXNAd7lI02FPH','The Most Famous Painters Today',2,'user9@gmail.com','2022-12-10 07:07:32'),('nmC9NYdlWCY0jWqqp9tzvbpXgkF5i6Jg','비발디 : 4계 봄 1악장',2,'user1@gmail.com','2022-12-10 06:29:52'),('Q4AAe6p0Rk0HkW4ZZtJJDdkwBZ55iOK4','Top 10 Paintings of All Time',1,'user9@gmail.com','2022-12-10 07:06:33'),('sVfRfl0ksXYAFFNqE5xA17ymNxSfzyg3','코딩하면서 틀어 놓기 좋은 Jazz 3시간 플레이리스트',3,'user3@gmail.com','2022-12-10 06:42:24'),('sXHhC43n4TvH0ioRLrnunSUTgFDtqGpm','오늘도 평화로운 바보들의 하루',4,'user4@gmail.com','2022-12-10 06:50:28'),('u2AbGw39r3r1MzpT8zD5nZdGFnUqyUfk','당신을 종신형에 선고합니다. - 쇼생크 탈출(1994)',8,'user2@gmail.com','2022-12-10 06:38:05'),('V9RX4R5i0Urc3NreZsdIzHtL8OTZcweT','베토벤 : 로망스 2번',1,'user1@gmail.com','2022-12-10 06:32:00'),('xAQTAZJxP7sQpHtJlnDQnv8yQTzD9kj3','모차르트 : 아이네 크라이네',0,'user1@gmail.com','2022-12-10 06:30:09'),('YjfFuxxlQFwFZohMRIYAhKlXMp9FX95R','2022년 아이돌 노래모음 TOP 30 (가사 포함)',11,'user3@gmail.com','2022-12-10 06:43:07'),('Ztoao6AEhjM7eR4UZ3FQLNPUaVYmptJi','슈베르트 : 송어 4악장',0,'user1@gmail.com','2022-12-10 06:30:23');
/*!40000 ALTER TABLE `video` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `video_categories`
--

DROP TABLE IF EXISTS `video_categories`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `video_categories` (
  `video_id` char(64) NOT NULL,
  `category` char(150) NOT NULL,
  PRIMARY KEY (`video_id`,`category`),
  CONSTRAINT `video_categories_ibfk_1` FOREIGN KEY (`video_id`) REFERENCES `video` (`video_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `video_categories`
--

LOCK TABLES `video_categories` WRITE;
/*!40000 ALTER TABLE `video_categories` DISABLE KEYS */;
INSERT INTO `video_categories` VALUES ('0srJwFoNSfkLXQDSZwjgbMm5rWNvCBwG','#SPORTS'),('9isggOYC9KfbfVg26O6vnzLLRojjLMag','#NONE'),('A5OhsZKVMvkr07QIE2OTd2FrZQXm91lW','#FILM/ANIMATION'),('awRpmasPDO9OMQicLzOG5WtEHdYjd9oS','#MUSIC'),('Bbo9qekAE4oZe3JkizoNFuc6PpkGgNCb','#MUSIC'),('C99TbUX8y6nSpOt0Blhqlik9kzQ0AWoU','#EDUCATION'),('C99TbUX8y6nSpOt0Blhqlik9kzQ0AWoU','#SCIENCE/TECHNOLOGY'),('CFAvmEiM39kuHdg0oaUJpBaLHCuHwDL5','#NONE'),('E22dSDIyXcoF5yCyg4EY2mAuyBJYpqbk','#PEOPLE/BLOGS'),('GN1UvMAQp5jLg71mhPoOn1eCRDB3yLUo','#MUSIC'),('Gxis7HDTUKUeI2J0PWLYGru43viqzbo8','#EDUCATION'),('Gxis7HDTUKUeI2J0PWLYGru43viqzbo8','#ENTERTAINMENT'),('haqIE7tK1O66CAKoKj0ZcfCfuRAqqofW','#COMEDY'),('haqIE7tK1O66CAKoKj0ZcfCfuRAqqofW','#ENTERTAINMENT'),('hDl4vbcUDsRTYF2Wbmu6fteYV4X2tJk8','#MUSIC'),('Iltc83wQ1G3vZ1M9fOVyxQbuXGOYujfc','#FILM/ANIMATION'),('IQuqFi5xNn8Zc7gP5uVkcBxRabtmPrAI','#PEOPLE/BLOGS'),('J7UR63BwwH1LIN36LGbsjqKJwJx7WnyX','#NONE'),('jf8qzdZCZukQ2EU12wNaH4oYG4iCz8EI','#EDUCATION'),('jf8qzdZCZukQ2EU12wNaH4oYG4iCz8EI','#SCIENCE/TECHNOLOGY'),('jOlGyREOq55NJzYl2Xz6UNkgoyfvd4R3','#FOOD'),('jOlGyREOq55NJzYl2Xz6UNkgoyfvd4R3','#PEOPLE/BLOGS'),('JQcQR4AqGqAHW0fPlRlqv8ba4bl1oqw9','#COMEDY'),('JzPbbUOUCRKtMCYQ4dPsW6dWz6oo5OnV','#EDUCATION'),('JzPbbUOUCRKtMCYQ4dPsW6dWz6oo5OnV','#NEWS/POLITICS'),('LG1DFaqLnisjIlTIcP7uzZuktsMfNnIT','#MUSIC'),('lWAVtjvNig0VsIGL50cn9qM8C0Qz57Lf','#FILM/ANIMATION'),('nBT2OvJ9Hp8EnhIaPELrHQe25MRREWKu','#FILM/ANIMATION'),('nhy1AWUUceNFTvPfVCjEXNAd7lI02FPH','#EDUCATION'),('nhy1AWUUceNFTvPfVCjEXNAd7lI02FPH','#ENTERTAINMENT'),('nmC9NYdlWCY0jWqqp9tzvbpXgkF5i6Jg','#MUSIC'),('Q4AAe6p0Rk0HkW4ZZtJJDdkwBZ55iOK4','#ENTERTAINMENT'),('sVfRfl0ksXYAFFNqE5xA17ymNxSfzyg3','#MUSIC'),('sXHhC43n4TvH0ioRLrnunSUTgFDtqGpm','#COMEDY'),('u2AbGw39r3r1MzpT8zD5nZdGFnUqyUfk','#FILM/ANIMATION'),('V9RX4R5i0Urc3NreZsdIzHtL8OTZcweT','#MUSIC'),('xAQTAZJxP7sQpHtJlnDQnv8yQTzD9kj3','#MUSIC'),('YjfFuxxlQFwFZohMRIYAhKlXMp9FX95R','#MUSIC'),('Ztoao6AEhjM7eR4UZ3FQLNPUaVYmptJi','#MUSIC');
/*!40000 ALTER TABLE `video_categories` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `watch`
--

DROP TABLE IF EXISTS `watch`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `watch` (
  `email` varchar(100) NOT NULL,
  `video_id` char(64) NOT NULL,
  `last_viewed_time` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`email`,`video_id`),
  KEY `video_id` (`video_id`),
  CONSTRAINT `watch_ibfk_1` FOREIGN KEY (`email`) REFERENCES `users` (`email`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `watch_ibfk_2` FOREIGN KEY (`video_id`) REFERENCES `video` (`video_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `watch`
--

LOCK TABLES `watch` WRITE;
/*!40000 ALTER TABLE `watch` DISABLE KEYS */;
INSERT INTO `watch` VALUES ('user@gmail.com','0srJwFoNSfkLXQDSZwjgbMm5rWNvCBwG','2022-12-10 14:00:44'),('user1@gmail.com','0srJwFoNSfkLXQDSZwjgbMm5rWNvCBwG','2022-12-10 07:11:24'),('user1@gmail.com','9isggOYC9KfbfVg26O6vnzLLRojjLMag','2022-12-10 07:12:55'),('user1@gmail.com','awRpmasPDO9OMQicLzOG5WtEHdYjd9oS','2022-12-10 07:13:03'),('user1@gmail.com','Bbo9qekAE4oZe3JkizoNFuc6PpkGgNCb','2022-12-10 07:13:11'),('user1@gmail.com','hDl4vbcUDsRTYF2Wbmu6fteYV4X2tJk8','2022-12-10 07:13:28'),('user1@gmail.com','LG1DFaqLnisjIlTIcP7uzZuktsMfNnIT','2022-12-10 07:13:53'),('user1@gmail.com','Q4AAe6p0Rk0HkW4ZZtJJDdkwBZ55iOK4','2022-12-10 07:14:01'),('user2@gmail.com','0srJwFoNSfkLXQDSZwjgbMm5rWNvCBwG','2022-12-10 07:24:11'),('user2@gmail.com','9isggOYC9KfbfVg26O6vnzLLRojjLMag','2022-12-10 07:24:01'),('user2@gmail.com','hDl4vbcUDsRTYF2Wbmu6fteYV4X2tJk8','2022-12-10 07:24:16'),('user2@gmail.com','JQcQR4AqGqAHW0fPlRlqv8ba4bl1oqw9','2022-12-10 07:24:25'),('user2@gmail.com','lWAVtjvNig0VsIGL50cn9qM8C0Qz57Lf','2022-12-10 07:24:42'),('user2@gmail.com','nBT2OvJ9Hp8EnhIaPELrHQe25MRREWKu','2022-12-10 07:24:37'),('user2@gmail.com','sXHhC43n4TvH0ioRLrnunSUTgFDtqGpm','2022-12-10 07:24:47'),('user2@gmail.com','u2AbGw39r3r1MzpT8zD5nZdGFnUqyUfk','2022-12-10 07:24:53'),('user2@gmail.com','YjfFuxxlQFwFZohMRIYAhKlXMp9FX95R','2022-12-10 07:25:43'),('user3@gmail.com','0srJwFoNSfkLXQDSZwjgbMm5rWNvCBwG','2022-12-10 07:28:14'),('user3@gmail.com','9isggOYC9KfbfVg26O6vnzLLRojjLMag','2022-12-10 07:28:36'),('user3@gmail.com','A5OhsZKVMvkr07QIE2OTd2FrZQXm91lW','2022-12-10 07:28:42'),('user3@gmail.com','C99TbUX8y6nSpOt0Blhqlik9kzQ0AWoU','2022-12-10 07:28:56'),('user3@gmail.com','GN1UvMAQp5jLg71mhPoOn1eCRDB3yLUo','2022-12-10 07:29:02'),('user3@gmail.com','Iltc83wQ1G3vZ1M9fOVyxQbuXGOYujfc','2022-12-10 07:29:13'),('user4@gmail.com','0srJwFoNSfkLXQDSZwjgbMm5rWNvCBwG','2022-12-10 07:32:58'),('user4@gmail.com','C99TbUX8y6nSpOt0Blhqlik9kzQ0AWoU','2022-12-10 07:33:26'),('user4@gmail.com','CFAvmEiM39kuHdg0oaUJpBaLHCuHwDL5','2022-12-10 07:33:31'),('user4@gmail.com','E22dSDIyXcoF5yCyg4EY2mAuyBJYpqbk','2022-12-10 07:33:38'),('user4@gmail.com','Iltc83wQ1G3vZ1M9fOVyxQbuXGOYujfc','2022-12-10 07:35:22'),('user4@gmail.com','lWAVtjvNig0VsIGL50cn9qM8C0Qz57Lf','2022-12-10 07:35:33'),('user4@gmail.com','nBT2OvJ9Hp8EnhIaPELrHQe25MRREWKu','2022-12-10 07:35:28'),('user4@gmail.com','YjfFuxxlQFwFZohMRIYAhKlXMp9FX95R','2022-12-10 07:35:40'),('user5@gmail.com','0srJwFoNSfkLXQDSZwjgbMm5rWNvCBwG','2022-12-10 07:38:39'),('user5@gmail.com','awRpmasPDO9OMQicLzOG5WtEHdYjd9oS','2022-12-10 07:38:47'),('user5@gmail.com','Bbo9qekAE4oZe3JkizoNFuc6PpkGgNCb','2022-12-10 07:38:54'),('user5@gmail.com','GN1UvMAQp5jLg71mhPoOn1eCRDB3yLUo','2022-12-10 07:39:38'),('user5@gmail.com','hDl4vbcUDsRTYF2Wbmu6fteYV4X2tJk8','2022-12-10 07:39:48'),('user5@gmail.com','IQuqFi5xNn8Zc7gP5uVkcBxRabtmPrAI','2022-12-10 07:40:11'),('user5@gmail.com','LG1DFaqLnisjIlTIcP7uzZuktsMfNnIT','2022-12-10 07:40:28'),('user5@gmail.com','nmC9NYdlWCY0jWqqp9tzvbpXgkF5i6Jg','2022-12-10 07:40:35'),('user5@gmail.com','V9RX4R5i0Urc3NreZsdIzHtL8OTZcweT','2022-12-10 07:40:44'),('user5@gmail.com','YjfFuxxlQFwFZohMRIYAhKlXMp9FX95R','2022-12-10 07:41:00'),('user6@gmail.com','0srJwFoNSfkLXQDSZwjgbMm5rWNvCBwG','2022-12-10 07:43:13'),('user6@gmail.com','9isggOYC9KfbfVg26O6vnzLLRojjLMag','2022-12-10 07:43:25'),('user6@gmail.com','A5OhsZKVMvkr07QIE2OTd2FrZQXm91lW','2022-12-10 07:43:34'),('user6@gmail.com','awRpmasPDO9OMQicLzOG5WtEHdYjd9oS','2022-12-10 07:44:02'),('user6@gmail.com','E22dSDIyXcoF5yCyg4EY2mAuyBJYpqbk','2022-12-10 07:44:13'),('user6@gmail.com','GN1UvMAQp5jLg71mhPoOn1eCRDB3yLUo','2022-12-10 07:44:21'),('user6@gmail.com','haqIE7tK1O66CAKoKj0ZcfCfuRAqqofW','2022-12-10 07:44:27'),('user6@gmail.com','hDl4vbcUDsRTYF2Wbmu6fteYV4X2tJk8','2022-12-10 07:44:33'),('user6@gmail.com','Iltc83wQ1G3vZ1M9fOVyxQbuXGOYujfc','2022-12-10 07:44:42'),('user6@gmail.com','IQuqFi5xNn8Zc7gP5uVkcBxRabtmPrAI','2022-12-10 07:44:47'),('user6@gmail.com','jf8qzdZCZukQ2EU12wNaH4oYG4iCz8EI','2022-12-10 07:44:52'),('user6@gmail.com','jOlGyREOq55NJzYl2Xz6UNkgoyfvd4R3','2022-12-10 07:45:00'),('user6@gmail.com','JQcQR4AqGqAHW0fPlRlqv8ba4bl1oqw9','2022-12-10 07:45:06'),('user6@gmail.com','lWAVtjvNig0VsIGL50cn9qM8C0Qz57Lf','2022-12-10 07:45:17'),('user6@gmail.com','nBT2OvJ9Hp8EnhIaPELrHQe25MRREWKu','2022-12-10 07:45:11'),('user6@gmail.com','sXHhC43n4TvH0ioRLrnunSUTgFDtqGpm','2022-12-10 07:45:23'),('user6@gmail.com','u2AbGw39r3r1MzpT8zD5nZdGFnUqyUfk','2022-12-10 07:45:27'),('user7@gmail.com','0srJwFoNSfkLXQDSZwjgbMm5rWNvCBwG','2022-12-10 07:47:12'),('user7@gmail.com','9isggOYC9KfbfVg26O6vnzLLRojjLMag','2022-12-10 07:47:41'),('user7@gmail.com','awRpmasPDO9OMQicLzOG5WtEHdYjd9oS','2022-12-10 07:47:58'),('user7@gmail.com','C99TbUX8y6nSpOt0Blhqlik9kzQ0AWoU','2022-12-10 07:48:09'),('user7@gmail.com','CFAvmEiM39kuHdg0oaUJpBaLHCuHwDL5','2022-12-10 07:48:04'),('user7@gmail.com','Iltc83wQ1G3vZ1M9fOVyxQbuXGOYujfc','2022-12-10 07:48:28'),('user7@gmail.com','J7UR63BwwH1LIN36LGbsjqKJwJx7WnyX','2022-12-10 07:48:34'),('user7@gmail.com','jf8qzdZCZukQ2EU12wNaH4oYG4iCz8EI','2022-12-10 07:48:38'),('user7@gmail.com','jOlGyREOq55NJzYl2Xz6UNkgoyfvd4R3','2022-12-10 07:48:46'),('user7@gmail.com','JQcQR4AqGqAHW0fPlRlqv8ba4bl1oqw9','2022-12-10 07:48:52'),('user7@gmail.com','JzPbbUOUCRKtMCYQ4dPsW6dWz6oo5OnV','2022-12-10 07:48:57'),('user7@gmail.com','lWAVtjvNig0VsIGL50cn9qM8C0Qz57Lf','2022-12-10 07:49:03'),('user7@gmail.com','nBT2OvJ9Hp8EnhIaPELrHQe25MRREWKu','2022-12-10 07:49:08'),('user7@gmail.com','nhy1AWUUceNFTvPfVCjEXNAd7lI02FPH','2022-12-10 07:49:14'),('user7@gmail.com','sVfRfl0ksXYAFFNqE5xA17ymNxSfzyg3','2022-12-10 07:49:20'),('user7@gmail.com','sXHhC43n4TvH0ioRLrnunSUTgFDtqGpm','2022-12-10 07:49:24'),('user7@gmail.com','u2AbGw39r3r1MzpT8zD5nZdGFnUqyUfk','2022-12-10 07:49:28'),('user7@gmail.com','YjfFuxxlQFwFZohMRIYAhKlXMp9FX95R','2022-12-10 07:49:35'),('user8@gmail.com','0srJwFoNSfkLXQDSZwjgbMm5rWNvCBwG','2022-12-10 07:52:07'),('user8@gmail.com','9isggOYC9KfbfVg26O6vnzLLRojjLMag','2022-12-10 07:51:57'),('user8@gmail.com','awRpmasPDO9OMQicLzOG5WtEHdYjd9oS','2022-12-10 07:52:30'),('user8@gmail.com','CFAvmEiM39kuHdg0oaUJpBaLHCuHwDL5','2022-12-10 07:52:37'),('user8@gmail.com','E22dSDIyXcoF5yCyg4EY2mAuyBJYpqbk','2022-12-10 07:52:43'),('user8@gmail.com','haqIE7tK1O66CAKoKj0ZcfCfuRAqqofW','2022-12-10 07:58:08'),('user8@gmail.com','hDl4vbcUDsRTYF2Wbmu6fteYV4X2tJk8','2022-12-10 07:58:25'),('user8@gmail.com','Iltc83wQ1G3vZ1M9fOVyxQbuXGOYujfc','2022-12-10 07:53:01'),('user8@gmail.com','J7UR63BwwH1LIN36LGbsjqKJwJx7WnyX','2022-12-10 07:53:13'),('user8@gmail.com','jf8qzdZCZukQ2EU12wNaH4oYG4iCz8EI','2022-12-10 07:53:17'),('user8@gmail.com','JzPbbUOUCRKtMCYQ4dPsW6dWz6oo5OnV','2022-12-10 07:53:23'),('user8@gmail.com','nhy1AWUUceNFTvPfVCjEXNAd7lI02FPH','2022-12-10 07:53:36'),('user8@gmail.com','sVfRfl0ksXYAFFNqE5xA17ymNxSfzyg3','2022-12-10 07:58:13'),('user8@gmail.com','sXHhC43n4TvH0ioRLrnunSUTgFDtqGpm','2022-12-10 07:53:43'),('user8@gmail.com','u2AbGw39r3r1MzpT8zD5nZdGFnUqyUfk','2022-12-10 07:58:52'),('user8@gmail.com','YjfFuxxlQFwFZohMRIYAhKlXMp9FX95R','2022-12-10 07:53:55'),('user9@gmail.com','0srJwFoNSfkLXQDSZwjgbMm5rWNvCBwG','2022-12-10 07:56:52'),('user9@gmail.com','YjfFuxxlQFwFZohMRIYAhKlXMp9FX95R','2022-12-10 07:56:43');
/*!40000 ALTER TABLE `watch` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2022-12-10 23:49:04
