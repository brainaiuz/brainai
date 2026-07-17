package com.edatasite.workforce.gwt.core.server.app.hmrc.service;

import com.edatasite.workforce.gwt.core.server.app.hmrc.dto.HmrcUserCredentialsDTO;

public interface HmrcAuthService {

    String generateAuthorizationRequestURL();

    HmrcUserCredentialsDTO exchangeToken(String code, boolean refreshToken);
}
