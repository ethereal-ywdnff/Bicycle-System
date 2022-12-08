package view.CustomerSystem;

import model.BicycleOrder;
import view.GuiFrame;
import controllers.CustomerService;
import controllers.ServiceProvider;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * The page that allows the customer to view their order, delete order and change their
 * address if needed.
 *
 * CustomerDashboard.java
 */

public class CustomerDashboard extends JPanel {

    private GuiFrame frame;
    private BicycleOrder[] orders;
    private JButton homeButton;
    private CustomerService customerService;
    private JTabbedPane panelChooser;

    /**
     * The constructor for creating the customer dashboard. This page allows the customer to
     * view their order/s and change their address if the order has not been fulfilled.
     *
     * @param frame  the main frame to allow switching between pages.
     * @param orders All customer's orders
     */
    public CustomerDashboard(GuiFrame frame, BicycleOrder[] orders) {
        this.customerService = ServiceProvider.getSingleton(CustomerService.class);
        this.frame = frame;
        this.orders = orders;
        this.setLayout(new BorderLayout());
        this.add(homeButtonSection(), BorderLayout.NORTH);
        this.panelChooser = new JTabbedPane();

        for (int i = 0; i < orders.length; i++) {
            BicycleOrder order = orders[i];
            if (order == null) {
                System.out.println("Trying to add a null order to the panel?");
                continue;
            }
            this.panelChooser.addTab("Order " + (i + 1), new CustomerOrderPanel(order, frame));
        }
        this.add(panelChooser);
        this.addActionListeners();
    }

    /**
     * Creates the top bar on this page which only contains the home button.
     * @return top bar panel
     */
    private JPanel homeButtonSection() {
        JPanel homeSection = new JPanel();
        homeSection.setBackground(Color.DARK_GRAY);
        homeSection.setLayout(new BorderLayout());
        homeSection.setBorder(new EmptyBorder(5, 0, 5, 10));
        homeButton = new JButton("Home");
        homeSection.add(homeButton, BorderLayout.EAST);
        return homeSection;
    }

    private void addActionListeners() {
        homeButton.addActionListener(e -> frame.switchToMainWindowPage());
    }
}