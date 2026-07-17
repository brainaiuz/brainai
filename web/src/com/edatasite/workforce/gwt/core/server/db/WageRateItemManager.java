package com.edatasite.workforce.gwt.core.server.db;

import com.edatasite.workforce.core.domain.EdsWageRateItem;

public interface WageRateItemManager extends Manager<EdsWageRateItem> {
    void deleteWageRateItems(Integer wageRateId);
}
