package com.edatasite.workforce.gwt.core.server.db.impl.rbac;

import com.edatasite.workforce.core.domain.rbac.EdsGroup;
import com.edatasite.workforce.core.domain.rbac.EdsTrusteeType;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.server.db.impl.BaseManager;
import com.edatasite.workforce.gwt.core.server.db.rbac.GroupManager;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * User: Abdulaziz
 * Date: Jan 28, 2010
 * Time: 1:11:18 PM
 */
@Repository("groupManager")
public class GroupManagerImpl extends BaseManager<EdsGroup> implements GroupManager {
    public GroupManagerImpl() {
        super(EdsGroup.class);
    }

    public EdsGroup getCompanyBuiltInGroup(String groupConstantName) {
        return (EdsGroup) findSingle("SELECT gr FROM EdsGroup gr WHERE gr.entryType = ? AND gr.constantName = ?", Constants.BUILT_IN, groupConstantName);
    }

    public List<EdsGroup> getCompanyGroups() {
        return (List<EdsGroup>) find("SELECT gr FROM EdsGroup gr WHERE gr.entryType = ? OR gr.entryType = ?",Constants.BUILT_IN,EdsGroup.CUSTOM);
    }
    public List<EdsGroup> getZeroSettings(){
        return (List<EdsGroup>) findNative("SELECT * FROM \"0\".trusteegroup");
    }

    public List<EdsGroup> getCompanyDefaultGroups(){
        return (List<EdsGroup>)find("SELECT gr FROM EdsGroup gr WHERE gr.entryType=?",Constants.BUILT_IN);
    }
    public List<EdsGroup> getGroupsNotAssigneeTaskPolice() {
        return find("SELECT distinct gp FROM EdsGroup gp " +
                "WHERE (gr.entryType = ? OR gr.entryType = ?) AND gp.objectID NOT IN " +
                "(SELECT tp.trustee.trusteeID FROM EdsTaskPolicy tp WHERE tp.trustee IS NOT NULL and tp.trustee.type.objectID=?) " +
                "order by gp.name ", Constants.BUILT_IN,EdsGroup.CUSTOM, EdsTrusteeType.GROUP);

    }

    @Override
    public List<EdsGroup> getUserGroups(Integer userId) {
        Map params = new HashMap();
        params.put("userId", userId);
        params.put("groupName", EdsGroup.ADMINISTRATORS);
        params.put("trusteeType", EdsTrusteeType.USER);
        params.put("groupType", EdsTrusteeType.GROUP);

        return (List<EdsGroup>) findByNamedParams("SELECT gr FROM EdsGroup gr WHERE gr.owner.objectID in " +
                "(SELECT distinct t.objectID FROM EdsTrustee t WHERE (t.trusteeID=:userId and t.type.objectID=:trusteeType) or (t.type.objectID=:groupType and t.trusteeID in " +
                "(select distinct gg.objectID from EdsGroup gg join gg.members memb WHERE gg.constantName=:groupName " +
                "and memb.objectID in (select distinct tt.objectID from EdsTrustee tt where tt.trusteeID=:userId and tt.type.objectID=:trusteeType))))" +
                " ORDER BY gr.name asc", params);
    }

    @Override
    public boolean existsGroup(Integer userId, String groupName) {
        List<EdsGroup> groupList = getUserGroups(userId);
        for (EdsGroup group : groupList) {
            if (groupName.equals(group.getName())) {
                return true;
            }
        }
        return false;
    }
}
