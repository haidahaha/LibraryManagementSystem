CREATE OR REPLACE PROCEDURE loaning
(memid IN NUMBER, bid IN NUMBER, bcopy IN NUMBER)
IS
	user_valid NUMBER;
	user_defined_error1 EXCEPTION;
	user_defined_error2 EXCEPTION;
	user_defined_error3 EXCEPTION;
	memtype Member.type%TYPE;
	counter NUMBER;
BEGIN
	SELECT COUNT(*) INTO counter FROM Library_Catalog WHERE bookid = bid AND bookcopy = bcopy;
	IF counter = 0
	THEN 
		RAISE user_defined_error1;
	END IF;
	
	SELECT COUNT(*) INTO counter FROM Loan WHERE bookid = bid AND bookcopy = bcopy;
	IF counter > 0
	THEN 
		RAISE user_defined_error3;
	END IF;
	
	user_valid := is_user_delinquent(memid);
	
	IF user_valid = 1
	THEN
		RAISE user_defined_error2;
	END IF;
	
	SELECT type INTO memtype FROM Member WHERE memberid = memid;
	IF memtype = 'Undergraduate'
	THEN
		INSERT INTO Loan VALUES(memid, bid, bcopy, sysdate, sysdate+7, 0);
	ELSE
		INSERT INTO Loan VALUES(memid, bid, bcopy, sysdate, sysdate+14, 0);
	END IF;
EXCEPTION
	WHEN user_defined_error1 THEN
		RAISE_APPLICATION_ERROR(-20004,'This book is not in Library_Catalog table.');
	WHEN user_defined_error2 THEN
		RAISE_APPLICATION_ERROR(-20005,'This user is a delinquent.');
	WHEN user_defined_error3 THEN
		RAISE_APPLICATION_ERROR(-20006,'This book is already loaned by other member.');
END;
/