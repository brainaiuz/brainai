package com.edatasite.workforce.gwt.accounting.server.app;

import com.edatasite.workforce.core.domain.EdsExpensePayment;
import com.edatasite.workforce.core.domain.EdsExpenseReport;
import com.edatasite.workforce.core.domain.EdsItem;
import com.edatasite.workforce.core.domain.EdsStockAdjustment;
import com.edatasite.workforce.core.domain.EdsStockTransfer;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.EdsVat;
import com.edatasite.workforce.core.domain.accounting.EdsBankAccount;
import com.edatasite.workforce.core.domain.accounting.EdsBankCheck;
import com.edatasite.workforce.core.domain.accounting.EdsBankTransfer;
import com.edatasite.workforce.core.domain.accounting.EdsBankTransferTransaction;
import com.edatasite.workforce.core.domain.accounting.EdsDeferredTransactionItem;
import com.edatasite.workforce.core.domain.accounting.EdsInvoice;
import com.edatasite.workforce.core.domain.accounting.EdsInvoicePayment;
import com.edatasite.workforce.core.domain.accounting.EdsInvoiceTransaction;
import com.edatasite.workforce.core.domain.accounting.EdsOverPayment;
import com.edatasite.workforce.core.domain.accounting.EdsPaymentRefund;
import com.edatasite.workforce.core.domain.accounting.EdsPurchaseOrder;
import com.edatasite.workforce.core.domain.accounting.EdsSaleInvoice;
import com.edatasite.workforce.core.domain.accounting.EdsSaleQuote;
import com.edatasite.workforce.core.domain.accounting.EdsShippingData;
import com.edatasite.workforce.core.domain.accounting.EdsVatAdjustment;
import com.edatasite.workforce.core.domain.crm.EdsCrmAccount;
import com.edatasite.workforce.core.domain.customfields.EdsBankTransferCustomFields;
import com.edatasite.workforce.gwt.accounting.client.rpc.AccountsByCategory;
import com.edatasite.workforce.gwt.accounting.client.rpc.AddAccountItem;
import com.edatasite.workforce.gwt.accounting.client.rpc.BudgetManagerItems;
import com.edatasite.workforce.gwt.accounting.client.rpc.CashFlow;
import com.edatasite.workforce.gwt.accounting.client.rpc.NewManualTransaction;
import com.edatasite.workforce.gwt.accounting.client.rpc.NewProduct;
import com.edatasite.workforce.gwt.accounting.client.rpc.PnLFilter;
import com.edatasite.workforce.gwt.accounting.client.rpc.ProductCategoryItem;
import com.edatasite.workforce.gwt.accounting.client.rpc.ProductImportFillingData;
import com.edatasite.workforce.gwt.accounting.client.rpc.TaxData;
import com.edatasite.workforce.gwt.accounting.client.rpc.TaxList;
import com.edatasite.workforce.gwt.accounting.client.rpc.Transaction;
import com.edatasite.workforce.gwt.accounting.client.rpc.TrialBalance;
import com.edatasite.workforce.gwt.accounting.client.rpc.TrialBalanceItem;
import com.edatasite.workforce.gwt.accounting.client.rpc.WarehouseItem;
import com.edatasite.workforce.gwt.accounting.client.rpc.consignment.TrialBalanceFilter;
import com.edatasite.workforce.gwt.core.client.rpc.Address;
import com.edatasite.workforce.gwt.core.client.rpc.CompanyCustomFieldItem;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.FileItem;
import com.edatasite.workforce.gwt.core.client.rpc.KeyValueStruct;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.TaxItem;
import com.edatasite.workforce.gwt.core.client.rpc.accounting.AccountItem;
import com.edatasite.workforce.gwt.core.client.rpc.currency.CurrencyItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.view.BankTransferNumberData;
import com.edatasite.workforce.gwt.core.server.app.RejectedImportRecord;
import com.edatasite.workforce.gwt.core.server.controllers.dto.AccountingSetupItem;
import com.edatasite.workforce.gwt.core.server.rabbitmq.data.ExchangeRateItemMQ;
import com.edatasite.workforce.gwt.documents.client.rest.resource.FileResource;
import com.edatasite.workforce.gwt.importfile.client.rpc.ImportFile;
import com.edatasite.workforce.gwt.invoice.client.rpc.PaymentData;
import com.edatasite.workforce.gwt.submodule.paymentdeduction.client.SettingsData;
import com.edatasite.workforce.rest.v3.release10.accounting.dto.BankAccountDto;
import com.edatasite.workforce.rest.v3.release10.accounting.dto.ProductLocationDto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: Anvarbek
 * Date: 21.02.2009
 * Time: 17:40:47
 * To change this template use File | Settings | File Templates.
 */
public interface AccountingServiceLocal {

    Integer createTransactionsForInvoice(EdsInvoice invoice, EdsUser user);

    void createTransactionForPayment(EdsInvoicePayment invoicePayment);

    void createTransactionForRefund(EdsInvoicePayment creditNoteRefund);

    void createTransactionForPaymentRefundCloseAmount(EdsPaymentRefund paymentRefund, BigDecimal closedAmount, SelectItem account, PaymentData[] payments);

    void createTransactionForCreditedInvoice(EdsInvoicePayment creditNoteRefund);

    Integer createTransactionsForCreditNote(EdsInvoice creditNote, Integer creditNoteInvoiceID);

    void createReversalTransactionForPayment(EdsInvoicePayment invoicePayment, DateNonConvertable voidDate);

    void createReversalTransactionForOverPayment(EdsOverPayment edsOverPayment, DateNonConvertable voidDate);

    void deleteInvoicePaymentTransaction(EdsInvoicePayment invoicePayment);

    void createOrUpdateTransactionForExpense(EdsExpenseReport expenseReport);

    void createTransactionForExpencePayment(EdsExpensePayment payment);

    Integer createTransactionsForInventory(EdsItem inventory, EdsUser user);

    Integer createTransactionsForGoodsReceived(EdsPurchaseOrder goodsReceived, EdsShippingData shippingData, HashMap<Integer, BigDecimal> oldAllocatedAmounts);

    void deleteGoodsReceivedTransaction(EdsShippingData goodsReceived);

    Integer createTransactionForGoodsDelivered(EdsSaleQuote goodsDelivered, EdsShippingData shippingData);

    void deleteGoodsDeliveryTransactions(EdsShippingData goodsDelivered);

    Integer createTransactionsForStockAdjustment(EdsStockAdjustment stockAdjustment);

    void createTransactionForOverPayment(EdsOverPayment overPayment);

    void deleteTransactionForOverPayment(Integer batchPaymentID);

    void createOrUpdateBankCheckTransaction(EdsBankCheck bankCheck);

    void createTransactionForDeferredObject(EdsDeferredTransactionItem item);

    AccountsByCategory getAccountsForInvoice();

    AccountItem[] getAccountsForExpense(ListingFilterParameter filterParametrs);

    EdsInvoiceTransaction getInvoiceTransaction(EdsInvoice invoice);

    TaxList createCompanyTaxList(List<EdsVat> vatList);

    List<EdsVat> companyVatList(ListingFilterParameter fp, Integer companyID);

    SelectItem[] getWarehousesAsSelectItem();

    SelectItem[] getUnitMeasurementsAsSelectItem();

    SelectItem[] getBrandsAsSelectItem();

    void createClientSupplierPaymentTransaction(Integer customerSupplierPaymentID);

    Integer companyVatListCount(ListingFilterParameter filterParametrs, Integer companyID);

    Integer voidInvoiceTransactions(Integer invoiceID, DateNonConvertable voidDate);

    void allChartOfAccountSolrReindexByCurrency(Integer currencyId);

    CurrencyItem getCompanyBaseCurrency();

    void createOrUpdateCustomerTransaction(Integer clientID, EdsUser user);

    void createOrUpdateSupplierTransaction(Integer supplierID, EdsUser user);

    SelectItem[] getCategoriesAsSelectItem();

    void createCompanyMultiCurrency(List<Integer> currencyIdList, Integer ownerCompanyId);

    void saveCurrenciesExchangeRate(ExchangeRateItemMQ data);

    void saveSubsidiariesCompany(List<SelectItem> data);

    List<SelectItem> getSubsidiariesCompanyList();

    void saveSubsidiariesProduct(List<SelectItem> selectItemList);

    void completeAccountingGettingStarted(SettingsData settingsData, boolean enableMultiCurrency);

    void initDefaultAndCompanyAccounts(Integer currencyID);

    void resaveCompanyAccountingSettings(Integer companyId, KeyValueStruct industry, String accounting_tool, Address billingAddress,
                                         AccountingSetupItem setupItem, boolean accountingSetup,
                                         String companyStripeAccount, String companyPaypalAccount);

    void completeAccountingGettingStarted(SettingsData settingsData);

    List<AddAccountItem> getInterCompanyTransactionAccounts(List<NewProduct> products);

    HashMap<Integer, Integer> convertInterCompanyAccounts(List<AddAccountItem> accounts);

    TaxItem saveTaxRate(TaxData data);

    Transaction getTransaction(Integer transactionID);

    void fixDoubleTransaction(List<Object> items, int type);
//    void applyCompanyTimeZoneCorrections(Integer companyID);

    TrialBalance getTrialBalance(TrialBalanceFilter tbf);

    CashFlow getCashFlow(ListingFilterParameter filter);

    Map<Integer, ArrayList<TrialBalanceItem>> getPRAccountClientSupplierBalance(Date from, Date to);

    FileItem[] getAttachments(int folderType, Integer objectID);

    FileResource[] getAttachmentResources(int folderType, Integer objectID);

    SelectItem[] getBankAccountItems();

    ArrayList<BankAccountDto> getBankAccountList();

    SelectItem[] getBankAccountItemsForReference();

    EdsBankAccount getBankAccount(Integer bankAccountId);

    void runSpendReceivePostDatedTransactions(Integer objectID);

    ArrayList<RejectedImportRecord[]> importProducts(ImportFile importFile, List<String[]> dataBank);

    Map<String, EdsCrmAccount> getVendorsMap();

    Map<String, EdsPurchaseOrder> getPurchaseOrders();

    ArrayList<RejectedImportRecord[]> importBankTransfer(ImportFile importFile, List<String[]> lists, Integer companyId, Integer reporterID);

    EdsBankTransferCustomFields createBankTransferCustomFields(List<CompanyCustomFieldItem> customFieldItems);

    NewManualTransaction getBankTransferData(ListingFilterParameter fp);

    EdsBankTransferTransaction saveBankTransferTransaction(NewManualTransaction manualTransaction, EdsBankTransfer bankTransfer);

    Date getConversionDate(int day);

    AccountItem[] getAccountsForInvoice(ListingFilterParameter fp, ArrayList<String> type);

    TaxItem[] getCompanyTaxesWithFilter(ListingFilterParameter filterParameter);

    ArrayList<AccountItem> getAccountsReceivablePayable(ListingFilterParameter filterParameter);

    SelectItem[] getUnitMeasurements(ListingFilterParameter filterParameter);

    SelectItem[] getWarehousesForLookUp(ListingFilterParameter filterParameter);

    SelectItem[] getAccountsForPaymentPost(ListingFilterParameter filterParameter);

    void sorlReindexImportedChartOfAccounts(List<String> updatedAccountCodes);

    ProductImportFillingData getProductImportFillingData();

    ProductCategoryItem getProductCategory(Integer categoryID);

    boolean deleteProductCategory(Integer productCategoryId);

    SelectItem[] getVendorsAsSelectItem();

    Integer spendOrReceiveMoney(NewManualTransaction manualTransaction);

    void deleteBankTransfer(Integer objectID, String transactionType);

    BankTransferNumberData reGenerateMoneyNumber(Integer transferType);

    ListResult<SelectItem> getAccountsForExpenseLookUp(ListingFilterParameter filterParameter);

    ListResult<ProductCategoryItem> getProductCategoriesList(ListingFilterParameter filterParametrs);

    ListResult<WarehouseItem> getWarehousesList(ListingFilterParameter filterParametrs);

    Integer saveWarehouse(WarehouseItem warehouse);

    WarehouseItem getWarehouse(Integer warehouseID);

    boolean deleteWarehouse(Integer warehouseId);

    void createTransactionForStockTransfer(EdsStockTransfer stockTransfer);

    LinkedHashMap<String, BigDecimal> getTopExpensesMap(ListingFilterParameter fp);

    BudgetManagerItems getProfitAndLoss(PnLFilter filter);

    SelectItem[] getMemorizedTransactionsForLookUp(ListingFilterParameter filterParametrs);

    void createTransactionForVatAdjustment(EdsVatAdjustment vatAdjustment);

    Integer saveProductCategory(ProductCategoryItem productCategoryItem);

    ListResult<NewManualTransaction> getBankCashTransferList(ListingFilterParameter fp);

    void eventCorrectionAssemblyBuildTransaction(Integer assemblyTransactionId);

    void createStripeInvoice(EdsSaleInvoice invoice);

    List<ProductLocationDto> getWarehousesByProductId(Integer productId);

}
