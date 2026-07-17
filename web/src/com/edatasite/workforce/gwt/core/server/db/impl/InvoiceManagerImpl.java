package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.*;
import com.edatasite.workforce.core.domain.accounting.*;
import com.edatasite.workforce.core.domain.crm.EdsCrmAccount;
import com.edatasite.workforce.core.domain.crm.EdsCustomCrmAccount;
import com.edatasite.workforce.core.domain.enums.EntityTypeEnum;
import com.edatasite.workforce.gwt.accounting.client.rpc.BillableExpenseItem;
import com.edatasite.workforce.gwt.accounting.client.rpc.FindMatchFilterData;
import com.edatasite.workforce.gwt.accounting.client.rpc.ProductKitItem;
import com.edatasite.workforce.gwt.accounting.client.rpc.discount.DiscountItem;
import com.edatasite.workforce.gwt.accounting.client.rpc.enums.ReceiveTypeEnum;
import com.edatasite.workforce.gwt.accounting.client.rpc.product.AssemblyItem;
import com.edatasite.workforce.gwt.accounting.client.ui.AccountingConstants;
import com.edatasite.workforce.gwt.core.client.enums.GenericSettingsEnum;
import com.edatasite.workforce.gwt.core.client.rpc.CompanyCustomFieldItem;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.TaxItem;
import com.edatasite.workforce.gwt.core.client.rpc.accounting.AccountItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.rpc.solr.SolrReindexRpc;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.ListLoadConfig;
import com.edatasite.workforce.gwt.core.client.ui.SchedulerConstant;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.db.*;
import com.edatasite.workforce.gwt.core.server.db.settings.GenericSettingsManager;
import com.edatasite.workforce.gwt.core.server.utils.CustomFieldsUtils;
import com.edatasite.workforce.gwt.dashboard.client.rpc.ClientsByAmmount;
import com.edatasite.workforce.gwt.invoice.client.rpc.*;
import com.edatasite.workforce.gwt.trainingcenter.client.rpc.TCScheduleItem;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gwt.user.server.rpc.security.ServerSecurityContext;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.Query;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Repository("invoiceManager")
public class InvoiceManagerImpl extends BaseManager<EdsInvoice> implements InvoiceManager, AccountingConstants, Constants {
    @Autowired
    private RoleManager roleManager;
    @Autowired
    private ReferenceManager referenceManager;
    @Autowired
    private RecurrenceManager recurrenceManager;
    @Autowired
    private JdbcSpringManager jdbcSpringManager;
    @Autowired
    private GenericSettingsManager genericSettingsManager;
    @Autowired
    private FinancialSettingsManager financialSettingsManager;
    @Autowired
    private AccountingManager accountingManager;
    @Autowired
    private CustomCrmAccountManager customCrmAccountManager;

    public InvoiceManagerImpl() {
        super(EdsInvoice.class);
    }

    @Override
    public List<Integer> getInvoiceIdsByIDs(String ids) {
        return find("SELECT si.objectID FROM EdsSaleInvoice si WHERE si.objectID IN(" + ids + ") AND si.status IS NOT NULL AND si.status.code!='REVERSED'");
    }

    @Override
    public List<Integer> getPurchaseInvoiceIdsByIDs(String ids) {
        return find("SELECT pi.objectID FROM EdsPurchaseInvoice pi WHERE pi.objectID IN(" + ids + ")");
    }

    @Override
    public List<Integer> getCompanyInoviceIdsWithLimit(Integer companyID, int startat, int limit) {
        return findLimited("SELECT si.objectID FROM EdsSaleInvoice si WHERE si.objectID > ? AND " + ServerUtils.checkForDeleted("si.deleted") + " AND si.status IS NOT NULL AND si.status.code!='REVERSED' ORDER BY si.objectID ASC", limit, startat);
    }

    @Override
    public List<Integer> getPurchaseInvoiceIdsWithLimit(Integer startat, Integer limit) {
        return findLimited("select pi.objectID from EdsPurchaseInvoice pi where pi.objectID > ? and " + ServerUtils.checkForDeleted("pi.deleted") + " order by pi.objectID ASC", limit, startat);
    }

    public Long getCountPurchaseInvoiceList() {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT COUNT(pi.objectID) FROM EdsPurchaseInvoice pi ");
        sql.append("WHERE ").append(ServerUtils.checkForDeleted("pi.deleted"));
        sql.append(" AND pi.status IS NOT NULL AND pi.status.code!='REVERSED'");
        return (Long) findSingle(sql.toString());
    }

    public List<EdsBaseSaleInvoice> getSaleInvoiceList(ListingFilterParameter fp) {
        if (fp == null) {
            fp = new ListingFilterParameter();
        }
        Map<String, Object> param = new HashMap<>();

        StringBuilder sql = new StringBuilder();
        sql.append("select distinct si from EdsInvoiceItem item join item.invoice inv, EdsSaleInvoice si ");

        //Below we are getting access only to PM.
        EdsUser user = getUser();
        if (fp.isLookUp() && user != null && roleManager.hasRole(user, PM) && !roleManager.hasEitherRoles(user, ACCOUNTANT, DR, ADMIN)) {
            sql.append(" left join si.relatedProject pr where " + ServerUtils.checkForDeleted("si.deleted") + " and (pr.manager = :user or pr.backupManager = :user");
            sql.append(" or pr.backupManager2 = :user  or pr.backupManager3 = :user  or pr.backupManager4 = :user");
            sql.append(" or pr.backupManager5 = :user or pr.backupManager6 = :user or pr.backupManager7 = :user");
            sql.append(" or pr.backupManager8 = :user or pr.backupManager9 = :user or pr.backupManager10 = :user");
            sql.append(" or si.creator = :user) ");
            param.put("user", user);
        } else {
            sql.append(" where " + ServerUtils.checkForDeleted("si.deleted"));
            sql.append(" and inv.id = si.id ");
            sql.append(" and inv.type = '").append(RECEIVABLE).append("' ");
        }
        if (fp.getInvoiceStatusId() != null) {
            sql.append(" and si.status.objectID = :statusID");
            param.put("statusID", fp.getInvoiceStatusId());
        }
        if (fp.getObjectIDs() != null && !fp.getObjectIDs().isEmpty()) {
            sql.append(" and si.id in (:ID)");
            param.put("ID", fp.getObjectIDs());
        }
        if (fp.getExcludedType() != null) {
            String[] status = fp.getExcludedType().split(",");
            sql.append(" and si.status.code not in ('").append(ServerUtils.getAsCommoDelimited(Arrays.asList(status), "", "','")).append("')");
        }
        if (fp.getProjectId() != null) {
            if (genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.PROJECT_IN_LINE_ITEM_ENABLE)) {
                sql.append(" and item.project.objectID in (:projectID)");
            } else {
                sql.append(" and si.relatedProject.objectID in (:projectID)");
            }
            if (fp.getProjectIdList() != null && fp.getProjectIdList().size() > 0) {
                param.put("projectID", fp.getProjectIdList());
            } else {
                param.put("projectID", fp.getProjectId());
            }
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
            sql.append(" and (lower(si.number) like '").append(fp.getSqlSearchKey()).append("' or ");
            sql.append(" lower(si.client.name) like '").append(fp.getSqlSearchKey()).append("' or ");
            sql.append(" lower (si.currency.name) like '").append(fp.getSqlSearchKey()).append("' or ");
            sql.append(" lower (si.status.name) like '").append(fp.getSqlSearchKey()).append("') ");
        }

        sql.append(" and si.status is not null and si.status.code!='REVERSED' ");

        if ("date".equalsIgnoreCase(fp.getSortField())) {
            sql.append(" order by si.invoiceDate desc ");
        } else {
            sql.append(" order by si.objectID desc ");
        }

        return findIntervalByNamedParams(sql.toString(), fp.getStart(), fp.getLimit(), param);
    }

    public List<EdsSaleInvoice> getSaleInvoiceListForSolr(SolrReindexRpc solrReindex, Integer start, Integer limit) {
        Map<String, Object> params = new HashMap<>();
        StringBuilder saleInvoiceSqlQuery = new StringBuilder("select si from EdsSaleInvoice si where " + ServerUtils.checkForDeleted("si.deleted") + " and si.status is not null and si.status.code!='REVERSED' ");
        if (!solrReindex.isAllReindex() && solrReindex.getLastUpdateTime() != null) {
            params.put("modifiedDate", solrReindex.getLastUpdateTime());
            saleInvoiceSqlQuery.append(" and si.invoiceDate >= :modifiedDate");
            if (solrReindex.getLastUpdateEndTime() != null) {
                saleInvoiceSqlQuery.append(" and si.invoiceDate<='").append(solrReindex.getLastUpdateEndTime()).append("'");
            }
        }
        saleInvoiceSqlQuery.append(" order by si.objectID asc");
        return findIntervalByNamedParams(saleInvoiceSqlQuery.toString(), start, limit, params);
    }

    public List<EdsBaseSaleInvoice> getRecurringInvoiceList(ListingFilterParameter fp, ListLoadConfig config) {
        if (fp == null) {
            fp = new ListingFilterParameter();
        }
        Calendar currentDate = new GregorianCalendar();

        Calendar calendar = new GregorianCalendar();
        calendar.add(Calendar.YEAR, -5);

        String schema = ServerSecurityContext.getInstance().getCompanyId();
        StringBuilder sql = new StringBuilder();
        sql.append("select si from EdsRecurringInvoice si ");
        sql.append(", EdsRecurrence r");
        sql.append(" where 1=1 and r.busObjectId = si.objectID and r.job.objectID=" + SchedulerConstant.RECURRING_INVOICE_REMINDER + " and r.companyID =").append(schema);
        if (fp.getRecurrenceStatus() != null) {
            List<EdsBaseSaleInvoice> items = getRecurrinSaleInvoiceWhichOccurence(fp, config);
            StringBuilder ins = new StringBuilder();
            if (items != null && items.size() > 0) {
                for (EdsBaseSaleInvoice item : items) {
                    ins.append(ins.toString() != "" ? "," : "").append(item.getObjectID());
                }
                ins = new StringBuilder("si.objectID in (" + ins + ") or ");
            }
            if (fp.getRecurrenceStatus().equals(SchedulerConstant.SUCCESS)) {
                sql.append(" and (" + ins + "(r.endDate is not null and r.endDate >= '" + currentDate.getTime().toString() + "') or (r.endType=" + SchedulerConstant.NO_END_DATE + " and r.startDate > '" + calendar.getTime().toString() + "'))");
            } else {
                sql.append(" and (" + ins + "(r.endDate is not null and r.endDate < '" + currentDate.getTime().toString() + "') or (r.endType=" + SchedulerConstant.NO_END_DATE + " and r.startDate < '" + calendar.getTime().toString() + "'))");
            }
        }

        sql.append(" and ").append(ServerUtils.checkForDeleted("si.deleted")).append(" ");
        if (fp.getInvoiceStatusId() != null) {
            sql.append(" and si.status.objectID ='").append(fp.getInvoiceStatusId()).append("'");
        }

        if (fp.getInvoiceClientId() != null && fp.getInvoiceClientId() > 0) {
            sql.append(" and si.client.objectID ='" + fp.getInvoiceClientId() + "'");
        } else if (fp.getClientId() != null && fp.getClientId() > 0) {
            sql.append(" and si.client.objectID ='").append(fp.getClientId()).append("'");
        }
        if (fp.getStartDate() != null && fp.getEndDate() != null) {
            sql.append(" and (si.invoiceDate >='").append(fp.getStartDate()).append("' and si.invoiceDate <='").append(fp.getEndDate()).append("')");
        } else if (fp.getStartDate() != null) {
            sql.append(" and (si.invoiceDate >='").append(fp.getStartDate()).append("')");
        } else if (fp.getEndDate() != null) {
            sql.append(" and (si.invoiceDate <='").append(fp.getEndDate()).append("')");
        }
        if (fp.getSqlSearchKey() != null) {
            sql.append(" and (lower(si.number) like '").append(fp.getSqlSearchKey()).append("' or ");
            sql.append(" lower(si.client.name) like '" + fp.getSqlSearchKey() + "' or ");
            sql.append(" lower (si.currency.name) like '" + fp.getSqlSearchKey() + "' or ");
            sql.append(" lower (si.status.name) like '" + fp.getSqlSearchKey() + "') ");
        }

        sql.append(" and si.status is not null and si.status.code!='REVERSED' ");
        if (config != null) {
            if (config.getSortField() != null) {
                if (AMOUNT_COLUMN.equals(config.getSortField())) {
                    sql.append("order by si.total " + (config.getSortDir() == 2 ? "desc" : ""));
                } else if (CLIENT_COLUMN.equals(config.getSortField())) {
                    sql.append("order by si.client.name " + (config.getSortDir() == 2 ? "desc" : ""));
                } else if (STATUS_COLUMN.equals(config.getSortField())) {
                    sql.append("order by si.status.name " + (config.getSortDir() == 2 ? "desc" : ""));
                } else {
                    sql.append("order by si.objectID " + (config.getSortDir() == 2 ? "desc" : ""));
                }
            } else {
                sql.append("order by si.objectID desc");
            }
        } else {
            sql.append("order by si.objectID desc");
        }

        return findInterval(sql.toString(), fp.getStart(), fp.getLimit());
    }

    private List<EdsBaseSaleInvoice> getRecurrinSaleInvoiceWhichOccurence(ListingFilterParameter fp, ListLoadConfig config) {
        if (fp == null) {
            fp = new ListingFilterParameter();
        }
        String schema = ServerSecurityContext.getInstance().getCompanyId();
        StringBuilder sql = new StringBuilder();
        sql.append("select si from EdsRecurringInvoice si, EdsRecurrence r ");
        sql.append(" where r.busObjectId = si.objectID and r.companyID =" + schema + " and r.job.objectID=" + SchedulerConstant.RECURRING_INVOICE_REMINDER + " and r.endType=" + SchedulerConstant.END_AFTER_OCCURRENCES);
        sql.append(" and ").append(ServerUtils.checkForDeleted("si.deleted")).append(" ");
        if (fp.getInvoiceStatusId() != null) {
            sql.append(" and si.status.objectID ='" + fp.getInvoiceStatusId() + "'");
        }
        if (fp.getInvoiceClientId() != null && fp.getInvoiceClientId() > 0) {
            sql.append(" and si.client.objectID ='" + fp.getInvoiceClientId() + "'");
        } else if (fp.getClientId() != null && fp.getClientId() > 0) {
            sql.append(" and si.client.objectID ='" + fp.getClientId() + "'");
        }
        if (fp.getStartDate() != null && fp.getEndDate() != null) {
            sql.append(" and (si.invoiceDate >='" + fp.getStartDate() + "' and si.invoiceDate <='" + fp.getEndDate() + "')");
        } else if (fp.getStartDate() != null) {
            sql.append(" and (si.invoiceDate >='" + fp.getStartDate() + "')");
        } else if (fp.getEndDate() != null) {
            sql.append(" and (si.invoiceDate <='" + fp.getEndDate() + "')");
        }
        if (fp.getSqlSearchKey() != null) {
            sql.append(" and (lower(si.number) like '" + fp.getSqlSearchKey() + "' or ");
            sql.append(" lower(si.client.name) like '" + fp.getSqlSearchKey() + "' or ");
            sql.append(" lower (si.currency.name) like '" + fp.getSqlSearchKey() + "' or ");
            sql.append(" lower (si.status.name) like '" + fp.getSqlSearchKey() + "') ");
        }

        sql.append(" and si.status is not null and si.status.code!='REVERSED' ");
        List<EdsBaseSaleInvoice> items = find(sql.toString());
        List<EdsBaseSaleInvoice> result = new ArrayList<>();
        Calendar calendar = new GregorianCalendar();
        for (EdsBaseSaleInvoice item : items) {
            EdsRecurrence recurrence = recurrenceManager.getRecurrenceJob(SchedulerConstant.RECURRING_INVOICE_REMINDER, item.getObjectID(), Integer.parseInt(schema));
            Date date = recurrenceManager.getTriggerEndDate(recurrence, true);
            if (fp.getRecurrenceStatus().equals(SchedulerConstant.SUCCESS) && calendar.getTime().before(date)) {
                result.add(item);
            } else if (!fp.getRecurrenceStatus().equals(SchedulerConstant.SUCCESS) && calendar.getTime().after(date)) {
                result.add(item);
            }
        }
        return result;
    }

    @Override
    public List<EdsRecurringBill> getRecurringBillList(ListingFilterParameter fp, ListLoadConfig config, EdsCompany company) {
        if (fp == null) {
            fp = new ListingFilterParameter();
        }
        Calendar currentDate = new GregorianCalendar();

        Calendar calendar = new GregorianCalendar();
        calendar.add(Calendar.YEAR, -5);

        String schema = ServerSecurityContext.getInstance().getCompanyId();
        StringBuilder sql = new StringBuilder();
        sql.append("select rb from EdsRecurringBill rb , EdsRecurrence r ");
        sql.append(" where 1=1 and r.busObjectId = rb.objectID and r.job.objectID=" + SchedulerConstant.RECURRING_BILL_REMINDER + " and r.companyID =" + schema);

        if (fp.getRecurrenceStatus() != null) {
            List<EdsRecurringBill> items = getRecurringBillWhichOccurence(fp, config);
            StringBuilder ins = new StringBuilder();
            if (items != null && items.size() > 0) {
                for (EdsRecurringBill item : items) {
                    ins.append(ins.toString() != "" ? "," : "").append(item.getObjectID());
                }
                ins = new StringBuilder("rb.objectID in (" + ins + ") or ");
            }
            if (fp.getRecurrenceStatus().equals(SchedulerConstant.SUCCESS)) {
                sql.append(" and (" + ins + "(r.endDate is not null and r.endDate >= '" + currentDate.getTime().toString() + "') or (r.endType=" + SchedulerConstant.NO_END_DATE + " and r.startDate > '" + calendar.getTime().toString() + "'))");
            } else {
                sql.append(" and (" + ins + "(r.endDate is not null and r.endDate < '" + currentDate.getTime().toString() + "') or (r.endType=" + SchedulerConstant.NO_END_DATE + " and r.startDate < '" + calendar.getTime().toString() + "'))");
            }
        }

        sql.append(" and ").append(ServerUtils.checkForDeleted("rb.deleted")).append(" ");
        if (fp.getInvoiceStatusId() != null) {
            sql.append(" and rb.status.objectID ='" + fp.getInvoiceStatusId() + "'");
        }

        if (fp.getInvoiceClientId() != null && fp.getInvoiceClientId() > 0) {
            sql.append(" and rb.supplier.objectID ='" + fp.getInvoiceClientId() + "'");
        } else if (fp.getClientId() != null && fp.getClientId() > 0) {
            sql.append(" and rb.supplier.objectID ='" + fp.getClientId() + "'");
        }
        if (fp.getStartDate() != null && fp.getEndDate() != null) {
            sql.append(" and (rb.invoiceDate >='" + fp.getStartDate() + "' and rb.invoiceDate <='" + fp.getEndDate() + "')");
        } else if (fp.getStartDate() != null) {
            sql.append(" and (rb.invoiceDate >='" + fp.getStartDate() + "')");
        } else if (fp.getEndDate() != null) {
            sql.append(" and (rb.invoiceDate <='" + fp.getEndDate() + "')");
        }
        if (fp.getSqlSearchKey() != null) {
            sql.append(" and (lower(rb.number) like '" + fp.getSqlSearchKey() + "' or ");
            sql.append(" lower(rb.supplier.name) like '" + fp.getSqlSearchKey() + "' or ");
            sql.append(" lower (rb.currency.name) like '" + fp.getSqlSearchKey() + "' or ");
            sql.append(" lower (rb.status.name) like '" + fp.getSqlSearchKey() + "') ");
        }

        sql.append(" and rb.status is not null and rb.status.code!='REVERSED' ");
        if (config != null) {
            if (config.getSortField() != null) {
                if (AMOUNT_COLUMN.equals(config.getSortField())) {
                    sql.append("order by rb.total " + (config.getSortDir() == 2 ? "desc" : ""));
                } else if (CLIENT_COLUMN.equals(config.getSortField())) {
                    sql.append("order by rb.supplier.name " + (config.getSortDir() == 2 ? "desc" : ""));
                } else if (STATUS_COLUMN.equals(config.getSortField())) {
                    sql.append("order by rb.status.name " + (config.getSortDir() == 2 ? "desc" : ""));
                } else {
                    sql.append("order by rb.objectID " + (config.getSortDir() == 2 ? "desc" : ""));
                }
            }
        } else {
            sql.append("order by rb.objectID desc");
        }
        return find(sql.toString());
    }

    private List<EdsRecurringBill> getRecurringBillWhichOccurence(ListingFilterParameter fp, ListLoadConfig config) {
        if (fp == null) {
            fp = new ListingFilterParameter();
        }
        String schema = ServerSecurityContext.getInstance().getCompanyId();
        StringBuilder sql = new StringBuilder();
        sql.append("select rb from EdsRecurringBill rb, EdsRecurrence r ");
        sql.append(" where r.busObjectId = rb.objectID and r.companyID =" + schema + " and r.job.objectID=" + SchedulerConstant.RECURRING_BILL_REMINDER + " and r.endType=" + SchedulerConstant.END_AFTER_OCCURRENCES);
        sql.append(" and ").append(ServerUtils.checkForDeleted("rb.deleted")).append(" ");
        if (fp.getInvoiceStatusId() != null) {
            sql.append(" and rb.status.objectID ='" + fp.getInvoiceStatusId() + "'");
        }
        if (fp.getInvoiceClientId() != null && fp.getInvoiceClientId() > 0) {
            sql.append(" and rb.supplier.objectID ='" + fp.getInvoiceClientId() + "'");
        } else if (fp.getClientId() != null && fp.getClientId() > 0) {
            sql.append(" and rb.supplier.objectID ='" + fp.getClientId() + "'");
        }
        if (fp.getStartDate() != null && fp.getEndDate() != null) {
            sql.append(" and (rb.invoiceDate >='" + fp.getStartDate() + "' and rb.invoiceDate <='" + fp.getEndDate() + "')");
        } else if (fp.getStartDate() != null) {
            sql.append(" and (rb.invoiceDate >='" + fp.getStartDate() + "')");
        } else if (fp.getEndDate() != null) {
            sql.append(" and (rb.invoiceDate <='" + fp.getEndDate() + "')");
        }
        if (fp.getSqlSearchKey() != null) {
            sql.append(" and (lower(rb.number) like '" + fp.getSqlSearchKey() + "' or ");
            sql.append(" lower(rb.supplier.name) like '" + fp.getSqlSearchKey() + "' or ");
            sql.append(" lower (rb.currency.name) like '" + fp.getSqlSearchKey() + "' or ");
            sql.append(" lower (rb.status.name) like '" + fp.getSqlSearchKey() + "') ");
        }

        sql.append(" and rb.status is not null and rb.status.code!='REVERSED' ");
        List<EdsRecurringBill> items = find(sql.toString());
        List<EdsRecurringBill> result = new ArrayList<>();
        Calendar calendar = new GregorianCalendar();
        for (EdsRecurringBill item : items) {
            EdsRecurrence recurrence = recurrenceManager.getRecurrenceJob(SchedulerConstant.RECURRING_BILL_REMINDER, item.getObjectID(), Integer.parseInt(schema));
            Date date = recurrenceManager.getTriggerEndDate(recurrence, true);
            if (fp.getRecurrenceStatus().equals(SchedulerConstant.SUCCESS) && calendar.getTime().before(date)) {
                result.add(item);
            } else if (!fp.getRecurrenceStatus().equals(SchedulerConstant.SUCCESS) && calendar.getTime().after(date)) {
                result.add(item);
            }
        }
        return result;
    }

    @Override
    public Long getTotalRecurringSaleInvoiceList(ListingFilterParameter fp, ListLoadConfig listLoadConfig, EdsCompany company, boolean isRecurringInvoice) {
        if (fp == null) {
            fp = new ListingFilterParameter();
        }
        Calendar currentDate = new GregorianCalendar();

        Calendar calendar = new GregorianCalendar();
        calendar.add(Calendar.YEAR, -5);

        StringBuilder sql = new StringBuilder();
        sql.append("select count(si.objectID) from " + (isRecurringInvoice ? "EdsRecurringInvoice " : "EdsSaleInvoice ") + " si ");
        if (isRecurringInvoice) {
            sql.append(", EdsRecurrence r");
            sql.append(" where 1=1 and r.busObjectId = si.objectID and r.job.objectID=" + SchedulerConstant.RECURRING_INVOICE_REMINDER + " and r.companyID =" + company.getObjectID());
            if (fp.getRecurrenceStatus() != null) {
                List<EdsBaseSaleInvoice> items = getRecurrinSaleInvoiceWhichOccurence(fp, listLoadConfig);
                StringBuilder ins = new StringBuilder();
                if (items != null && items.size() > 0) {
                    for (EdsBaseSaleInvoice item : items) {
                        ins.append(ins.toString() != "" ? "," : "").append(item.getObjectID());
                    }
                    ins = new StringBuilder("si.objectID in (" + ins + ") or ");
                }
                if (fp.getRecurrenceStatus().equals(SchedulerConstant.SUCCESS)) {
                    sql.append(" and (" + ins + "(r.endDate is not null and r.endDate >= '" + currentDate.getTime().toString() + "') or (r.endType=" + SchedulerConstant.NO_END_DATE + " and r.startDate > '" + calendar.getTime().toString() + "'))");
                } else {
                    sql.append(" and (" + ins + " (r.endDate is not null and r.endDate < '" + currentDate.getTime().toString() + "') or (r.endType=" + SchedulerConstant.NO_END_DATE + " and r.startDate < '" + calendar.getTime().toString() + "'))");
                }
            }
        } else {
            sql.append(" where 1=1 ");
        }
        sql.append(" and ").append(ServerUtils.checkForDeleted("si.deleted")).append(" ");
        if (fp.getInvoiceStatusId() != null) {
            sql.append(" and si.status.objectID ='" + fp.getInvoiceStatusId() + "'");
        }

        if (fp.getInvoiceClientId() != null && fp.getInvoiceClientId() > 0) {
            sql.append(" and si.client.objectID ='" + fp.getInvoiceClientId() + "'");

        } else if (fp.getClientId() != null && fp.getClientId() > 0) {
            sql.append(" and si.client.objectID ='" + fp.getClientId() + "'");

        }

        if (fp.getStartDate() != null && fp.getEndDate() != null) {
            sql.append(" and (si.invoiceDate >='" + fp.getStartDate() + "' and si.invoiceDate <='" + fp.getEndDate() + "')");

        } else if (fp.getStartDate() != null) {
            sql.append(" and (si.invoiceDate >='" + fp.getStartDate() + "')");

        } else if (fp.getEndDate() != null) {
            sql.append(" and (si.invoiceDate <='" + fp.getEndDate() + "')");

        }

        if (fp.getSqlSearchKey() != null) {
            sql.append(" and (lower(si.number) like '" + fp.getSqlSearchKey() + "' or ");
            sql.append(" lower(si.client.name) like '" + fp.getSqlSearchKey() + "' or ");
            sql.append(" lower (si.currency.name) like '" + fp.getSqlSearchKey() + "' or ");
            sql.append(" lower (si.status.name) like '" + fp.getSqlSearchKey() + "') ");
        }
        sql.append(" and si.status is not null and si.status.code!='REVERSED' ");

        return (Long) findSingle(sql.toString());

    }

    @Override
    public Long getTotalRecurringBillList(ListingFilterParameter fp, ListLoadConfig listLoadConfig, EdsCompany company) {
        if (fp == null) {
            fp = new ListingFilterParameter();
        }
        Calendar currentDate = new GregorianCalendar();

        Calendar calendar = new GregorianCalendar();
        calendar.add(Calendar.YEAR, -5);

        StringBuilder sql = new StringBuilder();
        sql.append("select count(rb.objectID) from EdsRecurringBill rb , EdsRecurrence r ");
        sql.append("where 1=1 and r.busObjectId = rb.objectID and r.job.objectID=" + SchedulerConstant.RECURRING_BILL_REMINDER + " and r.companyID =" + company.getObjectID());
        if (fp.getRecurrenceStatus() != null) {
            List<EdsBaseSaleInvoice> items = getRecurrinSaleInvoiceWhichOccurence(fp, listLoadConfig);
            StringBuilder ins = new StringBuilder();
            if (items != null && items.size() > 0) {
                for (EdsBaseSaleInvoice item : items) {
                    ins.append(ins.toString() != "" ? "," : "").append(item.getObjectID());
                }
                ins = new StringBuilder("rb.objectID in (" + ins + ") or ");
            }
            if (fp.getRecurrenceStatus().equals(SchedulerConstant.SUCCESS)) {
                sql.append(" and (" + ins + "(r.endDate is not null and r.endDate >= '" + currentDate.getTime().toString() + "') or (r.endType=" + SchedulerConstant.NO_END_DATE + " and r.startDate > '" + calendar.getTime().toString() + "'))");
            } else {
                sql.append(" and (" + ins + " (r.endDate is not null and r.endDate < '" + currentDate.getTime().toString() + "') or (r.endType=" + SchedulerConstant.NO_END_DATE + " and r.startDate < '" + calendar.getTime().toString() + "'))");
            }
        }
        sql.append(" and ").append(ServerUtils.checkForDeleted("rb.deleted")).append(" ");
        if (fp.getInvoiceStatusId() != null) {
            sql.append(" and rb.status.objectID ='" + fp.getInvoiceStatusId() + "'");
        }

        if (fp.getInvoiceClientId() != null && fp.getInvoiceClientId() > 0) {
            sql.append(" and rb.supplier.objectID ='" + fp.getInvoiceClientId() + "'");

        } else if (fp.getClientId() != null && fp.getClientId() > 0) {
            sql.append(" and rb.supplier.objectID ='" + fp.getClientId() + "'");

        }

        if (fp.getStartDate() != null && fp.getEndDate() != null) {
            sql.append(" and (rb.invoiceDate >='" + fp.getStartDate() + "' and rb.invoiceDate <='" + fp.getEndDate() + "')");

        } else if (fp.getStartDate() != null) {
            sql.append(" and (rb.invoiceDate >='" + fp.getStartDate() + "')");

        } else if (fp.getEndDate() != null) {
            sql.append(" and (rb.invoiceDate <='" + fp.getEndDate() + "')");

        }

        if (fp.getSqlSearchKey() != null) {
            sql.append(" and (lower(rb.number) like '" + fp.getSqlSearchKey() + "' or ");
            sql.append(" lower(rb.supplier.name) like '" + fp.getSqlSearchKey() + "' or ");
            sql.append(" lower (rb.currency.name) like '" + fp.getSqlSearchKey() + "' or ");
            sql.append(" lower (rb.status.name) like '" + fp.getSqlSearchKey() + "') ");
        }
        sql.append(" and rb.status is not null and rb.status.code!='REVERSED' ");

        return (Long) findSingle(sql.toString());
    }

    @Override
    public void removeInvoiceItems(Integer invoiceID) {
        update("update from EdsInvoiceItem set deleted='true' where invoice.objectID = ?", invoiceID);
    }

    @Override
    public EdsInvoicePayment getInvoicePaymentByCreditNote(EdsInvoice creditNote) {
        return (EdsInvoicePayment) findSingle("select p from EdsInvoicePayment p where p.creditNote = ?", creditNote);
    }

    public List<EdsPurchaseInvoice> getPurchaseInvoiceList(ListingFilterParameter fp, boolean isConversionBalance) {
        return getPurchaseInvoiceList(fp, isConversionBalance, false);
    }

    public List<EdsPurchaseInvoice> getPurchaseInvoiceList(ListingFilterParameter fp, boolean isConversionBalance, boolean isRecurringBill) {
        EdsUser user = getUser();
        Map<String, Object> param = new HashMap<>();

        StringBuilder sql = new StringBuilder();
        sql.append("select distinct pi from EdsInvoiceItem item join item.invoice pi, " + (isRecurringBill ? "EdsRecurringBill " : "EdsPurchaseInvoice ") + " inv ");
        //Below we are getting access only to PM.
        if (!fp.isFromBudgetSheet() && roleManager.hasRole(user, PM) && !roleManager.hasEitherRoles(user, ACCOUNTANT, DR, ADMIN)) {
            sql.append(" left join pi.relatedProject pr where " + ServerUtils.checkForDeleted("pi.deleted"));
            sql.append(" and pi.type = '").append(Constants.PAYABLE).append("'");
            sql.append(" and (pr.managerid = :user or pr.backup_ManagerId = :user");
            sql.append(" or pr.backup_ManagerId2 = :user or pr.backup_ManagerId3 = :user or pr.backup_ManagerId4 = :user");
            sql.append(" or pr.backup_ManagerId5 = :user or pr.backup_ManagerId6 = :user or pr.backup_ManagerId7 = :user");
            sql.append(" or pr.backup_ManagerId8 = :user or pr.backup_ManagerId9 = :user or pr.backup_ManagerId10 = :user");
            sql.append(" or pi.creator = :creator) ");
            param.put("user", user.getObjectID());
            param.put("creator", user);
        } else {
            sql.append(" where " + ServerUtils.checkForDeleted("pi.deleted"));
            sql.append(" and pi.id = inv.id");
            sql.append(" and pi.type = '").append(Constants.PAYABLE).append("'");
        }

        if (isConversionBalance) {
            EdsFinancialSettings fs = financialSettingsManager.getFinancialSettings();
            sql.append(" and pi.invoiceDate <= :conversionDate ");
            param.put("conversionDate", fs.getConversionDate());
        }
        if (fp != null) {
            if (fp.getInvoiceStatusId() != null) {
                sql.append(" and pi.status.objectID = :statusID ");
                param.put("statusID", fp.getInvoiceStatusId());
            }

            if (fp.getExcludedType() != null) {
                sql.append(" and pi.status.code != :statusCode ");
                param.put("statusCode", fp.getExcludedType());
            }
            if (fp.getProjectId() != null) {
                if (genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.PROJECT_IN_LINE_ITEM_ENABLE)) {
                    sql.append(" and (item.project.objectID in (:projectID) or item.project.parent.objectID= :projectID) ");
                } else {
                    sql.append(" and (pi.relatedProject.objectID in (:projectID) or pi.relatedProject.parent.objectID = :projectID) ");
                }
                if (fp.getProjectIdList() != null && fp.getProjectIdList().size() > 0) {
                    param.put("projectID", fp.getProjectIdList());
                } else {
                    param.put("projectID", fp.getProjectId());
                }
                sql.append(" and ( pi.status.objectID = :approve ");
                Integer approvedStatusId = referenceManager.findReference(INVOICE_STATUS, APPROVE).getObjectID();
                param.put("approve", approvedStatusId);

                sql.append(" or pi.status.objectID = :open ");
                Integer openStatusId = referenceManager.findReference(INVOICE_STATUS, OPEN).getObjectID();
                param.put("open", openStatusId);

                sql.append(" or pi.status.objectID = :paid ");
                Integer paidStatusId = referenceManager.findReference(INVOICE_STATUS, PAID).getObjectID();
                param.put("paid", paidStatusId);

                sql.append(" or pi.status.objectID = :overdue ");
                Integer overdueStatusId = referenceManager.findReference(INVOICE_STATUS, OVER_DUE).getObjectID();
                param.put("overdue", overdueStatusId);
                sql.append(" )");
            }
            if (fp.getInvoiceClientId() != null) {
                sql.append(" and pi.supplier.objectID = :supplierID ");
                param.put("supplierID", fp.getInvoiceClientId());
            } else if (fp.getClientId() != null) {
                sql.append(" and pi.supplier.objectID = :filterSupplierID ");
                param.put("filterSupplierID", fp.getClientId());
            }
            if (fp.getStartDate() != null && fp.getEndDate() != null) {
                sql.append(" and (pi.invoiceDate >= :startDate and pi.invoiceDate <= :dueDate) ");
                param.put("startDate", fp.getStartDate());
                param.put("dueDate", fp.getEndDate());
            } else if (fp.getStartDate() != null) {
                sql.append(" and (pi.invoiceDate >= :startDate) ");
                param.put("startDate", fp.getStartDate());
            } else if (fp.getEndDate() != null) {
                sql.append(" and (pi.invoiceDate <= :dueDate) ");
                param.put("dueDate", fp.getEndDate());
            }
            if (fp.getSqlSearchKey() != null) {
                sql.append(" and (lower(pi.number) like '" + fp.getSqlSearchKey() + "' or ");
                sql.append(" lower(pi.supplier.name) like '" + fp.getSqlSearchKey() + "' or ");
                sql.append(" lower (pi.currency.name) like '" + fp.getSqlSearchKey() + "' or ");
                sql.append(" lower (pi.status.name) like '" + fp.getSqlSearchKey() + "') ");
            }
        }
        sql.append(" and pi.status is not null and pi.status.code!='REVERSED' ");

        if (fp != null) {
            if (fp.getSortField() != null) {
                if (INVOICE_NUMBER_COLUMN.equals(fp.getSortField())) {
                    sql.append("order by pi.number " + (fp.getSortDir() == 2 ? "desc" : ""));
                } else if (INVOICE_DATE_COLUMN.equals(fp.getSortField())) {
                    sql.append("order by pi.invoiceDate " + (fp.getSortDir() == 2 ? "desc" : ""));
                } else if (DUE_DATE_COLUMN.equals(fp.getSortField())) {
                    sql.append("order by pi.dueDate " + (fp.getSortDir() == 2 ? "desc" : ""));
                } else if (CLIENT_COLUMN.equals(fp.getSortField())) {
                    sql.append("order by pi.supplier.name " + (fp.getSortDir() == 2 ? "desc" : ""));
                } else if (CURRENCY_COLUMN.equals(fp.getSortField())) {
                    sql.append("order by pi.currency.name " + (fp.getSortDir() == 2 ? "desc" : ""));
                } else if (STATUS_COLUMN.equals(fp.getSortField())) {
                    sql.append("order by pi.status.name " + (fp.getSortDir() == 2 ? "desc" : ""));
                }
            }
        } else {
            sql.append("order by pi.objectID desc");
        }

        return findByNamedParams(sql.toString(), param);
    }

    public List<EdsPurchaseInvoice> getPurchaseInvoiceListForSolr(SolrReindexRpc solrReindix, Integer start, Integer limit) {
        Map<String, Object> params = new HashMap<>();
        StringBuilder sqlQuery = new StringBuilder();
        sqlQuery.append("select pi from EdsPurchaseInvoice pi ");
        sqlQuery.append(" where  " + ServerUtils.checkForDeleted("pi.deleted"));
        sqlQuery.append(" and  pi.status.code != '" + REVERSED + "'");
        if (!solrReindix.isAllReindex() && solrReindix.getLastUpdateTime() != null) {
            params.put("modifiedDate", solrReindix.getLastUpdateTime());
            sqlQuery.append(" and pi.updatedDate >= :modifiedDate");
            if (solrReindix.getLastUpdateEndTime() != null) {
                sqlQuery.append(" and pi.updatedDate<='").append(solrReindix.getLastUpdateEndTime()).append("'");
            }
        }
        sqlQuery.append(" order by pi.objectID ");
        return findIntervalByNamedParams(sqlQuery.toString(), start, limit, params);
    }

    public Integer getSaleInvoiceFourDigitNumber(DateNonConvertable invoiceDate) {
        Calendar invDateCal = new GregorianCalendar();
        if (invoiceDate != null) {
            invDateCal.setTime(invoiceDate.getNonConvertedDate());
            ServerUtils.setEndOfTheDay(invDateCal);
        }

        StringBuilder query = new StringBuilder();
        Map<String, Object> values = new HashMap<>();
        values.put("REVERSED", REVERSED);
        query.append("select si.fourDigitNumber from EdsSaleInvoice si where si.fourDigitNumber is not null and si.status.code!=:REVERSED and " + ServerUtils.checkForDeleted("si.deleted"));
        Calendar financialYearStart = getFinancialYearStartIfEnabled(null);
        if (financialYearStart != null && financialYearStart.getTime().before(invoiceDate != null ? invDateCal.getTime() : new Date())) {
            values.put("financialYearStart", financialYearStart.getTime());
            query.append(" and si.invoiceDate > :financialYearStart");
        }
        query.append(" order by si.fourDigitNumber desc");
        return (Integer) findSingleByNamedParams(query.toString(), values);
    }

    public Integer getCreditNoteFourDigitNumber(DateNonConvertable invoiceDate) {
        Calendar invDateCal = new GregorianCalendar();
        if (invoiceDate != null) {
            invDateCal.setTime(invoiceDate.getNonConvertedDate());
            ServerUtils.setEndOfTheDay(invDateCal);
        }

        StringBuilder query = new StringBuilder();
        Map<String, Object> values = new HashMap<>();
        values.put("REVERSED", REVERSED);
        query.append("select si.fourDigitNumber from EdsSaleInvoice si where si.fourDigitNumber is not null and si.status.code!=:REVERSED and " + ServerUtils.checkForDeleted("si.deleted"));
        Calendar financialYearStart = getFinancialYearStartIfEnabled(null);
        if (financialYearStart != null && financialYearStart.getTime().before(invoiceDate != null ? invDateCal.getTime() : new Date())) {
            values.put("financialYearStart", financialYearStart.getTime());
            query.append(" and si.invoiceDate > :financialYearStart");
        }
        query.append(" and si.isCreditNote = true ");
        query.append(" order by si.fourDigitNumber desc");
        return (Integer) findSingleByNamedParams(query.toString(), values);
    }

    public Integer getPurchaseInvoiceFourDigitNumber(boolean isCreditNote, DateNonConvertable invoiceDate) {

        Calendar invDateCal = new GregorianCalendar();
        if (invoiceDate != null) {
            invDateCal.setTime(invoiceDate.getNonConvertedDate());
            ServerUtils.setEndOfTheDay(invDateCal);
        }
        StringBuilder query = new StringBuilder();
        Map<String, Object> values = new HashMap<>();
        values.put("REVERSED", REVERSED);
        query.append("select pi.fourDigitNumber from EdsPurchaseInvoice pi where pi.fourDigitNumber is not null and pi.status.code!=:REVERSED and " + ServerUtils.checkForDeleted("pi.deleted"));
        if (isCreditNote) {
            query.append(" and pi.isCreditNote is true");
        } else {
            query.append(" and (pi.isCreditNote is false or pi.isCreditNote is null)");
        }
        Calendar financialYearStart = getFinancialYearStartIfEnabled(null);
        if (financialYearStart != null && financialYearStart.getTime().before(invoiceDate != null ? invDateCal.getTime() : new Date())) {
            values.put("financialYearStart", financialYearStart.getTime());
            query.append(" and pi.invoiceDate > :financialYearStart ");
        }
        query.append(" order by pi.fourDigitNumber desc");
        return (Integer) findSingleByNamedParams(query.toString(), values);
    }


    private Calendar getFinancialYearStartIfEnabled(Date creationDate) {
        return accountingManager.getFinancialYearStartIfEnabled(creationDate);
    }

    public String getLastBillingInformation() {
        return (String) findSingle("select i.notes from EdsInvoice i where i.objectID=(select max(i2.objectID) from EdsInvoice i2 )");
    }

    public List<EdsInvoice> findCompanyInvoices() {
        EdsFinancialSettings fs = financialSettingsManager.getFinancialSettings();
        return find("select inv from EdsInvoice inv where inv.invoiceDate <= ?", fs.getConversionDate());
    }

    public List<ClientsByAmmount> findClientsByAmmount() {
        return findLimited("select new " +
                "com.edatasite.workforce.gwt.dashboard.client.rpc.ClientsByAmmount(si.client.objectID, si.client.name, sum(si.total)) " +
                "from EdsSaleInvoice si group by si.client.objectID, si.client.name ORDER BY sum(si.total) DESC", 5);
    }

    public List<EdsBaseSaleInvoice> getSaleInvoiceByNumber(String number, Date creationDate) {
        List<EdsBaseSaleInvoice> baseSaleInvoices = new LinkedList<>();

        List<EdsSaleInvoice> saleInvoices;
        List<EdsRecurringInvoice> recurringInvoices;

        Calendar financialYearStart = getFinancialYearStartIfEnabled(creationDate);
        if (financialYearStart != null && financialYearStart.getTime().before(new Date())) {
            if (creationDate != null) {
                Calendar financialYearEnd = new GregorianCalendar();
                financialYearEnd.setTime(financialYearStart.getTime());
                financialYearEnd.set(Calendar.YEAR, financialYearEnd.get(Calendar.YEAR) + 1);
                saleInvoices = (List<EdsSaleInvoice>) find("select inv from EdsSaleInvoice inv where inv.number=? and (inv.invoiceDate between ? and ?) and inv.status.code!=? and " + ServerUtils.checkForDeleted("inv.deleted"),
                        number, financialYearStart.getTime(), financialYearEnd.getTime(), REVERSED);
                recurringInvoices = (List<EdsRecurringInvoice>) find("select inv from EdsRecurringInvoice inv where inv.number=? and (inv.invoiceDate between ? and ?) and " + ServerUtils.checkForDeleted("inv.deleted"),
                        number, financialYearStart.getTime(), financialYearEnd.getTime());
            } else {
                saleInvoices = (List<EdsSaleInvoice>) find("select inv from EdsSaleInvoice inv where inv.number=? and inv.invoiceDate > ? and inv.status.code!=? and " + ServerUtils.checkForDeleted("inv.deleted"),
                        number, financialYearStart.getTime(), REVERSED);
                recurringInvoices = (List<EdsRecurringInvoice>) find("select inv from EdsRecurringInvoice inv where inv.number=? and inv.invoiceDate > ? and " + ServerUtils.checkForDeleted("inv.deleted"), number, financialYearStart.getTime());
            }
        } else {
            saleInvoices = (List<EdsSaleInvoice>) find("select inv from EdsSaleInvoice inv where inv.number=? and inv.status.code!=? and " + ServerUtils.checkForDeleted("inv.deleted"), number, REVERSED);
            recurringInvoices = (List<EdsRecurringInvoice>) find("select inv from EdsRecurringInvoice inv where inv.number=? and " + ServerUtils.checkForDeleted("inv.deleted"), number);
        }

        if (saleInvoices != null && saleInvoices.size() > 0) {
            baseSaleInvoices.addAll(saleInvoices);
        }
        if (recurringInvoices != null && recurringInvoices.size() > 0) {
            baseSaleInvoices.addAll(recurringInvoices);
        }
        return baseSaleInvoices;
    }

    public List<EdsPurchaseInvoice> getPurchaseInvoiceByNumber(String number, Integer supplierID, Date creationDate) {
        Calendar financialYearStart = getFinancialYearStartIfEnabled(creationDate);
        if (financialYearStart != null && financialYearStart.getTime().before(new Date())) {
            if (creationDate != null) {
                Calendar financialYearEnd = new GregorianCalendar();
                financialYearEnd.setTime(financialYearStart.getTime());
                financialYearEnd.set(Calendar.YEAR, financialYearEnd.get(Calendar.YEAR) + 1);
                return (List<EdsPurchaseInvoice>) find("select inv from EdsPurchaseInvoice inv where inv.number=? and (inv.creationDate between ? and ?) and inv.status.code!=? and " + ServerUtils.checkForDeleted("inv.deleted"),
                        number, financialYearStart.getTime(), financialYearEnd.getTime(), REVERSED);
            } else {
                return (List<EdsPurchaseInvoice>) find("select inv from EdsPurchaseInvoice inv where inv.number=? and inv.creationDate > ? and inv.status.code!=? and " + ServerUtils.checkForDeleted("inv.deleted"),
                        number, financialYearStart.getTime(), REVERSED);
            }
        } else {
            return (List<EdsPurchaseInvoice>) find("select inv from EdsPurchaseInvoice inv where inv.number=? and inv.status.code!=? and " + ServerUtils.checkForDeleted("inv.deleted"), number, REVERSED);
        }
    }

    @Override
    public List<EdsBaseSaleInvoice> getSaleInvoiceByNumberGlobal(String number) {
        List<EdsBaseSaleInvoice> baseSaleInvoices = new LinkedList<>();

        List<EdsSaleInvoice> saleInvoices = (List<EdsSaleInvoice>) find("select inv from EdsSaleInvoice inv where inv.number=? and inv.status.code!=? and " + ServerUtils.checkForDeleted("inv.deleted"), number, REVERSED);
        List<EdsRecurringInvoice> recurringInvoices = (List<EdsRecurringInvoice>) find("select inv from EdsRecurringInvoice inv where inv.number=? and " + ServerUtils.checkForDeleted("inv.deleted"), number);

        if (saleInvoices != null && saleInvoices.size() > 0) {
            baseSaleInvoices.addAll(saleInvoices);
        }
        if (recurringInvoices != null && recurringInvoices.size() > 0) {
            baseSaleInvoices.addAll(recurringInvoices);
        }
        return baseSaleInvoices;
    }

    @Override
    public List<EdsPurchaseInvoice> getPurchaseInvoiceByNumberGlobal(String number) {
        return (List<EdsPurchaseInvoice>) find("select inv from EdsPurchaseInvoice inv where inv.number=? and inv.status.code!=? and " + ServerUtils.checkForDeleted("inv.deleted"), number, REVERSED);
    }

    public void removeRelationFromQuote(Integer invoiceID) {
        updateNative("delete from  " + getCompanyId() + ".converted_items where  invoice_id = " + invoiceID);
    }

    @Override
    public void removeRelationFromQuote(Integer invoiceID, List<Integer> quoteIds) {
        updateNative("delete from  " + getCompanyId() + ".converted_items where  invoice_id = " + invoiceID + " and quote_id in (" + ServerUtils.getAsCommoDelimited(quoteIds, "0", ",") + ")");
    }

    public void deleteInvoiceOldTaxTotals(EdsInvoice invoice) {
        update("delete from EdsInvoiceTaxTotal itt where itt.invoice=?", invoice);
    }

    public List<Integer> deleteInvoiceItems(Integer invoiceID) {
        List<Integer> itemsDeleted = find("select objectID from EdsInvoiceItem where invoice.objectID = ?", invoiceID);
        update("delete from EdsInvoiceItem where invoice.objectID = ?", invoiceID);
        return itemsDeleted;
    }

    public List<EdsInvoice> getInvoicesByDate(Date from, Date to, boolean accrual) {
        Map<String, Object> map = new HashMap<>();
        map.put("from", from);
        map.put("to", to);
        map.put("RECEIVABLE", Constants.RECEIVABLE);
        map.put("PAYABLE", Constants.PAYABLE);
        map.put("approved", Constants.APPROVE);
        map.put("open", Constants.OPEN);
        map.put("overdue", Constants.OVER_DUE);
        map.put("paid", Constants.PAID);
        return findByNamedParams("select distinct it.invoice from EdsInvoiceTransaction it where it.invoice.invoiceDate between :from and :to and " + ServerUtils.checkForDeleted("it.invoice.deleted") +
                " and (it.invoice.type = :RECEIVABLE or it.invoice.type = :PAYABLE) and " +
                " (it.invoice.status.code = :approved or it.invoice.status.code=:open or it.invoice.status.code=:overdue or it.invoice.status.code=:paid) " +
                (accrual ? "" : " and it.invoice.payments.size>0"), map);
    }

    @Override
    public List<EdsInvoice> getInvoicesByPaymentsDate(Date from, Date to, boolean accrual) {
        Map<String, Object> map = new HashMap<>();
        map.put("from", from);
        map.put("to", to);
        map.put("RECEIVABLE", Constants.RECEIVABLE);
        map.put("PAYABLE", Constants.PAYABLE);
        map.put("approved", Constants.APPROVE);
        map.put("open", Constants.OPEN);
        map.put("overdue", Constants.OVER_DUE);
        map.put("paid", Constants.PAID);
        return findByNamedParams("select distinct ipt.invoicePayment.invoice from EdsInvoicePaymentTransaction ipt where ipt.invoicePayment.paymentDate between :from and :to and "
                + ServerUtils.checkForDeleted("ipt.invoicePayment.deleted") + " and ipt.invoicePayment.status is null and "
                + ServerUtils.checkForDeleted("ipt.invoicePayment.invoice.deleted") +
                " and (ipt.invoicePayment.invoice.type = :RECEIVABLE or ipt.invoicePayment.invoice.type = :PAYABLE) and " +
                " (ipt.invoicePayment.invoice.status.code = :approved or ipt.invoicePayment.invoice.status.code=:open or ipt.invoicePayment.invoice.status.code=:overdue or ipt.invoicePayment.invoice.status.code=:paid) ", map);
    }

    public List<EdsExpenseReport> getExpenseReportsByDate(Date from, Date to, boolean accrual) {
        Map<String, Object> map = new HashMap<>();
        map.put("from", from);
        map.put("to", to);
        map.put("approved", Constants.EXPENSE_APPROVED);
        map.put("paid", Constants.EXPENSE_PAID);
        return findByNamedParams("select distinct er from EdsExpenseReport er left join er.overallStatus st where er.startDate between :from and :to and " +
                ServerUtils.checkForDeleted("er.isDeleted") + " and " +
                "  (st.code = :approved or st.code=:paid) ", map);
    }

    public EdsInvoice getInvoice(Integer id) {
        return (EdsInvoice) findSingle("select si from EdsInvoice si where si.objectID=?", id);
    }

    @Override
    public List<EdsExpenseReport> getExpenseReportsByPaymentsDate(Date from, Date to, boolean accrual) {
        Map<String, Object> map = new HashMap<>();
        map.put("from", from);
        map.put("to", to);
        map.put("approved", Constants.EXPENSE_APPROVED);
        map.put("paid", Constants.EXPENSE_PAID);
        return findByNamedParams("select distinct ep.expenseReport from EdsExpensePayment ep left join ep.expenseReport.overallStatus st where ep.paymentDate between :from and :to and " +
                ServerUtils.checkForDeleted("ep.expenseReport.isDeleted") + " and " +
                " (st.code = :approved or st.code=:paid) ", map);
    }

    public List<EdsBankTransfer> getBankTransferByDate(Date from, Date to) {
        Map<String, Object> map = new HashMap<>();
        map.put("from", from);
        map.put("to", to);
        return findByNamedParams("select distinct bt from EdsBankTransfer bt where bt.date between :from and :to and " + ServerUtils.checkForDeleted("bt.deleted"), map);
    }

    public List<EdsInvoice> getInvoicesForAllocatingCredits(EdsInvoice creditNote, boolean isReceivable) {
        Map<String, Object> map = new HashMap<>();
        map.put("clientSupplierID", creditNote.getClientOrSupplier().getObjectID());
        map.put("currencyID", creditNote.getCurrency().getObjectID());
        map.put("APPROVED", Constants.APPROVE);
        map.put("OPEN", Constants.OPEN);
        map.put("OVERDUE", Constants.OVER_DUE);
        if (isReceivable) {
            return findByNamedParams("select inv from  EdsSaleInvoice  inv where inv.client.objectID = :clientSupplierID " +
                    "and inv.currency.objectID = :currencyID and (inv.status.code = :APPROVED or inv.status.code = :OPEN or inv.status.code = :OVERDUE) and (inv.isCreditNote is null or inv.isCreditNote=false) and " + ServerUtils.checkForDeleted("inv.deleted"), map);
        } else {
            return findByNamedParams("select inv from  EdsPurchaseInvoice  inv where inv.supplier.objectID = :clientSupplierID " +
                    "and inv.currency.objectID = :currencyID and (inv.status.code = :APPROVED or inv.status.code = :OPEN or inv.status.code = :OVERDUE) and (inv.isCreditNote is null or inv.isCreditNote=false) and " + ServerUtils.checkForDeleted("inv.deleted"), map);
        }
    }

    public EdsSaleInvoice getSaleInvoice(Integer id) {
        return (EdsSaleInvoice) findSingle("select si from EdsSaleInvoice si where si.objectID=?", id);
    }

    public EdsSaleInvoice getSaleInvoiceByZapierOrderNumber(Long orderNumber) {
        return (EdsSaleInvoice) findSingle("select si from EdsSaleInvoice si where si.zapierordernumber=? and " + ServerUtils.checkForDeleted("si.deleted"), orderNumber);
    }

    @Override
    public List<EdsSaleInvoice> getSaleInvoiceListByIDs(String IDs, String ascOrDesc) {
        return find("select si from EdsSaleInvoice si where si.objectID in (" + IDs + ") order by si.objectID " + ascOrDesc);
    }

    @Override
    public EdsPurchaseInvoice getPurchaseInvoice(Integer id) {
        return (EdsPurchaseInvoice) findSingle("select pi from EdsPurchaseInvoice pi where pi.objectID=?", id);
    }

    @Override
    public EdsRecurringBill getRecurringBill(Integer id) {
        return (EdsRecurringBill) findSingle("select rb from EdsRecurringBill rb where rb.objectID=?", id);
    }

    @Override
    public List<EdsSaleInvoice> getCompanySaleInvoiceByTimeZoneAndCorsor(Date companyCurrentDate, Integer lastId, int limit) {
        Calendar cal = Calendar.getInstance();
        cal.clear();
        cal.set(2022, Calendar.JANUARY, 1, 0, 0, 0);
        Date cutoff2022 = cal.getTime();
        StringBuilder query = new StringBuilder()
                .append("select si from EdsSaleInvoice si where ")
                .append(ServerUtils.checkForDeleted("si.deleted"))
                .append("and si.dueDate < ? ")
                .append("and si.creationDate >= ? ")
                .append("and si.status.code in ('APPROVE','OVER_DUE') ")
                .append("and si.total >= 0 ");

        boolean useCursor = (lastId != null && lastId > 0);
        if (useCursor) {
            query.append("and si.id < ? ").append(" ");
        }
        query.append("order by si.id desc");

        if (useCursor) {
            return findLimited(query.toString(), limit, companyCurrentDate, cutoff2022, lastId);
        }
        // no cursor on first page
        return findLimited(query.toString(), limit, companyCurrentDate, cutoff2022);
    }

    public List<EdsSaleInvoice> getCompanyOverdueSaleInvoiceByTimeZone(Date companyDate, int limit) {
        StringBuilder hql = new StringBuilder();
        hql.append("select si from EdsSaleInvoice si ");
        hql.append("where " + ServerUtils.checkForDeleted("si.deleted") + " and si.dueDate<? ");
        hql.append("and si.status.code = 'OVER_DUE' order by si.id desc");
        return limit > 0 ? findLimited(hql.toString(), limit, companyDate) : find(hql.toString(), companyDate);
    }

    public List<EdsPurchaseInvoice> getCompanyPurchaseInvoiceByTimeZone(Date companyCurrentDate, Integer lastId, int limit) {
        Calendar cal = Calendar.getInstance();
        cal.clear();
        cal.set(2022, Calendar.JANUARY, 1, 0, 0, 0);
        Date cutoff2022 = cal.getTime();

        StringBuilder query = new StringBuilder()
                .append("select pi from EdsPurchaseInvoice pi where ")
                .append(ServerUtils.checkForDeleted("pi.deleted "))
                .append("and pi.dueDate<? ")
                .append("and pi.creationDate >= ? ")
                .append("and pi.status.code in ('APPROVE','OVER_DUE') ")
                .append("and pi.total >= 0 ");

        boolean useCursor = (lastId != null && lastId > 0);
        if (useCursor) {
            query.append("and pi.id < ? ").append(" ");
        }
        query.append("order by pi.id desc");

        if (useCursor) {
            return findLimited(query.toString(), limit, companyCurrentDate, cutoff2022, lastId);
        }
        // no cursor on first page
        return findLimited(query.toString(), limit, companyCurrentDate, cutoff2022);
    }

    public List<EdsPurchaseInvoice> getCompanyOverduePurchaseInvoiceByTimeZone(Date companyDate, int limit) {
        StringBuilder hql = new StringBuilder();
        hql.append("select pi from EdsPurchaseInvoice pi ");
        hql.append("where " + ServerUtils.checkForDeleted("pi.deleted") + " and pi.dueDate<? ");
        hql.append("and pi.status.code = 'OVER_DUE' order by pi.id desc ");
        return limit > 0 ? findLimited(hql.toString(), limit, companyDate) : find(hql.toString(), companyDate);
    }

    public boolean isClientSupplierInvoiceExists(Integer clientSupplierID, boolean isClient) {
        if (isClient) {
            return find("select si.objectID from EdsSaleInvoice si where" + ServerUtils.checkForDeleted("si.deleted") + " and si.client.objectID = ?", clientSupplierID).size() > 0;
        } else {
            return find("select pi.objectID from EdsPurchaseInvoice pi where" + ServerUtils.checkForDeleted("pi.deleted") + " and pi.supplier.objectID = ?", clientSupplierID).size() > 0;
        }
    }

    public List<EdsInvoice> getInvoicesByClientSupplierAndStatuses(List<Integer> crmAccountIds, Integer currencyID, boolean isCustomer, List<String> statusList, FindMatchFilterData filterData, boolean isMultiCurrencyEnabled) {
        StringBuilder hql = new StringBuilder();
        StringBuilder searchQuery = new StringBuilder();
        if (filterData.getSearchKey() != null && !filterData.getSearchKey().isEmpty()) {
            searchQuery.append("and (lower(i.number) like '%" + filterData.getSearchKey().toLowerCase() + "%') ");
        }
        if (filterData.getStartDate() != null) {
            searchQuery.append("and i.invoiceDate >= '" + filterData.getStartDate().getNonConvertedDate() + "' ");
        }
        if (filterData.getEndDate() != null) {
            searchQuery.append("and i.invoiceDate <= '" + filterData.getEndDate().getNonConvertedDate() + "' ");
        }
        if (filterData.getReceivablePayableID() != null) {
            if (filterData.isParentReceivablePayable()) {
                hql.append(" and (i.receivablePayable is null or i.receivablePayable.objectID=").append(filterData.getReceivablePayableID()).append(") ");
            } else {
                hql.append(" and i.receivablePayable.objectID=").append(filterData.getReceivablePayableID()).append(" ");
            }
        }
        if (filterData.getProjectID() != null) {
            if (genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.PROJECT_IN_LINE_ITEM_ENABLE)) {
                searchQuery.append(" and item.project.objectID = " + filterData.getProjectID());
            } else {
                searchQuery.append(" and i.relatedProject.objectID = " + filterData.getProjectID());
            }
        }
        if (isCustomer) {
            hql.append("select distinct i from EdsInvoiceItem item join item.invoice inv, EdsSaleInvoice i ");
            hql.append(" where " + ServerUtils.checkForDeleted("i.deleted") + " and inv.id = i.id and i.client.objectID in (" + ServerUtils.getAsCommoDelimited(crmAccountIds, "0") + ") and (i.isCreditNote is null or i.isCreditNote=false) ");
        } else {
            hql.append("select distinct i from EdsInvoiceItem item join item.invoice inv, EdsPurchaseInvoice i ");
            hql.append(" where " + ServerUtils.checkForDeleted("i.deleted") + " and inv.id = i.id and i.supplier.objectID in (" + ServerUtils.getAsCommoDelimited(crmAccountIds, "0") + ") and (i.isCreditNote is null or i.isCreditNote=false) ");
        }

        if (currencyID != null) {
            if (isMultiCurrencyEnabled) {
                EdsFinancialSettings fs = financialSettingsManager.getFinancialSettings();
                List<Integer> currencyIDs = new LinkedList<>();
                currencyIDs.add(currencyID);
                if (!currencyID.equals(fs.getCurrency().getObjectID())) {
                    currencyIDs.add(fs.getCurrency().getObjectID());
                }
                hql.append(" and i.currency.objectID in (").append(ServerUtils.getAsCommoDelimited(currencyIDs, "0")).append(") ");
            } else {
                hql.append(" and i.currency.objectID=" + currencyID + " ");
            }
        }

        hql.append(searchQuery);
        addStatusFilter(hql, statusList);

        /*if (filterData.getSortField() != null) {
            hql.append("  ");
            if (AccountingConstants.DESCRIPTION_COLUMN.equals(filterData.getSortField())) {
                hql.append(" order by i.number ").append(filterData.getSortDirection());
            } else if (AccountingConstants.DATE_COLUMN.equals(filterData.getSortField())) {
                hql.append("order by i.invoiceDate ").append(filterData.getSortDirection());
            }
        } else {
        }*/
        hql.append(" order by i.objectID desc");
        return find(hql.toString());
    }

    public List<EdsSaleInvoice> getSaleInvoicesByCrmAccountID(Integer crmAccountID) {
        return (List<EdsSaleInvoice>) find("select si from EdsSaleInvoice si where " + ServerUtils.checkForDeleted("si.deleted") + " and si.client.objectID = ?", crmAccountID);
    }

    public boolean findSaleInvoicesByCrmAccountID(Integer crmAccountID) {
        List<Integer> invIds = (List<Integer>) find("select si.id from EdsSaleInvoice si where " + ServerUtils.checkForDeleted("si.deleted") +
                " and si.client.objectID = ?", crmAccountID);
        return invIds != null && invIds.size() > 0;
    }

    @Override
    public List<EdsSaleInvoice> getUnDeleteSaleInvoicesByCrmAccountID(Integer crmAccountID) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> param = new HashMap<>();
        param.put("clientID", crmAccountID);
        sql.append("select si from EdsSaleInvoice si where " + ServerUtils.checkForDeleted("si.deleted") + "  and si.client.objectID = :clientID ");
        return (List<EdsSaleInvoice>) findByNamedParams(sql.toString(), param);
    }

    public List<EdsRecurringInvoice> getRecurringInvoicesByCrmAccountID(Integer crmAccountID) {
        return (List<EdsRecurringInvoice>) find("select ri from EdsRecurringInvoice ri where " + ServerUtils.checkForDeleted("ri.deleted") + " and ri.client.objectID = ?", crmAccountID);
    }

    public List<EdsPurchaseInvoice> getPurchaseInvoicesByCrmAccountID(Integer crmAccountID) {
        return (List<EdsPurchaseInvoice>) find("select pi from EdsPurchaseInvoice pi where pi.supplier.objectID = ?", crmAccountID);
    }

    public List<EdsPurchaseInvoice> getUndeletedPurchaseInvoicesByCrmAccountID(Integer crmAccountID) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> param = new HashMap<>();
        param.put("accountID", crmAccountID);
        sql.append("select pi from EdsPurchaseInvoice pi where " + ServerUtils.checkForDeleted("pi.deleted") + "  and pi.supplier.objectID = :accountID ");
        return (List<EdsPurchaseInvoice>) findByNamedParams(sql.toString(), param);
    }

    private void addStatusFilter(StringBuilder hql, List<String> statusList) {
        if (statusList != null && statusList.size() > 0) {
            hql.append(" and i.status.code in (");
            int i = 0;
            for (String s : statusList) {
                if (i == 0) {
                    hql.append("'");
                    hql.append(s);
                    hql.append("'");
                } else {
                    hql.append(", '");
                    hql.append(s);
                    hql.append("'");
                }
                i++;
            }
            hql.append(")");
        }
    }

    @Override
    public List<EdsSaleInvoice> getSalesInvoicesByConvertedItem(Integer quoteID) {
        String schema = ServerSecurityContext.getInstance().getCompanyId();
        StringBuilder sql = new StringBuilder();
        sql.append("select * from \"" + schema + "\".converted_items ci ");
        sql.append("left join \"" + schema + "\".invoice i on i.id=ci.invoice_id ");
        sql.append("left join \"" + schema + "\".saleinvoice si on si.id=i.id ");
        sql.append("left join \"" + schema + "\".reference r on r.id=i.status_id ");
        sql.append("where " + ServerUtils.checkForDeleted("i.deleted") + " and ci.quote_id=" + quoteID + " and (r.code is not null and r.code!='" + Constants.REVERSED + "')");
        return findNative(sql.toString(), EdsSaleInvoice.class);
    }

    @Override
    public Boolean hasConvertedItems(Integer quoteID) {
        String companyId = getCompanyId();
        StringBuilder sql = new StringBuilder();
        sql.append("select count(*) > 0 from " + companyId + ".converted_items ci ");
        sql.append("left join " + companyId + ".invoice i on i.id=ci.invoice_id ");
        sql.append("left join " + companyId + ".saleinvoice si on si.id=i.id ");
        sql.append("left join " + companyId + ".reference r on r.id=i.status_id ");
        sql.append("where " + ServerUtils.checkForDeleted("i.deleted") + " and ci.quote_id=" + quoteID + " and (r.code is not null and r.code!='" + Constants.REVERSED + "')");
        return (Boolean) findNativeSingle(sql.toString());
    }

    @Override
    public List<EdsPurchaseInvoice> getPurchaseInvoicesByConvertedItem(Integer quoteID) {
        String companyId = getCompanyId();
        StringBuilder sql = new StringBuilder();
        sql.append("select * from " + companyId + ".converted_items ci ");
        sql.append("left join " + companyId + ".invoice i on i.id=ci.invoice_id ");
        sql.append("left join " + companyId + ".purchaseinvoice pi on pi.id=i.id ");
        sql.append("left join " + companyId + ".reference r on r.id=i.status_id ");
        sql.append("where " + ServerUtils.checkForDeleted("i.deleted") + " and ci.quote_id=" + quoteID + " and (r.code is not null and r.code!='" + Constants.REVERSED + "')");
        return findNative(sql.toString(), EdsPurchaseInvoice.class);
    }

    @Override
    public BigDecimal getConvertedInvoiceAmount(Integer quoteID, Integer currentInvoiceId) {
        String companyId = getCompanyId();
        StringBuilder sql = new StringBuilder();
        sql.append("select sum(i.totalInInvoiceCurrency-COALESCE(cni.cnamount,0.00)) ");
        return getConvertedInvoices(quoteID, currentInvoiceId, sql, companyId);
    }

    private BigDecimal getConvertedInvoices(Integer quoteID, Integer currentInvoiceId, StringBuilder sql, String companyId) {
        sql.append(" from " + companyId + ".converted_items ci ");
        sql.append(" left join " + companyId + ".invoice i on i.id=ci.invoice_id ");
        sql.append(" left join (select ip.invoiceid,sum(cn.totalInInvoiceCurrency) cnamount " +
                " from " + companyId + ".invoicepayments ip " +
                " join " + companyId + ".invoice cn on ip.creditnoteid=cn.id " +
                " left join " + companyId + ".reference cst on cn.status_id=cst.id " +
                " where ip.deleted is not true and " + ServerUtils.checkForDeleted("cn.deleted") +
                " and (cst.code is not null and cst.code!='" + Constants.REVERSED + "') " +
                " group by ip.invoiceid) cni on i.id= cni.invoiceid ");
        sql.append(" left join " + companyId + ".reference r on r.id=i.status_id ");
        sql.append(" where " + ServerUtils.checkForDeleted("i.deleted") + " and ci.quote_id=" + quoteID + " and (r.code is not null and r.code!='" + Constants.REVERSED + "')");
        if (currentInvoiceId != null) {
            sql.append(" and i.id!=" + currentInvoiceId);
        }
        BigDecimal val = (BigDecimal) findNativeSingle(sql.toString());
        return val != null ? val : BigDecimal.ZERO;
    }

    @Override
    public BigDecimal getConvertedBaseAmount(Integer quoteID, Integer currentInvoiceId) {
        String companyId = getCompanyId();
        StringBuilder sql = new StringBuilder();
        sql.append("select sum(i.total-COALESCE(cni.cnamount,0.00)) from " + companyId + ".converted_items ci ");
        sql.append("left join " + companyId + ".invoice i on i.id=ci.invoice_id ");
        sql.append(" left join (select ip.invoiceid,sum(cn.total) cnamount " +
                " from " + companyId + ".invoicepayments ip " +
                " join " + companyId + ".invoice cn on ip.creditnoteid=cn.id " +
                " left join " + companyId + ".reference cst on cn.status_id=cst.id " +
                " where ip.deleted is not true and " + ServerUtils.checkForDeleted("cn.deleted") +
                " and (cst.code is not null and cst.code!='" + Constants.REVERSED + "') " +
                " group by ip.invoiceid) cni on i.id= cni.invoiceid ");
        sql.append("left join " + companyId + ".reference r on r.id=i.status_id ");
        sql.append("where " + ServerUtils.checkForDeleted("i.deleted") + " and ci.quote_id=" + quoteID + " and (r.code is not null and r.code!='" + Constants.REVERSED + "')");
        if(currentInvoiceId != null){
            sql.append(" and i.id!= " + currentInvoiceId);

        }
        BigDecimal val = (BigDecimal) findNativeSingle(sql.toString());
        return val != null ? val : BigDecimal.ZERO;
    }


    @Override
    public HashMap<Integer, BigDecimal> getConvertedInvoiceAmountsForListing(String quoteIDs) {
        if (StringUtils.isNotBlank(quoteIDs)) {
            String companyId = getCompanyId();
            String sql = "select ci.quote_id, sum(i.totalInInvoiceCurrency-COALESCE(cni.cnamount,0.00)) " +
                    " from " + companyId + ".converted_items ci " +
                    "left join " + companyId + ".invoice i on i.id=ci.invoice_id " +
                    " left join (select ip.invoiceid, sum(cn.totalInInvoiceCurrency) cnamount " +
                    " from " + companyId + ".invoicepayments ip " +
                    " join " + companyId + ".invoice cn on ip.creditnoteid=cn.id " +
                    " left join " + companyId + ".reference cst on cn.status_id=cst.id " +
                    " where ip.deleted is not true and " + ServerUtils.checkForDeleted("cn.deleted") +
                    " and (cst.code is not null and cst.code!='" + Constants.REVERSED + "') " +
                    " group by ip.invoiceid) cni on i.id= cni.invoiceid " +
                    "left join " + companyId + ".reference r on r.id=i.status_id " +
                    "where " + ServerUtils.checkForDeleted("i.deleted") + " and ci.quote_id in (" + quoteIDs + ") and (r.code is not null and r.code!='" + Constants.REVERSED + "') group by ci.quote_id";
            ArrayList<Object[]> items = (ArrayList<Object[]>) findNative(sql);
            if (items != null && !items.isEmpty()) {
                HashMap<Integer, BigDecimal> result = new HashMap<>();
                for (Object[] item : items) {
                    result.put((Integer) item[0], item[1] != null ? (BigDecimal) item[1] : BigDecimal.ZERO);
                }
                return result;
            }
        }
        return null;
    }

    @Override
    public BigDecimal getConvertedInvoicesPercent(Integer quoteID, Integer invoiceID, boolean fromDelete) {
        String companyId = getCompanyId();
        StringBuilder sql = new StringBuilder();
        sql.append("select sum(si.quotepercent* (i.totalInInvoiceCurrency-COALESCE(cni.cnamount,0.00))/i.totalInInvoiceCurrency) ");
        sql.append(" from " + companyId + ".converted_items ci ");
        sql.append("left join " + companyId + ".invoice i on i.id=ci.invoice_id ");
        sql.append("left join " + companyId + ".saleinvoice si on si.id=ci.invoice_id ");
        sql.append(" left join (select ip.invoiceid,sum(cn.totalInInvoiceCurrency) cnamount " +
                " from " + companyId + ".invoicepayments ip " +
                " join " + companyId + ".invoice cn on ip.creditnoteid=cn.id " +
                " left join " + companyId + ".reference cst on cn.status_id=cst.id " +
                " where ip.deleted is not true and " + ServerUtils.checkForDeleted("cn.deleted") +
                " and (cst.code is not null and cst.code!='" + Constants.REVERSED + "') " +
                " group by ip.invoiceid) cni on i.id= cni.invoiceid ");
        sql.append("left join " + companyId + ".reference r on r.id=i.status_id ");
        sql.append("where " + ServerUtils.checkForDeleted("i.deleted"));
        if (fromDelete) {
            sql.append(" and si.id<>" + invoiceID + "");
        }
        sql.append(" and ci.quote_id=" + quoteID + " and (r.code is not null and r.code!='" + Constants.REVERSED + "')");
        return (BigDecimal) findNativeSingle(sql.toString());
    }

    @Override
    public BigDecimal getConvertedInvoicesAmount(Integer quoteID, Integer invoiceID, boolean fromDelete) {
        String companyId = getCompanyId();
        StringBuilder sql = new StringBuilder();
        sql.append("select sum(i.totalInInvoiceCurrency-COALESCE(cni.cnamount,0.00)) ");
        sql.append(" from " + companyId + ".converted_items ci ");
        sql.append(" left join " + companyId + ".invoice i on i.id=ci.invoice_id ");
        sql.append(" left join (select ip.invoiceid,sum(cn.totalInInvoiceCurrency) cnamount " +
                " from " + companyId + ".invoicepayments ip " +
                " join " + companyId + ".invoice cn on ip.creditnoteid=cn.id " +
                " left join " + companyId + ".reference cst on cn.status_id=cst.id " +
                " where ip.deleted is not true and " + ServerUtils.checkForDeleted("cn.deleted") +
                " and (cst.code is not null and cst.code!='" + Constants.REVERSED + "') " +
                " group by ip.invoiceid) cni on i.id= cni.invoiceid ");
        sql.append(" left join " + companyId + ".reference r on r.id=i.status_id ");
        sql.append(" where " + ServerUtils.checkForDeleted("i.deleted"));
        if (fromDelete) {
            sql.append(" and i.id<>" + invoiceID);
        }
        sql.append(" and ci.quote_id=" + quoteID + " and (r.code is not null and r.code!='" + Constants.REVERSED + "')");
        return (BigDecimal) findNativeSingle(sql.toString());
    }

    @Override
    public Map<Integer, BigDecimal> getEstAmounts(Integer quoteID) {
        List<EdsQuoteItem> quoteItems = getSaleQuote(quoteID).getQuoteItems();
        int i = 0;
        Map<Integer, BigDecimal> estAmountsMap = new HashMap<>();
        for (EdsQuoteItem qi : quoteItems) {
            estAmountsMap.put(i, qi.getAmmount());
            i++;
        }
        return estAmountsMap;
    }

    @Override
    public Map<Integer, BigDecimal> getPriorAmounts(Integer quoteID, Integer invoiceID) {
        List<EdsSaleInvoice> prevInvoices = getInvoiceForPriorAmount(quoteID, invoiceID);
        Map<Integer, BigDecimal> priorAmountsMap = new HashMap<>();
        for (EdsSaleInvoice si : prevInvoices) {
            List<EdsInvoiceItem> invItems = si.getInvoiceItems();
            int i = 0;
            for (EdsInvoiceItem ii : invItems) {
                if (priorAmountsMap.containsKey(i)) {
                    priorAmountsMap.put(i, priorAmountsMap.get(i).add(ii.getAmmount()));
                } else {
                    priorAmountsMap.put(i, ii.getAmmount());
                }
                i++;
            }
        }
        return priorAmountsMap;
    }

    private List<EdsSaleInvoice> getInvoiceForPriorAmount(Integer quoteID, Integer invoiceID) {
        String schema = ServerSecurityContext.getInstance().getCompanyId();
        StringBuilder sql = new StringBuilder();
        sql.append("select * from \"" + schema + "\".converted_items ci ");
        sql.append("left join \"" + schema + "\".quote q on q.id=ci.quote_id ");
        sql.append("left join \"" + schema + "\".saleinvoice si on si.id=ci.invoice_id ");
        sql.append("left join \"" + schema + "\".invoice i on i.id=ci.invoice_id ");
        sql.append("where ci.quote_id=" + quoteID);
        if (invoiceID != null) {
            sql.append(" and si.id <" + invoiceID);
        }
        return findNative(sql.toString(), EdsSaleInvoice.class);
    }

    @Override
    public EdsSaleQuote getSaleQuote(Integer quoteID) {
        return (EdsSaleQuote) findSingle("select sq from EdsSaleQuote sq where objectID = ?", quoteID);
    }

    @Override
    public RevisionHistoryItem[] getRevisionHistory(Integer objectID, String type) {
        RevisionHistoryItem[] historyItems = null;
        if (SALE_QUOTE.equals(type)) {
            List<EdsSaleQuote> quotes = find("select sq from EdsSaleQuote sq where sq.objectID = ? or sq.historicalParent.objectID = ? order by sq.historicalParent.objectID desc, sq.objectID desc", objectID, objectID);

            historyItems = new RevisionHistoryItem[quotes.size()];
            int i = 0;
            for (EdsSaleQuote sq : quotes) {
                historyItems[i++] = new RevisionHistoryItem(sq.getObjectID(), sq.getUpdater().getName(), sq.getUpdatedDate(), sq.getNumber());
            }
        } else if (SALE_INVOICE.equals(type)) {
            List<EdsSaleInvoice> invoices = find("select si from EdsSaleInvoice si where si.objectID = ? or si.historicalParent.objectID = ? order by si.historicalParent.objectID desc, si.objectID desc", objectID, objectID);

            historyItems = new RevisionHistoryItem[invoices.size()];
            int i = 0;
            for (EdsSaleInvoice si : invoices) {
                historyItems[i++] = new RevisionHistoryItem(si.getObjectID(), si.getUpdater().getName(), si.getUpdatedDate(), si.getNumber());
            }
        } else if (PURCHASE_ORDER.equals(type)) {
            List<EdsPurchaseOrder> orders = find("select po from EdsPurchaseOrder po where po.objectID = ? or po.historicalParent.objectID = ? order by po.historicalParent.objectID desc, po.objectID desc", objectID, objectID);

            historyItems = new RevisionHistoryItem[orders.size()];
            int i = 0;
            for (EdsPurchaseOrder po : orders) {
                historyItems[i++] = new RevisionHistoryItem(po.getObjectID(), po.getUpdater() != null ? po.getUpdater().getName() : null, po.getUpdatedDate(), po.getNumber());
            }
        } else if (PURCHASE_INVOICE.equals(type)) {
            List<EdsPurchaseInvoice> invoices = find("select pi from EdsPurchaseInvoice pi where pi.objectID = ? or pi.historicalParent.objectID = ? order by pi.historicalParent.objectID desc, pi.objectID desc", objectID, objectID);

            historyItems = new RevisionHistoryItem[invoices.size()];
            int i = 0;
            for (EdsPurchaseInvoice pi : invoices) {
                historyItems[i++] = new RevisionHistoryItem(pi.getObjectID(), pi.getUpdater().getName(), pi.getUpdatedDate(), pi.getNumber());
            }
        }
        return historyItems;
    }

    @Override
    public List<EdsSaleInvoice> getSaleInvoicesForSaasuSync(Integer startIndex, Integer limit) {
        return findLimited("SELECT si FROM EdsSaleInvoice si WHERE si.objectID > ? AND si.status.code='APPROVE' AND " + ServerUtils.checkForDeleted("si.deleted") + " ORDER BY si.objectID ASC", limit, startIndex);
    }

    @Override
    public List<TCScheduleItem> getTCInvoicesForSchedule(ListingFilterParameter filterParameter) {
        String companyID = getCompanyId();
        StringBuilder sql = new StringBuilder();
        sql.append("select distinct si.id as objectID, i.number as number, i.total as amount  from " + companyID + ".saleinvoice si ");
        sql.append("inner join " + companyID + ".invoice i on i.id=si.id ");
        sql.append("inner join " + companyID + ".courseschedule_invoice csi on csi.invoice_id=si.id ");
        sql.append("inner join " + companyID + ".scheduledcourse sc on sc.id = csi.courseschedule_id ");
        sql.append("inner join " + companyID + ".reference status on status.id = i.status_id ");
        sql.append("where " + ServerUtils.checkForDeleted("i.deleted"));
        sql.append(" and status.code in ('").append(APPROVE).append("', '").append(OVER_DUE).append("', '").append(OPEN).append("') ");
        if (filterParameter.getStartDate() != null && filterParameter.getEndDate() != null) {
            sql.append(" and i.invoiceDate between '" + filterParameter.getStartDate() + "' and '" + filterParameter.getEndDate() + "'");
        }
        if (filterParameter.getCrmAccountId() != null) {
            List<Integer> customerIDs = findNative("select cus.id from " + companyID + ".crmaccount cus where " + ServerUtils.checkForDeleted("cus.deleted") +
                    " and (cus.parent_id=" + filterParameter.getCrmAccountId().toString() + " or cus.id=" + filterParameter.getCrmAccountId().toString() + ")");
            sql.append(" and si.client_id in (" + ServerUtils.getAsCommoDelimited(customerIDs, "0") + ")");
        }
        if (filterParameter.getLocationId() != null) {
            sql.append(" and sc.location_id=" + filterParameter.getLocationId().toString());
        }

        sql.append(" order by si.id desc");

        return jdbcSpringManager.getSimpleJdbcTemplate().query(sql.toString(), BeanPropertyRowMapper.newInstance(TCScheduleItem.class));
    }

    @Override
    public EdsSaleInvoice getSaleInvoiceBySaasuGUID(String saasuGUID) {
        Map<String, Object> map = new HashMap<>();
        map.put("SAASU_UID", saasuGUID);
        return (EdsSaleInvoice) findSingleByNamedParams("select si from EdsSaleInvoice si where (si.deleted is null or si.deleted<>true) AND si.saasuGUID = :SAASU_UID", map);
    }

    @Override
    public List<EdsInvoice> getCurrentInvoicesByCrmAccount(Integer crmAccountID, String type) {
        if (type.equals(RECEIVABLE)) {
            return find("select inv from EdsSaleInvoice inv where inv.client.objectID=? and " + ServerUtils.checkForDeleted("inv.deleted") + " and (inv.isCreditNote is null or inv.isCreditNote <> true) and inv.status.code in('APPROVE', 'OPEN', 'OVER_DUE')", crmAccountID);
        } else {
            return find("select inv from EdsPurchaseInvoice inv where inv.supplier.objectID=? and " + ServerUtils.checkForDeleted("inv.deleted") + " and (inv.isCreditNote is null or inv.isCreditNote <> true) and inv.status.code in('APPROVE', 'OPEN', 'OVER_DUE')", crmAccountID);
        }
    }

    @Override
    public BigDecimal getCustomerPrePaymentBalance(Integer crmaccountID) {
        return (BigDecimal) findSingle("select pb.balance from EdsProjectPrepaymentBalance pb where pb.crmAccount.objectID = ?", crmaccountID);
    }

    @Override
    public List<EdsPurchaseInvoice> getPurchaseInvoiceByIds(String Ids) {
        return find("select pi from EdsPurchaseInvoice pi where " + ServerUtils.checkForDeleted("pi.deleted") + " and pi.objectID in(" + Ids + ")");
    }

    @Override
    public EdsPurchaseInvoice getPurchaseInvoiceByExternalGUID(String code) {
        return (EdsPurchaseInvoice) findSingle("select pi from EdsPurchaseInvoice pi where " + ServerUtils.checkForDeleted("pi.deleted") + " and pi.externalGUID=?", code);
    }

    @Override
    public List<EdsSaleInvoice> getSaleInvoiceByIds(String Ids) {
        return find("select si from EdsSaleInvoice si where " + ServerUtils.checkForDeleted("si.deleted") + " and si.objectID in(" + Ids + ")");
    }

    @Override
    public EdsSaleInvoice getSaleInvoiceByExternalGUID(String code) {
        return (EdsSaleInvoice) findSingle("select si from EdsSaleInvoice si where " + ServerUtils.checkForDeleted("si.deleted") + " and si.externalGUID=?", code);
    }

    @Override
    public BigDecimal getConvertedQtyByQuoteItem(Integer quoteItemId) {
//        BigDecimal convertedQty = (BigDecimal) findSingle("select sum(ii.qty) from EdsInvoiceItem ii where ii.quoteItemId = ? and ii.invoice.status.code!='REVERSED' and " + ServerUtils.checkForDeleted("ii.invoice.deleted"), quoteItemId);
        String companyId = getCompanyId();
        String sql =
                "SELECT SUM(ii.qty  - COALESCE(cni.cnamount, 0.00)) " +
                        "FROM " + companyId + ".invoiceitem ii " +
                        "LEFT JOIN " + companyId + ".invoice inv ON inv.id = ii.invoice_id " +
                        "LEFT JOIN " + companyId + ".reference r ON r.id = inv.status_id " +
                        "LEFT JOIN ( " +
                        "    SELECT ip.invoiceId, SUM(cnii.qty * cnii.unitPrice) AS cnamount " +
                        "    FROM " + companyId + ".invoicepayments ip " +
                        "    JOIN " + companyId + ".invoiceitem cnii ON ip.creditNoteId = cnii.invoice_id " +
                        "    JOIN " + companyId + ".invoice cn ON cnii.invoice_id = cn.id " +
                        "    JOIN " + companyId + ".reference cst ON cn.status_id = cst.id " +
                        "    WHERE ip.deleted IS NOT TRUE " +
                        "      AND (cn.deleted <> TRUE OR cn.deleted IS NULL) " +
                        "      AND (cst.code IS NOT NULL AND cst.code != '" + Constants.REVERSED + "') " +
                        "    GROUP BY ip.invoiceId " +
                        ") cni ON ii.invoice_id = cni.invoiceid " +
                        "WHERE ii.quoteItemId = " + quoteItemId + " " +
                        "AND (r.code IS NOT NULL AND r.code != '" + Constants.REVERSED + "') " +
                        "AND " + ServerUtils.checkForDeleted("inv.deleted");

        BigDecimal convertedAmount = (BigDecimal) findNativeSingle(sql);
        return convertedAmount != null ? convertedAmount : BigDecimal.ZERO;
        }

    @Override
    public BigDecimal getConvertedAmountByQuoteItem(Integer quoteItemId) {
        String companyId = getCompanyId();
        String sql =
                "SELECT SUM(ii.qty * ii.unitPrice - COALESCE(cni.cnamount, 0.00)) " +
                        "FROM " + companyId + ".invoiceitem ii " +
                        "LEFT JOIN " + companyId + ".invoice inv ON inv.id = ii.invoice_id " +
                        "LEFT JOIN " + companyId + ".reference r ON r.id = inv.status_id " +
                        "LEFT JOIN ( " +
                        "    SELECT ip.invoiceId, SUM(cnii.qty * cnii.unitPrice) AS cnamount " +
                        "    FROM " + companyId + ".invoicepayments ip " +
                        "    JOIN " + companyId + ".invoiceitem cnii ON ip.creditNoteId = cnii.invoice_id " +
                        "    JOIN " + companyId + ".invoice cn ON cnii.invoice_id = cn.id " +
                        "    JOIN " + companyId + ".reference cst ON cn.status_id = cst.id " +
                        "    WHERE ip.deleted IS NOT TRUE " +
                        "      AND (cn.deleted <> TRUE OR cn.deleted IS NULL) " +
                        "      AND (cst.code IS NOT NULL AND cst.code != '" + Constants.REVERSED + "') " +
                        "    GROUP BY ip.invoiceId " +
                        ") cni ON ii.invoice_id = cni.invoiceid " +
                        "WHERE ii.quoteItemId = " + quoteItemId + " " +
                        "AND (r.code IS NOT NULL AND r.code != '" + Constants.REVERSED + "') " +
                        "AND " + ServerUtils.checkForDeleted("inv.deleted");

        BigDecimal convertedAmount = (BigDecimal) findNativeSingle(sql);
        return convertedAmount != null ? convertedAmount : BigDecimal.ZERO;
    }

    @Override
    public boolean checkInvoiceForExisting(String ref) {
        String schema = ServerSecurityContext.getInstance().getCompanyId();
        return findNative("select * from \"" + schema + "\".invoice inv where " + ServerUtils.checkForDeleted("inv.deleted") + " and inv.reference=?", ref).size() > 0;
    }

    @Override
    public List<Integer> getInvoiceEnabledCurrencies(Integer crmAccountID, boolean customer) {
        if (customer) {
            return find("select distinct si.currency.id from EdsSaleInvoice si where si.client.id=? and " + ServerUtils.checkForDeleted("si.deleted")
                    + " and si.status.code in ('" + Constants.APPROVE + "', '" + Constants.OPEN + "', '" + Constants.OVER_DUE + "')", crmAccountID);
        } else {
            return find("select distinct pi.currency.id from EdsPurchaseInvoice pi where pi.supplier.id=? and " + ServerUtils.checkForDeleted("pi.deleted")
                    + " and pi.status.code in ('" + Constants.APPROVE + "', '" + Constants.OPEN + "', '" + Constants.OVER_DUE + "')", crmAccountID);
        }
    }

    @Override
    public Integer getSaleInvoiceListCount(ListingFilterParameter fp, boolean isRecurringInvoice) {
        if (fp == null) {
            fp = new ListingFilterParameter();
        }
        Map<String, Object> param = new HashMap<>();

        StringBuilder sql = new StringBuilder();
        sql.append("select count(si) from " + (isRecurringInvoice ? "EdsRecurringInvoice " : "EdsSaleInvoice ") + " si ");

        //Below we are getting access only to PM.
        EdsUser user = getUser();
        if (fp.isLookUp() && user != null && roleManager.hasRole(user, PM) && !roleManager.hasEitherRoles(user, ACCOUNTANT, DR, ADMIN)) {
            sql.append(" left join si.relatedProject pr where " + ServerUtils.checkForDeleted("si.deleted") + " and (pr.manager = :user or pr.backupManager = :user");
            sql.append(" or pr.backupManager2 = :user  or pr.backupManager3 = :user  or pr.backupManager4 = :user");
            sql.append(" or pr.backupManager5 = :user or pr.backupManager6 = :user or pr.backupManager7 = :user");
            sql.append(" or pr.backupManager8 = :user or pr.backupManager9 = :user or pr.backupManager10 = :user");
            sql.append(" or si.creator = :user) ");
            param.put("user", user);
        } else {
            sql.append(" where " + ServerUtils.checkForDeleted("si.deleted"));
        }

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

        return (Integer) findSingleByNamedParams(sql.toString(), param);
    }

    public List<EdsInvoice> getProductInvoice(Integer productID, String type) {
        return (List<EdsInvoice>) find("select it.invoice from EdsInvoiceItem it where it.item.objectID=? and it.invoice.type =? and " + ServerUtils.checkForDeleted("it.deleted") + " and " + ServerUtils.checkForDeleted("it.invoice.deleted"), productID, type);
    }

    public List<EdsInvoiceItem> getProjectInvoiceItems(Integer projectID) {
        return (List<EdsInvoiceItem>) find("select si from EdsInvoiceItem si where si.invoice.relatedProject.objectID=? and " + ServerUtils.checkForDeleted("si.deleted") + " and " + ServerUtils.checkForDeleted("si.invoice.deleted"), projectID);
    }

    @Override
    public HashMap<String, BigDecimal> getInvoiceTransactionsForChart(Date fromDate, Date toDate, boolean isPayable) {
        HashMap<String, BigDecimal> resultMap = new HashMap<>();

        StringBuilder sql = new StringBuilder();
        sql.append("SELECT ")
                .append("to_char((date_trunc('month', inv.invoiceDate) + interval '1 month' - interval '1 day'), 'Mon yy') as inv_date, ")
                .append("SUM(CASE WHEN inv.isCreditNote is true THEN 0-COALESCE(inv.total, 0) ELSE COALESCE(inv.total, 0) END) amount ")
                .append("FROM ")
                .append(getCompanyId()).append(".invoice inv \n")
                .append("JOIN ").append(getCompanyId()).append(".reference status on status.id = inv.status_id \n")
                .append("WHERE inv.deleted is not true AND inv.type = '" + (isPayable ? PAYABLE : RECEIVABLE) + "' \n")
                .append("AND status.code not in ('" + DRAFT + "', '" + REVERSED + "') \n")
                .append("AND inv.invoiceDate between ? and ? ")
                .append("GROUP BY to_char((date_trunc('month', inv.invoiceDate) + interval '1 month' - interval '1 day'), 'Mon yy') ");

        List<Object[]> items = findNative(sql.toString(), fromDate, toDate);

        if (items != null && !items.isEmpty()) {
            for (Object[] objects : items) {
                resultMap.put((String) objects[0], (BigDecimal) objects[1]);
            }
        }

        return resultMap;
    }

    @Override
    public BigDecimal getInvoiceTotalByCreditNoteId(Integer invCreditNoteId) {
        String schema = ServerSecurityContext.getInstance().getCompanyId();
        StringBuilder sqlQuery = new StringBuilder();
        sqlQuery.append("select i.total from \"" + schema + "\".invoicepayments ip " +
                "left join \"" + schema + "\".invoice i on i.id=ip.invoiceid " +
                "where ip.creditnoteid=" + invCreditNoteId);
        return (BigDecimal) findNativeSingle(sqlQuery.toString());
    }

    @Override
    public BigDecimal getInvoiceQuoteUnrecTotal(Integer invoiceID, String unrecRevCode) {
        StringBuilder sql = new StringBuilder();
        sql.append("select sum(coalesce(qty,0) * coalesce(unitPrice,0)) from ").append(getCompanyId()).append(".quoteItem qi \n");
        sql.append("inner join ").append(getCompanyId()).append(".account a on a.id = qi.account_id \n");
        sql.append("inner join ").append(getCompanyId()).append(".quote q on q.id = qi.quote_id \n");
        sql.append("inner join ").append(getCompanyId()).append(".converted_items ci on ci.quote_id = q.id \n");
        sql.append("inner join ").append(getCompanyId()).append(".invoice i on i.id = ci.invoice_id \n");
        sql.append("where q.deleted is not true and a.accountCode = '").append(unrecRevCode).append("' \n");
        sql.append("and i.id = ").append(invoiceID);

        return (BigDecimal) findNativeSingle(sql.toString());
    }

    @Override
    public List<EdsInvoiceItem> getItemListAsExpenseByInvoice(Integer invoiceId) {
        StringBuilder sql = new StringBuilder();
        sql.append("select ii from EdsInvoiceItem ii ");
        sql.append(" left join ii.saleInvoice sinv ");
        sql.append(" where sinv.objectID = " + invoiceId);

        return find(sql.toString());
    }

    @Override
    public BigDecimal getPreviousConvertedSaleInvoiceItemsQuantity(Integer itemId, Integer quoteId, Date creationDate) {
        StringBuilder sql = new StringBuilder();
        sql.append("select sum(ii.qty) ");
        sql.append(" from ").append(getCompanyId()).append(".invoice i ");
        sql.append(" left join ").append(getCompanyId()).append(".converted_items ci on i.id = ci.invoice_id ");
        sql.append(" left join ").append(getCompanyId()).append(".invoiceitem ii on i.id = ii.invoice_id ");
        sql.append(" left join ").append(getCompanyId()).append(".salequote sq on ci.quote_id = sq.id ");
        sql.append(" where ii.item_id = ").append(itemId);
        sql.append(" and i.creationDate < '").append(creationDate != null ? creationDate : new Date()).append("'");
        sql.append(" and ci.quote_id = ").append(quoteId);
        sql.append(" and sq.progressInvoicing = true ");
        sql.append(" and i.deleted = false ");
        return (BigDecimal) findNativeSingle(sql.toString());
    }

    @Override
    public BigDecimal getConvertedQuoteItemQuantity(Integer itemId, Integer quoteId, Date creationDate, Integer quoteItemId) {
        StringBuilder sql = new StringBuilder();
        sql.append("select qi.qty ");
        sql.append(" from ").append(getCompanyId()).append(".invoice i ");
        sql.append(" left join ").append(getCompanyId()).append(".converted_items ci on i.id = ci.invoice_id ");
        sql.append(" left join ").append(getCompanyId()).append(".quoteitem qi on qi.quote_id = ci.quote_id ");
        sql.append(" left join ").append(getCompanyId()).append(".salequote sq on qi.quote_id = sq.id ");
        sql.append(" where qi.item_id = ").append(itemId);
        sql.append(" and i.creationDate = '").append(creationDate != null ? creationDate : new Date()).append("'");
        sql.append(" and ci.quote_id = ").append(quoteId);
        if (quoteItemId != null) {
            sql.append(" and qi.id = ").append(quoteItemId);
        }
        sql.append(" and sq.progressInvoicing = true ");
        return (BigDecimal) findNativeSingle(sql.toString());
    }

    @Override
    public List<NewInvoice> getInvoiceDueAmountsByProjectId(Integer projectId, Integer invoiceId, Date from, Date to) {
        StringBuilder sql = new StringBuilder()
                .append("SELECT\n")
                .append("  inv.number                                                                              AS reference,\n")
                .append("  CASE WHEN inv.totalininvoicecurrency NOTNULL\n")
                .append("    THEN inv.totalininvoicecurrency\n")
                .append("  ELSE inv.total * inv.exchangerate END - sum(coalesce(ip.amount / ip.exchangerate, 0.0)) AS dueAmount,\n")
                .append("  inv.invoicedate                                                                         AS date\n")
                .append("FROM ").append(getCompanyId()).append(".saleinvoice si\n")
                .append("  JOIN ").append(getCompanyId()).append(".invoice inv ON si.id = inv.id\n")
                .append("  JOIN ").append(getCompanyId()).append(".reference ref ON inv.status_id = ref.id AND ref.code != 'PAID' AND ref.code != 'REVERSED'\n")
                .append("  LEFT JOIN ").append(getCompanyId()).append(".invoicepayments ip ON inv.id = ip.invoiceid AND (ip.deleted IS NULL OR ip.deleted = FALSE)\n")
                .append("  JOIN (SELECT\n")
                .append("          invoice_id      AS invoiceId,\n")
                .append("          max(project_id) AS projectId\n")
                .append("        FROM ").append(getCompanyId()).append(".invoiceitem\n")
                .append("        WHERE invoice_id NOT IN (SELECT invoice_id\n")
                .append("                                 FROM ").append(getCompanyId()).append(".invoiceitem\n")
                .append("                                 WHERE project_id IS NULL)\n")
                .append("        GROUP BY invoice_id\n")
                .append("        HAVING count(DISTINCT project_id) = 1\n")
                .append("        ORDER BY invoice_id) ct ON si.id = ct.invoiceId AND ct.projectId = ?\n")
                .append("WHERE (inv.deleted IS NULL OR inv.deleted = FALSE) AND inv.id != ?\n");
        if (to != null) sql.append(" AND inv.invoicedate < '" + to + "'\n");
        sql.append("GROUP BY inv.id");

        List<Object[]> list = findNative(sql.toString(), projectId, invoiceId);
        List<NewInvoice> res = new ArrayList<>();
        for (Object[] obj : list) {
            NewInvoice invoice = new NewInvoice();
            invoice.setInvoiceNumber((String) obj[0]);
            invoice.setDuePayments((BigDecimal) obj[1]);
            invoice.setInvoiceDate(new DateNonConvertable((Date) obj[2]));
            res.add(invoice);
        }
        return res;
    }

    @Override
    public EdsInvoice getGDNInvoiceNumber(EdsShippingDataItem edsShippingDataItem) {
        final String queryString = "select inv from EdsInvoiceItem ii " +
                "    join ii.invoice inv " +
                "    where " + ServerUtils.checkForDeleted("ii.deleted") +
                "        and " + ServerUtils.checkForDeleted("inv.deleted") +
                "        and ii.quoteItemId=?";
        return (EdsInvoice) findSingle(queryString, edsShippingDataItem.getQuoteItemId());
    }

    @Override
    public Map<Integer, EdsInvoice> getInvoiceListByIds(List<Integer> invoiceIds) {
        Map<Integer, EdsInvoice> map = new HashMap<>();

        StringBuilder sql = new StringBuilder("select inv from EdsInvoiceItem ii \n");
        sql.append("join ii.invoice inv \n")
                .append(" where ").append(ServerUtils.checkForDeleted("ii.deleted")).append(" and ").append(ServerUtils.checkForDeleted("inv.deleted \n"))
                .append(" and ii.quoteItemId in (").append(ServerUtils.getAsCommoDelimited(invoiceIds, "0")).append(")");

        List<EdsInvoice> list = find(sql.toString());

        list.forEach(i -> {
            map.put(i.getObjectID(), i);
        });

        return map;
    }

    @Override
    public ArrayList<Integer> quoteCreatorInvoices(Integer creator) {
        String schema = ServerSecurityContext.getInstance().getCompanyId();
        StringBuilder sql = new StringBuilder();
        sql.append("select si.id from \"" + schema + "\".converted_items ci ");
        sql.append("join \"" + schema + "\".quote q on ci.quote_id=q.id ");
        sql.append("join \"" + schema + "\".invoice i on ci.invoice_id=i.id ");
        sql.append("join \"" + schema + "\".saleinvoice si on si.id=i.id ");
        sql.append("left join \"" + schema + "\".reference r on r.id=i.status_id ");
        sql.append("where q.creator_id= " + creator + " and " + ServerUtils.checkForDeleted("i.deleted"));
        return (ArrayList<Integer>) findNative(sql.toString());
    }

    @Override
    public List<SelectItem> getPurchaseInvoicesForLookUp(ListingFilterParameter filterParameter) {

        String schema = ServerSecurityContext.getInstance().getCompanyId();
        StringBuilder sql = new StringBuilder();
        sql.append("select i.id, i.number from \"" + schema + "\".purchaseinvoice pi ");
        sql.append("left join \"" + schema + "\".invoice i on i.id=pi.id ");
        sql.append("left join \"" + schema + "\".reference r on r.id=i.status_id ");

        sql.append("where " + ServerUtils.checkForDeleted("i.deleted"));
        if (filterParameter.getInvoiceType() != null) {
            sql.append(" and i.type='" + filterParameter.getInvoiceType() + "'");
        }
        sql.append(" and ( i.isCreditNote is null or i.isCreditNote=false) ");
        if (filterParameter.getStatusCode() != null) {
            sql.append(" and r.code !='" + filterParameter.getStatusCode() + "'");
        }

        List<Object[]> lists = (List<Object[]>) slaveEntityManager.createNativeQuery(sql.toString())
                .getResultList();

        List<SelectItem> piLists = new ArrayList<>();
        for (Object[] object : lists) {
            SelectItem item = new SelectItem();
            item.setId(((Integer) object[0]));
            item.setName((String) object[1]);
            piLists.add(item);
        }


        return piLists;
    }

    @Override
    public List<Object[]> getMultiQuoteAndGdnConvertedInvoiceData(Integer invoiceId) {

        String sql = "select i.number, it.id, it.product_number, it.name, iit.description, iit.qty, iit.unitprice, iit.net, iit.ammount, iit.discount, iit.discount_amount, gdns, gdnDates, qts " +
                "from " + getCompanyId() + ".invoice i " +
                "         join " + getCompanyId() + ".invoiceitem iit on i.id = iit.invoice_id " +
                "         join " + getCompanyId() + ".item it on iit.item_id = it.id " +
                "         left join (select cshd.invoice_id, " +
                "                           qi.item_id, " +
                "                           array_to_string(array_agg(distinct shd.number), ', ') gdns, " + " " +
                "                          array_to_string(array_agg(distinct to_char(shd.shippingdate, 'YYYY-MM-DD')), ', ') gdnDates, " +
                "                           array_to_string(array_agg(distinct q.number), ', ')   qts " +
                "                    from " + getCompanyId() + ".converted_shipping_data cshd " +
                "                             join " + getCompanyId() + ".shipping_data shd on cshd.shipping_data_id = shd.id and shd.deleted is not true " +
                "                             join " + getCompanyId() + ".shipping_data_items shdi on shd.id = shdi.shippingdataid " +
                "                             join " + getCompanyId() + ".quoteitem qi on shdi.quoteitemid = qi.id " +
                "                             join " + getCompanyId() + ".quote q on qi.quote_id = q.id " +
                "                   group by cshd.invoice_id, qi.item_id) cit on i.id = cit.invoice_id and iit.item_id = cit.item_id " +
                "where i.id = " + invoiceId;

        return slaveEntityManager.createNativeQuery(sql).getResultList();
    }

    @Override
    public boolean checkIsBatchConvert(Integer invoiceId) {
        return find("FROM EdsInvoiceItem WHERE quoteItemId <> null AND invoice.objectID = ? ", invoiceId).size() == 0;
    }

    @Override
    @Transactional
    public void updateConvertedQuotes(Integer saleInvoiceId, List<Integer> quoteIds, String methodKey) {
        if ("saveSI".equals(methodKey)) {
            updateNative("delete from " + getCompanyId() + ".converted_items where invoice_id = " + saleInvoiceId + " and quote_id in (" + ServerUtils.getAsCommoDelimited(quoteIds, "0", ",") + ")");
            StringBuilder sql = new StringBuilder();
            for (int i = 0; i < quoteIds.size(); i++) {
                if (sql.toString().isEmpty()) {
                    sql.append("INSERT INTO ").append(getCompanyId()).append(".converted_items(invoice_id, quote_id) values ");
                }
                sql.append("(").append(saleInvoiceId).append(", ").append(quoteIds.get(i)).append(")").append(i != quoteIds.size() - 1 ? "," : ";");
            }
            updateNative(sql.toString());

            EdsInvoice saleInvoice = get(saleInvoiceId);
            EdsReference status = referenceManager.findReference(INVOICE_STATUS, !DRAFT.equals(saleInvoice.getStatus().getCode()) ? INVOICED : CONVERTED);
            updateNative("update " + getCompanyId() + ".quote set status_id = " + status.getObjectID() + " where id in (" + ServerUtils.getAsCommoDelimited(quoteIds, "0", ",") + ") and status_id != " + referenceManager.findReference(INVOICE_STATUS, PARTIAL_SHIPPED).getObjectID());
        } else {
            updateNative("delete from " + getCompanyId() + ".converted_items where invoice_id = " + saleInvoiceId + " and quote_id in (" + ServerUtils.getAsCommoDelimited(quoteIds, "0", ",") + ")");

            StringBuilder sql = new StringBuilder("select status,  array_to_string(array_agg(distinct q_id), ',') ids from ( \n")
                    .append("select sq.id q_id, ")
                    .append("  (case ")
                    .append("    when sq.issalesorder is false then 'sq' ")
                    .append("    when sum(qi.qty) = sum(qi.qty) - sum(coalesce(shdi.receivedQty,0.0)) then 'so' ")
                    .append("    when sum(qi.qty) - sum(coalesce(shdi.receivedQty,0.0)) = 0 then 'shipped' ")
                    .append("    when sum(coalesce(shdi.receivedQty,0.0)) > 0 then 'partial_shipped' ")
                    .append("   end) status \n")
                    .append("from ").append(getCompanyId()).append(".quote q \n")
                    .append("  join ").append(getCompanyId()).append(".salequote sq on sq.id = q.id \n")
                    .append("  join ").append(getCompanyId()).append(".quoteitem qi on qi.quote_id = q.id \n")
                    .append("  left join ").append(getCompanyId()).append(".shipping_data_items shdi on shdi.quoteItemId = qi.id and shdi.deleted is not true \n")
                    .append("where q.deleted is not true \n and q.id in (").append(ServerUtils.getAsCommoDelimited(quoteIds, "0", ",")).append(") \n")
                    .append("group by sq.id) t group by status ");
            List<Object[]> list = findNative(sql.toString());
            list.forEach(objs -> {
                String status = (String) objs[0];
                String Ids = (String) objs[1];
                Integer statusID = null;
                if ("sq".equals(status)) {
                    statusID = referenceManager.findReference(INVOICE_STATUS, CLIENT_APPROVE).getObjectID();
                } else if ("so".equals(status)) {
                    statusID = referenceManager.findReference(INVOICE_STATUS, SALE_ORDER).getObjectID();
                } else if ("partial_shipped".equals(status)) {
                    statusID = referenceManager.findReference(INVOICE_STATUS, PARTIAL_SHIPPED).getObjectID();
                } else if ("shipped".equals(status)) {
                    statusID = referenceManager.findReference(INVOICE_STATUS, SHIPPED).getObjectID();
                }
                if (statusID != null && StringUtils.isNotBlank(Ids)) {
                    updateNative("update " + getCompanyId() + ".quote set status_id = " + statusID + " where id in (" + Ids + ")");

                    if ("partial_shipped".equals(status) || "shipped".equals(status)) {
                        updateNative("update " + getCompanyId() + ".shipping_data set status = null where id in (select shipping_data_id from " + getCompanyId() + ".converted_shipping_data where invoice_id = " + saleInvoiceId + ")");
                        updateNative("delete from " + getCompanyId() + ".converted_shipping_data where invoice_id = " + saleInvoiceId);
                    }
                }
            });
        }
    }

    @Override
    public String getConvertedQuoteIds(Integer invoiceId) {
        StringBuilder orderIds = new StringBuilder();
        findNative("SELECT quote_id FROM " + getCompanyId() + ".converted_items where invoice_id = ?", invoiceId).stream().forEach(quoteId -> {
            if (!orderIds.toString().isEmpty()) {
                orderIds.append(",");
            }
            orderIds.append(quoteId);
        });
        return !orderIds.toString().isEmpty() ? orderIds.toString() : null;
    }

    @Override
    public List<EdsInvoice> getCreditOrDebitNotes(ListingFilterParameter fp) {

        if (fp.isReceivable()) {
            return find("select inv from EdsSaleInvoice inv where inv.client.objectID=? and inv.currency.objectID = ? and " + ServerUtils.checkForDeleted("inv.deleted") + " and inv.isCreditNote is true and inv.status.code in('APPROVE', 'OVER_DUE')", fp.getCrmAccountId(), fp.getCurrencyID());
        } else {
            return find("select inv from EdsPurchaseInvoice inv where inv.supplier.objectID=? and inv.currency.objectID = ?  and " + ServerUtils.checkForDeleted("inv.deleted") + " and inv.isCreditNote is true  and inv.status.code in('APPROVE', 'OVER_DUE') ", fp.getCrmAccountId(), fp.getCurrencyID());
        }
    }

    @Override
    public Integer getDefaultTaxCalcType() {
        return (Integer) findSingle("SELECT taxCalculationType FROM EdsInvoicingSettings");
    }

    @Override
    public EdsInvoice getByObjectKey(String objectKey) {
        if (StringUtils.isBlank(objectKey)) {
            return null;
        }
        return (EdsInvoice) findSingle("select inv from EdsInvoice inv where objectKey = ?", objectKey);
    }

    @Override
    public String getOrderNumber(Integer itemId) {
        StringBuilder sql = new StringBuilder();
        sql.append("select number from " + getCompanyId() + ".quote where id = (select quote_id from " + getCompanyId() + ".quoteItem where id = " + itemId + ")");
        return (String) findNativeSingle(sql.toString());
    }

    @Override
    public List<Integer> getInvoicesByVat(Integer objectID) {
        StringBuilder sql = new StringBuilder("SELECT ii.invoice_id from ");
        sql.append(getCompanyId()).append(".invoiceItem ii ");
        sql.append(" left join ").append(getCompanyId()).append(".invoice i on ii.invoice_id = i.id");
        sql.append(" where ii.vat_id =").append(objectID);
        sql.append(" and ").append(ServerUtils.checkForDeleted("i.deleted"));
        sql.append(" group by ii.invoice_id ");
        return findNative(sql.toString());
    }

    @Override
    public List<EdsSaleInvoice> getInvoicesByCategoryId(Integer categoryId) {
        StringBuilder sql = new StringBuilder();
        sql.append("select s.*,inv.* from ").append(getCompanyId()).append(".saleinvoice s");
        sql.append(" join ").append(getCompanyId()).append(".invoiceitem si on si.invoice_id = s.id");
        sql.append(" join ").append(getCompanyId()).append(".item i on i.id = si.item_id");
        sql.append(" join ").append(getCompanyId()).append(".invoice inv on inv.id = s.id");
        sql.append(" where i.categoryid =").append(categoryId).append(" and (inv.deleted is null or inv.deleted <> true)");

        return (List<EdsSaleInvoice>) findNative(sql.toString(), EdsSaleInvoice.class);
    }

    @Override
    public void insertRevolutUrl(Integer salesInvoiceId, String url, String publicId, Integer companyId) {
        StringBuilder sql = new StringBuilder();
        sql.append("update \"").append(companyId).append("\"").append(".invoice set integrationid  = '").append(publicId).append("' where id = ").append(salesInvoiceId).append(" ;");
        updateNative(sql.toString());
        sql = new StringBuilder();
        sql.append("update ").append(getCompanyId()).append(".saleinvoice set revolut_url = '").append(url).append("' where id = ").append(salesInvoiceId);
        updateNative(sql.toString());
    }

    @Override
    public EdsInvoice getByIntegrationId(String integrationId) {
        if (StringUtils.isBlank(integrationId)) {
            return null;
        }
        return (EdsInvoice) findSingle("select inv from EdsInvoice inv where inv.deleted <> true and inv.integrationId = ?", integrationId);
    }

    @Override
    public String getPreviouseInoiceHash() {
        EdsInvoice invoice = (EdsInvoice) findSingle("select inv from EdsInvoice inv order by reportedDate desc");
        if (invoice != null && invoice.getZatcaHash() != null) {
            return invoice.getZatcaHash();
        }
        return null;
    }

    public List<Integer> getCompanyDeletedInvoicesForSolr(SolrReindexRpc solrReindex) {
        StringBuilder newsSqlQuery = new StringBuilder("SELECT ns.objectID FROM EdsSaleInvoice ns WHERE ns.deleted=true");
        newsSqlQuery.append(" AND ns.updatedDate>=").append("'").append(solrReindex.getLastUpdateTime()).append("'");
        if (solrReindex.getLastUpdateEndTime() != null) {
            newsSqlQuery.append(" and ns.updatedDate<='").append(solrReindex.getLastUpdateEndTime()).append("'");
        }
        return (List<Integer>) find(newsSqlQuery.toString());
    }

    public List<Integer> getCompanyDeletedPurchaseInvoicesForSolr(SolrReindexRpc solrReindex) {
        StringBuilder newsSqlQuery = new StringBuilder("SELECT ns.objectID FROM EdsPurchaseInvoice ns WHERE ns.deleted=true");
        newsSqlQuery.append(" AND ns.updatedDate>=").append("'").append(solrReindex.getLastUpdateTime()).append("'");
        if (solrReindex.getLastUpdateEndTime() != null) {
            newsSqlQuery.append(" and ns.updatedDate<='").append(solrReindex.getLastUpdateEndTime()).append("'");
        }
        return (List<Integer>) find(newsSqlQuery.toString());
    }

    @Override
    @Transactional(readOnly = true)
    public EdsInvoice getInvoiceForPdfGeneration(Integer invoiceId) {
        // Q1: Load invoice + invoiceItems (one bag) + all ManyToOne associations on invoice and items.
        List results = find(
                "SELECT DISTINCT i FROM EdsInvoice i " +
                        "LEFT JOIN FETCH i.invoiceItems items " +
                        "LEFT JOIN FETCH items.item edsItem " +
                        "LEFT JOIN FETCH edsItem.category " +
                        "LEFT JOIN FETCH edsItem.brand " +
                        "LEFT JOIN FETCH edsItem.account " +
                        "LEFT JOIN FETCH edsItem.vat " +
                        "LEFT JOIN FETCH items.vat " +
                        "LEFT JOIN FETCH items.doubleVat " +
                        "LEFT JOIN FETCH items.account itemAcc " +
                        "LEFT JOIN FETCH itemAcc.parent " +
                        "LEFT JOIN FETCH items.department " +
                        "LEFT JOIN FETCH items.warehouse " +
                        "LEFT JOIN FETCH items.project itemProj " +
                        "LEFT JOIN FETCH itemProj.parent " +
                        "LEFT JOIN FETCH itemProj.status " +
                        "LEFT JOIN FETCH items.itemDiscount " +
                        "LEFT JOIN FETCH items.itemDoubleDiscount " +
                        "LEFT JOIN FETCH items.unitMeasurement " +
                        "LEFT JOIN FETCH items.customFields " +
                        "LEFT JOIN FETCH i.currency " +
                        "LEFT JOIN FETCH i.creator " +
                        "LEFT JOIN FETCH i.markupAccount " +
                        "LEFT JOIN FETCH i.creditNoteInvoice " +
                        "LEFT JOIN FETCH i.convertedQuotes " +
                        "WHERE i.objectID = ?", invoiceId);

        EdsInvoice invoice = results.isEmpty() ? null : (EdsInvoice) results.get(0);
        if (invoice == null) {
            return null;
        }

        // Q2: Initialize payments collection with its ManyToOne associations.
        find("SELECT DISTINCT i FROM EdsInvoice i " +
                "LEFT JOIN FETCH i.payments p " +
                "LEFT JOIN FETCH p.user " +
                "LEFT JOIN FETCH p.account " +
                "LEFT JOIN FETCH p.status " +
                "LEFT JOIN FETCH p.crmAccount " +
                "LEFT JOIN FETCH p.creditNote " +
                "LEFT JOIN FETCH p.project " +
                "LEFT JOIN FETCH p.department " +
                "LEFT JOIN FETCH p.appliedPayment " +
                "WHERE i.objectID = ?", invoiceId);

        // Q3: Initialize refunds collection with its ManyToOne associations.
        find("SELECT DISTINCT i FROM EdsInvoice i " +
                "LEFT JOIN FETCH i.refunds r " +
                "LEFT JOIN FETCH r.user " +
                "LEFT JOIN FETCH r.account " +
                "LEFT JOIN FETCH r.status " +
                "LEFT JOIN FETCH r.crmAccount " +
                "LEFT JOIN FETCH r.creditNote " +
                "LEFT JOIN FETCH r.project " +
                "LEFT JOIN FETCH r.department " +
                "WHERE i.objectID = ?", invoiceId);

        // Q4: Initialize invoiceTaxTotals with their VAT associations.
        find("SELECT DISTINCT i FROM EdsInvoice i " +
                "LEFT JOIN FETCH i.invoiceTaxTotals tt " +
                "LEFT JOIN FETCH tt.vat " +
                "WHERE i.objectID = ?", invoiceId);

        // Q5: Initialize assemblyItems (and their productItem) for EdsItem entities used in this invoice.
        find("SELECT DISTINCT ei FROM EdsItem ei " +
                "LEFT JOIN FETCH ei.assemblyItems asm " +
                "LEFT JOIN FETCH asm.productItem " +
                "WHERE ei IN (" +
                "  SELECT ii.item FROM EdsInvoiceItem ii " +
                "  WHERE ii.invoice.objectID = ? AND ii.item IS NOT NULL" +
                ")", invoiceId);

        // Q6: Initialize productKitItems for EdsItem entities used in this invoice.
        find("SELECT DISTINCT ei FROM EdsItem ei " +
                "LEFT JOIN FETCH ei.productKitItems " +
                "WHERE ei IN (" +
                "  SELECT ii.item FROM EdsInvoiceItem ii " +
                "  WHERE ii.invoice.objectID = ? AND ii.item IS NOT NULL" +
                ")", invoiceId);

        // Q7: Initialize multiPrices (with currency) for EdsItem entities used in this invoice.
        find("SELECT DISTINCT ei FROM EdsItem ei " +
                "LEFT JOIN FETCH ei.multiPrices mp " +
                "LEFT JOIN FETCH mp.currency " +
                "WHERE ei IN (" +
                "  SELECT ii.item FROM EdsInvoiceItem ii " +
                "  WHERE ii.invoice.objectID = ? AND ii.item IS NOT NULL" +
                ")", invoiceId);

        // Q8: Initialize discounts for EdsItem entities used in this invoice.
        find("SELECT DISTINCT ei FROM EdsItem ei " +
                "LEFT JOIN FETCH ei.discounts " +
                "WHERE ei IN (" +
                "  SELECT ii.item FROM EdsInvoiceItem ii " +
                "  WHERE ii.invoice.objectID = ? AND ii.item IS NOT NULL" +
                ")", invoiceId);

        return invoice;
    }

    @Override
    @Transactional(readOnly = true)
    public NewInvoice buildInvoiceData(EdsInvoice invoice) {
        NewInvoice result = new NewInvoice();

        result.setID(invoice.getObjectID());

        EdsCrmAccount clientSupp = invoice.getClientOrSupplier();
        result.setClientID(clientSupp.getObjectID());
        result.setClientName(clientSupp.getName());
        result.setClientNumber(clientSupp.getNumber());

        if (genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.ENABLE_CUSTOM_CRM_ACCOUNT)) {
            EdsCustomCrmAccount customCrm = customCrmAccountManager.getCustomCrmAccountByEntityTypeAndEntityId(
                    invoice.getObjectID(), EntityTypeEnum.SALE_INVOICE.name());
            if (customCrm != null) {
                result.setCustomCrmAccountId(customCrm.getObjectID());
                result.setCustomCrmAccountName(customCrm.getClientName());
            }
        }

        SelectItem creatorItem = new SelectItem();
        creatorItem.setId(invoice.getCreator().getObjectID());
        creatorItem.setName(invoice.getCreator().getName());
        result.setCreator(creatorItem);
        result.setCreatorName(creatorItem.getName());

        Integer clientCurrencyID = clientSupp.getCurrency() != null ? clientSupp.getCurrency().getObjectID() : null;
        TypeItem typeItem = new TypeItem(clientSupp.getObjectID(), clientSupp.getName(), clientSupp.getNumber(), clientCurrencyID);
        typeItem.setSubsidiary(clientSupp.getSubsidiary() != null);
        typeItem.setPlaceOfSupply(clientSupp.getPlaceOfSupply());
        if (clientSupp.getTaxTreatment() != null) {
            SelectItem treatment = clientSupp.getTaxTreatment().getAsSelectItem();
            treatment.setCode(clientSupp.getTaxTreatment().getCode());
            typeItem.setTaxTreatment(treatment);
        }
        if (clientSupp.getTerms() != null) {
            typeItem.setTermsItem(clientSupp.getTerms().getAsRPC());
        }
        if (clientSupp.getPaymentMethod() != null) {
            typeItem.setPaymentType(clientSupp.getPaymentMethod().getName());
        }
        typeItem.setBillAddressID(invoice.getBillAddressID());
        typeItem.setMailAddressID(invoice.getMailAddressID());
        result.setTypeItem(typeItem);
        result.setBillAddressID(invoice.getBillAddressID());
        result.setMailAddressID(invoice.getMailAddressID());

        if (invoice.getReceivablePayable() != null) {
            result.setAccountsReceivablePayable(invoice.getReceivablePayable().createAccountItem());
            typeItem.setAccountsReceivablePayable(invoice.getReceivablePayable().createAccountItem());
        }

        if (invoice.getClientContact() != null) {
            result.setClientContactID(invoice.getClientContact().getObjectID());
            result.setClientContactEmail(invoice.getClientContact().getPrimaryEmail());
        } else if (invoice instanceof EdsSaleInvoice
                && ((EdsSaleInvoice) invoice).getClient() != null
                && ((EdsSaleInvoice) invoice).getClient().getPrimaryContact() != null) {
            result.setClientContactID(((EdsSaleInvoice) invoice).getClient().getPrimaryContact().getObjectID());
            result.setClientContactEmail(((EdsSaleInvoice) invoice).getClient().getPrimaryContact().getPrimaryEmail());
        }

        if (invoice.getCurrency() != null) {
            result.setCurrencyID(invoice.getCurrency().getObjectID());
            result.setCurrencyName(invoice.getCurrency().getName());
            result.setCurrencySymbol(invoice.getCurrency().getSymbol());
        }
        result.setPoNumber(invoice.getPoNumber());
        result.setQuoteNumberCN(invoice.getQuoteNumberCN());
        result.setInvoiceNumber(invoice.getNumber());
        if (invoice.getInvoiceDate() != null) result.setInvoiceDate(new DateNonConvertable(invoice.getInvoiceDate()));
        if (invoice.getDueDate() != null) result.setDueDate(new DateNonConvertable(invoice.getDueDate()));
        result.setReference(invoice.getReference());

        EdsProject relatedProject = invoice.getRelatedProject();
        if (relatedProject != null) {
            String projLabel = (relatedProject.getNumber() != null && !relatedProject.getNumber().trim().isEmpty()
                    ? relatedProject.getNumber() + " -> " : "") + relatedProject.getName();
            result.setRelatedProject(new SelectItem(relatedProject.getObjectID(), projLabel, relatedProject.getNumber()));
            result.setProjectStatusCode(relatedProject.getStatus().getCode());
        }
        if (invoice instanceof EdsSaleInvoice) {
            result.setProjectBasedInvoice(((EdsSaleInvoice) invoice).isProjectBasedInvoice());
        }
        result.setZatcaStatus(invoice.getZatcaStatus());
        result.setIntroduction(invoice.getIntroduction());

        // ── Part 2: invoice line items (replaces EdsInvoice.initItems()) ─────────────
        boolean inventoryItemIncluded = false;
        ArrayList<NewInvoiceItem> invoiceItems = new ArrayList<>();

        Map<Integer, SelectItem> faiCategoryMap;
        if (invoice instanceof EdsSaleInvoice) {
            faiCategoryMap = referenceManager.listReferences(EdsVat.FAI_CATEGORY).stream()
                    .map(r -> new SelectItem(r.getObjectID(), r.getName()))
                    .collect(Collectors.toMap(SelectItem::getId, Function.identity()));
        } else if (invoice instanceof EdsPurchaseInvoice) {
            faiCategoryMap = referenceManager.listReferences(EdsVat.FAI_PURCHASE_CATEGORY).stream()
                    .map(r -> new SelectItem(r.getObjectID(), r.getName()))
                    .collect(Collectors.toMap(SelectItem::getId, Function.identity()));
        } else {
            faiCategoryMap = new HashMap<>();
        }

        List<CompanyCustomFieldItem> itemCustomFieldTemplate = invoice.getItemCustomFields();
        for (EdsInvoiceItem lineItem : invoice.getInvoiceItems()) {
            NewInvoiceItem invItem = invoice.getItem(lineItem);

            // Clone the custom-field template for each item (mirrors generateCloneItemCustomFields())
            if (itemCustomFieldTemplate != null && !itemCustomFieldTemplate.isEmpty()
                    && lineItem.getCustomFields() != null) {
                ArrayList<CompanyCustomFieldItem> clonedCF = new ArrayList<>();
                for (CompanyCustomFieldItem cf : itemCustomFieldTemplate) {
                    clonedCF.add(cf.cloneObject());
                }
                invItem.setCustomFieldItems(CustomFieldsUtils.setRPCCustomFieldItems(lineItem.getCustomFields(), clonedCF));
            }

            Optional.ofNullable(lineItem.getFromDate()).ifPresent(d -> invItem.setFromDate(new DateNonConvertable(d)));
            Optional.ofNullable(lineItem.getToDate()).ifPresent(d -> invItem.setToDate(new DateNonConvertable(d)));

            if (lineItem.getVat() != null && invItem.getTaxItem() != null) {
                TaxItem taxItem = invItem.getTaxItem();
                if (lineItem.getVat().getFaiCategorieIds() != null) {
                    SelectItem[] cats = lineItem.getVat().getFaiCategorieIds().stream()
                            .map(faiCategoryMap::get).filter(Objects::nonNull).toArray(SelectItem[]::new);
                    taxItem.setFaiCategories(cats);
                }
                if (lineItem.getVat().getFaiPurchaseCategoryIds() != null) {
                    SelectItem[] purchaseCats = lineItem.getVat().getFaiPurchaseCategoryIds().stream()
                            .map(faiCategoryMap::get).filter(Objects::nonNull).toArray(SelectItem[]::new);
                    taxItem.setFaiPurchaseCategories(purchaseCats);
                }
                invItem.setTaxItem(taxItem);
            }
            invItem.setFaiCategory(faiCategoryMap.getOrDefault(lineItem.getFaiCategoryId(), null));
            invoiceItems.add(invItem);

            if (lineItem.getItem() != null && lineItem.getItem().getType() != null
                    && (EdsItem.INVENTORY_ITEM.equals(lineItem.getItem().getType())
                    || EdsItem.RENTAL_ITEM.equals(lineItem.getItem().getType())
                    || EdsItem.PRODUCT_KIT.equals(lineItem.getItem().getType()))) {
                inventoryItemIncluded = true;
            }
        }
        result.setItems(invoiceItems.toArray(new NewInvoiceItem[]{}));
        result.setInventoryItemIncluded(inventoryItemIncluded);

        result.setSubtotal(invoice.getSubtotal() != null ? invoice.getSubtotal() : AccountingConstants.ZERO);
        result.setTotalTaxes(invoice.getTotalTaxes());
        result.setTotal(invoice.getTotal() != null ? invoice.getTotal() : AccountingConstants.ZERO);
        result.setAmount(invoice.getTotalInInvoiceCurrency() != null ? invoice.getTotalInInvoiceCurrency() : AccountingConstants.ZERO);
        result.setTotalInInvoiceCurrency(invoice.getTotalInInvoiceCurrency());
        result.setComissionAmount(invoice.getComissionAmount() != null ? invoice.getComissionAmount() : AccountingConstants.ZERO);
        result.setClientMessage(invoice.getClientMessage());
        result.setStatus(invoice.getStatus() != null ? invoice.getStatus().getName() : "");
        result.setStatusCode(invoice.getStatus() != null ? invoice.getStatus().getCode() : "");
        result.setType(invoice.getType());
        result.setPaymentInstruction(invoice.getPaymentInstruction());
        result.setExchageRate(invoice.getExchangeRate());
        result.setDeleted(invoice.isDeleted());
        result.setLastUpdateDate(invoice.getUpdatedDate());
        result.setLastUpdater(invoice.getUpdater() != null ? invoice.getUpdater().getName() : "");

        result.setTaxCalculationType(invoice.getTaxCalculationType());
        result.setTotalDiscount(invoice.getTotalDiscount());
        result.setCalcScale(invoice.getCalcScale());

        if (!genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.ENABLE_MULTI_QUOTE_CONVERT)) {
            if (invoice.getConvertedQuotes() != null && invoice.getConvertedQuotes().size() == 1) {
                result.setConvertedItemID(invoice.getConvertedQuotes().iterator().next().getObjectID());
            }
        } else {
            result.setOrderBaseinvoiceOrderIds(getConvertedQuoteIds(result.getID()));
        }

        if (!invoice.getInvoiceTaxTotals().isEmpty()) {
            List<TotalTaxItem> totalTaxItems = new LinkedList<>();
            for (EdsInvoiceTaxTotal taxTotal : invoice.getInvoiceTaxTotals()) {
                EdsVat vat = taxTotal.getVat();
                if (vat != null) {
                    TotalTaxItem tti = new TotalTaxItem();
                    tti.setTaxItem(vat.createTaxItem());
                    tti.setTaxAmount(taxTotal.getAmount());
                    totalTaxItems.add(tti);
                }
            }
            result.setTotalTaxItems(totalTaxItems.toArray(new TotalTaxItem[]{}));
        }

        result.setBillableExpenseAmount(invoice.getBillExpTotal());
        result.setBillableExpenseTaxAmount(invoice.getBillExpTaxTotal());
        result.setHasBillableExpense(invoice.getBillExpTotal() != null
                && invoice.getBillExpTotal().compareTo(BigDecimal.ZERO) != 0);
        result.setMarkupAmount(invoice.getMarkupAmount());
        if (invoice.getMarkupAccount() != null) {
            result.setMarkupAccount(new SelectItem(invoice.getMarkupAccount().getObjectID(), invoice.getMarkupAccount().getName()));
        }
        result.setPercent(invoice.isPercent());

        ArrayList<BillableExpenseItem> beList = null;
        if (invoice.getExpense() != null && !invoice.getExpense().isEmpty()) {
            beList = new ArrayList<>();
            for (EdsExpense exp : invoice.getExpense()) beList.add(exp.createBillableExpenseItem(true));
        }
        if (invoice.getItemsAsExpense() != null && !invoice.getItemsAsExpense().isEmpty()) {
            if (beList == null) beList = new ArrayList<>();
            for (EdsInvoiceItem exp : invoice.getItemsAsExpense()) beList.add(exp.createBillableExpenseItem(true));
        }
        if (invoice.getMjItemsAsExpense() != null && !invoice.getMjItemsAsExpense().isEmpty()) {
            if (beList == null) beList = new ArrayList<>();
            for (EdsManualJournalItem exp : invoice.getMjItemsAsExpense())
                beList.add(exp.createBillableExpenseItem(true));
        }
        if (invoice.getBtItemsAsExpense() != null && !invoice.getBtItemsAsExpense().isEmpty()) {
            if (beList == null) beList = new ArrayList<>();
            for (EdsBankTransferItem exp : invoice.getBtItemsAsExpense())
                beList.add(exp.createBillableExpenseItem(true));
        }
        if (invoice.getBchItemsAsExpense() != null && !invoice.getBchItemsAsExpense().isEmpty()) {
            if (beList == null) beList = new ArrayList<>();
            for (EdsBankCheckItem exp : invoice.getBchItemsAsExpense()) beList.add(exp.createBillableExpenseItem(true));
        }
        result.setExpenses(beList);

        result.setFixedAssetRelated(invoice.isFixedAssetRelated());
        if (invoice.getCreditNoteInvoice() != null) {
            result.setRelatedInvoiceNumber(invoice.getCreditNoteInvoice().getNumber());
            result.setRelatedInvoiceDate(invoice.getCreditNoteInvoice().getInvoiceDate());
        }
        result.setCreationDate(invoice.getCreationDate());
        result.setCreditNote(invoice.isCreditNote());
        result.setNoteReason(invoice.getNoteReason());
        result.setPaymentTypeCode(invoice.getNotePaymentCode());

        List<EdsInvoicePayment> paymentsOrRefunds = invoice.isCreditNote()
                ? invoice.getRefunds() : invoice.getPayments();
        List<PaymentItem> paymentItems = new ArrayList<>();
        int calcScale = ServerUtils.getSystemCalculationScale();
        BigDecimal paidAmount = AccountingConstants.ZERO;

        for (EdsInvoicePayment p : paymentsOrRefunds) {
            // calcScale update applies to ALL payments (including reversed) for max precision
            if (p.getCalcScale() != null && calcScale < p.getCalcScale()) {
                calcScale = p.getCalcScale();
            }
            boolean isReversed = p.getStatus() != null
                    && EdsInvoicePayment.REVERSED.equals(p.getStatus().getCode());
            if (!isReversed) {
                paymentItems.add(p.getPaymentAsRPC());
                if (!p.isDeleted()) {
                    Integer pmtCurrencyId = p.getCreditNote() != null && p.getCreditNote().getCurrency() != null
                            ? p.getCreditNote().getCurrency().getObjectID() : p.getCurrencyID();
                    if (invoice.getCurrency() != null && invoice.getCurrency().getObjectID().equals(pmtCurrencyId)) {
                        paidAmount = paidAmount.add(
                                        p.getAmountInInvoiceCurrency() != null ? p.getAmountInInvoiceCurrency() : p.getAmount())
                                .setScale(calcScale, RoundingMode.HALF_UP);
                    } else {
                        paidAmount = paidAmount.add(
                                (p.getAmountInInvoiceCurrency() != null ? p.getAmountInInvoiceCurrency() : p.getAmount())
                                        .divide(p.getExchangeRate(), calcScale, RoundingMode.HALF_UP));
                    }
                }
            }
        }
        paymentItems.sort(Comparator.comparing(PaymentItem::getObjectId));
        result.setPaymentItems(paymentItems.toArray(new PaymentItem[]{}));
        result.setPaidAmount(paidAmount);

        return result;
    }

    public List<CrmAccountInvoiceTO> getPrioritizedInvoices(List<Integer> crmAccountIds) {
        StringBuilder sql = new StringBuilder(" select distinct on (ca.id) ca.id as client_id, cc.id as contact_id, i.dueDate, i.id as invoice_id \n");
        sql.append("from ").append(getCompanyId()).append(".crmAccount ca \n");
        sql.append("left join ").append(getCompanyId()).append(".crmContact cc on ca.id = cc.crmAccount \n");
        sql.append("left join ").append(getCompanyId()).append(".saleinvoice si on ca.id = si.client_id \n");
        sql.append("left join ").append(getCompanyId()).append(".invoice i on si.id = i.id \n");
        sql.append("left join ").append(getCompanyId()).append(".reference ir on i.status_id = ir.id \n");
        sql.append("where i.deleted is not true and ir.code = 'PAID' and ca.id in (").append(ServerUtils.getAsCommoDelimited(crmAccountIds, "0")).append(") \n");
        sql.append("order by ca.id, i.dueDate desc ");
        List<Object[]> resultList = slaveEntityManager.createNativeQuery(sql.toString()).getResultList();
        return resultList.stream().map(data -> new CrmAccountInvoiceTO((Integer) data[0], (Integer) data[1], (Date) data[2], (Integer) data[3])).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Map<Integer, NewInvoiceItem> getInvoiceItems(Integer entityId, boolean isQuote) {
        StringBuilder selectClause = new StringBuilder("select\n\t");
        selectClause.append("invi.id,\n\t")
                .append("i.id as item_id,\n\t")
                .append("(i.product_number || ' -> ' || i.name) as fullItemName,\n\t")
                .append("i.name as item_name,\n\t")
                .append("i.product_number as product_number,\n\t")
                .append("pc.name as item_category,\n\t")
                .append("i.type as product_type,\n\t")
                .append("coalesce(i.purchased_from_supplier, false) as product_purchased_from_supplier,\n\t")
                .append(hasInventoryCheck(isQuote)).append(",")
                .append("coalesce(i.inventory_tracking_enabled, false) as inventory_tracking_enabled,\n\t")
                .append("coalesce(i.batch_tracking_enabled, false) as batch_tracking_enabled,\n\t")
                .append("coalesce(i.track_batches_enabled, false) as track_batches_enabled,\n\t")
                .append("b.id brand_id,\n\t")
                .append("b.name brand_name,\n\t")
                .append("coalesce(i.sellingprice, 0.0000) as original_price,\n\t")
                .append("CAST(mp.multi_prices_map AS text) as multi_prices_map,\n\t")
                .append("CAST(dis_list.item_discount_list AS text) as item_discount_list,\n\t")
                .append("coalesce(i.type, 1) as item_type,\n\t")
                .append("i.barcode as item_bar_code,\n\t")
                .append("stock.items_in_stock,\n\t")
                .append("i.unitPrice as unit_cost,\n\t")
                .append("invi.itemName as inv_item_name,\n\t")
                .append("invi.itemName as full_item_name,\n\t")
                .append("COALESCE(inv.taxCalculationType, ").append(TAX_CALCULATION_EXCLUSIVE).append(")  tax_calculation_type,\n\t")
                .append("um.id unit_measurement_id,\n\t")
                .append("um.name unit_measurement_name,\n\t")
                .append("um.description unit_measurement_description,\n\t")
                .append("invi.shortLink,\n\t")
                .append("invi.description inv_item_description,\n\t")
                .append("coalesce(invi.qty, 0.0000) as item_qty,\n\t")
                .append("invi.uuid,\n\t")
                .append("invi.isLumpsum,\n\t")
                .append("invi.unitPrice inv_item_unitprice,\n\t")
                .append("invi.priceLevelAmount,\n\t")
                .append("invi.comission,\n\t")
                .append("invi.discount discount_percent,\n\t")
                .append("invi.discount_amount,\n\t")
                .append("i.discountAmount current_product_discount_amount,\n\t")
                .append("d.id item_discount_id,\n\t")
                .append("d.name item_discount_name,\n\t")
                .append("invi.discountItemStaticType,\n\t")
                .append("invi.double_discount,\n\t")
                .append("invi.double_discount_amount,\n\t")
                .append("dd.id item_double_discount_id,\n\t")
                .append("dd.name item_double_discount_name,\n\t")
                .append("coalesce(invi.receivedAllocation, 0.00000) as received_allocation,\n\t")
                .append("invi.net,\n\t")
                .append("t.id department_id,\n\t")
                .append("t.name department_name,\n\t")
                .append("a.id account_id,\n\t")
                .append("a.accountCode,\n\t")
                .append("a.name account_name,\n\t")
                .append("atype.id account_type_id,\n\t")
                .append("atype.code account_type_code,\n\t")
                .append("atype.category account_type_category,\n\t")
                .append("a.key account_key,\n\t")
                .append("a.groupKey account_groupKey,\n\t")
                .append("cur.id currency_id,\n\t")
                .append("cur.name currency_name,\n\t")
                .append("pa.accountCode parent_accound_code,\n\t")
                .append("sa.id sales_account_id,\n\t")
                .append("sa.accountCode sales_account_code,\n\t")
                .append("sa.name sales_account_name,\n\t")
                .append("satype.id sales_account_type_id,\n\t")
                .append("satype.code sales_account_type_code,\n\t")
                .append("satype.category sales_account_type_category,\n\t")
                .append("sa.key sales_account_key,\n\t")
                .append("sa.groupKey sales_account_groupKey,\n\t")
                .append("scur.id sales_currency_id,\n\t")
                .append("scur.name sales_currency_name,\n\t")
                .append("psa.accountCode sales_parent_accound_code,\n\t")
                .append("invi.vat_id,\n\t")
                .append("invi.doublevat_id,\n\t")
                .append("CAST(case when invi.vat_id is not null then\n\t\t")
                .append("jsonb_build_object(\n\t\t\t")
                .append("'id',                  vat_rate.vat_id,\n\t\t\t\t")
                .append("'name',                case\n\t\t\t\t\t")
                .append("when vat_rate.tax_key in ('EXEMPT', 'OUT_OF_SCOPE')\n\t\t\t\t\t\t")
                .append("then vat_rate.tax_name\n\t\t\t\t\t")
                .append("else vat_rate.tax_name\n\t\t\t\t\t\t")
                .append("|| '(' || CAST(round(vat_rate.tax_rate, 2) AS text) || '%)'\n\t\t\t\t")
                .append("end,\n\t\t\t\t")
                .append("'taxPercent',          vat_rate.tax_rate,\n\t\t\t\t")
                .append("'effectiveTaxPercent', vat_rate.effective_rate,\n\t\t\t\t")
                .append("'taxType', vat_rate.tax_type,\n\t\t\t\t")
                .append("'taxKey',              vat_rate.tax_key\n\t\t\t")
                .append(")\n\t\t")
                .append("end AS text) as tax_item,\n\t")
                .append("CAST(case when invi.doublevat_id is not null then\n\t\t")
                .append("jsonb_build_object(\n\t\t\t")
                .append("'id',                  double_vat_rate.vat_id,\n\t\t\t\t")
                .append("'name',                case\n\t\t\t\t\t")
                .append("when double_vat_rate.tax_key in ('EXEMPT', 'OUT_OF_SCOPE')\n\t\t\t\t\t\t")
                .append("then double_vat_rate.tax_name\n\t\t\t\t\t")
                .append("else double_vat_rate.tax_name\n\t\t\t\t\t\t")
                .append("|| '(' || CAST(round(double_vat_rate.tax_rate, 2) AS text) || '%)'\n\t\t\t\t")
                .append("end,\n\t\t\t\t")
                .append("'taxPercent',          double_vat_rate.tax_rate,\n\t\t\t\t")
                .append("'effectiveTaxPercent', double_vat_rate.effective_rate,\n\t\t\t\t")
                .append("'taxType',             double_vat_rate.tax_type,\n\t\t\t\t")
                .append("'taxKey',              double_vat_rate.tax_key\n\t\t\t")
                .append(")\n\t\t")
                .append("end AS text) as double_tax_item,\n\t")
                .append("invi.ammount,\n\t")
                .append("invi.receiveType,\n\t")
                .append("coalesce(invi.receivedAmount, 0.00000) received_amount,\n\t")
                .append("coalesce(invi.receivedQty, 0.00000) received_qty,\n\t")
                .append("CAST(case when invi.warehouseid is not null then\n\t\t")
                .append("jsonb_build_object('id', w.id, 'name', w.name)\n\t")
                .append("end AS text) as warehouse,\n\t")
                .append("invi.convertedQty converted_qty,\n\t")
                .append("invi.convertedAmount converted_amount,\n\t")
                .append("coalesce(invi.receive, 0.00000) receive,\n\t")
                .append("CAST(case when invi.project_id is not null then\n\t\t")
                .append("jsonb_build_object('id', p.id, 'name', p.number || ' -> ' || p.name)\n\t")
                .append("end AS text) as project,\n\t")
                .append("CAST(case when p.parnetId is not null then\n\t\t")
                .append("jsonb_build_object('id', pp.id, 'name', pp.number || ' -> ' || pp.name)\n\t")
                .append("end AS text) as parent_project,\n\t");

        if (!isQuote) {
            selectClause.append("invi.projectBasedInvoiceDescription,\n\t")
                    .append("invi.expenceItemId expence_item_id,\n\t")
                    .append("invi.invoice_id,\n\t")
                    .append("invi.fromDate,\n\t")
                    .append("invi.toDate,\n\t")
                    .append("invi.faiCategoryId,\n")
                    .append("invi.quoteItemId quote_item_id,\n\t")
                    .append("invi.fromTimesheet from_time_sheet,\n\t")
                    .append("CAST(case when ca.id is not null then\n\t\t")
                    .append("jsonb_build_object('id', ca.id, 'name', ca.name)\n\t")
                    .append("end AS text) as client\n");
        } else {
            selectClause.append("invi.quote_id,\n\t")
                    .append("invi.shippedQty shipped_qty,\n\t")
                    .append("invi.pickable pickable\n");
        }
        selectClause.append("from ").append(getCompanyId());

        if (!isQuote) {
            selectClause.append(".invoiceitem invi \n")
                    .append("left join ").append(getCompanyId()).append(".invoice inv    on invi.invoice_id = inv.id\n")
                    .append("left join ").append(getCompanyId()).append(".crmAccount ca  on invi.client_id   = ca.id\n");
        } else {
            selectClause.append(".quoteItem invi \n")
                    .append("left join ").append(getCompanyId()).append(".quote inv    on invi.quote_id = inv.id\n");
        }

        String fromClause = """
                left join %1$s.warehouse w    on invi.warehouseid = w.id
                left join %1$s.project p      on invi.project_id  = p.id
                left join %1$s.project pp     on p.parnetId       = pp.id
                
                left join lateral (
                    select jsonb_strip_nulls(
                        jsonb_build_object(
                            'RECEIVABLE-1', i_mp.sellingprice,
                            'PAYABLE-1',    i_mp.unitPrice
                        )
                        ||
                        coalesce(
                            (
                                select jsonb_object_agg(
                                    concat(imp2.type, imp2.currency_id),
                                    imp2.sellingprice
                                )
                                from %1$s.item_multi_price imp2
                                where imp2.item_id = invi.item_id
                            ),
                            CAST('{}' AS jsonb)
                        )
                    ) as multi_prices_map
                    from %1$s.item i_mp
                    where i_mp.id = invi.item_id
                ) mp on true
                
                left join lateral (
                    select jsonb_agg(entry order by entry_order) as item_discount_list
                    from (
                        select 0 as entry_order,
                               jsonb_build_object('id', 0, 'name', 'Percentage') as entry
                
                        union all
                
                        select 1,
                               jsonb_build_object('id', 1, 'name', 'Fixed Amount')
                
                        union all
                
                        select
                            2 + row_number() over (order by dis.id),
                            jsonb_build_object(
                                'id',                     dis.id,
                                'name',                   dis.name,
                                'code',                   dis.code,
                                'description',            dis.description,
                                'active',                 dis.isActive,
                                'type',                   dis.type,
                                'percentage',             dis.percentage,
                                'fixedAmount',            dis.fixedAmount,
                                'multiRangeDiscountType', (
                                    select dmv.type
                                    from %1$s.discount_multirange_values dmv
                                    where dmv.discount_id = dis.id
                                    order by dmv.id desc
                                    limit 1
                                ),
                                'multiRangeItems', (
                                    select jsonb_agg(
                                        jsonb_build_object(
                                            'id',          dmv.id,
                                            'type',        dmv.type,
                                            'fromQty',     dmv.fromQty,
                                            'toQty',       dmv.toQty,
                                            'fromAmount',  dmv.fromAmount,
                                            'toAmount',    dmv.toAmount,
                                            'percentage',  dmv.percentage,
                                            'fixedAmount', dmv.fixedAmount
                                        )
                                        order by dmv.id
                                    )
                                    from %1$s.discount_multirange_values dmv
                                    where dmv.discount_id = dis.id
                                )
                            )
                        from %1$s.discount_applied_products dap
                        join %1$s.discounts dis on dap.discount_id = dis.id
                        where dap.product_id = invi.item_id
                          and coalesce(dis.deleted, false) = false
                          and coalesce(dis.isActive, true) = true
                    ) entries
                ) dis_list on true
                
                cross join lateral (
                    select coalesce(fs.enable_multi_warehouse, false) as multi_wh
                    from %1$s.financialsettings fs
                    limit 1
                ) fs_settings
                
                cross join lateral (
                    select w_def.id as default_wh_id
                    from %1$s.warehouse w_def
                    where w_def.isdefaultWarehouse = true
                    order by w_def.id desc
                    limit 1
                ) default_wh
                
                left join lateral (
                    select coalesce(sum(
                        case
                            when s.transaction_code = 'OUT'
                            then (0 - s.quantity)
                            else s.quantity
                        end
                    ), 0) as items_in_stock
                    from %1$s.item_stock s
                    join %1$s.transaction t on s.transactionid = t.id
                    where t.deleted is not true
                      and s.item_id = invi.item_id
                      and (
                          fs_settings.multi_wh = true
                          or s.warehouseid = default_wh.default_wh_id
                      )
                ) stock on true
                
                cross join lateral (
                    select
                        (coalesce(invi.qty, 0) * coalesce(invi.unitPrice, 0))
                        - (
                            case
                                when invi.discount is not null
                                    then round(coalesce(invi.qty, 0) * coalesce(invi.unitPrice, 0)
                                             * invi.discount / 100, 10)
                                else coalesce(invi.discount_amount, 0)
                            end
                            +
                            case
                                when invi.double_discount is not null
                                    then round(coalesce(invi.qty, 0) * coalesce(invi.unitPrice, 0)
                                             * invi.double_discount / 100, 10)
                                else coalesce(invi.double_discount_amount, 0)
                            end
                        ) as discounted_total
                ) as tax_calc
                
                left join lateral (
                    select
                        v.id      as vat_id,
                        v.taxType as tax_type,
                        v.name    as tax_name,
                        coalesce(
                            v.key,
                            case
                                when v.name ilike '%%Zero Rate%%'   then 'OUT_OF_SCOPE'
                                when v.name ilike '%%Exempt%%'       then 'EXEMPT'
                                when v.name ilike '%%Out of Scope%%' then 'OUT_OF_SCOPE'
                            end
                        ) as tax_key,
                        case
                            when v.groupTax = true then (
                                select round(sum(child_rate.total_rate), 10)
                                from %1$s.taxgroupitem tgi
                                join %1$s.vat child_v on tgi.itemid = child_v.id
                                join lateral (
                                    select sum(coalesce(tc2.rate, 0)) as total_rate
                                    from %1$s.taxcomponent tc2
                                    where tc2.taxid = child_v.id
                                ) child_rate on true
                                where tgi.taxid = v.id
                            )
                            else (
                                select round(sum(coalesce(tc.rate, 0)), 10)
                                from %1$s.taxcomponent tc
                                where tc.taxid = v.id
                            )
                        end as tax_rate,
                        coalesce(
                            case when v.groupTax = true then (
                                select round(sum(
                                    case
                                        when sum_tc.compound_sum > 0
                                        then (sum_tc.non_compound_sum / 100.0 + 1)
                                             * sum_tc.compound_sum
                                             + sum_tc.non_compound_sum
                                        else sum_tc.non_compound_sum
                                    end
                                ), 10)
                                from %1$s.taxgroupitem tgi
                                join %1$s.vat child_v on tgi.itemid = child_v.id
                                join lateral (
                                    select
                                        sum(case when tc2.compound = false then tc2.rate else 0 end) as non_compound_sum,
                                        sum(case when tc2.compound = true  then tc2.rate else 0 end) as compound_sum
                                    from %1$s.taxcomponent tc2
                                    where tc2.taxid = child_v.id
                                ) sum_tc on true
                                where tgi.taxid = v.id
                                  and child_v.groupTax = false
                            ) end,
                            (
                                select
                                    case
                                        when sum(case when tc.compound = true  then tc.rate else 0 end) > 0
                                        then round(
                                            (sum(case when tc.compound = false then tc.rate else 0 end) / 100.0 + 1)
                                            * sum(case when tc.compound = true  then tc.rate else 0 end)
                                            + sum(case when tc.compound = false then tc.rate else 0 end),
                                            10)
                                        else round(
                                            sum(case when tc.compound = false then tc.rate else 0 end),
                                            10)
                                    end
                                from %1$s.taxcomponent tc
                                where tc.taxid = v.id
                            ),
                            0
                        ) as effective_rate
                    from %1$s.vat v
                    where v.id = invi.vat_id
                ) as vat_rate on true
                
                -- Double VAT
                left join lateral (
                    select
                        v.id      as vat_id,
                        v.taxType as tax_type,
                        v.name    as tax_name,
                        coalesce(
                            v.key,
                            case
                                when v.name ilike '%%Zero Rate%%'   then 'OUT_OF_SCOPE'
                                when v.name ilike '%%Exempt%%'       then 'EXEMPT'
                                when v.name ilike '%%Out of Scope%%' then 'OUT_OF_SCOPE'
                            end
                        ) as tax_key,
                        case
                            when v.groupTax = true then (
                                select round(sum(child_rate.total_rate), 10)
                                from %1$s.taxgroupitem tgi
                                join %1$s.vat child_v on tgi.itemid = child_v.id
                                join lateral (
                                    select sum(coalesce(tc2.rate, 0)) as total_rate
                                    from %1$s.taxcomponent tc2
                                    where tc2.taxid = child_v.id
                                ) child_rate on true
                                where tgi.taxid = v.id
                            )
                            else (
                                select round(sum(coalesce(tc.rate, 0)), 10)
                                from %1$s.taxcomponent tc
                                where tc.taxid = v.id
                            )
                        end as tax_rate,
                        coalesce(
                            case when v.groupTax = true then (
                                select round(sum(
                                    case
                                        when sum_tc.compound_sum > 0
                                        then (sum_tc.non_compound_sum / 100.0 + 1)
                                             * sum_tc.compound_sum
                                             + sum_tc.non_compound_sum
                                        else sum_tc.non_compound_sum
                                    end
                                ), 10)
                                from %1$s.taxgroupitem tgi
                                join %1$s.vat child_v on tgi.itemid = child_v.id
                                join lateral (
                                    select
                                        sum(case when tc2.compound = false then tc2.rate else 0 end) as non_compound_sum,
                                        sum(case when tc2.compound = true  then tc2.rate else 0 end) as compound_sum
                                    from %1$s.taxcomponent tc2
                                    where tc2.taxid = child_v.id
                                ) sum_tc on true
                                where tgi.taxid = v.id
                                  and child_v.groupTax = false
                            ) end,
                            (
                                select
                                    case
                                        when sum(case when tc.compound = true  then tc.rate else 0 end) > 0
                                        then round(
                                            (sum(case when tc.compound = false then tc.rate else 0 end) / 100.0 + 1)
                                            * sum(case when tc.compound = true  then tc.rate else 0 end)
                                            + sum(case when tc.compound = false then tc.rate else 0 end),
                                            10)
                                        else round(
                                            sum(case when tc.compound = false then tc.rate else 0 end),
                                            10)
                                    end
                                from %1$s.taxcomponent tc
                                where tc.taxid = v.id
                            ),
                            0
                        ) as effective_rate
                    from %1$s.vat v
                    where v.id = invi.doublevat_id
                ) as double_vat_rate on true
                
                left join %1$s.UnitMeasurement um on invi.unitmeasurementid       = um.id
                left join %1$s.discounts d        on invi.item_discount_id        = d.id
                left join %1$s.discounts dd       on invi.item_double_discount_id = dd.id
                left join %1$s.team t             on invi.departmentid            = t.id
                left join %1$s.account a          on invi.account_id              = a.id
                left join %1$s.account pa         on a.parentid                   = pa.id
                left join accountType atype          on a.accountTypeId           = atype.id
                left join currency cur               on a.currencyid              = cur.id
                left join %1$s.item i             on invi.item_id                 = i.id
                left join %1$s.account sa         on i.accountid                  = sa.id
                left join %1$s.account psa        on sa.parentid                  = psa.id
                left join accountType satype         on sa.accountTypeId          = satype.id
                left join currency scur              on sa.currencyid             = scur.id
                left join %1$s.productcategory pc on i.categoryid                 = pc.id
                left join %1$s.brand b            on i.brandid                    = b.id
                where
                """.formatted(getCompanyId());

        selectClause.append(fromClause);

        if (isQuote) {
            selectClause.append(" invi.quote_id = ?");
        } else {
            selectClause.append(" invi.invoice_id = ?");
        }

        Query query = slaveEntityManager.createNativeQuery(String.valueOf(selectClause))
                .setParameter(1, entityId);

        @SuppressWarnings("unchecked")
        List<Object[]> rows = query.getResultList();

        Map<Integer, NewInvoiceItem> resultMap = new HashMap<>();

        if (!rows.isEmpty()) {
            final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

            for (Object[] row : rows) {
                NewInvoiceItem invoiceItem = new NewInvoiceItem();
                invoiceItem.setID((Integer) row[0]);
                if (row[1] != null) {
                    invoiceItem.setItemID((Integer) row[1]);
                    invoiceItem.setItemName((String) row[3]);
                    invoiceItem.setFullItemName((String) row[2]);
                    invoiceItem.setItemNumber((String) row[4]);
                    if (row[5] != null)
                        invoiceItem.setItemCategory((String) row[5]);
                    invoiceItem.setProductType((Integer) row[6]);
                    invoiceItem.setProductPurchasedFromSupplier((Boolean) row[7]);
                    invoiceItem.setHasInventoryInProductKit((Boolean) row[8]);
                    invoiceItem.setInventoryTrackingEnabled((Boolean) row[9]);
                    invoiceItem.setBatchTrackingEnabled((Boolean) row[10]);//for track serials
                    invoiceItem.setTrackBatchesEnabled((Boolean) row[11]);
                    if (row[12] != null) {
                        invoiceItem.setProductBrandID((Integer) row[12]);
                        invoiceItem.setProductBrand((String) row[13]);
                    }
                    invoiceItem.setItemOriginalPrice((BigDecimal) row[14]);
                    if (row[15] != null) {
                        String json = (String) row[15];
                        Map<String, BigDecimal> map;
                        try {
                            map = OBJECT_MAPPER.readValue(
                                    json,
                                    new TypeReference<>() {
                                    }
                            );
                            invoiceItem.getMultiPricesMap().putAll(map);
                        } catch (JsonProcessingException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    if (row[16] != null) {
                        String json = (String) row[16];
                        try {
                            List<DiscountItem> discountItems = OBJECT_MAPPER.readValue(
                                    json,
                                    new TypeReference<List<DiscountItem>>() {
                                    }
                            );
                            invoiceItem.setItemDiscountList(discountItems.toArray(new DiscountItem[0]));
                        } catch (JsonProcessingException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    invoiceItem.setItemType((Integer) row[17]);
                    if (row[18] != null) {
                        invoiceItem.setItemBarcode((String) row[18]);
                    }
                    invoiceItem.setItemsInStockQty((BigDecimal) row[19]);
                    invoiceItem.setUnitCost((BigDecimal) row[20]);
                    invoiceItem.setCurrentProductDiscountAmount((BigDecimal) row[37]);
                } else if (row[21] != null) {
                    invoiceItem.setItemName((String) row[21]);
                    invoiceItem.setFullItemName((String) row[22]);
                }
                if (row[24] != null) {
                    invoiceItem.setMeasurement(new SelectItem((Integer) row[24], (String) row[25], (String) row[26]));
                }
                invoiceItem.setShortLink((String) row[27]);
                invoiceItem.setDescription((String) row[28]);
                invoiceItem.setQuantity((BigDecimal) row[29]);
                invoiceItem.setUuid((String) row[30]);
                invoiceItem.setLumpsum((Boolean) row[31]);
                invoiceItem.setUnitPrice((BigDecimal) row[32]);
                invoiceItem.setPriceLevelAmount((BigDecimal) row[33]);
                invoiceItem.setComission((BigDecimal) row[34]);
                invoiceItem.setDiscountPercent((BigDecimal) row[35]);
                invoiceItem.setDiscountAmount((BigDecimal) row[36]);
                if (row[38] != null) {
                    invoiceItem.setItemDiscountID((Integer) row[38]);
                    invoiceItem.setItemDiscount((String) row[39]);
                }
                invoiceItem.setDiscountItemStaticType((Integer) row[40]);
                invoiceItem.setDoubleDiscountPercent((BigDecimal) row[41]);
                invoiceItem.setDoubleDiscountAmount((BigDecimal) row[42]);
                if (row[43] != null) {
                    invoiceItem.setItemDoubleDiscountID((Integer) row[43]);
                    invoiceItem.setItemDoubleDiscount((String) row[44]);
                }
                invoiceItem.setReceivedAllocation((BigDecimal) row[45]);
                invoiceItem.setNet((BigDecimal) row[46]);
                if (row[47] != null) {
                    invoiceItem.setDepartmentItem(new SelectItem((Integer) row[47], (String) row[48]));
                }
                if (row[49] != null) {
                    invoiceItem.setAccountID((Integer) row[49]);
                    invoiceItem.setAccountName((String) row[51]);

                    AccountItem accountItem = new AccountItem((Integer) row[49],
                            (String) row[50], (String) row[51],
                            (Integer) row[52],
                            (String) row[53],
                            (String) row[54],
                            row[55] != null ? (Integer) row[55] : (Integer) row[56],
                            (row[57] != null ? (Integer) row[57] : null),
                            (row[58] != null ? (String) row[58] : null));

                    accountItem.setParentCode(row[59] != null ? (String) row[59] : null);
                    invoiceItem.setAccountItem(accountItem);
                }
                if (row[1] != null && row[60] != null) {
                    AccountItem salesAccount = new AccountItem((Integer) row[60],
                            (String) row[61], (String) row[62],
                            (Integer) row[63],
                            (String) row[64],
                            (String) row[65],
                            row[66] != null ? (Integer) row[66] : (Integer) row[67],
                            (row[68] != null ? (Integer) row[68] : null),
                            (row[69] != null ? (String) row[69] : null));

                    salesAccount.setParentCode(row[70] != null ? (String) row[70] : null);
                    invoiceItem.setSalesAccount(salesAccount);
                }
                if (row[73] != null) {
                    String json = (String) row[73];
                    TaxItem taxItem;
                    try {
                        taxItem = OBJECT_MAPPER.readValue(
                                json,
                                new TypeReference<TaxItem>() {
                                }
                        );
                        invoiceItem.setTaxItem(taxItem);
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException(e);
                    }

                    boolean forDoubleTax = false;
                    Integer taxCalculationType = (Integer) row[23];
                    BigDecimal qty = row[29] != null ? (BigDecimal) row[29] : null;
                    BigDecimal unitPrice = row[32] != null ? (BigDecimal) row[32] : null;
                    BigDecimal discount = row[35] != null ? (BigDecimal) row[35] : null;
                    BigDecimal discountAmount = row[36] != null ? (BigDecimal) row[36] : null;
                    BigDecimal doubleDiscount = row[41] != null ? (BigDecimal) row[41] : null;
                    BigDecimal doubleDiscountAmount = row[42] != null ? (BigDecimal) row[42] : null;
                    BigDecimal vatEffectiveRat = taxItem != null && taxItem.getEffectiveTaxPercent() != null ? taxItem.getEffectiveTaxPercent() : null;

                    invoiceItem.setTaxAmount(
                            getItemCalculatedTaxAmount(
                                    forDoubleTax,
                                    taxCalculationType,
                                    qty,
                                    unitPrice,
                                    discount,
                                    discountAmount,
                                    doubleDiscount,
                                    doubleDiscountAmount,
                                    null,
                                    vatEffectiveRat
                            )
                    );
                }
                if (row[74] != null) {
                    String json = (String) row[74];
                    TaxItem taxItem;
                    try {
                        taxItem = OBJECT_MAPPER.readValue(
                                json,
                                new TypeReference<TaxItem>() {
                                }
                        );
                        invoiceItem.setDoubleTaxItem(taxItem);
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException(e);
                    }

                    boolean forDoubleTax = true;
                    Integer taxCalculationType = (Integer) row[23];
                    BigDecimal qty = row[29] != null ? (BigDecimal) row[29] : null;
                    BigDecimal unitPrice = row[32] != null ? (BigDecimal) row[32] : null;
                    BigDecimal discount = row[35] != null ? (BigDecimal) row[35] : null;
                    BigDecimal discountAmount = row[36] != null ? (BigDecimal) row[36] : null;
                    BigDecimal doubleDiscount = row[41] != null ? (BigDecimal) row[41] : null;
                    BigDecimal doubleDiscountAmount = row[42] != null ? (BigDecimal) row[42] : null;
                    BigDecimal doubleVatEffectiveRate = taxItem != null && taxItem.getEffectiveTaxPercent() != null ? taxItem.getEffectiveTaxPercent() : null;

                    invoiceItem.setTaxAmount(
                            getItemCalculatedTaxAmount(
                                    forDoubleTax,
                                    taxCalculationType,
                                    qty,
                                    unitPrice,
                                    discount,
                                    discountAmount,
                                    doubleDiscount,
                                    doubleDiscountAmount,
                                    doubleVatEffectiveRate,
                                    null
                            )
                    );
                }
                invoiceItem.setTotalAmount((BigDecimal) row[75]);
                invoiceItem.setReceiveType(row[76] != null ? ReceiveTypeEnum.valueOf((String) row[76]) : null);
                invoiceItem.setReceivedAmount((BigDecimal) row[77]);
                invoiceItem.setReceivedQty((BigDecimal) row[78]);
                if (row[79] != null) {
                    String json = (String) row[79];
                    try {
                        SelectItem warehouse = OBJECT_MAPPER.readValue(
                                json,
                                new TypeReference<SelectItem>() {
                                }
                        );
                        invoiceItem.setWarehouse(warehouse);
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException(e);
                    }
                }
                invoiceItem.setConvertedQty((BigDecimal) row[80]);
                invoiceItem.setConvertedAmount((BigDecimal) row[81]);
                invoiceItem.setReceive((BigDecimal) row[82]);
                if (row[83] != null) {
                    String json = (String) row[83];
                    try {
                        SelectItem project = OBJECT_MAPPER.readValue(
                                json,
                                new TypeReference<SelectItem>() {
                                }
                        );
                        invoiceItem.setProject(project);
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException(e);
                    }
                    if (row[84] != null) {
                        String projectJson = (String) row[84];
                        try {
                            SelectItem parentProject = OBJECT_MAPPER.readValue(
                                    projectJson,
                                    new TypeReference<SelectItem>() {
                                    }
                            );
                            invoiceItem.setParentProject(parentProject);
                        } catch (JsonProcessingException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
                if (row[87] != null && !isQuote) {
                    if (row[85] != null) {
                        invoiceItem.setItemName((String) row[85]);
                        invoiceItem.setMeasurement(new SelectItem(null, "hours"));
                    }
                    if (row[86] != null) {
                        invoiceItem.setExpanceItemId((Integer) row[86]);
                    }
                    invoiceItem.setSaleInvoiceId((Integer) row[87]);
                    invoiceItem.setFromDate(new DateNonConvertable((Date) row[88]));
                    invoiceItem.setToDate(new DateNonConvertable((Date) row[89]));
                    invoiceItem.setFaiCategoryId((Integer) row[90]);
                    invoiceItem.setQuoteItemId((Integer) row[91]);
                    invoiceItem.setFromTimesheet((Boolean) row[92]);
                    if (row[93] != null) {
                        String json = (String) row[93];
                        try {
                            SelectItem client = OBJECT_MAPPER.readValue(
                                    json,
                                    new TypeReference<SelectItem>() {
                                    }
                            );
                            invoiceItem.setClient(client);
                        } catch (JsonProcessingException e) {
                            throw new RuntimeException(e);
                        }
                    }
                } else {
                    invoiceItem.setShippedQty(row[86] != null ? (BigDecimal) row[86] : BigDecimal.ZERO);
                    invoiceItem.setPickable(row[87] != null ? (Boolean) row[87] : false);
                }
                resultMap.put(invoiceItem.getID(), invoiceItem);
            }
        }

        return resultMap;
    }

    private StringBuilder hasInventoryCheck(boolean isQuote) {
        StringBuilder query = new StringBuilder("exists (\n\t\t")
                .append("select 1\n\t\t")
                .append("from ").append(getCompanyId());
        if (isQuote) {
            query.append(".quoteItem ii2\n\t\t");
        } else {
            query.append(".invoiceitem ii2\n\t\t");
        }

        query.append("join ").append(getCompanyId()).append(".item i2 on ii2.item_id = i2.id\n\t\t")
                .append("join ").append(getCompanyId()).append(".product_kit_items pki on pki.product_kit_id = i2.id\n\t\t")
                .append("join ").append(getCompanyId()).append(".item kit_item on pki.product_id = kit_item.id\n\t\t");
        if (isQuote) {
            query.append("where ii2.quote_id = invi.quote_id ");
        } else {
            query.append("where ii2.invoice_id = invi.invoice_id ");
        }
        query.append("and kit_item.type in (").append(INVENTORY_ITEM).append(", ").append(ASSEMBLY_ITEM).append(")\n\t")
                .append(") as has_inventory_in_product_kit\n\t");

        return query;
    }

    private BigDecimal getItemCalculatedTaxAmount(
            boolean forDoubleTax,
            Integer taxCalculationType,
            BigDecimal qty,
            BigDecimal unitPrice,
            BigDecimal discount,
            BigDecimal discountAmount,
            BigDecimal doubleDiscount,
            BigDecimal doubleDiscountAmount,
            BigDecimal doubleVatEffectiveRate,
            BigDecimal vatEffectiveRate
    ) {
        BigDecimal netAmount = qty.multiply(unitPrice);
        BigDecimal itemDiscount;
        if (discount != null) {
            itemDiscount = netAmount.multiply(discount).divide(AccountingConstants.HUNDRED, ServerUtils.getSystemCalculationScale(), RoundingMode.HALF_UP);
        } else {
            itemDiscount = discountAmount != null ? discountAmount : AccountingConstants.ZERO;
        }

        if (doubleDiscount != null) {
            itemDiscount = itemDiscount.add(netAmount.multiply(doubleDiscount).divide(AccountingConstants.HUNDRED, ServerUtils.getSystemCalculationScale(), RoundingMode.HALF_UP));
        } else {
            itemDiscount = itemDiscount.add(doubleDiscountAmount != null ? doubleDiscountAmount : AccountingConstants.ZERO);
        }

        BigDecimal discountedTotal = netAmount.subtract(itemDiscount);
        BigDecimal taxPercent = AccountingConstants.ZERO;
        if (forDoubleTax) {
            if (doubleVatEffectiveRate != null) {
                taxPercent = doubleVatEffectiveRate;
            }
        } else {
            if (vatEffectiveRate != null) {
                taxPercent = vatEffectiveRate;
            }
        }

        if (AccountingConstants.TAX_CALCULATION_INCLUSIVE.equals(taxCalculationType)) {
            return discountedTotal.multiply(taxPercent).divide(AccountingConstants.HUNDRED.add(taxPercent), ServerUtils.getSystemCalculationScale(), RoundingMode.HALF_UP).setScale(ServerUtils.getSystemCalculationScale(), RoundingMode.HALF_UP);
        } else {
            return discountedTotal.multiply(taxPercent).divide(AccountingConstants.HUNDRED, ServerUtils.getSystemCalculationScale(), RoundingMode.HALF_UP).setScale(ServerUtils.getSystemCalculationScale(), RoundingMode.HALF_UP);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public NewInvoice getInvoiceProductSubItemsByTypes(Integer invoiceId) {
        NewInvoice result = new NewInvoice();

        String sql = """
                 with fs_settings as (
                    select coalesce(
                        (select fs.enable_multi_warehouse from %1$s.financialsettings fs limit 1),
                        false
                    ) as multi_wh
                ),
                default_wh as (
                    select w.id as default_wh_id
                    from %1$s.warehouse w
                    where w.isdefaultWarehouse = true
                    order by w.id desc
                    limit 1
                ),
                stock_agg as (
                    select
                        s.item_id,
                        coalesce(sum(
                            case
                                when s.transaction_code = 'OUT' then (0 - s.quantity)
                                else s.quantity
                            end
                        ), 0) as qty
                    from %1$s.assemblyItem ai
                    join %1$s.item parent_i    on ai.item_id     = parent_i.id
                    join %1$s.invoiceitem invi on invi.item_id   = parent_i.id
                    join %1$s.item_stock s     on s.item_id      = ai.product_id
                    join %1$s.transaction t    on s.transactionid = t.id
                    cross join fs_settings
                    cross join default_wh
                    where invi.invoice_id = ?
                      and parent_i.type   = ?
                      and t.deleted is not true
                      and (
                          fs_settings.multi_wh = true
                          or s.warehouseid = default_wh.default_wh_id
                      )
                    group by s.item_id
                )
                
                select
                    CAST((
                        select jsonb_agg(
                            jsonb_build_object(
                                'assemblyItemId',  ai.id,
                                'description',     ai.description,
                                'quantity',        ai.qty,
                                'costPrice',       ai.costPrice,
                                'productType',     ai.type,
                                'product', case when pi.id is not null then
                                    jsonb_build_object(
                                        'id',          pi.id,
                                        'name',        pi.product_number || ' -> ' || pi.name,
                                        'description', pi.description
                                    )
                                end,
                                'active',       case when pi.id is not null then coalesce(pi.isactive, false) end,
                                'itemsInStock', case when pi.id is not null then coalesce(stock.qty, 0) end
                            )
                            order by ai.id
                        )
                        from %1$s.invoiceitem invi
                        join %1$s.item i          on invi.item_id  = i.id
                        join %1$s.assemblyItem ai on ai.item_id    = i.id
                        left join %1$s.item pi    on ai.product_id = pi.id
                        left join stock_agg stock    on stock.item_id = pi.id
                        where invi.invoice_id = ?
                          and i.type = ?
                    ) AS text) as assembly_items,
                
                    CAST((
                        select jsonb_agg(
                            jsonb_build_object(
                                'productItem', jsonb_build_object(
                                    'id',                      i.id,
                                    'name',                    i.product_number || ' -> ' || i.name,
                                    'description',             i.description,
                                    'productType',             i.type,
                                    'isPurchasedFromSupplier', coalesce(i.purchased_from_supplier, false),
                                    'active',                  coalesce(i.isactive, false)
                                ),
                                'quantity', pki.quantity,
                                'price',    CAST(round(coalesce(i.sellingprice, 0), 2) AS text),
                                'cost',     CAST(round(coalesce(i.unitPrice, 0), 2) AS text),
                                'tax', CAST(round(coalesce((
                                    select
                                        case when v.groupTax = true then (
                                            select round(sum(
                                                case
                                                    when sum_tc.compound_sum > 0
                                                    then (sum_tc.non_compound_sum / 100.0 + 1)
                                                         * sum_tc.compound_sum
                                                         + sum_tc.non_compound_sum
                                                    else sum_tc.non_compound_sum
                                                end
                                            ), 2)
                                            from %1$s.taxgroupitem tgi
                                            join %1$s.vat child_v on tgi.itemid = child_v.id
                                            join lateral (
                                                select
                                                    sum(case when tc2.compound = false then tc2.rate else 0 end) as non_compound_sum,
                                                    sum(case when tc2.compound = true  then tc2.rate else 0 end) as compound_sum
                                                from %1$s.taxcomponent tc2
                                                where tc2.taxid = child_v.id
                                            ) sum_tc on true
                                            where tgi.taxid = v.id
                                              and child_v.groupTax = false
                                        )
                                        else (
                                            select
                                                case
                                                    when sum(case when tc.compound = true then tc.rate else 0 end) > 0
                                                    then round(
                                                        (sum(case when tc.compound = false then tc.rate else 0 end) / 100.0 + 1)
                                                        * sum(case when tc.compound = true  then tc.rate else 0 end)
                                                        + sum(case when tc.compound = false then tc.rate else 0 end),
                                                        2)
                                                    else round(
                                                        sum(case when tc.compound = false then tc.rate else 0 end),
                                                        2)
                                                end
                                            from %1$s.taxcomponent tc
                                            where tc.taxid = v.id
                                        )
                                        end
                                    from %1$s.vat v
                                    where v.id = i.vatid
                                ), 0), 2) AS text),
                                'subtotal', CAST(round(
                                    coalesce(i.sellingprice, 0) * pki.quantity,
                                    2
                                ) AS text)
                            )
                            order by pki.id
                        )
                        from %1$s.invoiceitem invi
                        join %1$s.item i                on invi.item_id       = i.id
                        join %1$s.product_kit_items pki on pki.product_kit_id = i.id
                        where invi.invoice_id = ?
                          and i.type = ?
                    ) AS text) as product_kit_items
                
                from (values(1)) as dual
                """.formatted(getCompanyId());

        Query query = slaveEntityManager.createNativeQuery(sql)
                .setParameter(1, invoiceId)
                .setParameter(2, EdsItem.ASSEMBLY_ITEM)
                .setParameter(3, invoiceId)
                .setParameter(4, EdsItem.ASSEMBLY_ITEM)
                .setParameter(5, invoiceId)
                .setParameter(6, EdsItem.PRODUCT_KIT);

        @SuppressWarnings("unchecked")
        List<Object[]> rows = query.getResultList();

        if (!rows.isEmpty()) {
            final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

            for (Object[] row : rows) {
                if (row[0] != null) {
                    String json = (String) row[0];
                    try {
                        ArrayList<AssemblyItem> assemblyItems = OBJECT_MAPPER.readValue(
                                json,
                                new TypeReference<>() {
                                }
                        );
                        result.setAssemblyItems(assemblyItems);
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException(e);
                    }
                }
                if (row[1] != null) {
                    String json = (String) row[1];
                    try {
                        ArrayList<ProductKitItem> productKitItems = OBJECT_MAPPER.readValue(
                                json,
                                new TypeReference<>() {
                                }
                        );
                        result.setProductKitItems(productKitItems);
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }

        return result;
    }

    @Override
    public Map<Integer, String> getOrderNumbers(Set<Integer> quoteItemIds) {
        Map params = new HashMap();
        params.put("ids", quoteItemIds);

        StringBuilder sql = new StringBuilder("select qi.id, q.number from ");
        sql.append(getCompanyId()).append(".quote q ")
                .append(" join ").append(getCompanyId()).append(".quoteItem qi on q.id = qi.quote_id ")
                .append(" where qi.id in (:ids)");

        List<Object[]> list = findNativeByNamedParams(sql.toString(), params);

        Map<Integer, String> result = new HashMap<>();
        list.forEach(o -> {
            result.put((Integer) o[0], (String) o[1]);
        });

        return result;
    }

    /**
     * Batch version of getConvertedQuoteItemQuantity.
     * Key: itemId
     * Note: original uses quoteItemId as a tiebreaker filter when present —
     * in the batch version we pass the full quoteItemIds set as an
     * additional IN filter, preserving the same intent.
     */
    @Override
    public Map<Integer, BigDecimal> getConvertedQuoteItemQuantities(
            Set<Integer> itemIds,
            Integer quoteId,
            Date creationDate,
            Set<Integer> quoteItemIds) {

        if (itemIds == null || itemIds.isEmpty() || quoteId == null) {
            return Collections.emptyMap();
        }

        Date effectiveDate = creationDate != null ? creationDate : new Date();

        StringBuilder sql = new StringBuilder();
        sql.append("select qi.item_id, qi.qty ");
        sql.append(" from ").append(getCompanyId()).append(".invoice i ");
        sql.append(" left join ").append(getCompanyId()).append(".converted_items ci on i.id = ci.invoice_id ");
        sql.append(" left join ").append(getCompanyId()).append(".quoteitem qi on qi.quote_id = ci.quote_id ");
        sql.append(" left join ").append(getCompanyId()).append(".salequote sq on qi.quote_id = sq.id ");
        sql.append(" where qi.item_id in (")
                .append(itemIds.stream().map(String::valueOf).collect(Collectors.joining(","))).append(") ");
        sql.append(" and i.creationDate = '").append(effectiveDate).append("'");
        sql.append(" and ci.quote_id = ").append(quoteId);
        if (quoteItemIds != null && !quoteItemIds.isEmpty()) {
            sql.append(" and qi.id in (")
                    .append(quoteItemIds.stream().map(String::valueOf).collect(Collectors.joining(","))).append(") ");
        }
        sql.append(" and sq.progressInvoicing = true ");

        List<Object[]> rows = findNative(sql.toString());
        Map<Integer, BigDecimal> result = new HashMap<>();

        if (rows != null) {
            for (Object[] row : rows) {
                Integer itemId = (Integer) row[0];
                BigDecimal qty = (BigDecimal) row[1];
                result.put(itemId, qty);
            }
        }
        return result;
    }

    /**
     * Batch version of getPreviousConvertedSaleInvoiceItemsQuantity.
     * Key: itemId
     */
    @Override
    public Map<Integer, BigDecimal> getPreviousConvertedSaleInvoiceItemsQuantities(
            Set<Integer> itemIds,
            Integer quoteId,
            Date creationDate) {

        if (itemIds == null || itemIds.isEmpty() || quoteId == null) {
            return Collections.emptyMap();
        }

        Date effectiveDate = creationDate != null ? creationDate : new Date();

        StringBuilder sql = new StringBuilder();
        sql.append("select ii.item_id, sum(ii.qty) ");
        sql.append(" from ").append(getCompanyId()).append(".invoice i ");
        sql.append(" left join ").append(getCompanyId()).append(".converted_items ci on i.id = ci.invoice_id ");
        sql.append(" left join ").append(getCompanyId()).append(".invoiceitem ii on i.id = ii.invoice_id ");
        sql.append(" left join ").append(getCompanyId()).append(".salequote sq on ci.quote_id = sq.id ");
        sql.append(" where ii.item_id in (")
                .append(itemIds.stream().map(String::valueOf).collect(Collectors.joining(","))).append(") ");
        sql.append(" and i.creationDate < '").append(effectiveDate).append("'");
        sql.append(" and ci.quote_id = ").append(quoteId);
        sql.append(" and sq.progressInvoicing = true ");
        sql.append(" and i.deleted = false ");
        sql.append(" group by ii.item_id ");

        List<Object[]> rows = findNative(sql.toString());
        Map<Integer, BigDecimal> result = new HashMap<>();

        if (rows != null) {
            for (Object[] row : rows) {
                Integer itemId = (Integer) row[0];
                BigDecimal qty = (BigDecimal) row[1];
                result.put(itemId, qty);
            }
        }
        return result;
    }
}
