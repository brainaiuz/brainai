package com.edatasite.workforce.gwt.core.server.db.impl.accounting;

import com.edatasite.workforce.core.domain.accounting.EdsProductWarehouseLocation;
import com.edatasite.workforce.gwt.accounting.client.rpc.ProductLocationItem;
import com.edatasite.workforce.gwt.accounting.client.ui.AccountingConstants;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.db.accounting.ProductWarehouseLocationManager;
import com.edatasite.workforce.gwt.core.server.db.impl.BaseManager;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * User: Anvar Akramov
 * Date: Apr 15, 2010
 * Time: 1:57:11 AM
 */
@Repository("productWarehouseLocationManager")
public class ProductWarehouseLocationManagerImpl extends BaseManager<EdsProductWarehouseLocation> implements ProductWarehouseLocationManager {

    public ProductWarehouseLocationManagerImpl() {
        super(EdsProductWarehouseLocation.class);
    }

    public List<Object> getLocationsByWarehouseID(Integer warehouseID, ListingFilterParameter filterParametrs) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT distinct i.id, i.name, i.product_number,  ");
        sql.append(" SUM(CASE WHEN isl.transaction_code = '").append(Constants.TC_OUT).append("' THEN (0 - isl.quantity) ELSE coalesce(isl.quantity,0.00) END) as qty ");
        sql.append(" ,i.description, i.pictureid ");
        sql.append(" FROM ").append(getCompanyId()).append(".item i ");
        sql.append(" LEFT JOIN (select ist.* from ").append(getCompanyId()).append(".item_stock ist ");
        sql.append(" join ").append(getCompanyId()).append(".transaction t on ist.transactionid=t.id where t.deleted is not true) isl on isl.item_id = i.id ");

        sql.append(" WHERE ").append("isl.warehouseid =").append(warehouseID);
        sql.append(" AND (i.type =").append(AccountingConstants.INVENTORY_ITEM).append(" OR i.type =").append(AccountingConstants.RENTAL_ITEM).append(" OR i.type =").append(AccountingConstants.ASSEMBLY_ITEM).append(") ");
        if (!ServerUtils.isNullOrEmpty(filterParametrs.getSearchKey())) {
            if (filterParametrs.isFromMobile()) {
                sql.append(" AND (lower(i.upcNumber) like lower('%").append(filterParametrs.getSearchKey()).append("%') ");
                sql.append(" OR lower(i.name) like lower('%").append(filterParametrs.getSearchKey()).append("%') ");
                sql.append(" OR lower(i.product_number) like lower('%").append(filterParametrs.getSearchKey()).append("%') ");
                sql.append(")");
            } else {
                sql.append(" AND (lower(i.name) like lower('%").append(filterParametrs.getSearchKey()).append("%') OR lower(i.product_number) like lower('%").append(filterParametrs.getSearchKey()).append("%'))");
            }
        }
        String grouping = " group by i.id ";

        String regExp = "^.+__.+__(\\d+)$";
        if (filterParametrs.getColOper() != null && filterParametrs.getColOper().matches(regExp)) {
            String[] split = filterParametrs.getColOper().split("__");
            String column = split[0];
            String oper = split[1];
            String number = split[2];
            if (ProductLocationItem.QTY.equals(column)) {
                sql.append(grouping);
                String selectColumn = " having SUM(CASE WHEN isl.transaction_code = '" + Constants.TC_OUT + "' THEN (0 - isl.quantity) ELSE coalesce(isl.quantity,0.00) END)";
                sql.append(selectColumn).append(" ").append(oper).append(" ").append(number).append(" ");
            } else {
                sql.append(grouping); //no column matches
            }
        } else {
            sql.append(grouping); // DO NOT Miss the grouping
        }

        if (filterParametrs.getSortField() != null) {
            String ascOrDesc = filterParametrs.isAscending() ? " " : " desc";
            sql.append(" order by ");
            if (ProductLocationItem.NAME.equals(filterParametrs.getSortField())) {
                sql.append(" i.name ");
            } else if (ProductLocationItem.NUMBER.equals(filterParametrs.getSortField())) {
                sql.append(" i.product_number ");
            } else if (ProductLocationItem.QTY.equals(filterParametrs.getSortField())) {
                sql.append(" qty ");
            } else {
                sql.append(" i.id ");
            }
            sql.append(" ").append(ascOrDesc).append(" ");
        }

        return findNative(sql.toString());
    }

    public List<EdsProductWarehouseLocation> getProductWarehouseLocations(ListingFilterParameter filterParameter) {
        boolean hasSearch = StringUtils.isNotEmpty(filterParameter.getSearchKey());
        Map<String, Object> map = new HashMap<>();
        map.put("productID", filterParameter.getProductId());
        if (hasSearch) {
            map.put("searchKey", "%" + filterParameter.getSearchKey().toLowerCase() + "%");
        }

        StringBuilder sql = new StringBuilder();
        sql.append("SELECT l FROM EdsProductWarehouseLocation l ");
        sql.append(" left join l.warehouse.owners owners ");
        sql.append(" left join l.warehouse w  where l.product.objectID = :productID ");
//        if (StringUtils.isNotBlank(filterParameter.getViewType())) {
//            if (!ServerUtils.hasPermission(PermissionConstants.ACCOUNTING_WAREHOUSE_FULL_LIST_ACCESS)) {
//                if (ServerUtils.hasPermission(PermissionConstants.WAREHOUSE_SEE_OWN) && !getUser().hasRole(EdsRole.ADMIN_CODE)) {
//                    sql.append(" and owners.objectID =:ownerID ");
//                    map.put("ownerID", getUser().getObjectID());
//                }
//            }
//        }
        if (hasSearch) {
            sql.append("and lower(w.name) like :searchKey ");
        }

        return findByNamedParams(sql.toString(), map);
    }

    public void deleteProductLocations(Integer productID) {
        update("DELETE FROM EdsProductWarehouseLocation where product.objectID = ?", productID);
    }

}
