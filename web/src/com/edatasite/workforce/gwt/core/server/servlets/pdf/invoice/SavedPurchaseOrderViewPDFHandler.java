package com.edatasite.workforce.gwt.core.server.servlets.pdf.invoice;

import com.edatasite.workforce.core.domain.EdsCompany;
import com.edatasite.workforce.core.domain.EdsInvoicingSettings;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.accounting.EdsPurchaseOrder;
import com.edatasite.workforce.core.domain.accounting.EdsQuote;
import com.edatasite.workforce.core.domain.accounting.EdsShippingMethod;
import com.edatasite.workforce.core.tools.StringUtil;
import com.edatasite.workforce.gwt.core.client.rpc.CompanyCustomFieldItem;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.RequestObject;
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
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: admin
 * Date: Jun 10, 2009
 * Time: 2:03:33 PM
 * To change this template use File | Settings | File Templates.
 */
public class SavedPurchaseOrderViewPDFHandler extends PurchaseOrderViewPDFHandler {

    public boolean prepareRequest(HttpServletRequest request) {
        return true;
    }

    protected Object getDataClass(HttpServletRequest request) {
        return new InvoiceQuoteRequestObject();
    }

    @Override
    public ITextGenericPdfData buildPdfDocument(Object dataClass, Document document, PdfWriter writer) throws IOException {
        InvoiceQuoteRequestObject requestObject = (InvoiceQuoteRequestObject) dataClass;
        EdsQuote invoice = quoteManager.get(requestObject.getObjectID());
        ArrayList<CompanyCustomFieldItem> itemCustomFields = commonServiceLocal.getCompanyCustomFields(ViewName.PurchaseOrderItem);
        invoice.setItemCustomFields(CustomFieldsUtils.setRPCCustomFieldItems(null, itemCustomFields));

        NewInvoice mainInvoicing = EdsQuote.getQuoteData(invoice);
        mainInvoicing.setCustomItemColumns(itemTableSettingService.getColumnConfigs(ItemTableEnum.PURCHASE_ORDER_ITEM));

        if (invoice instanceof EdsPurchaseOrder) {
            EdsPurchaseOrder order = (EdsPurchaseOrder) invoice;
            EdsShippingMethod shippingMethod = invoice.getShippingMethod();
            if (order.getClientID() != null) {
                TypeItem clientItem = new TypeItem(order.getClientID(), null, null);
                clientItem.setMailAddressID(order.getClientMailAddressID());
                mainInvoicing.setClientItem(clientItem);
            }
            mainInvoicing.setRequisitionedBy(order.getRequisitionedBy() != null ? order.getRequisitionedBy().getAsSelectItem() : null);
            mainInvoicing.setPaymentMethodID(order.getPaymentMethod() != null ? order.getPaymentMethod().getObjectID() : null);
            mainInvoicing.setPaymentTerms(order.getPaymentTerms());
            mainInvoicing.setShippingTerms(order.getShippingTerms());
            mainInvoicing.setQuoteNumber(order.getQuoteNumber());
            mainInvoicing.setShippingMethodID(shippingMethod != null ? shippingMethod.getObjectID() : null);
            mainInvoicing.setShippingPrice(order.getShippingAmount());
            if (order.getCancelDate() != null) {
                mainInvoicing.setCancelDate(new DateNonConvertable(order.getCancelDate()));
            }
        }
        mainInvoicing.setClientContactID(requestObject.getContactID());
        mainInvoicing.setPoNumber(mainInvoicing.getInvoiceNumber());
        return super.buildPdfDocument(mainInvoicing, document, writer);
    }

    @Override
    protected ITextGenericPdfData buildPdfDocumentCustomise(Object dataClass, EdsCompany company, boolean hasPhantom) {
        InvoiceQuoteRequestObject requestObject = (InvoiceQuoteRequestObject) dataClass;
        EdsQuote invoice = quoteManager.get(requestObject.getObjectID());
        ArrayList<CompanyCustomFieldItem> itemCustomFields = commonServiceLocal.getCompanyCustomFields(ViewName.PurchaseOrderItem);
        invoice.setItemCustomFields(CustomFieldsUtils.setRPCCustomFieldItems(null, itemCustomFields));
        NewInvoice mainInvoicing = EdsQuote.getQuoteData(invoice);
        mainInvoicing.setCustomItemColumns(itemTableSettingService.getColumnConfigs(ItemTableEnum.PURCHASE_ORDER_ITEM));
        mainInvoicing.setPdfTemplateID(requestObject.getTemplateID());
        if (invoice instanceof EdsPurchaseOrder) {
            EdsPurchaseOrder order = (EdsPurchaseOrder) invoice;
            EdsShippingMethod shippingMethod = invoice.getShippingMethod();
            mainInvoicing.setRequisitionedBy(order.getRequisitionedBy() != null ? order.getRequisitionedBy().getAsSelectItem() : null);
            mainInvoicing.setPaymentMethodID(order.getPaymentMethod() != null ? order.getPaymentMethod().getObjectID() : null);
            mainInvoicing.setReference(order.getReference());
            mainInvoicing.setPaymentTerms(order.getPaymentTerms());
            mainInvoicing.setShippingTerms(order.getShippingTerms());
            mainInvoicing.setShippingMethodID(shippingMethod != null ? shippingMethod.getObjectID() : null);
            mainInvoicing.setShippingPrice(order.getShippingAmount());
//            if (order.getWarehouse() != null)
//                mainInvoicing.setWarehouseID(order.getWarehouse().getObjectID());
            if (order.getOrderTerms() != null) {
                mainInvoicing.setInvoiceTermsItem(order.getOrderTerms().getAsRPC());
            }
            if (order.getClientID() != null) {
                TypeItem clientItem = new TypeItem(order.getClientID(), null, null);
//                clientItem.setBillAddressID(order.getClientBillAddressID());
                clientItem.setMailAddressID(order.getClientMailAddressID());
                mainInvoicing.setClientItem(clientItem);
            }
            mainInvoicing.setQuoteNumber(order.getQuoteNumber());
            mainInvoicing.setTaxCalculationType(order.getTaxCalculationType());
        }
        mainInvoicing.setPoNumber(mainInvoicing.getInvoiceNumber());
        mainInvoicing.setCustomFieldItems(CustomFieldsUtils.setRPCCustomFieldItems(invoice.getCustomFields(), commonService.getCompanyCustomFields(ViewName.PurchaseOrder)));
        return super.buildPdfDocumentCustomise(mainInvoicing, company, hasPhantom);
    }

    @Override
    protected void setFileName(EdsUser edsUser, Object dataClass) {
        if (edsUser == null || dataClass == null) {
            return;
        }
        final Integer invoiceId = ((RequestObject) dataClass).getObjectID();

        if (invoiceId == null) {
            return;
        }

        final EdsInvoicingSettings invoicingSettings = invoicingSettingsManager.getInvoiceSettings(edsUser.getCompany());
        final StringBuilder fileName = new StringBuilder();

        if (invoicingSettings != null && !StringUtil.isEmpty(invoicingSettings.getPurchaseOrderPdfNamingFormat())) {
            final Map<String, String> params = this.getFileNameParams(invoiceId);
            final String pdfPrefix = invoicingSettings.getPurchaseOrderPdfNamingPrefix();

            if (!StringUtil.isEmpty(pdfPrefix)) {
                params.put(PDF_PREFIX, pdfPrefix);
            }
            params.put(PDF_COMPANY_NAME, escapeHtml(edsUser.getCompany().getName()));
            params.put(PDF_GENERATED_DATE, escapeHtml(dateFormat.format(edsUser.getUserDate())));
            params.put(PDF_USER_NAME, escapeHtml(edsUser.getName()));
            params.put(PDF_TYPE, getFileName());
            final String[] format = invoicingSettings.getPurchaseOrderPdfNamingFormat().split("_");

            for (String aFormat : format) {
                final String value = params.get(aFormat);

                if (!StringUtil.isEmpty(value)) {
                    fileName.append(fileName.length() > 0 ? ("-" + value) : value);
                }
            }
        }
        if (fileName.length() <= 0) {
            fileName.append(getFileName() + "-" + edsUser.getCompany().getName() + "-" + dateFormat.format(edsUser.getUserDate()));
        }
        this.setFileName(fileName.toString());
    }
}
