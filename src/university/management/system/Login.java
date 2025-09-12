package university.management.system;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;  // Needed for ResultSet, SQLException

public class Login extends JFrame implements ActionListener {
    JTextField textField;
    JPasswordField passwordField;
    JButton loginButton;
    JButton backButton;

    Login() {
        // Username label
        JLabel labelName = new JLabel("Username");
        labelName.setBounds(50, 50, 100, 30);
        add(labelName);

        // Username field
        textField = new JTextField();
        textField.setBounds(150, 50, 150, 30);
        add(textField);

        // Password label
        JLabel labelPassword = new JLabel("Password");
        labelPassword.setBounds(50, 100, 100, 30);
        add(labelPassword);

        // Password field
        passwordField = new JPasswordField();
        passwordField.setBounds(150, 100, 150, 30);
        add(passwordField);

        // Login button
        loginButton = new JButton("Login");
        loginButton.setBounds(50, 150, 100, 30);
        loginButton.setBackground(Color.black);
        loginButton.setForeground(Color.white);
        loginButton.setOpaque(true);
        loginButton.setBorderPainted(false);
        loginButton.addActionListener(this);
        add(loginButton);

        // Back button
        backButton = new JButton("Back");
        backButton.setBounds(200, 150, 100, 30);
        backButton.setBackground(Color.black);
        backButton.setForeground(Color.white);
        backButton.setOpaque(true);
        backButton.setBorderPainted(false);
        backButton.addActionListener(this);
        add(backButton);

        // Icon
        ImageIcon i1 = new ImageIcon(ClassLoader.getSystemResource("icons/second.png"));
        Image i2 = i1.getImage().getScaledInstance(200, 200, Image.SCALE_DEFAULT);
        JLabel image = new JLabel(new ImageIcon(i2));
        image.setBounds(350, 20, 200, 200);
        add(image);

        // Background
        ImageIcon i4 = new ImageIcon(ClassLoader.getSystemResource("icons/loginback.png"));
        Image i5 = i4.getImage().getScaledInstance(580, 300, Image.SCALE_DEFAULT);
        JLabel background = new JLabel(new ImageIcon(i5));
        background.setBounds(0, 0, 580, 300);
        add(background);

        getContentPane().setBackground(Color.white);
        setTitle("Login Page");

        setSize(580, 300);
        setLocationRelativeTo(null);
        setLayout(null);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == loginButton) {
            String username = textField.getText();
            String password = new String(passwordField.getPassword());

            String query = "select * from login where username='" + username + "' and password='" + password + "'";
            try {
                conn c1 = new conn();   // ✅ Make sure your class is named conn (or Conn)
                ResultSet rs = c1.s.executeQuery(query);

                if (rs.next()) {
                    // this is where you go to the next window
                    setVisible(false);
                    JOptionPane.showMessageDialog(null, "✅ Login Successful!");
                    new main_class();
                } else {
                    JOptionPane.showMessageDialog(null, "❌ Invalid username or password");
                    new Login();
                }

                System.out.println("Login attempted with Username: " + username + " and Password: " + password);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } else if (e.getSource() == backButton) {
            // Go back to the previous window
            setVisible(false);
            new splash(); // Make sure Splash class exists
        }
    }

    public static void main(String[] args) {
        new Login();
        // new conn(); // Test DB connection
        // new TestDB(); // Run DB test
    }
}
