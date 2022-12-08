package view.StaffSystem;

import model.BicycleOrder;
import model.users.Staff;

import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;
import java.util.List;

/**
 * Creates the table for customer orders.
 * Staff use this table to manage customer orders.
 *
 * OrderTableModel.java
 */

public class OrderTableModel extends DefaultTableModel {

    private List<BicycleOrder> orders = new ArrayList<>();

    /**
     * Creates the orders table with the required column names.
     */
    public OrderTableModel() {
        this.addColumn("Customer ID");
        this.addColumn("Forename");
        this.addColumn("Surname");
        this.addColumn("Date");
        this.addColumn("Address");
        this.addColumn("Order Number");
        this.addColumn("Status");
        this.addColumn("Processed By");
    }

    /**
     * Adds an order to the table in the required columns.
     * @param order to add to the table.
     */
    public void addOrderRow(BicycleOrder order) {
        String customerId = String.valueOf(order.getCustomer().getId());
        String forename = order.getCustomer().getForename();
        String surname = order.getCustomer().getSurname();
        String date = order.getDateOrdered().toString();
        String address = order.getCustomer().getAddress().toStringOneLine();
        int orderNum = order.getOrderId();
        String status = order.getOrderStatus().toString();
        Staff processedBy = order.getProcessedBy();
        String[] orderList = {customerId, forename, surname, date, address, String.valueOf(orderNum), status, processedBy == null ? "Nobody" : processedBy.getUsername()};
        this.addRow(orderList);
        orders.add(order);
    }

    /**
     * Gets the order at a given index.
     * @param rowIndex index of the required row.
     * @return order at the index
     */
    public BicycleOrder getOrderAt(int rowIndex) {
        return orders.get(rowIndex);
    }

    /**
     * Clears the table and the orders.
     */
    public void reset() {
        this.setRowCount(0);
        orders = new ArrayList<>();
    }
}
