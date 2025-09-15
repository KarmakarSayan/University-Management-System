package university.management.system;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;

public class main_class extends JFrame implements ActionListener {

    main_class() {
        // --- FRAME SETUP ---
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        ImageIcon i1 = new ImageIcon(ClassLoader.getSystemResource("icons/third.jpg"));
        Image i2 = i1.getImage().getScaledInstance(screenSize.width, screenSize.height, Image.SCALE_SMOOTH);
        ImageIcon i3 = new ImageIcon(i2);
        JLabel background = new JLabel(i3);
        background.setLayout(new BorderLayout()); // Use BorderLayout for top/bottom panels
        add(background);

        // --- Title Panel at the Top ---
        JPanel titlePanel = new JPanel();
        titlePanel.setOpaque(false); // Make it transparent
        titlePanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0)); // Add some padding
        JLabel title = new JLabel("University Management System");
        title.setFont(new Font("Serif", Font.BOLD, 60));
        title.setForeground(Color.WHITE);
        titlePanel.add(title);
        background.add(titlePanel, BorderLayout.NORTH);

        // --- Developer Credit Panel at the Bottom ---
        JPanel devPanel = new JPanel();
        devPanel.setOpaque(false); // Make it transparent
        devPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0)); // Add some padding
        JLabel developer = new JLabel("Developed by: Sayan Karmakar");
        developer.setFont(new Font("Serif", Font.PLAIN, 24));
        developer.setForeground(Color.WHITE);
        devPanel.add(developer);
        background.add(devPanel, BorderLayout.SOUTH);

        // --- MENU BAR ---
        JMenuBar mb = new JMenuBar();
        Font menuFont = new Font("Tahoma", Font.BOLD, 20); // Thicker font for menus

        addMenu(mb, "New Information", menuFont, Color.BLUE, new String[]{"New Faculty Information", "New Student Information"});
        addMenu(mb, "View Details", menuFont, Color.RED, new String[]{"View Faculty Details", "View Student Details"});
        addMenu(mb, "Apply Leave", menuFont, Color.BLUE, new String[]{"Faculty Leave", "Student Leave"});
        addMenu(mb, "Leave Details", menuFont, Color.RED, new String[]{"View Faculty Leave Details", "View Student Leave Details"});
        addMenu(mb, "Examination", menuFont, Color.BLUE, new String[]{"Examination Results", "Enter Marks"});
        addMenu(mb, "Update Details", menuFont, Color.RED, new String[]{"Update Faculty Details", "Update Student Details"});
        addMenu(mb, "Fee Details", menuFont, Color.BLUE, new String[]{"Fee Structure", "Student Fee Form"});
        addMenu(mb, "Utility", menuFont, Color.RED, new String[]{"Notepad", "Calculator"});
        addMenu(mb, "About", menuFont, Color.BLUE, new String[]{"About Us"});
        
        // --- Exit Menu ---
        JMenu exitMenu = new JMenu("Exit");
        exitMenu.setFont(menuFont);
        exitMenu.setForeground(Color.RED);
        
        // --- NEW: Logout Menu Item ---
        JMenuItem logoutItem = new JMenuItem("Logout");
        logoutItem.setFont(new Font("Serif", Font.PLAIN, 16));
        logoutItem.setBackground(Color.WHITE);
        logoutItem.addActionListener(this);
        exitMenu.add(logoutItem);

        JMenuItem exitItem = new JMenuItem("Exit Application");
        exitItem.setFont(new Font("Serif", Font.PLAIN, 16));
        exitItem.setBackground(Color.WHITE);
        exitItem.addActionListener(this);
        exitMenu.add(exitItem);
        
        mb.add(exitMenu);

        setJMenuBar(mb);

        // Frame properties
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }
    
    // Helper method to create menus and menu items
    private void addMenu(JMenuBar menuBar, String menuName, Font menuFont, Color color, String[] itemNames) {
        JMenu menu = new JMenu(menuName);
        menu.setFont(menuFont);
        menu.setForeground(color);
        
        Font menuItemFont = new Font("Monospaced", Font.PLAIN, 16);
        for (String itemName : itemNames) {
            JMenuItem menuItem = new JMenuItem(itemName);
            menuItem.setFont(menuItemFont);
            menuItem.setBackground(Color.WHITE);
            menuItem.addActionListener(this);
            menu.add(menuItem);
        }
        menuBar.add(menu);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();
        
        switch(command) {
            case "New Faculty Information": new addfaculty(); break;
            case "New Student Information": new AddStudent(); break;
            case "View Faculty Details": new ViewTeacherDetails(); break;
            case "View Student Details": new ViewStudentDetails(); break;
            case "Faculty Leave": new TeacherLeave(); break;
            case "Student Leave": new StudentLeave(); break;
            case "View Faculty Leave Details": new ViewTeacherLeave(); break;
            case "View Student Leave Details": new ViewStudentLeave(); break;
            case "Examination Results": new ExaminationDetails(); break;
            case "Enter Marks": new EnterMarks(); break;
            case "Update Faculty Details": new ViewTeacherDetails(); break;
            case "Update Student Details": new ViewStudentDetails(); break;
            case "Fee Structure": new FeeStructure(); break;
            case "Student Fee Form": new StudentFeeForm(); break;
            case "About Us": new About(); break;
            case "Notepad":
                try { Runtime.getRuntime().exec("notepad.exe"); } catch (Exception ex) { ex.printStackTrace(); }
                break;
            case "Calculator":
                try { Runtime.getRuntime().exec("calc.exe"); } catch (Exception ex) { ex.printStackTrace(); }
                break;
            // --- NEW: Handle Logout Action ---
            case "Logout":
                setVisible(false);
                dispose();
                new Login();
                break;
            case "Exit Application":
                System.exit(0);
                break;
        }
    }

    public static void main(String[] args) {
        new main_class();
    }
}

