CREATE OR REPLACE FUNCTION estimate_late_fee
(bid IN NUMBER, bcopy IN NUMBER)
RETURN NUMBER
IS
	rentdate Loan.duedate%TYPE;
	latedate NUMBER;
	user_defined_error EXCEPTION;
	counter NUMBER;
BEGIN
	SELECT COUNT(*) INTO counter FROM Loan WHERE bookid = bid AND bookcopy = bcopy;
	IF counter = 0
	THEN 
		RAISE user_defined_error;
	END IF;
	
	SELECT duedate INTO rentdate
		FROM Loan
		WHERE bookid = bid AND bookcopy = bcopy;
	
	latedate := trunc(sysdate) - trunc(rentdate);	
	RETURN latedate*300; 
	
EXCEPTION
	WHEN NO_DATA_FOUND THEN
		RETURN 0;
	WHEN user_defined_error THEN
		RAISE_APPLICATION_ERROR(-20003,'This book is not in Loan table.');
END;
/