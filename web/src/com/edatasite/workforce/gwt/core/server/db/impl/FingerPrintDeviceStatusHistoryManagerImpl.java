package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.EdsFingerPrintDeviceStatusHistory;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.db.FingerPrintDeviceStatusHistoryManager;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by dilsh0d on 21.04.2016.
 */
@Repository("fingerPrintDeviceStatusHistoryManager")
public class FingerPrintDeviceStatusHistoryManagerImpl extends BaseManager<EdsFingerPrintDeviceStatusHistory> implements FingerPrintDeviceStatusHistoryManager {

    public FingerPrintDeviceStatusHistoryManagerImpl() {
        super(EdsFingerPrintDeviceStatusHistory.class);
    }

    @Override
    public List<EdsFingerPrintDeviceStatusHistory> getList(ListingFilterParameter filterParameter) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT fp FROM EdsFingerPrintDeviceStatusHistory fp ");
        sql.append(getBaseQuery(filterParameter));
        sql.append(" ORDER BY fp.statusUpdateTime desc");

        return findInterval(sql.toString(), filterParameter.getStart(), filterParameter.getLimit());

    }

    @Override
    public Integer getListTotal(ListingFilterParameter filterParameter) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT count(fp.objectID)  FROM EdsFingerPrintDeviceStatusHistory fp");
        sql.append(getBaseQuery(filterParameter));
        return ((Long) findSingle(sql.toString())).intValue();
    }

    private String getBaseQuery(ListingFilterParameter filterParameter) {
        StringBuilder sql = new StringBuilder();
        sql.append(" WHERE fp.deviceUniqueKey IN ('").append(ServerUtils.getAsCommoDelimited(filterParameter.getAccountTypes(), "0", "','")).append("')");
        if (StringUtils.isNotBlank(filterParameter.getDeviceID())) {
            sql.append(" AND fp.deviceUniqueKey = '").append(filterParameter.getDeviceID()).append("'");
        }
        if (filterParameter.getSearchKey() != null && !"".equals(filterParameter.getSearchKey())) {
            sql.append(" AND (LOWER(fp.deviceStatus) LIKE '").append(filterParameter.getSqlSearchKey()).append("' ");
            sql.append(" OR LOWER(fp.deviceName) LIKE '").append(filterParameter.getSqlSearchKey()).append("' ");
            sql.append(" OR LOWER(fp.description) LIKE '").append(filterParameter.getSqlSearchKey()).append("')");
        }
        return sql.toString();
    }
}
