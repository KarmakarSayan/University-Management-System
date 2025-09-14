package university.management.system;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;

public class ViewStudentLeave extends JFrame implements ActionListener {

    JComboBox<String> rollNumberComboBox;
    JTable leaveTable;
    JButton searchButton, printButton, cancelButton;

    public ViewStudentLeave() {
        // --- FRAME SETUP ---
        setTitle("View Student Leave Details");
        setSize(1000, 600);
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
        populateRollNumbers();
        topPanel.add(rollNumberComboBox);
        
        searchButton = new JButton("Search");
        searchButton.addActionListener(this);
        topPanel.add(searchButton);

        printButton = new JButton("Print");
        printButton.addActionListener(this);
        topPanel.add(printButton);

        cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(this);
        topPanel.add(cancelButton);

        add(topPanel, BorderLayout.NORTH);

        // --- TABLE for Displaying Data ---
        leaveTable = new JTable();
        JScrollPane scrollPane = new JScrollPane(leaveTable);
        add(scrollPane, BorderLayout.CENTER);

        // Load all leave records initially
        loadAllLeave();

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
            leaveTable.setModel(tableModel);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void loadAllLeave() {
        try {
            conn c = new conn();
            ResultSet rs = c.s.executeQuery("SELECT * FROM student_leave");
            populateTable(rs);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == searchButton) {
            String rollNo = (String) rollNumberComboBox.getSelectedItem();
            try {
                conn c = new conn();
                String query = "SELECT * FROM student_leave WHERE student_id = '" + rollNo + "'";
                ResultSet rs = c.s.executeQuery(query);
                populateTable(rs);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } else if (e.getSource() == printButton) {
            try {
                leaveTable.print();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } else if (e.getSource() == cancelButton) {
            this.dispose();
        }
    }
    
    public static void main(String[] args) {
        new ViewStudentLeave();
    }
}
