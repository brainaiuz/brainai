package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.EdsCheckInLocation;
import com.edatasite.workforce.gwt.core.server.db.CheckInLocationManager;
import org.springframework.stereotype.Repository;

@Repository("checkInLocationManager")
public class CheckInLocationManagerImp extends BaseManager<EdsCheckInLocation> implements CheckInLocationManager {
    public CheckInLocationManagerImp() {
        super(EdsCheckInLocation.class);
    }
}
