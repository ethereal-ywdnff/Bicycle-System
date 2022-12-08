package view.MainWindow;
import model.BicycleOrder;
import view.CustomerSystem.CustomerDetailsForm;
import view.GuiFrame;
import controllers.CustomerService;
import controllers.OrderService;
import controllers.ServiceProvider;
import model.CleanCustomerDetails;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * Allows the user to fill in their details so their bicycle order can
 * be placed and processed by staff.
 *
 * CheckoutPanel.java
 */

public class CheckoutPanel extends JPanel {

    private GuiFrame frame;
    private BasketComponent basketComponent;
    private OrderService orderService;
    private CustomerService customerService;
    private CustomerDetailsForm customerDetailsForm;
    private BicycleOrder bicycle;
    private JButton backButton;
    private JLabel errorMessageField;

    /**
     * Creates the page where the user can see the bicycle they have created
     * and a form which they fill in to place their order.
     * @param frame the frame to add this panel to (allows switching of pages)
     * @param bicycle created by the customer
     */
    public CheckoutPanel(GuiFrame frame, BicycleOrder bicycle) {
        this.frame = frame;

        // Set the panel properties
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.setBackground(Color.DARK_GRAY);
        this.setBorder(new EmptyBorder(10, 10, 10, 10));

        // Required services
        orderService = ServiceProvider.getSingleton(OrderService.class);
        customerService = ServiceProvider.getSingleton(CustomerService.class);

        this.bicycle = bicycle;

        // Create the basket panel so the order items can be seen with their prices
        this.basketComponent = new BasketComponent();
        this.basketComponent.setBike(bicycle);
        this.add(basketComponent);

        // Details for the customer to fill in to place the order
        this.customerDetailsForm = new CustomerDetailsForm("Save Order");
        customerDetailsForm.setBorder(new EmptyBorder(10, 10, 10, 10));
        customerDetailsForm.setBackground(Color.DARK_GRAY);
        this.add(customerDetailsForm);

        // Error messages
        errorMessageField = new JLabel("");
        errorMessageField.setForeground(Color.RED);
        errorMessageField.setAlignmentX(CENTER_ALIGNMENT);
        errorMessageField.setFont(new Font("Arial", Font.BOLD, 15));
        errorMessageField.setVerticalAlignment(1);
        this.add(errorMessageField);

        // Back button
        backButton = new JButton("Back");
        backButton.setAlignmentX(CENTER_ALIGNMENT);
        backButton.setHorizontalAlignment(SwingConstants.CENTER);
        this.add(backButton);
        this.addActionListeners();
    }

    private void submitOrder() {
        CleanCustomerDetails customerDetails = customerDetailsForm.getCustomerFromFields();

        errorMessageField.setText(customerDetails.isValid() ? "" : "Invalid Details");

        if (customerDetails.isValid()) {
            System.out.println("Submitting order...");

            BicycleOrder order = this.orderService.createOrder(this.bicycle, customerDetails);

            System.out.println("Trying to find order of id: " + order.getOrderId());

            if(order != null) {
                System.out.println("Successfully created order of id " + order.getOrderId());
                frame.switchCustomerDashboardPage(new BicycleOrder[] {order});
            } else {
                System.out.println("Failed to create order!");
            }
        } else {
            System.out.println("Failed to submit order. Customer details form is invalid.");
        }
    }

    /**
     * Creates the listeners for this class
     */
    private void addActionListeners() {
        customerDetailsForm.addActionListenerToButton(e -> submitOrder());
        backButton.addActionListener(e -> frame.switchToMainWindowPage());
    }

}
