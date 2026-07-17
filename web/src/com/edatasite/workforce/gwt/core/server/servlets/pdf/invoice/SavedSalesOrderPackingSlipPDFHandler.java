package com.edatasite.workforce.gwt.core.server.servlets.pdf.invoice;

import com.edatasite.workforce.core.domain.EdsCompany;
import com.edatasite.workforce.core.domain.accounting.EdsQuote;
import com.edatasite.workforce.core.domain.accounting.EdsSaleQuote;
import com.edatasite.workforce.core.domain.customfields.EdsInvoiceCustomFields;
import com.edatasite.workforce.gwt.core.client.rpc.CompanyCustomFieldItem;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.customfields.ViewName;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.ITextGenericPdfData;
import com.edatasite.workforce.gwt.core.server.utils.CustomFieldsUtils;
import com.edatasite.workforce.gwt.invoice.client.rpc.InvoiceQuoteRequestObject;
import com.edatasite.workforce.gwt.invoice.client.rpc.NewInvoice;
import com.lowagie.text.Document;
import com.lowagie.text.pdf.PdfWriter;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: Atabek Boboyev
 * Date: 16.07.12
 * Time: 18:32
 * To change this template use File | Settings | File Templates.
 */
public class SavedSalesOrderPackingSlipPDFHandler extends SalesOrderPackingSlipPDFHandler {
    public boolean prepareRequest(HttpServletRequest request) {
        return true;
    }

    protected Object getDataClass(HttpServletRequest request) {
        return new InvoiceQuoteRequestObject();
    }

    @Override
    public ITextGenericPdfData buildPdfDocument(Object dataClass, Document document, PdfWriter writer) throws IOException {
        InvoiceQuoteRequestObject requestObject = (InvoiceQuoteRequestObject) dataClass;
        EdsQuote quote = quoteManager.get(requestObject.getObjectID());
        NewInvoice mainInvoicing = EdsQuote.getQuoteData(quote);
        mainInvoicing.setClientContactID(requestObject.getContactID());
        mainInvoicing.setUserID(requestObject.getUserID());
        mainInvoicing.setInvoiceNumber(mainInvoicing.getInvoiceNumber() + " - " + ServerUtils.shortDateFormat(mainInvoicing.getInvoiceDate().getDate(), quoteManager.getUser().getCompany()));
        String statusCode = quote.getStatus().getCode();
        mainInvoicing.setSalesOrder(Constants.SALE_ORDER.equals(statusCode) || PICKED.equals(statusCode) || PACKED.equals(statusCode) || SHIPPED.equals(statusCode));

        return super.buildPdfDocument(mainInvoicing, document, writer);
    }

    @Override
    protected ITextGenericPdfData buildPdfDocumentCustomise(Object dataClass, EdsCompany company, boolean hasPhantom) {
        InvoiceQuoteRequestObject requestObject = (InvoiceQuoteRequestObject) dataClass;
        EdsQuote quote = quoteManager.get(requestObject.getObjectID());
        NewInvoice mainInvoicing = EdsQuote.getQuoteData(quote);

        String statusCode = quote.getStatus().getCode();
        mainInvoicing.setSalesOrder(Constants.SALE_ORDER.equals(statusCode) || PICKED.equals(statusCode) || PACKED.equals(statusCode) || SHIPPED.equals(statusCode) || PARTIAL_SHIPPED.equals(statusCode));

        EdsInvoiceCustomFields customField = quote.getCustomFields();
        if (customField != null) {
            ArrayList<CompanyCustomFieldItem> customFieldItems = commonService.getCompanyCustomFields(ViewName.SaleInvoice);
            mainInvoicing.setCustomFieldItems(CustomFieldsUtils.setRPCCustomFieldItems(customField, customFieldItems));
        }
        if (quote instanceof EdsSaleQuote) {
            EdsSaleQuote saleQuote = (EdsSaleQuote) quote;
            if (((EdsSaleQuote) quote).getInvoiceTerms() != null) {
                mainInvoicing.setInvoiceTermsItem(((EdsSaleQuote) quote).getInvoiceTerms().getAsRPC());
            }
        }
        mainInvoicing.setPdfTemplateID(requestObject.getTemplateID());
        mainInvoicing.setUserID(requestObject.getUserID());
        mainInvoicing.setHistoryList(invoiceCircularResolver.getInvoiceNotes(quote.getObjectID()));
        mainInvoicing.setCustomFieldItems(CustomFieldsUtils.setRPCCustomFieldItems(quote.getCustomFields(), commonService.getCompanyCustomFields(ViewName.SaleQuote)));

        return super.buildPdfDocumentCustomise(mainInvoicing, company, hasPhantom);
    }

}
