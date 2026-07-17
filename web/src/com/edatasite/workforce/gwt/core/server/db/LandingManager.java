package com.edatasite.workforce.gwt.core.server.db;

import com.edatasite.workforce.core.domain.EdsLanding;

public interface LandingManager extends Manager<EdsLanding> {

    void setFirstView(String fieldName, String firstView);
}
