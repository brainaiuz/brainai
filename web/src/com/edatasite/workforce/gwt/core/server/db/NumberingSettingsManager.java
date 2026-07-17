package com.edatasite.workforce.gwt.core.server.db;

import com.edatasite.workforce.core.domain.EdsNumberingSettings;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: 20.11.2010
 * Time: 19:35:57
 * To change this template use File | Settings | File Templates.
 */
public interface NumberingSettingsManager extends Manager<EdsNumberingSettings> {
    EdsNumberingSettings getNumberingSetting();
}
