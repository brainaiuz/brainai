package com.edatasite.workforce.gwt.core.server.db.dashboard;

import com.edatasite.workforce.core.domain.dashboard.EdsDashboardSetupConfiguration;
import com.edatasite.workforce.gwt.core.server.db.Manager;

import java.util.List;

public interface DashboardSetupConfigurationManager extends Manager<EdsDashboardSetupConfiguration> {

    List<EdsDashboardSetupConfiguration> getListByDashboardId(Integer dashboardId);
}
