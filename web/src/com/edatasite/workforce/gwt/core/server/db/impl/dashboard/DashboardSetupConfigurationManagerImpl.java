package com.edatasite.workforce.gwt.core.server.db.impl.dashboard;

import com.edatasite.workforce.core.domain.dashboard.EdsDashboardSetupConfiguration;
import com.edatasite.workforce.gwt.core.server.db.dashboard.DashboardSetupConfigurationManager;
import com.edatasite.workforce.gwt.core.server.db.impl.BaseManager;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("dashboardSetupConfigurationManager")
public class DashboardSetupConfigurationManagerImpl extends BaseManager<EdsDashboardSetupConfiguration> implements DashboardSetupConfigurationManager {

    public DashboardSetupConfigurationManagerImpl() {
        super(EdsDashboardSetupConfiguration.class);
    }

    @Override
    public List<EdsDashboardSetupConfiguration> getListByDashboardId(Integer dashboardId) {
        String sqlQuery = "select dsc from EdsDashboardSetupConfiguration dsc " +
                " where dsc.dashboardId = :dashboardId " +
                "order by dsc.objectID";
        return slaveEntityManager.createQuery(sqlQuery, EdsDashboardSetupConfiguration.class)
                .setParameter("dashboardId", dashboardId)
                .getResultList();
    }
}
