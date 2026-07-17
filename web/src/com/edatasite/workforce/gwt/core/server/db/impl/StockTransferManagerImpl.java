package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.EdsRole;
import com.edatasite.workforce.core.domain.EdsStockTransfer;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.gwt.core.client.rpc.RelationItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.db.ApproverManager;
import com.edatasite.workforce.gwt.core.server.db.StockTransferManager;
import com.edatasite.workforce.gwt.core.server.db.UserManager;
import com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Map;

/**
 * Created by Dilshod Madrahimov on 2/25/15.
 */
@Repository("stockTransferManager")
public class StockTransferManagerImpl extends BaseManager<EdsStockTransfer> implements StockTransferManager {

    @Autowired
    private UserManager userManager;
    @Autowired
    private ApproverManager approverManager;

    public StockTransferManagerImpl() {
        super(EdsStockTransfer.class);
    }

    @Override
    public ArrayList<EdsStockTransfer> getList(ListingFilterParameter fp) {
        EdsUser user = userManager.getUser();
        boolean ownerAccess = ServerUtils.hasPermission(PermissionConstants.ACCOUNTING_STOCK_TRANSFER_SEE_OWN);
        boolean isApprowall = approverManager.isExistApproverByEntityType(RelationItem.TYPE_STOCK_TRANSFER);

        StringBuilder sql = new StringBuilder();
        sql.append("select distinct st.id, st.*   from " + getCompanyId() + ".stockTransfer st \n");
        sql.append(" left join " + getCompanyId() + ".stock_adjustment sa on st.id = sa.stocktransferid \n");
        sql.append(" left join " + getCompanyId() + ".adjustment_item ai on sa.id = ai.adjustment_id \n");
        sql.append(" left join " + getCompanyId() + ".approvers currap on currap.id=st.currentApprover \n");
        sql.append(" left join " + getCompanyId() + ".approvers prevap on prevap.id=st.prevApprover \n");
        sql.append(" where st.deleted is not true and sa.deleted is not true \n");
        // Need to Add See All Permission!
        if (!user.hasRole(EdsRole.ADMIN_CODE) && ownerAccess) {
            if (isApprowall) {
                sql.append(" and (st.creator = " + user.getObjectID());
                sql.append(" or currap.exactApprover = " + user.getObjectID());
                sql.append(" or prevap.exactApprover = " + user.getObjectID());
            } else {
                sql.append(" and (st.creator = " + user.getObjectID());
            }
            if (ownerAccess) {
                sql.append(" or sa.fromwarehouseid in (select warehouse_id " +
                        "from " + getCompanyId() + ".warehouse_owners where owner_id = " + user.getObjectID()).append(")");
                sql.append(" or sa.towarehouseid in (select warehouse_id " +
                        "from " + getCompanyId() + ".warehouse_owners where owner_id = " + user.getObjectID()).append(")) \n");
            } else {
                sql.append(" )");
            }
        }
        if (fp.getProductId() != null) {
            sql.append(" and ai.item_id = " + fp.getProductId());
        }
        if (fp.getWarehouseID() != null && fp.getWarehouseID() > 0) {
            sql.append(" and (sa.fromwarehouseid = ").append(fp.getWarehouseID());
            sql.append(" or sa.towarehouseid = ").append(fp.getWarehouseID()).append(")");
        }
        if (fp.getSearchKey() != null && !"".equals(fp.getSearchKey())) {
            sql.append(" AND (lower(st.transferName) like '").append(fp.getSqlSearchKey()).append("' )");
            sql.append(" OR (lower(st.number) like '").append(fp.getSqlSearchKey()).append("' )");
        }
        if (fp.getStartDate() != null && fp.getEndDate() != null) {
            sql.append("AND st.date between '").append(fp.getStartDate()).append("' and '").append(fp.getEndDate()).append("' ");
        } else if (fp.getStartDate() != null) {
            sql.append("AND st.date >= '").append(fp.getStartDate()).append("' ");
        } else if (fp.getEndDate() != null) {
            sql.append("AND st.date <= '").append(fp.getEndDate()).append("' ");
        }
        if ("date".equalsIgnoreCase(fp.getSortField())) {
            sql.append(" order by st.date desc ");
        } else {
            sql.append(" order by st.id desc ");
        }

        if (fp.getLimit() > 0) {
            sql.append(" limit " + fp.getLimit());
        }
        if (fp.getStart() > 0) {
            sql.append(" offset " + fp.getStart());
        }
        ArrayList<EdsStockTransfer> result = (ArrayList<EdsStockTransfer>) findNative(sql.toString(), EdsStockTransfer.class);
        if (result == null && result.isEmpty()) {
            result = new ArrayList<>();
        }
        return result;
    }

    @Override
    public Integer getTotalCount(ListingFilterParameter fp) {
        EdsUser user = userManager.getUser();
        boolean ownerAccess = ServerUtils.hasPermission(PermissionConstants.ACCOUNTING_STOCK_TRANSFER_SEE_OWN);
        boolean isApprowall = approverManager.isExistApproverByEntityType(RelationItem.TYPE_STOCK_TRANSFER);

        StringBuilder sql = new StringBuilder();
        String companyID = getCompanyId();
        sql.append("select count(distinct st.id) from " + companyID + ".stockTransfer st ");
        sql.append("left join " + companyID + ".stock_adjustment sa on st.id = sa.stocktransferid ");
        sql.append("left join " + companyID + ".adjustment_item ai on sa.id = ai.adjustment_id ");
        sql.append("where st.deleted is not true and sa.deleted is not true ");
        if (!user.hasRole(EdsRole.ADMIN_CODE) && ownerAccess) {
            if (isApprowall) {
                sql.append(" and (st.creator = " + user.getObjectID());
                sql.append(" or st.currentapprover = " + user.getObjectID());
                sql.append(" or st.prevapprover = " + user.getObjectID()).append(" )");
            } else {
                sql.append(" and st.creator = " + user.getObjectID());
            }
        }
        if (fp.getProductId() != null) {
            sql.append("and ai.item_id = " + fp.getProductId());
        }
        if (fp.getWarehouseID() != null && fp.getWarehouseID() > 0) {
            sql.append("and (sa.fromwarehouseid = ").append(fp.getWarehouseID());
            sql.append("or sa.towarehouseid = ").append(fp.getWarehouseID()).append(")");
        }
        if (fp.getSearchKey() != null && !"".equals(fp.getSearchKey())) {
            sql.append(" AND ((lower(st.transferName) like '").append(fp.getSqlSearchKey()).append("' )");
            sql.append(" OR (lower(st.number) like '").append(fp.getSqlSearchKey()).append("' ))");
        }
        if (fp.getStartDate() != null && fp.getEndDate() != null) {
            sql.append("AND st.date between '").append(fp.getStartDate()).append("' and '").append(fp.getEndDate()).append("' ");
        } else if (fp.getStartDate() != null) {
            sql.append("AND st.date >= '").append(fp.getStartDate()).append("' ");
        } else if (fp.getEndDate() != null) {
            sql.append("AND st.date <= '").append(fp.getEndDate()).append("' ");
        }
        return  ((BigInteger) findNativeSingle(sql.toString())).intValue();
    }

    @Override
    public Integer getStockTranferNumberInt() {
        return (Integer) findSingle("select max(intNumber) from EdsStockTransfer where (deleted is null or deleted <> true)");
    }

    @Override
    public boolean numberExists(String numberString, Integer objectId) {
        if (ServerUtils.isNullOrEmpty(numberString)) {
            return false;
        }
        final Map<String, Object> valueMap = Maps.newHashMap();
        StringBuilder sql = new StringBuilder();

        sql.append("select st.objectID from EdsStockTransfer st")
                .append("    where (st.deleted is null or st.deleted<>true)")
                .append("        and st.number = :numberString");
        valueMap.put("numberString", numberString);

        if (objectId != null) {
            sql.append("    and st.objectID <> :objectId");
            valueMap.put("objectId", objectId);
        }
        if (this.findByNamedParams(sql.toString(), valueMap).isEmpty()) {
            return false;
        } else {
            return true;
        }
    }

}
