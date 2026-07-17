package com.edatasite.shared.sms;

import com.edatasite.workforce.core.domain.EdsSmsSettings;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: hayot
 * Date: 12/11/12
 * Time: 4:05 PM
 * To change this template use File | Settings | File Templates.
 */
public class SunCellularSmsProvider extends SmsProvider {
    private String sessionID;

    public SunCellularSmsProvider(EdsSmsSettings edsSmsSettings, Map<String, String> replacements) {
        super(edsSmsSettings, replacements);
    }

    @Override
    protected void connect() {
        if (smsSetting != null) {
            String url = EdsSmsSettings.URL_LOGIN_SUNCELLULAR;
            url = url.replaceAll(EdsSmsSettings.USERNAME, smsSetting.getParametr(EdsSmsSettings.USERNAME));
            url = url.replaceAll(EdsSmsSettings.PASSWORD, smsSetting.getParametr(EdsSmsSettings.PASSWORD));
            try {
                URL u = new URL(url);
                System.out.println("LOGIN=>=>=>=>=>=>" + url);
                URLConnection uc = u.openConnection();
                uc.setDoOutput(true);
                uc.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

                BufferedReader in = new BufferedReader(new InputStreamReader(uc.getInputStream()));
                String res = in.readLine();
                in.close();
                if (res != null && !"".equals(res) && res.contains("20100")) {
                    sessionID = res.replaceAll("\\d*,.*,\\s*", "");
                }
            } catch (Exception exp) {
                log.error(exp.getMessage());
                sessionID = null;
            }
        }
    }

    @Override
    protected String initSettings() {
        String s = super.initSettings();
        if (s.contains("<sessionID>") && sessionID != null) {
            s = s.replace("<sessionID>", sessionID);
        }
        if (s.contains("&session=<sessionID>")) {
            s = s.replace("&session=<sessionID>", "");
        }
        if (s.contains("&from=<from>")) {
            s = s.replace("&from=<from>", "");
        }
        return s;
    }

    @Override
    protected boolean checkUrlResult(String res) {
        setResponse(res);
        System.out.println("-------------------------------------- " + res + " -----------------------------");
        return res != null && (res.contains("20300") || res.contains("20310") || res.contains("20320") || res.contains("20330"));
    }
}
