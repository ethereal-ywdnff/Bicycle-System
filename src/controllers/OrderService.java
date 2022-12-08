package controllers;

import model.*;
import model.users.Customer;
import model.CleanCustomerDetails;
import model.users.Staff;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Order service. Customers can create an order or delete one.
 * Also, this class can provide the order details
 *
 * OrderService.java
 */
public class OrderService {

    /**
     * Creates a new order record and saves it to the database. If this is a new customer, it will automatically create
     * the new customer record in the database
     * @param order
     * @param customerDetails
     */
    public BicycleOrder createOrder(BicycleOrder order, CleanCustomerDetails customerDetails) {
        DatabaseService databaseService = ServiceProvider.getSingleton(DatabaseService.class);
        CustomerService customerService = ServiceProvider.getSingleton(CustomerService.class);
        Connection connection = databaseService.getConnection();

        try {
            // Create the customer and then add the order into `orders` table
            Customer customer = customerService.createCustomer(customerDetails);
            if (customer == null) {
                System.out.println("Failed to create customer, cannot create order.");
                return null;
            }

            String sql = "INSERT INTO orders values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            statement.setInt(1, 0); // Auto-assigned
            statement.setInt(2, customer.getId());
//            statement.setInt(3, order.getOrderId());
            statement.setDate(3, new Date(System.currentTimeMillis()));
            statement.setInt(4, OrderStatus.PENDING.ordinal());
            statement.setString(5, null);

            statement.setString(6, order.getBicycleName());
            statement.setString(7, order.getWheel().getBrandName());
            statement.setString(8, order.getWheel().getSerialNumber());
            statement.setString(9, order.getFrameSet().getBrandName());
            statement.setString(10, order.getFrameSet().getSerialNumber());
            statement.setString(11, order.getHandleBar().getBrandName());
            statement.setString(12, order.getHandleBar().getSerialNumber());

            int rowsUpdated = statement.executeUpdate();

            if(rowsUpdated > 0) {
                ResultSet generatedKeys = statement.getGeneratedKeys();
                if (generatedKeys.next()) {
                    System.out.println("Found key of" + generatedKeys.getInt(1));
                    return getOrderById(generatedKeys.getInt(1));
                }
            }

            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Delete the order based on an id
     * @param id
     */
    public void deleteOrderById(int id) {
        DatabaseService databaseService = ServiceProvider.getSingleton(DatabaseService.class);
        Connection connection = databaseService.getConnection();

        try {
            System.out.println("Deleting order of id " + id);
            String sql = "DELETE FROM orders WHERE bicycleSerialNumber = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setInt(1, id);

            preparedStatement.executeUpdate();
            preparedStatement.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Returns the order object with the given id
     * @param id The id to search for
     * @return The order object. Null if order with id is not present in the database
     */
    public BicycleOrder getOrderById(int id) {
        List<BicycleOrder> orders = getAllOrders();
        System.out.println("Length of orders returned: " + orders.stream().count());

        Optional<BicycleOrder> optional = orders.stream()
                .filter(x -> x.getOrderId() == id)
                .findFirst();

        return optional.orElse(null);
    }

    /**
     * Returns all orders pertaining to a specified customerId
     * @param customerId The id that represents the customer
     * @return A list of orders matching the criteria
     */
    public List<BicycleOrder> getAllOrdersForCustomer(int customerId) {
        List<BicycleOrder> orders = getAllOrders();

        return orders.stream()
                .filter(x -> x.getCustomer().getId() == customerId)
                .collect(Collectors.toList());
    }

    /**
     * Returns all orders in the database.
     * @return A list of orders
     */
    public List<BicycleOrder> getAllOrders() {
        System.out.println("Getting all orders");
        DatabaseService databaseService = ServiceProvider.getSingleton(DatabaseService.class);
        CustomerService customerService = ServiceProvider.getSingleton(CustomerService.class);
        StaffService staffService = ServiceProvider.getSingleton(StaffService.class);
        Connection connection = databaseService.getConnection();

        try {
            String sql = "SELECT bicycleSerialNumber, customerId, dateOrdered, status, processedBy, bicycleName, f.*, h.*, w.* FROM orders\n" +
                    "INNER JOIN framesets f on orders.framesetBrandName = f.brandName and orders.framesetSerialNumber = f.serialNumber\n" +
                    "INNER JOIN handlebars h on orders.handlebarBrandName = h.brandName and orders.handlebarSerialNumber = h.serialNumber\n" +
                    "INNER JOIN wheels w on orders.wheelBrandName = w.brandName and orders.wheelSerialNumber = w.serialNumber";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            ResultSet resultSet = preparedStatement.executeQuery();

            List<BicycleOrder> orders = new ArrayList<>();

            while (resultSet.next()) {
                int orderId = resultSet.getInt(1);
                Customer customer = customerService.getCustomer(resultSet.getInt(2));
//                String orderId = resultSet.getString(3);
                Date dateOrdered = resultSet.getDate(3);
                OrderStatus orderStatus = OrderStatus.values()[resultSet.getInt(4)];
                Staff processedBy = staffService.getStaff(resultSet.getString(5));
                String bikeName = resultSet.getString(6);

                String framesetBrandName = resultSet.getString(7);
                String framesetSerialNumber = resultSet.getString(8);
                String framesetComponentName = resultSet.getString(9);
                double framesetUnitCost = resultSet.getDouble(10);
                int framesetQuantity = resultSet.getInt(11);
                double framesetFrameSize = resultSet.getDouble(12);
                int framesetShocks = resultSet.getInt(13);
                int framesetGears = resultSet.getInt(14);

                FrameSet frameset = new FrameSet(framesetBrandName, framesetSerialNumber, framesetComponentName, framesetUnitCost, framesetQuantity, framesetFrameSize, ShockAbsorberType.values()[framesetShocks], framesetGears);

                String handlebarBrandName = resultSet.getString(15);
                String handlebarSerialNumber = resultSet.getString(16);
                String handlebarComponentName = resultSet.getString(17);
                double handlebarUnitCost = resultSet.getDouble(18);
                int handlebarQuantity = resultSet.getInt(19);
                int handlebarType = resultSet.getInt(20);

                HandleBar handleBar = new HandleBar(handlebarBrandName, handlebarSerialNumber, handlebarComponentName, handlebarUnitCost, handlebarQuantity, HandleBarType.values()[handlebarType]);

                String wheelBrandName = resultSet.getString(21);
                String wheelSerialNumber = resultSet.getString(22);
                String wheelComponentName = resultSet.getString(23);
                double wheelUnitCost = resultSet.getDouble(24);
                int wheelQuantity = resultSet.getInt(25);
                double wheelDiameter = resultSet.getDouble(26);
                int wheelStyle = resultSet.getInt(27);
                int wheelBrakeType = resultSet.getInt(28);

                Wheel wheel = new Wheel(wheelBrandName, wheelSerialNumber, wheelComponentName, wheelUnitCost, wheelQuantity, wheelDiameter, WheelStyle.values()[wheelStyle], BrakeType.values()[wheelBrakeType]);
                BicycleOrder order = BicycleOrder.fromStore(orderId, bikeName, frameset, wheel, handleBar, dateOrdered, orderStatus, customer, processedBy);

                orders.add(order);
            }

            preparedStatement.close();
            resultSet.close();

            return orders;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Sets the order status of a given order.
     * @param orderId The order number to change the status of
     * @param status The new status for the given order
     */
    public void setOrderStatus(int orderId, OrderStatus status) {
        DatabaseService databaseService = ServiceProvider.getSingleton(DatabaseService.class);
        Connection connection = databaseService.getConnection();

        try {
            String sql = "UPDATE orders SET status = ? WHERE bicycleSerialNumber = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setInt(1, status.ordinal());
            preparedStatement.setInt(2, orderId);

            preparedStatement.executeUpdate();
            preparedStatement.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean fulfillOrder(BicycleOrder order, Staff staff) {
        ComponentService componentService = ServiceProvider.getSingleton(ComponentService.class);
        DatabaseService databaseService = ServiceProvider.getSingleton(DatabaseService.class);
        Connection connection = databaseService.getConnection();

        Wheel wheel = order.getWheel();
        FrameSet frameSet = order.getFrameSet();
        HandleBar handleBar = order.getHandleBar();

        // We can't fulfill an order if the quantity does not permit us to.
        if(wheel.getQuantity() < 2 || frameSet.getQuantity() < 1 || handleBar.getQuantity() < 1) {
            return false;
        }

        // Update wheel
        componentService.updateWheel(wheel.getBrandName(), wheel.getComponentName(), wheel.getSerialNumber(), wheel.getUnitCost(), wheel.getQuantity() - 2, wheel.getDiameter(), wheel.getWheelStyle(), wheel.getBrakeType());

        // Update wheel
        componentService.updateFrameset(frameSet.getBrandName(), frameSet.getComponentName(), frameSet.getSerialNumber(), frameSet.getUnitCost(), frameSet.getQuantity() - 1, frameSet.getFrameSize(), frameSet.getShocks(), frameSet.getGears());

        // Update wheel
        componentService.updateHandlebar(handleBar.getBrandName(), handleBar.getComponentName(), handleBar.getSerialNumber(), handleBar.getUnitCost(), handleBar.getQuantity() - 1, handleBar.getHandleType());

        // Set the fulfilled staff member on order
        try {
            String sql = "UPDATE orders SET processedBy = ? WHERE bicycleSerialNumber = ?";
            PreparedStatement statement = connection.prepareStatement(sql);

            statement.setString(1, staff.getUsername());
            statement.setInt(2, order.getOrderId());

            statement.executeUpdate();
            statement.close();

            System.out.println("Successfully fulfilled an order");
        } catch (Exception e) {
            System.out.println("Failed to fulfil the order. Could not update the 'processedBy' column");
        }

        return true;
    }
}
