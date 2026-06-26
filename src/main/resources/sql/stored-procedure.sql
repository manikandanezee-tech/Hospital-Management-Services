
CREATE DATABASE /*!32312 IF NOT EXISTS*/`hospital_management_services_db` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;

USE `hospital_management_services_db`;

/* Procedure structure for procedure `EZEE_SP_APPOINTMENT_IUD` */

!50003 DROP PROCEDURE IF EXISTS  `EZEE_SP_APPOINTMENT_IUD` ;

DELIMITER $$

!50003 CREATE DEFINER=`root`@`localhost` PROCEDURE `EZEE_SP_APPOINTMENT_IUD`(
	Inout pcrCode VARCHAR(30),
	IN pitPatientId INT,
	IN pitDoctorId INT,
	IN pitNamespaceId INT,
	IN pdtAppointmentDateTime DATETIME,
	IN pitTokenNumber INT,
	IN pitStatus TINYINT,
	IN pitActiveFlag TINYINT,
	IN pitUpdatedBy INT,
	OUT pitRowCount INT)
BEGIN
	
-- 	DECLARE THE VARIABLE FOR FIND THE UNIQUE VALUE
	DECLARE litNextValue INT;
-- 	DECLARE THE VARIABLE FOR CREATE UNIQUE CODE
	DECLARE lcrCode VARCHAR(30);
		SET pitRowCount=0;
IF(pitActiveFlag=1 AND EZEE_FN_ISNULL(pcrCode))THEN
	SELECT next_value INTO litNextValue FROM code_sequence WHERE entity_name="APPOINTMENT";
	--  CREATE THE UNIQUE CODE
	SET lcrCode=CONCAT("APNMT",LPAD(litNextValue,5,'0'));
	--  UPDATE THE CODE_SEQUENCE TABLE'S NEXT_VALUE USING ENTITY NAME FOR NEXT UNIQUE CODE	
	UPDATE code_sequence SET next_value=next_value+1 WHERE entity_name="APPOINTMENT";
	SET pcrCode=lcrCode;
	INSERT INTO appointments (CODE,patient_id,doctor_id,namespace_id,appointment_datetime,token_number,STATUS,active_flag,updated_by,updated_at) 
	VALUES(pcrCode,pitPatientId,pitDoctorId,pitNamespaceId,pdtAppointmentDateTime,pitTokenNumber,pitStatus,pitActiveFlag,pitUpdatedBy,NOW());
SET pitRowCount=ROW_COUNT();	
-- UPDATE APPOINTMENT TABLE DATA
ELSEIF(pitActiveFlag=1 AND EZEE_FN_ISNOTNULL(pcrCode))THEN

	UPDATE appointments SET patient_id=pitPatientId, doctor_id=pitDoctorId, appointment_datetime=pdtAppointmentDateTime, 
	token_number=pitTokenNumber, STATUS=pitStatus, active_flag=pitActiveFlag, updated_by=pitUpdatedBy,updated_at=NOW() WHERE CODE=pcrCode AND namespace_id=pitNamespaceId;
SET pitRowCount=ROW_COUNT();
-- 	DELETE APPOINTMENT TABLE DATA
ELSEIF(pitActiveFlag!=1 AND EZEE_FN_ISNOTNULL(pcrCode))THEN
UPDATE appointments SET patient_id=pitPatientId, doctor_id=pitDoctorId, appointment_datetime=pdtAppointmentDateTime, 
	token_number=pitTokenNumber, STATUS=pitStatus, active_flag=pitActiveFlag, updated_by=pitUpdatedBy,updated_at=NOW() WHERE CODE=pcrCode AND namespace_id=pitNamespaceId;
SET pitRowCount=ROW_COUNT();
END IF;
END $$
DELIMITER ;

/* Procedure structure for procedure `EZEE_SP_BILLING_IUD` */

!50003 DROP PROCEDURE IF EXISTS  `EZEE_SP_BILLING_IUD` ;

DELIMITER $$

!50003 CREATE DEFINER=`root`@`localhost` PROCEDURE `EZEE_SP_BILLING_IUD`(

	INOUT pcrCode VARCHAR(30),
	IN pitNamespaceId int,
	IN pitAppointmentId INT,
	in pitTotalAmount decimal(10,2),
	IN pitPaidAmount DECIMAL(10,2),
	IN pitBalanceAmount DECIMAL(10,2),
	in pitBillingStatus int,
	IN pitActiveFlag TINYINT,
	IN pitUpdatedBy INT,
	OUT pitRowCount INT)
BEGIN

-- 	DECLARE THE VARIABLE FOR FIND THE UNIQUE VALUE

	DECLARE litNextValue INT;

-- 	DECLARE THE VARIABLE FOR CREATE UNIQUE CODE

	DECLARE lcrCode VARCHAR(30);

	SET pitRowCount=0;

IF(pitActiveFlag=1 AND EZEE_FN_ISNULL(pcrCode))THEN

	

	SELECT next_value INTO litNextValue FROM code_sequence WHERE entity_name="BILLING";

	--  CREATE THE UNIQUE CODE

	SET lcrCode=CONCAT("BILL",LPAD(litNextValue,5,'0'));

	--  UPDATE THE CODE_SEQUENCE TABLE'S NEXT_VALUE USING ENTITY NAME FOR NEXT UNIQUE CODE	

	

	UPDATE code_sequence SET next_value=next_value+1 WHERE entity_name="BILLING";

	

	SET pcrCode=lcrCode;

	INSERT INTO billing(CODE,namespace_id,appointment_id,bill_datetime,total_amount,paid_amount,balance_amount,billing_status,active_flag,updated_by,updated_at) 

	VALUES(pcrCode,pitNamespaceId,pitAppointmentId,now(),pitTotalAmount,pitPaidAmount,pitBalanceAmount,pitBillingStatus,pitActiveFlag,pitUpdatedBy,NOW());

	SET pitRowCount=ROW_COUNT();

	-- UPDATE BILLING TABLE DATA

ELSEIF(pitActiveFlag=1 AND EZEE_FN_ISNOTNULL(pcrCode))THEN

	UPDATE billing set namespace_id=pitNamespaceId,appointment_id=pitAppointmentId, bill_datetime=now(),total_amount= pitTotalAmount,paid_amount=pitPaidAmount,
	balance_amount=pitBalanceAmount,active_flag=pitActiveFlag, billing_status=pitBillingStatus, updated_by=pitUpdatedBy,updated_at=now() where code = pcrCode;

	SET pitRowCount=ROW_COUNT();

	END IF;

	END $$
DELIMITER ;

/* Procedure structure for procedure `EZEE_SP_CONSULTATION_IUD` */

!50003 DROP PROCEDURE IF EXISTS  `EZEE_SP_CONSULTATION_IUD` ;

DELIMITER $$

!50003 CREATE DEFINER=`root`@`localhost` PROCEDURE `EZEE_SP_CONSULTATION_IUD`(
	INOUT pcrCode VARCHAR(30),
	IN pitAppointmentId INT,
	IN pcrChiefComplaint VARCHAR(200),
	IN pcrDiagnosis VARCHAR(100),
	IN pcrDoctorNotes TEXT,
	IN pdtConsultationDateTime DATETIME,
	IN pitActiveFlag TINYINT,
	IN pitUpdatedBy INT,
	out pitRowCount INT)
BEGIN
-- 	DECLARE THE VARIABLE FOR FIND THE UNIQUE VALUE
	DECLARE litNextValue INT;
-- 	DECLARE THE VARIABLE FOR CREATE UNIQUE CODE
	DECLARE lcrCode VARCHAR(30);
	SET pitRowCount=0;
IF(pitActiveFlag=1 AND EZEE_FN_ISNULL(pcrCode))THEN
	
	SELECT next_value INTO litNextValue FROM code_sequence WHERE entity_name="CONSULTATION";
	--  CREATE THE UNIQUE CODE
	SET lcrCode=CONCAT("CONSULT",LPAD(litNextValue,5,'0'));
	--  UPDATE THE CODE_SEQUENCE TABLE'S NEXT_VALUE USING ENTITY NAME FOR NEXT UNIQUE CODE	
	
	UPDATE code_sequence SET next_value=next_value+1 WHERE entity_name="CONSULTATION";
	
	SET pcrCode=lcrCode;
	INSERT INTO consultations(CODE,appointment_id,chief_complaint,diagnosis, doctor_notes, consultation_datetime, active_flag,updated_by,updated_at) 
	VALUES(pcrCode,pitAppointmentId,pcrChiefComplaint,pcrDiagnosis,pcrDoctorNotes,pdtConsultationDateTime,pitActiveFlag,pitUpdatedBy,NOW());
	SET pitRowCount=ROW_COUNT();
	-- UPDATE CONSULTATION TABLE DATA
ELSEIF(pitActiveFlag=1 AND EZEE_FN_ISNOTNULL(pcrCode))THEN
	UPDATE consultations set appointment_id=pitAppointmentId, chief_complaint=pcrChiefComplaint, 
	diagnosis=pcrDiagnosis,doctor_notes=pcrDoctorNotes,consultation_datetime=pdtConsultationDateTime, 
	active_flag=pitActiveFlag, updated_by=pitUpdatedBy,updated_at=now() where code = pcrCode;
	SET pitRowCount=ROW_COUNT();
	--  DELETE CONSULTATION TABLE ROW (SOFT DELETE)
ELSEIF(pitActiveFlag!=1 AND EZEE_FN_ISNOTNULL(pcrCode))THEN
	UPDATE consultations SET active_flag=pitActiveFlag, updated_by=pitUpdatedBy,
	updated_at=NOW() WHERE CODE = pcrCode;
	SET pitRowCount=ROW_COUNT();
	END IF;
	END $$
DELIMITER ;

/* Procedure structure for procedure `EZEE_SP_DEPARTMENT_IUD` */

!50003 DROP PROCEDURE IF EXISTS  `EZEE_SP_DEPARTMENT_IUD` ;

DELIMITER $$

!50003 CREATE DEFINER=`root`@`localhost` PROCEDURE `EZEE_SP_DEPARTMENT_IUD`(
	INOUT pcrCode VARCHAR(30),
	IN pcrName VARCHAR(50),
	IN pitNamespaceId INT,
	IN pitActiveFlag TINYINT,
	IN pitUpdatedBy INT,
	OUT pitRowCount INT)
BEGIN
-- 	DECLARE THE VARIABLE FOR FIND THE UNIQUE VALUE
	DECLARE litNextValue INT;
-- 	DECLARE THE VARIABLE FOR CREATE UNIQUE CODE
	DECLARE lcrCode VARCHAR(30);
	SET pitRowCount=0;
IF(pitActiveFlag=1 AND EZEE_FN_ISNULL(pcrCode))THEN
	SELECT next_value INTO litNextValue FROM code_sequence WHERE entity_name="DEPARTMENT";
	--  CREATE THE UNIQUE CODE
	SET lcrCode=CONCAT("DEPT",LPAD(litNextValue,5,'0'));
	--  UPDATE THE CODE_SEQUENCE TABLE'S NEXT_VALUE USING ENTITY NAME FOR NEXT UNIQUE CODE	
	UPDATE code_sequence SET next_value=next_value+1 WHERE entity_name="DEPARTMENT";
	SET pcrCode=lcrCode;
	INSERT INTO departments(CODE,NAME,namespace_id,active_flag,updated_by,updated_at) 
	VALUES(pcrCode,pcrName,pitNamespaceId,pitActiveFlag,pitUpdatedBy,NOW());
	SET pitRowCount=ROW_COUNT();
ELSEIF(pitActiveFlag=1 AND EZEE_FN_ISNOTNULL(pcrCode))THEN
	-- UPDATE DEPARTMENT TABLE DATA
	UPDATE departments SET NAME=pcrName, namespace_id=pitNamespaceId,
	updated_by=pitUpdatedBy, active_flag=pitActiveFlag, updated_at=NOW() 
	WHERE CODE=pcrCode AND namespace_id=pitNamespaceId;
	SET pitRowCount=ROW_COUNT();
ELSEIF(pitActiveFlag!=1 AND EZEE_FN_ISNOTNULL(pcrCode))THEN
	--  DELETE DEPARTMENT TABLE ROW (SOFT DELETE)
	UPDATE departments SET NAME=pcrName,namespace_id=pitNamespaceId, active_flag=pitActiveFlag, updated_by=pitUpdatedBy, updated_at=NOW() 
	WHERE CODE=pcrCode AND namespace_id=pitNamespaceId;
	SET pitRowCount=ROW_COUNT();
	END IF;
	END $$
DELIMITER ;

/* Procedure structure for procedure `EZEE_SP_DOCTOR_IUD` */

!50003 DROP PROCEDURE IF EXISTS  `EZEE_SP_DOCTOR_IUD` ;

DELIMITER $$

!50003 CREATE DEFINER=`root`@`localhost` PROCEDURE `EZEE_SP_DOCTOR_IUD`(
	INOUT pcrCode VARCHAR(30),
	IN pcrName VARCHAR(50),
	in pitUserId int,
	IN pitDepartmentId INT,
	IN pitNamespaceId INT,
	IN pcrMobile VARCHAR(15),
	IN pcrEmail VARCHAR(100),
	IN pcrSpecialization VARCHAR(100),
	IN pitConsultationFee DECIMAL(10,2),
	IN pitActiveFlag TINYINT,
	IN pitUpdatedBy INT,
	OUT pitRowCount INT)
BEGIN
	
-- 	DECLARE THE VARIABLE FOR FIND THE UNIQUE VALUE
	DECLARE litNextValue INT;
-- 	DECLARE THE VARIABLE FOR CREATE UNIQUE CODE
	DECLARE lcrCode VARCHAR(30);
	SET pitRowCount=0;
	
IF(pitActiveFlag=1 AND EZEE_FN_ISNULL(pcrCode))THEN
	SELECT next_value INTO litNextValue FROM code_sequence WHERE entity_name="DOCTOR";
	--  CREATE THE UNIQUE CODE
	
	SET lcrCode=CONCAT("DOC",LPAD(litNextValue,5,'0'));
	
	--  UPDATE THE CODE_SEQUENCE TABLE'S NEXT_VALUE USING ENTITY NAME FOR NEXT UNIQUE CODE		
	UPDATE code_sequence SET next_value=litNextValue+1 WHERE entity_name="DOCTOR";
	
	SET pcrCode=lcrCode;
	INSERT INTO doctors (CODE,NAME,user_id,department_id,namespace_id,mobile,email,specialization,consultation_fee,active_flag,
	updated_by,updated_at) 
	VALUES(pcrCode,pcrName,pitUserId,pitDepartmentId,pitNamespaceId,pcrMobile,pcrEmail,
	pcrSpecialization,pitConsultationFee,pitActiveFlag,pitUpdatedBy,NOW());
	SET pitRowCount=ROW_COUNT();
	-- 	UPDATE DOCTOR TABLE
ELSEIF(pitActiveFlag=1 AND EZEE_FN_ISNOTNULL(pcrCode))THEN
	UPDATE doctors SET NAME=pcrName, user_id=pitUserId,department_id=pitDepartmentId, namespace_id=pitNamespaceId, mobile=pcrMobile, 
	specialization=pcrSpecialization, consultation_fee=pitConsultationFee,active_flag=pitActiveFlag, 
	updated_by=pitUpdatedBy, updated_at=NOW() WHERE CODE=pcrCode AND namespace_id=pitNamespaceId;
	SET pitRowCount=ROW_COUNT();
-- 	SOFT DELETE DOCTOR
ELSEIF(pitActiveFlag!=1 AND EZEE_FN_ISNOTNULL(pcrCode))THEN
	UPDATE doctors SET NAME=pcrName, user_id=pitUserId,department_id=pitDepartmentId,mobile=pcrMobile, 
	specialization=pcrSpecialization, consultation_fee=pitConsultationFee,active_flag=pitActiveFlag,updated_by=pitUpdatedBy,
	updated_at=NOW() WHERE CODE =pcrCode AND namespace_id=pitNamespaceId;
	SET pitRowCount=ROW_COUNT();
	END IF;
	END $$
DELIMITER ;

/* Procedure structure for procedure `EZEE_SP_MEDICINE_IUD` */

!50003 DROP PROCEDURE IF EXISTS  `EZEE_SP_MEDICINE_IUD` ;

DELIMITER $$

!50003 CREATE DEFINER=`root`@`localhost` PROCEDURE `EZEE_SP_MEDICINE_IUD`(
	INOUT pcrCode varchar(30),
	IN pcrName VARCHAR(50),
	IN pitNamespaceId INT,
	IN pitPrice DECIMAL(10,2),
	IN pitCurrentStock INT,
	IN pcrSupplier VARCHAR(100),
	IN pitActiveFlag TINYINT,
	IN pitUpdatedBy INT,
	out pitRowCount int)
BEGIN
	
-- 	DECLARE THE VARIABLE FOR FIND THE UNIQUE VALUE
	DECLARE litNextValue INT;
-- 	DECLARE THE VARIABLE FOR CREATE UNIQUE CODE
	DECLARE lcrCode VARCHAR(30);
	
	set pitRowCount=0;
if(pitActiveFlag=1 and EZEE_FN_ISNULL(pcrCode))then
	SELECT next_value INTO litNextValue FROM code_sequence WHERE entity_name="MEDICINE";
	--  CREATE THE UNIQUE CODE
	SET lcrCode=CONCAT("MED",LPAD(litNextValue,5,'0'));
	--  UPDATE THE CODE_SEQUENCE TABLE'S NEXT_VALUE USING ENTITY NAME FOR NEXT UNIQUE CODE	
	UPDATE code_sequence SET next_value=next_value+1 WHERE entity_name="MEDICINE";
	set pcrCode=lcrCode;
	INSERT INTO medicines (CODE,NAME,namespace_id,price,current_stock,supplier,active_flag,updated_by,updated_at) 
	VALUES(pcrCode,pcrName,pitNamespaceId,pitPrice,pitCurrentStock,pcrSupplier,pitActiveFlag,pitUpdatedBy,NOW());
	set pitRowCount=row_count();
	
	-- UPDATE MEDICINE TABLE DATA
elseif(pitActiveFlag=1 and EZEE_FN_ISNOTNULL(pcrCode))then
	UPDATE medicines SET NAME=pcrName, namespace_id=pitNamespaceId, price=pitPrice, current_stock=pitCurrentStock,
	supplier=pcrSupplier, active_flag=pitActiveFlag, updated_by=pitUpdatedBy,updated_at=NOW()  
	WHERE CODE=pcrCode and namespace_id=pitNamespaceId;
	SET pitRowCount=ROW_COUNT();
	
	-- 	DELETE MEDICINE TABLE DATA
elseif(pitActiveFlag!=1 and EZEE_FN_ISNOTNULL(pcrCode))THEN
	UPDATE medicines SET active_flag=pitActiveFlag,updated_by=pitUpdatedBy,updated_at=NOW() 
	WHERE CODE =pcrCode and namespace_id=pitNamespaceId;
	SET pitRowCount=ROW_COUNT();
	end if;
	END $$
DELIMITER ;

/* Procedure structure for procedure `EZEE_SP_NAMESPACE_IUD` */

!50003 DROP PROCEDURE IF EXISTS  `EZEE_SP_NAMESPACE_IUD` ;

DELIMITER $$

!50003 CREATE DEFINER=`root`@`localhost` PROCEDURE `EZEE_SP_NAMESPACE_IUD`(
	INOUT pcrCode VARCHAR(30),
	IN pcrName VARCHAR(100),
	IN pcrAddress VARCHAR(100),
	IN pitActiveFlag TINYINT,
	IN pitUpdatedBy INT,
	OUT pitRowCount INT)
BEGIN
	
-- 	DECLARE THE VARIABLE FOR FIND THE UNIQUE VALUE
	DECLARE litNextValue INT;
-- 	DECLARE THE VARIABLE FOR CREATE UNIQUE CODE
	DECLARE lcrCode VARCHAR(30);
	SET pitRowCount=0;
	
IF(pitActiveFlag=1 AND EZEE_FN_ISNULL(pcrCode))THEN
    SELECT next_value
    INTO litNextValue
    FROM code_sequence
    WHERE entity_name="NAMESPACE";
--  CREATE THE UNIQUE CODE
    SET lcrCode = CONCAT(UPPER(LEFT(pcrName,3)),LPAD(litNextValue,5,'0'));
--  UPDATE THE CODE_SEQUENCE TABLE'S NEXT_VALUE USING ENTITY NAME FOR NEXT UNIQUE CODE	
    UPDATE code_sequence
    SET next_value = next_value + 1
    WHERE entity_name="NAMESPACE";
	SET pcrCode=lcrCode;
	INSERT INTO namespace (CODE,NAME,address,active_flag,updated_by,updated_at) VALUES(pcrCode,pcrName,pcrAddress,pitActiveFlag,pitUpdatedBy,NOW());
	SET pitRowCount=ROW_COUNT();
	--  UPDDATE NAMESPACE TABLE
ELSEIF(pitActiveFlag=1 AND EZEE_FN_ISNOTNULL(pcrCode))THEN
	UPDATE namespace SET NAME=pcrName, address=pcrAddress,active_flag=pitActiveFlag, 
	updated_by=pitUpdatedBy, updated_at=NOW() WHERE CODE=pcrCode;
	SET pitRowCount=ROW_COUNT();
	--  DELETE NAMESPACE TABLE
ELSEIF(pitActiveFlag!=1 AND EZEE_FN_ISNOTNULL(pcrCode))THEN
	UPDATE namespace SET NAME=pcrName, address=pcrAddress,active_flag=pitActiveFlag,updated_by=pitUpdatedBy,
	updated_at=NOW() WHERE CODE =pcrCode;
	SET pitRowCount=ROW_COUNT();
	END IF;
END $$
DELIMITER ;

/* Procedure structure for procedure `EZEE_SP_PATIENT_IUD` */

!50003 DROP PROCEDURE IF EXISTS  `EZEE_SP_PATIENT_IUD` ;

DELIMITER $$

!50003 CREATE DEFINER=`root`@`localhost` PROCEDURE `EZEE_SP_PATIENT_IUD`(
	inout pcrCode varchar(30),
	IN pcrName VARCHAR(50),
	IN pitNamespaceId INT,
	IN pcrMobile VARCHAR(15),
	IN pcrGender VARCHAR(20),
	IN pcrAddress VARCHAR(200),
	IN pitActiveFlag TINYINT,
	IN pitUpdatedBy INT,
	out pitRowCount int)
BEGIN
	
-- 	DECLARE THE VARIABLE FOR FIND THE UNIQUE VALUE
	DECLARE litNextValue INT;
-- 	DECLARE THE VARIABLE FOR CREATE UNIQUE CODE
	DECLARE lcrCode VARCHAR(30);
	
	set pitRowCount=0;
if(pitActiveFlag=1 and EZEE_FN_ISNULL(pcrCode))then
	SELECT next_value INTO litNextValue FROM code_sequence WHERE entity_name="PATIENT";
	--  CREATE THE UNIQUE CODE
	SET lcrCode=CONCAT("PAT",LPAD(litNextValue,5,'0'));
	--  UPDATE THE CODE_SEQUENCE TABLE'S NEXT_VALUE USING ENTITY NAME FOR NEXT UNIQUE CODE	
	UPDATE code_sequence SET next_value=next_value+1 WHERE entity_name="PATIENT";
	set pcrCode=lcrCode;
	INSERT INTO patients (CODE,NAME,namespace_id,mobile,gender,address,active_flag,updated_by,updated_at) 
	VALUES(pcrCode,pcrName,pitNamespaceId,pcrMobile,pcrGender,pcrAddress,pitActiveFlag,pitUpdatedBy,NOW());
	set pitRowCount=row_count();
	--  UPDATE PATIENT TABLE
elseif(pitActiveFlag=1 and EZEE_FN_ISNOTNULL(pcrCode))then
	UPDATE patients SET NAME=pcrName, namespace_id=pitNamespaceId, mobile=pcrMobile, gender=pcrGender, address=pcrAddress,
	updated_by=pitUpdatedBy, active_flag=pitActiveFlag, updated_at=NOW() WHERE CODE=pcrCode and namespace_id=pitNamespaceId;
	SET pitRowCount=ROW_COUNT();
	-- 	SOFT DELETE THE PATIENT
elseif(pitActiveFlag!=1 and EZEE_FN_ISNOTNULL(pcrCode))then
	UPDATE patients SET active_flag=pitActiveFlag,updated_by=pitUpdatedBy, updated_at=NOW() 
	WHERE CODE =pcrCode and namespace_id=pitNamespaceId;
	SET pitRowCount=ROW_COUNT();
	end if;
	END $$
DELIMITER ;

/* Procedure structure for procedure `EZEE_SP_PAYMENT_IUD` */

!50003 DROP PROCEDURE IF EXISTS  `EZEE_SP_PAYMENT_IUD` ;

DELIMITER $$

!50003 CREATE DEFINER=`root`@`localhost` PROCEDURE `EZEE_SP_PAYMENT_IUD`(
	INOUT pcrCode varchar(30),
	in pitBillingId int,
	in pitAmount decimal(10,2),
	in pitPaymentMode tinyint,
	in pcrTransactionRefNo varchar(100),
	in pitActiveFlag tinyint,
	in pitUpdatedBy int,
	out pitRowCount int)
begin
	
-- 	DECLARE THE VARIABLE FOR FIND THE UNIQUE VALUE

	DECLARE litNextValue INT;

-- 	DECLARE THE VARIABLE FOR CREATE UNIQUE CODE

	DECLARE lcrCode VARCHAR(30);

	SET pitRowCount=0;

IF(pitActiveFlag=1 AND EZEE_FN_ISNULL(pcrCode))THEN

	SELECT next_value INTO litNextValue FROM code_sequence WHERE entity_name="PAYMENT";

	--  CREATE THE UNIQUE CODE

	SET lcrCode=CONCAT("PAY",LPAD(litNextValue,5,'0'));

	--  UPDATE THE CODE_SEQUENCE TABLE'S NEXT_VALUE USING ENTITY NAME FOR NEXT UNIQUE CODE	

	UPDATE code_sequence SET next_value=next_value+1 WHERE entity_name="PAYMENT";
	SET pcrCode=lcrCode;
-- 	INSERT QUERY FOR PAYMENT TABLE
	
	INSERT into payment (code, billing_id, amount, payment_mode, payment_datetime, transaction_ref_no, active_flag, updated_by, updated_at)
	values(pcrCode, pitBillingId, pitAmount, pitPaymentMode, now(), pcrTransactionRefNo, pitActiveFlag,pitUpdatedBy,now());
	SET pitRowCount=ROW_COUNT();

	-- UPDATE PAYMENT TABLE DATA

ELSEIF(pitActiveFlag=1 AND EZEE_FN_ISNOTNULL(pcrCode))THEN
	UPDATE payment set billing_id=pitBillingId, amount=PitAmount, payment_mode=PitPaymentMode, payment_datetime=now(), updated_by=pitUpdatedBy,
	active_flag=pitActiveFlag,updated_at=now() where code=pcrCode;
	SET pitRowCount=ROW_COUNT();
	end if;
	end $$
DELIMITER ;

/* Procedure structure for procedure `EZEE_SP_PRESCRIPTION_IUD` */

!50003 DROP PROCEDURE IF EXISTS  `EZEE_SP_PRESCRIPTION_IUD` ;

DELIMITER $$

!50003 CREATE DEFINER=`root`@`localhost` PROCEDURE `EZEE_SP_PRESCRIPTION_IUD`(
	INOUT pcrCode varchar(30),
	in pitAppointmentId int,
	in pitMedicineId int,
	in pcrNotes varchar(200),
	in pitQuantity int,
	in pitActiveFlag int,
	in pitUpdatedBy int,
	out pitRowCount int)
begin
-- 	DECLARE THE VARIABLE FOR FIND UNIQUE VALUE FOR UNIQUE CODE
	declare litNextValue int;
-- 	DECLARE THE VARIABLE FOR STORE THE UNIQUE CODE
	declare lcrCode varchar(30);
	set pitRowCount=0;
if(EZEE_FN_ISNULL(pcrCode) and pitActiveFlag=1)then
	
	SELECT next_value into litNextValue from code_sequence where entity_name="PRESCRIPTION" ;
	
	SET lcrCode=CONCAT("PRESC",LPAD(litNextValue,5,'0'));
	--  UPDATE THE CODE_SEQUENCE TABLE'S NEXT_VALUE USING ENTITY NAME FOR NEXT UNIQUE CODE	
	update code_sequence set next_value=next_value+1 where entity_name="PRESCRIPTION";
	set pcrCode=lcrCode;
	
	insert into prescription_item (code, appointment_id, medicine_id, notes, quantity, active_flag, updated_by, updated_at)
	values(pcrCode,pitAppointmentId, pitMedicineId, pcrNotes, pitQuantity,pitActiveFlag,pitUpdatedBy,now());	
	SET pitRowCount=ROW_COUNT();
-- 	UPDATE QUERY FOR PRESCRIPTION IUD
elseIf(pitActiveFlag=1 and EZEE_FN_ISNOTNULL(pcrCode))then
	update prescription_item set appointment_id=pitAppointmentId, medicine_id=pitMedicineId, notes=pcrNotes, quantity=pitQuantity,
	active_flag=pitActiveFlag,updated_by=pitUpdatedBy,updated_at=now() where code= pcrCode and appointment_id=pitAppointmentId;
	SET pitRowCount=ROW_COUNT();
-- 	SOFT DELETE QUERY FOR PRESCRIPITON IUD
elseif(pitActiveFlag!=1 and EZEE_FN_ISNOTNULL(pcrCode))THEN
UPDATE prescription_item SEt active_flag=pitActiveFlag,updated_by=pitUpdatedBy,updated_at=NOW() WHERE CODE= pcrCode AND appointment_id=pitAppointmentId;
	SET pitRowCount=ROW_COUNT();
	end if;
	end $$
DELIMITER ;

/* Procedure structure for procedure `EZEE_SP_USER_IUD` */

!50003 DROP PROCEDURE IF EXISTS  `EZEE_SP_USER_IUD` ;

DELIMITER $$

!50003 CREATE DEFINER=`root`@`localhost` PROCEDURE `EZEE_SP_USER_IUD`(
	INOUT pcrCode VARCHAR(30),
	IN pcrUserName VARCHAR(50),
	IN pcrToken VARCHAR(255),
	IN pitRole int,
	IN pitNamespaceId INT,
	IN pitActiveFlag TINYINT,
	IN pitUpdatedBy INT,
	OUT pitRowCount INT)
BEGIN
	
-- 	DECLARE THE VARIABLE FOR FIND THE UNIQUE VALUE
	DECLARE litNextValue INT;
-- 	DECLARE THE VARIABLE FOR CREATE UNIQUE CODE
	DECLARE lcrCode VARCHAR(30);
	
	SET pitRowCount=0;
IF(pitActiveFlag=1 AND EZEE_FN_ISNULL(pcrCode))THEN
    SELECT next_value
    INTO litNextValue
    FROM code_sequence
    WHERE entity_name="USER";
--  CREATE THE UNIQUE CODE
    SET lcrCode = CONCAT("USR",LPAD(litNextValue,5,'0'));

--  UPDATE THE CODE_SEQUENCE TABLE'S NEXT_VALUE USING ENTITY NAME FOR NEXT UNIQUE CODE	
    UPDATE code_sequence
    SET next_value = next_value + 1
    WHERE entity_name="USER";
	SET pcrCode=lcrCode;
	INSERT INTO user(CODE,user_name,token,ROLE,namespace_id,active_flag,updated_by,updated_at)
	VALUES(pcrCode,pcrUserName,pcrToken,pitRole,pitNamespaceId,pitActiveFlag,pitUpdatedBy,NOW());
	SET pitRowCount=ROW_COUNT();
-- 	UPDATE  

	ELSEIF(pitActiveFlag=1 AND EZEE_FN_ISNOTNULL(pcrCode))THEN
	
	UPDATE user SET user_name=pcrUserName, token=pcrToken, ROLE=pitRole,
	namespace_id=pitNamespaceId,active_flag=pitActiveFlag,
	updated_by=pitUpdatedBy, updated_at=NOW() WHERE CODE=pcrCode AND namespace_id=pitNamespaceId;
	SET pitRowCount=ROW_COUNT();
-- 	SOFT DELETE 
	ELSEIF(pitActiveFlag!=1 AND EZEE_FN_ISNOTNULL(pcrCode))THEN
	
	UPDATE user SET user_name=pcrUserName, token=pcrToken, ROLE=pitRole,active_flag=pitActiveFlag,updated_by=pitUpdatedBy, updated_at=NOW() WHERE CODE =pcrCode 
	AND namespace_id=pitNamespaceId;
	SET pitRowCount=ROW_COUNT();
	END IF;
END $$
DELIMITER ;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
