package com.edatasite.workforce.gwt.core.server.db;

import com.edatasite.workforce.core.domain.settings.EdsEmailNotificationSettings;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Ilhombek
 * Date: 03.11.2010
 * Time: 16:42:48
 * To change this template use File | Settings | File Templates.
 */
public interface EmailNotificationSettingsManager extends Manager<EdsEmailNotificationSettings> {

    List<EdsEmailNotificationSettings> getUserEmailNotifications(Integer userId);

    EdsEmailNotificationSettings getUserEmailNotification(Integer userId, String notificationType);

    boolean hasEmailNotification(Integer userId, String notificationType);

    boolean hasEmailNotificationForUserOrAllCompany(Integer userId, String notificationType);
}
