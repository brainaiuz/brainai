package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.EdsGoogleCalendarSettings;
import com.edatasite.workforce.gwt.core.server.db.GoogleCalendarSettingsManager;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by IntelliJ IDEA.
 * User: Ilxom Lutfullaev
 * Date: Nov 2, 2010
 * Time: 8:15:04 PM
 * To change this template use File | Settings | File Templates.
 */

@Repository("calendarSettingsManager")
public class GoogleCalendarSettingsManagerImpl extends BaseManager<EdsGoogleCalendarSettings> implements GoogleCalendarSettingsManager {

    public GoogleCalendarSettingsManagerImpl() {
        super(EdsGoogleCalendarSettings.class);
    }

    @Transactional
    public EdsGoogleCalendarSettings getUserCalendarSettings(Integer userId) {
        EdsGoogleCalendarSettings calendarSettings = (EdsGoogleCalendarSettings) findSingle("select settings from EdsGoogleCalendarSettings settings where settings.calendarOwner.objectID=?", userId);
        if (calendarSettings == null) {
            EdsGoogleCalendarSettings googleCalendarSettings = new EdsGoogleCalendarSettings();
            googleCalendarSettings.setCalendarOwner(getUser());
            googleCalendarSettings.setEventIsChecked(true);
            googleCalendarSettings.setCallIsChecked(false);
            googleCalendarSettings.setProjectIsChecked(false);
            googleCalendarSettings.setTaskIsChecked(false);
            googleCalendarSettings.setIssueIsChecked(false);
            googleCalendarSettings.setPaIsChecked(false);
            googleCalendarSettings.setLeaveRequestIsChecked(false);
            googleCalendarSettings.setHolidayIsChecked(false);
            googleCalendarSettings.setCourseIsChecked(false);
            googleCalendarSettings.setDefaultView(2); // 2 is Monthly View
            create(googleCalendarSettings);
            return googleCalendarSettings;
        } else {
            return calendarSettings;
        }
    }
}
