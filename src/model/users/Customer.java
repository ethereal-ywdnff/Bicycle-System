package model.users;

import model.Address;
import model.CleanCustomerDetails;

/**
 * The customer class contains the details pertaining to a customer who would use the system. This will be saved in the database.
 */
public class Customer extends CleanCustomerDetails {

    private int id;

    public Customer(int id, String forename, String surname, Address address) {
        super(forename, surname, address);

        this.id = id;
    }

    public int getId() {
        return id;
    }
}
