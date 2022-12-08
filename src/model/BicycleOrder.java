package model;

import model.users.Customer;
import model.users.Staff;

import java.util.Date;

/**
 * The Bicycle class contains the configuration selected by a user. This is the data that matches
 * the database schema.
 *
 * Bicycle.java
 */
public class BicycleOrder {

    public static BicycleOrder createNew(String name) {
        return new BicycleOrder(name);
    }
    public static BicycleOrder fromStore(int orderId, String bicycleName, FrameSet frameSet, Wheel wheel, HandleBar handleBar,
                                         Date dateOrdered, OrderStatus orderStatus, Customer customer, Staff processedBy) {
        BicycleOrder order = new BicycleOrder();
        order.orderId = orderId;
        order.bicycleName = bicycleName;
        order.frameSet=  frameSet;
        order.wheel = wheel;
        order.handleBar = handleBar;
        order.dateOrdered = dateOrdered;
        order.orderStatus = orderStatus;
        order.customer = customer;
        order.processedBy = processedBy;

        return order;
    }

    /**
     * We may have issues with using UUID.randomUUID() as there is such a tiny chance
     * of a collision.
     */
    private int orderId = -1;
    private String bicycleName;

    private FrameSet frameSet;
    private Wheel wheel;
    private HandleBar handleBar;

    private Date dateOrdered;
    private OrderStatus orderStatus;
    private Customer customer;
    private Staff processedBy;

    private BicycleOrder() {}

    /**
     * We should only use this constructor inside the database service class,
     * so that the ID can be consumed in business-logic code.
     *
     * @param customer
     */
    public BicycleOrder(Date dateOrdered, OrderStatus status, Customer customer, Staff processedBy) {
        this.dateOrdered = dateOrdered;
        this.orderStatus = status;
        this.customer = customer;
        this.processedBy = processedBy;
    }

    public BicycleOrder(Customer customer) {
        this.dateOrdered = new Date(System.currentTimeMillis());
        this.orderStatus = OrderStatus.PENDING;
        this.customer = customer;
        this.processedBy = null;
    }

    private BicycleOrder(String name) {
        this.bicycleName = name;
    }

    public BicycleOrder(String name, FrameSet frameSet, Wheel wheel, HandleBar handleBar) {
        this.bicycleName = name;

        this.frameSet = frameSet;
        this.wheel = wheel;
        this.handleBar = handleBar;
    }

    public String getBrandName() {
        if(this.frameSet == null || this.wheel == null) {
            return "";
        }

        return (this.frameSet.getBrandName() + this.wheel.getWheelStyle()).toLowerCase();
    }

    public int getOrderId() {
        return orderId;
    }

    public String getBicycleName() {
        return bicycleName;
    }

    public void setBicycleName(String bicycleName) {
        this.bicycleName = bicycleName;
    }

    public FrameSet getFrameSet() {
        return frameSet;
    }

    public void setFrameSet(FrameSet frameSet) {
        this.frameSet = frameSet;
    }

    public Wheel getWheel() {
        return wheel;
    }

    public void setWheel(Wheel wheel) {
        this.wheel = wheel;
    }

    public HandleBar getHandleBar() {
        return handleBar;
    }

    public void setHandleBar(HandleBar handleBar) {
        this.handleBar = handleBar;
    }

    /**
     * Checks if the bicycle is complete. There can be stages where the wheel/frameset/handlebar reference null and so
     * we shouldn't be able to go to checkout.
     * @return True if the bike is complete, false otherwise.
     */
    public boolean isComplete() {
        return this.frameSet != null && this.handleBar != null && this.wheel != null;
    }

    public Date getDateOrdered() {
        return dateOrdered;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public Customer getCustomer() {
        return customer;
    }

    /**
     * Gets the running total for all the components on the bike. If a component is null, it doesn't count the price
     * for that item.
     * @return The price in pounds.
     */
    public double getPrice() {
        double price = 0;
        price += wheel == null ? 0 : wheel.getUnitCost() * 2;
        price += frameSet == null ? 0 : frameSet.getUnitCost();
        price += handleBar == null ? 0 : handleBar.getUnitCost();

        return price;
    }

    public Staff getProcessedBy() {
        return processedBy;
    }
}
