package security;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.SecureRandom;
import java.security.spec.KeySpec;
import java.util.Base64;

/**
 * Staff password are hashed unlike customer details as the customer details need
 * to be decrypted whereas staff passwords must use a 1 way hash.
 *
 * HashingStaffPassword.java
 */
public class HashingStaffPassword {

    /**
     * Creates a hash using a salt.
     * @param password plain password
     * @return a string in the from "hash:salt"
     */
    public static String createPasswordHashAndSalt(String password) {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        return hashPasswordGivenSalt(password, salt);
    }

    /**
     * Check if a password matches the hash
     * @param password input password.
     * @param saltAndHashedPassword from the database.
     * @return boolean whether the password is correct or not.
     */
    public static boolean checkPassword(String password, String saltAndHashedPassword) {
        String[] seperateSaltAndHash = saltAndHashedPassword.split(":");
        String userHash = seperateSaltAndHash[0];
        byte[] userSalt = Base64.getDecoder().decode(seperateSaltAndHash[1]);
        String hashForPasswordInput = hashPasswordGivenSalt(password, userSalt).split(":")[0];
        return userHash.equals(hashForPasswordInput);
    }

    /**
     * Uses the salt provided to create a hash.
     * @param password plain password.
     * @param salt
     * @return a string in the from "hash:salt"
     */
    private static String hashPasswordGivenSalt(String password, byte[] salt) {
        KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, 65536, 128);
        SecretKeyFactory secretKeyFactory = null;
        byte[] hash = null;

        try {
            secretKeyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            hash = secretKeyFactory.generateSecret(spec).getEncoded();
        } catch (Exception e) {
            System.out.println("Error during encryption.");
        }
        String saltString = Base64.getEncoder().encodeToString(salt);
        String hashString = Base64.getEncoder().encodeToString(hash);
        return hashString + ":" + saltString;
    }

}
