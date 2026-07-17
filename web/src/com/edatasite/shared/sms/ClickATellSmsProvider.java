package com.edatasite.shared.sms;

import com.edatasite.shared.components.EncryptionHelper;
import com.edatasite.workforce.core.domain.EdsSmsSettings;
import com.google.gwt.user.server.rpc.security.ServerSecurityContext;
import org.marre.SmsSender;
import org.marre.sms.SmsException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: hayot
 * Date: 12/11/12
 * Time: 2:32 PM
 * To change this template use File | Settings | File Templates.
 */
public class ClickATellSmsProvider extends SmsProvider {
    private SmsSender sender;
    private boolean isUSNumber;


    public ClickATellSmsProvider(EdsSmsSettings edsSmsSettings, Map<String, String> replacements) {
        super(edsSmsSettings, replacements);
    }

    @Override
    protected void connect() {
        if (smsSetting != null) {
            try {
                sender = SmsSender.getClickatellSender(smsSetting.getParametr(EdsSmsSettings.USERNAME), smsSetting.getParametr(EdsSmsSettings.PASSWORD), smsSetting.getParametr(EdsSmsSettings.API_ID));
                sender.connect();
            } catch (SmsException | IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean send() {
        //CmedLab url boshqa
        if ("35231".equals(ServerSecurityContext.getInstance().getCompanyId())) {
            return sendURLRequest();
        }
        if (sender != null) {
            boolean result = false;
            try {
                if (getReplacements().containsKey(EdsSmsSettings.SENDER_ID)) {
                    sender.sendTextSms(getReplacements().get(EdsSmsSettings.MESSAGE), getReplacements().get(EdsSmsSettings.PHONE_NUMBER), getReplacements().get(EdsSmsSettings.SENDER_ID));
                } else {
                    sender.sendTextSms(getReplacements().get(EdsSmsSettings.MESSAGE), getReplacements().get(EdsSmsSettings.PHONE_NUMBER));
                }
                result = true;
            } catch (Exception e) {
                checkUrlResult(e.getMessage());
            }
            return result;
        } else {
            return super.send();
        }
    }

    @Override
    protected boolean sendURLRequest() {
        if (getReplacements().get(EdsSmsSettings.PHONE_NUMBER).startsWith("1")) {
            isUSNumber = true;
        }
        if (getReplacements().containsKey(EdsSmsSettings.PASSWORD)) {
            getReplacements().put(EdsSmsSettings.PASSWORD, EncryptionHelper.encodeURL(getReplacements().get(EdsSmsSettings.PASSWORD)));
        }
        if (getReplacements().containsKey(EdsSmsSettings.USERNAME)) {
            getReplacements().put(EdsSmsSettings.USERNAME, EncryptionHelper.encodeURL(getReplacements().get(EdsSmsSettings.USERNAME)));
        }
        if (getReplacements().containsKey(EdsSmsSettings.API_ID)) {
            getReplacements().put(EdsSmsSettings.API_ID, EncryptionHelper.encodeURL(getReplacements().get(EdsSmsSettings.API_ID)));
        }
        if (getReplacements().containsKey(EdsSmsSettings.SENDER_ID)) {
            getReplacements().put(EdsSmsSettings.SENDER_ID, EncryptionHelper.encodeURL(getReplacements().get(EdsSmsSettings.SENDER_ID)));
        }
        if (getReplacements().containsKey(EdsSmsSettings.CLIENT_ID)) {
            getReplacements().put(EdsSmsSettings.CLIENT_ID, EncryptionHelper.encodeURL(getReplacements().get(EdsSmsSettings.CLIENT_ID)));
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
    protected String initSettings() {
        String url = isUSNumber ? EdsSmsSettings.URL_SEND_CLICKATELL_FOR_USA : smsSetting.getURL();
        if (replacements != null && !replacements.isEmpty()) {
            for (Map.Entry<String, String> entry : replacements.entrySet()) {
                if (entry.getKey() != null && entry.getValue() != null) {
                    url = url.replaceAll(entry.getKey(), entry.getValue());
                }
            }
        }
        if (smsSetting.getParametrMap() != null && !smsSetting.getParametrMap().isEmpty()) {
            for (Map.Entry<String, String> entry : smsSetting.getParametrMap().entrySet()) {
                if (entry.getKey() != null && entry.getValue() != null) {
                    url = url.replaceAll(entry.getKey(), entry.getValue());
                }
            }
        }
        return url;
    }

    @Override
    protected boolean checkUrlResult(String response) {
        if (response.contains(":")) {
            if ("ID".equals(response.split(":")[0].toUpperCase())) {
                return true;
            } else {
                String s = response.split(":")[1];
                s = s.substring((s.indexOf(",") + 1));
                setResponse(response);
                return false;
            }
        } else {
            setResponse(response);
            return false;
        }
    }
}
