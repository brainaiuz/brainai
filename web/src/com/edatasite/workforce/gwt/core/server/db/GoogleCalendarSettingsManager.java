package com.edatasite.workforce.gwt.core.server.db;

import com.edatasite.workforce.core.domain.EdsGoogleCalendarSettings;

/**
 * Created by IntelliJ IDEA.
 * User: Ilxom Lutfullaev
 * Date: Nov 2, 2010
 * Time: 8:15:38 PM
 * To change this template use File | Settings | File Templates.
 */

public interface GoogleCalendarSettingsManager extends Manager<EdsGoogleCalendarSettings>{

    EdsGoogleCalendarSettings getUserCalendarSettings(Integer userId);
}
