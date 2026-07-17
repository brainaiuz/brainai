package com.edatasite.workforce.gwt.core.server.commons;

import com.edatasite.shared.poiutils.WorkBook;
import com.edatasite.workforce.core.domain.EdsProperty;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.accounting.EdsFinancialSettings;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.rpc.payroll.EndOfServiceRules;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.db.PropertManager;
import com.edatasite.workforce.gwt.core.server.db.UploadManager;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.localization.PdfLocalizationName;
import com.edatasite.workforce.gwt.payroll.client.rpc.EndOfServiceData;
import com.edatasite.workforce.gwt.payroll.client.rpc.EoSCalculationData;
import com.edatasite.workforce.gwt.payroll.client.rpc.EosReportData;
import com.edatasite.workforce.gwt.payroll.client.rpc.PayrollService;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.WfmResourceBundleMessageSource;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Shohruh on 22-Oct-15.
 */
public class EndOfServiceReportExcelHandler extends BaseExcelHandler {

    @Autowired
    private UploadManager uploadManager;

    @Autowired
    private PayrollService payrollService;
    private static final SimpleDateFormat format = new SimpleDateFormat("MMMM d, yyyy");
    private final int aCellSize = 24;
    private ServerUtils serverUtils;
    private final int bCellSize = 20;
    private List<ExcelData[]> list;
    private ListingFilterParameter lfp;
    private EosReportData data;
    private EndOfServiceData eosSettings;
    private EdsUser user;
    private Integer calculationScale;
    private final int cCellSize = 20;
    private final int dCellSize = 20;
    private final int eCellSize = 18;
    private final int fCellSize = 24;
    private final int gCellSize = 18;
    @Autowired
    private PropertManager propertManager;
    private WfmResourceBundleMessageSource pdfWfmMessageSource;

    @Override
    protected void setFileName() {
        filename = "End_Of_Service_Report_" + dateFormat(uploadManager.getUser().getUserDate());
    }

    @Override
    protected HSSFWorkbook getWorkBook(Object object) {
        user = uploadManager.getUser();
        lfp = (ListingFilterParameter) object;
        ListingFilterParameter filterParameters = (ListingFilterParameter) object;
        EdsProperty property = propertManager.findByCode(filterParameters.getPropertyCode());

        String sheetName = property != null ? property.getPlural() : commonLocalizer.localize("endOfService");
        eosSettings = payrollService.getEndOfServiceSettings(user.getCompany().getCountryZone().getCountry().getCode());
        data = payrollService.getEmployeeEosDataList(lfp);
        EdsFinancialSettings financialSettings = financialSettingsManager.getFinancialSettings();
        calculationScale = getCalculationScale(financialSettings);

        list = new LinkedList<>();
        list.add(new ExcelData[]{
                new ExcelData("", ExcelData.STRING, aCellSize, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL)
        });

        String currencySymbol = financialSettings.getCurrency().getSymbol();
        String currencyCode = financialSettings.getCurrency().getName();
        currencySymbol = currencySymbol != null ? currencySymbol : "";

        int lastColumnIndex = 6;

        ExcelData titleData = ExcelData.getReportNameData(commonLocalizer.localize(PdfLocalizationName.endOfServiceGratuityPaymentReport), aCellSize, lastColumnIndex);

        ExcelData companyData = ExcelData.getReportNameChildData(user.getCompany().getName(), aCellSize, lastColumnIndex);

        ExcelData childData = ExcelData.getReportNameChildData(commonLocalizer.localize(PdfLocalizationName.calculatedAsOfResignationDate), aCellSize, lastColumnIndex);

        ExcelData dateData = ExcelData.getReportNameChildData(ServerUtils.getUserLocale().getLanguage().equals("uz") ? ServerUtils.convertToUzbDateFormat(format.format(lfp.getStartDate())) : format.format(lfp.getStartDate()), aCellSize, lastColumnIndex);

        ExcelData currencyData = ExcelData.getReportNameChildData(accountingLocalizer.localizeAccounting(PdfLocalizationName.figuresIn) + " " + currencySymbol + "(" + currencyCode + ")", aCellSize, lastColumnIndex);

        list.add(new ExcelData[]{titleData});
        list.add(new ExcelData[]{companyData});
        list.add(new ExcelData[]{childData});
        list.add(new ExcelData[]{dateData});
        list.add(new ExcelData[]{currencyData});

        ExcelData emptyData = new ExcelData("", ExcelData.STRING, aCellSize, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL);
        ExcelData[] cellEmptyHeader = new ExcelData[]{emptyData};
        list.add(cellEmptyHeader);

        LinkedList<ExcelData> cellHeader = new LinkedList<>();

        cellHeader.add(new ExcelData(commonLocalizer.localize(PdfLocalizationName.employee), ExcelData.STRING, aCellSize, false, true, ExcelData.NO_BORDER, ExcelData.HEADER_LEFT));

//        cellHeader.add(new ExcelData("Payroll Group", ExcelData.STRING, bCellSize, false, true, ExcelData.NO_BORDER, ExcelData.HEADER_LEFT));
        cellHeader.add(new ExcelData(commonLocalizer.localize(PdfLocalizationName.hireDate), ExcelData.STRING, cCellSize, false, true, ExcelData.NO_BORDER, ExcelData.HEADER_RIGHT));
        cellHeader.add(new ExcelData(commonLocalizer.localize(PdfLocalizationName.resignationDate), ExcelData.STRING, dCellSize, false, true, ExcelData.NO_BORDER, ExcelData.HEADER_RIGHT));
        cellHeader.add(new ExcelData(commonLocalizer.localize(PdfLocalizationName.workedDays), ExcelData.STRING, eCellSize, false, true, ExcelData.NO_BORDER, ExcelData.HEADER_RIGHT));
        if (eosSettings.isIncludeLeaveAllowances()) {
            cellHeader.add(new ExcelData(commonLocalizer.localize(PdfLocalizationName.leftLeaveDays), ExcelData.STRING, eCellSize, false, true, ExcelData.NO_BORDER, ExcelData.HEADER_RIGHT));
            cellHeader.add(new ExcelData(commonLocalizer.localize(PdfLocalizationName.leaveAllowance), ExcelData.STRING, eCellSize, false, true, ExcelData.NO_BORDER, ExcelData.HEADER_RIGHT));
        }
        if (eosSettings.isIncludeBenefitPayments()) {
            cellHeader.add(new ExcelData(commonLocalizer.localize(PdfLocalizationName.benefitPayment), ExcelData.STRING, eCellSize, false, true, ExcelData.NO_BORDER, ExcelData.HEADER_RIGHT));
        }
        cellHeader.add(new ExcelData(commonLocalizer.localize(PdfLocalizationName.calculationType), ExcelData.STRING, fCellSize, false, true, ExcelData.NO_BORDER, ExcelData.HEADER_RIGHT));
        cellHeader.add(new ExcelData(commonLocalizer.localize(PdfLocalizationName.gratuityTotal), ExcelData.STRING, gCellSize, false, true, ExcelData.NO_BORDER, ExcelData.HEADER_RIGHT));

        list.add(cellHeader.toArray(new ExcelData[]{}));

        setData();

        HSSFWorkbook wb = new WorkBook(list).getWorkBook(filename, 0, 0, 0, 8);
        wb.setRepeatingRowsAndColumns(0, 0, lastColumnIndex, 0, 7);
        return wb;
    }

    private void setData() {
        BigDecimal total = BigDecimal.ZERO;
        double year;
        Date date = parseFilterParameterDate(lfp.getStartDateNC());
        LinkedList<ExcelData> cellBody;
        if (data != null && data.getEoSCalculationData().size() > 0) {
            for (EoSCalculationData eosData : data.getEoSCalculationData()) {
                total = BigDecimal.ZERO;
                cellBody = new LinkedList<>();
                if (eosData.getTotalWorkedDays() != null) {
                    total = calculateTotal(total, eosData);
                    if (eosSettings.isIncludeLeaveAllowances()) {
                        total = total.add(eosData.getLeaveAllowanceTotal());
                    }
                    if (eosSettings.isIncludeBenefitPayments()) {
                        total = total.add(eosData.getBenefitPaymentTotal());
                    }
                    String employeeNumber = eosData.getEmployee().getDescription() != null && !"".equals(eosData.getEmployee().getDescription()) ? eosData.getEmployee().getDescription() + " - " : "";
                    cellBody.add(new ExcelData(employeeNumber + (eosData.getEmployee().getName() != null ? eosData.getEmployee().getName() : ""), ExcelData.STRING, aCellSize, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL_LEFT));
//                    cellBody.add(new ExcelData(eosData.getPayrollGroup() != null ? eosData.getPayrollGroup().getName() : "n/a", ExcelData.STRING, bCellSize, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL_LEFT));
                    cellBody.add(new ExcelData(ServerUtils.getUserLocale().getLanguage().equals("uz") ? ServerUtils.convertToUzbDateFormat(format.format(eosData.getHireDate().getNonConvertedDate())) : format.format(eosData.getHireDate().getNonConvertedDate()), ExcelData.STRING, cCellSize, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL_RIGHT));
                    cellBody.add(new ExcelData((eosData.getResignationDate() != null && eosData.getResignationDate().getDate() != null) ? (ServerUtils.getUserLocale().getLanguage().equals("uz") ? ServerUtils.convertToUzbDateFormat(format.format(eosData.getResignationDate().getNonConvertedDate())) : format.format(eosData.getResignationDate().getNonConvertedDate())) : (ServerUtils.getUserLocale().getLanguage().equals("uz") ? ServerUtils.convertToUzbDateFormat(format.format(date)) : format.format(date)), ExcelData.STRING, dCellSize, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL_RIGHT));
                    cellBody.add(new ExcelData(eosData.getTotalWorkedDays().toString(), ExcelData.STRING, eCellSize, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL_RIGHT));
                    if (eosSettings.isIncludeLeaveAllowances()) {
                        cellBody.add(new ExcelData(createCell(eosData.getLeftLeaveDays(), calculationScale), ExcelData.STRING, eCellSize, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL_RIGHT));
                        cellBody.add(new ExcelData(createCell(eosData.getLeaveAllowanceTotal(), calculationScale), ExcelData.STRING, eCellSize, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL_RIGHT));
                    }
                    if (eosSettings.isIncludeBenefitPayments()) {
                        cellBody.add(new ExcelData(createCell(eosData.getBenefitPaymentTotal(), calculationScale), ExcelData.STRING, eCellSize, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL_RIGHT));
                    }
                    cellBody.add(new ExcelData(lfp.getReasonID() == 0 ? commonLocalizer.localize(PdfLocalizationName.employeeResignation) : commonLocalizer.localize(PdfLocalizationName.contractTermination), ExcelData.STRING, fCellSize, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL_RIGHT));
                    cellBody.add(new ExcelData(createCell(total, calculationScale), ExcelData.STRING, gCellSize, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL_RIGHT));

                    list.add(cellBody.toArray(new ExcelData[]{}));

                    //
                }
            }
        }
    }

    private BigDecimal calculateTotal(BigDecimal total, EoSCalculationData eosData) {
        double year;
        year = Double.valueOf(eosData.getTotalWorkedDays()) / 365;
        if (lfp.isCorporate()) {
            BigDecimal amount = eosData.getBasicSalary();
            amount = amount.add(eosData.getLastPaymentsTotal());
            if (lfp.getReasonID() == 1) {
                if (year <= 5) {
                    total = total.add(amount.multiply(BigDecimal.valueOf(year)).divide(new BigDecimal(2), 2, RoundingMode.HALF_UP));
                } else {
                    total = total.add(amount.multiply(new BigDecimal("2.5"))).add(eosData.getBasicSalary().multiply(new BigDecimal(year - 5)));
                }
            } else {
                if (year > 2 && year <= 5) {
                    total = total.add(amount.divide(new BigDecimal(6), 2, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(year)));
                } else if (year > 5 && year <= 10) {
                    total = total.add(amount.multiply(new BigDecimal("2.5"))).add(eosData.getBasicSalary().multiply(new BigDecimal(year - 5))).multiply(new BigDecimal(2)).divide(new BigDecimal(3), 2, RoundingMode.HALF_UP);
                } else if (year > 10) {
                    total = total.add(amount.multiply(new BigDecimal("2.5"))).add(eosData.getBasicSalary().multiply(new BigDecimal(year - 5)));
                }
            }
        } else {
            double d = eosData.getNumberOfWorkDay() != null ? 1 / eosData.getNumberOfWorkDay() : 12.00 / 365;
            Integer days = 0;
            Integer months = null;
            boolean moreThan5Years = false;
            for (EndOfServiceRules rule : eosSettings.getRules()) {
                if (rule.getMonths() != null && rule.isUseMonthPayment()) {
                    months = rule.getMonths();
                }
                if (lfp.getReasonID() == 0) {  // Employee Resignation
                    if (rule.getRuleCode().equals("0<x<1") && rule.getReasonCode().equals(Constants.EMPLOYEE_RESIGNATION) && rule.getRuleType().equals(eosData.getEmployeeContractType())) {
                        if (year > 0 && year < 1) {
                            days = rule.getDays();
                        }
                    } else if (rule.getRuleCode().equals("1<=x<3")) {
                        if (year >= 1 && year < 3) {
                            days = rule.getDays();
                        }
                    } else if (rule.getRuleCode().equals("3<=x<5")) {
                        if (year >= 3 && year < 5) {
                            days = rule.getDays();
                        }
                    } else if (rule.getRuleCode().equals("x>5") && rule.getReasonCode().equals(Constants.EMPLOYEE_RESIGNATION) && rule.getRuleType().equals(eosData.getEmployeeContractType())) {
                        if (year > 5) {
                            moreThan5Years = true;
                            days = rule.getDays();
                        }
                    } else if (rule.getRuleCode().equals("1<=x<5") && rule.getReasonCode().equals(Constants.EMPLOYEE_RESIGNATION) && rule.getRuleType().equals(eosData.getEmployeeContractType())) {
                        if (year >= 1 && year < 5) {
                            days = rule.getDays();
                        }
                    }
                } else {
                    if (rule.getRuleCode().equals("x<=5")) {
                        if (year <= 5) {
                            days = rule.getDays();
                        }
                    } else if (rule.getRuleCode().equals("x>5") && rule.getReasonCode().equals(Constants.CONTRACT_TERMINATION) && rule.getRuleType().equals(eosData.getEmployeeContractType())) {
                        if (year > 5) {
                            moreThan5Years = true;
                            days = rule.getDays();
                        }
                    } else if (rule.getRuleCode().equals("0<x<1") && rule.getReasonCode().equals(Constants.CONTRACT_TERMINATION) && rule.getRuleType().equals(eosData.getEmployeeContractType())) {
                        if (year > 0 && year < 1) {
                            days = rule.getDays();
                        }
                    } else if (rule.getRuleCode().equals("x>1") && rule.getRuleType().equals(eosData.getEmployeeContractType())) {
                        if (year > 1) {
                            days = rule.getDays();
                        }
                    }
                }
            }
            BigDecimal calculatePeriod;
            if (months != null) {
                calculatePeriod = new BigDecimal(months);
            } else {
                calculatePeriod = BigDecimal.valueOf(d).multiply(new BigDecimal(days));
            }
            if (moreThan5Years) {
                BigDecimal years5Total = eosData.getBasicSalary().multiply(BigDecimal.valueOf(d)).multiply(new BigDecimal(21)).multiply(new BigDecimal(5));
                BigDecimal after5YearsTotal = eosData.getBasicSalary().multiply(calculatePeriod).multiply(new BigDecimal(year - 5));
                total = total.add(years5Total).add(after5YearsTotal);
            } else {
                total = total.add(eosData.getBasicSalary().multiply(calculatePeriod).multiply(new BigDecimal(year)));
            }
        }
        return total;
    }

    private BigDecimal createCell(BigDecimal value, Integer calculationScale) {
        return (value != null ? value.setScale(calculationScale, RoundingMode.HALF_UP) : BigDecimal.ZERO);
    }
}
