package com.edatasite.workforce.gwt.core.client.rpc;

import com.edatasite.workforce.gwt.core.client.rpc.dashboard.DashboardComponentItem;
import com.edatasite.workforce.gwt.core.client.rpc.dashboard.DashboardDefaultComponentItem;
import com.edatasite.workforce.gwt.core.client.rpc.dashboard.GettingStartedItem;
import com.edatasite.workforce.gwt.core.client.rpc.dashboard.ModuleDashboardListItem;
import com.edatasite.workforce.gwt.core.client.rpc.dashboard.UserDashboardSettingsItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.google.gwt.user.client.rpc.AsyncCallback;

import java.util.ArrayList;

/**
 * User: Abror Abdukadirov
 * Date: 10.04.2018 14:58
 */
public interface ModuleDashboardServiceAsync {

    void getModuleDashboardList(ListingFilterParameter fp, AsyncCallback<ListResult<ModuleDashboardListItem>> callback);

    void getModuleDashboards(ListingFilterParameter fp, AsyncCallback<ArrayList<SelectItem>> callback);

    void getModuleDashboardItem(Integer objectId, AsyncCallback<ModuleDashboardListItem> callback);

    void saveDashboardComponents(UserDashboardSettingsItem data, AsyncCallback<Integer> callback);

    void getComponentList(Integer dashboardId, ArrayList<String> componentCodes, AsyncCallback<ArrayList<DashboardDefaultComponentItem>> callback);

    void getDashboardComponentList(Integer dashboardId, AsyncCallback<ArrayList<DashboardComponentItem>> callback);

    void getModuleDashboardItemForEdit(Integer objectId, AsyncCallback<ModuleDashboardListItem> callback);

    void saveModuleDashboardItem(ModuleDashboardListItem item, AsyncCallback<Integer> callback);

    void saveUserDashboardSettings(UserDashboardSettingsItem data, AsyncCallback<Integer> callback);

    void getUserDashboardSettings(Integer dashboardId, AsyncCallback<UserDashboardSettingsItem> callback);

    void deleteModuleDashboardItem(Integer objectId, AsyncCallback<Integer> callback);

    void getDashboardSetupConfiguration(Integer dashboardId, AsyncCallback<ArrayList<GettingStartedItem>> callback);

    void updateDashboardSetupConfiguration(GettingStartedItem item, AsyncCallback<Void> callback);

    void getDashboardWidgetsMaxCount(Integer dashboardId, AsyncCallback<Integer> callback);
}
