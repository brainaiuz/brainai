package com.edatasite.workforce.gwt.core.client;

import com.googlecode.gwt.crypto.bouncycastle.DataLengthException;
import com.googlecode.gwt.crypto.bouncycastle.InvalidCipherTextException;
import com.googlecode.gwt.crypto.client.TripleDesCipher;

/**
 * Created with IntelliJ IDEA.
 * User: Fathulla
 * Date: 11.04.13
 * Time: 14:57
 * To change this template use File | Settings | File Templates.
 */
public class EncryptUtils {
    public static String encrypt(String str, String key) {
        if (str == "" || str == null) {
            return "";
        }
        TripleDesCipher cipher = new TripleDesCipher();
        cipher.setKey(key.length() > 24 ? key.substring(0, 23).getBytes() : key.getBytes());
        try {
            return cipher.encrypt(String.valueOf(str));
        } catch (DataLengthException | InvalidCipherTextException | IllegalStateException e1) {
            e1.printStackTrace();
        }
        return "";
    }

    public static String decrypt(String enc, String key) {
        if (enc == "" || enc == null) {
            return "";
        }
        TripleDesCipher cipher = new TripleDesCipher();
        cipher.setKey(key.length() > 24 ? key.substring(0, 23).getBytes() : key.getBytes());
        try {
            return cipher.decrypt(enc);
        } catch (DataLengthException | InvalidCipherTextException | IllegalStateException e) {
            e.printStackTrace();
        }
        return "";
    }
}
