package com.edatasite.workforce.gwt.expenses.client.rpc;

import com.edatasite.workforce.gwt.core.client.Utils;
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
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.ServiceDefTarget;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Admin
 * Date: 20.10.2008
 * Time: 20:58:57
 * To change this template use File | Settings | File Templates.
 */
public interface ExpenseService extends RemoteService {

    ReportData getReportData(ExpenseReportViewParameters parameters);

    ReportData getReportSummaryData(Integer reportId);

    ExpensePaymentData[] getExpensePayments(Integer reportID);

    SelectItem[] getCategories();

    Integer addExpenseCategory(ExpenseCategory category);

    SelectItem[] getRelatedProjects();

    SelectItem[] getApprover(Integer projectID, boolean firstApprover);

    SelectItem[] getApproversForLookUp(ListingFilterParameter parametrs);

    CurrencyItem getBaseCurrency();

    Double getExchRateForExpenseReport(String from, String to);

    Integer saveReport(ExpenseReportsListItem report);

    BankTransferNumberData generateExpenseReportNumber();

    Boolean deleteExpenseReport(Integer objectID);

    void deleteSelectedExpenseReports(ArrayList<Integer> idArray);

    ListResult<ExpenseReportsListItem> getExpenseReportsDataFromSolr(ListingFilterParameter filterParameter);

    ListResult<ExpenseReportsListItem> getCompanyReports(String status, ListingFilterParameter fp);

    ExpenseReportsListItem getReport(Integer objectId);

    ExpenseListItem[] getExpenses(Integer reportId);

    Double getExchRate(String to);

    String changeExpenseStatus(Integer reportId, String status, String note, Boolean isApproveForAll, ArrayList<ExpenseListItem> lineItems);

    void sendEmail(Integer reportId);

    void sendEmail(Integer reportId, String message, Integer emailTemplateID);

    Integer savePayment(ExpensePaymentData epd);

    ListResult<ExpenseReportsListItem> getEmployeesReportList(ListingFilterParameter filter);

    String reportSubmitOrResubmitReport(Integer reportID);

    ExpenseEmailTemplateData getEmailTemplateData(Integer expenseReportID);

    SelectItem[] getEmailTemplates(String templcatCategoryCode);

    ExpenseListItem[] getExpenseItemsForPOAllocation(Integer purchaseOrderID);

    ExpensePaymentData getPaymentData(Integer reportID);

    //NumberData generateExpenseReportNumber();

    void deleteExpensePayment(Integer objectId);

    void changeRelatedPurchaseOrder(Integer purchaseOrderID, Integer relatedPOID);

    LinkedHashMap<String, BigDecimal> getExpenseChartData(ListingFilterParameter fp);

    LinkedHashMap<String, BigDecimal> getExpensesByOldNewEmployeesChartData(ListingFilterParameter fp);

    Integer createExpenseClaimHistory(Integer expenseReportId, HistoryListItem hisItem);

    Boolean deleteExpenseHistory(Integer expenseHistoryId);

    PaymentAndPrePaymentData getExpenseReportPaymentsHistory(Integer objectID);

    ArrayList<ExpenseProjectItem> getExpenseProjects(DateNonConvertable startPeriod, DateNonConvertable endPeriod, ListingFilterParameter fp);

    SelectItem[] getEmployeeClients(Integer employeeId);

    String getProjectBaseExpenseFormLayout();

    SelectItem getDefaultAccountForProjectBaseExpense();

    Integer getEmployeesReportListCount(ListingFilterParameter filterParameter);

    ApprovalListResult getExpenseApprovers(Integer userId, boolean fromSettings);

    boolean saveExpenseCellValue(ExpenseReportsListItem rowValue, String columnCodeName);

    List<HistoryNote> loadExpenseNoteHistory(Integer objectId);


    class App {
        public static ExpenseServiceAsync get() {
            ServiceDefTarget target = GWT.create(ExpenseService.class);
            target.setServiceEntryPoint(Utils.getRpcBaseUrl() + "/expense");
            return (ExpenseServiceAsync) target;
        }
    }
}
