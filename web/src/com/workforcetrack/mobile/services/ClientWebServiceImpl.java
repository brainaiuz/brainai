package com.workforcetrack.mobile.services;

import com.edatasite.workforce.core.domain.crm.EdsCrmAccount;
import com.edatasite.workforce.gwt.client.client.rpc.ClientService;
import com.edatasite.workforce.gwt.client.client.rpc.ClientSupplierAddressData;
import com.edatasite.workforce.gwt.client.client.rpc.ContactItem;
import com.edatasite.workforce.gwt.client.client.rpc.NewClientList;
import com.edatasite.workforce.gwt.contact.client.rpc.ContactListItem;
import com.edatasite.workforce.gwt.contact.client.rpc.CrmAccountItem;
import com.edatasite.workforce.gwt.core.client.rpc.Address;
import com.edatasite.workforce.gwt.core.client.rpc.ClientContact;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.crm.client.rpc.CRMService;
import com.edatasite.workforce.gwt.invoice.server.app.InvoiceServiceLocal;
import com.workforcetrack.mobile.rpc.base.WebServiceUtils;
import com.workforcetrack.mobile.rpc.client.*;
import com.workforcetrack.mobile.rpc.contact.MCountryList;
import com.workforcetrack.mobile.rpc.contact.MStateList;
import com.workforcetrack.mobile.rpc.expense.MCurrencyList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

/**
 * User: HAveANiceDay  Date: 20.06.11
 */
@Service("clientWebService")
public class ClientWebServiceImpl implements ClientWebService {

    @Autowired
    ClientService clientService;
    @Autowired
    CRMService crmService;
    @Autowired
    InvoiceServiceLocal invoiceServiceLocal;

    @Override
    public MContactItemList getContactEmails(Integer objectID, String type) {
        ContactItem[] contactItems = invoiceServiceLocal.getContactsEmailAsSelectItem(objectID, type, null, false);

        return new MContactItemList(contactItems);
    }

    @Override
    public String getClientCode() {
        return clientService.getClientCode();  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public MCountryList getCountries() {
        ContactWebServiceImpl result = new ContactWebServiceImpl();
        return result.getCountries();  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public MStateList getRegions() {
        ContactWebServiceImpl result = new ContactWebServiceImpl();
        return result.getStates();   //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public MStateList getRegions(Integer countryID) {
        ContactWebServiceImpl result = new ContactWebServiceImpl();
        return result.getStatesByCountryID(countryID);

    }


    @Override
    public MClientListItem get(Integer clientId) {
        if (clientId == null || clientId == 0) {
            return null;
        }
        CrmAccountItem crmAccountItem = clientService.getClient(clientId);
        MClientListItem mClientListItem = new MClientListItem(crmAccountItem);
        ContactListItem contactListItem = clientService.getPrimaryContact(clientId);
        if (contactListItem != null) {
            mClientListItem.setContact_email(contactListItem.getPrimaryEmail());
            mClientListItem.setContact_firstName(contactListItem.getFirstName());
            mClientListItem.setContact_lastName(contactListItem.getLastName());
            mClientListItem.setContact_objectID(contactListItem.getObjectId() != null ? contactListItem.getObjectId().toString() : null);
            mClientListItem.setContact_phone(contactListItem.getPrimaryPhone());
        }
        return mClientListItem;

    }

    @Override
    public MClientListItem edit(Integer clientId) {
        if (clientId == null || clientId == 0) {
            return null;
        }
        CrmAccountItem crmAccountItem = clientService.getClientForEdit(clientId);
        MClientListItem mClientListItem = new MClientListItem(crmAccountItem);
        mClientListItem.setClientCode(crmAccountItem.getCode());
        ContactListItem contactListItem = clientService.getPrimaryContact(clientId);
        if (contactListItem != null) {
            mClientListItem.setContact_email(contactListItem.getPrimaryEmail());
            mClientListItem.setContact_firstName(contactListItem.getFirstName());
            mClientListItem.setContact_lastName(contactListItem.getLastName());
            mClientListItem.setContact_objectID(contactListItem.getObjectId() != null ? contactListItem.getObjectId().toString() : null);
            mClientListItem.setContact_phone(contactListItem.getPrimaryPhone());
        }
        return mClientListItem; //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public MClientListItem edit() {
        CrmAccountItem crmAccountItem = clientService.getClientForEdit(null);
        MClientListItem mClientListItem = new MClientListItem(crmAccountItem);
        mClientListItem.setClientCode(getClientCode());

        return mClientListItem;
    }

    @Override
    public Integer save(MClientListItem mClientListItem) {
        if (mClientListItem == null) {
            return null;
        }
        try {
            CrmAccountItem crmAccountItem = new CrmAccountItem();
            ContactListItem contactItem = new ContactListItem();

            if (mClientListItem.getObjectID() != null && mClientListItem.getObjectID() != 0) {
                crmAccountItem = clientService.getClientForEdit(mClientListItem.getObjectID());
                contactItem = clientService.getPrimaryContact(mClientListItem.getObjectID());
            }

            crmAccountItem = mClientListItem.convertToCrmAccountItem(crmAccountItem, contactItem);

            return crmService.saveAccount(crmAccountItem, EdsCrmAccount.CUSTOMER, mClientListItem.getOwnerID(), false, false, false, true);
        } catch (Exception ex) {
            ex.printStackTrace();
            return 0;
        }
    }

    @Override
    public MClientContactListItem getClientContact(Integer id) {
        if (id == null) {
            return null;
        }
        return new MClientContactListItem(clientService.getPrimaryContact(id));
    }

    @Override
    public MCurrencyList getCurrencies() {
        SelectItem[] currencyList = clientService.getCurrencies();

        return new MCurrencyList(currencyList);
    }

    @Override
    public MListArray getListData() {
        MListArray result = new MListArray();
        result.setCountryList(getCountries().getCountryList());
        result.setRegionList(getRegions().getStateList());
        result.setCurrencyList(getCurrencies().getCurrencyList());
        return result;
    }

    @Override
    public MListArray getListData(Integer objectID) {
        MListArray result = new MListArray();
        result.setCountryList(getCountries().getCountryList());
        result.setRegionList(getRegions(objectID).getStateList());
        result.setCurrencyList(getCurrencies().getCurrencyList());
        return result;

    }

    @Override
    public Integer saveClientContact(MClientContactListItem contactItem) {

        ClientContact clientContact = new ClientContact();
        return clientService.createContact(contactItem.convertToContactListItem(clientContact));
    }


    @Override
    public boolean delete(Integer clientID) {
        if (clientID == null || clientID == 0) {
            return false;
        }
        return clientService.deleteClient(clientID, false, false);
    }

//    @Override
//    public MBillingData getContactAddress(Integer id) {
//        MBillingData result;
//        if (id == null || id == 0) {
//            return null;
//        }
//        result = new MBillingData(clientService.getContactAddress(id, true));
//        return result;
//    }


    @Override
    public MNewClientList getList(MFilterParametrs fp) {
        if (fp == null) {
            return null;
        }
        ListingFilterParameter listingFilterParameter = new ListingFilterParameter();
        fp.convertToListingFilterParameter(listingFilterParameter);
        listingFilterParameter.setFromMobile(true);
        NewClientList newClientList = clientService.getNewClients(listingFilterParameter);
        MNewClientList result = new MNewClientList();
        if (newClientList != null) {
            result.setTotalCount(newClientList.getTotal());
            ArrayList<MClientListItem> items = new ArrayList<>();
            MClientListItem item;
            ClientSupplierAddressData addressData;
            for (CrmAccountItem crmAccountItem : newClientList.getList()) {
                item = new MClientListItem(crmAccountItem);
                addressData = clientService.getAddressData(item.getObjectID(), true, Address.EntityType.CrmAccount);
                item.setPrimaryBillAddressID(addressData.getPrimaryBillAddressID());
                item.setBillAddresses(WebServiceUtils.getAsMSelectItemList(addressData.getBillAddresses()));
                item.setCurrencyID(addressData.getCurrencyID());
                items.add(item);
            }
            result.setClientListItem(items);
        }

        return result;
    }

}
