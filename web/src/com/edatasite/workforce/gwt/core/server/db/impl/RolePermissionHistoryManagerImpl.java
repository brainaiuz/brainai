package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.EdsRolePermissionHistory;
import com.edatasite.workforce.gwt.core.client.rpc.RolePermissionHistoryItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.server.db.RolePermissionHistoryManager;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository("rolePermissionHistoryManager")
public class RolePermissionHistoryManagerImpl extends BaseManager<EdsRolePermissionHistory> implements RolePermissionHistoryManager {

    public RolePermissionHistoryManagerImpl() {
        super(EdsRolePermissionHistory.class);
    }

    @Override
    public List<EdsRolePermissionHistory> getHistoryList(ListingFilterParameter fp, boolean isTotalQuery) {
        StringBuilder sql = new StringBuilder();
        String companyID = getCompanyId();
        sql.append("SELECT rph.* from ").append(companyID).append(".rolepermission_history rph \n");
        sql.append(" left join ").append(companyID).append(".myuser myc on myc.id = rph.updaterid \n");

        if (fp.getSqlSearchKey() != null) {
            sql.append(" where  (");
            sql.append("lower(myc.firstName) like '").append(fp.getSqlSearchKey()).append("' ");
            sql.append("or lower(myc.lastName) like '").append(fp.getSqlSearchKey()).append("' ");
            sql.append("or lower(rph.permissionName) like '").append(fp.getSqlSearchKey()).append("' ");
            sql.append("or lower(rph.context) like '").append(fp.getSqlSearchKey()).append("' ");
            sql.append("or lower(rph.oldValue) like '").append(fp.getSqlSearchKey()).append("' ");
            sql.append("or lower(rph.newValue) like '").append(fp.getSqlSearchKey()).append("' ");
            sql.append("or lower(rph.rolename) like '").append(fp.getSqlSearchKey()).append("' ");
            sql.append(") ");
        }

        if (isTotalQuery) {
            return findNative(sql.toString(), EdsRolePermissionHistory.class);
        }

        if (fp.getSortField() != null) {
            sql.append(" order by ");
            if (RolePermissionHistoryItem.PERMISSION_NAME.equals(fp.getSortField())) {
                sql.append("rph.permissionName ");
            } else if (RolePermissionHistoryItem.MODULE_NAME.equals(fp.getSortField())) {
                sql.append("rph.context ");
            } else if (RolePermissionHistoryItem.ROLE_NAME.equals(fp.getSortField())) {
                sql.append("rph.roleName");
            } else if (RolePermissionHistoryItem.MODIFIED_BY.equals(fp.getSortField())) {
                if (fp.isAscending()) {
                    sql.append(" myc.firstname, myc.lastname ");
                } else {
                    sql.append(" myc.firstname desc, myc.lastname ");
                }
            } else if (RolePermissionHistoryItem.MODIFIED_DATE.equals(fp.getSortField())) {
                sql.append(" rph.lastUpdateTime ");
            } else {
                sql.append(" rph.lastUpdateTime ");
            }
            if (fp.getSortDir() != null) {
                if (Integer.valueOf(1).equals(fp.getSortDir())) {
                    sql.append(" asc");
                } else {
                    sql.append(" desc");
                }
            } else {
                sql.append(" desc");
            }
        } else {
            sql.append(" order by rph.lastUpdateTime desc");
        }
        if (fp.getLimit() > 0) {
            sql.append(" OFFSET ").append(fp.getStart()).append(" LIMIT ").append(fp.getLimit());
        }

        return findNative(sql.toString(), EdsRolePermissionHistory.class);
    }
}