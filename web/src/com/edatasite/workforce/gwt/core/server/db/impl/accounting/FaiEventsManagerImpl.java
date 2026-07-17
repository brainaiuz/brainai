package com.edatasite.workforce.gwt.core.server.db.impl.accounting;

import com.edatasite.workforce.core.domain.accounting.EdsFaiEvents;
import com.edatasite.workforce.gwt.core.server.db.accounting.FaiEventsManager;
import com.edatasite.workforce.gwt.core.server.db.impl.BaseManager;
import org.springframework.stereotype.Repository;

@Repository("faiEventsManager")
public class FaiEventsManagerImpl extends BaseManager<EdsFaiEvents> implements FaiEventsManager {

    public FaiEventsManagerImpl() {
        super(EdsFaiEvents.class);
    }

}

