package com.edatasite.workforce.gwt.core.server.servlets.pdf.payroll;

import com.edatasite.workforce.core.domain.EdsCompany;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.payrolluk.EdsAdditionalPayment;
import com.edatasite.workforce.core.domain.payrolluk.EdsEmployeePayrollSettings;
import com.edatasite.workforce.gwt.core.client.rpc.RequestObject;
import com.edatasite.workforce.gwt.core.client.rpc.form.CustomFormConstants;
import com.edatasite.workforce.gwt.core.client.ui.view.PaymentDeductionObject;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.db.payroll.EmployeePayrollSettingsManager;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.PdfReferenceCodeNameEnum;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.localization.PdfLocalizationName;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.CellData;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.ITextGenericPdfData;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.ITextTableList;
import com.edatasite.workforce.gwt.payroll.client.rpc.AdditionalPayment;
import com.lowagie.text.Document;
import com.lowagie.text.pdf.PdfWriter;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * Created by Shohruh on 06 Dec 2016.
 */
public class AdditionalPaymentItemPdfHandler extends AdditionalPaymentPdfHandler {

    @Autowired
    private EmployeePayrollSettingsManager employeePayrollSettingsManager;

    @Override
    protected void initTableColumns() {
        topColumns = 5;
        centerColumns = byCommission ? 4 : 2;
        bottomColumns = 2;
        TABLE_WIDTH = byCommission ? 450 : 350;
    }

    @Override
    public ITextGenericPdfData buildPdfDocument(Object dataClass, Document document, PdfWriter writer) throws IOException {
        /*RequestObject requestObject = (RequestObject) dataClass;
        ListingFilterParameter fp = new ListingFilterParameter();
        fp.setObjectId(requestObject.getObjectID());
        fp.setEmployeeId(requestObject.getUserID());
        this.additionalPayment = payrollService.getAdditionalPaymentData(fp);
        byCommission = additionalPayment.getByCommission();
        currencySymbol = additionalPayment.getCurrency() != null ? additionalPayment.getCurrency().getName() : getCompanyCurrencySymbol();

        ITextGenericPdfData pdfData = new ITextGenericPdfData();
        pdfData.setPdfViewType(ITextPdfViewTypeEnum.SUMMARYVIEW);

        ITextSummaryView summaryView = new ITextSummaryView();
        summaryView.setFontName(ITextFontTypeEnum.DEJAVUSANS_BOLD.getName());

        addSpace(summaryView);
        initTableColumns();

        ITextTableList title = new ITextTableList(1);
        title.setTableAlignment(Element.ALIGN_LEFT);
        CellData cellData = new CellData(commonLocalizer.localizeAccounting(AdditionalPaymentItem.PAYMENT.equals(additionalPayment.getCategoryType())
                ? PdfLocalizationName.additionalPayment
                : PdfLocalizationName.additionalDeduction), Element.ALIGN_LEFT);
        cellData.setPadding(3, 50, 0, 20);
        cellData.setFont(FontFactory.getFont(ITextFontTypeEnum.DEJAVUSANS.getName(), BaseFont.IDENTITY_H, 11, com.lowagie.text.Font.BOLD));
        title.addPdfTableRows(cellData);
        title.setBorderWidth(0);

        ITextTableList top = new ITextTableList(3);
        top.setTableAlignment(Element.ALIGN_RIGHT);
        top.setTotalWidth(350);
        top.setCellPadding(1.5f);
        top.setBorderWidth(0);

        ITextTableList center = new ITextTableList(centerColumns);
        center.setTotalWidth(TABLE_WIDTH);

        ITextTableList bottom = new ITextTableList(bottomColumns);
        bottom.setTotalWidth(TABLE_WIDTH);

        ITextTableList employeeTable = new ITextTableList(3);
        employeeTable.setTableAlignment(Element.ALIGN_RIGHT);
        employeeTable.setTotalWidth(350);
        employeeTable.setCellPadding(1.5f);
        employeeTable.setBorderWidth(0);

        titlePanel(employeeTable, additionalPayment);
        summaryView.addTable(title, employeeTable);

        topPanel(top, additionalPayment);
        summaryView.addTable(top);

        centerPanel(center, additionalPayment);
        summaryView.addTable(center);

        bottomPanel(bottom, additionalPayment);
        summaryView.addTable(bottom);

        pdfData.setSummaryView(summaryView);

        return pdfData;*/
        return null;
    }

    @Override
    protected void titlePanel(ITextTableList employeeTable, AdditionalPayment additionalPayment) {
        EdsEmployeePayrollSettings wpsSettings = null;
        if (additionalPayment.getEmployee() != null && additionalPayment.getEmployee().getId() != null) {
            wpsSettings = employeePayrollSettingsManager.getEmployeeSettingValue(additionalPayment.getEmployee().getId(), CustomFormConstants.WPS_NUMBER);
        }

        employeeTable.addPdfTableRows(createHeader("Employee"),
                createHeader("Employee Code"),
                createHeader("WPS No."));
        employeeTable.addPdfTableRows(additionalPayment.getEmployee() != null ? createCell(additionalPayment.getEmployee().getName()) : createCell("n/a"),
                additionalPayment.getEmployee() != null ? createCell(additionalPayment.getEmployee().getDescription()) : createCell("n/a"),
                wpsSettings != null && !"".equals(wpsSettings.getValue()) ? createCell(wpsSettings.getValue()) : createCell("n/a"));

        employeeTable.addPdfTableRows(createCell(""), createCell(""), createCell(""));
        employeeTable.addPdfTableRows(createCell(""), createCell(""), createCell(""));
        employeeTable.addPdfTableRows(createCell(""), createCell(""), createCell(""));
        employeeTable.addPdfTableRows(createCell(""), createCell(""), createCell(""));

        CellData[] topHeaders = new CellData[3];
        topHeaders[0] = createHeader(commonLocalizer.localizeAccounting(PdfLocalizationName.period));
        topHeaders[1] = createHeader(commonLocalizer.localizeAccounting(PdfLocalizationName.approver));
        topHeaders[2] = createHeader(commonLocalizer.localizeAccounting(PdfLocalizationName.group));
        employeeTable.addPdfTableRows(topHeaders);

        CellData[] topRows = new CellData[3];
        if (ServerUtils.getUserLocale().getLanguage().equals("uz")) {
            topRows[0] = createCell(ServerUtils.convertToUzbDateFormat(additionalPayment.getPeriod()));
        } else {
            topRows[0] = createCell(additionalPayment.getPeriod());
        }
        topRows[1] = createCell(additionalPayment.getCurrentApprover() != null ? additionalPayment.getCurrentApprover().toString() : "N/A");
        topRows[2] = createCell(additionalPayment.getPayrollBatch() != null ? additionalPayment.getPayrollBatch().getName() : "All Employees");
        employeeTable.addPdfTableRows(topRows);
    }

    @Override
    protected void topPanel(ITextTableList top, AdditionalPayment additionalPayment) {
        CellData[] topHeaders = new CellData[3];
        topHeaders[0] = createHeader(commonLocalizer.localizeAccounting(PdfLocalizationName.paymentMethod));
        topHeaders[1] = createHeader(commonLocalizer.localizeAccounting(PdfLocalizationName.reference));
        topHeaders[2] = createHeader(commonLocalizer.localizeAccounting(PdfLocalizationName.employeePosition));
        top.addPdfTableRows(topHeaders);

        CellData[] topRows = new CellData[3];
        topRows[0] = createCell(additionalPayment.getEmployeePayMethod() != null ? additionalPayment.getEmployeePayMethod() : "n/a");
        topRows[1] = createCell(additionalPayment.getReference() != null ? additionalPayment.getReference() : "n/a");
        topRows[2] = createCell(additionalPayment.getEmployeePosition() != null ? additionalPayment.getEmployeePosition() : "n/a");
        top.addPdfTableRows(topRows);
        top.setAfterSpacing(25);
    }

    @Override
    protected void centerPanel(ITextTableList center, AdditionalPayment additionalPayment) {
        boolean byCommission = additionalPayment.getByCommission();
        if (byCommission) {
            center.addPdfTableHeader(
                    createHeader(commonLocalizer.localizeAccounting(PdfLocalizationName.payment)),
                    createNumberHeader(commonLocalizer.localizeAccounting(PdfLocalizationName.salesAmount)),
                    createNumberHeader(commonLocalizer.localizeAccounting(PdfLocalizationName.commission)),
                    createNumberHeader(commonLocalizer.localizeAccounting(PdfLocalizationName.amount)));

        } else {
            center.addPdfTableHeader(
                    createHeader(commonLocalizer.localizeAccounting(PdfLocalizationName.payment)),
                    createNumberHeader(commonLocalizer.localizeAccounting(PdfLocalizationName.amount)));

        }
        for (PaymentDeductionObject item : additionalPayment.getItems()) {
            CellData[] datas = new CellData[centerColumns];
            if (byCommission) {
                datas[0] = createCell(item.getCategoryItem().getName());
                datas[1] = createNumberCell(formatCurrency(item.getTotalAmount(), currencySymbol));
                datas[2] = createNumberCell(formatCurrency(item.getCommission(), null));
                datas[3] = createNumberCell(formatCurrency(item.getPaymentAmount(), currencySymbol));
            } else {
                datas[0] = createCell(item.getCategoryItem().getName());
                datas[1] = createNumberCell(formatCurrency(item.getPaymentAmount(), currencySymbol));
            }
            center.addPdfTableRows(datas);
        }
    }

    @Override
    protected ITextGenericPdfData buildPdfDocumentCustomise(Object dataClass, EdsCompany company, boolean hasPhantom) {
        return super.buildPdfDocumentCustomise(dataClass, company, hasPhantom);
    }

    @Override
    protected void setFileName(EdsUser user, Object dataClass) {
        RequestObject requestObject = (RequestObject) dataClass;
        EdsAdditionalPayment additionalPayment = additionalPaymentManager.get(requestObject.getObjectID());
        setFileName("Single_" + additionalPayment.getReference());
    }

    @Override
    protected String getTableName(Object dataClass) {
        return payrollLocalizer.localize(PdfLocalizationName.singlePayment, "Single Payment");
    }

    @Override
    protected PdfReferenceCodeNameEnum getPdfCodeName(Object dataClass) {
        return PdfReferenceCodeNameEnum.SINGLE_PAYMENT;
    }

    @Override
    protected Integer getCustomisedPDFTemplateId(Object object) {
        return super.getCustomisedPDFTemplateId(object);
    }

    @Override
    protected Object getDataClass(HttpServletRequest request) {
        return super.getDataClass(request);
    }


}
