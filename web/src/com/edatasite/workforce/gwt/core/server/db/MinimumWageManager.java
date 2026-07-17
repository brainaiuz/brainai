package com.edatasite.workforce.gwt.core.server.db;

import com.edatasite.workforce.core.domain.EdsMinimumWage;

import java.util.List;

public interface MinimumWageManager extends Manager<EdsMinimumWage> {
    List<EdsMinimumWage> findMinimumWages();

    EdsMinimumWage getMinimumWage(Integer id);
}
