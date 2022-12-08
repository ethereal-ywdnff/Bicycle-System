package view.CustomerSystem;

import model.Address;
import model.BicycleOrder;
import model.CleanCustomerDetails;
import model.OrderStatus;
import view.GuiFrame;
import view.MainWindow.BasketComponent;
import controllers.CustomerService;
import controllers.OrderService;
import controllers.ServiceProvider;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;

/**
 * Allows customers to see their orders, change their address
 * and delete their order if necessary.
 *
 * CustomerOrderPanel.java
 */

public class CustomerOrderPanel extends JPanel {

    private JTextField forenameField;
    private JTextField surnameField;
    private JTextField houseNumberField;
    private JTextField streetNameField;
    private JTextField postcodeField;
    private JTextField cityField;
    private JLabel errorMessageField;
    private BasketComponent basketComponent;
    private JButton submitButton;
    private final CustomerService customerService;
    private final OrderService orderService;
    private final BicycleOrder order;
    private final GuiFrame frame;
    private JButton deleteOrderButton;

    /**
     * Constructor to set up the page.
     * @param order of customer
     * @param frame to allow switching between pages
     */
    public CustomerOrderPanel(BicycleOrder order, GuiFrame frame) {
        // 0 rows and 1 column to make it expand the full screen
        this.setLayout(new GridLayout(0,1));
        this.order = order;
        this.frame = frame;
        this.setBackground(Color.DARK_GRAY);
        this.add(combineAddressAndOrderNumberPanels());
        this.orderService = ServiceProvider.getSingleton(OrderService.class);
        this.customerService = ServiceProvider.getSingleton(CustomerService.class);
        this.addActionListeners();

        // Disable the 'edit order' UI if the order status isn't Pending
        boolean isEnabled = order.getOrderStatus() == OrderStatus.PENDING;

        forenameField.setEnabled(isEnabled);
        surnameField.setEnabled(isEnabled);
        houseNumberField.setEnabled(isEnabled);
        streetNameField.setEnabled(isEnabled);
        postcodeField.setEnabled(isEnabled);
        cityField.setEnabled(isEnabled);
        submitButton.setEnabled(isEnabled);
        deleteOrderButton.setEnabled(isEnabled);
    }

    /**
     * Combines the panels for the address section and the order number section.
     * @return
     */
    private JPanel combineAddressAndOrderNumberPanels() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(1, 2));
        panel.add(showOrderNumberAndBasketPanel());
        panel.add(changeAddressAndNameFieldPanel());
        return panel;
    }

    /**
     * @param label name of the text field.
     * @param field associated with the label.
     * @return panel with the field and its label neatly laid out with spacing.
     */
    private JPanel createFieldWithLabel(String label, JTextField field) {
        // Section contain label and field
        JPanel section = new JPanel();
        section.setLayout(new GridLayout(2, 1));
        JLabel sectionLabel = new JLabel(label);
        // Set width of the field
        field.setColumns(40);
        section.add(sectionLabel);
        section.add(field);
        section.setBorder(new EmptyBorder(10, 10, 10, 10));
        return section;
    }

    private JPanel showOrderNumberAndBasketPanel() {
        JPanel orderNumberPanel = new JPanel();
        orderNumberPanel.setLayout(new BorderLayout());
        orderNumberPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        basketComponent = new BasketComponent();
        basketComponent.setBike(this.order);
        deleteOrderButton = new JButton("Delete Order");

        orderNumberPanel.add(basketComponent, BorderLayout.CENTER);
        orderNumberPanel.add(deleteOrderButton, BorderLayout.SOUTH);
        return orderNumberPanel;
    }

    private JPanel changeAddressAndNameFieldPanel() {
        JPanel changeDetailsPanel = new JPanel();
        changeDetailsPanel.setLayout(new BorderLayout());
        changeDetailsPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        changeDetailsPanel.setAlignmentX(CENTER_ALIGNMENT);

        JPanel changeOrderPanel = new JPanel();
        changeOrderPanel.add(new JLabel("Change Order Details"));
        changeOrderPanel.setBorder(new LineBorder(Color.BLACK));
        changeOrderPanel.setLayout(new BoxLayout(changeOrderPanel, BoxLayout.Y_AXIS));
        changeOrderPanel.setAlignmentX(CENTER_ALIGNMENT);
        changeOrderPanel.setBackground(Color.LIGHT_GRAY);

        Address address = this.order.getCustomer().getAddress();

        JTextField orderNumberField = new JTextField();
        orderNumberField.setText(String.valueOf(order.getOrderId()));
        orderNumberField.setEditable(false);
        changeOrderPanel.add(createFieldWithLabel("Order Id: ", orderNumberField));

        JTextField dateOrderedField = new JTextField();
        dateOrderedField.setText(order.getDateOrdered().toString());
        dateOrderedField.setEditable(false);
        changeOrderPanel.add(createFieldWithLabel("Date Ordered: ", dateOrderedField));

        // Customer forename and surname fields
        forenameField = new JTextField();
        forenameField.setText(order.getCustomer().getForename());
        changeOrderPanel.add(createFieldWithLabel("Forename: ", forenameField));

        surnameField = new JTextField();
        surnameField.setText(order.getCustomer().getSurname());
        changeOrderPanel.add(createFieldWithLabel("Surname: ", surnameField));

        // Address detail fields
        houseNumberField = new JTextField();
        houseNumberField.setText(address.getHouseNumber());
        changeOrderPanel.add(createFieldWithLabel("House Number/Name: ", houseNumberField));
        streetNameField = new JTextField();
        streetNameField.setText(address.getStreetName());
        changeOrderPanel.add(createFieldWithLabel("Street Name: ", streetNameField));
        postcodeField = new JTextField();
        postcodeField.setText(address.getPostcode());
        changeOrderPanel.add(createFieldWithLabel("Postcode: ", postcodeField));
        cityField = new JTextField();
        cityField.setText(address.getCityName());
        changeOrderPanel.add(createFieldWithLabel("City: ", cityField));

        // Error messages
        errorMessageField = new JLabel("");
        errorMessageField.setForeground(Color.RED);
        errorMessageField.setAlignmentX(CENTER_ALIGNMENT);
        errorMessageField.setFont(new Font("Arial", Font.BOLD, 15));
        errorMessageField.setVerticalAlignment(1);
        changeOrderPanel.add(errorMessageField);
        submitButton = new JButton("Submit");

        changeDetailsPanel.add(changeOrderPanel, BorderLayout.CENTER);
        changeDetailsPanel.add(submitButton, BorderLayout.SOUTH);
        return changeDetailsPanel;
    }

    /**
     * Adds action listeners for this class.
     */
    private void addActionListeners() {
        submitButton.addActionListener(e -> {
            String newForename = forenameField.getText();
            String newSurname = surnameField.getText();

            String houseNumberInput = houseNumberField.getText();
            String streetNameInput = streetNameField.getText();
            String cityInput = cityField.getText();
            String postcodeInput = postcodeField.getText();

            Address newAddress = new Address(houseNumberInput, streetNameInput, cityInput, postcodeInput);
            CleanCustomerDetails newDetails = new CleanCustomerDetails(newForename, newSurname, newAddress);

            if (newDetails.isValid()) {
                this.customerService.updateCustomerDetails(order.getCustomer().getId(), newDetails);
                errorMessageField.setText("");
            } else {
                errorMessageField.setText("Invalid details supplied.");
            }
        });

        deleteOrderButton.addActionListener(e -> {
            orderService.deleteOrderById(order.getOrderId());
            frame.switchToCustomerLoginPage();
        });
    }
}
