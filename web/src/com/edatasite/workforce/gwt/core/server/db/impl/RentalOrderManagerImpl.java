package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.EdsRentalOrder;
import com.edatasite.workforce.gwt.accounting.client.rpc.RentalOrderData;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.db.RentalOrderManager;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.List;


@Repository("rentalOrderManager")
public class RentalOrderManagerImpl extends BaseManager<EdsRentalOrder> implements RentalOrderManager {

    public RentalOrderManagerImpl() {
        super(EdsRentalOrder.class);
    }

    @Override
    public Integer getOrderLastIntNumber() {
        return (Integer) findSingle("select p.intNumber from EdsRentalOrder p where p.deleted=false and p.intNumber is not null order by p.intNumber desc");
    }

    @Override
    public List<EdsRentalOrder> getRentalOrderList(ListingFilterParameter fp) {
        StringBuilder sql = new StringBuilder(" SELECT distinct rent.* ");
        sql = sql.append(baseQuery(fp));

        if (fp.getSortField() != null) {
            sql.append(" order by ");
            if (RentalOrderData.NUMBER.equals(fp.getSortField())) {
                sql.append("rent.number ");
            } else if (RentalOrderData.CUSTOMER.equals(fp.getSortField())) {
                sql.append(" rent.customer_id ");
            } else if (RentalOrderData.EXPIRATION.equals(fp.getSortField())) {
                sql.append(" rent.expiration ");
            } else {
                sql.append(" rent.id ");
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
            sql.append("order by rent.id desc");
        }
        if (fp.getLimit() > 0) {
            sql.append(" OFFSET ").append(fp.getStart()).append(" LIMIT ").append(fp.getLimit());
        }

        return findNative(sql.toString(), EdsRentalOrder.class);
    }

    @Override
    public Integer getRentalOrderCount(ListingFilterParameter fp) {
        StringBuilder sql = new StringBuilder(" SELECT count(rent.id) ");
        sql = sql.append(baseQuery(fp));

        BigInteger totalCount = (BigInteger) findNativeSingle(sql.toString());
        return totalCount != null ? totalCount.intValue() : 0;
    }

    private StringBuilder baseQuery(ListingFilterParameter fp) {
        StringBuilder sql = new StringBuilder();
        String companyID = getCompanyId();
        sql.append(" from ").append(companyID).append(".rental_order rent \n");
        sql.append("left join ").append(companyID).append(".crmAccount client on client.id = rent.customer_id \n");
        sql.append("left join ").append(companyID).append(".rental_order_item rental_order_item on rental_order_item.rental_order_id = rent.id \n");
        sql.append("where ").append(ServerUtils.checkForDeleted("rent.deleted "));

        if (fp.getProductId() != null) {
            sql.append(" and rental_order_item.item_id = " + fp.getProductId());
        }
        if (fp.getSqlSearchKey() != null) {
            sql.append(" and (");
            sql.append(" lower(rent.number) like '").append(fp.getSqlSearchKey()).append("' ");
            sql.append(") ");
        }
        if (fp.getObjectsIds() != null) {
            sql.append(" and (");
            sql.append("rent.id in (" + fp.getObjectsIds() + ")");
        }
        return sql;
    }

    @Override
    public void deleteRentalOrderItems(Integer rentOrderID) {
        update("delete from EdsRentalOrderItem where rentalOrder.objectID = ?", rentOrderID);
    }

    @Override
    public boolean isRentOrderNumberExist(String number, Integer objectID) {
        List numberList;
        if (objectID != null) {
            numberList = find("select rent.intNumber from EdsRentalOrder rent where " + ServerUtils.checkForDeleted("rent.deleted") + " and rent.number= ? and rent.objectID <>? ", number, objectID);
        } else {
            numberList = find("select rent.intNumber from EdsRentalOrder rent where " + ServerUtils.checkForDeleted("rent.deleted") + " and rent.number= ?", number);
        }
        return numberList != null && numberList.size() > 0;
    }
}