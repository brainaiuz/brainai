package com.edatasite.workforce.gwt.core.server.app.hmrc.service.impl;

import com.edatasite.workforce.core.domain.accounting.EdsFinancialSettings;
import com.edatasite.workforce.core.domain.hmrc.EdsHmrcUserCredentials;
import com.edatasite.workforce.gwt.core.server.app.hmrc.dto.HmrcUserCredentialsDTO;
import com.edatasite.workforce.gwt.core.server.app.hmrc.service.HmrcUserCredentialsService;
import com.edatasite.workforce.gwt.core.server.db.FinancialSettingsManager;
import com.edatasite.workforce.gwt.core.server.db.hmrc.HmrcUserCredentialsManager;
import com.google.api.client.auth.oauth2.TokenResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("hmrcUserCredentialsService")
public class HmrcUserCredentialsServiceImpl implements HmrcUserCredentialsService {

    @Autowired
    private HmrcUserCredentialsManager hmrcUserCredentialsManager;
    @Autowired
    private FinancialSettingsManager financialSettingsManager;

    @Override
    @Transactional
    public void saveCredentials(HmrcUserCredentialsDTO credentialsDTO) {
        EdsHmrcUserCredentials edsHmrcUserCredentials = hmrcUserCredentialsManager.findFirst().orElse(new EdsHmrcUserCredentials());
        edsHmrcUserCredentials.setAccessToken(credentialsDTO.getAccessToken());
        edsHmrcUserCredentials.setTokenType(credentialsDTO.getTokenType());
        edsHmrcUserCredentials.setRefreshToken(credentialsDTO.getRefreshToken());
        edsHmrcUserCredentials.setScope(credentialsDTO.getScope());
        edsHmrcUserCredentials.setExpiresInSeconds(credentialsDTO.getExpiresInSeconds());

        hmrcUserCredentialsManager.createOrUpdate(edsHmrcUserCredentials);
    }

    @Override
    @Transactional(readOnly = true)
    public HmrcUserCredentialsDTO getCredentials() {
        return hmrcUserCredentialsManager.findFirst().map(EdsHmrcUserCredentials::toDTO).orElse(null);
    }

    @Override
    @Transactional
    public void updateFinancialSettingsAuthorized() {
        EdsFinancialSettings financialSettings = financialSettingsManager.getFinancialSettings();
        financialSettings.setHmrcAuthorized(true);
        financialSettingsManager.createOrUpdate(financialSettings);
    }


}
