package view.LoginSystem;

import model.BicycleOrder;
import view.CustomerSystem.CustomerDetailsForm;
import model.CleanCustomerDetails;
import view.GuiFrame;
import controllers.CustomerService;
import controllers.OrderService;
import controllers.ServiceProvider;
import model.users.Customer;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;

/**
 * Customer order access panel (customer login area). The customer can use their
 * order id or their address to login.
 *
 * CustomerLoginPanel.java
 */

public class CustomerLoginPanel extends  JPanel {

    private GuiFrame frame;
    private JTextField orderNumberField;
    private JButton customerOrderNumberButton;
    private JButton backButton;
    private CustomerService customerService;
    private OrderService orderService;
    private CustomerDetailsForm customerDetailsForm;
    private JLabel errorMessageField;

    /**
     * Sets up the main panel for the customer login/ fields
     * for the customer to access their order.
     * @param frame the frame this panel is added to (allows switching of pages).
     */
    public CustomerLoginPanel(GuiFrame frame) {
        // Servies required
        this.customerService = ServiceProvider.getSingleton(CustomerService.class);
        this.orderService = ServiceProvider.getSingleton(OrderService.class);
        this.frame = frame;
        this.setBackground(Color.DARK_GRAY);

        // This panel only gets added to add padding to the login section so it is away from the top of the page
        JPanel orderOrCustomerDetailsSection = new JPanel();
        orderOrCustomerDetailsSection.setBorder(new EmptyBorder(100, 0, 0, 0));
        orderOrCustomerDetailsSection.setBackground(Color.DARK_GRAY);
        orderOrCustomerDetailsSection.add(combineCustomerOrderAndAddressDetailsPanel());

        this.add(orderOrCustomerDetailsSection);
        this.addActionListeners();
    }

    /**
     * Combine the order number section and address section panels.
     * Gives the choice whether the user wants to access their order using their order number or address.
     * @return JPanel combining the address section and order number section panels.
     */
    private JPanel combineCustomerOrderAndAddressDetailsPanel() {
        JPanel orderAndCustomerDetailsPanel = new JPanel();
        orderAndCustomerDetailsPanel.setBorder(new LineBorder(Color.BLACK));
        orderAndCustomerDetailsPanel.setLayout(new BoxLayout(orderAndCustomerDetailsPanel, BoxLayout.Y_AXIS));
        orderAndCustomerDetailsPanel.add(customerOrderNumberPanel());
        orderAndCustomerDetailsPanel.add(customerEnterDetailsPanel());
        return orderAndCustomerDetailsPanel;
    }

    /**
     * Fields to allow the customer to access their order via the order id.
     * @return panel containing the field for order id.
     */
    private JPanel customerOrderNumberPanel() {
        JPanel orderNumberPanel = new JPanel();
        orderNumberPanel.setLayout(new BoxLayout(orderNumberPanel, BoxLayout.Y_AXIS));
        orderNumberPanel.setBackground(Color.LIGHT_GRAY);

        // Create label to show user they can use their order number
        JLabel useOrderNumberLabel = new JLabel("Order Number");
        useOrderNumberLabel.setAlignmentX(CENTER_ALIGNMENT);
        useOrderNumberLabel.setFont(new Font("Arial", Font.BOLD, 15));
        useOrderNumberLabel.setVerticalAlignment(1);
        orderNumberPanel.add(useOrderNumberLabel);

        // Create order number field
        orderNumberField = new JTextField();
        orderNumberPanel.add(createFieldWithLabel("Order Number: ", orderNumberField));

        // Find order button
        customerOrderNumberButton = new JButton("Find My Order");
        customerOrderNumberButton.setAlignmentX(CENTER_ALIGNMENT);
        customerOrderNumberButton.setPreferredSize(new Dimension(100, 20));
        orderNumberPanel.add(customerOrderNumberButton);
        return orderNumberPanel;
    }

    /**
     * A panel with all the fields the user requires to access their order.
     * @return panel with address fields.
     */
    private JPanel customerEnterDetailsPanel() {
        JPanel customerDetailsPanel = new JPanel();
        customerDetailsPanel.setLayout(new BoxLayout(customerDetailsPanel, BoxLayout.Y_AXIS));
        customerDetailsPanel.setBackground(Color.LIGHT_GRAY);

        // Address Label
        JLabel customerLoginLabel = new JLabel("My Details");
        customerLoginLabel.setBorder(new EmptyBorder(10, 0, 10, 0));
        customerLoginLabel.setAlignmentX(CENTER_ALIGNMENT);
        customerLoginLabel.setFont(new Font("Arial", Font.BOLD, 15));
        customerDetailsPanel.add(customerLoginLabel);

        customerDetailsForm = new CustomerDetailsForm("Find My Order");
        customerDetailsPanel.add(customerDetailsForm);

        // Back Button
        backButton = new JButton("Go Back to Building a Bike");
        backButton.setAlignmentX(CENTER_ALIGNMENT);
        backButton.setPreferredSize(new Dimension(100, 20));
        backButton.setOpaque(false);
        backButton.setContentAreaFilled(false);
        backButton.setBorderPainted(false);
        backButton.setForeground(Color.BLUE);

        // Error messages
        errorMessageField = new JLabel("");
        errorMessageField.setForeground(new Color(200, 0, 0));
        errorMessageField.setAlignmentX(CENTER_ALIGNMENT);
        errorMessageField.setFont(new Font("Arial", Font.BOLD, 15));
        errorMessageField.setVerticalAlignment(1);

        customerDetailsPanel.add(errorMessageField);
        customerDetailsPanel.add(backButton);
        return customerDetailsPanel;
    }

    /**
     * This is a helper function to create a neatly formatted and positioned text field
     * with an associated label.
     * @param label name of the text field.
     * @param field associated with the label.
     * @return panel with the field and its label neatly laid out with spacing.
     */
    private JPanel createFieldWithLabel(String label, JTextField field) {
        // Section contain label and field
        JPanel section = new JPanel();
        section.setLayout(new GridLayout(1, 2));
        JLabel sectionLabel = new JLabel(label);
        // Set width of the field
        field.setColumns(40);
        section.add(sectionLabel);
        section.add(field);
        // Add padding
        section.setBorder(new EmptyBorder(10, 10, 10, 10));
        return section;
    }

    /**
     * Adds the actions listeners for this customer order access panel.
     */
    public void addActionListeners() {
        // Back Button Action Listener
        backButton.addActionListener(e -> frame.switchToMainWindowPage());

        // Order ID Login Action Listener
        customerOrderNumberButton.addActionListener(e -> {
            int orderNumber = tryParseInt(orderNumberField.getText(), -1);
            BicycleOrder order = orderService.getOrderById(orderNumber);
            if (order != null) {
                System.out.println("Got the order!");
                frame.switchCustomerDashboardPage(new BicycleOrder[] {order});
                errorMessageField.setText("");
            } else {
                System.out.println("Failed to get the order.");
                errorMessageField.setText("Failed to find an order matching the order number provided.");
            }
        });

        customerDetailsForm.addActionListenerToButton(e -> {
            CleanCustomerDetails loginForm = customerDetailsForm.getCustomerFromFields();
            Customer customer = customerService.getCustomer(loginForm.getForename(), loginForm.getSurname(), loginForm.getAddress());

            // Check if the customer exists
            if (customer != null) {
                BicycleOrder[] orders = orderService.getAllOrdersForCustomer(customer.getId()).toArray(new BicycleOrder[0]);

                frame.switchCustomerDashboardPage(orders);
                errorMessageField.setText("");
            } else {
                errorMessageField.setText("Failed to find any orders matching the address provided.");
            }
        });
    }

    public Integer tryParseInt(String value, int defaultValue) {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }
}
