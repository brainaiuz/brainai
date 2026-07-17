package com.edatasite.workforce.gwt.core.server.commons;

import com.edatasite.workforce.core.domain.EdsProperty;
import com.edatasite.workforce.gwt.client.client.rpc.ClientService;
import com.edatasite.workforce.gwt.contact.client.rpc.CrmAccountItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.server.db.PropertManager;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.localization.PdfLocalizationName;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by IntelliJ IDEA.
 * User: Admin
 * Date: 27.07.2009
 * Time: 16:34:16
 * To change this template use File | Settings | File Templates.
 */
public class AccountingSupplierListExcelHandler extends CrmAccountsExcelHandler {

    @Autowired
    private ClientService clientService;
    @Autowired
    private PropertManager propertManager;

    @Override
    protected void setFileName() {
        filename = "Suppliers";
    }

    protected String getName() {
        ListingFilterParameter fp = new ListingFilterParameter();
        EdsProperty property = propertManager.findByCode(fp.getPropertyCode());
        return property != null ? property.getPlural() : commonLocalizer.localize(PdfLocalizationName.supplierCenter);
    }
    @Override
    public ListResult<CrmAccountItem> getList(ListingFilterParameter filterParametrs) {
        return clientService.getSuppliers(filterParametrs);
    }
}
