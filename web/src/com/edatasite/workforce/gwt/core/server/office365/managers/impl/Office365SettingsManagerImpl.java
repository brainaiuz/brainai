package com.edatasite.workforce.gwt.core.server.office365.managers.impl;

import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.gwt.core.server.db.impl.BaseManager;
import com.edatasite.workforce.gwt.core.server.office365.domain.EdsOffice365Settings;
import com.edatasite.workforce.gwt.core.server.office365.managers.Office365SettingsManager;
import com.edatasite.workforce.gwt.office365.client.rpc.Office365CalendarSettings;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by umidbekkarimov on 11/23/15.
 */
@Repository("office365SettingsManager")
public class Office365SettingsManagerImpl extends BaseManager<EdsOffice365Settings> implements Office365SettingsManager {
    public Office365SettingsManagerImpl() {
        super(EdsOffice365Settings.class);
    }

    @Override
    @Transactional
    public EdsOffice365Settings getOrCreate() {
        return this.getOrCreate(this.getUser());
    }

    @Override
    @Transactional
    public EdsOffice365Settings getOrCreate(EdsUser user) {
        EdsOffice365Settings settings = (EdsOffice365Settings)
                findSingle("SELECT s from EdsOffice365Settings s WHERE s.user = ?", user);

        if (settings == null) {
            settings = new EdsOffice365Settings();
            settings.setUser(user);
            this.create(settings);
        }

        return settings;
    }

    @Override
    @Transactional
    public void updateCalendarSettings(Office365CalendarSettings settings) {
        EdsOffice365Settings office365Settings = this.getOrCreate();

        office365Settings.setSyncCalendar(settings.isSync());
        office365Settings.setTaskCalendarId(settings.getTaskCalendarId());
        office365Settings.setEventCalendarId(settings.getEventCalendarId());

        this.update(office365Settings);
    }
}
