package com.edatasite.workforce.gwt.core.server.db.impl.googlegroups;

import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.crm.EdsGoogleWFTGroups;
import com.edatasite.workforce.gwt.core.server.db.googlegroups.GoogleGroupsManager;
import com.edatasite.workforce.gwt.core.server.db.impl.BaseManager;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: 30.06.11
 * Time: 10:03
 * To change this template use File | Settings | File Templates.
 */
@Repository("googleGroupsManager")
public class GoogleGroupsManagerImpl extends BaseManager<EdsGoogleWFTGroups> implements GoogleGroupsManager {

    public GoogleGroupsManagerImpl() {
        super(EdsGoogleWFTGroups.class);
    }

    public List<EdsGoogleWFTGroups> getGroupSettings(EdsUser user, Boolean... isOfficeGroup) {
        String sss = "(gwg.isOfficeGroup <> true or gwg.isOfficeGroup = null)";
        if (isOfficeGroup.length > 0 && isOfficeGroup[0] != null && isOfficeGroup[0]) {
            sss = "gwg.isOfficeGroup = true";
        }
        return (List<EdsGoogleWFTGroups>) find("SELECT gwg FROM EdsGoogleWFTGroups gwg WHERE gwg.user=? and " + sss + " AND (gwg.deleted is false OR gwg.deleted is null)", user);
    }

    public Long getGroupSettingsCount(EdsUser user, Boolean... isOfficeGroup) {
        String sss = "(gwg.isOfficeGroup <> true or gwg.isOfficeGroup = null)";
        if (isOfficeGroup.length > 0 && isOfficeGroup[0] != null && isOfficeGroup[0]) {
            sss = "gwg.isOfficeGroup = true";
        }
        return (Long) findSingle("SELECT count(gwg) FROM EdsGoogleWFTGroups gwg WHERE gwg.user=? and " + sss + " AND (gwg.deleted is false OR gwg.deleted is null)", user);
    }
}
