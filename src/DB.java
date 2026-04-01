import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class DB {
    private static final String URL = "jdbc:sqlite:insurance.db";
    private static boolean initialised = false;

    public static Connection getConnection() throws Exception {
        Connection c = DriverManager.getConnection(URL);
        try (Statement s = c.createStatement()) {
            s.execute("PRAGMA foreign_keys = ON;");
        }
        return c;
    }

    public static void init() {
        if (initialised) return;

        try (Connection c = getConnection(); Statement s = c.createStatement()) {
            s.execute("""
                CREATE TABLE IF NOT EXISTS customers (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    name TEXT NOT NULL,
                    surname TEXT NOT NULL,
                    address TEXT NOT NULL,
                    phone TEXT NOT NULL,
                    email TEXT NOT NULL UNIQUE,
                    password TEXT NOT NULL
                );
            """);

            s.execute("""
                CREATE TABLE IF NOT EXISTS quotes (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    customer_id INTEGER NOT NULL,
                    vehicle_text TEXT NOT NULL,
                    driver_age INTEGER NOT NULL,
                    ncb_years INTEGER NOT NULL,
                    policy_type TEXT NOT NULL,
                    premium REAL NOT NULL,
                    status TEXT NOT NULL,
                    created_at TEXT NOT NULL,
                    FOREIGN KEY (customer_id) REFERENCES customers(id)
                );
            """);

            initialised = true;
        } catch (Exception e) {
            throw new RuntimeException("DB init failed: " + e.getMessage(), e);
        }
    }
}