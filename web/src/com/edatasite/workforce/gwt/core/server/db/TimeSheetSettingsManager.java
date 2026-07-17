package com.edatasite.workforce.gwt.core.server.db;

import com.edatasite.workforce.core.domain.EdsCompany;
import com.edatasite.workforce.core.domain.EdsTimeSheetSettings;

/**
 * Created by IntelliJ IDEA.
 * User: Ruslan Muhammadov
 * Date: Jul 13, 2010
 * Time: 11:09:46 AM
 * To change this template use File | Settings | File Templates.
 */
public interface TimeSheetSettingsManager extends Manager<EdsTimeSheetSettings> {

    EdsTimeSheetSettings getCompanyTimesheetSettings(EdsCompany company);
}
