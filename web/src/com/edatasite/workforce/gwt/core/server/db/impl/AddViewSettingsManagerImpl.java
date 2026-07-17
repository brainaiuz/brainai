package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.EdsAddViewSettings;
import com.edatasite.workforce.gwt.core.server.db.AddViewSettingsManager;
import org.springframework.stereotype.Repository;

/**
 * User: Dilsh0d
 * Email: dilshod.toj@gmail.com
 * Date: 17-Nov-2010
 * Time: 21:40:24
 */
@Repository("addViewSettingsManager")
public class AddViewSettingsManagerImpl extends BaseManager<EdsAddViewSettings> implements AddViewSettingsManager {
    public AddViewSettingsManagerImpl() {
        super(EdsAddViewSettings.class);
    }

    @Override
    public EdsAddViewSettings getAddViewSettings() {
        return (EdsAddViewSettings) findSingle("select add from EdsAddViewSettings add");
    }
}
