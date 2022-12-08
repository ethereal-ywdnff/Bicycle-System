package controllers;

import model.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Manages data for components such as adding and removing components.
 * ComponentService.java
 */
public class ComponentService {

    /**
     * Adds a frameset component to the database. If it already exists with the brand name and serial number,
     * the insert will be ignored.
     *
     * @param brandName
     * @param serialNumber
     * @param componentName
     * @param unitCost
     * @param quantity
     * @param frameSize
     * @param shocks
     * @param gears
     * @return Returns true if successful, else false.
     */
    public boolean addFrameset(String brandName, String serialNumber, String componentName, double unitCost, int quantity, double frameSize, ShockAbsorberType shocks, int gears) {
        String sql = "INSERT INTO framesets VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        DatabaseService databaseService = ServiceProvider.getSingleton(DatabaseService.class);
        Connection connection = databaseService.getConnection();

        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, brandName);
            statement.setString(2, serialNumber);
            statement.setString(3, componentName);
            statement.setDouble(4, unitCost);
            statement.setInt(5, quantity);
            statement.setDouble(6, frameSize);
            statement.setInt(7, shocks.ordinal());
            statement.setInt(8, gears);

            statement.executeUpdate();
            statement.close();

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Adds a wheel component to the database.
     *
     * @param brandName
     * @param serialNumber
     * @param componentName
     * @param unitCost
     * @param quantity
     * @param diameter
     * @param wheelStyle
     * @param brakeType
     * @return Returns true if successful, else false.
     */
    public boolean addWheel(String brandName, String serialNumber, String componentName, double unitCost, int quantity, double diameter, WheelStyle wheelStyle, BrakeType brakeType) {
        String sql = "INSERT INTO wheels VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        DatabaseService databaseService = ServiceProvider.getSingleton(DatabaseService.class);
        Connection connection = databaseService.getConnection();

        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, brandName);
            statement.setString(2, serialNumber);
            statement.setString(3, componentName);
            statement.setDouble(4, unitCost);
            statement.setInt(5, quantity);
            statement.setDouble(6, diameter);
            statement.setInt(7, wheelStyle.ordinal());
            statement.setInt(8, brakeType.ordinal());

            statement.executeUpdate();
            statement.close();

            System.out.println("Successfully added a wheel component to the database.");

            return true;
        } catch (Exception e) {
            e.printStackTrace();

            return false;
        }
    }

    /**
     * Adds a handlebar component to the database
     *
     * @param brandName
     * @param serialNumber
     * @param componentName
     * @param unitCost
     * @param quantity
     * @param handleBarType
     * @return Returns true if successful, else false.
     */
    public boolean addHandlebar(String brandName, String serialNumber, String componentName, double unitCost, int quantity, HandleBarType handleBarType) {
        String sql = "INSERT INTO handlebars VALUES (?, ?, ?, ?, ?, ?)";
        DatabaseService databaseService = ServiceProvider.getSingleton(DatabaseService.class);
        Connection connection = databaseService.getConnection();

        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, brandName);
            statement.setString(2, serialNumber);
            statement.setString(3, componentName);
            statement.setDouble(4, unitCost);
            statement.setInt(5, quantity);
            statement.setInt(6, handleBarType.ordinal());

            statement.executeUpdate();
            statement.close();

            return true;
        } catch (Exception e) {
            e.printStackTrace();

            return false;
        }
    }

    /**
     * Returns the entire list of frame-sets present in the database table 'framesets'
     * @return The list of framesets present in the database
     */
    public List<FrameSet> getAllFrameSets() {
        String sql = "SELECT * FROM framesets";
        DatabaseService databaseService = ServiceProvider.getSingleton(DatabaseService.class);
        Connection connection = databaseService.getConnection();

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet queryResult = preparedStatement.executeQuery();
            ArrayList<FrameSet> result = new ArrayList<FrameSet>();

            while (queryResult.next()) {
                String brandName = queryResult.getString(1);
                String serialNumber = queryResult.getString(2);
                String componentName = queryResult.getString(3);
                double unitCost = queryResult.getDouble(4);
                int quantity = queryResult.getInt(5);
                double frameSize = queryResult.getDouble(6);
                int shocks = queryResult.getInt(7);
                int gears = queryResult.getInt(8);

                FrameSet frameSet = new FrameSet(brandName, serialNumber, componentName, unitCost, quantity, frameSize, ShockAbsorberType.values()[shocks], gears);
                result.add(frameSet);
            }

            preparedStatement.close();
            queryResult.close();

            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Returns the entire list of wheels present in the database table 'wheels'
     * @return The list of wheels present in the database
     */
    public List<Wheel> getAllWheels() {
        String sql = "SELECT * FROM wheels";
        DatabaseService databaseService = ServiceProvider.getSingleton(DatabaseService.class);
        Connection connection = databaseService.getConnection();

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet queryResult = preparedStatement.executeQuery();
            ArrayList<Wheel> result = new ArrayList<Wheel>();

            while (queryResult.next()) {
                String brandName = queryResult.getString(1);
                String serialNumber = queryResult.getString(2);
                String componentName = queryResult.getString(3);
                double unitCost = queryResult.getDouble(4);
                int quantity = queryResult.getInt(5);
                double diameter = queryResult.getDouble(6);
                int wheelStyle = queryResult.getInt(7);
                int brakeType = queryResult.getInt(8);

                result.add(new Wheel(brandName, serialNumber, componentName, unitCost, quantity, diameter, WheelStyle.values()[wheelStyle], BrakeType.values()[brakeType]));
            }

            preparedStatement.close();
            queryResult.close();

            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Returns the entire list of wheels present in the database table 'wheels'
     * @return The list of wheels present in the database
     */
    public List<HandleBar> getAllHandlebars() {
        String sql = "SELECT * FROM handlebars";
        DatabaseService databaseService = ServiceProvider.getSingleton(DatabaseService.class);
        Connection connection = databaseService.getConnection();

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet queryResult = preparedStatement.executeQuery();
            ArrayList<HandleBar> result = new ArrayList<HandleBar>();

            while (queryResult.next()) {
                String brandName = queryResult.getString(1);
                String serialNumber = queryResult.getString(2);
                String componentName = queryResult.getString(3);
                double unitCost = queryResult.getDouble(4);
                int quantity = queryResult.getInt(5);
                int handlebarType = queryResult.getInt(6);

                result.add(new HandleBar(brandName, serialNumber, componentName, unitCost, quantity, HandleBarType.values()[handlebarType]));
            }

            preparedStatement.close();
            queryResult.close();

            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Updates the frameset given a brand name. All parameters except the first one will be updated in the database.
     * @param brandName The brand name of the component to change
     * @param newComponentName The component name to replace the old component name
     * @param serialNumber The serial number of the component to change
     * @param unitCost The new unit cost
     * @param quantity The new quantity
     * @param frameSize The new frame size
     * @param shocksType The new shock absorber type
     * @param gearCount The new gear count
     */
    public void updateFrameset(String brandName, String newComponentName, String serialNumber, double unitCost, int quantity, double frameSize, ShockAbsorberType shocksType, int gearCount) {
        DatabaseService databaseService = ServiceProvider.getSingleton(DatabaseService.class);
        Connection connection = databaseService.getConnection();

        try {
            String sql = "UPDATE framesets as f SET f.componentName = ?, f.unitCost = ?, f.quantity = ?, f.frameSize = ?, f.shocks = ?, f.gears = ? WHERE f.brandName = ? AND f.serialNumber = ?";
            PreparedStatement statement = connection.prepareStatement(sql);

            statement.setString(1, newComponentName);
            statement.setDouble(2, unitCost);
            statement.setInt(3, quantity);
            statement.setDouble(4, frameSize);
            statement.setInt(5, shocksType.ordinal());
            statement.setInt(6, gearCount);
            statement.setString(7, brandName);
            statement.setString(8, serialNumber);

            statement.executeUpdate();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Updates the handlebar given a brand name. All parameters except the first one will be updated in the database.
     * @param brandName The brand name of the component to change
     * @param newComponentName The component name to replace the old component name
     * @param serialNumber The serial number of the component to change
     * @param unitCost The new unit cost
     * @param quantity The new quantity
     * @param handleBarType The new type of handlebar
     */
    public void updateHandlebar(String brandName, String newComponentName, String serialNumber, double unitCost, int quantity, HandleBarType handleBarType) {
        DatabaseService databaseService = ServiceProvider.getSingleton(DatabaseService.class);
        Connection connection = databaseService.getConnection();

        try {
            String sql = "UPDATE handlebars as h SET h.componentName = ?, h.unitCost = ?, h.quantity = ?, h.handleBarType = ? WHERE h.brandName = ? AND h.serialNumber = ?";
            PreparedStatement statement = connection.prepareStatement(sql);

            statement.setString(1, newComponentName);
            statement.setDouble(2, unitCost);
            statement.setInt(3, quantity);
            statement.setInt(4, handleBarType.ordinal());
            statement.setString(5, brandName);
            statement.setString(6, serialNumber);

            statement.executeUpdate();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Updates the handlebar given a brand name. All parameters except the first one will be updated in the database.
     * @param brandName The brand name of the component to change
     * @param newComponentName The component name to replace the old component name
     * @param serialNumber The serial number of the component to change
     * @param unitCost The new unit cost
     * @param quantity The new quantity
     * @param diameter The new diameter of the wheel
     * @param wheelStyle The new wheelstyle
     * @param brakeType The new brake type
     */
    public void updateWheel(String brandName, String newComponentName, String serialNumber, double unitCost, int quantity, double diameter, WheelStyle wheelStyle, BrakeType brakeType) {
        DatabaseService databaseService = ServiceProvider.getSingleton(DatabaseService.class);
        Connection connection = databaseService.getConnection();

        try {
            String sql = "UPDATE wheels as w SET w.componentName = ?, w.unitCost = ?, w.quantity = ?, w.diameter = ?, w.wheelStyle = ?, w.brakeType = ? WHERE w.brandName = ? AND w.serialNumber = ?";
            PreparedStatement statement = connection.prepareStatement(sql);

            statement.setString(1, newComponentName);
            statement.setDouble(2, unitCost);
            statement.setInt(3, quantity);
            statement.setDouble(4, diameter);
            statement.setInt(5, wheelStyle.ordinal());
            statement.setInt(6, brakeType.ordinal());
            statement.setString(7, brandName);
            statement.setString(8, serialNumber);

            statement.executeUpdate();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
