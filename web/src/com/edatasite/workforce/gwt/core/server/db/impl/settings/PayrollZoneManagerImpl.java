package com.edatasite.workforce.gwt.core.server.db.impl.settings;

import com.edatasite.workforce.core.domain.settings.EdsPayrollZone;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.db.impl.BaseManager;
import com.edatasite.workforce.gwt.core.server.db.settings.PayrollZoneManager;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("payrollZoneManager")
public class PayrollZoneManagerImpl extends BaseManager<EdsPayrollZone> implements PayrollZoneManager {
    public PayrollZoneManagerImpl() {
        super(EdsPayrollZone.class);
    }

    @Override
    public List<EdsPayrollZone> findZones() {
        return find("select z from EdsPayrollZone z where " + ServerUtils.checkForDeleted("z.deleted"));
    }

    @Override
    public EdsPayrollZone getZone(Integer id) {
        return (EdsPayrollZone) findSingle("select z from EdsPayrollZone z where z.objectID = " + id + " and " + ServerUtils.checkForDeleted("z.deleted"));
    }
}