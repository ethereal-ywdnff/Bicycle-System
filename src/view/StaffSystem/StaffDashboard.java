package view.StaffSystem;

import view.GuiFrame;
import model.users.Staff;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * Dashboard a staff member can see once logged into the system.
 * StaffDashboard.java
 */

public class StaffDashboard extends JPanel {

    private final GuiFrame frame;
    private JTabbedPane panelChooser;
    private JButton logoutButton;
    private Staff user;

    /**
     * Creates the dashboard page once a staff member has logged in.
     * Allows staff members to see the orders table and change quantities,
     * add and remove bike items.
     * @param frame the gui frame that this panel gets added to.
     */
    public StaffDashboard(GuiFrame frame, Staff user) {
        this.user = user;
        this.setLayout(new BorderLayout());
        this.setBackground(Color.DARK_GRAY);
        this.frame = frame;
        this.panelChooser = new JTabbedPane();
        this.panelChooser.addTab("Manage Orders", new OrderManagement(user));
        this.panelChooser.addTab("Manage Stock", new StockManagement());
        this.add(greetingAndLogoutSection(), BorderLayout.NORTH);
        this.add(panelChooser, BorderLayout.CENTER);
        this.addActionListeners();
        System.out.println("Hello "+user.getUsername());
    }

    /**
     * Creates the greeting section and the logout button for staff to return back to the main window.
     * @return JPanel containing the greeting by username and logout section on top of the window.
     */
    private JPanel greetingAndLogoutSection() {
        JPanel greetingAndLogoutSection = new JPanel();

         // Logout button
        greetingAndLogoutSection.setBackground(Color.DARK_GRAY);
        greetingAndLogoutSection.setLayout(new BorderLayout());
        greetingAndLogoutSection.setBorder(new EmptyBorder(5, 0, 0, 10));
        logoutButton = new JButton("Logout");
        greetingAndLogoutSection.add(logoutButton,BorderLayout.EAST);

        // Greeting user by username
        String username = user.getUsername().toLowerCase();
        username = username.substring(0, 1).toUpperCase() + username.substring(1);
        JLabel greet = new JLabel("Welcome " + username + "!");
        greet.setBorder(new EmptyBorder(0, 10, 0, 0));
        greet.setFont(new Font("Arial", Font.BOLD, 18));
        greet.setForeground(Color.WHITE);
        greetingAndLogoutSection.add(greet);

        return greetingAndLogoutSection;
    }


    /**
     * Adds the action listeners
     */
    private void addActionListeners() {
        logoutButton.addActionListener(e -> frame.switchToMainWindowPage());
    }
}