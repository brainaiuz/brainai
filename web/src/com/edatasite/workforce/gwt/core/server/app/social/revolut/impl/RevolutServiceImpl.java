package com.edatasite.workforce.gwt.core.server.app.social.revolut.impl;

import com.edatasite.workforce.core.domain.EdsCompany;
import com.edatasite.workforce.core.domain.EdsInvoicingSettings;
import com.edatasite.workforce.gwt.core.server.app.social.revolut.RevolutService;
import com.edatasite.workforce.gwt.core.server.app.social.revolut.dto.RevolutDto;
import com.edatasite.workforce.gwt.core.server.app.social.revolut.dto.RevolutResponseDto;
import com.edatasite.workforce.gwt.core.server.db.InvoicingSettingsManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


@Service
public class RevolutServiceImpl implements RevolutService {
    private final RestTemplate restTemplate = new RestTemplate();
    @Autowired
    protected InvoicingSettingsManager invoicingSettingsManager;

    @Override
    public RevolutResponseDto createOrder(Integer amount, String currency, boolean isOurAccount, String description) {
        EdsCompany company = invoicingSettingsManager.getUser().getCompany();
        EdsInvoicingSettings invSettings = invoicingSettingsManager.getInvoiceSettings(company);
        String secretApiKey = "";
        if (isOurAccount) {
            secretApiKey = "sk_7see7YqnBkzljCdhs3Vt-DprkDvV-gBEwGfTS9cSL7QbxDp1hxtaAMvOO3dYjV_w";
        } else {
            if (invSettings.getRevolutSecretApiKey() == null) {
                return null;
            } else {
                secretApiKey = invSettings.getRevolutSecretApiKey();
            }
        }


        RevolutDto dto = new RevolutDto();
        dto.setEmail(invSettings.getRevolutEmail());
        dto.setCurrency(currency);
        dto.setAmount(amount);
        dto.setDescription(description);
        ResponseEntity<RevolutResponseDto> response = restTemplate.postForEntity("https://merchant.revolut.com/api/1.0/orders",
                getRequestEntity(dto, secretApiKey), RevolutResponseDto.class);
        return response.getBody();
    }


    public <T> HttpEntity<T> getRequestEntity(T body, String secretKey) {
        HttpHeaders httpHeaders = createHeaders(secretKey);
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        return new HttpEntity<>(body, httpHeaders);
    }

    private HttpHeaders createHeaders(String secretKey) {
        return new HttpHeaders() {{
            String authHeader = "Bearer " + secretKey;
            set("Authorization", authHeader);
        }};
    }
}
