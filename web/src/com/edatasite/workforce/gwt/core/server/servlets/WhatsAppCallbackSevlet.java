package com.edatasite.workforce.gwt.core.server.servlets;

import com.edatasite.workforce.core.domain.EdsMessengersIntegration;
import com.edatasite.workforce.gwt.core.server.app.social.whatsapp.dto.response.AccessToken;
import com.edatasite.workforce.gwt.core.server.db.MessengersIntegrationManager;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jooq.tools.json.JSONArray;
import org.jooq.tools.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.HttpRequestHandler;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WhatsAppCallbackSevlet  implements HttpRequestHandler {
    @Autowired
    private MessengersIntegrationManager messengersIntegrationManager;

    @Transactional
    @Override
    public void handleRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Extract authorization code from the request
        String code = request.getParameter("code");

        if (code == null || code.isEmpty()) {
            // Handle error: No authorization code available
            return;
        }

        // Facebook API credentials
        String clientId = "544686667080872";
        String clientSecret = "1f3f1eeeec3e79ecc4c05cc672639b37";
        String fullHost = (String) request.getAttribute("fullHost");
        String redirectUrl = fullHost + "common/whatsAppCallbackServlet";

        // Exchange the authorization code for an access token
        AccessToken accessToken = exchangeCodeForAccessToken(code, clientId, clientSecret, redirectUrl);
        if (accessToken == null) {
            // Handle error: No access token retrieved
            return;
        }

        // Get WABA ID using the access token
        String wabaId = fetchWabaId(accessToken.accessToken());
        if (wabaId == null) {
            // Handle error: No WABA ID retrieved
            return;
        }

        subscribeAppToWaba(wabaId,accessToken.accessToken());

        String phoneNumberId = getPhoneNumberId(wabaId, accessToken.accessToken());

        // Business logic to update or create your entity with the obtained token and WABA ID
        updateEntityWithWhatsAppData(accessToken.accessToken(), wabaId,phoneNumberId);

        registerWebhook(accessToken.accessToken(),wabaId);

        // Send success response to close the window
        sendSuccessResponse(response);


    }

    private AccessToken exchangeCodeForAccessToken(String code, String clientId, String clientSecret, String redirectUrl) {
        String tokenEndpoint = "https://graph.facebook.com/v18.0/oauth/access_token";
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("code", code);
        params.add("client_id", clientId);
        params.add("client_secret", clientSecret);
        params.add("redirect_uri", redirectUrl);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);

        try {
            ResponseEntity<AccessToken> response = restTemplate.exchange(tokenEndpoint, HttpMethod.POST, request, AccessToken.class);
            return response.getBody();
        } catch (Exception e) {
            // Log exception
            return null;
        }
    }

    private String fetchWabaId(String accessToken) {
        String wabaEndpoint = "https://graph.facebook.com/v18.0/401186610439051/client_whatsapp_business_accounts";
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);

        try {
            ResponseEntity<Map> response = restTemplate.exchange(wabaEndpoint, HttpMethod.GET, new HttpEntity<>(headers), Map.class);

            // Extract the ID from the response
            List<Map<String, Object>> data = (List<Map<String, Object>>) response.getBody().get("data");
            if (data != null && !data.isEmpty()) {
                Map<String, Object> firstAccount = data.get(0);
                return (String) firstAccount.get("id");
            } else {
                // Handle the case where no data is returned or is empty
                return null;
            }
        } catch (Exception e) {
            // Log exception
            return null;
        }
    }

    private boolean subscribeAppToWaba(String wabaId, String accessToken) {
        String subscriptionEndpoint = "https://graph.facebook.com/v18.0/" + wabaId + "/subscribed_apps";
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + accessToken);

        // Building the request body
        JSONObject requestBody = new JSONObject();
        requestBody.put("subscribed_fields", new JSONArray(Arrays.asList("messages", "messaging_postbacks"))); // the events you are interested in

        HttpEntity<String> request = new HttpEntity<>(requestBody.toString(), headers);

        try {
            ResponseEntity<Map> response = restTemplate.postForEntity(subscriptionEndpoint, request, Map.class);

            // Check if the subscription was successful
            return response.getStatusCode() == HttpStatus.OK;
        } catch (HttpClientErrorException e) {
            e.printStackTrace();
            // Here you can log the response body which will give you more insight into the error
            System.out.println(e.getResponseBodyAsString());
            return false;
        }
    }

    private String getPhoneNumberId(String wabaId, String pageAccessToken) {
        String phoneNumberEndpoint = "https://graph.facebook.com/v18.0/" + wabaId + "/phone_numbers";
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + pageAccessToken);

        try {
            ResponseEntity<Map> response = restTemplate.exchange(phoneNumberEndpoint, HttpMethod.GET, new HttpEntity<>(headers), Map.class);

            // Extract the phone number ID from the response
            List<Map<String, Object>> data = (List<Map<String, Object>>) response.getBody().get("data");
            if (data != null && !data.isEmpty()) {
                Map<String, Object> phoneNumberInfo = data.get(0); // Assuming you want the first phone number, adjust as needed
                String phoneNumberId = (String) phoneNumberInfo.get("id");

                // Now you can use 'phoneNumberId' for your further operations
                return phoneNumberId;
            } else {
                // Handle the case where no phone numbers are returned or the data is empty
                return null;
            }
        } catch (Exception e) {
            // Log exception
            return null;
        }
    }


    private void updateEntityWithWhatsAppData(String accessToken, String wabaId,String phoneNumberId) {
        EdsMessengersIntegration companyCredentials = messengersIntegrationManager.getCompanyCredentials();
        EdsMessengersIntegration messengersIntegration =  companyCredentials != null ? companyCredentials : new EdsMessengersIntegration();
        messengersIntegration.setWhatsappToken(accessToken);
        messengersIntegration.setWhatsappBussinnessId(wabaId);
        messengersIntegration.setPhoneNumberId(phoneNumberId);
        messengersIntegrationManager.createOrUpdate(messengersIntegration);

    }

        private void sendSuccessResponse(HttpServletResponse response) throws IOException {
            String successResponse = "<script>" +
                    "setTimeout(function() { window.open('', '_self', ''); window.close(); }, 1000);" +
                    "</script></body></html>";

            response.setContentType("text/html");
            response.getWriter().write(successResponse);
        }


    private boolean registerWebhook(String accessToken, String wabaId) throws JsonProcessingException {
        String webhookEndpoint = "https://graph.facebook.com/v18.0/" + wabaId + "/subscribed_apps";
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(accessToken);  // Set bearer token

        // Use a publicly accessible callback URL
        String yourWebhookUrl = "https://apps.kpi.com/services/api/v2/whatsapp/updates/65159";

        // Build the request body using Jackson
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> requestBodyMap = new HashMap<>();
        requestBodyMap.put("override_callback_uri", yourWebhookUrl);
        requestBodyMap.put("verify_token", "token_2001");

        String requestBodyString = mapper.writeValueAsString(requestBodyMap);
        HttpEntity<String> request = new HttpEntity<>(requestBodyString, headers);

        try {
            ResponseEntity<Map> response = restTemplate.postForEntity(webhookEndpoint, request, Map.class);
            // Check if the subscription was successful
            return response.getStatusCode() == HttpStatus.OK;
        } catch (HttpClientErrorException e) {
            e.printStackTrace();
            // Log the response body for more insight into the error
            System.out.println("Error Body: " + e.getResponseBodyAsString());
            return false;
        }
    }

}
