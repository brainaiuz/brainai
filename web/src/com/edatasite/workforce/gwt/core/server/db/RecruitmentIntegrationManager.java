package com.edatasite.workforce.gwt.core.server.db;

import com.edatasite.workforce.core.domain.settings.EdsRecruitmentIntegration;

import java.util.Date;

public interface RecruitmentIntegrationManager extends Manager<EdsRecruitmentIntegration> {
    EdsRecruitmentIntegration getCompanyCredentials();

    void updateHHCredentials(String code, String accessToken, String refreshToken, Date expireAt);

    void updateZoomCredentials(String code, String accessToken, String refreshToken);
}
