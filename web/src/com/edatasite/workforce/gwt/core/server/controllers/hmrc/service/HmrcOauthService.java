package com.edatasite.workforce.gwt.core.server.controllers.hmrc.service;

import com.edatasite.workforce.gwt.core.server.controllers.hmrc.HmrcUserType;
import com.edatasite.workforce.gwt.core.server.controllers.hmrc.dto.HmrcAgentToClientRequest;
import com.edatasite.workforce.gwt.core.server.rpc.office365.TokenResponseTO;
import com.edatasite.workforce.utils.EdsContextParams;
import com.google.common.base.Joiner;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Arrays;
import java.util.UUID;

@Component
public class HmrcOauthService {

    final String authorizeUrlTest = "https://test-api.service.hmrc.gov.uk/oauth/authorize";
    final String authorizeUrlProd = "https://api.service.hmrc.gov.uk/oauth/authorize";

    public final String tokenUrlTest = "https://test-api.service.hmrc.gov.uk/oauth/token";
    public final String tokenUrlLive = "https://api.service.hmrc.gov.uk/oauth/token";

    public final String inviteUrlTest = "https://test-api.service.hmrc.gov.uk/agents/%s/invitations";
    public final String inviteUrlLive = "https://api.service.hmrc.gov.uk/agents/{arn}/invitations";

//    @Value("${clientId}")
    private String clientId = "v9JHBJlr7A2xWBB2lX1h9FhP8DQa";
//    @Value("${clientSecret}")
    private String clientSecret = "def4b8ba-0faa-4e96-81dc-adb57c318fff";

    RestTemplate restTemplate = new RestTemplate();

    public HmrcOauthService() {

    }

    public String getAuthorizationRequestUrl() {
        final String state = UUID.randomUUID().toString();

        return UriComponentsBuilder.fromHttpUrl(authorizeUrlTest)
                .queryParam("client_id", clientId)
                .queryParam("scope", Joiner.on(" ").join("read:vat", "write:vat", "write:sent-invitations", "read:sent-invitations"))
                .queryParam("state", state)
                .queryParam("response_type", "code")
                .queryParam("redirect_uri", EdsContextParams.getHost() + "/hmrc/oauth-callback")
                .toUriString();
    }

    public TokenResponseTO exchangeToken(String code) {
        try {

            final MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
            headers.add("Content-Type", "application/x-www-form-urlencoded");
            final MultiValueMap<String, String> map = new LinkedMultiValueMap<>();

            map.add("client_id", clientId);
            map.add("client_secret", clientSecret);
            map.add("grant_type", "authorization_code");
            map.add("code", code);
            map.add("redirect_uri", EdsContextParams.getHost() + "/hmrc/oauth-callback");

            final HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(map, headers);

            return restTemplate.postForObject(tokenUrlTest, entity, TokenResponseTO.class);


        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public TokenResponseTO refreshToken(String refreshToken) {
        try {

            final MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
            headers.add("Content-Type", "application/x-www-form-urlencoded");
            final MultiValueMap<String, String> map = new LinkedMultiValueMap<>();

            map.add("client_id", clientId);
            map.add("client_secret", clientSecret);
            map.add("grant_type", "refresh_token");
            map.add("refresh_token", refreshToken);
            map.add("redirect_uri", EdsContextParams.getHost() + "/hmrc/oauth-callback");

            final HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(map, headers);

            return restTemplate.postForObject(tokenUrlTest, entity, TokenResponseTO.class);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public String createAgentClientRelationship(String agentToken, String arn, HmrcUserType clientType, String clientVrn, String knownFact) {
        try {
            final MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
            headers.add("Accept", "application/vnd.hmrc.1.0+json");
            headers.add("Authorization", "Bearer " + agentToken);
            headers.add("Content-Type", MediaType.APPLICATION_JSON_VALUE);

            HmrcAgentToClientRequest invitation = new HmrcAgentToClientRequest();
            invitation.setService(Arrays.asList("MTD-VAT"));
            invitation.setClientType(clientType);
            invitation.setClientIdType("vrn");
            invitation.setClientId(clientVrn);
            invitation.setKnownFact(knownFact);

            final HttpEntity<HmrcAgentToClientRequest> entity = new HttpEntity<>(invitation, headers);

            ResponseEntity<String> responseEntity = restTemplate.exchange(String.format(inviteUrlTest, arn), HttpMethod.POST, entity, String.class);
            if(responseEntity.getStatusCode().equals(HttpStatus.NO_CONTENT) && responseEntity.getHeaders()!=null && responseEntity.getHeaders().getLocation()!=null) {
                return responseEntity.getHeaders().getLocation().toString();
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return null;
    }


}
