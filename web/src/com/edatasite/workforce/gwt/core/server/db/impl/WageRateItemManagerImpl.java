package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.EdsWageRateItem;
import com.edatasite.workforce.gwt.core.server.db.WageRateItemManager;
import org.springframework.stereotype.Repository;

@Repository("wageRateItemManager")
public class WageRateItemManagerImpl extends BaseManager<EdsWageRateItem> implements WageRateItemManager {
    public WageRateItemManagerImpl() {
        super(EdsWageRateItem.class);
    }


    @Override
    public void deleteWageRateItems(Integer wageRateId) {
        update("delete from EdsWageRateItem where wageRate.objectID = " + wageRateId);
    }
}
