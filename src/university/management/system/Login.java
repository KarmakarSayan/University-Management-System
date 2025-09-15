package university.management.system;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class Login extends JFrame implements ActionListener {

    JButton loginButton, cancelButton;
    JTextField usernameField;
    JPasswordField passwordField;
    JComboBox<String> userTypeComboBox;

    public Login() {
        // --- FRAME SETUP ---
        setTitle("University Login");
        setSize(600, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        // --- Background Image ---
        ImageIcon i1 = new ImageIcon(ClassLoader.getSystemResource("icons/loginback.png"));
        Image i2 = i1.getImage().getScaledInstance(600, 300, Image.SCALE_DEFAULT);
        ImageIcon i3 = new ImageIcon(i2);
        JLabel background = new JLabel(i3);
        background.setLayout(null); // Set layout to null to place components on top
        add(background);

        // --- COMPONENTS ---
        // --- UPDATED: Increased font size and changed color to black ---
        Font labelFont = new Font("Tahoma", Font.BOLD, 14);

        JLabel usernameLabel = new JLabel("Username");
        usernameLabel.setBounds(40, 20, 100, 20);
        usernameLabel.setFont(labelFont);
        usernameLabel.setForeground(Color.BLACK); // Set text color to black
        background.add(usernameLabel);

        usernameField = new JTextField();
        usernameField.setBounds(150, 20, 150, 25);
        background.add(usernameField);

        JLabel passwordLabel = new JLabel("Password");
        passwordLabel.setBounds(40, 70, 100, 20);
        passwordLabel.setFont(labelFont);
        passwordLabel.setForeground(Color.BLACK); // Set text color to black
        background.add(passwordLabel);

        passwordField = new JPasswordField();
        passwordField.setBounds(150, 70, 150, 25);
        background.add(passwordField);
        
        JLabel userTypeLabel = new JLabel("Login As");
        userTypeLabel.setBounds(40, 120, 100, 20);
        userTypeLabel.setFont(labelFont);
        userTypeLabel.setForeground(Color.BLACK); // Set text color to black
        background.add(userTypeLabel);
        
        userTypeComboBox = new JComboBox<>(new String[]{"Teacher", "Student"});
        userTypeComboBox.setBounds(150, 120, 150, 25);
        userTypeComboBox.setBackground(Color.WHITE);
        background.add(userTypeComboBox);

        loginButton = new JButton("Login");
        loginButton.setBounds(40, 180, 120, 30);
        loginButton.setBackground(Color.BLACK);
        loginButton.setForeground(Color.WHITE);
        loginButton.addActionListener(this);
        background.add(loginButton);

        cancelButton = new JButton("Cancel");
        cancelButton.setBounds(180, 180, 120, 30);
        cancelButton.setBackground(Color.BLACK);
        cancelButton.setForeground(Color.WHITE);
        cancelButton.addActionListener(this);
        background.add(cancelButton);
        
        // --- NEW: Added the second.png image back to the right side ---
        ImageIcon i4 = new ImageIcon(ClassLoader.getSystemResource("icons/second.png"));
        Image i5 = i4.getImage().getScaledInstance(150, 150, Image.SCALE_DEFAULT);
        ImageIcon i6 = new ImageIcon(i5);
        JLabel image = new JLabel(i6);
        image.setBounds(380, 40, 150, 150);
        background.add(image);

        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == loginButton) {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());
            String userType = (String) userTypeComboBox.getSelectedItem();

            try {
                conn c = new conn();
                String query = "";
                
                if ("Teacher".equals(userType)) {
                    query = "SELECT * FROM teachers WHERE faculty_id = ? AND faculty_id = ?";
                } else if ("Student".equals(userType)) {
                    query = "SELECT * FROM students WHERE student_id = ? AND student_id = ?";
                }
                
                PreparedStatement pstmt = c.c.prepareStatement(query);
                pstmt.setString(1, username);
                pstmt.setString(2, password);

                ResultSet rs = pstmt.executeQuery();

                if (rs.next()) {
                    setVisible(false);
                    new main_class();
                } else {
                    JOptionPane.showMessageDialog(this, "Invalid username or password", "Login Error", JOptionPane.ERROR_MESSAGE);
                }

            } catch (Exception ex) {
                ex.printStackTrace();
            }

        } else if (e.getSource() == cancelButton) {
            setVisible(false);
            System.exit(0);
        }
    }
    
    public static void main(String[] args) {
        new Login();
    }
}

