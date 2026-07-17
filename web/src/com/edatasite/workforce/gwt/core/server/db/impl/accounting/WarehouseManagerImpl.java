package com.edatasite.workforce.gwt.core.server.db.impl.accounting;

import com.edatasite.workforce.core.domain.EdsRole;
import com.edatasite.workforce.core.domain.accounting.EdsWarehouse;
import com.edatasite.workforce.gwt.accounting.client.rpc.WarehouseItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.db.accounting.WarehouseManager;
import com.edatasite.workforce.gwt.core.server.db.impl.BaseManager;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository("warehouseManager")
public class WarehouseManagerImpl extends BaseManager<EdsWarehouse> implements WarehouseManager {

    public WarehouseManagerImpl() {
        super(EdsWarehouse.class);
    }

    @Override
    public List<EdsWarehouse> getWarehouseList(ListingFilterParameter fp) {
        if (!hasWarehousePermissions()) {
            return new ArrayList<>();
        }

        StringBuilder sql = new StringBuilder("SELECT w FROM EdsWarehouse w");
        if (!ServerUtils.hasPermission(PermissionConstants.ACCOUNTING_WAREHOUSES_SEE_ALL)) {
            if (!getUser().hasRole(EdsRole.ADMIN_CODE)) {
                sql.append(" LEFT JOIN w.owners owners");
            }
        }
        sql.append(" WHERE 1=1 ");
        if (fp != null) {
            addSqlWhere(fp, sql);
            addSqlOrderBy(fp, sql);
            return findInterval(sql.toString(), fp.getStart(), fp.getLimit());
        }
        return find(sql.toString());
    }

    private boolean hasWarehousePermissions() {
        return ServerUtils.hasPermission(PermissionConstants.ACCOUNTING_WAREHOUSES_SEE_ALL) || ServerUtils.hasPermission(PermissionConstants.ACCOUNTING_WAREHOUSES_SEE_OWN);
    }

    private void addSqlWhere(ListingFilterParameter fp, StringBuilder sql) {
        if (fp.getValueMap().get("VIEW_TYPE") != null && fp.getValueMap().get("VIEW_TYPE").equals("warehouseList")) {
            fp.setCheckBeforeSelected(true);
        }
        if (fp != null) {

            if (!ServerUtils.hasPermission(PermissionConstants.ACCOUNTING_WAREHOUSES_SEE_ALL) && fp.isCheckBeforeSelected()) {
                if (ServerUtils.hasPermission(PermissionConstants.ACCOUNTING_WAREHOUSES_SEE_OWN) && !getUser().hasRole(EdsRole.ADMIN_CODE)) {
                    sql.append(" AND owners.objectID = ").append(getUser().getObjectID());
                }
            }
        }
        if (fp != null && fp.getSqlSearchKey() != null) {
            String sqlSearchKey = fp.getSqlSearchKey();
            if (fp.isLookUp()) {
                sql.append(" AND ( LOWER(w.name) LIKE '").append(sqlSearchKey).append("' ");
            } else {
                sql.append(" AND (");
                sql.append(" LOWER(w.name) LIKE '").append(sqlSearchKey).append("' ");
                sql.append(" OR LOWER(w.notes) LIKE '").append(sqlSearchKey).append("' ");
            }
            sql.append(")");
        }
    }

    private void addSqlOrderBy(ListingFilterParameter fp, StringBuilder sql) {
        if (fp == null || fp.isLookUp()) {
            sql.append(" ORDER BY w.name");
        } else if (fp.getSortField() != null) {
            String ascOrDesc = fp.isAscending() ? " ASC" : " DESC";
            sql.append(" ORDER BY ");
            switch (fp.getSortField()) {
                case WarehouseItem.NAME -> sql.append("w.name");
                case WarehouseItem.NOTES -> sql.append("w.notes");
                case WarehouseItem.EMAIL -> sql.append("w.email");
                default -> sql.append("w.objectID");
            }
            sql.append(ascOrDesc);
        }
    }

    @Override
    public Integer getWarehouseListCount(ListingFilterParameter filterParameters) {
        if (!hasWarehousePermissions()) {
            return 0;
        }

        StringBuilder sql = new StringBuilder("SELECT COUNT(w.id) FROM EdsWarehouse w");
        if (!ServerUtils.hasPermission(PermissionConstants.ACCOUNTING_WAREHOUSES_SEE_ALL)) {
            if (!getUser().hasRole(EdsRole.ADMIN_CODE)) {
                sql.append(" LEFT JOIN w.owners owners");
            }
        }
        sql.append(" WHERE 1=1 ");

        addSqlWhere(filterParameters, sql);

        return Integer.parseInt(findSingle(sql.toString()).toString());
    }

    @Override
    public EdsWarehouse getByName(String name) {
        if (StringUtils.isBlank(name)) {
            return null;
        }
        String trimmedName = name.trim().toLowerCase();
        return (EdsWarehouse) findSingle("FROM EdsWarehouse w WHERE LOWER(TRIM(w.name)) = ?", trimmedName);
    }

    @Override
    public List<EdsWarehouse> getWarehousesByOwner(Integer userId) {
        return find("SELECT w FROM EdsWarehouse w JOIN w.owners owners WHERE owners.objectID = " + userId);
    }

    @Override
    public boolean hasAccessToWarehouse(Integer userId, Integer warehouseId) {
        StringBuilder query = new StringBuilder();
        query.append("SELECT count(w.objectID) FROM EdsWarehouse w JOIN w.owners owners WHERE ")
                .append(" w.objectID = " + warehouseId)
                .append(" AND owners.objectID = " + userId);
        Long count = (Long) findSingle(query.toString());
        return count != null && count > 0;
    }

    @Override
    public EdsWarehouse getDefaultWarehouse() {
        return (EdsWarehouse) findSingle("SELECT w FROM EdsWarehouse w WHERE w.defaultWarehouse = true ORDER BY id DESC");
    }
}
