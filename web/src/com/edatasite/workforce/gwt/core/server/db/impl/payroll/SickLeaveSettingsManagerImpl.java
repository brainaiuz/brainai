package com.edatasite.workforce.gwt.core.server.db.impl.payroll;

import com.edatasite.workforce.core.domain.payrolluk.EdsSickLeaveSettings;
import com.edatasite.workforce.gwt.core.server.db.impl.BaseManager;
import com.edatasite.workforce.gwt.core.server.db.payroll.SickLeaveSettingsManager;
import org.springframework.stereotype.Repository;

@Repository("sickLeaveSettingsManager")
public class SickLeaveSettingsManagerImpl extends BaseManager<EdsSickLeaveSettings> implements SickLeaveSettingsManager {
    public SickLeaveSettingsManagerImpl() {
        super(EdsSickLeaveSettings.class);
    }

    @Override
    public EdsSickLeaveSettings getOne() {
        return (EdsSickLeaveSettings) findSingle("select esls from EdsSickLeaveSettings esls");
    }
}
