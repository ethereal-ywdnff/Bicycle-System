package view.LoginSystem;

import controllers.StaffService;
import view.GuiFrame;
import controllers.ServiceProvider;
import model.users.Staff;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;

/**
 * Staff login page that allows staff members to login
 * with their username and password.
 *
 * StaffLoginPanel.java
 */

public class StaffLoginPanel extends JPanel {

    private GuiFrame frame;
    private JButton loginButton;
    private JButton backButton;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private StaffService staffService;
    private JLabel errorMessageField;

    /**
     * Sets up the panel for the login page.
     * @param frame the frame this panel is added to.
     */
    public StaffLoginPanel(GuiFrame frame) {
        // Required services
        this.staffService = ServiceProvider.getSingleton(StaffService.class);

        this.frame = frame;
        this.setBackground(Color.DARK_GRAY);
        this.add(loginPanel());
        this.addActionListeners();
    }

    /**
     * Positions the login area approximately in the middle/
     * towards the top of the page.
     * @return JPanel containing the centered login area
     */
    private JPanel loginPanel() {
        JPanel loginPanel = new JPanel();
        loginPanel.setLayout(new BoxLayout(loginPanel, BoxLayout.Y_AXIS));
        loginPanel.setBackground(Color.DARK_GRAY);

        // Positioning the login area
        loginPanel.setBorder(new EmptyBorder(100, 0, 10, 0));
        loginPanel.add(usernamePasswordPanel());
        return loginPanel;
    }

    /**
     * Main login area. Sets up the fields for the username and password.
     * @return Panel containing the required fields and labels.
     */
    private JPanel usernamePasswordPanel() {
        JPanel loginPanel = new JPanel();
        // Setting panel properties
        loginPanel.setLayout(new BoxLayout(loginPanel, BoxLayout.Y_AXIS));
        loginPanel.setBackground(Color.LIGHT_GRAY);
        loginPanel.setBorder(new LineBorder(Color.BLACK));

        // Staff Login Label
        JLabel staffLoginLabel = new JLabel("Staff Login");
        staffLoginLabel.setAlignmentX(CENTER_ALIGNMENT);
        staffLoginLabel.setFont(new Font("Arial", Font.BOLD, 15));
        staffLoginLabel.setVerticalAlignment(1);
        loginPanel.add(staffLoginLabel);

        // Username input field section and label
        JPanel usernameSection = new JPanel();
        usernameSection.setLayout(new FlowLayout());
        JLabel usernameLabel = new JLabel("Username");
        usernameField = new JTextField();
        usernameField.setColumns(40);
        usernameSection.add(usernameLabel);
        usernameSection.add(usernameField);

        // Password field section and label
        JPanel passwordSection = new JPanel();
        passwordSection.setLayout(new FlowLayout());
        JLabel passwordLabel = new JLabel("Password");
        passwordField = new JPasswordField();
        passwordField.setColumns(40);
        passwordSection.setBorder(new EmptyBorder(10, 10, 10, 10));
        passwordSection.add(passwordLabel);
        passwordSection.add(passwordField);

        // Error messages
        errorMessageField = new JLabel("");
        errorMessageField.setForeground(new Color(200, 0, 0));
        errorMessageField.setAlignmentX(CENTER_ALIGNMENT);
        errorMessageField.setFont(new Font("Arial", Font.BOLD, 15));
        errorMessageField.setVerticalAlignment(1);

        // Login Button
        loginButton = new JButton("Login");
        loginButton.setAlignmentX(CENTER_ALIGNMENT);
        loginButton.setPreferredSize(new Dimension(100, 20));

        // Back Button
        backButton = new JButton("Go Back to Building a Bike");
        backButton.setAlignmentX(CENTER_ALIGNMENT);
        backButton.setPreferredSize(new Dimension(100, 20));
        backButton.setOpaque(false);
        backButton.setContentAreaFilled(false);
        backButton.setBorderPainted(false);
        backButton.setForeground(Color.BLUE);

        loginPanel.add(usernameSection);
        loginPanel.add(passwordSection);
        loginPanel.add(errorMessageField);
        loginPanel.add(loginButton);
        loginPanel.add(backButton);
        return loginPanel;
    }

    /**
     * Takes all the details in from the fields.
     */
    private void getLoginDetails() {
        String username = usernameField.getText();
        char[] password = passwordField.getPassword();
        Staff user = staffService.staffLogin(username, password);
        // Check if the user exists
        if (user != null) {
            frame.switchToStaffDashboardPage(user);
            errorMessageField.setText("");
        } else {
            errorMessageField.setText("Failed to login: Incorrect credentials.");
        }
    }

    /**
     * Adds the actions listeners that have been used, for the staff login panel.
     */
    public void addActionListeners() {
        // Login Button Action Listener
        loginButton.addActionListener(e -> getLoginDetails());

        // Back Button Action Listener
        backButton.addActionListener(e -> frame.switchToMainWindowPage());
    }

}
