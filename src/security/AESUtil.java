package security;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.File;
import java.util.Base64;
import java.util.Scanner;

/**
 * Encrypt and decrypt the data in the database
 *
 * AESUtil.java
 */

public class AESUtil {
    private static Cipher cipher;
    private static final String key = "ppEO6AqjvsjgwfPbh5DvCQ==";
    /**
     * A helper function to encrypt a string using the embedded public key
     * @param text
     * @return The encrypted text
     * @throws Exception
     */
    public static String encryptString(String text) {
        try {
            cipher = Cipher.getInstance("aes");

            byte[] decodedKey = Base64.getDecoder().decode(key);
            SecretKey originalKey = new SecretKeySpec(decodedKey, 0, decodedKey.length, "aes");
            return encrypt(text, originalKey);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * A helper function to decrypt a string using the embedded private key
     * @param encrypted
     * @return
     * @throws Exception
     */
    public static String decryptString(String encrypted) throws Exception {
        cipher = Cipher.getInstance("aes");

        byte[] decodedKey = Base64.getDecoder().decode(key);
        SecretKey originalKey = new SecretKeySpec(decodedKey, 0, decodedKey.length, "aes");
        return decrypt(encrypted, originalKey);
    }

    /**
     * encrypt a plain text base on the secret key
     * @param plainText
     * @param secretKey
     * @return
     * @throws Exception
     */
    public static String encrypt(String plainText, SecretKey secretKey) throws Exception {
        byte[] plainTextByte = plainText.getBytes();
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        byte[] encryptedByte = cipher.doFinal(plainTextByte);
        Base64.Encoder encoder = Base64.getEncoder();
        String encryptedText = encoder.encodeToString(encryptedByte);
        return encryptedText;
    }

    /**
     * decrypt an encrypted text base on the secret key
     * @param encryptedText
     * @param secretKey
     * @return
     * @throws Exception
     */
    public static String decrypt(String encryptedText, SecretKey secretKey) throws Exception {
        Base64.Decoder decoder = Base64.getDecoder();
        byte[] encryptedTextByte = decoder.decode(encryptedText);
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        byte[] decryptedByte = cipher.doFinal(encryptedTextByte);
        String decryptedText = new String(decryptedByte);
        return decryptedText;
    }
}
