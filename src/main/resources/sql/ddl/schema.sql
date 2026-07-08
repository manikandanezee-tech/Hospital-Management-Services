
CREATE DATABASE `hospital_management_services_db` ;

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
)


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
)

/*Data for the table `billing` */

/*Table structure for table `code_sequence` */

DROP TABLE IF EXISTS `code_sequence`;

CREATE TABLE `code_sequence` (
  `entity_name` varchar(30) NOT NULL,
  `next_value` int NOT NULL,
  PRIMARY KEY (`entity_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


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
)

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
  KEY `idx_department_namespace_active_flag` (`namespace_id`,`active_flag`),
  KEY `fk_department_updated_by` (`updated_by`),
  CONSTRAINT `fk_department_namespace_id` FOREIGN KEY (`namespace_id`) REFERENCES `namespace` (`id`),
  CONSTRAINT `fk_department_updated_by` FOREIGN KEY (`updated_by`) REFERENCES `user` (`id`)
)


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
) 

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
) 
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
) 

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
) 

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
)

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
) 
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
) 

