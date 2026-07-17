package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.settings.EdsCompanyEmailNotificationSettings;
import com.edatasite.workforce.gwt.core.server.db.CompanyEmailNotificationSettingsManager;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Ilhombek
 * Date: 09.11.2010
 * Time: 17:04:47
 * To change this template use File | Settings | File Templates.
 */
@Repository("companyEmailNotificationSettingsManager")
public class CompanyEmailNotificationSettingsManagerImpl extends BaseManager<EdsCompanyEmailNotificationSettings> implements CompanyEmailNotificationSettingsManager {

    public CompanyEmailNotificationSettingsManagerImpl() {
        super(EdsCompanyEmailNotificationSettings.class);
    }

    public List<EdsCompanyEmailNotificationSettings> getCompanyEmailNotificationSettingsWithGroup(String groupIds) {
        if (groupIds != null && !"".equals(groupIds) && groupIds.trim().length() > 0) {
            return (List<EdsCompanyEmailNotificationSettings>) find("select cen from EdsCompanyEmailNotificationSettings cen where cen.roleGroup.objectID in ( " + groupIds + " ) order by cen.objectID");
        }
        return new ArrayList<>();
    }

    public List<EdsCompanyEmailNotificationSettings> getCompanyEmailNotification(String notificationName) {
        if (notificationName != null && !"".equals(notificationName)) {
            return find("select cens from EdsCompanyEmailNotificationSettings cens where cens.notificationName = ?", notificationName);
        }
        return new ArrayList<>();
    }

    public List<EdsCompanyEmailNotificationSettings> getCompanyEmailNotificationSettingsWithNotificationName(String groupIds, String notificationName) {
        if (groupIds != null && !"".equals(groupIds) && groupIds.trim().length() > 0 && notificationName != null && !"".equals(notificationName)) {
            return (List<EdsCompanyEmailNotificationSettings>) find("select cen from EdsCompanyEmailNotificationSettings cen where cen.roleGroup.objectID in ( " + groupIds + " ) and cen.notificationName = ? order by cen.objectID", notificationName);
        }
        return new ArrayList<>();
    }
}
