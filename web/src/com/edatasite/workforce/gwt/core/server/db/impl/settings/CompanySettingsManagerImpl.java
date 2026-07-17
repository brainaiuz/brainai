package com.edatasite.workforce.gwt.core.server.db.impl.settings;

import com.edatasite.workforce.core.domain.settings.EdsCompanySettings;
import com.edatasite.workforce.gwt.core.server.db.impl.BaseManager;
import com.edatasite.workforce.gwt.core.server.db.settings.CompanySettingsManager;
import org.springframework.stereotype.Repository;

/**
 * Created by IntelliJ IDEA.
 * User: Sherali
 * Date: 08.05.2010
 * Time: 18:32:44
 * To change this template use File | Settings | File Templates.
 */
@Repository("companySettingsManager")
public class CompanySettingsManagerImpl extends BaseManager<EdsCompanySettings> implements CompanySettingsManager {

    public CompanySettingsManagerImpl() {
        super(EdsCompanySettings.class);
    }

    @Override
    public Object getColumnValue(Integer companyID, String columnName) {
        return findNativeSingle("select " + columnName + " from companysettings where id = (select companysettingsid from company where id = " + companyID + ")");
    }

    @Override
    public EdsCompanySettings getCompanySettings(Integer companyID) {
        return (EdsCompanySettings) findSingle("SELECT c.companySettings FROM EdsCompany c WHERE c.objectID = ?", companyID);
    }
}
