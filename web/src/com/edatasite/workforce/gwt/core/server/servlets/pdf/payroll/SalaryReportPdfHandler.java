package com.edatasite.workforce.gwt.core.server.servlets.pdf.payroll;

import com.edatasite.workforce.core.domain.EdsCompany;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.accounting.EdsFinancialSettings;
import com.edatasite.workforce.core.domain.settings.EdsCompanySettings;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.db.UserManager;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.AbstractITextPostPdfHandler;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.PDFConstants;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.localization.PdfLocalizationName;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.ITextFontTypeEnum;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.ITextPdfViewTypeEnum;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.CellData;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.ITextGenericPdfData;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.ITextTableList;
import com.edatasite.workforce.gwt.payroll.client.rpc.PayrollService;
import com.edatasite.workforce.gwt.payroll.client.rpc.SalaryReportData;
import com.edatasite.workforce.gwt.payroll.client.rpc.SalaryReportItem;
import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfWriter;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: Bunyod Xalilov
 * Date: 4/5/16
 * Time: 6:04 PM
 * To change this template use File | Settings | File Templates.
 */
public class SalaryReportPdfHandler extends AbstractITextPostPdfHandler implements  PDFConstants {

    @Autowired
    private PayrollService payrollService;

    @Autowired
    private UserManager userManager;

    @Override
    public ITextGenericPdfData buildPdfDocument(Object dataClass, Document document, PdfWriter writer) throws IOException {
        return null;
    }

    @Override
    protected ITextGenericPdfData buildPdfDocumentCustomise(Object dataClass, EdsCompany company, boolean hasPhantom) {
        SimpleDateFormat format = new SimpleDateFormat("MMMM d, yyyy");
        EdsFinancialSettings fs = financialSettingsManager.getFinancialSettings();
        DecimalFormat priceScaleNumberFormat = getPriceScaleNumberFormat(fs);
        ListingFilterParameter lfp = (ListingFilterParameter) dataClass;

        EdsCompanySettings companySettings = userManager.getUser().getCompany().getCompanySettings();
        if (companySettings != null && companySettings.getExcelLimit() != null && !"".equals(companySettings.getExcelLimit())) {
            lfp.setLimit(Integer.parseInt(companySettings.getExcelLimit()));
        } else {
            lfp.setLimit(LIMIT_PDF_ROWS);
        }
        Date startDate = parseFilterParameterDate(lfp.getStartDateNC());
        Date endDate = parseFilterParameterDate(lfp.getEndDateNC());
        SalaryReportData data = payrollService.getSalarReportData(lfp);
        ITextGenericPdfData pdfData = new ITextGenericPdfData();
        pdfData.setPdfViewType(ITextPdfViewTypeEnum.LISTTABLE);
        ITextTableList table = new ITextTableList(10);
        pdfData.setListTable(table);

        table.addPdfTableHeader(
                drawHeader(commonLocalizer.localize(PdfLocalizationName.employee), Element.ALIGN_LEFT),
                drawHeader(commonLocalizer.localize(PdfLocalizationName.month), Element.ALIGN_CENTER),
                drawHeader(commonLocalizer.localize(PdfLocalizationName.currency), Element.ALIGN_CENTER),
                drawHeader(commonLocalizer.localize(PdfLocalizationName.salaryAmount), Element.ALIGN_RIGHT),
                drawHeader(pdfWfmMessageSource.localize("allowance"), Element.ALIGN_RIGHT),
                drawHeader(pdfWfmMessageSource.localize("expensePayment"), Element.ALIGN_RIGHT),
                drawHeader(pdfWfmMessageSource.localize("expenseDeduction"), Element.ALIGN_RIGHT),
                drawHeader(commonLocalizer.localize(PdfLocalizationName.pension), Element.ALIGN_RIGHT),
                drawHeader(commonLocalizer.localize(PdfLocalizationName.deductions), Element.ALIGN_RIGHT),
                drawHeader(commonLocalizer.localize("totalPaid"), Element.ALIGN_RIGHT));

        BigDecimal basicSalaryTotal = BigDecimal.ZERO, allowanceTotal = BigDecimal.ZERO, expPaymentTotal = BigDecimal.ZERO, expDeductionTotal = BigDecimal.ZERO,
                pensionTotal = BigDecimal.ZERO, deductionTotal = BigDecimal.ZERO, overallTotal = BigDecimal.ZERO;
        for (SalaryReportItem item : data.getSalaryReportItems()) {
            String employeeNumber = item.getEmployeeCode() != null && !"".equals(item.getEmployeeCode()) ? item.getEmployeeCode() + " - " : "";
            CellData employeeCell = new CellData(employeeNumber + (item.getEmployeeName() != null ? item.getEmployeeName() : ""), Element.ALIGN_LEFT);
            CellData currency = new CellData(item.getCurrency() != null ? item.getCurrency() : "n/a", Element.ALIGN_CENTER);
            CellData month = new CellData(item.getMonth() != null ? item.getMonth() + " " + escapeHtml(item.getYear()) : "n/a", Element.ALIGN_CENTER);
            CellData basicSalary = createCell(item.getBasicSalary(), priceScaleNumberFormat);
            CellData allowance = createCell(item.getAllowance(), priceScaleNumberFormat);
            CellData expPayment = createCell(item.getExpensePayment(), priceScaleNumberFormat);
            CellData expDeduction = createCell(item.getExpenseDeduction(), priceScaleNumberFormat);
            CellData pension = createCell(item.getPensionAmount(), priceScaleNumberFormat);
            CellData deductions = createCell(item.getDeduction(), priceScaleNumberFormat);
            CellData totalPaid = createCell(item.getTotal(), priceScaleNumberFormat);
            table.addPdfTableRows(employeeCell, month, currency, basicSalary, allowance, expPayment, expDeduction, pension, deductions, totalPaid);
            basicSalaryTotal = basicSalaryTotal.add(item.getBasicSalary() != null ? item.getBasicSalary() : BigDecimal.ZERO);
            allowanceTotal = allowanceTotal.add(item.getAllowance() != null ? item.getAllowance() : BigDecimal.ZERO);
            expPaymentTotal = expPaymentTotal.add(item.getExpensePayment() != null ? item.getExpensePayment() : BigDecimal.ZERO);
            expDeductionTotal = expDeductionTotal.add(item.getExpenseDeduction() != null ? item.getExpenseDeduction() : BigDecimal.ZERO);
            pensionTotal = pensionTotal.add(item.getPensionAmount() != null ? item.getPensionAmount() : BigDecimal.ZERO);
            deductionTotal = deductionTotal.add(item.getDeduction() != null ? item.getDeduction() : BigDecimal.ZERO);
            overallTotal = overallTotal.add(item.getTotal() != null ? item.getTotal() : BigDecimal.ZERO);
        }
        table.addPdfTableRows(
                new CellData(commonLocalizer.localize(PdfLocalizationName.total), Element.ALIGN_LEFT),
                new CellData("", Element.ALIGN_LEFT),
                new CellData("", Element.ALIGN_LEFT),
                createCell(basicSalaryTotal, priceScaleNumberFormat),
                createCell(allowanceTotal, priceScaleNumberFormat),
                createCell(expPaymentTotal, priceScaleNumberFormat),
                createCell(expDeductionTotal, priceScaleNumberFormat),
                createCell(pensionTotal, priceScaleNumberFormat),
                createCell(deductionTotal, priceScaleNumberFormat),
                createCell(overallTotal, priceScaleNumberFormat));

        table.addTableWidthPercentage(2f, 2f, 2f, 2f, 2f, 2f, 2f, 2f, 2f, 2f);
        return pdfData;
    }

    private CellData drawHeader(String name, Integer alignment) {
        CellData nameCell = new CellData(name, alignment);
        nameCell.setFont(createFont(9, true));
        return nameCell;
    }

    private Font createFont(Integer fontSize, boolean bold) {
        return FontFactory.getFont(ITextFontTypeEnum.TIMES_NEW_ROMAN.getName(), BaseFont.IDENTITY_H, fontSize, bold ? Font.BOLD : Font.NORMAL);
    }

    private CellData createCell(BigDecimal value, DecimalFormat numberFormat) {
        return new CellData(getValueAsString(value, numberFormat), Element.ALIGN_RIGHT);
    }

    private String getValueAsString(BigDecimal value, DecimalFormat numberFormat) {
        if (value != null) {
            if (value.compareTo(BigDecimal.ZERO) >= 0) {
                return " " + numberFormat.format(value);
            } else {
                return "(" + numberFormat.format(value.abs()) + ")";
            }
        }
        return " ";
    }

    @Override
    protected boolean prepareRequest(HttpServletRequest request) {
        return false;
    }


    @Override
    protected void setFileName(EdsUser user, Object dataClass) {
        super.setFileName("SalaryReport");
    }

    @Override
    protected boolean isListingPDF() {
        return true;
    }

    @Override
    protected String getTableName(Object dataClass) {
        ListingFilterParameter lfp = (ListingFilterParameter) dataClass;
        SimpleDateFormat format = new SimpleDateFormat("MMMM d, yyyy");
        Date startDate = parseFilterParameterDate(lfp.getStartDateNC());
        Date endDate = parseFilterParameterDate(lfp.getEndDateNC());
        if (ServerUtils.getUserLocale().getLanguage().equals("uz")) {
            return pdfWfmMessageSource.localize("salaryReport") + " " + commonLocalizer.localize(PdfLocalizationName.from) + " "
                    + ServerUtils.convertToUzbDateFormat(format.format(startDate)) +
                    " " + commonLocalizer.localize(PdfLocalizationName.to) + " " + ServerUtils.convertToUzbDateFormat(format.format(endDate));
        } else {
            return pdfWfmMessageSource.localize("salaryReport") + " " + commonLocalizer.localize(PdfLocalizationName.from) + " "
                    + format.format(startDate) + " " + commonLocalizer.localize(PdfLocalizationName.to) + " " + format.format(endDate);
        }
    }
}
