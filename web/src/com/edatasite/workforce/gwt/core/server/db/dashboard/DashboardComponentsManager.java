package com.edatasite.workforce.gwt.core.server.db.dashboard;

import com.edatasite.workforce.core.domain.dashboard.EdsDashboardComponents;
import com.edatasite.workforce.gwt.core.server.db.Manager;

import java.util.List;

public interface DashboardComponentsManager extends Manager<EdsDashboardComponents> {

    EdsDashboardComponents getItemByDashboardIdAndComponentId(Integer dashboardId, Integer componentId);

    void deleteNotExistByIds(Integer dashboardId, List<Integer> componentIds);

    List<EdsDashboardComponents> getListByDashboardId(Integer dashboardId);

    List<EdsDashboardComponents> getListWithComponentByDashboardId(Integer dashboardId);

    EdsDashboardComponents getByIdAndDashboardId(Integer id, Integer dashboardId);
}
