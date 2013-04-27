CREATE OR REPLACE PROCEDURE add_user_information
(memid IN NUMBER, memname IN VARCHAR2, memdept IN VARCHAR2, memtype IN VARCHAR2)
IS
	user_define_error EXCEPTION;
BEGIN
	IF memtype NOT IN ('Graduate', 'Undergraduate', 'Professor', 'Staff')
	THEN
		RAISE user_define_error;
	END IF;
	
	INSERT INTO Member VALUES(memid, memname, memdept, memtype);
EXCEPTION
	WHEN DUP_VAL_ON_INDEX THEN
		RAISE_APPLICATION_ERROR(-20000,'Member ID is already in member table.');
	WHEN user_define_error THEN
		RAISE_APPLICATION_ERROR(-20001,'Type must be Graduate, Undergraduate, Professor, or Staff');
END;
/