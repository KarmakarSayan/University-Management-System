package university.management.system;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class GenerateFeeReceipt extends JFrame implements ActionListener {

    private JPanel printablePanel;
    private JButton printButton, cancelButton;

    // --- UPDATED CONSTRUCTOR ---
    public GenerateFeeReceipt(String studentId, String name, String course, String branch, String semester, String amount) {
        // --- FRAME SETUP ---
        setTitle("Fee Payment Receipt");
        setSize(600, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(false);
        setLayout(new BorderLayout());

        // --- PRINTABLE PANEL ---
        printablePanel = new JPanel();
        printablePanel.setLayout(new BoxLayout(printablePanel, BoxLayout.Y_AXIS));
        printablePanel.setBackground(Color.WHITE);
        printablePanel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));

        // --- Build UI ---
        buildHeader();
        buildDetailsSection(studentId, name, course, branch, semester, amount);
        buildFooter();
        
        add(new JScrollPane(printablePanel), BorderLayout.CENTER);

        // --- BUTTONS ---
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
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        headerPanel.setOpaque(false);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JLabel title = new JLabel("UNIVERSITY MANAGEMENT SYSTEM");
        title.setFont(new Font("Serif", Font.BOLD, 24));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitle = new JLabel("FEE RECEIPT");
        subtitle.setFont(new Font("Tahoma", Font.BOLD, 18));
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        headerPanel.add(title);
        headerPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        headerPanel.add(subtitle);
        headerPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        headerPanel.add(new JSeparator());
        
        printablePanel.add(headerPanel);
    }

    private void buildDetailsSection(String studentId, String name, String course, String branch, String semester, String amount) {
        JPanel detailsPanel = new JPanel(new GridBagLayout());
        detailsPanel.setOpaque(false);
        detailsPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(8, 5, 8, 5);
        
        // Get current date
        String paymentDate = LocalDate.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));

        addDetailRow(detailsPanel, gbc, "Roll Number:", studentId, 0);
        addDetailRow(detailsPanel, gbc, "Student Name:", name, 1);
        addDetailRow(detailsPanel, gbc, "Course:", course, 2);
        addDetailRow(detailsPanel, gbc, "Branch:", branch, 3);
        addDetailRow(detailsPanel, gbc, "Semester:", semester, 4);
        addDetailRow(detailsPanel, gbc, "Amount Paid:", "₹" + amount, 5);
        addDetailRow(detailsPanel, gbc, "Payment Date:", paymentDate, 6);

        printablePanel.add(detailsPanel);
    }

    private void addDetailRow(JPanel panel, GridBagConstraints gbc, String label, String value, int y) {
        JLabel labelComponent = new JLabel(label);
        labelComponent.setFont(new Font("Tahoma", Font.BOLD, 14));
        gbc.gridx = 0;
        gbc.gridy = y;
        gbc.weightx = 0.3;
        panel.add(labelComponent, gbc);

        JLabel valueComponent = new JLabel(value);
        valueComponent.setFont(new Font("Tahoma", Font.PLAIN, 14));
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        panel.add(valueComponent, gbc);
    }

    private void buildFooter() {
        printablePanel.add(new JSeparator());
        JPanel footerPanel = new JPanel();
        footerPanel.setOpaque(false);
        footerPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        JLabel footerLabel = new JLabel("This is a computer-generated receipt and does not require a signature.");
        footerLabel.setFont(new Font("Tahoma", Font.ITALIC, 12));
        footerPanel.add(footerLabel);
        printablePanel.add(footerPanel);
    }

    private void printReceipt() {
        PrinterJob pj = PrinterJob.getPrinterJob();
        pj.setJobName("Print Fee Receipt");

        pj.setPrintable((pg, pf, pageNum) -> {
            if (pageNum > 0) {
                return Printable.NO_SUCH_PAGE;
            }

            Graphics2D g2 = (Graphics2D) pg;

            // Get the preferred size of the content panel to be printed
            Dimension panelSize = printablePanel.getPreferredSize();
            double printableWidth = pf.getImageableWidth();
            double printableHeight = pf.getImageableHeight();

            // Calculate the scaling factor to fit the content within the page's printable area
            // We use the minimum of the horizontal and vertical scaling to ensure the entire content fits.
            double scale = Math.min(printableWidth / panelSize.getWidth(), printableHeight / panelSize.getHeight());

            // If the content is smaller than the page, do not scale it up
            if (scale > 1.0) {
                scale = 1.0;
            }

            // Calculate the translation (x, y) needed to center the content on the page
            double xTranslation = (printableWidth - (panelSize.getWidth() * scale)) / 2;
            double yTranslation = (printableHeight - (panelSize.getHeight() * scale)) / 2;

            // Apply the translation to move the content to the center of the printable area
            g2.translate(pf.getImageableX() + xTranslation, pf.getImageableY() + yTranslation);

            // Apply the scaling
            g2.scale(scale, scale);

            // Ensure the panel's layout is valid before painting
            printablePanel.doLayout();
            
            // Use print() instead of paint() as it is designed for printing components and their children
            printablePanel.print(g2);

            return Printable.PAGE_EXISTS;
        });

        if (!pj.printDialog()) {
            return;
        }

        try {
            pj.print();
        } catch (PrinterException ex) {
            JOptionPane.showMessageDialog(this, "Cannot print receipt: " + ex.getMessage(), "Print Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == printButton) {
            printReceipt();
        } else if (e.getSource() == cancelButton) {
            this.dispose();
        }
    }
}