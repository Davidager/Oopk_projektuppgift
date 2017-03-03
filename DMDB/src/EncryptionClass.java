import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Random;
import java.util.stream.IntStream;

/**
 * Created by David on 02-Mar-17.
 */
public class EncryptionClass {
    final protected static char[] hexArray = "0123456789ABCDEF".toCharArray();
    final protected static char[] alphArray = "abcdefghijklmnopqrstuvwxyzåäö".toCharArray();
    final protected static char[] alphArrayCaps = "ABCDEFGHIJKLMNOPQRSTUVWXYZÅÄÖ".toCharArray();

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

    public static String[] encryptCaesar(String message) {
        Random random = new Random();
        //int key = random.nextInt(28-1+1)+1;
        int key = 1;
        char[] charArray = message.toCharArray();
        for (char c : charArray) {
            if (contains(c, alphArray)) {
                int newIndex = (indexOf(c, alphArray) + key)%29;
                charArray[indexOf(c, charArray)] = alphArray[newIndex];

            } else if (contains(c, alphArrayCaps)) {
                int newIndex = (indexOf(c, alphArrayCaps) + key)%29;
                charArray[indexOf(c, charArray)] = alphArrayCaps[newIndex];
            }

        }
        return new String[]{bytesToHex(Integer.toString(key).getBytes()),
                bytesToHex(String.valueOf(charArray).getBytes())};
    }

    public static String decryptCaesar(String stringKey, String message) {


        int key = Integer.parseInt(new String(hexStringToByteArray(stringKey), StandardCharsets.UTF_8));
        message = new String(hexStringToByteArray(message), StandardCharsets.UTF_8);
        char[] charArray = message.toCharArray();
        for (char c : charArray) {
            if (contains(c, alphArray)) {
                int newIndex = (indexOf(c, alphArray) + 29 - key)%29;
                charArray[indexOf(c, charArray)] = alphArray[newIndex];
            } else if (contains(c, alphArrayCaps)) {
                int newIndex = (indexOf(c, alphArrayCaps) + 29 - key)%29;
                charArray[indexOf(c, charArray)] = alphArrayCaps[newIndex];
            }

        }
        return String.valueOf(charArray);
    }



    private static int indexOf(char val, char[] arr) {    // från stackoverflow
        return IntStream.range(0, arr.length).filter(i -> arr[i] == val).findFirst().orElse(-1);
    }

    private static boolean contains(char c, char[] array) {     // från stackoverflow
        for (char x : array) {
            if (x == c) {
                return true;
            }
        }
        return false;
    }

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
