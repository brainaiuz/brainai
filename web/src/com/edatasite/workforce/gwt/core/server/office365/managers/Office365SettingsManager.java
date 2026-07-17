package com.edatasite.workforce.gwt.core.server.office365.managers;

import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.gwt.core.server.office365.domain.EdsOffice365Settings;
import com.edatasite.workforce.gwt.office365.client.rpc.Office365CalendarSettings;

/**
 * Created by umidbekkarimov on 11/23/15.
 */
public interface Office365SettingsManager {
    EdsOffice365Settings getOrCreate();

    EdsOffice365Settings getOrCreate(EdsUser user);

    void updateCalendarSettings(Office365CalendarSettings settings);
}
