package com.edatasite.workforce.gwt.payroll.client.rpc;

import com.edatasite.workforce.gwt.core.client.EmailHostException;
import com.edatasite.workforce.gwt.core.client.Exceptions.EmployeeCodeExistsException;
import com.edatasite.workforce.gwt.core.client.Exceptions.NoAccessUserLimitException;
import com.edatasite.workforce.gwt.core.client.Exceptions.UsernameAlreadyExistsException;
import com.edatasite.workforce.gwt.core.client.Exceptions.UsersLimitExceededException;
import com.edatasite.workforce.gwt.core.client.PayslipItemFilter;
import com.edatasite.workforce.gwt.core.client.Utils;
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
import com.edatasite.workforce.gwt.documents.client.exceptions.ObjectNotFoundException;
import com.edatasite.workforce.gwt.employee.client.rpc.EmployeePayrollSettingsObject;
import com.edatasite.workforce.gwt.importfile.client.rpc.ImportFile;
import com.edatasite.workforce.gwt.newemployee.client.rpc.NewEmployee;
import com.edatasite.workforce.gwt.payroll.client.ui.view.dailyRateCalculation.DailyRateSettings;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.ServiceDefTarget;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;

public interface PayrollService extends RemoteService {

    PayrollSettings getEmployeeDetailsAndPayrollSettings(Integer employeeId, Date date);

    Integer createCategory(CategoryObject category);

    CategoryObject[] getCompanyCategories(boolean isArabic);

    Boolean checkUsingCategory(Integer id);

    void deleteCategory(Integer id);

    ListResult<CategoryObject> getCompanyCategories(ListingFilterParameter fp);

    Integer createPaymentDeduction(PaymentDeductionObject paymentOrDeductionItem);

    ListResult<PayrollGlobalSettingsData> getPaymentDections(ListingFilterParameter fp);

    void savePaymentDeductionSettings(PayrollGlobalSettingsData pds);

    EmployeePayrollSettingsObject getEmployeePayrollSettings(Integer employeeID, String key);

    Integer addNewStarter(NewEmployee employee, HashMap<String, String> payrollSettings) throws UsernameAlreadyExistsException, EmailHostException, UsersLimitExceededException, NoAccessUserLimitException;

    Integer saveCompanyPayrollSettings(EmployerSettings payrollSettings);

    String getCompanyPayrollSettings(String key);

    GroupPayrunData getGroupPayrunSettings();

    EmployerSettings getCompanyPayrollSettings();

    String calculateNITableLetter(HashMap<Integer, Boolean> params);

    SelectItem[] getPensionProviders();

    SelectItem[] getPensionSchemes();

    ListResult<PensionSchemeData> getPensionSchemeList(ListingFilterParameter filterParametrs);

    PensionSchemeData getPensionSchemeById(Integer id);

    void deletePensionScheme(Integer id);

    void savePensionScheme(PensionSchemeData pensionScheme, Boolean update);

    void savePensionProvider(PensionProviderData pensionProvider);

    ListResult<NiTaxChangesListItem> getNiTaxChanges(ListingFilterParameter fp);

    void createBankAccount(UserBankAccountData bankAccount);

    HashMap<String, Date> calculateDatesByWeekOrMonthNumber(int weekOrMonthNumber, int frequencyID, Date selectedDate);

    Date calculatePeriodStartDate(int weekOrMonthNumber, int frequencyID, Date selectedDate);

    Date calculatePeriodEndDate(int weekOrMonthNumber, int frequencyID, Date date);

    Integer savePaymentDeductionCategory(CategoryObject category);

    CategoryObject getPaymentDeductionCategory(Integer objectID);

    Integer rollback(Integer frequency, Integer period, Integer taxYear, Integer employeeID);

    SelectItem[] getCompanyTaxYears(Integer companyID);

    boolean isCountryUK();

    PaymentDeductionSelectItem[] getCategoriesForLookUp(ListingFilterParameter filterParametrs);

    GroupPayrunData getPayslipTable(PayslipFilter filter);

    ListResult<GroupPayrunData> getPayslipTableList(ListingFilterParameter filterParameter);

    void sendPayslipNotification(Integer payslipTableID);

    void deletePayslipTable(Integer objectID);

    EndOfServiceData saveEndOfServiceSettings(EndOfServiceData settings);

    EndOfServiceData getEndOfServiceSettings(String countryCode);

    ListResult<EoSCalculationData> getEndOfServiceGratuityList(ListingFilterParameter filterParameter);

    EoSCalculationData getEmployeeEosData(PayslipItemFilter filter, String countryCode);

    Boolean saveEosCalculationData(EoSCalculationData data);

    SelectItem[] getJobTitles();

    CashAdvanceItem getCashAdvancedItem(ListingFilterParameter fp);

    ListResult<CashAdvanceItem> getCashAdvanceList(ListingFilterParameter fp);

    TestRPC saveCashAdvance(CashAdvanceItem cashAdvanceItem);

    void approvedActionForCashAdvance(Integer cashAdvanceId, boolean createTransaction);

    EosReportData getEmployeeEosDataList(ListingFilterParameter lfp);

    WpsReportData getWpsReportData(ListingFilterParameter lfp);

    HashMap<SelectItem, SelectItem[]> getYearMonthsForWps();

    boolean deleteCashAdvance(Integer objectID);

    void deleteEndOfServiceGratuity(Integer objectID);

    EoSCalculationData getEndOfServiceGratuity(Integer objectID);

    void deleteSinglePayrun(Integer objectID, Integer employeeID);

    PayrollGlobalSettingsData getPaymentDeductionSettingsData(Integer objectID);

    ArrayList<PensionContributionData> getPensionContributionByFilter(Integer month, Integer year);

    void deletePaymentDeductionSettings(Integer settingsID);

    Integer saveSinglePayrun(SinglePayrunItem data);

    void approveSinglePayrun(SinglePayrunItem data);

    Boolean hasPaymentItems(Integer singlePayrunID);

    SinglePayrunItem getSinglePayrunData(PayslipItemFilter filter);

    SinglePayrunItem getSinglePayrunItemPaymentDeductionCategories(Integer objectID);

    ListResult<SinglePayrunItem> getSinglePayrunList(ListingFilterParameter filterParameter);

    PayslipTableRequestObject getSinglePayrunPdfData(Integer singlePayrunID);

    void deletePensionProvider(Integer pensionProviderID);

    ListResult<PensionProviderData> getPensionProviders(ListingFilterParameter filterParameter);

    PensionProviderData getPensionProvider(Integer objectID);

    ArrayList<MyUpdateItem> getSinglePayrunUpdates(Integer objectID);

    ArrayList<MyUpdateItem> getGroupPayrunUpdates(Integer objectID);

    ArrayList<MyUpdateItem> getCashAdvanceUpdates(Integer objectID);

    PayrunPayment initPayrunPayment(ListingFilterParameter fp);

    PayrunPaymentItem initPayrunPaymentItem(Integer singlePayrunID);

    SaveResultTO<Integer> createPayrunPayment(PayrunPayment payment);

    SaveResultTO<Integer> createPayrunPaymentItem(PayrunPaymentItem paymentItem);

    ListResult<PayrunPayment> getPayrunPaymentList(ListingFilterParameter filterParameter);

    PayrunPayment getPayrunPayment(ListingFilterParameter fp);

    PayrunPaymentItem getPayrunPaymentItem(Integer paymentId);

    Boolean deletePayrunPayment(Integer objectID);

    Boolean deletePayrunPaymentItem(Integer objectID);

    PayrollPayment initPayrollPayment(ListingFilterParameter fp);

    PayrollPaymentItem initPayrollPaymentItem(Integer singlePayrunID);

    SaveResultTO<Integer> createPayrollPayment(PayrollPayment payment);

    SaveResultTO<Integer> createPayrollPaymentItem(PayrollPaymentItem paymentItem);

    ListResult<PayrollPayment> getPayrollPaymentList(ListingFilterParameter filterParameter);

    PayrollPayment getPayrollPayment(ListingFilterParameter fp);

    PayrollPaymentItem getPayrollPaymentItem(Integer paymentId);

    Boolean deletePayrollPayment(Integer objectID);

    Boolean deletePayrollPaymentItem(Integer objectID);

    List<HistoryNote> loadPaymentNotes(Integer objectId);

    Integer createPaymentHistoryNote(Integer objectId, HistoryListItem hisItem);

    AdditionalPayment getAdditionalPaymentData(ListingFilterParameter fp);

    void saveAdditionalPayment(AdditionalPayment data);

    void updatePaymentItemsAndStatus(AdditionalPayment payment);

    Integer deleteAdditionalPayment(Integer objectID);

    Integer deleteAdditionalPaymentItem(Integer objectID, Integer employeeID);

    ListResult<AdditionalPayment> getAdditionalPaymentList(ListingFilterParameter filterParameter);

    ListResult<AdditionalPayment> getAdditionalPaymentItemList(ListingFilterParameter filterParameter);

    AdditionalPayment getEmployeesForAdditionalPayment(ListingFilterParameter filterParameter, HashMap<Integer, PaymentDeductionObject> existingItems);

    CurrencyItem getPayrollBatchCurrency(ListingFilterParameter filterParameter);

    String addImportToQueue(AdditionalPayment data, ImportFile importFile);

    void savePayrollBatch(PayrollBatchData data);

    void deletePayrollBatch(Integer payrollBatchId);

    PayrollBatchData getPayrollBatchData(Integer objectID);

    ListResult<PayrollBatchData> getPayrollBatches(ListingFilterParameter lfp);

    ArrayList<SelectItem> getPayrollBatchesForLookUp(ListingFilterParameter lfp);

    LinkedHashMap<KpiTreeInfo, ArrayList<KpiTreeInfo>> getEmployeesForPaymentDeductionSettings(ListingFilterParameter lfp);

    Integer saveEmployeePayrollSettingsTemplate(NewEmployee employee, HashMap<String, String> payrollSettings) throws UsernameAlreadyExistsException, EmailHostException, EmployeeCodeExistsException;

    ListResult<NewEmployee> getEmployeeTemplateList(ListingFilterParameter fp);

    PayrollSettings getEmployeeTemplate(Integer employeeTemplateID);

    void updateEmployeeTemplateStatus(Integer employeeTemplateID, String status, String rejectionNote);

    void deleteEmployeeTemplate(Integer employeeTemplateID);

    Date getFinancialYearDate();

    CashAdvanceReportData getCashAdvanceReportData(ListingFilterParameter lfp);

    SalaryReportData getSalarReportData(ListingFilterParameter lfp);

    EmployerSettingsObject getCompanyPayrollSettingsForGroupPayrunPDF();

    SelectItem[] getDriversForLookUp(ListingFilterParameter filterParameter);

    TestRPC saveCashAdvancePayment(CashAdvancePayment payment);

    HashMap<WfmTreeItem, LinkedList<WfmTreeItem>> getEmployeesMap(ListingFilterParameter fp, String formType);

    void saveGroupEmployees(HashSet<Integer> members, Integer objectID, boolean isChecked);

    TestRPC saveSickLeaveSettings(SickLeaveSettings sickLeaveSettings);

    SickLeaveSettings getSickLeaveSettings();

    TestRPC saveDailyRateSettings(DailyRateSettings settings);

    DailyRateSettings getDailyRateSettings();

    PayrolTableItemListResult getPayslipItems(PayslipFilter fp);

    SaveResultTO<Integer> createPayslipTable(GroupPayrunData item, PayslipFilter filter);

    PayrollTotalTO deleteSinglePayrun(Integer id);

    PayrolTableItemListResult getPayslipTableItemsList(ListingFilterParameter filterParameter);

    GroupPayrunData getPayslipTableSimple(PayslipFilter filter);

    PayrollTotalTO batchChangePayrollGroupStatus(Integer id, String status);

    SinglePayrunItem updateSinglePayrollItem(SinglePayrunItem item, Boolean dateChange);

    boolean saveSingleParunCellValue(SinglePayrunItem rowValue, String columnCodeName);

    BigDecimal getPredefinedValueOfCategory(Integer employeeId, Integer categoryId);

    Boolean isExistSuchAdditionalPaymentByCategory(ListingFilterParameter fp);

    ListResult<RecurringPayDeductItem> getRecurringPaymentDeductionList(ListingFilterParameter fp);

    RecurringPayDeductItem getRecurringPayDeduction(Integer objectId);

    TestRPC saveRecurringPaymentDeduction(RecurringPayDeductItem pdItem);

    Boolean deleteRecurringPaymentDeduction(Integer objectID);

    SalaryDetailedReportData getSalaryDetailedReportData(ListingFilterParameter lfp);

    AdditionalPayment getAdditionalPaymentItemsData(ListingFilterParameter fp);

    ListResult<OvertimeObject> getOvertimeObjectList(ListingFilterParameter filterParametrs);

    AdditionalPayment createAdditionalPaymentFromOvertime(Integer overtimeItemId);

    void deleteOvertimeItemById(Integer objectId) throws ObjectNotFoundException;

    List<SelectItem> getOvertimeEmployees(ListingFilterParameter filterParameter);

    NumberData generateOvertimeCode();

    Integer saveOvertimeItem(OvertimeObject overtimeObject);

    OvertimeObject getOvertimeObject(Integer objectId, Boolean isEditForm);

    void updateOvertimeItemsAndStatus(OvertimeObject data);

    ListResult<SelectItem> getPayrollZones(ListingFilterParameter fp);

    Boolean saveIndustrySettings(String value);

    class App {
        public static PayrollServiceAsync get() {
            ServiceDefTarget target = GWT.create(PayrollService.class);
            target.setServiceEntryPoint(Utils.getRpcBaseUrl() + "/payroll");
            return (PayrollServiceAsync) target;
        }
    }
}
