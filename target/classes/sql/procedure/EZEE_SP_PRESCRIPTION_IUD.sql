DELIMITER //
CREATE PROCEDURE EZEE_SP_PRESCRIPTION_IUD(

	INOUT pcrCode VARCHAR(30),
	IN pitAppointmentId INT,
	IN pitMedicineId INT,
	IN pcrNotes VARCHAR(200),
	IN pitQuantity INT,
	IN pitActiveFlag INT,
	IN pitUpdatedBy INT,
	OUT pitRowCount INT)
	
	BEGIN
-- 	DECLARE THE VARIABLE FOR FIND UNIQUE VALUE FOR UNIQUE CODE
	DECLARE litNextValue INT;
-- 	DECLARE THE VARIABLE FOR STORE THE UNIQUE CODE
	DECLARE lcrCode VARCHAR(30);
	
	SET pitRowCount=0;
	
	IF(EZEE_FN_ISNULL(pcrCode) AND pitActiveFlag=1)THEN
	
	SELECT next_value INTO litNextValue FROM code_sequence WHERE entity_name="PRESCRIPTION" ;
	
	SET lcrCode=CONCAT("PRESC",LPAD(litNextValue,5,'0'));
	
	--  UPDATE THE CODE_SEQUENCE TABLE'S NEXT_VALUE USING ENTITY NAME FOR NEXT UNIQUE CODE	
	UPDATE code_sequence SET next_value=next_value+1 WHERE entity_name="PRESCRIPTION";
	
	SET pcrCode=lcrCode;
	
	INSERT INTO prescription_item (CODE, appointment_id, medicine_id, notes, quantity, active_flag, updated_by, updated_at)
	VALUES(pcrCode,pitAppointmentId, pitMedicineId, pcrNotes, pitQuantity,pitActiveFlag,pitUpdatedBy,NOW());	
	
	SET pitRowCount=ROW_COUNT();

	-- 	UPDATE QUERY FOR PRESCRIPTION IUD

	ELSEIF(pitActiveFlag=1 AND EZEE_FN_ISNOTNULL(pcrCode))THEN
	
	UPDATE prescription_item SET appointment_id=pitAppointmentId, medicine_id=pitMedicineId, notes=pcrNotes, quantity=pitQuantity,
	active_flag=pitActiveFlag,updated_by=pitUpdatedBy,updated_at=NOW() WHERE CODE= pcrCode AND appointment_id=pitAppointmentId;
	
	SET pitRowCount=ROW_COUNT();
	
	END IF;
	END//
DELIMITER ;