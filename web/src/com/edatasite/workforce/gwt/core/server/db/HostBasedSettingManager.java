package com.edatasite.workforce.gwt.core.server.db;

import com.edatasite.workforce.core.domain.EdsHostBasedSetting;
import com.edatasite.workforce.gwt.core.client.rpc.DynamicLogin;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListPanelGuideSettingsRPC;

import java.util.List;

/**
 * User: Aziz
 * Date: 21.08.2010, Time: 14:52:27
 */
public interface HostBasedSettingManager {

    EdsHostBasedSetting getByHostname(String hostname);

    List<DynamicLogin> getList(ListingFilterParameter filterParameter);

    List<DynamicLogin> getWhiteLabelList(ListingFilterParameter filterParameter);

    EdsHostBasedSetting getLinksByHostName(String hostname);

    Integer getWhiteLabelCount(ListingFilterParameter filterParameter);

    DynamicLogin getDynamicLoginItem(String hostname);

    void saveDynamicLogin(DynamicLogin item);

    void saveWhiteLabel(DynamicLogin item);

    DynamicLogin getWhiteLabelItem(String hostname);

    SelectItem[] getHosts();

    void applyWhiteLabelOverrides(ListPanelGuideSettingsRPC guideSettings);

    boolean isShowAppLinks();

    boolean isShowWiki();
}
