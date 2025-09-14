package university.management.system;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class main_class extends JFrame implements ActionListener { // extend JFrame

    main_class() {
        // Get screen size
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

        // Load and scale the image to full screen
        ImageIcon i1 = new ImageIcon(ClassLoader.getSystemResource("icons/third.jpg"));
        Image i2 = i1.getImage().getScaledInstance(screenSize.width, screenSize.height, Image.SCALE_SMOOTH);
        ImageIcon i3 = new ImageIcon(i2);

        // Background label
        JLabel background = new JLabel(i3);
        background.setLayout(new BorderLayout()); // for overlaying components

        // Title panel
        JPanel titlePanel = new JPanel();
        titlePanel.setOpaque(false); // transparent panel
        JLabel title = new JLabel("University Management System", SwingConstants.CENTER);
        title.setFont(new Font("Serif", Font.BOLD, 60));
        title.setForeground(Color.WHITE);
        titlePanel.add(title);
        background.add(titlePanel, BorderLayout.NORTH);

        // Developer panel
        JPanel devPanel = new JPanel();
        devPanel.setOpaque(false);
        JLabel developer = new JLabel("Developed by: Sayan Karmakar", SwingConstants.CENTER);
        developer.setFont(new Font("Serif", Font.BOLD, 30));
        developer.setForeground(Color.RED);
        devPanel.add(developer);
        background.add(devPanel, BorderLayout.SOUTH);

        // Add background to frame
        add(background);

        // Menu bar
        JMenuBar mb = new JMenuBar();
        JMenu information = new JMenu("Information");
        information.setFont(new Font("Serif", Font.BOLD, 20));
        information.setForeground(Color.BLUE);
        mb.add(information);

        // for items in information menu, you can add JMenuItems here
        JMenuItem facultyItem = new JMenuItem("Faculty ");
        facultyItem.setFont(new Font("Serif", Font.PLAIN, 18));
        facultyItem.setForeground(Color.BLACK);
        facultyItem.setBackground(Color.white); // match menu text color
        information.add(facultyItem);

        JMenuItem studentItem = new JMenuItem("Student ");
        studentItem.setFont(new Font("Serif", Font.PLAIN, 18));
        studentItem.setForeground(Color.BLACK);
        studentItem.setBackground(Color.white); // match menu text color
        information.add(studentItem);

        // detaails menu
        JMenu details = new JMenu("Details");
        details.setFont(new Font("Serif", Font.BOLD, 20));
        details.setForeground(Color.BLUE);
        mb.add(details);

        // view faculty details
        JMenuItem viewFaculty = new JMenuItem("View Faculty Details");
        viewFaculty.setFont(new Font("Serif", Font.PLAIN, 18));
        viewFaculty.setForeground(Color.BLACK);
        viewFaculty.setBackground(Color.white); // match menu text color
        details.add(viewFaculty);

        // view student details
        JMenuItem viewStudent = new JMenuItem("View Student Details");
        viewStudent.setFont(new Font("Serif", Font.PLAIN, 18));
        viewStudent.setForeground(Color.BLACK);
        viewStudent.setBackground(Color.white); // match menu text color
        details.add(viewStudent);

        // leave menu

        JMenu leave = new JMenu("Leave");
        leave.setFont(new Font("Serif", Font.BOLD, 20));
        leave.setForeground(Color.BLUE);
        mb.add(leave);

        // apply leave
        JMenuItem applyLeave = new JMenuItem("Faculty Leave");
        applyLeave.setFont(new Font("Serif", Font.PLAIN, 18));
        applyLeave.setForeground(Color.BLACK);
        applyLeave.setBackground(Color.white); // match menu text color
        leave.add(applyLeave);

        JMenuItem applyStudentLeave = new JMenuItem("Student Leave");
        applyStudentLeave.setFont(new Font("Serif", Font.PLAIN, 18));
        applyStudentLeave.setForeground(Color.BLACK);
        applyStudentLeave.setBackground(Color.white); // match menu text color
        leave.add(applyStudentLeave);

        // leave details
        JMenu leaveDetails = new JMenu("Leave Details");
        leaveDetails.setFont(new Font("Serif", Font.BOLD, 20));
        leaveDetails.setForeground(Color.BLUE);
        mb.add(leaveDetails);

        // view faculty leave details
        JMenuItem viewFacultyLeave = new JMenuItem("View Faculty Leave");
        viewFacultyLeave.setFont(new Font("Serif", Font.PLAIN, 18));
        viewFacultyLeave.setForeground(Color.BLACK);
        viewFacultyLeave.setBackground(Color.white); // match menu text color
        leaveDetails.add(viewFacultyLeave);

        // view student leave details
        JMenuItem viewStudentLeave = new JMenuItem("View Student Leave");
        viewStudentLeave.setFont(new Font("Serif", Font.PLAIN, 18));
        viewStudentLeave.setForeground(Color.BLACK);
        viewStudentLeave.setBackground(Color.white); // match menu text color
        leaveDetails.add(viewStudentLeave);

        // exam details
        JMenu examDetails = new JMenu("Examination");
        examDetails.setFont(new Font("Serif", Font.BOLD, 20));
        examDetails.setForeground(Color.BLUE);
        mb.add(examDetails);

        JMenuItem examResults = new JMenuItem("Examination Results");
        examResults.setFont(new Font("Serif", Font.PLAIN, 18));
        examResults.setForeground(Color.BLACK);
        examResults.setBackground(Color.white); // match menu text color
        examDetails.add(examResults);

        // Enter marks
        JMenuItem enterMarks = new JMenuItem("Enter Marks");
        enterMarks.setFont(new Font("Serif", Font.PLAIN, 18));
        enterMarks.setForeground(Color.BLACK);
        enterMarks.setBackground(Color.white); // match menu text color
        examDetails.add(enterMarks);

        // Update details
        JMenu updateDetails = new JMenu("Update Details");
        updateDetails.setFont(new Font("Serif", Font.BOLD, 20));
        updateDetails.setForeground(Color.BLUE);
        mb.add(updateDetails);

        JMenuItem updateFaculty = new JMenuItem("Update Faculty Details");
        updateFaculty.setFont(new Font("Serif", Font.PLAIN, 18));
        updateFaculty.setForeground(Color.BLACK);
        updateFaculty.setBackground(Color.white); // match menu text color
        updateDetails.add(updateFaculty);

        // update student details
        JMenuItem updateStudent = new JMenuItem("Update Student Details");
        updateStudent.setFont(new Font("Serif", Font.PLAIN, 18));
        updateStudent.setForeground(Color.BLACK);
        updateStudent.setBackground(Color.white); // match menu text color
        updateDetails.add(updateStudent);

        // Fee Details
        JMenu feeDetails = new JMenu("Fee Details");
        feeDetails.setFont(new Font("Serif", Font.BOLD, 20));
        feeDetails.setForeground(Color.BLUE);
        mb.add(feeDetails);
        JMenuItem feeStructure = new JMenuItem("Fee Structure");
        feeStructure.setFont(new Font("Serif", Font.PLAIN, 18));
        feeStructure.setForeground(Color.BLACK);
        feeStructure.setBackground(Color.white); // match menu text color
        feeDetails.add(feeStructure);
        JMenuItem studentFeeForm = new JMenuItem("Student Fee Form");
        studentFeeForm.setFont(new Font("Serif", Font.PLAIN, 18));
        studentFeeForm.setForeground(Color.BLACK);
        studentFeeForm.setBackground(Color.white); // match menu text color
        feeDetails.add(studentFeeForm);

        // utility menu
        JMenu utility = new JMenu("Utility");
        utility.setFont(new Font("Serif", Font.BOLD, 20));
        utility.setForeground(Color.BLUE);
        mb.add(utility);
        JMenuItem notepad = new JMenuItem("Notepad");
        notepad.setFont(new Font("Serif", Font.PLAIN, 18));
        notepad.setForeground(Color.BLACK);
        notepad.setBackground(Color.white); // match menu text color
        notepad.addActionListener(e -> {
            try {
                Runtime.getRuntime().exec("notepad"); // For Windows
                // Runtime.getRuntime().exec("gedit"); // For Linux (GNOME)
                // Runtime.getRuntime().exec("open -a TextEdit"); // For macOS
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
        utility.add(notepad);
        JMenuItem calculator = new JMenuItem("Calculator");
        calculator.setFont(new Font("Serif", Font.PLAIN, 18));
        calculator.setForeground(Color.BLACK);
        calculator.setBackground(Color.white); // match menu text color
        calculator.addActionListener(null);
        calculator.addActionListener(e -> {
            try {
                Runtime.getRuntime().exec("calc"); // For Windows
                // Runtime.getRuntime().exec("gnome-calculator"); // For Linux (GNOME)
                // Runtime.getRuntime().exec("open -a Calculator"); // For macOS
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
        utility.add(calculator);

        // about us
        JMenu about = new JMenu("About");

        about.setFont(new Font("Serif", Font.BOLD, 20));
        about.setForeground(Color.BLUE);
        mb.add(about);
        JMenuItem aboutUs = new JMenuItem("About Us");
        aboutUs.setFont(new Font("Serif", Font.PLAIN, 18));
        aboutUs.setForeground(Color.BLACK);
        aboutUs.setBackground(Color.white); // match menu text color
        about.add(aboutUs);

        // Exit
        // Exit (use JMenuItem instead of JMenu so it is clickable)
        JMenuItem exit = new JMenuItem("Exit");
        exit.setFont(new Font("Serif", Font.BOLD, 20));
        exit.setForeground(Color.BLUE);
        mb.add(exit); // add exit option directly
        exit.addActionListener(e -> System.exit(0)); // exit application on click
        setJMenuBar(mb);

        // Frame properties
        setExtendedState(JFrame.MAXIMIZED_BOTH); // maximize frame
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    @Override
    public void actionPerformed(java.awt.event.ActionEvent e) {
        // Handle menu item actions here
        if(e.getActionCommand().equals("Exit")) // handle exit action
        {
            System.exit(0);
        }else if (e.getActionCommand().equals("Notepad")) {
            try {
                Runtime.getRuntime().exec("notepad");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } else if (e.getActionCommand().equals("Calculator")) {
            try {
                Runtime.getRuntime().exec("calc");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            
        }

    }

    public static void main(String[] args) {
        new main_class();
    }
}
