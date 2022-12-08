package view;

import model.BicycleOrder;
import view.CustomerSystem.CustomerDashboard;
import view.LoginSystem.CustomerLoginPanel;
import view.LoginSystem.StaffLoginPanel;
import view.MainWindow.CheckoutPanel;
import view.MainWindow.MainWindowPanel;
import view.StaffSystem.StaffDashboard;
import model.users.Staff;

import javax.swing.*;
import java.awt.*;

/**
 * Sets up the main window for the software.
 * GuiFrame.java
 */

public class GuiFrame extends JFrame {

    private MainWindowPanel mainWindowPanel;
    private StaffLoginPanel staffLoginPanel;
    private CustomerLoginPanel customerLoginPanel;
    private CheckoutPanel checkoutPanel;
    private StaffDashboard staffDashboard;
    private CustomerDashboard customerDashboard;

    /**
     * Sets up the GUI frame.
     */
    public GuiFrame() {
        super("Bicycle System");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        initiatePages();
        this.add(mainWindowPanel);
        this.setMinimumSize(new Dimension(1200, 900));
        this.pack();
        this.setVisible(true);
    }

    /**
     * Instantiates the panels/ pages.
     */
    private void initiatePages() {
        mainWindowPanel = new MainWindowPanel(this);
        staffLoginPanel = new StaffLoginPanel(this);
        customerLoginPanel = new CustomerLoginPanel(this);
    }

    /**
     * Switches to the staff login page.
     */
    public void switchToStaffLoginPage() {
        this.getContentPane().removeAll();
        this.staffLoginPanel = new StaffLoginPanel(this);
        this.add(staffLoginPanel);
        this.revalidate();
        this.repaint();
    }

    /**
     * Switches to the checkout page.
     */
    public void switchToCheckoutPage() {
        this.getContentPane().removeAll();
        this.checkoutPanel = new CheckoutPanel(this, mainWindowPanel.getBicycleFromFields());
        this.add(checkoutPanel);
        this.revalidate();
        this.repaint();
    }

    /**
     * Switches to the checkout page.
     */
    public void switchCustomerDashboardPage(BicycleOrder[] orders) {
        this.getContentPane().removeAll();
        this.customerDashboard = new CustomerDashboard(this, orders);
        this.add(customerDashboard);
        this.revalidate();
        this.repaint();
    }

    /**
     * Switches to the customer login page.
     */
    public void switchToCustomerLoginPage() {
        this.getContentPane().removeAll();
        customerLoginPanel = new CustomerLoginPanel(this);
        this.add(customerLoginPanel);
        this.revalidate();
        this.repaint();
    }

    /**
     * Switches to the main window page.
     */
    public void switchToMainWindowPage() {
        this.getContentPane().removeAll();
        mainWindowPanel = new MainWindowPanel(this);
        this.add(mainWindowPanel);
        this.revalidate();
        this.repaint();
    }

    /**
     * Switches to the staff dashboard page.
     */
    public void switchToStaffDashboardPage(Staff staff) {
        staffDashboard = new StaffDashboard(this, staff);
        this.getContentPane().removeAll();
        this.add(staffDashboard);
        this.revalidate();
        this.repaint();
    }

}
