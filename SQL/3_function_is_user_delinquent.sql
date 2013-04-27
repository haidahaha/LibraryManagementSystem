CREATE OR REPLACE FUNCTION is_user_delinquent
(memid IN NUMBER)
RETURN NUMBER
IS
	rentdate Loan.duedate%TYPE;
	user_defined_error EXCEPTION;
	counter NUMBER;
BEGIN
	SELECT COUNT(*) INTO counter FROM Member WHERE memberid = memid;
	IF counter = 0
	THEN 
		RAISE user_defined_error;
	END IF;
	
	rentdate := sysdate;
	
	SELECT MIN(duedate) INTO rentdate
		FROM Loan
		WHERE memberid = memid;
		
	IF (trunc(sysdate) - trunc(rentdate)) > 0 
	THEN 
		RETURN 1; 
	ELSE 
		RETURN 0; 
	END IF;
EXCEPTION
	WHEN user_defined_error THEN
		RAISE_APPLICATION_ERROR(-20002,'This user is not in MEMBER table.');
END;
/