package com.edatasite.workforce.gwt.core.server.db.impl.settings;

import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.settings.EdsListPanelSettings;
import com.edatasite.workforce.gwt.core.server.db.impl.BaseManager;
import com.edatasite.workforce.gwt.core.server.db.settings.ListPanelSettingsManager;
import org.springframework.stereotype.Repository;

/**
 * User: Dilsh0d
 * Email: dilshod.toj@gmail.com
 * Date: 18-Aug-2010
 * Time: 14:32:02
 */
@Repository("listPanelSettingsManager")
public class ListPanelSettingsManagerImpl extends BaseManager<EdsListPanelSettings> implements ListPanelSettingsManager {

    public ListPanelSettingsManagerImpl() {
        super(EdsListPanelSettings.class);
    }

    @Override
    public EdsListPanelSettings getUserListPanelSettings(String type, Integer parentID) {
        return parentID == null ? getUserListPanelSettings(getUser(), type) : getUserListPanelSettings(getUser(), type, parentID);
    }

    @Override
    public void deleteListPanelSettings(String type) {
        if (type != null) {
            updateNative("delete from " + getCompanyId() + ".listPanelSettings where panelType='" + type + "'");
        }
    }

    @Override
    public EdsListPanelSettings getUserListPanelSettings(EdsUser user, String type) {

        if (user == null || type == null) {
            return null;
        }
        return (EdsListPanelSettings) findSingle("select lp from EdsListPanelSettings lp where lp.user=? and lp.panelType=?", user, type);
    }

    @Override
    public EdsListPanelSettings getUserListPanelSettings(EdsUser user, String type, Integer parentID) {
        return (EdsListPanelSettings) findSingle("select lp from EdsListPanelSettings lp where lp.user=? and lp.panelType=? and lp.parentID=?", user, type, parentID);
    }

    @Override
    public EdsListPanelSettings getDefaultListPanelSettings(String type) {
        return (EdsListPanelSettings) findSingle("select lp from EdsListPanelSettings lp where lp.defaultSetting=? and lp.panelType=?", true, type);
    }
}
