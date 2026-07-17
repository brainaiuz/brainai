package com.edatasite.workforce.gwt.core.server.db.impl.dashboard;

import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.dashboard.EdsDashboard;
import com.edatasite.workforce.gwt.core.client.enums.ModuleEnum;
import com.edatasite.workforce.gwt.core.client.rpc.dashboard.ModuleDashboardListItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.server.db.dashboard.DashboardManager;
import com.edatasite.workforce.gwt.core.server.db.impl.BaseManager;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;

import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.List;

@Repository("dashboardManager")
public class DashboardManagerImpl extends BaseManager<EdsDashboard> implements DashboardManager {

    public DashboardManagerImpl() {
        super(EdsDashboard.class);
    }

    @Override
    public Integer getListCount(ListingFilterParameter fp) {
        boolean hasSearch = !StringUtils.isEmpty(fp.getSearchKey());
        boolean hasModule = !StringUtils.isEmpty(fp.getModule());
        String sqlQuery = "select count(d.id) from EdsDashboard d " +
                          " where d.deleted = false ";

        if (fp.getBoolean(Constants.ACTIVE_STRING) != null && fp.isActive()) {
            sqlQuery += " and d.isActive =:isActive ";
        }
        if (hasModule) {
            sqlQuery += " and d.module =:module ";
        }
        if (hasSearch) {
            sqlQuery += " and lower(d.name) like :searchKey ";
        }
        TypedQuery<Long> query = slaveEntityManager.createQuery(sqlQuery, Long.class)
                                              .setMaxResults(1);
        if (fp.getBoolean(Constants.ACTIVE_STRING) != null && fp.isActive()) {
            query = query.setParameter("isActive", fp.isActive());
        }
        if (hasModule) {
            query = query.setParameter("module", ModuleEnum.getModule(fp.getModule()));
        }
        if (hasSearch) {
            query = query.setParameter("searchKey", "%" + fp.getSearchKey().toLowerCase() + "%");
        }
        List<Long> list = query.getResultList();

        return list.isEmpty() ? 0 : list.get(0).intValue();
    }

    @Override
    public List<EdsDashboard> getList(ListingFilterParameter fp) {
        boolean hasSearch = !StringUtils.isEmpty(fp.getSearchKey());
        boolean hasSort = !StringUtils.isEmpty(fp.getSortField());
        boolean hasModule = !StringUtils.isEmpty(fp.getModule());
        String sqlQuery = "select d from EdsDashboard d " +
                          " left join d.creator c " +
                          " left join d.updator u " +
                          " where d.deleted = false ";

        if (fp.getBoolean(Constants.ACTIVE_STRING) != null && fp.isActive()) {
            sqlQuery += " and d.isActive =:isActive ";
        }
        if (hasModule) {
            sqlQuery += " and d.module =:module ";
        }
        if (hasSearch) {
            sqlQuery += " and lower(d.name) like :searchKey ";
        }
        if (hasSort) {
            switch (fp.getSortField()) {
                case ModuleDashboardListItem.DASHBOARD_NAME -> sqlQuery += " order by d.name ";
                case ModuleDashboardListItem.MODULE -> sqlQuery += " order by d.module ";
                case ModuleDashboardListItem.IS_ACTIVE -> sqlQuery += " order by d.isActive ";
                case ModuleDashboardListItem.IS_DEFAULT -> sqlQuery += " order by d.isDefault ";
                case ModuleDashboardListItem.IS_SYSTEM -> sqlQuery += " order by d.isSystem ";
                case ModuleDashboardListItem.CREATOR -> sqlQuery += " order by c.firstName||' '||c.lastName ";
                case ModuleDashboardListItem.CREATION_DATE -> sqlQuery += " order by d.creationDate ";
                case ModuleDashboardListItem.UPDATOR -> sqlQuery += " order by u.firstName||' '||u.lastName ";
                case ModuleDashboardListItem.UPDATED_DATE -> sqlQuery += " order by d.updatedDate ";
                default -> sqlQuery += " order by d.id ";
            }
            sqlQuery += fp.isAscending() ? "asc" : "desc";
        } else {
            sqlQuery += " order by d.module, d.name ";
        }

        TypedQuery<EdsDashboard> query = slaveEntityManager.createQuery(sqlQuery, EdsDashboard.class)
                                                      .setFirstResult(fp.getStart())
                                                      .setMaxResults(fp.getLimit());
        if (fp.getBoolean(Constants.ACTIVE_STRING) != null && fp.isActive()) {
            query = query.setParameter("isActive", fp.isActive());
        }
        if (hasModule) {
            query = query.setParameter("module", ModuleEnum.getModule(fp.getModule()));
        }
        if (hasSearch) {
            query = query.setParameter("searchKey", "%" + fp.getSearchKey().toLowerCase() + "%");
        }
        return query.getResultList();
    }

    @Override
    public boolean duplicateDashboardName(Integer objectId, ModuleEnum module, String name) {
        String sql = "select d from EdsDashboard d " +
                     "  where d.deleted = false " +
                     "      and d.name = :name " +
                     "      and d.module = :module ";
        if (objectId != null) {
            sql += " and d.objectID != :objectId ";
        }
        TypedQuery<EdsDashboard> query = slaveEntityManager.createQuery(sql, EdsDashboard.class)
                                                      .setParameter("name", name.trim())
                                                      .setParameter("module", module);
        if (objectId != null) {
            query.setParameter("objectId", objectId);
        }
        return query.getResultList().size() > 0;
    }

    @Override
    public List<EdsDashboard> getUserDashboardList(ListingFilterParameter fp) {
        boolean hasRole = fp.getObjectIDs() != null && fp.getObjectIDs().size() > 0;
        boolean hasModule = !StringUtils.isEmpty(fp.getModule());
        EdsUser user = getUser();
        StringBuilder sql = new StringBuilder().append("select distinct d.* ")
                .append("from ").append(getCompanyId()).append(".module_dashboards d ")
                .append("  left join ").append(getCompanyId()).append(".dashboard_accesses da on da.dashboard_id = d.id ")
                .append("      where d.deleted = false ")
                .append("      and d.is_active = true ");
        if (hasRole) {
            sql.append("    and (da.role_id in(:roleIds) ");
        }
        if (user != null) {
            sql.append(" or creator_id = :creatorId) ");
        } else {
            sql.append(" ) ");
        }
        if (hasModule) {
            sql.append(" and d.module = :moduleCode ");
        }
        sql.append(" order by d.name ");
        Query query = slaveEntityManager.createNativeQuery(sql.toString(), EdsDashboard.class)
                                   .setFirstResult(fp.getStart())
                                   .setMaxResults(fp.getLimit());
        if (hasRole) {
            query = query.setParameter("roleIds", fp.getObjectIDs());
        }
        if (user != null) {
            query = query.setParameter("creatorId", user.getObjectID());
        }
        if (hasModule) {
            query = query.setParameter("moduleCode", ModuleEnum.getModule(fp.getModule()).name());
        }
        return query.getResultList();
    }

    @Override
    public void updateDefaultDashboards(Integer objectId, ModuleEnum module) {
        masterEntityManager.createQuery(new StringBuilder().append("update EdsDashboard d set d.isDefault = false ")
                .append("     where d.objectID != :objectId ")
                .append("         and d.module = :module").toString())
                     .setParameter("objectId", objectId)
                     .setParameter("module", module)
                     .executeUpdate();
    }
}
