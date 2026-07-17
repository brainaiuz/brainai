package com.edatasite.workforce.gwt.core.server.db.payroll;

import com.edatasite.workforce.core.domain.payrolluk.EdsSickLeaveSettings;
import com.edatasite.workforce.gwt.core.server.db.Manager;

public interface SickLeaveSettingsManager extends Manager<EdsSickLeaveSettings> {
    EdsSickLeaveSettings getOne();
}
