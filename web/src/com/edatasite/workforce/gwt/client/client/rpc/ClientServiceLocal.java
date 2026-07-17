package com.edatasite.workforce.gwt.client.client.rpc;

import com.edatasite.workforce.gwt.client.client.rpc.supplier.SupplierList;
import com.edatasite.workforce.gwt.contact.client.rpc.CrmAccountItem;
import com.edatasite.workforce.gwt.core.client.rpc.InvoiceTermsItem;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;

import java.util.ArrayList;

public interface ClientServiceLocal {

    NewClientList getNewClients(ListingFilterParameter filterParameter);

    SupplierList getSuppliers(ListingFilterParameter filterParameter);

    ArrayList<Integer> deleteClientsOrSuppliers(ArrayList<Integer> clientIDs, boolean isClient, boolean deleteCrmContact);

    CrmAccountItem editAccount(Integer objectId, String type);

    SelectItem[] getSubsidiaries(ListingFilterParameter filterParameter);

    InvoiceTermsItem[] getInvoiceTermsForLookUp(ListingFilterParameter filterParametrs);



}
