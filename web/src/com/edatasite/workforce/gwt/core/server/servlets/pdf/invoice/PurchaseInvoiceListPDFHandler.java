package com.edatasite.workforce.gwt.core.server.servlets.pdf.invoice;

import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.ListLoadConfig;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.localization.PdfLocalizationName;
import com.edatasite.workforce.gwt.invoice.client.rpc.InvoiceList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.support.WfmMessageSource;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by IntelliJ IDEA.
 * User: Ruslan Muhammadov
 * Date: 29.05.2009
 * Time: 20:36:10
 * To change this template use File | Settings | File Templates.
 */

public class PurchaseInvoiceListPDFHandler extends BaseInvoiceListPDFHandler {

    @Autowired
    @Qualifier("accountingLocalizer")
    protected WfmMessageSource accountingLocalizer;

    @Override
    protected boolean prepareRequest(HttpServletRequest request) {
        return false;
    }

    public String getFileName() {
        return "Purchase_Invoices_List";
    }

    @Override
    protected int getWhat() {
        return PURCHASE_INVOICE;
    }


    protected String getTitle() {
        return accountingLocalizer.localize(PdfLocalizationName.purchaseInvoicesList);
    }

    protected InvoiceList getInvoiceData(ListingFilterParameter fp, ListLoadConfig config) {
        ListingFilterParameter lfp = new ListingFilterParameter();

        lfp.setStart(fp.getStart());
        lfp.setLimit(config.getLimit());

        if (fp.getStartDateNC() != null) {
            lfp.setStartDate(ServerUtils.parseFilterParameterDate(fp.getStartDateNC()));
        }
        if (fp.getEndDateNC() != null) {
            lfp.setEndDate(ServerUtils.parseFilterParameterDate(fp.getEndDateNC()));
        }
        lfp.setSearchKey(fp.getSearchKey());
        lfp.setFacetFilter(fp.getFacetFilter());
        lfp.setClientId(fp.getClientId());
        lfp.setSortField(fp.getSortField());
        lfp.setSortDir(fp.getSortDir());
        lfp.setSortFieldType(fp.getSortFieldType());
        lfp.setCrmContactId(fp.getClientContactId());
        lfp.setObjectsIds(fp.getObjectsIds());
        if(fp.getListPanelTool() != null){
            lfp.setListPanelTool(fp.getListPanelTool());
        }
        return invoiceCircularResolver.getPurchaseInvoiceListFromSolr(lfp);
    }
}
