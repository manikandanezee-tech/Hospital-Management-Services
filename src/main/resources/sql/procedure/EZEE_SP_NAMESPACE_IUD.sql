DELIMITER //
CREATE PROCEDURE EZEE_SP_NAMESPACE_IUD(

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
	
	END IF;
	END//
DELIMITER;