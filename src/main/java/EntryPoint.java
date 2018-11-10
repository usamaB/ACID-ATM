
import java.sql.Connection;
import java.util.concurrent.*;

public class EntryPoint {
    private final int THREAD_POOL_SIZE = 5;
    Connection conn;
    ATMTransactions atm;
    int accountID;
    int pin;

    public void TestConcurrency() {
        Connection conn = DBSetup.getConnection();
        for(int i = 0 ; i<THREAD_POOL_SIZE ;i++) {
//            System.out.println("\n"+Thread.currentThread().getName()+" Starts.");
            new ATMTransactions().start(ATMTransactions.Transaction.WITHDRAW,1, 30, conn);
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args){

        // Change this boolean to test normal atm
        boolean concurrencyTest = true;

        DBSetup db = new DBSetup(concurrencyTest);
        new EntryPoint().TestConcurrency();


    }
}
