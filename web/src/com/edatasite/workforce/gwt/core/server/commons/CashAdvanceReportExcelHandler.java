package com.edatasite.workforce.gwt.core.server.commons;

import com.edatasite.shared.poiutils.WorkBook;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.accounting.EdsFinancialSettings;
import com.edatasite.workforce.core.domain.settings.EdsCompanySettings;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.db.UserManager;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.localization.PdfLocalizationName;
import com.edatasite.workforce.gwt.payroll.client.rpc.CashAdvanceReportData;
import com.edatasite.workforce.gwt.payroll.client.rpc.CashAdvanceReportItem;
import com.edatasite.workforce.gwt.payroll.client.rpc.PayrollService;
import org.apache.commons.lang3.StringUtils;
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
 * Date: 4/4/16
 * Time: 9:14 PM
 * To change this template use File | Settings | File Templates.
 */
public class CashAdvanceReportExcelHandler extends BaseExcelHandler {

    @Autowired
    private PayrollService payrollService;

    @Autowired
    private UserManager userManager;

    private CashAdvanceReportData data;
    private List<ExcelData[]> list;
    private ListingFilterParameter lfp;
    private SimpleDateFormat format;
    private Integer calculationScale;


    @Override
    protected HSSFWorkbook getWorkBook(Object object) {
        lfp = (ListingFilterParameter) object;
        EdsUser user = userManager.getUser();
        EdsCompanySettings companySettings = user.getCompany().getCompanySettings();
        if (companySettings != null && StringUtils.isNotBlank(companySettings.getExcelLimit())) {
            lfp.setLimit(Integer.parseInt(companySettings.getExcelLimit()));
        } else {
            lfp.setLimit(LIMIT_EXCEL_ROW);
        }
        format = new SimpleDateFormat("MMMM d, yyyy");
        EdsFinancialSettings fs = financialSettingsManager.getFinancialSettings();
        calculationScale = getCalculationScale(fs);
        Date startDate = parseFilterParameterDate(lfp.getStartDateNC());
        Date endDate = parseFilterParameterDate(lfp.getEndDateNC());
        data = payrollService.getCashAdvanceReportData(lfp);
        list = new LinkedList<>();
        list.add(new ExcelData[]{
                new ExcelData("", ExcelData.STRING, 24, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL)
        });

        ExcelData reportTitle = ExcelData.getReportNameData(commonLocalizer.localize(PdfLocalizationName.cashAdvanceReport), 24, 6);
        ExcelData companyTitle = ExcelData.getReportNameChildData(userManager.getUser().getCompany().getName(), 24, 6);
        ExcelData dateTitle;

        if (ServerUtils.getUserLocale().getLanguage().equals("uz")) {
            dateTitle = ExcelData.getReportNameChildData(commonLocalizer.localize(PdfLocalizationName.from) + " " + ServerUtils.convertToUzbDateFormat(format.format(startDate)) + " " + commonLocalizer.localize(PdfLocalizationName.to) + " " + ServerUtils.convertToUzbDateFormat(format.format(endDate)), 24, 6);
        } else {
            dateTitle = ExcelData.getReportNameChildData(commonLocalizer.localize(PdfLocalizationName.from) + " " + format.format(startDate) + " " + commonLocalizer.localize(PdfLocalizationName.to) + " " + format.format(endDate), 24, 6);
        }
        list.add(new ExcelData[]{reportTitle});
        list.add(new ExcelData[]{companyTitle});
        list.add(new ExcelData[]{dateTitle});

        ExcelData emptyData = new ExcelData("", ExcelData.STRING, 24, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL);
        ExcelData[] cellEmptyHeader = new ExcelData[]{emptyData};
        list.add(cellEmptyHeader);

        ExcelData employeeHeader = new ExcelData(commonLocalizer.localize(PdfLocalizationName.employee), ExcelData.STRING, 24, false, true, ExcelData.NO_BORDER, ExcelData.HEADER_LEFT);
//        ExcelData payrollGroupHeader = new ExcelData("Payroll Group", ExcelData.STRING, 20, false, true, ExcelData.NO_BORDER, ExcelData.HEADER_LEFT);
        ExcelData dateHeader = new ExcelData(commonLocalizer.localize(PdfLocalizationName.cashAdvanceDate), ExcelData.STRING, 28, false, true, ExcelData.NO_BORDER, ExcelData.HEADER_LEFT);
        ExcelData amountHeader = new ExcelData(commonLocalizer.localize(PdfLocalizationName.amount), ExcelData.STRING, 20, false, true, ExcelData.NO_BORDER, ExcelData.HEADER_RIGHT);
        ExcelData paidAmountHeader = new ExcelData(commonLocalizer.localize(PdfLocalizationName.paidAmount), ExcelData.STRING, 20, false, true, ExcelData.NO_BORDER, ExcelData.HEADER_RIGHT);
        ExcelData remainingAmountHeader = new ExcelData(commonLocalizer.localize(PdfLocalizationName.remainingAmount), ExcelData.STRING, 20, false, true, ExcelData.NO_BORDER, ExcelData.HEADER_RIGHT);

        ExcelData[] cellHeader = new ExcelData[]{employeeHeader, dateHeader, amountHeader, paidAmountHeader, remainingAmountHeader};
        list.add(cellHeader);
        ExcelData[] cellBody;
        BigDecimal amountTotal = BigDecimal.ZERO, paidAmountTotal = BigDecimal.ZERO, remainingTotal = BigDecimal.ZERO;
        for (CashAdvanceReportItem item : data.getCashAdvanceReportItems()) {
            String employeeNumber = item.getEmployeeCode() != null && !"".equals(item.getEmployeeCode()) ? item.getEmployeeCode() + " - " : "";
            ExcelData employee = new ExcelData(employeeNumber + (item.getEmployeeName() != null ? item.getEmployeeName() : ""), ExcelData.STRING, 24, false, true, ExcelData.NO_BORDER, ExcelData.HEADER_LEFT);
//            ExcelData payrollGroup = new ExcelData(item.getPayrollGroup() != null ? item.getPayrollGroup() : "n/a", ExcelData.STRING, 20, false, true, ExcelData.NO_BORDER, ExcelData.HEADER_LEFT);
            ExcelData date = new ExcelData(format.format(item.getDate()), ExcelData.STRING, 28, false, true, ExcelData.NO_BORDER, ExcelData.HEADER_LEFT);
            ExcelData amount = new ExcelData(createCell(item.getAmount(), calculationScale), ExcelData.STRING, 20, false, true, ExcelData.NO_BORDER, ExcelData.HEADER_RIGHT);
            ExcelData paidAmount = new ExcelData(createCell(item.getPaidAmount(), calculationScale), ExcelData.STRING, 20, false, true, ExcelData.NO_BORDER, ExcelData.HEADER_RIGHT);
            ExcelData remainingAmount = new ExcelData(createCell(item.getRemainingAmount(), calculationScale), ExcelData.STRING, 20, false, true, ExcelData.NO_BORDER, ExcelData.HEADER_RIGHT);
            cellBody = new ExcelData[]{employee, date, amount, paidAmount, remainingAmount};
            amountTotal = amountTotal.add(item.getAmount());
            paidAmountTotal = paidAmountTotal.add(item.getPaidAmount());
            remainingTotal = remainingTotal.add(item.getRemainingAmount());
            list.add(cellBody);
        }

        list.add(new ExcelData[]{
                new ExcelData(commonLocalizer.localize(PdfLocalizationName.total), ExcelData.STRING, 24, false, true, ExcelData.NO_BORDER, ExcelData.HEADER_LEFT),
                new ExcelData("", ExcelData.STRING, 24, false, true, ExcelData.NO_BORDER, ExcelData.HEADER_LEFT),
                new ExcelData(createCell(amountTotal, calculationScale), ExcelData.STRING, 20, false, true, ExcelData.NO_BORDER, ExcelData.HEADER_RIGHT),
                new ExcelData(createCell(paidAmountTotal, calculationScale), ExcelData.STRING, 20, false, true, ExcelData.NO_BORDER, ExcelData.HEADER_RIGHT),
                new ExcelData(createCell(remainingTotal, calculationScale), ExcelData.STRING, 20, false, true, ExcelData.NO_BORDER, ExcelData.HEADER_RIGHT)
        });

        HSSFWorkbook wb = new WorkBook(list).getWorkBook(filename, 0, 0, 0, 8);
        wb.setRepeatingRowsAndColumns(0, 0, 5, 0, 7);
        return wb;
    }

    private BigDecimal createCell(BigDecimal value, Integer calculationScale) {
        return (value != null ? value.setScale(calculationScale, BigDecimal.ROUND_HALF_UP) : BigDecimal.ZERO);
    }

    @Override
    protected void setFileName() {
        filename = "Cash_Advance_Report";
    }
}
