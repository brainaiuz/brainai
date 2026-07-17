package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.EdsMonth;
import com.edatasite.workforce.gwt.core.server.db.MonthManager;
import org.springframework.stereotype.Repository;

@Repository("monthManager")
public class MonthManagerImpl extends BaseManager<EdsMonth> implements MonthManager {

    public MonthManagerImpl() {
        super(EdsMonth.class);
    }

}
