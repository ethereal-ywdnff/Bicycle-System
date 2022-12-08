package view.StaffSystem.Stocks;

import model.Wheel;

/**
 * Creates the Wheels table which is accessed by the staff.
 *
 * WheelTableModel.java
 */

public class WheelTableModel extends StockTableModel<Wheel> {

    public WheelTableModel() {
        this.addColumn("Brand Name");
        this.addColumn("Serial Number");
        this.addColumn("Component Name");
        this.addColumn("Unit Cost");
        this.addColumn("Quantity");
        this.addColumn("Diameter");
        this.addColumn("Wheel Style");
        this.addColumn("Brake Type");
    }

    @Override
    public void addDataRow(Wheel wheel) {
        String brandName = wheel.getBrandName();
        String serialNumber = wheel.getSerialNumber();
        String componentName = wheel.getComponentName();
        String unitCost = String.valueOf(wheel.getUnitCost());
        String quantity = String.valueOf(wheel.getQuantity());
        String diameter = String.valueOf(wheel.getDiameter());
        String wheelStyle = String.valueOf(wheel.getWheelStyle());
        String brakeType = String.valueOf(wheel.getBrakeType());

        String[] wheelList = {brandName, serialNumber, componentName, unitCost, quantity, diameter, wheelStyle, brakeType};
        this.addRow(wheelList);
        this.data.add(wheel);
    }
}
