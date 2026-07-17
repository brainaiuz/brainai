package com.edatasite.workforce.gwt.accounting.client.rpc;

import com.edatasite.workforce.gwt.accounting.client.DateFormatParseException;
import com.edatasite.workforce.gwt.accounting.client.rpc.consignment.TrialBalanceFilter;
import com.edatasite.workforce.gwt.accounting.client.ui.view.balancesheet.BalancesheetSettings;
import com.edatasite.workforce.gwt.accounting.client.ui.view.inventory.product.ProductPicture;
import com.edatasite.workforce.gwt.accounting.client.ui.view.newreport.BudgetSheet.BudgetColumn;
import com.edatasite.workforce.gwt.accounting.client.ui.view.newreport.BudgetSheet.BudgetManagerItem;
import com.edatasite.workforce.gwt.contact.client.rpc.CrmAccountItem;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.DeleteRPC;
import com.edatasite.workforce.gwt.core.client.rpc.HistoryListItem;
import com.edatasite.workforce.gwt.core.client.rpc.NumberData;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.TaxItem;
import com.edatasite.workforce.gwt.core.client.rpc.TestRPC;
import com.edatasite.workforce.gwt.core.client.rpc.TreeSelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.accounting.AccountItem;
import com.edatasite.workforce.gwt.core.client.rpc.accounting.BankAccountItem;
import com.edatasite.workforce.gwt.core.client.rpc.accounting.TransactionsReport;
import com.edatasite.workforce.gwt.core.client.rpc.currency.CurrencyItem;
import com.edatasite.workforce.gwt.core.client.rpc.facet.FacetFilterRpc;
import com.edatasite.workforce.gwt.core.client.rpc.historyNote.HistoryNote;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.view.BankTransferNumberData;
import com.edatasite.workforce.gwt.core.client.ui.view.CustomFormItemPdfTemplateList;
import com.edatasite.workforce.gwt.importfile.client.rpc.ImportFile;
import com.edatasite.workforce.gwt.importfile.client.rpc.ImportStatus;
import com.edatasite.workforce.gwt.submodule.paymentdeduction.client.SettingsData;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.ServiceDefTarget;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;


/**
 * Created by IntelliJ IDEA.
 * User: Admin
 * Date: 18.02.2009
 * Time: 15:27:13
 * To change this template use File | Settings | File Templates.
 */
public interface AccountingService extends RemoteService {

    void createBankAccount(BankAccount bankAccount);

    ListingResult<Transaction> getJournalReportWithPaging(DateNonConvertable fromDate, DateNonConvertable toDate, String orderBy, Integer journalId, ListingFilterParameter listingFilterParameter);

    TrialBalanceFilterData getTrialBalanceFilterData();

    TrialBalance getTrialBalance(TrialBalanceFilter tbf);

    TrialBalance getTrialBalanceDetailed(TrialBalanceFilter tbf);

    CashFlow getCashFlow(ListingFilterParameter filter);

    BalanceSheet getBalanceSheet(DateNonConvertable fromDate, DateNonConvertable toDate, boolean isConsolidation, Integer departmentID, Integer projectID, Integer currencyId);

    BalanceSheetSummary getBalanceSheetSummary(DateNonConvertable fromDate, DateNonConvertable toDate, Boolean isConsoladition, Integer departmentID, Integer projectID, Integer currencyId);

    BudgetManagerItems getProfitAndLoss(PnLFilter filter);

    TransactionsBetweenDatesInAccount findTransactionsByAccountAndJournalDate(ListingFilterParameter fp, DateNonConvertable fromDate, DateNonConvertable toDate);

    TransactionsReport getTransactionsReportForDashboard(DateNonConvertable from, DateNonConvertable to);

    AccountsByCategory getAccounts();

    AccountsByCategory getAccountsForPayment();

    AccountsByCategory getAccountsForInvoice();

    AccountItem[] getAccountsForInvoice(ListingFilterParameter fp, ArrayList<String> types);

    AccountsByCategory getAllGLAccounts();

    AccountItem[] getAccountsForExpense(ListingFilterParameter filterParametrs);

    ListResult<SelectItem> getAccountsForExpenseLookUp(ListingFilterParameter filterParameter);

    AccountTypesByCategory getAccountTypes();

    AccountItem getAccountCodeUnique(String code, Integer accountID);

    AccountItem createAccount(AddAccountItem accountItem);

    void createAccounts(ArrayList<AddAccountItem> accountItems);

    void createDefaultAccountingParametersForMobile();

    AddAccountItem getAccount(Integer accountId);

    TaxItem saveTaxRate(TaxData data);

    ListResult<WarehouseItem> getWarehousesList(ListingFilterParameter filterParametrs);

    WarehouseItem getWarehouse(Integer warehouseID);

    ListResult<ProductLocationItem> getWarehouseProductsList(ListingFilterParameter filterParametrs);

    ListResult<BankAccount> getBankAccounts(ListingFilterParameter filterParametrs);

    BankAccountItem[] getBankAccountsForLookUp(ListingFilterParameter filterParametrs);

    SelectItem[] getBankAccountItems();

    SelectItem[] getBankAccountItemsForReference();

    BudgetRelatedIds createAccountsBudget(AccountBudget budget);

    ListResult<AccountListItem> getAccountList(ListingFilterParameter filterParametrs);

    AccountSingleItem getAccountById(Integer accountId);

    AddAccountData getAccountData(Integer objectID);

    SelectItem getSelectedWarehouseForTransactions();

    AddAccountItem getAccountForEdit(Integer accountId);

    Integer updateAccount(AddAccountItem editAccount);

    TransactionItem[] getAccountBalances();

    void createConBalFromInvoiceTransactionAndTrialBalance();

    Integer voidExpenseTransaction(Integer expenseReportId, DateNonConvertable voidDate);

    SelectItem[] getTaxPeriods();

    SelectItem[] getTaxBasises();

    void saveFinancialSettings(FinancialSettingsItem fs);

    FinancialSettingsItem getCompanyFinancialSettings();

    boolean hasTransaction();

    DeleteRPC removeGLAccount(Integer accountID);

    CurrencyItem getCompanyBaseCurrency();

    String deleteBankAccount(Integer bankAccountId);

    Integer transferMoney(TransferMoneyData data);

    SelectItem[] getWarehousesAsSelectItem();

    SelectItem[] getCategoriesAsSelectItem();

    SelectItem[] getCategoriesAsSelectItemForSettings();

    TreeSelectItem[] getCategoriesAsTreeSelectItem();

    TreeSelectItem[] getCategoriesAsTreeSelectItemForSettings();

    SelectItem[] getProductCategoriesAsSelectItem();

    SelectItem[] getProductCategoriesAsSelectItem(ListingFilterParameter filterParametrs);

    SelectItem[] getBrandsAsSelectItem();

    SelectItem[] getVendorsAsSelectItem();

    SelectItem[] getUnitMeasurementsAsSelectItem();

    Integer saveWarehouse(WarehouseItem warehouse);

    boolean deleteWarehouse(Integer warehouseId);

    Integer saveWarehouseLocation(WarehouseLocationItem warehouseLocation);

    SelectItem saveUnitMeasurement(UnitMeasurementItem warehouseLocation);

    HashMap<String, Integer> deleteUnitMeasurement(Integer unitMeasurementId);

    void updateUnitMeasurement(UnitMeasurementItem measurementItem);

    Integer saveProductCategory(ProductCategoryItem productCategory);

    Integer[] saveProductCategories(ProductCategoryList productCategoriesList);

    boolean deleteProductCategory(Integer productCategoryId);

    boolean deleteShippingMedthod(Integer shippingMedthodId);

    Boolean deleteBrand(Integer brandID);

    ListResult<ProductCategoryItem> getProductCategoriesList(ListingFilterParameter filterParametrs);

    ListResult<UnitMeasurementItem> getUnitMeasurementsList(ListingFilterParameter filterParametrs);

    UnitMeasurementItem getUnitMeasurement(Integer measurementID);

    ProductCategoryItem getProductCategory(Integer categoryID);

    boolean deleteProductCategoryImage(Integer categoryID, Integer imageID);

    BankAccountAttachment[] getBankAccountFilesList(Integer bankAccountID);

    BankAccountImportStatementData getTransactionMapping(Integer bankAccountAttachmentID);
    BankAccountImportStatementData getTransactionMappingByAI(Integer bankAccountAttachmentID);

    Boolean saveStatements(BankAccountImportStatementData importStatementData) throws DateFormatParseException;
    Boolean saveStatementsByAIMapping(BankAccountImportStatementData importStatementData) throws DateFormatParseException;

    BankAccountStatementTO[] getStatementItems(Integer bankAccountAttachementID);

    ArrayList<Transaction> findOthers(FindMatchFilterData filterData);

    ArrayList<Transaction> findAndMatchTransactions(FindMatchFilterData filterData);

    Boolean reconcileStatement(Integer statementItemID, ArrayList<Integer> transactionIDs, Integer accountId);

    void changeReconcileStatus(Integer transactionID, String status, Integer accountId);

    ListResult<BankStatementListItem> getBankAccountStatements(Integer bankAccountID, ListingFilterParameter fp);

    ListResult<BankStatementItemListItem> getBankAccountStatementItems(Integer bankStatementID, ListingFilterParameter fp);

    BankStatementItemListItem getBankAccountStatementItem(Integer objectID, Integer bankStatementID);

    void saveBankAccountStatementItem(BankStatementItemListItem item);

    Boolean deleteBankAccountStatementItem(Integer objectID, Integer bankStatementID);

    Integer spendOrReceiveMoney(NewManualTransaction manualTransaction);

    BankTransferNumberData reGenerateMoneyNumber(Integer transferType);

    NewManualTransaction getBankTransferData(ListingFilterParameter filterParametrs);

    void deleteBankTransfer(Integer objectID, String transactionType);

    TestRPC checkIfBrandExists(BrandItem brand);

    Integer saveBrand(BrandItem brand);

    void updateBrand(BrandItem brand);

    ListResult<BrandItem> getBrandsList(ListingFilterParameter filterParametrs);

    BrandItem getBrand(Integer brandID);

    ProductPicture[] getProductCategoryPictures(Integer categoryID, Integer fileSizeType);

    Boolean deleteProductCategoryPicture(Integer categoryPictureId);

    Boolean setDefaultProductCategoryPicture(Integer pictureId, Integer categoryID);

    DateNonConvertable[] getVatReturnDateInterval();

    VatReturnTransferObject getVatReturnReport(DateNonConvertable from, DateNonConvertable to, BigDecimal flatPercent);

    BankReconcilationReportData getBankAccountReconcilationReport(String givenDateString, String givenFromDateString, Integer bankAccountID);

    String saveVatReturnReport(DateNonConvertable from, DateNonConvertable to, VatReturnTransferObject vatReturn);

    String submitVatReturnReportToHMRC(Integer vatReturnID);

    ListResult<VatReturnTransferObject> getVatReturnReportList(ListingFilterParameter filterParametrs);

    void deleteVatReturnReport(Integer id);

    HashMap<String, SelectItem[]> getCSVColumns(Integer objectId);

    ImportStatus onChartOfAccountsImport(ImportFile importFile, boolean deleteNotUsedAccounts);

    ImportStatus onBudgetManagerImport(ImportFile importFile);

    void importProductsFromParentCompany(ImportFile importFile);

    ListResult<ReservationItem> getReservationList(ListingFilterParameter filterParametrs);

    Integer makeReservation(ReservationItem item);

    NewProduct[] getRentalItems(ListingFilterParameter filterParametrs);

    ReservationItem getReservation(Integer objectID);

    ProductImportFillingData getProductImportFillingData();

    TaxItem[] getCompanyTaxes();

    Integer makeReservation(ReservationItem reservationItem, Integer companyId);

    boolean copyCustomFieldsToSubCategories(Integer objectID);

    AccountTypeItem[] getAccountTypeItems();

    void saveAccountTypeNumbering(AccountTypeItem[] items);

    String getGeneratedAccountNumber(Integer accountTypeID);

    SelectItem[] getClientList(ListingFilterParameter filterParametrs);

    void completeAccountingGettingStarted(SettingsData settingsData, boolean enableMultiCurrency);

    void createOrUpdateAccountFromSaasu(AddAccountItem accountItem);

    void saveBankCheckData(BankCheckData bankCheckData);

    BankCheckData getBankCheckData(Integer objectID);

    ListResult<BankCheckData> getBankCheckList(ListingFilterParameter filterParametrs);

    TransferMoneyData getBankAccountSummaryData(Integer objectId, Integer fromAccountID);

    ArrayList<String> deleteBankCheckData(Integer objectID);

    SelectItem[] getLookUpItems(ListingFilterParameter filterParametrs, int type);

    SelectItem[] getWarehousesForLookUp(ListingFilterParameter filterParameter);

    SelectItem[] getProductLocations(ListingFilterParameter filterParameter);

    void updateAccountsAfterExportSaasu(Integer objectId, Date lastUpdateDate, String saasuLastUpdatedUid, Integer saasuGUID);

    WarehouseLocationItem getDetailedProductLocation(ListingFilterParameter filterParameter);

    SelectItem[] getMemorizedTransactionsForLookUp(ListingFilterParameter filterParametrs);

    BigDecimal getCompanyIncome(ListingFilterParameter filterParameter);

//    void updateAccountByQB(AddAccountItem accountItem, Integer synchItemId);
//
//    void updateCheckByQB(BankCheckData bankCheckData, Integer synchItemId);

    CurrencyAdjustmentFillingData getCurrencyAdjustmentData();

    CurrencyAdjustmentData calculateCurrencyAdjustment(CurrencyAdjustmentData adjustmentData);

    Integer saveCurrencyAdjustment(CurrencyAdjustmentData currencyAdjustmentData);

    MultiCurrencyExchangeRateItem getMultiCurrencyExchangeRate(Integer year, Integer month);

    Boolean saveCurrenciesExchangeRate(ExchangeRateItem exchangeRateItem);

    Transaction getTransaction(Integer transactionID);

    boolean deleteSupplierPayment(Integer transactionID);

    SelectItem[] getSalesMansAsSelectItem(ListingFilterParameter listingFilterParameter);

    ListResult<TrashBinListItem> getTrashBinList(ListingFilterParameter filterParametrs);

    void changeTrashBinStatus(Integer objectID, String status);

    ImportProductInitItem getProductInitData();

    Integer updateIncomeTaxData(DateNonConvertable fromDate, DateNonConvertable toDate);

    Boolean getVatReturnReportVisibility();

    SelectItem[] getDepartmentsForAccounting(ListingFilterParameter filterParametrs);

    void deleteBankAccountFile(Integer fileID);

    Integer saveCsvTemplate(String templateName, String templateType);

    SelectItem[] getCsvTemplates(String templateType);

    ArrayList<CsvTemplateItem> getCsvTemplateData(Integer templateId);

    Boolean saveCsvTemplateData(Integer csvTemplateID, ArrayList<CsvTemplateItem> csvTemplateData);

    String[] getCompanyDetails(Integer userId);

    SelectItem[] getAccountsForPaymentPost(ListingFilterParameter filterParameter);

    ListResult<NewManualTransaction> getBankCashTransferList(ListingFilterParameter fp);

    SelectItem[] getAccountsForLookUp(ListingFilterParameter filterParametrs);

    /**
     * Account ID is a chart of account id of bank account not the self bank account id
     *
     * @param accountID
     * @return
     */
    BigDecimal getBankAccountLastExchangeRate(Integer accountID);

    AccountList getAccountListByAccountType(ListingFilterParameter fp);

    SelectItem[] getBrandList(ListingFilterParameter lfp);

    SelectItem[] getRFQList(ListingFilterParameter lfp);

    SelectItem[] getRFPList(ListingFilterParameter lfp);

    Integer createBankTransferNote(Integer transferID, HistoryListItem hisItem);

    Boolean deleteBankTransferNote(Integer bankTransferID);

    ListResult<BankStatementItemListItem> getBankAccountStatementItemList(ListingFilterParameter fp);

    ListResult<PaymentMethodItem> getAllPaymentMethods(ListingFilterParameter fp);

    Integer savePaymentMethod(PaymentMethodItem pmi);

    Boolean deletePaymentMethod(Integer objectID);

    ArrayList<AccountItem> getAccountsReceivablePayable(ListingFilterParameter filterParametrs);

    PaymentMethodItem getPaymentMethodById(Integer objectID);

    BalancesheetSettings getBalancesheetSettings();

    void saveBalancesheetSettings(BalancesheetSettings settings);

    ArrayList<SelectItem> getTransactionJournals(ListingFilterParameter fp);

    boolean saveBankAccountCellValue(BankAccount rowValue, String columnCodeName);

    boolean saveBankTransferCellValue(NewManualTransaction rowValue, String columnCodeName);

    ConversionBalanceItem getConversionBalanceItem();

    List<HistoryNote> getBankTransferHistoryNotes(Integer id, String viewType);

    Boolean deleteSelectedAccounts(ArrayList<Integer> ids);

    BigDecimal getItemQtyByWarehouse(Integer productId, Integer warehouseId);

    void saveProductCategoryCellValue(ProductCategoryItem rowValue, String columnCodeName);

    NumberData generateProductCategoryNumber();

    NumberData generateProductCategoryNumber(Integer intNumber);

    BankAccount getBankAccountForEdit(Integer objectId);

    BudgetManagerItems getBudgetedDataItem(Integer budgetManagerId, DateNonConvertable from, DateNonConvertable to, boolean isAsc);

    Integer saveBudgetManager(BudgetManagerItem budgetsheetItem);

    void createBudgetManagerItem(AccountBudget budget);

    BudgetManagerItem getBudgetManagerData(Integer objectID);

    void deleteBudgetManager(Integer objectID);

    void saveBudgetManagerAssignItems(Integer budgetManagerId, HashMap<Integer, List<Integer>> items);

    Integer saveBudgetManagerColumn(Integer budgetID, BudgetColumn budgetColumn, boolean isEdit);

    BudgetColumn getBudgetManagerColumnData(Integer budgetID, String columnCode);

    void deleteBudgetManagerColumn(Integer budgetID, String columnCode);

    List<SelectItem> getCustomfieldsByType(String description);

    ExpensesAndRevenue getBudgetedAccounts(DateNonConvertable from, DateNonConvertable to, Integer departmentID, boolean isAsc);

    void saveAccountBalances(ConversionBalanceItem conversionBalanceItem);

    FacetFilterRpc getProductCatecorFacetFilterData(FacetFilterRpc facetFilterRpc);

    void saveHMRCAuthSettings(HMRCAuthSettingsItem hmrcAuthSettingsItem);

    boolean activeProductCategory(Integer id, boolean b);

    void initZatcaSettings();

    boolean saveWEditCellValue(WarehouseItem rowValue, String columnCodeName);

    String shortenLink(String link, CrmAccountItem invocieId);


    CustomFormItemPdfTemplateList getCrmAccountBalancePDFTemplates();

    List<SelectItem> getProductLocationReference(String referenceCode);

    class App {
        public static AccountingServiceAsync get() {
            ServiceDefTarget target = GWT.create(AccountingService.class);
            target.setServiceEntryPoint(Utils.getRpcBaseUrl() + "/accounting");
            return (AccountingServiceAsync) target;
        }
    }
}
