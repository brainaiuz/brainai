package com.edatasite.shared.sms;


import com.edatasite.workforce.core.domain.EdsSmsSettings;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.jooq.tools.json.JSONObject;

import java.util.Map;

public class EskizSMSProvider extends SmsProvider {

    public EskizSMSProvider(EdsSmsSettings edsSmsSettings, Map<String, String> replacements) {
        super(edsSmsSettings, replacements);
    }

    @Override
    protected boolean sendURLRequest() {
        String basic = getEskizToken();
        String number = null;
        String message = null;
        if (getReplacements().containsKey(EdsSmsSettings.PHONE_NUMBER)) {
            number = getReplacements().get(EdsSmsSettings.PHONE_NUMBER);
        }
        if (getReplacements().containsKey(EdsSmsSettings.MESSAGE)) {
            message = getReplacements().get(EdsSmsSettings.MESSAGE);
        }

        if (!StringUtils.isEmpty(basic)) {

            JSONObject request = new JSONObject();
            request.put("mobile_phone", number.startsWith("+") ? number.substring(1) : number);
            request.put("message", message);
            request.put("from", smsSetting.getParametr(EdsSmsSettings.FROM));

            String url = "http://notify.eskiz.uz/api/message/sms/send";

            try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
                HttpPost post = new HttpPost(url);
                post.addHeader("Content-type", "application/json");
                post.addHeader("Authorization", "Bearer " + basic);
                post.setEntity(new StringEntity(request.toString(), "UTF-8"));

                CloseableHttpResponse response = httpClient.execute(post);
                HttpEntity entity = response.getEntity();

                org.json.JSONObject jsonObject = new org.json.JSONObject(EntityUtils.toString(entity));
                if (jsonObject != null && jsonObject.get("status") != null && "error".equals(jsonObject.get("status"))) {
                    return false;
                } else if (jsonObject != null && jsonObject.get("id") != null) { //TODO return Sms Status
                    return checkUrlResult(message);
                } else {
                    return false;
                }
            } catch (Exception exp) {
                log.error(exp.getMessage());
                return false;
            }

        }
        return false;
    }

    private String getEskizToken() {

        JSONObject request = new JSONObject();
        request.put("email", smsSetting.getParametr(EdsSmsSettings.EMAIL));
        request.put("password", smsSetting.getParametr(EdsSmsSettings.PASSWORD));

        String url = "http://notify.eskiz.uz/api/auth/login";

        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpPost post = new HttpPost(url);
            post.addHeader("Content-type", "application/json");
            post.setEntity(new StringEntity(request.toString(), "UTF-8"));

            CloseableHttpResponse response = httpClient.execute(post);
            HttpEntity entity = response.getEntity();

            org.json.JSONObject jsonObject = new org.json.JSONObject(EntityUtils.toString(entity));
            if (jsonObject != null && jsonObject.getJSONObject("data") != null && jsonObject.getJSONObject("data").get("token") != null) {
                return jsonObject.getJSONObject("data").get("token").toString();
            } else {
                return null;
            }
        } catch (Exception exp) {
            log.error(exp.getMessage());
            return null;
        }
    }

    @Override
    protected boolean checkUrlResult(String res) {
        setResponse(res);
        System.out.println("-------------------------------------- " + res + " -----------------------------");
        return res != null;
    }
}
