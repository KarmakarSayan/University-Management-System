package university.management.system;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class UpdateStudent extends JFrame implements ActionListener {

    JTextField nameField, fathersNameField, addressField, phoneField, emailField, classXField, classXIIField, aadharField, dobField;
    JLabel rollNumberLabelValue;
    JComboBox<String> courseComboBox, branchComboBox;
    JButton submitButton, cancelButton, uploadButton;
    JLabel photoPreviewLabel;
    
    private String studentId;
    private String photoPath = "";
    private ViewStudentDetails parentView;

    // Helper class for the rounded border
    private static class RoundedBorder extends javax.swing.border.AbstractBorder {
        private int radius; private Color color;
        RoundedBorder(Color color, int radius) { this.radius = radius; this.color = color; }
        @Override public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(color); g2.drawRoundRect(x, y, width - 1, height - 1, radius, radius); g2.dispose();
        }
    }

    public UpdateStudent(String studentId, ViewStudentDetails parent) {
        this.studentId = studentId;
        this.parentView = parent;

        // --- FRAME SETUP ---
        setTitle("Update Student Details");
        setSize(1050, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(false);
        setLayout(new BorderLayout());
        getContentPane().setBackground(new Color(240, 248, 255));

        // --- HEADER ---
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(240, 248, 255));
        JLabel headerLabel = new JLabel("Update Student Details");
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
        rollNumberLabelValue = new JLabel();
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
        submitButton = new JButton("Update");
        submitButton.addActionListener(this);
        cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(this);
        buttonPanel.add(submitButton);
        buttonPanel.add(cancelButton);
        add(buttonPanel, BorderLayout.SOUTH);

        // --- Populate form with existing data ---
        try {
            populateForm();
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Could not load student data: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            dispose();
            return;
        }

        setVisible(true);
    }
    
    private void populateForm() throws Exception {
        conn c = new conn();
        String query = "SELECT * FROM students WHERE student_id = ?";
        PreparedStatement pstmt = c.c.prepareStatement(query);
        pstmt.setString(1, studentId);
        ResultSet rs = pstmt.executeQuery();

        if (rs.next()) {
            nameField.setText(safeGetString(rs, "full_name"));
            fathersNameField.setText(safeGetString(rs, "fathers_name"));
            rollNumberLabelValue.setText(safeGetString(rs, "student_id"));
            dobField.setText(safeGetString(rs, "date_of_birth"));
            addressField.setText(safeGetString(rs, "address"));
            phoneField.setText(safeGetString(rs, "phone_no"));
            emailField.setText(safeGetString(rs, "email"));
            classXField.setText(safeGetString(rs, "class_x_percentage"));
            classXIIField.setText(safeGetString(rs, "class_xii_percentage"));
            aadharField.setText(safeGetString(rs, "aadhar_no"));
            courseComboBox.setSelectedItem(safeGetString(rs, "course"));
            branchComboBox.setSelectedItem(safeGetString(rs, "branch"));
            
            photoPath = safeGetString(rs, "photo_path");
            if (!photoPath.isEmpty()) {
                ImageIcon imageIcon = new ImageIcon(photoPath);
                Image image = imageIcon.getImage().getScaledInstance(150, 180, Image.SCALE_SMOOTH);
                photoPreviewLabel.setIcon(new ImageIcon(image));
                photoPreviewLabel.setText("");
            }
        } else {
            throw new Exception("Student with Roll Number " + studentId + " not found.");
        }
    }
    
    private String safeGetString(ResultSet rs, String columnName) throws Exception {
        String value = rs.getString(columnName);
        return (value == null) ? "" : value;
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
                photoPath = selectedFile.getAbsolutePath();
                try {
                    ImageIcon imageIcon = new ImageIcon(photoPath);
                    Image image = imageIcon.getImage().getScaledInstance(150, 180, Image.SCALE_SMOOTH);
                    photoPreviewLabel.setIcon(new ImageIcon(image));
                    photoPreviewLabel.setText("");
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Error loading image!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        } else if (e.getSource() == submitButton) {
            try {
                conn c = new conn();
                String query = "UPDATE students SET full_name=?, fathers_name=?, date_of_birth=?, address=?, phone_no=?, email=?, class_x_percentage=?, class_xii_percentage=?, aadhar_no=?, course=?, branch=?, photo_path=? WHERE student_id=?";
                
                PreparedStatement pstmt = c.c.prepareStatement(query);
                pstmt.setString(1, nameField.getText());
                pstmt.setString(2, fathersNameField.getText());
                pstmt.setString(3, dobField.getText());
                pstmt.setString(4, addressField.getText());
                pstmt.setString(5, phoneField.getText());
                pstmt.setString(6, emailField.getText());
                pstmt.setString(7, classXField.getText());
                pstmt.setString(8, classXIIField.getText());
                pstmt.setString(9, aadharField.getText());
                pstmt.setString(10, (String) courseComboBox.getSelectedItem());
                pstmt.setString(11, (String) branchComboBox.getSelectedItem());
                pstmt.setString(12, photoPath);
                pstmt.setString(13, studentId);
                
                pstmt.executeUpdate();

                JOptionPane.showMessageDialog(this, "Student Details Updated Successfully");

                if (parentView != null) {
                    parentView.refreshTable();
                }
                
                this.dispose();

            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error updating student details: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else if (e.getSource() == cancelButton) {
            this.dispose();
        }
    }
}

