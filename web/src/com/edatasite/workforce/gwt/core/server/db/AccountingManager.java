package com.edatasite.workforce.gwt.core.server.db;

import com.edatasite.workforce.core.domain.EdsCurrency;
import com.edatasite.workforce.core.domain.accounting.EdsAccount;
import com.edatasite.workforce.core.domain.accounting.EdsAccountType;
import com.edatasite.workforce.core.domain.accounting.EdsBankAccount;
import com.edatasite.workforce.core.domain.accounting.EdsBrand;
import com.edatasite.workforce.core.domain.accounting.EdsRFP;
import com.edatasite.workforce.core.domain.accounting.EdsRFQ;
import com.edatasite.workforce.core.domain.accounting.EdsTransaction;
import com.edatasite.workforce.core.domain.accounting.EdsTransactionItem;
import com.edatasite.workforce.core.domain.accounting.TotalDebitCredit;
import com.edatasite.workforce.core.domain.crm.EdsCrmAccount;
import com.edatasite.workforce.gwt.accounting.client.rpc.AccountListItem;
import com.edatasite.workforce.gwt.accounting.client.rpc.CashFlowItem;
import com.edatasite.workforce.gwt.accounting.client.rpc.ChartOfAccountItem;
import com.edatasite.workforce.gwt.accounting.client.rpc.TransactionItem;
import com.edatasite.workforce.gwt.accounting.client.rpc.TrialBalanceItem;
import com.edatasite.workforce.gwt.core.client.rpc.FromToDate;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.rpc.solr.SolrReindexRpc;
import com.edatasite.workforce.gwt.invoice.client.rpc.AgingSummaryInvoiceItem;
import com.edatasite.workforce.gwt.invoice.client.rpc.ProjectBaseData;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: Admin
 * Date: 20.02.2009
 * Time: 17:11:25
 * To change this template use File | Settings | File Templates.
 */
public interface AccountingManager extends Manager<EdsAccount> {

    EdsAccount getAccountByKey(int code);

    HashMap<Integer, EdsAccount> getAccountsMapByKey(int code);

    EdsAccount getAccountByKey(int code, Integer currencyID);

    EdsAccount getAccountByCode(String code);

    List<EdsAccount> getAccountByCodes(List<String> codes);

    HashMap<String, EdsAccount> getAccountsMap();

    HashMap<String, EdsAccount> getAccountPayableOrReceivableMap(boolean isPayableAccount);

    List<EdsAccount> getCompanyAccounts();

    List<EdsAccount> getCompanyAccountsOrderByType();

    List<EdsAccountType> getAccountTypes();

    List<EdsAccount> getAccountsForPayment(ListingFilterParameter fp);

    List<EdsAccount> getAccountsForInvoice();

    List<EdsAccount> getAccountsForInvoice(ListingFilterParameter filterParametrs, boolean searchInGivenTypes);

    List<EdsAccount> getAccountsForPayableType(ListingFilterParameter filterParametrs);

    List<EdsAccount> getAllGLAccounts();

    List<EdsAccount> getAccountsForExpense(ListingFilterParameter filterParametrs);

    Integer getAccountsForExpenseTotalCount(ListingFilterParameter filterParameter);

    List<EdsAccount> getAccountsAttendedInTransactions(Date to, String departmentAndTreeChildIDs, Integer projectID, Integer showAccounts, boolean foreignOnly);

    List<EdsAccount> getRevenueExpensesAttendedInTransactions(FromToDate main, FromToDate[] compareTo, String departmentAndTreeChildIDs, Integer projectId, String sortField, String sortDirection);

    List<EdsAccount> getRevenueExpensesAttendedInTransactions();

    EdsAccountType getAccountType(Integer objectID);

    EdsAccountType getAccountTypeByCode(String code);

    EdsAccount getAccountCount(String code, Integer accountID);

    List<EdsBankAccount> getBankAccountList(ListingFilterParameter fp);

    List<EdsBankAccount> getBankAccounts();

    List<EdsBankAccount> getBankAccountsForReference();

    List<EdsAccount> getAccounts(ListingFilterParameter filterParametrs);

    List<ProjectBaseData> getProjectBaseData(List<Integer> projectIds, Date startDate, Date endDate);

    List<ProjectBaseData> getProjectBaseData(Integer employeeId, List<Integer> projectIds, Date startDate, Date endDate);

    List<ProjectBaseData> getProjectBaseDataFE(Integer employeeId, List<Integer> projectIds, Date startDate, Date endDate);

    List<ProjectBaseData> getProjectBaseMonthlyData(List<Integer> projects, Date from, Date to);

    void deleteGLAccountsInattendedInInvoices();

    EdsAccount getAccountTypeWithMinCode(String accountType);

    List<Integer> getGLAccountsInattendedInInvoices();

    EdsAccount getAccountForDelete(Integer objectId);

    EdsTransaction getConversionBalanceTransaction();

    List<EdsAccount> getAccountsByType(String typeCode);

    List<EdsAccount> getAccountsByCategory(String category, String code);

    EdsAccount getOneAccountByType(String typeCode);

    boolean isDuplicateReference(String reference, Integer transactionID);

    Integer getProductLastIntNumber();

    Integer getExpenseLastIntNumber();

    Integer getSpendOrReceivMoneyLastIntNumber(Integer transferType);

    boolean isProductNumberExists(String number, Integer productID);

    boolean isProductNameExists(String Name, Integer productID);

    boolean isSpendOrReceiveMoneyNumberExists(String number, Integer moneyID, Integer transferType);

    Integer getBankAccountListCount(ListingFilterParameter filterParametrs);

    void recalculateAccountBalances();

    BigDecimal getAccountBalance(Integer accountID);

    TransactionItem getAccountTransactionItems(Integer accountID);

    BigDecimal getAccountForeignBalance(Integer accountID);

    Map<Integer, AccountListItem> getAccountBalanceMap(List<Integer> accountIds);

    EdsAccount getAccountBySaasuUID(String saasuUID);

    void updateAccountCurrency(EdsCurrency currency);

    List<EdsAccount> getAccountListByCurrency(EdsCurrency currency);

    List<EdsAccount> getAllAccounts(ListingFilterParameter fp);

    ChartOfAccountItem getAccountsForSyncSaasu(Integer startIndex, Integer limit);

    String generateNewAccountNumberByAccountType(Integer startNumberingRange, Integer endNumberingRange);

    EdsAccount getMultiCurrencyAccount(Integer key, EdsCurrency currency);

    EdsAccount getGlAccount(Integer key, EdsCrmAccount crmAccount);

    EdsAccount getAccountByQbAccountID(String qbGUID);

    List<EdsAccount> getAccountsByIds(String Ids);

    EdsAccount getAccountByName(String name);

    List<EdsCurrency> getAdjustmentEnabledCurrencies();

    List<EdsTransactionItem> getCrmAccountAdjustmentItems(Integer currencyID, EdsAccount account);

    List<EdsTransactionItem> getBankAccountAdjustmentItems(Integer currencyID);

    Integer getStockAccountID();

    Integer getAccountIDByNameAndAccountType(String name, Integer accountTypeID);

    List<SelectItem> getCompanySalesMans(ListingFilterParameter filterParameter);

    Integer getCurrencyFromFinancialSettings(Integer objectID);

    HashMap<String, Integer> getAccountsMapForCustomInvoiceImport();

    TotalDebitCredit getDebitCreditTotal(EdsAccount account, String departmentAndTreeChildIDs, Integer projectID, Date fromDate, Date toDate);

    HashMap<Integer, TotalDebitCredit> getDebitCreditTotalForPNL(String departmentAndTreeChildIDs, Integer projectID, Date fromDate, Date toDate);

    HashMap<Integer, TotalDebitCredit> getForeignAccountsDebitCreditTotal(String departmentAndTreeChildIDs, Integer projectID, Date from, Date to, Integer accountID);

    HashMap<Integer, TotalDebitCredit> getAllAccountsDebitCredit(String departmentAndTreeChildIDs, Date from, Date to);

    HashMap<Integer, TotalDebitCredit> getSubsidiariesDebitCreditTotal(Date from, Date to, Integer currencyID);

    LinkedHashMap<String, TrialBalanceItem> getPRAccountClientSupplierBalance(Date from, Date to);

    HashMap<Integer, TotalDebitCredit> getSubsidiariesAllAccountsDebitCredit(Date from, Date to, Integer currencyID);

    LinkedHashMap<Integer, ArrayList<AgingSummaryInvoiceItem>> getClientSupplierBalanceForAging(ListingFilterParameter filter);

    LinkedHashMap<Integer, ArrayList<AgingSummaryInvoiceItem>> getClientSupplierBalanceForAgingDetails(ListingFilterParameter filter);

    boolean isProductUpcNumberExists(String upcNumber, Integer productID);

    List<EdsAccount> getGroupAccounts(ListingFilterParameter filterParametrs, ArrayList<String> accountTypes);

    void setParentCodesToChilds(String updateQuery);

    LinkedList<CashFlowItem> getCashFlowItems(String groupCode, ListingFilterParameter fp, String departmentAndTreeChildIDs, boolean isDebitAccount);

    BigDecimal getCashFlowItemBalance(String groupCode, ListingFilterParameter fp, boolean isDebitAccount);

    List<EdsBrand> getBrandList(ListingFilterParameter fp);

    List<EdsRFQ> getRFQList(ListingFilterParameter fp);

    List<EdsRFP> getRFPList(ListingFilterParameter fp);

    Map<String, Integer> getAccountAsMapByCode(ListingFilterParameter fp);

    HashMap<String, EdsAccount> getAccountAsMap(ListingFilterParameter fp);

    List<EdsAccount> getAccountsReceivablePayable(ListingFilterParameter filterParametrs);

    EdsAccount getDefaultAccount(Integer accountKey);

    void clearDefaultAccount(Integer accountKey);

    Calendar getFinancialYearStartIfEnabled(Date creationDate);

    List<Integer> getDeletedAccountListForSolr(SolrReindexRpc solrReindex);

    List<Integer> getAccountIdsWithLimit(Integer startat, Integer limit);

    List<Integer> getAccountIdsByIds(String IDs);

    List<EdsAccount> getAccountListForSolr(SolrReindexRpc solrReindex, int startat, int limit);

    List<EdsAccount> getAccountsByTypeList(ListingFilterParameter fp);

    EdsAccount getVatAccount(String type);

    Integer getVatAccountKey(String type);

    String getOwnerName(Integer clientOrSupplierId);
}
