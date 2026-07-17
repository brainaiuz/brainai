package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.EdsProjectBudget;
import com.edatasite.workforce.core.domain.EdsProjectBudgetItem;
import com.edatasite.workforce.core.domain.accounting.EdsAccount;
import com.edatasite.workforce.core.domain.accounting.EdsAccountType;
import com.edatasite.workforce.core.domain.accounting.EdsManualJournal;
import com.edatasite.workforce.gwt.accounting.client.ui.AccountingConstants;
import com.edatasite.workforce.gwt.core.client.enums.GenericSettingsEnum;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.db.AccountingManager;
import com.edatasite.workforce.gwt.core.server.db.ProjectBudgetManager;
import com.edatasite.workforce.gwt.core.server.db.settings.GenericSettingsManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: 5/18/12
 * Time: 2:29 PM
 * To change this template use File | Settings | File Templates.
 */
@Repository("projectBudgetManager")
public class ProjectBudgetManagerImpl extends BaseManager<EdsProjectBudget> implements ProjectBudgetManager {

    @Autowired
    private GenericSettingsManager genericSettingsManager;
    @Autowired
    private AccountingManager accountingManager;

    public ProjectBudgetManagerImpl() {
        super(EdsProjectBudget.class);
    }

    @Override
    public EdsProjectBudget getBudgetByProject(Integer projectID) {
        return (EdsProjectBudget) findSingle("select pb from EdsProjectBudget pb where pb.project.objectID = ?", projectID);
    }

    @Override
    public HashMap<Integer, HashMap<String, EdsProjectBudgetItem>> getProjectBudgetItems(Integer accountID, EdsProjectBudget projectBudget, boolean isDetailedPurchasesEnabled) {
        List<EdsProjectBudgetItem> budgetItems;
        if (accountID != null) {
            budgetItems = (List<EdsProjectBudgetItem>) find("select pbi from EdsProjectBudgetItem pbi where pbi.projectBudget = ? and pbi.account.objectID = ? order by pbi.objectID", projectBudget, accountID);
        } else {
            budgetItems = (List<EdsProjectBudgetItem>) find("select pbi from EdsProjectBudgetItem pbi where pbi.projectBudget = ? order by pbi.objectID", projectBudget);
        }

        HashMap<Integer, HashMap<String, EdsProjectBudgetItem>> budgetItemsAsMap = new HashMap<>();
        for (EdsProjectBudgetItem pbi : budgetItems) {
            Integer accID = null;
            if (EdsProjectBudgetItem.EMPLOYEE_COST.equals(pbi.getType())) {
                accID = -1;
            } else if (EdsProjectBudgetItem.PURCHASE.equals(pbi.getType())) {
                accID = (isDetailedPurchasesEnabled && pbi.getAccount() != null) ? pbi.getAccount().getObjectID() : -2;
            } else {
                accID = pbi.getAccount().getObjectID();
            }
            if (budgetItemsAsMap.containsKey(accID)) {
                budgetItemsAsMap.get(accID).put(pbi.getPeriodKey(), pbi);
            } else {
                HashMap<String, EdsProjectBudgetItem> accountBudgetsMap = new HashMap<>();
                accountBudgetsMap.put(pbi.getPeriodKey(), pbi);
                budgetItemsAsMap.put(accID, accountBudgetsMap);
            }
        }

        return budgetItemsAsMap;
    }

    @Override
    public List<EdsAccount> getBudgetAccounts(EdsProjectBudget projectBudget, String type) {
        return (List<EdsAccount>) find("select distinct pbi.account from EdsProjectBudgetItem  pbi where pbi.projectBudget = ? and pbi.type = ? ", projectBudget, type);
    }

    @Override
    public HashMap<Integer, BigDecimal> getAccountsActualByProjectAndMonth(Integer accountID, Integer projectID, String type, Date startDate, Date endDate) {
        boolean isProjectInLineEnable = genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.PROJECT_IN_LINE_ITEM_ENABLE);
        String companyID = getCompanyId();

        if (Constants.PURCHASES_STR.equals(type)) {
            StringBuilder sql = new StringBuilder("SELECT ti.accountid, sum(coalesce(ti.debit,0)) FROM " + companyID + ".transactionitem ti ");
            sql.append(" INNER JOIN " + companyID + ".transaction t on t.id=ti.transactionid");
            sql.append(" INNER JOIN " + companyID + ".account a on a.id=ti.accountid");
            sql.append(" LEFT JOIN " + companyID + ".purchaseinvoice pi on pi.id=t.invoiceid");
            sql.append(" LEFT JOIN " + companyID + ".invoice i on i.id=pi.id");

            sql.append(" LEFT JOIN " + companyID + ".purchaseorder po on po.id=t.purchaseorder_id");
            sql.append(" LEFT JOIN " + companyID + ".quote q on po.id=q.id");
            sql.append(" LEFT JOIN " + companyID + ".reference st on st.id=coalesce(i.status_id, q.status_id)");
            sql.append(" WHERE " + ServerUtils.checkForDeleted("t.deleted") + " AND t.reversalid is null AND a.accountcode!='" + accountingManager.getVatAccountKey(Constants.PAYABLE) + "' ");

            if (accountID != null) {
                sql.append(" AND ti.accountid = " + accountID + " ");
            }
            if (!isProjectInLineEnable) {
                sql.append(" AND (i.relatedproject_id = " + projectID + " OR q.relatedproject_id = " + projectID + " ) ");
            } else {
                sql.append(" AND ti.project_id = " + projectID);
            }
            sql.append(" AND (st.code!='REVERSED' ) AND t.journaldate between ? and ? ");
            sql.append(" GROUP BY ti.accountid ");

            HashMap<Integer, BigDecimal> result = new HashMap<>();
            List<Object[]> dataList = (List<Object[]>) findNative(sql.toString(), startDate, endDate);
            for (Object[] data : dataList) {
                result.put((Integer) data[0], (BigDecimal) data[1]);
            }
            return result;
        } else {
            StringBuilder sql = new StringBuilder("SELECT ti.accountid, case when acct.category='EXPENSES' or acct.category='ASSETS' then sum(coalesce(ti.debit,0)) - sum(coalesce(ti.credit,0)) else sum(coalesce(ti.credit,0)) - sum(coalesce(ti.debit,0)) end FROM " + companyID + ".transactionitem ti ");
            sql.append(" INNER JOIN " + companyID + ".transaction t on t.id=ti.transactionid");
            sql.append(" INNER JOIN " + companyID + ".account acc on acc.id=ti.accountid");
            sql.append(" LEFT JOIN " + getPublic() + ".accounttype acct on acct.id=acc.accountTypeId");
            sql.append(" LEFT JOIN " + companyID + ".invoice i on i.id=t.invoiceid");
            sql.append(" LEFT JOIN " + companyID + ".reference invs on invs.id=i.status_id");
            sql.append(" LEFT JOIN " + companyID + ".quote po on po.id=t.purchaseorder_id");
            sql.append(" LEFT JOIN " + companyID + ".expensereport exp on exp.id=t.expenseReportid");
            sql.append(" LEFT JOIN " + companyID + ".reference exps on exps.id=exp.overallStatus");
            sql.append(" LEFT JOIN " + companyID + ".manualjournalitem mji on mji.transactionitemid=ti.id");
            sql.append(" LEFT JOIN " + companyID + ".manualjournal mj on mj.id=mji.manualjournalid");
            sql.append(" LEFT JOIN " + companyID + ".reference r on r.id=mj.overallStatus");
            sql.append(" LEFT JOIN " + companyID + ".spendreceivemoney srm on srm.id=t.banktransferid");
            sql.append(" LEFT JOIN " + companyID + ".adjustment_item sai on ti.stock_adjustment_item_id=sai.id");
            sql.append(" WHERE " + ServerUtils.checkForDeleted("t.deleted") + " AND t.reversalid is null ");
            if (accountID != null) {
                sql.append(" AND ti.accountid = " + accountID + " ");
            }
            if (!isProjectInLineEnable) {
                sql.append(" AND (i.relatedproject_id = " + projectID + " OR po.relatedproject_id = " + projectID
                        + " OR exp.projectId = " + projectID + " OR mji.project_id = " + projectID + " OR sai.projectid = " + projectID + " OR srm.projectid = " + projectID + ") ");
            } else {
                sql.append(" AND ti.project_id = " + projectID);
            }
            sql.append(" AND (invs.id is null OR invs.code!='" + Constants.REVERSED + "') AND (mj.id is null OR r.code='" + EdsManualJournal.POST + "') ");
            sql.append(" AND (exps.id is null OR exps.code!='EXPENSE_REVERSED') AND t.journaldate between ? and ? ");
            sql.append(" GROUP BY ti.accountid, srm.transferType, acct.category");

            HashMap<Integer, BigDecimal> result = new HashMap<>();
            List<Object[]> dataList = (List<Object[]>) findNative(sql.toString(), startDate, endDate);
            for (Object[] data : dataList) {
                Integer accountId = (Integer) data[0];
                BigDecimal amount = (BigDecimal) data[1];
                BigDecimal existingAmount = result.get(accountId);
                if (existingAmount != null) {
                    existingAmount = existingAmount.add(amount);
                } else
                    existingAmount = amount;
                result.put(accountId, existingAmount);
            }
            return result;
        }
    }

    @Override
    public BigDecimal getProjectIncome(Integer projectID) {
        String companyID = getCompanyId();
        String sql = "SELECT p.id, coalesce(sum(ti.credit),0) - coalesce(sum(ti.debit),0) FROM " + companyID + ".transactionitem ti " +
                " INNER JOIN " + companyID + ".transaction t on t.id=ti.transactionid " +
                " INNER JOIN " + companyID + ".account acc on acc.id=ti.accountid " +
                " LEFT JOIN " + getPublic() + ".accounttype acct on acct.id=acc.accountTypeId " +
                " LEFT JOIN " + companyID + ".invoice i on i.id=t.invoiceid " +
                " LEFT JOIN " + companyID + ".reference invs on invs.id=i.status_id " +
                " LEFT JOIN " + companyID + ".quote po on po.id=t.purchaseorder_id " +
                " LEFT JOIN " + companyID + ".expensereport exp on exp.id=t.expenseReportid " +
                " LEFT JOIN " + companyID + ".reference exps on exps.id=exp.overallStatus " +
                " LEFT JOIN " + companyID + ".manualjournalitem mji on mji.transactionitemid=ti.id " +
                " LEFT JOIN " + companyID + ".manualjournal mj on mj.id=mji.manualjournalid " +
                " LEFT JOIN " + companyID + ".reference r on r.id=mj.overallStatus " +
                " LEFT JOIN " + companyID + ".project p on p.id = (CASE WHEN i.relatedproject_id is not null THEN i.relatedproject_id " +
                " WHEN po.relatedproject_id is not null THEN po.relatedproject_id WHEN exp.projectId is not null THEN exp.projectId ELSE mji.project_id END) " +
                " WHERE " + ServerUtils.checkForDeleted("t.deleted") + " AND p.isDeleted is not TRUE AND  p.id = '" + projectID + "' " +
                " AND (invs is null OR invs.code!='" + Constants.REVERSED + "') " +
                " AND (mj is null OR r.code='" + EdsManualJournal.POST + "') " +
                " AND (exp is null OR exps.code!='EXPENSE_REVERSED')" +
                " AND acct.category in ('" + EdsAccountType.REVENUE + "')" +
                " GROUP BY p.id ";

        List<Object[]> dataList = (List<Object[]>) findNative(sql);
        for (Object[] data : dataList) {
            return data[1] != null ? (BigDecimal) data[1] : BigDecimal.ZERO;
        }
        return BigDecimal.ZERO;
    }

    @Override
    public HashMap<Integer, BigDecimal> getActualIncomeByProjectIDs(String projectIDs) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT t.projectid, SUM(coalesce(amount, 0)) FROM (");

        //PURCHASE INVOICE
        sql.append("SELECT DISTINCT ti.id, p.id AS projectid, ((CASE WHEN i.isCreditNote THEN -1 ELSE 1 END) * coalesce(ti.debit,ti.credit)) as amount FROM ").append(getCompanyId()).append(".transactionitem ti ");
        sql.append("INNER JOIN ").append(getCompanyId()).append(".transaction t on t.id = ti.transactionid ");
        sql.append("INNER JOIN ").append(getCompanyId()).append(".invoice i on i.id = t.invoiceid ");
        sql.append("INNER JOIN ").append(getCompanyId()).append(".invoiceitem ii on ii.invoice_id = i.id and ii.account_id = ti.accountid ");
        sql.append("INNER JOIN ").append(getCompanyId()).append(".reference invs on invs.id = i.status_id ");
        sql.append("INNER JOIN ").append(getCompanyId()).append(".project p on p.id = i.relatedproject_id ");
        sql.append("WHERE ").append(ServerUtils.checkForDeleted("t.deleted"));
        sql.append(" AND p.isDeleted is not true ");
        sql.append("AND i.type = 'RECEIVABLE' AND invs.code != 'REVERSED' ");
        sql.append("AND p.id IN (").append(projectIDs).append(") ");

        sql.append(" UNION ");

        //MANUAL JOURNAL WITH TYPE = 'POST'
        sql.append("SELECT DISTINCT ti.id, p.id AS projectid, coalesce(ti.debit,ti.credit) as amount FROM ").append(getCompanyId()).append(".transactionitem ti ");
        sql.append("INNER JOIN ").append(getCompanyId()).append(".transaction t on t.id = ti.transactionid ");
        sql.append("INNER JOIN ").append(getCompanyId()).append(".account acc on acc.id=ti.accountid ");
        sql.append("INNER JOIN ").append(getPublic()).append(".accounttype acct on acct.id=acc.accountTypeId ");
        sql.append("INNER JOIN ").append(getCompanyId()).append(".manualjournalitem mji on mji.transactionitemid=ti.id ");
        sql.append("INNER JOIN ").append(getCompanyId()).append(".manualjournal mj on mj.id=mji.manualjournalid ");
        sql.append("INNER JOIN ").append(getCompanyId()).append(".reference r on r.id=mj.overallStatus ");
        sql.append("INNER JOIN ").append(getCompanyId()).append(".project p on p.id = mji.project_id ");
        sql.append("WHERE ").append(ServerUtils.checkForDeleted("t.deleted"));
        sql.append(" AND p.isDeleted is not true ");
        sql.append(" AND r.code = '").append(EdsManualJournal.POST).append("' ");
        sql.append(" AND acct.category in ('").append(EdsAccountType.REVENUE).append("') ");
        sql.append("AND p.id IN (").append(projectIDs).append(") ");
        sql.append(") t GROUP BY t.projectid ");

        List<Object[]> actualIncomeList = findNative(sql.toString());
        HashMap<Integer, BigDecimal> result = new HashMap<>();
        for (Object[] actualIncome : actualIncomeList) {
            Integer projectID = (Integer) actualIncome[0];
            BigDecimal actualIncomeAmount = (BigDecimal) actualIncome[1];
            result.put(projectID, actualIncomeAmount);
        }

        return result;
    }

    @Override
    public HashMap<Integer, BigDecimal> getActualIncomeFromBudgetByProjectIDs(String projectIDs, boolean isAgencyFees) {
        List<String> status = Arrays.asList(Constants.APPROVE, Constants.OPEN, Constants.PAID, Constants.OVER_DUE);

        StringBuilder sql = new StringBuilder();
        sql.append("SELECT projectid, sum(amount) FROM (\n");

        if (isAgencyFees) {
            sql.append("SELECT p.id projectid, \n")
                    .append("(SUM(CASE WHEN i.isCreditNote THEN 0-ii.net ELSE ii.net END) \n")
                    .append("+ SUM(CASE WHEN i.taxCalculationType = 2 THEN \n")
                    .append("           CASE WHEN i.isCreditNote THEN 0-(round(coalesce(ii.taxamount,0)/i.exchangerate, 5) + round(coalesce(ii.doubletaxamount,0)/i.exchangerate, 5)) \n")
                    .append("                ELSE round(coalesce(ii.taxamount,0)/i.exchangerate, 5) + round(coalesce(ii.doubletaxamount,0)/i.exchangerate, 5) END \n")
                    .append("           ELSE 0 END)) as amount \n")
                    .append("FROM ").append(getCompanyId()).append(".saleinvoice si \n");
        } else {
            sql.append("""
                    SELECT p.id projectid, (sum(CASE WHEN i.isCreditNote THEN 0-ii.net ELSE ii.net END) + sum(CASE WHEN i.taxCalculationType = 1 THEN\s
                     (case when i.isCreditNote then round(coalesce(ii.taxamount,0)/i.exchangerate, 5) + round(coalesce(ii.doubletaxamount,0)/i.exchangerate, 5) else\s
                      0 - (round(coalesce(ii.taxamount,0)/i.exchangerate, 5) + round(coalesce(ii.doubletaxamount,0)/i.exchangerate, 5)) end) ELSE 0 END)) as amount FROM\s""").append(getCompanyId()).append(".saleinvoice si \n");
        }

        sql.append("INNER JOIN ").append(getCompanyId()).append(".invoice i ON i.id = si.id \n");
        sql.append("INNER JOIN ").append(getCompanyId()).append(".invoiceitem ii on ii.invoice_id = i.id \n");
        sql.append("INNER JOIN ").append(getCompanyId()).append(".reference s ON s.id = i.status_id \n");
        sql.append("INNER JOIN ").append(getCompanyId()).append(".project p ON (p.id = i.relatedproject_id or p.id = ii.project_id) \n");
        sql.append("WHERE i.deleted is not true and s.code in ('" + ServerUtils.getAsCommoDelimited(status, "0", "','") + "') \n");
        sql.append("AND p.id in (" + projectIDs + ") \n");
        sql.append("GROUP BY p.id \n");

        sql.append("UNION ALL \n");

        if (isAgencyFees) {
            sql.append("SELECT p.id projectid, \n")
                    .append("SUM(bti.amount/bt.exchangeRate) \n")
                    .append("+ SUM(case when bt.taxCalculationType = 2 THEN round(coalesce(bti.taxamount,0)/bt.exchangerate, 5) ELSE 0 END) AS amount \n")
                    .append("FROM ").append(getCompanyId()).append(".spendreceivemoney bt \n");
        } else {
            sql.append("SELECT p.id projectid, SUM(bti.amount/bt.exchangeRate) - SUM(case when bt.taxCalculationType = 1 THEN round(coalesce(bti.taxamount,0)/bt.exchangerate, 5) ELSE 0 END) as amount from ").append(getCompanyId()).append(".spendreceivemoney bt \n");
        }

        sql.append("INNER JOIN ").append(getCompanyId()).append(".spendreceivemoneyitem bti on bti.banktransferid = bt.id \n");
        sql.append("INNER JOIN ").append(getCompanyId()).append(".project p on p.id = bt.projectid or p.id = bti.projectid \n");
        sql.append("WHERE bt.deleted is not true and p.id in (").append(projectIDs).append(") \n");
        sql.append("AND bt.transferType in (" + AccountingConstants.RECEIVE_MONEY + ", " + AccountingConstants.CASH_RECEIPT + ") \n");
        sql.append("GROUP BY p.id \n");
        sql.append(") t \n");
        sql.append("GROUP BY projectid ");

        List<Object[]> actualIncomes = findNative(sql.toString());
        HashMap<Integer, BigDecimal> result = new HashMap<>();
        for (Object[] actualIncome : actualIncomes) {
            Integer projectID = (Integer) actualIncome[0];
            BigDecimal actualIncomeAmount = (BigDecimal) actualIncome[1];
            result.put(projectID, actualIncomeAmount);
        }

        return result;
    }

    @Override
    public BigDecimal getProjectExpense(Integer projectID) {
        String companyID = getCompanyId();
        String sql = "SELECT p.id, coalesce(sum(ti.debit),0) - coalesce(sum(ti.credit),0) FROM " + companyID + ".transactionitem ti " +
                " INNER JOIN " + companyID + ".transaction t on t.id=ti.transactionid " +
                " INNER JOIN " + companyID + ".account acc on acc.id=ti.accountid " +
                " LEFT JOIN " + getPublic() + ".accounttype acct on acct.id=acc.accountTypeId " +
                " LEFT JOIN " + companyID + ".invoice i on i.id=t.invoiceid " +
                " LEFT JOIN " + companyID + ".reference invs on invs.id=i.status_id " +
                " LEFT JOIN " + companyID + ".quote po on po.id=t.purchaseorder_id " +
                " LEFT JOIN " + companyID + ".expensereport exp on exp.id=t.expenseReportid " +
                " LEFT JOIN " + companyID + ".reference exps on exps.id=exp.overallStatus " +
                " LEFT JOIN " + companyID + ".manualjournalitem mji on mji.transactionitemid=ti.id " +
                " LEFT JOIN " + companyID + ".manualjournal mj on mj.id=mji.manualjournalid " +
                " LEFT JOIN " + companyID + ".reference r on r.id=mj.overallStatus " +
                " LEFT JOIN " + companyID + ".project p on p.id = (CASE WHEN i.relatedproject_id is not null THEN i.relatedproject_id " +
                " WHEN po.relatedproject_id is not null THEN po.relatedproject_id WHEN exp.projectId is not null THEN exp.projectId ELSE mji.project_id END) " +
                " WHERE " + ServerUtils.checkForDeleted("t.deleted") + " AND p.isDeleted<>TRUE AND p.id = '" + projectID + "' " +
                " AND (invs is null OR (invs.code!='" + Constants.REVERSED + "' AND i.type='PAYABLE')) AND (mj is null OR r.code='" + EdsManualJournal.POST + "') AND (exp is null OR exps.code!='EXPENSE_REVERSED') " +
                " AND acct.category in ('" + EdsAccountType.EXPENSES + "') " +
//                " AND (acc.key is null OR acc.key!='2202') " +
                " GROUP BY p.id;";

        List<Object[]> dataList = (List<Object[]>) findNative(sql);
        for (Object[] data : dataList) {
            return data[1] != null ? (BigDecimal) data[1] : BigDecimal.ZERO;
        }
        return BigDecimal.ZERO;
    }

    @Override
    public HashMap<Integer, BigDecimal> getActualExpenseByProjectIDs(String projectIDs) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT t.projectid, SUM(coalesce(amount, 0)) FROM (");

        //PURCHASE INVOICE
        sql.append("SELECT DISTINCT ti.id, p.id AS projectid, ((CASE WHEN i.isCreditNote THEN -1 ELSE 1 END) * coalesce(ti.debit,ti.credit)) as amount FROM ").append(getCompanyId()).append(".transactionitem ti ");
        sql.append("INNER JOIN ").append(getCompanyId()).append(".transaction t on t.id = ti.transactionid ");
        sql.append("INNER JOIN ").append(getCompanyId()).append(".invoice i on i.id = t.invoiceid ");
        sql.append("INNER JOIN ").append(getCompanyId()).append(".invoiceitem ii on ii.invoice_id = i.id and ii.account_id = ti.accountid ");
        sql.append("INNER JOIN ").append(getCompanyId()).append(".reference invs on invs.id = i.status_id ");
        sql.append("INNER JOIN ").append(getCompanyId()).append(".project p on p.id = i.relatedproject_id ");
        sql.append("WHERE ").append(ServerUtils.checkForDeleted("t.deleted"));
        sql.append(" AND p.isDeleted is not true ");
        sql.append("AND i.type = 'PAYABLE' AND invs.code != 'REVERSED' ");
        sql.append("AND p.id IN (").append(projectIDs).append(") ");

        sql.append(" UNION ");

        //PURCHASE ORDER
        sql.append("SELECT DISTINCT ti.id, p.id AS projectid, coalesce(ti.debit,ti.credit) as amount FROM ").append(getCompanyId()).append(".transactionitem ti ");
        sql.append("INNER JOIN ").append(getCompanyId()).append(".transaction t on t.id = ti.transactionid ");
        sql.append("INNER JOIN ").append(getCompanyId()).append(".quote po on po.id = t.purchaseorder_id ");
        sql.append("INNER JOIN ").append(getCompanyId()).append(".quoteitem poi on poi.quote_id = po.id and poi.account_id = ti.accountid ");
        sql.append("INNER JOIN ").append(getCompanyId()).append(".project p on p.id = po.relatedproject_id ");
        sql.append("WHERE ").append(ServerUtils.checkForDeleted("t.deleted"));
        sql.append(" AND p.isDeleted is not true ");
        sql.append("AND p.id IN (").append(projectIDs).append(") ");

        sql.append(" UNION ");

        //EXPENSE REPORT
        sql.append("SELECT DISTINCT ti.id, p.id AS projectid, coalesce(ti.debit,ti.credit) as amount FROM ").append(getCompanyId()).append(".transactionitem ti ");
        sql.append("INNER JOIN ").append(getCompanyId()).append(".transaction t on t.id = ti.transactionid ");
        sql.append("INNER JOIN ").append(getCompanyId()).append(".expensereport exp on exp.id=t.expenseReportid ");
        sql.append("INNER JOIN ").append(getCompanyId()).append(".expense ei on ei.reportid = exp.id and ei.accountid = ti.accountid ");
        sql.append("INNER JOIN ").append(getCompanyId()).append(".reference exps on exps.id=exp.overallStatus ");
        sql.append("INNER JOIN ").append(getCompanyId()).append(".project p on p.id = exp.projectid ");
        sql.append("WHERE ").append(ServerUtils.checkForDeleted("t.deleted"));
        sql.append(" AND p.isDeleted is not true ");
        sql.append("AND exps.code!='EXPENSE_REVERSED' ");
        sql.append("AND p.id IN (").append(projectIDs).append(") ");

        sql.append(" UNION ");

        //MANUAL JOURNAL WITH TYPE = 'POST'
        sql.append("SELECT DISTINCT ti.id, p.id AS projectid, coalesce(ti.debit,ti.credit) as amount FROM ").append(getCompanyId()).append(".transactionitem ti ");
        sql.append("INNER JOIN ").append(getCompanyId()).append(".transaction t on t.id = ti.transactionid ");
        sql.append("INNER JOIN ").append(getCompanyId()).append(".account acc on acc.id=ti.accountid ");
        sql.append("INNER JOIN ").append(getPublic()).append(".accounttype acct on acct.id=acc.accountTypeId ");
        sql.append("INNER JOIN ").append(getCompanyId()).append(".manualjournalitem mji on mji.transactionitemid=ti.id ");
        sql.append("INNER JOIN ").append(getCompanyId()).append(".manualjournal mj on mj.id=mji.manualjournalid ");
        sql.append("INNER JOIN ").append(getCompanyId()).append(".reference r on r.id=mj.overallStatus ");
        sql.append("INNER JOIN ").append(getCompanyId()).append(".project p on p.id = mji.project_id ");
        sql.append("WHERE ").append(ServerUtils.checkForDeleted("t.deleted"));
        sql.append(" AND p.isDeleted is not true ");
        sql.append(" AND r.code = '").append(EdsManualJournal.POST).append("' ");
        sql.append(" AND acct.category in ('").append(EdsAccountType.EXPENSES).append("') ");
        sql.append("AND p.id IN (").append(projectIDs).append(") ");

        sql.append(") t GROUP BY t.projectid ");

        List<Object[]> actualExpenses = findNative(sql.toString());
        HashMap<Integer, BigDecimal> result = new HashMap<>();
        for (Object[] actualExpense : actualExpenses) {
            Integer projectID = (Integer) actualExpense[0];
            BigDecimal actualExpenseAmount = (BigDecimal) actualExpense[1];
            result.put(projectID, actualExpenseAmount);
        }

        return result;
    }

    @Override
    public HashMap<Integer, BigDecimal> getActualExpenseFromBudgetByProjectIDs(String projectIDs, boolean isAgencyFees) {
        List<String> status = Arrays.asList(Constants.EXPENSE_APPROVED, Constants.EXPENSE_PAID, Constants.EXPENSE_CLOSED);

        StringBuilder sql = new StringBuilder();
        sql.append("SELECT projectid, sum(amount) FROM (\n");
        sql.append("SELECT p.id projectid,  sum(coalesce(e.basesubtotal,0) " + (!isAgencyFees ? " - round(coalesce(e.taxAmount,0)/er.exchageRate, 5) - round(coalesce(e.doubleTaxAmount,0)/er.exchageRate,5)" : "") + ") amount FROM ").append(getCompanyId()).append(".expenseReport er \n");
        sql.append("INNER JOIN ").append(getCompanyId()).append(".expense e ON e.reportid = er.id \n");
        sql.append("INNER JOIN ").append(getCompanyId()).append(".reference s ON s.id = er.overallstatus \n");
        sql.append("INNER JOIN ").append(getCompanyId()).append(".project p ON (p.id = er.projectId or p.id = e.project_id) \n");
        sql.append("WHERE er.isDeleted is not true and s.code in ('" + ServerUtils.getAsCommoDelimited(status, "0", "','") + "') \n");
        sql.append(" AND p.id IN (").append(projectIDs).append(") \n");
        sql.append("GROUP BY p.id \n");


        sql.append("UNION ALL \n");

        status = Arrays.asList(Constants.APPROVE, Constants.OPEN, Constants.PAID, Constants.OVER_DUE);
        if (isAgencyFees) {
            sql.append("SELECT p.id projectid, \n")
                    .append("(SUM(CASE WHEN i.isCreditNote THEN 0-ii.net ELSE ii.net END) \n")
                    .append(" + SUM(CASE WHEN i.taxCalculationType = 2 THEN \n")
                    .append("         CASE WHEN i.isCreditNote THEN 0 - (round(coalesce(ii.taxamount,0)/i.exchangerate, 5) + round(coalesce(ii.doubletaxamount,0)/i.exchangerate, 5)) \n")
                    .append("              ELSE round(coalesce(ii.taxamount,0)/i.exchangerate, 5) + round(coalesce(ii.doubletaxamount,0)/i.exchangerate, 5) END \n")
                    .append("         ELSE 0 END)) AS amount \n")
                    .append("FROM ").append(getCompanyId()).append(".purchaseinvoice pi \n");
        } else {
            sql.append("""
                    SELECT p.id projectid, (sum(CASE WHEN i.isCreditNote THEN 0-ii.net ELSE ii.net END) + sum(CASE WHEN i.taxCalculationType = 1 THEN\s
                     (case when i.isCreditNote then round(coalesce(ii.taxamount,0)/i.exchangerate, 5) + round(coalesce(ii.doubletaxamount,0)/i.exchangerate, 5) else\s
                      0 - (round(coalesce(ii.taxamount,0)/i.exchangerate, 5) + round(coalesce(ii.doubletaxamount,0)/i.exchangerate, 5)) end) ELSE 0 END)) as amount FROM\s""").append(getCompanyId()).append(".purchaseinvoice pi \n");
        }

        sql.append("INNER JOIN ").append(getCompanyId()).append(".invoice i ON i.id = pi.id \n");
        sql.append("INNER JOIN ").append(getCompanyId()).append(".invoiceitem ii on ii.invoice_id = i.id \n");
        sql.append("INNER JOIN ").append(getCompanyId()).append(".reference s ON s.id = i.status_id \n");
        sql.append("INNER JOIN ").append(getCompanyId()).append(".project p ON (p.id = i.relatedproject_id or p.id = ii.project_id) \n");
        sql.append("WHERE i.deleted is not true and s.code in ('" + ServerUtils.getAsCommoDelimited(status, "0", "','") + "') \n");
        sql.append("AND p.id in (" + projectIDs + ") \n");
        sql.append("GROUP BY p.id \n");

        sql.append("UNION ALL \n");

        if (isAgencyFees) {
            sql.append("SELECT p.id projectid, \n")
                    .append("SUM(bti.amount/bt.exchangeRate) \n")
                    .append("+ SUM(case when bt.taxCalculationType = 2 THEN round(coalesce(bti.taxamount,0)/bt.exchangerate, 5) ELSE 0 END) AS amount \n")
                    .append("FROM ").append(getCompanyId()).append(".spendreceivemoney bt \n");
        } else {
            sql.append("SELECT p.id projectid, SUM(bti.amount/bt.exchangeRate) - SUM(case when bt.taxCalculationType = 1 THEN round(coalesce(bti.taxamount,0)/bt.exchangerate, 5) ELSE 0 END) as amount from ").append(getCompanyId()).append(".spendreceivemoney bt \n");
        }
        sql.append("INNER JOIN ").append(getCompanyId()).append(".spendreceivemoneyitem bti on bti.banktransferid = bt.id \n");
        sql.append("INNER JOIN ").append(getCompanyId()).append(".project p on p.id = bt.projectid or p.id = bti.projectid \n");
        sql.append("WHERE bt.deleted is not true and p.id in (").append(projectIDs).append(") \n");
        sql.append("AND bt.transferType in (" + AccountingConstants.SPEND_MONEY + ", " + AccountingConstants.CASH_PAYMENT + ") \n");
        sql.append("GROUP BY p.id \n");
        sql.append(") t \n");
        sql.append("GROUP BY projectid ");


        List<Object[]> actualExpenses = findNative(sql.toString());
        HashMap<Integer, BigDecimal> result = new HashMap<>();
        for (Object[] actualExpense : actualExpenses) {
            Integer projectID = (Integer) actualExpense[0];
            BigDecimal actualExpenseAmount = (BigDecimal) actualExpense[1];
            result.put(projectID, actualExpenseAmount);
        }

        return result;
    }

    @Override
    public BigDecimal getProjectPlanedExpense(Integer projectID) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT SUM(pbi.amount) FROM EdsProjectBudgetItem pbi join pbi.projectBudget pb join pb.project p ");
        sql.append("WHERE pbi.total is true AND  pbi.type IN ('").append(EdsProjectBudgetItem.PURCHASE).append("', '").append(EdsProjectBudgetItem.EXPENSE).append("') ");
        sql.append("AND p.objectID = '").append(projectID).append("' ");

        BigDecimal expense = (BigDecimal) findSingle(sql.toString());
        return expense != null ? expense : BigDecimal.ZERO;
    }

    @Override
    public HashMap<Integer, Double> getPlannedExpenseByProjectIDs(String projectIDs) {
        String companyID = getCompanyId();
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT p.id, SUM(pbi.amount) ");
        sql.append(" FROM ").append(companyID).append(".ProjectBudgetItem pbi ");
        sql.append(" LEFT JOIN ").append(companyID).append(".projectBudget pb on pbi.projectbudgetid = pb.id ");
        sql.append(" LEFT JOIN ").append(companyID).append(".project p on pb.projectid = p.id ");
        sql.append(" WHERE pbi.total is true AND pbi.type IN ('").append(EdsProjectBudgetItem.PURCHASE).append("', '").append(EdsProjectBudgetItem.EXPENSE).append("') ");
        sql.append(" AND p.id IN (").append(projectIDs).append(") ");
        sql.append(" GROUP BY p.id ");
        List<Object[]> plannedExpenses = findNative(sql.toString());
        HashMap<Integer, Double> result = new HashMap<>();
        for (Object[] plannedExpense : plannedExpenses) {
            Integer projectID = (Integer) plannedExpense[0];
            Double plannedExpenseAmount = ((BigDecimal) plannedExpense[1]).doubleValue();
            result.put(projectID, plannedExpenseAmount);
        }

        return result;
    }

    @Override
    public HashMap<Integer, Double> getPlannedExpenseFromBudgetByProjectIDs(String projectIDs, boolean isAgencyFees) {
        List<String> status = Arrays.asList(Constants.EXPENSE_APPROVED, Constants.EXPENSE_PAID, Constants.EXPENSE_SUBMITTED, Constants.EXPENSE_CLOSED);

        StringBuilder sql = new StringBuilder();
        sql.append("SELECT projectid, sum(amount) FROM (\n");
        sql.append("SELECT p.id projectid,  sum(coalesce(e.basesubtotal,0) " + (!isAgencyFees ? " - round(coalesce(e.taxAmount,0)/er.exchageRate, 5) - round(coalesce(e.doubleTaxAmount,0)/er.exchageRate,5)" : "") + ") amount FROM ").append(getCompanyId()).append(".expenseReport er \n");
        sql.append("INNER JOIN ").append(getCompanyId()).append(".expense e ON e.reportid = er.id \n");
        sql.append("INNER JOIN ").append(getCompanyId()).append(".reference s ON s.id = er.overallstatus \n");
        sql.append("INNER JOIN ").append(getCompanyId()).append(".project p ON (p.id = er.projectId or p.id = e.project_id) \n");
        sql.append("WHERE er.isDeleted is not true and s.code in ('" + ServerUtils.getAsCommoDelimited(status, "0", "','") + "') \n");
        sql.append(" AND p.id IN (").append(projectIDs).append(") \n");
        sql.append("GROUP BY p.id \n");

        sql.append("UNION ALL \n");

        status = Arrays.asList(Constants.CONVERTED, Constants.RECEIVED, Constants.PARTIAL_RECEIVED, Constants.APPROVE, Constants.INVOICED, Constants.OPEN);
        sql.append("SELECT p.id projectid, sum(coalesce(qi.net,0) " + (isAgencyFees ? " + round(coalesce(qi.taxAmount,0)/q.exchangeRate, 5) + round(coalesce(qi.doubleTaxAmount,0)/q.exchangeRate,5)" : "") + ") amount FROM ").append(getCompanyId()).append(".purchaseorder po \n");
        sql.append("INNER JOIN ").append(getCompanyId()).append(".quote q ON q.id = po.id \n");
        sql.append("INNER JOIN ").append(getCompanyId()).append(".quoteitem qi on qi.quote_id = q.id \n");
        sql.append("INNER JOIN ").append(getCompanyId()).append(".reference s on s.id = q.status_id \n");
        sql.append("INNER JOIN ").append(getCompanyId()).append(".project p ON (p.id = q.relatedproject_id or p.id = qi.project_id) \n");
        sql.append("WHERE q.deleted is not true AND s.code in ('" + ServerUtils.getAsCommoDelimited(status, "0", "','") + "') \n");
        sql.append(" AND p.id IN (").append(projectIDs).append(") \n");
        sql.append("GROUP BY p.id \n");
        sql.append(") t \n");
        sql.append("GROUP BY projectid ");

        List<Object[]> plannedExpenses = findNative(sql.toString());
        HashMap<Integer, Double> result = new HashMap<>();
        for (Object[] plannedExpense : plannedExpenses) {
            Integer projectID = (Integer) plannedExpense[0];
            Double plannedExpenseAmount = plannedExpense[1] != null ? ((BigDecimal) plannedExpense[1]).doubleValue() : 0;
            result.put(projectID, plannedExpenseAmount);
        }

        return result;
    }

    @Override
    public BigDecimal getProjectPlanedIncome(Integer projectID) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT SUM(pbi.amount) FROM EdsProjectBudgetItem pbi join pbi.projectBudget pb join pb.project p ");
        sql.append("WHERE pbi.total is true AND pbi.type = '").append(EdsProjectBudgetItem.REVENUE).append("' ");
        sql.append("AND p.objectID = '").append(projectID).append("' ");

        BigDecimal pincome = (BigDecimal) findSingle(sql.toString());
        return pincome != null ? pincome : BigDecimal.ZERO;
    }

    @Override
    public HashMap<Integer, Double> getPlannedIncomeByProjectIDs(String projectIDs) {
        String companyID = getCompanyId();
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT p.id, SUM(pbi.amount) ");
        sql.append(" FROM ").append(companyID).append(".ProjectBudgetItem pbi ");
        sql.append(" LEFT JOIN ").append(companyID).append(".projectBudget pb on pbi.projectbudgetid = pb.id ");
        sql.append(" LEFT JOIN ").append(companyID).append(".project p on pb.projectid = p.id ");
        sql.append(" WHERE pbi.total is true AND pbi.type = '").append(EdsProjectBudgetItem.REVENUE).append("' ");
        sql.append(" AND p.id IN (").append(projectIDs).append(") ");
        sql.append(" GROUP BY p.id ");
        List<Object[]> plannedExpenses = findNative(sql.toString());
        HashMap<Integer, Double> result = new HashMap<>();
        for (Object[] plannedExpense : plannedExpenses) {
            Integer projectID = (Integer) plannedExpense[0];
            Double plannedExpenseAmount = ((BigDecimal) plannedExpense[1]).doubleValue();
            result.put(projectID, plannedExpenseAmount);
        }

        return result;
    }

    @Override
    public HashMap<Integer, Double> getPlannedIncomeFromBudgetByProjectIDs(String projectIDs, boolean isAgencyFees) {
        List<String> status = Arrays.asList(Constants.CONVERTED, Constants.CLIENT_APPROVE,
                Constants.INVOICED, Constants.APPROVE, Constants.OPEN, Constants.SALE_ORDER,
                Constants.PICKED, Constants.PACKED, Constants.SHIPPED);

        StringBuilder sql = new StringBuilder();
        sql.append("SELECT p.id, sum(coalesce(qi.net,0) " + (isAgencyFees ? " + round(coalesce(qi.taxAmount,0)/q.exchangeRate, 5) + round(coalesce(qi.doubleTaxAmount,0)/q.exchangeRate,5)" : "") + ") amount FROM ").append(getCompanyId()).append(".salequote sq \n");
        sql.append("INNER JOIN ").append(getCompanyId()).append(".quote q ON q.id = sq.id \n");
        sql.append("INNER JOIN ").append(getCompanyId()).append(".quoteItem qi on qi.quote_id = q.id \n");
        sql.append("INNER JOIN ").append(getCompanyId()).append(".reference s on s.id = q.status_id \n");
        sql.append("LEFT JOIN ").append(getCompanyId()).append(".project p ON (p.id = q.relatedproject_id or qi.project_id = p.id) \n");
        sql.append("WHERE p.id is not null and q.deleted is not true AND s.code in ('" + ServerUtils.getAsCommoDelimited(status, "0", "','") + "') \n");
        sql.append(" AND p.id IN (").append(projectIDs).append(") \n");
        sql.append("GROUP BY p.id \n");

        List<Object[]> plannedIncome = findNative(sql.toString());
        HashMap<Integer, Double> result = new HashMap<>();
        for (Object[] plannedExpense : plannedIncome) {
            Integer projectID = (Integer) plannedExpense[0];
            Double plannedExpenseAmount = ((BigDecimal) plannedExpense[1]).doubleValue();
            result.put(projectID, plannedExpenseAmount);
        }

        return result;
    }

    @Override
    public List<EdsAccount> getAccountsForProjectBudget(ListingFilterParameter filterParameters) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT ac FROM EdsAccount ac WHERE " + ServerUtils.checkForDeleted("ac.deleted") + " ");

        if (Constants.REVENUE.equals(filterParameters.getAccountType())) {
            sql.append(" AND ac.accountType.category = '" + EdsAccountType.REVENUE + "' ");
        } else if (Constants.EXPENSES.equals(filterParameters.getAccountType())) {
            sql.append(" AND ac.accountType.category = '" + EdsAccountType.EXPENSES + "' ");
        } else if (Constants.EXPENSES_AND_CURRENT_ASSET.equals(filterParameters.getAccountType())) {
            sql.append(" AND (ac.accountType.category = '" + EdsAccountType.EXPENSES + "'");
            sql.append(" OR ac.accountType.code = '" + EdsAccountType.CURRENT_ASSET + "')");
        } else if (Constants.PURCHASES_STR.equals(filterParameters.getAccountType())) {
            sql.append(" AND (ac.accountType.category = '" + EdsAccountType.ASSETS
                    + "' OR ac.accountType.category = '" + EdsAccountType.EXPENSES
                    + "' OR ac.accountType.category = '" + EdsAccountType.LIABILITIES
                    + "' OR ac.accountType.category = '" + EdsAccountType.EQUITY + "') ");
        }
        if (filterParameters.getSqlSearchKey() != null) {
            sql.append("  AND ( lower(ac.name) like '" + filterParameters.getSqlSearchKey() + "%' ");
            sql.append("  OR  lower(ac.codeString) like '" + filterParameters.getSqlSearchKey() + "%') ");
        }
        sql.append(" ORDER BY ac.accountCode");
        return find(sql.toString());
    }

    @Override
    public void deleteProjectBudgetItems(Integer projectBudgetID) {
        update("delete from EdsProjectBudgetItem where projectBudget.objectID = ?", projectBudgetID);
    }

    @Override
    public BigDecimal getProjectEmployeeCostByMonth(Integer projectID, Date startDate, Date endDate) {
        startDate = getUser().getUserDate(startDate);
        endDate = getUser().getUserDate(endDate);
        String companyId = getCompanyId();
        int year = startDate.getYear() + 1900;
        String sql = " select sum(total) as total from (" +
                "select sum(coalesce(ts.wageRate, 0) * ts.timeSpent)/cast(60 as double precision) as total from " + companyId + ".TimeSheet ts " +
                " left join " + companyId + ".task t on ts.taskid = t.id " +
                " left join " + companyId + ".reference r1 on ts.statusid = r1.id " +
                " where t.projectid = " + projectID + " and (t.deleted <> true or t.deleted is null) " +
                " and r1.code = '_APPROVE' and ts.date between '" + startDate + "' and '" + endDate + "' " +
                " union " +
                " select sum(cast(pti.total as double precision)) as total from " + companyId + ".payslipTableItem pti " +
                " left join " + companyId + ".reference r2 on pti.status_id = r2.id " +
                " where (pti.deleted <> true or pti.deleted is null) " +
                " and r2.code = 'PY_APPROVED' and pti.monthid = " + startDate.getMonth() +
                " and pti.year =" + year +
                " and pti.projectid = " + projectID +
                " ) as t";
        Double actualWageAmount = (Double) findNativeSingle(sql);

        return actualWageAmount != null ? new BigDecimal(actualWageAmount).setScale(ServerUtils.getSystemCalculationScale(), RoundingMode.HALF_UP) : BigDecimal.ZERO;
    }

    public BigDecimal getProjectEmployeeBudget(Integer projectID) {
        String companyID = getCompanyId();

        StringBuilder sql = new StringBuilder();
        sql.append("select sum(coalesce(et.estimatedTime, 0)* coalesce(pe.wagerate,0)/60) from " + companyID + ".employeetask et");
        sql.append(" inner join " + companyID + ".projectemployee pe on pe.id=et.projectemployeeid");
        sql.append(" inner join " + companyID + ".task t on t.id=et.taskid");
        sql.append(" where t.projectid = ? and t.deleted is not true and et.deleted is not true");
        Double plannedAmount = (Double) findNativeSingle(sql.toString(), projectID);
        return plannedAmount != null ? new BigDecimal(plannedAmount) : BigDecimal.ZERO;
    }

    @Override
    public BigDecimal getProjectPurchasesByMonth(Integer projectID, Date startDate, Date endDate) {
        String companyID = getCompanyId();
        StringBuilder sql = new StringBuilder("SELECT sum(case when t.invoiceid is not null and i.isCreditNote is true then (-1)*coalesce(ti.credit,0) else coalesce(ti.debit,0) end) FROM " + companyID + ".transactionitem ti ");
        sql.append(" INNER JOIN " + companyID + ".transaction t on t.id=ti.transactionid");
        sql.append(" INNER JOIN " + companyID + ".account a on a.id=ti.accountid");
        sql.append(" LEFT JOIN (SELECT pi2.id FROM " + companyID + ".purchaseinvoice pi2 WHERE pi2.id NOT IN (SELECT invoice_id FROM " + companyID + ".converted_items)) pi ON pi.id = t.invoiceid");
        sql.append(" LEFT JOIN " + companyID + ".invoice i on i.id=pi.id");
        sql.append(" LEFT JOIN " + companyID + ".purchaseorder po on po.id=t.purchaseorder_id");
        sql.append(" LEFT JOIN " + companyID + ".quote q on po.id=q.id");
        sql.append(" LEFT JOIN " + companyID + ".reference st on st.id=coalesce(i.status_id, q.status_id)");
        sql.append(" WHERE " + ServerUtils.checkForDeleted("t.deleted") + " AND a.accountcode!='" + EdsAccount.VAT_PAYABLE + "' AND ");
        sql.append(" (i.relatedproject_id = " + projectID + " OR q.relatedproject_id = " + projectID + ") " +
                " AND (st.code!='REVERSED' AND st.code!='INVOICE_STATUS_INVOICED') AND t.journaldate between ? and ? ");

        BigDecimal amount = (BigDecimal) findNativeSingle(sql.toString(), startDate, endDate);
        return amount != null ? amount : BigDecimal.ZERO;
    }

    @Override
    public List<EdsProjectBudgetItem> getBudgetItems(Integer budgetID, String type, Integer accountID) {
        Map<String, Object> map = new HashMap<>();
        map.put("budgetID", budgetID);
        map.put("type", type);
        return findByNamedParams("SELECT bi FROM EdsProjectBudgetItem bi WHERE bi.total is false AND bi.projectBudget.objectID = :budgetID AND bi.type = :type " + (accountID != null ? " AND bi.account.objectID = " + accountID : ""), map);
    }

    @Override
    public EdsProjectBudgetItem getBudgetItem(Integer budgetID, String type, Integer accountID, Integer month, Integer year) {
        return (EdsProjectBudgetItem) findSingle("SELECT bi FROM EdsProjectBudgetItem bi WHERE bi.projectBudget.objectID = ? AND bi.type = ? " + (accountID != null ? " AND bi.account.objectID = " + accountID : "") + " AND bi.month = ? AND bi.year = ?", budgetID, type, month, year);
    }

    @Override
    public EdsProjectBudgetItem getTotalBudgetItem(Integer budgetID, String type, Integer accountID) {
        return (EdsProjectBudgetItem) findSingle("SELECT bi FROM EdsProjectBudgetItem bi WHERE bi.projectBudget.objectID = ? AND bi.type = ? " + (accountID != null ? " AND bi.account.objectID = " + accountID : "") + " AND bi.total = ?", budgetID, type, Boolean.TRUE);
    }
}
