package com.edatasite.workforce.gwt.core.server.app.hmrc.service.impl;

import com.edatasite.workforce.gwt.core.server.app.hmrc.dto.HmrcUserCredentialsDTO;
import com.edatasite.workforce.gwt.core.server.app.hmrc.service.HmrcAuthService;
import com.edatasite.workforce.gwt.core.server.controllers.login.BaseLoginController;
import com.edatasite.workforce.gwt.core.server.rpc.office365.TokenResponseTO;
import com.edatasite.workforce.utils.EdsContextParams;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.UUID;

import static com.edatasite.workforce.gwt.core.server.app.hmrc.constants.HmrcConstants.*;

@Service("hmrcAuthService")
public class HmrcAuthServiceImpl implements HmrcAuthService {
    private static final Logger log = LoggerFactory.getLogger(BaseLoginController.class);

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public String generateAuthorizationRequestURL() {
        String SCOPES = "read:vat write:vat";
        final String state = UUID.randomUUID().toString();

        return UriComponentsBuilder.fromHttpUrl(EdsContextParams.getHmrcUrl() + OAUTH_AUTHORIZE_URL)
                .queryParam("response_type", "code")
                .queryParam("client_id", EdsContextParams.getHmrcClientId())
                .queryParam("scope", SCOPES)
                .queryParam("state", state)
                .queryParam("redirect_uri", EdsContextParams.getHost() + CALLBACK_URL)
                .toUriString();
    }

    @Override
    public HmrcUserCredentialsDTO exchangeToken(String token, boolean refreshToken) {

        String EXCHANGE_TOKEN_URL = EdsContextParams.getHmrcEndpointDomain() + OAUTH_TOKEN_URL;
        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.add("Content-Type", "application/x-www-form-urlencoded");
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();


        map.add("client_id", EdsContextParams.getHmrcClientId());
        map.add("client_secret", EdsContextParams.getHmrcClientSecret());
        map.add("grant_type", refreshToken ? "refresh_token" : "authorization_code");
        map.add(refreshToken ? "refresh_token" : "code", token);
        map.add("redirect_uri", EdsContextParams.getHost() + CALLBACK_URL);

        final HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(map, headers);

        ResponseEntity<TokenResponseTO> responseEntity = restTemplate.postForEntity(EXCHANGE_TOKEN_URL, entity, TokenResponseTO.class);
        if (responseEntity.hasBody()) {
            return new HmrcUserCredentialsDTO(responseEntity.getBody());
        }
        return null;
    }
}
