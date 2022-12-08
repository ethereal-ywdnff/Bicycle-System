package view.StaffSystem.Stocks.Popup;

import model.HandleBarType;

import javax.swing.*;

public class CreateHandlebarPopup extends CreateComponentPopup {

    private JComboBox<HandleBarType> handleBarTypeComboBox;

    public CreateHandlebarPopup() {
        super("Create a Handlebar");
    }

    @Override
    protected JPanel createContent() {
        JPanel contentPanel = super.createContent();

        // Add custom content
        handleBarTypeComboBox = new JComboBox<>();
        for(HandleBarType type : HandleBarType.values()) {
            handleBarTypeComboBox.addItem(type);
        }
        contentPanel.add(createLayoutForLabelAndField("Handlebar Type", handleBarTypeComboBox));

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
            HandleBarType handleBarType = (HandleBarType) this.handleBarTypeComboBox.getSelectedItem();

            return componentService.addHandlebar(brandName, serialNumber, componentName, unitCost, quantity, handleBarType);
        } catch (Exception e) {
            return false;
        }
    }
}
