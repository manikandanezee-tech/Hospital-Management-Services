-- DOCTOR TABLE
DELIMITER// 
CREATE PROCEDURE EZEE_SP_DOCTOR_IUD(
	
	INOUT pcrCode VARCHAR(30),
	IN pcrName VARCHAR(50),
	IN pitUserId INT,
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
	
	INSERT INTO doctor (CODE,NAME,user_id,department_id,namespace_id,mobile,email,specialization,consultation_fee,active_flag,
	updated_by,updated_at) 
	VALUES(pcrCode,pcrName,pitUserId,pitDepartmentId,pitNamespaceId,pcrMobile,pcrEmail,
	pcrSpecialization,pitConsultationFee,pitActiveFlag,pitUpdatedBy,NOW());
	
	SET pitRowCount=ROW_COUNT();
	
	-- 	UPDATE DOCTOR TABLE
	ELSEIF(pitActiveFlag=1 AND EZEE_FN_ISNOTNULL(pcrCode))THEN
	
	UPDATE doctor SET NAME=pcrName, user_id=pitUserId,department_id=pitDepartmentId, namespace_id=pitNamespaceId, mobile=pcrMobile, 
	specialization=pcrSpecialization, consultation_fee=pitConsultationFee,active_flag=pitActiveFlag, 
	updated_by=pitUpdatedBy, updated_at=NOW() WHERE CODE=pcrCode AND namespace_id=pitNamespaceId;
	
	SET pitRowCount=ROW_COUNT();

	END IF;
	END//
DELIMITER;