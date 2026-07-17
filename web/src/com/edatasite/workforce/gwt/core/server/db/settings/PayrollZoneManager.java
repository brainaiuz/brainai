package com.edatasite.workforce.gwt.core.server.db.settings;

import com.edatasite.workforce.core.domain.settings.EdsPayrollZone;
import com.edatasite.workforce.gwt.core.server.db.Manager;

import java.util.List;

public interface PayrollZoneManager extends Manager<EdsPayrollZone> {
    List<EdsPayrollZone> findZones();

    EdsPayrollZone getZone(Integer id);
}
