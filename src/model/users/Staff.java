package model.users;

/**
 * The staff class contains the details pertaining to a staff member who would use the system. This will be saved in the database.
 */
public class Staff {

    private final String username;
    private final char[] password;

    public Staff(String username, char[] password) {
        this.username = username;
        this.password = password; 
    }

    public String getUsername() {
        return this.username;
    }
    public char[] getPassword() {
        return this.password;
    }
}
