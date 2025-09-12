package university.management.system;

import javax.swing.*;
import java.awt.*;

public class splash extends JFrame implements Runnable {
    Thread t;

    splash() {
        ImageIcon i1 = new ImageIcon(ClassLoader.getSystemResource("icons/first.png"));
        Image i2 = i1.getImage().getScaledInstance(1000, 700, Image.SCALE_DEFAULT); 
        ImageIcon i3 = new ImageIcon(i2);
        JLabel img = new JLabel(i3);
        add(img);

        // Make the window borderless
        // setUndecorated(true);
        
        // This makes the window visible before the animation starts
        setVisible(true);

        // --- NEW ANIMATION LOOP ---
        int finalWidth = 900;
        int finalHeight = 650;
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

        for (int i = 0; i <= 100; i++) {
            int currentWidth = finalWidth * i / 100;
            int currentHeight = finalHeight * i / 100;
            
            // This calculation keeps it centered at all times
            int x = (screenSize.width - currentWidth) / 2;
            int y = (screenSize.height - currentHeight) / 2;

            setSize(currentWidth, currentHeight);
            setLocation(x, y);
            
            try {
                Thread.sleep(10); // Controls the speed of the animation
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        
        // Start the timer to close the splash screen after it's done animating
        t = new Thread(this);
        t.start();
    }

    public void run() {
        try {
            Thread.sleep(5000); // Window will stay for 5 seconds AFTER animating
            setVisible(false);
            dispose(); // Frees up memory
            new Login();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new splash();
    }
}