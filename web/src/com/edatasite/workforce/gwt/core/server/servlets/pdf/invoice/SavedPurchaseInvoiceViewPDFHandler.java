package com.edatasite.workforce.gwt.core.server.servlets.pdf.invoice;

import com.edatasite.workforce.core.domain.EdsCompany;
import com.edatasite.workforce.core.domain.accounting.EdsInvoice;
import com.edatasite.workforce.core.domain.accounting.EdsPurchaseInvoice;
import com.edatasite.workforce.gwt.core.client.rpc.CompanyCustomFieldItem;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.itemtablesettings.ItemTableEnum;
import com.edatasite.workforce.gwt.core.client.ui.customfields.ViewName;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.ITextGenericPdfData;
import com.edatasite.workforce.gwt.core.server.utils.CustomFieldsUtils;
import com.edatasite.workforce.gwt.invoice.client.rpc.InvoiceQuoteRequestObject;
import com.edatasite.workforce.gwt.invoice.client.rpc.NewInvoice;
import com.edatasite.workforce.gwt.invoice.client.rpc.TypeItem;
import com.lowagie.text.Document;
import com.lowagie.text.pdf.PdfWriter;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: admin
 * Date: Jun 10, 2009
 * Time: 2:06:08 PM
 * To change this template use File | Settings | File Templates.
 */
public class SavedPurchaseInvoiceViewPDFHandler extends PurchaseInvoiceViewPDFHandler {

    @Override
    public ITextGenericPdfData buildPdfDocument(Object dataClass, Document document, PdfWriter writer) throws IOException {
        InvoiceQuoteRequestObject requestObject = (InvoiceQuoteRequestObject) dataClass;
        EdsInvoice invoice = invoiceManager.get(requestObject.getObjectID());
        ArrayList<CompanyCustomFieldItem> itemCustomFields = commonServiceLocal.getCompanyCustomFields(ViewName.PurchaseInvoiceItem);
        invoice.setItemCustomFields(CustomFieldsUtils.setRPCCustomFieldItems(null, itemCustomFields));

        NewInvoice mainInvoicing = EdsInvoice.getInvoiceData(invoice);
        mainInvoicing.setCustomItemColumns(itemTableSettingService.getColumnConfigs(ItemTableEnum.PURCHASE_INVOICE_ITEM));

        if (invoice instanceof EdsPurchaseInvoice) {
            EdsPurchaseInvoice pInv = (EdsPurchaseInvoice) invoice;
            if (pInv.getClientID() != null) {
                TypeItem clientItem = new TypeItem(pInv.getClientID(), null, null);
                clientItem.setMailAddressID(pInv.getClientMailAddressID());
                mainInvoicing.setClientItem(clientItem);
            }
            if (pInv.getCancelDate() != null) {
                mainInvoicing.setCancelDate(new DateNonConvertable(pInv.getCancelDate()));
            }
        }
        return super.buildPdfDocument(mainInvoicing, document, writer);
    }

    @Override
    protected ITextGenericPdfData buildPdfDocumentCustomise(Object dataClass, EdsCompany company, boolean hasPhantom) {
        InvoiceQuoteRequestObject requestObject = (InvoiceQuoteRequestObject) dataClass;
        EdsInvoice invoice = invoiceManager.get(requestObject.getObjectID());
        ArrayList<CompanyCustomFieldItem> itemCustomFields = commonServiceLocal.getCompanyCustomFields(ViewName.PurchaseInvoiceItem);
        invoice.setItemCustomFields(CustomFieldsUtils.setRPCCustomFieldItems(null, itemCustomFields));
        NewInvoice mainInvoicing = EdsInvoice.getInvoiceData(invoice);
        mainInvoicing.setCustomItemColumns(itemTableSettingService.getColumnConfigs(ItemTableEnum.PURCHASE_INVOICE_ITEM));
        mainInvoicing.setPdfTemplateID(requestObject.getTemplateID());

        NewInvoice subItems = EdsInvoice.getInvoiceProductSubItemsByTypes(invoice);
        mainInvoicing.setProductKitItems(subItems.getProductKitItems());
        mainInvoicing.setAssemblyItems(subItems.getAssemblyItems());

        if (invoice instanceof EdsPurchaseInvoice) {
            EdsPurchaseInvoice pInv = (EdsPurchaseInvoice) invoice;

            if (pInv.getClientID() != null) {
                TypeItem clientItem = new TypeItem(pInv.getClientID(), null, null);
                clientItem.setMailAddressID(pInv.getClientMailAddressID());
                mainInvoicing.setClientItem(clientItem);
            }
            if (pInv.getInvoiceTerms() != null) {
                mainInvoicing.setInvoiceTermsItem(pInv.getInvoiceTerms().getAsRPC());
            }
            mainInvoicing.setTaxCalculationType(pInv.getTaxCalculationType());
        }
        mainInvoicing.setCustomFieldItems(CustomFieldsUtils.setRPCCustomFieldItems(invoice.getCustomFields(), commonService.getCompanyCustomFields(ViewName.PurchaseInvoice)));
        return super.buildPdfDocumentCustomise(mainInvoicing, company, hasPhantom);
    }

    public boolean prepareRequest(HttpServletRequest request) {
        return true;
    }

    protected Object getDataClass(HttpServletRequest request) {
        return new InvoiceQuoteRequestObject();
    }
}
