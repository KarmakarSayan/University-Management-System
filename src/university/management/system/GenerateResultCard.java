// IMPORTANT: This class must be saved in a file named "GenerateResultCard.java"
package university.management.system;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class GenerateResultCard extends JFrame implements ActionListener {

    private String studentId;
    private JPanel printablePanel; // The panel that will be printed
    private JButton printButton, cancelButton;
    private Image watermarkImage;

    // --- Custom Panel with Watermark ---
    private class WatermarkPanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (watermarkImage != null) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.1f));
                int x = (this.getWidth() - watermarkImage.getWidth(null)) / 2;
                int y = (this.getHeight() - watermarkImage.getHeight(null)) / 2;
                g2d.drawImage(watermarkImage, x, y, this);
                g2d.dispose();
            }
        }
    }

    public GenerateResultCard(String studentId) {
        this.studentId = studentId;

        // Load watermark image
        try {
            URL imageUrl = ClassLoader.getSystemResource("icons/exam.png");
            if (imageUrl != null) {
                watermarkImage = new ImageIcon(imageUrl).getImage();
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Watermark image not found.");
        }

        // --- FRAME SETUP ---
        setTitle("Student Result Card");
        setSize(850, 950);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(false);
        setLayout(new BorderLayout());

        // --- PRINTABLE PANEL SETUP ---
        printablePanel = new WatermarkPanel();
        printablePanel.setLayout(new BoxLayout(printablePanel, BoxLayout.Y_AXIS));
        printablePanel.setBackground(Color.WHITE);
        printablePanel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        printablePanel.setOpaque(false);

        // --- Build the result card UI ---
        buildHeader();
        buildStudentInfo();
        buildMarksSection();
        buildFooter();

        JScrollPane scrollPane = new JScrollPane(printablePanel);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setOpaque(false);
        add(scrollPane, BorderLayout.CENTER);

        // --- BUTTONS PANEL ---
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        printButton = new JButton("Print");
        printButton.addActionListener(this);
        cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(this);
        buttonPanel.add(printButton);
        buttonPanel.add(cancelButton);
        add(buttonPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    private void buildHeader() {
        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        titlePanel.setOpaque(false);
        titlePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JLabel schoolName = new JLabel("UNIVERSITY MANAGEMENT SYSTEM");
        schoolName.setFont(new Font("Serif", Font.BOLD, 28));
        schoolName.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel address = new JLabel("A Co-educational English / Hindi Medium School, Affiliated to M.P. Govt.");
        address.setFont(new Font("Tahoma", Font.PLAIN, 14));
        address.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        titlePanel.add(schoolName);
        titlePanel.add(address);
        
        printablePanel.add(titlePanel);

        JLabel session = new JLabel("Annual Result Session: 2024-25");
        session.setFont(new Font("Tahoma", Font.BOLD, 18));
        session.setAlignmentX(Component.CENTER_ALIGNMENT);
        session.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.BLACK),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));

        printablePanel.add(session);
        printablePanel.add(Box.createRigidArea(new Dimension(0, 10)));
    }

    private void buildStudentInfo() {
        JPanel infoPanel = new JPanel(new BorderLayout(20, 10));
        infoPanel.setOpaque(false);
        infoPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        JPanel detailsGrid = new JPanel(new GridBagLayout());
        detailsGrid.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(4, 5, 4, 5);
        
        // --- FIXED: Create a container for the photo to control alignment ---
        JPanel photoContainer = new JPanel(new GridBagLayout());
        photoContainer.setOpaque(false);

        JLabel photoLabel = new JLabel();
        photoLabel.setPreferredSize(new Dimension(120, 150));
        photoLabel.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        photoLabel.setHorizontalAlignment(SwingConstants.CENTER);
        photoLabel.setText("Photo");
        
        photoContainer.add(photoLabel, new GridBagConstraints());
        infoPanel.add(photoContainer, BorderLayout.EAST);

        // Fetch and display student details and photo
        try {
            conn c = new conn();
            String query = "SELECT *, photo_path FROM students WHERE student_id = ?";
            PreparedStatement pstmt = c.c.prepareStatement(query);
            pstmt.setString(1, this.studentId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                addInfoRow(detailsGrid, gbc, "Student's Name", ":", rs.getString("full_name"), 0);
                addInfoRow(detailsGrid, gbc, "Father's Name", ":", rs.getString("fathers_name"), 1);
                addInfoRow(detailsGrid, gbc, "Roll Number", ":", rs.getString("student_id"), 2);
                addInfoRow(detailsGrid, gbc, "Date of Birth", ":", rs.getString("date_of_birth"), 3);
                addInfoRow(detailsGrid, gbc, "Course", ":", rs.getString("course"), 4);
                addInfoRow(detailsGrid, gbc, "Branch", ":", rs.getString("branch"), 5);

                // --- NEW: Fetch and display the student's photo ---
                String photoPath = rs.getString("photo_path");
                if (photoPath != null && !photoPath.isEmpty()) {
                    ImageIcon imageIcon = new ImageIcon(photoPath);
                    Image image = imageIcon.getImage().getScaledInstance(120, 150, Image.SCALE_SMOOTH);
                    photoLabel.setIcon(new ImageIcon(image));
                    photoLabel.setText(""); // Remove "Photo" text
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        infoPanel.add(detailsGrid, BorderLayout.CENTER);
        printablePanel.add(infoPanel);
    }

    private void addInfoRow(JPanel panel, GridBagConstraints gbc, String label, String separator, String value, int y) {
        JLabel labelComponent = new JLabel(label);
        labelComponent.setFont(new Font("Tahoma", Font.BOLD, 14));
        gbc.gridx = 0; gbc.gridy = y; gbc.weightx = 0;
        panel.add(labelComponent, gbc);

        JLabel sepComponent = new JLabel(separator);
        sepComponent.setFont(new Font("Tahoma", Font.BOLD, 14));
        gbc.gridx = 1;
        panel.add(sepComponent, gbc);

        JLabel valueComponent = new JLabel(value);
        valueComponent.setFont(new Font("Tahoma", Font.PLAIN, 14));
        gbc.gridx = 2; gbc.weightx = 1.0;
        panel.add(valueComponent, gbc);
    }
    
    private void buildMarksSection() {
        JPanel marksContainer = new JPanel();
        marksContainer.setLayout(new BoxLayout(marksContainer, BoxLayout.Y_AXIS));
        marksContainer.setOpaque(false);
        marksContainer.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createEmptyBorder(10, 10, 10, 10),
            BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLACK), "ASSESSMENT OF ACADEMIC AREAS")
        ));

        try {
            conn c = new conn();
            String query = "SELECT * FROM marks WHERE student_id = ? ORDER BY semester";
            PreparedStatement pstmt = c.c.prepareStatement(query);
            pstmt.setString(1, this.studentId);
            ResultSet rs = pstmt.executeQuery();
            
            if (!rs.isBeforeFirst()) {
                marksContainer.add(new JLabel("No marks data found for this student."));
            } else {
                 while(rs.next()) {
                    marksContainer.add(createSemesterTable(rs));
                    marksContainer.add(Box.createRigidArea(new Dimension(0, 10)));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            marksContainer.add(new JLabel("Error loading marks data."));
        }

        printablePanel.add(marksContainer);
    }

    private JScrollPane createSemesterTable(ResultSet rs) throws Exception {
        String[] columnNames = {"SUBJECT", "MARKS"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);

        addMarkRow(model, rs.getString("subject1"), rs.getString("marks1"));
        addMarkRow(model, rs.getString("subject2"), rs.getString("marks2"));
        addMarkRow(model, rs.getString("subject3"), rs.getString("marks3"));
        addMarkRow(model, rs.getString("subject4"), rs.getString("marks4"));
        addMarkRow(model, rs.getString("subject5"), rs.getString("marks5"));

        JTable table = new JTable(model);
        table.setFillsViewportHeight(true);
        table.setOpaque(false);
        table.getTableHeader().setFont(new Font("Tahoma", Font.BOLD, 12));
        
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createTitledBorder(rs.getString("semester").toUpperCase()));
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);

        int rowCount = table.getRowCount();
        int rowHeight = table.getRowHeight();
        int headerHeight = table.getTableHeader().getPreferredSize().height;
        int preferredHeight = (rowCount * rowHeight) + headerHeight + 20;
        scrollPane.setPreferredSize(new Dimension(0, preferredHeight));
        
        return scrollPane;
    }
    
    private void addMarkRow(DefaultTableModel model, String subject, String marks) {
        if (subject != null && !subject.trim().isEmpty() && marks != null && !marks.trim().isEmpty()) {
            model.addRow(new Object[]{subject, marks});
        }
    }
    
    private void buildFooter() {
        JPanel footerPanel = new JPanel(new GridLayout(1, 3, 20, 10));
        footerPanel.setOpaque(false);
        footerPanel.setBorder(BorderFactory.createEmptyBorder(50, 20, 20, 20));
        
        footerPanel.add(new JLabel("Class Teacher", SwingConstants.CENTER));
        footerPanel.add(new JLabel("Parent", SwingConstants.CENTER));
        footerPanel.add(new JLabel("Principal", SwingConstants.CENTER));

        printablePanel.add(footerPanel);
    }

    private void printResultCard() {
        PrinterJob pj = PrinterJob.getPrinterJob();
        
        if (pj == null) {
            JOptionPane.showMessageDialog(this, 
                "No printer services are available on your system.", 
                "Print Error", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }

        pj.setJobName("Print Result Card");
        
        pj.setPrintable(new Printable() {
            public int print(Graphics pg, PageFormat pf, int pageNum) {
                if (pageNum > 0) {
                    return Printable.NO_SUCH_PAGE;
                }
                
                Graphics2D g2 = (Graphics2D) pg;
                g2.translate(pf.getImageableX(), pf.getImageableY());
                
                printablePanel.setSize(printablePanel.getPreferredSize());
                
                double scale = Math.min(pf.getImageableWidth() / printablePanel.getWidth(), pf.getImageableHeight() / printablePanel.getHeight());
                
                if (scale < 1.0) {
                    g2.scale(scale, scale);
                }
                
                printablePanel.paint(g2);
                
                return Printable.PAGE_EXISTS;
            }
        });
        
        if (pj.printDialog() == false) {
            return;
        }
        
        try {
            pj.print();
        } catch (PrinterException ex) {
            JOptionPane.showMessageDialog(this, "An error occurred while printing: " + ex.getMessage(), "Print Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == printButton) {
            printResultCard();
        } else if (e.getSource() == cancelButton) {
            this.dispose();
        }
    }
}

