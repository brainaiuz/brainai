package com.edatasite.workforce.gwt.core.server.utils;

import com.edatasite.workforce.rest.base.exception.TimeOutException;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.SocketTimeoutException;
import java.text.ParseException;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: Apr 11, 2011
 * Time: 6:07:36 PM
 * To change this template use File | Settings | File Templates.
 */
public class NewCurrencyConverter {

    private static Logger log = LoggerFactory.getLogger(NewCurrencyConverter.class);
//    public static final String EX_RATE_URL = "http://www.webservicex.net/CurrencyConvertor.asmx/ConversionRate?FromCurrency=GBP&ToCurrency=USD";

    private static NewCurrencyConverter instance;

    public static NewCurrencyConverter getInstance() {
        if (instance == null)
            instance = new NewCurrencyConverter();
        return instance;
    }

    public double convert(double amount, String fromCurrency, String toCurrency) {
        try {
            Double exRate = readExchangeRateFromWeb("http://www.webservicex.net/CurrencyConvertor.asmx/ConversionRate?FromCurrency="
                    + fromCurrency + "&ToCurrency=" + toCurrency);
            amount *= exRate;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return amount;
    }

    public double getExchangeRateDouble(String fromCurrency, String toCurrency)
            throws IOException, ParseException, IllegalArgumentException {
        return convert(1d, fromCurrency, toCurrency);
    }

    private Double readExchangeRateFromWeb(String exRateUrl) throws Exception {
        HttpEntity entity = null;
        RequestConfig defaultRequestConfig = RequestConfig.custom()
                .setSocketTimeout(5000)
                .build();
        try (CloseableHttpClient httpclient = HttpClients
                .custom()
                .setDefaultRequestConfig(defaultRequestConfig)
                .build()) {

            HttpGet httpget = new HttpGet(exRateUrl);

            HttpResponse response = httpclient.execute(httpget);
            entity = response.getEntity();
            System.out.println("Got response from www.webservicex.net");
        } catch (SocketTimeoutException e) {
            log.info(e.getMessage());
            throw new TimeOutException("Service is not available");
        }
        return extractExchangeRateValue(entity.getContent());

    }

    private Double extractExchangeRateValue(InputStream is) throws Exception {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();

        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        String exRateStr = stripTags(sb.toString());
        if (exRateStr != null)
            return Double.parseDouble(exRateStr.trim());
        else {
            System.err.println("Cant extract exchange rate");
            throw new ParseException("Cant extract exchange rate", 0);
        }
    }

    private String stripTags(String inputStr) {
        return inputStr.replaceAll("<.*?>", "").replace("\r\n", "").trim();
    }
}
