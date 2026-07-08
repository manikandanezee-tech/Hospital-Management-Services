DELIMITER //
CREATE PROCEDURE EZEE_SP_PAYMENT_IUD(

	INOUT pcrCode VARCHAR(30),
	IN pitBillingId INT,
	IN pitAmount DECIMAL(10,2),
	IN pitPaymentMode TINYINT,
	IN pcrTransactionRefNo VARCHAR(100),
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

	SELECT next_value INTO litNextValue FROM code_sequence WHERE entity_name="PAYMENT";

	--  CREATE THE UNIQUE CODE

	SET lcrCode=CONCAT("PAY",LPAD(litNextValue,5,'0'));

	--  UPDATE THE CODE_SEQUENCE TABLE'S NEXT_VALUE USING ENTITY NAME FOR NEXT UNIQUE CODE	
	UPDATE code_sequence SET next_value=next_value+1 WHERE entity_name="PAYMENT";
	
	SET pcrCode=lcrCode;
	
-- 	INSERT QUERY FOR PAYMENT TABLE
	INSERT INTO payment (CODE, billing_id, amount, payment_mode, payment_datetime, transaction_ref_no, active_flag, updated_by, updated_at)
	VALUES(pcrCode, pitBillingId, pitAmount, pitPaymentMode, NOW(), pcrTransactionRefNo, pitActiveFlag,pitUpdatedBy,NOW());
	
	SET pitRowCount=ROW_COUNT();

	-- UPDATE PAYMENT TABLE DATA
	ELSEIF(pitActiveFlag=1 AND EZEE_FN_ISNOTNULL(pcrCode))THEN

	UPDATE payment SET billing_id=pitBillingId, amount=PitAmount, payment_mode=PitPaymentMode, payment_datetime=NOW(), updated_by=pitUpdatedBy,
	active_flag=pitActiveFlag,updated_at=NOW() WHERE CODE=pcrCode;
	
	SET pitRowCount=ROW_COUNT();
	
	END IF;
	END// 
DELIMITER ;
