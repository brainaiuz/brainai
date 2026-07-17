package com.edatasite.workforce.gwt.office365.server.app;

import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.SchedulerConstant;
import com.edatasite.workforce.gwt.core.server.office365.services.Office365AuthService;
import com.edatasite.workforce.gwt.office365.client.rpc.Office365AuthTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service("office365AuthTokenService")
public class Office365AuthTokenTokenServiceImpl implements Office365AuthTokenService, Office365AuthTokenServiceLocal, SchedulerConstant, Constants {

    @Autowired
    Office365AuthService office365AuthService;

    @Override
    public Boolean hasAccessToken(String storageType) {
        return office365AuthService.isUserLinked(storageType);
    }
}
