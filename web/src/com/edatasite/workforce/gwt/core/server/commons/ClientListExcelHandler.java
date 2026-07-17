package com.edatasite.workforce.gwt.core.server.commons;

import com.edatasite.workforce.gwt.client.client.rpc.ClientService;
import com.edatasite.workforce.gwt.contact.client.rpc.CrmAccountItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import org.springframework.beans.factory.annotation.Autowired;

public class ClientListExcelHandler extends CrmAccountsExcelHandler {

    @Autowired
    private ClientService clientService;

    @Override
    protected void setFileName() {
        filename = "Clients";
    }

    @Override
    public ListResult<CrmAccountItem> getList(ListingFilterParameter filterParametrs) {
        return clientService.getNewClients(filterParametrs);
    }
}
