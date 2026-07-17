package com.edatasite.workforce.gwt.expenses.client.rpc;

import com.edatasite.workforce.gwt.core.client.rpc.ApprovalListResult;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.HistoryListItem;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.currency.CurrencyItem;
import com.edatasite.workforce.gwt.core.client.rpc.historyNote.HistoryNote;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.view.BankTransferNumberData;
import com.edatasite.workforce.gwt.expenses.client.ui.view.report.ExpenseProjectItem;
import com.edatasite.workforce.gwt.invoice.client.rpc.PaymentAndPrePaymentData;
import com.google.gwt.http.client.Request;
import com.google.gwt.user.client.rpc.AsyncCallback;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;


/**
 * Created by IntelliJ IDEA.
 * User: Admin
 * Date: 20.10.2008
 * Time: 21:01:29
 * To change this template use File | Settings | File Templates.
 */
public interface ExpenseServiceAsync {

    void getReportData(ExpenseReportViewParameters parameters, AsyncCallback<ReportData> asyncCallback);

    void getReportSummaryData(Integer reportId, AsyncCallback<ReportData> asyncCallback);

    void getExpensePayments(Integer reportID, AsyncCallback<ExpensePaymentData[]> callback);

    void getCategories(AsyncCallback<SelectItem[]> callback);

    void addExpenseCategory(ExpenseCategory category, AsyncCallback<Integer> callback);

    void getRelatedProjects(AsyncCallback<SelectItem[]> callback);

    void getApprover(Integer projectID, boolean firstApprover, AsyncCallback<SelectItem[]> callback);

    void getApproversForLookUp(ListingFilterParameter parametrs, AsyncCallback<SelectItem[]> callback);

    void getBaseCurrency(AsyncCallback<CurrencyItem> callback);

    void getExchRateForExpenseReport(String from, String to, AsyncCallback<Double> callback);

    void saveReport(ExpenseReportsListItem report, AsyncCallback<Integer> callback);

    void generateExpenseReportNumber(AsyncCallback<BankTransferNumberData> callback);

    void deleteExpenseReport(Integer objectID, AsyncCallback<Boolean> callback);

    void deleteSelectedExpenseReports(ArrayList<Integer> objectID, AsyncCallback<Void> callback);

    Request getExpenseReportsDataFromSolr(ListingFilterParameter filterParameter, AsyncCallback<ListResult<ExpenseReportsListItem>> callback);

    Request getCompanyReports(String status, ListingFilterParameter fp, AsyncCallback<ListResult<ExpenseReportsListItem>> callback);

    void getReport(Integer objectId, AsyncCallback<ExpenseReportsListItem> callback);

    void getExpenses(Integer reportId, AsyncCallback<ExpenseListItem[]> callback);

    void getExchRate(String to, AsyncCallback<Double> callback);

    void changeExpenseStatus(Integer reportId, String status, String note, Boolean isApproveForAll, ArrayList<ExpenseListItem> lineItems, AsyncCallback<String> callback);

    void sendEmail(Integer reportId, AsyncCallback<Void> callback);

    void sendEmail(Integer reportId, String message, Integer emailTemplateID, AsyncCallback<Void> callback);

    void savePayment(ExpensePaymentData epd, AsyncCallback<Integer> callback);

    void getEmployeesReportList(ListingFilterParameter filter, AsyncCallback<ListResult<ExpenseReportsListItem>> callback);

    void reportSubmitOrResubmitReport(Integer reportID, AsyncCallback<String> callback);

    void getEmailTemplateData(Integer expenseReportID, AsyncCallback<ExpenseEmailTemplateData> callback);

    void getEmailTemplates(String templcatCategoryCode, AsyncCallback<SelectItem[]> callback);

    void getExpenseItemsForPOAllocation(Integer purchaseOrderID, AsyncCallback<ExpenseListItem[]> callback);

    void getPaymentData(Integer reportID, AsyncCallback<ExpensePaymentData> callback);

    void deleteExpensePayment(Integer objectId, AsyncCallback<Void> async);

    void changeRelatedPurchaseOrder(Integer purchaseOrderID, Integer relatedPOID, AsyncCallback<Void> async);

    void getExpenseChartData(ListingFilterParameter fp, AsyncCallback<LinkedHashMap<String, BigDecimal>> async);

    void getExpensesByOldNewEmployeesChartData(ListingFilterParameter fp, AsyncCallback<LinkedHashMap<String, BigDecimal>> asyncCallback);

    void createExpenseClaimHistory(Integer expenseReportId, HistoryListItem hisItem, AsyncCallback<Integer> callback);

    void deleteExpenseHistory(Integer expenseHistoryId, AsyncCallback<Boolean> callback);

    void getExpenseReportPaymentsHistory(Integer objectID, AsyncCallback<PaymentAndPrePaymentData> callback);

    void getExpenseProjects(DateNonConvertable startPeriod, DateNonConvertable endPeriod, ListingFilterParameter fp, AsyncCallback<ArrayList<ExpenseProjectItem>> callback);

    void getEmployeeClients(Integer employeeId, AsyncCallback<SelectItem[]> callback);

    void getProjectBaseExpenseFormLayout(AsyncCallback<String> callback);

    void getDefaultAccountForProjectBaseExpense(AsyncCallback<SelectItem> callback);

    void getEmployeesReportListCount(ListingFilterParameter filterParameter, AsyncCallback<Integer> async);

    void getExpenseApprovers(Integer userId, boolean fromSettings, AsyncCallback<ApprovalListResult> async);

    //void generateExpenseReportNumber(AsyncCallback<NumberData> async);

    void saveExpenseCellValue(ExpenseReportsListItem rowValue, String columnCodeName, AsyncCallback<Boolean> asyncCallback);

    void loadExpenseNoteHistory(Integer objectId, AsyncCallback<List<HistoryNote>> callback);

}
