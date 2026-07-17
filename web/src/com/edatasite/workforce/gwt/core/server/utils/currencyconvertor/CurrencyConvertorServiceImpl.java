package com.edatasite.workforce.gwt.core.server.utils.currencyconvertor;

//import com.edatasite.workforce.gwt.core.server.utils.CurrencyConvertorService;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CurrencyConvertorServiceImpl {


    /**
     * exchange rate and lastUpDate date collection
     */
    private Map<String, CurrencyConvertorItem> fxRates = Collections.synchronizedMap(new HashMap<String, CurrencyConvertorItem>());

    /**
     * publishing date
     */
    private Date referenceDate = null;

    /**
     * internal error message
     */
    private String lastError = null;
    /**
     * Base currency
     */

    private String baseCurrency = "EUR";

    /**
     * Currency convertor url
     */
    private String currencyURL = "http://www.webservicex.net/CurrencyConvertor.asmx/ConversionRate";

    /**
     * Currency param
     */

    private String param;

    /**
     * Rate which has responsed from server
     */
    private Double rate;

    /**
     * Converts a double precision floating point value from one currency to
     * another. Example: convert(29.95, "USD", "EUR") - converts $29.95 US Dollars
     * to Euro.
     *
     * @param amount       Amount of money (in source currency) to be converted.
     * @param fromCurrency Three letter ISO 4217 currency code of source currency.
     * @param toCurrency   Three letter ISO 4217 currency code of target currency.
     * @return Amount in target currency
     * @throws java.io.IOException      If cache file cannot be read/written or if URL cannot be
     *                                  opened.
     * @throws java.text.ParseException If an error occurs while parsing the XML cache file.
     * @throws IllegalArgumentException If a wrong (non-existing) currency argument was supplied.
     */

    public double convert(double amount, String fromCurrency, String toCurrency)
            throws IOException, ParseException, IllegalArgumentException {
        double fromRate = getRate(fromCurrency);
        double toRate = getRate(toCurrency);
        amount *= toRate;
        amount /= fromRate;
        return amount;
    }

    /**
     * Method check currency for Map contains ans UpToDate
     * if not updated value of rate
     *
     * @param currency
     * @return double
     * @throws IOException
     * @throws ParseException
     * @throws IllegalArgumentException
     */
    private double getRate(String currency) throws IOException, ParseException, IllegalArgumentException {
        double rate;
        if (fxRates.containsKey(currency)) {
            if (checkUpToDate(currency)) {
                rate = fxRates.get(currency).getRate();
            } else {
                rate = update(currency);
            }
        } else {
            rate = update(currency);
        }
        return rate;
    }

    /**
     * @param currency
     * @return boolean
     * @throws ParseException
     * @throws IllegalArgumentException
     */
    private boolean checkUpToDate(String currency) throws ParseException, IllegalArgumentException {

        if (fxRates.get(currency).getLastUpdateTime() != null) {
            referenceDate = fxRates.get(currency).getLastUpdateTime();
        }

        return true;
    }

    /**
     * Method
     *
     * @param currency
     * @return
     * @throws IOException
     * @throws ParseException
     * @throws IllegalArgumentException
     */
    private double update(String currency) throws IOException, ParseException, IllegalArgumentException {

        try (CloseableHttpClient client = HttpClients.createDefault()) {
            List<NameValuePair> postParameters = new ArrayList<>();
            postParameters.add(new BasicNameValuePair("FromCurrency", baseCurrency));
            postParameters.add(new BasicNameValuePair("ToCurrency", currency));

            URIBuilder uriBuilder = new URIBuilder(currencyURL);
            uriBuilder.addParameters(postParameters);

            HttpPost postMethod = new HttpPost(uriBuilder.build());

            // Execute the method.
            HttpResponse httpResponse = client.execute(postMethod);
            int statusCode = httpResponse.getStatusLine().getStatusCode();

            if (statusCode != HttpStatus.SC_OK) {
                System.err.println("Method failed: " + httpResponse.getStatusLine());
            }
            // Read the response body.
            String responseBody = EntityUtils.toString(httpResponse.getEntity());
        } catch (URISyntaxException e) {
            System.err.println("Fatal transport error: " + e.getMessage());
            e.printStackTrace();
        }

        return 1;
    }

    private void connect(HttpClient client, HttpUriRequest method) {
        try {
            // Execute the method.
            HttpResponse httpResponse = client.execute(method);
            int statusCode = httpResponse.getStatusLine().getStatusCode();

            if (statusCode != HttpStatus.SC_OK) {
                System.err.println("Method failed: " + httpResponse.getStatusLine());
            }
            // Read the response body.


        } catch (IOException e) {
            System.err.println("Fatal transport error: " + e.getMessage());
            e.printStackTrace();
        } finally {
            // Release the connection.

        }

    }

    /**
     * Returns exchange rate for given currency.
     * Its simply uses function convert() for amount equal 1.
     *
     * @param fromCurrency Three letter ISO 4217 currency code of source currency.
     * @param toCurrency   Three letter ISO 4217 currency code of target currency.
     * @return Amount in target currency
     * @throws IOException              If cache file cannot be read/written or if URL cannot be
     *                                  opened.
     * @throws ParseException           If an error occurs while parsing the XML cache file.
     * @throws IllegalArgumentException If a wrong (non-existing) currency argument was supplied.
     */
    public double getExchangeRateDouble(String fromCurrency, String toCurrency)
            throws IOException, ParseException, IllegalArgumentException {
        return convert(1d, fromCurrency, toCurrency);
    }

    public long convert(long amount, String fromCurrency, String toCurrency) throws IOException, ParseException, IllegalArgumentException {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public String[] getCurrencies() throws IOException, ParseException {
        return new String[0];  //To change body of implemented methods use File | Settings | File Templates.
    }
}
