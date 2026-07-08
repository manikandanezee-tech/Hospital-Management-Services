DELIMITER //
CREATE PROCEDURE `EZEE_SP_BILLING_IUD`(

	INOUT pcrCode VARCHAR(30),
	IN pitNamespaceId INT,
	IN pitAppointmentId INT,
	IN pitTotalAmount DECIMAL(10,2),
	IN pitPaidAmount DECIMAL(10,2),
	IN pitBalanceAmount DECIMAL(10,2),
	IN pitBillingStatus INT,
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
	VALUES(pcrCode,pitNamespaceId,pitAppointmentId,NOW(),pitTotalAmount,pitPaidAmount,pitBalanceAmount,pitBillingStatus,pitActiveFlag,pitUpdatedBy,NOW());

	SET pitRowCount=ROW_COUNT();

	-- UPDATE BILLING TABLE DATA

	ELSEIF(pitActiveFlag=1 AND EZEE_FN_ISNOTNULL(pcrCode))THEN

	UPDATE billing SET namespace_id=pitNamespaceId,appointment_id=pitAppointmentId, bill_datetime=NOW(),total_amount= pitTotalAmount,paid_amount=pitPaidAmount,
	balance_amount=pitBalanceAmount,active_flag=pitActiveFlag, billing_status=pitBillingStatus, updated_by=pitUpdatedBy,updated_at=NOW() WHERE CODE = pcrCode;

	SET pitRowCount=ROW_COUNT();

	END IF;
	END//
DELIMITER ;