package com.edatasite.workforce.gwt.core.server.servlets.pdf.invoice;

import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.ListLoadConfig;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.localization.PdfLocalizationName;
import com.edatasite.workforce.gwt.invoice.client.rpc.InvoiceList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.support.WfmMessageSource;

/**
 * Created by IntelliJ IDEA.
 * User: Ruslan Muhammadov
 * Date: 29.05.2009
 * Time: 20:37:35
 * To change this template use File | Settings | File Templates.
 */
public class SaleOrderListPDFHandler extends BaseInvoiceListPDFHandler {

    @Autowired
    @Qualifier("accountingLocalizer")
    protected WfmMessageSource accountingLocalizer;

    protected InvoiceList getInvoiceData(ListingFilterParameter fp, ListLoadConfig config) {
        return invoiceCircularResolver.getSaleOrderData(fp);
    }

    public String getFileName() {
        return "Sales_Orders_List";
    }

    @Override
    protected int getWhat() {
        return SALE_ORDER;
    }

    protected String getTitle() {
        return commonLocalizer.localize(PdfLocalizationName.salesOrders);
    }
}