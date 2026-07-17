package com.edatasite.workforce.gwt.core.server.servlets.pdf.payroll;

import com.edatasite.workforce.core.domain.EdsCompany;
import com.edatasite.workforce.core.domain.EdsProperty;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.accounting.EdsFinancialSettings;
import com.edatasite.workforce.core.domain.payrolluk.EdsCompanyPayrollSettings;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.rpc.payroll.EndOfServiceRules;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.server.db.UploadManager;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.AbstractITextPostPdfHandler;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.IPostPDFHandler;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.PDFConstants;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.localization.PdfLocalizationName;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.ITextFontTypeEnum;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.ITextPdfViewTypeEnum;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.CellData;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.ITextGenericPdfData;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.ITextTableList;
import com.edatasite.workforce.gwt.payroll.client.rpc.EndOfServiceData;
import com.edatasite.workforce.gwt.payroll.client.rpc.EoSCalculationData;
import com.edatasite.workforce.gwt.payroll.client.rpc.EosReportData;
import com.edatasite.workforce.gwt.payroll.client.rpc.PayrollService;
import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfWriter;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;

/**
 * Created by Shohruh on 22-Oct-15.
 */
public class EndOfServiceReportPdfHandler extends AbstractITextPostPdfHandler implements IPostPDFHandler, PDFConstants {

    @Autowired
    private UploadManager uploadManager;

    @Autowired
    private PayrollService payrollService;

    private static final SimpleDateFormat format = new SimpleDateFormat("MMMM d, yyyy");
    private ListingFilterParameter lfp;
    private EosReportData data;
    private EndOfServiceData eosSettings;
    private EdsUser user;

    @Override
    public ITextGenericPdfData buildPdfDocument(Object dataClass, Document document, PdfWriter writer) throws IOException {
        return null;
    }

    @Override
    protected ITextGenericPdfData buildPdfDocumentCustomise(Object dataClass, EdsCompany company, boolean hasPhantom) {
        int numColumns = 6;
        user = uploadManager.getUser();
        lfp = (ListingFilterParameter) dataClass;
        eosSettings = payrollService.getEndOfServiceSettings(user.getCompany().getCountryZone().getCountry().getCode());
        boolean enableMultiCurrency = false;
        EdsCompanyPayrollSettings employeerSettings = companyPayrollSettingsManager.getCompanySettingValue(MULTI_CURRENCY_FOR_PAYROLL);
        if (employeerSettings != null && "true".equals(employeerSettings.getValue())) {
            enableMultiCurrency = true;
        }
        data = payrollService.getEmployeeEosDataList(lfp);

        ITextGenericPdfData pdfData = new ITextGenericPdfData();
        pdfData.setPdfViewType(ITextPdfViewTypeEnum.LISTTABLE);
        if(eosSettings.isIncludeLeaveAllowances()){
            numColumns +=2;
        }
        if(eosSettings.isIncludeBenefitPayments()){
            numColumns++;
        }

        ITextTableList table = new ITextTableList(numColumns);
        pdfData.setListTable(table);
        EdsFinancialSettings fs = financialSettingsManager.getFinancialSettings();
        String currencySymbol = fs.getCurrency().getSymbol();
        String baseccurrencyName = fs.getCurrency().getName();
        currencySymbol = currencySymbol != null ? currencySymbol : "";

        LinkedList<CellData> header = new LinkedList<>();
        header.add(drawHeader(commonLocalizer.localize(PdfLocalizationName.employee), Element.ALIGN_LEFT));
//        header.add(drawHeader("Payroll Group", Element.ALIGN_CENTER));
        header.add(drawHeader(commonLocalizer.localize(PdfLocalizationName.hireDateField), Element.ALIGN_LEFT));
        header.add(drawHeader(commonLocalizer.localize(PdfLocalizationName.resignationDate), Element.ALIGN_LEFT));
        header.add(drawHeader(commonLocalizer.localize(PdfLocalizationName.workedDays), Element.ALIGN_RIGHT));
        if(eosSettings.isIncludeLeaveAllowances()){
            header.add(drawHeader(pdfWfmMessageSource.localize("leftLeaveDays"), Element.ALIGN_RIGHT));
            header.add(drawHeader(pdfWfmMessageSource.localize("leaveAllowance"), Element.ALIGN_RIGHT));
        }
        if(eosSettings.isIncludeBenefitPayments()){
            header.add(drawHeader(pdfWfmMessageSource.localize("benefitPayment"), Element.ALIGN_RIGHT));
        }
        header.add(drawHeader(pdfWfmMessageSource.localize("calculationType"), Element.ALIGN_CENTER));
        header.add(drawHeader(pdfWfmMessageSource.localize("gratuityTotal"), Element.ALIGN_RIGHT));
        header.add(drawHeader(pdfWfmMessageSource.localize("currency"), Element.ALIGN_RIGHT));


        table.addPdfTableHeader(header.toArray(new CellData[]{}));

        DecimalFormat priceScaleNumberFormat = getPriceScaleNumberFormat(fs);

        setData(table, priceScaleNumberFormat, baseccurrencyName, enableMultiCurrency);
        return pdfData;
    }

    private void setData(ITextTableList table, DecimalFormat numberFormat, String baseccurrencyName, boolean enableMultiCurrency) {
        BigDecimal total = BigDecimal.ZERO;
        double year;
        Date date = parseFilterParameterDate(lfp.getStartDateNC());
        LinkedList<CellData> cellBody;
        if (data != null && data.getEoSCalculationData().size() > 0) {
            for (EoSCalculationData eosData : data.getEoSCalculationData()) {
                cellBody = new LinkedList<>();
                total = BigDecimal.ZERO;
                if (eosData.getTotalWorkedDays() != null) {
                    total = calculateTotal(total, eosData);
                    total = total.add(eosData.getLeaveAllowanceTotal());
                    total = total.add(eosData.getBenefitPaymentTotal());
                    String employeeNumber = eosData.getEmployee().getDescription() != null && !"".equals(eosData.getEmployee().getDescription()) ? eosData.getEmployee().getDescription() + " - " : "";
                    cellBody.add(new CellData(employeeNumber + (eosData.getEmployee().getName() != null ? eosData.getEmployee().getName() : ""), Element.ALIGN_LEFT));
//                    cellBody.add(new CellData(eosData.getPayrollGroup() != null ? eosData.getPayrollGroup().getName() : "n/a", Element.ALIGN_CENTER));
                    cellBody.add(new CellData(format.format(eosData.getHireDate().getNonConvertedDate()), Element.ALIGN_LEFT));
                    cellBody.add(new CellData((eosData.getResignationDate() != null && eosData.getResignationDate().getDate() != null) ? format.format(eosData.getResignationDate().getNonConvertedDate()) : format.format(date), Element.ALIGN_LEFT));
                    cellBody.add(new CellData(eosData.getTotalWorkedDays().toString(), Element.ALIGN_RIGHT));
                    if (eosSettings.isIncludeLeaveAllowances()) {
                        cellBody.add(createCell(eosData.getLeftLeaveDays(), numberFormat));
                        cellBody.add(createCell(eosData.getLeaveAllowanceTotal(), numberFormat));
                    }
                    if (eosSettings.isIncludeBenefitPayments()) {
                        cellBody.add(createCell(eosData.getBenefitPaymentTotal(), numberFormat));
                    }
                    cellBody.add(new CellData(lfp.getReasonID() == 0 ? pdfWfmMessageSource.localize("employeeResignation") : pdfWfmMessageSource.localize("contractTermination"), Element.ALIGN_CENTER));
                    cellBody.add(createCell(total, numberFormat));
                    if (enableMultiCurrency) {
                        cellBody.add(new CellData(eosData.getEmployeeSalaryCurrency(), Element.ALIGN_CENTER));
                    } else {
                        cellBody.add(new CellData(baseccurrencyName, Element.ALIGN_CENTER));
                    }

                    table.addPdfTableRows(cellBody.toArray(new CellData[]{}));
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
                    total = total.add(amount.multiply(BigDecimal.valueOf(year)).divide(new BigDecimal(2), 2, BigDecimal.ROUND_HALF_UP));
                } else {
                    total = total.add(amount.multiply(new BigDecimal(2.5))).add(eosData.getBasicSalary().multiply(new BigDecimal(year - 5)));
                }
            } else {
                if (year > 2 && year <= 5) {
                    total = total.add(amount.divide(new BigDecimal(6), 2, BigDecimal.ROUND_HALF_UP).multiply(BigDecimal.valueOf(year)));
                } else if (year > 5 && year <= 10) {
                    total = total.add(amount.multiply(new BigDecimal(2.5))).add(eosData.getBasicSalary().multiply(new BigDecimal(year - 5))).multiply(new BigDecimal(2)).divide(new BigDecimal(3), 2, BigDecimal.ROUND_HALF_UP);
                } else if (year > 10) {
                    total = total.add(amount.multiply(new BigDecimal(2.5))).add(eosData.getBasicSalary().multiply(new BigDecimal(year - 5)));
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

    @Override
    protected void setFileName(EdsUser user, Object dataClass) {
        StringBuilder fileName = new StringBuilder("End_of_Service_Report_" + dateFormat(user.getUserDate()));
        super.setFileName(fileName.toString());
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
    protected boolean isListingPDF() {
        return true;
    }

    @Override
    protected String getTableName(Object dataClass) {
//        lfp = (ListingFilterParameter) dataClass;
//        String date = format.format(parseFilterParameterDate(lfp.getStartDateNC()));
//        EdsFinancialSettings fs = financialSettingsManager.getFinancialSettings();
//        String currencySymbol = "";
//        String currencyCode = "";
//        if (fs != null && fs.getCurrency() != null) {
//            currencySymbol = escapeHtml(fs.getCurrency().getSymbol());
//            currencyCode = escapeHtml(fs.getCurrency().getName());
//        }
//        StringBuilder nameLabel = new StringBuilder();
//        nameLabel.append(pdfWfmMessageSource.localize("calculatedAsOfResigDate"));
//                .append(" ").append(date).append(", ")
//                .append(accountingLocalizer.localizeAccounting(PdfLocalizationName.figuresIn))
//                .append(" ")
//                .append(currencySymbol).append("(").append(currencyCode).append(")");
//        return nameLabel.toString();
        ListingFilterParameter fp = (ListingFilterParameter) dataClass;
        EdsProperty property = propertManager.findByCode(fp.getPropertyCode());
        return property != null ? property.getPlural() : pdfWfmMessageSource.localize("endOfService");
    }
}
