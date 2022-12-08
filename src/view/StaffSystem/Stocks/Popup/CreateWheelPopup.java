package view.StaffSystem.Stocks.Popup;

import model.BrakeType;
import model.WheelStyle;

import javax.swing.*;

public class CreateWheelPopup extends CreateComponentPopup {

    private JFormattedTextField diameterField;
    private JComboBox<WheelStyle> wheelStyleCombobox;
    private JComboBox<BrakeType> brakeTypeCombobox;

    public CreateWheelPopup() {
        super("Create a Wheel");
    }

    @Override
    protected JPanel createContent() {
        JPanel contentPanel = super.createContent();

        // Add custom content
        diameterField = createNumberField(2);
        contentPanel.add(createLayoutForLabelAndField("Diameter", diameterField));

        wheelStyleCombobox = new JComboBox<>();
        for (WheelStyle style : WheelStyle.values()) {
            wheelStyleCombobox.addItem(style);
        }
        contentPanel.add(createLayoutForLabelAndField("Wheel Style", wheelStyleCombobox));

        brakeTypeCombobox = new JComboBox<>();
        for (BrakeType type : BrakeType.values()) {
            brakeTypeCombobox.addItem(type);
        }
        contentPanel.add(createLayoutForLabelAndField("Brake Type", brakeTypeCombobox));

        return contentPanel;
    }

    @Override
    protected boolean createComponent() {
        try {
            String brandName = this.brandNameField.getText().replace(",", "");
            String serialNumber = this.serialNumberField.getText().replace(",", "");
            String componentName = this.componentNameField.getText().replace(",", "");
            double unitCost = Double.parseDouble(this.unitCostField.getText().replace(",", ""));
            int quantity = Integer.parseInt(this.quantityField.getText().replace(",", ""));
            double diameter = Double.parseDouble(this.diameterField.getText().replace(",", ""));
            WheelStyle wheelStyle = (WheelStyle) this.wheelStyleCombobox.getSelectedItem();
            BrakeType brakeType = (BrakeType) this.brakeTypeCombobox.getSelectedItem();

            System.out.println("Adding wheel component to the database...");
            return componentService.addWheel(brandName, serialNumber, componentName, unitCost, quantity, diameter, wheelStyle, brakeType);
        } catch (Exception e) {
            return false;
        }
    }
}