package university.management.system;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class TeacherLeave extends JFrame implements ActionListener {

    JComboBox<String> facultyIdComboBox;
    JTextField dateField;
    JComboBox<String> durationComboBox;
    JTextArea reasonArea;
    JButton submitButton, cancelButton;

    public TeacherLeave() {
        // --- FRAME SETUP ---
        setTitle("Apply for Leave (Teacher)");
        setSize(500, 550);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(false);
        setLayout(new BorderLayout());
        getContentPane().setBackground(new Color(240, 248, 255));

        // --- HEADER ---
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(240, 248, 255));
        JLabel headerLabel = new JLabel("Teacher Leave Application");
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
        gbc.anchor = GridBagConstraints.WEST;

        // --- Initialize Components ---
        facultyIdComboBox = new JComboBox<>();
        populateFacultyIds();

        dateField = new JTextField();
        durationComboBox = new JComboBox<>(new String[]{"Full Day", "Half Day"});
        reasonArea = new JTextArea(5, 20);
        reasonArea.setLineWrap(true);
        reasonArea.setWrapStyleWord(true);

        // --- Add Components to Form ---
        int y = 0;
        addLabelAndField("Select Faculty ID", y++, formPanel, gbc, facultyIdComboBox);
        addLabelAndField("Date (YYYY-MM-DD)", y++, formPanel, gbc, dateField);
        addLabelAndField("Duration", y++, formPanel, gbc, durationComboBox);
        addLabelAndField("Reason", y++, formPanel, gbc, new JScrollPane(reasonArea));

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

    private void populateFacultyIds() {
        try {
            conn c = new conn();
            ResultSet rs = c.s.executeQuery("SELECT faculty_id FROM teachers ORDER BY faculty_id");
            while (rs.next()) {
                facultyIdComboBox.addItem(rs.getString("faculty_id"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addLabelAndField(String labelText, int y, Container container, GridBagConstraints gbc, JComponent field) {
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Tahoma", Font.BOLD, 14));
        gbc.gridx = 0;
        gbc.gridy = y;
        gbc.weightx = 0.1;
        gbc.anchor = GridBagConstraints.WEST;
        container.add(label, gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.9;
        container.add(field, gbc);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == submitButton) {
            try {
                String facultyId = (String) facultyIdComboBox.getSelectedItem();
                String date = dateField.getText();
                String duration = (String) durationComboBox.getSelectedItem();
                String reason = reasonArea.getText();
                
                // Simple Validation
                if (facultyId == null || date.trim().isEmpty() || reason.trim().isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Please fill all required fields.", "Validation Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                conn c = new conn();
                String query = "INSERT INTO teacher_leave (faculty_id, leave_date, duration, reason) VALUES (?, ?, ?, ?)";
                PreparedStatement pstmt = c.c.prepareStatement(query);
                pstmt.setString(1, facultyId);
                pstmt.setString(2, date);
                pstmt.setString(3, duration);
                pstmt.setString(4, reason);
                pstmt.executeUpdate();

                JOptionPane.showMessageDialog(this, "Leave Application Submitted Successfully");
                this.dispose();

            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error submitting leave application: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            }
        } else if (e.getSource() == cancelButton) {
            this.dispose();
        }
    }

    public static void main(String[] args) {
        new TeacherLeave();
    }
}

 