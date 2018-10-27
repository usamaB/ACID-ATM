package com.tuberlin.EC;

import java.sql.*;

public class ATMTransactions {

    public Boolean login(Connection conn, int accountId, int pinCode){
//        Connection conn = getConnection();
        Statement stmt = null;
        String query = "SELECT * FROM account WHERE account_id=" + accountId + " AND pinCode=" + pinCode;
        try {
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            if(rs.next()){
                System.out.println("Login Successful");
                return true;
            } else{
                System.out.println("Invalid Credentials, try again");
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public double checkBalance(Connection conn, int accountId){
//        if(login(accountId,pinCode)){
//            Connection conn = getConnection();
            Statement stmt = null;
            String query = "SELECT balance FROM account WHERE account_id=" + accountId;
            try {
                stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(query);
                return rs.getDouble(1);

            } catch (SQLException e) {
                e.printStackTrace();
            }
//        }
        return -1;
    }

    public void withdrawAmount(Connection conn, int accountId, double amount){
//        Connection conn = getConnection();
        Statement stmt = null;
        String query = null;
//        if(login(accountId,pinCode)){
            try {
                DatabaseMetaData db = conn.getMetaData();
                System.out.println("Transaction Isolation level= " + conn.getTransactionIsolation());
                if(db.supportsTransactionIsolationLevel(Connection.TRANSACTION_SERIALIZABLE))
                    conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);

                query = "SELECT balance FROM account WHERE account_id=" + accountId;

                stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(query);
                rs.next();
                double balance =  rs.getDouble(1);

                String update = null;
                if(balance > amount){
                    update = "Update amount SET balance=" +(balance-amount) + " WHERE account_id=" + accountId;
                }

                //enable transaction demarcation
                conn.setAutoCommit(false);
                stmt = conn.createStatement();

                //submit transaction (prepare)
                System.out.println("Submit demarcated transactions to database");
                stmt.executeUpdate(update);

                query = "SELECT balance FROM account WHERE account_id=" + accountId;
                stmt = conn.createStatement();
                rs = stmt.executeQuery(query);
                rs.next();
                balance =  rs.getDouble(1);
                if(balance<0) {
                    System.out.println("The transaction not complteted, Rolling back");
                    conn.rollback();
                } else {
                    //Commit both transactions.
                    System.out.println("Commit demarcated transactions.");
                    conn.commit();
                }
            } catch (SQLException e){
                e.printStackTrace();
            }
//        }

        return;
    }

    public void depositAmount(Connection conn, int accountId, double amount){
//        Connection conn = getConnection();
        Statement stmt = null;
        String query = null;

//        if(login(accountId,pinCode)){
            try {
                DatabaseMetaData db = conn.getMetaData();
                System.out.println("Transaction Isolation level= " + conn.getTransactionIsolation());
                if(db.supportsTransactionIsolationLevel(Connection.TRANSACTION_SERIALIZABLE))
                    conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);

                query = "SELECT balance FROM account WHERE account_id=" + accountId;

                stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(query);
                rs.next();
                double balance =  rs.getDouble(1);

                String update = null;
//                if(balance > amount){
                update = "Update amount SET balance=" +(balance+amount) + " WHERE account_id=" + accountId;
//                }

                //enable transaction demarcation
//                conn.setAutoCommit(false);
                stmt = conn.createStatement();

                //submit transaction (prepare)
//                System.out.println("Submit demarcated transactions to database");
                stmt.executeUpdate(update);

//                query = "SELECT balance FROM account WHERE account_id=" + accountId;
//                stmt = conn.createStatement();
//                rs = stmt.executeQuery(query);
//                rs.next();
//                balance =  rs.getDouble(1);
//                if(balance<0) {
//                    System.out.println("The transaction not complteted, Rolling back");
//                    conn.rollback();
//                } else {
//                    //Commit both transactions.
//                    System.out.println("Commit demarcated transactions.");
//                    conn.commit();
//                }
            } catch (SQLException e){
                e.printStackTrace();
            }
//        }

        return;
    }
}
