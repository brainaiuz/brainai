package com.edatasite.workforce.gwt.core.server.db;

import com.edatasite.workforce.core.domain.EdsAddViewSettings;

/**
 * User: Dilsh0d
 * Email: dilshod.toj@gmail.com
 * Date: 17-Nov-2010
 * Time: 21:37:49
 */
public interface AddViewSettingsManager extends Manager<EdsAddViewSettings> {
    EdsAddViewSettings getAddViewSettings();
}
