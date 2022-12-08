package view.StaffSystem.Stocks.Popup;

import model.ShockAbsorberType;

import javax.swing.*;

public class CreateFramesetPopup extends CreateComponentPopup {

    private JFormattedTextField framesizeField;
    private JComboBox<ShockAbsorberType> shockAbsorberField;
    private JFormattedTextField gearsField;

    public CreateFramesetPopup() {
        super("Create a Frameset");
    }

    @Override
    protected JPanel createContent() {
        JPanel contentPanel = super.createContent();

        // Add custom content
        framesizeField = createNumberField(2);
        contentPanel.add(createLayoutForLabelAndField("Frame Size", framesizeField));

        shockAbsorberField = new JComboBox<>();
        for(ShockAbsorberType type : ShockAbsorberType.values()) {
            shockAbsorberField.addItem(type);
        }
        contentPanel.add(createLayoutForLabelAndField("Shocks Type", shockAbsorberField));

        gearsField = createNumberField(0);
        contentPanel.add(createLayoutForLabelAndField("Gear Count", gearsField));

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
            double frameSize = Double.parseDouble(this.framesizeField.getText().replace(",", ""));
            ShockAbsorberType shocks = (ShockAbsorberType) this.shockAbsorberField.getSelectedItem();
            int gears = Integer.parseInt(this.gearsField.getText().replace(",", ""));

            return componentService.addFrameset(brandName, serialNumber, componentName, unitCost, quantity, frameSize, shocks, gears);
        } catch (Exception e) {
            return false;
        }
    }
}
