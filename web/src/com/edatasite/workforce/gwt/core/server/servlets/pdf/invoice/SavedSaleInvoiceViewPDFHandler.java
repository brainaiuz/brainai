package com.edatasite.workforce.gwt.core.server.servlets.pdf.invoice;

import com.edatasite.workforce.core.domain.EdsCompany;
import com.edatasite.workforce.core.domain.EdsProject;
import com.edatasite.workforce.core.domain.accounting.EdsBankAccount;
import com.edatasite.workforce.core.domain.accounting.EdsBaseSaleInvoice;
import com.edatasite.workforce.core.domain.accounting.EdsInvoice;
import com.edatasite.workforce.core.domain.accounting.EdsSaleInvoice;
import com.edatasite.workforce.core.domain.accounting.EdsShippingMethod;
import com.edatasite.workforce.gwt.core.client.rpc.CompanyCustomFieldItem;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.itemtablesettings.ItemTableEnum;
import com.edatasite.workforce.gwt.core.client.ui.customfields.ViewName;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.ITextGenericPdfData;
import com.edatasite.workforce.gwt.core.server.utils.CustomFieldsUtils;
import com.edatasite.workforce.gwt.invoice.client.rpc.InvoiceQuoteRequestObject;
import com.edatasite.workforce.gwt.invoice.client.rpc.NewInvoice;
import com.edatasite.workforce.gwt.invoice.client.rpc.PaymentItem;
import com.lowagie.text.Document;
import com.lowagie.text.pdf.PdfWriter;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: admin
 * Date: Jun 10, 2009
 * Time: 2:04:59 PM
 * To change this template use File | Settings | File Templates.
 */
public class SavedSaleInvoiceViewPDFHandler extends SaleInvoiceViewPDFHandler {


    public boolean prepareRequest(HttpServletRequest request) {
        return true;
    }

    protected Object getDataClass(HttpServletRequest request) {
        return new InvoiceQuoteRequestObject();
    }

    @Override
    public ITextGenericPdfData buildPdfDocument(Object dataClass, Document document, PdfWriter writer) throws IOException {
        InvoiceQuoteRequestObject requestObject = (InvoiceQuoteRequestObject) dataClass;
        EdsInvoice invoice = invoiceManager.getInvoiceForPdfGeneration(requestObject.getObjectID());
        ArrayList<CompanyCustomFieldItem> itemCustomFields = commonServiceLocal.getCompanyCustomFields(ViewName.SaleInvoiceItem);
        invoice.setItemCustomFields(CustomFieldsUtils.setRPCCustomFieldItems(null, itemCustomFields));

        NewInvoice mainInvoicing = invoiceManager.buildInvoiceData(invoice);
        mainInvoicing.setCustomItemColumns(itemTableSettingService.getColumnConfigs(ItemTableEnum.SALE_INVOICE_ITEM));

        if (invoice instanceof EdsBaseSaleInvoice) {
            EdsShippingMethod shippMethod = ((EdsBaseSaleInvoice) invoice).getShippingMethod();
            mainInvoicing.setShippingMethodID(shippMethod != null ? shippMethod.getObjectID() : null);
            mainInvoicing.setShippingPrice(((EdsBaseSaleInvoice) invoice).getShippingAmount());

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
            mainInvoicing.setTaxCalculationType(salesInvoice.getTaxCalculationType());
            mainInvoicing.setRevolutUrl(salesInvoice.getRevolutUrl());
        }
        mainInvoicing.setClientContactID(requestObject.getContactID());
        mainInvoicing.setUserID(requestObject.getUserID());
        return super.buildPdfDocument(mainInvoicing, document, writer);
    }

    @Override
    public ITextGenericPdfData buildPdfDocumentCustomise(Object dataClass, EdsCompany company, boolean hasPhantom) {
        InvoiceQuoteRequestObject requestObject = (InvoiceQuoteRequestObject) dataClass;
        EdsInvoice invoice = invoiceManager.get(requestObject.getObjectID());
        ArrayList<CompanyCustomFieldItem> itemCustomFields = commonServiceLocal.getCompanyCustomFields(ViewName.SaleInvoiceItem);
        invoice.setItemCustomFields(CustomFieldsUtils.setRPCCustomFieldItems(null, itemCustomFields));
        NewInvoice mainInvoicing = EdsInvoice.getInvoiceData(invoice);
        mainInvoicing.setCustomItemColumns(itemTableSettingService.getColumnConfigs(ItemTableEnum.SALE_INVOICE_ITEM));
        NewInvoice subItems = invoiceManager.getInvoiceProductSubItemsByTypes(requestObject.getObjectID());
        mainInvoicing.setProductKitItems(subItems.getProductKitItems());
        mainInvoicing.setAssemblyItems(subItems.getAssemblyItems());

        if (mainInvoicing.getClientID() != null) {
            mainInvoicing.setCustomerBalance(crmAccountManager.getClientBalance(mainInvoicing.getClientID()));
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
            mainInvoicing.setPreviosBalance(saleInvoice.getPreviousBalance());
            mainInvoicing.setPaymentsReceived(saleInvoice.getPaymentReceived());
            if (saleInvoice.getInvoiceTerms() != null) {
                mainInvoicing.setInvoiceTermsItem(saleInvoice.getInvoiceTerms().getAsRPC());
            }
            mainInvoicing.setRevolutUrl(saleInvoice.getRevolutUrl());
            EdsShippingMethod shippMethod = saleInvoice.getShippingMethod();
            mainInvoicing.setShippingMethodID(shippMethod != null ? shippMethod.getObjectID() : null);
            mainInvoicing.setShippingPrice(((EdsBaseSaleInvoice) invoice).getShippingAmount());
            mainInvoicing.setInvoiceType(saleInvoice.getInvoiceType());
        }
        mainInvoicing.setClientContactID(requestObject.getContactID());
        mainInvoicing.setUserID(requestObject.getUserID());
        mainInvoicing.setHistoryList(invoiceCircularResolver.getInvoiceNotes(invoice.getObjectID()));
        mainInvoicing.setCustomFieldItems(CustomFieldsUtils.setRPCCustomFieldItems(invoice.getCustomFields(), commonService.getCompanyCustomFields(ViewName.SaleInvoice)));

        if (invoice.getProjects().size() == 1 && mainInvoicing.getCustomFieldItems() != null) {
            EdsProject project = invoice.getProjects().iterator().next();
            Date from = null, to = null;
            for (CompanyCustomFieldItem customFieldItem : mainInvoicing.getCustomFieldItems()) {
                if ("Period".equals(customFieldItem.getFieldName()) && customFieldItem.getFieldStringValue() != null && customFieldItem.getFieldStringValue().matches("^\\d{2}/\\d{2}/\\d{4}.*\\d{2}/\\d{2}/\\d{4}$")) {
                    from = ServerUtils.parseDate(customFieldItem.getFieldStringValue().substring(0, 10), "dd/MM/yyyy");
                    to = ServerUtils.parseDate(customFieldItem.getFieldStringValue().substring(customFieldItem.getFieldStringValue().length() - 10), "dd/MM/yyyy");
                }
            }
            ArrayList<NewInvoice> dueAmounts = (ArrayList<NewInvoice>) invoiceManager.getInvoiceDueAmountsByProjectId(project.getObjectID(), invoice.getObjectID(), from, to);
            ArrayList<PaymentItem> prepayments = (ArrayList<PaymentItem>) invoicePaymentManager.getInvoicePaymentsByProject(project.getObjectID(), from, to);
            mainInvoicing.setSameProjectInvoices(dueAmounts);
            mainInvoicing.setProjectPrepayments(prepayments);
        }
        return super.buildPdfDocumentCustomise(mainInvoicing, company, hasPhantom);
    }
}
