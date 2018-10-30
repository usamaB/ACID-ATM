import java.sql.Connection;

public class ConcurrencyTest extends Thread {
    Connection conn;

    public ConcurrencyTest(Connection conn){
        this.conn = conn;
    }

    @Override
    public void run() {
        for (int i = 1; i <= 5; ++i) {
            System.out.println(conn.toString() + ": " + i);
            yield();
        }
    }
}
