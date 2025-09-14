package university.management.system;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class UpdateTeacher extends JFrame implements ActionListener {

    JTextField nameField, fathersNameField, dobField, ageField, nationalityField, religionField, permanentTelField, permanentPinField, presentTelField, presentPinField, emailField;
    JTextArea permanentAddressArea, presentAddressArea;
    JRadioButton maleRadio, femaleRadio, marriedRadio, unmarriedRadio;
    JLabel facultyIdLabelValue;
    JButton submitButton, cancelButton;
    String facultyId;

    // A reference to the parent ViewTeacherDetails window
    private ViewTeacherDetails parentView;

    /**
     * Constructor for the update form.
     * @param facultyId The ID of the teacher to be updated.
     * @param parent A reference to the ViewTeacherDetails window for refreshing the table.
     */
    public UpdateTeacher(String facultyId, ViewTeacherDetails parent) {
        this.facultyId = facultyId;
        this.parentView = parent;

        // --- FRAME SETUP ---
        setTitle("Update Teacher Details");
        setSize(900, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(false);
        setLayout(new BorderLayout());
        getContentPane().setBackground(new Color(240, 248, 255));

        // --- HEADER ---
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(240, 248, 255));
        JLabel headerLabel = new JLabel("Update Teacher Details");
        headerLabel.setFont(new Font("Serif", Font.BOLD, 28));
        headerPanel.add(headerLabel);
        add(headerPanel, BorderLayout.NORTH);

        // --- FORM PANEL ---
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(new Color(240, 248, 255));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        add(new JScrollPane(formPanel), BorderLayout.CENTER);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 5, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // --- Initialize Components ---
        nameField = new JTextField();
        fathersNameField = new JTextField();
        facultyIdLabelValue = new JLabel();
        facultyIdLabelValue.setFont(new Font("Tahoma", Font.BOLD, 16));
        dobField = new JTextField();
        ageField = new JTextField();
        nationalityField = new JTextField();
        religionField = new JTextField();
        permanentAddressArea = new JTextArea(3, 20);
        permanentTelField = new JTextField();
        permanentPinField = new JTextField();
        presentAddressArea = new JTextArea(3, 20);
        presentTelField = new JTextField();
        presentPinField = new JTextField();
        emailField = new JTextField();
        maleRadio = new JRadioButton("Male");
        femaleRadio = new JRadioButton("Female");
        marriedRadio = new JRadioButton("Married");
        unmarriedRadio = new JRadioButton("Unmarried");

        // --- Add Components to Form ---
        int y = 0;
        addLabelAndField("Name", y, 0, formPanel, gbc, nameField);
        addLabelAndField("Father's Name", y++, 2, formPanel, gbc, fathersNameField);
        addLabelAndField("Faculty ID", y, 0, formPanel, gbc, facultyIdLabelValue);
        addLabelAndField("Date of Birth (YYYY-MM-DD)", y++, 2, formPanel, gbc, dobField);
        addLabelAndField("Age", y, 0, formPanel, gbc, ageField);
        addLabelAndField("Nationality", y++, 2, formPanel, gbc, nationalityField);
        addLabelAndField("Religion", y, 0, formPanel, gbc, religionField);
        
        JPanel sexPanel = createRadioPanel(new Color(240, 248, 255), maleRadio, femaleRadio);
        addLabelAndField("Sex", y++, 2, formPanel, gbc, sexPanel);
        
        JPanel maritalPanel = createRadioPanel(new Color(240, 248, 255), marriedRadio, unmarriedRadio);
        addLabelAndField("Marital Status", y++, 0, formPanel, gbc, maritalPanel);
        
        addLabelAndField("Permanent Address", y++, 0, formPanel, gbc, new JScrollPane(permanentAddressArea), 3);
        addLabelAndField("Tel. No.", y, 0, formPanel, gbc, permanentTelField);
        addLabelAndField("PIN", y++, 2, formPanel, gbc, permanentPinField);
        
        addLabelAndField("Present Address", y++, 0, formPanel, gbc, new JScrollPane(presentAddressArea), 3);
        addLabelAndField("Tel. No.", y, 0, formPanel, gbc, presentTelField);
        addLabelAndField("PIN", y++, 2, formPanel, gbc, presentPinField);
        
        addLabelAndField("Email", y, 0, formPanel, gbc, emailField);

        // --- Populate form with existing data ---
        try {
            populateForm();
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Could not load teacher data: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            dispose();
            return;
        }

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

        setVisible(true);
    }

    private void populateForm() throws Exception {
        conn c = new conn();
        String query = "SELECT * FROM teachers WHERE faculty_id = ?";
        PreparedStatement pstmt = c.c.prepareStatement(query);
        pstmt.setString(1, facultyId);
        ResultSet rs = pstmt.executeQuery();

        if (rs.next()) {
            nameField.setText(safeGetString(rs, "full_name"));
            fathersNameField.setText(safeGetString(rs, "fathers_name"));
            facultyIdLabelValue.setText(safeGetString(rs, "faculty_id"));
            dobField.setText(safeGetString(rs, "date_of_birth"));
            ageField.setText(safeGetString(rs, "age"));
            nationalityField.setText(safeGetString(rs, "nationality"));
            religionField.setText(safeGetString(rs, "religion"));
            permanentAddressArea.setText(safeGetString(rs, "permanent_address"));
            permanentTelField.setText(safeGetString(rs, "permanent_tel_no"));
            permanentPinField.setText(safeGetString(rs, "permanent_pin"));
            presentAddressArea.setText(safeGetString(rs, "present_address"));
            presentTelField.setText(safeGetString(rs, "present_tel_no"));
            presentPinField.setText(safeGetString(rs, "present_pin"));
            emailField.setText(safeGetString(rs, "email"));

            String sex = safeGetString(rs, "sex");
            if ("Male".equals(sex)) maleRadio.setSelected(true); else femaleRadio.setSelected(true);

            String maritalStatus = safeGetString(rs, "marital_status");
            if ("Married".equals(maritalStatus)) marriedRadio.setSelected(true); else unmarriedRadio.setSelected(true);
        } else {
            throw new Exception("Teacher with Faculty ID " + facultyId + " not found.");
        }
    }

    private String safeGetString(ResultSet rs, String columnName) throws Exception {
        String value = rs.getString(columnName);
        return (value == null) ? "" : value;
    }
    
    private void addLabelAndField(String labelText, int y, int x, Container c, GridBagConstraints gbc, JComponent field) {
        addLabelAndField(labelText, y, x, c, gbc, field, 1);
    }

    private void addLabelAndField(String labelText, int y, int x, Container c, GridBagConstraints gbc, JComponent field, int gridwidth) {
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Tahoma", Font.BOLD, 14));
        gbc.gridx = x;
        gbc.gridy = y;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.weightx = 0.1;
        gbc.gridwidth = 1;
        c.add(label, gbc);

        gbc.gridx = x + 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.weightx = 0.9;
        gbc.gridwidth = gridwidth;
        c.add(field, gbc);
    }

    private JPanel createRadioPanel(Color bg, JRadioButton... buttons) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        panel.setBackground(bg);
        ButtonGroup group = new ButtonGroup();
        for(JRadioButton button : buttons) {
            button.setBackground(bg);
            group.add(button);
            panel.add(button);
        }
        return panel;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == submitButton) {
            try {
                conn c = new conn();
                String query = "UPDATE teachers SET full_name=?, fathers_name=?, date_of_birth=?, age=?, nationality=?, religion=?, sex=?, marital_status=?, permanent_address=?, permanent_tel_no=?, permanent_pin=?, present_address=?, present_tel_no=?, present_pin=?, email=? WHERE faculty_id=?";
                PreparedStatement pstmt = c.c.prepareStatement(query);
                pstmt.setString(1, nameField.getText());
                pstmt.setString(2, fathersNameField.getText());
                pstmt.setString(3, dobField.getText());
                pstmt.setString(4, ageField.getText());
                pstmt.setString(5, nationalityField.getText());
                pstmt.setString(6, religionField.getText());
                pstmt.setString(7, maleRadio.isSelected() ? "Male" : "Female");
                pstmt.setString(8, marriedRadio.isSelected() ? "Married" : "Unmarried");
                pstmt.setString(9, permanentAddressArea.getText());
                pstmt.setString(10, permanentTelField.getText());
                pstmt.setString(11, permanentPinField.getText());
                pstmt.setString(12, presentAddressArea.getText());
                pstmt.setString(13, presentTelField.getText());
                pstmt.setString(14, presentPinField.getText());
                pstmt.setString(15, emailField.getText());
                pstmt.setString(16, facultyId);
                pstmt.executeUpdate();

                JOptionPane.showMessageDialog(this, "Teacher Details Updated Successfully");

                if (parentView != null) {
                    parentView.refreshTable();
                }
                
                this.dispose();

            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error updating teacher details: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else if (e.getSource() == cancelButton) {
            this.dispose();
        }
    }
}

