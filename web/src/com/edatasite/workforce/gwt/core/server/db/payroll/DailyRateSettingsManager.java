package com.edatasite.workforce.gwt.core.server.db.payroll;

import com.edatasite.workforce.core.domain.payrolluk.EdsDailyRateSettings;
import com.edatasite.workforce.gwt.core.server.db.Manager;

public interface DailyRateSettingsManager extends Manager<EdsDailyRateSettings> {
    EdsDailyRateSettings getOne();
}
