package com.edatasite.workforce.gwt.core.server.servlets.pdf.invoice;

import com.edatasite.workforce.gwt.core.server.servlets.pdf.localization.PdfLocalizationName;

/**
 * Created by IntelliJ IDEA.
 * User: Bunyod Xalilov
 * Date: 2/11/12
 * Time: 3:29 PM
 * To change this template use File | Settings | File Templates.
 */
public class SaleOrderViewPDFHandler extends SaleQuoteViewPDFHandler {

    @Override
    protected String getFooterContactText() {
        return pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.footerSaleOrderText);
    }

    @Override
    protected boolean isSalesOrder() {
        return true;
    }

    @Override
    protected String getTableName(Object dataClass) {
        return accountingLocalizer.localizeAccounting(PdfLocalizationName.salesOrder);
    }

    protected String getFromInvoice() {
        return SALE_ORDER;
    }
}
