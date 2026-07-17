package com.edatasite.workforce.gwt.core.server.servlets.pdf.qrcode;


import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

public class QRCodeGenerator {
    private static String BASE_URL = "https://api.qrserver.com/v1/create-qr-code/?";

    public static String generate(String data, int width, int height){
        Map<String, String> params = new HashMap<>();
        params.put("size", width + "x" + height);
        params.put("data", data);
        try {
            return BASE_URL + getParamsString(params);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getParamsString(Map<String, String> params)
            throws UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();

        for (Map.Entry<String, String> entry : params.entrySet()) {
            result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
            result.append("&");
        }

        String resultString = result.toString();
        return resultString.length() > 0
                ? resultString.substring(0, resultString.length() - 1)
                : resultString;
    }
}
