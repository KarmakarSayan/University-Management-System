package university.management.system;

import java.sql.*;

public class conn {
    Connection c;
    Statement s;

    public conn() {
        try {
            // Load MySQL driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Connect to MySQL (adjust DB name, username, password)
            c = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/universitymanagement",
                "root",           // your MySQL username
                "12345"   // your MySQL password
            );

            s = c.createStatement();
            System.out.println("✅ Database connected successfully!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
