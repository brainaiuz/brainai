package com.edatasite.workforce.gwt.core.server.app.hmrc.service;

import com.edatasite.workforce.gwt.core.server.app.hmrc.dto.HmrcUserCredentialsDTO;
import com.google.api.client.auth.oauth2.TokenResponse;

public interface HmrcUserCredentialsService {

    void saveCredentials(HmrcUserCredentialsDTO credentialsDTO);

    HmrcUserCredentialsDTO getCredentials();

    void updateFinancialSettingsAuthorized();
}
