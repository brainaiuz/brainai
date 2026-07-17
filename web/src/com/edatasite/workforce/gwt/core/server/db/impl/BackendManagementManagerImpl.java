package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.EdsBackendManagement;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.server.db.BackendManagementManager;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Ilhombek
 * Date: 4/23/12
 * Time: 2:51 PM
 * To change this template use File | Settings | File Templates.
 */
@Repository("backendManagementManager")
public class BackendManagementManagerImpl extends BaseManager<EdsBackendManagement> implements BackendManagementManager {

    public BackendManagementManagerImpl() {
        super(EdsBackendManagement.class);
    }

    public List<EdsBackendManagement> getBackendManagements(ListingFilterParameter fp) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT bm ");
        sql.append(getBaseSql(fp));
        if (StringUtils.isNotBlank(fp.getSortField())) {
            sql.append(" ORDER BY bm.").append(fp.getSortField()).append(fp.isAscending() ? " ASC" : " DESC");
        } else {
            sql.append(" ORDER BY bm.userName DESC");
        }

        return findInterval(sql.toString(), fp.getStart(), fp.getLimit());
    }

    public Integer getBackendManagementsCount(ListingFilterParameter fp) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT COUNT(bm.objectID) ");
        sql.append(getBaseSql(fp));
        return ((Long) findSingle(sql.toString())).intValue();
    }

    private String getBaseSql(ListingFilterParameter fp) {
        StringBuilder sql = new StringBuilder();
        sql.append(" FROM EdsBackendManagement bm ");
        sql.append("WHERE (bm.deleted is null or bm.deleted<>true) ");
        if (fp.getCompanyID() != null) {
            sql.append(" AND bm.companyID=").append(fp.getCompanyID());
        }
        if (StringUtils.isNotBlank(fp.getSqlSearchKey())) {
            sql.append(" AND ( ");
            sql.append(" lower(bm.companyName) like '").append(fp.getSqlSearchKey()).append("' ");
            sql.append(" or lower(bm.creatorName) like '").append(fp.getSqlSearchKey()).append("' ");
            sql.append(" or lower(bm.localComputerIPAddress) like '").append(fp.getSqlSearchKey()).append("' ");
            sql.append(" or lower(bm.localComputerName) like '").append(fp.getSqlSearchKey()).append("' ");
            sql.append(" or lower(bm.updaterName) like '").append(fp.getSqlSearchKey()).append("' ");
            sql.append(" or lower(bm.userName) like '").append(fp.getSqlSearchKey()).append("' ");
            sql.append(" ) ");
        }

        return sql.toString();
    }

    public EdsBackendManagement getBackendManagement(Integer companyID, Integer userID) {
        return (EdsBackendManagement) findSingle("SELECT bm FROM EdsBackendManagement bm WHERE bm.deleted<>true AND bm.companyID=? AND bm.userID=?", companyID, userID);
    }
}
