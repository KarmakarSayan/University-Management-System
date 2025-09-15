package university.management.system;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;

public class PaymentGateway extends JFrame implements ActionListener {

    private String studentId, name, branch, semester, amount;
    private JButton payButton, backButton;

    // --- NEW: Components are now instance variables for validation ---
    private JTabbedPane tabbedPane;
    private JTextField netBankingUserField, upiIdField, cardNoField, cardExpiryField, cardNameField;
    private JPasswordField netBankingPassField, cardCvvField;

    public PaymentGateway(String studentId, String name, String branch, String semester, String amount) {
        this.studentId = studentId;
        this.name = name;
        this.branch = branch;
        this.semester = semester;
        this.amount = amount;

        // --- FRAME SETUP ---
        setTitle("Payment Gateway");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // --- HEADER ---
        JPanel headerPanel = new JPanel();
        headerPanel.add(new JLabel("Choose a Payment Option"));
        add(headerPanel, BorderLayout.NORTH);

        // --- PAYMENT OPTIONS TABS ---
        tabbedPane = new JTabbedPane();

        tabbedPane.addTab("Net Banking", createTabIcon("icons/netbanking.png"), createNetBankingPanel());
        tabbedPane.addTab("UPI", createTabIcon("icons/upi.png"), createUpiPanel());
        tabbedPane.addTab("Debit/Credit Card", createTabIcon("icons/card.png"), createCardPanel());
        
        add(tabbedPane, BorderLayout.CENTER);
        
        // --- BUTTONS ---
        JPanel buttonPanel = new JPanel();
        payButton = new JButton("Pay ₹" + amount);
        payButton.addActionListener(this);
        backButton = new JButton("Back");
        backButton.addActionListener(this);
        buttonPanel.add(payButton);
        buttonPanel.add(backButton);
        add(buttonPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    // --- Helper methods to create each payment panel with a proper layout ---

    private JPanel createNetBankingPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0; gbc.anchor = GridBagConstraints.EAST;
        panel.add(new JLabel("Select Bank:"), gbc);
        gbc.gridx = 1; gbc.anchor = GridBagConstraints.WEST; gbc.weightx = 1.0;
        panel.add(new JComboBox<>(new String[]{"State Bank of India", "HDFC Bank", "ICICI Bank", "Axis Bank"}), gbc);

        gbc.gridx = 0; gbc.gridy = 1; gbc.anchor = GridBagConstraints.EAST; gbc.weightx = 0;
        panel.add(new JLabel("Username:"), gbc);
        gbc.gridx = 1; gbc.anchor = GridBagConstraints.WEST;
        netBankingUserField = new JTextField(); // Assign to instance variable
        panel.add(netBankingUserField, gbc);

        gbc.gridx = 0; gbc.gridy = 2; gbc.anchor = GridBagConstraints.EAST;
        panel.add(new JLabel("Password:"), gbc);
        gbc.gridx = 1; gbc.anchor = GridBagConstraints.WEST;
        netBankingPassField = new JPasswordField(); // Assign to instance variable
        panel.add(netBankingPassField, gbc);
        
        gbc.gridy = 3; gbc.weighty = 1.0;
        panel.add(new JLabel(), gbc);

        return panel;
    }

    private JPanel createUpiPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0; gbc.anchor = GridBagConstraints.EAST;
        panel.add(new JLabel("Enter UPI ID:"), gbc);
        gbc.gridx = 1; gbc.anchor = GridBagConstraints.WEST; gbc.weightx = 1.0;
        upiIdField = new JTextField(); // Assign to instance variable
        panel.add(upiIdField, gbc);

        gbc.gridx = 1; gbc.gridy = 1; gbc.anchor = GridBagConstraints.NORTHWEST;
        JLabel exampleLabel = new JLabel("e.g., username@okhdfcbank");
        exampleLabel.setForeground(Color.GRAY);
        panel.add(exampleLabel, gbc);

        gbc.gridy = 2; gbc.weighty = 1.0;
        panel.add(new JLabel(), gbc);

        return panel;
    }

    private JPanel createCardPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0; gbc.anchor = GridBagConstraints.EAST;
        panel.add(new JLabel("Card Number:"), gbc);
        gbc.gridx = 1; gbc.anchor = GridBagConstraints.WEST; gbc.weightx = 1.0;
        cardNoField = new JTextField(); // Assign to instance variable
        panel.add(cardNoField, gbc);

        gbc.gridx = 0; gbc.gridy = 1; gbc.anchor = GridBagConstraints.EAST; gbc.weightx = 0;
        panel.add(new JLabel("Expiry (MM/YY):"), gbc);
        gbc.gridx = 1; gbc.anchor = GridBagConstraints.WEST;
        cardExpiryField = new JTextField(); // Assign to instance variable
        panel.add(cardExpiryField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 2; gbc.anchor = GridBagConstraints.EAST;
        panel.add(new JLabel("CVV:"), gbc);
        gbc.gridx = 1; gbc.anchor = GridBagConstraints.WEST;
        cardCvvField = new JPasswordField(); // Assign to instance variable
        panel.add(cardCvvField, gbc);

        gbc.gridx = 0; gbc.gridy = 3; gbc.anchor = GridBagConstraints.EAST;
        panel.add(new JLabel("Name on Card:"), gbc);
        gbc.gridx = 1; gbc.anchor = GridBagConstraints.WEST;
        cardNameField = new JTextField(); // Assign to instance variable
        panel.add(cardNameField, gbc);

        gbc.gridy = 4; gbc.weighty = 1.0;
        panel.add(new JLabel(), gbc);
        
        return panel;
    }
    
    // --- NEW: Validation method for payment details ---
    private boolean validatePaymentDetails() {
        int selectedIndex = tabbedPane.getSelectedIndex();
        switch (selectedIndex) {
            case 0: // Net Banking
                if (netBankingUserField.getText().trim().isEmpty() || new String(netBankingPassField.getPassword()).trim().isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Please enter Username and Password.", "Validation Error", JOptionPane.ERROR_MESSAGE);
                    return false;
                }
                break;
            case 1: // UPI
                if (upiIdField.getText().trim().isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Please enter your UPI ID.", "Validation Error", JOptionPane.ERROR_MESSAGE);
                    return false;
                }
                break;
            case 2: // Card
                if (cardNoField.getText().trim().isEmpty() || 
                    cardExpiryField.getText().trim().isEmpty() || 
                    new String(cardCvvField.getPassword()).trim().isEmpty() || 
                    cardNameField.getText().trim().isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Please fill all card details.", "Validation Error", JOptionPane.ERROR_MESSAGE);
                    return false;
                }
                break;
        }
        return true;
    }
    
    private ImageIcon createTabIcon(String path) {
        try {
            URL imageUrl = ClassLoader.getSystemResource(path);
            if (imageUrl != null) {
                return new ImageIcon(new ImageIcon(imageUrl).getImage().getScaledInstance(20, 20, Image.SCALE_DEFAULT));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == payButton) {
            if (validatePaymentDetails()) {
                try {
                    conn c = new conn();
                    
                    String courseQuery = "SELECT course FROM students WHERE student_id = ?";
                    PreparedStatement coursePstmt = c.c.prepareStatement(courseQuery);
                    coursePstmt.setString(1, studentId);
                    
                    String studentCourse = "";
                    ResultSet rs = coursePstmt.executeQuery();
                    if (rs.next()) {
                        studentCourse = rs.getString("course");
                    }

                    String query = "INSERT INTO student_fee (student_id, course, branch, semester, total_amount, payment_date) VALUES (?, ?, ?, ?, ?, ?)";
                    PreparedStatement pstmt = c.c.prepareStatement(query);
                    pstmt.setString(1, studentId);
                    pstmt.setString(2, studentCourse);
                    pstmt.setString(3, branch);
                    pstmt.setString(4, semester);
                    pstmt.setInt(5, Integer.parseInt(amount));
                    pstmt.setDate(6, java.sql.Date.valueOf(LocalDate.now()));
                    
                    pstmt.executeUpdate();

                    // --- NEW: Payment success confirmation and receipt option ---
                    int dialogResult = JOptionPane.showConfirmDialog(
                            this,
                            "Fee Paid Successfully! Would you like to print a receipt?",
                            "Payment Successful",
                            JOptionPane.YES_NO_OPTION
                    );

                    if (dialogResult == JOptionPane.YES_OPTION) {
                        new GenerateFeeReceipt(studentId, name, studentCourse, branch, semester, amount);
                    }
                    
                    this.dispose();

                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(this, "Error processing payment: " + ex.getMessage(), "Payment Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        } else if (e.getSource() == backButton) {
            new StudentFeeForm();
            this.dispose();
        }
    }
}