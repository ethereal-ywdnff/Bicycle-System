package view.StaffSystem.Stocks;

import model.FrameSet;

/**
 * Creates the Frameset table which is accessed by the staff.
 *
 * FramesetTableModel.java
 */

public class FramesetTableModel extends StockTableModel<FrameSet> {

    /**
     * Creates a table with the required column names.
     */
    public FramesetTableModel() {
        this.addColumn("Brand Name");
        this.addColumn("Serial Number");
        this.addColumn("Component Name");
        this.addColumn("Unit Cost");
        this.addColumn("Quantity");
        this.addColumn("Frame Size");
        this.addColumn("Shocks");
        this.addColumn("Gear Count");
    }

    /**
     * Adds a frameset to the table.
     * @param frameset to add to the table.
     */
    @Override
    public void addDataRow(FrameSet frameset) {
        String brandName = frameset.getBrandName();
        String serialNumber = frameset.getSerialNumber();
        String componentName = frameset.getComponentName();
        String unitCost = String.valueOf(frameset.getUnitCost());
        String quantity = String.valueOf(frameset.getQuantity());
        String frameSize = String.valueOf(frameset.getFrameSize());
        String shocks = String.valueOf(frameset.getShocks());
        String gears = String.valueOf(frameset.getGears());

        // Add items to each column
        String[] row = {brandName, serialNumber, componentName, unitCost, quantity, frameSize, shocks, gears};
        this.addRow(row);
        this.data.add(frameset);
    }
}
