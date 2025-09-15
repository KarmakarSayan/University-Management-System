package university.management.system;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class StudentFeeForm extends JFrame implements ActionListener {

    JComboBox<String> rollNumberComboBox, semesterComboBox;
    JLabel nameLabelValue, branchLabelValue, totalAmountLabel;
    JButton payFeeButton, cancelButton;

    public StudentFeeForm() {
        // --- FRAME SETUP ---
        setTitle("Student Fee Payment");
        setSize(600, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(false);
        setLayout(new BorderLayout());
        getContentPane().setBackground(new Color(240, 248, 255));

        // --- HEADER ---
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(240, 248, 255));
        JLabel headerLabel = new JLabel("Student Fee Form");
        headerLabel.setFont(new Font("Serif", Font.BOLD, 28));
        headerPanel.add(headerLabel);
        add(headerPanel, BorderLayout.NORTH);

        // --- FORM PANEL ---
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(new Color(240, 248, 255));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        add(formPanel, BorderLayout.CENTER);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // --- Initialize Components ---
        rollNumberComboBox = new JComboBox<>();
        populateRollNumbers();
        rollNumberComboBox.addActionListener(this);

        nameLabelValue = new JLabel();
        branchLabelValue = new JLabel();
        String[] semesters = {"Semester 1", "Semester 2", "Semester 3", "Semester 4", "Semester 5", "Semester 6", "Semester 7", "Semester 8"};
        semesterComboBox = new JComboBox<>(semesters);
        semesterComboBox.addActionListener(this);

        totalAmountLabel = new JLabel("0");
        totalAmountLabel.setFont(new Font("Tahoma", Font.BOLD, 16));

        // --- Add Components to Form ---
        int y = 0;
        addLabelAndField("Select Roll Number", y++, formPanel, gbc, rollNumberComboBox);
        addLabelAndField("Name", y++, formPanel, gbc, nameLabelValue);
        addLabelAndField("Branch", y++, formPanel, gbc, branchLabelValue);
        addLabelAndField("Select Semester", y++, formPanel, gbc, semesterComboBox);
        addLabelAndField("Total Payable", y++, formPanel, gbc, totalAmountLabel);

        // --- BUTTONS ---
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttonPanel.setBackground(new Color(240, 248, 255));
        payFeeButton = new JButton("Pay Fee");
        payFeeButton.addActionListener(this);
        cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(this);
        buttonPanel.add(payFeeButton);
        buttonPanel.add(cancelButton);
        add(buttonPanel, BorderLayout.SOUTH);

        // Initial data load
        if (rollNumberComboBox.getItemCount() > 0) {
            rollNumberComboBox.setSelectedIndex(0);
            updateStudentDetails();
        }

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

    private void updateStudentDetails() {
        String studentId = (String) rollNumberComboBox.getSelectedItem();
        if (studentId == null) return;

        try {
            conn c = new conn();
            String query = "SELECT full_name, branch, course FROM students WHERE student_id = ?";
            PreparedStatement pstmt = c.c.prepareStatement(query);
            pstmt.setString(1, studentId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                nameLabelValue.setText(rs.getString("full_name"));
                branchLabelValue.setText(rs.getString("branch"));
                updateFeeAmount(rs.getString("course"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateFeeAmount(String course) {
        String semester = (String) semesterComboBox.getSelectedItem();
        if (course == null || semester == null) return;
        
        // Convert "Semester 1" to "semester1" to match column name
        String semesterColumn = semester.toLowerCase().replace(" ", "");

        try {
            conn c = new conn();
            String query = "SELECT " + semesterColumn + " FROM fee WHERE course = ?";
            PreparedStatement pstmt = c.c.prepareStatement(query);
            pstmt.setString(1, course);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                totalAmountLabel.setText(rs.getString(semesterColumn));
            } else {
                totalAmountLabel.setText("N/A");
            }
        } catch (Exception e) {
            e.printStackTrace();
            totalAmountLabel.setText("Error");
        }
    }

    private void addLabelAndField(String labelText, int y, Container container, GridBagConstraints gbc, JComponent field) {
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Tahoma", Font.BOLD, 14));
        gbc.gridx = 0;
        gbc.gridy = y;
        gbc.anchor = GridBagConstraints.WEST;
        container.add(label, gbc);

        gbc.gridx = 1;
        container.add(field, gbc);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == rollNumberComboBox || e.getSource() == semesterComboBox) {
            updateStudentDetails();
        } else if (e.getSource() == payFeeButton) {
            String studentId = (String) rollNumberComboBox.getSelectedItem();
            String name = nameLabelValue.getText();
            String branch = branchLabelValue.getText();
            String semester = (String) semesterComboBox.getSelectedItem();
            String amount = totalAmountLabel.getText();

            // Open the payment gateway window
            new PaymentGateway(studentId, name, branch, semester, amount);
            this.dispose();

        } else if (e.getSource() == cancelButton) {
            this.dispose();
        }
    }

    public static void main(String[] args) {
        new StudentFeeForm();
    }
}
