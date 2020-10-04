package kr.ac.kaist.iu1nguoi.java;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Types;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;


public class LibraryManagementSystem {
    static Scanner stdin = new Scanner(System.in);

    static Connection con = null;

    static CallableStatement cs = null;
    
    static final int MILISECONDS_IN_A_DAY = 24*3600*1000;
    
    public static void main(String[] args) {
        int func;

        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            con = DriverManager.getConnection(
                    "server", "username", "password");

            do {
                System.out.println();
                System.out.println("Welcome! Library Management System");
                System.out.println("----------------------------------");
                System.out.println("<Menu>");
                System.out.println("0. Become a member");
                System.out.println("1. Search by title");
                System.out.println("2. Search by author");
                System.out.println("3. Loan");
                System.out.println("4. Return");
                System.out.println("5. Renewal");
                System.out.println("6. Quit");
                System.out.print("Command >> ");
                func = stdin.nextInt();
                stdin.nextLine();
                switch (func) {
                case 0:
                	function0_add_member_PLSQL();
                    break;
                case 1:
                	function1_search_by_title_JDBC_API();
                    break;
                case 2:
                	function2_search_by_author_JDBC_API();
                    break;
                case 3:
                	function3_loan_PLSQL();
                    break;
                case 4:
                	function4_return_PLSQL();
                    break;
                case 5: 
                	function5_renewal_PLSQL();
                    break;
                case 6:
                    break;
                default:
                    System.out.println("Wrong input. Try again!");
                }
            } while (func != 5);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (con != null)
                    con.close();
            } catch (Exception e) {
            }
        }
    }
    
    private static void function1_search_by_title_JDBC_API() {
        // TODO 
        int bookid, bookcopy, floor, loaned;
        Date date;
        String title, author, area;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        boolean exist = false;

        System.out.println("Catalog Explorer - Search by title");
        System.out.print("Title >> ");
        title = stdin.nextLine();

        System.out.println();
        System.out.printf("%-4s  %-4s   %-30s   %-15s  %-7s  %-5s %-7s %-15s %n", "ID", "Copy", "Title", "Author", "Floor", "Area", "Loan#", "Avaiability");
        System.out.println("------------------------------------------------------------");

        try {
            stmt = con.prepareStatement("SELECT bookid, bookcopy, title, author, floor, area, numofloaned, duedate FROM library_catalog NATURAL LEFT OUTER JOIN loan WHERE title like '%" + title + "%' ORDER BY bookid, bookcopy");
            rs = stmt.executeQuery();

            while (rs.next()) {                
                bookid = rs.getInt(1);
                bookcopy = rs.getInt(2);
                title = rs.getString(3);
                author = rs.getString(4);
                floor = rs.getInt(5);
                area = rs.getString(6);
                loaned = rs.getInt(7);
                date = rs.getDate(8);
                System.out.printf("%-4d  %-4d   %-30s   %-15s  %-7d  %-5s %-7d %-15s %n", bookid, bookcopy, title, author, floor, area, loaned, (date == null?"Now Available":(new SimpleDateFormat("YYYY-MM-dd")).format(date)));
                exist = true;
            }
            
            if (!exist) 
                System.out.println("There is no result.");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                if (stmt != null)
                    stmt.close();
            } catch (Exception e) {
                System.out.println(e);
            }
        }
    }
    
    private static void function2_search_by_author_JDBC_API() {
        // TODO 
        int bookid, bookcopy, floor, loaned;
        Date date;
        String title, author, area;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        boolean exist = false;

        System.out.println("Catalog Explorer - Search by author");
        System.out.print("Author >> ");
        author = stdin.nextLine();

        System.out.println();
        System.out.printf("%-4s  %-4s   %-30s   %-15s  %-7s  %-5s %-7s %-15s %n", "ID", "Copy", "Title", "Author", "Floor", "Area", "Loan#", "Avaiability");
        System.out.println("------------------------------------------------------------");

        try {
            stmt = con.prepareStatement("SELECT bookid, bookcopy, title, author, floor, area, numofloaned, duedate FROM library_catalog NATURAL LEFT OUTER JOIN loan WHERE author like '%" + author + "%' ORDER BY bookid, bookcopy");
            rs = stmt.executeQuery();

            while (rs.next()) {                
                bookid = rs.getInt(1);
                bookcopy = rs.getInt(2);
                title = rs.getString(3);
                author = rs.getString(4);
                floor = rs.getInt(5);
                area = rs.getString(6);
                loaned = rs.getInt(7);
                date = rs.getDate(8);
                System.out.printf("%-4d  %-4d   %-30s   %-15s  %-7d  %-5s %-7d %-15s %n", bookid, bookcopy, title, author, floor, area, loaned, (date == null?"Now Available":(new SimpleDateFormat("YYYY-MM-dd")).format(date)));
                exist = true;
            }
            
            if (!exist) 
                System.out.println("There is no result.");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                if (stmt != null)
                    stmt.close();
            } catch (Exception e) {
            }
        }
    }
    
    private static void function3_loan_PLSQL() {
        // TODO Auto-generated method stub
        int mid, bid, copyno;

        System.out.println("Loan book >>");

        System.out.print("Enter Member ID : ");
        mid = stdin.nextInt();
        stdin.nextLine();
        System.out.print("Book ID : ");
        bid = stdin.nextInt();
        stdin.nextLine();
        System.out.print("Copy No. : ");
        copyno = stdin.nextInt();
        stdin.nextLine();

        System.out.println();

        try {
            cs = con.prepareCall("{call loaning(?,?,?)}");
            cs.setInt(1, mid);
            cs.setInt(2, bid);
            cs.setInt(3, copyno);
            cs.execute();
            System.out.println("Success! Please return the book within due date.");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    
    }
    
    
    private static void function3_loan_JDBC_API() {
        // TODO Auto-generated method stub
        int mid, bid, copyno;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        System.out.println("Loan book >>");
        try {
            while (true) {
                System.out.print("Enter Member ID : ");
                mid = stdin.nextInt();
                stdin.nextLine();
                pstmt = con.prepareStatement("SELECT * FROM member WHERE memberid = ?");
                pstmt.setInt(1, mid);
                rs = pstmt.executeQuery();
                if (rs.next())
                    break;
                else
                    System.out.println("Not a member of our library.");
            }
            System.out.print("Book ID : ");
            bid = stdin.nextInt();
            stdin.nextLine();
            System.out.print("Copy No. : ");
            copyno = stdin.nextInt();
            stdin.nextLine();
    
            System.out.println();

            pstmt = con.prepareStatement("SELECT * FROM library_catalog WHERE bookid = ? AND bookcopy = ?");
            pstmt.setInt(1, bid);
            pstmt.setInt(2, copyno);
            rs = pstmt.executeQuery();
            
            if (!rs.next())
                System.out.println("The Book does not exist in catalog.");
            else {
                pstmt = con.prepareStatement("SELECT * FROM loan WHERE bookid = ? AND bookcopy = ?");
                pstmt.setInt(1, bid);
                pstmt.setInt(2, copyno);
                rs = pstmt.executeQuery();
                
                if (rs.next())
                    System.out.println("The book has been loaned already!");
                else {
                    try {
                        con.setAutoCommit(false);
                        pstmt = con.prepareStatement("INSERT INTO loan VALUES(?,?,?,?,?)");
                        pstmt.setInt(1, mid);
                        pstmt.setInt(2, bid);
                        pstmt.setInt(3, copyno);
                        Date d = new Date();
                        pstmt.setDate(4, new java.sql.Date(d.getTime()));
                        pstmt.setDate(5, new java.sql.Date(d.getTime() + 7*MILISECONDS_IN_A_DAY));
                        pstmt.executeUpdate();
                        pstmt = con.prepareStatement("UPDATE library_catalog SET numofloaned = numofloaned+1 WHERE bookid = ? AND bookcopy = ?");
                        pstmt.setInt(1, bid);
                        pstmt.setInt(2, copyno);
                        pstmt.executeUpdate();
                        con.commit();
                        System.out.println("Success! Please return the book within 7 days.");
                    } catch (Exception e) {
                        con.rollback();
                    }
                }
            }
            
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                if (pstmt != null)
                   pstmt.close();
            } catch (Exception e) {
            }
        }
    }
    
    private static void function4_return_PLSQL() {
        // TODO Auto-generated method stub
        int bid, copyno, result;

        System.out.println("Return book >>");

        System.out.print("Book ID : ");
        bid = stdin.nextInt();
        stdin.nextLine();
        System.out.print("Copy No. : ");
        copyno = stdin.nextInt();
        stdin.nextLine();

        System.out.println();

        try {
            cs = con.prepareCall("{? = call returning(?,?)}");
            cs.registerOutParameter(1, Types.NUMERIC);
            cs.setInt(2, bid);
            cs.setInt(3, copyno);
            cs.execute();
            result = cs.getInt(1);
            System.out.println("Late fee is " + result);
            System.out.println("Your book has been returned successfully.");  
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }            
    }
    
    private static void function4_return_JDBC_API() {
        // TODO Auto-generated method stub
        int bid, copyno;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        System.out.println("Return book >>");
        try {
            System.out.print("Book ID : ");
            bid = stdin.nextInt();
            stdin.nextLine();
            System.out.print("Copy No. : ");
            copyno = stdin.nextInt();
            stdin.nextLine();
    
            System.out.println();

            pstmt = con.prepareStatement("SELECT * FROM library_catalog WHERE bookid = ? AND bookcopy = ?");
            pstmt.setInt(1, bid);
            pstmt.setInt(2, copyno);
            rs = pstmt.executeQuery();
            
            if (!rs.next())
                System.out.println("The Book does not exist in catalog.");
            else {
                pstmt = con.prepareStatement("SELECT duedate FROM loan WHERE bookid = ? AND bookcopy = ?");
                pstmt.setInt(1, bid);
                pstmt.setInt(2, copyno);
                rs = pstmt.executeQuery();
                
                if (!rs.next())
                    System.out.println("The book has not been loaned yet!");
                else {
                    Date due = rs.getDate(1);
                    Date current = new Date();
                    pstmt = con.prepareStatement("DELETE FROM loan WHERE bookid = ? AND bookcopy = ?");
                    pstmt.setInt(1, bid);
                    pstmt.setInt(2, copyno);
                    pstmt.executeUpdate();
                    System.out.printf("Due date: %s Today: %s Late fee: %d %n",(new SimpleDateFormat("YYYY-MM-dd")).format(due),(new SimpleDateFormat("YYYY-MM-dd")).format(current),300*(due.getTime()<current.getTime()?(int)((current.getTime() - due.getTime())/ MILISECONDS_IN_A_DAY):0));
                    System.out.println("Your book has been returned successfully.");               
                }
            }
            
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                if (pstmt != null)
                   pstmt.close();
            } catch (Exception e) {
            }
        }
    }
    
    
    private static void function0_add_member_PLSQL() {
        // TODO Auto-generated method stub
        int memid;
        String memname, memdept, memtype;

        System.out.print("Member ID : ");
        memid = stdin.nextInt();
        stdin.nextLine();
        System.out.print("Name : ");
        memname = stdin.nextLine();
        System.out.print("Department : ");
        memdept = stdin.nextLine();
        System.out.print("Type : ");
        memtype = stdin.nextLine();
        try {
            cs = con.prepareCall("{call add_user_information(?,?,?,?)}");
            cs.setInt(1, memid);
            cs.setString(2, memname);
            cs.setString(3, memdept);
            cs.setString(4, memtype);
            cs.execute();
            System.out.println("You joined as library member!");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }        
    }
    
    private static void function5_renewal_PLSQL() {
        // TODO Auto-generated method stub
        int mid, bid, copyno;

        System.out.println("Renewal book >>");

        System.out.print("Enter Member ID : ");
        mid = stdin.nextInt();
        stdin.nextLine();
        System.out.print("Book ID : ");
        bid = stdin.nextInt();
        stdin.nextLine();
        System.out.print("Copy No. : ");
        copyno = stdin.nextInt();
        stdin.nextLine();

        System.out.println();

        try {
            cs = con.prepareCall("{call renewaling(?,?,?)}");
            cs.setInt(1, mid);
            cs.setInt(2, bid);
            cs.setInt(3, copyno);
            cs.execute();
            System.out.println("The due date is successfully extended.");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    
    }
}
