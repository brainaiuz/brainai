package com.edatasite.workforce.gwt.client.client.rpc;

import com.edatasite.workforce.gwt.accounting.client.rpc.discount.DiscountItem;
import com.edatasite.workforce.gwt.accounting.client.rpc.priceLevel.PriceLevelItem;
import com.edatasite.workforce.gwt.client.client.rpc.supplier.SupplierList;
import com.edatasite.workforce.gwt.contact.client.rpc.ContactListItem;
import com.edatasite.workforce.gwt.contact.client.rpc.CrmAccountItem;
import com.edatasite.workforce.gwt.core.client.rpc.*;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.ListLoadConfig;
import com.edatasite.workforce.gwt.documents.client.rest.resource.FileResource;
import com.edatasite.workforce.gwt.invoice.client.rpc.MessageItem;
import com.edatasite.workforce.gwt.invoice.client.rpc.TypeItem;
import com.google.gwt.http.client.Request;
import com.google.gwt.user.client.rpc.AsyncCallback;

import java.util.ArrayList;

/**
 * User: mansur
 * Date: Jan 8, 2008
 * Time: 1:49:16 PM
 */

public interface ClientServiceAsync {

    void editAccount(Integer objectId, String type, AsyncCallback<CrmAccountItem> async);

    void createClient(CrmAccountItem newClient, Integer userID, AsyncCallback<Integer> async);

    void getCountries(AsyncCallback<SelectItem[]> async);

    void getRegions(AsyncCallback<SelectItem[]> async);

    void getRegions(Integer countryId, AsyncCallback<SelectItem[]> async);

    void getPrimaryContact(Integer clientId, AsyncCallback<ContactListItem> async);

    Request getClients(ListingFilterParameter fp, ListLoadConfig config, AsyncCallback<NewClientList> async);

    void getClient(Integer objectID, AsyncCallback<CrmAccountItem> async);

    void getClientForEdit(Integer objectId, AsyncCallback<CrmAccountItem> async);

    void getProjects(Integer clientId, ListLoadConfig config, AsyncCallback<ClientProjectList> async);

    void getContacts(Integer clientId, ListLoadConfig config, AsyncCallback<ClientContactList> async);

    void getContacts(Integer clientId, AsyncCallback<ClientContactList> async);

    void deleteClient(Integer clientID, boolean deleteCrmContact, boolean isBatchDelete, AsyncCallback<Boolean> async);

    void deleteClientsOrSuppliers(ArrayList<Integer> clientIDs, boolean isClient, boolean deleteCrmContact, AsyncCallback<ArrayList<Integer>> async);

//    void getCRMContactsCount(String[] contactsEmail, AsyncCallback<Integer> async);

    void getCurrencies(AsyncCallback<SelectItem[]> async);

    void getPaymentMethod(AsyncCallback<SelectItem[]> async);

    //Supplier

    Request getSuppliers(ListingFilterParameter lfp, AsyncCallback<SupplierList> async);

    void createSupplier(CrmAccountItem supplier, Integer userID, AsyncCallback<Integer> async);

//    void getSupplierForEdit(Integer objectId, AsyncCallback<CrmAccountItem> async);

    void getSupplier(Integer objectID, AsyncCallback<CrmAccountItem> async);

    void getSupplierContacts(Integer supplierID, AsyncCallback<SelectItem[]> async);

    //End Supplier

    void getClientCode(AsyncCallback<String> async);

//    void getClientSupplierNotes(Integer clientID, boolean isClient, AsyncCallback<HistoryListItem[]> async);

//    void saveClientNoteComments(NewsComment data, AsyncCallback<NewsComment> callback);

//    void getClientNoteComments(Integer noteID, AsyncCallback<NewsComment[]> callback);

    void createClientFromLead(Integer leadId, Integer userID, AsyncCallback<TypeItem> callback);

    void getOrCreateCrmAccountFromLead(Integer leadId, AsyncCallback<TypeItem> callback);

    void createClientFromCrmAccount(Integer accountID, Integer userID, Boolean visible, AsyncCallback<TypeItem> callback);

    void createContact(ClientContact clientContact, AsyncCallback<Integer> callback);

    void getAttachments(Integer clientID, AsyncCallback<FileResource[]> callback);

//    void getClientSalesInvoices(Integer clientID, AsyncCallback<ClientInvoiceList> callback);

    void deleteSupplier(Integer supplierID, boolean deleteCrmContact, boolean isBatchDelete, AsyncCallback<Boolean> async);

    void getSupplierContactLists(Integer supplierID, AsyncCallback<ClientContactList> callback);

    /*void getClientByQBCustomerID(String qbCustomerId, AsyncCallback<CrmAccountItem> async);

    void updateClientByQB(CrmAccountItem client, String externalGUID, Integer synchItemId, AsyncCallback<Boolean> async);*/

//    void getSupplierAsSelectItem(AsyncCallback<SelectItem[]> asyncCallback);

    void getClientPriceLevels(Integer clientID, AsyncCallback<PriceLevelItem[]> async);

    void getClientDiscounts(Integer clientID, AsyncCallback<DiscountItem[]> async);

    void enableAccess(Integer contactID, Boolean fromSubscriptionForm, AsyncCallback<Integer> callback);

    void disableAccess(Integer contactID, AsyncCallback<Integer> callback);

//    void createAccessEnabledContact(Integer clientID, ClientContact clientContact, AsyncCallback<Void> callback);

    void isContactsExist(Integer objectID, String type, AsyncCallback<Boolean> callback);

//    void getContactAddress(Integer id, boolean isClient, AsyncCallback<BillingData> async);

    void getNewClients(ListingFilterParameter fp, AsyncCallback<NewClientList> asyncCallback);

    void getAddressData(Integer clientSupplierID, boolean isClient, Address.EntityType entityType, AsyncCallback<ClientSupplierAddressData> callback);

    void editAddress(Integer addressID, AsyncCallback<Address> callback);

    void saveAddress(Address data, Integer clientSupplierID, boolean isClient, boolean isBilling, Address.EntityType entityType, AsyncCallback<Integer> callback);

    void getCrmAccountBalanceReport(DateNonConvertable fromDate, DateNonConvertable toDate, ListingFilterParameter fp, AsyncCallback<CrmAccountBalance> callback);

//    void updateCrmAccountsAfterExportSaasu(Integer objectId, Date lastUpdateDate, String saasuLastUpdatedUid, Integer saasuGUID, AsyncCallback<Void> async);

    void getInvoiceTermsForLookUp(ListingFilterParameter filterParametrs, AsyncCallback<InvoiceTermsItem[]> callback);

//    void getSupplierByQBSupplierID(String qbCustomerID, AsyncCallback<CrmAccountItem> async);

//    void getSupplierCode(AsyncCallback<String> async);

    void getSubsidiaries(ListingFilterParameter filterParametrs, AsyncCallback<SelectItem[]> callback);

    void sendCustomerBalanceEmail(MessageItem messageItem, DateNonConvertable from, DateNonConvertable to, AsyncCallback<Void> callback);

    void blockAccount(Integer objectId, boolean blockOrUnblock, AsyncCallback<Void> callback);

//    void getClientContactPhoneNumber(Integer crmAccountId, AsyncCallback<CrmAccountItem> async);

    void getCustomerQuickData(String accountType, AsyncCallback<CrmAccountItem> async);
}