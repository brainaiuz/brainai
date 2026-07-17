package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.EdsLanding;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.server.db.LandingManager;
import org.springframework.stereotype.Repository;

@Repository
public class LandingManagerImpl extends BaseManager<EdsLanding> implements LandingManager, Constants {

    public LandingManagerImpl() {
        super(EdsLanding.class);
    }

    public void setFirstView(String fieldName, String firstView) {
        EdsUser user = getUser();
        update("update EdsLanding l set l." + fieldName + "=?"
                + " where l=(select u.landing from EdsUser u where u=?)", firstView, user);
    }
}
