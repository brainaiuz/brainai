package com.edatasite.workforce.gwt.core.server.utils;

import com.edatasite.workforce.gwt.core.client.rpc.currency.CurrencyLayerItem;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Shohruh on 2/15/2016.
 */
public class CurrencyLayer {
    /**
     * Essential URL structure is built using constants
     */
    private static final String ACCESS_KEY = "80b36a16e099eb6de0a554474d7f0d62";
    private static final String BASE_URL = "https://apilayer.net/api/";
    private static final String ENDPOINT = "convert";
    private static final String dateTimeFormat = "yyyy-MM-dd";

    /**
     * This object is used for executing requests to the (REST) API
     */
    private static CloseableHttpClient httpClient;

    /**
     * singleton instance
     */
    private static CurrencyLayer instance = null;

    private CurrencyLayer() {
    }

    /**
     * Returns a singleton instance of CurrencyLayer.
     *
     * @return CurrencyConverter instance
     */
    public static CurrencyLayer getInstance() {
        if (instance == null) {
            instance = new CurrencyLayer();
        }
        return instance;
    }

    /**
     * Returns exchange rate for given currency.
     * Its simply uses function convert() for amount equal 1.
     */
    public CurrencyLayerItem convert(double amount, String fromCurrency, String toCurrency, Date date) throws IOException, JSONException {
        DateFormat dateFormat = new SimpleDateFormat(dateTimeFormat);
        HttpGet get = new HttpGet(BASE_URL + ENDPOINT + "?access_key=" + ACCESS_KEY + "&from=" + fromCurrency + "&to=" + toCurrency + "&amount=" + amount + "&date=" + dateFormat.format(date));
        httpClient = HttpClients.createDefault();
        CloseableHttpResponse response = httpClient.execute(get);
        HttpEntity entity = response.getEntity();

        // the following line converts the JSON Response to an equivalent Java Object
        JSONObject jsonObject = new JSONObject(EntityUtils.toString(entity));
        Double rate = jsonObject.getDouble("result");
        Date lastUpdateTime = new Date((jsonObject.getJSONObject("info").getLong("timestamp")*1000));

        response.close();
        httpClient.close();
        return new CurrencyLayerItem(rate, lastUpdateTime);
    }

    /**
     * Returns exchange rate for given currency.
     * Its simply uses function convert() for amount equal 1.
     */
    public CurrencyLayerItem getExchangeRateDouble(String fromCurrency, String toCurrency, Date date) throws IOException, JSONException {
        return convert(1d, fromCurrency, toCurrency, date);
    }
}
