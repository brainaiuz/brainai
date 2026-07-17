package com.edatasite.workforce.gwt.core.server.db.impl.settings;

import com.edatasite.workforce.core.domain.settings.EdsHRSettings;
import com.edatasite.workforce.gwt.core.server.db.impl.BaseManager;
import com.edatasite.workforce.gwt.core.server.db.settings.HRSettingsManager;
import org.springframework.stereotype.Repository;

@Repository("hrSettingsManager")
public class HRSettingsManagerImpl extends BaseManager<EdsHRSettings> implements HRSettingsManager {
    public HRSettingsManagerImpl() {
        super(EdsHRSettings.class);
    }

    @Override
    public EdsHRSettings findOne() {
         return (EdsHRSettings) findSingle("select hrs from EdsHRSettings hrs");
    }
}
