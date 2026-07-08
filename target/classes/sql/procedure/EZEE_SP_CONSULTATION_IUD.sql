--  CONSULTATION TABLE IUD
DELIMITER //
CREATE PROCEDURE EZEE_SP_CONSULTATION_IUD(

	INOUT pcrCode VARCHAR(30),
	IN pitAppointmentId INT,
	IN pcrChiefComplaint VARCHAR(200),
	IN pcrDiagnosis VARCHAR(100),
	IN pcrDoctorNotes TEXT,
	IN pdtConsultationDateTime DATETIME,
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
	
	UPDATE consultations SET appointment_id=pitAppointmentId, chief_complaint=pcrChiefComplaint, 
	diagnosis=pcrDiagnosis,doctor_notes=pcrDoctorNotes,consultation_datetime=pdtConsultationDateTime, 
	active_flag=pitActiveFlag, updated_by=pitUpdatedBy,updated_at=NOW() WHERE CODE = pcrCode;
	
	SET pitRowCount=ROW_COUNT();

	END IF;
	END//
DELIMITER ; 