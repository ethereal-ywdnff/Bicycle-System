package view.StaffSystem.Stocks;

import model.HandleBar;

/**
 * Creates the Handlebar table which is accessed by the staff.
 *
 * HandlebarTableModel.java
 */

public class HandlebarTableModel extends StockTableModel<HandleBar> {

    /**
     * Creates a table with the required column names.
     */
    public HandlebarTableModel() {
        this.addColumn("Brand Name");
        this.addColumn("Serial Number");
        this.addColumn("Component Name");
        this.addColumn("Unit Cost");
        this.addColumn("Quantity");
        this.addColumn("Handlebar Type");
    }

    /**
     * Adds a handlebar to the table.
     * @param handleBar to add to the table.
     */
    @Override
    public void addDataRow(HandleBar handleBar) {
        String brandName = handleBar.getBrandName();
        String serialNumber = handleBar.getSerialNumber();
        String componentName = handleBar.getComponentName();
        String unitCost = String.valueOf(handleBar.getUnitCost());
        String quantity = String.valueOf(handleBar.getQuantity());
        String handlebarType = String.valueOf(handleBar.getHandleType());

        String[] wheelList = {brandName, serialNumber, componentName, unitCost, quantity, handlebarType};
        this.addRow(wheelList);
        this.data.add(handleBar);
    }

    /**
     * Gets the data at a given row
     * @param rowIndex row to get the data from.
     * @return handlebar
     */
    public HandleBar getDataAt(int rowIndex) {
        return this.data.get(rowIndex);
    }
}
