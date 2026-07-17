package com.edatasite.workforce.gwt.core.server.servlets.pdf.invoice;

import com.edatasite.workforce.core.domain.accounting.EdsTaxillaSettings;
import com.edatasite.workforce.gwt.core.server.TaxillaSettingsManager;
import com.edatasite.workforce.gwt.core.server.db.TaxillaCredentialsManager;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class TaxillaPDFProvider {

    @Autowired
    private TaxillaSettingsManager taxillaSettingsManager;
    @Autowired
    private TaxillaCredentialsManager taxillaCredentialsManager;


    public String getQRFromTaxilla(String invoiceNumber, String invoiceType, String financialYear) {
        String accessToken = getAccessToken();
        if (accessToken == null || accessToken.isEmpty()) {
            return "";
        }

        String url = String.format(
                "https://sa.live.taxilla.com/process/v1/einvoicearksa/reports/KSA-eInvoice-Json-Ouput?financialyear=%s&ref_nm=%s&invoicetypecode=%s",
                URLEncoder.encode(financialYear, StandardCharsets.UTF_8),
                URLEncoder.encode(invoiceNumber, StandardCharsets.UTF_8),
                URLEncoder.encode(invoiceType, StandardCharsets.UTF_8)
        );

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Authorization", "Bearer " + accessToken)
                .GET()
                .build();

        String qrString = "";
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                JSONObject jsonResponse = new JSONObject(response.body());
                qrString = jsonResponse
                        .getJSONObject("Invoice")
                        .getJSONObject("AdditionalDocumentReference")
                        .getJSONObject("QR")
                        .getJSONObject("Attachment")
                        .getString("EmbeddedDocumentBinaryObject");
                System.out.println("JSON Response: " + qrString);
            } else {
                System.out.println("Request failed with status code: " + response.statusCode());
            }
        } catch (IOException | InterruptedException | JSONException e) {
            throw new RuntimeException(e);
        }

        return qrString;
    }

    private String getAccessToken() {
        EdsTaxillaSettings taxillaCredentialSettings = taxillaSettingsManager.getTaxillaSettings();
        if (taxillaCredentialSettings == null) {
            return null;
        }
        String response = "";
        try {
            URL url = new URL("https://sa.live.taxilla.com/oauth2/v1/token");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            connection.setDoOutput(true);

            try (OutputStream os = connection.getOutputStream()) {
                String parameters =
                        "grant_type=" + taxillaCredentialSettings.getGrandType() +
                                "&client_id=" + taxillaCredentialSettings.getClientID() +
                                "&client_secret=" + taxillaCredentialSettings.getClientSecret();

                os.write(parameters.getBytes());
                os.flush();
            }

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                try (Scanner scanner = new Scanner(connection.getInputStream())) {
                    response = scanner.useDelimiter("\\A").next();
                    if (response != null) {
                        JSONObject jsonObject = new JSONObject(response);
                        response = jsonObject.getString("access_token");
                    }
                }
                System.out.println("TAXILLA RESPONSE: " + response);
            } else {
                System.out.println("TAXILLA RESPONSE ERROR: " + responseCode);
            }
            connection.disconnect();
        } catch (Exception e) {
            System.out.println("TAXILLA RESPONSE ERROR: " + e.getMessage());
        }
        return response;
    }
}
