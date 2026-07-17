package com.edatasite.workforce.gwt.core.server.servlets.pdf.invoice;

import com.edatasite.workforce.core.domain.EdsProperty;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.gwt.client.client.rpc.ClientService;
import com.edatasite.workforce.gwt.contact.client.rpc.CrmAccountItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.crm.CrmAccountListPDFHandler;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by IntelliJ IDEA.
 * User: admin
 * Date: Jul 10, 2009
 * Time: 5:32:52 PM
 * To change this template use File | Settings | File Templates.
 */
public class SuppliersListPDFHandler extends CrmAccountListPDFHandler {
    @Autowired
    private ClientService clientService;

    @Override
    protected String getTableName(Object dataClass) {
        ListingFilterParameter fp = (ListingFilterParameter) dataClass;
        EdsProperty property = propertManager.findByCode(fp.getPropertyCode());
        return property != null ? property.getPlural() : pdfWfmMessageSource.localize("supplierCenter");
    }

    @Override
    public ListResult<CrmAccountItem> getList(ListingFilterParameter filterParametrs) {
        return clientService.getSuppliers(filterParametrs);
    }

    @Override
    protected void setFileName(EdsUser user, Object dataClass) {
        setFileName(user.getFirstName() + "_" + user.getLastName() + "_Supplier/Bills_List_" + dateFormat(user.getUserDate()));
    }
}