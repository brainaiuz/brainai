package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.EdsWageRate;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.db.WageRateManager;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("wageRateManager")
public class WageRateManagerImpl extends BaseManager<EdsWageRate> implements WageRateManager {
    public WageRateManagerImpl() {
        super(EdsWageRate.class);
    }

    @Override
    public List<EdsWageRate> findWageRates() {
        return find("select w from EdsWageRate w where " + ServerUtils.checkForDeleted("w.deleted"));
    }

    @Override
    public EdsWageRate getWageRate(Integer id) {
        return (EdsWageRate) findSingle("select w from EdsWageRate w where w.objectID = " + id + " and " + ServerUtils.checkForDeleted("w.deleted"));
    }
}
