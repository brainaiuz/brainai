package com.edatasite.workforce.gwt.core.server.commons;

import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.utils.EdsContextParams;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;

import javax.net.ssl.SSLContext;
import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

/**
 * Created by Normurod on 6/22/2016.
 */
public class PayPalUtils implements Constants {

    public static String getPayPalLink() {
        if (EdsContextParams.isLiveEnvironment()) {
            return paypal_LINK_Live;
        } else {
            return paypal_LINK_Test;
        }
    }

    protected String getPayPalIpnVerification(HttpServletRequest request) throws IOException {
        SSLContext sslContext = null;
        try {
            sslContext = org.apache.http.conn.ssl.SSLContexts.custom()
                    .useTLS()
                    .build();
        } catch (NoSuchAlgorithmException | KeyManagementException e) {
            e.printStackTrace();
        }

        SSLConnectionSocketFactory f = new SSLConnectionSocketFactory(
                sslContext,
                new String[]{"TLSv1.2"},
                null,
                null);

        try (CloseableHttpClient httpClient = HttpClients.custom()
                .setSSLSocketFactory(f)
                .build()) {

            HttpPost post = new HttpPost("https://" + getPayPalLink());
            List<NameValuePair> params = new ArrayList<>();
            params.add(new BasicNameValuePair("cmd", "_notify-validate")); //You need to add this parameter to tell PayPal to verify
            for (Enumeration<String> e = request.getParameterNames(); e.hasMoreElements(); ) {
                String name = e.nextElement();
                String value = request.getParameter(name);
                params.add(new BasicNameValuePair(name, value));
            }
            post.setEntity(new UrlEncodedFormEntity(params));

            return getRC(httpClient.execute(post)).trim();
        }
    }

    protected String getRC(HttpResponse response) throws IOException, IllegalStateException {
        InputStream is = response.getEntity().getContent();
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        StringBuilder result = new StringBuilder();
        String line = null;
        while ((line = br.readLine()) != null) {
            result.append(line);
        }
        return result.toString();
    }
}
