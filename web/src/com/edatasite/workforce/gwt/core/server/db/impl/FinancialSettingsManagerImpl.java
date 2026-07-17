package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.accounting.EdsFinancialSettings;
import com.edatasite.workforce.gwt.core.server.db.FinancialSettingsManager;
import org.springframework.stereotype.Repository;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: Jun 3, 2009
 * Time: 4:40:17 PM
 * To change this template use File | Settings | File Templates.
 */
@Repository("financialSettingsManager")
public class FinancialSettingsManagerImpl extends BaseManager<EdsFinancialSettings> implements FinancialSettingsManager {

    public FinancialSettingsManagerImpl() {
        super(EdsFinancialSettings.class);
    }

    @Override
    public EdsFinancialSettings getSettingsByCompany(Integer companyID) {
        return (EdsFinancialSettings) findNativeSingle("select fc.* from \"" + companyID + "\".financialsettings fc ", EdsFinancialSettings.class);
    }

    @Override
    public EdsFinancialSettings getFinancialSettings() {
        return (EdsFinancialSettings) findSingle("from EdsFinancialSettings fc");
    }
}
