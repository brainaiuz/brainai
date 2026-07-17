package com.edatasite.workforce.gwt.core.server.servlets.pdf;

import com.edatasite.workforce.core.domain.EdsProperty;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.gwt.client.client.rpc.ClientService;
import com.edatasite.workforce.gwt.contact.client.rpc.CrmAccountItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.crm.CrmAccountListPDFHandler;
import org.springframework.beans.factory.annotation.Autowired;

public class ClientListPDFHandler extends CrmAccountListPDFHandler implements Constants {
    @Autowired
    private ClientService clientService;

    @Override
    public ListResult<CrmAccountItem> getList(ListingFilterParameter filterParametrs) {
        return clientService.getNewClients(filterParametrs);
    }

    @Override
    protected void setFileName(EdsUser user, Object dataClass) {
        setFileName(user.getFirstName() + "_" + user.getLastName() + "_ClientList_" + dateFormat(user.getUserDate()));
    }

    @Override
    protected String getTableName(Object dataClass) {
        ListingFilterParameter fp = (ListingFilterParameter) dataClass;
        EdsProperty property = propertManager.findByCode(fp.getPropertyCode());
        return property != null ? property.getPlural() : pdfWfmMessageSource.localize("clients");
    }
}
