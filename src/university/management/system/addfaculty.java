package university.management.system;

import javax.swing.*;
import javax.swing.border.AbstractBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.File;
import java.awt.RenderingHints;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;

public class addfaculty extends JFrame implements ActionListener {

    // --- All component declarations remain the same ---
    JTextField nameField, fathersNameField, nationalityField, religionField, telNoField, pinField, presentTelNoField, presentPinField, emailField, subjectField, dobField, ageField;
    JTextArea permanentAddressArea, presentAddressArea;
    JRadioButton maleRadio, femaleRadio, marriedRadio, unmarriedRadio;
    JButton uploadButton, submitButton, cancelButton, printButton;
    JLabel photoPreviewLabel;
    JCheckBox seniorSecTeacher, secondaryTeacher, primaryTeacher, librarian, pet, musicTeacher, others;
    JCheckBox assam, arunachal, nagaland;
    private JPanel formPanel;
    private String photoPath = ""; // To store the path of the uploaded photo

    // --- Helper class for the rounded border remains the same ---
    private static class RoundedBorder extends AbstractBorder {
        private int radius;
        private Color color;
        RoundedBorder(Color color, int radius) { this.radius = radius; this.color = color; }
        @Override
        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(color);
            g2.drawRoundRect(x, y, width - 1, height - 1, radius, radius);
            g2.dispose();
        }
        @Override
        public Insets getBorderInsets(Component c) { return new Insets(this.radius, this.radius, this.radius, this.radius); }
        @Override
        public Insets getBorderInsets(Component c, Insets insets) {
            insets.left = insets.top = insets.right = insets.bottom = this.radius;
            return insets;
        }
    }

    addfaculty() {
        // --- The entire constructor and UI setup remains the same ---
        setTitle("Application Form for the Posts of Teachers");
        setSize(1000, 800);
        setMinimumSize(new Dimension(950, 700));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));
        Color backgroundColor = new Color(240, 248, 255);
        getContentPane().setBackground(backgroundColor);
        JMenuBar menuBar = new JMenuBar();
        JMenu toolsMenu = new JMenu("Tools");
        JMenuItem searchUpdateItem = new JMenuItem("Search/Update Faculty");
        searchUpdateItem.addActionListener(this);
        toolsMenu.add(searchUpdateItem);
        menuBar.add(toolsMenu);
        setJMenuBar(menuBar);
        add(createHeaderPanel(backgroundColor), BorderLayout.NORTH);
        formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(backgroundColor);
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        add(formPanel, BorderLayout.CENTER);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);
        JPanel applicationPanel = createApplicationPanel(backgroundColor);
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2; gbc.weightx = 1.0;
        formPanel.add(applicationPanel, gbc);
        JPanel personalDetailsPanel = createPersonalDetailsPanel(backgroundColor);
        gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 1; gbc.weightx = 0.7;
        gbc.anchor = GridBagConstraints.NORTHWEST; gbc.fill = GridBagConstraints.HORIZONTAL;
        formPanel.add(personalDetailsPanel, gbc);
        JPanel photoPanel = createPhotoPanel(backgroundColor);
        gbc.gridx = 1; gbc.gridy = 1; gbc.weightx = 0.3;
        gbc.fill = GridBagConstraints.NONE; gbc.anchor = GridBagConstraints.NORTH;
        formPanel.add(photoPanel, gbc);
        JPanel glue = new JPanel();
        glue.setBackground(backgroundColor);
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2; gbc.weighty = 1.0;
        formPanel.add(glue, gbc);
        add(createButtonPanel(backgroundColor), BorderLayout.SOUTH);
        setVisible(true);
    }
    
    // --- All UI helper methods (createHeaderPanel, createPersonalDetailsPanel, etc.) remain the same ---
    private JPanel createHeaderPanel(Color bg) {
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        headerPanel.setBackground(bg);
        JLabel headerTitle = new JLabel("APPLICATION FORM FOR THE POSTS OF TEACHERS");
        headerTitle.setFont(new Font("Serif", Font.BOLD, 20));
        headerTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        JLabel subHeaderTitle = new JLabel("VIVEKANANDA KENDRA VIDYALAYAS IN NORTH - EAST");
        subHeaderTitle.setFont(new Font("Serif", Font.PLAIN, 16));
        subHeaderTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        JLabel officeUseLabel = new JLabel("(For office use only)");
        officeUseLabel.setFont(new Font("Serif", Font.ITALIC, 12));
        officeUseLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        headerPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        headerPanel.add(headerTitle); headerPanel.add(subHeaderTitle);
        headerPanel.add(officeUseLabel); headerPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        return headerPanel;
    }
    private JPanel createPersonalDetailsPanel(Color bg) {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(bg);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        int yPos = 0;
        nameField = new JTextField(20);
        addDetailRow(panel, gbc, "1. Name (In BLOCK Letters)", yPos++, 4, nameField);
        fathersNameField = new JTextField(20);
        addDetailRow(panel, gbc, "2. Father's/Guardian's Name", yPos++, 4, fathersNameField);
        JPanel sexPanel = createRadioPanel(bg, "Male", "Female");
        maleRadio = (JRadioButton) sexPanel.getComponent(0);
        femaleRadio = (JRadioButton) sexPanel.getComponent(1);
        addDetailRow(panel, gbc, "3. Sex", yPos, 1, sexPanel);
        JPanel maritalPanel = createRadioPanel(bg, "Married", "Unmarried");
        marriedRadio = (JRadioButton) maritalPanel.getComponent(0);
        unmarriedRadio = (JRadioButton) maritalPanel.getComponent(1);
        addDetailRow(panel, gbc, "4. Marital Status", yPos++, 1, maritalPanel, 2);
        dobField = new JTextField();
        addDetailRow(panel, gbc, "5. Date of Birth (YYYY-MM-DD)", yPos, 1, dobField);
        ageField = new JTextField();
        addDetailRow(panel, gbc, "6. Age (as on 01/01/2011)", yPos++, 1, ageField, 2);
        nationalityField = new JTextField();
        addDetailRow(panel, gbc, "7. Nationality", yPos, 1, nationalityField);
        religionField = new JTextField();
        addDetailRow(panel, gbc, "8. Religion", yPos++, 1, religionField, 2);
        permanentAddressArea = new JTextArea(3, 20);
        addDetailRow(panel, gbc, "9. Permanent Address", yPos++, 4, new JScrollPane(permanentAddressArea));
        telNoField = new JTextField();
        addDetailRow(panel, gbc, "Tel. No.(STD)", yPos, 1, telNoField);
        pinField = new JTextField();
        addDetailRow(panel, gbc, "PIN", yPos++, 1, pinField, 2);
        presentAddressArea = new JTextArea(3, 20);
        addDetailRow(panel, gbc, "10. Present Address", yPos++, 4, new JScrollPane(presentAddressArea));
        presentTelNoField = new JTextField();
        addDetailRow(panel, gbc, "Tel. No.", yPos, 1, presentTelNoField);
        presentPinField = new JTextField();
        addDetailRow(panel, gbc, "PIN", yPos++, 1, presentPinField, 2);
        emailField = new JTextField();
        addDetailRow(panel, gbc, "e-mail", yPos++, 4, emailField);
        return panel;
    }
    private void addDetailRow(JPanel p, GridBagConstraints g, String l, int y, int w, JComponent c) { addDetailRow(p, g, l, y, w, c, 0); }
    private void addDetailRow(JPanel p, GridBagConstraints g, String l, int y, int w, JComponent c, int x) {
        JLabel label = new JLabel(l);
        label.setFont(new Font("SansSerif", Font.BOLD, 14));
        g.gridx = x; g.gridy = y; g.gridwidth = (w == 4) ? 1 : w;
        g.fill = GridBagConstraints.NONE; g.anchor = (x == 0) ? GridBagConstraints.WEST : GridBagConstraints.EAST;
        g.weightx = 0.0; p.add(label, g);
        g.gridx = x + 1; g.gridwidth = (w == 4) ? 3 : w;
        g.anchor = GridBagConstraints.WEST; g.fill = GridBagConstraints.HORIZONTAL;
        g.weightx = 1.0; p.add(c, g);
    }
    private JPanel createRadioPanel(Color bg, String... options) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        panel.setBackground(bg);
        ButtonGroup group = new ButtonGroup();
        for (String option : options) {
            JRadioButton button = new JRadioButton(option);
            button.setBackground(bg);
            group.add(button);
            panel.add(button);
        }
        return panel;
    }
    private JPanel createApplicationPanel(Color bg) {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(bg);
        panel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 5, 10);
        gbc.anchor = GridBagConstraints.WEST; gbc.fill = GridBagConstraints.HORIZONTAL;
        JLabel postLabel = new JLabel("Application for the post of: (Please ✓ in the appropriate box)");
        postLabel.setFont(new Font("SansSerif", Font.BOLD, 12));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2; gbc.weightx = 0.5;
        panel.add(postLabel, gbc);
        JPanel postCheckPanel = new JPanel(new GridLayout(0, 2, 15, 5));
        postCheckPanel.setBackground(bg);
        postCheckPanel.add(seniorSecTeacher = new JCheckBox("Senior Secondary Teacher"));
        postCheckPanel.add(pet = new JCheckBox("Physical Education Teacher"));
        postCheckPanel.add(secondaryTeacher = new JCheckBox("Secondary Teacher"));
        postCheckPanel.add(musicTeacher = new JCheckBox("Music Teacher (Vocal)"));
        postCheckPanel.add(primaryTeacher = new JCheckBox("Primary Teacher"));
        postCheckPanel.add(others = new JCheckBox("Others (Specify)..."));
        postCheckPanel.add(librarian = new JCheckBox("Librarian"));
        gbc.gridy = 1; gbc.gridwidth = 2;
        panel.add(postCheckPanel, gbc);
        JLabel statePrefLabel = new JLabel("Preference of posting in the state:");
        statePrefLabel.setFont(new Font("SansSerif", Font.BOLD, 12));
        gbc.gridx = 2; gbc.gridy = 0; gbc.gridwidth = 1; gbc.weightx = 0.25;
        panel.add(statePrefLabel, gbc);
        JPanel stateCheckPanel = new JPanel(new GridLayout(0, 1, 0, 5));
        stateCheckPanel.setBackground(bg);
        stateCheckPanel.add(assam = new JCheckBox("Assam"));
        stateCheckPanel.add(arunachal = new JCheckBox("Arunachal Pradesh"));
        stateCheckPanel.add(nagaland = new JCheckBox("Nagaland"));
        gbc.gridy = 1;
        panel.add(stateCheckPanel, gbc);
        JLabel subjectLabel = new JLabel("Subject:");
        subjectLabel.setFont(new Font("SansSerif", Font.BOLD, 12));
        gbc.gridx = 2; gbc.gridy = 2; gbc.anchor = GridBagConstraints.EAST; gbc.weightx = 0.0;
        panel.add(subjectLabel, gbc);
        subjectField = new JTextField();
        gbc.gridx = 3; gbc.anchor = GridBagConstraints.WEST; gbc.weightx = 0.25;
        panel.add(subjectField, gbc);
        return panel;
    }
    private JPanel createPhotoPanel(Color bg) {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(bg);
        panel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));
        JPanel photoContainer = new JPanel(new GridBagLayout());
        photoContainer.setBackground(Color.WHITE);
        photoContainer.setBorder(new RoundedBorder(Color.GRAY, 15));
        photoContainer.setPreferredSize(new Dimension(150, 180));
        photoPreviewLabel = new JLabel("<html><center>Affix<br>Passport<br>Size<br>photograph</center></html>");
        photoPreviewLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));
        photoPreviewLabel.setForeground(Color.DARK_GRAY);
        photoPreviewLabel.setHorizontalAlignment(SwingConstants.CENTER);
        photoContainer.add(photoPreviewLabel);
        panel.add(photoContainer, BorderLayout.CENTER);
        uploadButton = new JButton("Upload Photo");
        uploadButton.addActionListener(this);
        panel.add(uploadButton, BorderLayout.SOUTH);
        return panel;
    }
    private JPanel createButtonPanel(Color bg) {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttonPanel.setBackground(bg);
        submitButton = new JButton("Submit"); printButton = new JButton("Print"); cancelButton = new JButton("Cancel");
        submitButton.addActionListener(this); printButton.addActionListener(this); cancelButton.addActionListener(this);
        buttonPanel.add(submitButton); buttonPanel.add(printButton); buttonPanel.add(cancelButton);
        return buttonPanel;
    }
    private boolean validateForm() {
        if (nameField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Name is a required field.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            nameField.requestFocus(); return false;
        }
        if (fathersNameField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Father's/Guardian's Name is a required field.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            fathersNameField.requestFocus(); return false;
        }
        if (!maleRadio.isSelected() && !femaleRadio.isSelected()) {
            JOptionPane.showMessageDialog(this, "Please select a gender (Sex).", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        String email = emailField.getText().trim();
        if (email.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Email is a required field.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            emailField.requestFocus(); return false;
        }
        String emailRegex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$";
        if (!email.matches(emailRegex)) {
            JOptionPane.showMessageDialog(this, "Please enter a valid email address.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            emailField.requestFocus(); return false;
        }
        return true;
    }

    // --- NEW: Reusable method for printing the form ---
    private void printForm() {
        PrinterJob pj = PrinterJob.getPrinterJob();
        pj.setJobName("Print Application Form");
        pj.setPrintable((pg, pf, pageNum) -> {
            if (pageNum > 0) return Printable.NO_SUCH_PAGE;
            Graphics2D g2 = (Graphics2D) pg;
            g2.translate(pf.getImageableX(), pf.getImageableY());
            double scale = Math.min(pf.getImageableWidth() / formPanel.getWidth(), pf.getImageableHeight() / formPanel.getHeight());
            if (scale < 1.0) g2.scale(scale, scale);
            formPanel.printAll(g2);
            return Printable.PAGE_EXISTS;
        });
        if (!pj.printDialog()) return;
        try {
            pj.print();
        } catch (PrinterException ex) {
            JOptionPane.showMessageDialog(this, "Cannot print the form: " + ex.getMessage(), "Print Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();

        if (e.getSource() == uploadButton) {
            JFileChooser fileChooser = new JFileChooser();
            FileNameExtensionFilter filter = new FileNameExtensionFilter("Image Files", "jpg", "png", "gif", "jpeg");
            fileChooser.setFileFilter(filter);
            if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                photoPath = selectedFile.getAbsolutePath();
                try {
                    ImageIcon imageIcon = new ImageIcon(photoPath);
                    Container photoContainer = photoPreviewLabel.getParent();
                    Image image = imageIcon.getImage().getScaledInstance(photoContainer.getWidth(), photoContainer.getHeight(), Image.SCALE_SMOOTH);
                    photoPreviewLabel.setIcon(new ImageIcon(image));
                    photoPreviewLabel.setText("");
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Error loading image!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        } else if (e.getSource() == submitButton) {
            if (validateForm()) {
                try {
                    String facultyId = "F" + System.currentTimeMillis();
                    
                    // --- Get data from all fields ---
                    List<String> postsList = new ArrayList<>();
                    if (seniorSecTeacher.isSelected()) postsList.add("Senior Secondary Teacher");
                    if (secondaryTeacher.isSelected()) postsList.add("Secondary Teacher");
                    if (primaryTeacher.isSelected()) postsList.add("Primary Teacher");
                    if (librarian.isSelected()) postsList.add("Librarian");
                    if (pet.isSelected()) postsList.add("Physical Education Teacher");
                    if (musicTeacher.isSelected()) postsList.add("Music Teacher (Vocal)");
                    if (others.isSelected()) postsList.add("Others");
                    String postsAppliedFor = String.join(", ", postsList);

                    List<String> statesList = new ArrayList<>();
                    if (assam.isSelected()) statesList.add("Assam");
                    if (arunachal.isSelected()) statesList.add("Arunachal Pradesh");
                    if (nagaland.isSelected()) statesList.add("Nagaland");
                    String statePreference = String.join(", ", statesList);
                    
                    String subject = subjectField.getText();
                    String fullName = nameField.getText();
                    String fathersName = fathersNameField.getText();
                    String sex = maleRadio.isSelected() ? "Male" : "Female";
                    String maritalStatus = marriedRadio.isSelected() ? "Married" : (unmarriedRadio.isSelected() ? "Unmarried" : null);
                    String dob = dobField.getText();
                    Integer age = ageField.getText().isEmpty() ? null : Integer.parseInt(ageField.getText());
                    String nationality = nationalityField.getText();
                    String religion = religionField.getText();
                    String permanentAddress = permanentAddressArea.getText();
                    String permanentTel = telNoField.getText();
                    String permanentPin = pinField.getText();
                    String presentAddress = presentAddressArea.getText();
                    String presentTel = presentTelNoField.getText();
                    String presentPin = presentPinField.getText();
                    String email = emailField.getText();

                    // --- Database INSERT Query ---
                    conn conn = new conn();
                    String query = "INSERT INTO teachers (faculty_id, posts_applied_for, state_preference, subject, full_name, fathers_name, sex, marital_status, date_of_birth, age, nationality, religion, permanent_address, permanent_tel_no, permanent_pin, present_address, present_tel_no, present_pin, email, photo_path) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
                    
                    PreparedStatement pstmt = conn.c.prepareStatement(query);
                    pstmt.setString(1, facultyId); pstmt.setString(2, postsAppliedFor);
                    pstmt.setString(3, statePreference); pstmt.setString(4, subject);
                    pstmt.setString(5, fullName); pstmt.setString(6, fathersName);
                    pstmt.setString(7, sex); pstmt.setString(8, maritalStatus);
                    pstmt.setString(9, dob);
                    if (age != null) pstmt.setInt(10, age); else pstmt.setNull(10, java.sql.Types.INTEGER);
                    pstmt.setString(11, nationality); pstmt.setString(12, religion);
                    pstmt.setString(13, permanentAddress); pstmt.setString(14, permanentTel);
                    pstmt.setString(15, permanentPin); pstmt.setString(16, presentAddress);
                    pstmt.setString(17, presentTel); pstmt.setString(18, presentPin);
                    pstmt.setString(19, email); pstmt.setString(20, photoPath);

                    pstmt.executeUpdate();
                    
                    // --- UPDATED: Ask user if they want to print ---
                    int response = JOptionPane.showConfirmDialog(
                        this,
                        "Faculty Details Inserted Successfully. Faculty ID: " + facultyId + "\n\nWould you like to print this form?",
                        "Submission Successful",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.INFORMATION_MESSAGE
                    );

                    if (response == JOptionPane.YES_OPTION) {
                        printForm();
                    }

                    setVisible(false); // Close form after submission and optional printing

                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(this, "Error inserting data into database: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        } else if (e.getSource() == cancelButton) {
            setVisible(false);
            dispose();
        } else if (e.getSource() == printButton) {
            // --- UPDATED: Call the reusable print method ---
            printForm();
        } else if (command.equals("Search/Update Faculty")) {
            JOptionPane.showMessageDialog(this, "This would open a new window to search and update faculty records.", "Feature Placeholder", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new addfaculty());
    }
}

