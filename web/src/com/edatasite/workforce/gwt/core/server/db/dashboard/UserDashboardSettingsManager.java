package com.edatasite.workforce.gwt.core.server.db.dashboard;

import com.edatasite.workforce.core.domain.dashboard.EdsDashboardComponents;
import com.edatasite.workforce.core.domain.dashboard.EdsUserDashboardSettings;
import com.edatasite.workforce.gwt.core.server.db.Manager;

import java.util.List;

public interface UserDashboardSettingsManager extends Manager<EdsUserDashboardSettings> {

    List<EdsUserDashboardSettings> getListByDashboardIdAndUserId(Integer dashboardId, Integer userId);

    List<EdsUserDashboardSettings> getListWithLocalizationByDashboardIdAndUserId(Integer dashboardId, Integer userId);

    List<EdsDashboardComponents> getUserInactiveComponentList(Integer dashboardId, List<Integer> activeComponentIds, Integer userId);

    EdsUserDashboardSettings getItemByDashboardIdAndComponentIdAndUserId(Integer dashboardId, Integer componentId, Integer userId);

    void deleteNotExistByIds(Integer dashboardId, Integer userId, List<Integer> componentIds);

    void deleteAllByDashboardId(Integer dashboardId);
}
