package com.edatasite.workforce.gwt.core.server.office365.utils;

import com.edatasite.workforce.gwt.core.server.office365.resources.base.Office365BaseResource;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.text.SimpleDateFormat;

/**
 * Created by umakarimov on 9/30/15.
 */
public class Office365Utils {
    private static ObjectMapper createMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        return mapper;
    }

    public static String toJSON(Office365BaseResource resource) {
        ObjectMapper mapper = Office365Utils.createMapper();
        mapper.setDateFormat(new SimpleDateFormat("dd MMM yyyy"));

        try {
            return mapper.writeValueAsString(resource);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static <T> T fromJSON(String json, Class<?> aClass) {
        if (json == null) {
            return null;
        }

        try {
            return (T) Office365Utils.createMapper().readValue(json, aClass);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static <T> T fromJSON(String json, TypeReference<?> type) {
        if (json == null) {
            return null;
        }

        try {
            return (T) Office365Utils.createMapper().readValue(json, type);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getHostUrl(HttpServletRequest request) {
        StringBuilder url = new StringBuilder();

        int httpPort = 80;
        int httpsPort = 443;
        int port = request.getServerPort();

        String serverName = request.getServerName();



        boolean isHttps = false/* ( port == httpsPort || "https".equalsIgnoreCase(request.getScheme()) )*/;

        if (request.getHeader("X-FORWARDED-PROTO") != null) {//in aws or locally, there is no Load Balancer, so this header is null
            isHttps = "https".equalsIgnoreCase(request.getHeader("X-FORWARDED-PROTO"));//in Load Balance for user's original protocol take from request.getHeader("X-FORWARDED-PROTO")
        } else {
            isHttps = "https".equals(request.getScheme());//in aws/local machines take from request.getScheme().equals("https")
        }

        if (isHttps) {
            url.append("https://");
        } else {
            url.append("http://");
        }

        url.append(serverName);

        if (port != httpPort && port != httpsPort) {
            url.append(":").append(port);
        }

        return url.toString();
    }

}
