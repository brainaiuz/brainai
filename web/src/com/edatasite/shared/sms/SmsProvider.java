package com.edatasite.shared.sms;

import com.edatasite.shared.components.EncryptionHelper;
import com.edatasite.workforce.core.domain.EdsSmsSettings;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: hayot
 * Date: 8/23/12
 * Time: 5:33 PM
 * To change this template use File | Settings | File Templates.
 */
public abstract class SmsProvider {
    public static Logger log = LoggerFactory.getLogger(SmsProvider.class);

    protected EdsSmsSettings smsSetting;
    protected Map<String, String> replacements;
    private String response;

    protected SmsProvider(EdsSmsSettings smsSetting, Map<String, String> replacements) {
        this.smsSetting = smsSetting;
        this.replacements = replacements;
        connect();
    }

    protected void connect() {
        //...
    }

    protected SmsProvider() {
        //To change body of created methods use File | Settings | File Templates.
    }

    public boolean send(String message, String to) {
        if (replacements == null) {
            replacements = new HashMap<>();
        }
        replacements.put(EdsSmsSettings.PHONE_NUMBER, to);
        replacements.put(EdsSmsSettings.MESSAGE, message);
        return send();
    }

    protected Map<String, String> getReplacements() {
        replacements = replacements == null ? new HashMap<>() : replacements;
        return replacements;
    }

    public boolean send() {
        if (smsSetting != null) {
            return sendURLRequest();
        }
        return false;
    }

    /**
     * created By:Hayot
     * this method uses XML sending pattern to send sms
     *
     * @return
     */
    protected boolean sendXMLRequest() {
        Socket socket = null;
        try {
            socket = new Socket(smsSetting.getParametr(EdsSmsSettings.SERVER), 80);
        } catch (Exception ex1) {
            log.error("Error: " + ex1.getMessage());
            return false;
        }
        try {
            OutputStream os = socket.getOutputStream();
            boolean autoflush = true;
            PrintWriter out = new PrintWriter(socket.getOutputStream(), autoflush);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String xml = initSettings();
            String xml_part = xml.substring(0, xml.lastIndexOf("/>"));
            xml = xml.replace(EdsSmsSettings.CONTENT_LENGTH, xml_part.length() + "");
            out.println(xml);
            out.println();

            String inputLine;
            StringBuilder sb = new StringBuilder(1000);

            int wait_seconds = 60;
            boolean timeout = false;
            long m = System.currentTimeMillis();
            while ((inputLine = in.readLine()) != null && !timeout) {
                sb.append(inputLine + "\n");
                if ((System.currentTimeMillis() - m) > (1000 * wait_seconds)) {
                    timeout = true;
                }
            }
            in.close();
            System.out.println(sb.toString());
            String response = sb.toString();
            socket.close();
            return checkXMLResult(response);
        } catch (Exception ex) {
            log.error(("Error: cannot communicate."));
        }
        return false;
    }

    protected boolean checkXMLResult(String response) {
        if (response != null && !"".equals(response)) {
            if (response.contains(smsSetting.getParametr(EdsSmsSettings.RESPONSE_SUCCESS))) {
                log.info("Sms Sent to ");
                return true;
            } else {
                log.error(response);
                return false;
            }
        }
        return false;
    }

    protected String initSettings() {
        String url = smsSetting.getURL();
        if (replacements != null && replacements.size() > 0) {
            for (Map.Entry<String, String> entry : replacements.entrySet()) {
                if (entry.getKey() != null && entry.getValue() != null) {
                    url = url.replaceAll(entry.getKey(), entry.getValue());
                }
            }
        }
        if (smsSetting.getParametrMap() != null && smsSetting.getParametrMap().size() > 0) {
            for (Map.Entry<String, String> entry : smsSetting.getParametrMap().entrySet()) {
                if (entry.getKey() != null && entry.getValue() != null) {
                    url = url.replaceAll(entry.getKey(), entry.getValue());
                }
            }
        }
        return url;
    }

    protected boolean sendURLRequest() {
        if (getReplacements().containsKey(EdsSmsSettings.MESSAGE)) {
            getReplacements().put(EdsSmsSettings.MESSAGE, EncryptionHelper.encodeURL(getReplacements().get(EdsSmsSettings.MESSAGE)));
        }
        String url = initSettings();
        try {
            URL u = new URL(url);
            System.out.println("=>=>=>=>=>=>=>=>=>" + url);
            URLConnection uc = u.openConnection();
            uc.setDoOutput(true);
            uc.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            BufferedReader in = new BufferedReader(new InputStreamReader(uc.getInputStream()));
            String res = in.readLine();
            in.close();
            return checkUrlResult(res);
        } catch (Exception e) {
            log.error(e.getMessage());
            return false;
        }
    }

    protected boolean checkUrlResult(String res) {
        return false;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }
}
