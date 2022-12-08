package view.CustomerSystem;

import model.Address;
import model.CleanCustomerDetails;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionListener;

/**
 * Form for the customer to fill in using their name and
 * address. Such as at checkout or when finding their order.
 *
 * CustomerDetailsForm.java
 */

public class CustomerDetailsForm extends JPanel {

    private JTextField forenameField;
    private JTextField surnameField;
    private JTextField houseNumberField;
    private JTextField streetNameField;
    private JTextField postcodeField;
    private JTextField cityField;
    private JButton submitButton;

    /**
     * Creates a form for the customer to fill in with their
     * address and name.
     * @param submitButtonName label for the submit button.
     */
    public CustomerDetailsForm(String submitButtonName) {
        this.add(createCustomerDetailsForm(submitButtonName));
    }

    /**
     * Creates the form with the required fields.
     * @param submitButtonName label on the submit button.
     * @return JPanel form.
     */
    private JPanel createCustomerDetailsForm(String submitButtonName) {
        JPanel customerDetailsPanel = new JPanel();
        customerDetailsPanel.setLayout(new BoxLayout(customerDetailsPanel, BoxLayout.Y_AXIS));

        forenameField = new JTextField();
        customerDetailsPanel.add(createFieldWithLabel("Forename: ", forenameField));

        surnameField = new JTextField();
        customerDetailsPanel.add(createFieldWithLabel("Surname: ", surnameField));

        houseNumberField = new JTextField();
        customerDetailsPanel.add(createFieldWithLabel("House Number/Name: ", houseNumberField));

        streetNameField = new JTextField();
        customerDetailsPanel.add(createFieldWithLabel("Street Name: ", streetNameField));

        postcodeField = new JTextField();
        customerDetailsPanel.add(createFieldWithLabel("Postcode: ", postcodeField));

        cityField = new JTextField();
        customerDetailsPanel.add(createFieldWithLabel("City: ", cityField));

        submitButton = new JButton(submitButtonName);
        customerDetailsPanel.add(submitButton);

        return customerDetailsPanel;
    }

    /**
     * @param label name of the text field.
     * @param field associated with the label.
     * @return panel with the field and its label neatly laid out with spacing.
     */
    private JPanel createFieldWithLabel(String label, JTextField field) {
        // Section contain label and field
        JPanel section = new JPanel();
        section.setBackground(Color.LIGHT_GRAY);
        section.setLayout(new GridLayout(1, 2));
        JLabel sectionLabel = new JLabel(label);
        // Set width of the field
        field.setColumns(40);
        section.add(sectionLabel);
        section.add(field);
        section.setBorder(new EmptyBorder(10, 10, 10, 10));
        return section;
    }

    /**
     * Gets the values from the form.
     * @return validated customer inputs
     */
    public CleanCustomerDetails getCustomerFromFields() {
        String houseNumber = houseNumberField.getText();
        String streetName = streetNameField.getText();
        String postcode = postcodeField.getText();
        String city = cityField.getText();

        Address address = new Address(houseNumber, streetName, city, postcode);
        return new CleanCustomerDetails(forenameField.getText(), surnameField.getText(), address);
    }

    /**
     * Action listener for submit button.
     * @param listener to add to the submit button.
     */
    public void addActionListenerToButton(ActionListener listener) {
        submitButton.setAlignmentX(CENTER_ALIGNMENT);
        submitButton.setPreferredSize(new Dimension(100, 20));
        submitButton.addActionListener(listener);
    }
}
