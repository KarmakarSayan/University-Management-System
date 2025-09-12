package university.management.system;

import javax.swing.*;

public class main_class extends JFrame {  // extend JFrame
    main_class() {
        // new splash();
        setSize(500, 500);
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // good practice
    }

    public static void main(String[] args) {
        new main_class();
        // new splash();
        // new Login();
        // new conn(); // Test DB connection
        // new TestDB(); // Run DB test
    }
}
