-- 	PATIENT TABLE INSERT
DELIMITER //
CREATE PROCEDURE EZEE_SP_PATIENT_IUD(

	INOUT pcrCode VARCHAR(30),
	IN pcrName VARCHAR(50),
	IN pitNamespaceId INT,
	IN pcrMobile VARCHAR(15),
	IN pcrGender VARCHAR(20),
	IN pcrAddress VARCHAR(200),
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
	
	SELECT next_value INTO litNextValue FROM code_sequence WHERE entity_name="PATIENT";
	--  CREATE THE UNIQUE CODE
	SET lcrCode=CONCAT("PAT",LPAD(litNextValue,5,'0'));
	
	--  UPDATE THE CODE_SEQUENCE TABLE'S NEXT_VALUE USING ENTITY NAME FOR NEXT UNIQUE CODE	
	UPDATE code_sequence SET next_value=next_value+1 WHERE entity_name="PATIENT";
	SET pcrCode=lcrCode;
	
	INSERT INTO patient (CODE,NAME,namespace_id,mobile,gender,address,active_flag,updated_by,updated_at) 
	VALUES(pcrCode,pcrName,pitNamespaceId,pcrMobile,pcrGender,pcrAddress,pitActiveFlag,pitUpdatedBy,NOW());
	
	SET pitRowCount=ROW_COUNT();
	
	--  UPDATE PATIENT TABLE
	ELSEIF(pitActiveFlag=1 AND EZEE_FN_ISNOTNULL(pcrCode))THEN
	
	UPDATE patient SET NAME=pcrName, namespace_id=pitNamespaceId, mobile=pcrMobile, gender=pcrGender, address=pcrAddress,
	updated_by=pitUpdatedBy, active_flag=pitActiveFlag, updated_at=NOW() WHERE CODE=pcrCode AND namespace_id=pitNamespaceId;
	
	SET pitRowCount=ROW_COUNT();

	END IF;
	END//
DELIMITER;