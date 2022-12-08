package controllers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Creates and manages the connection to the database. We persist the same database connection throughout the
 * application life-cycle so that lots of queries take minimal time.
 *
 * DatabaseService.java
 */
public class DatabaseService {

    private static final String URL = "jdbc:mysql://stusql.dcs.shef.ac.uk/";
    private static final String DbName = "team010?allowMultiQueries=true";
    private static final String User = "team010";
    private static final String Password = "1395a1a5";

    private Connection connection;

    public DatabaseService() {
        try {
            this.connection = DriverManager.getConnection(URL + DbName, User, Password);
            this.createTables();

            System.out.println("Successfully connected to database");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Connection getConnection() {
        return connection;
    }

    private void executePreparedStatement(String sql) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Create all the tables that needed
     */
    private void createTables() {
        String createStaffTableQuery = "CREATE TABLE IF NOT EXISTS staff (username VARCHAR(100), password VARCHAR(100), PRIMARY KEY(username));";
        String createAddressesQuery = "CREATE TABLE IF NOT EXISTS addresses (" +
                "houseNumber VARCHAR(15), " +
                "streetName VARCHAR(45)," +
                "cityName VARCHAR(45)," +
                "postcode VARCHAR(20)," +
                "PRIMARY KEY(houseNumber, postcode));";
        String createCustomersQuery = "CREATE TABLE IF NOT EXISTS customers (" +
                "id INTEGER AUTO_INCREMENT," +
                "forename VARCHAR(100)," +
                "surname VARCHAR(100)," +
                "houseNumber VARCHAR(15)," +
                "postcode VARCHAR(20)," +
                "UNIQUE (forename, surname, houseNumber, postcode)," +
                "FOREIGN KEY(houseNumber, postcode) REFERENCES addresses (houseNumber, postcode)," +
                "PRIMARY KEY(id));";

        String createWheelsQuery = "CREATE TABLE IF NOT EXISTS wheels (" +
                "brandName VARCHAR(100)," +
                "serialNumber VARCHAR(100)," +
                "componentName VARCHAR(100)," +
                "unitCost REAL," +
                "quantity INTEGER," +
                "diameter REAL," +
                "wheelStyle INTEGER," +
                "brakeType INTEGER," +
                "PRIMARY KEY(brandName,serialNumber));";
        String createFramesetsQuery = "CREATE TABLE IF NOT EXISTS framesets (" +
                "brandName VARCHAR(100)," +
                "serialNumber VARCHAR(100)," +
                "componentName VARCHAR(100)," +
                "unitCost REAL," +
                "quantity INTEGER," +
                "frameSize REAL," +
                "shocks INTEGER," +
                "gears INTEGER," +
                "PRIMARY KEY(brandName, serialNumber));";

        String createHandlebarsQuery = "CREATE TABLE IF NOT EXISTS handlebars (" +
                "brandName VARCHAR(100)," +
                "serialNumber VARCHAR(100)," +
                "componentName VARCHAR(100)," +
                "unitCost REAL," +
                "quantity INTEGER," +
                "handlebarType INTEGER," +
                "PRIMARY KEY(brandName, serialNumber));";

        String createOrdersQuery = "CREATE TABLE IF NOT EXISTS orders (" +
                "bicycleSerialNumber INT AUTO_INCREMENT," +
                "customerId INT," +
                "dateOrdered DATE," +
                "status INT," +
                "processedBy VARCHAR(100)," +
                "bicycleName VARCHAR(100)," +
                "wheelBrandName VARCHAR(100)," +
                "wheelSerialNumber VARCHAR(100)," +
                "framesetBrandName VARCHAR(100)," +
                "framesetSerialNumber VARCHAR(100)," +
                "handlebarBrandName VARCHAR(100)," +
                "handlebarSerialNumber VARCHAR(100)," +
                "PRIMARY KEY(bicycleSerialNumber)," +
                "FOREIGN KEY (framesetBrandName, framesetSerialNumber) REFERENCES framesets (brandName, serialNumber)," +
                "FOREIGN KEY (handlebarBrandName, handlebarSerialNumber) REFERENCES handlebars (brandName, serialNumber)," +
                "FOREIGN KEY (customerId) REFERENCES customers (id)," +
                "FOREIGN KEY (processedBy) REFERENCES staff (username)," +
                "FOREIGN KEY (wheelBrandName, wheelSerialNumber) REFERENCES wheels (brandName, serialNumber));";

        executePreparedStatement(createStaffTableQuery);
        executePreparedStatement(createAddressesQuery);
        executePreparedStatement(createCustomersQuery);
        executePreparedStatement(createWheelsQuery);
        executePreparedStatement(createFramesetsQuery);
        executePreparedStatement(createHandlebarsQuery);
        executePreparedStatement(createOrdersQuery);
    }
}
