package university.management.system;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;

public class ViewTeacherDetails extends JFrame implements ActionListener {

    JComboBox<String> facultyIdComboBox;
    JTable teacherTable;
    JButton searchButton, printButton, addButton, updateButton, cancelButton;

    public ViewTeacherDetails() {
        // --- FRAME SETUP ---
        setTitle("View Teacher Details");
        setSize(1200, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
        getContentPane().setBackground(new Color(210, 225, 240));

        // --- TOP PANEL for Searching ---
        JPanel topPanel = new JPanel();
        topPanel.setBackground(new Color(210, 225, 240));
        topPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 10));

        JLabel searchLabel = new JLabel("Search by Faculty ID");
        searchLabel.setFont(new Font("Tahoma", Font.BOLD, 14));
        topPanel.add(searchLabel);

        facultyIdComboBox = new JComboBox<>();
        populateFacultyIds();
        topPanel.add(facultyIdComboBox);
        
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
        teacherTable = new JTable();
        JScrollPane scrollPane = new JScrollPane(teacherTable);
        add(scrollPane, BorderLayout.CENTER);

        loadAllTeachers();

        setVisible(true);
    }

    private void populateFacultyIds() {
        try {
            facultyIdComboBox.removeAllItems();
            conn c = new conn();
            ResultSet rs = c.s.executeQuery("SELECT faculty_id FROM teachers ORDER BY faculty_id");
            while (rs.next()) {
                facultyIdComboBox.addItem(rs.getString("faculty_id"));
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
            teacherTable.setModel(tableModel);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void loadAllTeachers() {
        try {
            conn c = new conn();
            ResultSet rs = c.s.executeQuery("SELECT * FROM teachers");
            populateTable(rs);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // --- NEW: Public method to allow other classes to refresh this table ---
    public void refreshTable() {
        loadAllTeachers();
        populateFacultyIds();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == searchButton) {
            String facultyId = (String) facultyIdComboBox.getSelectedItem();
            try {
                conn c = new conn();
                String query = "SELECT * FROM teachers WHERE faculty_id = '" + facultyId + "'";
                ResultSet rs = c.s.executeQuery(query);
                populateTable(rs);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } else if (e.getSource() == printButton) {
            try {
                teacherTable.print();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } else if (e.getSource() == addButton) {
            new addfaculty();
            this.dispose();
        } else if (e.getSource() == updateButton) {
            String selectedFacultyId = (String) facultyIdComboBox.getSelectedItem();
            if (selectedFacultyId != null) {
                new UpdateTeacher(selectedFacultyId, this);
            } else {
                JOptionPane.showMessageDialog(this, "Please select a teacher to update.", "No Teacher Selected", JOptionPane.WARNING_MESSAGE);
            }
        } else if (e.getSource() == cancelButton) {
            this.dispose();
        }
    }
    
    public static void main(String[] args) {
        new ViewTeacherDetails();
    }
}

