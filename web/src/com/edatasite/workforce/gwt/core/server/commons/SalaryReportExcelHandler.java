package com.edatasite.workforce.gwt.core.server.commons;

import com.edatasite.shared.poiutils.WorkBook;
import com.edatasite.workforce.core.domain.accounting.EdsFinancialSettings;
import com.edatasite.workforce.core.domain.settings.EdsCompanySettings;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.db.UserManager;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.localization.PdfLocalizationName;
import com.edatasite.workforce.gwt.payroll.client.rpc.PayrollService;
import com.edatasite.workforce.gwt.payroll.client.rpc.SalaryReportData;
import com.edatasite.workforce.gwt.payroll.client.rpc.SalaryReportItem;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Bunyod Xalilov
 * Date: 4/5/16
 * Time: 6:03 PM
 * To change this template use File | Settings | File Templates.
 */
public class SalaryReportExcelHandler extends BaseExcelHandler {

    @Autowired
    private PayrollService payrollService;
    @Autowired
    private UserManager userManager;

    private SalaryReportData data;
    private List<ExcelData[]> list;
    private ListingFilterParameter lfp;
    private SimpleDateFormat format;
    private Integer calculationScale;

    @Override
    protected HSSFWorkbook getWorkBook(Object object) {
        lfp = (ListingFilterParameter) object;
        EdsCompanySettings companySettings = userManager.getUser().getCompany().getCompanySettings();
        if (companySettings != null && companySettings.getExcelLimit() != null && !"".equals(companySettings.getExcelLimit())) {
            lfp.setLimit(Integer.parseInt(companySettings.getExcelLimit()));
        } else {
            lfp.setLimit(LIMIT_EXCEL_ROW);
        }
        format = new SimpleDateFormat("MMMM d, yyyy");
        Date startDate = parseFilterParameterDate(lfp.getStartDateNC());
        Date endDate = parseFilterParameterDate(lfp.getEndDateNC());
        EdsFinancialSettings fs = financialSettingsManager.getFinancialSettings();
        calculationScale = getCalculationScale(fs);
        data = payrollService.getSalarReportData(lfp);

        list = new LinkedList<>();
        list.add(new ExcelData[]{
                new ExcelData("", ExcelData.STRING, 24, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL)
        });

        ExcelData reportTitle = ExcelData.getReportNameData(commonLocalizer.localize(PdfLocalizationName.salaryReport), 24, 10);
        ExcelData companyTitle = ExcelData.getReportNameChildData(userManager.getUser().getCompany().getName(), 24, 10);
        ExcelData dateTitle;
        if (ServerUtils.getUserLocale().getLanguage().equals("uz")) {
            dateTitle = ExcelData.getReportNameChildData(commonLocalizer.localize(PdfLocalizationName.from) + " " + ServerUtils.convertToUzbDateFormat(format.format(startDate)) + " " + commonLocalizer.localize(PdfLocalizationName.to) + " " + ServerUtils.convertToUzbDateFormat(format.format(endDate)), 24, 10);
        } else {
            dateTitle = ExcelData.getReportNameChildData(commonLocalizer.localize(PdfLocalizationName.from) + " " + format.format(startDate) + " " + commonLocalizer.localize(PdfLocalizationName.to) + " " + format.format(endDate), 24, 10);
        }
        list.add(new ExcelData[]{reportTitle});
        list.add(new ExcelData[]{companyTitle});
        list.add(new ExcelData[]{dateTitle});

        ExcelData emptyData = new ExcelData("", ExcelData.STRING, 24, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL);
        ExcelData[] cellEmptyHeader = new ExcelData[]{emptyData};
        list.add(cellEmptyHeader);

        ExcelData employeeHeader = new ExcelData(commonLocalizer.localize(PdfLocalizationName.employee), ExcelData.STRING, 24, false, true, ExcelData.NO_BORDER, ExcelData.HEADER_LEFT);
        ExcelData monthHeader = new ExcelData(commonLocalizer.localize(PdfLocalizationName.month), ExcelData.STRING, 15, false, true, ExcelData.NO_BORDER, ExcelData.HEADER_LEFT);
        ExcelData currencyHeader = new ExcelData(commonLocalizer.localize(PdfLocalizationName.currency), ExcelData.STRING, 15, false, true, ExcelData.NO_BORDER, ExcelData.HEADER_LEFT);
        ExcelData basicSalaryHeader = new ExcelData(commonLocalizer.localize(PdfLocalizationName.basicSalary), ExcelData.STRING, 20, false, true, ExcelData.NO_BORDER, ExcelData.HEADER_RIGHT);
        ExcelData allowanceHeader = new ExcelData(commonLocalizer.localize(PdfLocalizationName.allowance), ExcelData.STRING, 20, false, true, ExcelData.NO_BORDER, ExcelData.HEADER_RIGHT);
        ExcelData exPaymentHeader = new ExcelData(commonLocalizer.localize(PdfLocalizationName.expensePayment), ExcelData.STRING, 20, false, true, ExcelData.NO_BORDER, ExcelData.HEADER_RIGHT);
        ExcelData exDeductionHeader = new ExcelData(commonLocalizer.localize(PdfLocalizationName.expenseDeduction), ExcelData.STRING, 20, false, true, ExcelData.NO_BORDER, ExcelData.HEADER_RIGHT);
        ExcelData pensionHeader = new ExcelData(commonLocalizer.localize(PdfLocalizationName.pension), ExcelData.STRING, 20, false, true, ExcelData.NO_BORDER, ExcelData.HEADER_RIGHT);
        ExcelData deductionHeader = new ExcelData(commonLocalizer.localize(PdfLocalizationName.deductions), ExcelData.STRING, 20, false, true, ExcelData.NO_BORDER, ExcelData.HEADER_RIGHT);
        ExcelData totalHeader = new ExcelData(commonLocalizer.localize(PdfLocalizationName.totalPaid), ExcelData.STRING, 20, false, true, ExcelData.NO_BORDER, ExcelData.HEADER_RIGHT);
        ExcelData[] cellHeader = new ExcelData[]{employeeHeader, monthHeader, currencyHeader, basicSalaryHeader, allowanceHeader, exPaymentHeader, exDeductionHeader, pensionHeader, deductionHeader, totalHeader};
        list.add(cellHeader);

        BigDecimal basicSalaryTotal = BigDecimal.ZERO, allowanceTotal = BigDecimal.ZERO, expPaymentTotal = BigDecimal.ZERO, expDeductionTotal = BigDecimal.ZERO,
                pensionTotal = BigDecimal.ZERO, deductionTotal = BigDecimal.ZERO, overallTotal = BigDecimal.ZERO;
        ExcelData[] cellBody;
        for (SalaryReportItem item : data.getSalaryReportItems()) {
            String employeeNumber = item.getEmployeeCode() != null && !"".equals(item.getEmployeeCode()) ? item.getEmployeeCode() + " - " : "";
            ExcelData employee = new ExcelData(employeeNumber + (item.getEmployeeName() != null ? item.getEmployeeName() : ""), ExcelData.STRING, 24, false, true, ExcelData.NO_BORDER, ExcelData.HEADER_LEFT);
            ExcelData month = new ExcelData(item.getMonth() != null ? item.getMonth() : "n/a", ExcelData.STRING, 20, false, true, ExcelData.NO_BORDER, ExcelData.HEADER_RIGHT);
            ExcelData currency = new ExcelData(item.getCurrency() != null ? item.getCurrency() : "n/a", ExcelData.STRING, 20, false, true, ExcelData.NO_BORDER, ExcelData.HEADER_RIGHT);
            ExcelData basicSalary = new ExcelData(createCell(item.getBasicSalary(), calculationScale), ExcelData.STRING, 20, false, true, ExcelData.NO_BORDER, ExcelData.HEADER_RIGHT);
            ExcelData allowance = new ExcelData(createCell(item.getAllowance(), calculationScale), ExcelData.STRING, 20, false, true, ExcelData.NO_BORDER, ExcelData.HEADER_RIGHT);
            ExcelData expPayment = new ExcelData(createCell(item.getExpensePayment(), calculationScale), ExcelData.STRING, 20, false, true, ExcelData.NO_BORDER, ExcelData.HEADER_RIGHT);
            ExcelData expDeduction = new ExcelData(createCell(item.getExpenseDeduction(), calculationScale), ExcelData.STRING, 20, false, true, ExcelData.NO_BORDER, ExcelData.HEADER_RIGHT);
            ExcelData pensionAmount = new ExcelData(createCell(item.getPensionAmount(), calculationScale), ExcelData.STRING, 20, false, true, ExcelData.NO_BORDER, ExcelData.HEADER_RIGHT);
            ExcelData deduction = new ExcelData(createCell(item.getDeduction(), calculationScale), ExcelData.STRING, 20, false, true, ExcelData.NO_BORDER, ExcelData.HEADER_RIGHT);
            ExcelData total = new ExcelData(createCell(item.getTotal(), calculationScale), ExcelData.STRING, 20, false, true, ExcelData.NO_BORDER, ExcelData.HEADER_RIGHT);
            cellBody = new ExcelData[]{employee, month, currency, basicSalary, allowance, expPayment, expDeduction, pensionAmount, deduction, total};

            basicSalaryTotal = basicSalaryTotal.add(item.getBasicSalary() != null ? item.getBasicSalary() : BigDecimal.ZERO);
            allowanceTotal = allowanceTotal.add(item.getAllowance() != null ? item.getAllowance() : BigDecimal.ZERO);
            expPaymentTotal = expPaymentTotal.add(item.getExpensePayment() != null ? item.getExpensePayment() : BigDecimal.ZERO);
            expDeductionTotal = expDeductionTotal.add(item.getExpenseDeduction() != null ? item.getExpenseDeduction() : BigDecimal.ZERO);
            pensionTotal = pensionTotal.add(item.getPensionAmount() != null ? item.getPensionAmount() : BigDecimal.ZERO);
            deductionTotal = deductionTotal.add(item.getDeduction() != null ? item.getDeduction() : BigDecimal.ZERO);
            overallTotal = overallTotal.add(item.getTotal() != null ? item.getTotal() : BigDecimal.ZERO);

            list.add(cellBody);
        }

        list.add(new ExcelData[]{
                new ExcelData("Total", ExcelData.STRING, 24, false, true, ExcelData.NO_BORDER, ExcelData.HEADER_LEFT),
                new ExcelData("", ExcelData.STRING, 24, false, true, ExcelData.NO_BORDER, ExcelData.HEADER_LEFT),
                new ExcelData("", ExcelData.STRING, 20, false, true, ExcelData.NO_BORDER, ExcelData.HEADER_LEFT),
                new ExcelData(createCell(basicSalaryTotal, calculationScale), ExcelData.STRING, 20, false, true, ExcelData.NO_BORDER, ExcelData.HEADER_RIGHT),
                new ExcelData(createCell(allowanceTotal, calculationScale), ExcelData.STRING, 20, false, true, ExcelData.NO_BORDER, ExcelData.HEADER_RIGHT),
                new ExcelData(createCell(expPaymentTotal, calculationScale), ExcelData.STRING, 20, false, true, ExcelData.NO_BORDER, ExcelData.HEADER_RIGHT),
                new ExcelData(createCell(expDeductionTotal, calculationScale), ExcelData.STRING, 20, false, true, ExcelData.NO_BORDER, ExcelData.HEADER_RIGHT),
                new ExcelData(createCell(pensionTotal, calculationScale), ExcelData.STRING, 20, false, true, ExcelData.NO_BORDER, ExcelData.HEADER_RIGHT),
                new ExcelData(createCell(deductionTotal, calculationScale), ExcelData.STRING, 20, false, true, ExcelData.NO_BORDER, ExcelData.HEADER_RIGHT),
                new ExcelData(createCell(overallTotal, calculationScale), ExcelData.STRING, 20, false, true, ExcelData.NO_BORDER, ExcelData.HEADER_RIGHT)
        });

        HSSFWorkbook wb = new WorkBook(list).getWorkBook(filename, 0, 0, 0, 8);
        wb.setRepeatingRowsAndColumns(0, 0, 9, 0, 7);
        return wb;
    }

    private BigDecimal createCell(BigDecimal value, Integer calculationScale) {
        return (value != null ? value.setScale(calculationScale, BigDecimal.ROUND_HALF_UP) : BigDecimal.ZERO);
    }

    @Override
    protected void setFileName() {
        filename = "SalaryReport";
    }
}
