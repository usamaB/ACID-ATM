
import java.sql.*;

public class ATMTransactions extends Thread{

    enum Transaction{
        DEPOSIT,
        WITHDRAW
    }

    Transaction type;
    Connection conn;
    int accountId;
    int amount;

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
            try {
//                DatabaseMetaData db = conn.getMetaData();
//                if(db.supportsTransactionIsolationLevel(Connection.TRANSACTION_SERIALIZABLE))
//                    conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);

                String query = "SELECT balance FROM account WHERE account_id=" + accountId + " FOR UPDATE" ;
                stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(query);
                rs.next();
                double balance = rs.getDouble(1);
                stmt.close();
                return balance;

            } catch (SQLException e) {
                e.printStackTrace();
            }
        return -1;
    }

    public void withdrawAmount(Connection conn, int accountId, double amount){
        Statement stmt = null;
        String query = null;
            try {
//                DatabaseMetaData db = conn.getMetaData();
//                if(db.supportsTransactionIsolationLevel(Connection.TRANSACTION_SERIALIZABLE))
//                    conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);

                conn.setAutoCommit(false);
                double  balance = checkBalance(conn,accountId);
                System.out.println( Thread.currentThread().getName() + " WITHDRAW STARTED Your Current balance is " + balance);
                String update = null;
                if((balance-amount)<0){

                    System.out.println("Balance less than the withdrawl amount");
                    return;
                }

                update = "Update account SET balance=" +(checkBalance(conn,accountId)-amount) + " WHERE account_id=" + accountId;
                stmt = conn.createStatement();
                stmt.executeUpdate(update);
//
                if(checkBalance(conn,accountId) < 0){
                    conn.rollback();
                    System.out.println("Balance less than then 0. Rolling back");
                    return;
                }
                stmt.close();
                conn.commit();
            } catch (SQLException e){
                e.printStackTrace();
            }
        System.out.println( Thread.currentThread().getName() + " WITHDRAW FINISHED Your Current balance is " + checkBalance(conn,accountId));
        return;
    }

    public void depositAmount(Connection conn, int accountId, double amount){
        System.out.println( Thread.currentThread().getName() + " DEPOSIT STARTED Your Current balance is " + checkBalance(conn,accountId));
        Statement stmt = null;
        String query = null;

            try {
//                DatabaseMetaData db = conn.getMetaData();
//                if(db.supportsTransactionIsolationLevel(Connection.TRANSACTION_SERIALIZABLE))
//                    conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
                conn.setAutoCommit(false);
                stmt = conn.createStatement();
                double  balance = checkBalance(conn,accountId);
                String update = null;
                update = "UPDATE account SET balance=" +(checkBalance(conn,accountId)+amount) + " WHERE account_id=" + accountId;
                stmt = conn.createStatement();
                stmt.executeUpdate(update);
                stmt.close();
                conn.commit();
            } catch (SQLException e){
                e.printStackTrace();
            }
        System.out.println( Thread.currentThread().getName() + " DEPOSIT FINISED Your Current balance is " + checkBalance(conn,accountId));
        return;
    }

    public void close(){
        System.exit(0);
    }


    /*
    Thread Functions
    */
    public void start(Transaction type, int account, int amount, Connection conn){
        this.type = type;
        this.accountId = account;
        this.amount = amount;
        this.conn = conn;
        this.start();
    }

    @Override
    public void run() {
        if(type == Transaction.DEPOSIT) {
                System.out.println(Thread.currentThread().getName() + " Begins to Deposit Amount: " + this.amount);
                depositAmount(this.conn, this.accountId, this.amount);
                System.out.println(Thread.currentThread().getName() + " Final balance : " + checkBalance(this.conn, this.accountId));
        } else {
                System.out.println(Thread.currentThread().getName()+" Begins to Withdraw Amount: "+this.amount);
                withdrawAmount(this.conn,this.accountId,this.amount);
                System.out.println(Thread.currentThread().getName()+" Final balance : "+checkBalance(this.conn,this.accountId));

        }
    }
}
