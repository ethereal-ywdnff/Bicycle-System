package view.StaffSystem.Stocks.Popup;

import controllers.ComponentService;
import controllers.ServiceProvider;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.text.InternationalFormatter;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;

/**
 * A backing class for the component creation popup.
 *
 * CreateComponentPopup.java
 */
public abstract class CreateComponentPopup {

    protected ComponentService componentService;
    protected JTextField brandNameField;
    protected JTextField serialNumberField;
    protected JTextField componentNameField;
    protected JFormattedTextField unitCostField;
    protected JFormattedTextField quantityField;
    protected JButton submitButton;
    private JLabel errorMessageField;
    private JFrame frame;

    public CreateComponentPopup(String title) {
        componentService = ServiceProvider.getSingleton(ComponentService.class);

        this.frame = new JFrame(title);
        this.frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        contentPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        // Error messages
        errorMessageField = new JLabel("");
        errorMessageField.setForeground(Color.RED);
        errorMessageField.setAlignmentX(Component.CENTER_ALIGNMENT);
        errorMessageField.setFont(new Font("Arial", Font.BOLD, 15));
        errorMessageField.setVerticalAlignment(1);

        contentPanel.add(createContent());
        contentPanel.setBackground(Color.DARK_GRAY);
        submitButton = new JButton("Submit");
        submitButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        contentPanel.add(errorMessageField);
        contentPanel.add(submitButton);

        // Scrollable to make sure error can be seen for invalid component values
        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        this.frame.add(scrollPane);
        this.frame.setMinimumSize(new Dimension(400, 200));
        this.frame.pack();
        this.frame.setVisible(true);

        this.addActionListeners();
    }

    protected JPanel createContent() {
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        brandNameField = new JTextField();
        contentPanel.add(createLayoutForLabelAndField("Brand Name", brandNameField));

        serialNumberField = new JTextField();
        contentPanel.add(createLayoutForLabelAndField("Serial Number", serialNumberField));

        componentNameField = new JTextField();
        contentPanel.add(createLayoutForLabelAndField("Component Name", componentNameField));

        unitCostField = createNumberField(2);
        contentPanel.add(createLayoutForLabelAndField("Unit Cost (£)", unitCostField));

        quantityField = createNumberField(0);
        contentPanel.add(createLayoutForLabelAndField("Start Quantity", quantityField));

        return contentPanel;
    }

    /**
     * A helper method to create a number field that is never an empty string. Very helpful as we do not need to do backend
     * input sanitization.
     *
     * @param decimalPlaces The decimal accuracy a user is allowed to specify
     * @return The JFormattedTextField component created.
     */
    protected JFormattedTextField createNumberField(int decimalPlaces) {
        JFormattedTextField numberField = new JFormattedTextField(new JFormattedTextField.AbstractFormatterFactory() {
            @Override
            public JFormattedTextField.AbstractFormatter getFormatter(JFormattedTextField tf) {
                NumberFormat format = DecimalFormat.getInstance();
                format.setMinimumFractionDigits(decimalPlaces);
                format.setMaximumFractionDigits(decimalPlaces);
                format.setRoundingMode(RoundingMode.HALF_UP);
                InternationalFormatter formatter = new InternationalFormatter(format);
                formatter.setAllowsInvalid(false);
                formatter.setMinimum(0.0);
                formatter.setMaximum(10_000_000.00);
                return formatter;
            }
        });
        numberField.setColumns(20);
        return numberField;
    }

    /**
     * A helper function that adds a label to a field.
     *
     * @param label The label to add
     * @param field The field to add the label to
     * @return A JPanel containing the label and field.
     */
    protected JPanel createLayoutForLabelAndField(String label, JComponent field) {
        JPanel labelAndField = new JPanel();
        labelAndField.setLayout(new GridLayout(2, 1));

        labelAndField.add(new JLabel(label));
        labelAndField.add(field);
        return labelAndField;
    }

    /**
     * This adds the component to the database. If any of the fields are invalid, it will not save to the database.
     */
    protected abstract boolean createComponent();

    private void addActionListeners() {
        submitButton.addActionListener(e -> {
            boolean result = createComponent();

            errorMessageField.setText(result ? "" : "Failed to add component!");

            if (result) {
                this.frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
            }
        });
    }
}
