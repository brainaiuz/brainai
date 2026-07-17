package com.edatasite.workforce.gwt.core.client.rpc;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;

public class EncryptionUtils {

    private static final String ALGORITHM = "AES/CBC/PKCS5Padding";

    /**
     * IMPORTANT: This key must be 16, 24, or 32 bytes.
     * In a real production app, do NOT hardcode this.
     * Load it from an Environment Variable or a secure Secret Manager.
     */
    private static final String MASTER_KEY =  "KPI_SECRET_KEY";

    public static String encrypt(String plainText) {
        try {
            // 1. Generate a random IV (Initialization Vector)
            byte[] iv = new byte[16];
            new SecureRandom().nextBytes(iv);
            IvParameterSpec ivSpec = new IvParameterSpec(iv);

            // 2. Prepare the Key (Hash the master key to ensure it's exactly 32 bytes for AES-256)
            SecretKeySpec keySpec = deriveKey(MASTER_KEY);

            // 3. Encrypt
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec);
            byte[] cipherText = cipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8));

            // 4. Combine IV + CipherText into one string for storage
            // This is safe because the IV doesn't need to be secret, just unique.
            byte[] combined = new byte[iv.length + cipherText.length];
            System.arraycopy(iv, 0, combined, 0, iv.length);
            System.arraycopy(cipherText, 0, combined, iv.length, cipherText.length);

            return Base64.getEncoder().encodeToString(combined);
        } catch (Exception e) {
            throw new RuntimeException("Encryption failed", e);
        }
    }

    public static String decrypt(String encryptedText) {
        try {
            if (encryptedText == null || encryptedText.isBlank()) {
                throw new IllegalArgumentException(
                        "encryptedText must not be null or blank");
            }

            byte[] combined = Base64.getDecoder().decode(encryptedText);

            // Need at least 16 bytes for the IV + 1 byte of ciphertext
            if (combined.length <= 16) {
                throw new IllegalArgumentException(
                        "Decoded data too short to contain IV + ciphertext (got "
                                + combined.length + " bytes)");
            }

            // 1. Extract IV (first 16 bytes)
            byte[] iv = new byte[16];
            System.arraycopy(combined, 0, iv, 0, iv.length);
            IvParameterSpec ivSpec = new IvParameterSpec(iv);

            // 2. Extract CipherText (remaining bytes)
            byte[] cipherText = new byte[combined.length - 16];
            System.arraycopy(combined, 16, cipherText, 0, cipherText.length);

            // 3. Prepare Key
            SecretKeySpec keySpec = deriveKey(MASTER_KEY);

            // 4. Decrypt
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);
            byte[] plainText = cipher.doFinal(cipherText);

            return new String(plainText, StandardCharsets.UTF_8);
        } catch (IllegalArgumentException e) {
            throw e; // re-throw validation errors as-is
        } catch (Exception e) {
            throw new RuntimeException(
                    "Decryption failed. Key might be wrong or data is corrupted.", e);
        }
    }

    private static SecretKeySpec deriveKey(String key) throws Exception {
        MessageDigest sha = MessageDigest.getInstance("SHA-256");
        byte[] hashedKey = sha.digest(key.getBytes(StandardCharsets.UTF_8));
        return new SecretKeySpec(hashedKey, "AES");
    }
}