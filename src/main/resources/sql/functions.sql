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

/* Function  structure for function  `EZEE_FN_ISNOTNULL` */

/*!50003 DROP FUNCTION IF EXISTS `EZEE_FN_ISNOTNULL` */;
DELIMITER $$

 CREATE  FUNCTION `EZEE_FN_ISNOTNULL`(pcrString VARCHAR(100)) 
 RETURNS tinyint
    DETERMINISTIC
RETURN IF(ISNULL(pcrString)OR TRIM(pcrString)='',FALSE,TRUE)
 $$
DELIMITER ;

/* Function  structure for function  `EZEE_FN_ISNULL` */

 DROP FUNCTION IF EXISTS `EZEE_FN_ISNULL` ;
DELIMITER $$

 CREATE  FUNCTION `EZEE_FN_ISNULL`(pcrString varchar(100)) 
 RETURNS tinyint
    DETERMINISTIC
return if(isnull(pcrString)or trim(pcrString)='',true,false) 
$$
DELIMITER ;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
