package com.edatasite.workforce.gwt.core.server.db;

import com.edatasite.workforce.core.domain.crm.EdsCrmAccount;
import com.edatasite.workforce.gwt.core.client.rpc.NumberData;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.rpc.solr.SolrReindexRpc;

import java.math.BigDecimal;
import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: Sherali
 * Date: 09-Jul-2009
 * Time: 17:22:16
 * To change this template use File | Settings | File Templates.
 */
public interface CrmAccountManager extends Manager<EdsCrmAccount> {
    List<EdsCrmAccount> getList(ListingFilterParameter fp, String accountType);

    List<EdsCrmAccount> list(ListingFilterParameter fp);

    List<Object[]> getAccountsByIndustry();

    List<Object[]> getListByAmount();

    EdsCrmAccount getCrmAccountByName(String s, Integer objectID);

    EdsCrmAccount getCrmAccountByName(String name);

    EdsCrmAccount getCrmAccountByNumber(String number);

    EdsCrmAccount getCrmAccountByObjectKey(String objectKey);

    ArrayList<EdsCrmAccount> getCrmAccountsByNames(String namesAsCommaSeparated);

    Integer isAccountNameOrNumberAlreadyExists(String name, String number, Integer objectID);

    Map<String, String[]> checkEmailExistence(String[] email);

    EdsCrmAccount getCrmAccountByEmail(String from, Integer companyID);

    EdsCrmAccount getByPhone(String phone);

    List<EdsCrmAccount> getAllByPhone(String phone);

    Integer getAccountsCountByImportFileID(Integer objectID);

    int getListCount(ListingFilterParameter fp);

    Map<String, EdsCrmAccount> getAllCrmAccountsMap();

    HashMap<String, EdsCrmAccount> getCrmAccountsMap(String crmAccountType, boolean mappedByNumber);

    HashMap<Integer, EdsCrmAccount> getCrmAccountsAsMap();

    List<Object[]> getList();

    List<Object[]> getListWithAccountNumber();

    List<Integer> getCompanyDeletedCrmAccountsForSolr(SolrReindexRpc solrReindex);

    List<EdsCrmAccount> getCompanyCrmAccountsForSolr(SolrReindexRpc solrReindex, int startat, int limit);

    List<Integer> getCrmAccountIDsByIDs(List<Integer> ids, String... types);

    List<Integer> getCompanyCrmAccountIds(Integer companyID, int startat, int limit);

    List<EdsCrmAccount> getCrmAccountsByIDs(List<Integer> ids);

    List<EdsCrmAccount> getCrmAccountsByImportFileID(Integer entityID, int start, int limit);

    Map<Integer, String> getMapIdAndName(List<Integer> crmAccountIDs, boolean getDeletedAlso);

    Map<String, Integer> getMapNumberAndName();

    List<Integer> getCrmAccountIDsByName(String accountName, boolean exactName, boolean withDeleteds);

    Integer getLastNumber(String format);

    void update(EdsCrmAccount account, boolean solrUpdateAlso);

    String generateAccountNumber(String accountType);

    String generateAccountNumber(String accountType, Integer intNumber);

    Set<String> getDuplicateNamesSet(List<Integer> idsFromSolrDocument, List<Integer> idsOfAccountDetectingForDuplicates);

    EdsCrmAccount getAccountByOwner(Integer ownerId);

    BigDecimal getClientBalance(Integer crmAccountID, boolean... isBaseCurrency);

    Map<Integer, BigDecimal> getClientBalanceByCustomerIds(String crmAccountIDs);

    BigDecimal getSupplierBalance(Integer crmAccountID, boolean... isBaseCurrency);

    Map<Integer, BigDecimal> getSupplierBalanceWithMap(String crmAccountIds, boolean... isBaseCurrency);

    EdsCrmAccount getAccountBySaasuUID(String saasuUID);

    List<EdsCrmAccount> getAccountTreeLookUpItems(ListingFilterParameter fp, String treeLevel);

    EdsCrmAccount findCustomerByRegistrationNum(String registrationNumber);

    List<String> getCrmAccountNumberById(Integer clientId);

    EdsCrmAccount getCrmAccountBySubsidiary(Integer externalCompanyID, String crmAccountType);

    List<EdsCrmAccount> getAllSubAccounts(EdsCrmAccount account, boolean recursively);

    Object getCustomFieldValue(Integer objectID, String fieldCode);

    List<EdsCrmAccount> getAccountByCompanyId(Integer companyId);

    EdsCrmAccount getAccountByMagentoId(Integer magentoId);

    void updateCrmAccountsByAccountType(Integer accountID, boolean isReceivable);

    EdsCrmAccount getSignupLeadByCompanyId(Integer companyId);

    List<Integer> getAccountIDsByOwner(Integer ownerID);

    NumberData generateAccountNumberData(String accountType);

    NumberData generateAccountNumberData(String accountType, Integer intNumber);

    Integer getCustomerExportInvoiceCount(Integer customerId);

    EdsCrmAccount getByEntityId(Integer objectID);
}
