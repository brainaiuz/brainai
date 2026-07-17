package com.edatasite.workforce.gwt.core.server.controllers.hmrc.service;

import com.edatasite.workforce.gwt.core.server.controllers.hmrc.dto.HmrcObligations;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Component
public class HmrcMDTService {

    final String authorizeUrlTest = "https://test-api.service.hmrc.gov.uk/oauth/authorize";
    final String authorizeUrlProd = "https://api.service.hmrc.gov.uk/oauth/authorize";

    public final String obligationsUrlTest = "https://test-api.service.hmrc.gov.uk/organisations/vat/{vrn}/obligations";
    public final String obligationsUrlLive = "https://api.service.hmrc.gov.uk/organisations/vat/{vrn}/obligations";

//    @Value("${clientId}")
    private String clientId = "v9JHBJlr7A2xWBB2lX1h9FhP8DQa";
//    @Value("${clientSecret}")
    private String clientSecret = "def4b8ba-0faa-4e96-81dc-adb57c318fff";

    RestTemplate restTemplate = new RestTemplate();

    public HmrcMDTService() {

    }

    public HmrcObligations getObligations(String token, String vrn, String from, String to, String status) {
        try {

            final MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
//            headers.add("Content-Type", "application/x-www-form-urlencoded");
            headers.add("Authorization", "Bearer " + token);
            headers.add("Accept", "application/vnd.hmrc.1.0+json");
            headers.add("Gov-Test-Scenario", "-");

            final MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
            final HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(map, headers);

            UriComponentsBuilder url = UriComponentsBuilder.fromHttpUrl(obligationsUrlTest.replace("{vrn}", vrn));
            if(StringUtils.isNotBlank(from)) {
                url = url.queryParam("from", from);
            }
            if(StringUtils.isNotBlank(to)) {
                url = url.queryParam("to", to);
            }
            if (StringUtils.isNotBlank(status)) {
                url = url.queryParam("status", status);
            }

            ResponseEntity<String> responseEntity = restTemplate.exchange(url.toUriString(), HttpMethod.GET, entity, String.class);
            if(responseEntity.getStatusCode()==HttpStatus.OK) {
                return new ObjectMapper().readValue(responseEntity.getBody(), HmrcObligations.class);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return null;
    }

}
