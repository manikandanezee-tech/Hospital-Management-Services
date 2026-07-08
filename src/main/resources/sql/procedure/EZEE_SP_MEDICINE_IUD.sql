	-- 	MEDICINE TABLE INSERT DATA
DELIMITER //
CREATE PROCEDURE EZEE_SP_MEDICINE_IUD(

	INOUT pcrCode VARCHAR(30),
	IN pcrName VARCHAR(50),
	IN pitNamespaceId INT,
	IN pitPrice DECIMAL(10,2),
	IN pitCurrentStock INT,
	IN pcrSupplier VARCHAR(100),
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
	
	SELECT next_value INTO litNextValue FROM code_sequence WHERE entity_name="MEDICINE";
	--  CREATE THE UNIQUE CODE
	SET lcrCode=CONCAT("MED",LPAD(litNextValue,5,'0'));
	
	--  UPDATE THE CODE_SEQUENCE TABLE'S NEXT_VALUE USING ENTITY NAME FOR NEXT UNIQUE CODE	
	UPDATE code_sequence SET next_value=next_value+1 WHERE entity_name="MEDICINE";
	
	SET pcrCode=lcrCode;
	
	INSERT INTO medicines (CODE,NAME,namespace_id,price,current_stock,supplier,active_flag,updated_by,updated_at) 
	VALUES(pcrCode,pcrName,pitNamespaceId,pitPrice,pitCurrentStock,pcrSupplier,pitActiveFlag,pitUpdatedBy,NOW());
	
	SET pitRowCount=ROW_COUNT();
	
	-- UPDATE MEDICINE TABLE DATA
	ELSEIF(pitActiveFlag=1 AND EZEE_FN_ISNOTNULL(pcrCode))THEN
	
	UPDATE medicines SET NAME=pcrName, namespace_id=pitNamespaceId, price=pitPrice, current_stock=pitCurrentStock,
	supplier=pcrSupplier, active_flag=pitActiveFlag, updated_by=pitUpdatedBy,updated_at=NOW()  
	WHERE CODE=pcrCode AND namespace_id=pitNamespaceId;
	
	SET pitRowCount=ROW_COUNT();

	END IF;
	END//
DELIMITER;