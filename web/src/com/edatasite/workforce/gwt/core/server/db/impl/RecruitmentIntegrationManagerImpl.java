package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.settings.EdsRecruitmentIntegration;
import com.edatasite.workforce.gwt.core.server.db.RecruitmentIntegrationManager;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository("recruitmentManager")
public class RecruitmentIntegrationManagerImpl extends BaseManager<EdsRecruitmentIntegration> implements RecruitmentIntegrationManager {
    public RecruitmentIntegrationManagerImpl() {
        super(EdsRecruitmentIntegration.class);
    }

    @Override
    public EdsRecruitmentIntegration getCompanyCredentials() {
        return (EdsRecruitmentIntegration) findSingle("select r from EdsRecruitmentIntegration r");
    }

    @Override
    public void updateHHCredentials(String code, String accessToken, String refreshToken, Date expireAt) {
        update("update EdsRecruitmentIntegration set hhCode = ?, hhAccessToken = ?, hhRefreshToken = ?, hhTokenExpireAt = ?", code, accessToken, refreshToken, expireAt);
    }

    @Override
    public void updateZoomCredentials(String code, String accessToken, String refreshToken) {
        updateNative("update " + getCompanyId() + ".recruitment_integration set zoom_code='" + code + "',zoom_access_token='" + accessToken + "', zoom_refresh_token ='" + refreshToken + "'");
    }
}
