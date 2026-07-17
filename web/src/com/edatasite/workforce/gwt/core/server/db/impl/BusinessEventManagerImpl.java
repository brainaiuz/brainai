package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.businessevent.EdsBusinessEvent;
import com.edatasite.workforce.gwt.core.server.db.BusinessEventManager;
import org.springframework.stereotype.Repository;

@Repository("businessEventManager")
public class BusinessEventManagerImpl extends BaseManager<EdsBusinessEvent> implements BusinessEventManager {
    public BusinessEventManagerImpl() {
        super(EdsBusinessEvent.class);
    }
}
