package model;

import java.util.regex.Pattern;

/**
 * This is the structure that contains all Address information for a given customer. The fields
 * in this class match with the columns in the table.
 *
 * Address.java
 */
public class Address {

    private String houseNumber;
    private String streetName;
    private String cityName;
    private String postcode;

    public Address(String houseNumber, String streetName, String cityName, String postcode) {
        this.houseNumber = houseNumber;
        this.streetName = streetName;
        this.cityName = cityName;
        this.postcode = postcode;
    }

    public String getHouseNumber() {
        return houseNumber;
    }

    public void setHouseNumber(String houseNumber) {
        this.houseNumber = houseNumber;
    }

    public String getStreetName() {
        return streetName;
    }

    public void setStreetName(String streetName) {
        this.streetName = streetName;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getPostcode() {
        return postcode;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    public String toString() {
        return houseNumber + "\n" + streetName + "\n" + postcode + "\n" + cityName;
    }

    public String toStringOneLine() {
        return houseNumber + ", " + streetName + ", " + postcode + ", " + cityName;
    }

    /**
     * Checks if the address object is valid. If it isn't,
     * then we shouldn't be able to do actions such as placing an
     * order
     * @return True if the address is valid, false otherwise.
     */
    public boolean isValid() {
        System.out.println("House Number: " + regexAlphaNumericSpace(houseNumber));
        System.out.println("Street Name: " + isOnlyAlphabetCharactersRegex(streetName));
        System.out.println("City Name: : " + isOnlyAlphabetCharactersRegex(cityName));
        System.out.println("Postcode: " + regexAlphaNumericSpace(postcode));
        return regexAlphaNumericSpace(houseNumber) && isOnlyAlphabetCharactersRegex(streetName) &&
                isOnlyAlphabetCharactersRegex(cityName) && regexAlphaNumericSpace(postcode);
    }

    /**
     * Check if the input is valid. Allows letters, numbers and space but not spaces
     * on its own or other symbols.
     * @param string to check
     * @return if valid
     */
    private static boolean regexAlphaNumericSpace(String string) {
        return Pattern.matches("[a-zA-Z0-9\\s]+", string);
    }

    /**
     * Check if it only contains alphabet characters.
     * @param string to check.
     * @return true it if only contains alphabet characters.
     */
    private boolean isOnlyAlphabetCharactersRegex(String string) {
        return Pattern.matches("[a-zA-Z\\s]+", string);
    }
}
