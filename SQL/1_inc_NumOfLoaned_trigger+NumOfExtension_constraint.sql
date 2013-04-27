CREATE OR REPLACE TRIGGER inc_numofloaned_trigger
AFTER INSERT ON Loan
FOR EACH ROW
BEGIN
	UPDATE Library_Catalog
	SET numofloaned = numofloaned + 1
	WHERE bookid = :new.bookid and bookcopy = :new.bookcopy;
END;
/
ALTER TABLE Loan
ADD CONSTRAINT chk_extension CHECK(numofextension BETWEEN 0 AND 3);
