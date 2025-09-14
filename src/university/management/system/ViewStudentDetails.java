package university.management.system;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;

public class ViewStudentDetails extends JFrame implements ActionListener {

    JComboBox<String> rollNumberComboBox;
    JTable studentTable;
    JButton searchButton, printButton, addButton, updateButton, cancelButton;

    public ViewStudentDetails() {
        // --- FRAME SETUP ---
        setTitle("View Student Details");
        setSize(1200, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
        getContentPane().setBackground(new Color(210, 225, 240));

        // --- TOP PANEL for Searching ---
        JPanel topPanel = new JPanel();
        topPanel.setBackground(new Color(210, 225, 240));
        topPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 10));

        JLabel searchLabel = new JLabel("Search by Roll Number");
        searchLabel.setFont(new Font("Tahoma", Font.BOLD, 14));
        topPanel.add(searchLabel);

        rollNumberComboBox = new JComboBox<>();
        populateRollNumbers(); // Fill the dropdown with roll numbers from DB
        topPanel.add(rollNumberComboBox);
        
        searchButton = new JButton("Search");
        searchButton.addActionListener(this);
        topPanel.add(searchButton);

        printButton = new JButton("Print");
        printButton.addActionListener(this);
        topPanel.add(printButton);

        addButton = new JButton("Add");
        addButton.addActionListener(this);
        topPanel.add(addButton);

        updateButton = new JButton("Update");
        updateButton.addActionListener(this);
        topPanel.add(updateButton);

        cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(this);
        topPanel.add(cancelButton);

        add(topPanel, BorderLayout.NORTH);

        // --- TABLE for Displaying Data ---
        studentTable = new JTable();
        JScrollPane scrollPane = new JScrollPane(studentTable);
        add(scrollPane, BorderLayout.CENTER);

        // Load all students initially
        loadAllStudents();

        setVisible(true);
    }

    private void populateRollNumbers() {
        try {
            rollNumberComboBox.removeAllItems(); // Clear old items before repopulating
            conn c = new conn();
            ResultSet rs = c.s.executeQuery("SELECT student_id FROM students ORDER BY student_id");
            while (rs.next()) {
                rollNumberComboBox.addItem(rs.getString("student_id"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void populateTable(ResultSet rs) {
        try {
            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();
            DefaultTableModel tableModel = new DefaultTableModel();
            for (int i = 1; i <= columnCount; i++) {
                tableModel.addColumn(metaData.getColumnName(i));
            }
            while (rs.next()) {
                Object[] row = new Object[columnCount];
                for (int i = 1; i <= columnCount; i++) {
                    row[i - 1] = rs.getObject(i);
                }
                tableModel.addRow(row);
            }
            studentTable.setModel(tableModel);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void loadAllStudents() {
        try {
            conn c = new conn();
            ResultSet rs = c.s.executeQuery("SELECT * FROM students");
            populateTable(rs);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void refreshTable() {
        loadAllStudents();
        populateRollNumbers();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == searchButton) {
            String rollNo = (String) rollNumberComboBox.getSelectedItem();
            try {
                conn c = new conn();
                String query = "SELECT * FROM students WHERE student_id = '" + rollNo + "'";
                ResultSet rs = c.s.executeQuery(query);
                populateTable(rs);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } else if (e.getSource() == printButton) {
            try {
                studentTable.print();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } else if (e.getSource() == addButton) {
            new AddStudent();
        } else if (e.getSource() == updateButton) {
            String selectedRollNo = (String) rollNumberComboBox.getSelectedItem();
            if (selectedRollNo != null) {
                new UpdateStudent(selectedRollNo, this);
            } else {
                JOptionPane.showMessageDialog(this, "Please select a student to update.", "No Student Selected", JOptionPane.WARNING_MESSAGE);
            }
        } else if (e.getSource() == cancelButton) {
            this.dispose();
        }
    }
    
    public static void main(String[] args) {
        new ViewStudentDetails();
    }
}

