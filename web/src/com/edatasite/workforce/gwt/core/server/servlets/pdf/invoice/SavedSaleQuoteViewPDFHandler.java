package com.edatasite.workforce.gwt.core.server.servlets.pdf.invoice;

import com.edatasite.workforce.core.domain.EdsCompany;
import com.edatasite.workforce.core.domain.accounting.*;
import com.edatasite.workforce.gwt.core.client.rpc.CompanyCustomFieldItem;
import com.edatasite.workforce.gwt.core.client.rpc.FileItem;
import com.edatasite.workforce.gwt.core.client.rpc.itemtablesettings.ItemTableEnum;
import com.edatasite.workforce.gwt.core.client.ui.customfields.ViewName;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.ITextGenericPdfData;
import com.edatasite.workforce.gwt.core.server.utils.CustomFieldsUtils;
import com.edatasite.workforce.gwt.documents.client.rest.resource.FileResource;
import com.edatasite.workforce.gwt.invoice.client.rpc.InvoiceQuoteRequestObject;
import com.edatasite.workforce.gwt.invoice.client.rpc.NewInvoice;
import com.edatasite.workforce.gwt.invoice.client.rpc.NewInvoiceItem;
import com.lowagie.text.Document;
import com.lowagie.text.pdf.PdfWriter;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: admin
 * Date: Jun 10, 2009
 * Time: 1:09:09 PM
 * To change this template use File | Settings | File Templates.
 */
public class SavedSaleQuoteViewPDFHandler extends SaleQuoteViewPDFHandler {

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
        EdsQuote quote = quoteManager.get(requestObject.getObjectID());
        ArrayList<CompanyCustomFieldItem> itemCustomFields = commonServiceLocal.getCompanyCustomFields(ViewName.SaleQuoteItem);
        quote.setItemCustomFields(CustomFieldsUtils.setRPCCustomFieldItems(null, itemCustomFields));

        NewInvoice mainInvoicing = EdsQuote.getQuoteData(quote);
        mainInvoicing.setCustomItemColumns(itemTableSettingService.getColumnConfigs(ItemTableEnum.SALE_QUOTE_ITEM));

        EdsBankAccount bankAccount = quote.getBankAccount();
        mainInvoicing.setBankAccount(bankAccount != null ? bankAccount.getAsSelectItem() : null);
        if (quote instanceof EdsSaleQuote) {
            EdsShippingMethod shippMethod = ((EdsSaleQuote) quote).getShippingMethod();
            mainInvoicing.setShippingMethodID(shippMethod != null ? shippMethod.getObjectID() : null);
            mainInvoicing.setTotalDiscount(quote.getTotalDiscount());
            mainInvoicing.setShippingPrice(((EdsSaleQuote) quote).getShippingAmount());
            mainInvoicing.setIntroduction(quote.getIntroduction());
        }
        mainInvoicing.setClientContactID(requestObject.getContactID());
        return super.buildPdfDocument(mainInvoicing, document, writer);
    }

    @Override
    protected ITextGenericPdfData buildPdfDocumentCustomise(Object dataClass, EdsCompany company, boolean hasPhantom) {
        InvoiceQuoteRequestObject requestObject = (InvoiceQuoteRequestObject) dataClass;
        EdsQuote quote = quoteManager.get(requestObject.getObjectID());
        ArrayList<CompanyCustomFieldItem> itemCustomFields = commonServiceLocal.getCompanyCustomFields(ViewName.SaleQuoteItem);
        quote.setItemCustomFields(CustomFieldsUtils.setRPCCustomFieldItems(null, itemCustomFields));
        NewInvoice mainInvoicing = EdsQuote.getQuoteData(quote);
        for (NewInvoiceItem qItem : mainInvoicing.getItems()) {
            List<FileResource> attachments = attachmentUtilsManager.getAttachments(F_SALE_QUOTE_ITEM, qItem.getID(), qItem.getID(), crmAccountManager.getUser());
            if (attachments != null && !attachments.isEmpty()) {
                for (FileResource fileResource : attachments) {
                    FileItem fileItem = new FileItem();
                    fileItem.setId(fileResource.getObjectId());
                    fileItem.setFileName(fileResource.getFileName());
                    fileItem.setDescription(fileResource.getDescription());
                    fileItem.setDate(fileResource.getCreationDate());
                    fileItem.setContentType(fileResource.getContentType());
                    fileItem.setSize(fileResource.getContentLength());
                    fileItem.setUploadType(fileResource.getUploadType());
                    fileItem.setAmazonLink(fileResource.getAmazonLink());
                    fileItem.setGoogleDocumentLink(fileResource.getGoogleDownloadLink());
                    fileItem.setOfficeDocumentLink(fileResource.getOfficeDownloadLink());
                    qItem.getAttachments().add(fileItem);
                }
            }
        }
        mainInvoicing.setCustomItemColumns(itemTableSettingService.getColumnConfigs(ItemTableEnum.SALE_QUOTE_ITEM));
        mainInvoicing.setPdfTemplateID(requestObject.getTemplateID());
        EdsBankAccount bankAccount = quote.getBankAccount();
        mainInvoicing.setBankAccount(bankAccount != null ? bankAccount.getAsSelectItem() : null);
        if (quote instanceof EdsSaleQuote) {
            mainInvoicing.setTotalDiscount(quote.getTotalDiscount());
            mainInvoicing.setIntroduction(quote.getIntroduction());
            EdsShippingMethod shippMethod = ((EdsSaleQuote) quote).getShippingMethod();
            mainInvoicing.setShippingMethodID(shippMethod != null ? shippMethod.getObjectID() : null);
            mainInvoicing.setShippingPrice(((EdsSaleQuote) quote).getShippingAmount());
            mainInvoicing.setOpportunityID(((EdsSaleQuote) quote).getOpportunityID());
            if (((EdsSaleQuote) quote).getInvoiceTerms() != null) {
                mainInvoicing.setInvoiceTermsItem(((EdsSaleQuote) quote).getInvoiceTerms().getAsRPC());
            }
            if (quote.getCurrentApprover() != null && quote.getCurrentApprover().getExactEmployee() != null) {
                mainInvoicing.setCurrentApproverSelectItem(quote.getCurrentApprover().getExactEmployee().getAsSelectItem());
            }
            mainInvoicing.setProgressInvoicing(((EdsSaleQuote) quote).isProgressInvoicing());
            if (mainInvoicing.isProgressInvoicing()) {
                mainInvoicing.setInvoicedAmount(invoiceManager.getConvertedInvoiceAmount(quote.getObjectID(), null));
            }
            if (quote.getInvoices() != null && !quote.getInvoices().isEmpty()) {
                for (EdsInvoice edsInvoice : quote.getInvoices()) {
                    if (edsInvoice != null && edsInvoice.getInvoiceItems() != null && !edsInvoice.getInvoiceItems().isEmpty()) {
                        NewInvoice item = new NewInvoice();
                        item.setID(edsInvoice.getObjectID());
                        item.setInvoiceNumber(edsInvoice.getNumber());
                        ArrayList<NewInvoiceItem> items = new ArrayList<>();
                        for (EdsInvoiceItem invoiceItem : edsInvoice.getInvoiceItems()) {
                            NewInvoiceItem item1 = new NewInvoiceItem();
                            item1.setID(invoiceItem.getQuoteItemId());
                            item1.setQuantity(invoiceItem.getQty());
                            items.add(item1);
                        }
                        item.setItems(items.toArray(new NewInvoiceItem[]{}));
                        mainInvoicing.getConvertedInvoices().add(item);
                    }
                }
            }
            mainInvoicing.setTaxCalculationType(quote.getTaxCalculationType());
        }
        mainInvoicing.setCustomFieldItems(CustomFieldsUtils.setRPCCustomFieldItems(quote.getCustomFields(), commonService.getCompanyCustomFields(ViewName.SaleQuote)));
        return super.buildPdfDocumentCustomise(mainInvoicing, company, hasPhantom);
    }
}
