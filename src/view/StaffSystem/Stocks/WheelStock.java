package view.StaffSystem.Stocks;

import model.*;
import view.StaffSystem.Stocks.Popup.CreateWheelPopup;
import controllers.ComponentService;
import controllers.ServiceProvider;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.util.List;

/**
 * This class creates the page for wheel properties managed by staff.
 *
 * WheelStock.java
 */

public class WheelStock extends StockDashboard {

    private JFormattedTextField diameterSection;
    private JComboBox<WheelStyle> wheelStyleSection;
    private JComboBox<BrakeType> brakeTypeSection;

    public WheelStock(WheelTableModel model) {
        super(model);
        this.componentService = ServiceProvider.getSingleton(ComponentService.class);
    }

    @Override
    public JPanel changePropertiesRightPanel() {
        JPanel panel = new JPanel();
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        this.diameterSection = this.createNumberField(2);
        this.wheelStyleSection = new JComboBox<>();
        for (WheelStyle type : WheelStyle.values()) {
            this.wheelStyleSection.addItem(type);
        }

        this.brakeTypeSection = new JComboBox<>();
        for (BrakeType type : BrakeType.values()) {
            this.brakeTypeSection.addItem(type);
        }

        panel.add(createLayoutForLabelAndField("Wheel Diameter", this.diameterSection));
        panel.add(createLayoutForLabelAndField("Wheel Style", this.wheelStyleSection));
        panel.add(createLayoutForLabelAndField("Brake Type", this.brakeTypeSection));
        return panel;
    }

    @Override
    protected List<Wheel> getAll() {
        System.out.println("ComponentService: " + this.componentService);
        return this.componentService.getAllWheels();
    }

    @Override
    protected void createComponentPopup() {
        new CreateWheelPopup();
    }

    @Override
    protected void onApplyChanges() {
        WheelTableModel model = (WheelTableModel) getModel();
        Wheel wheel = model.getDataAt(getSelectedRow());

        String componentName = this.componentNameSection.getText();
        double unitCost = Double.parseDouble(this.unitCostSection.getText().replace(",", ""));
        int quantity = Integer.parseInt(this.quantitySection.getText().replace(",", ""));
        double diameter = Double.parseDouble(this.diameterSection.getText().replace(",", ""));
        WheelStyle wheelStyle = (WheelStyle) this.wheelStyleSection.getSelectedItem();
        BrakeType brakeType = (BrakeType) this.brakeTypeSection.getSelectedItem();

        System.out.println("We're updating the handlebar with: " + unitCost + " " + quantity + " " + diameter + " " + wheelStyle + " " + brakeType);
        this.componentService.updateWheel(wheel.getBrandName(), componentName, wheel.getSerialNumber(), unitCost, quantity, diameter, wheelStyle, brakeType);
    }

    @Override
    protected void onRowSelected() {
        boolean isEnabled = getSelectedRow() != -1;

        this.unitCostSection.setEnabled(isEnabled);
        this.quantitySection.setEnabled(isEnabled);

        this.diameterSection.setEnabled(isEnabled);
        this.wheelStyleSection.setEnabled(isEnabled);
        this.brakeTypeSection.setEnabled(isEnabled);

        if (isEnabled) {
            StockTableModel model = getModel();
            Wheel data = (Wheel) model.getDataAt(getSelectedRow());

            this.componentNameSection.setText(data.getComponentName());
            this.unitCostSection.setText(String.valueOf(data.getUnitCost()));
            this.quantitySection.setText(String.valueOf(data.getQuantity()));

            this.diameterSection.setText(String.valueOf(data.getDiameter()));
            this.wheelStyleSection.setSelectedItem(data.getWheelStyle());
            this.brakeTypeSection.setSelectedItem(data.getBrakeType());

            System.out.println("We're populating the fields with " + data.getBrandName() + " to " + data.getDiameter() + " " + data.getBrakeType() + " " + data.getWheelStyle());
        }
    }
}
