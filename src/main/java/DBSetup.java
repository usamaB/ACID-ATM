

import java.sql.*;
import java.util.Scanner;

public class DBSetup {

    Connection conn = null;
    Statement stmt = null;
    boolean isConcurrentDB;

    public static void main(String[] args) throws SQLException {
        Connection conn = null;
        Statement stmt = null;

        checkDriver();
        conn = getConnection();

        Scanner scanner = new Scanner(System.in);
        ATMTransactions atm = new ATMTransactions();
        int accountId=1;
        int pinCode=1111;

        boolean isLoggedIn = false;
        while (!isLoggedIn) {
            System.out.println("Enter AccountID and PIN");
//            accountId = scanner.nextInt();
//            pinCode = scanner.nextInt();
            isLoggedIn = atm.login(conn, accountId, pinCode);
        }
//
//        EntryPoint test = new EntryPoint(conn,accountId,pinCode);
//        test.TestConcurrency();

//        boolean play = true;
//        while(play){
//
//            System.out.println("Press\n" +
//                    "1) for Show Balance \n" +
//                    "2) for Deposit \n" +
//                    "3) for Withdraw \n" +
//                    "4) for Exit");
//
//            switch(scanner.nextInt()){
//                case 1:
//                    System.out.println("You current balance is " + atm.checkBalance(conn,accountId));
//                    break;
//                case 2:
//                    System.out.println("Enter the amount you want to deposit");
//                    atm.depositAmount(conn,accountId,scanner.nextInt());
//                    break;
//                case 3:
//                    System.out.println("Enter the amount you want to deposit");
//                    atm.withdrawAmount(conn,accountId,scanner.nextInt());
//                    break;
//                case 4:
//                    System.out.println("Exiting...");
//                    atm.close();
//                    return;
//            }
//        }
    }


    public DBSetup(boolean concurrency){
        isConcurrentDB = concurrency;
        checkDriver();
        this.conn = getConnection();
    }


    public void setupDB(){
//        checkDriver();
//        this.conn = getConnection();
        Scanner scanner = new Scanner(System.in);
        ATMTransactions atm = new ATMTransactions();
        int accountId=1;
        int pinCode=1111;

        boolean isLoggedIn = false;
        while (!isLoggedIn) {
            System.out.println("Enter AccountID and PIN");
//            accountId = scanner.nextInt();
//            pinCode = scanner.nextInt();
            isLoggedIn = atm.login(conn, accountId, pinCode);
        }
    }

    private static void checkDriver(){
        System.out.println("Load MySQL JDBC driver");
        try {
            Class.forName(Credentials.JDBC_DRIVER);
        } catch (ClassNotFoundException e) {
            System.out.println("MySQL JDBC Driver not found.");
            e.printStackTrace();
            return;
        }
    }

    public static Connection getConnection(){
        System.out.println("Connecting to database...");
        try {
            return DriverManager.getConnection(Credentials.DB_URL, Credentials.USER, Credentials.PASS);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}