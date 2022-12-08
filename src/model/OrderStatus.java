package model;

/**
 * The status of an order. This is used to filter orders so staff members can progress an order.
 *
 * OrderStatus.java
 */
public enum OrderStatus {
    /**
     * In this state, an order has been placed but not 'consumed' by a staff member
     */
    PENDING,
    /**
     * This means that an order has been confirmed by a member of staff, waiting for payment.
     */
    CONFIRMED,
    /**
     * This means that a staff member has 'completed' an order, and is ready to be shipped.
     */
    FULFILLED;

    /**
     * Iterates through the enums of this class and
     * creates a list of them. This list is displayed
     * in the gui.
     *
     * @return string list of the enum values.
     */
    public static String[] getOrderStatusNames() {
        OrderStatus[] values = OrderStatus.values();
        String[] arr = new String[values.length];

        for (int i = 0; i < values.length; i++) {
            arr[i] = getOrderStatusName(values[i].toString());
        }

        return arr;
    }

    /**
     * The string value of an enum. Capitalises the first letter
     * and the rest is lower case.
     *
     * @return string name for an enum.
     */
    public static String getOrderStatusName(String statusName) {
        String name = statusName.toLowerCase();
        return name.substring(0, 1).toUpperCase() + name.substring(1);
    }
}
