-- MySQL dump 10.13  Distrib 8.0.42, for Win64 (x86_64)
--
-- Host: localhost    Database: socialnetworkdb
-- ------------------------------------------------------
-- Server version	9.1.0

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
-- Table structure for table `accounts`
--

DROP TABLE IF EXISTS `accounts`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `accounts` (
  `id` int NOT NULL AUTO_INCREMENT,
  `createdAt` date DEFAULT NULL,
  `email` varchar(255) NOT NULL,
  `is_active` bit(1) DEFAULT NULL,
  `is_verified` bit(1) DEFAULT NULL,
  `must_change_password` bit(1) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `password_expires_at` datetime(6) DEFAULT NULL,
  `role` enum('ADMIN','ALUMNI','LECTURER') DEFAULT NULL,
  `user_id` int NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKn7ihswpy07ci568w34q0oi8he` (`email`),
  UNIQUE KEY `UKe4w4av1wrhanry7t6mxt42nou` (`user_id`),
  CONSTRAINT `FKf5vyi6yktkuwkfot8hctl2e75` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=26 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `accounts`
--

LOCK TABLES `accounts` WRITE;
/*!40000 ALTER TABLE `accounts` DISABLE KEYS */;
INSERT INTO `accounts` VALUES (1,'2025-07-17','2251052131truong@ou.edu.vn',_binary '',_binary '',_binary '\0','$2a$10$5X9k5N1sTc1/CjVH5XJoje3QMYijH3ETpgkox00R0MdPaJPPrf7wO',NULL,'ADMIN',1),(2,'2025-07-17','tranquangtruong21@gmail.com',_binary '',_binary '',_binary '\0','$2a$10$KbMi7U4WFvtdHar0A6H3nunThh9i4ZD5gJseG9LLDXsNf4FUjFni6',NULL,'ALUMNI',3),(4,'2025-07-17','truongtranquang91@gmail.com',_binary '',_binary '',_binary '\0','$2a$10$YYdnOVpf3gCVIA21N0/JCek.IfwU5qBUwSjs.aBQnZ8bcbeAGAkwG',NULL,'LECTURER',2),(5,'2025-07-17','tqt25@gmail.com',_binary '',_binary '',_binary '\0','$2a$10$UkWscu2fBG3SB1SLJJFduup3hZImInmSmnsgu1p/YABmaMVyWAnO2',NULL,'ALUMNI',4),(6,'2025-07-19','tqt125@gmail.com',_binary '',_binary '',_binary '\0','$2a$10$OU2mQ.84C7jHBLBh/L6vl.oYu/IoGioZhVIyIofVJOTlEb4NhKjhm',NULL,'ALUMNI',5),(7,'2025-07-28','tqt1@gmail.com',_binary '\0',_binary '\0',_binary '\0','$2a$10$8h5Xm1fkJpsHK2.fE0LzB.Xus7gWly9pgcH/xEjMDi8oKe1prpCeS',NULL,'ALUMNI',6),(8,'2025-07-28','tranquangtruong25@gmail.com',_binary '',_binary '',_binary '\0','$2a$10$kkKnzw6F0qVRy0wx9EB47elCH2LFJErSE59YGdnSvenRrFRtSM.XO',NULL,'ALUMNI',9),(11,'2025-07-28','tqt12@gmail.com',_binary '\0',_binary '\0',_binary '\0','$2a$10$3uUTZqnl6ZE.Z3X0wIC1sOlMhpydaP5ZbJcZHiFd0mE6HL.fQgRr2',NULL,'ALUMNI',12),(14,'2025-07-28','tqt01@gmail.com',_binary '\0',_binary '\0',_binary '\0','$2a$10$tLGRDFJyVbARjzRLb0U9j.K3meN0vYgRlB6EnsIOxhsPODhZa1Aa6',NULL,'ALUMNI',25),(15,'2025-07-28','tqt010@gmail.com',_binary '',_binary '',_binary '\0','$2a$10$8JzTf3wDOVssdoua7xrD5OQ3lOW/yKLq8BxPMdABAILnuyZYc3wI6',NULL,'ALUMNI',26),(16,'2025-07-28','tqt010s@gmail.com',_binary '\0',_binary '\0',_binary '\0','$2a$10$PRovgnxXnP5DOVXwtpVbleYzmsy5XKS6vBhgKVRdIlC1IVdnAs5fa',NULL,'ALUMNI',27),(17,'2025-07-28','tqt010s',_binary '\0',_binary '\0',_binary '\0','$2a$10$Wy13/IIhJRlTExT1hRHxkexMExsQGDYr8YsShYcNo/SX4rhYM6kx.',NULL,'ALUMNI',28),(18,'2025-07-28','tqt0101@gmail.com',_binary '\0',_binary '\0',_binary '\0','$2a$10$OpakcyaE0N6CYA9l0t7b4.DuD4NxZQHfQ3ygBm9GXwgNI4xi44buq',NULL,'ALUMNI',29),(19,'2025-08-06','minh@gmail.com',_binary '\0',_binary '\0',_binary '\0','$2a$10$.8uVpd/sVrSlRJGcgp5xFeiaXnbdzDFnHWiRNUapyOv62N4qy5mv2',NULL,'ALUMNI',33),(21,'2025-08-07','tranquangtruong555@gmail.com',_binary '',_binary '',_binary '\0','$2a$10$VTTVO6qzdARPbmENpIQUwOARrf4GQu6Xe0zD1mtwCOW97pM7Wdjlq',NULL,'ALUMNI',39),(22,'2025-08-07','khoa@gmail.com',_binary '',_binary '',_binary '\0','$2a$10$V63K.JKqnIm4ct/StsuaWO1Iiy22U1fkV3qlq5iDiVRIYnyr/kPHy',NULL,'ALUMNI',35),(23,'2025-08-07','tran5@gmail.com',_binary '',_binary '',_binary '\0','$2a$10$HPuoWVwhFsG5pX9tBul2deGVto5jg9RMrrtbb2Ewr.2Cy1UAD0L0K',NULL,'ALUMNI',36),(24,NULL,'giaovien1@example.com',_binary '',_binary '',_binary '','$2a$10$Gxi/Y3ZJ7mi0gVKxDJurH.DpvLmliBXpYZFYIlrTO8eLwtzvZDe5e','2025-08-10 18:43:44.365743','LECTURER',38);
/*!40000 ALTER TABLE `accounts` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `admin_profile`
--

DROP TABLE IF EXISTS `admin_profile`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `admin_profile` (
  `id` int NOT NULL AUTO_INCREMENT,
  `cover_image` varchar(255) DEFAULT NULL,
  `created_at` date DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `position` varchar(255) DEFAULT NULL,
  `user_id` int NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK15wdhbf9a3mi08tuunu8h08ey` (`user_id`),
  CONSTRAINT `FKouqcwh7ttk8ou894a87batkdg` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `admin_profile`
--

LOCK TABLES `admin_profile` WRITE;
/*!40000 ALTER TABLE `admin_profile` DISABLE KEYS */;
INSERT INTO `admin_profile` VALUES (1,NULL,'2025-07-29','Quản trị viên đăng thông báo','Quản trị viên',1);
/*!40000 ALTER TABLE `admin_profile` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `alumni_profile`
--

DROP TABLE IF EXISTS `alumni_profile`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `alumni_profile` (
  `id` int NOT NULL AUTO_INCREMENT,
  `company` varchar(255) DEFAULT NULL,
  `cover_image` varchar(255) DEFAULT NULL,
  `current_job` varchar(255) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `faculty` varchar(255) DEFAULT NULL,
  `graduation_year` int DEFAULT NULL,
  `major` varchar(255) DEFAULT NULL,
  `user_id` int NOT NULL,
  `created_at` date DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKopgxmgk1eyvfjxfqh4wkugd9a` (`user_id`),
  CONSTRAINT `FKq29p0kk1karar4lw82yxd9wfs` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `alumni_profile`
--

LOCK TABLES `alumni_profile` WRITE;
/*!40000 ALTER TABLE `alumni_profile` DISABLE KEYS */;
INSERT INTO `alumni_profile` VALUES (1,'Github','https://res.cloudinary.com/dabb0yavq/image/upload/v1753452225/dzn58mhjl8s62hnfxwod.png','Software Engineer','I am working on backend systems','Information Technology',2020,'Computer Science',3,'2025-07-25'),(3,'TMA','https://res.cloudinary.com/dabb0yavq/image/upload/v1753771373/k6qgp3kc28okbse8gtyq.jpg','Backend Developer','Tôi là trường, tốt nghiệp đại học Mở TPHCM k19','Công nghệ thông tin',2023,'Công nghệ thông tin',4,'2025-07-29'),(4,'Open University','https://res.cloudinary.com/dabb0yavq/image/upload/v1754051751/sceorgxt0whh1eulfdvj.jpg','Frontend Developer','Tôi là trường, tốt nghiệp đại học Mở TPHCM k22','Công nghệ thông tin',2022,'Công nghệ thông tin',9,'2025-08-01'),(5,'Google','https://res.cloudinary.com/dabb0yavq/image/upload/v1754061350/dcixxn75yzda1j2njvo4.jpg','Frontend Developer','Tôi là trường, tốt nghiệp đại học Bách khoa k12','Công nghệ thông tin',2022,'Công nghệ thông tin',26,'2025-08-01'),(6,'TMA','https://res.cloudinary.com/dabb0yavq/image/upload/v1754662689/x8xnu68tv4icp2beg8ry.jpg','DevOps','Tốt nghiệp k23 đại học bách khoa','CNTT',2023,'KHoa học máy tính',35,'2025-08-08');
/*!40000 ALTER TABLE `alumni_profile` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `comment`
--

DROP TABLE IF EXISTS `comment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `comment` (
  `id` int NOT NULL AUTO_INCREMENT,
  `content` varchar(1000) NOT NULL,
  `created_date` datetime(6) NOT NULL,
  `is_deleted` bit(1) DEFAULT NULL,
  `updated_date` datetime(6) DEFAULT NULL,
  `parent_comment_id` int DEFAULT NULL,
  `post_id` int NOT NULL,
  `user_id` int NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKhvh0e2ybgg16bpu229a5teje7` (`parent_comment_id`),
  KEY `FK7jok1s6lywoh0srylq8lt7tmn` (`post_id`),
  KEY `FK8kcum44fvpupyw6f5baccx25c` (`user_id`),
  CONSTRAINT `FK7jok1s6lywoh0srylq8lt7tmn` FOREIGN KEY (`post_id`) REFERENCES `posts` (`id`),
  CONSTRAINT `FK8kcum44fvpupyw6f5baccx25c` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`),
  CONSTRAINT `FKhvh0e2ybgg16bpu229a5teje7` FOREIGN KEY (`parent_comment_id`) REFERENCES `comment` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `comment`
--

LOCK TABLES `comment` WRITE;
/*!40000 ALTER TABLE `comment` DISABLE KEYS */;
INSERT INTO `comment` VALUES (1,'bài viết hay','2025-07-26 22:38:54.954652',_binary '\0',NULL,NULL,3,3),(2,'Nội dung mới','2025-07-26 23:57:03.935183',_binary '\0','2025-07-27 21:09:31.105370',NULL,3,3),(3,'bài viết tốt','2025-07-27 00:02:02.183943',_binary '','2025-07-27 21:09:43.347672',NULL,3,3),(4,'Bài viet hay','2025-08-02 20:47:42.675151',_binary '\0',NULL,NULL,3,9),(5,'Hay quá','2025-08-02 20:49:56.339961',_binary '\0',NULL,NULL,3,9),(6,'Lại 1 năm học trôi qua nữa','2025-08-02 20:53:21.003716',_binary '\0','2025-08-03 14:57:38.312392',NULL,46,9),(7,'Haha','2025-08-02 21:46:58.619597',_binary '\0',NULL,NULL,3,9),(8,'Chúc mừng nhá','2025-08-03 15:03:31.630683',_binary '\0',NULL,NULL,13,9),(9,'hehhehehehe\n','2025-08-05 19:06:50.396295',_binary '','2025-08-05 19:12:08.814073',NULL,3,9),(10,'wowowowowhahahaha','2025-08-06 19:37:45.239260',_binary '','2025-08-06 19:50:09.789767',NULL,18,9);
/*!40000 ALTER TABLE `comment` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `follow`
--

DROP TABLE IF EXISTS `follow`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `follow` (
  `id` int NOT NULL AUTO_INCREMENT,
  `created_at` date DEFAULT NULL,
  `follower_id` int DEFAULT NULL,
  `following_id` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKmow2qk674plvwyb4wqln37svv` (`follower_id`),
  KEY `FKqme6uru2g9wx9iysttk542esm` (`following_id`),
  CONSTRAINT `FKmow2qk674plvwyb4wqln37svv` FOREIGN KEY (`follower_id`) REFERENCES `user` (`id`),
  CONSTRAINT `FKqme6uru2g9wx9iysttk542esm` FOREIGN KEY (`following_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=18 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `follow`
--

LOCK TABLES `follow` WRITE;
/*!40000 ALTER TABLE `follow` DISABLE KEYS */;
INSERT INTO `follow` VALUES (1,'2025-08-04',2,3),(12,'2025-08-04',9,2),(13,'2025-08-05',2,9),(14,'2025-08-06',9,3),(15,'2025-08-06',9,1),(16,'2025-08-06',3,1),(17,'2025-08-09',1,9);
/*!40000 ALTER TABLE `follow` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `group_members`
--

DROP TABLE IF EXISTS `group_members`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `group_members` (
  `group_id` int NOT NULL,
  `student_id` int NOT NULL,
  KEY `FKeph0m18nue9ixvgns65stblnc` (`student_id`),
  KEY `FK33mqgp5o7e9x26kshybt27ref` (`group_id`),
  CONSTRAINT `FK33mqgp5o7e9x26kshybt27ref` FOREIGN KEY (`group_id`) REFERENCES `group_user` (`id`),
  CONSTRAINT `FKeph0m18nue9ixvgns65stblnc` FOREIGN KEY (`student_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `group_members`
--

LOCK TABLES `group_members` WRITE;
/*!40000 ALTER TABLE `group_members` DISABLE KEYS */;
INSERT INTO `group_members` VALUES (1,1),(1,2),(1,3),(1,4),(1,5),(1,6),(2,38),(2,39),(3,33),(3,40);
/*!40000 ALTER TABLE `group_members` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `group_user`
--

DROP TABLE IF EXISTS `group_user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `group_user` (
  `id` int NOT NULL AUTO_INCREMENT,
  `created_date` date NOT NULL,
  `name` varchar(100) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKpwuvlsyx5795oratdw9nqbcdr` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `group_user`
--

LOCK TABLES `group_user` WRITE;
/*!40000 ALTER TABLE `group_user` DISABLE KEYS */;
INSERT INTO `group_user` VALUES (1,'2025-07-31','K22'),(2,'2025-08-07','K21'),(3,'2025-08-07','K20');
/*!40000 ALTER TABLE `group_user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `lecturer_profile`
--

DROP TABLE IF EXISTS `lecturer_profile`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `lecturer_profile` (
  `id` int NOT NULL AUTO_INCREMENT,
  `cover_image` varchar(255) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `position` varchar(255) DEFAULT NULL,
  `degree` varchar(255) DEFAULT NULL,
  `faculty` varchar(255) DEFAULT NULL,
  `specialization` varchar(255) DEFAULT NULL,
  `user_id` int NOT NULL,
  `created_at` date DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK4es4s43snfkh5olm4lw5bw2sr` (`user_id`),
  CONSTRAINT `FKrs657i5hwnx3rreccdl4c78yc` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `lecturer_profile`
--

LOCK TABLES `lecturer_profile` WRITE;
/*!40000 ALTER TABLE `lecturer_profile` DISABLE KEYS */;
INSERT INTO `lecturer_profile` VALUES (1,'https://res.cloudinary.com/dabb0yavq/image/upload/v1753772059/jf7nuo9ou7aezjgsp6dt.jpg','Giảng viên Đại học Mở TP.HCM','Giảng viên','Tiến sĩ','Công nghệ thông tin','Kiểm thử',2,'2025-07-29'),(2,'https://res.cloudinary.com/dabb0yavq/image/upload/v1754662772/qsfugcyjjqjiiwnaufhc.jpg','Giảng viên Đại học Mở TP.HCM','Giảng viên','Tiến sĩ','Công nghệ thông tin','Kiểm thử',38,'2025-08-08');
/*!40000 ALTER TABLE `lecturer_profile` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `notification`
--

DROP TABLE IF EXISTS `notification`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `notification` (
  `id` int NOT NULL AUTO_INCREMENT,
  `created_at` date DEFAULT NULL,
  `message` varchar(255) DEFAULT NULL,
  `is_read` bit(1) NOT NULL,
  `from_user_id` int NOT NULL,
  `to_user_id` int NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKqq8sa8nn030wp2n9chd9mw2kh` (`from_user_id`),
  KEY `FK1u6860t77y9lea8nl3jq6yyjw` (`to_user_id`),
  CONSTRAINT `FK1u6860t77y9lea8nl3jq6yyjw` FOREIGN KEY (`to_user_id`) REFERENCES `user` (`id`),
  CONSTRAINT `FKqq8sa8nn030wp2n9chd9mw2kh` FOREIGN KEY (`from_user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=18 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `notification`
--

LOCK TABLES `notification` WRITE;
/*!40000 ALTER TABLE `notification` DISABLE KEYS */;
INSERT INTO `notification` VALUES (1,'2025-08-04','Trần Quốc Long đã theo dõi bạn.',_binary '',2,3),(2,'2025-08-04','Trần Trường đã theo dõi bạn.',_binary '',9,9),(3,'2025-08-04','Trần Trường đã theo dõi bạn.',_binary '\0',9,2),(4,'2025-08-04','Trần Trường đã theo dõi bạn.',_binary '\0',9,1),(5,'2025-08-04','Trần Trường đã theo dõi bạn.',_binary '',9,9),(6,'2025-08-04','Trần Trường đã theo dõi bạn.',_binary '',9,9),(7,'2025-08-04','Trần Trường đã theo dõi bạn.',_binary '',9,9),(8,'2025-08-04','Trần Trường đã theo dõi bạn.',_binary '\0',9,1),(9,'2025-08-04','Trần Trường đã theo dõi bạn.',_binary '\0',9,1),(10,'2025-08-04','Trần Trường đã theo dõi bạn.',_binary '\0',9,1),(11,'2025-08-04','Trần Trường đã theo dõi bạn.',_binary '\0',9,1),(12,'2025-08-04','Trần Trường đã theo dõi bạn.',_binary '\0',9,2),(13,'2025-08-05','Trần Quốc Long đã theo dõi bạn.',_binary '',2,9),(14,'2025-08-06','Trần Trường đã theo dõi bạn.',_binary '\0',9,3),(15,'2025-08-06','Trần Trường đã theo dõi bạn.',_binary '\0',9,1),(16,'2025-08-06','Trần Khang đã theo dõi bạn.',_binary '\0',3,1),(17,'2025-08-09','Trần Trường đã theo dõi bạn.',_binary '\0',1,9);
/*!40000 ALTER TABLE `notification` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `post_recipient`
--

DROP TABLE IF EXISTS `post_recipient`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `post_recipient` (
  `id` int NOT NULL AUTO_INCREMENT,
  `is_to_all` bit(1) DEFAULT NULL,
  `account_id` int DEFAULT NULL,
  `group_id` int DEFAULT NULL,
  `post_id` int NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKf0sob20yja73a3toqedykvrf9` (`account_id`),
  KEY `FK645qtaht9akb3je2ywnafrgp1` (`group_id`),
  KEY `FK3oeio7j2gssi6jp9m83woj1k8` (`post_id`),
  CONSTRAINT `FK3oeio7j2gssi6jp9m83woj1k8` FOREIGN KEY (`post_id`) REFERENCES `posts` (`id`),
  CONSTRAINT `FK645qtaht9akb3je2ywnafrgp1` FOREIGN KEY (`group_id`) REFERENCES `group_user` (`id`),
  CONSTRAINT `FKf0sob20yja73a3toqedykvrf9` FOREIGN KEY (`account_id`) REFERENCES `accounts` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=48 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `post_recipient`
--

LOCK TABLES `post_recipient` WRITE;
/*!40000 ALTER TABLE `post_recipient` DISABLE KEYS */;
INSERT INTO `post_recipient` VALUES (1,_binary '\0',NULL,NULL,9),(2,_binary '\0',NULL,NULL,9),(3,_binary '\0',NULL,NULL,9),(5,_binary '\0',2,NULL,11),(6,_binary '\0',4,NULL,11),(7,_binary '\0',8,NULL,11),(8,_binary '\0',2,NULL,12),(9,_binary '\0',4,NULL,12),(10,_binary '\0',8,NULL,12),(41,_binary '\0',8,NULL,46),(42,_binary '\0',4,NULL,46),(43,_binary '\0',NULL,1,50),(44,_binary '\0',NULL,2,50),(45,_binary '\0',NULL,2,52),(46,_binary '\0',NULL,1,53),(47,_binary '\0',NULL,1,55);
/*!40000 ALTER TABLE `post_recipient` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `posts`
--

DROP TABLE IF EXISTS `posts`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `posts` (
  `id` int NOT NULL AUTO_INCREMENT,
  `content` varchar(255) DEFAULT NULL,
  `createdAt` date DEFAULT NULL,
  `image` varchar(255) DEFAULT NULL,
  `is_locked` bit(1) DEFAULT NULL,
  `post_type` enum('INVITATION','POST','SURVEY') DEFAULT NULL,
  `user_id` int NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKam8ar6luvp8afhfu20gfsydo9` (`user_id`),
  CONSTRAINT `FKam8ar6luvp8afhfu20gfsydo9` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=56 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `posts`
--

LOCK TABLES `posts` WRITE;
/*!40000 ALTER TABLE `posts` DISABLE KEYS */;
INSERT INTO `posts` VALUES (2,'bài viết thứ 2 .....','2025-07-18','https://res.cloudinary.com/dabb0yavq/image/upload/v1752832013/rwrqvlslrin11ekioolu.jpg',_binary '\0','POST',3),(3,'Bài viết mẫu thứ nhất','2025-07-18','https://res.cloudinary.com/dabb0yavq/image/upload/v1752832537/d0wocfqbgnx3mk7p7fef.jpg',_binary '\0','POST',2),(4,'Bài viết Test Đầu tiên','2025-07-25','https://res.cloudinary.com/dabb0yavq/image/upload/v1753455224/owealz5zcxroulbhautc.webp',_binary '\0','POST',3),(5,'Bài khảo sát mẫu','2025-07-28','https://res.cloudinary.com/dabb0yavq/image/upload/v1753682826/bl7bjyfavibaxug9g0xk.jpg',_binary '\0','POST',3),(6,'Bài khảo sát mẫu','2025-07-28','https://res.cloudinary.com/dabb0yavq/image/upload/v1753715042/pyr4pfotou92jn5tyqup.jpg',_binary '\0','SURVEY',1),(7,'Bài khảo sát mẫu','2025-07-28','https://res.cloudinary.com/dabb0yavq/image/upload/v1753683021/z4f0w0jnzbhifqwic0hw.jpg',_binary '\0','SURVEY',1),(8,'Bài viết mới','2025-07-29','https://res.cloudinary.com/dabb0yavq/image/upload/v1753782230/b5oundcqojkfacvvlako.webp',_binary '\0','POST',1),(9,'Bài khảo sát mẫu','2025-08-01',NULL,_binary '\0','INVITATION',1),(11,'Thư mời dự lễ tốt nghiệp','2025-08-01',NULL,_binary '\0','INVITATION',1),(12,'Thư mời dự lễ chào mừng năm học mới','2025-08-01',NULL,_binary '\0','INVITATION',1),(13,'Ăn mừng chiến thắng hội thao năm nay hahahha','2025-08-02','https://res.cloudinary.com/dabb0yavq/image/upload/v1754113648/vmltxxmuxhpdsbf1qgvr.jpg',_binary '\0','POST',9),(15,'Nhớ tô phở gần trường','2025-08-02','https://res.cloudinary.com/dabb0yavq/image/upload/v1754114180/easy4pq1ckomuoqutyqb.webp',_binary '','POST',9),(16,'Banh mi banh mi','2025-08-02','https://res.cloudinary.com/dabb0yavq/image/upload/v1754114299/zy7s3dewbv50tnwg1ocd.jpg',_binary '\0','POST',9),(17,'Xin chào mọi người','2025-08-02',NULL,_binary '\0','POST',9),(18,'Công ty của tôi','2025-08-02','https://res.cloudinary.com/dabb0yavq/image/upload/v1754115288/bwkcehmnrhyjjan8mvjr.jpg',_binary '\0','POST',9),(19,'Test mẫu','2025-08-02','https://res.cloudinary.com/dabb0yavq/image/upload/v1754115663/yd0f2qzfflqc6m3xylar.jpg',_binary '\0','POST',9),(20,'Bài Test Mẫu','2025-08-02',NULL,_binary '\0','POST',9),(21,'Mỗi ngày là một cơ hội để bắt đầu lại. Hôm nay mình chọn sống chậm lại và tận hưởng khoảnh khắc nhỏ nhất ? #SimpleLife #PeacefulMind','2025-08-01',NULL,_binary '\0','POST',9),(22,'Test bài viết','2025-08-02','https://res.cloudinary.com/dabb0yavq/image/upload/v1754407768/xjxd61nhxfqo0bc5t400.jpg',_binary '\0','POST',9),(23,'Khảo sát mẫu','2025-08-02','https://res.cloudinary.com/dabb0yavq/image/upload/v1754118372/lnvjtwpz4kvy3wl9utq0.jpg',_binary '\0','SURVEY',1),(24,'Khảo sát phở','2025-08-02','https://res.cloudinary.com/dabb0yavq/image/upload/v1754118562/a9iyboukmjvrfwuxeg4w.webp',_binary '\0','SURVEY',1),(46,'Thư mời đến dự lễ tuyên dương học sinh xuất sắc\r\nTại Trường chi nhánh 1\r\nKính mong các bạn đến tham dự','2025-08-02','https://res.cloudinary.com/dabb0yavq/image/upload/v1754137042/m4qyph0pinypcxpbnx0k.jpg',_binary '\0','INVITATION',1),(50,'Nội dung gửi đến các bạn cựu sinh viên K21, K22 đến dự lễ tri ân cựu sinh viên diễn ra tại Trường chi nhánh 1','2025-08-08',NULL,_binary '\0','INVITATION',1),(51,'Bài viết khảo sát','2025-08-09','https://res.cloudinary.com/dabb0yavq/image/upload/v1754709668/q5ydzejbnfwawewzvhgt.jpg',_binary '\0','SURVEY',1),(52,'THư mời','2025-08-09','https://res.cloudinary.com/dabb0yavq/image/upload/v1754709844/wutapiql9gz42fvo3s14.jpg',_binary '\0','INVITATION',1),(53,'thư mời k22','2025-08-09',NULL,_binary '\0','INVITATION',1),(54,'khao sat','2025-08-09',NULL,_binary '\0','SURVEY',1),(55,'Thư mời test','2025-08-09',NULL,_binary '\0','INVITATION',1);
/*!40000 ALTER TABLE `posts` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `reaction`
--

DROP TABLE IF EXISTS `reaction`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `reaction` (
  `id` int NOT NULL AUTO_INCREMENT,
  `createdDate` date DEFAULT NULL,
  `reaction_type` enum('ANGRY','HAHA','LIKE','LOVE','NONE','SAD') NOT NULL,
  `post_id` int NOT NULL,
  `user_id` int NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK50ck737989ihkcrjbwr6vi0ta` (`post_id`),
  KEY `FKp68qgeq3telx6adl7hssrdxbw` (`user_id`),
  CONSTRAINT `FK50ck737989ihkcrjbwr6vi0ta` FOREIGN KEY (`post_id`) REFERENCES `posts` (`id`),
  CONSTRAINT `FKp68qgeq3telx6adl7hssrdxbw` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `reaction`
--

LOCK TABLES `reaction` WRITE;
/*!40000 ALTER TABLE `reaction` DISABLE KEYS */;
INSERT INTO `reaction` VALUES (1,'2025-07-27','HAHA',3,3),(2,'2025-08-03','LOVE',3,9),(3,'2025-08-03','LOVE',13,9),(4,'2025-08-03','LOVE',46,9),(5,'2025-08-03','LOVE',22,9),(6,'2025-08-03','LOVE',20,9),(7,'2025-08-03','HAHA',19,9),(8,'2025-08-03','LIKE',3,1),(9,'2025-08-03','LOVE',46,1),(10,'2025-08-03','LOVE',24,9),(11,'2025-08-06','LOVE',21,9),(12,'2025-08-08','LOVE',50,9),(13,'2025-08-09','HAHA',53,9);
/*!40000 ALTER TABLE `reaction` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `survey_options`
--

DROP TABLE IF EXISTS `survey_options`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `survey_options` (
  `id` int NOT NULL AUTO_INCREMENT,
  `optionText` varchar(255) DEFAULT NULL,
  `voteCount` int DEFAULT NULL,
  `post_id` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK83nysd2iex16t5b25y2eridnh` (`post_id`),
  CONSTRAINT `FK83nysd2iex16t5b25y2eridnh` FOREIGN KEY (`post_id`) REFERENCES `posts` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `survey_options`
--

LOCK TABLES `survey_options` WRITE;
/*!40000 ALTER TABLE `survey_options` DISABLE KEYS */;
INSERT INTO `survey_options` VALUES (1,'Bánh mì heo quay',0,6),(2,'Bánh mì chả cá',2,6),(3,'Cơm gà xối mỡ',1,7),(4,'Cơm gà chiên xù',1,7),(5,'Bánh mì chả cá',1,23),(6,'Bánh mì heo quay',0,23),(7,'Phở gà',0,24),(8,'Phở bò',1,24),(9,'Phở đặc biệt',0,24),(10,'GOogle',0,51),(11,'Apple',1,51),(12,'test 1',0,54),(13,'test 2',0,54);
/*!40000 ALTER TABLE `survey_options` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `survey_votes`
--

DROP TABLE IF EXISTS `survey_votes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `survey_votes` (
  `id` int NOT NULL AUTO_INCREMENT,
  `option_id` int DEFAULT NULL,
  `user_id` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKluo7ixf6gqdj65pcciv8nwr64` (`option_id`),
  KEY `FKebbk0d65hsn66c9rbwdj7jmyy` (`user_id`),
  CONSTRAINT `FKebbk0d65hsn66c9rbwdj7jmyy` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`),
  CONSTRAINT `FKluo7ixf6gqdj65pcciv8nwr64` FOREIGN KEY (`option_id`) REFERENCES `survey_options` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `survey_votes`
--

LOCK TABLES `survey_votes` WRITE;
/*!40000 ALTER TABLE `survey_votes` DISABLE KEYS */;
INSERT INTO `survey_votes` VALUES (1,2,1),(2,5,9),(3,8,9),(4,3,9),(5,2,9),(6,4,1),(7,11,9);
/*!40000 ALTER TABLE `survey_votes` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user` (
  `id` int NOT NULL AUTO_INCREMENT,
  `avatar` varchar(255) DEFAULT NULL,
  `created_at` date DEFAULT NULL,
  `date_of_birth` date DEFAULT NULL,
  `first_name` varchar(255) NOT NULL,
  `gender` enum('FEMALE','MALE','OTHER') DEFAULT NULL,
  `last_name` varchar(255) NOT NULL,
  `phone` varchar(11) NOT NULL,
  `user_code` varchar(20) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKlk8ogj8cf8jrqen0wof8iyora` (`user_code`)
) ENGINE=InnoDB AUTO_INCREMENT=52 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES (1,'https://res.cloudinary.com/dabb0yavq/image/upload/v1752485711/e6v3boebo9qdabizx5lv.jpg','2025-03-14','2004-12-12','Trần','MALE','Trường','0356558555','2251052222'),(2,'https://res.cloudinary.com/dabb0yavq/image/upload/v1752734539/ixx4vgxll9cwdkqj4sih.jpg','2025-06-17','2002-11-22','Trần','MALE','Quốc Long','0866770692','2251052223'),(3,'https://res.cloudinary.com/dabb0yavq/image/upload/v1752741378/yys7n2gwp1vnmnzvpkoq.jpg','2024-07-17','2000-03-03','Trần','MALE','Khang','0866770655','2251052255'),(4,'https://res.cloudinary.com/dabb0yavq/image/upload/v1752931687/l2rnikfbgbt2ogb4rn87.jpg','2025-07-19','2000-05-15','Long','MALE','Nam','0866776622','2251052111'),(5,'https://res.cloudinary.com/dabb0yavq/image/upload/v1752931919/qk3nthptknh4oukldvzt.jpg','2025-08-19','2000-05-15','Long','MALE','Hoàng','0866776622','2251052000'),(6,'https://res.cloudinary.com/dabb0yavq/image/upload/v1753702844/rw7sjzdtxpxfdquvpun6.jpg','2025-07-28','2000-05-12','Long Nhâm','MALE','Hoàng','0866776622','2251052001'),(9,'https://res.cloudinary.com/dabb0yavq/image/upload/v1754668197/lt7fq01piw1jffzeyopd.webp','2025-07-28','2000-05-12','Trần Quang','MALE','Trường','0866776622','2251052131'),(12,'https://res.cloudinary.com/dabb0yavq/image/upload/v1753704007/mm3djhms3dtyseqhu1tm.jpg','2025-07-28','2000-05-12','Long Nhâm','MALE','Hoàng','0866776622','2251052011'),(25,'https://res.cloudinary.com/dabb0yavq/image/upload/v1753712667/doniks9113p2iqqetcvv.jpg','2025-07-28','2000-05-12','Long Nhâm','MALE','Hoàng','0866776622','225105201'),(26,'https://res.cloudinary.com/dabb0yavq/image/upload/v1753712751/r3iyfrx9c5j9druqkro1.jpg','2025-07-28','2000-05-12','Long Nhâm','MALE','Hoàng','0866776622','2251052012'),(27,'https://res.cloudinary.com/dabb0yavq/image/upload/v1753713674/fxcsjshqmydxk3b63uhk.jpg','2025-07-28','2000-05-12','Long Nhâm sss','MALE','Hoàng','0866776622','2251052015'),(28,'https://res.cloudinary.com/dabb0yavq/image/upload/v1753713774/k1nix34xvssnnevexrmy.jpg','2025-07-28','2000-05-12','Long Nhâm sss','MALE','Hoàng','0866776622','2251052016'),(29,'https://res.cloudinary.com/dabb0yavq/image/upload/v1753714035/taeb70thcmct1fa0qmie.jpg','2025-07-28','2000-05-12','Long Nhâm sss','MALE','Hoàng','0866776622','2251052017'),(33,'https://res.cloudinary.com/dabb0yavq/image/upload/v1753714035/taeb70thcmct1fa0qmie.jpg','2025-08-06','2001-12-01','Trần','MALE','Minh','06546411','2051152001'),(35,'https://res.cloudinary.com/dabb0yavq/image/upload/v1754492956/y10eiuso1ktwgcvxb5vv.jpg','2025-08-06','2001-11-12','Trần','MALE','Khoa','0356558292','2251052221'),(36,'https://res.cloudinary.com/dabb0yavq/image/upload/v1754498211/c2r7jggmrngjb3spemww.jpg','2025-08-06','2001-11-11','Trần','MALE','Trường','1234567891','2251012345'),(37,'https://res.cloudinary.com/dabb0yavq/image/upload/v1754495530/e7q1wk2exoelyrgndmhn.jpg','2025-08-06','2001-11-02','','MALE','Trường Long','0356558292','2251041223'),(38,'https://res.cloudinary.com/dabb0yavq/image/upload/v1754495511/qanda4h00zpp1n3ze0rt.jpg','2025-08-06','2001-02-20','Trần','MALE','Trường Khoa','0377858292','2151041923'),(39,'https://res.cloudinary.com/dabb0yavq/image/upload/v1754496013/tu3dn8w2u2vamuolg4ua.webp','2025-08-06','2001-05-21','Hoa','MALE','Lưu Đức','0531246789','2151012345'),(40,'https://res.cloudinary.com/dabb0yavq/image/upload/v1754558323/w9dluvdci0f8o5gsm8ie.webp','2025-08-07','2001-02-09','Hoàng','MALE','Trường','0451234569','2052041223'),(51,'https://res.cloudinary.com/dabb0yavq/image/upload/v1754670773/odnkjbzmhh3wbkhnkt3h.jpg','2025-08-08','2001-11-02','Trương','MALE','HUỳnh ngọc','0546978132','1952101133');
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-08-09 19:41:20
