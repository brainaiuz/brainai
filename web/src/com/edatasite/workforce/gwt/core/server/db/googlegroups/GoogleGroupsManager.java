package com.edatasite.workforce.gwt.core.server.db.googlegroups;


import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.crm.EdsGoogleWFTGroups;
import com.edatasite.workforce.gwt.core.server.db.Manager;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: 30.06.11
 * Time: 10:01
 * To change this template use File | Settings | File Templates.
 */
public interface GoogleGroupsManager extends Manager<EdsGoogleWFTGroups> {

    List<EdsGoogleWFTGroups> getGroupSettings(EdsUser user, Boolean... isOfficeGroup);

    Long getGroupSettingsCount(EdsUser user, Boolean... isOfficeGroup);
}
