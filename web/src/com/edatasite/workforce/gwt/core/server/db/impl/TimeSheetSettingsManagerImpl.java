package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.EdsCompany;
import com.edatasite.workforce.core.domain.EdsTimeSheetSettings;
import com.edatasite.workforce.gwt.core.server.db.TimeSheetSettingsManager;
import org.springframework.stereotype.Repository;

/**
 * Created by IntelliJ IDEA.
 * User: Ruslan Muhammadov
 * Date: Jul 13, 2010
 * Time: 11:10:52 AM
 * To change this template use File | Settings | File Templates.
 */
@Repository("timesheetSettingsManager")
public class TimeSheetSettingsManagerImpl extends BaseManager<EdsTimeSheetSettings> implements TimeSheetSettingsManager {

    public TimeSheetSettingsManagerImpl() {
        super(EdsTimeSheetSettings.class);
    }

    public EdsTimeSheetSettings getCompanyTimesheetSettings(EdsCompany company) {
        Object settings = findSingle("from EdsTimeSheetSettings tss ");

        if (settings != null) {
            return (EdsTimeSheetSettings) settings;
        }
        return EdsTimeSheetSettings.DEFAULT_SETTINGS;
    }
}
