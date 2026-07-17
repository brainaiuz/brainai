package com.edatasite.workforce.gwt.core.server.db.impl.customfields;

import com.edatasite.workforce.core.domain.customfields.EdsCompanySettingsCustomFields;
import com.edatasite.workforce.gwt.core.server.db.customfields.CompanySettingsCFManager;
import com.edatasite.workforce.gwt.core.server.db.impl.BaseManager;
import org.springframework.stereotype.Repository;

/**
 * Created by Hurshid on 2/4/2016.
 */
@Repository("companySettingsCFManager")
public class CompanySettingsCFManagerImpl extends BaseManager<EdsCompanySettingsCustomFields> implements CompanySettingsCFManager {
    public CompanySettingsCFManagerImpl() {
        super(EdsCompanySettingsCustomFields.class);
    }
}
