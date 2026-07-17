package com.edatasite.workforce.gwt.core.server.db;

import com.edatasite.workforce.core.domain.EdsWageRate;

import java.util.List;

public interface WageRateManager extends Manager<EdsWageRate> {
    List<EdsWageRate> findWageRates();

    EdsWageRate getWageRate(Integer id);
}
