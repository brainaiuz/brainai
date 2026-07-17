package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.EdsPaymentMethod;
import com.edatasite.workforce.gwt.accounting.client.rpc.PaymentMethodItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.db.PaymentMethodManager;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("paymentMethodManager")
public class PaymentMethodManagerImpl extends BaseManager<EdsPaymentMethod> implements PaymentMethodManager {

    public PaymentMethodManagerImpl() {
        super(EdsPaymentMethod.class);
    }

    public List<EdsPaymentMethod> list() {
        String sql = "SELECT pm FROM EdsPaymentMethod pm where deleted is null or deleted<>true";
        return find(sql);
    }

    @Override
    public List<EdsPaymentMethod> getPaymentMethods(ListingFilterParameter fp) {
        String sql = "Select pm from EdsPaymentMethod pm where " + ServerUtils.checkForDeleted("pm.deleted");
        if (fp != null) {
            if (!StringUtils.isEmpty(fp.getSearchKey())) {
                sql += " and (lower(pm.name) like '" + fp.getSqlSearchKey() +
                       "'    or lower(pm.description) like '" + fp.getSqlSearchKey() + "') ";
            }
            if (fp.getSortField() != null) {
                if (fp.getSortField().equals(PaymentMethodItem.NAME)) {
                    sql = sql + " ORDER BY NAME ";
                } else if (fp.getSortField().equals(PaymentMethodItem.DESCRIPTION)) {
                    sql = sql + " ORDER BY DESCRIPTION ";
                } else {
                    sql = sql + " ORDER BY ID ";
                }
                if (fp.getSortDir() != null) {
                    if (fp.getSortDir() == 1) {
                        sql = sql + " DESC ";
                    } else {
                        sql = sql + " ASC ";
                    }
                } else {
                    sql = sql + " DESC ";
                }
            } else {
                sql = sql + " ORDER BY ID DESC";
            }
            return findInterval(sql, fp.getStart(), fp.getLimit());
        } else {
            return find(sql);

        }
    }

    public Integer getCount(ListingFilterParameter fp) {
        String sql = "select count(pm.id) from EdsPaymentMethod pm where " + ServerUtils.checkForDeleted("pm.deleted");
        if (!StringUtils.isEmpty(fp.getSearchKey())) {
            sql += " and (lower(pm.name) like '" + fp.getSqlSearchKey() +
                    "'    or lower(pm.description) like '" + fp.getSqlSearchKey() + "') ";
        }
        return Integer.valueOf(findSingle(sql).toString());
    }

    @Override
    public EdsPaymentMethod getByName(String methodName) {
        if (StringUtils.isBlank(methodName)) {
            return null;
        }

        return (EdsPaymentMethod) findSingle("FROM EdsPaymentMethod where lower(name) = '" + methodName.trim().toLowerCase() + "' and " + ServerUtils.checkForDeleted("deleted"));
    }
}
