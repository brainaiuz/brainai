package com.edatasite.shared.sms;

import com.edatasite.shared.sms.playmobile.PlayMobileRequestDto;
import com.edatasite.shared.sms.playmobile.SmsContent;
import com.edatasite.shared.sms.playmobile.SmsContext;
import com.edatasite.shared.sms.playmobile.SmsMessage;
import com.edatasite.workforce.core.domain.EdsSmsSettings;
import com.google.api.client.repackaged.org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.RandomStringUtils;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.Map;

import static com.edatasite.workforce.core.domain.EdsSmsSettings.BASE_URL;
import static com.edatasite.workforce.core.domain.EdsSmsSettings.FROM;
import static com.edatasite.workforce.core.domain.EdsSmsSettings.MESSAGE;
import static com.edatasite.workforce.core.domain.EdsSmsSettings.PASSWORD;
import static com.edatasite.workforce.core.domain.EdsSmsSettings.PHONE_NUMBER;
import static com.edatasite.workforce.core.domain.EdsSmsSettings.USERNAME;

public class PlayMobileSmsProvider extends SmsProvider {

    private RestTemplate restTemplate = new RestTemplate();

    public PlayMobileSmsProvider(EdsSmsSettings edsSmsSettings, Map<String, String> replacements) {
        super(edsSmsSettings, replacements);
    }

    @Override
    protected boolean sendURLRequest() {
        String messageId = "kpi" + DateTimeFormatter.ofPattern("yyMMdd").format(ZonedDateTime.now()) + RandomStringUtils.random(11, true, true);
        PlayMobileRequestDto request = new PlayMobileRequestDto(Collections.singletonList(new SmsMessage(getReplacements().get(PHONE_NUMBER).replace("\\+", ""), messageId, new SmsContext(smsSetting.getParametr(FROM), new SmsContent(getReplacements().get(MESSAGE))))));
        try {
            ResponseEntity<String> response = restTemplate.postForEntity(smsSetting.getParametr(BASE_URL) + "/send",
                    getRequestEntity(request, smsSetting.getParametr(USERNAME), smsSetting.getParametr(PASSWORD)), String.class);
            log.info("Send Sms response body: " + response.getBody());
            if (response.getStatusCode().equals(HttpStatus.OK) && response.getBody() != null && response.getBody().equals("Request is received")) {
                return true;
            }
        } catch (HttpStatusCodeException e) {
            log.error("Error sending sms : {}", e.getLocalizedMessage());
        }
        return false;
    }

    @Override
    protected boolean checkUrlResult(String res) {
        setResponse(res);
        System.out.println("-------------------------------------- " + res + " -----------------------------");
        return res != null;
    }

    private <T> HttpEntity<T> getRequestEntity(T body, String username, String password) {
        HttpHeaders httpHeaders = createHeaders(username, password);
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        return new HttpEntity<>(body, httpHeaders);
    }

    private HttpHeaders createHeaders(String username, String password) {
        return new HttpHeaders() {{
            String auth = username + ":" + password;
            byte[] encodedAuth = Base64.encodeBase64(
                    auth.getBytes(StandardCharsets.US_ASCII));
            String authHeader = "Basic " + new String(encodedAuth);
            set("Authorization", authHeader);
        }};
    }
}
