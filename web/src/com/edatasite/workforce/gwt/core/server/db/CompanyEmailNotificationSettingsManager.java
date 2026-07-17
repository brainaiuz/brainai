package com.edatasite.workforce.gwt.core.server.db;

import com.edatasite.workforce.core.domain.settings.EdsCompanyEmailNotificationSettings;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Ilhombek
 * Date: 09.11.2010
 * Time: 17:04:18
 * To change this template use File | Settings | File Templates.
 */
public interface CompanyEmailNotificationSettingsManager extends Manager<EdsCompanyEmailNotificationSettings> {

    List<EdsCompanyEmailNotificationSettings> getCompanyEmailNotificationSettingsWithGroup(String groupIds);

    List<EdsCompanyEmailNotificationSettings> getCompanyEmailNotification(String notificationName);

    List<EdsCompanyEmailNotificationSettings> getCompanyEmailNotificationSettingsWithNotificationName(String groupIds, String notificationName);
}
