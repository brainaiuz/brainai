package com.edatasite.workforce.gwt.core.server.servlets.pdf.invoice;

import com.edatasite.workforce.core.domain.EdsCompany;
import com.edatasite.workforce.core.domain.EdsProject;
import com.edatasite.workforce.core.domain.accounting.EdsBankAccount;
import com.edatasite.workforce.core.domain.accounting.EdsBaseSaleInvoice;
import com.edatasite.workforce.core.domain.accounting.EdsInvoice;
import com.edatasite.workforce.core.domain.accounting.EdsSaleInvoice;
import com.edatasite.workforce.core.domain.accounting.EdsShippingMethod;
import com.edatasite.workforce.core.domain.customfields.EdsInvoiceCustomFields;
import com.edatasite.workforce.gwt.core.client.rpc.CompanyCustomFieldItem;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.ui.customfields.ViewName;
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

/**
 * Created by IntelliJ IDEA.
 * User: admin
 * Date: Jun 10, 2009
 * Time: 2:15:25 PM
 * To change this template use File | Settings | File Templates.
 */
public class SavedProjectBaseInvoiceViewPDFHandler extends ProjectBaseInvoiceViewPDFHandler {

    public boolean prepareRequest(HttpServletRequest request) {
        return true;
    }

    protected Object getDataClass(HttpServletRequest request) {
        return new InvoiceQuoteRequestObject();
    }

    @Override
    public ITextGenericPdfData buildPdfDocument(Object dataClass, Document document, PdfWriter writer) throws IOException {
        InvoiceQuoteRequestObject requestObject = (InvoiceQuoteRequestObject) dataClass;
        EdsInvoice invoice = invoiceManager.get(requestObject.getObjectID());
        NewInvoice mainInvoicing = EdsInvoice.getInvoiceData(invoice);
        mainInvoicing.setProjectBasedInvoice(true);
        if (invoice instanceof EdsBaseSaleInvoice) {
            EdsShippingMethod shippMethod = ((EdsBaseSaleInvoice) invoice).getShippingMethod();
            mainInvoicing.setShippingMethodID(shippMethod != null ? shippMethod.getObjectID() : null);

            EdsBankAccount bankAcc = ((EdsBaseSaleInvoice) invoice).getBankAccount();
            mainInvoicing.setBankAccount(bankAcc != null ? bankAcc.getAsSelectItem() : null);
        }
        if (invoice instanceof EdsSaleInvoice) {
            EdsSaleInvoice projectBasedInvoice = (EdsSaleInvoice) invoice;
            mainInvoicing.setQuoteNumber(projectBasedInvoice.getQuoteNumber());
            mainInvoicing.setReference(projectBasedInvoice.getReference());
            mainInvoicing.setProgressInvoicing(projectBasedInvoice.getQuotePercent() != null);
            mainInvoicing.setConvertedPercent(projectBasedInvoice.getQuotePercent());
            mainInvoicing.setIntroduction(projectBasedInvoice.getIntroduction());
            mainInvoicing.setInvoiceType(projectBasedInvoice.getInvoiceType());
            if (projectBasedInvoice.getInvoiceTerms() != null) {
                mainInvoicing.setInvoiceTermsItem(projectBasedInvoice.getInvoiceTerms().getAsRPC());
            }
        }
        mainInvoicing.setUserID(requestObject.getUserID());
        return super.buildPdfDocument(mainInvoicing, document, writer);
    }

    @Override
    protected ITextGenericPdfData buildPdfDocumentCustomise(Object dataClass, EdsCompany company, boolean hasPhantom) {
        InvoiceQuoteRequestObject requestObject = (InvoiceQuoteRequestObject) dataClass;
        EdsInvoice invoice = invoiceManager.get(requestObject.getObjectID());
//        setPayPalLink(invoiceCircularResolver.getInvoicePaymentLink(requestObject.getObjectID(), null, invoice.getCompany().getObjectID()));
//        setGoogleCheckOutMerchantID(invoiceCircularResolver.getInvoiceGoogleCheckoutMerchantId(invoice.getCompany().getObjectID()));
        NewInvoice mainInvoicing = EdsInvoice.getInvoiceData(invoice);

        if (mainInvoicing.getClientID() != null) {
            mainInvoicing.setCustomerBalance(crmAccountManager.getClientBalance(mainInvoicing.getClientID()));
        }
        EdsInvoiceCustomFields customField = invoice.getCustomFields();
        if (customField != null) {
            ArrayList<CompanyCustomFieldItem> customFieldItems = commonService.getCompanyCustomFields(ViewName.SaleInvoice);
            mainInvoicing.setCustomFieldItems(CustomFieldsUtils.setRPCCustomFieldItems(customField, customFieldItems));
        }
        mainInvoicing.setPdfTemplateID(requestObject.getTemplateID());
        mainInvoicing.setProjectBasedInvoice(true);
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
            EdsShippingMethod shippMethod = saleInvoice.getShippingMethod();
            mainInvoicing.setShippingMethodID(shippMethod != null ? shippMethod.getObjectID() : null);
        }
        mainInvoicing.setUserID(requestObject.getUserID());
        mainInvoicing.setHistoryList(invoiceCircularResolver.getInvoiceNotes(invoice.getObjectID()));
        mainInvoicing.setClientContactID(requestObject.getContactID());
        if (invoice.getProjects().size() == 1) {
            EdsProject project = invoice.getProjects().iterator().next();
            ArrayList<NewInvoice> dueAmounts = (ArrayList<NewInvoice>) invoiceManager.getInvoiceDueAmountsByProjectId(project.getObjectID(), invoice.getObjectID(), mainInvoicing.getPeriodStart().getNonConvertedDate(), mainInvoicing.getPeriodEnd().getNonConvertedDate());
            ArrayList<PaymentItem> prepayments = (ArrayList<PaymentItem>) invoicePaymentManager.getInvoicePaymentsByProject(project.getObjectID(), mainInvoicing.getInvoiceDate().getNonConvertedDate(), mainInvoicing.getDueDate().getNonConvertedDate());
            mainInvoicing.setSameProjectInvoices(dueAmounts);
            mainInvoicing.setProjectPrepayments(prepayments);
        }
        return super.buildPdfDocumentCustomise(mainInvoicing, company, hasPhantom);
    }
}
