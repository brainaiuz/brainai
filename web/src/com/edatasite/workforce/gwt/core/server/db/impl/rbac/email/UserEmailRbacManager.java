package com.edatasite.workforce.gwt.core.server.db.impl.rbac.email;

import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.rbac.email.EdsUserEmailRbac;
import com.edatasite.workforce.core.domain.settings.EdsEmailSetting;
import com.edatasite.workforce.gwt.core.server.db.Manager;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Sancho
 * Date: 05.01.12
 * Time: 12:48
 * To change this template use File | Settings | File Templates.
 */
public interface UserEmailRbacManager extends Manager<EdsUserEmailRbac> {

    List<EdsUserEmailRbac> getEmailRbacEntries(Integer settingID);

    List<EdsEmailSetting> getSharedEmailAccounts(boolean onlyActive);

    void createEmailOwnerRbacEntry(EdsEmailSetting emailSetting);

    void removeEmailEntries(Integer settingID);

    List<EdsEmailSetting> getSharedUserEmailsForUser(EdsUser user);
}
