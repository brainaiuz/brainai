package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.EdsCompany;
import com.edatasite.workforce.core.domain.EdsInvoicingSettings;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.accounting.EdsBaseInvoiceItem;
import com.edatasite.workforce.core.domain.accounting.EdsPurchaseOrder;
import com.edatasite.workforce.core.domain.accounting.EdsQuote;
import com.edatasite.workforce.core.domain.accounting.EdsQuoteItem;
import com.edatasite.workforce.core.domain.accounting.EdsSaleQuote;
import com.edatasite.workforce.gwt.accounting.client.ui.AccountingConstants;
import com.edatasite.workforce.gwt.core.client.enums.GenericSettingsEnum;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.rpc.solr.SolrReindexRpc;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.ListLoadConfig;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.db.JdbcSpringManager;
import com.edatasite.workforce.gwt.core.server.db.QuoteManager;
import com.edatasite.workforce.gwt.core.server.db.ReferenceManager;
import com.edatasite.workforce.gwt.core.server.db.RoleManager;
import com.edatasite.workforce.gwt.core.server.db.settings.GenericSettingsManager;
import com.edatasite.workforce.gwt.invoice.client.rpc.enums.QIGroupingField;
import com.edatasite.workforce.gwt.invoice.client.ui.view.saleorderbaseinvoice.SOBaseInvoiceGroups;
import com.google.gwt.user.server.rpc.security.ServerSecurityContext;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * User: Anvarbek
 * Date: 07.04.2009
 * Time: 18:53:05
 */
@Repository("quoteManager")
public class QuoteManagerImpl extends BaseManager<EdsQuote> implements QuoteManager, AccountingConstants, Constants {
    @Autowired
    private RoleManager roleManager;

    @Autowired
    private ReferenceManager referenceManager;

    @Autowired
    private GenericSettingsManager genericSettingsManager;

    @Autowired
    private JdbcSpringManager jdbcSpringManager;

    public QuoteManagerImpl() {
        super(EdsQuote.class);
    }

    public Long getCountSaleQuoteList(boolean isSalesQuote) {
        StringBuilder sql = new StringBuilder();
        String saleOrderStatus = "'SALE_ORDER', 'PICKED', 'PACKED', 'SHIPPED'";
        sql.append("SELECT COUNT(sq.objectID) FROM EdsSaleQuote sq ");
        sql.append("WHERE ").append(ServerUtils.checkForDeleted("sq.deleted"));
        sql.append(" and sq.status.code ").append(isSalesQuote ? "not" : "").append(" in (").append(saleOrderStatus).append(")");
        sql.append(" AND sq.status IS NOT NULL");
        return (Long) findSingle(sql.toString());
    }

    public Long getCountPurchaseOrderList() {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT COUNT(po.objectID) FROM EdsPurchaseOrder po ");
        sql.append("WHERE ").append(ServerUtils.checkForDeleted("po.deleted"));
        sql.append(" AND po.status IS NOT NULL");
        return (Long) findSingle(sql.toString());
    }

    public List<EdsSaleQuote> getSaleQuoteList(ListingFilterParameter fp, ListLoadConfig config) {
        if (fp == null) {
            fp = new ListingFilterParameter();
        }

        Map<String, Object> param = new HashMap<>();

        StringBuffer sql = new StringBuffer();
        if (genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.PROJECT_IN_LINE_ITEM_ENABLE) && fp.getProjectId() != null) {
            sql.append("select distinct sq from EdsQuoteItem qi join qi.quote q, EdsSaleQuote sq");
        } else {
            sql.append("select sq from EdsQuote q, EdsSaleQuote sq");
        }
        //Below we are getting access only to PM.
        EdsUser user = getUser();
        if (fp.isLookUp() && user != null && roleManager.hasRole(user, PM) && !roleManager.hasEitherRoles(user, ACCOUNTANT, DR, ADMIN)) {
            sql.append(" left join sq.relatedProject pr where " + ServerUtils.checkForDeleted("sq.deleted") + " and (pr.manager = :user or pr.backupManager = :user or ");
            sql.append(" pr.backupManager2 = :user or ");
            sql.append(" pr.backupManager3 = :user or ");
            sql.append(" pr.backupManager4 = :user or ");
            sql.append(" pr.backupManager5 = :user or ");
            sql.append(" pr.backupManager6 = :user or ");
            sql.append(" pr.backupManager7 = :user or ");
            sql.append(" pr.backupManager8 = :user or ");
            sql.append(" pr.backupManager9 = :user or ");
            sql.append(" pr.backupManager10 = :user or ");
            sql.append(" sq.creator = :user) ");
            param.put("user", user);
        } else {
            sql.append(" where " + ServerUtils.checkForDeleted("sq.deleted"));
            sql.append(" and q.id = sq.id ");
            sql.append(" and sq.type = '").append(RECEIVABLE).append("' ");
        }

        if (fp.getInvoiceStatusId() != null) {
            sql.append(" and sq.status.objectID = :statusID");
            param.put("statusID", fp.getInvoiceStatusId());
        }

        if (fp.getExcludedType() != null) {
            sql.append(" and sq.status.code not in(:draftStatus)");
            param.put("draftStatus", fp.getExcludedType());
        }
        if (fp.getParams() != null && SALE_QUOTE.equals(fp.getParams()) && !fp.isAllByFilter()) {
            String saleOrderStatus = "'SALE_ORDER', 'PICKED', 'PACKED', 'SHIPPED'";
            sql.append(" and sq.status.code ").append(fp.isSearchByParent() ? "not" : "").append(" in (").append(saleOrderStatus).append(")");
            if (fp.isSearchByParent()) {
                sql.append(" and " + ServerUtils.checkForDeleted("sq.isSalesOrder"));
            }
        }
        if (fp.getClientContactId() != null) {
            sql.append(" and sq.clientContact.objectID = " + fp.getClientContactId());
        }
        if (fp.getOpportunityID() != null) {
            sql.append(" and sq.opportunityID = :relatedOpportunityID");
            param.put("relatedOpportunityID", fp.getOpportunityID());
        }

        if (fp.getInvoiceClientId() != null) {
            sql.append(" and sq.client.objectID = :clientID");
            param.put("clientID", fp.getInvoiceClientId());
        } else if (fp.getClientId() != null) {
            sql.append(" and sq.client.objectID = :filterClientID");
            param.put("filterClientID", fp.getClientId());
        }

        if (fp.getProjectId() != null) {
            if (genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.PROJECT_IN_LINE_ITEM_ENABLE)) {
                sql.append(" and qi.project.objectID in (:projectID)");
            } else {
                sql.append(" and sq.relatedProject.objectID in (:projectID)");
            }
            if (fp.getProjectIdList() != null && fp.getProjectIdList().size() > 0) {
                param.put("projectID", fp.getProjectIdList());
            } else {
                param.put("projectID", fp.getProjectId());
            }
        }

        if (fp.getStartDate() != null && fp.getEndDate() != null) {
            sql.append(" and (sq.invoiceDate >= :startDate and sq.invoiceDate <= :dueDate)");
            param.put("startDate", fp.getStartDate());
            param.put("dueDate", fp.getEndDate());
        } else if (fp.getStartDate() != null) {
            sql.append(" and (sq.invoiceDate >= :startDate)");
            param.put("startDate", fp.getStartDate());
        } else if (fp.getEndDate() != null) {
            sql.append(" and (sq.invoiceDate <= :dueDate)");
            param.put("dueDate", fp.getEndDate());
        }

        if (fp.getSqlSearchKey() != null) {
            sql.append(" and (lower(sq.number) like '" + fp.getSqlSearchKey() + "' or ");
            sql.append(" lower(sq.client.name) like '" + fp.getSqlSearchKey() + "' or ");
            sql.append(" lower (sq.currency.name) like '" + fp.getSqlSearchKey() + "' or ");
            sql.append(" lower (sq.status.name) like '" + fp.getSqlSearchKey() + "') ");
        }

        sql.append(" and sq.status is not null ");
        addFilterSortingQuery(fp, sql, "sq");
        return findIntervalByNamedParams(sql.toString(), fp.getStart(), fp.getLimit(), param);
    }

    public List<EdsSaleQuote> getSaleQuoteList(ListingFilterParameter fp, EdsCompany company, boolean isRecurringInvoice) {
        if (fp == null) {
            fp = new ListingFilterParameter();
        }
        Map<String, Object> param = new HashMap<>();

        StringBuilder sql = new StringBuilder();
        sql.append("select si from EdsSaleQuote si where 1=1 ");

        if (fp.getInvoiceStatusId() != null) {
            sql.append(" and si.status.objectID = :statusID");
            param.put("statusID", fp.getInvoiceStatusId());
        }

        if (fp.getInvoiceClientId() != null) {
            sql.append(" and si.client.objectID = :clientID");
            param.put("clientID", fp.getInvoiceClientId());
        } else if (fp.getClientId() != null) {
            sql.append(" and si.client.objectID = :filterClientID");
            param.put("filterClientID", fp.getClientId());
        }

        if (fp.getStartDate() != null && fp.getEndDate() != null) {
            sql.append(" and (si.invoiceDate >= :startDate and si.invoiceDate <= :dueDate)");
            param.put("startDate", fp.getStartDate());
            param.put("dueDate", fp.getEndDate());
        } else if (fp.getStartDate() != null) {
            sql.append(" and (si.invoiceDate >= :startDate)");
            param.put("startDate", fp.getStartDate());
        } else if (fp.getEndDate() != null) {
            sql.append(" and (si.invoiceDate <= :dueDate)");
            param.put("dueDate", fp.getEndDate());
        }
        if (fp.getSqlSearchKey() != null) {
            sql.append(" and (lower(si.number) like '" + fp.getSqlSearchKey() + "' or ");
            sql.append(" lower(si.client.name) like '" + fp.getSqlSearchKey() + "' or ");
            sql.append(" lower (si.currency.name) like '" + fp.getSqlSearchKey() + "' or ");
            sql.append(" lower (si.status.name) like '" + fp.getSqlSearchKey() + "') ");
        }

        sql.append(" and si.status is not null and si.status.code!='REVERSED' order by si.objectID desc");
        return findByNamedParams(sql.toString(), param);
    }

    public List<EdsSaleQuote> getSaleQuoteListForSolr(SolrReindexRpc solrReindex, Integer start, Integer limit) {
        StringBuilder saleQuoteSqlQuery = new StringBuilder("SELECT sq FROM EdsSaleQuote sq where " + ServerUtils.checkForDeleted("sq.deleted") + " ");
        if (!solrReindex.isAllReindex() && solrReindex.getLastUpdateTime() != null) {
            saleQuoteSqlQuery.append(" and sq.updatedDate>=").append("'").append(solrReindex.getLastUpdateTime()).append("'");
            if (solrReindex.getLastUpdateEndTime() != null) {
                saleQuoteSqlQuery.append(" and sq.updatedDate<='").append(solrReindex.getLastUpdateEndTime()).append("'");
            }
        }
        saleQuoteSqlQuery.append(" order by sq.objectID asc");
        return findIntervalByNamedParams(saleQuoteSqlQuery.toString(), start, limit,  new HashMap<>());
    }

    public List<EdsPurchaseOrder> getPurchaseOrderListForSolr(SolrReindexRpc solrReindex, Integer start, Integer limit) {
        Map<String, Object> params = new HashMap<>();
        StringBuilder purOrderSqlQuery = new StringBuilder("SELECT po FROM EdsPurchaseOrder po WHERE " + ServerUtils.checkForDeleted("po.deleted"));
        if (!solrReindex.isAllReindex() && solrReindex.getLastUpdateTime() != null) {
            params.put("modifiedDate", solrReindex.getLastUpdateTime());
            purOrderSqlQuery.append(" and po.updatedDate >= :modifiedDate");
            if (solrReindex.getLastUpdateEndTime() != null) {
                purOrderSqlQuery.append(" and po.updatedDate<='").append(solrReindex.getLastUpdateEndTime()).append("'");
            }
        }
        purOrderSqlQuery.append(" order by po.objectID asc ");
        return findIntervalByNamedParams(purOrderSqlQuery.toString(), start, limit, params);
    }

    @Override
    public List<Integer> getPurchaseOrderIdsByIDs(String ids) {
        return find("SELECT po.objectID FROM EdsPurchaseOrder po WHERE po.objectID IN(" + ids + ") AND po.status IS NOT NULL");
    }

    @Override
    public List<Integer> getQuoteIdsByIDs(String ids) {
        return find("SELECT si.objectID FROM EdsSaleQuote si WHERE si.objectID IN(" + ids + ") AND si.status IS NOT NULL AND si.status.code!='REVERSED'");
    }

    @Override
    public List<Integer> getCompanyQuoteIdsWithLimit(Integer companyID, int startat, int limit) {
        return findLimited("SELECT si.objectID FROM EdsSaleQuote si WHERE si.objectID > ? AND " + ServerUtils.checkForDeleted("si.deleted") + " AND si.status IS NOT NULL AND si.status.code!='REVERSED' ORDER BY si.objectID ASC", limit, startat);
    }

    @Override
    public List<Integer> getPurchaseOrderIdsWithLimit(int startat, int limit) {
        return findLimited("SELECT po.objectID FROM EdsPurchaseOrder po WHERE po.objectID > ? AND " + ServerUtils.checkForDeleted("po.deleted") + " AND po.status IS NOT NULL ORDER BY po.objectID ASC", limit, startat);
    }

    private void addFilterSortingQuery(ListingFilterParameter config, StringBuffer sql, String table) {
        if (config != null) {
            if (config.getSortField() != null) {
                if (INVOICE_NUMBER_COLUMN.equals(config.getSortField())) {
                    sql.append("order by " + table + ".number " + (config.getSortDir() == 2 ? "desc" : ""));
                } else if (INVOICE_DATE_COLUMN.equals(config.getSortField())) {
                    sql.append("order by " + table + ".invoiceDate " + (config.getSortDir() == 2 ? "desc" : ""));
                } else if (DUE_DATE_COLUMN.equals(config.getSortField())) {
                    sql.append("order by " + table + ".dueDate " + (config.getSortDir() == 2 ? "desc" : ""));
                } else if (CLIENT_COLUMN.equals(config.getSortField())) {
                    sql.append("order by " + table + (table.equals("sq") ? ".client.name" : ".supplier.name"));
                    sql.append(config.getSortDir() == 2 ? " desc" : "");
                } else if (CURRENCY_COLUMN.equals(config.getSortField())) {
                    sql.append("order by " + table + ".currency.name " + (config.getSortDir() == 2 ? "desc" : ""));
                }/*else if(DUE_AMOUNT_COLUMN.equals(config.getSortField())){
                    sql.append("order by pi.objectID " + (config.getSortDir()==2 ? "desc" : ""));
                }*/ else if (STATUS_COLUMN.equals(config.getSortField())) {
                    sql.append("order by " + table + ".status.name " + (config.getSortDir() == 2 ? "desc" : ""));
                }
            } else {
                sql.append("order by " + table + ".objectID desc");
            }
        }
    }

    public List<EdsPurchaseOrder> getPurchaseOrderList(ListingFilterParameter fp, ListLoadConfig config) {
        Map<String, Object> param = new HashMap<>();

        StringBuffer sql = new StringBuffer();
        EdsUser user = getUser();
        boolean joinOnRelatedProject = false;
        //User equals null when import
        if (user != null)
            joinOnRelatedProject = roleManager.hasRole(user, PM) && !roleManager.hasEitherRoles(user, ACCOUNTANT, DR, ADMIN) && !fp.isFromBudgetSheet();
        if (fp.getProjectId() != null && genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.PROJECT_IN_LINE_ITEM_ENABLE)) {
            sql.append("select distinct po from EdsQuoteItem qi join qi.quote q, EdsPurchaseOrder po ");
        } else {
            sql.append("select po from EdsQuote q, EdsPurchaseOrder po ");
        }

        //Below we are getting access only to PM.

        if (joinOnRelatedProject) {
            sql.append(" left join po.relatedProject pr where " + ServerUtils.checkForDeleted("po.deleted") + " and (pr.managerid = :user or pr.backup_ManagerId = :user or ");
            sql.append(" pr.backup_ManagerId2 = :user or ");
            sql.append(" pr.backup_ManagerId3 = :user or ");
            sql.append(" pr.backup_ManagerId4 = :user or ");
            sql.append(" pr.backup_ManagerId5 = :user or ");
            sql.append(" pr.backup_ManagerId6 = :user or ");
            sql.append(" pr.backup_ManagerId7 = :user or ");
            sql.append(" pr.backup_ManagerId8 = :user or ");
            sql.append(" pr.backup_ManagerId9 = :user or ");
            sql.append(" pr.backup_ManagerId10 = :user or ");
            sql.append(" po.creator = :userr) ");
            param.put("user", user.getObjectID());
            param.put("userr", user);
        } else {
            sql.append(" where " + ServerUtils.checkForDeleted("po.deleted"));
            sql.append(" and q.id = po.id ");
            sql.append(" and q.type = '").append(PAYABLE).append("' ");
        }

        if (fp != null) {
            if (fp.getInvoiceStatusId() != null) {
                sql.append(" and po.status.objectID = :statusID");
                param.put("statusID", fp.getInvoiceStatusId());
            } else if (fp.getStatusValues() != null) {
                sql.append(" and po.status.code in (" + fp.getStatusValues() + ") ");
            }
            if (fp.getExcludedType() != null) {
                sql.append(" and po.status.code != :statusCode");
                param.put("statusCode", fp.getExcludedType());
            }

            if (fp.getInvoiceClientId() != null) {
                sql.append(" and po.supplier.objectID = :supplierID");
                param.put("supplierID", fp.getInvoiceClientId());
            } else if (fp.getClientId() != null) {
                sql.append(" and po.supplier.objectID = :filterSupplierID");
                param.put("filterSupplierID", fp.getClientId());
            }

            if (fp.getStartDate() != null && fp.getEndDate() != null) {
                sql.append(" and (po.invoiceDate >= :startDate and po.invoiceDate <= :dueDate)");
                param.put("startDate", fp.getStartDate());
                param.put("dueDate", fp.getEndDate());
            } else if (fp.getStartDate() != null) {
                sql.append(" and (po.invoiceDate >= :startDate)");
                param.put("startDate", fp.getStartDate());
            } else if (fp.getEndDate() != null) {
                sql.append(" and (po.invoiceDate <= :dueDate)");
                param.put("dueDate", fp.getEndDate());
            }

            if (fp.getProjectId() != null) {
                if (genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.PROJECT_IN_LINE_ITEM_ENABLE)) {
                    sql.append(" and qi.project.objectID in (:projectID)");
                } else {
                    sql.append(" and po.relatedProject.objectID in (:projectID)");
                }
                if (fp.getProjectIdList() != null && fp.getProjectIdList().size() > 0) {
                    param.put("projectID", fp.getProjectIdList());
                } else {
                    param.put("projectID", fp.getProjectId());
                }
            }

            if (fp.getSqlSearchKey() != null) {
                sql.append(" and (lower(po.number) like '" + fp.getSqlSearchKey() + "' or ");
                sql.append(" lower(po.supplier.name) like '" + fp.getSqlSearchKey() + "' or ");
                sql.append(" lower (po.currency.name) like '" + fp.getSqlSearchKey() + "' or ");
                sql.append(" lower (po.status.name) like '" + fp.getSqlSearchKey() + "') ");
            }
        }

        sql.append(" and po.status is not null ");
        addFilterSortingQuery(fp, sql, "po");
        return findIntervalByNamedParams(sql.toString(), fp.getStart(), fp.getLimit(), param);
    }

    public EdsPurchaseOrder getPurchaseOrderByID(Integer purchaseOrderID) {
        if (purchaseOrderID == null) {
            return null;
        }
        return (EdsPurchaseOrder) findSingle("select po from EdsPurchaseOrder po where po.objectID=?", purchaseOrderID);
    }

    public String getLastBillingInformation() {
        return (String) findSingle("select q.notes from EdsQuote q where q.objectID=(select max(q2.objectID) " +
                "from EdsQuote q2 ) ");
    }

    public Integer getQuoteFourDigitNumber(boolean isSalesOrder, DateNonConvertable quoteOrderDate) {
        Calendar ordDateCal = new GregorianCalendar();
        if (quoteOrderDate != null) {
            ordDateCal.setTime(quoteOrderDate.getNonConvertedDate());
            ServerUtils.setEndOfTheDay(ordDateCal);
        }

        StringBuilder query = new StringBuilder();
        Map<String, Object> values = new HashMap<>();
        query.append("select sq.fourDigitNumber from EdsSaleQuote sq where sq.fourDigitNumber is not null and " + ServerUtils.checkForDeleted("sq.deleted"));
        if (genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.ENABLE_SALES_ORDER_NUMBERING)) {
            if (isSalesOrder) {
                query.append(" and sq.isSalesOrder is true");
            } else {
                query.append(" and (sq.isSalesOrder is false or sq.isSalesOrder is null)");
            }
        }
        Calendar financialYearStart = getFinancialYearStartIfEnabled(null);
        if (financialYearStart != null && financialYearStart.getTime().before(quoteOrderDate != null ? ordDateCal.getTime() : new Date())) {
            values.put("financialYearStart", financialYearStart.getTime());
            query.append(" and sq.invoiceDate > :financialYearStart");
        }
        query.append(" order by sq.fourDigitNumber desc");
        return (Integer) findSingleByNamedParams(query.toString(), values);
    }

    public Integer getOrderFourDigitNumber(DateNonConvertable orderDate) {
        Calendar ordDateCal = new GregorianCalendar();
        if (orderDate != null) {
            ordDateCal.setTime(orderDate.getNonConvertedDate());
            ServerUtils.setEndOfTheDay(ordDateCal);
        }
        StringBuilder query = new StringBuilder();
        Map<String, Object> values = new HashMap<>();
        query.append("select po.fourDigitNumber from EdsPurchaseOrder po where po.fourDigitNumber is not null and " + ServerUtils.checkForDeleted("po.deleted"));
        Calendar financialYearStart = getFinancialYearStartIfEnabled(null);
        if (financialYearStart != null && financialYearStart.getTime().before(orderDate != null ? ordDateCal.getTime() : new Date())) {
            values.put("financialYearStart", financialYearStart.getTime());
            query.append(" and po.invoiceDate > :financialYearStart");
        }
        query.append(" order by po.fourDigitNumber desc");
        return (Integer) findSingleByNamedParams(query.toString(), values);
    }

    private Calendar getFinancialYearStartIfEnabled(Date creationDate) {
        EdsInvoicingSettings settings = (EdsInvoicingSettings) findSingle("select eis from EdsInvoicingSettings eis ");
        if (settings != null && settings.isNumberingRestartEnabled()) {
            Calendar financialYearStart = new GregorianCalendar();
            if (creationDate != null) {
                financialYearStart.setTime(creationDate);
            }
            financialYearStart.set(Calendar.MONTH, settings.getNumberingRestartMonth());
            financialYearStart.set(Calendar.DATE, settings.getNumberingRestartDate());
            ServerUtils.setBeginningOfTheDay(financialYearStart);
            return financialYearStart;
        }
        return null;
    }

    public List<EdsSaleQuote> getSalesQuoteByNumber(String number, Date creationDate, boolean isOrder) {
        Calendar financialYearStart = getFinancialYearStartIfEnabled(creationDate);
        String subQuery = "";
        if (genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.ENABLE_SALES_ORDER_NUMBERING)) {
            if (isOrder) {
                subQuery = " and q.isSalesOrder is true ";
            } else {
                subQuery = " and (q.isSalesOrder is false or q.isSalesOrder is null) ";
            }
        }
        if (financialYearStart != null && financialYearStart.getTime().before(new Date())) {
            if (creationDate != null) {
                Calendar financialYearEnd = new GregorianCalendar();
                financialYearEnd.setTime(financialYearStart.getTime());
                financialYearEnd.set(Calendar.YEAR, financialYearEnd.get(Calendar.YEAR) + 1);
                return (List<EdsSaleQuote>) find("select q from EdsSaleQuote q where q.number=? and (q.invoiceDate between ? and ?) and " + ServerUtils.checkForDeleted("q.deleted") + subQuery,
                        number, financialYearStart.getTime(), financialYearEnd.getTime());
            } else {
                return (List<EdsSaleQuote>) find("select q from EdsSaleQuote q where q.number=?  and q.invoiceDate > ? and " + ServerUtils.checkForDeleted("q.deleted") + subQuery, number, financialYearStart.getTime());
            }
        } else {
            return (List<EdsSaleQuote>) find("select q from EdsSaleQuote q where q.number=? and " + ServerUtils.checkForDeleted("q.deleted") + subQuery, number);
        }
    }

    public List<EdsSaleQuote> getQuoteByNumber(String number) {

        return (List<EdsSaleQuote>) find("select q from EdsSaleQuote q where q.number=? and " + ServerUtils.checkForDeleted("q.deleted"), number);
    }

    public EdsPurchaseOrder getPurchaseOrderByNumber(String number) {

        return (EdsPurchaseOrder) findSingle("select q from EdsPurchaseOrder q where q.number=? and " + ServerUtils.checkForDeleted("q.deleted"), number);
    }

    @Override
    public EdsQuote getByObjectKey(String objectKey) {
        if (StringUtils.isBlank(objectKey)) {
            return null;
        }
        return (EdsQuote) findSingle("select q from EdsQuote q where q.objectKey = ? ", objectKey);
    }

    public List<EdsPurchaseOrder> getPurchaseOrderByNumber(String number, Date creationDate) {
        Calendar financialYearStart = getFinancialYearStartIfEnabled(creationDate);
        if (financialYearStart != null && financialYearStart.getTime().before(new Date())) {
            if (creationDate != null) {
                Calendar financialYearEnd = new GregorianCalendar();
                financialYearEnd.setTime(financialYearStart.getTime());
                financialYearEnd.set(Calendar.YEAR, financialYearEnd.get(Calendar.YEAR) + 1);
                return (List<EdsPurchaseOrder>) find("select po from EdsPurchaseOrder po where po.number=? and (po.invoiceDate between ? and ?) and " + ServerUtils.checkForDeleted("po.deleted"),
                        number, financialYearStart.getTime(), financialYearEnd.getTime());
            } else {
                return (List<EdsPurchaseOrder>) find("select po from EdsPurchaseOrder po where po.number=?  and po.invoiceDate > ? and " + ServerUtils.checkForDeleted("po.deleted"), number, financialYearStart.getTime());
            }
        } else {
            return (List<EdsPurchaseOrder>) find("select po from EdsPurchaseOrder po where po.number=? and " + ServerUtils.checkForDeleted("po.deleted"), number);
        }
    }

    @Override
    public List<EdsSaleQuote> getSalesQuoteByNumberGlobal(String number, boolean isOrder) {
        String subQuery = "";
        if (genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.ENABLE_SALES_ORDER_NUMBERING)) {
            if (isOrder) {
                subQuery = " and q.isSalesOrder is true ";
            } else {
                subQuery = " and (q.isSalesOrder is false or q.isSalesOrder is null) ";
            }
        }
        return (List<EdsSaleQuote>) find("select q from EdsSaleQuote q where q.number=? and " + ServerUtils.checkForDeleted("q.deleted") + subQuery, number);
    }

    @Override
    public List<EdsPurchaseOrder> getPurchaseOrderByNumberGlobal(String number) {
        return (List<EdsPurchaseOrder>) find("select po from EdsPurchaseOrder po where po.number=? and " + ServerUtils.checkForDeleted("po.deleted"), number);
    }

    public void deleteQuoteOldTaxTotals(EdsQuote quote) {
        update("delete from EdsQuoteTaxTotal qtt where qtt.quote=?", quote);
    }

    public List<Integer> deleteQuoteItems(Integer quoteID, ArrayList<Integer> qiIds) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("objectId", quoteID);
        map.put("itemIds", qiIds);

        List<Integer> itemsDeleted = findByNamedParams("select objectID from EdsQuoteItem where quote.objectID = :objectId and objectID NOT IN :itemIds", map);

        if (itemsDeleted != null && !itemsDeleted.isEmpty()) {
            map.clear();
            map.put("itemIds", itemsDeleted);
            updateByNamedParams("delete from EdsQuoteItem where objectID IN :itemIds", map);
        }
        return itemsDeleted;
    }

    public EdsSaleQuote getSaleQuote(Integer saleQuoteID) {
        return (EdsSaleQuote) findSingle("select sq from EdsSaleQuote sq where sq.objectID=?", saleQuoteID);
    }

    @Override
    public void removeRelationFromInvoice(Integer quoteID) {
        updateNative("delete from  " + getCompanyId() + ".converted_items where  quote_id = " + quoteID);
    }

    @Override
    public List<EdsSaleQuote> getSaleQuotesByCrmAccountID(Integer crmAccountID) {
        return (List<EdsSaleQuote>) find("select sq from EdsSaleQuote sq where " + ServerUtils.checkForDeleted("sq.deleted") + " and sq.client.objectID = ?", crmAccountID);
    }

    @Override
    public List<EdsSaleQuote> getSaleQuotesByCrmContactID(Integer crmContactID) {
        return (List<EdsSaleQuote>) find("select sq from EdsSaleQuote sq where " + ServerUtils.checkForDeleted("sq.deleted") +
                " and sq.clientContact.objectID = ?", crmContactID);
    }

    @Override
    public boolean findSaleQuotesByCrmAccountID(Integer crmAccountID) {
        List<Integer> invIds = (List<Integer>) find("select sq.id from EdsSaleQuote sq where " + ServerUtils.checkForDeleted("sq.deleted") +
                " and sq.client.objectID = ?", crmAccountID);
        return invIds != null && invIds.size() > 0;
    }

    @Override
    public List<EdsSaleQuote> getUnDeletedSaleQuotesByCrmAccountID(Integer crmAccountID) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> param = new HashMap<>();
        param.put("clientID", crmAccountID);
        sql.append("select sq from EdsSaleQuote sq where " + ServerUtils.checkForDeleted("sq.deleted") + "  and sq.client.objectID = :clientID ");
        return (List<EdsSaleQuote>) findByNamedParams(sql.toString(), param);
    }

    @Override
    public List<EdsPurchaseOrder> getPurchaseOrdersByCrmAccountID(Integer crmAccountID) {
        return (List<EdsPurchaseOrder>) find("select po from EdsPurchaseOrder po where po.supplier.objectID = ?", crmAccountID);
    }

    @Override
    public List<EdsPurchaseOrder> getUnDeletedPurchaseOrdersByCrmAccountID(Integer crmAccountID) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> param = new HashMap<>();
        param.put("supplierID", crmAccountID);
        sql.append("select po from EdsPurchaseOrder po where " + ServerUtils.checkForDeleted("po.deleted") + "  and po.supplier.objectID = :supplierID ");
        return (List<EdsPurchaseOrder>) findByNamedParams(sql.toString(), param);
    }

    @Override
    public List<EdsSaleQuote> getQuotesByClient(ListingFilterParameter fp, List<Integer> statusIds, boolean multiConvertEnabled) {
        Map<String, Object> param = new HashMap<>();
        StringBuffer sql = new StringBuffer();
        sql.append("select sq from EdsSaleQuote sq where 1=1 and " + ServerUtils.checkForDeleted("sq.deleted"));

        if (fp.getInvoiceClientId() != null) {
            sql.append(" and sq.client.objectID = :clientID ");
            param.put("clientID", fp.getInvoiceClientId());
        }

        if (multiConvertEnabled) {
            sql.append(" and " + ServerUtils.checkForDeleted("sq.progressInvoicing"));
        }

        if (statusIds != null && statusIds.size() > 0) {
            sql.append(" and sq.status.objectID IN ('" + ServerUtils.getAsCommoDelimited(statusIds, "0", "','") + "') ");
        }

        addFilterSortingQuery(fp, sql, "sq");


        return findByNamedParams(sql.toString(), param);
    }

    @Override
    public List<EdsPurchaseOrder> getPurchaseOrderBySupplier(ListingFilterParameter fp, Map<String, Integer> statusId) {
        Map<String, Object> param = new HashMap<>();

        StringBuffer sql = new StringBuffer();
        sql.append("select po from EdsPurchaseOrder po where 1=1 ");

        if (fp.getInvoiceClientId() != null) {
            sql.append("and po.supplier.objectID = :supplierID ");
            param.put("supplierID", fp.getInvoiceClientId());
        }

        if (statusId.get(RECEIVED) != null && statusId.get(OPEN) != null) {
            sql.append("and (po.status.objectID = :RECEIVED or po.status.objectID = :OPEN) ");
            param.put("RECEIVED", statusId.get(RECEIVED));
            param.put("OPEN", statusId.get(OPEN));
        } else if (statusId.get(RECEIVED) != null) {
            sql.append("and po.status.objectID = :RECEIVED ");
            param.put("RECEIVED", statusId.get(RECEIVED));
        } else if (statusId.get(OPEN) != null) {
            sql.append("and po.status.objectID = :OPEN ");
            param.put("OPEN", statusId.get(OPEN));
        }

        sql.append(" and po.deleted <> :deleted ");
        param.put("deleted", true);

        addFilterSortingQuery(fp, sql, "po");

        return findByNamedParams(sql.toString(), param);
    }

    @Override
    public void removeQuoteItems(Integer quoteId) {
        update("update from EdsQuoteItem set deleted='true' where quote.objectID = ?", quoteId);
    }

    @Override
    public List<EdsPurchaseOrder> getPurchaseOrderListForSaasuSync(Integer startIndex, Integer limit) {
        return findLimited("SELECT po FROM EdsPurchaseOrder po WHERE po.objectID > ? AND po.status.code='APPROVE' AND " + ServerUtils.checkForDeleted("po.deleted") + " ORDER BY po.objectID ASC", limit, startIndex);
    }

    @Override
    public EdsPurchaseOrder getPurchaseOrderBySaasuGUID(String saasuGUID) {
        Map<String, Object> map = new HashMap<>();
        map.put("SAASU_UID", saasuGUID);
        return (EdsPurchaseOrder) findSingleByNamedParams("select po from EdsPurchaseOrder po where (po.deleted is null or po.deleted<>true) AND po.saasuGUID = :SAASU_UID", map);
    }

    @Override
    public List<EdsSaleQuote> getSaleQuotesByIds(String Ids) {
        return find("select sq from EdsSaleQuote sq where " + ServerUtils.checkForDeleted("sq.deleted") + " and sq.objectID in(" + Ids + ")");
    }

    @Override
    public List<EdsPurchaseOrder> getPurchaseOrdersByIds(String Ids) {
        return find("select po from EdsPurchaseOrder po where " + ServerUtils.checkForDeleted("po.deleted") + " and po.objectID in(" + Ids + ")");
    }

    @Override
    public EdsSaleQuote getSalesQuoteByCode(String code) {
        return (EdsSaleQuote) findSingle("select sq from EdsSaleQuote sq where " + ServerUtils.checkForDeleted("sq.deleted") + " and sq.externalGUID=?", code);
    }

    @Override
    public List<EdsPurchaseOrder> getPurchaseOrderByQuoteId(Integer quoteId) {
        return find("select po from EdsPurchaseOrder po where " + ServerUtils.checkForDeleted("po.deleted") + " and po.quoteId=?", quoteId);
    }

    @Override
    public EdsQuoteItem getQuoteItemByID(Integer quoteItemID) {
        return (EdsQuoteItem) findSingle("select qi from EdsQuoteItem qi where qi.objectID = ?", quoteItemID);
    }

    @Override
    public BigDecimal getRemainingQtyByQuoteId(Integer quoteId) {
        StringBuilder query = new StringBuilder();
        query.append("select sum(coalesce(qi.qty,0) - coalesce(qi.shippedQty,0)) from EdsQuoteItem qi ");
        query.append("where qi.quote.objectID = ? and qi.deleted is false and qi.pickable is not false");
        BigDecimal remaining = (BigDecimal) findSingle(query.toString(), quoteId);
        return remaining == null ? BigDecimal.ZERO : remaining;
    }

    @Override
    public BigDecimal getInventoryItemOrders(Integer itemID) {

        String saleOrderStatus = "'SALE_ORDER', 'PICKED', 'PACKED', 'SHIPPED'";
        StringBuilder sql = new StringBuilder();
        sql = new StringBuilder();
        sql.append("SELECT SUM(qi.qty) FROM EdsQuoteItem qi ");
        sql.append("JOIN qi.quote q ");
        sql.append("JOIN q.status s ");
        sql.append("JOIN qi.item i ");
        sql.append("WHERE s.code IN (" + saleOrderStatus + ") AND " + ServerUtils.checkForDeleted("qi.quote.deleted"));
        sql.append(" AND i.objectID = '" + itemID + "' ");
        return (BigDecimal) findSingle(sql.toString());
    }

    @Override
    public HashMap<Integer, BigDecimal> getInventoryItemOrders(String itemIDs) {
        StringBuilder sql = new StringBuilder();
        HashMap<Integer, BigDecimal> result = new HashMap<>();
        sql.append("SELECT\n")
                .append("    i.id,\n")
                .append("    coalesce(sum(qi.qty - coalesce(coalesce(qi.shippedqty,qi.convertedqty), 0)), 0.00) as qty\n")
                .append(" FROM ").append(getCompanyId()).append(".quoteitem qi\n")
                .append("  LEFT JOIN ").append(getCompanyId()).append(".quote q ON q.id = qi.quote_id\n")
                .append("  INNER JOIN ").append(getCompanyId()).append(".item i ON i.id = qi.item_id\n")
                .append("  LEFT JOIN ").append(getCompanyId()).append(".reference s ON s.id = q.status_id\n")
                .append(" WHERE ").append(ServerUtils.checkForDeleted("q.deleted"))
                .append(" AND s.code in('SALE_ORDER', 'PICKED', 'PACKED', 'PARTIAL_SHIPPED')\n")
                .append(" AND i.id in (").append(itemIDs).append(") group by i.id");

        List<Map<String, Object>> queryResult = jdbcSpringManager.getSimpleJdbcTemplate().queryForList(sql.toString(), new HashMap<String, String>());
        for (Map<String, Object> map : queryResult) {
            for (String key : map.keySet()) {
                result.put((Integer) map.get("id"), (BigDecimal) map.get("qty"));
            }
        }
        return result;
    }

//    @Override
//    public HashMap<Integer, BigDecimal> getSaleRamaingQty(String itemIDs) {
//        Integer schema = Integer.valueOf(ServerSecurityContext.getInstance().getCompanyId());
//        HashMap<Integer, BigDecimal> result = new HashMap<>();
//        StringBuilder sql = new StringBuilder();
//        sql.append("SELECT i.id,coalesce(sum(qi.shippedqty), 0.00) as qty \n");
//        sql.append(" FROM "+ getCompanyId() +".quoteitem qi \n");
//        sql.append(" LEFT JOIN "+ getCompanyId() +".quote q ON q.id = qi.quote_id \n");
//        sql.append(" INNER JOIN "+ getCompanyId() +".item i ON i.id = qi.item_id \n");
//        sql.append(" LEFT JOIN "+ getCompanyId() +".reference s ON s.id = q.status_id \n");
//        sql.append(" WHERE q.deleted is not true \n");
//        sql.append(" AND s.code in('SALE_ORDER', 'PICKED', 'PACKED', 'PARTIAL_SHIPPED') \n");
//        sql.append(" AND i.id in (").append(itemIDs).append(") group by i.id \n");
//
//        List<Map<String, Object>> queryResult = jdbcSpringManager.getSimpleJdbcTemplate().queryForList(sql.toString(), new HashMap<String, String>());
//        for (Map<String, Object> map : queryResult) {
//            for (String key : map.keySet()) {
//                result.put((Integer) map.get("id"), (BigDecimal) map.get("qty"));
//            }
//        }
//        return result;
//    }

    @Override
    public HashMap<Integer, BigDecimal> getOnPurchaseOrderCountByItem(String itemIDs) {
        Integer schema = Integer.valueOf(ServerSecurityContext.getInstance().getCompanyId());
        HashMap<Integer, BigDecimal> result = new HashMap<>();
        StringBuilder sql = new StringBuilder();
        sql.append("select qi.item_id as id, coalesce(sum(qi.qty-(coalesce(qi.receivedqty, 0))), 0.00) as qty from  \"" + schema + "\".quoteItem qi ")
                .append("left join \"" + schema + "\".quote q on qi.quote_id=q.id ")
                .append("left join \"" + schema + "\".reference r on q.status_id=r.id ")
                .append("where ").append(ServerUtils.checkForDeleted("qi.deleted")).append(" and ").append(ServerUtils.checkForDeleted("q.deleted"))
                .append(" and qi.item_id in(").append(itemIDs).append(") and r.code in ('OPEN','APPROVE','PARTIAL_RECEIVED') and q.type='PAYABLE'")
                .append(" group by qi.item_id");

        List<Map<String, Object>> queryResult = jdbcSpringManager.getSimpleJdbcTemplate().queryForList(sql.toString(), new HashMap<String, String>());
        for (Map<String, Object> map : queryResult) {
            for (String key : map.keySet()) {
                result.put((Integer) map.get("id"), (BigDecimal) map.get("qty"));
            }
        }
        return result;
    }

    @Override
    public Integer getSaleQuoteListCount(ListingFilterParameter fp) {
        if (fp == null) {
            fp = new ListingFilterParameter();
        }

        Map<String, Object> param = new HashMap<>();

        StringBuilder sql = new StringBuilder();
        sql.append("select count(sq) from EdsSaleQuote sq ");

        //Below we are getting access only to PM.
        EdsUser user = getUser();
        if (fp.isLookUp() && user != null && roleManager.hasRole(user, PM) && !roleManager.hasEitherRoles(user, ACCOUNTANT, DR, ADMIN)) {
            sql.append(" left join sq.relatedProject pr where " + ServerUtils.checkForDeleted("sq.deleted") + " and (pr.manager = :user or pr.backupManager = :user or ");
            sql.append(" pr.backupManager2 = :user or ");
            sql.append(" pr.backupManager3 = :user or ");
            sql.append(" pr.backupManager4 = :user or ");
            sql.append(" pr.backupManager5 = :user or ");
            sql.append(" pr.backupManager6 = :user or ");
            sql.append(" pr.backupManager7 = :user or ");
            sql.append(" pr.backupManager8 = :user or ");
            sql.append(" pr.backupManager9 = :user or ");
            sql.append(" pr.backupManager10 = :user or ");
            sql.append(" sq.creator = :user) ");
            param.put("user", user);
        } else {
            sql.append(" where " + ServerUtils.checkForDeleted("sq.deleted"));
        }

        if (fp.getInvoiceStatusId() != null) {
            sql.append(" and sq.status.objectID = :statusID");
            param.put("statusID", fp.getInvoiceStatusId());
        }
        if (fp.getParams() != null && SALE_QUOTE.equals(fp.getParams()) && !fp.isAllByFilter()) {
            String saleOrderStatus = "'SALE_ORDER', 'PICKED', 'PACKED', 'SHIPPED'";
            sql.append(" and sq.status.code ").append(fp.isSearchByParent() ? "not" : "").append(" in (").append(saleOrderStatus).append(")");
            if (fp.isSearchByParent()) {
                sql.append(" and " + ServerUtils.checkForDeleted("sq.isSalesOrder"));
            }
        }
        if (fp.getClientContactId() != null) {
            sql.append(" and sq.clientContact.objectID = " + fp.getClientContactId());
        }
        if (fp.getOpportunityID() != null) {
            sql.append(" and sq.opportunityID = :relatedOpportunityID");
            param.put("relatedOpportunityID", fp.getOpportunityID());
        }

        if (fp.getInvoiceClientId() != null) {
            sql.append(" and sq.client.objectID = :clientID");
            param.put("clientID", fp.getInvoiceClientId());
        } else if (fp.getClientId() != null) {
            sql.append(" and sq.client.objectID = :filterClientID");
            param.put("filterClientID", fp.getClientId());
        }

        if (fp.getProjectId() != null) {
            sql.append(" and sq.relatedProject.objectID = :projectID");
            param.put("projectID", fp.getProjectId());
        }

        if (fp.getStartDate() != null && fp.getEndDate() != null) {
            sql.append(" and (sq.invoiceDate >= :startDate and sq.invoiceDate <= :dueDate)");
            param.put("startDate", fp.getStartDate());
            param.put("dueDate", fp.getEndDate());
        } else if (fp.getStartDate() != null) {
            sql.append(" and (sq.invoiceDate >= :startDate)");
            param.put("startDate", fp.getStartDate());
        } else if (fp.getEndDate() != null) {
            sql.append(" and (sq.invoiceDate <= :dueDate)");
            param.put("dueDate", fp.getEndDate());
        }

        if (fp.getSqlSearchKey() != null) {
            sql.append(" and (lower(sq.number) like '" + fp.getSqlSearchKey() + "' or ");
            sql.append(" lower(sq.client.name) like '" + fp.getSqlSearchKey() + "' or ");
            sql.append(" lower (sq.currency.name) like '" + fp.getSqlSearchKey() + "' or ");
            sql.append(" lower (sq.status.name) like '" + fp.getSqlSearchKey() + "') ");
        }

        sql.append(" and sq.status is not null ");
        return (Integer) findSingleByNamedParams(sql.toString(), param);
    }

    @Override
    public void calculateCustomerQuoteBalance(Integer customerID) {
        StringBuilder sql = new StringBuilder();
        String companyID = getCompanyId();
        sql.append("update ").append(companyID).append(".crmaccount set otherbalance=vt.balance from(");
        sql.append("select coalesce(sum(q.total), 0.00) as balance from ").append(companyID).append(".quote q ");
        sql.append("left join ").append(companyID).append(".salequote sq on sq.id=q.id ");
        sql.append("left join ").append(companyID).append(".reference rf on rf.id=q.status_id ");
        sql.append("where q.type='RECEIVABLE' and ").append(ServerUtils.checkForDeleted("q.deleted")).append(" and ").append(ServerUtils.checkForDeleted("sq.issalesorder"));
        sql.append(" and rf.code not in ('DRAFT', 'INVOICE_STATUS_INVOICED', 'REJECT',  'SALES_ORDER')");
        sql.append(" and sq.client_id=").append(customerID).append(") as vt where id=").append(customerID);
        updateNative(sql.toString());
    }

    @Override
    public Object getQuotedItemCountByPeriod(Date startDate, Date endDate, Integer itemID, Integer objectID) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

        StringBuilder sql = new StringBuilder();
        sql.append("select sum(coalesce(qi.qty, 0)) qty, array_to_string(array_agg(q.number), ',') number from ").append(getCompanyId()).append(".quote q \n");
        sql.append("inner join ").append(getCompanyId()).append(".quoteItem qi on qi.quote_id = q.id \n");
        sql.append("inner join ").append(getCompanyId()).append(".item i on i.id = qi.item_id \n");
        sql.append("inner join ").append(getCompanyId()).append(".reference qs on qs.id = q.status_id \n");
        sql.append("where q.deleted is not true and q.type = '").append(RECEIVABLE).append("' \n");
        sql.append("and i.id = ").append(itemID).append(" \n");
        sql.append("and qs.code in ('").append(APPROVE).append("', '").append(OPEN).append("') \n");
        sql.append("and (q.invoiceDate between '").append(format.format(startDate)).append("' and '").append(format.format(endDate)).append("' or \n");
        sql.append("     q.dueDate between '").append(format.format(startDate)).append("' and '").append(format.format(endDate)).append("' or \n");
        sql.append("     q.invoiceDate < '").append(format.format(startDate)).append("' and q.dueDate > '").append(format.format(endDate)).append("') \n");

        if (objectID != null) {
            sql.append("and q.id != ").append(objectID).append(" \n");
        }
        return findNativeSingle(sql.toString());
    }

    public Boolean hasConvertedItems(Integer quoteId) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT count(pi.id) > 0 as boolvalue" +
                " FROM " + getCompanyId() + ".converted_items ci" +
                "  JOIN " + getCompanyId() + ".purchaseorder po ON po.id = ci.quote_id" +
                "  JOIN " + getCompanyId() + ".purchaseinvoice pi ON pi.id = ci.invoice_id" +
                "  JOIN " + getCompanyId() + ".invoice i ON pi.id = i.id " +
                " WHERE (i.deleted is null or i.deleted <> true) " +
                " and po.id = ?");
        return (Boolean) findNativeSingle(sql.toString(), quoteId);
    }

    @Override
    public boolean hasConvertedShippingData(Integer shippingDataId) {
        if (shippingDataId == null) {
            return false;
        }
        final String sql = "SELECT count(i.id) from " + getCompanyId() + ".converted_shipping_data ci " +
//                           "    JOIN " + getCompanyId() + ".shipping_data sh on ci.shipping_data_id = sh.id " +
//                           "    JOIN " + getCompanyId() + ".purchaseinvoice pi ON pi.id = ci.invoice_id" +
                "    JOIN " + getCompanyId() + ".invoice i ON ci.invoice_id = i.id " +
                " WHERE (i.deleted is null or i.deleted <> true) " +
                " AND ci.shipping_data_id = :shippingDataId";
        final List<BigInteger> list = this.slaveEntityManager.createNativeQuery(sql)
                .setParameter("shippingDataId", shippingDataId)
                .setMaxResults(1)
                .getResultList();

        return list.isEmpty() ? false : list.get(0).longValue() > 0;
    }


    @Override
    public boolean isFullyShipped(Integer saleOrderId) {

        if (saleOrderId == null) {
            return false;
        }

        StringBuilder sql = new StringBuilder("SELECT sum(qi.qty) FROM ").append(getCompanyId()).append(".quoteItem qi \n");
        sql.append(" JOIN ").append(getCompanyId()).append(".quote q ON q.id = qi.quote_id \n");
        sql.append(" WHERE qi.deleted is not true AND q.id = :saleOrderId \n");

        BigDecimal totalQty = (BigDecimal) slaveEntityManager.createNativeQuery(sql.toString())
                .setParameter("saleOrderId", saleOrderId)
                .getSingleResult();

        sql = new StringBuilder("SELECT sum(shdi.receivedQty) FROM ").append(getCompanyId()).append(".shipping_data_items shdi \n");
        sql.append(" JOIN ").append(getCompanyId()).append(".shipping_data shd ON shd.id = shdi.shippingDataId \n");
        sql.append(" JOIN ").append(getCompanyId()).append(".quote q ON q.id = shd.quoteId \n");
        sql.append(" WHERE shd.deleted is not true and q.id = :saleOrderId \n");

        BigDecimal shippedQty = (BigDecimal) slaveEntityManager.createNativeQuery(sql.toString())
                .setParameter("saleOrderId", saleOrderId)
                .getSingleResult();

        return totalQty.compareTo(shippedQty) <= 0;
    }

    @Override
    public SelectItem getBookingQty(Integer productId, Integer warehouseId, Integer entityId) {
        StringBuilder sql = new StringBuilder("SELECT sum(case when coalesce(qi.bookReservation,0) - coalesce(qi.shippedqty, 0) > 0 " +
                "then coalesce(qi.bookReservation,0) - coalesce(qi.shippedqty, 0) " +
                "else 0 end) totalBook, array_to_string(array_agg(case when qi.bookReservation > 0 then q.number  end),', ') sqnumbers  FROM ")
                .append(getCompanyId()).append(".quoteItem qi \n");
        sql.append(" JOIN ").append(getCompanyId()).append(".quote q ON q.id = qi.quote_id \n");
        sql.append(" JOIN ").append(getCompanyId()).append(".salequote sq ON sq.id = q.id \n");
        sql.append(" JOIN ").append(getCompanyId()).append(".item it ON it.id = qi.item_id \n");
        sql.append(" JOIN ").append(getCompanyId()).append(".picklist pl ON pl.salequote_id = sq.id \n");
        sql.append(" JOIN ").append(getCompanyId()).append(".warehouse w ON qi.warehouseid = w.id \n");
        sql.append(" WHERE qi.deleted is not true AND it.id = :productId \n ");
        sql.append(" AND w.id = :warehouseId \n");
        if (entityId != null) {
            sql.append(" AND pl.id != " + entityId);
        }

        Object[] total = (Object[]) slaveEntityManager.createNativeQuery(sql.toString())
                .setParameter("productId", productId)
                .setParameter("warehouseId", warehouseId)
                .getResultList().get(0);

        SelectItem item = new SelectItem();
        item.setTotalAmount(total[0] != null ? ((BigDecimal) total[0]).doubleValue() : BigDecimal.ZERO.doubleValue());
        item.setDescription(total[1] != null && ((String) total[1]).length() > 0 ? (String) total[1] : "");

        return item;
    }

    @Override
    public List<EdsSaleQuote> getSaleQuotes(ListingFilterParameter fp, List<Integer> statusIds) {
        Map<String, Object> param = new HashMap<>();
        param.put("clientId", fp.getClientId());
        param.put("fromDate", ServerUtils.parseFilterParameterDate(fp.getStartDateNC()));
        param.put("toDate", ServerUtils.parseFilterParameterDate(fp.getEndDateNC()));
        param.put("taxCalcType", fp.getRelationID());

        StringBuilder sql = new StringBuilder();
        sql.append("select sq from EdsSaleQuote sq where " + ServerUtils.checkForDeleted("sq.deleted") + " and " + ServerUtils.checkForDeleted("sq.progressInvoicing"));
        sql.append(" and sq.client.objectID = :clientId");
        sql.append(" and " + (fp.getCategoryID() != null && fp.getCategoryID() == 1 ? "sq.dueDate" : "sq.invoiceDate") + " between :fromDate and :toDate ");
        sql.append(" and sq.taxCalculationType = :taxCalcType ");

        if (fp.getType() != null && fp.getType() == 1) {
            sql.append(" and sq.isSalesOrder = true ");
        } else if (fp.getType() != null && fp.getType() == 2) {
            sql.append(" and (sq.isSalesOrder <> true or sq.isSalesOrder is null) ");
        }

        if (statusIds != null && statusIds.size() > 0) {
            sql.append(" and sq.status.objectID IN ('" + ServerUtils.getAsCommoDelimited(statusIds, "0", "','") + "') ");
        }

        if (fp.getCustomFields() != null && !fp.getCustomFields().isEmpty()) {
            for (String key : fp.getCustomFields().keySet()) {
                if (key != null) {
                    String[] keys = key.split("_");
                    String keyFinal = keys[0] + StringUtils.capitalize(keys[1]);
                    sql.append(" AND sq.customFields." + keyFinal + " IN ('" + fp.getCustomFields().get(key) + "') ");
                }
            }
        }

        return findByNamedParams(sql.toString(), param);
    }

    @Override
    public List<SelectItem> getGroupedItems(List<Integer> Ids, HashMap<String, Boolean> fieldsForName, HashMap<String, Boolean> fieldsForDesc) {
        StringBuilder queryFieldsForName = new StringBuilder();

        for (String key : fieldsForName.keySet()) {
            if (StringUtils.isNotBlank(queryFieldsForName.toString())) {
                queryFieldsForName.append("||'-'||");
            }
            if (SOBaseInvoiceGroups.FIELDS.NUMBER.equals(key)) {
                queryFieldsForName.append("q.number");
            } else if (SOBaseInvoiceGroups.FIELDS.REFERENCE.equals(key)) {
                queryFieldsForName.append("q.reference");
            } else if (fieldsForName.get(key).booleanValue()) {
                String alias = "cf." + key;
                if (key.startsWith("date")) {
                    queryFieldsForName.append("(case when " + alias + " is not null then to_char(" + alias + " 'yyyy-MM-dd') else '' end)");
                } else if (key.startsWith("string")) {
                    queryFieldsForName.append("coalesce(" + alias + ", '')");
                } else {
                    queryFieldsForName.append(alias);
                }
            }
        }

        StringBuilder queryFieldsForDesc = new StringBuilder();
        if (fieldsForDesc != null && !fieldsForDesc.isEmpty()) {
            for (String key : fieldsForDesc.keySet()) {
                if (StringUtils.isNotBlank(queryFieldsForDesc.toString())) {
                    queryFieldsForDesc.append("||'-'||");
                }
                if (SOBaseInvoiceGroups.FIELDS.REFERENCE.equals(key)) {
                    queryFieldsForDesc.append("q.reference");
                } else if (SOBaseInvoiceGroups.FIELDS.DATE.equals(key)) {
                    queryFieldsForDesc.append("to_char(q.invoiceDate, 'yyyy-MM-dd')");
                } else if (SOBaseInvoiceGroups.FIELDS.PO_NUMBER.equals(key)) {
                    queryFieldsForDesc.append("coalesce(q.poNumber,'')");
                } else if (SOBaseInvoiceGroups.FIELDS.PROJECT.equals(key)) {
                    queryFieldsForDesc.append("coalesce(p.name,'')");
                } else if (fieldsForDesc.get(key).booleanValue()) {
                    String alias = "cf." + key;
                    if (key.startsWith("date")) {
                        queryFieldsForDesc.append("(case when " + alias + " is not null then to_char(" + alias + " 'yyyy-MM-dd') else '' end)");
                    } else if (key.startsWith("string")) {
                        queryFieldsForDesc.append("coalesce(" + alias + ", '')");
                    } else {
                        queryFieldsForDesc.append(alias);
                    }
                }
            }
        }

        StringBuilder query = new StringBuilder("SELECT " + queryFieldsForName + " as name, ");

        if (StringUtils.isNotBlank(queryFieldsForDesc.toString())) {
            query.append(queryFieldsForDesc + " as desc, ");
        } else {
            query.append("cast('' as varchar(50)) as desc, ");
        }
        query.append("q.totalInInvoiceCurrency total FROM ").append(getCompanyId()).append(".quote q \n")
                .append("LEFT JOIN ").append(getCompanyId()).append(".project p ON p.id = q.relatedproject_id \n")
                .append("LEFT JOIN ").append(getCompanyId()).append(".invoicecustomfields cf ON cf.id = q.customfields_id \n");
        query.append("WHERE q.deleted is not true and q.id in (").append(ServerUtils.getAsCommoDelimited(Ids, "0", ",")).append(")");
        List<Object[]> list = findNative(query.toString());

        List<SelectItem> items = new ArrayList<>();
        for (Object[] objects : list) {
            SelectItem item = new SelectItem();
            item.setName((String) objects[0]);
            item.setDescription((String) objects[1]);
            item.setTotalAmount(objects[2] != null ? ((BigDecimal) objects[2]).doubleValue() : 0d);
            items.add(item);
        }
        return items;
    }

    @Override
    public List<Object[]> getGroupedItems(List<Integer> Ids, List<QIGroupingField> groupingFields) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT qi.item_id, qi.itemName ")
                .append(groupingFields.contains(QIGroupingField.PRICE) ?
                        ", qi.unitPrice, sum(case when qs.code = 'PARTIAL_SHIPPED' or qs.code = 'SHIPPED' then coalesce(qi.shippedQty,0.0) - coalesce(qi.convertedQty,0.0) else qi.qty end) quantity "
                        : ", sum(qi.unitPrice * (case when qs.code = 'PARTIAL_SHIPPED' or qs.code = 'SHIPPED' then coalesce(qi.shippedQty,0.0) - coalesce(qi.convertedQty,0.0) else qi.qty end)) as unitPrice, 1.0 quantity ")
                .append(groupingFields.contains(QIGroupingField.ACCOUNT) ? ", qi.account_id " : ", NULLIF(0,0) ").append(" as accountId ")
                .append(groupingFields.contains(QIGroupingField.TAX) ? ", qi.vat_id " : ", NULLIF(0,0) ").append(" as taxId ")
                .append(groupingFields.contains(QIGroupingField.DEPARTMENT) ? ", qi.departmentid " : ", NULLIF(0,0) ").append(" as departmentId ")
                .append(" FROM ").append(getCompanyId()).append(".quoteitem qi \n")
                .append("JOIN ").append(getCompanyId()).append(".quote q ON q.id = qi.quote_id \n")
                .append("JOIN ").append(getCompanyId()).append(".reference qs on qs.id = q.status_id \n")
                .append("WHERE q.deleted is not true AND q.type = '")
                .append(RECEIVABLE).append("' AND qi.deleted is not true ")
                .append("AND q.id in (").append(ServerUtils.getAsCommoDelimited(Ids, "0", ",")).append(") \n");
        sql.append("GROUP BY qi.item_id, qi.itemName ");
        if (groupingFields.contains(QIGroupingField.PRICE)) {
            sql.append(", qi.unitPrice ");
        }
        if (groupingFields.contains(QIGroupingField.ACCOUNT)) {
            sql.append(", qi.account_id ");
        }
        if (groupingFields.contains(QIGroupingField.TAX)) {
            sql.append(", qi.vat_id ");
        }
        if (groupingFields.contains(QIGroupingField.DEPARTMENT)) {
            sql.append(", qi.departmentid ");
        }

        return findNative(sql.toString());
    }

    @Override
    public List<Object[]> getGroupedItemsByName(List<Integer> Ids) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT qi.itemName, sum(qi.qty) as quantity, sum(qi.unitPrice * qi.qty) as net, sum(coalesce(qi.taxAmount,0.0)) as taxAmount, (sum(qi.unitPrice * qi.qty) + sum(coalesce(qi.taxAmount,0.0))) as totalAmount, max(coalesce(qi.vat_id, 0)) vatId ")
                .append(" FROM ").append(getCompanyId()).append(".quoteitem qi \n")
                .append("JOIN ").append(getCompanyId()).append(".quote q ON q.id = qi.quote_id \n")
                .append("WHERE q.deleted is not true AND q.type = '")
                .append(RECEIVABLE).append("' AND qi.deleted is not true ")
                .append("AND q.id in (").append(ServerUtils.getAsCommoDelimited(Ids, "0", ",")).append(") \n");
        sql.append("GROUP BY qi.itemName ");
        return findNative(sql.toString());
    }

    @Override
    public List<Integer> getQuotesByVat(Integer objectID) {
        StringBuilder sql = new StringBuilder("SELECT ii.quote_id from ");
        sql.append(getCompanyId()).append(".quoteItem ii ");
        sql.append(" left join ").append(getCompanyId()).append(".quote i on ii.quote_id = i.id");
        sql.append(" where ii.vat_id =").append(objectID);
        sql.append(" and ").append(ServerUtils.checkForDeleted("i.deleted"));
        sql.append(" group by ii.quote_id");
        return findNative(sql.toString());
    }

    @Override
    public List<Integer> getExpensesByVat(Integer objectID) {
        StringBuilder sql = new StringBuilder("SELECT ii.reportId from ");
        sql.append(getCompanyId()).append(".expense ii ");
        sql.append(" left join ").append(getCompanyId()).append(".expenseReport i on ii.reportId = i.id");
        sql.append(" where ii.taxId =").append(objectID);
        sql.append(" and ").append(ServerUtils.checkForDeleted("i.isDeleted"));
        sql.append(" group by ii.reportId ");
        return findNative(sql.toString());
    }

    @Override
    public List<Integer> getBankTransafersByVat(Integer objectID) {
        StringBuilder sql = new StringBuilder("SELECT spi.banktransferid from ");
        sql.append(getCompanyId()).append(".spendreceivemoneyitem spi ");
        sql.append(" left join ").append(getCompanyId()).append(".spendreceivemoney sp on spi.banktransferid = sp.id");
        sql.append(" where spi.taxId =").append(objectID);
        sql.append(" and ").append(ServerUtils.checkForDeleted("sp.deleted"));
        sql.append(" group by spi.banktransferid ");
        return findNative(sql.toString());
    }

    @Override
    public List<EdsSaleQuote> getSaleQuotesByCategoryId(Integer categoryId) {
        StringBuilder sql = new StringBuilder();
        sql.append("select distinct q.*,qt.*, 0 as clazz_ from ").append(getCompanyId()).append(".salequote q ");
        sql.append("join ").append(getCompanyId()).append(".quoteitem qi on qi.quote_id = q.id  ");
        sql.append("join ").append(getCompanyId()).append(".quote qt on q.id = qt.id ");
        sql.append("join ").append(getCompanyId()).append(".item i on i.id = qi.item_id ");
        sql.append("where qt.deleted is not true and i.categoryid = ").append(categoryId);
        sql.append(" and (q.issalesorder is null or q.issalesorder <> true)");

        return (List<EdsSaleQuote>) findNative(sql.toString(), EdsSaleQuote.class);
    }

    @Override
    public List<EdsSaleQuote> getSaleOrderByProductCategoryID(Integer categoryId) {
        StringBuilder sql = new StringBuilder();
        sql.append("select distinct q.*,qt.*, 0 as clazz_ from ").append(getCompanyId()).append(".salequote q ");
        sql.append("join ").append(getCompanyId()).append(".quoteitem qi on qi.quote_id = q.id  ");
        sql.append("join ").append(getCompanyId()).append(".quote qt on q.id = qt.id ");
        sql.append("join ").append(getCompanyId()).append(".item i on i.id = qi.item_id ");
        sql.append("where qt.deleted is not true and i.categoryid = ").append(categoryId);
        sql.append(" and q.issalesorder = true");

        return (List<EdsSaleQuote>) findNative(sql.toString(), EdsSaleQuote.class);
    }

    @Override
    public ArrayList<EdsQuote> getSaleQuotesByDetailed(List<Integer> ids) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT * FROM ").append(getCompanyId()).append(".quote q ")
                .append("WHERE q.deleted is not true ")
                .append("AND q.id in (").append(ServerUtils.getAsCommoDelimited(ids, "0", ",")).append(") \n")
                .append("Order by q.invoicedate");
        return (ArrayList<EdsQuote>) findNative(sql.toString(), EdsQuote.class);
    }

    public List<Integer> getCompanyDeletedQuotesForSolr(SolrReindexRpc solrReindex) {
        StringBuilder newsSqlQuery = new StringBuilder("SELECT ns.objectID FROM EdsSaleQuote ns WHERE ns.deleted=true");
        newsSqlQuery.append(" AND ns.updatedDate>=").append("'").append(solrReindex.getLastUpdateTime()).append("'");
        if (solrReindex.getLastUpdateEndTime() != null) {
            newsSqlQuery.append(" and ns.updatedDate<='").append(solrReindex.getLastUpdateEndTime()).append("'");
        }
        return (List<Integer>) find(newsSqlQuery.toString());
    }

    public List<Integer> getCompanyDeletedPurchaseOrdersForSolr(SolrReindexRpc solrReindex) {
        StringBuilder newsSqlQuery = new StringBuilder("SELECT ns.objectID FROM EdsPurchaseOrder ns WHERE ns.deleted=true");
        newsSqlQuery.append(" AND ns.updatedDate>=").append("'").append(solrReindex.getLastUpdateTime()).append("'");
        if (solrReindex.getLastUpdateEndTime() != null) {
            newsSqlQuery.append(" and ns.updatedDate<='").append(solrReindex.getLastUpdateEndTime()).append("'");
        }
        return (List<Integer>) find(newsSqlQuery.toString());
    }

    @Override
    public EdsQuote getOrderById(Integer quoteId) {
        StringBuilder sql = new StringBuilder("SELECT w.* FROM ").append(getCompanyId()).append(".quote w " +
                        "join ").append(getCompanyId()).append(".salequote q on q.id=w.id where w.id=").append(quoteId)
                .append(" and q.issalesorder = true");
        return (EdsQuote) findSingle(sql.toString());
    }

    @Override
    public Map<Integer, EdsQuoteItem> getQuoteItemsByIds(Set<Integer> quoteItemIds) {
        Map params = new HashMap();
        params.put("ids", quoteItemIds);
        List<EdsQuoteItem> list = findByNamedParams("SELECT q.quoteItems FROM EdsQuote q WHERE q.deleted IS NOT TRUE AND q.objectID in (:ids)", params);

        return list.stream().collect(Collectors.toMap(EdsBaseInvoiceItem::getObjectID, obj -> obj));
    }
}
