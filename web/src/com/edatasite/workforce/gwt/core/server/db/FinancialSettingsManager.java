package com.edatasite.workforce.gwt.core.server.db;

import com.edatasite.workforce.core.domain.accounting.EdsFinancialSettings;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: Jun 3, 2009
 * Time: 4:39:55 PM
 * To change this template use File | Settings | File Templates.
 */
public interface FinancialSettingsManager extends Manager<EdsFinancialSettings> {
    EdsFinancialSettings getSettingsByCompany(Integer companyID);

    EdsFinancialSettings getFinancialSettings();
}
