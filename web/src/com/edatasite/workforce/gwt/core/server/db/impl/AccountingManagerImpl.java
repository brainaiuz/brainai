package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.*;
import com.edatasite.workforce.core.domain.accounting.*;
import com.edatasite.workforce.core.domain.crm.EdsCrmAccount;
import com.edatasite.workforce.core.solr.component.ChartOfAccountSolrComponent;
import com.edatasite.workforce.gwt.accounting.client.rpc.*;
import com.edatasite.workforce.gwt.accounting.client.ui.AccountingConstants;
import com.edatasite.workforce.gwt.core.client.enums.BankAccountTypeEnum;
import com.edatasite.workforce.gwt.core.client.enums.GenericSettingsEnum;
import com.edatasite.workforce.gwt.core.client.rpc.CurrencyServiceLocal;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.FromToDate;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.currency.CurrencyListItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.rpc.solr.SolrReindexRpc;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.db.*;
import com.edatasite.workforce.gwt.core.server.db.rbac.SolrManager;
import com.edatasite.workforce.gwt.core.server.db.settings.GenericSettingsManager;
import com.edatasite.workforce.gwt.invoice.client.rpc.AgingSummaryInvoiceItem;
import com.edatasite.workforce.gwt.invoice.client.rpc.ProjectBaseData;
import com.google.gwt.user.server.rpc.security.ServerSecurityContext;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.StrBuilder;
import org.apache.solr.client.solrj.SolrServerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.support.WfmMessageSource;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: Anvarbek
 * Date: 20.02.2009
 * Time: 17:11:39
 * To change this template use File | Settings | File Templates.
 */

@Repository("accountingManager")
public class AccountingManagerImpl extends BaseManager<EdsAccount> implements AccountingManager, Constants, AccountingConstants {

    @Autowired
    @Qualifier("accountingLocalizer")
    private WfmMessageSource accountingLocalizer;
    @Autowired
    private JdbcSpringManager jdbcSpringManager;
    @Autowired
    private AccountTypeSettingManager accountTypeSettingManager;
    @Autowired
    private UserManager userManager;
    @Autowired
    private GenericSettingsManager genericSettingsManager;
    @Autowired
    private BankAccountManager bankAccountManager;
    @Autowired
    private FinancialSettingsManager financialSettingsManager;
    @Autowired
    private SolrManager solrManager;
    @Autowired
    private ChartOfAccountSolrComponent chartOfAccountSolrComponent;
    @Autowired
    private AccountingManager accountingManager;
    @Autowired
    private CurrencyServiceLocal currencyServiceLocal;

    public AccountingManagerImpl() {
        super(EdsAccount.class);
    }

    public EdsAccount getAccountByKey(int key) {
        return (EdsAccount) findSingle("from EdsAccount ac where (ac.deleted is null or ac.deleted<>true) and active is true and ac.key=? and baseAccount is null", key);
    }

    @Override
    public HashMap<Integer, EdsAccount> getAccountsMapByKey(int key) {
        List<EdsAccount> accounts = find("from EdsAccount ac where (ac.deleted is null or ac.deleted<>true) and ac.key=?", key);
        HashMap<Integer, EdsAccount> accountsMap = new HashMap<>();
        for (EdsAccount acc : accounts) {
            accountsMap.put(acc.getObjectID(), acc);
        }
        return accountsMap;
    }

    @Override
    public EdsAccount getAccountByKey(int key, Integer currencyID) {
        return (EdsAccount) findSingle("from EdsAccount ac where (ac.deleted is null or ac.deleted<>true) and active is true and ac.key=? and ac.currency.objectID = ?", key, currencyID);
    }

    public EdsAccount getAccountByCode(String code) {
        return (EdsAccount) findSingle("from EdsAccount ac where (ac.deleted is null or ac.deleted<>true) and active is true and ac.accountCode=?", code);
    }

    @Override
    public List<EdsAccount> getAccountByCodes(List<String> codes) {
        return find("from EdsAccount ac where ac.accountCode in ('" + ServerUtils.getAsCommoDelimited(codes, "0", "','")
                + "') and (ac.deleted is false or ac.deleted is null) ");
    }

    public HashMap<String, EdsAccount> getAccountsMap() {
        String sql = "SELECT ac.*, 0 as clazz_ from " + getCompanyId() + ".account ac " +
                "WHERE " + ServerUtils.checkForDeleted("ac.deleted");
        List<EdsAccount> accountList = (List<EdsAccount>) findNative(sql, EdsAccount.class);
        HashMap<String, EdsAccount> map = new HashMap<>();
        for (EdsAccount account : accountList) {
            map.put(account.getCodeString().toLowerCase().trim(), account);
        }
        return map;

    }

    public HashMap<String, EdsAccount> getAccountPayableOrReceivableMap(boolean isPayableAccount) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT ac.*, 0 as clazz_ from ").append(getCompanyId()).append(".account ac ");
        sql.append("WHERE ").append(ServerUtils.checkForDeleted("ac.deleted"));
        if (isPayableAccount) {
            sql.append(" AND (ac.key=").append(EdsAccount.ACCOUNTS_PAYABLE).append(" or ").append(" ac.groupkey=").append(EdsAccount.ACCOUNTS_PAYABLE).append(")");
        } else {
            sql.append(" AND (ac.key=").append(EdsAccount.ACCOUNTS_RECEIVABLE).append(" or ").append(" ac.groupkey=").append(EdsAccount.ACCOUNTS_RECEIVABLE).append(")");
        }
        List<EdsAccount> accountList = (List<EdsAccount>) findNative(sql.toString(), EdsAccount.class);
        HashMap<String, EdsAccount> map = new HashMap<>();
        for (EdsAccount account : accountList) {
            map.put(account.getCodeString().toLowerCase().trim(), account);
        }
        return map;

    }

    public List<EdsAccount> getCompanyAccounts() {
        return find("from EdsAccount ac where (ac.deleted is null or ac.deleted<>true)");
    }

    public List<EdsAccount> getCompanyAccountsOrderByType() {
        return find("from EdsAccount ac where (ac.deleted is null or ac.deleted<>true) order by ac.accountType.order, ac.name");
    }


    public List<EdsAccountType> getAccountTypes() {
        return find("from EdsAccountType ac");
    }

    public List<EdsAccount> getAccountsForPayment(ListingFilterParameter fp) {
        if (fp == null) {
            fp = new ListingFilterParameter();
        }
        StringBuilder sql = new StringBuilder();
        sql.append("select ac.*, 0 as clazz_ from ").append(getCompanyId()).append(".account ac");
        sql.append(" left join ").append(getCompanyId()).append(".bankAccount ba on ac.id = ba.accountid");
        if (!getUser().hasEitherRoles(EdsRole.ADMIN)) {
            sql.append(" left join ").append(getCompanyId()).append(".bankAccount_owners bao on bao.bankAccount_id = ba.id");
        }
        if (fp.isOverpayment()) {
            sql.append(" left join accountType act on ac.accountTypeId= act.id ");
            sql.append(" where (ac.enablePayments=true or ").append("act.category in ('").append(EdsAccountType.EXPENSES).append("', '").append(EdsAccountType.REVENUE).append("'))");
        } else {
            sql.append(" where ac.enablePayments=true ");
        }
        sql.append(" and ").append(ServerUtils.checkForDeleted("ac.deleted"));
        sql.append(" and ac.active is true ");
        if (genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.MULTICURRENCY_ENABLED) && !fp.isIgnoreAllCurrencyValidation()) {
            List<Integer> currencyIDs = new LinkedList<>();
            EdsFinancialSettings fs = financialSettingsManager.getFinancialSettings();
            currencyIDs.add(fs.getCurrency().getObjectID());
            if (fp != null && fp.getCurrencyID() != null) {
                currencyIDs.add(fp.getCurrencyID());
            }
            sql.append(" and ac.currencyid in (").append(ServerUtils.getAsCommoDelimited(currencyIDs, "0")).append(")");
        }
        if (fp != null && fp.isLookUp()) {
            sql.append(" and (ba.id is null or (ba.active = true  ");

            if (!getUser().hasEitherRoles(EdsRole.ADMIN)) {
                sql.append("and (bao.owner_id is null or bao.owner_id = ").append(getUser().getObjectID()).append(" )");
            }
            sql.append(" )) ");

            if (fp.getSearchKey() != null) {
                sql.append(" and (lower(ac.name) like lower('%").append(fp.getSearchKey()).append("%') ");
                sql.append(" or lower(ac.accountCode) like lower('%").append(fp.getSearchKey()).append("%')) ");
            }
        }
        return findNative(sql.toString(), EdsAccount.class);
    }

    public List<EdsAccount> getAccountsForInvoice() {
        return find("from EdsAccount ac where " + ServerUtils.checkForDeleted("ac.deleted") + " and ((ac.key is null and ac.groupKey is null) or ac.showInExpense is true or ((ac.key is not null or ac.groupKey is not null) and ac.accountType.code = ?))", EdsAccountType.BANK);
    }

    @Override
    public List<EdsAccount> getAccountsForInvoice(ListingFilterParameter filterParametrs, boolean searchInGivenTypes) {
        if (filterParametrs == null) {
            filterParametrs = new ListingFilterParameter();
        }

        StringBuilder sql = new StringBuilder();
        sql.append("SELECT ac FROM EdsAccount ac ");
        sql.append("WHERE ").append(ServerUtils.checkForDeleted("ac.deleted "));
        sql.append("AND ac.active is true ");
        if (StringUtils.isNotBlank(filterParametrs.getSystemAccountCodes())) {
            if (filterParametrs.isSystem()) {
                sql.append(" AND ((ac.key is null and ac.groupKey is null) or ac.showInExpense is true or ((ac.key is not null or ac.groupKey is not null) and (ac.key IN (").append(filterParametrs.getSystemAccountCodes()).append(")))) ");
            } else {
                sql.append(" AND ((ac.key is null and ac.groupKey is null) or ac.showInExpense is true or ((ac.key is not null or ac.groupKey is not null) and (ac.accountType.code='" + EdsAccountType.BANK + "' or ac.key IN (").append(filterParametrs.getSystemAccountCodes()).append(")))) ");
            }
        } else {
            if (filterParametrs.isSystem()) {
                sql.append(" AND ((ac.key is null and ac.groupKey is null) or ac.showInExpense is true or (ac.key is not null or ac.groupKey is not null)) ");
            } else if (filterParametrs.isFromProduct()) {
                sql.append(" AND ((ac.key is null and ac.groupKey is null) or ac.showInExpense is true or (ac.key is not null or ac.groupKey is not null))");
                sql.append(" AND (ac.accountType.code != '" + EdsAccountType.BANK + "' ");
                sql.append(" AND ac.accountType.category IN ('").append(ServerUtils.getAsCommoDelimited(filterParametrs.getAccountTypes(), "0", "','")).append("'))");
            } else {
                sql.append(" AND ((ac.key is null and ac.groupKey is null) or ac.showInExpense is true or ((ac.key is not null or ac.groupKey is not null) and ac.accountType.code='" + EdsAccountType.BANK + "')) ");
            }
        }

        if (filterParametrs.isStockAdjustment() != null && filterParametrs.isStockAdjustment()) {
            sql.append(" OR ( ac.accountType.code='" + EdsAccountType.EQUITY + "')");
        }

        if (searchInGivenTypes) {
            ArrayList<String> accountcodes = new ArrayList<>();
            if (filterParametrs.getAccountCode() != null) {
                accountcodes = new ArrayList<>(Collections.singletonList(filterParametrs.getAccountCode()));
            }
            if (filterParametrs.getAccountTypes() != null && !filterParametrs.getAccountTypes().isEmpty() && !filterParametrs.isFromProduct()) {
                sql.append(" AND ac.accountType.category IN ('").append(ServerUtils.getAsCommoDelimited(filterParametrs.getAccountTypes(), "0", "','")).append("')");
                if (filterParametrs.getAccountTypes().contains(NON_CURRENT_ASSET )|| filterParametrs.getAccountTypes().contains(AccountingConstants.CURRENT_ASSET)) {
                    accountcodes.add(NON_CURRENT_ASSET);
                    accountcodes.add(AccountingConstants.CURRENT_ASSET);
                }
            }
            if (!accountcodes.isEmpty()) {
                sql.append(" AND ac.accountType.code  in ('").append(ServerUtils.getAsCommoDelimited(accountcodes, "0", "','")).append("') ");
            }
        }

        if (StringUtils.isNotBlank(filterParametrs.getSqlSearchKey()) && filterParametrs.isLookUp()) {
            sql.append("  AND ( lower(ac.name) like '").append(filterParametrs.getSearchKey().toLowerCase()).append("%' ");
            sql.append("  OR  lower(ac.codeString) like '").append(filterParametrs.getSearchKey().toLowerCase()).append("%') ");
        }

        if (filterParametrs.isValidateChildAccounts() && filterParametrs.getObjectId() != null) {
            EdsAccount parentAccount = get(filterParametrs.getObjectId());
            if (parentAccount != null) {
                List<Integer> childIds = parentAccount.getChildIDs(new LinkedList<>());
                sql.append(" AND ac.objectID NOT IN (").append(ServerUtils.getAsCommoDelimited(childIds, "0")).append(")");
            }
        }
        sql.append(" ORDER BY ac.showInExpense desc, ac.accountCode");
        return find(sql.toString());
    }

    @Override
    public List<EdsAccount> getAccountsForPayableType(ListingFilterParameter filterParametrs) {
        if (filterParametrs == null) {
            filterParametrs = new ListingFilterParameter();
        }

        StringBuilder sql = new StringBuilder();
        sql.append("SELECT ac FROM EdsAccount ac ");
        sql.append("WHERE ").append(ServerUtils.checkForDeleted("ac.deleted "));
        sql.append("AND ac.active is true ");
        if (StringUtils.isNotBlank(filterParametrs.getSystemAccountCodes())) {
            if (filterParametrs.isSystem()) {
                sql.append(" AND ((ac.key is null and ac.groupKey is null) or ac.showInExpense is true or ((ac.key is not null or ac.groupKey is not null) and (ac.key IN (").append(filterParametrs.getSystemAccountCodes()).append(")))) ");
            } else {
                sql.append(" AND ((ac.key is null and ac.groupKey is null) or ac.showInExpense is true or ((ac.key is not null or ac.groupKey is not null) and (ac.accountType.code='" + EdsAccountType.BANK + "' or ac.key IN (").append(filterParametrs.getSystemAccountCodes()).append(")))) ");
            }
        } else {
            if (filterParametrs.isSystem()) {
                sql.append(" AND ((ac.key is null and ac.groupKey is null) or ac.showInExpense is true or (ac.key is not null or ac.groupKey is not null)) ");
            } else {
                sql.append(" AND ((ac.key is null and ac.groupKey is null) or ac.showInExpense is true or (ac.key is not null or ac.groupKey is not null))");
                sql.append(" AND (ac.accountType.code != '" + EdsAccountType.BANK + "' ");
                sql.append(" AND ac.accountType.category IN ('").append(ServerUtils.getAsCommoDelimited(filterParametrs.getAccountTypes(), "0", "','")).append("'))");
            }
        }

        if (StringUtils.isNotBlank(filterParametrs.getSqlSearchKey()) && filterParametrs.isLookUp()) {
            sql.append("  AND ( lower(ac.name) like '").append(filterParametrs.getSearchKey().toLowerCase()).append("%' ");
            sql.append("  OR  lower(ac.codeString) like '").append(filterParametrs.getSearchKey().toLowerCase()).append("%') ");
        }

        if (filterParametrs.isValidateChildAccounts() && filterParametrs.getObjectId() != null) {
            EdsAccount parentAccount = get(filterParametrs.getObjectId());
            if (parentAccount != null) {
                List<Integer> childIds = parentAccount.getChildIDs(new LinkedList<>());
                sql.append(" AND ac.objectID NOT IN (").append(ServerUtils.getAsCommoDelimited(childIds, "0")).append(")");
            }
        }
        sql.append(" ORDER BY ac.showInExpense desc, ac.accountCode");
        return find(sql.toString());
    }

    public List<EdsAccount> getAllGLAccounts() {
        return find("from EdsAccount ac where ac.deleted<>true ");
    }

    //todo we need to fetch data from solr when accounts are moved to solr
    public List<EdsAccount> getAccountsForExpense(ListingFilterParameter fp) {
        StringBuilder sql = new StringBuilder();
        if (StringUtils.isNotBlank(fp.getSearchKey())) {
            sql.append("select (case when lower(ac.name) like lower('").append(fp.getSearchKey()).append("')").append(" or lower(ac.accountCode) like lower('").append(fp.getSearchKey()).append("') then 1");
            sql.append(" when lower(ac.name) like lower('").append(fp.getSearchKey()).append("%')").append(" or lower(ac.accountCode) like lower('").append(fp.getSearchKey()).append("%') then 2");
            sql.append(" else 3 end) rank,ac.*,0 as clazz_");
            sql.append(getAccountsForExpenseWhereSql(fp));
            sql.append(" order by rank ");
        } else {
            sql.append(" select ac.*,0 as clazz_");
            sql.append(getAccountsForExpenseWhereSql(fp));
        }
        if (fp.getLimit() > 0) {
            sql.append(" offset ").append(fp.getStart()).append(" limit ").append(fp.getLimit());
        }

        return findNative(sql.toString(), EdsAccount.class);
    }

    private String getAccountsForExpenseWhereSql(ListingFilterParameter fp) {
        StringBuilder sql = new StringBuilder();
        sql.append(" from ").append(getCompanyId()).append(".account ac");
        sql.append(" left join ").append(getPublic()).append(".accounttype act on ac.accountTypeId = act.id");
        sql.append(" where (ac.deleted <> true or ac.deleted is null)");
        if (fp.getAccountType() != null && BANK_CHECK.equals(fp.getAccountType())) {
            sql.append(" and (act.category = '" + EdsAccountType.EXPENSES +
                    "' or act.code = '" + EdsAccountType.CURRENT_ASSET +
                    "' or act.code = '" + EdsAccountType.PREPAYMENT +
                    "' or act.code = '" + EdsAccountType.BANK +
                    "' or ac.key = '" + EdsAccount.ACCOUNTS_PAYABLE +
                    "' or act.code = '" + EdsAccountType.LIABILITY +
                    "' or act.code = '" + EdsAccountType.EQUITY +
                    "' or act.code = '" + EdsAccountType.CURRENT_LIABILITY +
                    "' or act.code = '" + EdsAccountType.LONG_TERM_LIABILITY + "')");

            if (fp.getCurrencyID() != null) {
                List<Integer> currencyIDs = new ArrayList<>();
                currencyIDs.add(fp.getCurrencyID());
                EdsFinancialSettings fs = financialSettingsManager.getFinancialSettings();
                currencyIDs.add(fs.getCurrency().getObjectID());
                sql.append(" and ac.currencyid in (").append(ServerUtils.getAsCommoDelimited(currencyIDs, "0")).append(")");
            }
        } else {
            sql.append(" and ac.showInExpense is true ");
        }
        if (StringUtils.isNotBlank(fp.getSearchKey())) {
            sql.append(" and (");
            sql.append(" lower(ac.name) like lower('").append(fp.getSearchKey()).append("')").append(" or lower(ac.accountCode) like lower('").append(fp.getSearchKey()).append("')");
            sql.append(" or lower(ac.name) like lower('").append(fp.getSearchKey()).append("%')").append(" or lower(ac.accountCode) like lower('").append(fp.getSearchKey()).append("%')");
            sql.append(" or lower(ac.name) like lower('%").append(fp.getSearchKey()).append("%')").append(" or lower(ac.accountCode) like lower('%").append(fp.getSearchKey()).append("%')");
            sql.append(")");
        }

        return sql.toString();
    }

    public Integer getAccountsForExpenseTotalCount(ListingFilterParameter fp) {
        String sql = "select count(ac.id) " +
                getAccountsForExpenseWhereSql(fp);
        return Integer.valueOf(findNativeSingle(sql).toString());
    }

    public List<EdsAccount> getAccountsAttendedInTransactions(Date to, String departmentAndTreeChildIDs, Integer projectID, Integer showAccounts, boolean foreignOnly) {
        if (showAccounts != null && showAccounts.equals(ALL_ACCOUNTS)) { // 3 means, All Accounts
            return find("select ac from EdsAccount ac join ac.accountType at where (ac.deleted is null or ac.deleted <> true) " + (foreignOnly ? " and ac.foreignAccount is true" : ""));
        }
        StringBuilder query = new StringBuilder();
        query.append("select account.*, faccount.*, (case when faccount.id is not null then 1 else 0 end) as clazz_ from ").append(getCompanyId()).append(".account account ");
        query.append("join ").append(getPublic()).append(".accounttype at on  account.accountTypeId=at.id ");
        query.append("join ").append(getCompanyId()).append(".transactionItem it on account.id=it.accountid ");
        query.append("join ").append(getCompanyId()).append(".transaction tr on it.transactionid=tr.id ");

        query.append("left outer join ").append(getCompanyId()).append(".floatingAccount faccount on faccount.id = account.id ");

        query.append("left join ").append(getCompanyId()).append(".invoice i on tr.invoiceid=i.id ");
        query.append("left join ").append(getCompanyId()).append(".manualjournal mj on tr.manualjournalid=mj.id ");
        query.append("left join ").append(getCompanyId()).append(".expensereport expr on tr.expensereportid=expr.id ");
        query.append("left join ").append(getCompanyId()).append(".expensePayments expay on tr.expensePaymentId=expay.id ");
        query.append("left join ").append(getCompanyId()).append(".expensereport payexp on expay.expenseReportId=payexp.id ");
        query.append("left join ").append(getCompanyId()).append(".quote quo on tr.purchaseorder_id=quo.id ");
        query.append("left join ").append(getCompanyId()).append(".spendreceivemoney spr on tr.banktransferid=spr.id ");
        query.append("left join ").append(getCompanyId()).append(".adjustment_item adjit on it.stock_adjustment_item_id=adjit.id ");

        query.append("left join ").append(getCompanyId()).append(".quoteitem poitem on tr.purchaseorder_id is not null and it.itemid=poitem.id \n");
        query.append("left join ").append(getCompanyId()).append(".quoteitem soitem on tr.saleorder_id is not null and it.itemid=soitem.id \n");
        query.append("left join ").append(getCompanyId()).append(".invoiceitem invitem on tr.invoiceid is not null and it.itemid=invitem.id \n");
        query.append("left join ").append(getCompanyId()).append(".shipping_data_items shi on tr.shippingDataId is not null and it.itemid = shi.id \n");
        query.append("left join ").append(getCompanyId()).append(".quoteitem shitem on shi.quoteItemId = shitem.id \n");
        query.append("left join ").append(getCompanyId()).append(".adjustment_item adjitem on tr.stockTransferId is not null and it.itemid=adjitem.id \n");
        query.append("left join ").append(getCompanyId()).append(".payslipTableItem pti on tr.payrun_id is not null and tr.payrun_id=pti.id ");
        query.append("left join ").append(getCompanyId()).append(".payslipTable pt on pti.payslipTable_id is not null and pti.payslipTable_id=pt.id ");
        if (genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.PROJECT_IN_LINE_ITEM_ENABLE)) {
            query.append("left join ").append(getCompanyId()).append(".project prj on coalesce(coalesce(coalesce(coalesce(coalesce(coalesce(coalesce(coalesce(coalesce(coalesce(it.project_id,expr.projectId),payexp.projectId),spr.projectid),adjit.projectid),adjitem.projectid),mj.projectid),poitem.project_id),soitem.project_id),invitem.project_id),shitem.project_id) = prj.id \n");
        } else {
            query.append("left join ").append(getCompanyId()).append(".project prj on coalesce(coalesce(coalesce(coalesce(coalesce(coalesce(coalesce(coalesce(it.project_id,expr.projectId),payexp.projectId),spr.projectid),adjit.projectid),adjitem.projectid),mj.projectid),i.relatedproject_id),quo.relatedproject_id) = prj.id \n");
        }

        query.append("where ").append(ServerUtils.checkForDeleted(" account.deleted ")).append(foreignOnly ? " and account.foreignAccount is true " : " ");

        if (departmentAndTreeChildIDs != null && !departmentAndTreeChildIDs.trim().isEmpty()) {
            query.append(" and (");
            query.append("it.department_id in (").append(departmentAndTreeChildIDs).append(") \n");
            query.append("or adjit.departmentid in (").append(departmentAndTreeChildIDs).append(") \n");
            query.append("or adjitem.departmentid in (").append(departmentAndTreeChildIDs).append(") \n");
            query.append("or (tr.invoiceid is not null and invitem.departmentid in (").append(departmentAndTreeChildIDs).append(")) \n");
            query.append("or (tr.purchaseorder_id is not null and poitem.departmentid in (").append(departmentAndTreeChildIDs).append(")) \n");
            query.append("or (tr.saleorder_id is not null and soitem.departmentid in (").append(departmentAndTreeChildIDs).append(")) \n");
            query.append("or (tr.shippingDataId is not null and shitem.departmentid in (").append(departmentAndTreeChildIDs).append(")) \n");
            query.append(") \n");
        }
        if (projectID != null) {
            query.append(" AND (");
            query.append("it.project_id = ").append(projectID).append(" \n");
            query.append("or (tr.expensereportid is not null and expr.projectId=").append(projectID).append(") \n");
            query.append("or (tr.expensePaymentId is not null and payexp.projectId=").append(projectID).append(") \n");
            query.append("or (tr.banktransferid is not null and spr.projectid=").append(projectID).append(") \n");
            query.append("or (tr.adjustment_id is not null and adjit.projectid=").append(projectID).append(") \n");
            query.append("or (tr.stockTransferId is not null and adjitem.projectid=").append(projectID).append(") \n");
            query.append("or (pt.projectid=").append(projectID).append(") \n");
            if (genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.MANUAL_JOURNAL_PM_TO_HEAD_ENABLED)) {
                query.append("or (tr.manualjournalid is not null and mj.projectid=").append(projectID).append(") \n");
            }
            if (genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.PROJECT_IN_LINE_ITEM_ENABLE)) {
                query.append("or (tr.purchaseorder_id is not null and poitem.project_id=").append(projectID).append(") \n");
                query.append("or (tr.saleorder_id is not null and soitem.project_id=").append(projectID).append(") \n");
                query.append("or (tr.invoiceid is not null and invitem.project_id=").append(projectID).append(") \n");
                query.append("or (tr.shippingDataId is not null and shitem.project_id=").append(projectID).append(") \n");
            } else {
                query.append("or (tr.invoiceid is not null and i.relatedproject_id=").append(projectID).append(") \n");
                query.append("or (tr.purchaseorder_id is not null and quo.relatedproject_id=").append(projectID).append(") \n");
            }
            query.append(") ");
        } else if (!ServerUtils.hasPermission(PermissionConstants.PM_SEE_ALL_PROJECTS) && genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.MANUAL_JOURNAL_PM_TO_HEAD_ENABLED)) {
            query.append(" and prj.managerid = ").append(((EdsUser) ServerSecurityContext.getInstance().getUser()).getObjectID()).append(" ");
        }

        if (to != null) {
            query.append(" and  ").append(ServerUtils.checkForDeleted("tr.deleted")).append(" and tr.journalDate < '" + to + "'");
            query.append(" group by account.id,faccount.id ");
            return findNative(query.toString(), EdsAccount.class);
        }
        return new LinkedList<>();
    }

    public List<EdsAccount> getRevenueExpensesAttendedInTransactions(FromToDate main, FromToDate[] compareTo, String departmentAndTreeChildIDs, Integer projectID, String sortField, String sortDirection) {

        StringBuilder query = new StringBuilder();
        query.append("select ac.*, faccount.*, (case when faccount.id is not null then 1 else 0 end) as clazz_ from ").append(getCompanyId()).append(".account ac ");
        query.append("join ").append(getPublic()).append(".accounttype act on  ac.accountTypeId=act.id ");
        query.append("join ").append(getCompanyId()).append(".transactionItem it on ac.id=it.accountid ");
        query.append("join ").append(getCompanyId()).append(".transaction tr on it.transactionid=tr.id ");

        query.append("left outer join ").append(getCompanyId()).append(".floatingAccount faccount on faccount.id = ac.id ");

        query.append("left join ").append(getCompanyId()).append(".invoice i on i.id=tr.invoiceid ");
        query.append("left join ").append(getCompanyId()).append(".manualjournal mj on tr.manualjournalid=mj.id ");
        query.append("left join ").append(getCompanyId()).append(".expensereport expr on expr.id=tr.expensereportid ");
        query.append("left join ").append(getCompanyId()).append(".expensePayments expay on tr.expensePaymentId=expay.id ");
        query.append("left join ").append(getCompanyId()).append(".expensereport payexp on expay.expenseReportId=payexp.id ");
        query.append("left join ").append(getCompanyId()).append(".expense exp on expr.id=exp.reportId ");
        query.append("left join ").append(getCompanyId()).append(".expense exp2 on payexp.id=exp2.reportId ");
        query.append("left join ").append(getCompanyId()).append(".quote quo on quo.id=tr.purchaseorder_id ");
        query.append("left join ").append(getCompanyId()).append(".shipping_data shd on tr.shippingdataid=shd.id ");
        query.append("left join ").append(getCompanyId()).append(".quote shq on shd.quoteid=shq.id ");
        query.append("left join ").append(getCompanyId()).append(".spendreceivemoney spr on spr.id = tr.banktransferid ");
        query.append("left join ").append(getCompanyId()).append(".adjustment_item adjit on adjit.id=it.stock_adjustment_item_id ");

        query.append("left join ").append(getCompanyId()).append(".quoteitem poitem on tr.purchaseorder_id is not null and it.itemid=poitem.id \n");
        query.append("left join ").append(getCompanyId()).append(".quoteitem soitem on tr.saleorder_id is not null and it.itemid=soitem.id \n");
        query.append("left join ").append(getCompanyId()).append(".invoiceitem invitem on tr.invoiceid is not null and it.itemid=invitem.id \n");
        query.append("left join ").append(getCompanyId()).append(".shipping_data_items shi on tr.shippingDataId is not null and it.itemid = shi.id \n");
        query.append("left join ").append(getCompanyId()).append(".quoteitem shitem on shi.quoteItemId = shitem.id \n");
        query.append("left join ").append(getCompanyId()).append(".adjustment_item adjitem on tr.stockTransferId is not null and it.itemid=adjitem.id \n");
        query.append("left join ").append(getCompanyId()).append(".payslipTableItem pti on tr.payrun_id is not null and tr.payrun_id=pti.id ");
        query.append("left join ").append(getCompanyId()).append(".payslipTable pt on pti.payslipTable_id is not null and pti.payslipTable_id=pt.id ");
        if (genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.PROJECT_IN_LINE_ITEM_ENABLE)) {
            query.append("left join ").append(getCompanyId()).append(".project prj on coalesce(coalesce(coalesce(coalesce(coalesce(coalesce(coalesce(coalesce(coalesce(coalesce(it.project_id,expr.projectId),payexp.projectId),spr.projectid),adjit.projectid),adjitem.projectid),mj.projectid),poitem.project_id),soitem.project_id),invitem.project_id),shitem.project_id) = prj.id \n");
        } else {
            query.append("left join ").append(getCompanyId()).append(".project prj on coalesce(coalesce(coalesce(coalesce(coalesce(coalesce(coalesce(coalesce(it.project_id,expr.projectId),payexp.projectId),spr.projectid),adjit.projectid),adjitem.projectid),mj.projectid),i.relatedproject_id),quo.relatedproject_id) = prj.id \n");
        }

        query.append("where ").append(ServerUtils.checkForDeleted("tr.deleted")).append(" and (act.category='").append(EdsAccountType.REVENUE).append("' or act.category ='").append(EdsAccountType.EXPENSES).append("') ");
        if (departmentAndTreeChildIDs != null && !departmentAndTreeChildIDs.trim().isEmpty()) {
            query.append(" and (");
            query.append("it.department_id in (").append(departmentAndTreeChildIDs).append(") \n");
            query.append("or adjit.departmentid in (").append(departmentAndTreeChildIDs).append(") \n");
            query.append("or adjitem.departmentid in (").append(departmentAndTreeChildIDs).append(") \n");
            query.append("or (tr.invoiceid is not null and invitem.departmentid in (").append(departmentAndTreeChildIDs).append(")) \n");
            query.append("or (tr.purchaseorder_id is not null and poitem.departmentid in (").append(departmentAndTreeChildIDs).append(")) \n");
            query.append("or (tr.saleorder_id is not null and soitem.departmentid in (").append(departmentAndTreeChildIDs).append(")) \n");
            query.append("or (tr.shippingDataId is not null and shitem.departmentid in (").append(departmentAndTreeChildIDs).append(")) \n");
            query.append(") \n");
        }
        if (projectID != null) {
            query.append(" AND (");
            query.append("it.project_id = ").append(projectID).append(" \n");
            query.append("or (tr.expensereportid is not null and (expr.projectId=").append(projectID).append(" or exp.project_id=").append(projectID).append(")").append(") ");
            query.append("or (tr.expensePaymentId is not null and (payexp.projectId=").append(projectID).append(" or exp2.project_id=").append(projectID).append(")").append(") ");
            query.append("or (tr.banktransferid is not null and spr.projectid=").append(projectID).append(") ");
            query.append("or (tr.adjustment_id is not null and adjit.projectid=").append(projectID).append(") ");
            query.append("or (tr.stockTransferId is not null and adjitem.projectid=").append(projectID).append(") ");
            query.append("or (pt.projectid=").append(projectID).append(") \n");
            if (genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.MANUAL_JOURNAL_PM_TO_HEAD_ENABLED)) {
                query.append("or (tr.manualjournalid is not null and mj.projectid=").append(projectID).append(") ");
            }
            if (genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.PROJECT_IN_LINE_ITEM_ENABLE)) {
                query.append("or (tr.purchaseorder_id is not null and poitem.project_id=").append(projectID).append(") ");
                query.append("or (tr.saleorder_id is not null and soitem.project_id=").append(projectID).append(") ");
                query.append("or (tr.invoiceid is not null and  invitem.project_id=").append(projectID).append(") ");
                query.append("or (tr.shippingDataId is not null and shitem.project_id=").append(projectID).append(") ");
            } else {
                query.append("or (tr.invoiceid is not null and (i.relatedproject_id=").append(projectID).append(")").append(") ");
                query.append("or (tr.purchaseorder_id is not null and (quo.relatedproject_id=").append(projectID).append(")").append(") ");
                query.append("or (tr.shippingdataid is not null and (shq.relatedproject_id=").append(projectID).append(")").append(") ");
            }
            query.append(") ");
        } else if (!ServerUtils.hasPermission(PermissionConstants.PM_SEE_ALL_PROJECTS) && genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.MANUAL_JOURNAL_PM_TO_HEAD_ENABLED)) {
            query.append(" and prj.managerid = ").append(((EdsUser) ServerSecurityContext.getInstance().getUser()).getObjectID()).append(" ");
        }
        if (compareTo != null && compareTo.length > 0) {
            query.append(" and ((tr.journalDate between '").append(main.getFrom().getNonConvertedDate()).append("' and '").append(main.getTo().getNonConvertedDate()).append("')");
            for (FromToDate compare : compareTo) {
                query.append(" or (tr.journalDate between '").append(compare.getFrom().getNonConvertedDate()).append("' and '").append(compare.getTo().getNonConvertedDate()).append("')");
            }
            query.append(")");
        } else {
            query.append(" and (tr.journalDate between '").append(main.getFrom().getNonConvertedDate()).append("' and '").append(main.getTo().getNonConvertedDate()).append("')");
        }
        query.append(" group by ac.id,faccount.id ");
        if (sortField != null) {
            if (ACC_CODE.equals(sortField)) {
                query.append(" order by ac.accountCode ").append(sortDirection);
            } else if (ACC_NAME.equals(sortField)) {
                query.append(" order by ac.name ").append(sortDirection);
            }
        }
        return findNative(query.toString(), EdsAccount.class);
    }

    public List<EdsAccount> getRevenueExpensesAttendedInTransactions() {
        Map<String, Object> map = new HashMap<>();
        map.put("revenue", EdsAccountType.REVENUE);
        map.put("expenses", EdsAccountType.EXPENSES);
        return findByNamedParams("select ac from EdsAccount ac where " + ServerUtils.checkForDeleted("ac.deleted") +
                " and (ac.accountType.category=:revenue or ac.accountType.category =:expenses)", map);
    }

    public EdsAccountType getAccountType(Integer objectID) {
        return (EdsAccountType) findSingle("from EdsAccountType at where at.objectID=?", objectID);
    }

    public EdsAccountType getAccountTypeByCode(String code) {
        return (EdsAccountType) findSingle("select ac from EdsAccountType ac where ac.code=?", code);
    }
    //for csv parsing usage only

    public EdsAccount getAccountCount(String code, Integer accountID) {
        if (accountID != null) {
            return (EdsAccount) findSingle("from EdsAccount ac where " + ServerUtils.checkForDeleted("ac.deleted") + " and  ac.accountCode=? and ac.objectID!=?", code, accountID);
        } else {
            return (EdsAccount) findSingle("from EdsAccount ac where " + ServerUtils.checkForDeleted("ac.deleted") + " and  ac.accountCode=?", code);
        }
    }

    public List<EdsBankAccount> getBankAccountList(ListingFilterParameter fp) {
        EdsUser user = getUser();
        boolean searAll = fp.getBeforeSelectedId() == null;
        if (fp.getBeforeSelectedId() != null) {
            EdsBankAccount beforeSelectedAccount = bankAccountManager.getBankAccountByAccountID(fp.getBeforeSelectedId());
            if (beforeSelectedAccount != null) {
                for (EdsUser owner : beforeSelectedAccount.getOwners()) {
                    if (user.getObjectID().equals(owner.getObjectID())) {
                        searAll = true;
                        break;
                    }
                }
            }
        }
        searAll = searAll && fp.isCheckBeforeSelected();

        StringBuilder sql = new StringBuilder();
        sql.append("select ba from EdsBankAccount ba where ba.objectID in ( ");
        sql.append("select b.objectID from EdsBankAccount b ");
        if (!getUser().hasEitherRoles(EdsRole.ADMIN)) {
            sql.append(" left join b.owners owners");
        }
        sql.append(" where ").append(ServerUtils.checkForDeleted("b.account.deleted"));
        if (!getUser().hasEitherRoles(EdsRole.ADMIN) && !searAll) {
            sql.append(" and (owners.objectID is null or owners.objectID = ").append(getUser().getObjectID()).append(")");
        }

        if (fp.isEnablePayments()) {
            sql.append(" and b.account.enablePayments = true ");
        }
        sql.append(" and b.account.accountType.code = '" + EdsAccountType.BANK + "' ");

        if (fp.isLookUp()) {
            sql.append(" and b.active = true ");
            String sqlSearchKey = fp.getSqlSearchKey() != null ? fp.getSqlSearchKey() : "";
            if (fp.getLookUpBy() != null && Constants.B_ACCOUNT_NUMBER.equals(fp.getLookUpBy())) {
                sql.append(" and lower(b.accountNumber) like '%").append(sqlSearchKey).append("%' ");
            } else if (fp.getLookUpBy() != null && Constants.B_ACCOUNT_CODE.equals(fp.getLookUpBy())) {
                sql.append(" and lower(b.account.accountCode) like '%").append(sqlSearchKey).append("%' ");
            } else if (fp.isValidSearchKey()) {
                sql.append(" and lower(b.account.name) like '%").append(sqlSearchKey).append("%' ");
            }
        } else {
            getSqlWhereBankAccountList(fp, sql);
        }
        sql.append(" ) ");
        if (fp.isLookUp()) {
            sql.append(" order by ba.account.name desc");
        } else {
            if (fp.getSortField() != null) {
                if ((BankAccount.CODE_COLUMN).equals(fp.getSortField())) {
                    sql.append(" order by ba.account.codeString").append(fp.isAscending() ? " desc" : "");
                } else if ((BankAccount.NAME_COLUMN).equals(fp.getSortField())) {
                    sql.append(" order by ba.account.name").append(fp.isAscending() ? " desc" : "");
                } else if ((BankAccount.NUMBER_COLUMN).equals(fp.getSortField())) {
                    sql.append(" order by ba.accountNumber").append(fp.isAscending() ? " desc" : "");
                } else if ((BankAccount.CURRENCY_COLUMN).equals(fp.getSortField())) {
                    sql.append(" order by ba.account.currency.name").append(fp.isAscending() ? " desc" : "");
                } else {
                    sql.append(" order by ba.objectID desc");
                }
            } else {
                sql.append(" order by ba.objectID desc");
            }
        }
        return findInterval(sql.toString(), fp.getStart(), fp.getLimit());
    }

    private void getSqlWhereBankAccountList(ListingFilterParameter fp, StringBuilder sql) {
        if (fp != null) {
            if (fp.isShowActive()) {
                sql.append(" and b.active = ").append(fp.isShowActive());
            }
            if (fp.getSqlSearchKey() != null) {
                sql.append(" and (lower(b.account.codeString) like '").append(fp.getSqlSearchKey()).append("' or ");
                sql.append(" lower(b.account.name) like '").append(fp.getSqlSearchKey()).append("' or ");
                sql.append(" lower(b.accountNumber) like '").append(fp.getSqlSearchKey()).append("') ");
            }
            if (fp.getBankAccountCode() != null) {
                sql.append(" and (b.account.accountCode = '").append(fp.getBankAccountCode()).append("') ");
            }
            if (fp.getBankAccountName() != null) {
                sql.append(" and (b.account.name = '").append(fp.getBankAccountName()).append("') ");
            }
            if (fp.getBankAccountNumber() != null) {
                sql.append(" and (b.accountNumber = '").append(fp.getBankAccountNumber()).append("') ");
            }
            if (fp.getBankAccountCurrencyId() != null) {
                sql.append(" and (b.account.currency.objectID = ").append(fp.getBankAccountCurrencyId()).append(") ");
            }
        }
    }

    public List<EdsBankAccount> getBankAccounts() {
        StrBuilder sql = new StrBuilder("select b from EdsBankAccount b ");

        sql.append(" left join b.owners owners");
        sql.append(" where ").append(ServerUtils.checkForDeleted("b.account.deleted"));
        if (!getUser().hasEitherRoles(EdsRole.ADMIN)) {
            sql.append(" and (owners.objectID is null or owners.objectID = ").append(getUser().getObjectID()).append(")");
        }
        return find(sql.toString());
    }

    @Override
    public List<EdsBankAccount> getBankAccountsForReference() {
        StrBuilder sql = new StrBuilder("select b from EdsBankAccount b ");
        sql.append(" where ").append(ServerUtils.checkForDeleted("b.account.deleted"));

        return find(sql.toString());
    }

    public List<EdsAccount> getAccounts(ListingFilterParameter filterParametrs) {
        filterParametrs = filterParametrs != null ? filterParametrs : new ListingFilterParameter();
        String sql = "select ac from EdsAccount ac where " + ServerUtils.checkForDeleted("ac.deleted ");
        sql += " and ac.active is true ";
        if (filterParametrs.getAccountType() != null) {
            sql += " and ac.accountType.category = '" + filterParametrs.getAccountType() + "'";
        }
        if (filterParametrs.getAccountTypes() != null && !filterParametrs.getAccountTypes().isEmpty()) {
            sql += " and ac.accountType.code in ('" + ServerUtils.getAsCommoDelimited(filterParametrs.getAccountTypes(), "0", "','") + "')";
        }
        if (filterParametrs.getParameters() != null) {
            sql += " and ( ac.accountType.code = '" + filterParametrs.getParameters()[0] + "'";
            sql += " or ac.accountType.code = '" + filterParametrs.getParameters()[1] + "'";
            if (filterParametrs.getParameters().length == 3) {//see CashAccountLookUp.java 28-row. this is for showing Bank account on Cash Receipts(Payments) add/edit Account loockup
                sql += " or ac.accountType.code = '" + filterParametrs.getParameters()[2] + "' )";
            } else {
                sql += " )";
            }
            if (BankAccountTypeEnum.CURRENT_ASSET.getCode().equals(filterParametrs.getParameters()[0])) {
                sql += " and ac.enablePayments = true";
            }
        }
        if (filterParametrs.isCheckNumber()) {
            sql += " and ac.balance is not null ";
        }
        if (filterParametrs.getSqlSearchKey() != null && !"".equals(filterParametrs.getSqlSearchKey())) {
            String searchKey = filterParametrs.getSqlSearchKey();
            sql += " and (";
            sql += "lower(ac.codeString) like '" + searchKey + "' or ";
            sql += "lower(ac.name) like '" + searchKey + "' )";
        }
        return find(sql);
    }

    // 1st case - Detailed invoice

    public List<ProjectBaseData> getProjectBaseData(List<Integer> projects, Date startDate, Date endDate) {
        return getProjectBaseData(null, projects, startDate, endDate);
    }

    @Override
    public List<ProjectBaseData> getProjectBaseData(Integer employeeId, List<Integer> projects, Date startDate, Date endDate) {
        boolean doNotShowDeletedTaskInInvoice = genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.DO_NOT_SHOW_DELETED_TASKS_IN_INVOICE);

        Map<String, Object> map = new HashMap<>();
        map.put("projects", projects);
        map.put("startDate", startDate);
        map.put("endDate", endDate);
        map.put("status", EdsTimeSheet._APPROVE);
        String sb = "select new com.edatasite.workforce.gwt.invoice.client.rpc.ProjectBaseData " + "  (p.objectID, e.objectID, t.objectID, e.firstName, e.lastName, p.name, p.description, t.name, t.description, tsh.objectID, tsh.timeSpent, tsh.date, " + " (case when t.taskAmount is not null and t.taskAmount > 0 then t.taskAmount else pe.clientChargeRate end) as clientChargeRate, " + "pe.wageRate, ep.name, (case when t.taskAmount is not null and t.taskAmount > 0 then true else false end) as fixed) from EdsTimeSheet tsh" + //Can also support rates change history
                "  left join tsh.employeeTask et" + "  left join et.task t  " + "  left join et.projectEmployee pe " + "  left join pe.employeeDepartment ed " + "  left join ed.employee e " + "  left join e.position ep " + "  left join pe.project p where" + "  p.objectID in (:projects) and t.billable is true and (tsh.usedInInvoice is null or tsh.usedInInvoice is false) and tsh.status.code =:status and tsh.date between :startDate and :endDate and tsh.timeSpent > 0" +
                (employeeId != null ? " and tsh.employeeID = " + employeeId : "") + (doNotShowDeletedTaskInInvoice ? " and (t.deleted = false or t.deleted is null) " : "") + "  order by p.name, tsh.date, e.objectID, t.objectID";

        return findByNamedParams(sb, map);
    }

    @Override
    public List<ProjectBaseData> getProjectBaseDataFE(Integer employeeId, List<Integer> projects, Date startDate, Date endDate) {
        boolean doNotShowDeletedTaskInInvoice = genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.DO_NOT_SHOW_DELETED_TASKS_IN_INVOICE);

        Map<String, Object> map = new HashMap<>();
        map.put("projects", projects);
        map.put("startDate", startDate);
        map.put("endDate", endDate);
        map.put("status", EdsTimeSheet._APPROVE);
        String sb = "select new com.edatasite.workforce.gwt.invoice.client.rpc.ProjectBaseData " + "  (p.objectID, e.objectID, t.objectID, e.firstName, e.lastName, p.name, p.description, t.name, t.description, tsh.objectID, tsh.timeSpent, tsh.date, pe.clientChargeRate, pe.wageRate, '') from EdsTimeSheet tsh" + //Can also support rates change history
                "  left join tsh.employeeTask et" + "  left join et.task t  " + "  left join et.projectEmployee pe " + "  left join pe.employeeDepartment ed " + "  left join ed.employee e " + "  left join pe.project p where" + "  p.objectID in (:projects) and t.billable is true and (tsh.usedInExpense is null or tsh.usedInExpense is false) and tsh.status.code =:status and tsh.date between :startDate and :endDate and tsh.timeSpent > 0" +
                (employeeId != null ? " and tsh.employeeID = " + employeeId : "") + (doNotShowDeletedTaskInInvoice ? " and (t.deleted = false or t.deleted is null) " : "") + "  order by p.objectID, e.objectID, t.objectID";

        return findByNamedParams(sb, map);
    }

    public List<ProjectBaseData> getProjectBaseMonthlyData(List<Integer> projects, Date from, Date to) {
        Map<String, Object> map = new HashMap<>();
        map.put("projects", projects);
        map.put("startDate", from);
        map.put("endDate", to);
        map.put("selectDate", ServerUtils.getMonthYearList(from, to));

        SqlParameterSource args = new MapSqlParameterSource(map);

        String sql = "SELECT p.id as projectId, e.id as employeeId, t.id as taskId, e.firstName, e.lastName, p.name as projectName, p.description as projectDescription, t.name as taskName, t.description as taskDescription, ts.id as timesheetEntryId, ts.date as tsEntryDate, ts.timeSpent, tsh.total_days_worked as totalDaysWorked, pe.clientChargeRate, tsh.overtime, tsh.weekend_overtime as weekendOvertime, tsh.holiday_overtime as holidayOvertime, tsh.month_year as monthYear, ep.name as employeePosition \n" +
                " FROM " + getCompanyId() + ".monthly_timesheet tsh \n" +
                " LEFT JOIN " + getCompanyId() + ".projectEmployee pe on pe.id = tsh.project_employee_id \n" +
                " LEFT JOIN " + getCompanyId() + ".employeetask et on et.projectemployeeId = pe.id \n" +
                " LEFT JOIN " + getCompanyId() + ".task t on t.id = et.taskId \n" +
                " LEFT JOIN " + getCompanyId() + ".teamEmployee ed on ed.id = pe.employeeDepartmentId \n" +
                " LEFT JOIN " + getCompanyId() + ".employee emp on emp.id = ed.employeeId \n" +
                " LEFT JOIN " + getCompanyId() + ".position ep on emp.positionid = ep.id \n" +
                " LEFT JOIN " + getCompanyId() + ".myuser e on e.id = emp.id \n" +
                " LEFT JOIN " + getCompanyId() + ".timesheet ts on ts.employeetaskId = et.id \n" +
                " LEFT JOIN " + getCompanyId() + ".project p on p.id = pe.projectid \n" +
                " WHERE p.id in (:projects) AND tsh.month_year in (:selectDate) \n" +
                " AND t.billable is true \n" +
                " AND ts.date between :startDate and :endDate \n" +
                " AND (ts.usedininvoice is null or ts.usedininvoice is false) \n" +
                " AND (tsh.total_days_worked > 0 or ts.timeSpent > 0) \n";

        return jdbcSpringManager.getSimpleJdbcTemplate().query(sql, args, BeanPropertyRowMapper.newInstance(ProjectBaseData.class));
    }

    //FOR USAGE INSIDE OF ACCOUNTING GETTING STARTED ONLY ???????

    public void deleteGLAccountsInattendedInInvoices() {        //invoiceItems                       //transactionItems
        update("delete from EdsAccount ac where " +
                "ac.itemsInAccount IS EMPTY " +
                "and ac.invoiceItemsInAccount IS EMPTY " +
                "and ac.expenseItemsInAccount IS EMPTY " +
                "and ac.conversionBalanceItemsInAccount IS EMPTY " +
                "and ac.productsWithDeletesInAccount IS EMPTY " +
                "and ac.productsInCogsAccount IS EMPTY " +
                "and ac.productsInAssetAccount IS EMPTY " +
                "and ac.quoteItemsInAccount IS EMPTY " +
                "and ac.invoicePaymentsWithDeletesInAccount IS EMPTY " +
                "and ac.expensePaymentsWithDeletesInAccount IS EMPTY " +
                "and ac.budgetsInAccount IS EMPTY " +
                "and ac.assetsWithDeletesInAccount IS EMPTY " +
                "and ac.financedAssetsWithDeletesInAccount IS EMPTY " +
                "and ac.invoicesWithBillExp IS EMPTY " +
                "and ac.manualJournalItems IS EMPTY " +
                "and ac.expenseReportList IS EMPTY " +
                "and ac.key IS NULL");
    }

    @Override
    public List<Integer> getGLAccountsInattendedInInvoices() {
        return find("select ac.objectID from EdsAccount ac where " +
                "ac.itemsInAccount IS EMPTY " +
                "and ac.invoiceItemsInAccount IS EMPTY " +
                "and ac.expenseItemsInAccount IS EMPTY " +
                "and ac.conversionBalanceItemsInAccount IS EMPTY " +
                "and ac.productsWithDeletesInAccount IS EMPTY " +
                "and ac.productsInCogsAccount IS EMPTY " +
                "and ac.productsInAssetAccount IS EMPTY " +
                "and ac.quoteItemsInAccount IS EMPTY " +
                "and ac.invoicePaymentsWithDeletesInAccount IS EMPTY " +
                "and ac.expensePaymentsWithDeletesInAccount IS EMPTY " +
                "and ac.budgetsInAccount IS EMPTY " +
                "and ac.assetsWithDeletesInAccount IS EMPTY " +
                "and ac.financedAssetsWithDeletesInAccount IS EMPTY " +
                "and ac.invoicesWithBillExp IS EMPTY " +
                "and ac.manualJournalItems IS EMPTY " +
                "and ac.expenseReportList IS EMPTY " +
                "and ac.key IS NULL");
    }

    public EdsAccount getAccountForDelete(Integer objectId) {
        return (EdsAccount) findSingle("select ac from EdsAccount ac where " +
                "ac.itemsInAccount IS EMPTY " +
                "and ac.invoiceItemsInAccount IS EMPTY " +
                "and ac.expenseItemsInAccount IS EMPTY " +
                "and ac.conversionBalanceItemsInAccount IS EMPTY " +
                "and ac.productsInAccount IS EMPTY " +
                "and ac.productsInCogsAccount IS EMPTY " +
                "and ac.productsInAssetAccount IS EMPTY " +
                "and ac.quoteItemsInAccount IS EMPTY " +
                "and ac.invoicePaymentsInAccount IS EMPTY " +
                "and ac.expensePaymentsInAccount IS EMPTY " +
                "and ac.budgetsInAccount IS EMPTY " +
                "and ac.objectID=?", objectId);
    }

    public EdsTransaction getConversionBalanceTransaction() {
        return (EdsTransaction) findSingle("select t from EdsTransaction t where t.name = ?", "Conversion Balance");
    }

    public EdsAccount getAccountTypeWithMinCode(String accountType) {
        return (EdsAccount) findSingle("select a from EdsAccount a where " + ServerUtils.checkForDeleted("a.deleted") + " and a.accountType.objectID = " +
                "(select at.id from EdsAccountType at where at.code = ?) order by a.accountCode asc", accountType);
    }

    @Override
    public List<EdsAccount> getAccountsByType(String typeCode) {
        return (List<EdsAccount>) find("select a from EdsAccount a where " + ServerUtils.checkForDeleted("a.deleted") +
                " and a.accountType.code = ? order by a.name", typeCode);
    }

    @Override
    public List<EdsAccount> getAccountsByCategory(String category, String code) {
        return (List<EdsAccount>) find("select a from EdsAccount a where " + ServerUtils.checkForDeleted("a.deleted") +
                " and (a.accountType.category = ? or a.accountType.code = ?) order by a.name", category, code);
    }

    @Override
    public EdsAccount getOneAccountByType(String typeCode) {
        return (EdsAccount) findSingle("select a from EdsAccount a where " + ServerUtils.checkForDeleted("a.deleted") + " and a.accountType.category = ? order by a.name", typeCode);
    }

    public boolean isDuplicateReference(String reference, Integer transactionID) {
        if (reference != null && !reference.trim().isEmpty()) {
            if (transactionID == null) {
                return !find("select t.objectID from EdsTransaction t where " + ServerUtils.checkForDeleted("t.deleted") + " and  t.reference = ?", reference).isEmpty();
            } else {
                return !find("select t.objectID from EdsTransaction t where " + ServerUtils.checkForDeleted("t.deleted") + " and t.reference = ? and t.objectID <> ?", reference, transactionID).isEmpty();
            }
        }
        return false;
    }

    @Override
    public Integer getProductLastIntNumber() {
        return (Integer) findSingle("select p.intNumber from EdsItem p where p.deleted=false and p.intNumber is not null order by p.intNumber desc");
    }

    @Override
    public Calendar getFinancialYearStartIfEnabled(Date creationDate) {
        EdsInvoicingSettings settings = (EdsInvoicingSettings) findSingle("select eis from EdsInvoicingSettings eis ");

        if (settings != null && settings.isNumberingRestartEnabled()) {
            Calendar financialYearStart = new GregorianCalendar();

            if (creationDate != null) {
                financialYearStart.setTime(creationDate);
            }
            financialYearStart.set(Calendar.MONTH, settings.getNumberingRestartMonth());
            financialYearStart.set(Calendar.DATE, settings.getNumberingRestartDate());
            ServerUtils.setBeginningOfTheDay(financialYearStart);

            if (financialYearStart.getTime().after(new Date())) {
                financialYearStart.set(Calendar.YEAR, financialYearStart.get(Calendar.YEAR) - 1);
            }
            return financialYearStart;
        }
        return null;
    }

    @Override
    public Integer getExpenseLastIntNumber() {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> values = new HashMap<>();
        sql.append("SELECT p.intNumber FROM EdsExpenseReport p WHERE p.isDeleted = FALSE and p.intNumber IS NOT NULL ");
        Calendar financialYearStart = getFinancialYearStartIfEnabled(null);
        if (financialYearStart != null && financialYearStart.getTime().before(new Date())) {
            values.put("financialYearStart", financialYearStart.getTime());
            sql.append(" and p.creationTime > :financialYearStart");
            sql.append(" and p.creationTime is not null");
        }
        sql.append(" order by p.intNumber desc");
        return (Integer) findSingleByNamedParams(sql.toString(), values);
    }

    @Override
    public Integer getSpendOrReceivMoneyLastIntNumber(Integer transferType) {
        if (transferType == null) {
            transferType = SPEND_MONEY;
        }
        return (Integer) findSingle("SELECT bt.intNumber FROM EdsBankTransfer bt WHERE bt.transferType = ? and bt.deleted = FALSE and bt.intNumber IS NOT NULL order by bt.intNumber desc", transferType);
    }

    @Override
    public boolean isProductNumberExists(String number, Integer productID) {
        if (productID != null) {
            return !find("select p from EdsItem p where (p.deleted = false or p.deleted is null) and p.productNumber = ? and p.objectID != ?", number.trim(), productID).isEmpty();
        } else {
            return !find("select p from EdsItem p where (p.deleted = false or p.deleted is null) and p.productNumber = ?", number.trim()).isEmpty();
        }
    }

    @Override
    public boolean isProductNameExists(String name, Integer productID) {
        if (productID == null) {
            return ((Long) findSingle("select count(objectID) from EdsItem b where " + ServerUtils.checkForDeleted("b.deleted") + " and upper(b.name)=?", name.toUpperCase())) > 0;
        } else {
            return ((Long) findSingle("select count(objectID) from EdsItem b where " + ServerUtils.checkForDeleted("b.deleted") + " and upper(b.name)=? and b.objectID != ?", name.toUpperCase(), productID)) > 0;
        }
    }


    public boolean isSpendOrReceiveMoneyNumberExists(String number, Integer moneyId, Integer transferType) {
        if (transferType == null) {
            transferType = SPEND_MONEY;
        }
        if (moneyId != null) {
            return !find("select bt from EdsBankTransfer bt where (bt.deleted = false or bt.deleted is null) and bt.transferType = ? and bt.number = ? and bt.objectID != ?", transferType, number.trim(), moneyId).isEmpty();
        } else {
            return !find("select bt from EdsBankTransfer bt where (bt.deleted = false or bt.deleted is null) and bt.transferType = ? and bt.number = ? ", transferType, number.trim()).isEmpty();
        }
    }

    @Override
    public Integer getBankAccountListCount(ListingFilterParameter fp) {
        EdsUser user = getUser();
        boolean searAll = fp.getBeforeSelectedId() == null;
        if (fp.getBeforeSelectedId() != null) {
            EdsBankAccount beforeSelectedAccount = bankAccountManager.getBankAccountByAccountID(fp.getBeforeSelectedId());
            if (beforeSelectedAccount != null) {
                for (EdsUser owner : beforeSelectedAccount.getOwners()) {
                    if (user.getObjectID().equals(owner.getObjectID())) {
                        searAll = true;
                        break;
                    }
                }
            }
        }
        searAll = searAll && fp.isCheckBeforeSelected();

        StringBuilder sql = new StringBuilder();
        sql.append("select count(ba.objectID) from EdsBankAccount ba where ba.objectID in ( ");
        sql.append("select b.objectID from EdsBankAccount b ");
        if (!getUser().hasEitherRoles(EdsRole.ADMIN)) {
            sql.append(" left join b.owners owners");
        }
        sql.append(" where ").append(ServerUtils.checkForDeleted("b.account.deleted"));
        if (!getUser().hasEitherRoles(EdsRole.ADMIN) && !searAll) {
            sql.append(" and (owners.objectID is null or owners.objectID = ").append(getUser().getObjectID()).append(")");
        }

        if (fp.isEnablePayments()) {
            sql.append(" and b.account.enablePayments = true ");
        }
        sql.append(" and b.account.accountType.code = '" + EdsAccountType.BANK + "' ");

        if (fp.isLookUp()) {
            sql.append(" and b.active = true ");
            String sqlSearchKey = fp.getSqlSearchKey() != null ? fp.getSqlSearchKey() : "";
            if (fp.getLookUpBy() != null && Constants.B_ACCOUNT_NUMBER.equals(fp.getLookUpBy())) {
                sql.append(" and lower(b.accountNumber) like '%").append(sqlSearchKey).append("%' ");
            } else if (fp.getLookUpBy() != null && Constants.B_ACCOUNT_CODE.equals(fp.getLookUpBy())) {
                sql.append(" and lower(b.account.accountCode) like '%").append(sqlSearchKey).append("%' ");
            } else if (fp.isValidSearchKey()) {
                sql.append(" and lower(b.account.name) like '%").append(sqlSearchKey).append("%' ");
            }
        } else {
            getSqlWhereBankAccountList(fp, sql);
        }
        sql.append(" ) ");

        return Integer.valueOf(findSingle(sql.toString()).toString());
    }


    @Override
    public void recalculateAccountBalances() {

        StringBuilder sql = new StringBuilder();
        sql.append("update ").append(getCompanyId()).append(".account set balance=vt.total, foreignbalance=vt.ftotal, balancecalculated=true from (");
        sql.append("select a.id as aid, coalesce(ab.total,0) total, coalesce(ab.ftotal, 0) ftotal from ").append(getCompanyId()).append(".account a ");
        sql.append("left join (select ac.id as aid, sum(case when ti.debit is not null then ");
        sql.append("case when at.category in ('ASSETS', 'EXPENSES') then ti.debit else (0-ti.debit) end ");
        sql.append("else case when at.category in ('LIABILITIES', 'EQUITY', 'REVENUE') then ti.credit else (0-ti.credit) end end) as total, ");
        sql.append("sum(case when ti.foreigndebit is not null then ");
        sql.append("case when at.category in ('ASSETS', 'EXPENSES') then ti.foreigndebit else (0-ti.foreigndebit) end ");
        sql.append("else case when at.category in ('LIABILITIES', 'EQUITY', 'REVENUE') then ti.foreigncredit else (0-ti.foreigncredit) end end) as ftotal ");
        sql.append("from ").append(getCompanyId()).append(".account ac ");
        sql.append("left join ").append(getPublic()).append(".accounttype at on at.id = ac.accounttypeid ");
        sql.append("left join ").append(getCompanyId()).append(".transactionitem ti on ti.accountid = ac.id ");
        sql.append("left join ").append(getCompanyId()).append(".transaction t on t.id = ti.transactionid ");
        sql.append("where ").append(ServerUtils.checkForDeleted("ac.deleted")).append(" and ");
        sql.append(ServerUtils.checkForDeleted("t.deleted"));
//        sql.append(" and ").append(ServerUtils.checkForDeleted("ac.balancecalculated"));
        sql.append(" group by ac.id) as ab on ab.aid = a.id ");
        sql.append("where ").append(ServerUtils.checkForDeleted("a.deleted"));
//        sql.append(" and ").append(ServerUtils.checkForDeleted("a.balancecalculated"));
        sql.append(") as vt  where vt.aid=id");

        updateNative(sql.toString());
    }

    @Override
    public BigDecimal getAccountBalance(Integer accountID) {
        String schema = ServerSecurityContext.getInstance().getCompanyId();

        StringBuilder sql = new StringBuilder();
        sql.append("SELECT SUM(CASE WHEN ti.debit is not null THEN " +
                "         CASE WHEN acct.category IN ('ASSETS', 'EXPENSES') THEN ti.debit ELSE (0 - ti.debit) END " +
                "    ELSE CASE WHEN acct.category IN ('LIABILITIES', 'EQUITY', 'REVENUE') THEN ti.credit ELSE (0-ti.credit) END " +
                "    END) ");
        sql.append("FROM \"").append(schema).append("\".account acc ");
        sql.append("LEFT JOIN ").append(getPublic()).append(".accountType acct ON acct.id = acc.accountTypeId ");
        sql.append("LEFT JOIN \"").append(schema).append("\".transactionItem ti ON ti.accountid = acc.id ");
        sql.append("INNER JOIN \"").append(schema).append("\".transaction tr ON tr.id = ti.transactionid ");
        sql.append("WHERE ").append(ServerUtils.checkForDeleted("tr.deleted"));
        sql.append(" AND acc.id = ").append(accountID);

        return (BigDecimal) findNativeSingle(sql.toString());
    }

    @Override
    public TransactionItem getAccountTransactionItems(Integer accountID) {

        String sql = "select sum(debit) as debit, sum(credit) as credit from " + getCompanyId() + ".transactionitem ti " +
                "left join " + getCompanyId() + ".transaction t on t.id=ti.transactionid " +
                "WHERE " + ServerUtils.checkForDeleted("t.deleted") +
                " AND ti.accountid = " + accountID;


        Object[] object = (Object[]) findNativeSingle(sql);

        TransactionItem transactionItem = new TransactionItem();
        if (object != null) {
            if (object[0] instanceof BigDecimal) {
                transactionItem.setDebit((BigDecimal) object[0]);
            }
            if (object[1] instanceof BigDecimal) {
                transactionItem.setCredit((BigDecimal) object[1]);
            }
        }

        return transactionItem;
    }

    @Override
    public BigDecimal getAccountForeignBalance(Integer accountID) {
        String schema = ServerSecurityContext.getInstance().getCompanyId();

        StringBuilder sql = new StringBuilder();
        sql.append("SELECT SUM(CASE WHEN ti.foreigndebit is not null THEN " +
                "         CASE WHEN acct.category IN ('ASSETS', 'EXPENSES') THEN ti.foreigndebit ELSE (0 - ti.foreigndebit) END " +
                "    ELSE CASE WHEN acct.category IN ('LIABILITIES', 'EQUITY', 'REVENUE') THEN ti.foreigncredit ELSE (0-ti.foreigncredit) END " +
                "    END) ");
        sql.append("FROM \"").append(schema).append("\".account acc ");
        sql.append("LEFT JOIN ").append(getPublic()).append(".accounttype acct ON acct.id = acc.accountTypeId ");
        sql.append("LEFT JOIN \"").append(schema).append("\".transactionItem ti ON ti.accountid = acc.id ");
        sql.append("INNER JOIN \"").append(schema).append("\".transaction tr ON tr.id = ti.transactionid ");
        sql.append("WHERE ").append(ServerUtils.checkForDeleted("tr.deleted"));
        sql.append(" AND acc.id = ").append(accountID);

        return (BigDecimal) findNativeSingle(sql.toString());
    }

    public Map<Integer, AccountListItem> getAccountBalanceMap(List<Integer> accountIds) {
        String company = getCompanyId();
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT acc.id, (CASE WHEN acc.key is null and max(tr.id) is null THEN true ELSE false END) as editable, ");
        sql.append("SUM(CASE WHEN ti.debit is not null THEN ");
        sql.append("CASE WHEN acct.category IN ('ASSETS', 'EXPENSES') THEN ti.debit ELSE (0 - ti.debit) END ");
        sql.append("ELSE CASE WHEN acct.category IN ('LIABILITIES', 'EQUITY', 'REVENUE') THEN ti.credit ELSE (0-ti.credit) END ");
        sql.append("END) as debit, ");
        sql.append("SUM(CASE WHEN ti.foreigndebit is not null THEN ");
        sql.append("CASE WHEN acct.category IN ('ASSETS', 'EXPENSES') THEN ti.foreigndebit ELSE (0 - ti.foreigndebit) END ");
        sql.append("ELSE CASE WHEN acct.category IN ('LIABILITIES', 'EQUITY', 'REVENUE') THEN ti.foreigncredit ELSE (0-ti.foreigncredit) END ");
        sql.append("END) as fdebit ");
        sql.append("FROM ").append(company).append(".account acc ");
        sql.append("LEFT JOIN ").append(getPublic()).append(".accounttype acct ON acct.id = acc.accountTypeId ");
        sql.append("LEFT JOIN ").append(company).append(".transactionItem ti ON ti.accountid = acc.id ");
        sql.append("LEFT JOIN ").append(company).append(".transaction tr ON tr.id = ti.transactionid ");
        sql.append("WHERE ").append(ServerUtils.checkForDeleted("tr.deleted"));
        sql.append(" AND acc.id in (").append(ServerUtils.getAsCommoDelimited(accountIds, "0")).append(") ");
        sql.append("GROUP BY acc.id");

        List<Object[]> items = (List<Object[]>) findNative(sql.toString());

        Map<Integer, AccountListItem> result = new HashMap<>();
        if (items != null && !items.isEmpty()) {
            for (Object[] item : items) {
                AccountListItem listItem = new AccountListItem();
                Integer id = (Integer) item[0];
                Boolean editable = item[1] != null ? (Boolean) item[1] : Boolean.FALSE;
                BigDecimal debit = item[2] != null ? (BigDecimal) item[2] : null;
                BigDecimal fDebit = item[3] != null ? (BigDecimal) item[3] : null;
                listItem.setObjectID(id);
                listItem.setBalance(fDebit != null ? fDebit : debit);
                listItem.setEditable(editable);

                result.put(id, listItem);
            }
        }

        return result;
    }


    @Override
    public EdsAccount getAccountBySaasuUID(String saasuUID) {
        return (EdsAccount) findSingle("select a from EdsAccount a where a.deleted<>true and a.saasuGUID = ?", saasuUID);
    }

    @Override
    public void updateAccountCurrency(EdsCurrency currency) {
        update("update EdsAccount set currency=? where " + ServerUtils.checkForDeleted("deleted"), currency);
    }

    @Override
    public List<EdsAccount> getAccountListByCurrency(EdsCurrency currency) {
        return find("select ac from EdsAccount ac where ac.currency=? and " + ServerUtils.checkForDeleted("ac.deleted"), currency);
    }

    @Override
    public List<EdsAccount> getAllAccounts(ListingFilterParameter fp) {
        fp = fp != null ? fp : new ListingFilterParameter();
        StringBuilder sql = new StringBuilder();
        sql.append("select acc.*, 0 as clazz_ from ").append(getCompanyId()).append(".account acc");
        sql.append(" left join ").append(getCompanyId()).append(".bankAccount ba on acc.id = ba.accountid");
        EdsUser edsUser = this.getUser();
        if (edsUser == null && ServerSecurityContext.getInstance().getStaticUserID() != null) {
            edsUser = this.userManager.get(ServerSecurityContext.getInstance().getStaticUserID());
        }
        if (!edsUser.hasEitherRoles(EdsRole.ADMIN)) {
            sql.append(" left join ").append(getCompanyId()).append(".bankAccount_owners bao on bao.bankAccount_id = ba.id");
        }
        sql.append(" where ").append(ServerUtils.checkForDeleted("acc.deleted "));
        sql.append(" and acc.active is true ");

        if (fp.getAccountID() != null) {
            sql.append(" and acc.id = ").append(fp.getAccountID());
        }
        // !fp.isAllByFilter() get all accounts for account transaction report
        if (genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.MULTICURRENCY_ENABLED) && !fp.isAllByFilter()) {
            List<Integer> currencyIDs = new LinkedList<>();
            EdsFinancialSettings fs = financialSettingsManager.getFinancialSettings();
            currencyIDs.add(fs.getCurrency().getObjectID());
            if (fp.getCurrencyID() != null) {
                currencyIDs.add(fp.getCurrencyID());
            }
            if (!fs.getCurrency().getObjectID().equals(fp.getCurrencyID())) {
                sql.append(" and acc.currencyid in (").append(ServerUtils.getAsCommoDelimited(currencyIDs, "0")).append(")");
            }
        }

        sql.append(" and (ba.id is null or (ba.active = true  ");

        if (!edsUser.hasEitherRoles(EdsRole.ADMIN)) {
            sql.append("and (bao.owner_id is null or bao.owner_id = ").append(edsUser.getObjectID()).append(" )");
        }
        sql.append(" )) ");

        if (fp.getSqlSearchKey() != null && !fp.getSqlSearchKey().isEmpty()) {
            String searchKey = fp.getSqlSearchKey();
            sql.append(" AND (lower(acc.name) like '%").append(searchKey).append("' ");
            sql.append(" OR lower(acc.codeString) like '").append(searchKey).append("') ");
        }
        if (fp.isForExportOnly()) {
            sql.append(" AND acc.quickbook_account_id is null");
        }
        return (List<EdsAccount>) findNative(sql.toString(), EdsAccount.class);
    }

    @Override
    public ChartOfAccountItem getAccountsForSyncSaasu(Integer startIndex, Integer limit) {
        ChartOfAccountItem accountItem = new ChartOfAccountItem();
        ArrayList<EdsAccount> accountList = (ArrayList<EdsAccount>) findLimited("SELECT acc FROM EdsAccount acc WHERE " + ServerUtils.checkForDeleted("acc.deleted") + " AND acc.objectID > ?  ORDER BY acc.objectID ASC", limit, startIndex);
        int totalCount = find("select acc from EdsAccount acc where " + ServerUtils.checkForDeleted("acc.deleted")).size();
        accountItem.setAccountList(accountList);
        accountItem.setTotalCount(totalCount);
        return accountItem;
    }

    @Override
    public EdsAccount getAccountByQbAccountID(String qbGUID) {
        return (EdsAccount) findSingle("select a from EdsAccount a where a.deleted<>true and a.qbAccountID=?", qbGUID);
    }

    @Override
    public List<EdsAccount> getAccountsByIds(String Ids) {
        return (List<EdsAccount>) find("SELECT a FROM EdsAccount a WHERE " + ServerUtils.checkForDeleted("a.deleted") + " and a.objectID IN (" + Ids + ")");
    }

    @Override
    public EdsAccount getAccountByName(String name) {
        if (StringUtils.isBlank(name)) {
            return null;
        }
        return (EdsAccount) findSingle("from EdsAccount ac where " + ServerUtils.checkForDeleted("ac.deleted") + " and lower(trim(ac.name)) = ?", name.trim().toLowerCase());
    }

    @Override
    public String generateNewAccountNumberByAccountType(Integer startNumberingRange, Integer endNumberingRange) {
        String schema = ServerSecurityContext.getInstance().getCompanyId();
        Object object = findNativeSingle("SELECT \"" + schema + "\".generateAccountNumber(?, ?)", startNumberingRange, endNumberingRange);
        return (String) object;
    }

    @Override
    public EdsAccount getMultiCurrencyAccount(Integer key, EdsCurrency currency) {
        EdsUser user = userManager.getUser();
        EdsAccount account = getAccountByKey(key, currency.getObjectID());
        if (account == null) {
            EdsAccount baseAccount = getAccountByKey(key);
            account = baseAccount.createMultiCurrencyAccount(currency, user);
            String accountCode = generateAccountCode(baseAccount.getAccountType().getObjectID());
            account.setAccountCode(accountCode);
            account.setCodeString(accountCode);
            create(account);

            try {
                solrManager.addChartOfAccountToIndex(account);
            } catch (SolrServerException | IOException e) {
                e.printStackTrace();
            }
        }
        return account;
    }

    @Override
    public EdsAccount getGlAccount(Integer key, EdsCrmAccount crmAccount) {
        EdsUser user = userManager.getUser();
        EdsAccount baseAccount = getAccountByKey(key);
        EdsFinancialSettings financialSettings = financialSettingsManager.getFinancialSettings();
        EdsAccount account = null;
        if (key.equals(EdsAccount.ACCOUNTS_RECEIVABLE)) {
            account = crmAccount.getReceivable();
        } else if (key.equals(EdsAccount.ACCOUNTS_PAYABLE)) {
            account = crmAccount.getPayable();
        }
        if (account == null) {
            account = new EdsAccount();
            account.setName(crmAccount.getName());
            account.setAccountType(baseAccount.getAccountType());
            account.setBalance(BigDecimal.ZERO);
            account.setParent(baseAccount);
            account.setForeignAccount(false);
            account.setCurrency(financialSettings.getCurrency());
            account.setShowInExpense(baseAccount.getShowInExpense());
            account.setActive(baseAccount.isActive());
            account.setEnablePayments(baseAccount.getEnablePayments());
            String accountCode = generateAccountCode(baseAccount.getAccountType().getObjectID());
            account.setAccountCode(accountCode);
            account.setCodeString(accountCode);
            account.setLastUpdatedDate(new Date());
            account.setCreationTime(new Date());
            account.setLastUpdatedDate(new Date());
            account.setCreator(user);
            account.setUpdater(user);
            create(account);

            try {
                chartOfAccountSolrComponent.index(account);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return account;
    }

    private String generateAccountCode(Integer accountTypeID) {
        String accountCode = accountTypeSettingManager.getGeneratedAccountNumber(accountTypeID);
        EdsAccount account = getAccountByCode(accountCode);
        DecimalFormat codeFormat = new DecimalFormat("0000");
        while (account != null) {
            accountCode = codeFormat.format(Integer.parseInt(accountCode) + 1);
            account = getAccountByCode(accountCode);
        }

        return accountCode;
    }

    @Override
    public List<EdsCurrency> getAdjustmentEnabledCurrencies() {
        EdsFinancialSettings fs = financialSettingsManager.getFinancialSettings();
        Integer baseCurrencyID = fs.getCurrency().getObjectID();
        String companyID = getCompanyId();
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT c.id, c.* FROM ").append(companyID).append(".account acc ");
        sql.append("INNER JOIN ").append(getPublic()).append(".currency c ON c.id = acc.currencyid ");
        sql.append("WHERE c.id NOT IN (").append(baseCurrencyID).append(")");
        return (List<EdsCurrency>) findNative(sql.toString(), EdsCurrency.class);
    }

    @Override
    public List<EdsTransactionItem> getCrmAccountAdjustmentItems(Integer currencyID, EdsAccount account) {
        String companyID = getCompanyId();
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT ti.id, ti.* FROM ").append(companyID).append(".transactionitem ti ");
        sql.append("INNER JOIN ").append(companyID).append(".transaction t on t.id=ti.transactionid ");
        sql.append("INNER JOIN ").append(companyID).append(".account acc on acc.id=ti.accountid ");
        sql.append("INNER JOIN ").append(getPublic()).append(".currency c on c.id=acc.currencyid ");
        sql.append("WHERE acc.id = ").append(account.getObjectID().toString());
        sql.append(" AND c.id=").append(currencyID.toString());
        sql.append(" AND ").append(ServerUtils.checkForDeleted("t.deleted"));
        return findNative(sql.toString(), EdsTransactionItem.class);
    }

    @Override
    public List<EdsTransactionItem> getBankAccountAdjustmentItems(Integer currencyID) {
        String companyID = getCompanyId();
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT ti.id, ti.* FROM ").append(companyID).append(".transactionitem ti ");
        sql.append("INNER JOIN ").append(companyID).append(".transaction t on t.id=ti.transactionid ");
        sql.append("INNER JOIN ").append(companyID).append(".account acc on acc.id=ti.accountid ");
        sql.append("INNER JOIN ").append(getPublic()).append(".accounttype acct on acct.id=acc.accounttypeid ");
        sql.append("INNER JOIN ").append(getPublic()).append(".currency c on c.id=acc.currencyid ");
        sql.append("WHERE (acc.key = " + EdsAccount.BANK + "OR acct.code='" + EdsAccountType.BANK + "') ");
        sql.append(" AND c.id=").append(currencyID.toString());
        sql.append(" AND ").append(ServerUtils.checkForDeleted("t.deleted"));
        return findNative(sql.toString(), EdsTransactionItem.class);
    }

    @Override
    public Integer getStockAccountID() {
        return (Integer) findSingle("select acc.objectID from EdsAccount acc where acc.name = ? and acc.accountType.code = ? and "
                + ServerUtils.checkForDeleted("acc.deleted"), "Stock", EdsAccountType.CURRENT_ASSET);
    }

    public Integer getAccountIDByNameAndAccountType(String name, Integer accountTypeID) {
        return (Integer) findSingle("select acc.objectID from EdsAccount acc where acc.name = ? and acc.accountType.objectID = ? and "
                + ServerUtils.checkForDeleted("acc.deleted"), name, accountTypeID);
    }

    @Override
    public List<SelectItem> getCompanySalesMans(ListingFilterParameter filterParameter) {
        String companyID = "\"" + ServerSecurityContext.getInstance().getCompanyId() + "\"";
        StringBuilder sql = new StringBuilder();
        sql.append("select e.id as id, (mu.firstname ||' '|| mu.lastname) as name from ").append(companyID).append(".employee e ");
        sql.append("left join ").append(companyID).append(".myuser mu on (e.id=mu.id) ");
        sql.append("left join ").append(companyID).append(".myuser_role mur on (mur.users_id=mu.id) ");
        sql.append("where (mur.roles_id=").append(EdsUser.SALESPERSON).append(" or mur.roles_id=").append(EdsUser.SALESMAN).append(")");

        if (filterParameter != null && filterParameter.getSqlSearchKey() != null) {
            sql.append(" and (lower(mu.firstName) like '").append(filterParameter.getSqlSearchKey()).append("'");
            sql.append(" or lower(mu.lastName) like '").append(filterParameter.getSqlSearchKey()).append("')");
        }

        sql.append(" order by name asc");

        return jdbcSpringManager.getSimpleJdbcTemplate().query(sql.toString(), BeanPropertyRowMapper.newInstance(SelectItem.class));

    }

    @Override
    public Integer getCurrencyFromFinancialSettings(Integer companyID) {
        return (Integer) findNativeSingle("select currency_id from " + getPublic() + ".financialsettings where id=(select financialsettingsid from company where id=" + companyID.toString() + ")");
    }

    @Override
    public HashMap<String, Integer> getAccountsMapForCustomInvoiceImport() {
        String companyID = getCompanyId();
        String sql = "select acc.accountCode, acc.id from" + companyID + ".account acc " +
                "WHERE " + ServerUtils.checkForDeleted("acc.deleted") + " order by acc.id desc";
        List<Object[]> dataList = findNative(sql);

        HashMap<String, Integer> customersMap = new HashMap<>();
        for (Object[] data : dataList) {
            if (data.length > 1 && data[0] != null && data[1] != null) {
                customersMap.put(((String) data[0]).trim(), (Integer) data[1]);
            }
        }

        return customersMap;
    }

    @Override
    public TotalDebitCredit getDebitCreditTotal(EdsAccount account, String departmentAndTreeChildIDs, Integer projectID, Date from, Date to) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        StringBuilder sql = new StringBuilder();
        sql.append("SELECT SUM(coalesce(ti.debit,0)) as debit, SUM(coalesce(ti.credit, 0)) as credit ");

        sql.append("FROM ").append(getCompanyId()).append(".transactionitem ti ");
        sql.append("INNER JOIN ").append(getCompanyId()).append(".transaction t ON t.id = ti.transactionid ");

        sql.append("left join ").append(getCompanyId()).append(".invoice i on t.invoiceid=i.id ");
        sql.append("left join ").append(getCompanyId()).append(".manualjournal mj on t.manualjournalid=mj.id ");
        sql.append("left join ").append(getCompanyId()).append(".expensereport expr on t.expensereportid=expr.id ");
        sql.append("left join ").append(getCompanyId()).append(".expensePayments expay on t.expensePaymentId=expay.id  ");
        sql.append("left join ").append(getCompanyId()).append(".expensereport payexp on expay.expenseReportId=payexp.id ");
        sql.append("left join ").append(getCompanyId()).append(".quote quo on t.purchaseorder_id=quo.id ");
        sql.append("left join ").append(getCompanyId()).append(".spendreceivemoney spr on t.banktransferid=spr.id ");
        sql.append("left join ").append(getCompanyId()).append(".adjustment_item adjit on ti.stock_adjustment_item_id=adjit.id ");

        sql.append("left join ").append(getCompanyId()).append(".quoteitem poitem on t.purchaseorder_id is not null and ti.itemid=poitem.id \n");
        sql.append("left join ").append(getCompanyId()).append(".quoteitem soitem on t.saleorder_id is not null and ti.itemid=soitem.id \n");
        sql.append("left join ").append(getCompanyId()).append(".invoiceitem invitem on t.invoiceid is not null and ti.itemid=invitem.id \n");
        sql.append("left join ").append(getCompanyId()).append(".shipping_data_items shi on t.shippingDataId is not null and ti.itemid = shi.id \n");
        sql.append("left join ").append(getCompanyId()).append(".quoteitem shitem on shi.quoteItemId = shitem.id  \n");
        sql.append("left join ").append(getCompanyId()).append(".adjustment_item adjitem on t.stockTransferId is not null and ti.itemid=adjitem.id \n");
        sql.append("left join ").append(getCompanyId()).append(".payslipTableItem pti on t.payrun_id is not null and t.payrun_id=pti.id ");
        sql.append("left join ").append(getCompanyId()).append(".payslipTable pt on pti.payslipTable_id is not null and pti.payslipTable_id=pt.id ");
        if (genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.PROJECT_IN_LINE_ITEM_ENABLE)) {
            sql.append("left join ").append(getCompanyId()).append(".project prj on coalesce(coalesce(coalesce(coalesce(coalesce(coalesce(coalesce(coalesce(coalesce(coalesce(ti.project_id,expr.projectId),payexp.projectId),spr.projectid),adjit.projectid),adjitem.projectid),mj.projectid),poitem.project_id),soitem.project_id),invitem.project_id),shitem.project_id) = prj.id \n");
        } else {
            sql.append("left join ").append(getCompanyId()).append(".project prj on coalesce(coalesce(coalesce(coalesce(coalesce(coalesce(coalesce(coalesce(ti.project_id,expr.projectId),payexp.projectId),spr.projectid),adjit.projectid),adjitem.projectid),mj.projectid),i.relatedproject_id),quo.relatedproject_id) = prj.id \n");
        }
        sql.append("WHERE t.deleted is not true ");
        sql.append("AND ti.accountid = ").append(account.getObjectID()).append(" ");

        if (departmentAndTreeChildIDs != null && !departmentAndTreeChildIDs.trim().isEmpty()) {
            sql.append(" and (");
            sql.append("ti.department_id in (").append(departmentAndTreeChildIDs).append(") \n");
            sql.append("or adjit.departmentid in (").append(departmentAndTreeChildIDs).append(") \n");
            sql.append("or adjitem.departmentid in (").append(departmentAndTreeChildIDs).append(") \n");
            sql.append("or (t.invoiceid is not null and invitem.departmentid in (").append(departmentAndTreeChildIDs).append(")) \n");
            sql.append("or (t.purchaseorder_id is not null and poitem.departmentid in (").append(departmentAndTreeChildIDs).append(")) \n");
            sql.append("or (t.saleorder_id is not null and soitem.departmentid in (").append(departmentAndTreeChildIDs).append(")) \n");
            sql.append("or (t.shippingDataId is not null and shitem.departmentid in (").append(departmentAndTreeChildIDs).append(")) \n");
            sql.append(") \n");
        }
        if (projectID != null) {
            sql.append(" AND (");
            sql.append("ti.project_id = ").append(projectID).append(" \n");
            sql.append("or (t.expensereportid is not null and expr.projectId=").append(projectID).append(") ");
            sql.append("or (t.expensePaymentId is not null and payexp.projectId=").append(projectID).append(") ");
            sql.append("or (t.banktransferid is not null and spr.projectid=").append(projectID).append(") ");
            sql.append("or (t.adjustment_id is not null and adjit.projectid=").append(projectID).append(") ");
            sql.append("or (t.stockTransferId is not null and adjitem.projectid=").append(projectID).append(") ");
            sql.append("or (pt.projectid=").append(projectID).append(") \n");
            if (genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.MANUAL_JOURNAL_PM_TO_HEAD_ENABLED)) {
                sql.append("or (t.manualjournalid is not null and mj.projectid=").append(projectID).append(") ");
            }
            if (genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.PROJECT_IN_LINE_ITEM_ENABLE)) {
                sql.append("or (t.purchaseorder_id is not null and poitem.project_id=").append(projectID).append(") ");
                sql.append("or (t.saleorder_id is not null and soitem.project_id=").append(projectID).append(") ");
                sql.append("or (t.invoiceid is not null and invitem.project_id=").append(projectID).append(") ");
                sql.append("or (t.shippingDataId is not null and shitem.project_id=").append(projectID).append(") ");
            } else {
                sql.append("or (t.invoiceid is not null and i.relatedproject_id=").append(projectID).append(") ");
                sql.append("or (t.purchaseorder_id is not null and quo.relatedproject_id=").append(projectID).append(") ");
            }
            sql.append(") ");
        } else if (!ServerUtils.hasPermission(PermissionConstants.PM_SEE_ALL_PROJECTS) && genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.MANUAL_JOURNAL_PM_TO_HEAD_ENABLED)) {
            sql.append(" and prj.managerid = ").append(((EdsUser) ServerSecurityContext.getInstance().getUser()).getObjectID()).append(" ");
        }

        if (from != null && to != null) {
            sql.append("AND to_date(to_char(t.journalDate, 'yyyy-MM-dd'),'yyyy-MM-dd') BETWEEN '").append(dateFormat.format(from)).append("' AND '").append(dateFormat.format(to)).append("' ");
        } else if (from == null && to != null) { //this will run when calculation opening balance of the account
            sql.append("AND to_date(to_char(t.journalDate, 'yyyy-MM-dd'),'yyyy-MM-dd') < '").append(dateFormat.format(to)).append("' ");
        }

        Object[] object = (Object[]) findNativeSingle(sql.toString());

        TotalDebitCredit tDC = new TotalDebitCredit();
        tDC.setAccountTypeCode(account.getAccountType().getCode());
        tDC.debit = object[0] != null ? (BigDecimal) object[0] : BigDecimal.ZERO;
        tDC.credit = object[1] != null ? (BigDecimal) object[1] : BigDecimal.ZERO;

        if ((tDC.debit != null && tDC.debit.compareTo(BigDecimal.ZERO) > 0) || (tDC.credit != null && tDC.credit.compareTo(BigDecimal.ZERO) > 0)) {
            tDC.setContainsTransaction(true);
        }

        return tDC;
    }

    @Override
    public HashMap<Integer, TotalDebitCredit> getDebitCreditTotalForPNL(String departmentAndTreeChildIDs, Integer projectID, Date from, Date to) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        StringBuilder sql = new StringBuilder();
        sql.append("SELECT a.id accountId, act.code, SUM(coalesce(ti.debit,0)) as debit, SUM(coalesce(ti.credit, 0)) as credit \n");
        sql.append("FROM ").append(getCompanyId()).append(".transactionitem ti \n");
        sql.append("JOIN ").append(getCompanyId()).append(".account a on a.id = ti.accountid \n");
        sql.append("JOIN ").append(getPublic()).append(".accounttype act on act.id = a.accountTypeId \n");
        sql.append("JOIN ").append(getCompanyId()).append(".transaction t ON t.id = ti.transactionid \n");

        sql.append("left join ").append(getCompanyId()).append(".invoice i on t.invoiceid=i.id \n");
        sql.append("left join ").append(getCompanyId()).append(".manualjournal mj on t.manualjournalid=mj.id \n");
        sql.append("left join ").append(getCompanyId()).append(".expensereport expr on t.expensereportid=expr.id \n");
        sql.append("left join ").append(getCompanyId()).append(".expensePayments expay on t.expensePaymentId=expay.id \n");
        sql.append("left join ").append(getCompanyId()).append(".expensereport payexp on expay.expenseReportId=payexp.id \n");
        sql.append("left join ").append(getCompanyId()).append(".quote quo on t.purchaseorder_id=quo.id \n");
        sql.append("left join ").append(getCompanyId()).append(".spendreceivemoney spr on t.banktransferid=spr.id \n");
        sql.append("left join ").append(getCompanyId()).append(".adjustment_item adjit on ti.stock_adjustment_item_id=adjit.id \n");

        sql.append("left join ").append(getCompanyId()).append(".quoteitem poitem on t.purchaseorder_id is not null and ti.itemid=poitem.id \n");
        sql.append("left join ").append(getCompanyId()).append(".quoteitem soitem on t.saleorder_id is not null and ti.itemid=soitem.id \n");
        sql.append("left join ").append(getCompanyId()).append(".invoiceitem invitem on t.invoiceid is not null and ti.itemid=invitem.id \n");
        sql.append("left join ").append(getCompanyId()).append(".shipping_data_items shi on t.shippingDataId is not null and ti.itemid = shi.id \n");
        sql.append("left join ").append(getCompanyId()).append(".quoteitem shitem on shi.quoteItemId = shitem.id \n");
        sql.append("left join ").append(getCompanyId()).append(".adjustment_item adjitem on t.stockTransferId is not null and ti.itemid=adjitem.id \n");
        sql.append("left join ").append(getCompanyId()).append(".payslipTableItem pti on t.payrun_id is not null and t.payrun_id=pti.id \n");
        sql.append("left join ").append(getCompanyId()).append(".payslipTable pt on pti.payslipTable_id is not null and pti.payslipTable_id=pt.id \n");
        if (genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.PROJECT_IN_LINE_ITEM_ENABLE)) {
            sql.append("left join ").append(getCompanyId()).append(".project prj on coalesce(coalesce(coalesce(coalesce(coalesce(coalesce(coalesce(coalesce(coalesce(coalesce(ti.project_id,expr.projectId),payexp.projectId),spr.projectid),adjit.projectid),adjitem.projectid),mj.projectid),poitem.project_id),soitem.project_id),invitem.project_id),shitem.project_id) = prj.id \n");
        } else {
            sql.append("left join ").append(getCompanyId()).append(".project prj on coalesce(coalesce(coalesce(coalesce(coalesce(coalesce(coalesce(coalesce(ti.project_id,expr.projectId),payexp.projectId),spr.projectid),adjit.projectid),adjitem.projectid),mj.projectid),i.relatedproject_id),quo.relatedproject_id) = prj.id \n");
        }

        sql.append("WHERE t.deleted is not true ").append(" and (act.category='").append(EdsAccountType.REVENUE).append("' or act.category ='").append(EdsAccountType.EXPENSES).append("') \n");

        if (departmentAndTreeChildIDs != null && !departmentAndTreeChildIDs.trim().isEmpty()) {
            sql.append(" and (");
            sql.append("ti.department_id in (").append(departmentAndTreeChildIDs).append(") \n");
            sql.append("or adjit.departmentid in (").append(departmentAndTreeChildIDs).append(") \n");
            sql.append("or adjitem.departmentid in (").append(departmentAndTreeChildIDs).append(") \n");
            sql.append("or (t.invoiceid is not null and invitem.departmentid in (").append(departmentAndTreeChildIDs).append(")) \n");
            sql.append("or (t.purchaseorder_id is not null and poitem.departmentid in (").append(departmentAndTreeChildIDs).append(")) \n");
            sql.append("or (t.saleorder_id is not null and soitem.departmentid in (").append(departmentAndTreeChildIDs).append(")) \n");
            sql.append("or (t.shippingDataId is not null and shitem.departmentid in (").append(departmentAndTreeChildIDs).append(")) \n");
//            sql.append("or adi.departmentid in (").append(departmentAndTreeChildIDs).append(")");
            sql.append(") \n");
        }

        if (projectID != null) {
            sql.append(" AND ( \n");
            sql.append("ti.project_id = ").append(projectID).append(" \n");
            sql.append("or (t.expensereportid is not null and expr.projectId=").append(projectID).append(") \n");
            sql.append("or (t.expensePaymentId is not null and payexp.projectId=").append(projectID).append(") \n");
            sql.append("or (t.banktransferid is not null and spr.projectid=").append(projectID).append(") \n");
            sql.append("or (t.adjustment_id is not null and adjit.projectid=").append(projectID).append(") \n");
            sql.append("or (t.stockTransferId is not null and adjitem.projectid=").append(projectID).append(") \n");
            sql.append("or (pt.projectid=").append(projectID).append(") \n");
            if (genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.MANUAL_JOURNAL_PM_TO_HEAD_ENABLED)) {
                sql.append("or (t.manualjournalid is not null and mj.projectid=").append(projectID).append(") \n");
            }
            if (genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.PROJECT_IN_LINE_ITEM_ENABLE)) {
                sql.append("or (t.purchaseorder_id is not null and poitem.project_id=").append(projectID).append(") \n");
                sql.append("or (t.saleorder_id is not null and soitem.project_id=").append(projectID).append(") \n");
                sql.append("or (t.invoiceid is not null and invitem.project_id=").append(projectID).append(") \n");
                sql.append("or (t.shippingDataId is not null and shitem.project_id=").append(projectID).append(") \n");
            } else {
                sql.append("or (t.invoiceid is not null and i.relatedproject_id=").append(projectID).append(") \n");
                sql.append("or (t.purchaseorder_id is not null and quo.relatedproject_id=").append(projectID).append(") \n");
            }
            sql.append(") \n");
        } else if (!ServerUtils.hasPermission(PermissionConstants.PM_SEE_ALL_PROJECTS) && genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.MANUAL_JOURNAL_PM_TO_HEAD_ENABLED)) {
            sql.append(" and prj.managerid = ").append(((EdsUser) ServerSecurityContext.getInstance().getUser()).getObjectID()).append(" ");
        }

        if (from != null && to != null) {
            sql.append("AND to_date(to_char(t.journalDate, 'yyyy-MM-dd'),'yyyy-MM-dd') BETWEEN '").append(dateFormat.format(from)).append("' AND '").append(dateFormat.format(to)).append("' \n");
        } else if (from == null && to != null) { //this will run when calculation opening balance of the account
            sql.append("AND to_date(to_char(t.journalDate, 'yyyy-MM-dd'),'yyyy-MM-dd') < '").append(dateFormat.format(to)).append("' \n");
        }

        sql.append("GROUP BY a.id, act.code \n");

        List<Object[]> list = (List<Object[]>) findNative(sql.toString());

        HashMap<Integer, TotalDebitCredit> map = new HashMap<>();
        for (Object[] object : list) {

            TotalDebitCredit tDC = new TotalDebitCredit();
            tDC.setAccountTypeCode((String) object[1]);
            tDC.debit = object[2] != null ? (BigDecimal) object[2] : BigDecimal.ZERO;
            tDC.credit = object[3] != null ? (BigDecimal) object[3] : BigDecimal.ZERO;

            if ((tDC.debit != null && tDC.debit.compareTo(BigDecimal.ZERO) > 0) || (tDC.credit != null && tDC.credit.compareTo(BigDecimal.ZERO) > 0)) {
                tDC.setContainsTransaction(true);
            }
            map.put((Integer) object[0], tDC);
        }


        return map;
    }

    public HashMap<Integer, TotalDebitCredit> getForeignAccountsDebitCreditTotal(String departmentAndTreeChildIDs, Integer projectID, Date from, Date to, Integer accountID) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        StringBuilder sql = new StringBuilder();
        sql.append("SELECT a.id accountid, at.code, max(a.currencyid) currencyid, \n")
                .append("SUM(CASE WHEN ti.foreignDebit is not null THEN ti.foreignDebit \n")
                .append("         WHEN t.currencyid is not null and t.exchangerate is not null THEN round(coalesce(ti.debit, 0) * t.exchangerate, 2) \n")
                .append("         ELSE coalesce(ti.debit,0) END) as debit, \n")
                .append("SUM(CASE WHEN ti.foreignCredit is not null THEN ti.foreignCredit \n")
                .append("         WHEN t.currencyid is not null and t.exchangerate is not null THEN round(coalesce(ti.credit, 0) * t.exchangerate, 2) \n")
                .append("         ELSE coalesce(ti.credit,0) END) as credit, \n")
                .append("SUM(coalesce(ti.debit,0)) debitInBase, \n")
                .append("SUM(coalesce(ti.credit,0)) creditInBase \n");
        sql.append("FROM ").append(getCompanyId()).append(".transactionitem ti \n");
        sql.append("JOIN ").append(getCompanyId()).append(".transaction t ON t.id = ti.transactionid \n");
        sql.append("JOIN ").append(getCompanyId()).append(".account a ON a.id = ti.accountid \n");
        sql.append("INNER JOIN ").append(getPublic()).append(".accounttype at ON at.id = a.accounttypeid \n");

        sql.append("left join ").append(getCompanyId()).append(".invoice i on t.invoiceid=i.id \n");
        sql.append("left join ").append(getCompanyId()).append(".manualjournal mj on t.manualjournalid=mj.id \n");
        sql.append("left join ").append(getCompanyId()).append(".expensereport expr on t.expensereportid=expr.id \n");
        sql.append("left join ").append(getCompanyId()).append(".expensePayments expay on t.expensePaymentId=expay.id  \n");
        sql.append("left join ").append(getCompanyId()).append(".expensereport payexp on expay.expenseReportId=payexp.id  \n");
        sql.append("left join ").append(getCompanyId()).append(".quote quo on t.purchaseorder_id=quo.id \n");
        sql.append("left join ").append(getCompanyId()).append(".spendreceivemoney spr on t.banktransferid=spr.id \n");
        sql.append("left join ").append(getCompanyId()).append(".adjustment_item adjit on ti.stock_adjustment_item_id=adjit.id \n");

        sql.append("left join ").append(getCompanyId()).append(".quoteitem poitem on t.purchaseorder_id is not null and ti.itemid=poitem.id \n");
        sql.append("left join ").append(getCompanyId()).append(".quoteitem soitem on t.saleorder_id is not null and ti.itemid=soitem.id \n");
        sql.append("left join ").append(getCompanyId()).append(".invoiceitem invitem on t.invoiceid is not null and ti.itemid=invitem.id \n");
        sql.append("left join ").append(getCompanyId()).append(".shipping_data_items shi on t.shippingDataId is not null and ti.itemid = shi.id \n");
        sql.append("left join ").append(getCompanyId()).append(".quoteitem shitem on shi.quoteItemId = shitem.id \n");
        sql.append("left join ").append(getCompanyId()).append(".adjustment_item adjitem on t.stockTransferId is not null and ti.itemid=adjitem.id \n");
        sql.append("left join ").append(getCompanyId()).append(".payslipTableItem pti on t.payrun_id is not null and t.payrun_id=pti.id ");
        sql.append("left join ").append(getCompanyId()).append(".payslipTable pt on pti.payslipTable_id is not null and pti.payslipTable_id=pt.id ");
        if (genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.PROJECT_IN_LINE_ITEM_ENABLE)) {
            sql.append("left join ").append(getCompanyId()).append(".project prj on coalesce(coalesce(coalesce(coalesce(coalesce(coalesce(coalesce(coalesce(coalesce(coalesce(ti.project_id,expr.projectId),payexp.projectId),spr.projectid),adjit.projectid),adjitem.projectid),mj.projectid),poitem.project_id),soitem.project_id),invitem.project_id),shitem.project_id) = prj.id \n");
        } else {
            sql.append("left join ").append(getCompanyId()).append(".project prj on coalesce(coalesce(coalesce(coalesce(coalesce(coalesce(coalesce(coalesce(ti.project_id,expr.projectId),payexp.projectId),spr.projectid),adjit.projectid),adjitem.projectid),mj.projectid),i.relatedproject_id),quo.relatedproject_id) = prj.id \n");
        }

        sql.append("WHERE t.deleted is not true and a.foreignAccount is true \n");

        if (departmentAndTreeChildIDs != null && !departmentAndTreeChildIDs.trim().isEmpty()) {
            sql.append(" and (");
            sql.append("ti.department_id in (").append(departmentAndTreeChildIDs).append(") \n");
            sql.append("or adjit.departmentid in (").append(departmentAndTreeChildIDs).append(") \n");
            sql.append("or adjitem.departmentid in (").append(departmentAndTreeChildIDs).append(") \n");
            sql.append("or (t.invoiceid is not null and invitem.departmentid in (").append(departmentAndTreeChildIDs).append(")) \n");
            sql.append("or (t.purchaseorder_id is not null and poitem.departmentid in (").append(departmentAndTreeChildIDs).append(")) \n");
            sql.append("or (t.saleorder_id is not null and soitem.departmentid in (").append(departmentAndTreeChildIDs).append(")) \n");
            sql.append("or (t.shippingDataId is not null and shitem.departmentid in (").append(departmentAndTreeChildIDs).append(")) \n");
            sql.append(") \n");
        }
        if (projectID != null) {
            sql.append(" AND (");
            sql.append("ti.project_id = ").append(projectID).append(" \n");
            sql.append("or (t.expensereportid is not null and expr.projectId=").append(projectID).append(") \n");
            sql.append("or (t.expensePaymentId is not null and payexp.projectId=").append(projectID).append(") \n");
            sql.append("or (t.banktransferid is not null and spr.projectid=").append(projectID).append(") \n");
            sql.append("or (t.adjustment_id is not null and adjit.projectid=").append(projectID).append(") \n");
            sql.append("or (t.stockTransferId is not null and adjitem.projectid=").append(projectID).append(") ");
            sql.append("or (pt.projectid=").append(projectID).append(") \n");
            if (genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.MANUAL_JOURNAL_PM_TO_HEAD_ENABLED)) {
                sql.append("or (t.manualjournalid is not null and mj.projectid=").append(projectID).append(") ");
            }
            if (genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.PROJECT_IN_LINE_ITEM_ENABLE)) {
                sql.append("or (t.purchaseorder_id is not null and poitem.project_id=").append(projectID).append(") ");
                sql.append("or (t.saleorder_id is not null and soitem.project_id=").append(projectID).append(") ");
                sql.append("or (t.invoiceid is not null and invitem.project_id=").append(projectID).append(") ");
                sql.append("or (t.shippingDataId is not null and shitem.project_id=").append(projectID).append(") ");
            } else {
                sql.append("or (t.invoiceid is not null and i.relatedproject_id=").append(projectID).append(") \n");
                sql.append("or (t.purchaseorder_id is not null and quo.relatedproject_id=").append(projectID).append(") \n");
            }
            sql.append(") \n");
        } else if (!ServerUtils.hasPermission(PermissionConstants.PM_SEE_ALL_PROJECTS) && genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.MANUAL_JOURNAL_PM_TO_HEAD_ENABLED)) {
            sql.append(" and prj.managerid = ").append(((EdsUser) ServerSecurityContext.getInstance().getUser()).getObjectID()).append(" ");
        }

        if (from != null && to != null) {
            sql.append("AND to_date(to_char(t.journalDate, 'yyyy-MM-dd'),'yyyy-MM-dd') BETWEEN '").append(dateFormat.format(from)).append("' AND '").append(dateFormat.format(to)).append("' \n");
        } else if (from == null && to != null) { //this will run when calculation opening balance of the account
            sql.append("AND to_date(to_char(t.journalDate, 'yyyy-MM-dd'),'yyyy-MM-dd') < '").append(dateFormat.format(to)).append("' \n");
        }

        if (accountID != null) {
            sql.append("AND a.id = ").append(accountID).append(" \n");
        }

        sql.append("GROUP BY a.id, at.code ");

        List<Object[]> objects = findNative(sql.toString());

        HashMap<Integer, TotalDebitCredit> map = new HashMap<>();

        if (objects != null && !objects.isEmpty()) {
            for (Object[] object : objects) {
                TotalDebitCredit tDC = new TotalDebitCredit();
                tDC.setAccountID((Integer) object[0]);
                tDC.setAccountTypeCode((String) object[1]);
                tDC.setCurrencyID((Integer) object[2]);
                tDC.debit = object[3] != null ? (BigDecimal) object[3] : BigDecimal.ZERO;
                tDC.credit = object[4] != null ? (BigDecimal) object[4] : BigDecimal.ZERO;
                tDC.debitInBase = object[5] != null ? (BigDecimal) object[5] : BigDecimal.ZERO;
                tDC.creditInBase = object[6] != null ? (BigDecimal) object[6] : BigDecimal.ZERO;

                if ((tDC.debit != null && tDC.debit.intValue() > 0) || (tDC.credit != null && tDC.credit.intValue() > 0)) {
                    tDC.setContainsTransaction(true);
                }

                map.put(tDC.getAccountID(), tDC);
            }
        }

        return map;
    }

    @Override
    public HashMap<Integer, TotalDebitCredit> getAllAccountsDebitCredit(String departmentAndTreeChildIDs, Date from, Date to) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        StringBuilder sql = new StringBuilder();
        sql.append("SELECT a.id accountid, at.code, \n")
                .append("SUM(coalesce(ti.debit,0)) debit, \n")
                .append("SUM(coalesce(ti.credit,0)) credit \n");
        sql.append("FROM ").append(getCompanyId()).append(".transactionitem ti \n");
        sql.append("JOIN ").append(getCompanyId()).append(".transaction t ON t.id = ti.transactionid \n");
        sql.append("JOIN ").append(getCompanyId()).append(".account a ON a.id = ti.accountid \n");
        sql.append("INNER JOIN ").append(getPublic()).append(".accounttype at ON at.id = a.accounttypeid \n");
        sql.append("WHERE t.deleted is not true and a.foreignAccount is not true ");
        if (departmentAndTreeChildIDs != null && !departmentAndTreeChildIDs.trim().isEmpty()) {
            sql.append(" AND ti.department_id in (").append(departmentAndTreeChildIDs).append(") ");
        }

        if (from != null && to != null) {
            sql.append("AND to_date(to_char(t.journalDate, 'yyyy-MM-dd'),'yyyy-MM-dd') BETWEEN '").append(dateFormat.format(from)).append("' AND '").append(dateFormat.format(to)).append("' ");
        } else if (from == null && to != null) { //this will run when calculation opening balance of the account
            sql.append("AND to_date(to_char(t.journalDate, 'yyyy-MM-dd'),'yyyy-MM-dd') < '").append(dateFormat.format(to)).append("' ");
        }

        sql.append("GROUP BY a.id, at.code ");

        List<Object[]> objects = findNative(sql.toString());

        HashMap<Integer, TotalDebitCredit> map = new HashMap<>();

        if (objects != null && !objects.isEmpty()) {
            for (Object[] object : objects) {
                TotalDebitCredit tDC = new TotalDebitCredit();
                tDC.setAccountID((Integer) object[0]);
                tDC.setAccountTypeCode((String) object[1]);
                tDC.debit = object[2] != null ? (BigDecimal) object[2] : BigDecimal.ZERO;
                tDC.credit = object[3] != null ? (BigDecimal) object[3] : BigDecimal.ZERO;

                if ((tDC.debit != null && tDC.debit.intValue() > 0) || (tDC.credit != null && tDC.credit.intValue() > 0)) {
                    tDC.setContainsTransaction(true);
                }

                map.put(tDC.getAccountID(), tDC);
            }
        }

        return map;
    }

    @Override
    public HashMap<Integer, TotalDebitCredit> getSubsidiariesDebitCreditTotal(Date from, Date to, Integer currencyID) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        StringBuilder sql = new StringBuilder();
        sql.append("SELECT aid, (debit - interCompanyDebit) debit, (credit - interCompanyCredit) credit, interCompanyDebit, interCompanyCredit FROM ");
        sql.append("(SELECT \n")
                .append("a.id aid, \n")
                .append("sum(coalesce(ti.debit, 0)/(case when er.exchangerate is not null and er.exchangerate != 0 then er.exchangerate else 1 end)) debit, \n")
                .append("sum(coalesce(ti.credit, 0)/(case when er.exchangerate is not null and er.exchangerate != 0 then er.exchangerate else 1 end)) credit, \n")
                .append("sum(case when  \n")
                .append("      a.key is not null and \n").append("      a.key in (" + ACCOUNTS_RECEIVABLE_KEY + ", " + ACCOUNTS_PAYABLE_KEY + ") and \n")
                .append("      c.id is not null and c.subsidiary is not null \n")
                .append(" then coalesce(ti.debit, 0)/(case when er.exchangerate is not null and er.exchangerate != 0 then er.exchangerate else 1 end) else 0 end) interCompanyDebit,  \n")
                .append("sum(case when  \n")
                .append("      a.key is not null and \n").append("      a.key in (" + ACCOUNTS_RECEIVABLE_KEY + ", " + ACCOUNTS_PAYABLE_KEY + ") and \n")
                .append("      c.id is not null and c.subsidiary is not null \n")
                .append(" then coalesce(ti.credit, 0)/(case when er.exchangerate is not null and er.exchangerate != 0 then er.exchangerate else 1 end) else 0 end) interCompanyCredit  \n");
        sql.append("FROM ").append(getCompanyId()).append(".account a \n");
        sql.append("INNER JOIN ").append(getCompanyId()).append(".transactionitem ti ON ti.accountid = a.id \n");
        sql.append("INNER JOIN ").append(getCompanyId()).append(".transaction t ON t.id = ti.transactionid \n");
        sql.append("LEFT JOIN (SELECT currencyid, date, max(exchangerate) exchangerate FROM ").append(getCompanyId()).append(".exchangerate GROUP BY currencyid, date) er ON to_date(to_char(er.date,'yyyy-MM-dd'),'yyyy-MM-dd') = to_date(to_char(t.journaldate,'yyyy-MM-dd'),'yyyy-MM-dd') and er.currencyid = ").append(currencyID).append(" \n");
        sql.append("LEFT JOIN ").append(getCompanyId()).append(".crmaccount c ON (c.id = ti.crmaccount_id or c.id = t.clientid or c.id = t.supplierid) \n");
        sql.append("WHERE t.deleted is not true ");

        if (from != null && to != null) {
            sql.append("AND to_date(to_char(t.journalDate,'yyyy-MM-dd'),'yyyy-MM-dd') BETWEEN '").append(dateFormat.format(from)).append("' AND '").append(dateFormat.format(to)).append("' ");
        } else if (from != null && to == null) { //this will run when calculation opening balance of the account
            sql.append("AND to_date(to_char(t.journalDate,'yyyy-MM-dd'),'yyyy-MM-dd') < '").append(dateFormat.format(from)).append("' ");
        }

        sql.append("GROUP BY a.id) t ");

        List<Object[]> objects = findNative(sql.toString());

        HashMap<Integer, TotalDebitCredit> map = new HashMap<>();

        if (objects != null && !objects.isEmpty()) {
            for (Object[] object : objects) {
                TotalDebitCredit tDC = new TotalDebitCredit();
                tDC.setAccountID((Integer) object[0]);
                tDC.debit = object[1] != null ? (BigDecimal) object[1] : BigDecimal.ZERO;
                tDC.credit = object[2] != null ? (BigDecimal) object[2] : BigDecimal.ZERO;
                tDC.interCompanyDebit = object[3] != null ? (BigDecimal) object[3] : BigDecimal.ZERO;
                tDC.interCompanyCredit = object[4] != null ? (BigDecimal) object[4] : BigDecimal.ZERO;

                if ((tDC.debit != null && tDC.debit.intValue() > 0) || (tDC.credit != null && tDC.credit.intValue() > 0)) {
                    tDC.setContainsTransaction(true);
                }

                map.put(tDC.getAccountID(), tDC);
            }
        }

        return map;
    }

    /**
     * This method is working for only Lenzo Customization
     *
     * @param from
     * @param to
     * @return
     */
    public LinkedHashMap<String, TrialBalanceItem> getPRAccountClientSupplierBalance(Date from, Date to) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        StringBuilder sql = new StringBuilder();
        sql.append("SELECT a.id, ca.id crmaccount_id, ca.number, ca.name, sum(debit) debit, sum(credit) credit FROM ");
        sql.append("(SELECT a.id, coalesce(ti.crmaccount_id, coalesce(t.clientid, t.supplierid)) crmaccount_id, coalesce(ti.debit, 0) debit, coalesce(ti.credit,0) credit FROM ").append(getCompanyId()).append(".transactionitem ti \n");
        sql.append("INNER JOIN ").append(getCompanyId()).append(".account a on a.id = ti.accountid \n");
        sql.append("INNER JOIN ").append(getCompanyId()).append(".transaction t on t.id = ti.transactionid \n");
        sql.append("WHERE (ti.crmaccount_id is not null or t.clientid is not null) and (a.key = " + EdsAccount.ACCOUNTS_RECEIVABLE + " or a.key = " + EdsAccount.ACCOUNTS_PAYABLE + ") \n");

        if (from != null && to != null) {
            sql.append("AND t.journalDate BETWEEN '").append(dateFormat.format(from)).append("' AND '").append(dateFormat.format(to)).append("' ");
        } else if (from != null && to == null) { //this will run when calculation opening balance of the account
            sql.append("AND t.journalDate < '").append(dateFormat.format(from)).append("' ");
        }
        sql.append("AND t.deleted is not true) a \n");
        sql.append("INNER JOIN ").append(getCompanyId()).append(".crmaccount ca on ca.id = a.crmaccount_id \n");
        sql.append("GROUP BY a.id, ca.id \n");
        sql.append("ORDER BY a.id, ca.name \n");

        List<Object[]> objects = findNative(sql.toString());

        LinkedHashMap<String, TrialBalanceItem> map = new LinkedHashMap<>();

        if (objects != null && !objects.isEmpty()) {
            for (Object[] object : objects) {
                TrialBalanceItem item = new TrialBalanceItem();
                item.setAccountId((Integer) object[0]);
                item.setParentId((Integer) object[1]);
                String[] number = ((String) object[2]).split("_");
                item.setCode(number.length > 1 ? number[1] : number[0]);
                item.setName((String) object[3]);
                item.setDebit(object[4] != null ? (BigDecimal) object[4] : BigDecimal.ZERO);
                item.setCredit(object[5] != null ? (BigDecimal) object[5] : BigDecimal.ZERO);

                map.put(item.getAccountId() + "_" + item.getParentId(), item);
            }
        }

        return map;
    }

    public HashMap<Integer, TotalDebitCredit> getSubsidiariesAllAccountsDebitCredit(Date from, Date to, Integer currencyID) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        StringBuilder sql = new StringBuilder();
        sql.append("SELECT aid, (debit - interCompanyDebit) debit, (credit - interCompanyCredit) credit, interCompanyDebit, interCompanyCredit FROM ");
        sql.append("(SELECT \n")
                .append("a.id aid, \n")
                .append("sum(coalesce(ti.debit, 0)/(case when er.exchangerate is not null and er.exchangerate != 0 then er.exchangerate else 1 end)) debit, \n")
                .append("sum(coalesce(ti.credit, 0)/(case when er.exchangerate is not null and er.exchangerate != 0 then er.exchangerate else 1 end)) credit, \n")
                .append("sum(case when  \n")
                .append("      a.key is not null and \n").append("      a.key in (" + ACCOUNTS_RECEIVABLE_KEY + ", " + ACCOUNTS_PAYABLE_KEY + ") and \n")
                .append("      c.id is not null and c.subsidiary is not null \n")
                .append(" then coalesce(ti.debit, 0)/(case when er.exchangerate is not null and er.exchangerate != 0 then er.exchangerate else 1 end) else 0 end) interCompanyDebit,  \n")
                .append("sum(case when  \n")
                .append("      a.key is not null and \n").append("      a.key in (" + ACCOUNTS_RECEIVABLE_KEY + ", " + ACCOUNTS_PAYABLE_KEY + ") and \n")
                .append("      c.id is not null and c.subsidiary is not null \n")
                .append(" then coalesce(ti.credit, 0)/(case when er.exchangerate is not null and er.exchangerate != 0 then er.exchangerate else 1 end) else 0 end) interCompanyCredit  \n");
        sql.append("FROM ").append(getCompanyId()).append(".account a \n");
        sql.append("LEFT JOIN ").append(getCompanyId()).append(".transactionitem ti ON ti.accountid = a.id \n");
        sql.append("INNER JOIN ").append(getCompanyId()).append(".transaction t ON t.id = ti.transactionid \n");
        sql.append("LEFT JOIN (SELECT currencyid, date, max(exchangerate) exchangerate FROM ").append(getCompanyId()).append(".exchangerate GROUP BY currencyid, date) er ON to_date(to_char(er.date,'yyyy-MM-dd'),'yyyy-MM-dd') = to_date(to_char(t.journaldate,'yyyy-MM-dd'),'yyyy-MM-dd') and er.currencyid = ").append(currencyID).append(" \n");
        sql.append("LEFT JOIN ").append(getCompanyId()).append(".crmaccount c ON (c.id = ti.crmaccount_id or c.id = t.clientid or c.id = t.supplierid) \n");
        sql.append("WHERE t.deleted is not true ");

        if (from != null && to != null) {
            sql.append("AND to_date(to_char(t.journalDate,'yyyy-MM-dd'),'yyyy-MM-dd') BETWEEN '").append(dateFormat.format(from)).append("' AND '").append(dateFormat.format(to)).append("' ");
        } else if (from != null && to == null) { //this will run when calculation opening balance of the account
            sql.append("AND to_date(to_char(t.journalDate,'yyyy-MM-dd'),'yyyy-MM-dd') < '").append(dateFormat.format(from)).append("' ");
        }

        sql.append("GROUP BY a.id) t ");

        List<Object[]> objects = findNative(sql.toString());

        HashMap<Integer, TotalDebitCredit> map = new HashMap<>();

        if (objects != null && !objects.isEmpty()) {
            for (Object[] object : objects) {
                TotalDebitCredit tDC = new TotalDebitCredit();
                tDC.setAccountID((Integer) object[0]);
                tDC.debit = object[1] != null ? (BigDecimal) object[1] : BigDecimal.ZERO;
                tDC.credit = object[2] != null ? (BigDecimal) object[2] : BigDecimal.ZERO;
                tDC.interCompanyDebit = object[3] != null ? (BigDecimal) object[3] : BigDecimal.ZERO;
                tDC.interCompanyCredit = object[4] != null ? (BigDecimal) object[4] : BigDecimal.ZERO;

                if ((tDC.debit != null && tDC.debit.intValue() > 0) || (tDC.credit != null && tDC.credit.intValue() > 0)) {
                    tDC.setContainsTransaction(true);
                }

                map.put(tDC.getAccountID(), tDC);
            }
        }

        return map;
    }

    public LinkedHashMap<Integer, ArrayList<AgingSummaryInvoiceItem>> getClientSupplierBalanceForAging(ListingFilterParameter filter) { // TODO put interval into filter
        //String type, Date date, Integer interval, Integer clientID, boolean isExcludePrePayments
        EdsFinancialSettings fs = financialSettingsManager.getFinancialSettings();
        String currentExRates = getCurrentExRates(fs, filter);
        StringBuilder sql = new StringBuilder();
        sql.append("select * from (SELECT coalesce(clientOrSupplierId,0) clientOrSupplierId, date, due_date,accountType, invoiceNumber, clientOrSupplierName as customerOrSupplierName, (case when aging <= 0 then 0 else (case when aging%").append(filter.getInterval()).append(" = 0 then aging else aging + ").append(filter.getInterval()).append(" - aging%").append(filter.getInterval()).append(" end) end) aging, sum(amount) amount FROM ");
        sql.append("(").append(RECEIVABLE.equals(filter.getAccountType()) ? getReceivableAgingQuery(currentExRates, filter.getDate(), filter.isExcludePrePayments(), fs) : getPayableAgingQuery(currentExRates, filter.getDate(), filter.isExcludePrePayments(), fs)).append(") t WHERE 1 = 1 ");

        if (filter.getClientId() != null) {
            sql.append("AND t.clientOrSupplierId = '").append(filter.getClientId()).append("' ");
        }
        sql.append("GROUP BY date, due_date, invoiceNumber, clientOrSupplierId, accountType, clientOrSupplierName, (case when aging <= 0 then 0 else (case when aging%").append(filter.getInterval()).append(" = 0 then aging else aging + ").append(filter.getInterval()).append(" - aging%").append(filter.getInterval()).append(" end) end)) t ");
        sql.append(" where round(amount," + fs.getCalculationScale() + ") != 0 ");
        sql.append("ORDER BY customerOrSupplierName, aging ");

        List<AgingSummaryInvoiceItem> list = jdbcSpringManager.getSimpleJdbcTemplate().query(sql.toString(), BeanPropertyRowMapper.newInstance(AgingSummaryInvoiceItem.class));

        LinkedHashMap<Integer, ArrayList<AgingSummaryInvoiceItem>> map = new LinkedHashMap<>();
        for (AgingSummaryInvoiceItem item : list) {

            if (map.get(item.getClientOrSupplierId()) == null) {
                ArrayList<AgingSummaryInvoiceItem> items = new ArrayList<>();
                items.add(item);
                map.put(item.getClientOrSupplierId(), items);
            } else {
                map.get(item.getClientOrSupplierId()).add(item);
            }
        }
        return map;
    }

    private String getCurrentExRates(EdsFinancialSettings fs, ListingFilterParameter fp) {
        StringBuilder s = new StringBuilder("(Case ");
        ArrayList<Integer> currencyIDs = new ArrayList<>();
        Map<Integer, EdsAccount> accountMap = RECEIVABLE.equals(fp.getAccountType()) ? accountingManager.getAccountsMapByKey(EdsAccount.ACCOUNTS_RECEIVABLE) : accountingManager.getAccountsMapByKey(EdsAccount.ACCOUNTS_PAYABLE);
        for (EdsAccount account : accountMap.values()) {
            if (account.getCurrency() != null && !currencyIDs.contains(account.getCurrency().getObjectID())) {
                currencyIDs.add(account.getCurrency().getObjectID());
                CurrencyListItem item = currencyServiceLocal.getCurrencyRateByDate(account.getCurrency().getObjectID(), new DateNonConvertable(fp.getDate()));
                BigDecimal exchangeRate = BigDecimal.valueOf(item.getExchangeRate()).setScale(fs.getExchangeRateScale(), RoundingMode.HALF_UP);
                s.append("WHEN a.currencyid = ").append(account.getCurrency().getObjectID()).append(" THEN ").append(exchangeRate).append(" ");
            }
        }
        s.append("ELSE 1 END) ");
        return s.toString();
    }

    @Override
    public String getOwnerName(Integer clientOrSupplierId) {
        String sql = "select (coalesce(muser.firstname,'')||' '||coalesce(muser.lastname,'')) as ownername from " + getCompanyId() + " .crmaccount cr " +
                "LEFT JOIN  " + getCompanyId() + " .crmaccount_owners cro on cro.crmaccount_id=cr.id " +
                "LEFT JOIN  " + getCompanyId() + " .myuser muser on muser.id=cro.owner_id " +
                " where cro.crmaccount_id=" + clientOrSupplierId;
        List<String> ownerList = findNative(sql);
        StringBuilder owners = new StringBuilder();
        if (ownerList != null && !ownerList.isEmpty()) {
            for (String owner : ownerList) {
                owners.append(owner).append(", ");
            }
        }
        return owners.toString().replaceAll(", $", "");
    }

    public LinkedHashMap<Integer, ArrayList<AgingSummaryInvoiceItem>> getClientSupplierBalanceForAgingDetails(ListingFilterParameter filter) {
//        parameter.getAccountType(), date.getNonConvertedDate(), parameter.getType(), parameter.getClientId(), parameter.isExcludePrePayments()
        EdsFinancialSettings fs = financialSettingsManager.getFinancialSettings();
        String currencyExRates = getCurrentExRates(fs, filter);
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT coalesce(clientOrSupplierId,0) clientOrSupplierId, clientOrSupplierName as customerOrSupplierName, typeName, objectID, invoiceNumber, due_date, date, (case when aging <= 0 then 0 else aging  end) aging, amount,journalId,exchangeRates,currencyName,currencyDifference FROM ");
        sql.append("(").append(RECEIVABLE.equals(filter.getAccountType()) ? getReceivableAgingQuery(currencyExRates, filter.getDate(), filter.isExcludePrePayments(), fs) : getPayableAgingQuery(currencyExRates, filter.getDate(), filter.isExcludePrePayments(), fs)).append(") t \n");
        sql.append(" where round(amount," + fs.getCalculationScale() + ") != 0 ");

        if (filter.getClientId() != null) {
            sql.append("AND t.clientOrSupplierId = '").append(filter.getClientId()).append("' ");
        }
        sql.append("ORDER BY customerOrSupplierName, aging, date ");

        List<AgingSummaryInvoiceItem> list = jdbcSpringManager.getSimpleJdbcTemplate().query(sql.toString(), BeanPropertyRowMapper.newInstance(AgingSummaryInvoiceItem.class));

        LinkedHashMap<Integer, ArrayList<AgingSummaryInvoiceItem>> map = new LinkedHashMap<>();
        for (AgingSummaryInvoiceItem item : list) {
            map.computeIfAbsent(item.getClientOrSupplierId(), l -> new ArrayList<>());
            map.get(item.getClientOrSupplierId()).add(item);
        }
        return map;
    }

    @Override
    public boolean isProductUpcNumberExists(String upcNumber, Integer productID) {
        if (productID != null) {
            return !find("select p from EdsItem p where (p.deleted = false or p.deleted is null) and p.upcNumber = ? and p.objectID != ?", upcNumber.trim(), productID).isEmpty();
        } else {
            return !find("select p from EdsItem p where (p.deleted = false or p.deleted is null) and p.upcNumber = ?", upcNumber.trim()).isEmpty();
        }
    }

    private String getReceivableAgingQuery(String currentExRates, Date date, boolean isExcludePrePayments, EdsFinancialSettings fs) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        StringBuilder sql = new StringBuilder();
        sql.append("SELECT * FROM ( \n");

        //Invoice/Credit Note Transaction
        sql.append("SELECT clientOrSupplierId, clientOrSupplierName, typeName, accountType, invoiceNumber, objectID, sum(amount) amount, due_date, min(date) date, (to_date('").append(dateFormat.format(date)).append("','yyyy-MM-dd') - due_date) aging,NULL::integer[] AS journalId,  NULL::numeric[] as exchangeRates, '' AS currencyName, NULL::numeric[] AS currencyDifference  FROM (").append("\n");
        sql.append("SELECT c.id clientOrSupplierId,").append("\n");
        sql.append("       c.name clientOrSupplierName,").append("\n");
        sql.append("       (CASE WHEN pinv.id is not null THEN (CASE WHEN pinv.iscreditnote THEN 'Credit Note' ELSE 'Invoice' END) \n")
                .append("       WHEN finv.id is not null THEN 'Invoice' ELSE \n")
                .append("       (CASE WHEN inv.iscreditnote THEN 'Credit Note' ELSE 'Invoice' END) \n")
                .append("  END) as typeName, \n");
        sql.append("       'CUSTOMER' as accountType, ").append("\n");
        sql.append("       coalesce(inv.number, coalesce(pinv.number, finv.number)) as invoiceNumber,").append("\n");
        sql.append("       coalesce(inv.id, coalesce(pinv.id, finv.id)) as objectID, ").append("\n");
        sql.append("       (case when ip.id is not null and pinv.iscreditnote is not true and t.reversalid is null then 0 else  coalesce(ti.debit, 0) end) -  (case when ip.id is not null and pinv.iscreditnote then 0 else coalesce(ti.credit,0) end) amount, \n");
        sql.append("       to_date(to_char(coalesce(inv.duedate, coalesce(pinv.duedate, finv.duedate)),'yyyy-MM-dd'),'yyyy-MM-dd') due_date, ").append("\n");
        sql.append("       to_date(to_char(t.journaldate,'yyyy-MM-dd'),'yyyy-MM-dd') date, ").append("\n");
        sql.append("       NULL::integer[]  as journalId, ").append("\n");
        sql.append("       NULL::numeric[] as exchangeRates, ").append("\n");
        sql.append("       '' AS currencyName, ").append("\n");
        sql.append("       NULL::numeric[] AS currencyDifference ").append("\n");
        sql.append("FROM ").append(getCompanyId()).append(".transactionitem ti ").append("\n");
        sql.append("INNER JOIN ").append(getCompanyId()).append(".account a on a.id = ti.accountid").append("\n");
        sql.append("INNER JOIN ").append(getCompanyId()).append(".transaction t on t.id = ti.transactionid ").append("\n");

        sql.append("LEFT JOIN ").append(getCompanyId()).append(".invoice inv on inv.id = t.invoiceid ").append("\n");
        sql.append("LEFT JOIN ").append(getCompanyId()).append(".reference invs on invs.id = inv.status_id ").append("\n");

        sql.append("LEFT JOIN ").append(getCompanyId()).append(".invoicepayments ip on ip.id = t.invoicepaymentid ").append("\n");
        sql.append("LEFT JOIN ").append(getCompanyId()).append(".invoice pinv on (pinv.id = ip.invoiceid or pinv.id = ip.creditnoteid) ").append("\n");
        sql.append("LEFT JOIN ").append(getCompanyId()).append(".reference ips on ips.id = ip.statusid ").append("\n");

        sql.append("LEFT JOIN ").append(getCompanyId()).append(".fixedasset fa on fa.id = t.fixedassetid ").append("\n");
        sql.append("LEFT JOIN ").append(getCompanyId()).append(".invoice finv on finv.id = fa.salesinvoiceid \n");
        sql.append("LEFT JOIN ").append(getCompanyId()).append(".reference finvs on finvs.id = finv.status_id ").append("\n");

        sql.append("LEFT JOIN ").append(getCompanyId()).append(".crmaccount c on (c.id = t.clientid or c.id = ti.crmaccount_id) ").append("\n");
        sql.append("WHERE t.deleted is not true and (").append("\n");

        //invoice where clause
        sql.append("(t.invoiceid is not null AND inv.deleted is not true ) OR ").append("\n");

        //invoice payment where clause
        sql.append("(t.invoicepaymentid is not null ");
//        sql.append(" AND (ips.id is null or ips.code != '").append(EdsInvoicePayment.REVERSED).append("') \n");
        sql.append(" AND (ip.invoiceid is not null OR ip.creditnoteid is not null)) OR \n");

        //fixed asset where clause
        sql.append("(t.fixedassetid is not null AND fa.salesinvoiceid is not null AND finvs.code in ('APPROVE', 'OPEN', 'OVER_DUE', 'PAID')) \n");

        //close OR operation
        sql.append(") \n");

        sql.append("AND (a.key in (" + EdsAccount.ACCOUNTS_RECEIVABLE + "," + EdsAccount.UNEARNED_REVENUE + ") or a.groupKey in (" + EdsAccount.ACCOUNTS_RECEIVABLE + "," + EdsAccount.UNEARNED_REVENUE + ") ) \n");
        sql.append("AND to_date(to_char(t.journaldate,'yyyy-MM-dd'),'yyyy-MM-dd') <= '").append(dateFormat.format(date)).append("' ").append("\n");
        sql.append(") inv \n");
        sql.append("GROUP BY clientOrSupplierId, clientOrSupplierName,accountType, objectID, invoiceNumber, typeName, due_date \n");

        sql.append("UNION ALL ").append("\n");

        if (!isExcludePrePayments) {
            //Pre Payment Transactions - By New Logic
            sql.append("SELECT ");
            sql.append("       c.id clientOrSupplierId,").append("\n");
            sql.append("       c.name clientOrSupplierName,").append("\n");
            sql.append("       'Pre Payment' as typeName,").append("\n");
            sql.append("       'CUSTOMER' as accountType, ").append("\n");
            sql.append("       ip.reference as invoiceNumber,").append("\n");
            sql.append("       ip.id as objectID, ").append("\n");
            sql.append("       sum(coalesce(aip.amount,0) - coalesce(ti.credit,0) + coalesce(ti.debit,0)) as amount,").append("\n");
            sql.append("       to_date(to_char(t.journaldate,'yyyy-MM-dd'),'yyyy-MM-dd') due_date, ").append("\n");
            sql.append("       to_date(to_char(t.journaldate,'yyyy-MM-dd'),'yyyy-MM-dd') date, ").append("\n");
            sql.append("       (to_date('").append(dateFormat.format(date)).append("','yyyy-MM-dd') - to_date(to_char(t.journaldate,'yyyy-MM-dd'),'yyyy-MM-dd')) aging, ").append("\n");
            sql.append("       NULL::integer[]  as journalId, ").append("\n");
            sql.append("       NULL::numeric[] as exchangeRates, ").append("\n");
            sql.append("       '' AS currencyName, ").append("\n");
            sql.append("       NULL::numeric[] AS currencyDifference ").append("\n");
            sql.append("FROM ").append(getCompanyId()).append(".transactionitem ti ").append("\n");
            sql.append("INNER JOIN ").append(getCompanyId()).append(".account a on a.id = ti.accountid").append("\n");
            sql.append("INNER JOIN ").append(getCompanyId()).append(".transaction t on t.id = ti.transactionid ").append("\n");
            sql.append("INNER JOIN ").append(getCompanyId()).append(".invoicepayments ip on ip.id = t.invoicepaymentid ").append("\n");
            sql.append("LEFT JOIN ").append(getCompanyId()).append(".reference ips on ips.id = ip.statusid ").append("\n");
            sql.append("LEFT JOIN ").append(getCompanyId()).append(".crmaccount c on (c.id = t.clientid or c.id = ti.crmaccount_id) ").append("\n");
            sql.append("LEFT JOIN ").append("(select ip.crmaccountid, ip.appliedPaymentId, sum(coalesce(ti.debit, 0)) amount \n ")
                    .append("from ").append(getCompanyId()).append(".invoicepayments ip ").append("\n")
                    .append("inner join ").append(getCompanyId()).append(".transaction t on t.invoicepaymentid = ip.id ")
                    .append("inner join ").append(getCompanyId()).append(".transactionitem ti on ti.transactionid = t.id ")
                    .append("inner join ").append(getCompanyId()).append(".account a on a.id = ti.accountid ")
                    .append("left join ").append(getCompanyId()).append(".reference ips on ips.id = ip.statusid ").append("\n")
                    .append("where ip.deleted is not true and ip.appliedPaymentId is not null ").append("\n")
                    .append("AND (a.key in (" + EdsAccount.ACCOUNTS_RECEIVABLE + "," + EdsAccount.UNEARNED_REVENUE + ") or a.groupKey in (" + EdsAccount.ACCOUNTS_RECEIVABLE + "," + EdsAccount.UNEARNED_REVENUE + ") ) \n")
                    .append("and to_date(to_char(ip.paymentDate,'yyyy-MM-dd'),'yyyy-MM-dd') <= '").append(dateFormat.format(date)).append("' ").append("\n")
//                    .append("and (ips.id is null or ips.code != '").append(EdsInvoicePayment.REVERSED).append("')\n ")
//                    .append("and (ip.paymentStatus is null or ip.paymentStatus != '").append(AccountingConstants.VOID).append("')\n ")
                    .append("group by ip.crmaccountid, ip.appliedPaymentId) aip on (aip.appliedPaymentId = ip.id and aip.crmaccountid = c.id) \n");
            sql.append("WHERE t.deleted is not true and ip.deleted is not true and ip.paymentStatus is not null ").append("\n");
            sql.append("AND ip.type = 'RECEIVABLE_PREPAYMENT' ").append("\n");
            sql.append("AND (a.key in (" + EdsAccount.ACCOUNTS_RECEIVABLE + "," + EdsAccount.UNEARNED_REVENUE + ") or a.groupKey in (" + EdsAccount.ACCOUNTS_RECEIVABLE + "," + EdsAccount.UNEARNED_REVENUE + ") ) \n");
            sql.append("AND to_date(to_char(t.journaldate,'yyyy-MM-dd'),'yyyy-MM-dd') <= '").append(dateFormat.format(date)).append("' ").append("\n");
            sql.append("GROUP BY c.id, c.name, ip.id, ip.reference, t.journaldate ").append("\n");

            sql.append("UNION ALL ").append("\n");
        }

        //ManualJournal Transaction
        sql.append("SELECT ").append("\n");
        sql.append("       c.id clientOrSupplierId,").append("\n");
        sql.append("       c.name clientOrSupplierName,").append("\n");
        sql.append("       'Manual Journal' as typeName,").append("\n");
        sql.append("       'CUSTOMER' as accountType, ").append("\n");
        sql.append("       mj.number as invoiceNumber,").append("\n");
//        sql.append("       mj.reference as reference,").append("\n");
        sql.append("       max(mj.id) as objectID, ").append("\n");
        sql.append("       sum(coalesce(ti.debit,0)) + max(coalesce(ip.amount,0)) - sum(coalesce(ti.credit,0)) - max(coalesce(csp.amount,0)) as amount,").append("\n");
        sql.append("       to_date(to_char(t.journaldate,'yyyy-MM-dd'),'yyyy-MM-dd') due_date, ").append("\n");
        sql.append("       to_date(to_char(t.journaldate,'yyyy-MM-dd'),'yyyy-MM-dd') date, ").append("\n");
        sql.append("       (to_date('").append(dateFormat.format(date)).append("','yyyy-MM-dd') - to_date(to_char(t.journaldate,'yyyy-MM-dd'),'yyyy-MM-dd')) aging, ").append("\n");
        sql.append("       NULL::integer[]  as journalId, ").append("\n");
        sql.append("       NULL::numeric[] as exchangeRates, ").append("\n");
        sql.append("       '' AS currencyName, ").append("\n");
        sql.append("       NULL::numeric[] AS currencyDifference ").append("\n");
        sql.append("FROM ").append(getCompanyId()).append(".transactionitem ti ").append("\n");
        sql.append("INNER JOIN ").append(getCompanyId()).append(".account a on a.id = ti.accountid").append("\n");
        sql.append("INNER JOIN ").append(getCompanyId()).append(".transaction t on t.id = ti.transactionid ").append("\n");
        sql.append("INNER JOIN ").append(getCompanyId()).append(".manualjournal mj on mj.id = t.manualjournalid ").append("\n");
        sql.append("LEFT JOIN ").append(getCompanyId()).append(".reference r on r.id = mj.overallStatus ").append("\n");
        sql.append("LEFT JOIN ").append(getCompanyId()).append(".crmaccount c on (c.id = t.clientid or c.id = ti.crmaccount_id) ").append("\n");
        sql.append("LEFT JOIN ").append("(select ip.crmaccountid, ip.manualjournalid, sum(coalesce(round(coalesce(ip.amountininvoicecurrency, ip.amount)/(case when mj.currencyid = ip.currencyID then mj.exchangerate else coalesce(ip.exchangerate,1) end), 5), 0)) amount ")
                .append(" from ").append(getCompanyId()).append(".invoicepayments ip ").append("\n")
                .append("inner join ").append(getCompanyId()).append(".invoice inv on (inv.id = ip.invoiceid and inv.type = '" + RECEIVABLE + "') ")
                .append("inner join ").append(getCompanyId()).append(".manualjournal mj on mj.id = ip.manualjournalid \n")
                .append("left join ").append(getCompanyId()).append(".reference ips on ips.id = ip.statusid ").append("\n")
                .append("where ip.deleted is not true and ip.manualjournalid is not null ").append("\n")
                .append("and to_date(to_char(ip.paymentDate,'yyyy-MM-dd'),'yyyy-MM-dd') <= '").append(dateFormat.format(date)).append("' ").append("\n")
//                .append("and (ips is null or ips.code != '").append(EdsInvoicePayment.REVERSED).append("') ")
                .append(" group by ip.crmaccountid, ip.manualjournalid) ip on (ip.manualjournalid = mj.id and ip.crmaccountid = c.id) \n");
        sql.append("LEFT JOIN ").append("(select customersupplierid as crmaccountid, manualjournalid, sum(coalesce(round(cp.amount/(case when mj.currencyid = cp.currencyid then mj.exchangerate else coalesce(cp.exchangerate,1) end),5), 0)) amount from ").append(getCompanyId())
                .append(".customerPayment cp \n")
                .append("join ").append(getCompanyId()).append(".manualjournal mj on mj.id = cp.manualjournalid ")
                .append("where cp.deleted is not true and cp.manualjournalid is not null \n")
                .append("and cp.type = ").append(EdsCustomerSupplierPayment.CUSTOMER_PAYMENT).append(" \n")
                .append("and to_date(to_char(cp.paymentDate,'yyyy-MM-dd'),'yyyy-MM-dd') <= '").append(dateFormat.format(date)).append("' ").append("\n")
                .append("group by cp.customersupplierid, cp.manualjournalid) csp on (csp.manualjournalid = mj.id and csp.crmaccountid = c.id) ").append("\n");
        sql.append("WHERE t.deleted is not true and mj.deleted is not true and r.code = 'POST' ").append("\n");
        sql.append("AND (a.key in (" + EdsAccount.ACCOUNTS_RECEIVABLE + "," + EdsAccount.UNEARNED_REVENUE + ") or a.groupKey in (" + EdsAccount.ACCOUNTS_RECEIVABLE + "," + EdsAccount.UNEARNED_REVENUE + ") ) \n");
        sql.append("AND to_date(to_char(t.journaldate,'yyyy-MM-dd'),'yyyy-MM-dd') <= '").append(dateFormat.format(date)).append("' ").append("\n");
        sql.append("GROUP BY c.id, c.name, mj.id, mj.number, t.journaldate ").append("\n");

        if (isExcludePrePayments) {
            sql.append("HAVING sum(coalesce(ti.debit,0)) + max(coalesce(ip.amount,0)) - sum(coalesce(ti.credit,0)) - max(coalesce(csp.amount,0)) > 0 \n");
        }

        sql.append("UNION ALL ").append("\n");

        //Bank Transfer Transaction
        sql.append("SELECT ").append("\n");
        sql.append("       c.id clientOrSupplierId,").append("\n");
        sql.append("       c.name clientOrSupplierName,").append("\n");
        sql.append("       (case when bt.transferType = " + RECEIVE_MONEY + " then 'Bank Receipt'" + "                when bt.transferType = " + CASH_RECEIPT + " then 'Cash Receipt'" + "                when bt.transferType = " + SPEND_MONEY + " then 'Bank Payment' else 'Cash Payment' end) as typeName,").append("\n");
        sql.append("       'CUSTOMER' as accountType, ").append("\n");
        sql.append("       bt.number as invoiceNumber,").append("\n");
//        sql.append("       bt.reference as reference,").append("\n");
        sql.append("       bt.id as objectID, ").append("\n");
        sql.append("       max(coalesce(ip.amount,0)) - sum(coalesce(ti.credit,0)) + sum(coalesce(ti.debit,0)) as amount,").append("\n");
        sql.append("       to_date(to_char(t.journaldate,'yyyy-MM-dd'),'yyyy-MM-dd') due_date, ").append("\n");
        sql.append("       to_date(to_char(t.journaldate,'yyyy-MM-dd'),'yyyy-MM-dd') date, ").append("\n");
        sql.append("       (to_date('").append(dateFormat.format(date)).append("','yyyy-MM-dd') - to_date(to_char(t.journaldate,'yyyy-MM-dd'),'yyyy-MM-dd')) aging, ").append("\n");
        sql.append("       NULL::integer[]  as journalId, ").append("\n");
        sql.append("       NULL::numeric[] as exchangeRates, ").append("\n");
        sql.append("       '' AS currencyName, ").append("\n");
        sql.append("       NULL::numeric[] AS currencyDifference ").append("\n");
        sql.append("FROM ").append(getCompanyId()).append(".transactionitem ti ").append("\n");
        sql.append("INNER JOIN ").append(getCompanyId()).append(".account a on a.id = ti.accountid").append("\n");
        sql.append("INNER JOIN ").append(getCompanyId()).append(".transaction t on t.id = ti.transactionid ").append("\n");
        sql.append("INNER JOIN ").append(getCompanyId()).append(".spendreceivemoney bt on bt.id = t.banktransferid ").append("\n");
        sql.append("LEFT JOIN ").append(getCompanyId()).append(".crmaccount c on (c.id = t.clientid or c.id = ti.crmaccount_id) ").append("\n");
        sql.append("LEFT JOIN ").append("(select ip.crmaccountid, ip.bankTransferID, sum(coalesce(round(coalesce(ip.amountininvoicecurrency, ip.amount)/(case when bt.currencyid = ip.currencyID then bt.exchangerate else coalesce(ip.exchangerate,1) end), 5), 0)) amount ")
                .append(" from ").append(getCompanyId()).append(".invoicepayments ip ").append("\n")
                .append(" join ").append(getCompanyId()).append(".invoice inv on (inv.id = ip.invoiceid and inv.type = '" + RECEIVABLE + "') ")
                .append(" join ").append(getCompanyId()).append(".spendreceivemoney bt on bt.id = ip.banktransferid \n")
                .append(" left join ").append(getCompanyId()).append(".reference ips on ips.id = ip.statusid ").append("\n")
                .append(" where ip.deleted is not true and ip.bankTransferID is not null ").append("\n")
                .append(" and to_date(to_char(ip.paymentDate,'yyyy-MM-dd'),'yyyy-MM-dd') <= '").append(dateFormat.format(date)).append("' ").append("\n")
//                .append("and (ips is null or ips.code != '").append(EdsInvoicePayment.REVERSED).append("') ")
                .append(" group by ip.crmaccountid, ip.bankTransferID) ip on (ip.bankTransferID = bt.id and ip.crmaccountid = c.id) \n");
        sql.append("WHERE t.deleted is not true and bt.deleted is not true ").append("\n");
        sql.append("AND (a.key in (" + EdsAccount.ACCOUNTS_RECEIVABLE + "," + EdsAccount.UNEARNED_REVENUE + ") or a.groupKey in (" + EdsAccount.ACCOUNTS_RECEIVABLE + "," + EdsAccount.UNEARNED_REVENUE + ") ) \n");
        sql.append("AND to_date(to_char(t.journaldate,'yyyy-MM-dd'),'yyyy-MM-dd') <= '").append(dateFormat.format(date)).append("' ").append("\n");
        sql.append("GROUP BY c.id, c.name, bt.id, bt.number, bt.reference, bt.transferType, t.journaldate ").append("\n");

        if (isExcludePrePayments) {
            sql.append("HAVING max(coalesce(ip.amount,0)) - sum(coalesce(ti.credit,0)) + sum(coalesce(ti.debit,0)) > 0 \n");
        }

        sql.append("UNION ALL ").append("\n");

        //Customer Transaction(Opening balance)
        sql.append("SELECT ").append("\n");
        sql.append("       c.id clientOrSupplierId,").append("\n");
        sql.append("       c.name clientOrSupplierName,").append("\n");
        sql.append("       'Opening balance' as typeName,").append("\n");
        sql.append("       'CUSTOMER' as accountType, ").append("\n");
        sql.append("       '' as invoiceNumber,").append("\n");
//        sql.append("       '' as reference,").append("\n");
        sql.append("       null as objectID, ").append("\n");
        sql.append("       sum(coalesce(ti.debit, 0)) + sum(coalesce(ip.amount, 0)) - sum(coalesce(ti.credit, 0)) - sum(coalesce(csp.amount,0)) as amount,").append("\n");
        sql.append("       to_date(to_char(t.journaldate,'yyyy-MM-dd'),'yyyy-MM-dd') due_date, ").append("\n");
        sql.append("       to_date(to_char(t.journaldate,'yyyy-MM-dd'),'yyyy-MM-dd') date, ").append("\n");
        sql.append("       (to_date('").append(dateFormat.format(date)).append("','yyyy-MM-dd') - to_date(to_char(t.journaldate,'yyyy-MM-dd'),'yyyy-MM-dd')) aging, ").append("\n");
        sql.append("       NULL::integer[]  as journalId, ").append("\n");
        sql.append("       NULL::numeric[] as exchangeRates, ").append("\n");
        sql.append("       '' AS currencyName, ").append("\n");
        sql.append("       NULL::numeric[] AS currencyDifference ").append("\n");
        sql.append("FROM ").append(getCompanyId()).append(".transactionitem ti ").append("\n");
        sql.append("INNER JOIN ").append(getCompanyId()).append(".account a on a.id = ti.accountid").append("\n");
        sql.append("INNER JOIN ").append(getCompanyId()).append(".transaction t on t.id = ti.transactionid ").append("\n");
        sql.append("INNER JOIN ").append(getCompanyId()).append(".crmaccount c on c.id = t.clientid ").append("\n");
        sql.append("LEFT JOIN ").append("(select crmaccountid, sum(coalesce(round(coalesce(ip.amountininvoicecurrency, ip.amount)/exchangerate, 2), 0)) amount ")
                .append(" from ").append(getCompanyId()).append(".invoicepayments ip").append("\n")
                .append(" left join ").append(getCompanyId()).append(".reference ips on ips.id = ip.statusid ").append("\n")
                .append(" where ip.deleted is not true and ip.type = 'RECEIVABLE_CRM_ACCOUNT_CREDIT' ").append("\n")
                .append("and to_date(to_char(ip.paymentDate,'yyyy-MM-dd'),'yyyy-MM-dd') <= '").append(dateFormat.format(date)).append("' ").append("\n")
//                .append("and (ips is null or ips.code != '").append(EdsInvoicePayment.REVERSED).append("') ")
                .append(" group by ip.crmaccountid) ip on ip.crmaccountid = c.id ").append("\n");
        sql.append("LEFT JOIN ").append("(select customersupplierid as crmaccountid, sum(coalesce(amount, 0)/COALESCE(exchangerate,1)) amount from ").append(getCompanyId())
                .append(".customerPayment where deleted is not true and manualjournalid is null \n")
                .append("and type = ").append(EdsCustomerSupplierPayment.CUSTOMER_PAYMENT).append(" \n")
                .append("and to_date(to_char(paymentDate,'yyyy-MM-dd'),'yyyy-MM-dd') <= '").append(dateFormat.format(date)).append("' ").append("\n")
                .append("group by customersupplierid) csp on csp.crmaccountid = c.id ").append("\n");
        sql.append("WHERE t.deleted is not true ").append("\n");
        sql.append("AND t.dtype = 'EdsCustomerTransaction' ").append("\n");
        sql.append("AND (a.key in (" + EdsAccount.ACCOUNTS_RECEIVABLE + "," + EdsAccount.UNEARNED_REVENUE + ") or a.groupKey in (" + EdsAccount.ACCOUNTS_RECEIVABLE + "," + EdsAccount.UNEARNED_REVENUE + ") ) \n");
        sql.append("AND to_date(to_char(t.journaldate,'yyyy-MM-dd'),'yyyy-MM-dd') <= '").append(dateFormat.format(date)).append("' ").append("\n");
        sql.append("GROUP BY c.id, c.name, t.journaldate ").append("\n");

        if (isExcludePrePayments) {
            sql.append("HAVING sum(coalesce(ti.debit, 0)) + sum(coalesce(ip.amount, 0)) - sum(coalesce(ti.credit, 0)) - sum(coalesce(csp.amount,0)) > 0 \n");
        }

        sql.append("UNION ALL ").append("\n");

        //Other Transactions
        sql.append("SELECT ").append("\n");
        sql.append("       c.id clientOrSupplierId,").append("\n");
        sql.append("       c.name clientOrSupplierName,").append("\n");
        sql.append("       (case when t.dtype = 'EdsBankTransferTransaction' then 'Bank Transfer' else 'Other' end) as typeName,").append("\n");
        sql.append("       'CUSTOMER' as accountType, ").append("\n");
        sql.append("       '' as invoiceNumber,").append("\n");
//        sql.append("       '' as reference,").append("\n");
        sql.append("       null as objectID, ").append("\n");
        sql.append("       sum(coalesce(ti.debit, 0)) - sum(coalesce(ti.credit, 0)) as amount,").append("\n");
        sql.append("       to_date(to_char(t.journaldate,'yyyy-MM-dd'),'yyyy-MM-dd') due_date, ").append("\n");
        sql.append("       to_date(to_char(t.journaldate,'yyyy-MM-dd'),'yyyy-MM-dd') date, ").append("\n");
        sql.append("       (to_date('").append(dateFormat.format(date)).append("','yyyy-MM-dd') - to_date(to_char(t.journaldate,'yyyy-MM-dd'),'yyyy-MM-dd')) aging, ").append("\n");
        sql.append("       NULL::integer[]  as journalId, ").append("\n");
        sql.append("       NULL::numeric[] as exchangeRates, ").append("\n");
        sql.append("       '' AS currencyName, ").append("\n");
        sql.append("       NULL::numeric[] AS currencyDifference ").append("\n");
        sql.append("FROM ").append(getCompanyId()).append(".transactionitem ti ").append("\n");
        sql.append("INNER JOIN ").append(getCompanyId()).append(".account a on a.id = ti.accountid").append("\n");
        sql.append("INNER JOIN ").append(getCompanyId()).append(".transaction t on t.id = ti.transactionid ").append("\n");
        sql.append("LEFT JOIN ").append(getCompanyId()).append(".crmaccount c on (c.id = t.clientid or c.id = ti.crmaccount_id) ").append("\n");
        sql.append("WHERE t.deleted is not true ").append("\n");
        sql.append("AND t.dtype not in('EdsInvoiceTransaction', 'EdsInvoicePaymentTransaction', 'EdsManualTransaction', 'EdsCustomerTransaction', 'EdsCusSuppPaymentTransaction', 'EdsFixedAssetTransaction', 'EdsBankTransferTransaction')").append("\n");
        sql.append("AND (a.key in (" + EdsAccount.ACCOUNTS_RECEIVABLE + "," + EdsAccount.UNEARNED_REVENUE + ") or a.groupKey in (" + EdsAccount.ACCOUNTS_RECEIVABLE + "," + EdsAccount.UNEARNED_REVENUE + ") ) \n");
        sql.append("AND to_date(to_char(t.journaldate,'yyyy-MM-dd'),'yyyy-MM-dd') <= '").append(dateFormat.format(date)).append("' ").append("\n");
        sql.append("GROUP BY c.id, c.name, t.dtype, t.journaldate ").append("\n");


        sql.append("UNION ALL ").append("\n");

        //Other Transactions
        sql.append("SELECT ").append("\n");
        sql.append("       c.id clientOrSupplierId,").append("\n");
        sql.append("       c.name clientOrSupplierName,").append("\n");
        sql.append("       'EXCHANGEGAINANDLOSS' as typeName,").append("\n");
        sql.append("       'CUSTOMER' as accountType,").append("\n");
        sql.append("       '' as invoiceNumber,").append("\n");
        sql.append("       null as objectID, ").append("\n");
        sql.append("       sum(coalesce(ti.foreigndebit, 0) - coalesce(ti.foreigncredit, 0))/" + currentExRates + "- (sum(coalesce(ti.debit, 0) - coalesce(ti.credit, 0))) as amount,").append("\n");
        sql.append("       max(to_date(to_char(t.journaldate,'yyyy-MM-dd'),'yyyy-MM-dd')) due_date, ").append("\n");
        sql.append("       min(to_date(to_char(t.journaldate,'yyyy-MM-dd'),'yyyy-MM-dd')) date, ").append("\n");
        sql.append("       max(to_date('").append(dateFormat.format(date)).append("','yyyy-MM-dd')) - min(to_date(to_char(t.journaldate,'yyyy-MM-dd'),'yyyy-MM-dd')) aging, ").append("\n");
        sql.append("       NULL::integer[]  as journalId, ").append("\n");
        sql.append("       NULL::numeric[] as exchangeRates, ").append("\n");
        sql.append("       '' AS currencyName, ").append("\n");
        sql.append("       NULL::numeric[] AS currencyDifference ").append("\n");
        sql.append(" FROM ").append(getCompanyId()).append(".transactionitem ti ").append("\n");
        sql.append(" JOIN ").append(getCompanyId()).append(".account a on a.id = ti.accountid").append("\n");
        sql.append(" JOIN ").append(getCompanyId()).append(".transaction t on t.id = ti.transactionid ").append("\n");
        sql.append(" JOIN ").append(getCompanyId()).append(".crmaccount c on (c.id = t.clientid or c.id = ti.crmaccount_id) ").append("\n");
        sql.append(" WHERE t.deleted is not true and t.currencyid!=" + fs.getCurrency().getObjectID() + " and a.foreignAccount  ").append("\n");
        sql.append(" AND (a.key in (" + EdsAccount.ACCOUNTS_RECEIVABLE + "," + EdsAccount.UNEARNED_REVENUE + ") or a.groupKey in (" + EdsAccount.ACCOUNTS_RECEIVABLE + "," + EdsAccount.UNEARNED_REVENUE + ") ) \n");
        sql.append(" AND to_date(to_char(t.journaldate,'yyyy-MM-dd'),'yyyy-MM-dd') <= '").append(dateFormat.format(date)).append("' ").append("\n");
        sql.append(" GROUP BY c.id, a.id ").append("\n");

        sql.append(") t WHERE amount != 0 \n");

        return sql.toString();
    }

    private String getPayableAgingQuery(String currentExRates, Date date, boolean isExcludePrePayments, EdsFinancialSettings fs) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        StringBuilder sql = new StringBuilder();
        sql.append("SELECT * FROM ( \n");

        //Invoice/Credit Note Transaction
        sql.append("SELECT clientOrSupplierId, clientOrSupplierName, typeName, accountType, invoiceNumber, objectID, sum(amount) amount, due_date, min(date) date, (to_date('").append(dateFormat.format(date)).append("','yyyy-MM-dd') - due_date) aging ,NULL::integer[] AS journalId,  NULL::numeric[] as exchangeRates, '' AS currencyName, NULL::numeric[] AS currencyDifference  FROM (").append("\n");
        sql.append("SELECT c.id clientOrSupplierId,").append("\n");
        sql.append("       c.name clientOrSupplierName,").append("\n");
        sql.append("       (CASE WHEN pinv.id is not null THEN (CASE WHEN pinv.iscreditnote THEN 'Credit Note' ELSE 'Invoice' END) \n")
                .append("       WHEN finv.id is not null THEN 'Invoice' ELSE \n")
                .append("       (CASE WHEN inv.iscreditnote THEN 'Credit Note' ELSE 'Invoice' END) \n")
                .append("  END) as typeName, \n");
        sql.append("       'CUSTOMER' as accountType, ").append("\n");
        sql.append("       coalesce(inv.number, coalesce(pinv.number, finv.number)) as invoiceNumber,").append("\n");
//        sql.append("       coalesce(inv.reference, coalesce(finv.reference, pinv.reference)) as reference,").append("\n");
        sql.append("       coalesce(inv.id, coalesce(pinv.id, finv.id)) as objectID, ").append("\n");
        sql.append("       (case when ip.id is not null and pinv.iscreditnote is not true and t.reversalid is null then 0 else coalesce(ti.credit,0) end) - (case when ip.id is not null and pinv.iscreditnote is true then 0 else  coalesce(ti.debit, 0) end) amount, \n");
        sql.append("       to_date(to_char(coalesce(inv.duedate, coalesce(pinv.duedate, finv.duedate)),'yyyy-MM-dd'),'yyyy-MM-dd') due_date, ").append("\n");
        sql.append("       to_date(to_char(t.journaldate,'yyyy-MM-dd'),'yyyy-MM-dd') date, ").append("\n");
        sql.append("       NULL::integer[]  as journalId, ").append("\n");
        sql.append("       NULL::numeric[] as exchangeRates, ").append("\n");
        sql.append("       '' AS currencyName, ").append("\n");
        sql.append("       NULL::numeric[] AS currencyDifference ").append("\n");
        sql.append("FROM ").append(getCompanyId()).append(".transactionitem ti ").append("\n");
        sql.append("INNER JOIN ").append(getCompanyId()).append(".account a on a.id = ti.accountid").append("\n");
        sql.append("INNER JOIN ").append(getCompanyId()).append(".transaction t on t.id = ti.transactionid ").append("\n");

        sql.append("LEFT JOIN ").append(getCompanyId()).append(".invoice inv on inv.id = t.invoiceid ").append("\n");
        sql.append("LEFT JOIN ").append(getCompanyId()).append(".reference invs on invs.id = inv.status_id ").append("\n");

        sql.append("LEFT JOIN ").append(getCompanyId()).append(".invoicepayments ip on ip.id = t.invoicepaymentid ").append("\n");
        sql.append("LEFT JOIN ").append(getCompanyId()).append(".invoice pinv on (pinv.id = ip.invoiceid or pinv.id = ip.creditnoteid) ").append("\n");
        sql.append("LEFT JOIN ").append(getCompanyId()).append(".reference ips on ips.id = ip.statusid ").append("\n");

        sql.append("LEFT JOIN ").append(getCompanyId()).append(".fixedasset fa on fa.id = t.fixedassetid ").append("\n");
        sql.append("LEFT JOIN ").append(getCompanyId()).append(".invoice finv on finv.id = fa.purchaseinvoiceid \n");
        sql.append("LEFT JOIN ").append(getCompanyId()).append(".reference finvs on finvs.id = finv.status_id ").append("\n");

        sql.append("LEFT JOIN ").append(getCompanyId()).append(".crmaccount c on (c.id = t.supplierid or c.id = ti.crmaccount_id) ").append("\n");
        sql.append("WHERE t.deleted is not true and (").append("\n");

        //invoice where clause
        sql.append("(t.invoiceid is not null AND inv.deleted is not true ) OR ").append("\n");

        //invoice payment where clause
        sql.append("(t.invoicepaymentid is not null ");
//        sql.append(" AND (ips.id is null or ips.code != '").append(EdsInvoicePayment.REVERSED).append("') \n");
        sql.append("AND (ip.invoiceid is not null OR ip.creditnoteid is not null)) OR \n");

        //fixed asset where clause
        sql.append("(t.fixedassetid is not null AND fa.purchaseinvoiceid is not null AND finvs.code in ('APPROVE', 'OPEN', 'OVER_DUE', 'PAID')) \n");

        //close OR operation
        sql.append(") \n");

        sql.append("AND (a.key in (" + EdsAccount.ACCOUNTS_PAYABLE + "," + EdsAccount.PREPAID_EXPANSES + ") or a.groupKey in (" + EdsAccount.ACCOUNTS_PAYABLE + "," + EdsAccount.PREPAID_EXPANSES + ") ) ");
        sql.append("AND to_date(to_char(t.journaldate,'yyyy-MM-dd'),'yyyy-MM-dd') <= '").append(dateFormat.format(date)).append("' ").append("\n");
        sql.append(") inv \n");
        sql.append("GROUP BY clientOrSupplierId, clientOrSupplierName,accountType, objectID, invoiceNumber, typeName, due_date \n");

        sql.append("UNION ALL ").append("\n");

        //Expense Transactions
        sql.append("SELECT clientOrSupplierId, clientOrSupplierName, typeName, accountType, invoiceNumber, objectID, sum(amount) amount, min(due_date) due_date, min(date) date, (to_date('").append(dateFormat.format(date)).append("','yyyy-MM-dd') - min(due_date)) aging ,NULL::integer[] AS journalId,  NULL::numeric[] as exchangeRates, '' AS currencyName, NULL::numeric[] AS currencyDifference  FROM (").append("\n");
        sql.append("SELECT c.id clientOrSupplierId,").append("\n");
        sql.append("       c.name clientOrSupplierName,").append("\n");
        sql.append("       'Expense'::text typeName, \n");
        sql.append("       'CUSTOMER' as accountType, ").append("\n");
        sql.append("       coalesce(coalesce(exp.number, pexp.number),iexp.number) as invoiceNumber,").append("\n");
        sql.append("       coalesce(coalesce(exp.id, pexp.id),iexp.id) as objectID, ").append("\n");
        sql.append("       coalesce(ti.credit,0) - coalesce(ti.debit, 0) amount, \n");
        sql.append("       to_date(to_char(t.journaldate,'yyyy-MM-dd'),'yyyy-MM-dd') due_date, ").append("\n");
        sql.append("       to_date(to_char(t.journaldate,'yyyy-MM-dd'),'yyyy-MM-dd') date, ").append("\n");
        sql.append("       NULL::integer[] as journalId, ").append("\n");
        sql.append("       NULL::numeric[] as exchangeRates, ").append("\n");
        sql.append("       '' AS currencyName, ").append("\n");
        sql.append("       NULL::numeric[] AS currencyDifference ").append("\n");
        sql.append("FROM ").append(getCompanyId()).append(".transactionitem ti ").append("\n");
        sql.append("INNER JOIN ").append(getCompanyId()).append(".account a on a.id = ti.accountid").append("\n");
        sql.append("INNER JOIN ").append(getCompanyId()).append(".transaction t on t.id = ti.transactionid ").append("\n");

        sql.append("LEFT JOIN ").append(getCompanyId()).append(".expenseReport exp on exp.id = t.expenseReportid ").append("\n");
        sql.append("LEFT JOIN ").append(getCompanyId()).append(".reference exps on exps.id = exp.overallStatus ").append("\n");

        sql.append("LEFT JOIN ").append(getCompanyId()).append(".expensePayments ep on ep.id = t.expensePaymentId ").append("\n");
        sql.append("LEFT JOIN ").append(getCompanyId()).append(".expenseReport pexp on pexp.id = ep.expenseReportId ").append("\n");
        sql.append("LEFT JOIN ").append(getCompanyId()).append(".reference pexpst on pexpst.id = pexp.overallStatus ").append("\n");

        sql.append("LEFT JOIN ").append(getCompanyId()).append(".invoicePayments ip on t.invoicepaymentid =ip.id and ip.expenseID is not null and coalesce(ti.debit,0)!=0 ").append("\n");
        sql.append("LEFT JOIN ").append(getCompanyId()).append(".expenseReport iexp on ip.expenseid=iexp.id ").append("\n");
        sql.append("LEFT JOIN ").append(getCompanyId()).append(".reference iexpst on iexpst.id = iexp.overallStatus ").append("\n");

        sql.append("LEFT JOIN ").append(getCompanyId()).append(".crmaccount c on (c.id = t.supplierid or c.id = ti.crmaccount_id) ").append("\n");
        sql.append("WHERE t.deleted is not true ").append("\n");
        sql.append(" and (").append("\n");
        //expense where clause
        sql.append("(exp.id is not null AND exps.code in ('EXPENSE_APPROVED', 'PARTIALLY_PAID', 'EXPENSE_PAID'))  \n");
        //expense payment where clause
        sql.append(" OR (ep.id is not null AND pexpst.code in ('EXPENSE_APPROVED', 'PARTIALLY_PAID', 'EXPENSE_PAID') ) \n");
        //Payments made through "apply patch"
        sql.append(" OR (ip.id is not null AND iexpst.code in ('EXPENSE_APPROVED', 'PARTIALLY_PAID', 'EXPENSE_PAID')  ) \n");
        //close OR operation
        sql.append(") \n");

        sql.append("AND (a.key in (" + EdsAccount.ACCOUNTS_PAYABLE + "," + EdsAccount.PREPAID_EXPANSES + ") or a.groupKey in (" + EdsAccount.ACCOUNTS_PAYABLE + "," + EdsAccount.PREPAID_EXPANSES + ") ) ");
        sql.append("AND to_date(to_char(t.journaldate,'yyyy-MM-dd'),'yyyy-MM-dd') <= '").append(dateFormat.format(date)).append("' ").append("\n");
        sql.append(") exp \n");
        sql.append("GROUP BY clientOrSupplierId, clientOrSupplierName, accountType, objectID, invoiceNumber, typeName \n");

        sql.append("UNION ALL ").append("\n");

        if (!isExcludePrePayments) {

            //Pre Payment Transactions - By New Logic
            sql.append("SELECT ");
            sql.append("       c.id clientOrSupplierId,").append("\n");
            sql.append("       c.name clientOrSupplierName,").append("\n");
            sql.append("       'Pre Payment' as typeName,").append("\n");
            sql.append("       'CUSTOMER' as accountType, ").append("\n");
            sql.append("       ip.reference as invoiceNumber,").append("\n");
            sql.append("       ip.id as objectID, ").append("\n");
            sql.append("       sum(coalesce(aip.amount,0) - coalesce(ti.debit,0) + coalesce(ti.credit,0)) as amount,").append("\n");
            sql.append("       to_date(to_char(t.journaldate,'yyyy-MM-dd'),'yyyy-MM-dd') due_date, ").append("\n");
            sql.append("       to_date(to_char(t.journaldate,'yyyy-MM-dd'),'yyyy-MM-dd') date, ").append("\n");
            sql.append("       (to_date('").append(dateFormat.format(date)).append("','yyyy-MM-dd') - to_date(to_char(t.journaldate,'yyyy-MM-dd'),'yyyy-MM-dd')) aging, ").append("\n");
            sql.append("       NULL::integer[] as journalId, ").append("\n");
            sql.append("       NULL::numeric[] as exchangeRates, ").append("\n");
            sql.append("       '' AS currencyName, ").append("\n");
            sql.append("       NULL::numeric[] AS currencyDifference ").append("\n");
            sql.append("FROM ").append(getCompanyId()).append(".transactionitem ti ").append("\n");
            sql.append("INNER JOIN ").append(getCompanyId()).append(".account a on a.id = ti.accountid").append("\n");
            sql.append("INNER JOIN ").append(getCompanyId()).append(".transaction t on t.id = ti.transactionid ").append("\n");
            sql.append("INNER JOIN ").append(getCompanyId()).append(".invoicepayments ip on ip.id = t.invoicepaymentid ").append("\n");
            sql.append("LEFT JOIN ").append(getCompanyId()).append(".reference ips on ips.id = ip.statusid ").append("\n");
            sql.append("LEFT JOIN ").append(getCompanyId()).append(".crmaccount c on (c.id = t.supplierid or c.id = ti.crmaccount_id) ").append("\n");
            sql.append("LEFT JOIN ").append("(select ip.crmaccountid, ip.appliedPaymentId, sum(coalesce(ti.credit, 0)) amount from ").append(getCompanyId()).append(".invoicepayments ip ").append("\n")
                    .append("inner join ").append(getCompanyId()).append(".transaction t on t.invoicepaymentid = ip.id ")
                    .append("inner join ").append(getCompanyId()).append(".transactionitem ti on ti.transactionid = t.id ")
                    .append("inner join ").append(getCompanyId()).append(".account a on a.id = ti.accountid ")
                    .append("left join ").append(getCompanyId()).append(".reference ips on ips.id = ip.statusid ").append("\n")
                    .append("where ip.deleted is not true and ip.appliedPaymentId is not null ").append("\n")
                    .append("AND (a.key in (" + EdsAccount.ACCOUNTS_PAYABLE + "," + EdsAccount.PREPAID_EXPANSES + ") or a.groupKey in (" + EdsAccount.ACCOUNTS_PAYABLE + "," + EdsAccount.PREPAID_EXPANSES + ") ) ")
                    .append("and to_date(to_char(ip.paymentDate,'yyyy-MM-dd'),'yyyy-MM-dd') <= '").append(dateFormat.format(date)).append("' ").append("\n")
//                    .append("and (ips is null or ips.code != '").append(EdsInvoicePayment.REVERSED).append("')\n ")
//                    .append("and (ip.paymentStatus is null or ip.paymentStatus != '").append(AccountingConstants.VOID).append("')\n ")
                    .append(" group by ip.crmaccountid, ip.appliedPaymentId) aip on (aip.appliedPaymentId = ip.id and aip.crmaccountid = c.id) \n");
            sql.append("WHERE t.deleted is not true and ip.deleted is not true and ip.paymentStatus is not null ").append("\n");
            sql.append("AND ip.type = 'PAYABLE_SUPPLIER_CREDIT' ").append("\n");
            sql.append("AND (a.key in (" + EdsAccount.ACCOUNTS_PAYABLE + "," + EdsAccount.PREPAID_EXPANSES + ") or a.groupKey in (" + EdsAccount.ACCOUNTS_PAYABLE + "," + EdsAccount.PREPAID_EXPANSES + ") ) \n");
            sql.append("AND to_date(to_char(t.journaldate,'yyyy-MM-dd'),'yyyy-MM-dd') <= '").append(dateFormat.format(date)).append("' ").append("\n");
            sql.append("GROUP BY c.id, c.name, ip.id, ip.reference, t.journaldate ").append("\n");

            sql.append("UNION ALL ").append("\n");
        }

        if (!isExcludePrePayments) {

            //Bank Check Transaction
            sql.append("SELECT ").append("\n");
            sql.append("       c.id clientOrSupplierId,").append("\n");
            sql.append("       c.name clientOrSupplierName,").append("\n");
            sql.append("       'Check Transaction' as typeName,").append("\n");
            sql.append("       'CUSTOMER' as accountType, ").append("\n");
            sql.append("       max(coalesce(bch.number,ip.reference)) as invoiceNumber,").append("\n");
//            sql.append("       bch.memo as reference,").append("\n");
            sql.append("       coalesce(bch.id, t.bankcheckid) as objectID, ").append("\n");
            sql.append("       sum(case when t.dtype = 'EdsInvoicePaymentTransaction' then coalesce(ti.credit,0) else 0-coalesce(ti.debit,0) end) as amount,").append("\n");
            sql.append("       to_date(to_char(t.journaldate,'yyyy-MM-dd'),'yyyy-MM-dd') due_date, ").append("\n");
            sql.append("       to_date(to_char(t.journaldate,'yyyy-MM-dd'),'yyyy-MM-dd') date, ").append("\n");
            sql.append("       (to_date('").append(dateFormat.format(date)).append("','yyyy-MM-dd') - to_date(to_char(t.journaldate,'yyyy-MM-dd'),'yyyy-MM-dd')) aging, ").append("\n");
            sql.append("       NULL::integer[] as journalId, ").append("\n");
            sql.append("       NULL::numeric[] as exchangeRates, ").append("\n");
            sql.append("       '' AS currencyName, ").append("\n");
            sql.append("       NULL::numeric[] AS currencyDifference ").append("\n");
            sql.append("FROM ").append(getCompanyId()).append(".transactionitem ti ").append("\n");
            sql.append("INNER JOIN ").append(getCompanyId()).append(".account a on a.id = ti.accountid").append("\n");
            sql.append("INNER JOIN ").append(getCompanyId()).append(".transaction t on t.id = ti.transactionid ").append("\n");
            sql.append("LEFT JOIN ").append(getCompanyId()).append(".bankcheck bch on bch.id = t.bankcheckid ").append("\n");
            sql.append("LEFT JOIN ").append(getCompanyId()).append(".invoicepayments ip on ip.id = t.invoicepaymentid ").append("\n");
            sql.append("LEFT JOIN ").append(getCompanyId()).append(".reference ips on ips.id = ip.statusid ").append("\n");
            sql.append("LEFT JOIN ").append(getCompanyId()).append(".crmaccount c on (c.id = t.supplierid or c.id = ti.crmaccount_id) ").append("\n");
            sql.append("WHERE t.deleted is not true ").append("\n");
            sql.append("AND (t.dtype = 'EdsBankCheckTransaction' or (t.dtype = 'EdsInvoicePaymentTransaction' and ip.type = 'PAYABLE_BANK_CHECK_SHARE' and (ips is null or ips.code != '" + EdsInvoicePayment.REVERSED + "') )) ").append("\n");
            sql.append("AND (a.key in (" + EdsAccount.ACCOUNTS_PAYABLE + "," + EdsAccount.PREPAID_EXPANSES + ") or a.groupKey in (" + EdsAccount.ACCOUNTS_PAYABLE + "," + EdsAccount.PREPAID_EXPANSES + ") ) \n");
            sql.append("AND to_date(to_char(t.journaldate,'yyyy-MM-dd'),'yyyy-MM-dd') <= '").append(dateFormat.format(date)).append("' ").append("\n");
            sql.append("GROUP BY c.id, c.name, t.id, t.journaldate, bch.id, t.bankcheckid ").append("\n");

            sql.append("UNION ALL ").append("\n");
        }

        //ManualJournal Transaction
        sql.append("SELECT ").append("\n");
        sql.append("       c.id clientOrSupplierId,").append("\n");
        sql.append("       c.name clientOrSupplierName,").append("\n");
        sql.append("       'Manual Journal' as typeName,").append("\n");
        sql.append("       'CUSTOMER' as accountType, ").append("\n");
        sql.append("       mj.reference as invoiceNumber,").append("\n");
        sql.append("       max(mj.id) as objectID, ").append("\n");
        sql.append("       max(coalesce(ip.amount,0)) + sum(coalesce(ti.credit,0)) - sum(coalesce(ti.debit,0)) - max(coalesce(csp.amount,0)) as amount,").append("\n");
        sql.append("       to_date(to_char(t.journaldate,'yyyy-MM-dd'),'yyyy-MM-dd') due_date, ").append("\n");
        sql.append("       to_date(to_char(t.journaldate,'yyyy-MM-dd'),'yyyy-MM-dd') date, ").append("\n");
        sql.append("       (to_date('").append(dateFormat.format(date)).append("','yyyy-MM-dd') - to_date(to_char(t.journaldate,'yyyy-MM-dd'),'yyyy-MM-dd')) aging, ").append("\n");
        sql.append("       NULL::numeric[] as journalId, ").append("\n");
        sql.append("       NULL::numeric[] as exchangeRates, ").append("\n");
        sql.append("       '' AS currencyName, ").append("\n");
        sql.append("       NULL::numeric[] AS currencyDifference ").append("\n");
        sql.append("FROM ").append(getCompanyId()).append(".transactionitem ti ").append("\n");
        sql.append("INNER JOIN ").append(getCompanyId()).append(".account a on a.id = ti.accountid").append("\n");
        sql.append("INNER JOIN ").append(getCompanyId()).append(".transaction t on t.id = ti.transactionid ").append("\n");
        sql.append("INNER JOIN ").append(getCompanyId()).append(".manualjournal mj on mj.id = t.manualjournalid ").append("\n");
        sql.append("LEFT JOIN ").append(getCompanyId()).append(".reference r on r.id = mj.overallStatus ").append("\n");
        sql.append("LEFT JOIN ").append(getCompanyId()).append(".crmaccount c on (c.id = t.supplierid or c.id = ti.crmaccount_id) ").append("\n");
        sql.append("LEFT JOIN ").append("(select ip.crmaccountid, ip.manualjournalid, sum(coalesce(coalesce(ip.amountininvoicecurrency, ip.amount)/(case when mj.currencyid = ip.currencyID then mj.exchangerate else coalesce(ip.exchangerate,1) end), 0)) amount ")
                .append(" from ").append(getCompanyId()).append(".invoicepayments ip ").append("\n")
                .append(" join ").append(getCompanyId()).append(".invoice inv on (inv.id = ip.invoiceid and inv.type = '" + PAYABLE + "') ")
                .append(" join ").append(getCompanyId()).append(".manualjournal mj on mj.id = ip.manualjournalid \n")
                .append("left join ").append(getCompanyId()).append(".reference ips on ips.id = ip.statusid ").append("\n")
                .append("where ip.deleted is not true and ip.manualjournalid is not null ").append("\n")
                .append("and to_date(to_char(ip.paymentDate,'yyyy-MM-dd'),'yyyy-MM-dd') <= '").append(dateFormat.format(date)).append("' ").append("\n")
//                .append("and (ips is null or ips.code != '").append(EdsInvoicePayment.REVERSED).append("') ")
                .append(" group by ip.crmaccountid, ip.manualjournalid) ip on (ip.manualjournalid = mj.id and ip.crmaccountid = c.id) \n");
        sql.append("LEFT JOIN ").append("(select customersupplierid as crmaccountid, manualjournalid, sum(coalesce(cp.amount/(case when mj.currencyid = cp.currencyid then mj.exchangerate else coalesce(cp.exchangerate,1) end), 0)) amount from ").append(getCompanyId())
                .append(".customerPayment cp \n")
                .append("join ").append(getCompanyId()).append(".manualjournal mj on mj.id = cp.manualjournalid \n")
                .append("where cp.deleted is not true and cp.manualjournalid is not null \n")
                .append("and cp.type = ").append(EdsCustomerSupplierPayment.SUPPLIER_PAYMENT).append(" \n")
                .append("and to_date(to_char(cp.paymentDate,'yyyy-MM-dd'),'yyyy-MM-dd') <= '").append(dateFormat.format(date)).append("' ").append("\n")
                .append("group by cp.customersupplierid, cp.manualjournalid) csp on (csp.manualjournalid = mj.id and csp.crmaccountid = c.id) ").append("\n");
        sql.append("WHERE t.deleted is not true and mj.deleted is not true and r.code = 'POST' ").append("\n");
        sql.append("AND (a.key in (" + EdsAccount.ACCOUNTS_PAYABLE + "," + EdsAccount.PREPAID_EXPANSES + ") or a.groupKey in (" + EdsAccount.ACCOUNTS_PAYABLE + "," + EdsAccount.PREPAID_EXPANSES + ") ) \n");
        sql.append("AND to_date(to_char(t.journaldate,'yyyy-MM-dd'),'yyyy-MM-dd') <= '").append(dateFormat.format(date)).append("' ").append("\n");
        sql.append("GROUP BY c.id, c.name, mj.id, mj.reference, t.journaldate ").append("\n");

        if (isExcludePrePayments) {
            sql.append(" HAVING max(coalesce(ip.amount,0)) + sum(coalesce(ti.credit,0)) - sum(coalesce(ti.debit,0)) - max(coalesce(csp.amount,0)) > 0 \n");
        }

        sql.append("UNION ALL ").append("\n");

        //Bank Transfer Transaction
        sql.append("SELECT ").append("\n");
        sql.append("       c.id clientOrSupplierId,").append("\n");
        sql.append("       c.name clientOrSupplierName,").append("\n");
        sql.append("       (case when bt.transferType = " + RECEIVE_MONEY + " then 'Bank Receipt'" + "                when bt.transferType = " + CASH_RECEIPT + " then 'Cash Receipt'" + "                when bt.transferType = " + SPEND_MONEY + " then 'Bank Payment' else 'Cash Payment' end) as typeName,").append("\n");
        sql.append("       'CUSTOMER' as accountType, ").append("\n");
        sql.append("       bt.number as invoiceNumber,").append("\n");
//        sql.append("       bt.reference as reference,").append("\n");
        sql.append("       bt.id as objectID, ").append("\n");
        sql.append("       max(coalesce(ip.amount,0)) - sum(coalesce(ti.debit,0)) + sum(coalesce(ti.credit,0)) as amount,").append("\n");
        sql.append("       to_date(to_char(t.journaldate,'yyyy-MM-dd'),'yyyy-MM-dd') due_date, ").append("\n");
        sql.append("       to_date(to_char(t.journaldate,'yyyy-MM-dd'),'yyyy-MM-dd') date, ").append("\n");
        sql.append("       (to_date('").append(dateFormat.format(date)).append("','yyyy-MM-dd') - to_date(to_char(t.journaldate,'yyyy-MM-dd'),'yyyy-MM-dd')) aging, ").append("\n");
        sql.append("       NULL::integer[] as journalId, ").append("\n");
        sql.append("       NULL::numeric[] as exchangeRates, ").append("\n");
        sql.append("       '' AS currencyName, ").append("\n");
        sql.append("       NULL::numeric[] AS currencyDifference ").append("\n");
        sql.append("FROM ").append(getCompanyId()).append(".transactionitem ti ").append("\n");
        sql.append("INNER JOIN ").append(getCompanyId()).append(".account a on a.id = ti.accountid").append("\n");
        sql.append("INNER JOIN ").append(getCompanyId()).append(".transaction t on t.id = ti.transactionid ").append("\n");
        sql.append("INNER JOIN ").append(getCompanyId()).append(".spendreceivemoney bt on bt.id = t.banktransferid ").append("\n");
        sql.append("LEFT JOIN ").append(getCompanyId()).append(".crmaccount c on (c.id = t.clientid or c.id = ti.crmaccount_id) ").append("\n");
        sql.append("LEFT JOIN ").append("(select ip.crmaccountid, ip.bankTransferID, sum(coalesce(coalesce(ip.amountininvoicecurrency, ip.amount)/(case when bt.currencyid = ip.currencyID then bt.exchangerate else coalesce(ip.exchangerate,1) end), 0)) amount ")
                .append(" from ").append(getCompanyId()).append(".invoicepayments ip ").append("\n")
                .append(" join ").append(getCompanyId()).append(".invoice inv on (inv.id = ip.invoiceid and inv.type = '" + PAYABLE + "') ")
                .append(" join ").append(getCompanyId()).append(".spendreceivemoney bt on bt.id = ip.bankTransferID \n")
                .append("left join ").append(getCompanyId()).append(".reference ips on ips.id = ip.statusid ").append("\n")
                .append("where ip.deleted is not true and ip.bankTransferID is not null ").append("\n")
                .append("and to_date(to_char(ip.paymentDate,'yyyy-MM-dd'),'yyyy-MM-dd') <= '").append(dateFormat.format(date)).append("' ").append("\n")
//                .append("and (ips is null or ips.code != '").append(EdsInvoicePayment.REVERSED).append("') ")
                .append("  group by ip.crmaccountid, ip.bankTransferID) ip on (ip.bankTransferID = bt.id and ip.crmaccountid = c.id) \n");
        sql.append("WHERE t.deleted is not true and bt.deleted is not true ").append("\n");
        sql.append("AND (a.key in (" + EdsAccount.ACCOUNTS_PAYABLE + "," + EdsAccount.PREPAID_EXPANSES + ") or a.groupKey in (" + EdsAccount.ACCOUNTS_PAYABLE + "," + EdsAccount.PREPAID_EXPANSES + ") ) \n");
        sql.append("AND to_date(to_char(t.journaldate,'yyyy-MM-dd'),'yyyy-MM-dd') <= '").append(dateFormat.format(date)).append("' ").append("\n");
        sql.append("GROUP BY c.id, c.name, bt.id, bt.number, bt.transferType, t.journaldate ").append("\n");

        if (isExcludePrePayments) {
            sql.append(" HAVING max(coalesce(ip.amount,0)) - sum(coalesce(ti.debit,0)) + sum(coalesce(ti.credit,0)) > 0 \n");
        }

        sql.append("UNION ALL ").append("\n");

        //Supplier Transactions(Opening Balance)
        sql.append("SELECT ").append("\n");
        sql.append("       c.id clientOrSupplierId,").append("\n");
        sql.append("       c.name clientOrSupplierName,").append("\n");
        sql.append("       'Supplier balance' as typeName,").append("\n");
        sql.append("       'CUSTOMER' as accountType, ").append("\n");
        sql.append("       '' as invoiceNumber,").append("\n");
//        sql.append("       '' as reference,").append("\n");
        sql.append("       null as objectID, ").append("\n");
        sql.append("       sum(coalesce(ti.credit, 0)) + sum(coalesce(ip.amount, 0)) - sum(coalesce(ti.debit, 0)) - sum(coalesce(csp.amount,0)) as amount,").append("\n");
        sql.append("       to_date(to_char(t.journaldate,'yyyy-MM-dd'),'yyyy-MM-dd') due_date, ").append("\n");
        sql.append("       to_date(to_char(t.journaldate,'yyyy-MM-dd'),'yyyy-MM-dd') date, ").append("\n");
        sql.append("       (to_date('").append(dateFormat.format(date)).append("','yyyy-MM-dd') - to_date(to_char(t.journaldate,'yyyy-MM-dd'),'yyyy-MM-dd')) aging, ").append("\n");
        sql.append("       NULL::integer[] as journalId, ").append("\n");
        sql.append("       NULL::numeric[] as exchangeRates, ").append("\n");
        sql.append("       '' AS currencyName, ").append("\n");
        sql.append("       NULL::numeric[] AS currencyDifference ").append("\n");
        sql.append("FROM ").append(getCompanyId()).append(".transactionitem ti ").append("\n");
        sql.append("INNER JOIN ").append(getCompanyId()).append(".account a on a.id = ti.accountid").append("\n");
        sql.append("INNER JOIN ").append(getCompanyId()).append(".transaction t on t.id = ti.transactionid ").append("\n");
        sql.append("INNER JOIN ").append(getCompanyId()).append(".crmaccount c on c.id = t.supplierid ").append("\n");
        sql.append("LEFT JOIN ").append("(select crmaccountid, sum(coalesce(coalesce(ip.amountininvoicecurrency, ip.amount)/exchangerate, 0)) amount ")
                .append(" from ").append(getCompanyId()).append(".invoicepayments ip").append("\n")
                .append("left join ").append(getCompanyId()).append(".reference ips on ips.id = ip.statusid ").append("\n")
                .append("where ip.deleted is not true and ip.type = 'PAYABLE_CRM_ACCOUNT_CREDIT' ").append("\n")
                .append("and to_date(to_char(ip.paymentDate,'yyyy-MM-dd'),'yyyy-MM-dd') <= '").append(dateFormat.format(date)).append("' ").append("\n")
//                .append("and (ips is null or ips.code != '").append(EdsInvoicePayment.REVERSED).append("') ")
                .append("  group by ip.crmaccountid) ip on ip.crmaccountid = c.id ").append("\n");
        sql.append("LEFT JOIN ").append("(select customersupplierid as crmaccountid, sum(coalesce(amount, 0)/COALESCE(exchangerate,1)) amount from ").append(getCompanyId())
                .append(".customerPayment where deleted is not true and manualjournalid is null ")
                .append("and type = ").append(EdsCustomerSupplierPayment.SUPPLIER_PAYMENT).append(" \n")
                .append("and to_date(to_char(paymentDate,'yyyy-MM-dd'),'yyyy-MM-dd') <= '").append(dateFormat.format(date)).append("' ").append("\n")
                .append("group by customersupplierid) csp on csp.crmaccountid = c.id ").append("\n");
        sql.append("WHERE t.deleted is not true ").append("\n");
        sql.append("AND t.dtype = 'EdsSupplierTransaction' ").append("\n");
        sql.append("AND (a.key in (" + EdsAccount.ACCOUNTS_PAYABLE + "," + EdsAccount.PREPAID_EXPANSES + ") or a.groupKey in (" + EdsAccount.ACCOUNTS_PAYABLE + "," + EdsAccount.PREPAID_EXPANSES + ") ) \n");
        sql.append("AND to_date(to_char(t.journaldate,'yyyy-MM-dd'),'yyyy-MM-dd') <= '").append(dateFormat.format(date)).append("' ").append("\n");
        sql.append("GROUP BY c.id, c.name, t.journaldate ").append("\n");

        if (isExcludePrePayments) {
            sql.append(" HAVING sum(coalesce(ti.credit, 0)) + sum(coalesce(ip.amount, 0)) - sum(coalesce(ti.debit, 0)) - sum(coalesce(csp.amount,0)) > 0 \n");
        }

        sql.append("UNION ALL ").append("\n");

        //Other Transactions
        sql.append("SELECT ").append("\n");
        sql.append("       c.id clientOrSupplierId,").append("\n");
        sql.append("       c.name clientOrSupplierName,").append("\n");
        sql.append("       (case when t.dtype = 'EdsBankTransferTransaction' then 'Bank Transfer' else 'Other' end) as typeName,").append("\n");
        sql.append("       'CUSTOMER' as accountType, ").append("\n");
        sql.append("       '' as invoiceNumber,").append("\n");
//        sql.append("       '' as reference,").append("\n");
        sql.append("       null as objectID, ").append("\n");
        sql.append("       sum(coalesce(ti.credit, 0)) - sum(coalesce(ti.debit, 0)) as amount,").append("\n");
        sql.append("       to_date(to_char(t.journaldate,'yyyy-MM-dd'),'yyyy-MM-dd') due_date, ").append("\n");
        sql.append("       to_date(to_char(t.journaldate,'yyyy-MM-dd'),'yyyy-MM-dd') date, ").append("\n");
        sql.append("       (to_date('").append(dateFormat.format(date)).append("','yyyy-MM-dd') - to_date(to_char(t.journaldate,'yyyy-MM-dd'),'yyyy-MM-dd')) aging, ").append("\n");
        sql.append("       NULL::integer[] as journalId, ").append("\n");
        sql.append("       NULL::numeric[] as exchangeRates, ").append("\n");
        sql.append("       '' AS currencyName, ").append("\n");
        sql.append("       NULL::numeric[] AS currencyDifference ").append("\n");
        sql.append("FROM ").append(getCompanyId()).append(".transactionitem ti ").append("\n");
        sql.append("INNER JOIN ").append(getCompanyId()).append(".account a on a.id = ti.accountid").append("\n");
        sql.append("INNER JOIN ").append(getCompanyId()).append(".transaction t on t.id = ti.transactionid ").append("\n");
        sql.append("LEFT JOIN ").append(getCompanyId()).append(".crmaccount c on (c.id = t.supplierid or c.id = ti.crmaccount_id)").append("\n");
        sql.append("WHERE t.deleted is not true ").append("\n");
        sql.append("AND t.dtype not in('EdsInvoiceTransaction', 'EdsInvoicePaymentTransaction', 'EdsManualTransaction', 'EdsSupplierTransaction', 'EdsBankCheckTransaction'," +
                " 'EdsCusSuppPaymentTransaction', 'EdsFixedAssetTransaction', 'EdsBankTransferTransaction', 'EdsExpenseTransaction', 'EdsExpensePaymentTransaction')").append("\n");
        sql.append("AND (a.key in (" + EdsAccount.ACCOUNTS_PAYABLE + "," + EdsAccount.PREPAID_EXPANSES + ") or a.groupKey in (" + EdsAccount.ACCOUNTS_PAYABLE + "," + EdsAccount.PREPAID_EXPANSES + ") ) \n");
        sql.append("AND to_date(to_char(t.journaldate,'yyyy-MM-dd'),'yyyy-MM-dd') <= '").append(dateFormat.format(date)).append("' ").append("\n");
        sql.append("GROUP BY c.id, c.name, t.dtype, t.journaldate ").append("\n");


        sql.append("UNION ALL ").append("\n");

        //Gain and Loss Balance from Open Transaction
        sql.append("SELECT ").append("\n");
        sql.append("       c.id clientOrSupplierId,").append("\n");
        sql.append("       c.name clientOrSupplierName,").append("\n");
        sql.append("       'EXCHANGEGAINANDLOSS' as typeName,").append("\n");
        sql.append("       'CUSTOMER' as accountType, ").append("\n");
        sql.append("       '' as invoiceNumber,").append("\n");
        sql.append("       null as objectID, ").append("\n");
        sql.append("       round(sum(coalesce(ti.foreigncredit, 0) - coalesce(ti.foreigndebit, 0))/" + currentExRates + ",2)- (sum(coalesce(ti.credit, 0) - coalesce(ti.debit, 0))) as amount,").append("\n");
        sql.append("       max(to_date(to_char(t.journaldate,'yyyy-MM-dd'),'yyyy-MM-dd')) due_date, ").append("\n");
        sql.append("       min(to_date(to_char(t.journaldate,'yyyy-MM-dd'),'yyyy-MM-dd')) date, ").append("\n");
        sql.append("       max(to_date('").append(dateFormat.format(date)).append("','yyyy-MM-dd')) - min(to_date(to_char(t.journaldate,'yyyy-MM-dd'),'yyyy-MM-dd')) aging, ").append("\n");
        sql.append("       ARRAY_AGG(t.journalid) AS journalId, ").append("\n");
        sql.append("       ARRAY_AGG(t.exchangerate) as exchangeRates, ").append("\n");
        sql.append("       MAX(cr.name) AS currencyName, ").append("\n");
        sql.append("       ARRAY_AGG(round((coalesce(ti.foreigncredit, 0) - coalesce(ti.foreigndebit, 0))/" + currentExRates + ",2)- ((coalesce(ti.credit, 0) - coalesce(ti.debit, 0)))) AS currencyDifference ").append("\n");
        sql.append("       FROM ").append(getCompanyId()).append(".transactionitem ti ").append("\n");
        sql.append("       JOIN ").append(getCompanyId()).append(".account a on a.id = ti.accountid").append("\n");
        sql.append("       JOIN currency cr on cr.id = a.currencyid").append("\n");
        sql.append("       JOIN ").append(getCompanyId()).append(".transaction t on t.id = ti.transactionid ").append("\n");
        sql.append("       JOIN ").append(getCompanyId()).append(".crmaccount c on (c.id = t.supplierid or c.id = ti.crmaccount_id)").append("\n");
        sql.append("       WHERE t.deleted is not true and t.currencyid!=" + fs.getCurrency().getObjectID() + " and a.foreignAccount ").append("\n");
        sql.append("       AND (a.key in (" + EdsAccount.ACCOUNTS_PAYABLE + "," + EdsAccount.PREPAID_EXPANSES + ") or a.groupKey in (" + EdsAccount.ACCOUNTS_PAYABLE + "," + EdsAccount.PREPAID_EXPANSES + ") ) \n");
        sql.append("       AND to_date(to_char(t.journaldate,'yyyy-MM-dd'),'yyyy-MM-dd') <= '").append(dateFormat.format(date)).append("' ").append("\n");
        sql.append("       GROUP BY c.id, c.name, a.id ").append("\n");

        sql.append(") t WHERE amount != 0 \n");
        return sql.toString();
    }

    @Override
    public List<EdsAccount> getGroupAccounts(ListingFilterParameter filterParametrs, ArrayList<String> accountTypes) {
        StringBuilder queryBuilder = new StringBuilder();
        filterParametrs.setAccountTypes(accountTypes);
        queryBuilder.append("select acc from EdsAccount acc where ((acc.deleted is null or acc.deleted<>true) and acc.active is true ) ");

        if (filterParametrs.isValidSearchKey()) {
            queryBuilder.append("and lower(acc.name) like ('").append(filterParametrs.getSqlSearchKey()).append("') ");
        }
        if (filterParametrs.getAccountTypes() != null && !filterParametrs.getAccountTypes().isEmpty()) {
            queryBuilder.append(" AND acc.accountType.code IN ('").append(ServerUtils.getAsCommoDelimited(filterParametrs.getAccountTypes(), "0", "','")).append("')");
        }
        queryBuilder.append(" order by acc.id ");
        return find(queryBuilder.toString());
    }

    @Override
    public void setParentCodesToChilds(String updateQuery) {
        if (updateQuery != null && !updateQuery.isEmpty()) {
            updateNative(updateQuery);
        }
    }

    @Override
    public LinkedList<CashFlowItem> getCashFlowItems(String groupCode, ListingFilterParameter fp, String departmentAndTreeChildIDs, boolean isDebitAccount) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        Integer departmentID = fp.getDepartmentId();
        Date from = ServerUtils.parseFilterParameterDate(fp.getStartDateNC());
        Date to = ServerUtils.parseFilterParameterDate(fp.getEndDateNC());

        StringBuilder sql = new StringBuilder();
        sql.append("SELECT ti.accountid, a.name, a.accountcode as code, a.foreignaccount, a.currencyid, \n")
                .append("SUM(coalesce((case when a.foreignaccount = true then ti.foreigndebit else ti.debit end),0)) as debit, \n")
                .append("SUM(coalesce((case when a.foreignaccount = true then ti.foreigncredit else ti.credit end), 0)) as credit, \n")
                .append("p.id as parentid, p.name as parentname, p.accountcode as parentcode")
                .append(" FROM ").append(getCompanyId()).append(".transactionitem ti ");
        sql.append("INNER JOIN ").append(getCompanyId()).append(".transaction t ON t.id = ti.transactionid ");
        sql.append("INNER JOIN ").append(getCompanyId()).append(".account a ON a.id = ti.accountid ");
        sql.append("LEFT OUTER JOIN ").append(getCompanyId()).append(".account p ON p.id = a.parentid ");
        sql.append("INNER JOIN ").append(getPublic()).append(".accounttype at ON at.id = a.accountTypeId ");
        sql.append("WHERE (t.deleted is null or t.deleted<>true) ");

        if ("CURRENT_ASSET".equals(groupCode)) {
            sql.append("AND at.code='").append(EdsAccountType.CURRENT_ASSET).append("' AND (a.enablePayments is null or a.enablePayments<>true) ");
        } else if ("NON_CURRENT_ASSET".equals(groupCode)) {
            sql.append("AND at.code='").append(EdsAccountType.NON_CURRENT_ASSET).append("' ");
        } else if ("PREPAYMENT".equals(groupCode)) {
            sql.append("AND at.code='").append(EdsAccountType.PREPAYMENT).append("' ");
        } else if ("CURRENT_LIABILITY".equals(groupCode)) {
            sql.append("AND at.code='").append(EdsAccountType.CURRENT_LIABILITY).append("' ");
        } else if ("ACCUMULATED_DEPRECIATION".equals(groupCode)) {
            sql.append("AND a.key=").append(EdsAccount.ACCUMULATED_DEPRECIATION).append(" ");
        } else if ("FIXED_ASSET".equals(groupCode)) {
            sql.append("AND at.code='").append(EdsAccountType.FIXED_ASSET).append("' AND (a.key is null or a.key!=").append(EdsAccount.ACCUMULATED_DEPRECIATION).append(") ");
        } else if ("LIABILITY".equals(groupCode)) {
            sql.append("AND at.code='").append(EdsAccountType.LIABILITY).append("' ");
        } else if ("LONG_TERM_LIABILITY".equals(groupCode)) {
            sql.append("AND at.code='").append(EdsAccountType.LONG_TERM_LIABILITY).append("' ");
        } else if ("EQUITY".equals(groupCode)) {
            sql.append("AND at.code='").append(EdsAccountType.EQUITY).append("' ");
        } else if ("CASH_AT_THE_BEGINNING_OF_THE_PERIOD".equals(groupCode)) {
            sql.append("AND (at.code='").append(EdsAccountType.BANK).append("' or a.enablePayments=true) ");
        }

        if (departmentID != null) {
            sql.append("AND ti.department_id in (").append(departmentAndTreeChildIDs).append(") ");
        }
        if (from != null && to != null) {
            sql.append("AND to_date(to_char(t.journalDate, 'yyyy-MM-dd'),'yyyy-MM-dd') BETWEEN '").append(dateFormat.format(from)).append("' AND '").append(dateFormat.format(to)).append("' ");
        } else if (from != null) {
            sql.append("AND to_date(to_char(t.journalDate, 'yyyy-MM-dd'),'yyyy-MM-dd') >= '").append(dateFormat.format(from)).append("' ");
        } else if (to != null) {
            sql.append("AND to_date(to_char(t.journalDate, 'yyyy-MM-dd'),'yyyy-MM-dd') <= '").append(dateFormat.format(to)).append("' ");
        }

        sql.append("GROUP BY ti.accountid, a.name, a.accountcode, a.foreignaccount, a.currencyid, p.id, p.name, p.accountcode ");


        List<Object[]> dataList = findNative(sql.toString());

        LinkedList<CashFlowItem> itemList = new LinkedList<>();
        Set<Integer> accountIds = new HashSet<>();
        if (dataList != null && !dataList.isEmpty()) {
            String localizeName = "";
            for (Object[] data : dataList) {
                Integer accountId = (Integer) data[0];
                localizeName = (String) data[1];
                localizeName = accountingLocalizer.localize(localizeName != null ? localizeName.replace(" ", "_") : localizeName, localizeName);
                CashFlowItem item = new CashFlowItem();
                item.setAccount(new SelectItem(accountId, localizeName));
                item.setCode(data[2] != null ? (String) data[2] : "");
                item.setForeignAccount(data[3] != null ? (Boolean) data[3] : Boolean.FALSE);
                item.setAccountCurrencyId((Integer) data[4]);

                BigDecimal debit = (BigDecimal) data[5];
                BigDecimal credit = (BigDecimal) data[6];
                item.setBalance(isDebitAccount ? debit.subtract(credit) : credit.subtract(debit));
                if (data[7] != null) {
                    item.setParentId((Integer) data[7]);
                    localizeName = (String) data[8];
                    localizeName = accountingLocalizer.localize(localizeName != null
                            ? localizeName.replace(" ", "_")
                            : localizeName, localizeName);
                    item.setParentName(localizeName);
                    item.setParentCode((String) data[9]);
                }
                itemList.add(item);

                accountIds.add(accountId);
            }
        }

        if ("CURRENT_LIABILITY".equals(groupCode) || "CURRENT_ASSET".equals(groupCode)) {
            Calendar calendar = Calendar.getInstance();
            calendar.set(1970, 0, 1, 0, 0, 0);
            from = calendar.getTime();

            sql = new StringBuilder();

            sql.append("SELECT distinct ti.accountid, a.name, a.accountcode as code, a.foreignaccount, a.currencyid, \n")
                    .append("SUM(coalesce((case when a.foreignaccount = true then ti.foreigndebit else ti.debit end),0)) as debit, \n")
                    .append("SUM(coalesce((case when a.foreignaccount = true then ti.foreigncredit else ti.credit end), 0)) as credit, \n")
                    .append("p.id as parentid, p.name as parentname, p.accountcode as parentcode")
                    .append(" FROM ").append(getCompanyId()).append(".transactionitem ti ");
            sql.append("INNER JOIN ").append(getCompanyId()).append(".transaction t ON t.id = ti.transactionid ");
            sql.append("INNER JOIN ").append(getCompanyId()).append(".account a ON a.id = ti.accountid ");
            sql.append("LEFT OUTER JOIN ").append(getCompanyId()).append(".account p ON p.id = a.parentid ");
            sql.append("INNER JOIN ").append(getPublic()).append(".accounttype at ON at.id = a.accountTypeId ");
            sql.append("WHERE (t.deleted is null or t.deleted<>true) ");
            sql.append(" AND a.foreignaccount = true ");

            if ("CURRENT_ASSET".equals(groupCode)) {
                sql.append("AND at.code='").append(EdsAccountType.CURRENT_ASSET).append("' AND (a.enablePayments is null or a.enablePayments<>true) ");
            } else if ("NON_CURRENT_ASSET".equals(groupCode)) {
                sql.append("AND at.code='").append(EdsAccountType.NON_CURRENT_ASSET).append("' ");
            } else if ("PREPAYMENT".equals(groupCode)) {
                sql.append("AND at.code='").append(EdsAccountType.PREPAYMENT).append("' ");
            } else if ("CURRENT_LIABILITY".equals(groupCode)) {
                sql.append("AND at.code='").append(EdsAccountType.CURRENT_LIABILITY).append("' ");
            } else if ("ACCUMULATED_DEPRECIATION".equals(groupCode)) {
                sql.append("AND a.key=").append(EdsAccount.ACCUMULATED_DEPRECIATION).append(" ");
            } else if ("FIXED_ASSET".equals(groupCode)) {
                sql.append("AND at.code='").append(EdsAccountType.FIXED_ASSET).append("' AND (a.key is null or a.key!=").append(EdsAccount.ACCUMULATED_DEPRECIATION).append(") ");
            } else if ("LIABILITY".equals(groupCode)) {
                sql.append("AND at.code='").append(EdsAccountType.LIABILITY).append("' ");
            } else if ("LONG_TERM_LIABILITY".equals(groupCode)) {
                sql.append("AND at.code='").append(EdsAccountType.LONG_TERM_LIABILITY).append("' ");
            } else if ("EQUITY".equals(groupCode)) {
                sql.append("AND at.code='").append(EdsAccountType.EQUITY).append("' ");
            } else if ("CASH_AT_THE_BEGINNING_OF_THE_PERIOD".equals(groupCode)) {
                sql.append("AND (at.code='").append(EdsAccountType.BANK).append("' or a.enablePayments=true) ");
            }

            if (departmentID != null) {
                sql.append("AND ti.department_id in (").append(departmentAndTreeChildIDs).append(") ");
            }

            if (from != null && to != null) {
                sql.append("AND to_date(to_char(t.journalDate, 'yyyy-MM-dd'),'yyyy-MM-dd') BETWEEN '").append(dateFormat.format(from)).append("' AND '").append(dateFormat.format(to)).append("' ");
            } else if (from != null) {
                sql.append("AND to_date(to_char(t.journalDate, 'yyyy-MM-dd'),'yyyy-MM-dd') >= '").append(dateFormat.format(from)).append("' ");
            } else if (to != null) {
                sql.append("AND to_date(to_char(t.journalDate, 'yyyy-MM-dd'),'yyyy-MM-dd') <= '").append(dateFormat.format(to)).append("' ");
            }

            sql.append("GROUP BY ti.accountid, a.name, a.accountcode, a.foreignaccount, a.currencyid, p.id, p.name, p.accountcode ");


            dataList = findNative(sql.toString());

            if (dataList != null && !dataList.isEmpty()) {
                String localizeName = "";
                for (Object[] data : dataList) {
                    Integer accountId = (Integer) data[0];
                    if (!accountIds.contains(accountId)) {
                        localizeName = (String) data[1];
                        localizeName = accountingLocalizer.localize(localizeName != null ? localizeName.replace(" ", "_") : localizeName, localizeName);
                        CashFlowItem item = new CashFlowItem();
                        item.setAccount(new SelectItem(accountId, localizeName));
                        item.setCode(data[2] != null ? (String) data[2] : "");
                        item.setForeignAccount(data[3] != null ? (Boolean) data[3] : Boolean.FALSE);
                        item.setAccountCurrencyId((Integer) data[4]);
                        item.setGainAndLoss(true);

                        BigDecimal debit = (BigDecimal) data[5];
                        BigDecimal credit = (BigDecimal) data[6];
                        item.setBalance(isDebitAccount ? debit.subtract(credit) : credit.subtract(debit));
                        if (data[7] != null) {
                            item.setParentId((Integer) data[7]);
                            localizeName = (String) data[8];
                            localizeName = accountingLocalizer.localize(localizeName != null
                                    ? localizeName.replace(" ", "_")
                                    : localizeName, localizeName);
                            item.setParentName(localizeName);
                            item.setParentCode((String) data[9]);
                        }
                        itemList.add(item);
                        accountIds.add(accountId);
                    }
                }
            }
        }

        return itemList;
    }

    @Override
    public BigDecimal getCashFlowItemBalance(String groupCode, ListingFilterParameter fp, boolean isDebitAccount) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        Integer departmentID = fp.getDepartmentId();
        Date from = ServerUtils.parseFilterParameterDate(fp.getStartDateNC());
        Date to = ServerUtils.parseFilterParameterDate(fp.getEndDateNC());

        StringBuilder sql = new StringBuilder();
        sql.append("SELECT SUM(coalesce(ti.debit,0)) as debit, SUM(coalesce(ti.credit, 0)) as credit FROM ").append(getCompanyId()).append(".transactionitem ti ");
        sql.append("INNER JOIN ").append(getCompanyId()).append(".transaction t ON t.id = ti.transactionid ");
        sql.append("INNER JOIN ").append(getCompanyId()).append(".account a ON a.id = ti.accountid ");
        sql.append("INNER JOIN ").append(getPublic()).append(".accounttype at ON at.id = a.accountTypeId ");
        sql.append("WHERE (t.deleted is null or t.deleted<>true) ");

        if ("CASH_AT_THE_BEGINNING_OF_THE_PERIOD".equals(groupCode)) {
            sql.append("AND (at.code='").append(EdsAccountType.BANK).append("' or a.enablePayments=true) ");
        }

        if (departmentID != null) {
            sql.append("AND ti.department_id = ").append(departmentID).append(" ");
        }

        if (from != null && to != null) {
            sql.append("AND to_date(to_char(t.journalDate, 'yyyy-MM-dd'),'yyyy-MM-dd') BETWEEN '").append(dateFormat.format(from)).append("' AND '").append(dateFormat.format(to)).append("' ");
        } else if (from != null) {
            sql.append("AND to_date(to_char(t.journalDate, 'yyyy-MM-dd'),'yyyy-MM-dd') >= '").append(dateFormat.format(from)).append("' ");
        } else if (to != null) {
            sql.append("AND to_date(to_char(t.journalDate, 'yyyy-MM-dd'),'yyyy-MM-dd') <= '").append(dateFormat.format(to)).append("' ");
        }

        List<Object[]> dataList = findNative(sql.toString());
        if (dataList != null && !dataList.isEmpty()) {
            Object[] data = dataList.get(0);
            BigDecimal debit = (BigDecimal) data[0];
            BigDecimal credit = (BigDecimal) data[1];
            if (debit == null) {
                debit = BigDecimal.ZERO;
            }
            if (credit == null) {
                credit = BigDecimal.ZERO;
            }
            return isDebitAccount ? debit.subtract(credit) : credit.subtract(debit);
        }
        return BigDecimal.ZERO;
    }

    @Override
    public List<EdsBrand> getBrandList(ListingFilterParameter fp) {
        StringBuilder sql = new StringBuilder();
        sql.append("select b from EdsBrand b where ").append(ServerUtils.checkForDeleted("b.deleted"));
        if (fp.isLookUp()) {
            if (fp.isValidSearchKey()) {
                sql.append(" and lower(b.name) like '").append(fp.getSqlSearchKey()).append("' ");
            }
            sql.append(" order by b.name");
        }
        return findInterval(sql.toString(), fp.getStart(), fp.getLimit());
    }

    @Override
    public List<EdsRFQ> getRFQList(ListingFilterParameter fp) {
        StringBuilder sql = new StringBuilder();
        sql.append("select rfq from EdsRFQ rfq where ").append(ServerUtils.checkForDeleted("rfq.deleted"));
        if (fp.isLookUp()) {
            if (fp.isValidSearchKey()) {
                sql.append(" and lower(rfq.number) like '").append(fp.getSqlSearchKey()).append("' ");
            }
            sql.append(" order by rfq.number");
        }
        return findInterval(sql.toString(), fp.getStart(), fp.getLimit());
    }

    @Override
    public List<EdsRFP> getRFPList(ListingFilterParameter fp) {
        StringBuilder sql = new StringBuilder();
        sql.append("select rfp from EdsRFP rfp where ").append(ServerUtils.checkForDeleted("rfp.deleted"));
        if (fp.isLookUp()) {
            if (fp.isValidSearchKey()) {
                sql.append(" and lower(rfp.number) like '").append(fp.getSqlSearchKey()).append("' ");
            }
            sql.append(" order by rfp.number");
        }
        return findInterval(sql.toString(), fp.getStart(), fp.getLimit());
    }

    @Override
    public Map<String, Integer> getAccountAsMapByCode(ListingFilterParameter fp) {
        StringBuilder sql = new StringBuilder();
        sql.append("select replace(a.accountCode, '-','') code, a.id from ").append(getCompanyId()).append(".account a where a.deleted is not true");
        if (fp != null && fp.getCurrencyID() != null) {
            sql.append(" and a.currencyid = ").append(fp.getCurrencyID());
        }
        List<Object[]> list = findNative(sql.toString());

        Map<String, Integer> map = new HashMap<>();
        if (list != null && !list.isEmpty()) {
            for (Object[] objects : list) {
                if (objects[0] != null && objects[1] != null) {
                    map.put((String) objects[0], (Integer) objects[1]);
                }
            }

            return map;
        }
        return null;
    }

    @Override
    public List<EdsAccount> getAccountsReceivablePayable(ListingFilterParameter filterParametrs) {
        Integer key = Constants.RECEIVABLE.equals(filterParametrs.getAccountType()) ? EdsAccount.ACCOUNTS_RECEIVABLE : EdsAccount.ACCOUNTS_PAYABLE;

        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("select acc from EdsAccount acc where ((acc.deleted is null or acc.deleted<>true) and acc.active is true) ");
        queryBuilder.append("and (acc.key = ").append(key).append(" or acc.groupKey = ").append(key);
        if (filterParametrs.isPrepayment()) {
            queryBuilder.append(" or acc.key=").append(Constants.RECEIVABLE.equals(filterParametrs.getAccountType()) ? EdsAccount.UNEARNED_REVENUE : EdsAccount.PREPAID_EXPANSES);
        }
        queryBuilder.append(") ");
        queryBuilder.append("and (acc.currency.objectID = ").append(filterParametrs.getBaseCurrencyID()).append(" or acc.currency.objectID = ").append(filterParametrs.getCurrencyID()).append(") ");
        queryBuilder.append("order by acc.id ");
        return find(queryBuilder.toString());
    }

    @Override
    public EdsAccount getDefaultAccount(Integer accountKey) {
        List<EdsAccount> edsAccountList = find("select acc from EdsAccount acc where acc.isDefaultAccount=true and (acc.key=? or acc.groupKey=?) and (acc.deleted is null or acc.deleted<>true)", accountKey, accountKey);
        if (edsAccountList.isEmpty()) {
            return getAccountByKey(accountKey);
        }
        return edsAccountList.get(0);
    }

    @Override
    public void clearDefaultAccount(Integer accountKey) {
        update("update EdsAccount set isDefaultAccount = false where (key=? or groupKey=?)", accountKey, accountKey);
    }

    @Override
    public HashMap<String, EdsAccount> getAccountAsMap(ListingFilterParameter fp) {
        HashMap<String, EdsAccount> map = new HashMap<>();
        StringBuilder sql = new StringBuilder();
        sql.append("select ac.*, 0 as clazz_ from ").append(getCompanyId()).append(".account ac ");
        sql.append(" left join ").append("accountType at on ac.accountTypeId = at.id ");
        sql.append(" where ").append(ServerUtils.checkForDeleted("ac.deleted"));

        if (fp.getParameters() != null) {
            sql.append(" and ( at.code = '").append(fp.getParameters()[0]).append("' ");
            sql.append(" or at.code = '").append(fp.getParameters()[1]).append("') ");
            if (BankAccountTypeEnum.CURRENT_ASSET.getCode().equals(fp.getParameters()[0])) {
                sql.append(" and ac.enablePayments = true");
            }
        }

        List<EdsAccount> list = findNative(sql.toString(), EdsAccount.class);
        if (list != null && !list.isEmpty()) {
            if (fp.isWithCode()) {
                for (EdsAccount account : list) {
                    map.put(account.getAccountCode() != null ? account.getAccountCode().trim() : null, account);
                }
            } else {
                for (EdsAccount account : list) {
                    map.put(account.getName() != null ? account.getName().trim() : null, account);

                }
            }
        }
        return map;
    }

    @Override
    public List<Integer> getDeletedAccountListForSolr(SolrReindexRpc solrReindex) {
        return find("select ac.objectID from EdsAccount ac " +
                "   where ac.deleted=true " +
                "       and ac.lastUpdatedDate>='" + solrReindex.getLastUpdateTime() + "'"
                + (solrReindex.getLastUpdateEndTime() != null ? " and ac.lastUpdateTime<='" + solrReindex.getLastUpdateEndTime() + "'" : ""));
    }

    @Override
    public List<Integer> getAccountIdsWithLimit(Integer startat, Integer limit) {
        return findLimited("select ac.objectID from EdsAccount ac " +
                "    where ac.objectID > ? " +
                "        and " + ServerUtils.checkForDeleted("ac.deleted") +
                "        order by ac.objectID", limit, startat);
    }

    @Override
    public List<Integer> getAccountIdsByIds(String ids) {
        return find("select ac.objectID from EdsAccount ac " +
                "   where ac.objectID in (" + ids + ") " +
                "       and " + ServerUtils.checkForDeleted("ac.deleted"));
    }

    @Override
    public List<EdsAccount> getAccountListForSolr(SolrReindexRpc solrReindex, int startat, int limit) {
        Map<String, Object> params = new HashMap<>();
        StringBuilder sqlQuery = new StringBuilder("select ac from EdsAccount ac where ");
        if (!solrReindex.isAllReindex() && solrReindex.getLastUpdateTime() != null) {
            params.put("modifiedDate", solrReindex.getLastUpdateTime());
            sqlQuery.append("ac.lastUpdatedDate >= :modifiedDate and ");
            if (solrReindex.getLastUpdateEndTime() != null) {
                sqlQuery.append(" and ac.lastUpdateTime<='").append(solrReindex.getLastUpdateEndTime()).append("'");
            }
        }
        sqlQuery.append("(ac.deleted is null or ac.deleted is false) ");
        sqlQuery.append("order by ac.objectID asc ");
        return findIntervalByNamedParams(sqlQuery.toString(), startat, limit, params);
    }

    @Override
    public List<EdsAccount> getAccountsByTypeList(ListingFilterParameter fp) {
        StringBuilder sql = new StringBuilder();
        sql.append("select a from EdsAccount a ");
        sql.append(" where ").append(ServerUtils.checkForDeleted("a.deleted "));
        if (fp.getAccountTypes() != null && !fp.getAccountTypes().isEmpty()) {
            sql.append(" and a.accountType.category in (").append("'").append(ServerUtils.getAsCommoDelimited(fp.getAccountTypes(), "0", "','")).append("')");
        }
        if (fp.getAccountType() != null && !"".equals(fp.getAccountType())) {
            sql.append(" and a.accountType.code = ").append("'").append(fp.getAccountType()).append("'");
        }
        if (fp.getCategory() != null && !"".equals(fp.getCategory())) {
            sql.append(" and a.accountType.category = ").append("'").append(fp.getCategory()).append("'");
        }
        if (fp.getSqlSearchKey() != null && !"".equals(fp.getSqlSearchKey())) {
            String searchKey = fp.getSqlSearchKey();
            sql.append(" and (");
            sql.append("lower(a.codeString) like '").append(searchKey).append("' or ");
            sql.append("lower(a.name) like '").append(searchKey).append("' ) ");
        }
        return findInterval(sql.toString(), fp.getStart(), fp.getLimit());
    }

    /**
     * Retrive vat account by request
     * <type>
     * type param contains {RECEIVABLE, PAYABLE}
     * </type>
     *
     * @param type
     * @return
     */
    public EdsAccount getVatAccount(String type) {

        if (genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.USE_SINGLE_ACCOUNT_TO_VAT_TRANSACTION)) {
            return getAccountByKey(EdsAccount.VAT_PAYABLE);
        } else if (RECEIVABLE.equalsIgnoreCase(type)) {
            return getAccountByKey(EdsAccount.VAT_OUTPUT);
        } else if (PAYABLE.equalsIgnoreCase(type)) {
            return getAccountByKey(EdsAccount.VAT_INPUT);
        }

        return getAccountByKey(EdsAccount.VAT_PAYABLE);
    }

    @Override
    public Integer getVatAccountKey(String type) {
        if (genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.USE_SINGLE_ACCOUNT_TO_VAT_TRANSACTION)) {
            return EdsAccount.VAT_PAYABLE;
        } else if (RECEIVABLE.equalsIgnoreCase(type)) {
            return EdsAccount.VAT_OUTPUT;
        } else if (PAYABLE.equalsIgnoreCase(type)) {
            return EdsAccount.VAT_INPUT;
        }

        return EdsAccount.VAT_PAYABLE;
    }
}
