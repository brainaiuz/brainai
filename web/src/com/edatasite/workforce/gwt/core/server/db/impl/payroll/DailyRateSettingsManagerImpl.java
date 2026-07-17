package com.edatasite.workforce.gwt.core.server.db.impl.payroll;

import com.edatasite.workforce.core.domain.payrolluk.EdsDailyRateSettings;
import com.edatasite.workforce.gwt.core.server.db.impl.BaseManager;
import com.edatasite.workforce.gwt.core.server.db.payroll.DailyRateSettingsManager;
import org.springframework.stereotype.Repository;

@Repository("dailyRateSettingsManager")
public class DailyRateSettingsManagerImpl extends BaseManager<EdsDailyRateSettings> implements DailyRateSettingsManager {
    public DailyRateSettingsManagerImpl() {
        super(EdsDailyRateSettings.class);
    }

    @Override
    public EdsDailyRateSettings getOne() {
        return (EdsDailyRateSettings) findSingle("select edrs from EdsDailyRateSettings edrs");
    }
}
