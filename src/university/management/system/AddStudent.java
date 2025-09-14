package university.management.system;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Random;

public class AddStudent extends JFrame implements ActionListener {

    JTextField nameField, fathersNameField, addressField, phoneField, emailField, classXField, classXIIField, aadharField, dobField;
    JLabel rollNumberLabelValue;
    JComboBox<String> courseComboBox, branchComboBox;
    JButton submitButton, cancelButton, uploadButton;
    JLabel photoPreviewLabel;
    
    // --- NEW: Variable to store the path of the uploaded photo ---
    private String photoPath = "";

    // --- Helper class for the rounded border ---
    private static class RoundedBorder extends javax.swing.border.AbstractBorder {
        private int radius; private Color color;
        RoundedBorder(Color color, int radius) { this.radius = radius; this.color = color; }
        @Override public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(color); g2.drawRoundRect(x, y, width - 1, height - 1, radius, radius); g2.dispose();
        }
    }

    public AddStudent() {
        // --- FRAME SETUP ---
        setTitle("New Student Details");
        setSize(1050, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(false);
        setLayout(new BorderLayout());
        getContentPane().setBackground(new Color(240, 248, 255));

        // --- HEADER ---
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(240, 248, 255));
        JLabel headerLabel = new JLabel("New Student Details");
        headerLabel.setFont(new Font("Serif", Font.BOLD, 28));
        headerPanel.add(headerLabel);
        add(headerPanel, BorderLayout.NORTH);

        // --- Main Content Panel ---
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(new Color(240, 248, 255));
        add(contentPanel, BorderLayout.CENTER);

        // --- FORM PANEL ---
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(new Color(240, 248, 255));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        contentPanel.add(formPanel, BorderLayout.CENTER);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // --- Initialize components ---
        nameField = new JTextField();
        fathersNameField = new JTextField();
        rollNumberLabelValue = new JLabel("" + Math.abs(new Random().nextInt() % 10000));
        rollNumberLabelValue.setFont(new Font("Tahoma", Font.BOLD, 16));
        dobField = new JTextField();
        addressField = new JTextField();
        phoneField = new JTextField();
        emailField = new JTextField();
        classXField = new JTextField();
        classXIIField = new JTextField();
        aadharField = new JTextField();
        courseComboBox = new JComboBox<>(new String[]{"B.Tech", "BBA", "BCA", "Bsc", "Msc", "MBA", "MCA", "MCom", "MA", "BA"});
        branchComboBox = new JComboBox<>(new String[]{"Computer Science", "Electronics", "Mechanical", "Civil", "IT"});

        // --- Add components to form ---
        addLabelAndField("Name", 0, 0, formPanel, gbc, nameField);
        addLabelAndField("Father's Name", 0, 2, formPanel, gbc, fathersNameField);
        addLabelAndField("Roll Number", 1, 0, formPanel, gbc, rollNumberLabelValue);
        addLabelAndField("Date of Birth (YYYY-MM-DD)", 1, 2, formPanel, gbc, dobField);
        addLabelAndField("Address", 2, 0, formPanel, gbc, addressField);
        addLabelAndField("Phone", 2, 2, formPanel, gbc, phoneField);
        addLabelAndField("Email ID", 3, 0, formPanel, gbc, emailField);
        addLabelAndField("Class X (%)", 3, 2, formPanel, gbc, classXField);
        addLabelAndField("Class XII (%)", 4, 0, formPanel, gbc, classXIIField);
        addLabelAndField("Aadhar Number", 4, 2, formPanel, gbc, aadharField);
        addLabelAndField("Course", 5, 0, formPanel, gbc, courseComboBox);
        addLabelAndField("Branch", 5, 2, formPanel, gbc, branchComboBox);
        
        // --- PHOTO PANEL ---
        JPanel photoPanel = new JPanel(new BorderLayout(10, 10));
        photoPanel.setBackground(new Color(240, 248, 255));
        photoPanel.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 20));

        JPanel photoContainer = new JPanel(new GridBagLayout());
        photoContainer.setOpaque(false);
        photoPreviewLabel = new JLabel();
        photoPreviewLabel.setPreferredSize(new Dimension(150, 180));
        photoPreviewLabel.setBorder(new RoundedBorder(Color.GRAY, 15));
        photoPreviewLabel.setHorizontalAlignment(SwingConstants.CENTER);
        photoPreviewLabel.setText("<html><center>Upload<br>Student<br>Photo</center></html>");
        photoContainer.add(photoPreviewLabel);
        photoPanel.add(photoContainer, BorderLayout.CENTER);

        uploadButton = new JButton("Upload Photo");
        uploadButton.addActionListener(this);
        photoPanel.add(uploadButton, BorderLayout.SOUTH);
        contentPanel.add(photoPanel, BorderLayout.EAST);

        // --- BUTTONS ---
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttonPanel.setBackground(new Color(240, 248, 255));
        submitButton = new JButton("Submit");
        submitButton.addActionListener(this);
        cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(this);
        buttonPanel.add(submitButton);
        buttonPanel.add(cancelButton);
        add(buttonPanel, BorderLayout.SOUTH);

        setVisible(true);
    }
    
    private void addLabelAndField(String labelText, int y, int x, Container container, GridBagConstraints gbc, JComponent field) {
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Tahoma", Font.BOLD, 14));
        gbc.gridx = x; gbc.gridy = y; gbc.anchor = GridBagConstraints.EAST; gbc.weightx = 0.1;
        container.add(label, gbc);
        gbc.gridx = x + 1; gbc.anchor = GridBagConstraints.WEST; gbc.weightx = 0.9;
        container.add(field, gbc);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == uploadButton) {
            JFileChooser fileChooser = new JFileChooser();
            FileNameExtensionFilter filter = new FileNameExtensionFilter("Image Files", "jpg", "png", "gif", "jpeg");
            fileChooser.setFileFilter(filter);
            if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                
                // --- UPDATED: Store the absolute path of the selected file ---
                photoPath = selectedFile.getAbsolutePath();
                
                try {
                    ImageIcon imageIcon = new ImageIcon(photoPath);
                    Image image = imageIcon.getImage().getScaledInstance(photoPreviewLabel.getWidth(), photoPreviewLabel.getHeight(), Image.SCALE_SMOOTH);
                    photoPreviewLabel.setIcon(new ImageIcon(image));
                    photoPreviewLabel.setText("");
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Error loading image!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        } else if (e.getSource() == submitButton) {
            try {
                String studentId = rollNumberLabelValue.getText();
                String name = nameField.getText();
                String fname = fathersNameField.getText();
                String dob = dobField.getText();
                String address = addressField.getText();
                String phone = phoneField.getText();
                String email = emailField.getText();
                String classX = classXField.getText();
                String classXII = classXIIField.getText();
                String aadhar = aadharField.getText();
                String course = (String) courseComboBox.getSelectedItem();
                String branch = (String) branchComboBox.getSelectedItem();

                conn c = new conn();
                // --- UPDATED: Query now includes the photo_path column ---
                String query = "INSERT INTO students (student_id, full_name, fathers_name, date_of_birth, address, phone_no, email, class_x_percentage, class_xii_percentage, aadhar_no, course, branch, photo_path) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
                
                PreparedStatement pstmt = c.c.prepareStatement(query);
                pstmt.setString(1, studentId);
                pstmt.setString(2, name);
                pstmt.setString(3, fname);
                pstmt.setString(4, dob);
                pstmt.setString(5, address);
                pstmt.setString(6, phone);
                pstmt.setString(7, email);
                pstmt.setString(8, classX);
                pstmt.setString(9, classXII);
                pstmt.setString(10, aadhar);
                pstmt.setString(11, course);
                pstmt.setString(12, branch);
                // --- UPDATED: Set the photo_path in the database ---
                pstmt.setString(13, photoPath);

                pstmt.executeUpdate();

                JOptionPane.showMessageDialog(this, "Student Details Inserted Successfully");
                this.dispose();

            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error inserting student details: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            }
        } else if (e.getSource() == cancelButton) {
            this.dispose();
        }
    }
    
    // --- NEW: Added main method to make the class runnable ---
    public static void main(String[] args) {
        new AddStudent();
    }
}

