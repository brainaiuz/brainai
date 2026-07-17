package com.edatasite.workforce.gwt.core.server.db.dashboard;

import com.edatasite.workforce.core.domain.dashboard.EdsDashboard;
import com.edatasite.workforce.gwt.core.client.enums.ModuleEnum;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.server.db.Manager;

import java.util.List;

public interface DashboardManager extends Manager<EdsDashboard> {

    Integer getListCount(ListingFilterParameter fp);

    List<EdsDashboard> getList(ListingFilterParameter fp);

    boolean duplicateDashboardName(Integer objectId, ModuleEnum module, String name);

    List<EdsDashboard> getUserDashboardList(ListingFilterParameter fp);

    void updateDefaultDashboards(Integer objectId, ModuleEnum module);
}
