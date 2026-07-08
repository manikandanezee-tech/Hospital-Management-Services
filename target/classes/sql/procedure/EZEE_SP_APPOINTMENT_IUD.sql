		-- 	APPOINTMENT TABLE INSERT DATA
DELIMITER //

CREATE PROCEDURE EZEE_SP_APPOINTMENT_IUD(

	INOUT pcrCode VARCHAR(30),
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
	
	INSERT INTO appointment (CODE,patient_id,doctor_id,namespace_id,appointment_datetime,token_number,STATUS,active_flag,updated_by,updated_at) 
	VALUES(pcrCode,pitPatientId,pitDoctorId,pitNamespaceId,pdtAppointmentDateTime,pitTokenNumber,pitStatus,pitActiveFlag,pitUpdatedBy,NOW());
	
	SET pitRowCount=ROW_COUNT();
	
-- UPDATE APPOINTMENT TABLE DATA
	ELSEIF(pitActiveFlag=1 AND EZEE_FN_ISNOTNULL(pcrCode))THEN

	UPDATE appointment SET patient_id=pitPatientId, doctor_id=pitDoctorId, appointment_datetime=pdtAppointmentDateTime, 
	token_number=pitTokenNumber, STATUS=pitStatus, active_flag=pitActiveFlag, updated_by=pitUpdatedBy,updated_at=NOW() WHERE CODE=pcrCode AND namespace_id=pitNamespaceId;
	
	SET pitRowCount=ROW_COUNT();

	END IF;
	END//
DELIMITER;