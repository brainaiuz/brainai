/**********************************************************************************************************************
 * LAST CHANGE                                                                                                        *
 * User: Hayot                                                                                                        *
 * Time: 2010/4/27 2:8:35                                                                                             *
 **********************************************************************************************************************/

package com.edatasite.workforce.gwt.client.client.rpc;


import com.edatasite.workforce.gwt.accounting.client.rpc.discount.DiscountItem;
import com.edatasite.workforce.gwt.accounting.client.rpc.priceLevel.PriceLevelItem;
import com.edatasite.workforce.gwt.client.client.rpc.supplier.SupplierList;
import com.edatasite.workforce.gwt.contact.client.rpc.ContactListItem;
import com.edatasite.workforce.gwt.contact.client.rpc.CrmAccountItem;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.rpc.*;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.ListLoadConfig;
import com.edatasite.workforce.gwt.documents.client.rest.resource.FileResource;
import com.edatasite.workforce.gwt.invoice.client.rpc.MessageItem;
import com.edatasite.workforce.gwt.invoice.client.rpc.TypeItem;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.ServiceDefTarget;

import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA. User: mansur Date: Jan 8, 2008 Time: 1:48:25 PM To
 * change this template use File | Settings | File Templates.
 */

public interface ClientService extends RemoteService {

    CrmAccountItem editAccount(Integer objectId, String type);

    Integer createClient(CrmAccountItem newClient, Integer userID);

    SelectItem[] getCountries();

    SelectItem[] getRegions();

    SelectItem[] getRegions(Integer countryId);

    ContactListItem getPrimaryContact(Integer clientId);

    NewClientList getClients(ListingFilterParameter fp, ListLoadConfig config);

    CrmAccountItem getClient(Integer objectID);

    CrmAccountItem getClientForEdit(Integer objectId);

    ClientProjectList getProjects(Integer clientId, ListLoadConfig config);

    ClientContactList getContacts(Integer clientId, ListLoadConfig config);

    ClientContactList getContacts(Integer clientId);

    Boolean deleteClient(Integer clientID, boolean deleteCrmContact, boolean isBatchDelete);

    ArrayList<Integer> deleteClientsOrSuppliers(ArrayList<Integer> clientIDs, boolean isClient, boolean deleteCrmContact);

//    Integer getCRMContactsCount(String[] contactsEmail);

//    SelectItem[] getOutgoingType();

    SelectItem[] getCurrencies();

    SelectItem[] getPaymentMethod();

    //Supplier

    SupplierList getSuppliers(ListingFilterParameter lfp);

    Integer createSupplier(CrmAccountItem supplier, Integer userID);

    Boolean deleteSupplier(Integer supplierID, boolean deleteCrmContact, boolean isBatchDelete);

//    CrmAccountItem getSupplierForEdit(Integer objectId);

    CrmAccountItem getSupplier(Integer objectID);

    SelectItem[] getSupplierContacts(Integer supplierID);

    //End Supplier

    String getClientCode();

//    HistoryListItem[] getClientSupplierNotes(Integer clientID, boolean isClient);

//    NewsComment saveClientNoteComments(NewsComment data);

//    NewsComment[] getClientNoteComments(Integer noteID);

    TypeItem createClientFromLead(Integer leadId, Integer userID);

    TypeItem getOrCreateCrmAccountFromLead(Integer leadId);

    TypeItem createClientFromCrmAccount(Integer accountID, Integer userID, Boolean visible);

    Integer createContact(ClientContact clientContact);

    FileResource[] getAttachments(Integer clientID);

//    ClientInvoiceList getClientSalesInvoices(Integer clientID);

    ClientContactList getSupplierContactLists(Integer supplierID);

//    SelectItem[] getSupplierAsSelectItem();

    PriceLevelItem[] getClientPriceLevels(Integer clientID);

    DiscountItem[] getClientDiscounts(Integer clientID);

    Integer enableAccess(Integer contactID, Boolean fromSubscriptionForm);

    Integer disableAccess(Integer contactID);

//    void createAccessEnabledContact(Integer clientID, ClientContact clientContact);

    boolean isContactsExist(Integer objectID, String type);

//    BillingData getContactAddress(Integer id, boolean isClient);

    ClientSupplierAddressData getAddressData(Integer clientSupplierID, boolean isClient, Address.EntityType entityType);

    Address editAddress(Integer addressID);

    Integer saveAddress(Address data, Integer clientSupplierID, boolean isClient, boolean isBilling, Address.EntityType entityType);

    NewClientList getNewClients(ListingFilterParameter fp);

    CrmAccountBalance getCrmAccountBalanceReport(DateNonConvertable fromDate, DateNonConvertable toDate, ListingFilterParameter fp);

//    void updateCrmAccountsAfterExportSaasu(Integer objectId, Date lastUpdateDate, String saasuLastUpdatedUid, Integer saasuGUID);

    InvoiceTermsItem[] getInvoiceTermsForLookUp(ListingFilterParameter filterParametrs);

//    CrmAccountItem getSupplierByQBSupplierID(String qbCustomerID);

//    String getSupplierCode();

    SelectItem[] getSubsidiaries(ListingFilterParameter filterParametrs);

    void sendCustomerBalanceEmail(MessageItem messageItem, DateNonConvertable from, DateNonConvertable to);

    void blockAccount(Integer objectId, boolean blockOrUnblock);

//    CrmAccountItem getClientContactPhoneNumber(Integer crmAccountId);

    CrmAccountItem getCustomerQuickData(String accountType);

    class App {
        public static ClientServiceAsync get() {
            ServiceDefTarget target = GWT.create(ClientService.class);
            target.setServiceEntryPoint(Utils.getRpcBaseUrl() + "/client");
            return (ClientServiceAsync) target;
        }
    }

}
