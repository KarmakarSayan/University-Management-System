package university.management.system;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;

public class FeeStructure extends JFrame {

    public FeeStructure() {
        // --- FRAME SETUP ---
        setTitle("Fee Structure");
        setSize(1000, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
        getContentPane().setBackground(new Color(240, 248, 255));

        // --- HEADER ---
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(240, 248, 255));
        JLabel headerLabel = new JLabel("University Fee Structure");
        headerLabel.setFont(new Font("Serif", Font.BOLD, 28));
        headerPanel.add(headerLabel);
        add(headerPanel, BorderLayout.NORTH);

        // --- TABLE for Displaying Fee Data ---
        JTable feeTable = new JTable();
        
        // --- Populate the table with data from the database ---
        try {
            conn c = new conn();
            ResultSet rs = c.s.executeQuery("SELECT * FROM fee");
            
            // Manually populate the JTable without external libraries
            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();
            DefaultTableModel tableModel = new DefaultTableModel();

            // Add column names to the model
            for (int i = 1; i <= columnCount; i++) {
                tableModel.addColumn(metaData.getColumnName(i).replace("_", " ").toUpperCase());
            }

            // Add rows to the model
            while (rs.next()) {
                Object[] row = new Object[columnCount];
                for (int i = 1; i <= columnCount; i++) {
                    row[i - 1] = rs.getObject(i);
                }
                tableModel.addRow(row);
            }

            feeTable.setModel(tableModel);

        } catch (Exception e) {
            e.printStackTrace();
        }

        JScrollPane scrollPane = new JScrollPane(feeTable);
        add(scrollPane, BorderLayout.CENTER);

        setVisible(true);
    }

    public static void main(String[] args) {
        new FeeStructure();
    }
}
