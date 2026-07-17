package com.edatasite.workforce.gwt.core.server.db.settings;

import com.edatasite.workforce.core.domain.settings.EdsCompanySettings;
import com.edatasite.workforce.gwt.core.server.db.Manager;

/**
 * Created by IntelliJ IDEA.
 * User: Sherali
 * Date: 08.05.2010
 * Time: 18:31:05
 * To change this template use File | Settings | File Templates.
 */
public interface CompanySettingsManager extends Manager<EdsCompanySettings> {
    Object getColumnValue(Integer companyID, String columnName);

    EdsCompanySettings getCompanySettings(Integer companyID);
}
