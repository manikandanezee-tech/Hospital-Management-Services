		-- 	DEPARTMENT TABLE INSERT DATA
DELIMITER //
CREATE PROCEDURE EZEE_SP_DEPARTMENT_IUD(
	
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

	END IF;
	END//
DELIMITER;