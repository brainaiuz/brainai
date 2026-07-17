package com.edatasite.workforce.gwt.core.server.db.impl.dashboard;

import com.edatasite.workforce.core.domain.dashboard.EdsDashboardComponents;
import com.edatasite.workforce.gwt.core.server.db.dashboard.DashboardComponentsManager;
import com.edatasite.workforce.gwt.core.server.db.impl.BaseManager;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository("dashboardComponentsManager")
public class DashboardComponentsManagerImpl extends BaseManager<EdsDashboardComponents> implements DashboardComponentsManager {

    public DashboardComponentsManagerImpl() {
        super(EdsDashboardComponents.class);
    }

    @Override
    public List<EdsDashboardComponents> getListByDashboardId(Integer dashboardId) {
        String sqlQuery = "select dc from EdsDashboardComponents dc " +
                " where dc.dashboard.objectID = :dashboardId order by y, x";
        return slaveEntityManager.createQuery(sqlQuery, EdsDashboardComponents.class)
                .setParameter("dashboardId", dashboardId)
                .getResultList();
    }

    @Override
    public List<EdsDashboardComponents> getListWithComponentByDashboardId(Integer dashboardId) {
        String sqlQuery = "select dc from EdsDashboardComponents dc " +
                "join fetch dc.component c " +
                "left join fetch c.kpiWidget kw " +
                "left join fetch kw.customFormLocalization " +
                "left join fetch c.report r " +
                "left join fetch r.chartConfig cc " +
                "left join fetch cc.customFormLocalization " +
                "where dc.dashboard.objectID = :dashboardId order by dc.y, dc.x";
        return slaveEntityManager.createQuery(sqlQuery, EdsDashboardComponents.class)
                .setParameter("dashboardId", dashboardId)
                .getResultList();
    }

    @Override
    public void deleteNotExistByIds(Integer dashboardId, List<Integer> componentIds) {
        masterEntityManager.createQuery("delete from EdsDashboardComponents dc " +
                "     where dc.dashboard.objectID =:dashboardId " +
                "         and dc.component.objectID not in (:componentIds)")
                .setParameter("dashboardId", dashboardId)
                .setParameter("componentIds", componentIds)
                .executeUpdate();
    }

    @Override
    public EdsDashboardComponents getByIdAndDashboardId(Integer id, Integer dashboardId) {
        String sqlQuery = "select dc from EdsDashboardComponents dc " +
                " join fetch dc.component c " +
                " left join fetch c.kpiWidget " +
                " left join fetch c.report " +
                " where dc.objectID = :id " +
                " and dc.dashboard.objectID = :dashboardId";
        return (EdsDashboardComponents) findSingleByNamedParams(sqlQuery,
                Map.of("id", id, "dashboardId", dashboardId));
    }

    @Override
    public EdsDashboardComponents getItemByDashboardIdAndComponentId(Integer dashboardId, Integer componentId) {
        return (EdsDashboardComponents) findSingle("select dc from EdsDashboardComponents dc " +
                "    where dc.dashboard.objectID =? " +
                "        and dc.component.objectID =?", dashboardId, componentId);
    }
}
