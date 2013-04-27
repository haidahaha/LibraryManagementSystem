CREATE OR REPLACE PROCEDURE renewaling
(memid IN NUMBER, bid IN NUMBER, bcopy IN NUMBER)
IS
	user_valid NUMBER;
	user_defined_error1 EXCEPTION;
	user_defined_error2 EXCEPTION;
	user_defined_error3 EXCEPTION;
	user_defined_error4 EXCEPTION;
	memtype Member.type%TYPE;
	counter NUMBER;
BEGIN
	SELECT COUNT(*) INTO counter FROM Library_Catalog WHERE bookid = bid AND bookcopy = bcopy;
	IF counter = 0
	THEN 
		RAISE user_defined_error1;
	END IF;
	
	SELECT COUNT(*) INTO counter FROM Loan WHERE memberid = memid AND bookid = bid AND bookcopy = bcopy;
	IF counter > 0
	THEN 
		RAISE user_defined_error3;
	END IF;
	
	user_valid := is_user_delinquent(memid);
	
	IF user_valid = 1
	THEN
		RAISE user_defined_error2;
	END IF;
	
	SELECT numofextension INTO user_valid FROM Loan WHERE memberid = memid AND bookid = bid AND bookcopy = bcopy;
	IF user_valid >= 3
	THEN 
		RAISE user_defined_error4;
	END IF;
	
	UPDATE Loan
	SET duedate = duedate + 7
	WHERE memberid = memid AND bookid = bid AND bookcopy = bcopy;
	
	UPDATE Loan
	SET numofextension = numofextension + 1
	WHERE memberid = memid AND bookid = bid AND bookcopy = bcopy;
	
EXCEPTION
	WHEN user_defined_error1 THEN
		RAISE_APPLICATION_ERROR(-20004,'This book is not in Library_Catalog table.');
	WHEN user_defined_error2 THEN
		RAISE_APPLICATION_ERROR(-20005,'This user is a delinquent.');
	WHEN user_defined_error3 THEN
		RAISE_APPLICATION_ERROR(-20007,'There is no loan history with the given member id, book id and book copy.');
	WHEN user_defined_error4 THEN
		RAISE_APPLICATION_ERROR(-20008,'This book has beened extened 3 times.');
END;
/