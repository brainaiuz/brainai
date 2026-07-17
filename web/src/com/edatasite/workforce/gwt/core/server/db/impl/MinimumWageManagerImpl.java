package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.EdsMinimumWage;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.db.MinimumWageManager;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("minimumWagerManager")
public class MinimumWageManagerImpl extends BaseManager<EdsMinimumWage> implements MinimumWageManager {
    public MinimumWageManagerImpl() {
        super(EdsMinimumWage.class);
    }

    @Override
    public List<EdsMinimumWage> findMinimumWages() {
        return find("select w from EdsMinimumWage w where " + ServerUtils.checkForDeleted("w.deleted"));
    }

    @Override
    public EdsMinimumWage getMinimumWage(Integer id) {
        return (EdsMinimumWage) findSingle("select w from EdsMinimumWage w where w.objectID = " + id + " and " + ServerUtils.checkForDeleted("w.deleted"));
    }
}
