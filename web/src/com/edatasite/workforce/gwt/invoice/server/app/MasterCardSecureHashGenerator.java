package com.edatasite.workforce.gwt.invoice.server.app;

import com.edatasite.shared.components.EncryptionHelper;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Formatter;
import java.util.Iterator;
import java.util.Map;
import java.util.SortedMap;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: 8/14/12
 * Time: 7:09 PM
 * To change this template use File | Settings | File Templates.
 */
public class MasterCardSecureHashGenerator {

    private static final Logger log = LoggerFactory.getLogger(MasterCardSecureHashGenerator.class);

    private static final String HMAC_SHA256 = "HmacSHA256";
    private static final String UTF8_CHARSET = "UTF-8";
    private static final String MASTERCARD_API_URL = "https://migs.mastercard.com.au/vpcpay?";

    private String generatedSecureHash;
    private String generatedURL;

    public MasterCardSecureHashGenerator(String secretKey, SortedMap<String, String> sortedParamMap, boolean generateURL) {
        initialize(secretKey, sortedParamMap, generateURL);
    }

    private void initialize(String secretKey, SortedMap<String, String> sortedParamMap, boolean generateURL){
        String valueToEncrypt = canonicalize(sortedParamMap);
        log.info("-------MASTERCARD_PARAMETERS_TO_HASH-------");
        log.info(valueToEncrypt);

        try {
            SecretKey signingKey = new SecretKeySpec(Hex.decodeHex(secretKey.toCharArray()), HMAC_SHA256);
            Mac mac = Mac.getInstance(HMAC_SHA256);
            mac.init(signingKey);
            byte[] digest = mac.doFinal(valueToEncrypt.getBytes(StandardCharsets.UTF_8));
            generatedSecureHash = toHexString(digest).toUpperCase();
            log.info("-------MASTERCARD_GENERATED_SECURE_HASH-------");
            log.info(generatedSecureHash);
        } catch (NoSuchAlgorithmException | DecoderException | InvalidKeyException e) {
            e.printStackTrace();
        }

        if (generateURL) {
            generateMasterCardURL(sortedParamMap);
        }
    }

    private void generateMasterCardURL(SortedMap<String, String> sortedParamMap) {
        sortedParamMap.put("user_amount", EncryptionHelper.encodeURL(sortedParamMap.get("user_amount")));
        sortedParamMap.put("user_cid", EncryptionHelper.encodeURL(sortedParamMap.get("user_cid")));
        sortedParamMap.put("user_key", EncryptionHelper.encodeURL(sortedParamMap.get("user_key")));
        sortedParamMap.put("user_type", EncryptionHelper.encodeURL(sortedParamMap.get("user_type")));

        if (sortedParamMap.get("user_url") != null && !sortedParamMap.get("user_url").isEmpty()) {
            sortedParamMap.put("user_url", EncryptionHelper.encodeURL(sortedParamMap.get("user_url")));
        }
        String urlParametersAsString = canonicalize(sortedParamMap);

        generatedURL = MASTERCARD_API_URL + urlParametersAsString + "&vpc_SecureHash=" + generatedSecureHash + "&vpc_SecureHashType=SHA256";

        log.info("-------MASTERCARD_PAYMENT_URL-------");
        log.info(generatedURL);
    }

    private static String toHexString(byte[] bytes) {
        Formatter formatter = new Formatter();

        for (byte b : bytes) {
            formatter.format("%02x", b);
        }

        return formatter.toString();
    }

    private static String canonicalize(SortedMap<String, String> sortedParamMap) {
        if (sortedParamMap.isEmpty()) {
            return "";
        }

        StringBuilder buffer = new StringBuilder();
        Iterator<Map.Entry<String, String>> iter = sortedParamMap.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry<String, String> kvpair = iter.next();
            buffer.append(kvpair.getKey());
            buffer.append("=");
            buffer.append(kvpair.getValue());
            if (iter.hasNext()) {
                buffer.append("&");
            }
        }
        return buffer.toString();
    }

    public String getGeneratedSecureHash() {
        return generatedSecureHash;
    }

    public String getGeneratedURL() {
        return generatedURL;
    }
}
