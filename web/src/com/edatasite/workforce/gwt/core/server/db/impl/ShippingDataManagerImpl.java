package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.accounting.EdsShippingData;
import com.edatasite.workforce.core.domain.accounting.EdsShippingDataItem;
import com.edatasite.workforce.core.tools.StringUtil;
import com.edatasite.workforce.gwt.core.client.enums.ShippingDataStatus;
import com.edatasite.workforce.gwt.core.client.enums.ShippingDataType;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.rpc.solr.SolrReindexRpc;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.db.ShippingDataManager;
import com.edatasite.workforce.gwt.invoice.client.rpc.ShippingData;
import com.edatasite.workforce.gwt.invoice.client.rpc.ShippingDataItem;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;

import javax.persistence.TypedQuery;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * User: Murad Satimov
 * Date: 1/9/18 8:43 PM
 */
@Repository
public class ShippingDataManagerImpl extends BaseManager<EdsShippingData> implements ShippingDataManager {

    public ShippingDataManagerImpl() {
        super(EdsShippingData.class);
    }

    @Override
    public Integer getListingCount(ListingFilterParameter fp) {
        if (fp == null || fp.getEntityID() == null) {
            return 0;
        }
        final String sql = "select count(sd.objectID) from EdsShippingData sd" +
                "    where sd.quote.objectID=:quoteId" +
                "        and (sd.deleted is null or sd.deleted <> true)";
        String searchClause = "";

        if (!StringUtil.isEmpty(fp.getSqlSearchKey())) {
            searchClause = "        and (lower(sd.number) like :searchKey" +
                    "                or lower(sd.shippingLabel) like :searchKey)";
        }
        TypedQuery<Long> query = this.slaveEntityManager.createQuery(sql + searchClause, Long.class)
                .setParameter("quoteId", fp.getEntityID())
                .setMaxResults(1);

        if (!StringUtil.isEmpty(searchClause)) {
            query = query.setParameter("searchKey", fp.getSqlSearchKey());
        }
        final List<Long> result = query.getResultList();

        return result.isEmpty() ? 0 : result.get(0).intValue();
    }

    @Override
    public List<EdsShippingData> getList(ListingFilterParameter fp) {
        if (fp == null) {
            return Collections.emptyList();
        }
        String sql = "select sd from EdsShippingData sd " +
                "    left join sd.crmAccount ca " +
                "    where (sd.deleted is null or sd.deleted <> true) " +
                "        and sd.crmAccount = ca";

        if (fp.getEntityID() != null) {
            sql = sql + " and sd.quote.objectID=:quoteId ";
        }
        if (fp.isGdn() != null) {
            sql = sql + " and sd.shippingType =:shippingType";
        }
        if (fp.getStartDate() != null && fp.getEndDate() != null) {
            sql += " and (sd.shippingDate >= :startDate and sd.shippingDate <= :dueDate) ";
        } else if (fp.getStartDate() != null) {
            sql += " and (sd.shippingDate >= :startDate) ";
        } else if (fp.getEndDate() != null) {
            sql += " and (sd.shippingDate <= :dueDate) ";
        }

        String searchClause = "";
        String sortSql = " order by ";

        if (!StringUtil.isEmpty(fp.getSqlSearchKey())) {
            searchClause = "        and (lower(sd.number) like :searchKey" +
                    "               or lower(sd.shippingLabel) like :searchKey" +
                    "               or lower(sd.crmAccount.name) like :searchKey" +
                    "               or lower(sd.quote.number) like :searchKey)";
        }
        if (fp.getSortField() != null) {
            switch (fp.getSortField()) {
                case ShippingData.NUMBER -> sortSql += " sd.number ";
                case ShippingData.SHIPPING_LABEL -> sortSql += " sd.shippingLabel ";
                case ShippingData.DATE -> sortSql += " sd.shippingDate ";
                case ShippingData.SUPPLIER -> sortSql += " ca.name ";
                case ShippingData.AMOUNT -> sortSql += " sd.amount ";
                case ShippingData.ORDER_NUMBER -> sortSql += " sd.quote.number ";
                case ShippingData.STATUS -> sortSql += " sd.status ";
                case ShippingData.CREATOR -> sortSql += " sd.quote.number ";
                default -> sortSql += " sd.objectID ";
            }
            if (fp.getSortDir() != null) {
                if (Integer.valueOf(1).equals(fp.getSortDir())) {
                    sortSql += " asc ";
                } else {
                    sortSql += " desc ";
                }
            } else {
                sortSql += " desc ";
            }
        } else {
            sortSql += " sd.objectID desc ";
        }
        TypedQuery<EdsShippingData> query = this.slaveEntityManager.createQuery(sql + searchClause + sortSql, EdsShippingData.class);

        if (fp.getStart() != null && fp.getLimit() != null && fp.getLimit() > 0) {
            query.setMaxResults(fp.getLimit())
                    .setFirstResult(fp.getStart());
        }

        if (fp.getEntityID() != null) {
            query = query.setParameter("quoteId", fp.getEntityID());
        }
        if (!StringUtil.isEmpty(searchClause)) {
            query = query.setParameter("searchKey", fp.getSqlSearchKey());
        }
        if (fp.isGdn() != null) {
            query = query.setParameter("shippingType", fp.isGdn() ? ShippingDataType.OUT : ShippingDataType.IN);
        }
        if (fp.getStartDate() != null) {
            query.setParameter("startDate", fp.getStartDate());
        }
        if (fp.getEndDate() != null) {
            query.setParameter("dueDate", fp.getEndDate());
        }
        return query.getResultList();
    }

    @Override
    public Integer getGrnGdnRelatedInvoiceNumber(Integer shippingDataId) {
        if (shippingDataId == null) {
            return null;
        }
        final String sql = "SELECT i.id from " + getCompanyId() + ".converted_shipping_data ci " +
                "    JOIN " + getCompanyId() + ".invoice i ON ci.invoice_id = i.id " +
                " WHERE (i.deleted is null or i.deleted <> true) " +
                " AND ci.shipping_data_id = :shippingDataId";
        final List<Integer> list = this.masterEntityManager.createNativeQuery(sql)
                .setParameter("shippingDataId", shippingDataId)
                .setMaxResults(1)
                .getResultList();

        return list.isEmpty() ? null : list.get(0);
    }

    @Override
    public List<EdsShippingData> getGrnGdnsByInvoiceId(Integer invoiceId) {
        if (invoiceId == null) {
            return Collections.emptyList();
        }
        final String sql = "SELECT sh.* from " + getCompanyId() + ".converted_shipping_data ci " +
                "    JOIN " + getCompanyId() + ".shipping_data sh ON ci.shipping_data_id = sh.id " +
                " WHERE (sh.deleted is null or sh.deleted <> true) " +
                " AND ci.invoice_id =" + invoiceId;

        return (List<EdsShippingData>) findNative(sql, EdsShippingData.class);
    }

    @Override
    public List<EdsShippingData> getNotConvertedGDNs(Integer orderId) {
        if (orderId == null) return Collections.emptyList();
        StringBuilder query = new StringBuilder();
        query.append("SELECT * FROM ").append(getCompanyId()).append(".shipping_data ");
        query.append("WHERE quoteid = ").append(orderId).append(" and shippingtype ='OUT' and status is null and deleted is not true;");
        return (List<EdsShippingData>) findNative(query.toString(), EdsShippingData.class);
    }


    @Override
    public Integer getLastIntNumberData(ShippingDataType shippingDataType) {
        if (shippingDataType == null) {
            shippingDataType = ShippingDataType.IN;
        }
        String sql = "select sd.intNumber from EdsShippingData sd " +
                "    where sd.intNumber is not null " +
                "          and (sd.deleted is null or sd.deleted <> true)" +
                "          and (";

        if (shippingDataType == ShippingDataType.IN) {
            sql += "        sd.shippingType is null or ";
        }
        sql += " sd.shippingType=:shippingDataType)";
        sql += "    order by sd.intNumber desc";

        final List<Integer> result = this.slaveEntityManager.createQuery(sql, Integer.class)
                .setParameter("shippingDataType", shippingDataType)
                .setMaxResults(1)
                .getResultList();

        return result.isEmpty() ? null : result.get(0);
    }

    @Override
    public Long countShippingDataByQuoteIdAndType(Integer quoteId, ShippingDataType shippingType) {
        if (quoteId == null || shippingType == null) {
            return 0L;
        }
        final String sql = "select count(sh.objectID) from EdsShippingData sh" +
                "    where sh.quote.objectID =:quoteId" +
                "        and sh.shippingType = :shippingType" +
                "        and (sh.deleted is null or sh.deleted <> true )";
        final List<Long> list = this.slaveEntityManager.createQuery(sql, Long.class)
                .setParameter("quoteId", quoteId)
                .setParameter("shippingType", shippingType)
                .setMaxResults(1)
                .getResultList();

        return list.isEmpty() ? 0L : list.get(0);
    }

    @Override
    public List<EdsShippingData> getByQuoteId(Integer quoteId) {
        if (quoteId == null) {
            return Collections.emptyList();
        }
        final String sql = "select sh from EdsShippingData sh " +
                "    where (sh.deleted is null or sh.deleted <> true)" +
                "   and sh.quote.objectID = :quoteId";
        return this.slaveEntityManager.createQuery(sql)
                .setParameter("quoteId", quoteId)
                .getResultList();
    }

    @Override
    public boolean hasGDNInSalesOrder(Integer salesOrderId) {
        StringBuilder query = new StringBuilder();
        query.append(" SELECT COUNT(id) FROM ").append(getCompanyId()).append(".shipping_data ")
                .append(" WHERE quoteid = ").append(salesOrderId)
                .append(" AND shippingtype = 'OUT' AND deleted IS NOT TRUE ");

        BigInteger count = (BigInteger) this.slaveEntityManager.createNativeQuery(query.toString()).getSingleResult();
        return count == null ? false : count.intValue() > 0;
    }

    @Override
    public boolean hasGRNInPurchaseOrder(Integer purchaseOrderId) {
        if (purchaseOrderId == null) return false;
        StringBuilder query = new StringBuilder();
        query.append(" SELECT COUNT(id) FROM ").append(getCompanyId()).append(".shipping_data ")
                .append(" WHERE quoteid = ").append(purchaseOrderId)
                .append(" AND shippingtype = 'IN' AND deleted IS NOT TRUE ");
        BigInteger count = (BigInteger) this.slaveEntityManager.createNativeQuery(query.toString()).getSingleResult();
        return count == null ? false : count.intValue() > 0;
    }

    @Override
    public List<Integer> getAllInvoicesByShippingDataIds(List<Integer> shippingDataIds, String status) {
        if (shippingDataIds == null) {
            return null;
        }
        final String sql = "SELECT i.id from " + getCompanyId() + ".converted_shipping_data ci " +
                " JOIN " + getCompanyId() + ".invoice i ON ci.invoice_id = i.id " +
                " JOIN " + getCompanyId() + ".reference r ON r.id = i.status_id " +
                " WHERE (i.deleted is null or i.deleted <> true) " +
                " AND ci.shipping_data_id in (:shippingDataId)" +
                " and (r.code is null or r.code = :status)";
        return this.slaveEntityManager.createNativeQuery(sql)
                .setParameter("shippingDataId", shippingDataIds)
                .setParameter("status", status)
                .getResultList();
    }

    @Override
    public List<EdsShippingData> getShippingDataQuoteIds(List<Integer> quoteIds) {
        if (quoteIds == null || quoteIds.isEmpty()) {
            return Collections.emptyList();
        }
        final String sql = "select s from EdsShippingData s " +
                "    where (s.deleted is null or s.deleted <> true)" +
                "        and s.quote.objectID in (" + ServerUtils.getAsCommoDelimited(quoteIds, "0", ",") + ")" +
                "        and (s.status is null or s.status != :converted)";
        return this.masterEntityManager.createQuery(sql)
                //.setParameter("quoteIds", quoteIds) //TODO 40K+ items list can not be handleted
                .setParameter("converted", ShippingDataStatus.CONVERTED)
                .getResultList();
    }

    @Override
    public List<EdsShippingData> getShippingDataList(ListingFilterParameter fp) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT sd.*");
        sql.append(getBaseSql(fp));
        sql.append(" ORDER BY ");
        if (StringUtils.isNotBlank(fp.getSortField())) {
            if (ShippingData.ORDER_NUMBER.equals(fp.getSortField())) {
                sql.append("q.number");
            } else if (ShippingData.NUMBER.equals(fp.getSortField())) {
                sql.append("sd.number");
            } else if (ShippingData.STATUS.equals(fp.getSortField())) {
                sql.append("sd.status");
            } else if (ShippingData.INVOICE_NUMBER.equals(fp.getSortField())) {
                sql.append("inv.number");
            } else if (ShippingData.INVOICE_STATUS.equals(fp.getSortField())) {
                sql.append("ref.name");
            } else if (ShippingData.SUPPLIER.equals(fp.getSortField())) {
                sql.append("ca.name");
            } else if (ShippingData.DATE.equals(fp.getSortField())) {
                sql.append("sd.createddate");
            } else {
                sql.append("sd.id");
            }
            if (fp.getSortDir() != null) {
                if (Integer.valueOf(1).equals(fp.getSortDir())) {
                    sql.append(" DESC");
                } else {
                    sql.append(" ASC");
                }
            } else {
                sql.append(" DESC");
            }
        } else {
            sql.append(" sd.id DESC");
        }
        if (fp.getLimit() > 0) {
            sql.append(" LIMIT ").append(fp.getLimit());
        }
        if (fp.getStart() > 0) {
            sql.append(" OFFSET ").append(fp.getStart());
        }
        return (List<EdsShippingData>) findNative(sql.toString(), EdsShippingData.class);
    }

    private StringBuilder getBaseSql(ListingFilterParameter fp) {
        StringBuilder sql = new StringBuilder();
        sql.append(" FROM ").append(getCompanyId()).append(".shipping_data sd");
        sql.append(" JOIN ").append(getCompanyId()).append(".quote q ON q.id = sd.quoteid");
        sql.append(" LEFT JOIN ").append(getCompanyId()).append(".crmAccount ca ON ca.id = sd.crmaccountid");
        sql.append(" LEFT JOIN ").append(getCompanyId()).append(".converted_shipping_data csd ON csd.shipping_data_id = sd.id");
        sql.append(" LEFT JOIN ").append(getCompanyId()).append(".invoice inv ON csd.invoice_id = inv.id");
        if (StringUtils.isNotBlank(fp.getSortField()) && ShippingData.INVOICE_STATUS.equals(fp.getSortField())) {
            sql.append(" LEFT JOIN ").append(getCompanyId()).append(".reference ref ON csd.invoice_id = inv.status_id");
        }

        sql.append(" WHERE ").append(ServerUtils.checkForDeleted("sd.deleted")).append(" AND ").append(ServerUtils.checkForDeleted("q.deleted"));

        sql.append("AND sd.shippingtype = '").append(fp.isGdn() ? ShippingDataType.OUT : ShippingDataType.IN).append("'");
        if (StringUtils.isNotBlank(fp.getSqlSearchKey())) {
            String searchKey = fp.getSqlSearchKey().toLowerCase();
            sql.append(" AND (lower(sd.number) LIKE '").append(searchKey).append("'")
                    .append(" OR lower(ca.name) LIKE '").append(searchKey).append("'")
                    .append(" OR lower(inv.number) LIKE '").append(searchKey).append("'")
                    .append(" OR lower(q.number) LIKE '").append(searchKey).append("')");
        }
        if (fp.getClientId() != null) {
            sql.append(" AND ca.id = ").append(fp.getClientId()).append(" ");
        }
        if (StringUtils.isNotBlank(fp.getStartDateNC()) && StringUtils.isNotBlank(fp.getEndDateNC())) {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            sql.append(" AND sd.shippingDate between '").append(format.format(ServerUtils.parseFilterParameterDate(fp.getStartDateNC())))
                    .append("' AND '").append(format.format(ServerUtils.parseFilterParameterDate(fp.getEndDateNC()))).append("' ");
        }
        return sql;
    }

    @Override
    public Integer getShippingDataCount(ListingFilterParameter fp) {
        StringBuilder sql = new StringBuilder();
        sql.append("select count(sdata.*) from (SELECT sd.* ");
        sql.append(getBaseSql(fp));
        sql.append(") sdata ");
        Object totalCount = findNativeSingle(sql.toString());
        return totalCount == null ? 0 : ((BigInteger) totalCount).intValue();
    }


    public List<Integer> getShippingDataIdsByIds(String ids) {
        return find("SELECT er.objectID FROM EdsShippingData er WHERE er.objectID IN(" + ids + ") and " + ServerUtils.checkForDeleted("er.deleted"));
    }

    public Boolean getShippingDataByNumber(String number) {
        EdsShippingData edsShippingData = (EdsShippingData) findSingle("select sd from EdsShippingData sd where sd.number = ? and " + ServerUtils.checkForDeleted("sd.deleted"), number);

        return edsShippingData != null;
    }

    public List<Integer> getShippingDataIdsWithLimit(Integer startat, Integer limit) {
        return findInterval("select s.objectID from EdsShippingData s where " + ServerUtils.checkForDeleted("s.deleted"), startat, limit);
    }

    @Override
    public List<EdsShippingData> getShippingDataListForSolr(SolrReindexRpc solrReindex, Integer start, Integer limit) {
        Map<String, Object> params = new HashMap<>();

        StringBuilder sql = new StringBuilder();
        sql.append("select s from EdsShippingData s ");
        sql.append(" where  " + ServerUtils.checkForDeleted("s.deleted"));
        if (!solrReindex.isAllReindex() && solrReindex.getLastUpdateTime() != null) {
            params.put("updatedDate", solrReindex.getLastUpdateTime());
            sql.append(" and s.lastUpdateTime >= :updatedDate");
            if (solrReindex.getLastUpdateEndTime() != null) {
                sql.append(" and s.lastUpdateTime<='").append(solrReindex.getLastUpdateEndTime()).append("'");
            }
        }
        sql.append(" order by s.objectID ");

        return findIntervalByNamedParams(sql.toString(), start, limit, params);
    }

    @Override
    public BigDecimal getUnallocatedAmount(Integer shippinDataId, Integer purchaseOrderId) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT ")
                .append("COALESCE(expenses.total_expense, 0.0) - COALESCE(allocs.total_allocated, 0.0) AS unAllocatedAmount ")
                .append("FROM ( ")
                .append("   SELECT SUM(exp.subtotal) AS total_expense ")
                .append("   FROM ").append(getCompanyId()).append(".expense exp ")
                .append("   LEFT JOIN ").append(getCompanyId()).append(".expenseReport r ON r.id = exp.reportId ")
                .append("   WHERE  (r.isDeleted IS NOT TRUE or exp.isDeleted is null) ")
                .append("    and  r.purchaseOrderID = ").append(purchaseOrderId).append(" ")
                .append(") expenses, ")
                .append("( ")
                .append("   SELECT SUM(items.receivedAllocation) AS total_allocated ")
                .append("   FROM ").append(getCompanyId()).append(".shipping_data_items items ")
                .append("   JOIN ").append(getCompanyId()).append(".shipping_data shd ON shd.id = items.shippingDataId ")
                .append("   JOIN ").append(getCompanyId()).append(".quote q ON q.id = shd.quoteId ")
                .append("   WHERE q.deleted IS NOT TRUE ")
                .append("     AND shd.deleted IS NOT TRUE ")
                .append("     AND q.id = ").append(purchaseOrderId).append(" ")
                .append("     AND shd.id != ").append(shippinDataId).append(" ")
                .append(") allocs");

        BigDecimal total = (BigDecimal) findNativeSingle(sql.toString());
        return total != null ? total : BigDecimal.ZERO;
    }

    @Override
    public BigDecimal getTotalAllocatedAmount(Integer purchaseOrderId) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT SUM(COALESCE(item.receivedAllocation, 0.0)) FROM ").append(getCompanyId()).append(".shipping_data_items item \n")
                .append("JOIN ").append(getCompanyId()).append(".shipping_data shd ON shd.id = item.shippingDataId \n")
                .append("JOIN ").append(getCompanyId()).append(".quote q on q.id = shd.quoteId \n");
        sql.append("WHERE q.deleted is not true AND shd.deleted is not true AND q.id = ").append(purchaseOrderId).append(" \n");

        BigDecimal total = (BigDecimal) findNativeSingle(sql.toString());
        return total != null ? total : BigDecimal.ZERO;
    }

    @Override
    public Map<Integer, ShippingDataItem> getQuoteItemIdsUsedInGrnOrGdn(Integer quoteId) {
        List<EdsShippingDataItem> items = (List<EdsShippingDataItem>) find("select i from EdsShippingDataItem i where i.shippingData.quote.objectID = " + quoteId + " and " + ServerUtils.checkForDeleted("i.deleted"));
        Map<Integer, ShippingDataItem> result = null;
        if (items != null && !items.isEmpty()) {
            result = new HashMap<>();
            for (EdsShippingDataItem item : items) {
                ShippingDataItem dataItem = item.toTO();
                if (result.get(item.getQuoteItemId()) != null) {
                    ShippingDataItem existing = result.get(item.getQuoteItemId());
                    existing.setAmount(existing.getAmount().add(dataItem.getAmount()));
                    existing.setNet(existing.getNet().add(dataItem.getNet()));
                    result.put(item.getQuoteItemId(), existing);
                } else {
                    result.put(item.getQuoteItemId(), dataItem);
                }
            }
        }
        return result;
    }

    @Override
    public Date getLatestGrnDate(Integer quoteId) {
        return (Date) findNativeSingle("select max(i.shippingDate) from " + getCompanyId() + ".shipping_data i where i.quoteId =" + quoteId + " and " + ServerUtils.checkForDeleted("i.deleted"));
    }

    public List<Integer> getCompanyDeletedShippingDatasForSolr(SolrReindexRpc solrReindex) {
        StringBuilder newsSqlQuery = new StringBuilder("SELECT ns.objectID FROM EdsShippingData ns WHERE ns.deleted=true");
        newsSqlQuery.append(" AND ns.lastUpdateTime>=").append("'").append(solrReindex.getLastUpdateTime()).append("'");
        if (solrReindex.getLastUpdateEndTime() != null) {
            newsSqlQuery.append(" and ns.lastUpdateTime<='").append(solrReindex.getLastUpdateEndTime()).append("'");
        }
        return (List<Integer>) find(newsSqlQuery.toString());
    }
}
