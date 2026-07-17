package com.edatasite.workforce.gwt.core.server.servlets.pdf.invoice;

//import com.edatasite.workforce.core.domain.accounting.EdsBankAccount;
//import com.edatasite.workforce.core.domain.accounting.EdsBaseSaleInvoice;

import com.edatasite.workforce.core.domain.EdsCompany;
import com.edatasite.workforce.core.domain.accounting.EdsInvoice;
import com.edatasite.workforce.core.domain.accounting.EdsInvoiceItem;
import com.edatasite.workforce.gwt.core.client.ui.customfields.ViewName;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.PdfReferenceCodeNameEnum;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.ITextGenericPdfData;
import com.edatasite.workforce.gwt.core.server.utils.CustomFieldsUtils;
import com.edatasite.workforce.gwt.invoice.client.rpc.InvoiceQuoteRequestObject;
import com.edatasite.workforce.gwt.invoice.client.rpc.NewInvoice;
import com.edatasite.workforce.gwt.invoice.client.rpc.NewInvoiceItem;
import com.lowagie.text.Document;
import com.lowagie.text.pdf.PdfWriter;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.math.BigDecimal;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: 04.08.2010
 * Time: 14:23:28
 * To change this template use File | Settings | File Templates.
 */
public class SavedCreditNoteViewPDFHandler extends CreditNoteViewPDFHandler {

    @Override
    public boolean prepareRequest(HttpServletRequest request) {
        return true;
    }

    @Override
    protected Object getDataClass(HttpServletRequest request) {
        return new InvoiceQuoteRequestObject();
    }

    @Override
    public ITextGenericPdfData buildPdfDocument(Object dataClass, Document document, PdfWriter writer) throws IOException {
        InvoiceQuoteRequestObject requestObject = (InvoiceQuoteRequestObject) dataClass;
        EdsInvoice invoice = invoiceManager.get(requestObject.getObjectID());
        NewInvoice mainInvoicing = EdsInvoice.getInvoiceData(invoice);
        mainInvoicing.setCreditNote(true);
        return super.buildPdfDocument(mainInvoicing, document, writer);
    }

    @Override
    protected ITextGenericPdfData buildPdfDocumentCustomise(Object dataClass, EdsCompany company, boolean hasPhantom) {
        InvoiceQuoteRequestObject requestObject = (InvoiceQuoteRequestObject) dataClass;
        EdsInvoice invoice = invoiceManager.get(requestObject.getObjectID());
        NewInvoice mainInvoicing = EdsInvoice.getInvoiceData(invoice);
        mainInvoicing.setCreditNoteInvoiceTotal(invoiceManager.getInvoiceTotalByCreditNoteId(requestObject.getObjectID()));
        NewInvoiceItem[] items = mainInvoicing.getItems();
        if (invoice.getCreditNoteInvoice() != null) {
            mainInvoicing.setCreditNoteInvoiceSubTotal(invoice.getCreditNoteInvoice().getSubtotal());
            int i = 0;
            for (EdsInvoiceItem item : invoice.getCreditNoteInvoice().getInvoiceItems()) {
                if (items.length > i) {
                    Double qty = item.getQty().doubleValue() - items[i].getQuantity().doubleValue();
                    items[i].setReceivedQty(new BigDecimal(qty));
                }
                i++;
            }
        }
        mainInvoicing.setCreditNote(true);
        mainInvoicing.setPdfTemplateID(requestObject.getTemplateID());
        if (PdfReferenceCodeNameEnum.RECEIVABLE_CREDIT_NOTE.equals(getPdfCodeName(null))) {
            mainInvoicing.setCustomFieldItems(CustomFieldsUtils.setRPCCustomFieldItems(invoice.getCustomFields(), commonService.getCompanyCustomFields(ViewName.SaleInvoice)));
        } else if (PdfReferenceCodeNameEnum.PAYABLE_CREDIT_NOTE.equals(getPdfCodeName(null))) {
            mainInvoicing.setCustomFieldItems(CustomFieldsUtils.setRPCCustomFieldItems(invoice.getCustomFields(), commonService.getCompanyCustomFields(ViewName.PurchaseInvoice)));
        }
        return super.buildPdfDocumentCustomise(mainInvoicing, company, hasPhantom);
    }
}
