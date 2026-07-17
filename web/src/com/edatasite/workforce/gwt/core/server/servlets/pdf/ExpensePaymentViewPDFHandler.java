package com.edatasite.workforce.gwt.core.server.servlets.pdf;

import com.edatasite.workforce.core.domain.EdsCompany;
import com.edatasite.workforce.core.domain.EdsExpensePayment;
import com.edatasite.workforce.core.domain.EdsExpenseReport;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.gwt.accounting.client.ui.AccountingConstants;
import com.edatasite.workforce.gwt.core.client.rpc.CommonService;
import com.edatasite.workforce.gwt.core.client.rpc.RequestObject;
import com.edatasite.workforce.gwt.core.server.db.ExpensePaymentManager;
import com.edatasite.workforce.gwt.core.server.db.ExpenseReportManager;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.localization.PdfLocalizationName;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.ITextPdfViewTypeEnum;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.CustomisedITextTable;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.ITextBaseInvoice;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.ITextGenericPdfData;
import com.lowagie.text.Document;
import com.lowagie.text.pdf.PdfWriter;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.text.SimpleDateFormat;

public class ExpensePaymentViewPDFHandler extends AbstractITextPostPdfHandler implements IPostPDFHandler, AccountingConstants, PDFConstants {

    @Autowired
    private ExpenseReportManager reportManager;
    @Autowired
    private ExpensePaymentManager expensePaymentManager;
    @Autowired
    protected CommonService commonService;

    @Override
    public ITextGenericPdfData buildPdfDocument(Object dataClass, Document document, PdfWriter writer) throws IOException {
        return null;
    }

    @Override
    protected ITextGenericPdfData buildPdfDocumentCustomise(Object dataClass, EdsCompany company, boolean hasPhantom) {

        RequestObject requestObject = (RequestObject) dataClass;

        ITextGenericPdfData pdfData = new ITextGenericPdfData();
        pdfData.setPdfViewType(ITextPdfViewTypeEnum.BASEINVOICE);
        ITextBaseInvoice baseInvoice = new ITextBaseInvoice();
        pdfData.setBaseInvoice(baseInvoice);

        Integer paymentId = requestObject.getObjectID();
        EdsUser user = uploadManager.getUser();
        EdsExpensePayment expensePayment = expensePaymentManager.getPaymentByID(paymentId);
        EdsExpenseReport report = reportManager.getExpenseReport(expensePayment.getExpenseReport().getObjectID());


        pdfData.setCompanyData(getCompanyData(user.getCompany(), true, hasPhantom));

        SimpleDateFormat shortDateFormat = getCompanyShortDateFormat(user.getCompany());

        CustomisedITextTable reportTitle = new CustomisedITextTable();
        reportTitle.addColumnOrder(COLUMN_NAME, COLUMN_VALUE);
        reportTitle.addRowWithCode(TITLE, commonLocalizer.localizeAccounting(PdfLocalizationName.reportTitle), escapeHtml(report.getTitle()));
        reportTitle.addRowWithCode(REFERENCE, commonLocalizer.localizeAccounting(PdfLocalizationName.expenseNumber, "Expense Number") + " ", report.getNumber() != null ? escapeHtml(report.getNumber()) : " ");
        reportTitle.addRowWithCode(PAID_FROM, commonLocalizer.localizeAccounting(PdfLocalizationName.paidFrom, "Paid From") + " ", expensePayment.getAccount() != null ? escapeHtml(expensePayment.getAccount().getName()) : " ");
        reportTitle.addRowWithCode(PAID, commonLocalizer.localizeAccounting(PdfLocalizationName.paidAmount), (expensePayment.getAmount() != null ? String.valueOf(expensePayment.getAmount().setScale(2)) : ""));
        reportTitle.addRowWithCode(PAYMENT_DATE, commonLocalizer.localizeAccounting(PdfLocalizationName.date), shortDateFormat.format(expensePayment.getPaymentDate()));
        if (report.getSupplier() != null && report.getSupplier().getName() != null) {
            reportTitle.addRowWithCode(SUPPLIER, pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.supplier), (report.getSupplier() != null ? escapeHtml(report.getSupplier().getName()) : ""));
        }
        baseInvoice.setCustomBillToAddress(reportTitle);

        return pdfData;
    }

    @Override
    protected Object getDataClass(HttpServletRequest request) {
        RequestObject requestObject = new RequestObject();
        requestObject.setObjectID(Integer.valueOf(request.getParameter("objectID")));
        return requestObject;
    }

    @Override
    protected void setFileName(EdsUser user, Object dataClass) {
        RequestObject requestObject = (RequestObject) dataClass;
        Integer paymentId = requestObject.getObjectID();
        EdsExpensePayment expensePayment = expensePaymentManager.getPaymentByID(paymentId);
        EdsExpenseReport report = reportManager.getExpenseReport(expensePayment.getExpenseReport().getObjectID());

        setFileName("ExpensePayment- " + report.getNumber() != null ? report.getNumber() : " ");
    }

    @Override
    protected String getTableName(Object dataClass) {
        return accountingLocalizer.localize(PdfLocalizationName.expensePayment);
    }


    @Override
    protected PdfReferenceCodeNameEnum getPdfCodeName(Object dataClass) {
        return PdfReferenceCodeNameEnum.EXPENSE_PAYMENT_FORM;
    }
}
