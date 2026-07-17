package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.EdsLocation;
import com.edatasite.workforce.core.domain.EdsRole;
import com.edatasite.workforce.core.domain.EdsStockAdjustment;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.gwt.accounting.client.ui.AccountingConstants;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.db.JdbcSpringManager;
import com.edatasite.workforce.gwt.core.server.db.StockAdjustmentManager;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: Normurod Buriev
 * Date: 11/23/11
 * Time: 3:22 PM
 * To change this template use File | Settings | File Templates.
 */
@Repository("stockAdjustmentManager")
public class StockAdjustmentManagerImpl extends BaseManager<EdsStockAdjustment> implements StockAdjustmentManager {

    @Autowired
    private JdbcSpringManager jdbcSpringManager;

    public StockAdjustmentManagerImpl() {
        super(EdsStockAdjustment.class);
    }

    @Override
    public List<EdsStockAdjustment> getList(ListingFilterParameter fp) {
        StringBuilder sql = new StringBuilder();
        String companyID = getCompanyId();
        if (fp.getViewType() == null) {
            fp.setViewType(AccountingConstants.STOCK_ADJUSTMENT_TYPE);
        }
        sql.append("select  sa.id objectID, sa.number,sa.date, sa.memo from " + companyID + ".stock_adjustment sa ");
        if ((fp.getProductId() != null)||(fp.getWarehouseID() != null)) {
            sql.append("left join " + companyID + ".adjustment_item ai on sa.id = ai.adjustment_id ");
        }
        sql.append("left join " + companyID + ".account ac on sa.account_id = ac.id ");
        sql.append("left join " + companyID + ".myuser u on u.id = sa.createdBy ");
        sql.append("where sa.deleted is not true and (sa.stockTransfer is null or sa.stockTransfer<>true) ");

        if (fp.getProductId() != null) {
            sql.append("and ai.item_id = " + fp.getProductId());
        }
        if (fp.getWarehouseID() != null) {
            sql.append("and ai.warehouseid = " + fp.getWarehouseID());
        }
        sql.append("and sa.type = '").append(fp.getViewType()).append("'");

        if (fp.getStartDate() != null && fp.getEndDate() != null) {
            sql.append("AND sa.date between '").append(fp.getStartDate()).append("' and '").append(fp.getEndDate()).append("' ");
        }
        if (fp.getStartDate() != null && fp.getEndDate() != null) {
            sql.append("AND sa.date between '").append(fp.getStartDate()).append("' and '").append(fp.getEndDate()).append("' ");
        } else if (fp.getStartDate() != null) {
            sql.append("AND sa.date >= '").append(fp.getStartDate()).append("' ");
        } else if (fp.getEndDate() != null) {
            sql.append("AND sa.date <= '").append(fp.getEndDate()).append("' ");
        }
        EdsUser currentUser = getUser();
        if (!currentUser.hasRole(EdsRole.ADMIN_CODE)) {
            EdsLocation location = currentUser.getLocation();
            if (ServerUtils.hasPermission(PermissionConstants.ACCOUNTING_STOCK_ADJUSTMENT_SEE_BY_LOCATION) && location != null) {
                sql.append(" AND u.locationId =").append(location.getObjectID());
            } else if (ServerUtils.hasPermission(PermissionConstants.ACCOUNTING_STOCK_ADJUSTMENT_SEE_OWN)) {
                sql.append(" AND sa.createdby =").append(currentUser.getObjectID());
            }
        }

        if (fp.getSearchKey() != null && !"".equals(fp.getSearchKey())) {
            sql.append(" AND (lower(sa.number) like '").append(fp.getSqlSearchKey() + "'");
            sql.append(" or lower(sa.memo) like '" + fp.getSqlSearchKey() + "'");
            sql.append(" or lower(sa.memo) like '" + fp.getSqlSearchKey() + "'");
            sql.append(" or lower(ac.name) like '" + fp.getSqlSearchKey() + "')");
        }
        if (fp.getSortField() != null) {
            sql.append(" order by ");
            if ("number".equals(fp.getSortField())) {
                sql.append(" sa.number ");
            } else if ("date".equals(fp.getSortField())) {
                sql.append(" sa.date ");
            } else if ("account".equals(fp.getSortField())) {
                sql.append(" ac.name ");
            } else if ("memo".equals(fp.getSortField())) {
                sql.append(" sa.memo ");
            } else {
                sql.append(" sa.id ");
            }
            if (fp.getSortDir() != null && fp.getSortDir() == 2) {
                sql.append(" desc ");
            }
        } else {
            sql.append(" order by sa.id desc ");
        }
        if (fp.getLimit() > 0) {
            sql.append(" OFFSET " + fp.getStart() + " LIMIT " + fp.getLimit() + " ");
        }
        List<Object[]> result = findNative(sql.toString());
        if (result != null && !result.isEmpty()) {
            ArrayList<EdsStockAdjustment> stockAdjustments = new ArrayList<>(result.size());
            for (Object[] item : result) {
                EdsStockAdjustment edsStockAdjustment = new EdsStockAdjustment();
                edsStockAdjustment.setObjectID((Integer) item[0]);
                edsStockAdjustment.setNumber((String) item[1]);
                edsStockAdjustment.setDate((Date) item[2]);
                edsStockAdjustment.setMemo((String) item[3]);
                stockAdjustments.add(edsStockAdjustment);
            }
            return stockAdjustments;
        }
        return new ArrayList<>();
    }

    @Override
    public Integer getTotalCount(ListingFilterParameter fp) {
        StringBuilder sql = new StringBuilder();
        String companyID = getCompanyId();
        sql.append("select count(distinct sa.id) from " + companyID + ".stock_adjustment sa ");
        sql.append("left join " + companyID + ".adjustment_item ai on sa.id = ai.adjustment_id ");
        sql.append("left join " + companyID + ".account ac on sa.account_id = ac.id ");
        sql.append("left join " + companyID + ".myuser u on u.id = sa.createdBy ");

        sql.append("where sa.deleted is not true and (sa.stockTransfer is null or sa.stockTransfer<>true) ");
        if (fp.getProductId() != null) {
            sql.append("and ai.item_id = " + fp.getProductId());
        }
        if (fp.getWarehouseID() != null) {
            sql.append("and ai.warehouseid = " + fp.getWarehouseID());
        }

        EdsUser currentUser = getUser();
        if (!currentUser.hasRole(EdsRole.ADMIN_CODE)) {
            EdsLocation location = currentUser.getLocation();

            if (ServerUtils.hasPermission(PermissionConstants.ACCOUNTING_STOCK_ADJUSTMENT_SEE_BY_LOCATION) && location != null) {
                sql.append(" AND u.locationId =").append(location.getObjectID());
            } else if (ServerUtils.hasPermission(PermissionConstants.ACCOUNTING_STOCK_ADJUSTMENT_SEE_OWN)) {
                sql.append(" AND sa.createdby =").append(currentUser.getObjectID());
            }
        }

        if (fp.getSearchKey() != null && !"".equals(fp.getSearchKey())) {
            sql.append(" AND (lower(sa.number) like '").append(fp.getSqlSearchKey() + "'");
            sql.append(" or lower(sa.memo) like '" + fp.getSqlSearchKey() + "'");
            sql.append(" or lower(sa.memo) like '" + fp.getSqlSearchKey() + "'");
            sql.append(" or lower(ac.name) like '" + fp.getSqlSearchKey() + "')");
        }
        return ((BigInteger) findNativeSingle(sql.toString())).intValue();
    }

    @Override
    public List<EdsStockAdjustment> getStockAdjustmentsByStockTransfer(Integer stockTransferId) {
        return getStockAdjustmentsByStockTransfer(stockTransferId, false);
    }

    @Override
    public List<EdsStockAdjustment> getStockAdjustmentsByStockTransfer(Integer stockTransferId, boolean evenDeleted) {
        return find("from EdsStockAdjustment sa where " + (evenDeleted ? "" : ServerUtils.checkForDeleted("sa.deleted") + " and ") + " sa.stockTrans.objectID = ? ", stockTransferId);
    }

    @Override
    public Integer getStockAdjustmentIntNumber() {
        return (Integer) findSingle("select max(intNumber) from EdsStockAdjustment where (deleted is null or deleted <> true)");
    }

    @Override
    public boolean numberExists(String numberString, Integer objectId) {
        if (ServerUtils.isNullOrEmpty(numberString)) {
            return false;
        }
        final Map<String, Object> valueMap = Maps.newHashMap();
        StringBuilder sql = new StringBuilder();

        sql.append("select sa.objectID from EdsStockAdjustment sa")
           .append("    where (sa.deleted is null or sa.deleted<>true)")
                .append("        and sa.number = :numberString");
        valueMap.put("numberString", numberString);

        if (objectId != null) {
            sql.append("    and sa.objectID <> :objectId");
            valueMap.put("objectId", objectId);
        }
        return !this.findByNamedParams(sql.toString(), valueMap).isEmpty();
    }

    @Override
    public EdsStockAdjustment getByNumber(String number) {
        if (StringUtils.isBlank(number)) {
            return null;
        }
        return (EdsStockAdjustment) findSingle("FROM EdsStockAdjustment sa where deleted <> true and number = ? ", number);
    }
}
