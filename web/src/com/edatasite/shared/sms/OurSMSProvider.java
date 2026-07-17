package com.edatasite.shared.sms;

import com.edatasite.shared.components.EncryptionHelper;
import com.edatasite.workforce.core.domain.EdsSmsSettings;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

/**
 * Created by Azazello on 3/30/16.
 */
public class OurSMSProvider extends SmsProvider {

    public OurSMSProvider(EdsSmsSettings edsSmsSettings, Map<String, String> replacements) {
        super(edsSmsSettings, replacements);
    }

    @Override
    protected boolean sendURLRequest() {
        if (getReplacements().containsKey(EdsSmsSettings.USERNAME)) {
            getReplacements().put(EdsSmsSettings.USERNAME, EncryptionHelper.encodeURL(getReplacements().get(EdsSmsSettings.USERNAME)));
        }
        if (getReplacements().containsKey(EdsSmsSettings.PASSWORD)) {
            getReplacements().put(EdsSmsSettings.PASSWORD, EncryptionHelper.encodeURL(getReplacements().get(EdsSmsSettings.PASSWORD)));
        }
        if (getReplacements().containsKey(EdsSmsSettings.SENDER_ID)) {
            getReplacements().put(EdsSmsSettings.SENDER_ID, EncryptionHelper.encodeURL(getReplacements().get(EdsSmsSettings.SENDER_ID)));
        }
        if (getReplacements().containsKey(EdsSmsSettings.PHONE_NUMBER)) {
            getReplacements().put(EdsSmsSettings.PHONE_NUMBER, EncryptionHelper.encodeURL(getReplacements().get(EdsSmsSettings.PHONE_NUMBER)));
        }
        if (getReplacements().containsKey(EdsSmsSettings.MESSAGE)) {
            getReplacements().put(EdsSmsSettings.MESSAGE, EncryptionHelper.encodeURL(getReplacements().get(EdsSmsSettings.MESSAGE)));
        }
        String url = initSettings();
        HttpURLConnection uc = null;
        try {
            URL u = new URL(url);
            System.out.println("=>=>=>=>=>=>=>=>=>" + url);
            uc = (HttpURLConnection) u.openConnection();
            uc.setDoOutput(true);
            uc.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            BufferedReader in = new BufferedReader(new InputStreamReader(uc.getInputStream()));
            String res = in.readLine();
            in.close();
            return checkUrlResult(res);
        } catch (Exception exp) {
            log.error(exp.getMessage());
            return false;
        } finally {
            if (uc != null) {
                uc.disconnect();
            }
        }
    }

    @Override
    protected boolean checkUrlResult(String res) {
        setResponse(res);
        System.out.println("-------------------------------------- " + res + " -----------------------------");
        return res != null;
    }
}
