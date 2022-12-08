package controllers;

import model.users.Staff;
import security.HashingStaffPassword;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * Manages the staff database.
 *
 * StaffService.java
 */
public class StaffService {

    /**
     * Checks if this staff user exists and return the staff object if it exists.
     *
     * @return Staff object if the staff exists otherwise null.
     */
    public Staff getStaff(String username) {
        DatabaseService databaseService = ServiceProvider.getSingleton(DatabaseService.class);
        Connection connection = databaseService.getConnection();

        try {
            String queryStaff = "SELECT * FROM team010.staff WHERE username = ?";
            PreparedStatement staffQuery = connection.prepareStatement(queryStaff);

            staffQuery.setString(1, username);

            ResultSet staffDetails = staffQuery.executeQuery();

            if(staffDetails.next()) {
                final String pwd = staffDetails.getString("password");

                return new Staff(username, pwd.toCharArray());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Checks if this staff user exists and return the staff object if it exists.
     *
     * @return Staff object if the staff exists otherwise null.
     */
    public Staff staffLogin(String username, char[] password) {
        StaffService staffService = ServiceProvider.getSingleton(StaffService.class);

        Staff staff = staffService.getStaff(username);
        if(staff == null) {
            return null;
        }

        boolean isPassword = HashingStaffPassword.checkPassword(new String(password), new String(staff.getPassword()));

        if (isPassword) {
            return new Staff(username, password);
        }

        return null;
    }
}
