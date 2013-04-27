CREATE OR REPLACE FUNCTION returning
(bid IN NUMBER, bcopy IN NUMBER)
RETURN NUMBER
IS
	user_defined_error1 EXCEPTION;
	counter NUMBER;
BEGIN
	SELECT COUNT(*) INTO counter FROM Library_Catalog WHERE bookid = bid AND bookcopy = bcopy;
	IF counter = 0
	THEN 
		RAISE user_defined_error1;
	END IF;
	
	counter := estimate_late_fee(bid, bcopy);
	
	DELETE FROM Loan
	WHERE bookid = bid AND bookcopy = bcopy;
	
	RETURN counter;
	
EXCEPTION
	WHEN user_defined_error1 THEN
		RAISE_APPLICATION_ERROR(-20004,'This book is not in Library_Catalog table.');
END;
/