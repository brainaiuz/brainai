package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.EdsMoreMenuSettings;
import com.edatasite.workforce.gwt.core.server.db.MoreMenuSettingsManager;
import com.google.gwt.user.server.rpc.security.ServerSecurityContext;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Ilhombek
 * Date: 14.01.2011
 * Time: 15:24:11
 * To change this template use File | Settings | File Templates.
 */
@Repository("moreMenuSettingsManager")
public class MoreMenuSettingsManagerImpl extends BaseManager<EdsMoreMenuSettings> implements MoreMenuSettingsManager {

    public MoreMenuSettingsManagerImpl() {
        super(EdsMoreMenuSettings.class);
    }

    public EdsMoreMenuSettings getMoreMenuSettings(String actionName) {
        return (EdsMoreMenuSettings) findSingle("SELECT moreMenu FROM EdsMoreMenuSettings moreMenu where moreMenu.actionName = ? and moreMenu.enabled = true", actionName);
    }

    public EdsMoreMenuSettings getMoreMenuSettings(String actionName, Integer companyID) {
        ServerSecurityContext.getInstance().setCompanyId(companyID);
        return (EdsMoreMenuSettings) findSingle("SELECT moreMenu FROM EdsMoreMenuSettings moreMenu where moreMenu.actionName = ?", actionName);
    }

    public List<EdsMoreMenuSettings> getMoreMenuSettingItems(Integer companyID) {
        ServerSecurityContext.getInstance().setCompanyId(companyID);
        return find("select moreMenu from EdsMoreMenuSettings moreMenu");
    }
}
