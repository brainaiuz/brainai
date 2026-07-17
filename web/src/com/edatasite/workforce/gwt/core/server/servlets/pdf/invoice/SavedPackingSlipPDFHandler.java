package com.edatasite.workforce.gwt.core.server.servlets.pdf.invoice;

import com.edatasite.workforce.core.domain.EdsCompany;
import com.edatasite.workforce.core.domain.accounting.EdsBankAccount;
import com.edatasite.workforce.core.domain.accounting.EdsBaseSaleInvoice;
import com.edatasite.workforce.core.domain.accounting.EdsInvoice;
import com.edatasite.workforce.core.domain.accounting.EdsSaleInvoice;
import com.edatasite.workforce.core.domain.accounting.EdsShippingMethod;
import com.edatasite.workforce.core.domain.customfields.EdsInvoiceCustomFields;
import com.edatasite.workforce.gwt.core.client.rpc.CompanyCustomFieldItem;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.itemtablesettings.ItemTableEnum;
import com.edatasite.workforce.gwt.core.client.ui.customfields.ViewName;
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
public class SavedPackingSlipPDFHandler extends PackingSlipPDFHandler {

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
        if (invoice instanceof EdsBaseSaleInvoice) {
            EdsShippingMethod shippMethod = ((EdsBaseSaleInvoice) invoice).getShippingMethod();
            mainInvoicing.setShippingMethodID(shippMethod != null ? shippMethod.getObjectID() : null);

            EdsBankAccount bankAcc = ((EdsBaseSaleInvoice) invoice).getBankAccount();
            mainInvoicing.setBankAccount(bankAcc != null ? bankAcc.getAsSelectItem() : null);
        }
        if (invoice instanceof EdsSaleInvoice) {
            EdsSaleInvoice salesInvoice = (EdsSaleInvoice) invoice;
            mainInvoicing.setQuoteNumber(salesInvoice.getQuoteNumber());
            mainInvoicing.setReference(salesInvoice.getReference());
            mainInvoicing.setProgressInvoicing(salesInvoice.getQuotePercent() != null);
            mainInvoicing.setConvertedPercent(salesInvoice.getQuotePercent());
            mainInvoicing.setIntroduction(salesInvoice.getIntroduction());
            mainInvoicing.setInvoiceType(salesInvoice.getInvoiceType());
            if (salesInvoice.getInvoiceTerms() != null) {
                mainInvoicing.setInvoiceTermsItem(salesInvoice.getInvoiceTerms().getAsRPC());
            }
        }
        mainInvoicing.setClientContactID(requestObject.getContactID());
        mainInvoicing.setUserID(requestObject.getUserID());
        return super.buildPdfDocument(mainInvoicing, document, writer);
    }

    @Override
    protected ITextGenericPdfData buildPdfDocumentCustomise(Object dataClass, EdsCompany company, boolean hasPhantom) {
        InvoiceQuoteRequestObject requestObject = (InvoiceQuoteRequestObject) dataClass;
        EdsInvoice invoice = invoiceManager.get(requestObject.getObjectID());
//        setPayPalLink(invoiceCircularResolver.getInvoicePaymentLink(requestObject.getObjectID(), null, invoice.getCompany().getObjectID()));
//        setGoogleCheckOutMerchantID(invoiceCircularResolver.getInvoiceGoogleCheckoutMerchantId(invoice.getCompany().getObjectID()));
        ArrayList<CompanyCustomFieldItem> itemCustomFields = commonServiceLocal.getCompanyCustomFields(ViewName.SaleInvoiceItem);
        invoice.setItemCustomFields(CustomFieldsUtils.setRPCCustomFieldItems(null, itemCustomFields));
        NewInvoice mainInvoicing = EdsInvoice.getInvoiceData(invoice);
        mainInvoicing.setCustomItemColumns(itemTableSettingService.getColumnConfigs(ItemTableEnum.SALE_INVOICE_ITEM));
        EdsInvoiceCustomFields customField = invoice.getCustomFields();
        if (customField != null) {
            ArrayList<CompanyCustomFieldItem> customFieldItems = commonService.getCompanyCustomFields(ViewName.SaleInvoice);
            mainInvoicing.setCustomFieldItems(CustomFieldsUtils.setRPCCustomFieldItems(customField, customFieldItems));
        }
        mainInvoicing.setPdfTemplateID(requestObject.getTemplateID());
        if (invoice instanceof EdsBaseSaleInvoice) {
            EdsBankAccount bankAcc = ((EdsBaseSaleInvoice) invoice).getBankAccount();
            mainInvoicing.setBankAccount(bankAcc != null ? bankAcc.getAsSelectItem() : null);
        }
        if (invoice instanceof EdsSaleInvoice) {
            EdsSaleInvoice saleInvoice = (EdsSaleInvoice) invoice;
            mainInvoicing.setQuoteNumber(saleInvoice.getQuoteNumber());
            mainInvoicing.setReference(saleInvoice.getReference());
            if (saleInvoice.getFromDate() != null) {
                mainInvoicing.setPeriodStart(new DateNonConvertable(saleInvoice.getFromDate()));
            }
            if (saleInvoice.getToDate() != null) {
                mainInvoicing.setPeriodEnd(new DateNonConvertable(saleInvoice.getToDate()));
            }
            if (((EdsSaleInvoice) invoice).getInvoiceTerms() != null) {
                mainInvoicing.setInvoiceTermsItem(((EdsSaleInvoice) invoice).getInvoiceTerms().getAsRPC());
            }
            mainInvoicing.setPreviosBalance(saleInvoice.getPreviousBalance());
            mainInvoicing.setPaymentsReceived(saleInvoice.getPaymentReceived());
            EdsShippingMethod shippMethod = saleInvoice.getShippingMethod();
            mainInvoicing.setShippingMethodID(shippMethod != null ? shippMethod.getObjectID() : null);
//            if (invoice.getConsignorID() != null)
//                mainInvoicing.setConsignorItem(new SelectItem(invoice.getConsignorID(), null));
//            if (invoice.getConsigneeID() != null)
//                mainInvoicing.setConsigneeItem(new SelectItem(invoice.getConsigneeID(), null));
        }
        mainInvoicing.setUserID(requestObject.getUserID());
        mainInvoicing.setHistoryList(invoiceCircularResolver.getInvoiceNotes(invoice.getObjectID()));
        mainInvoicing.setCustomFieldItems(CustomFieldsUtils.setRPCCustomFieldItems(invoice.getCustomFields(), commonService.getCompanyCustomFields(ViewName.SaleInvoice)));
        return super.buildPdfDocumentCustomise(mainInvoicing, company, hasPhantom);
    }

}
