package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.rbac.EdsGroup;
import com.edatasite.workforce.core.domain.settings.EdsCompanyEmailNotificationSettings;
import com.edatasite.workforce.core.domain.settings.EdsEmailNotificationSettings;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.server.db.CompanyEmailNotificationSettingsManager;
import com.edatasite.workforce.gwt.core.server.db.EmailNotificationSettingsManager;
import com.edatasite.workforce.gwt.core.server.db.UserManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: Ilhombek
 * Date: 03.11.2010
 * Time: 16:42:07
 * To change this template use File | Settings | File Templates.
 */
@Repository("emailNotificationSettingsManager")
public class EmailNotificationSettingsManagerImpl extends BaseManager<EdsEmailNotificationSettings> implements EmailNotificationSettingsManager {

    @Autowired
    private CompanyEmailNotificationSettingsManager companyEmailNotificationSettingsManager;
    @Autowired
    private UserManager userManager;

    public EmailNotificationSettingsManagerImpl() {
        super(EdsEmailNotificationSettings.class);
    }

    public List<EdsEmailNotificationSettings> getUserEmailNotifications(Integer userId) {
        return find("select ens from EdsEmailNotificationSettings ens where ens.user.objectID = ?", userId);
    }

    public EdsEmailNotificationSettings getUserEmailNotification(Integer userId, String notificationType) {
        return (EdsEmailNotificationSettings)
                findSingle("select ens from EdsEmailNotificationSettings ens where ens.user.objectID = ? and ens.notificationType = ?", userId, notificationType);
    }

    public boolean hasEmailNotification(Integer userId, String notificationType) {
        EdsEmailNotificationSettings emailNotificationSetting = getUserEmailNotification(userId, notificationType);
        if (emailNotificationSetting != null) {
            return emailNotificationSetting.isEnabled();
        } else {
            EdsUser user = userManager.get(userId);
            Set<EdsGroup> groups = user.getMembershipGroups();
            StringBuilder groupIds = new StringBuilder();
            String delimitr = "";
            for (EdsGroup group : groups) {
                groupIds.append(delimitr);
                groupIds.append(group.getObjectID());
                delimitr = ",";
            }
            List<EdsCompanyEmailNotificationSettings> companyEmailNotificationSettings = companyEmailNotificationSettingsManager.getCompanyEmailNotificationSettingsWithNotificationName(groupIds.toString(), notificationType);
            if (companyEmailNotificationSettings == null || companyEmailNotificationSettings.size() == 0) { //for custom roles only, some users might not have any system role
                companyEmailNotificationSettings = companyEmailNotificationSettingsManager.getCompanyEmailNotificationSettingsWithNotificationName(Constants.MEM.toString(), notificationType);
            }
            return companyEmailNotificationSettings != null && companyEmailNotificationSettings.size() > 0 && companyEmailNotificationSettings.get(0) != null && companyEmailNotificationSettings.get(0).isEnabled();
        }
    }

    public boolean hasEmailNotificationForUserOrAllCompany(Integer userId, String notificationType) {
        EdsEmailNotificationSettings emailNotificationSetting = getUserEmailNotification(userId, notificationType);
        if (emailNotificationSetting != null) {
            return emailNotificationSetting.isEnabled();
        } else {
            List<EdsCompanyEmailNotificationSettings> companyEmailNotificationSettings = companyEmailNotificationSettingsManager.getCompanyEmailNotification(notificationType);
            return companyEmailNotificationSettings != null && companyEmailNotificationSettings.size() > 0 && companyEmailNotificationSettings.get(0) != null && companyEmailNotificationSettings.get(0).isEnabled();
        }
    }
}
