package university.management.system;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;

public class ExaminationDetails extends JFrame implements ActionListener {

    JTextField searchField;
    JButton searchButton, printResultButton, cancelButton; // Added printResultButton
    JTable marksTable;

    public ExaminationDetails() {
        // --- FRAME SETUP ---
        setTitle("Examination Results");
        setSize(1000, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
        getContentPane().setBackground(new Color(240, 248, 255));

        // --- TOP PANEL for Searching ---
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(new Color(240, 248, 255));
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.setBackground(new Color(240, 248, 255));
        
        JLabel searchLabel = new JLabel("Enter Roll Number:");
        searchLabel.setFont(new Font("Tahoma", Font.BOLD, 14));
        searchPanel.add(searchLabel);

        searchField = new JTextField(15);
        searchPanel.add(searchField);

        searchButton = new JButton("Search");
        searchButton.addActionListener(this);
        searchPanel.add(searchButton);
        
        // --- NEW: Add the Print Result Button ---
        printResultButton = new JButton("Print Result Card");
        printResultButton.addActionListener(this);
        searchPanel.add(printResultButton);
        
        topPanel.add(searchPanel, BorderLayout.CENTER);
        
        add(topPanel, BorderLayout.NORTH);

        // --- TABLE for Displaying Marks ---
        marksTable = new JTable();
        JScrollPane scrollPane = new JScrollPane(marksTable);
        add(scrollPane, BorderLayout.CENTER);

        // --- BOTTOM PANEL for Buttons ---
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(new Color(240, 248, 255));
        
        cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(this);
        buttonPanel.add(cancelButton);
        
        add(buttonPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    private void populateTable(ResultSet rs) {
        try {
            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();
            DefaultTableModel tableModel = new DefaultTableModel();

            String[] columnNames = {"Semester", "Subject 1", "Marks 1", "Subject 2", "Marks 2", "Subject 3", "Marks 3", "Subject 4", "Marks 4", "Subject 5", "Marks 5"};
            for (String colName : columnNames) {
                tableModel.addColumn(colName);
            }

            while (rs.next()) {
                Object[] row = new Object[columnNames.length];
                row[0] = rs.getString("semester");
                row[1] = rs.getString("subject1");
                row[2] = rs.getString("marks1");
                row[3] = rs.getString("subject2");
                row[4] = rs.getString("marks2");
                row[5] = rs.getString("subject3");
                row[6] = rs.getString("marks3");
                row[7] = rs.getString("subject4");
                row[8] = rs.getString("marks4");
                row[9] = rs.getString("subject5");
                row[10] = rs.getString("marks5");
                tableModel.addRow(row);
            }

            marksTable.setModel(tableModel);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == searchButton) {
            String rollNo = searchField.getText().trim();
            if (rollNo.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter a roll number to search.", "Validation Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                conn c = new conn();
                String query = "SELECT semester, subject1, marks1, subject2, marks2, subject3, marks3, subject4, marks4, subject5, marks5 FROM marks WHERE student_id = ?";
                PreparedStatement pstmt = c.c.prepareStatement(query);
                pstmt.setString(1, rollNo);
                ResultSet rs = pstmt.executeQuery();

                if (!rs.isBeforeFirst()) {
                    JOptionPane.showMessageDialog(this, "No marks found for this roll number.", "Not Found", JOptionPane.INFORMATION_MESSAGE);
                    marksTable.setModel(new DefaultTableModel());
                } else {
                    populateTable(rs);
                }

            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error fetching marks: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            }
        } else if (e.getSource() == printResultButton) {
            // --- NEW: Logic to open the result card window ---
            String rollNo = searchField.getText().trim();
            if (rollNo.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter a roll number to generate the result card.", "Validation Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            new GenerateResultCard(rollNo);

        } else if (e.getSource() == cancelButton) {
            this.dispose();
        }
    }

    public static void main(String[] args) {
        new ExaminationDetails();
    }
}

