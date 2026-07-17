package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.EdsNumberingSettings;
import com.edatasite.workforce.gwt.core.server.db.NumberingSettingsManager;
import org.springframework.stereotype.Repository;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: 20.11.2010
 * Time: 19:36:35
 * To change this template use File | Settings | File Templates.
 */
@Repository("numberingSettingsManager")
public class NumberingSettingsManagerImpl extends BaseManager<EdsNumberingSettings> implements NumberingSettingsManager {
    public NumberingSettingsManagerImpl() {
        super(EdsNumberingSettings.class);
    }

    @Override
    public EdsNumberingSettings getNumberingSetting() {
        EdsNumberingSettings numberingSettings = (EdsNumberingSettings) findSingle("select ns from EdsNumberingSettings ns");
        return numberingSettings != null ? numberingSettings : new EdsNumberingSettings();
    }
}
