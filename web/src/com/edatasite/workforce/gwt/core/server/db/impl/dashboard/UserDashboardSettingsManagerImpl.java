package com.edatasite.workforce.gwt.core.server.db.impl.dashboard;

import com.edatasite.workforce.core.domain.dashboard.EdsDashboardComponents;
import com.edatasite.workforce.core.domain.dashboard.EdsUserDashboardSettings;
import com.edatasite.workforce.gwt.core.server.db.dashboard.UserDashboardSettingsManager;
import com.edatasite.workforce.gwt.core.server.db.impl.BaseManager;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("userDashboardSettingsManager")
public class UserDashboardSettingsManagerImpl extends BaseManager<EdsUserDashboardSettings> implements UserDashboardSettingsManager {

    public UserDashboardSettingsManagerImpl() {
        super(EdsUserDashboardSettings.class);
    }

    @Override
    public List<EdsUserDashboardSettings> getListByDashboardIdAndUserId(Integer dashboardId, Integer userId) {
        String sqlQuery = "select ds from EdsUserDashboardSettings ds " +
                " where ds.dashboard.objectID = :dashboardId " +
                "     and ds.user.objectID = :userId order by y,x";
        return slaveEntityManager.createQuery(sqlQuery, EdsUserDashboardSettings.class)
                .setParameter("dashboardId", dashboardId)
                .setParameter("userId", userId)
                .getResultList();
    }

    @Override
    public List<EdsUserDashboardSettings> getListWithLocalizationByDashboardIdAndUserId(Integer dashboardId, Integer userId) {
        String sqlQuery = "select ds from EdsUserDashboardSettings ds " +
                "left join fetch ds.component c " +
                "left join fetch c.kpiWidget kw " +
                "left join fetch kw.customFormLocalization " +
                "left join fetch c.report r " +
                "left join fetch r.chartConfig cc " +
                "left join fetch cc.customFormLocalization " +
                "where ds.dashboard.objectID = :dashboardId " +
                "    and ds.user.objectID = :userId order by ds.y, ds.x";
        return slaveEntityManager.createQuery(sqlQuery, EdsUserDashboardSettings.class)
                .setParameter("dashboardId", dashboardId)
                .setParameter("userId", userId)
                .getResultList();
    }

    @Override
    public List<EdsDashboardComponents> getUserInactiveComponentList(Integer dashboardId, List<Integer> activeComponentIds, Integer userId) {
        String sqlQuery = "select distinct dc from EdsDashboardComponents dc " +
                " where dc.dashboard.objectID = :dashboardId " +
                "     and dc.component.objectID not in (:componentIds) ";
        return slaveEntityManager.createQuery(sqlQuery, EdsDashboardComponents.class)
                .setParameter("dashboardId", dashboardId)
                .setParameter("componentIds", activeComponentIds)
                .getResultList();
    }

    @Override
    public EdsUserDashboardSettings getItemByDashboardIdAndComponentIdAndUserId(Integer dashboardId, Integer componentId, Integer userId) {
        return (EdsUserDashboardSettings) findSingle("select ds from EdsUserDashboardSettings ds " +
                "  where ds.dashboard.objectID =? " +
                "      and ds.component.objectID =? " +
                "      and ds.user.objectID =?", dashboardId, componentId, userId);
    }

    @Override
    public void deleteNotExistByIds(Integer dashboardId, Integer userId, List<Integer> componentIds) {
        masterEntityManager.createQuery("delete from EdsUserDashboardSettings ud " +
                "     where ud.user.objectID =:userId " +
                "         and ud.dashboard.objectID =:dashboardId" +
                "         and ud.component.objectID not in (:componentIds)")
                .setParameter("userId", userId)
                .setParameter("dashboardId", dashboardId)
                .setParameter("componentIds", componentIds)
                .executeUpdate();
    }

    @Override
    public void deleteAllByDashboardId(Integer dashboardId) {
        masterEntityManager.createQuery("delete from EdsUserDashboardSettings " +
                "     where dashboard.objectID = :dashboardId")
                .setParameter("dashboardId", dashboardId)
                .executeUpdate();
    }
}
