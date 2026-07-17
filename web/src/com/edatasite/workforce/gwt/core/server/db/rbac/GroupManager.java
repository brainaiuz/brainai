package com.edatasite.workforce.gwt.core.server.db.rbac;

import com.edatasite.workforce.core.domain.rbac.EdsGroup;
import com.edatasite.workforce.gwt.core.server.db.Manager;

import java.util.List;

/**
 * User: Abdulaziz
 * Date: Jan 28, 2010
 * Time: 1:07:43 PM
 */
public interface GroupManager extends Manager<EdsGroup> {
//    public List<EdsGroup> getSystemBuiltInGroups();

    EdsGroup getCompanyBuiltInGroup(String groupConstantName);

    List<EdsGroup> getCompanyGroups();

    List<EdsGroup> getZeroSettings();

//    EdsGroup getSystemBuiltInGroup(String groupConstantName);

    List<EdsGroup> getGroupsNotAssigneeTaskPolice();

    List<EdsGroup> getUserGroups(Integer userId);

    boolean existsGroup(Integer userId, String groupName);
    List<EdsGroup> getCompanyDefaultGroups();
}
