package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.accounting.EdsInvoiceTerms;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.db.InvoiceTermsManager;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: 7/11/12
 * Time: 7:21 PM
 * To change this template use File | Settings | File Templates.
 */
@Repository("invoiceTermsManager")
public class InvoiceTermsManagerImpl extends BaseManager<EdsInvoiceTerms> implements InvoiceTermsManager {
    public InvoiceTermsManagerImpl() {
        super(EdsInvoiceTerms.class);
    }

    @Override
    public List<EdsInvoiceTerms> getInvoiceTerms(ListingFilterParameter fp) {
        StringBuilder sql = new StringBuilder();
        sql.append("select it from EdsInvoiceTerms it where ").append(ServerUtils.checkForDeleted("it.deleted"));

        String sqlSearchKey = fp.getSqlSearchKey();
        if (sqlSearchKey != null) {
            sql.append(" and lower(it.name) like '").append(sqlSearchKey).append("' ");
        }
        if (fp.getSortField() != null) {
            if ("name".equals(fp.getSortField())) {
                sql.append(" order by it.name ");
            } else if ("days".equals(fp.getSortField())) {
                sql.append(" order by it.days ");
            } else {
                sql.append(" order by it.objectID ");
            }
        } else {
            sql.append(" order by it.objectID ");
        }
        if (fp.getSortDir() != null) {
            if (fp.getSortDir() == 1) {
                sql.append(" desc ");
            } else {
                sql.append(" asc ");
            }
        } else {
            sql.append(" desc ");
        }

        return (List<EdsInvoiceTerms>) findInterval(sql.toString(), fp.getStart(), fp.getLimit());
    }

    @Override
    public EdsInvoiceTerms getTermsByName(String name) {
        if (StringUtils.isBlank(name)) {
            return null;
        }

        return (EdsInvoiceTerms) findSingle("FROM EdsInvoiceTerms where lower(name) = '" + name.trim().toLowerCase() + "' and " + ServerUtils.checkForDeleted("deleted"));
    }

    @Override
    public Integer getInvoiceTermsCount(ListingFilterParameter fp) {
        StringBuilder sql = new StringBuilder();
        sql.append("select count(it.objectID) from EdsInvoiceTerms it where " + ServerUtils.checkForDeleted("it.deleted"));
        String sqlSearchKey = fp.getSqlSearchKey();
        if (sqlSearchKey != null) {
            sql.append(" and lower(it.name) like '" + sqlSearchKey + "' ");
        }
        Long termsCount = (Long) findSingle(sql.toString());
        return termsCount != null ? termsCount.intValue() : 0;
    }

    @Override
    public Map<String, Integer> getAsMap() {
        Map<String, Integer> result = new HashMap<>();
        List<Object[]> queryResult = find("select t.name, t.objectID from EdsInvoiceTerms t where " + ServerUtils.checkForDeleted("t.deleted"));
        if (queryResult != null && queryResult.size() > 0) {
            for (Object[] item : queryResult) {
                if (item != null && item.length > 1 && item[0] != null && item[1] != null) {
                    result.put(item[0].toString().toLowerCase(), Integer.valueOf(item[1].toString()));
                }
            }
        }
        return result;
    }
}
