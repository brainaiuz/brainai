package com.edatasite.workforce.gwt.core.server.servlets.pdf.invoice;

import com.edatasite.workforce.core.domain.EdsCompany;
import com.edatasite.workforce.core.domain.EdsInvoicingSettings;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.accounting.EdsInvoice;
import com.edatasite.workforce.core.domain.accounting.EdsSaleInvoice;
import com.edatasite.workforce.core.tools.StringUtil;
import com.edatasite.workforce.gwt.core.client.rpc.RequestObject;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.PdfReferenceCodeNameEnum;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.localization.PdfLocalizationName;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.ITextBaseInvoice;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.ITextGenericPdfData;
import com.edatasite.workforce.gwt.invoice.client.rpc.NewInvoice;
import com.lowagie.text.Document;
import com.lowagie.text.pdf.PdfWriter;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: admin
 * Date: Jul 18, 2009
 * Time: 3:53:22 PM
 * To change this template use File | Settings | File Templates.
 */
public class SalesReceiptViewPDFHandler extends SaleInvoiceViewPDFHandler {

    @Override
    public boolean prepareRequest(HttpServletRequest request) {
        return true;
    }

    @Override
    public String getFileName() {
        return SR_FILE_NAME;
    }

    @Override
    protected Map<String, String> getFileNameParams(Integer objectID) {
        Map<String, String> map = new HashMap<>();
        EdsInvoice invoice = invoiceManager.get(objectID);
        map.put(PDF_CLIENT, invoice.getClientOrSupplier().getName());
        map.put(PDF_CLIENT_CODE, invoice.getClientOrSupplier().getNumber());
        map.put(PDF_NUMBER, invoice.getNumber());
        return map;
    }

    @Override
    protected String getFromInvoice() {
        return SALES_RECEIPT;
    }

    @Override
    public ITextGenericPdfData buildPdfDocument(Object dataClass, Document document, PdfWriter writer) throws IOException {
        RequestObject requestObject = (RequestObject) dataClass;
        EdsInvoice invoice = invoiceManager.get(requestObject.getObjectID());
        NewInvoice mainInvoicing = EdsInvoice.getInvoiceData(invoice);
        ITextGenericPdfData pdfData = super.buildPdfDocument(mainInvoicing, document, writer);
        ITextBaseInvoice baseInvoice = pdfData.getBaseInvoice();
        EdsUser edsUser;
        if (mainInvoicing.getUserID() != null) {
            edsUser = userManager.get(mainInvoicing.getUserID());
        } else {
            edsUser = invoiceManager.getUser();
        }
        HashMap<String, String> numDateTableRowKeys = new HashMap<>();
        numDateTableRowKeys.put(RECEIPT_NO, pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.receiptNo));
        numDateTableRowKeys.put(RECEIPT_DATE, pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.receiptDate));
        numDateTableRowKeys.put(PAYMENT_DATE, pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.paymentDate));
        numDateTableRowKeys.put(REFERENCE, pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.reference));

        baseInvoice.setNumberAndDatesTable(getNumberAndDatesTableData(mainInvoicing, edsUser, numDateTableRowKeys/*numDatesColumns, addRowNumDates*/));
        return pdfData;
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

        if (invoicingSettings != null && !StringUtil.isEmpty(invoicingSettings.getSalesReceiptPdfNamingFormat())) {
            final Map<String, String> params = this.getFileNameParams(invoiceId);
            final String pdfPrefix = invoicingSettings.getSalesReceiptPdfNamingPrefix();

            if (!StringUtil.isEmpty(pdfPrefix)) {
                params.put(PDF_PREFIX, pdfPrefix);
            }
            params.put(PDF_COMPANY_NAME, escapeHtml(edsUser.getCompany().getName()));
            params.put(PDF_GENERATED_DATE, escapeHtml(dateFormat.format(edsUser.getUserDate())));
            params.put(PDF_USER_NAME, escapeHtml(edsUser.getName()));
            params.put(PDF_TYPE, getFileName());
            final String[] format = invoicingSettings.getSalesReceiptPdfNamingFormat().split("_");

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

    @Override
    protected ITextGenericPdfData buildPdfDocumentCustomise(Object dataClass, EdsCompany company, boolean hasPhantom) {

        RequestObject requestObject = (RequestObject) dataClass;
        EdsInvoice invoice = invoiceManager.get(requestObject.getObjectID());
        NewInvoice mainInvoicing = EdsInvoice.getInvoiceData(invoice);
        if (invoice instanceof EdsSaleInvoice) {
            EdsSaleInvoice saleInvoice = (EdsSaleInvoice) invoice;
            mainInvoicing.setInvoiceType(saleInvoice.getInvoiceType());
        }
        ITextGenericPdfData pdfData = super.buildPdfDocumentCustomise(mainInvoicing, company, hasPhantom);
        ITextBaseInvoice baseInvoice = pdfData.getBaseInvoice();
        EdsUser edsUser;
        if (mainInvoicing.getUserID() != null) {
            edsUser = userManager.get(mainInvoicing.getUserID());
        } else {
            edsUser = invoiceManager.getUser();
        }

        String[] numDatesColumns = {RECEIPT_NO,
                                    RECEIPT_DATE,
                                    PAYMENT_DATE,
                                    QT_NUMBER,
                                    PO_NUMBER,
                                    REFERENCE,
                                    INV_DATE,
                                    INV_DUE_DATE,
                                    PERIOD,
                                    INVOICE_STATUS,
                INV_NUMBER,
                "CLIENT_VAT_NUMBER_LABEL"};
        String[] numAndDatesLabels = {pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.receiptNo),
                                      pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.receiptDate),
                                      pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.paymentDate),
                                      pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.quoteNumber),
                                      pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.poNumber),
                                      pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.reference),
                                      commonLocalizer.localize(PdfLocalizationName.invoiceDate),
                                      pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.dueDate),
                                      pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.period),
                                      pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.invoiceStatus),
                pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.invoiceNo),
                pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.vatNumberLabel)};
        baseInvoice.setCustomNumberAndDatesTable(getCustomNumberAndDatesTable(mainInvoicing, edsUser, numDatesColumns, numAndDatesLabels));

        return pdfData;
    }

    @Override
    protected Object getDataClass(HttpServletRequest request) {
        return new RequestObject();
    }

    @Override
    protected PdfReferenceCodeNameEnum getPdfCodeName(Object dataClass) {
        return PdfReferenceCodeNameEnum.SALES_RECEIPT;
    }

    @Override
    protected String getTableName(Object dataClass) {
        return accountingLocalizer.localizeAccounting(PdfLocalizationName.salesReceipt);
    }
}
