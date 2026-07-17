package com.edatasite.workforce.gwt.core.server.db.impl.settings;

import com.edatasite.workforce.core.domain.settings.EdsQuickAddSettings;
import com.edatasite.workforce.gwt.core.client.rpc.quickAddSettings.QuickAddSettingsForm;
import com.edatasite.workforce.gwt.core.server.db.impl.BaseManager;
import com.edatasite.workforce.gwt.core.server.db.settings.QuickAddSettingsManager;
import org.springframework.stereotype.Repository;

@Repository("quickAddSettingsManager")
public class QuickAddSettingsManagerImpl extends BaseManager<EdsQuickAddSettings> implements QuickAddSettingsManager {
    public QuickAddSettingsManagerImpl() {
        super(EdsQuickAddSettings.class);
    }


    @Override
    public EdsQuickAddSettings getByForm(QuickAddSettingsForm form) {
        return (EdsQuickAddSettings) findSingle("select s from EdsQuickAddSettings s where s.form = '" + form + "'");
    }
}
