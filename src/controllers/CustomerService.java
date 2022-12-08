package controllers;

import security.AESUtil;
import model.Address;
import model.users.Customer;
import model.CleanCustomerDetails;

import java.sql.*;

/**
 * Customer service. People can create an account to become a customer.
 * Customers can update the address or insert a new one as well as get their personal details.
 *
 * CustomerService.java
 */

public class CustomerService {

    /**
     * Updates the customer details with the new (validated) details form.
     * @param id The id of the customer to update
     * @param newDetails The new details
     * @return The customer object with the updated details
     */
    public Customer updateCustomerDetails(int id, CleanCustomerDetails newDetails) {
        if(!newDetails.isValid()) {
            System.out.println("Trying to update customer when the details aren't valid!");
            return null;
        }

        DatabaseService databaseService = ServiceProvider.getSingleton(DatabaseService.class);
        Connection connection = databaseService.getConnection();

        try {
            Address address = newDetails.getAddress();

            insertIgnoreAddress(address, connection);

            // Execute update
            String sql = "UPDATE customers as c SET c.forename = ?, c.surname = ?, c.houseNumber = ?, c.postcode = ? WHERE c.id = ?";
            PreparedStatement statement = connection.prepareStatement(sql);

            String encryptedForename = AESUtil.encryptString(newDetails.getForename());
            String encryptedSurname = AESUtil.encryptString(newDetails.getSurname());

            statement.setString(1, encryptedForename);
            statement.setString(2, encryptedSurname);
            statement.setString(3, address.getHouseNumber());
            statement.setString(4, address.getPostcode());
            statement.setInt(5, id);

            statement.executeUpdate();
            statement.close();

            System.out.println("Successfully updated customer's address");

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Failed to update customer's address");
        }
        return null;
    }

    /**
     * Returns the customer object in the database that matches the forename, surname and address specified.
     * @param forename The forename of the customer to search for
     * @param surname The surname of the customer to search for
     * @param address The address of the customer to search for
     * @return The customer object matching the requirements
     */
    public Customer getCustomer(String forename, String surname, Address address) {
        // connect to the database
        DatabaseService databaseService = ServiceProvider.getSingleton(DatabaseService.class);
        Connection connection = databaseService.getConnection();

        // Try and insert the Address object
        try {
            String encryptedForename = AESUtil.encryptString(forename);
            String encryptedSurname = AESUtil.encryptString(surname);

            String sql = "SELECT c.id, c.forename, c.surname, a.houseNumber, a.streetName, a.cityName, a.postCode FROM customers AS c INNER JOIN addresses a on c.houseNumber = a.houseNumber and c.postcode = a.postcode " +
                    "WHERE c.forename = ? AND c.surname = ? AND c.houseNumber = ? AND c.postcode = ?";
            PreparedStatement statement = connection.prepareStatement(sql);

            statement.setString(1, encryptedForename);
            statement.setString(2, encryptedSurname);
            statement.setString(3, address.getHouseNumber());
            statement.setString(4, address.getPostcode());

            ResultSet resultSet = statement.executeQuery();
            if(resultSet.next()) {
                int customerId = resultSet.getInt(1);

                String houseNumber = resultSet.getString(4);
                String streetName = resultSet.getString(5);
                String cityName = resultSet.getString(6);
                String postcode = resultSet.getString(7);

                return new Customer(customerId, forename, surname, new Address(houseNumber, streetName,cityName,postcode));
            } else {
                System.out.println("Failed");
            }
        } catch (Exception exception) {
            exception.printStackTrace();
            return null;
        }

        System.out.println("Failed to find a customer with credentials: " + forename + " " + surname);
        return null;
    }

    /**
     * Returns the customer object in the database that matches the integer id specified.
     * @param id The id of the customer
     * @return The customer object matching the id
     */
    public Customer getCustomer(int id) {
        // connect to the database
        DatabaseService databaseService = ServiceProvider.getSingleton(DatabaseService.class);
        Connection connection = databaseService.getConnection();

        // Try and insert the Address object
        try {
            String sql = "SELECT c.id, c.forename, c.surname, a.houseNumber, a.streetName, a.cityName, a.postCode FROM customers AS c INNER JOIN addresses a on c.houseNumber = a.houseNumber and c.postcode = a.postcode WHERE id = ?";
            PreparedStatement statement = connection.prepareStatement(sql);

            statement.setInt(1, id);

            ResultSet resultSet = statement.executeQuery();
            if(resultSet.next()) {
                int customerId = resultSet.getInt(1);
                String encryptedForename = resultSet.getString(2);
                String encryptedSurname = resultSet.getString(3);

                String forename = AESUtil.decryptString(encryptedForename);
                String surname = AESUtil.decryptString(encryptedSurname);

                String houseNumber = resultSet.getString(4);
                String streetName = resultSet.getString(5);
                String cityName = resultSet.getString(6);
                String postcode = resultSet.getString(7);

                Address address = new Address(houseNumber, streetName,cityName,postcode);

                return new Customer(customerId, forename, surname, address);
            }
        } catch (Exception exception) {
            // Roll back
            exception.printStackTrace();
            return null;
        }

        return null;
    }

    /**
     * create a customer if it doesn't exist, and then return true
     * @param customerDetails
     * @return True if either an account already exists, or one has been created successfully.
     */
    public Customer createCustomer(CleanCustomerDetails customerDetails) {
        if (customerDetails == null) {
            return null;
        }

        // connect to the database
        DatabaseService databaseService = ServiceProvider.getSingleton(DatabaseService.class);
        Connection connection = databaseService.getConnection();

        Address address = customerDetails.getAddress();

        // Try and insert the Address object
        try {
            insertIgnoreAddress(address, connection);
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Failed to insert into addresses table. Cannot create customer.");
            return null;
        }

        try {
            String encryptedFirstName = AESUtil.encryptString(customerDetails.getForename());
            String encryptedSurname = AESUtil.encryptString(customerDetails.getSurname());

            String sql = "INSERT IGNORE INTO customers VALUES (?, ?, ?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(sql);

            statement.setInt(1, 0); // This gets magically assigned
            statement.setString(2, encryptedFirstName);
            statement.setString(3, encryptedSurname);
            statement.setString(4, address.getHouseNumber());
            statement.setString(5, address.getPostcode());

            statement.executeUpdate();
            statement.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Failed to insert into customers table. Cannot create customer.");
            return null;
        }

        return getCustomer(customerDetails.getForename(), customerDetails.getSurname(), customerDetails.getAddress());
    }

    /**
     * Attempts to insert an address record. If one exists already, nothing is done.
     * @param address The address to add to the database
     * @param connection The database connection
     * @throws SQLException
     */
    private void insertIgnoreAddress(Address address, Connection connection) throws SQLException {
        String sql = "INSERT IGNORE INTO addresses VALUES (?, ?, ?, ?)";
        PreparedStatement statement = connection.prepareStatement(sql);

        statement.setString(1, address.getHouseNumber());
        statement.setString(2, address.getStreetName());
        statement.setString(3, address.getCityName());
        statement.setString(4, address.getPostcode());

        statement.executeUpdate();
        statement.close();
    }
}
