/*
SQLyog Community v13.3.1 (64 bit)
MySQL - 8.4.6 : Database - hospital_management_services_db
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`hospital_management_services_db` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;

USE `hospital_management_services_db`;

/*Table structure for table `appointments` */

DROP TABLE IF EXISTS `appointments`;

CREATE TABLE `appointments` (
  `id` int NOT NULL AUTO_INCREMENT,
  `code` varchar(20) NOT NULL,
  `namespace_id` int DEFAULT NULL,
  `patient_id` int NOT NULL,
  `doctor_id` int NOT NULL,
  `appointment_datetime` datetime NOT NULL,
  `token_number` int NOT NULL,
  `status` tinyint NOT NULL,
  `active_flag` tinyint NOT NULL DEFAULT '1',
  `updated_by` int NOT NULL,
  `updated_at` datetime NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uq_appointment_code` (`code`),
  UNIQUE KEY `uq_appointment_sheduletime` (`doctor_id`,`appointment_datetime`),
  KEY `fk_appointment_updated_by` (`updated_by`),
  KEY `idx_appointment_patient_active` (`patient_id`,`active_flag`),
  KEY `fk_appointments_namespace_id` (`namespace_id`),
  CONSTRAINT `fk_appointment_doctor_id` FOREIGN KEY (`doctor_id`) REFERENCES `doctors` (`id`),
  CONSTRAINT `fk_appointment_patient_id` FOREIGN KEY (`patient_id`) REFERENCES `patients` (`id`),
  CONSTRAINT `fk_appointment_updated_by` FOREIGN KEY (`updated_by`) REFERENCES `user` (`id`),
  CONSTRAINT `fk_appointments_namespace_id` FOREIGN KEY (`namespace_id`) REFERENCES `namespace` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

/*Data for the table `appointments` */

insert  into `appointments`(`id`,`code`,`namespace_id`,`patient_id`,`doctor_id`,`appointment_datetime`,`token_number`,`status`,`active_flag`,`updated_by`,`updated_at`) values 
(6,'APNMT00006',2,1,2,'2026-06-15 10:00:00',3,2,1,19,'2026-06-19 19:33:52'),
(9,'APNMT00009',2,2,2,'2026-06-15 11:30:00',2,2,1,19,'2026-06-19 20:08:35'),
(10,'APNMT00010',2,3,3,'2026-06-15 01:30:00',1,1,1,2,'2026-06-19 15:17:37');

/*Table structure for table `billing` */

DROP TABLE IF EXISTS `billing`;

CREATE TABLE `billing` (
  `id` int NOT NULL AUTO_INCREMENT,
  `code` varchar(20) NOT NULL,
  `namespace_id` int DEFAULT NULL,
  `bill_datetime` datetime DEFAULT NULL,
  `total_amount` decimal(10,2) DEFAULT NULL,
  `appointment_id` int NOT NULL,
  `paid_amount` decimal(10,2) NOT NULL DEFAULT '0.00',
  `balance_amount` decimal(10,2) NOT NULL DEFAULT '0.00',
  `billing_status` tinyint NOT NULL,
  `active_flag` tinyint DEFAULT NULL,
  `updated_by` int DEFAULT NULL,
  `updated_at` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uq_billings_code` (`code`),
  UNIQUE KEY `uq_billing_appointment` (`appointment_id`),
  KEY `fk_billing_pdated_by` (`updated_by`),
  KEY `idx_billing_namespace_date` (`namespace_id`,`bill_datetime`),
  CONSTRAINT `fk_billing_appointment_id` FOREIGN KEY (`appointment_id`) REFERENCES `appointments` (`id`),
  CONSTRAINT `fk_billing_namespace_id` FOREIGN KEY (`namespace_id`) REFERENCES `namespace` (`id`),
  CONSTRAINT `fk_billing_pdated_by` FOREIGN KEY (`updated_by`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

/*Data for the table `billing` */

/*Table structure for table `code_sequence` */

DROP TABLE IF EXISTS `code_sequence`;

CREATE TABLE `code_sequence` (
  `entity_name` varchar(30) NOT NULL,
  `next_value` int NOT NULL,
  PRIMARY KEY (`entity_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

/*Data for the table `code_sequence` */

insert  into `code_sequence`(`entity_name`,`next_value`) values 
('ADMIN',3),
('APPOINTMENT',11),
('BILLING',1),
('CONSULTATION',8),
('DEPARTMENT',8),
('DOCTOR',8),
('MEDICINE',8),
('NAMESPACE',10),
('PATIENT',5),
('PRESCRIPTION',11),
('RECEPTIONIST',1),
('USER',20);

/*Table structure for table `consultations` */

DROP TABLE IF EXISTS `consultations`;

CREATE TABLE `consultations` (
  `id` int NOT NULL AUTO_INCREMENT,
  `code` varchar(20) NOT NULL,
  `appointment_id` int NOT NULL,
  `chief_complaint` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `diagnosis` varchar(100) DEFAULT NULL,
  `doctor_notes` text,
  `consultation_datetime` datetime NOT NULL,
  `active_flag` tinyint NOT NULL DEFAULT '1',
  `updated_by` int NOT NULL,
  `updated_at` datetime NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uq_consultation_code` (`code`),
  UNIQUE KEY `uq_consultation_appointment` (`appointment_id`),
  KEY `fk_consultation_updated_by` (`updated_by`),
  CONSTRAINT `fk_consultation_appointment_id` FOREIGN KEY (`appointment_id`) REFERENCES `appointments` (`id`),
  CONSTRAINT `fk_consultation_updated_by` FOREIGN KEY (`updated_by`) REFERENCES `user` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

/*Data for the table `consultations` */

insert  into `consultations`(`id`,`code`,`appointment_id`,`chief_complaint`,`diagnosis`,`doctor_notes`,`consultation_datetime`,`active_flag`,`updated_by`,`updated_at`) values 
(5,'CONSULT00005',6,'Fever for 3 days, Headache and vomiting','Viral Infection','avoid cool drinks and ice creame','2026-06-15 10:03:00',1,19,'2026-06-19 19:33:51'),
(7,'CONSULT00007',9,'Skin redness from a weak','skin alergy','avoid over cosmetics, and eat good things','2026-06-15 11:35:00',1,19,'2026-06-20 14:49:06');

/*Table structure for table `departments` */

DROP TABLE IF EXISTS `departments`;

CREATE TABLE `departments` (
  `id` int NOT NULL AUTO_INCREMENT,
  `code` varchar(20) NOT NULL,
  `name` varchar(50) NOT NULL,
  `namespace_id` int NOT NULL,
  `active_flag` tinyint NOT NULL DEFAULT '1',
  `updated_by` int NOT NULL,
  `updated_at` datetime NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uq_department_code` (`code`),
  KEY `fk_department_namespace_id` (`namespace_id`),
  KEY `fk_department_updated_by` (`updated_by`),
  CONSTRAINT `fk_department_namespace_id` FOREIGN KEY (`namespace_id`) REFERENCES `namespace` (`id`),
  CONSTRAINT `fk_department_updated_by` FOREIGN KEY (`updated_by`) REFERENCES `user` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

/*Data for the table `departments` */

insert  into `departments`(`id`,`code`,`name`,`namespace_id`,`active_flag`,`updated_by`,`updated_at`) values 
(1,'DEPT00002','ORTHOLOGY',2,1,1,'2026-06-17 15:56:28'),
(4,'DEPT00005','CARDIOLOGY',2,1,1,'2026-06-17 15:59:02'),
(5,'DEPT00006','NEUROLOGY',2,1,1,'2026-06-17 20:01:09'),
(6,'DEPT00007','DERMOTOLOGY',2,1,1,'2026-06-17 15:57:47');

/*Table structure for table `doctors` */

DROP TABLE IF EXISTS `doctors`;

CREATE TABLE `doctors` (
  `id` int NOT NULL AUTO_INCREMENT,
  `code` varchar(20) NOT NULL,
  `name` varchar(50) NOT NULL,
  `namespace_id` int DEFAULT NULL,
  `user_id` int NOT NULL,
  `department_id` int DEFAULT NULL,
  `mobile` varchar(15) DEFAULT NULL,
  `email` varchar(100) DEFAULT NULL,
  `specialization` varchar(100) DEFAULT NULL,
  `consultation_fee` decimal(10,2) NOT NULL,
  `active_flag` tinyint NOT NULL DEFAULT '1',
  `updated_by` int NOT NULL,
  `updated_at` datetime NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uq_doctor_code` (`code`),
  UNIQUE KEY `uq_doctors_user_id` (`user_id`),
  KEY `fk_doctor_department_id` (`department_id`),
  KEY `fk_doctor_updated_by` (`updated_by`),
  KEY `idx_doctor_namespace_department` (`namespace_id`,`department_id`),
  KEY `idx_doctor_namespace_active` (`namespace_id`,`active_flag`),
  CONSTRAINT `fk_doctor_department_id` FOREIGN KEY (`department_id`) REFERENCES `departments` (`id`),
  CONSTRAINT `fk_doctor_namespace_id` FOREIGN KEY (`namespace_id`) REFERENCES `namespace` (`id`),
  CONSTRAINT `fk_doctor_updated_by` FOREIGN KEY (`updated_by`) REFERENCES `user` (`id`),
  CONSTRAINT `fk_doctors_user_id` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

/*Data for the table `doctors` */

insert  into `doctors`(`id`,`code`,`name`,`namespace_id`,`user_id`,`department_id`,`mobile`,`email`,`specialization`,`consultation_fee`,`active_flag`,`updated_by`,`updated_at`) values 
(2,'DOC00005','Prasath',2,19,4,'9864542871','prasath@gmail.com','pediatrick cardialogist',5000.20,1,2,'2026-06-15 16:09:49'),
(3,'DOC00006','Robert',2,15,6,'9762314639','robert@gmail.com','skin and hair',3500.00,1,1,'2026-06-17 18:40:59');

/*Table structure for table `medicines` */

DROP TABLE IF EXISTS `medicines`;

CREATE TABLE `medicines` (
  `id` int NOT NULL AUTO_INCREMENT,
  `code` varchar(20) NOT NULL,
  `name` varchar(50) DEFAULT NULL,
  `namespace_id` int DEFAULT NULL,
  `price` decimal(10,2) DEFAULT NULL,
  `current_stock` int NOT NULL,
  `supplier` varchar(100) DEFAULT NULL,
  `active_flag` int DEFAULT NULL,
  `updated_by` int NOT NULL,
  `updated_at` datetime NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uq_medicine_code` (`code`),
  KEY `fk_medicine_updated_by` (`updated_by`),
  KEY `idx_medicine_namespace_active` (`namespace_id`,`active_flag`),
  CONSTRAINT `fk_medicine_namespace_id` FOREIGN KEY (`namespace_id`) REFERENCES `namespace` (`id`),
  CONSTRAINT `fk_medicine_updated_by` FOREIGN KEY (`updated_by`) REFERENCES `user` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

/*Data for the table `medicines` */

insert  into `medicines`(`id`,`code`,`name`,`namespace_id`,`price`,`current_stock`,`supplier`,`active_flag`,`updated_by`,`updated_at`) values 
(1,'MED00001','Crocin',2,20.00,150,'MediCore Distributors',1,18,'2026-06-19 16:57:17'),
(2,'MED00002','Paracetomol',2,15.00,100,'MediCore Distributors',1,18,'2026-06-19 16:58:57'),
(3,'MED00003','Doxycycline',2,30.00,150,'Medic pharmacy',1,18,'2026-06-19 16:53:08'),
(4,'MED00004','Fluconozole',2,25.00,100,'Medic pharmacy',1,18,'2026-06-19 16:53:36'),
(5,'MED00005','Carvedilol',2,28.35,140,'Nexus Healthcare Supplies',1,19,'2026-06-20 13:43:27'),
(6,'MED00006','Amlodipine',2,30.00,200,'Nexus Healthcare Supplies',1,18,'2026-06-19 17:02:21'),
(7,'MED00007','Asprine',2,20.00,100,'Med plus Wholesale',1,18,'2026-06-19 16:56:02');

/*Table structure for table `namespace` */

DROP TABLE IF EXISTS `namespace`;

CREATE TABLE `namespace` (
  `id` int NOT NULL AUTO_INCREMENT,
  `code` varchar(20) NOT NULL,
  `name` varchar(50) NOT NULL,
  `address` varchar(100) NOT NULL,
  `active_flag` tinyint NOT NULL DEFAULT '1',
  `updated_by` int NOT NULL,
  `updated_at` datetime NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uq_namespace_code` (`code`),
  UNIQUE KEY `uq_namespace_name` (`name`),
  KEY `idx_namespace_id_active_flag` (`id`,`active_flag`),
  KEY `idx_namespace_code_active_flag` (`code`,`active_flag`),
  KEY `fk_namespace_updated_by` (`updated_by`),
  CONSTRAINT `fk_namespace_updated_by` FOREIGN KEY (`updated_by`) REFERENCES `user` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

/*Data for the table `namespace` */

insert  into `namespace`(`id`,`code`,`name`,`address`,`active_flag`,`updated_by`,`updated_at`) values 
(1,'HS000','HospitalManagement','nil',1,1,'2026-06-10 11:39:13'),
(2,'APO00001','apollo','cromepet, chennai',1,1,'2026-06-10 16:18:57'),
(3,'HIN00002','Hindu Mission','Tambaram',1,1,'2026-06-16 18:58:18'),
(4,'SUB00003','Subam Hospital','kodambakkam, chennai',1,1,'2026-06-11 19:46:15'),
(5,'AGA00004','Agaram Hospital','tambaram',1,1,'2026-06-12 17:49:09'),
(6,'SAK00005','Sakthi Hospital','Egmore, chennai',1,1,'2026-06-16 19:05:20'),
(7,'KAM00006','Kamarajar Hospital','Tambaram',2,1,'2026-06-17 12:22:40');

/*Table structure for table `patients` */

DROP TABLE IF EXISTS `patients`;

CREATE TABLE `patients` (
  `id` int NOT NULL AUTO_INCREMENT,
  `code` varchar(20) NOT NULL,
  `name` varchar(50) NOT NULL,
  `namespace_id` int NOT NULL,
  `mobile` varchar(15) DEFAULT NULL,
  `gender` varchar(20) DEFAULT NULL,
  `address` varchar(200) DEFAULT NULL,
  `active_flag` tinyint NOT NULL DEFAULT '1',
  `updated_by` int NOT NULL,
  `updated_at` datetime NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uq_patient_code` (`code`),
  UNIQUE KEY `uq_patient_mobile` (`mobile`),
  KEY `fk_patient_updated_by` (`updated_by`),
  KEY `idx_patient_namespace_active` (`namespace_id`,`active_flag`),
  CONSTRAINT `fk_patient_namespace_id` FOREIGN KEY (`namespace_id`) REFERENCES `namespace` (`id`),
  CONSTRAINT `fk_patient_updated_by` FOREIGN KEY (`updated_by`) REFERENCES `user` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

/*Data for the table `patients` */

insert  into `patients`(`id`,`code`,`name`,`namespace_id`,`mobile`,`gender`,`address`,`active_flag`,`updated_by`,`updated_at`) values 
(1,'PAT00001','ranjan',2,'8765987078','male','west tambaram',1,2,'2026-06-18 19:06:54'),
(2,'PAT00002','Rahul',2,'8735849726','male','west tambaram',1,2,'2026-06-18 18:52:31'),
(3,'PAT00004','senthil',2,'87258497463','male','trichy',1,2,'2026-06-19 15:14:03');

/*Table structure for table `payments` */

DROP TABLE IF EXISTS `payments`;

CREATE TABLE `payments` (
  `id` int NOT NULL AUTO_INCREMENT,
  `code` varchar(20) NOT NULL,
  `billing_id` int NOT NULL,
  `amount` decimal(10,2) NOT NULL,
  `payment_mode` tinyint NOT NULL,
  `payment_date` datetime NOT NULL,
  `transaction_ref_no` varchar(100) DEFAULT NULL,
  `active_flag` tinyint NOT NULL DEFAULT '1',
  `updated_by` int NOT NULL,
  `updated_at` datetime NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uq_payment_code` (`code`),
  KEY `fk_payment_billing` (`billing_id`),
  KEY `fk_payment_updated_by` (`updated_by`),
  CONSTRAINT `fk_payment_billing` FOREIGN KEY (`billing_id`) REFERENCES `billing` (`id`),
  CONSTRAINT `fk_payment_updated_by` FOREIGN KEY (`updated_by`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

/*Data for the table `payments` */

/*Table structure for table `prescription_item` */

DROP TABLE IF EXISTS `prescription_item`;

CREATE TABLE `prescription_item` (
  `id` int NOT NULL AUTO_INCREMENT,
  `CODE` varchar(20) NOT NULL,
  `appointment_id` int DEFAULT NULL,
  `medicine_id` int DEFAULT NULL,
  `notes` varchar(200) DEFAULT NULL,
  `quantity` int DEFAULT NULL,
  `active_flag` tinyint NOT NULL DEFAULT '1',
  `updated_by` int NOT NULL,
  `updated_at` datetime NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uq_prescription_code` (`CODE`),
  KEY `fk_prescription_medicine_id` (`medicine_id`),
  KEY `fk_prescription_updated_by` (`updated_by`),
  KEY `idx_prescription_appointment_medicine` (`appointment_id`,`medicine_id`),
  CONSTRAINT `fk_prescription_appointment_id` FOREIGN KEY (`appointment_id`) REFERENCES `appointments` (`id`),
  CONSTRAINT `fk_prescription_medicine_id` FOREIGN KEY (`medicine_id`) REFERENCES `medicines` (`id`),
  CONSTRAINT `fk_prescription_updated_by` FOREIGN KEY (`updated_by`) REFERENCES `user` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

/*Data for the table `prescription_item` */

insert  into `prescription_item`(`id`,`CODE`,`appointment_id`,`medicine_id`,`notes`,`quantity`,`active_flag`,`updated_by`,`updated_at`) values 
(1,'PRESC00001',6,5,'take 2 tablete per day after food',10,1,19,'2026-06-20 12:57:26'),
(10,'PRESC00010',9,5,'take 2 tablete per day after food',10,1,19,'2026-06-20 13:43:27');

/*Table structure for table `user` */

DROP TABLE IF EXISTS `user`;

CREATE TABLE `user` (
  `id` int NOT NULL AUTO_INCREMENT,
  `code` varchar(20) NOT NULL,
  `user_name` varchar(50) NOT NULL,
  `namespace_id` int DEFAULT NULL,
  `token` varchar(255) NOT NULL,
  `role` tinyint NOT NULL,
  `active_flag` tinyint NOT NULL DEFAULT '1',
  `updated_by` int NOT NULL,
  `updated_at` datetime NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uq_users_code` (`code`),
  UNIQUE KEY `uq_users_user_name` (`user_name`),
  KEY `fk_users_updated_by` (`updated_by`),
  KEY `idx_users_namespace_active` (`namespace_id`,`active_flag`),
  KEY `idx_users_namespace_role` (`namespace_id`),
  CONSTRAINT `fk_users_namespace_id` FOREIGN KEY (`namespace_id`) REFERENCES `namespace` (`id`),
  CONSTRAINT `fk_users_updated_by` FOREIGN KEY (`updated_by`) REFERENCES `user` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=23 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

/*Data for the table `user` */

insert  into `user`(`id`,`code`,`user_name`,`namespace_id`,`token`,`role`,`active_flag`,`updated_by`,`updated_at`) values 
(1,'ADM000','admin',1,'$2a$10$mFvBAB.FdxfPmQ9axP0KW.gz9Rh1fvsqjtMhbYkaOUEWqWjqr0TOy',1,1,2,'2026-06-20 18:11:55'),
(2,'USR00001','Manikandan',2,'$2a$10$nu0NNvzagPBFXJ3qnHPdQ.GNJjqJzic14lYWHdcY2a3L6uGm5UcqK',2,1,1,'2026-06-10 16:22:34'),
(3,'USR00002','RamKumar',3,'$2a$10$YC/7Hjmsrsd.Y2NSiBK/ZewSIaIvEHSOZcFagfrlhCc87sEgH4KyG',2,1,1,'2026-06-10 16:31:53'),
(6,'USR00003','Rajesh K',5,'$2a$10$xYt4OSn4VZHmOCUWdWMB/.IYPZt4/7P3jSJ.gyghzy44zBa4ZEdJ2',2,1,1,'2026-06-13 17:03:24'),
(14,'USR00011','Jeni',2,'$2a$10$BhTjtCQzxUgshKbAxZRyU.gTMNC44jvRzP6dYZz67GF.7HIi00a6G',4,1,2,'2026-06-15 17:59:47'),
(15,'USR00012','Robert',3,'$2a$10$jfXq7js3THJNnNrbT5m7a.Rktc9TXeTMy3jtCpMOtgw4tRqLahVNS',3,1,1,'2026-06-15 17:04:27'),
(16,'USR00013','kumar',3,'$2a$10$imkYL8GoTKlr6A9rEwwRjuIFr7ktBRySgNL1Qah4joG5V4ITR65HK',4,1,1,'2026-06-15 17:45:04'),
(18,'USR00015','pradhap',2,'$2a$10$Aq9ZJPz37ItCErkpreXe3u0YiVA22llWRylWsitzIhGz0WZqFTvB6',5,1,2,'2026-06-16 10:42:49'),
(19,'USR00016','prasath',2,'$2a$10$4oxZZhhgisQCz.71ogj1g.V74RJ.V0eRT1r2zCIwbPI4KYOAH5Tyi',3,1,2,'2026-06-19 19:10:45'),
(20,'USR00017','vikram',6,'$2a$10$mhWP.P1f2CAgx689bc8gw.UC8.mJYX85S.2VLA/GO/EGQuU7R2ibK',2,1,2,'2026-06-19 10:37:09');

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
