package com.edatasite.workforce.gwt.payroll.client.rpc;

import com.edatasite.workforce.gwt.core.client.PayslipItemFilter;
import com.edatasite.workforce.gwt.core.client.rpc.HistoryListItem;
import com.edatasite.workforce.gwt.core.client.rpc.MyUpdateItem;
import com.edatasite.workforce.gwt.core.client.rpc.NumberData;
import com.edatasite.workforce.gwt.core.client.rpc.PayslipTableRequestObject;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.TestRPC;
import com.edatasite.workforce.gwt.core.client.rpc.UserBankAccountData;
import com.edatasite.workforce.gwt.core.client.rpc.WfmTreeItem;
import com.edatasite.workforce.gwt.core.client.rpc.currency.CurrencyItem;
import com.edatasite.workforce.gwt.core.client.rpc.historyNote.HistoryNote;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.rpc.payroll.EmployerSettings;
import com.edatasite.workforce.gwt.core.client.rpc.payroll.EmployerSettingsObject;
import com.edatasite.workforce.gwt.core.client.ui.assigneetree.KpiTreeInfo;
import com.edatasite.workforce.gwt.core.client.ui.view.CashAdvanceItem;
import com.edatasite.workforce.gwt.core.client.ui.view.CashAdvancePayment;
import com.edatasite.workforce.gwt.core.client.ui.view.PaymentDeductionObject;
import com.edatasite.workforce.gwt.core.client.ui.view.PaymentDeductionSelectItem;
import com.edatasite.workforce.gwt.core.client.ui.view.recurring.RecurringPayDeductItem;
import com.edatasite.workforce.gwt.employee.client.rpc.EmployeePayrollSettingsObject;
import com.edatasite.workforce.gwt.importfile.client.rpc.ImportFile;
import com.edatasite.workforce.gwt.newemployee.client.rpc.NewEmployee;
import com.edatasite.workforce.gwt.payroll.client.ui.view.dailyRateCalculation.DailyRateSettings;
import com.google.gwt.http.client.Request;
import com.google.gwt.user.client.rpc.AsyncCallback;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;

public interface PayrollServiceAsync {

    void getEmployeeDetailsAndPayrollSettings(Integer employeeId, Date date, AsyncCallback<PayrollSettings> async);

    void createCategory(CategoryObject category, AsyncCallback<Integer> async);

    Request getCompanyCategories(boolean isArabic, AsyncCallback<CategoryObject[]> async);

    Request getCompanyCategories(ListingFilterParameter fp, AsyncCallback<ListResult<CategoryObject>> async);

    Request checkUsingCategory(Integer id, AsyncCallback<Boolean> async);

    void deleteCategory(Integer id, AsyncCallback<Void> async);

    Request getPaymentDections(ListingFilterParameter fp, AsyncCallback<ListResult<PayrollGlobalSettingsData>> async);

    void savePaymentDeductionSettings(PayrollGlobalSettingsData pds, AsyncCallback<Void> async);

    void addNewStarter(NewEmployee employee, HashMap<String, String> payrollSettings, AsyncCallback<Integer> async);

    void saveCompanyPayrollSettings(EmployerSettings payrollSettings, AsyncCallback<Integer> async);

    void getCompanyPayrollSettings(String key, AsyncCallback<String> async);

    void getGroupPayrunSettings(AsyncCallback<GroupPayrunData> callback);

    void getCompanyPayrollSettings(AsyncCallback<EmployerSettings> async);

    void calculateNITableLetter(HashMap<Integer, Boolean> params, AsyncCallback<String> async);

    void getPensionProviders(AsyncCallback<SelectItem[]> async);

    void getPensionSchemes(AsyncCallback<SelectItem[]> async);

    Request getPensionSchemeById(Integer id, AsyncCallback<PensionSchemeData> async);

    Request getPensionSchemeList(ListingFilterParameter filterParametrs, AsyncCallback<ListResult<PensionSchemeData>> async);

    void deletePensionScheme(Integer id, AsyncCallback<Void> async);

    void savePensionScheme(PensionSchemeData pensionScheme, Boolean update, AsyncCallback<Void> async);

    void savePensionProvider(PensionProviderData pensionProvider, AsyncCallback<Void> async);

    Request getNiTaxChanges(ListingFilterParameter fp, AsyncCallback<ListResult<NiTaxChangesListItem>> async);

    void createBankAccount(UserBankAccountData bankAccount, AsyncCallback<Void> async);

    void calculateDatesByWeekOrMonthNumber(int weekOrMonthNumber, int frequencyID, Date selectedDate, AsyncCallback<HashMap<String, Date>> async);

    void savePaymentDeductionCategory(CategoryObject category, AsyncCallback<Integer> callback);

    void getPaymentDeductionCategory(Integer objectID, AsyncCallback<CategoryObject> callback);

    void calculatePeriodStartDate(int weekOrMonthNumber, int frequencyID, Date selectedDate, AsyncCallback<Date> async);

    void calculatePeriodEndDate(int weekOrMonthNumber, int frequencyID, Date date, AsyncCallback<Date> async);

    void getCompanyTaxYears(Integer companyID, AsyncCallback<SelectItem[]> async);

    void getEmployeePayrollSettings(Integer employeeID, String key, AsyncCallback<EmployeePayrollSettingsObject> async);

    void rollback(Integer frequency, Integer period, Integer taxYear, Integer employeeID, AsyncCallback<Integer> async);

    void isCountryUK(AsyncCallback<Boolean> callback);

    void getCategoriesForLookUp(ListingFilterParameter filterParametrs, AsyncCallback<PaymentDeductionSelectItem[]> callback);

    void getPayslipTable(PayslipFilter filter, AsyncCallback<GroupPayrunData> callback);

    void getSinglePayrunItemPaymentDeductionCategories(Integer objectID, AsyncCallback<SinglePayrunItem> callback);

    void getPayslipTableList(ListingFilterParameter filterParameter, AsyncCallback<ListResult<GroupPayrunData>> callback);

    void sendPayslipNotification(Integer payslipTableID, AsyncCallback<Void> async);

    void deletePayslipTable(Integer objectID, AsyncCallback<Void> asyn);

    void saveEndOfServiceSettings(EndOfServiceData settings, AsyncCallback<EndOfServiceData> async);

    void getEndOfServiceSettings(String countryCode, AsyncCallback<EndOfServiceData> async);

    void getEndOfServiceGratuityList(ListingFilterParameter filterParameter, AsyncCallback<ListResult<EoSCalculationData>> async);

    void getEmployeeEosData(PayslipItemFilter filter, String countryCode, AsyncCallback<EoSCalculationData> async);

    void saveEosCalculationData(EoSCalculationData data, AsyncCallback<Boolean> ac);

    void getJobTitles(AsyncCallback<SelectItem[]> abstractAsyncCallback);

    void getCashAdvancedItem(ListingFilterParameter fp, AsyncCallback<CashAdvanceItem> async);

    void getCashAdvanceList(ListingFilterParameter fp, AsyncCallback<ListResult<CashAdvanceItem>> async);

    void saveCashAdvance(CashAdvanceItem cashAdvanceItem, AsyncCallback<TestRPC> async);

    void getEmployeeEosDataList(ListingFilterParameter lfp, AsyncCallback<EosReportData> async);

    void getWpsReportData(ListingFilterParameter lfp, AsyncCallback<WpsReportData> async);

    void getYearMonthsForWps(AsyncCallback<HashMap<SelectItem, SelectItem[]>> async);

    void deleteCashAdvance(Integer objectID, AsyncCallback<Boolean> async);

    void deleteEndOfServiceGratuity(Integer objectID, AsyncCallback<Void> async);

    void getEndOfServiceGratuity(Integer objectID, AsyncCallback<EoSCalculationData> async);

    void getPaymentDeductionSettingsData(Integer objectID, AsyncCallback<PayrollGlobalSettingsData> async);

    void getPensionContributionByFilter(Integer month, Integer year, AsyncCallback<ArrayList<PensionContributionData>> async);

    void deletePaymentDeductionSettings(Integer settingsID, AsyncCallback<Void> async);

    void getSinglePayrunData(PayslipItemFilter filter, AsyncCallback<SinglePayrunItem> async);

    void getSinglePayrunList(ListingFilterParameter filterParameter, AsyncCallback<ListResult<SinglePayrunItem>> async);

    void saveSinglePayrun(SinglePayrunItem data, AsyncCallback<Integer> async);

    void deleteSinglePayrun(Integer objectID, Integer employeeID, AsyncCallback<Void> async);

    void deleteSinglePayrun(Integer objectID, AsyncCallback<PayrollTotalTO> async);

    void approveSinglePayrun(SinglePayrunItem data, AsyncCallback<Void> async);

    void hasPaymentItems(Integer singlePayrunID, AsyncCallback<Boolean> async);

    void getSinglePayrunPdfData(Integer singlePayrunID, AsyncCallback<PayslipTableRequestObject> async);

    void deletePensionProvider(Integer pensionProviderID, AsyncCallback<Void> async);

    void getPensionProviders(ListingFilterParameter filterParameter, AsyncCallback<ListResult<PensionProviderData>> async);

    void getPensionProvider(Integer objectID, AsyncCallback<PensionProviderData> async);

    void getSinglePayrunUpdates(Integer objectID, AsyncCallback<ArrayList<MyUpdateItem>> async);

    void getGroupPayrunUpdates(Integer objectID, AsyncCallback<ArrayList<MyUpdateItem>> async);

    void getCashAdvanceUpdates(Integer objectID, AsyncCallback<ArrayList<MyUpdateItem>> async);

    void initPayrunPayment(ListingFilterParameter fp, AsyncCallback<PayrunPayment> callback);

    void initPayrunPaymentItem(Integer singlePayrunID, AsyncCallback<PayrunPaymentItem> callback);

    void createPayrunPayment(PayrunPayment payment, AsyncCallback<SaveResultTO<Integer>> async);

    void createPayrunPaymentItem(PayrunPaymentItem paymentItem, AsyncCallback<SaveResultTO<Integer>> async);

    void getPayrunPaymentList(ListingFilterParameter filterParameter, AsyncCallback<ListResult<PayrunPayment>> callback);

    void getPayrunPayment(ListingFilterParameter fp, AsyncCallback<PayrunPayment> callback);

    void getPayrunPaymentItem(Integer paymentId, AsyncCallback<PayrunPaymentItem> callback);

    void deletePayrunPayment(Integer objectID, AsyncCallback<Boolean> async);

    void deletePayrunPaymentItem(Integer objectID, AsyncCallback<Boolean> async);

    void initPayrollPayment(ListingFilterParameter fp, AsyncCallback<PayrollPayment> callback);

    void initPayrollPaymentItem(Integer singlePayrunID, AsyncCallback<PayrollPaymentItem> callback);

    void createPayrollPayment(PayrollPayment payment, AsyncCallback<SaveResultTO<Integer>> async);

    void createPayrollPaymentItem(PayrollPaymentItem paymentItem, AsyncCallback<SaveResultTO<Integer>> async);

    void getPayrollPaymentList(ListingFilterParameter filterParameter, AsyncCallback<ListResult<PayrollPayment>> callback);

    void getPayrollPayment(ListingFilterParameter fp, AsyncCallback<PayrollPayment> callback);

    void getPayrollPaymentItem(Integer paymentId, AsyncCallback<PayrollPaymentItem> callback);

    void deletePayrollPayment(Integer objectID, AsyncCallback<Boolean> async);

    void deletePayrollPaymentItem(Integer objectID, AsyncCallback<Boolean> async);

    void loadPaymentNotes(Integer objectId, AsyncCallback<List<HistoryNote>> callback);

    void createPaymentHistoryNote(Integer objectId, HistoryListItem hisItem, AsyncCallback<Integer> callback);

    void getAdditionalPaymentData(ListingFilterParameter fp, AsyncCallback<AdditionalPayment> async);

    void saveAdditionalPayment(AdditionalPayment data, AsyncCallback<Void> async);

    void updatePaymentItemsAndStatus(AdditionalPayment payment, AsyncCallback<Void> async);

    void deleteAdditionalPayment(Integer objectID, AsyncCallback<Integer> async);

    void deleteAdditionalPaymentItem(Integer objectID, Integer employeeID, AsyncCallback<Integer> async);

    void getAdditionalPaymentList(ListingFilterParameter filterParameter, AsyncCallback<ListResult<AdditionalPayment>> async);

    void getAdditionalPaymentItemList(ListingFilterParameter filterParameter, AsyncCallback<ListResult<AdditionalPayment>> async);

    void getEmployeesForAdditionalPayment(ListingFilterParameter filterParameter, HashMap<Integer, PaymentDeductionObject> existingItems, AsyncCallback<AdditionalPayment> async);

    void getPayrollBatchCurrency(ListingFilterParameter filterParameter, AsyncCallback<CurrencyItem> async);

    void addImportToQueue(AdditionalPayment data, ImportFile importFile, AsyncCallback<String> callback);

    void getEmployeesForPaymentDeductionSettings(ListingFilterParameter lfp, AsyncCallback<LinkedHashMap<KpiTreeInfo, ArrayList<KpiTreeInfo>>> async);

    void getPayrollBatchData(Integer objectID, AsyncCallback<PayrollBatchData> async);

    void savePayrollBatch(PayrollBatchData data, AsyncCallback<Void> async);

    void deletePayrollBatch(Integer payrollBatchId, AsyncCallback<Void> async);

    void getPayrollBatches(ListingFilterParameter lfp, AsyncCallback<ListResult<PayrollBatchData>> async);

    void getPayrollBatchesForLookUp(ListingFilterParameter lfp, AsyncCallback<ArrayList<SelectItem>> async);

    void saveEmployeePayrollSettingsTemplate(NewEmployee employee, HashMap<String, String> payrollSettings, AsyncCallback<Integer> async);

    void getEmployeeTemplateList(ListingFilterParameter fp, AsyncCallback<ListResult<NewEmployee>> async);

    void getEmployeeTemplate(Integer employeeTemplateID, AsyncCallback<PayrollSettings> async);

    void updateEmployeeTemplateStatus(Integer employeeTemplateID, String status, String rejectionNote, AsyncCallback<Void> async);

    void deleteEmployeeTemplate(Integer employeeTemplateID, AsyncCallback<Void> async);

    void approvedActionForCashAdvance(Integer cashAdvanceId, boolean createTransaction, AsyncCallback<Void> async);

    void getFinancialYearDate(AsyncCallback<Date> async);

    void getCashAdvanceReportData(ListingFilterParameter lfp, AsyncCallback<CashAdvanceReportData> async);

    void getSalarReportData(ListingFilterParameter lfp, AsyncCallback<SalaryReportData> async);

    void getCompanyPayrollSettingsForGroupPayrunPDF(AsyncCallback<EmployerSettingsObject> async);

    void getDriversForLookUp(ListingFilterParameter filterParameter, AsyncCallback<SelectItem[]> callback);

    void saveCashAdvancePayment(CashAdvancePayment payment, AsyncCallback<TestRPC> callback);

    void getEmployeesMap(ListingFilterParameter fp, String formType, AsyncCallback<HashMap<WfmTreeItem, LinkedList<WfmTreeItem>>> async);

    void saveGroupEmployees(HashSet<Integer> members, Integer objectID, boolean isChecked, AsyncCallback<Void> async);

    void saveSickLeaveSettings(SickLeaveSettings sickLeaveSettings, AsyncCallback<TestRPC> callback);

    void getSickLeaveSettings(AsyncCallback<SickLeaveSettings> callback);

    void saveDailyRateSettings(DailyRateSettings settings, AsyncCallback<TestRPC> callback);

    void getDailyRateSettings(AsyncCallback<DailyRateSettings> callback);

    void getPayslipItems(PayslipFilter fp, AsyncCallback<PayrolTableItemListResult> callback);

    void createPayslipTable(GroupPayrunData item, PayslipFilter filter, AsyncCallback<SaveResultTO<Integer>> cb);

    void getPayslipTableItemsList(ListingFilterParameter filterParameter, AsyncCallback<PayrolTableItemListResult> cb);

    void getPayslipTableSimple(PayslipFilter filter, AsyncCallback<GroupPayrunData> cb);

    void batchChangePayrollGroupStatus(Integer id, String status, AsyncCallback<PayrollTotalTO> ac);

    void updateSinglePayrollItem(SinglePayrunItem item, Boolean dateChange, AsyncCallback<SinglePayrunItem> ac);

    void saveSingleParunCellValue(SinglePayrunItem rowValue, String columnCodeName, AsyncCallback<Boolean> asyncCallback);

    void getPredefinedValueOfCategory(Integer employeeId, Integer categoryId, AsyncCallback<BigDecimal> async);

    void isExistSuchAdditionalPaymentByCategory(ListingFilterParameter fp, AsyncCallback<Boolean> asyncCallback);

    void createPaymentDeduction(PaymentDeductionObject paymentOrDeductionItem, AsyncCallback<Integer> async);

    void getRecurringPaymentDeductionList(ListingFilterParameter fp, AsyncCallback<ListResult<RecurringPayDeductItem>> async);

    void getRecurringPayDeduction(Integer objectId, AsyncCallback<RecurringPayDeductItem> async);

    void saveRecurringPaymentDeduction(RecurringPayDeductItem pdItem, AsyncCallback<TestRPC> async);

    void deleteRecurringPaymentDeduction(Integer objectID, AsyncCallback<Boolean> async);

    void getSalaryDetailedReportData(ListingFilterParameter lfp, AsyncCallback<SalaryDetailedReportData> async);

    void getAdditionalPaymentItemsData(ListingFilterParameter fp, AsyncCallback<AdditionalPayment> async);

    void getOvertimeObjectList(ListingFilterParameter filterParametrs, AsyncCallback<ListResult<OvertimeObject>> listResultAbstractAsyncCallback);

    void createAdditionalPaymentFromOvertime(Integer overtimeItemId, AsyncCallback<AdditionalPayment> async);

    void deleteOvertimeItemById(Integer objectId, AsyncCallback<Void> async);

    void getOvertimeEmployees(ListingFilterParameter filterParameter, AsyncCallback<List<SelectItem>> async);

    void generateOvertimeCode(AsyncCallback<NumberData> async);

    void saveOvertimeItem(OvertimeObject overtimeObject, AsyncCallback<Integer> async);

    void getOvertimeObject(Integer objectId, Boolean isEditForm, AsyncCallback<OvertimeObject> async);

    void updateOvertimeItemsAndStatus(OvertimeObject data, AsyncCallback<Void> async);

    void getPayrollZones(ListingFilterParameter fp, AsyncCallback<ListResult<SelectItem>> async);

    void saveIndustrySettings(String value, AsyncCallback<Boolean> async);
}
