package university.management.system;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class EnterMarks extends JFrame implements ActionListener {

    JComboBox<String> rollNumberComboBox, semesterComboBox;
    JLabel nameLabelValue; // To display the student's name
    JTextField subject1Field, subject2Field, subject3Field, subject4Field, subject5Field;
    JTextField marks1Field, marks2Field, marks3Field, marks4Field, marks5Field;
    JButton submitButton, cancelButton;

    public EnterMarks() {
        // --- FRAME SETUP ---
        setTitle("Enter Student Marks");
        setSize(1200, 600); // Adjusted size for the image
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(false);
        setLayout(new BorderLayout());
        getContentPane().setBackground(new Color(240, 248, 255));

        // --- IMAGE PANEL (Left Side) ---
        JPanel imagePanel = new JPanel(new GridBagLayout()); // Use GridBagLayout to center
        imagePanel.setBackground(new Color(240, 248, 255));
        imagePanel.setPreferredSize(new Dimension(450, 0));
        try {
            java.net.URL imageUrl = ClassLoader.getSystemResource("icons/exam.png");
            
            if (imageUrl != null) {
                ImageIcon i1 = new ImageIcon(imageUrl);
                Image i2 = i1.getImage().getScaledInstance(400, 300, Image.SCALE_DEFAULT);
                ImageIcon i3 = new ImageIcon(i2);
                JLabel image = new JLabel(i3);
                imagePanel.add(image, new GridBagConstraints()); // Add with default constraints to center
            } else {
                JLabel errorLabel = new JLabel("Image not found at path: icons/exam.png");
                errorLabel.setFont(new Font("Tahoma", Font.ITALIC, 16));
                errorLabel.setForeground(Color.RED);
                imagePanel.add(errorLabel);
                System.err.println("Error: Could not find image at path: icons/exam.png");
            }
        } catch (Exception e) {
            e.printStackTrace();
            imagePanel.add(new JLabel("Error loading image."));
        }
        add(imagePanel, BorderLayout.WEST);
        
        // --- Container panel for the form on the right ---
        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.setBackground(new Color(240, 248, 255));
        add(rightPanel, BorderLayout.CENTER);

        // --- HEADER ---
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(240, 248, 255));
        JLabel headerLabel = new JLabel("Enter Marks of Student");
        headerLabel.setFont(new Font("Serif", Font.BOLD, 28));
        headerPanel.add(headerLabel);
        rightPanel.add(headerPanel, BorderLayout.NORTH);

        // --- FORM PANEL ---
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(new Color(240, 248, 255));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 50));
        rightPanel.add(formPanel, BorderLayout.CENTER);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // --- Initialize Components ---
        rollNumberComboBox = new JComboBox<>();
        populateRollNumbers();
        rollNumberComboBox.addActionListener(this);

        nameLabelValue = new JLabel();
        nameLabelValue.setFont(new Font("Tahoma", Font.PLAIN, 16));
        
        String[] semesters = {"Semester 1", "Semester 2", "Semester 3", "Semester 4", "Semester 5", "Semester 6", "Semester 7", "Semester 8"};
        semesterComboBox = new JComboBox<>(semesters);
        semesterComboBox.addActionListener(this); // To trigger subject update

        subject1Field = new JTextField(); subject2Field = new JTextField(); subject3Field = new JTextField();
        subject4Field = new JTextField(); subject5Field = new JTextField();

        marks1Field = new JTextField(); marks2Field = new JTextField(); marks3Field = new JTextField();
        marks4Field = new JTextField(); marks5Field = new JTextField();

        // --- Add Components to Form ---
        int y = 0;
        addLabelAndField("Select Roll Number", y++, formPanel, gbc, rollNumberComboBox);
        addLabelAndField("Name", y++, formPanel, gbc, nameLabelValue);
        addLabelAndField("Select Semester", y++, formPanel, gbc, semesterComboBox);
        
        gbc.gridx = 0;
        gbc.gridy = y++;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        JSeparator separator = new JSeparator();
        formPanel.add(separator, gbc);

        gbc.gridwidth = 1; // Reset gridwidth
        gbc.anchor = GridBagConstraints.WEST;

        addLabelAndField("Subject 1", y, formPanel, gbc, subject1Field);
        addLabelAndField("Marks 1", y++, formPanel, gbc, marks1Field, 2);
        addLabelAndField("Subject 2", y, formPanel, gbc, subject2Field);
        addLabelAndField("Marks 2", y++, formPanel, gbc, marks2Field, 2);
        addLabelAndField("Subject 3", y, formPanel, gbc, subject3Field);
        addLabelAndField("Marks 3", y++, formPanel, gbc, marks3Field, 2);
        addLabelAndField("Subject 4", y, formPanel, gbc, subject4Field);
        addLabelAndField("Marks 4", y++, formPanel, gbc, marks4Field, 2);
        addLabelAndField("Subject 5", y, formPanel, gbc, subject5Field);
        addLabelAndField("Marks 5", y++, formPanel, gbc, marks5Field, 2);

        // --- BUTTONS ---
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttonPanel.setBackground(new Color(240, 248, 255));
        submitButton = new JButton("Submit");
        submitButton.addActionListener(this);
        cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(this);
        buttonPanel.add(submitButton);
        buttonPanel.add(cancelButton);
        rightPanel.add(buttonPanel, BorderLayout.SOUTH);

        // --- Initial setup calls ---
        fetchStudentName((String) rollNumberComboBox.getSelectedItem());
        updateSubjectsForSemester((String) semesterComboBox.getSelectedItem());

        setVisible(true);
    }

    private void populateRollNumbers() {
        try {
            conn c = new conn();
            ResultSet rs = c.s.executeQuery("SELECT student_id FROM students ORDER BY student_id");
            while (rs.next()) {
                rollNumberComboBox.addItem(rs.getString("student_id"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void fetchStudentName(String studentId) {
        if (studentId == null) {
            nameLabelValue.setText("");
            return;
        }
        try {
            conn c = new conn();
            String query = "SELECT full_name FROM students WHERE student_id = ?";
            PreparedStatement pstmt = c.c.prepareStatement(query);
            pstmt.setString(1, studentId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                nameLabelValue.setText(rs.getString("full_name"));
            } else {
                nameLabelValue.setText("Not Found");
            }
        } catch (Exception e) {
            e.printStackTrace();
            nameLabelValue.setText("Error");
        }
    }
    
    // --- NEW: Method to update subjects based on semester ---
    private void updateSubjectsForSemester(String semester) {
        JTextField[] subjectFields = {subject1Field, subject2Field, subject3Field, subject4Field, subject5Field};
        String[] subjects;

        switch (semester) {
            case "Semester 1":
                subjects = new String[]{"Physics", "Chemistry", "Mathematics", "Programming", "English"};
                break;
            case "Semester 2":
                subjects = new String[]{"Data Structures", "Algorithms", "Database Systems", "Web Development", "Statistics"};
                break;
            case "Semester 3":
                subjects = new String[]{"Operating Systems", "Computer Networks", "Software Engineering", "Digital Logic", "Economics"};
                break;
            case "Semester 4":
                subjects = new String[]{"Theory of Computation", "Compiler Design", "AI", "Machine Learning", "Project Management"};
                break;
            // Add more cases for other semesters
            default:
                subjects = new String[]{"", "", "", "", ""};
                break;
        }

        for (int i = 0; i < subjectFields.length; i++) {
            subjectFields[i].setText(subjects[i]);
            subjectFields[i].setEditable(false); // Make subjects non-editable
            subjectFields[i].setBackground(new Color(230, 230, 230)); // Indicate non-editable status
        }
    }
    
    private void addLabelAndField(String text, int y, JPanel p, GridBagConstraints gbc, JComponent field) {
        addLabelAndField(text, y, p, gbc, field, 0);
    }

    private void addLabelAndField(String text, int y, JPanel p, GridBagConstraints gbc, JComponent field, int x) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Tahoma", Font.BOLD, 14));
        gbc.gridx = x;
        gbc.gridy = y;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.gridwidth = 1;
        gbc.weightx = 0.2;
        p.add(label, gbc);

        gbc.gridx = x + 1;
        gbc.weightx = 0.8;
        p.add(field, gbc);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == rollNumberComboBox) {
            fetchStudentName((String) rollNumberComboBox.getSelectedItem());
        } else if (e.getSource() == semesterComboBox) {
            updateSubjectsForSemester((String) semesterComboBox.getSelectedItem());
        } else if (e.getSource() == submitButton) {
            try {
                String studentId = (String) rollNumberComboBox.getSelectedItem();
                String semester = (String) semesterComboBox.getSelectedItem();

                String sub1 = subject1Field.getText();
                String m1 = marks1Field.getText();
                String sub2 = subject2Field.getText();
                String m2 = marks2Field.getText();
                String sub3 = subject3Field.getText();
                String m3 = marks3Field.getText();
                String sub4 = subject4Field.getText();
                String m4 = marks4Field.getText();
                String sub5 = subject5Field.getText();
                String m5 = marks5Field.getText();
                
                if (studentId == null) {
                    JOptionPane.showMessageDialog(this, "Please select a student.", "Validation Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                if (m1.trim().isEmpty() && m2.trim().isEmpty() && m3.trim().isEmpty() && m4.trim().isEmpty() && m5.trim().isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Please enter marks for at least one subject.", "Validation Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                conn c = new conn();
                String query = "INSERT INTO marks (student_id, semester, subject1, marks1, subject2, marks2, subject3, marks3, subject4, marks4, subject5, marks5) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
                PreparedStatement pstmt = c.c.prepareStatement(query);
                pstmt.setString(1, studentId);
                pstmt.setString(2, semester);
                pstmt.setString(3, sub1);
                pstmt.setString(4, m1);
                pstmt.setString(5, sub2);
                pstmt.setString(6, m2);
                pstmt.setString(7, sub3);
                pstmt.setString(8, m3);
                pstmt.setString(9, sub4);
                pstmt.setString(10, m4);
                pstmt.setString(11, sub5);
                pstmt.setString(12, m5);
                pstmt.executeUpdate();

                JOptionPane.showMessageDialog(this, "Marks Inserted Successfully");
                this.dispose();

            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error inserting marks: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            }
        } else if (e.getSource() == cancelButton) {
            this.dispose();
        }
    }

    public static void main(String[] args) {
        new EnterMarks();
    }
}

