package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.EdsGymFingerPrint;
import com.edatasite.workforce.gwt.core.server.db.GymFingerPrintManager;
import org.springframework.stereotype.Repository;

@Repository("gymFingerPrintManager")
public class GymFingerPrintManagerImpl extends BaseManager<EdsGymFingerPrint> implements GymFingerPrintManager {
    public GymFingerPrintManagerImpl() {
        super(EdsGymFingerPrint.class);
    }
}
