package com.edatasite.workforce.gwt.core.server.servlets.pdf.invoice;

import com.edatasite.workforce.core.domain.EdsCompany;
import com.edatasite.workforce.core.domain.EdsInvoicingSettings;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.accounting.EdsBankAccount;
import com.edatasite.workforce.core.domain.accounting.EdsQuote;
import com.edatasite.workforce.core.domain.accounting.EdsSaleQuote;
import com.edatasite.workforce.core.domain.accounting.EdsShippingMethod;
import com.edatasite.workforce.core.tools.StringUtil;
import com.edatasite.workforce.gwt.core.client.rpc.CompanyCustomFieldItem;
import com.edatasite.workforce.gwt.core.client.rpc.FileItem;
import com.edatasite.workforce.gwt.core.client.rpc.RequestObject;
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
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: Bunyod Xalilov
 * Date: 2/11/12
 * Time: 4:09 PM
 * To change this template use File | Settings | File Templates.
 */
public class SavedSaleOrderViewPdfHandler extends SaleOrderViewPDFHandler {
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
        ArrayList<CompanyCustomFieldItem> itemCustomFields = commonServiceLocal.getCompanyCustomFields(ViewName.SaleOrderItem);
        quote.setItemCustomFields(CustomFieldsUtils.setRPCCustomFieldItems(null, itemCustomFields));
        NewInvoice mainInvoicing = EdsQuote.getQuoteData(quote);
        for (NewInvoiceItem qItem : mainInvoicing.getItems()) {
            List<FileResource> attachments = attachmentUtilsManager.getAttachments(F_SALE_QUOTE_ITEM, qItem.getID(), qItem.getID(), crmAccountManager.getUser());
            if (attachments != null && !attachments.isEmpty()) {
                FileResource fileResource = attachments.get(0);
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
        mainInvoicing.setCustomItemColumns(itemTableSettingService.getColumnConfigs(ItemTableEnum.SALE_ORDER_ITEM));
        mainInvoicing.setPdfTemplateID(requestObject.getTemplateID());

        EdsBankAccount bankAccount = quote.getBankAccount();
        mainInvoicing.setBankAccount(bankAccount != null ? bankAccount.getAsSelectItem() : null);

        if (quote instanceof EdsSaleQuote) {
            EdsShippingMethod shippMethod = ((EdsSaleQuote) quote).getShippingMethod();
            mainInvoicing.setShippingMethodID(shippMethod != null ? shippMethod.getObjectID() : null);
            mainInvoicing.setShippingPrice(((EdsSaleQuote) quote).getShippingAmount());
            mainInvoicing.setTotalDiscount(quote.getTotalDiscount());
            mainInvoicing.setIntroduction(quote.getIntroduction());
            if (((EdsSaleQuote) quote).getInvoiceTerms() != null) {
                mainInvoicing.setInvoiceTermsItem(((EdsSaleQuote) quote).getInvoiceTerms().getAsRPC());
            }
            mainInvoicing.setTaxCalculationType(quote.getTaxCalculationType());
        }
        mainInvoicing.setClientContactID(requestObject.getContactID());
        mainInvoicing.setCustomFieldItems(CustomFieldsUtils.setRPCCustomFieldItems(quote.getCustomFields(), commonService.getCompanyCustomFields(ViewName.SaleOrder)));
        mainInvoicing.setSalesOrder(isSalesOrder());
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

        if (invoicingSettings != null && !StringUtil.isEmpty(invoicingSettings.getSalesOrderPdfNamingFormat())) {
            final Map<String, String> params = this.getFileNameParams(invoiceId);
            final String pdfPrefix = invoicingSettings.getSalesOrderPdfNamingPrefix();

            if (!StringUtil.isEmpty(pdfPrefix)) {
                params.put(PDF_PREFIX, pdfPrefix);
            }
            params.put(PDF_COMPANY_NAME, escapeHtml(edsUser.getCompany().getName()));
            params.put(PDF_GENERATED_DATE, escapeHtml(dateFormat.format(edsUser.getUserDate())));
            params.put(PDF_USER_NAME, escapeHtml(edsUser.getName()));
            params.put(PDF_TYPE, getFileName());
            final String[] format = invoicingSettings.getSalesOrderPdfNamingFormat().split("_");

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
