package com.edatasite.shared.sms;

import com.edatasite.shared.components.EncryptionHelper;
import com.edatasite.workforce.core.domain.EdsSmsSettings;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

/**
 * Created by Azazello on 2/6/16.
 */
public class EzyTextProSmsProvider extends SmsProvider {

    public EzyTextProSmsProvider(EdsSmsSettings edsSmsSettings, Map<String, String> replacements) {
        super(edsSmsSettings, replacements);
    }

    @Override
    protected boolean sendURLRequest() {
        if (getReplacements().containsKey(EdsSmsSettings.API_KEY)) {
            getReplacements().put(EdsSmsSettings.API_KEY, EncryptionHelper.encodeURL(getReplacements().get(EdsSmsSettings.API_KEY)));
        }
        if (getReplacements().containsKey(EdsSmsSettings.CAMPAIGN_ID)) {
            getReplacements().put(EdsSmsSettings.CAMPAIGN_ID, EncryptionHelper.encodeURL(getReplacements().get(EdsSmsSettings.CAMPAIGN_ID)));
        }
        if (getReplacements().containsKey(EdsSmsSettings.ROUTE_ID)) {
            getReplacements().put(EdsSmsSettings.ROUTE_ID, EncryptionHelper.encodeURL(getReplacements().get(EdsSmsSettings.ROUTE_ID)));
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
        try {
            URL u = new URL(url);
            System.out.println("=>=>=>=>=>=>=>=>=>" + url);
            HttpURLConnection uc = (HttpURLConnection) u.openConnection();
            uc.setDoOutput(true);
            uc.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            BufferedReader in = new BufferedReader(new InputStreamReader(uc.getInputStream()));
            String res = in.readLine();
            in.close();
            return checkUrlResult(res);
        } catch (Exception exp) {
            log.error(exp.getMessage());
            return false;
        }
    }

    @Override
    protected boolean checkUrlResult(String res) {
        setResponse(res);
        System.out.println("-------------------------------------- " + res + " -----------------------------");
        return res != null;
    }
}
