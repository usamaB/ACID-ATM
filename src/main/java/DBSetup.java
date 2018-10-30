

import java.sql.*;
import java.util.Scanner;

public class DBSetup {
    static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://testdb.cazdtabk6sqg.eu-central-1.rds.amazonaws.com/testdb";
    static final String USER = "sam";
    static final String PASS = "sambutt88";

    public static void main(String[] args) throws SQLException {
        Connection conn = null;
        Statement stmt = null;

        checkDriver();
        conn = getConnection();

        Scanner scanner = new Scanner(System.in);
        ATMTransactions atm = new ATMTransactions();
        System.out.println("Enter AccountID and PIN");

        int accountId = scanner.nextInt();
        int pinCode = scanner.nextInt();
        boolean isLoggedIn = atm.login(conn,accountId,pinCode);

        boolean play = true;
        while(play){

            System.out.println("Press\n" +
                    "1) for Show Balance \n" +
                    "2) for Deposit \n" +
                    "3) for Withdraw \n" +
                    "4) for exit");

            switch(scanner.nextInt()){
                case 1:
                    System.out.println("You current balance is " + atm.checkBalance(conn,accountId));
                    break;
                case 2:
                    System.out.println("Enter the amount you want to deposit");
                    atm.depositAmount(conn,accountId,scanner.nextInt());
                    break;
                case 3:
                    System.out.println("Enter the amount you want to deposit");
                    atm.withdrawAmount(conn,accountId,scanner.nextInt());
                    break;
                case 4:
                    System.out.println("Exiting...");
                    atm.close();
                    return;
            }
        }

//        System.out.println("Creating statement...");
//        stmt = conn.createStatement();
//        String sql;
//        System.out.println("Creating table...");
//        sql = 	"CREATE TABLE IF NOT EXISTS account (account_id INT, pinCode INT, balance DECIMAL, UNIQUE (account_id));";
//        stmt.executeUpdate(sql);
//
//        System.out.println("Inserting values...");
//        sql = "INSERT INTO account VALUES(1, 1111, 50), (2, 2222, 50), (3,3333,50)";
//        stmt.executeUpdate(sql);
//
//        //TRANSACTION DEMARCATION
//        //money transfer: send 10 from account 1 to account 2
//        String update_1 = "UPDATE account SET balance=40 WHERE account_id=1";
//        String update_2 = "UPDATE account SET balance=60 WHERE account_id=2";
//
//        //enable transaction demarcation
//        conn.setAutoCommit(false);
//        stmt = conn.createStatement();
//
//        try {
//            //submit transaction (prepare)
//            System.out.println("Submit demarcated transactions to database");
//            stmt.executeUpdate(update_1);
//            stmt.executeUpdate(update_2);
//
//            //Commit both transactions.
//            System.out.println("Commit demarcated transactions.");
//            conn.commit();
//        } catch (SQLException e) {
//            System.out.println("Rollback demarcated transactions.");
//            conn.rollback();
//        }
//
//        System.out.println("Closing connection...");
//        stmt.close();
    }

    private static void checkDriver(){
        System.out.println("Load MySQL JDBC driver");
        try {
            Class.forName(JDBC_DRIVER);
        } catch (ClassNotFoundException e) {
            System.out.println("MySQL JDBC Driver not found.");
            e.printStackTrace();
            return;
        }
    }

    private static Connection getConnection(){
        System.out.println("Connecting to database...");
        try {
            return DriverManager.getConnection(DB_URL, USER, PASS);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}