package model;

import java.text.DecimalFormat;

/**
 * Inherit from this abstract class if there are new components to add to the system.
 *
 * Component.java
 */
public abstract class Component {
    protected static final DecimalFormat df = new DecimalFormat("0.00");

    private final String componentName;
    private final double unitCost;
    private final String brandName;
    private final String serialNumber;
    private final int quantity;

    public String toReceiptString() {
        String result = "";
        result += "Name: " + componentName + "\n";
//        result += "Unit Cost: £" + df.format(unitCost) + "\n";
        result += "Brand: " + brandName + "\n";
        result += "Serial Number: " + serialNumber + "\n";
        return result;
    }

    public Component(String brandName, String serialNumber, String componentName, double unitCost, int quantity) {
        this.brandName = brandName;
        this.serialNumber = serialNumber;
        this.componentName = componentName;
        this.unitCost = unitCost;
        this.quantity = quantity;
    }

    public String getComponentName() {
        return componentName;
    }

    public double getUnitCost() {
        return unitCost;
    }

    public String getBrandName() {
        return brandName;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public int getQuantity() {
        return quantity;
    }

    @Override
    public String toString() {
        return this.componentName;
    }

}