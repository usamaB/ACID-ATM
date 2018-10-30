

import java.sql.*;

public class ATMTransactions {

    public Boolean login(Connection conn, int accountId, int pinCode){
        Statement stmt = null;
        String query = "SELECT * FROM account WHERE account_id=" + accountId + " AND pinCode=" + pinCode;
        try {
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            if(rs.next()){
                System.out.println("Login Successful");
                stmt.close();
                return true;
            } else{
                System.out.println("Invalid Credentials, try again");
                stmt.close();
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public double checkBalance(Connection conn, int accountId){
            Statement stmt = null;
            String query = "SELECT balance FROM account WHERE account_id=" + accountId;
            try {
                stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(query);
                rs.next();
                stmt.close();
                return rs.getDouble(1);

            } catch (SQLException e) {
                e.printStackTrace();
            }
        return -1;
    }

    public void withdrawAmount(Connection conn, int accountId, double amount){
        Statement stmt = null;
        String query = null;
            try {
                DatabaseMetaData db = conn.getMetaData();
                if(db.supportsTransactionIsolationLevel(Connection.TRANSACTION_SERIALIZABLE))
                    conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);

                query = "SELECT balance FROM account WHERE account_id=" + accountId + " FOR UPDATE";

                stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(query);
                rs.next();
                double balance =  rs.getDouble(1);

                String update = null;
                if(balance < amount){

                    System.out.println("Balance less than the withdrawl amount");
                    return;
                }

                update = "Update account SET balance=" +(balance-amount) + " WHERE account_id=" + accountId;
                conn.setAutoCommit(false);
                stmt = conn.createStatement();
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
                stmt.close();
            } catch (SQLException e){
                e.printStackTrace();
            }
        return;
    }

    public void depositAmount(Connection conn, int accountId, double amount){
        Statement stmt = null;
        String query = null;

            try {
                DatabaseMetaData db = conn.getMetaData();
                if(db.supportsTransactionIsolationLevel(Connection.TRANSACTION_SERIALIZABLE))
                    conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);

                query = "SELECT balance FROM account WHERE account_id=" + accountId + " FOR UPDATE";

                conn.setAutoCommit(false);
                stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(query);
                rs.next();
                double balance =  rs.getDouble(1);

                String update = null;
                update = "UPDATE account SET balance=" +(balance+amount) + " WHERE account_id=" + accountId;
                stmt = conn.createStatement();
                stmt.executeUpdate(update);
                stmt.close();
            } catch (SQLException e){
                e.printStackTrace();
            }
        return;
    }

    public void close(){
        System.exit(0);
    }
}
