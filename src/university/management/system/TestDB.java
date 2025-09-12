package university.management.system;

import java.sql.*;

public class TestDB {
    public static void main(String[] args) {
        try {
            conn conn = new conn(); // your connection class

            // Run a simple query (example: show tables in database)
            String query = "SHOW TABLES";   // works for MySQL
            Statement stmt = conn.c.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            System.out.println("✅ Connection successful! Tables in your database:");
            while (rs.next()) {
                System.out.println("- " + rs.getString(1));
            }

            conn.c.close(); // close connection
        } catch (Exception e) {
            System.out.println("❌ Connection failed: " + e.getMessage());
        }
    }
}
