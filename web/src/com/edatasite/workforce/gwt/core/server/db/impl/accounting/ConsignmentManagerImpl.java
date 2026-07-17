package com.edatasite.workforce.gwt.core.server.db.impl.accounting;

import com.edatasite.workforce.core.domain.accounting.EdsConsignment;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.db.accounting.ConsignmentManager;
import com.edatasite.workforce.gwt.core.server.db.impl.BaseManager;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by Normurod on 6/15/15.
 */
@Repository("consignmentManager")
public class ConsignmentManagerImpl extends BaseManager<EdsConsignment> implements ConsignmentManager {

    public ConsignmentManagerImpl() {
        super(EdsConsignment.class);
    }

    @Override
    public List<EdsConsignment> list(ListingFilterParameter filterParameter) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT c FROM EdsConsignment c ");
        sql.append("WHERE c.deleted = false ");
        if (filterParameter.getSqlSearchKey() != null) {
            sql.append("AND (lower(c.name) like '" + filterParameter.getSqlSearchKey() + "' OR ");
            sql.append("lower(c.number) like '" + filterParameter.getSqlSearchKey() + "' OR ");
            sql.append("lower(c.reference) like '" + filterParameter.getSqlSearchKey() + "' ) ");
        }
        if (filterParameter.getSortField() != null && !"".equals(filterParameter.getSortField())) {
            if ("name".equalsIgnoreCase(filterParameter.getSortField())) {
                sql.append("ORDER BY c.name ");
            } else if ("number".equalsIgnoreCase(filterParameter.getSortField())) {
                sql.append("ORDER BY c.number ");
            } else if ("date".equalsIgnoreCase(filterParameter.getSortField())) {
                sql.append("ORDER BY c.date ");
            } else if ("reference".equalsIgnoreCase(filterParameter.getSortField())) {
                sql.append("ORDER BY c.reference ");
            }

            if(!filterParameter.isAscending()){
                sql.append(" DESC ");
            }
        } else {
            sql.append("ORDER BY c.objectID DESC ");
        }

        return findInterval(sql.toString(),filterParameter.getStart(),filterParameter.getLimit());
    }

    @Override
    public Integer listCount(ListingFilterParameter fp) {
        if (fp == null) {
            fp = new ListingFilterParameter();
        }

        StringBuilder sql = new StringBuilder();
        sql.append("SELECT count(c) FROM EdsConsignment c ");
        sql.append("WHERE c.deleted = false ");
        if (fp.getSqlSearchKey() != null) {
            sql.append("AND (lower(c.name) like '" + fp.getSqlSearchKey() + "' OR ");
            sql.append("lower(c.number) like '" + fp.getSqlSearchKey() + "' OR ");
            sql.append("lower(c.reference) like '" + fp.getSqlSearchKey() + "') ");
        }

        Long count = (Long)findSingle(sql.toString());
        return count != null ? count.intValue() : 0;
    }

    @Override
    public Integer getLastInNumber() {
        return (Integer)findSingle("select max(c.intNumber) from EdsConsignment c where c.deleted is false");
    }

    @Override
    public EdsConsignment getConsignmentBySubsidiaryUniqNum(String subsidiaryUniqNum) {
        return (EdsConsignment)findSingle("select c from EdsConsignment c where c.subsidiaryUniqNum = ?", subsidiaryUniqNum);
    }

    @Override
    public void deleteConsignmentItems(Integer objectID) {
        updateNative("DELETE FROM " + getCompanyId() + ".consignmentitem where consignmentid = " + objectID + " ");
    }

    @Override
    public boolean isConsignmentNumberExists(String number, Integer objectID) {
        if (objectID != null) {
            return find("select c from EdsConsignment c where (c.deleted = false or c.deleted is null) and c.number = ? and c.objectID != ?", number.trim(), objectID).size() > 0;
        } else {
            return find("select c from EdsConsignment c where (c.deleted = false or c.deleted is null) and c.number = ?", number.trim()).size() > 0;
        }
    }

    @Override
    public BigDecimal getConsignmentQty(Integer clientID, Integer productID, Integer consignmentID) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT SUM(CASE WHEN ci.fromid = " + clientID + " THEN 0-ci.quantity ELSE ci.quantity END) FROM ").append(getCompanyId()).append(".consignmentitem ci \n");
        sql.append("INNER JOIN ").append(getCompanyId()).append(".consignment c on c.id = ci.consignmentid \n");
        sql.append("WHERE c.deleted is not true \n");
        sql.append("AND ci.productid = ").append(productID).append(" \n");
        sql.append("AND (ci.fromid = ").append(clientID).append(" OR ci.toid = ").append(clientID).append(") \n");

        if (consignmentID != null) {
            sql.append("AND c.id != ").append(consignmentID).append(" \n");
        }

        BigDecimal quantity = (BigDecimal)findNativeSingle(sql.toString());
        return quantity != null ? quantity : BigDecimal.ZERO;
    }

    @Override
    public BigDecimal getConsignmentQtyToSell(Integer toCompanyID, Integer productID, Integer invoiceID) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT sum(qty) FROM (\n");
        sql.append("SELECT SUM(CASE WHEN ci.fromCompanyID = " + toCompanyID + " THEN 0-ci.quantity ELSE ci.quantity END) qty FROM ").append(getCompanyId()).append(".consignmentitem ci \n");
        sql.append("INNER JOIN ").append(getCompanyId()).append(".consignment c on c.id = ci.consignmentid \n");
        sql.append("WHERE c.deleted is not true \n");
        sql.append("AND ci.productid = ").append(productID).append(" \n");
        sql.append("AND (ci.fromCompanyID = ").append(toCompanyID).append(" OR ci.toCompanyID = ").append(toCompanyID).append(") \n");

        sql.append("UNION ALL \n");
        sql.append("SELECT SUM(0 - ii.qty) qty FROM ").append(getCompanyId()).append(".invoiceitem ii \n");

        sql.append("INNER JOIN ").append(getCompanyId()).append(".invoice inv on inv.id = ii.invoice_id \n");
        sql.append("INNER JOIN ").append(getCompanyId()).append(".saleinvoice sinv on sinv.id = inv.id \n");
        sql.append("INNER JOIN ").append(getCompanyId()).append(".reference invs on invs.id = inv.status_id \n");
        sql.append("WHERE inv.deleted is not true and invs.code in ('APPROVE', 'OPEN', 'OVER_DUE', 'PAID') \n");
        sql.append("AND ii.item_id = ").append(productID).append(" \n");
        if (invoiceID != null) {
            sql.append(" AND inv.id != ").append(invoiceID);
        }
        sql.append(") t \n");

        BigDecimal quantity = (BigDecimal)findNativeSingle(sql.toString());
        return quantity != null ? quantity : BigDecimal.ZERO;
    }

    @Override
    public BigDecimal getConsignmentQtyToPurchase(Integer toCompanyID, Integer productID, Integer invoiceID) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT sum(qty) FROM (\n");
        sql.append("SELECT SUM(CASE WHEN ci.fromCompanyID = " + toCompanyID + " THEN 0-ci.quantity ELSE ci.quantity END) qty FROM ").append(getCompanyId()).append(".consignmentitem ci \n");
        sql.append("INNER JOIN ").append(getCompanyId()).append(".consignment c on c.id = ci.consignmentid \n");
        sql.append("WHERE c.deleted is not true \n");
        sql.append("AND ci.productid = ").append(productID).append(" \n");
        sql.append("AND (ci.fromCompanyID = ").append(toCompanyID).append(" OR ci.toCompanyID = ").append(toCompanyID).append(") \n");

        sql.append("UNION ALL \n");
        sql.append("SELECT SUM(0 - ii.qty) qty FROM ").append(getCompanyId()).append(".invoiceitem ii \n");
        sql.append("INNER JOIN ").append(getCompanyId()).append(".purchaseinvoice pinv on pinv.id = ii.invoice_id \n");
        sql.append("INNER JOIN ").append(getCompanyId()).append(".invoice inv on inv.id = pinv.id \n");
        sql.append("WHERE inv.deleted is not true \n");
        sql.append("AND ii.item_id = ").append(productID).append(" \n");
        if (invoiceID != null) {
            sql.append(" AND inv.id != ").append(invoiceID);
        }
        sql.append(") t \n");

        BigDecimal quantity = (BigDecimal)findNativeSingle(sql.toString());
        return quantity != null ? quantity : BigDecimal.ZERO;
    }

    @Override
    public BigDecimal getSoldQty(Integer clientID, Integer productID) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT SUM(coalesce(ii.qty, 0)) FROM ").append(getCompanyId()).append(".invoiceitem ii \n");
        sql.append("INNER JOIN ").append(getCompanyId()).append(".invoice inv on inv.id = ii.invoice_id \n");
        sql.append("INNER JOIN ").append(getCompanyId()).append(".saleinvoice sinv on sinv.id = inv.id \n");
        sql.append("INNER JOIN ").append(getCompanyId()).append(".reference invs on invs.id = inv.status_id \n");
        sql.append("WHERE inv.deleted is not true and invs.code in ('APPROVE', 'OPEN', 'OVER_DUE', 'PAID') \n");
        sql.append("AND ii.item_id = ").append(productID).append(" \n");
        sql.append("AND sinv.client_id = ").append(clientID).append(" \n");

        BigDecimal quantity = (BigDecimal)findNativeSingle(sql.toString());
        return quantity != null ? quantity : BigDecimal.ZERO;
    }

    @Override
    public BigDecimal getSoldQty(List<Integer> clientIds, Integer productID) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT SUM(coalesce(ii.qty, 0)) FROM ").append(getCompanyId()).append(".invoiceitem ii \n");
        sql.append("INNER JOIN ").append(getCompanyId()).append(".invoice inv on inv.id = ii.invoice_id \n");
        sql.append("INNER JOIN ").append(getCompanyId()).append(".saleinvoice sinv on sinv.id = inv.id \n");
        sql.append("INNER JOIN ").append(getCompanyId()).append(".reference invs on invs.id = inv.status_id \n");
        sql.append("WHERE inv.deleted is not true and invs.code in ('APPROVE', 'OPEN', 'OVER_DUE', 'PAID') \n");
        sql.append("AND ii.item_id = ").append(productID).append(" \n");
        sql.append("AND sinv.client_id in (").append(ServerUtils.getAsCommoDelimited(clientIds, "0", ",")).append(") \n");

        BigDecimal quantity = (BigDecimal) findNativeSingle(sql.toString());
        return quantity != null ? quantity : BigDecimal.ZERO;
    }

    @Override
    public List<Integer> getClientListByProduct(Integer productID) {
        StringBuilder sql = new StringBuilder();

        sql.append("select distinct ci.toid from ").append(getCompanyId()).append(".consignmentitem ci \n");
        sql.append("inner join ").append(getCompanyId()).append(".consignment c on c.id = ci.consignmentid \n");
        sql.append("where c.deleted is not true \n");
        sql.append("AND ci.productid = ").append(productID).append(" \n");
        return (List<Integer>) findNative(sql.toString());
    }
}
