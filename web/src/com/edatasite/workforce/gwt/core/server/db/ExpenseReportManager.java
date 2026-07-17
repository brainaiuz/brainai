package com.edatasite.workforce.gwt.core.server.db;

import com.edatasite.workforce.core.domain.EdsExpense;
import com.edatasite.workforce.core.domain.EdsExpenseReport;
import com.edatasite.workforce.core.domain.EdsProject;
import com.edatasite.workforce.core.domain.EdsReference;
import com.edatasite.workforce.core.domain.crm.EdsCrmAccount;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.rpc.solr.SolrReindexRpc;
import com.edatasite.workforce.gwt.core.client.ui.view.BankTransferNumberData;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

public interface ExpenseReportManager extends Manager<EdsExpenseReport> {

//    public Date[] getExpenseReportDates();

    EdsExpenseReport getExpenseReport(Integer objectID);

    List<EdsExpenseReport> getWaitingExpenseReports(ListingFilterParameter filterParametrs);

//    public List<EdsExpenseReport> getEmployeeReports(String searchKey, ListingFilterParameter filterParametrs, ListLoadConfig config);

    List<EdsExpenseReport> getAllExpenseReports(ListingFilterParameter filterParametrs);

    LinkedHashMap<String, BigDecimal> getAllExpenseReportsChartData(ListingFilterParameter filterParametrs);

    List<EdsExpenseReport> getReportersReportsByStatus(EdsReference[] status, Date maxDate, Integer count);

    List<EdsExpenseReport> getUnpaidExpenseClaimsForPayslip(ListingFilterParameter fp);

    List<EdsExpenseReport> getPayslipRelatedExpenseClaims(Integer payslipID);

    List<EdsExpenseReport> getPayslipTableItemRelatedExpenseClaims(Integer payslipTableItemID);

    Integer getReportersReportCount(EdsReference[] status, Date maxDate);

    List<EdsExpenseReport> getApproversReportsByStatus(EdsReference[] status, Date maxDate, Integer count);

    Integer getApproversReportCount(EdsReference[] status, Date maxDate);

    Integer getAllReportCount(EdsReference[] status, Date maxDate);

    List<EdsExpenseReport> getCompanyReports(String status, ListingFilterParameter fp);

    List<EdsExpenseReport> getEmployeeReports(ListingFilterParameter filterParametrs, Boolean isCount);

    List<Object[]> getEmployeeTopExpenses(Integer employeeId, Date fromDate, Date toDate);

    List<Integer> getCompanyDeletedExpenseReportListForSolr(SolrReindexRpc solrReindex);

    List<EdsExpenseReport> getCompanyExpenseReportListForSolr(SolrReindexRpc solrReindex, Integer start, Integer limit);

    List<Integer> getExpenseReportClaimsIdsByIDs(String IDs);

    List<Integer> getExpenseReportClaimsIdsWithLimit(Integer startat, Integer limit);

    List<EdsExpense> getPurchaseOrderRelatedExpenseItems(Integer purchaseOrderID);

    HashMap<Integer, BigDecimal> getExpensesAllocatedToPO(Integer purchaseOrderID);

    boolean isUsedForInvoices(Integer expenseID);

    void updateExpenseReport(Integer objectID, Integer projectID);

    void updateExpensesByPayslipTableID(Integer objectID);

    void removeRelatedPO(Integer objectID);

    void mergeExpenseItemWithOldCrmAccountToNewOne(List<Integer> oldAccountIDs, Integer newAccountID);

    List<EdsExpenseReport> getExpensesByCrmAccountID(Integer accId);

    List<EdsExpenseReport> getNotFullyPaidExpenses(Integer supplierId, Integer currencyId, boolean isMultiCurrencyEnabled);

    List<EdsProject> getExpenseProjects(DateNonConvertable startPeriod, DateNonConvertable endPeriod, ListingFilterParameter fp);

    List<EdsCrmAccount> getEmployeeClients(Integer employeeId);

    Integer getLastIntNumber();

    boolean isExpenseNumberExists(String number, Integer expenseID, Date date);

    BankTransferNumberData generateNewNumber(BankTransferNumberData numberData);
}
