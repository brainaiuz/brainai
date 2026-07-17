package com.edatasite.shared.components;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import java.security.spec.KeySpec;
import java.text.DecimalFormat;

/**
 * Created by IntelliJ IDEA.
 * User: Aziz
 * Date: 20.01.2010
 * Time: 19:06:21
 * To change this template use File | Settings | File Templates.
 */
public class SessionCryptor {

    //This is a key for encryption/decryption, can take arbitrary hex value
    public static final String STANDARD_KEY = "133457799BBCDFF1";

    public static String encrypt(String data) {

        try {
            byte[] theKey = null;
            byte[] theMsg = null;

            DecimalFormat format = new DecimalFormat("0000000000000000");
            data = format.format(Integer.parseInt(data));

            theKey = hexToBytes(STANDARD_KEY);
            theMsg = hexToBytes(data);

            KeySpec ks = new DESKeySpec(theKey);
            SecretKeyFactory kf = SecretKeyFactory.getInstance("DES");
            SecretKey ky = kf.generateSecret(ks);
            Cipher cf = Cipher.getInstance("DES/ECB/NoPadding");
            cf.init(Cipher.ENCRYPT_MODE, ky);
            byte[] theCph = cf.doFinal(theMsg);
            /*System.out.println("Key     : "+bytesToHex(theKey));
            System.out.println("Message : "+bytesToHex(theMsg));
            System.out.println("Cipher  : "+bytesToHex(theCph));*/
            return bytesToHex(theCph);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("SessionCryptor ID: " + data);

        }
        return null;
    }


    public static String decrypt(String data) {
        try {
            byte[] theKey = null;
            byte[] theMsg = null;

            theKey = hexToBytes(STANDARD_KEY);
            theMsg = hexToBytes(data);


            KeySpec ks = new DESKeySpec(theKey);
            SecretKeyFactory kf = SecretKeyFactory.getInstance("DES");
            SecretKey ky = kf.generateSecret(ks);
            Cipher cf = Cipher.getInstance("DES/ECB/NoPadding");
            cf.init(Cipher.DECRYPT_MODE, ky);
            byte[] theCph = cf.doFinal(theMsg);

            return bytesToHex(theCph);
        } catch (Exception e) {
            e.printStackTrace();

        }
        return null;
    }

    ;


    public static byte[] hexToBytes(String str) {
        if (str == null) {
            return null;
        } else if (str.length() < 2) {
            return null;
        } else {
            int len = str.length() / 2;
            byte[] buffer = new byte[len];
            for (int i = 0; i < len; i++) {
                buffer[i] = (byte) Integer.parseInt(
                        str.substring(i * 2, i * 2 + 2), 16);
            }
            return buffer;
        }

    }

    public static String bytesToHex(byte[] data) {
        if (data == null) {
            return null;
        } else {
            int len = data.length;
            StringBuilder str = new StringBuilder();
            for (byte aData : data) {
                if ((aData & 0xFF) < 16) {
                    str.append("0").append(Integer.toHexString(aData & 0xFF));
                } else {
                    str.append(Integer.toHexString(aData & 0xFF));
                }
            }
            return str.toString().toUpperCase();
        }
    }
}
