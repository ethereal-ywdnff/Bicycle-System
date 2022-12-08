package model;

import java.util.regex.Pattern;

/**
 * We use this to store data from a customer login form. The reason we cannot use the 'Customer' class is because
 * this data is written to from the UI code, and has not been read from the database, and so AUTO_INCREMENT primary keys
 * are not reliable from that class.
 *
 * We sanitise all inputs, making sure that any trailing spaces are removed.
 */
public class CleanCustomerDetails {

    private String forename;
    private String surname;
    private Address address;

    public CleanCustomerDetails(String forename, String surname, Address address) {
        this.forename = formatInput(forename);
        this.surname = formatInput(surname);
        this.address = formatAddress(address);
    }

    public String getForename() {
        return forename;
    }
    public String getSurname() {
        return surname;
    }
    public Address getAddress() {
        return address;
    }

    /**
     * Checks if the fields of the form contain special characters in places where there shouldn't be.
     * @return True if the form is valid, false otherwise.
     */
    public boolean isValid() {
        System.out.println("Forename: " + isOnlyAlphabetCharactersRegex(forename));
        System.out.println("Surname: " + isOnlyAlphabetCharactersRegex(surname));
        return isOnlyAlphabetCharactersRegex(forename) && isOnlyAlphabetCharactersRegex(surname) && address.isValid();
    }

    /**
     * Check if it only contains alphabet characters.
     * @param string to check.
     * @return true it if only contains alphabet characters.
     */
    private static boolean isOnlyAlphabetCharactersRegex(String string) {
        return Pattern.matches("[a-zA-Z\\s]+", string);
    }

    private String formatInput(String inputString) {
        return inputString.trim().replaceAll("\\s+", " ").toLowerCase();
    }

    private Address formatAddress(Address address) {
        address.setHouseNumber(formatInput(address.getHouseNumber()));
        address.setStreetName(formatInput(address.getStreetName()));
        address.setCityName(formatInput(address.getCityName()));
        address.setPostcode(formatInput(address.getPostcode()));
        return address;
    }
}
