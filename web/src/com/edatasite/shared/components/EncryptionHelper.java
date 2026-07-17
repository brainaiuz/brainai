package com.edatasite.shared.components;

/**
 * Created by IntelliJ IDEA.
 * User: Ula
 * Date: Jun 23, 2007
 * Time: 10:34:13 AM
 * To change this template use File | Settings | File Templates.
 */

import org.apache.commons.codec.digest.DigestUtils;

import javax.crypto.*;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Base64;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EncryptionHelper {
    public static final String PASS_PHRASE = "passphraseForAntiBotEncryption";

    private EncryptionHelper() {
        throw new IllegalStateException("Utility class");
    }

    public static String encrypt(String msg, String pass) {
        if (pass == null) {
            pass = PASS_PHRASE;
        }
        try {
            KeySpec keySpec = new DESKeySpec(pass.getBytes());
            SecretKey key = SecretKeyFactory.getInstance("DES").generateSecret(keySpec);
            Cipher ecipher = Cipher.getInstance(key.getAlgorithm());
            ecipher.init(Cipher.ENCRYPT_MODE, key);
            //Encode the string into bytes using utf-8
            byte[] utf8 = msg.getBytes(StandardCharsets.UTF_8);
            //Encrypt
            byte[] enc = ecipher.doFinal(utf8);
            //Encode bytes to base64 to get a string
            return Base64.getEncoder().encodeToString(enc);
        } catch (InvalidKeyException | BadPaddingException | IllegalBlockSizeException | IllegalStateException | NoSuchPaddingException | NoSuchAlgorithmException | InvalidKeySpecException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String decrypt(String msg, String pass) {
        if (pass == null) {
            pass = PASS_PHRASE;
        }
        try {
            KeySpec keySpec = new DESKeySpec(pass.getBytes());
            SecretKey key = SecretKeyFactory.getInstance("DES").generateSecret(keySpec);
            Cipher decipher = Cipher.getInstance(key.getAlgorithm());
            decipher.init(Cipher.DECRYPT_MODE, key);

            // Clean the Base64 string and ensure it has the correct padding
            msg = msg.trim().replaceAll("[^A-Za-z0-9+/=]", "");
            msg = addBase64Padding(msg);

            // Decode base64 to get bytes
            byte[] dec = Base64.getDecoder().decode(msg);
            //Decrypt
            byte[] utf8 = decipher.doFinal(dec);
            //Decode using utf-8
            return new String(utf8, StandardCharsets.UTF_8);
        } catch (InvalidKeyException | BadPaddingException | IllegalBlockSizeException | IllegalStateException | NoSuchPaddingException | NoSuchAlgorithmException | InvalidKeySpecException e) {
            return msg;
        }
    }

    private static String addBase64Padding(String base64) {
        int paddingCount = 4 - (base64.length() % 4);
        if (paddingCount < 4) {
            for (int i = 0; i < paddingCount; i++) {
                base64 += "=";
            }
        }
        return base64;
    }

    public static String encrypt(String msg) {
        return encrypt(msg, PASS_PHRASE);
    }

    public static String decrypt(String msg) {
        return decrypt(msg, PASS_PHRASE);
    }

    /**
     * Encodes string if it intended to be passed in content of  URL
     *
     * @param url to be encrypted and encoded
     * @return
     */
    public static String encryptURL(String url) {
        return encodeURL(encrypt(url));
    }

    /**
     * Encodes string if it intended to be passed in content of  URL
     *
     * @param url to be encrypted and encoded
     * @return
     */
    public static String encryptURL(String url, String pass) {
        return encodeURL(encrypt(url, pass));
    }

    public static String decryptURL(String url) {
        String result = decrypt(decodeURL(url));
        if (result == null) {
            result = decrypt(decodeURL(decodeURL(url)));
        }
        if (result == null || !url.contains("%")) {
            result = decrypt(url);
        }
        return result;
    }

    /**
     * Encodes encrypted URL to be passsed in content of URL
     *
     * @param url encrypted URL to be encoded
     * @return
     */
    public static String encodeURL(String url) {
        return URLEncoder.encode(url, StandardCharsets.UTF_8);
    }


    public static String decodeURL(String url) {
        return URLDecoder.decode(url, StandardCharsets.UTF_8);
    }


    public static String md5(String password) {
        if (password == null) {
            return null;
        }
        return DigestUtils.md5Hex(password);
    }

    public static final String CONST_SALT = "$ECRET-KEY";

    public static String sha512(String password, String salt) {
        if (password == null) {
            return null;
        }
        if (salt == null) {
            salt = "";
        }
        return DigestUtils.sha512Hex(CONST_SALT + salt + password);
    }

    public static String sha512Second(String password, String salt) {
        if (salt == null) {
            salt = "";
        }
        byte[] bytes = password.getBytes(StandardCharsets.UTF_16LE);
        byte[] array = Base64.getDecoder().decode(salt);
        byte[] array2 = new byte[bytes.length + array.length];
        System.arraycopy(bytes, 0, array2, 0, bytes.length);
        System.arraycopy(array, 0, array2, bytes.length, array.length);
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("SHA-512");
            return Base64.getEncoder().encodeToString(md.digest(array2));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static final String DOCUMENTS_APP_SALT = "*-_KPI_--2017_-*";

    public static String encryptAES(String plainText) {
        try {
            Cipher cipher = Cipher.getInstance("RSA/ECB/OAEPWITHSHA-256ANDMGF1PADDING");
            SecretKeySpec secretKey = new SecretKeySpec(DOCUMENTS_APP_SALT.getBytes(), "AES");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            byte[] cipherText = cipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(cipherText);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String decryptAES(String encryptedText) {
        try {
            Cipher cipher = Cipher.getInstance("RSA/ECB/OAEPWITHSHA-256ANDMGF1PADDING");
            SecretKeySpec secretKey = new SecretKeySpec(DOCUMENTS_APP_SALT.getBytes(), "AES");
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            byte[] cipherText = Base64.getDecoder().decode(encryptedText.getBytes(StandardCharsets.UTF_8));
            return new String(cipher.doFinal(cipherText), StandardCharsets.UTF_8);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String encodeBase64(String value){
        return Base64.getEncoder().encodeToString(value.getBytes());
    }

    public static String decodeBase64(String value){
        return new String(Base64.getDecoder().decode(value.getBytes()));
    }
}
