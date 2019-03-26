package se.vgregion.secure.util;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Formatter;

public class HmacUtil {

    private static final String MAC_ALGORITHM = "HMACmd5";

    public static String calculateHmac(String message, String key) {
        String calculatedHmac;
        try {
            Mac hmacInstance = Mac.getInstance(MAC_ALGORITHM);

            hmacInstance.init(new SecretKeySpec(key.getBytes(), MAC_ALGORITHM));

            byte[] bytes = hmacInstance.doFinal(message.getBytes(StandardCharsets.UTF_8));

            calculatedHmac = toHexString(bytes);
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            throw new RuntimeException(e);
        }
        return calculatedHmac;
    }

    private static String toHexString(byte[] bytes) {
        Formatter formatter = new Formatter();

        for (byte b : bytes) {
            formatter.format("%02x", b);
        }

        return formatter.toString();
    }
}
