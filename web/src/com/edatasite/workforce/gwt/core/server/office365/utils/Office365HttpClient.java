package com.edatasite.workforce.gwt.core.server.office365.utils;

import com.edatasite.workforce.gwt.core.server.office365.resources.Office365AccessTokenDTO;
import com.edatasite.workforce.gwt.core.server.office365.resources.Office365HttpResponse;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPatch;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by umakarimov on 9/30/15.
 */
public class Office365HttpClient {
    private static Logger log = LoggerFactory.getLogger(Office365HttpClient.class);

    public static Office365HttpResponse doFormPost(String url, HashMap<String, String> body) {
        HttpPost method = new HttpPost(url);


        List<NameValuePair> params = new ArrayList<>();
        for (Map.Entry<String, String> entry : body.entrySet()) {
            params.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
        }
        URI uri = null;
        try {
            uri = new URIBuilder(method.getURI())
                    .addParameters(params)
                    .build();
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        method.setURI(uri);

        return Office365HttpClient.doRequest(method, null);
    }

    public static Office365HttpResponse doGet(String url, List<NameValuePair> params, Office365AccessTokenDTO token) {
        HttpGet method = new HttpGet(url);

        if (params != null) {
            URI uri = null;
            try {
                uri = new URIBuilder(method.getURI())
                        .addParameters(params)
                        .build();
            } catch (URISyntaxException e) {
                throw new RuntimeException(e);
            }
            method.setURI(uri);
        }

        return Office365HttpClient.doRequest(method, token);
    }

    public static Office365HttpResponse doPost(String url, JSONObject data, Office365AccessTokenDTO token) {
        HttpPost method = new HttpPost(url);

        return Office365HttpClient.doRequestWithBody(method, data, token);
    }

    public static Office365HttpResponse doPut(String url, JSONObject data, Office365AccessTokenDTO token) {
        HttpPut method = new HttpPut(url);

        return Office365HttpClient.doRequestWithBody(method, data, token);
    }

    public static Office365HttpResponse doPatch(String url, JSONObject data, Office365AccessTokenDTO token) {
        HttpPatch method = new HttpPatch(url);

        return Office365HttpClient.doRequestWithBody(method, data, token);
    }

    private static Office365HttpResponse doRequestWithBody(HttpEntityEnclosingRequestBase method, JSONObject data, Office365AccessTokenDTO token) {
        if (data != null) {
            StringEntity requestEntity = null;

            try {
                requestEntity = new StringEntity(data.toJSONString(), "application/json", "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            method.setEntity(requestEntity);
        }

        return Office365HttpClient.doRequest(method, token);
    }

    public static Office365HttpResponse doDelete(String url, List<NameValuePair> params, Office365AccessTokenDTO token) {
        HttpDelete method = new HttpDelete(url);

        if (params != null) {
            URI uri = null;
            try {
                uri = new URIBuilder(method.getURI())
                        .addParameters(params)
                        .build();
            } catch (URISyntaxException e) {
                throw new RuntimeException(e);
            }
            method.setURI(uri);
        }

        return Office365HttpClient.doRequest(method, token);
    }

    public static Office365HttpResponse doRequest(HttpRequestBase method, Office365AccessTokenDTO token) {
        String responseString = null;
        int statusCode = 0;
        try (CloseableHttpClient client = HttpClients.createDefault()) {

            log.trace(String.format("Do request %s", method.getURI()));

            if (token != null) {
                method.addHeader("Authorization", "Bearer " + token.getAccessToken());
            }
            method.addHeader("Accept", "application/json");

            HttpResponse httpResponse = client.execute(method);
            responseString = new String(httpResponse.getEntity().getContent().readAllBytes(), StandardCharsets.UTF_8);
            statusCode = httpResponse.getStatusLine().getStatusCode();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            method.releaseConnection();
        }

        return new Office365HttpResponse(statusCode, responseString);
    }
}
