import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.spec.SecretKeySpec;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

/**
 * Created by David on 02-Mar-17.
 */
public class EncryptionClass {
    final protected static char[] hexArray = "0123456789ABCDEF".toCharArray();
    final protected static String alfString = "abcdefghijklmnopqrstuvwxyzåäöABCDEFGHIJKLMNOPQRSTUVWXYZÅÄÖ";

    public static String[] encryptAES(String message) {
        KeyGenerator AESgen;
        byte[] dataToEncrypt = message.getBytes();
        byte[]keyContent;

        try {
            AESgen = KeyGenerator.getInstance("AES");
            AESgen.init(128);
            SecretKeySpec AESkey = (SecretKeySpec)AESgen.generateKey();
            keyContent = AESkey.getEncoded();

            Cipher AEScipher = Cipher.getInstance("AES");
            AEScipher.init(Cipher.ENCRYPT_MODE, AESkey);
            byte[] cipherData = AEScipher.doFinal(dataToEncrypt);

            return new String[]{bytesToHex(keyContent), bytesToHex(cipherData)};
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new String[]{""}; // kommer ej hända
    }

    public static String decryptAES(String key, String message) {
        byte[] keyContent = hexStringToByteArray(key);
        byte[] cipherData = hexStringToByteArray(message);
        SecretKeySpec decodeKey = new SecretKeySpec(keyContent, "AES");

        try {
            Cipher AEScipher = Cipher.getInstance("AES");
            AEScipher.init(Cipher.DECRYPT_MODE, decodeKey);
            byte[] decryptedData = AEScipher.doFinal(cipherData);
            return new String(decryptedData);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /*public static String[] encryptCaesar(String message) {
        Random random = new Random();
        int key = random.nextInt(28-1+1)+1;
        char[] charArray = message.toCharArray();
        for (char c : charArray) {
            char test = (char)(c + key);
            if (test > )
        }
    }*/





    private static String bytesToHex(byte[] bytes) {   // från stackoverflow
        char[] hexChars = new char[bytes.length * 2];
        for ( int j = 0; j < bytes.length; j++ ) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }

    public static byte[] hexStringToByteArray(String s) {  // från stackoverflow
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i+1), 16));
        }
        return data;
    }
}
