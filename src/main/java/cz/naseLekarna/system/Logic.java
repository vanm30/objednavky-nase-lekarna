package cz.naseLekarna.system;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Random;

/**
 * @author Matěj Vaník
 * @created 03.03.2022
 */
public class Logic {

    private static Logic logic = new Logic();

    public Logic() {
        logic = this;
    }

    public static Logic getLogic() {
        return logic;
    }

    private static final Random RANDOM = new SecureRandom();

    public String hashPassword(String passwordToHash) throws NoSuchAlgorithmException {
        String hashedPassword = null;

        MessageDigest md = MessageDigest.getInstance("SHA-256");

        md.update(passwordToHash.getBytes());
        byte[] bytes = md.digest();

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
        }

        return hashedPassword = sb.toString();
    }

    public String generateSalt() {
        byte[] salt = new byte[16];
        RANDOM.nextBytes(salt);
        return Base64.getEncoder().encodeToString(salt);
    }
}
