package com.edatasite.workforce.gwt.core.server.app;


import com.edatasite.shared.components.EncryptionHelper;
import com.edatasite.workforce.core.domain.EdsHostBasedSetting;
import com.edatasite.workforce.gwt.core.server.rpc.office365.MeUserResponseTO;
import com.edatasite.workforce.gwt.core.server.rpc.office365.TokenResponseTO;
import com.edatasite.workforce.utils.EdsContextParams;
import com.google.common.base.Joiner;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.http.*;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collections;
import java.util.UUID;

/**
 * User: Murad Satimov
 * Date: 9/7/17 8:24 PM
 */
@Service
public class Office365LoginServiceImpl implements Office365LoginService {

    private static final Logger log = LoggerFactory.getLogger(Office365LoginServiceImpl.class);

    private static final String BASE_OFFICE_LOGIN_URL = "https://login.microsoftonline.com/common/oauth2/v2.0/";
    private static final String OFFICE_LOGIN_URL = BASE_OFFICE_LOGIN_URL + "authorize";
    private static final String TOKEN_URL = BASE_OFFICE_LOGIN_URL + "token";

    private static final String STATE_COOKIE = "__office__365__kpi";
    private static final String GRAPH_API = "https://graph.microsoft.com/v1.0/me";

    private static final String STATE_SECRET = "$$$$office$$$kpi";
//    private static final String REDIRECT_URL = EdsContextParams.getHost() + "/office365Login";

    @Override
    public String createLoginUrl(HttpServletRequest request, HttpServletResponse response) {
        final EdsHostBasedSetting hostSetting = EdsContextParams.getHostSetting(request.getServerName());

        if (hostSetting == null || ServerUtils.isNullOrEmpty(hostSetting.getOffice365ClientId())) {
            return null;
        }
        final String state = UUID.randomUUID().toString();
        final Cookie cookie = new Cookie(STATE_COOKIE, EncryptionHelper.md5(STATE_SECRET + state + STATE_SECRET));

        cookie.setPath("/");
        cookie.setMaxAge(15 * 60);
        response.addCookie(cookie);
        return UriComponentsBuilder.fromHttpUrl(OFFICE_LOGIN_URL)
                                   .queryParam("prompt", "login")
                                   .queryParam("response_type", "code")
                                   .queryParam("scope", Joiner.on(" ").join("user.read", "mail.read"))
                                   .queryParam("state", state)
                                   .queryParam("client_id", hostSetting.getOffice365ClientId())
                                   .queryParam("redirect_uri", EdsContextParams.getHost() + "/office365Login")
                                   .toUriString();
    }

    @Override
    @Transactional
    public MeUserResponseTO getUser(HttpServletRequest request, HttpServletResponse response) {
        if (request == null || response == null) {
            log.error("Error empty incoming params!");
            return null;
        }
        final String state = request.getParameter("state");

        if (ServerUtils.isNullOrEmpty(state)) {
            log.error("Error state not found!");
            return null;
        }
        final String cookieStateValue = this.getCookieValueAndExpire(request, response);

        if (ServerUtils.isNullOrEmpty(cookieStateValue) ||
            !cookieStateValue.equals(EncryptionHelper.md5(STATE_SECRET + state + STATE_SECRET))) {
            log.error("Error incorrect state value!");
            return null;
        }
        final EdsHostBasedSetting hostSetting = EdsContextParams.getHostSetting(request.getServerName());

        if (hostSetting == null) {
            log.error("EdsHostBasedSetting not found!");
            return null;
        }
        final String clientId = hostSetting.getOffice365ClientId();
        final String clientSecret = hostSetting.getOffice365ClientSecret();
        final String code = request.getParameter("code");

        if (ServerUtils.isNullOrEmpty(clientId) ||
            ServerUtils.isNullOrEmpty(clientSecret) ||
            ServerUtils.isNullOrEmpty(code)) {
            log.error("Error, office365  clientId or client sercret not found!");
            return null;
        }
        final TokenResponseTO tokenTO = this.getAccessToken(clientId, clientSecret, code);

        if (tokenTO == null || ServerUtils.isNullOrEmpty(tokenTO.getAccess_token())) {
            log.error("Error getting office 365 access token!");
            return null;
        }
        return this.getUserByToken(tokenTO);
    }

    private TokenResponseTO getAccessToken(String clientId, String clientSecret, String code) {
        if (ServerUtils.isNullOrEmpty(clientId) ||
            ServerUtils.isNullOrEmpty(clientSecret) ||
            ServerUtils.isNullOrEmpty(code)) {
            return null;
        }
        final RestTemplate restTemplate = new RestTemplate();
        final MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.add("Content-Type", "application/x-www-form-urlencoded");
        final MultiValueMap<String, String> map = new LinkedMultiValueMap<>();

        map.add("client_id", clientId);
        map.add("scope", "user.read mail.read");
        map.add("code", code);
        map.add("redirect_uri", EdsContextParams.getHost() + "/office365Login");
        map.add("grant_type", "authorization_code");
        map.add("client_secret", clientSecret);

        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
        final HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(map, headers);

        restTemplate.getMessageConverters().add(new FormHttpMessageConverter());
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
        return restTemplate.postForObject(TOKEN_URL, entity, TokenResponseTO.class);
    }

    public MeUserResponseTO getUserByToken(TokenResponseTO tokenTO) {
        if (tokenTO == null || ServerUtils.isNullOrEmpty(tokenTO.getAccess_token())) {
            return null;
        }
        final RestTemplate restTemplate = new RestTemplate();
        final HttpHeaders headers = new HttpHeaders();

        headers.set("Authorization", "Bearer " + tokenTO.getAccess_token());
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        final HttpEntity<String> entity = new HttpEntity<>("parameters", headers);
        
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
        final ResponseEntity<MeUserResponseTO> response = restTemplate.exchange(GRAPH_API,
                                                                                HttpMethod.GET,
                                                                                entity,
                                                                                MeUserResponseTO.class);

        return response.getBody();
    }

    private String getCookieValueAndExpire(HttpServletRequest req, HttpServletResponse res) {
        if (req == null || res == null) {
            return null;
        }
        String cookieValue = null;
        for (Cookie cookie : req.getCookies()) {
            if (STATE_COOKIE.equals(cookie.getName())) {
                cookieValue = cookie.getValue();
                break;
            }
        }
        final Cookie cookie = new Cookie(STATE_COOKIE, "");

        cookie.setPath("/");
        cookie.setMaxAge(-1);
        res.addCookie(cookie);
        return cookieValue;
    }
}
