package com.edatasite.workforce.gwt.accounting.client.rpc;

import com.edatasite.workforce.gwt.accounting.client.rpc.consignment.TrialBalanceFilter;
import com.edatasite.workforce.gwt.accounting.client.ui.view.balancesheet.BalancesheetSettings;
import com.edatasite.workforce.gwt.accounting.client.ui.view.inventory.product.ProductPicture;
import com.edatasite.workforce.gwt.accounting.client.ui.view.newreport.BudgetSheet.BudgetColumn;
import com.edatasite.workforce.gwt.accounting.client.ui.view.newreport.BudgetSheet.BudgetManagerItem;
import com.edatasite.workforce.gwt.contact.client.rpc.CrmAccountItem;
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
import com.google.gwt.http.client.Request;
import com.google.gwt.user.client.rpc.AsyncCallback;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Anvarbek
 * Date: 21.02.2009
 * Time: 14:47:03
 * To change this template use File | Settings | File Templates.
 */
public interface AccountingServiceAsync {

    void createBankAccount(BankAccount bankAccount, AsyncCallback<Void> async);

    void getTrialBalanceFilterData(AsyncCallback<TrialBalanceFilterData> callback);

    void getTrialBalance(TrialBalanceFilter tbf, AsyncCallback<TrialBalance> callback);

    void getTrialBalanceDetailed(TrialBalanceFilter tbf, AsyncCallback<TrialBalance> callback);

    void getCashFlow(ListingFilterParameter filter, AsyncCallback<CashFlow> callback);

    void getBalanceSheet(DateNonConvertable fromDate, DateNonConvertable toDate, boolean isConsolidation, Integer departmentID, Integer projectID, Integer currencyId, AsyncCallback<BalanceSheet> async);

    void getBalanceSheetSummary(DateNonConvertable fromDate, DateNonConvertable toDate, Boolean isConsoladition, Integer departmentID, Integer projectID, Integer currencyId, AsyncCallback<BalanceSheetSummary> async);

    void getProfitAndLoss(PnLFilter filter, AsyncCallback<BudgetManagerItems> async);

    Request findTransactionsByAccountAndJournalDate(ListingFilterParameter fp, DateNonConvertable fromDate, DateNonConvertable toDate, AsyncCallback<TransactionsBetweenDatesInAccount> async);

    void getTransactionsReportForDashboard(DateNonConvertable from, DateNonConvertable to, AsyncCallback<TransactionsReport> callback);

    void getAccounts(AsyncCallback<AccountsByCategory> async);

    void getAccountsForPayment(AsyncCallback<AccountsByCategory> async);

    void getAccountsForInvoice(AsyncCallback<AccountsByCategory> async);

    void getAllGLAccounts(AsyncCallback<AccountsByCategory> async);

    void getAccountsForExpense(ListingFilterParameter filterParametrs, AsyncCallback<com.edatasite.workforce.gwt.core.client.rpc.accounting.AccountItem[]> async);

    void getAccountTypes(AsyncCallback<AccountTypesByCategory> async);

    void getAccountCodeUnique(String code, Integer accountID, AsyncCallback<com.edatasite.workforce.gwt.core.client.rpc.accounting.AccountItem> async);

    void createAccount(AddAccountItem accountItem, AsyncCallback<com.edatasite.workforce.gwt.core.client.rpc.accounting.AccountItem> async);

    void createDefaultAccountingParametersForMobile(AsyncCallback<Void> callback);

    void getAccount(Integer accountId, AsyncCallback<AddAccountItem> async);

    void saveTaxRate(TaxData data, AsyncCallback<TaxItem> async);


    void deleteWarehouse(Integer warehouseId, AsyncCallback<Boolean> async);

    void deleteUnitMeasurement(Integer unitMeasurementId, AsyncCallback<HashMap<String, Integer>> async);

    Request getWarehousesList(ListingFilterParameter filterParametrs, AsyncCallback<ListResult<WarehouseItem>> async);

    Request getWarehouseProductsList(ListingFilterParameter filterParametrs, AsyncCallback<ListResult<ProductLocationItem>> async);

    void getWarehouse(Integer warehouseID, AsyncCallback<WarehouseItem> async);

    Request getBankAccounts(ListingFilterParameter filterParametrs, AsyncCallback<ListResult<BankAccount>> async);

    void getBankAccountsForLookUp(ListingFilterParameter filterParametrs, AsyncCallback<BankAccountItem[]> callback);

    void getBankAccountForEdit(Integer objectId, AsyncCallback<BankAccount> async);

    void getBankAccountItems(AsyncCallback<SelectItem[]> async);

    void getBankAccountItemsForReference(AsyncCallback<SelectItem[]> async);

    void createAccountsBudget(AccountBudget budget, AsyncCallback<BudgetRelatedIds> async);

    void getBudgetedDataItem(Integer budgetManagerId, DateNonConvertable from, DateNonConvertable to, boolean isAsc, AsyncCallback<BudgetManagerItems> callback);

    void getAccountListByAccountType(ListingFilterParameter fp, AsyncCallback<AccountList> async);

    void getAccountById(Integer accountId, AsyncCallback<AccountSingleItem> async);

    void getAccountData(Integer objectID, AsyncCallback<AddAccountData> callback);

    void getAccountForEdit(Integer accountId, AsyncCallback<AddAccountItem> async);

    void updateAccount(AddAccountItem editAccount, AsyncCallback<Integer> async);

    void saveAccountBalances(ConversionBalanceItem conversionBalanceItem, AsyncCallback<Void> async);

    void getAccountBalances(AsyncCallback<TransactionItem[]> async);

    void createConBalFromInvoiceTransactionAndTrialBalance(AsyncCallback<Void> async);

    void getTaxPeriods(AsyncCallback<SelectItem[]> async);

    void getTaxBasises(AsyncCallback<SelectItem[]> async);

    void saveFinancialSettings(FinancialSettingsItem fs, AsyncCallback<Void> async);

    void getCompanyFinancialSettings(AsyncCallback<FinancialSettingsItem> async);

    void hasTransaction(AsyncCallback<Boolean> async);

    void removeGLAccount(Integer accountID, AsyncCallback<DeleteRPC> async);

    void getCompanyBaseCurrency(AsyncCallback<CurrencyItem> async);

    void deleteBankAccount(Integer bankAccountId, AsyncCallback<String> async);

    void transferMoney(TransferMoneyData data, AsyncCallback<Integer> callback);

    void getWarehousesAsSelectItem(AsyncCallback<SelectItem[]> callback);

    void getProductCategoriesAsSelectItem(AsyncCallback<SelectItem[]> callback);

    void getBrandsAsSelectItem(AsyncCallback<SelectItem[]> callback);

    void getVendorsAsSelectItem(AsyncCallback<SelectItem[]> callback);

    void getUnitMeasurementsAsSelectItem(AsyncCallback<SelectItem[]> callback);

    void saveWarehouse(WarehouseItem warehouse, AsyncCallback<Integer> callback);

    void saveWarehouseLocation(WarehouseLocationItem warehouseLocation, AsyncCallback<Integer> callback);

    void saveUnitMeasurement(UnitMeasurementItem warehouseLocation, AsyncCallback<SelectItem> callback);

    void updateUnitMeasurement(UnitMeasurementItem measurementItem, AsyncCallback<Void> async);

    Request getUnitMeasurementsList(ListingFilterParameter filterParametrs, AsyncCallback<ListResult<UnitMeasurementItem>> async);

    void getUnitMeasurement(Integer measurementID, AsyncCallback<UnitMeasurementItem> async);

    void getProductCategory(Integer categoryID, AsyncCallback<ProductCategoryItem> async);

    void saveProductCategory(ProductCategoryItem productCategory, AsyncCallback<Integer> callback);

    void saveProductCategories(ProductCategoryList productCategoriesList, AsyncCallback<Integer[]> callback);

    void deleteProductCategory(Integer productCategoryId, AsyncCallback<Boolean> async);

    void deleteProductCategoryImage(Integer productCategoryId, Integer imageID, AsyncCallback<Boolean> async);

    void deleteBrand(Integer brandID, AsyncCallback<Boolean> async);

    void deleteShippingMedthod(Integer shippingMedthodId, AsyncCallback<Boolean> async);

    Request getProductCategoriesList(ListingFilterParameter filterParametrs, AsyncCallback<ListResult<ProductCategoryItem>> async);

    void getBankAccountFilesList(Integer bankAccountID, AsyncCallback<BankAccountAttachment[]> async);

    void getTransactionMapping(Integer bankAccountAttachmentID, AsyncCallback<BankAccountImportStatementData> callback);
    void getTransactionMappingByAI(Integer bankAccountAttachmentID, AsyncCallback<BankAccountImportStatementData> callback);

    void saveStatements(BankAccountImportStatementData importStatementData, AsyncCallback<Boolean> async);
    void saveStatementsByAIMapping(BankAccountImportStatementData importStatementData, AsyncCallback<Boolean> async);

    void getStatementItems(Integer bankAccountAttachementID, AsyncCallback<BankAccountStatementTO[]> async);

    void findOthers(FindMatchFilterData filterData, AsyncCallback<ArrayList<Transaction>> callback);

    void findAndMatchTransactions(FindMatchFilterData filterData, AsyncCallback<ArrayList<Transaction>> callback);

    void reconcileStatement(Integer statementItemID, ArrayList<Integer> transactionIDs, Integer accountId, AsyncCallback<Boolean> async);

    void changeReconcileStatus(Integer transactionID, String status, Integer accountId, AsyncCallback<Void> callback);

    Request getBankAccountStatements(Integer bankAccountID, ListingFilterParameter fp, AsyncCallback<ListResult<BankStatementListItem>> async);

    Request getBankAccountStatementItems(Integer bankStatementID, ListingFilterParameter fp, AsyncCallback<ListResult<BankStatementItemListItem>> async);

    void getBankAccountStatementItem(Integer objectID, Integer bankStatementID, AsyncCallback<BankStatementItemListItem> async);

    void saveBankAccountStatementItem(BankStatementItemListItem item, AsyncCallback<Void> async);

    void deleteBankAccountStatementItem(Integer objectID, Integer bankStatementID, AsyncCallback<Boolean> async);

    void spendOrReceiveMoney(NewManualTransaction transaction, AsyncCallback<Integer> async);

    void reGenerateMoneyNumber(Integer transferType, AsyncCallback<BankTransferNumberData> async);

    void getBankTransferData(ListingFilterParameter filterParameter, AsyncCallback<NewManualTransaction> callback);

    void deleteBankTransfer(Integer objectID, String transactionType, AsyncCallback<Void> callback);

    Request getBrandsList(ListingFilterParameter filterParametrs, AsyncCallback<ListResult<BrandItem>> async);

    void checkIfBrandExists(BrandItem brand, AsyncCallback<TestRPC> callback);

    void saveBrand(BrandItem brand, AsyncCallback<Integer> callback);

    void getBrand(Integer brandID, AsyncCallback<BrandItem> async);

    void updateBrand(BrandItem brand, AsyncCallback<Void> async);

    void getProductCategoryPictures(Integer productID, Integer fileSizeType, AsyncCallback<ProductPicture[]> async);

    void deleteProductCategoryPicture(Integer categoryPictureId, AsyncCallback<Boolean> async);

    void setDefaultProductCategoryPicture(Integer pictureId, Integer categoryID, AsyncCallback<Boolean> async);

    void getVatReturnDateInterval(AsyncCallback<DateNonConvertable[]> callback);

    void getVatReturnReport(DateNonConvertable from, DateNonConvertable to, BigDecimal flatPercent, AsyncCallback<VatReturnTransferObject> callback);

    void getBankAccountReconcilationReport(String givenDateString, String givenFromDateString, Integer bankAccountID, AsyncCallback<BankReconcilationReportData> callback);

    void saveVatReturnReport(DateNonConvertable from, DateNonConvertable to, VatReturnTransferObject vatReturn, AsyncCallback<String> callback);

    void submitVatReturnReportToHMRC(Integer vatReturnID, AsyncCallback<String> callback);

    Request getVatReturnReportList(ListingFilterParameter filterParametrs, AsyncCallback<ListResult<VatReturnTransferObject>> callback);

    void deleteVatReturnReport(Integer id, AsyncCallback<Void> callback);

    void getCSVColumns(Integer objectId, AsyncCallback<HashMap<String, SelectItem[]>> async);

    void onChartOfAccountsImport(ImportFile importFile, boolean deleteNotUsedAccounts, AsyncCallback<ImportStatus> callback);

    void onBudgetManagerImport(ImportFile importFile, AsyncCallback<ImportStatus> callback);

    void importProductsFromParentCompany(ImportFile importFile, AsyncCallback<Void> async);

    void getProductCategoriesAsSelectItem(ListingFilterParameter filterParametrs, AsyncCallback<SelectItem[]> async);

    void getCategoriesAsSelectItem(AsyncCallback<SelectItem[]> async);

    void getCategoriesAsSelectItemForSettings(AsyncCallback<SelectItem[]> async);

    void getCategoriesAsTreeSelectItem(AsyncCallback<TreeSelectItem[]> async);

    void getCategoriesAsTreeSelectItemForSettings(AsyncCallback<TreeSelectItem[]> async);

    void getAccountsForInvoice(ListingFilterParameter fp, ArrayList<String> types, AsyncCallback<com.edatasite.workforce.gwt.core.client.rpc.accounting.AccountItem[]> async);

    void createAccounts(ArrayList<AddAccountItem> accountItems, AsyncCallback<Void> async);

    Request getReservationList(ListingFilterParameter filterParametrs, AsyncCallback<ListResult<ReservationItem>> asyncCallback);

    void makeReservation(ReservationItem item, AsyncCallback<Integer> asyncCallback);

    void getRentalItems(ListingFilterParameter filterParametrs, AsyncCallback<NewProduct[]> asyncCallback);

    void getReservation(Integer objectID, AsyncCallback<ReservationItem> asyncCallback);

    void getProductImportFillingData(AsyncCallback<ProductImportFillingData> callback);

    void getCompanyTaxes(AsyncCallback<TaxItem[]> callback);

    void makeReservation(ReservationItem reservationItem, Integer companyId, AsyncCallback<Integer> asyncCallback);

    void copyCustomFieldsToSubCategories(Integer objectID, AsyncCallback<Boolean> asyncCallback);

    void voidExpenseTransaction(Integer expenseReportId, DateNonConvertable voidDate, AsyncCallback<Integer> async);

    void getAccountTypeItems(AsyncCallback<AccountTypeItem[]> async);

    void saveAccountTypeNumbering(AccountTypeItem[] items, AsyncCallback<Void> async);

    void getGeneratedAccountNumber(Integer accountTypeID, AsyncCallback<String> async);

    void getClientList(ListingFilterParameter filterParametrs, AsyncCallback<SelectItem[]> async);

    void completeAccountingGettingStarted(SettingsData settingsData, boolean enableMultiCurrency, AsyncCallback<Void> callback);

    void createOrUpdateAccountFromSaasu(AddAccountItem accountItem, AsyncCallback<Void> async);

    void saveBankCheckData(BankCheckData bankCheckData, AsyncCallback<Void> callback);

    void getBankCheckData(Integer objectID, AsyncCallback<BankCheckData> callback);

    void getBankCheckList(ListingFilterParameter filterParametrs, AsyncCallback<ListResult<BankCheckData>> callback);

    void deleteBankCheckData(Integer objectID, AsyncCallback<ArrayList<String>> async);

    void getBankAccountSummaryData(Integer objectID, Integer fromAccountID, AsyncCallback<TransferMoneyData> asyncCallback);

    void getLookUpItems(ListingFilterParameter filterParametrs, int type, AsyncCallback<SelectItem[]> async);

    void getWarehousesForLookUp(ListingFilterParameter filterParameter, AsyncCallback<SelectItem[]> callback);

    void getSelectedWarehouseForTransactions(AsyncCallback<SelectItem> asyncCallback);

    void getProductLocations(ListingFilterParameter filterParameter, AsyncCallback<SelectItem[]> callback);

    void updateAccountsAfterExportSaasu(Integer objectId, Date lastUpdateDate, String saasuLastUpdatedUid, Integer saasuGUID, AsyncCallback<Void> async);

    void getDetailedProductLocation(ListingFilterParameter  filterParameter, AsyncCallback<WarehouseLocationItem> callback);

    void getAccountList(ListingFilterParameter filterParametrs, AsyncCallback<ListResult<AccountListItem>> async);

    void getMemorizedTransactionsForLookUp(ListingFilterParameter filterParametrs, AsyncCallback<SelectItem[]> callback);

    void getCompanyIncome(ListingFilterParameter filterParameter, AsyncCallback<BigDecimal> callback);

//    void updateAccountByQB(AddAccountItem accountItem, Integer synchItemId, AsyncCallback<Void> async);
//
//    void updateCheckByQB(BankCheckData bankCheckData, Integer synchItemId, AsyncCallback<Void> async);

    void getCurrencyAdjustmentData(AsyncCallback<CurrencyAdjustmentFillingData> callback);

    void calculateCurrencyAdjustment(CurrencyAdjustmentData adjustmentData, AsyncCallback<CurrencyAdjustmentData> callback);

    void saveCurrencyAdjustment(CurrencyAdjustmentData currencyAdjustmentData, AsyncCallback<Integer> callback);

    void getMultiCurrencyExchangeRate(Integer year, Integer month, AsyncCallback<MultiCurrencyExchangeRateItem> asyncCallback);

    void saveCurrenciesExchangeRate(ExchangeRateItem exchangeRateItem, AsyncCallback<Boolean> asyncCallback);

    void getTransaction(Integer transactionID, AsyncCallback<Transaction> asyncCallback);

    void deleteSupplierPayment(Integer transactionID, AsyncCallback<Boolean> asyncCallback);

    void getSalesMansAsSelectItem(ListingFilterParameter listingFilterParameter, AsyncCallback<SelectItem[]> async);

    void getTrashBinList(ListingFilterParameter filterParametrs, AsyncCallback<ListResult<TrashBinListItem>> callback);

    void changeTrashBinStatus(Integer objectID, String status, AsyncCallback<Void> callback);

    void getProductInitData(AsyncCallback<ImportProductInitItem> async);

    void updateIncomeTaxData(DateNonConvertable fromDate, DateNonConvertable toDate, AsyncCallback<Integer> callback);

    void getVatReturnReportVisibility(AsyncCallback<Boolean> async);

    void getDepartmentsForAccounting(ListingFilterParameter filterParametrs, AsyncCallback<SelectItem[]> callback);

    void deleteBankAccountFile(Integer fileID, AsyncCallback<Void> callback);

    void saveCsvTemplate(String text, String templateType, AsyncCallback<Integer> callback);

    void getCsvTemplates(String templateType, AsyncCallback<SelectItem[]> callback);

    void getCsvTemplateData(Integer selectedId, AsyncCallback<ArrayList<CsvTemplateItem>> callback);

    void saveCsvTemplateData(Integer csvTemplateID, ArrayList<CsvTemplateItem> csvTemplateData, AsyncCallback<Boolean> callback);

    void getJournalReportWithPaging(DateNonConvertable fromDate, DateNonConvertable toDate, String orderBy, Integer journalId, ListingFilterParameter listingFilterParameter, AsyncCallback<ListingResult<Transaction>> async);

    void getCompanyDetails(Integer userId, AsyncCallback<String[]> callback);

    void getAccountsForPaymentPost(ListingFilterParameter filterParameter, AsyncCallback<SelectItem[]> async);

    void getBankAccountLastExchangeRate(Integer accountID, AsyncCallback<BigDecimal> async);

    Request getBankCashTransferList(ListingFilterParameter fp, AsyncCallback<ListResult<NewManualTransaction>> async);

    void getAccountsForLookUp(ListingFilterParameter filterParameter, AsyncCallback<SelectItem[]> asyncCallback);

    void getBrandList(ListingFilterParameter lfp, AsyncCallback<SelectItem[]> async);

    void getRFQList(ListingFilterParameter lfp, AsyncCallback<SelectItem[]> async);

    void getRFPList(ListingFilterParameter lfp, AsyncCallback<SelectItem[]> async);

    void createBankTransferNote(Integer transferID, HistoryListItem hisItem, AsyncCallback<Integer> callback);

    void deleteBankTransferNote(Integer bankTransferID, AsyncCallback<Boolean> callback);

    void getBankAccountStatementItemList(ListingFilterParameter filterParametrs, AsyncCallback<ListResult<BankStatementItemListItem>> callback);

    void getAllPaymentMethods(ListingFilterParameter fp, AsyncCallback<ListResult<PaymentMethodItem>> callback);

    void savePaymentMethod(PaymentMethodItem pmi, AsyncCallback<Integer> callback);

    void deletePaymentMethod(Integer objectID, AsyncCallback<Boolean> callback);

    void getPaymentMethodById(Integer objectID, AsyncCallback<PaymentMethodItem> callback);

    void getAccountsReceivablePayable(ListingFilterParameter filterParametrs, AsyncCallback<ArrayList<AccountItem>> callback);

    void getBalancesheetSettings(AsyncCallback<BalancesheetSettings> callback);

    void saveBalancesheetSettings(BalancesheetSettings settings, AsyncCallback<Void> callback);

    void getTransactionJournals(ListingFilterParameter filterParameter, AsyncCallback<ArrayList<SelectItem>> callback);

    void getAccountsForExpenseLookUp(ListingFilterParameter filterParametrs, AsyncCallback<ListResult<SelectItem>> async);

    void saveBankAccountCellValue(BankAccount rowValue, String columnCodeName, AsyncCallback<Boolean> asyncCallback);

    void saveBankTransferCellValue(NewManualTransaction rowValue, String columnCodeName, AsyncCallback<Boolean> asyncCallback);

    void getBankTransferHistoryNotes(Integer id, String viewType, AsyncCallback<List<HistoryNote>> callback);

    void deleteSelectedAccounts(ArrayList<Integer> ids, AsyncCallback<Boolean> async);

    void getItemQtyByWarehouse(Integer productId, Integer warehouseId, AsyncCallback<BigDecimal> callback);

    void saveProductCategoryCellValue(ProductCategoryItem rowValue, String columnCodeName, AsyncCallback<Void> async);

    void generateProductCategoryNumber(AsyncCallback<NumberData> async);

    void generateProductCategoryNumber(Integer intNumber, AsyncCallback<NumberData> async);

    void saveBudgetManagerAssignItems(Integer budgetManagerId, HashMap<Integer, List<Integer>> items, AsyncCallback<Void> async);

    void saveBudgetManager(BudgetManagerItem budgetsheetItem, AsyncCallback<Integer> async);

    void createBudgetManagerItem(AccountBudget budget, AsyncCallback<Void> async);

    void getBudgetManagerData(Integer objectID, AsyncCallback<BudgetManagerItem> async);

    void deleteBudgetManager(Integer objectID, AsyncCallback<Void> async);

    void saveBudgetManagerColumn(Integer budgetID, BudgetColumn budgetColumn, boolean isEdit, AsyncCallback<Integer> async);

    void getBudgetManagerColumnData(Integer budgetID, String columnCode, AsyncCallback<BudgetColumn> async);

    void deleteBudgetManagerColumn(Integer budgetID, String columnCode, AsyncCallback<Void> async);

    void getCustomfieldsByType(String description, AsyncCallback<List<SelectItem>> async);

    void getBudgetedAccounts(DateNonConvertable from, DateNonConvertable to, Integer departmentID, boolean isAsc, AsyncCallback<ExpensesAndRevenue> async);

    void getConversionBalanceItem(AsyncCallback<ConversionBalanceItem> async);

    void activeProductCategory(Integer id, boolean b, AsyncCallback<Boolean> async);

    void getProductCatecorFacetFilterData(FacetFilterRpc facetFilterRpc, AsyncCallback<FacetFilterRpc> async);

    void saveHMRCAuthSettings(HMRCAuthSettingsItem hmrcAuthSettingsItem, AsyncCallback<Void> callback);

    void initZatcaSettings(AsyncCallback<Void> callback);
    void getCrmAccountBalancePDFTemplates(AsyncCallback<CustomFormItemPdfTemplateList> async);

    void saveWEditCellValue(WarehouseItem rowValue, String columnCodeName, AsyncCallback<Boolean> async);

    void shortenLink(String link, CrmAccountItem id, AsyncCallback<String> callback);

    void getProductLocationReference(String referenceCode,AsyncCallback<List<SelectItem>> callback);
}
