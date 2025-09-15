package university.management.system;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;

public class About extends JFrame implements ActionListener {

    private JButton closeButton;

    public About() {
        // --- FRAME SETUP ---
        setTitle("About University Management System");
        setSize(700, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(false);
        setLayout(new BorderLayout());
        getContentPane().setBackground(Color.WHITE);

        // --- IMAGE PANEL (Top) ---
        JPanel imagePanel = new JPanel();
        imagePanel.setBackground(Color.WHITE);
        try {
            URL imageUrl = ClassLoader.getSystemResource("icons/about.png");
            if (imageUrl != null) {
                ImageIcon i1 = new ImageIcon(imageUrl);
                Image i2 = i1.getImage().getScaledInstance(300, 200, Image.SCALE_DEFAULT);
                ImageIcon i3 = new ImageIcon(i2);
                JLabel image = new JLabel(i3);
                imagePanel.add(image);
            } else {
                imagePanel.add(new JLabel("Image not found: icons/about.png"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        add(imagePanel, BorderLayout.NORTH);

        // --- TEXT PANEL (Center) ---
        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.setBackground(Color.WHITE);
        textPanel.setBorder(BorderFactory.createEmptyBorder(10, 30, 10, 30));

        JLabel title = new JLabel("University Management System");
        title.setFont(new Font("Serif", Font.BOLD, 28));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel developedBy = new JLabel("Developed By: [Your Name Here]");
        developedBy.setFont(new Font("Tahoma", Font.PLAIN, 16));
        developedBy.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel contact = new JLabel("Contact: contact@university.com");
        contact.setFont(new Font("Tahoma", Font.PLAIN, 16));
        contact.setAlignmentX(Component.CENTER_ALIGNMENT);

        textPanel.add(title);
        textPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        textPanel.add(developedBy);
        textPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        textPanel.add(contact);
        
        add(textPanel, BorderLayout.CENTER);

        // --- BUTTON PANEL (Bottom) ---
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 10));
        buttonPanel.setBackground(Color.WHITE);
        closeButton = new JButton("Close");
        closeButton.addActionListener(this);
        buttonPanel.add(closeButton);
        add(buttonPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == closeButton) {
            this.dispose();
        }
    }

    public static void main(String[] args) {
        new About();
    }
}
