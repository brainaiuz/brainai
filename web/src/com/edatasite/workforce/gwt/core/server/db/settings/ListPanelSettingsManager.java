package com.edatasite.workforce.gwt.core.server.db.settings;

import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.settings.EdsListPanelSettings;
import com.edatasite.workforce.gwt.core.server.db.Manager;

/**
 * User: Dilsh0d
 * Email: dilshod.toj@gmail.com
 * Date: 18-Aug-2010
 * Time: 14:30:54
 */
public interface ListPanelSettingsManager extends Manager<EdsListPanelSettings> {

    EdsListPanelSettings getUserListPanelSettings(String type, Integer parentID);

    void deleteListPanelSettings(String type);

    EdsListPanelSettings getUserListPanelSettings(EdsUser user, String type);

    EdsListPanelSettings getUserListPanelSettings(EdsUser user, String type, Integer parentID);

    EdsListPanelSettings getDefaultListPanelSettings(String type);
}
