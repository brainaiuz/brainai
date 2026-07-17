package com.edatasite.workforce.gwt.core.client.rpc;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.rpc.dashboard.DashboardComponentItem;
import com.edatasite.workforce.gwt.core.client.rpc.dashboard.DashboardDefaultComponentItem;
import com.edatasite.workforce.gwt.core.client.rpc.dashboard.GettingStartedItem;
import com.edatasite.workforce.gwt.core.client.rpc.dashboard.ModuleDashboardListItem;
import com.edatasite.workforce.gwt.core.client.rpc.dashboard.UserDashboardSettingsItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.ServiceDefTarget;

import java.util.ArrayList;

/**
 * User: Abror Abdukadirov
 * Date: 10.04.2018 14:57
 */
public interface ModuleDashboardService extends RemoteService {

    ListResult<ModuleDashboardListItem> getModuleDashboardList(ListingFilterParameter fp);

    ArrayList<SelectItem> getModuleDashboards(ListingFilterParameter fp);

    ModuleDashboardListItem getModuleDashboardItem(Integer objectId);

    Integer saveDashboardComponents(UserDashboardSettingsItem data);

    ArrayList<DashboardDefaultComponentItem> getComponentList(Integer dashboardId, ArrayList<String> componentCodes);

    ArrayList<DashboardComponentItem> getDashboardComponentList(Integer dashboardId);

    ModuleDashboardListItem getModuleDashboardItemForEdit(Integer objectId);

    Integer saveModuleDashboardItem(ModuleDashboardListItem item);

    Integer saveUserDashboardSettings(UserDashboardSettingsItem data);

    UserDashboardSettingsItem getUserDashboardSettings(Integer dashboardId);

    Integer deleteModuleDashboardItem(Integer objectId);

    ArrayList<GettingStartedItem> getDashboardSetupConfiguration(Integer dashboardId);

    void updateDashboardSetupConfiguration(GettingStartedItem item);

    Integer getDashboardWidgetsMaxCount(Integer dashboardId);

    class App {
        public static ModuleDashboardServiceAsync get() {
            ServiceDefTarget target = GWT.create(CoreGenericService.class);
            target.setServiceEntryPoint(Utils.getHostNameURL() + "rpc/moduleDashboard");
            return (ModuleDashboardServiceAsync) target;
        }
    }
}
