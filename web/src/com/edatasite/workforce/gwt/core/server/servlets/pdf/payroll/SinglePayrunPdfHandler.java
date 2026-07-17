package com.edatasite.workforce.gwt.core.server.servlets.pdf.payroll;

import com.edatasite.workforce.core.domain.*;
import com.edatasite.workforce.core.domain.accounting.EdsFinancialSettings;
import com.edatasite.workforce.core.domain.crm.EdsCrmAccount;
import com.edatasite.workforce.core.domain.payrolluk.EdsEmployeePayrollSettings;
import com.edatasite.workforce.core.domain.payrolluk.EdsPaymentDeduction;
import com.edatasite.workforce.core.domain.payrolluk.EdsPayslipTableItem;
import com.edatasite.workforce.gwt.core.client.enums.GenericSettingsEnum;
import com.edatasite.workforce.gwt.core.client.rpc.*;
import com.edatasite.workforce.gwt.core.client.rpc.currency.CurrencyLayerItem;
import com.edatasite.workforce.gwt.core.client.rpc.form.CustomFormConstants;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.rpc.payroll.DailyOvertimeData;
import com.edatasite.workforce.gwt.core.client.rpc.payroll.MonthlyOvertimeDataWithRates;
import com.edatasite.workforce.gwt.core.client.ui.DateUtil;
import com.edatasite.workforce.gwt.core.client.ui.customfields.ViewName;
import com.edatasite.workforce.gwt.core.client.ui.view.PaymentDeductionObject;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.db.*;
import com.edatasite.workforce.gwt.core.server.db.payroll.CompanyPayrollSettingsManager;
import com.edatasite.workforce.gwt.core.server.db.payroll.EmployeePayrollSettingsManager;
import com.edatasite.workforce.gwt.core.server.db.payroll.PayslipPaymentsManager;
import com.edatasite.workforce.gwt.core.server.db.payroll.PayslipTableItemManager;
import com.edatasite.workforce.gwt.core.server.db.settings.GenericSettingsManager;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.AbstractITextPostPdfHandler;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.IPostPDFHandler;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.PDFConstants;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.PdfReferenceCodeNameEnum;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.ITextPdfViewTypeEnum;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.CustomisedITextTable;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.ITextBaseInvoice;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.ITextGenericPdfData;
import com.edatasite.workforce.gwt.core.server.utils.*;
import com.edatasite.workforce.gwt.payroll.client.rpc.PayrollService;
import com.lowagie.text.Document;
import com.lowagie.text.pdf.PdfWriter;
import org.apache.commons.lang.WordUtils;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.support.WfmMessageSource;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.edatasite.workforce.gwt.core.client.ui.lookup.LookUpConstants.EMPLOYEE;

/**
 * Created with IntelliJ IDEA.
 * User: Bunyod Xalilov
 * Date: 3/18/15
 * Time: 12:21 AM
 * To change this template use File | Settings | File Templates.
 */
public class SinglePayrunPdfHandler extends AbstractITextPostPdfHandler implements IPostPDFHandler, PDFConstants {

    private static Date processDate;

    @Autowired
    protected PayrollService payrollService;
    @Autowired
    private EmployeePayrollSettingsManager employeePayrollSettingsManager;
    @Autowired
    private EmployeeManager employeeManager;
    @Autowired
    private PayslipPaymentsManager payslipPaymentsManager;
    @Autowired
    private CurrencyService currencyService;
    @Autowired
    private CurrencyManager currencyManager;
    @Autowired
    private CrmAccountManager crmAccountManager;
    @Autowired
    private ProjectManager projectManager;
    @Autowired
    private CompanyPayrollSettingsManager companyPayrollSettingsManager;
    @Autowired
    private PayslipTableItemManager payslipTableItemManager;
    @Autowired
    private CommonService commonService;
    @Autowired
    @Qualifier("payrollLocalizer")
    protected WfmMessageSource payrollLocalizer;
    @Autowired
    private AttendanceRawDataManager attendanceRawDataManager;
    @Autowired
    private GenericSettingsManager genericSettingsManager;
    @Autowired
    @Qualifier("wfmLocalizer")
    private WfmMessageSource wfmLocalizer;
    @Autowired
    private UserEmailSettingsManager userEmailSettingsManager;

    private DecimalFormat numberFormat;
    private static final DecimalFormat exRateNumberFormat = new DecimalFormat(",##0.0000");
    private DateFormat dateFormat;

    private String period;
    private String employeeCode;

    @Override
    public ITextGenericPdfData buildPdfDocument(Object dataClass, Document document, PdfWriter writer) throws IOException {
        return null;
    }

    @Override
    protected ITextGenericPdfData buildPdfDocumentCustomise(Object dataClass, EdsCompany company, boolean hasPhantom) {
        ITextGenericPdfData pdfData = new ITextGenericPdfData();
        pdfData.setPdfViewType(ITextPdfViewTypeEnum.BASEINVOICE);
        ITextBaseInvoice baseInvoice = new ITextBaseInvoice();
        pdfData.setBaseInvoice(baseInvoice);
        PayslipTableRequestObject singlePayrunData = (PayslipTableRequestObject) dataClass;
        EdsFinancialSettings fs = financialSettingsManager.getFinancialSettings();
        numberFormat = getPriceScaleNumberFormat(fs);

        EdsPayslipTableItem payslipTableItem = null;
        if (singlePayrunData.getObjectID() != null) {
            singlePayrunData = payrollService.getSinglePayrunPdfData(singlePayrunData.getObjectID());
            payslipTableItem = payslipTableItemManager.get(singlePayrunData.getObjectID());
        }

        HashMap<String, CustomisedITextTable> customData = new HashMap<>();
        if (genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.PAYSLIP_FROM_TIMESHEET)) {
            ListingFilterParameter lfp = new ListingFilterParameter();
            lfp.setStartDate(singlePayrunData.getFromDate());
            lfp.setEndDate(singlePayrunData.getToDate());
            lfp.setEmployeeId(singlePayrunData.getEmployeeId());
            List<DailyOvertimeData> dailyOvertimeDataFromTimesheet = attendanceRawDataManager.getDailyOvertimeData(lfp);

            CustomisedITextTable table = new CustomisedITextTable();
            customData.put("SALARY_FROM_TIMESHEET", table);
            table.addColumnOrder("DATE", "TIMESHEET_HOUR", "OVERTIME_HOUR", "ABSENCEHOUR", "DAY_OFF", "TIMESLOT_HOUR", "DAY_NAME");
            BigDecimal actualTotal = BigDecimal.ZERO;
            BigDecimal plannedTotal = BigDecimal.ZERO;
            BigDecimal overTimeTotal = BigDecimal.ZERO;
            BigDecimal oneDayTimeSlotHour = BigDecimal.ONE;
            boolean isOne = true;
            SimpleDateFormat dayFormat = new SimpleDateFormat("EEEE");
            for (DailyOvertimeData item : dailyOvertimeDataFromTimesheet) {
                String date = item.getDate() != null ? String.valueOf(item.getDate().getDate()) : "";
                String timesheet = item.getTimeSheet() != null ? item.getTimeSheet().toString() : "";
                String overtimeHour = item.getOvertimeHour() != null ? item.getOvertimeHour().toString() : "";
                String absenseHour = item.getAbsenceHour() != null ? item.getAbsenceHour().toString() : "";
                String dayOff = item.isDayOff() != null ? item.isDayOff().toString() : "";
                String timeslotHour = item.getTimeslotHour() != null ? item.getTimeslotHour().toString() : "";
                String dayName = dayFormat.format(item.getDate());
                if (!item.isDayOff() && !item.isHoliday() && isOne) {
                    oneDayTimeSlotHour = item.getTimeslotHour();
                    isOne = false;
                }
                actualTotal = actualTotal.add(item.getTimeSheet());
                plannedTotal = plannedTotal.add(item.getTimeslotHour());
                overTimeTotal = overTimeTotal.add(item.getOvertimeHour());

                table.addRow(date, timesheet, overtimeHour, absenseHour, dayOff, timeslotHour, dayName);
            }

            CustomisedITextTable timesheetTotalTable = new CustomisedITextTable();
            timesheetTotalTable.addColumnOrder(PDFConstants.COLUMN_VALUE);
            BigDecimal allowanceAndBasicSalaryTotal = singlePayrunData.getAllowanceAndBasicSalaryTotal();
            BigDecimal dailyRate = BigDecimal.ZERO;
            if (plannedTotal.compareTo(BigDecimal.ZERO) >= 0) {
                dailyRate = allowanceAndBasicSalaryTotal.divide(plannedTotal, 2, RoundingMode.HALF_UP);
            }
            if (oneDayTimeSlotHour.compareTo(BigDecimal.ZERO) == 0) {
                oneDayTimeSlotHour = BigDecimal.ONE;
            }
            BigDecimal hourlyRate = dailyRate.divide(oneDayTimeSlotHour, 2, RoundingMode.HALF_UP);
            BigDecimal overtimeTotalAmount = overTimeTotal.multiply(hourlyRate).multiply(BigDecimal.valueOf(2));

            timesheetTotalTable.addRowWithCode("ACTUAL_TOTAL", actualTotal.toString());
            timesheetTotalTable.addRowWithCode("PLANNED_TOTAL", plannedTotal.toString());
            timesheetTotalTable.addRowWithCode("OVERTIME_TOTAL", overTimeTotal.toString());
            timesheetTotalTable.addRowWithCode("NET_SALARY", numberFormat.format(allowanceAndBasicSalaryTotal.setScale(2, RoundingMode.HALF_UP)));
            timesheetTotalTable.addRowWithCode("DAILY_RATE", numberFormat.format(dailyRate.setScale(2, RoundingMode.HALF_UP)));
            timesheetTotalTable.addRowWithCode("HOURLY_RATE", numberFormat.format(hourlyRate.setScale(2, RoundingMode.HALF_UP)));
            timesheetTotalTable.addRowWithCode("OVERTIME_AMOUNT_TOTAL", numberFormat.format(overtimeTotalAmount.setScale(2, RoundingMode.HALF_UP)));

            customData.put("TIMESHEET_TOTAL_TABLE", timesheetTotalTable);
        }

        EdsUser user = userManager.getUser();
        String curSymbol = singlePayrunData.getCurrency() != null ? singlePayrunData.getCurrency() : getCompanyCurrencySymbol();
        String baseCur = getCompanyCurrencySymbol() + " ";
        String curName = getCompanyCurrencyName();
        String curFullName = getCompanyCurrencyFullName();

        dateFormat = company.getCompanySettings() != null ? new SimpleDateFormat(company.getCompanySettings().getShortDateFormat()) : SimpleDateFormat.getDateInstance();
        pdfData.setCompanyData(getCompanyData(user.getCompany(), true, hasPhantom));
        BigDecimal totalPayment = BigDecimal.ZERO;
        BigDecimal grossSalary = BigDecimal.ZERO;
        BigDecimal totalAdditional = BigDecimal.ZERO;
        BigDecimal totalOvertime = BigDecimal.ZERO;
        BigDecimal totalLiving = BigDecimal.ZERO;
        BigDecimal totalDeductions = BigDecimal.ZERO;
        Integer daysOfMonth;
        BigDecimal daysOfPresence = BigDecimal.ZERO;
        MonthlyOvertimeDataWithRates overtimeDataWithRates = null;
        EdsEmployeePayrollSettings wpsSettings = null;
        if (singlePayrunData.getEmployeeId() != null) {
            wpsSettings = employeePayrollSettingsManager.getEmployeeSettingValue(singlePayrunData.getEmployeeId(), CustomFormConstants.WPS_NUMBER);
        }

        List projectStaff = null;
        if (singlePayrunData.getEmployeeId() != null && singlePayrunData.getObjectID() != null) {
            String period = singlePayrunData.getMonthId() + 1 + "/" + singlePayrunData.getYear();
            projectStaff = projectManager.getEmployeeSalaryReport(singlePayrunData.getEmployeeId(), period,
                    singlePayrunData.getObjectID());
        }

        String ref = "PS" + (singlePayrunData.getYear() != null ? singlePayrunData.getYear() : "") +
                (singlePayrunData.getMonthId() != null ? singlePayrunData.getMonthId() : "") +
                (singlePayrunData.getEmployeeCode() != null ? singlePayrunData.getEmployeeCode() : "");

        if (singlePayrunData.getProcessDate() != null) {
            processDate = singlePayrunData.getProcessDate();
        }

        CustomisedITextTable headerData = new CustomisedITextTable();
        Map<String, String> header = new HashMap<>();

        header.put("CREATOR_LABEL", wfmLocalizer.localize("creator"));
        header.put("APPROVER_LABEL", wfmLocalizer.localize("approver"));
        header.put("NAME_LABEL", wfmLocalizer.localize("employee"));
        header.put("CODE_LABEL", wfmLocalizer.localize("employeeCode"));
        header.put("PROCESSED_DATE_LABEL", wfmLocalizer.localize("processDate"));
        header.put("APPROVED_DATE_LABEL", payrollLocalizer.localize("approveDate"));
        header.put("PAYMENT_PERIOD_LABEL", wfmLocalizer.localize("period"));
        header.put("WPS_NO_LABEL", wfmLocalizer.localize("wpsNumber"));
        header.put("REFERENCE_LABEL", wfmLocalizer.localize("reference"));
        header.put("TOTAL_LABEL", wfmLocalizer.localize("total"));
        header.put("DEDUCTIONS_LABEL", wfmLocalizer.localize("totalDeductions"));
        header.put("EXPENSES_LABEL", wfmLocalizer.localize("totalExpenses"));
        header.put("PAYMENT_LABEL", wfmLocalizer.localize("totalAllowances"));
        header.put("TOTALS_THIS_PERIOD_LABEL", payrollLocalizer.localize("totals"));
        header.put("TOTAL_GROSS_PAY_LABEL", payrollLocalizer.localize("totalGrossPay"));

        header.put(CREATOR, singlePayrunData.getCreator());
        header.put(APPROVER, singlePayrunData.getApprover());
        header.put(NAME, singlePayrunData.getEmployeeName());
        header.put(NUMBER, singlePayrunData.getEmployeeCode());
        header.put(CustomFormConstants.PAYROLL_STARTER.DRIVER_NUMBER, singlePayrunData.getDriverNumber() != null ? singlePayrunData.getDriverNumber() + "" : "");
        header.put(PERIOD_START_DATE, dateFormat.format(singlePayrunData.getFromDate()));
        header.put(PERIOD_END_DATE, dateFormat.format(singlePayrunData.getToDate()));
        header.put(PERIOD_FROM_TO, String.format("%1$tB %1$te to %2$tB %2$te", singlePayrunData.getFromDate(), singlePayrunData.getToDate()));
        header.put(CREATED_DATE, singlePayrunData.getCreatedDate() != null ? dateFormat.format(singlePayrunData.getCreatedDate()) : "");
        if (ServerUtils.getUserLocale().getLanguage().equals("uz")) {
            header.put(PROCESSED_DATE, ServerUtils.convertToUzbDateFormat(dateFormat.format(processDate != null ? processDate : user.getUserDate(new Date()))));
        } else {
            header.put(PROCESSED_DATE, dateFormat.format(processDate != null ? processDate : user.getUserDate(new Date())));
        }
        if (ServerUtils.getUserLocale().getLanguage().equals("uz")) {
            header.put(APPROVED_DATE, singlePayrunData.getApproveDate() != null ? ServerUtils.convertToUzbDateFormat(dateFormat.format(singlePayrunData.getApproveDate())) : "");
        } else {
            header.put(APPROVED_DATE, singlePayrunData.getApproveDate() != null ? dateFormat.format(singlePayrunData.getApproveDate()) : "");
        }
        if (ServerUtils.getUserLocale().getLanguage().equals("uz")) {
            header.put(PAYMENT_PERIOD, ServerUtils.convertToUzbDateFormat(singlePayrunData.getPeriod()));
        } else {
            header.put(PAYMENT_PERIOD, singlePayrunData.getPeriod());
        }
        header.put(PAYMENT_METHOD,
                singlePayrunData.getPayMethod() != null ? singlePayrunData.getPayMethod() :
                        payslipTableItem.getPayslipTable() != null && payslipTableItem.getPayslipTable().getPaymentMethod() != null ? payslipTableItem.getPayslipTable().getPaymentMethod().getName() : "");
        header.put(WPS_NO, wpsSettings != null && !"".equals(wpsSettings.getValue()) ? wpsSettings.getValue() : "n/a");
        header.put(IBAN_CODE, singlePayrunData.getiBanCode());
        header.put(REFERENCE, ref);
        header.put(REJECT, singlePayrunData.getRejectionNote() != null ? singlePayrunData.getRejectionNote() : "");
        header.put(COMMENT, singlePayrunData.getDescription() != null ? singlePayrunData.getDescription() : "");
        header.put(PAYMENT_POLICY, singlePayrunData.getPaymentPolicy() != null ? escapeHtml(singlePayrunData.getPaymentPolicy()) : "");
        header.put(DEPARTMENT, singlePayrunData.getEmployeeDepartment());
        header.put(POSITION, singlePayrunData.getEmployeePosition());
        header.put(LOCATION, singlePayrunData.getEmployeeLocation());
        header.put(HIRE_DATE, singlePayrunData.getEmployeeHireDate() != null ? dateFormat.format(singlePayrunData.getEmployeeHireDate()) : "");
        header.put(RESIGNATION_DATE, singlePayrunData.getResignationDate() != null ? dateFormat.format(singlePayrunData.getResignationDate()) : "");
        header.put(BANK_ACCOUNT_NUMBER, singlePayrunData.getBankAccountNumber() != null ? singlePayrunData.getBankAccountNumber() : "");
        header.put(BASE_CURRENCY, baseCur);
        header.put(EXCHANGE_RATE, singlePayrunData.getExchangeRate() != null ? exRateNumberFormat.format(singlePayrunData.getExchangeRate()) : "");
        double exchangeRate = singlePayrunData.getExchangeRate() != null ? singlePayrunData.getExchangeRate().doubleValue() : 1d;
        CustomisedITextTable customFields = new CustomisedITextTable();
        customFields.addColumnOrder(NAME, AMOUNTS);
        customFields.addHeaderColumns("Name", "Value");
        for (Map.Entry<String, String> entry : singlePayrunData.getCustomFields().entrySet()) {
            customFields.addRow(entry.getKey(), entry.getValue());
            if (SALARY_CURRENCY.equals(entry.getKey().toUpperCase().replaceAll("\\s", "_"))) {
                header.put(SALARY_CURRENCY, entry.getValue());
                EdsCurrency salaryCurrency = entry.getValue() != null ? currencyManager.getCurrency(entry.getValue()) : null;
                if (salaryCurrency != null) {
                    header.put(SALARY_CURRENCY_SYMBOL, salaryCurrency.getSymbol() != null ? salaryCurrency.getSymbol() : salaryCurrency.getName());
                    curFullName = salaryCurrency.getFullName();
                }
                if (singlePayrunData.getExchangeRate() == null) {
                    CurrencyLayerItem layerItem = entry.getValue() != null ? currencyService.getExchangeRateDouble(entry.getValue(), curName, processDate, 0) : null;
                    if (layerItem != null) {
                        exchangeRate = layerItem.getRate();
                    }
                }
            }
            if ("PDF Password".equals(entry.getKey()) && entry.getValue() != null && !entry.getValue().isEmpty()) {
                pdfData.setUserPassword(entry.getValue());
            }
        }
        baseInvoice.setCustomClientSupplierEntityCustomFieldTable(customFields);
        header.put(CURRENCY_NAME, curName);

        SimpleDateFormat shortDateFormat = new SimpleDateFormat("MMMM,yyyy");
        Calendar calendar = new GregorianCalendar();
        calendar.set(Calendar.DAY_OF_YEAR, singlePayrunData.getYear());
        calendar.set(Calendar.DAY_OF_MONTH, singlePayrunData.getMonthId());
        header.put(PERIOD, shortDateFormat.format(calendar.getTime()));

        this.employeeCode = singlePayrunData.getEmployeeCode();
        this.period = shortDateFormat.format(calendar.getTime());

        daysOfMonth = DateUtil.getDateInMonth(singlePayrunData.getYear(), singlePayrunData.getMonthId());
        daysOfPresence = singlePayrunData.getWorkDays() != null && singlePayrunData.getWorkDays().compareTo(BigDecimal.ZERO) > 0 ? singlePayrunData.getWorkDays() : BigDecimal.valueOf(daysOfMonth);
        header.put(DAYS_OF_MONTH, String.valueOf(daysOfMonth));
        if (singlePayrunData.getOvertimeDataWithRates() != null) {
            overtimeDataWithRates = singlePayrunData.getOvertimeDataWithRates();
            header.put(SALARY_RATE, formatCurrency(overtimeDataWithRates.getRate(), null));
            if (singlePayrunData.getEmployeePaymentType() != null && FIXED_TIMESHEET_OVERTIME_RATE.equals(singlePayrunData.getEmployeePaymentType())) {
                header.put(SALARY_RATE_TYPE, overtimeDataWithRates.getRateType() == 0 ? "hour" : "day");
                daysOfPresence = overtimeDataWithRates.getDaysOfPresence() != null && overtimeDataWithRates.getDaysOfPresence() > 0 ?
                        BigDecimal.valueOf(overtimeDataWithRates.getDaysOfPresence()) : daysOfPresence;
                header.put(WORKED_HOURS, overtimeDataWithRates.getWorkedHours() != null ? String.valueOf(overtimeDataWithRates.getWorkedHours()) : "");
            } else {
                header.put(SALARY_RATE_TYPE, "month");
            }
            header.put(REGULAR_OVERTIME, overtimeDataWithRates.getOvertimeHours() != null ? String.valueOf(overtimeDataWithRates.getOvertimeHours()) : "");
            header.put(REGULAR_OVERTIME_RATE, overtimeDataWithRates.getOvertimeRate() != null ? formatCurrency(overtimeDataWithRates.getOvertimeRate(), null) : "");
            header.put(WEEKEND_OVERTIME, overtimeDataWithRates.getWeekendOvertimeHours() != null ? String.valueOf(overtimeDataWithRates.getWeekendOvertimeHours()) : "");
            header.put(WEEKEND_OVERTIME_RATE, overtimeDataWithRates.getWeekendOvertimeRate() != null ? formatCurrency(overtimeDataWithRates.getWeekendOvertimeRate(), null) : "");
            header.put(HOLIDAY_OVERTIME, overtimeDataWithRates.getHolidayOvertimeHours() != null ? String.valueOf(overtimeDataWithRates.getHolidayOvertimeHours()) : "");
            header.put(HOLIDAY_OVERTIME_RATE, overtimeDataWithRates.getHolidayOvertimeRate() != null ? formatCurrency(overtimeDataWithRates.getHolidayOvertimeRate(), null) : "");
            if (overtimeDataWithRates.getClientId() != null) {
                EdsCrmAccount client = crmAccountManager.get(overtimeDataWithRates.getClientId());
                if (client != null) {
                    header.put(CLIENT_NAME, client.getName());
                }
            }
        }
        headerData.setHeader(header);
        baseInvoice.setCustomBillToAddress(headerData);
        baseInvoice.setExchangeRate(exRateNumberFormat.format(exchangeRate));
        baseInvoice.setCurrency(curSymbol);

        if (singlePayrunData.getAllPaymentCategories() != null && singlePayrunData.getAllPaymentCategories().size() > 0) {
            CustomisedITextTable paymentsTable = new CustomisedITextTable();
            paymentsTable.addColumnOrder(PAYMENT_, AMOUNTS, RATES, AMOUNTS_NUMERIC, UNIT, AMOUNTS_BASIC, REMARKS);
            paymentsTable.addHeaderColumns(wfmLocalizer.localize("payments"), wfmLocalizer.localize("amount"), "Rate", wfmLocalizer.localize("amount"), "Unit", "Amounts Basic", "Remarks");
            String payment, amount, rate, unit;
            BigDecimal basicSalaryAmount = BigDecimal.ZERO;
            String basicSalaryName = "";
            if (company != null && (company.getObjectID() == 57111 || company.getObjectID() == 54984)) {
                for (PaymentDeductionObject allowance : singlePayrunData.getAllPaymentCategories()) {
                    if (allowance.getCategoryItem() != null) {
                        if (LEAVE_ENCHASHMENT.equals(allowance.getCategoryItem().getCode())) {
                            basicSalaryAmount = basicSalaryAmount.add(allowance.getTotalAmount() != null ? allowance.getTotalAmount() : BigDecimal.ZERO);
                        } else if (BASIC_SALARY.equals(allowance.getCategoryItem().getCode())) {
                            basicSalaryName = allowance.getCategoryItem().getName();
                            basicSalaryAmount = basicSalaryAmount.add(allowance.getTotalAmount() != null ? allowance.getTotalAmount() : BigDecimal.ZERO);
                        } else {
                            payment = allowance.getCategoryItem() != null ? allowance.getCategoryItem().getName() : "";
                            amount = formatCurrency(allowance.getTotalAmount() != null ? allowance.getTotalAmount() : BigDecimal.ZERO, curSymbol);
                            paymentsTable.addRow(payment, amount, "", formatCurrency(allowance.getTotalAmount() != null ? allowance.getTotalAmount() : BigDecimal.ZERO, null), "", formatCurrency(allowance.getPaymentAmount(), null), escapeHtml(allowance.getRemarks()));
                            totalPayment = totalPayment.add(allowance.getTotalAmount() != null ? allowance.getTotalAmount() : BigDecimal.ZERO);
                        }
                    }
                }
                paymentsTable.addRow(basicSalaryName, formatCurrency(basicSalaryAmount, curSymbol), "", formatCurrency(basicSalaryAmount, null), "", "", "");
                totalPayment = totalPayment.add(basicSalaryAmount);
            } else {
                for (PaymentDeductionObject allowance : singlePayrunData.getAllPaymentCategories()) {
                    if (allowance.getTotalAmount().compareTo(BigDecimal.ZERO) > 0) {
                        payment = allowance.getCategoryItem() != null ? allowance.getCategoryItem().getName() : "";
                        amount = formatCurrency(allowance.getTotalAmount() != null ? allowance.getTotalAmount() : BigDecimal.ZERO, curSymbol);
                        rate = allowance.getLeaveDaysCount() != null && allowance.getLeaveDaysCount().compareTo(BigDecimal.ZERO) > 0 ?
                                formatCurrency(allowance.getTotalAmount().divide(allowance.getLeaveDaysCount(), 2, RoundingMode.HALF_UP), null) : "";
                        unit = allowance.getLeaveDaysCount() != null && allowance.getLeaveDaysCount().compareTo(BigDecimal.ZERO) > 0 ?
                                formatBigDecimal(allowance.getLeaveDaysCount()) : "";
                        paymentsTable.addRow(payment, amount, rate, formatCurrency(allowance.getTotalAmount() != null ? allowance.getTotalAmount() : BigDecimal.ZERO, null), unit, formatCurrency(allowance.getPaymentAmount(), null), escapeHtml(allowance.getRemarks()));
                        totalPayment = totalPayment.add(allowance.getTotalAmount() != null ? allowance.getTotalAmount() : BigDecimal.ZERO);

                        if (LEAVE_ENCHASHMENT.equals(allowance.getCategoryItem().getCode()) && allowance.getLeaveDaysCount() != null && allowance.getLeaveDaysCount().compareTo(BigDecimal.ZERO) > 0 && overtimeDataWithRates != null && overtimeDataWithRates.getRateType() != null && overtimeDataWithRates.getRate() != null) {
                            if (overtimeDataWithRates.getRateType() != 0 && overtimeDataWithRates.getDaysOfPresence() != null && overtimeDataWithRates.getDaysOfPresence() > 0) {
                                daysOfPresence = daysOfPresence.subtract(allowance.getLeaveDaysCount());
                            } else if (overtimeDataWithRates.getRateType() == 0 && overtimeDataWithRates.getRate().compareTo(BigDecimal.ZERO) > 0 && overtimeDataWithRates.getWorkedHours() != null && overtimeDataWithRates.getWorkedHours().compareTo(BigDecimal.ZERO) > 0) {
                                header.put(WORKED_HOURS, String.valueOf(overtimeDataWithRates.getWorkedHours().subtract(allowance.getTotalAmount() != null ? allowance.getTotalAmount() : BigDecimal.ZERO).divide(overtimeDataWithRates.getRate(), 0, RoundingMode.HALF_UP).intValue()));
                            }
                        } else if (LEAVE_ENCHASHMENT.equals(allowance.getCategoryItem().getCode()) && allowance.getLeaveDaysCount() != null) {
                            daysOfPresence = daysOfPresence.subtract(allowance.getLeaveDaysCount());
                        }
                        switch (allowance.getCategoryItem().getCode()) {
                            case BASIC_SALARY, LEAVE_ENCHASHMENT, OVERTIME_FIXED, REGULAR_OVERTIME, WEEKEND_OVERTIME, HOLIDAY_OVERTIME, ALLOWANCE_LIVING, COST_LIVING_ALLOWANCE, TRANSPORTATION_ALLOWANCE, HOUSING_ALLOWANCE ->
                                    grossSalary = grossSalary.add(allowance.getTotalAmount() != null ? allowance.getTotalAmount() : BigDecimal.ZERO);
                            default ->
                                    totalAdditional = totalAdditional.add(allowance.getTotalAmount() != null ? allowance.getTotalAmount() : BigDecimal.ZERO);
                        }
                        switch (allowance.getCategoryItem().getCode()) {
                            case ALLOWANCE_LIVING, COST_LIVING_ALLOWANCE, HOUSING_ALLOWANCE ->
                                    totalLiving = totalLiving.add(allowance.getTotalAmount() != null ? allowance.getTotalAmount() : BigDecimal.ZERO);
                            case OVERTIME_FIXED, REGULAR_OVERTIME, WEEKEND_OVERTIME, HOLIDAY_OVERTIME ->
                                    totalOvertime = totalOvertime.add(allowance.getTotalAmount() != null ? allowance.getTotalAmount() : BigDecimal.ZERO);
                        }
                    }
                }
            }
            paymentsTable.addRow("", "", "", "", "", "", "");
            paymentsTable.addRow("", "", "", "", "", "", "");
            if (totalPayment.compareTo(BigDecimal.ZERO) > 0) {
                paymentsTable.addRow(wfmLocalizer.localize("total") + " (" + curName + ")", formatCurrency(totalPayment, curSymbol), "", formatCurrency(totalPayment, null), "", "", "");
            }
            baseInvoice.setCustomProductTable(paymentsTable);
        }
        BigDecimal petrolChargeTotal = new BigDecimal(BigInteger.ZERO);
        BigDecimal salikChargeTotal = new BigDecimal(BigInteger.ZERO);
        BigDecimal basicSalary = singlePayrunData.getBasicSalary() != null ? singlePayrunData.getBasicSalary() : BigDecimal.ZERO;
        HashMap<String, List<PaymentDeductionObject>> deductionCategoryMap = new HashMap<>();
        CustomisedITextTable deductionsTable = new CustomisedITextTable();
        deductionsTable.addColumnOrder(DEDUCTIONS, AMOUNTS, RATES, AMOUNTS_NUMERIC, UNIT, AMOUNTS_BASIC, REMARKS);
        deductionsTable.addHeaderColumns(payrollLocalizer.localize("deductions"), wfmLocalizer.localize("amount"), "Rate", wfmLocalizer.localize("amount"), "Unit", "Amounts Basic", "Remarks");
        if (singlePayrunData.getAllDeductionCategories() != null && singlePayrunData.getAllDeductionCategories().size() > 0) {
            String payment, amount, rate, unit;
            BigDecimal totalAmount;
            for (PaymentDeductionObject deduction : singlePayrunData.getAllDeductionCategories()) {
                payment = deduction.getCategoryItem() != null ? deduction.getCategoryItem().getName() : "";
                totalAmount = deduction.getTotalAmount() != null ? deduction.getTotalAmount() : BigDecimal.ZERO;
                amount = formatCurrency(totalAmount, curSymbol);
                rate = deduction.getLeaveDaysCount() != null && deduction.getLeaveDaysCount().compareTo(BigDecimal.ZERO) > 0 ?
                        formatCurrency(totalAmount.divide(deduction.getLeaveDaysCount(), 2, RoundingMode.HALF_UP), null) : "";
                unit = deduction.getLeaveDaysCount() != null && deduction.getLeaveDaysCount().compareTo(BigDecimal.ZERO) > 0 ?
                        formatBigDecimal(deduction.getLeaveDaysCount()) : "";
                deductionsTable.addRow(payment, amount, rate, formatCurrency(totalAmount, null), unit, formatCurrency(deduction.getPaymentAmount(), null), escapeHtml(deduction.getRemarks()));
                totalDeductions = totalDeductions.add(totalAmount);
                if (LEAVE_DEDUCTIONS.equals(deduction.getCategoryItem().getCode()) && deduction.getLeaveDaysCount() != null) {
                    daysOfPresence = daysOfPresence.subtract(deduction.getLeaveDaysCount());
                }
                if (company.getObjectID() == 47229) {
                    if (deduction.getCategoryItem() != null && PETROL_CHARGE.equals(deduction.getCategoryItem().getCode())) {
                        petrolChargeTotal = petrolChargeTotal.add(totalAmount);
                    }
                    if (deduction.getCategoryItem() != null && SALIK_CHARGE.equals(deduction.getCategoryItem().getCode())) {
                        salikChargeTotal = salikChargeTotal.add(totalAmount);
                    }
                    List<PaymentDeductionObject> deductionObjects;
                    if (deductionCategoryMap.get(deduction.getCategoryItem().getCode()) == null) {
                        deductionObjects = new ArrayList<>();
                    } else {
                        deductionObjects = deductionCategoryMap.get(deduction.getCategoryItem().getCode());
                    }
                    deductionObjects.add(deduction);
                    deductionCategoryMap.put(deduction.getCategoryItem().getCode(), deductionObjects);
                }
            }
            //deductionsTable.addRow("&#160;", "&#160;", "&#160;", "&#160;", "&#160;", "&#160;", "&#160;");
            //deductionsTable.addRow("&#160;", "&#160;", "&#160;", "&#160;", "&#160;", "&#160;", "&#160;");
            if (singlePayrunData.getDeduction() != null && singlePayrunData.getDeduction().compareTo(BigDecimal.ZERO) > 0) {
                deductionsTable.addRow(wfmLocalizer.localize("total") + " (" + curName + ")", formatCurrency(singlePayrunData.getDeduction(), curSymbol), "", formatCurrency(singlePayrunData.getDeduction(), null), "", "", "");
            }
        }
        baseInvoice.setCustomNumberAndDatesTable(deductionsTable);
        header.put(WORKED_DAYS, formatBigDecimal(daysOfPresence));
        if (company.getObjectID() == 47229) {
            header.put(MONTHLY_SALIK, singlePayrunData.getMonthlySalik() != null ? formatBigDecimal(singlePayrunData.getMonthlySalik()) : "");
            header.put(BONUS, singlePayrunData.getAdditionalPay() != null ? formatBigDecimal(singlePayrunData.getAdditionalPay()) : "");
            header.put(USED_PETROL, singlePayrunData.getSpentFlueAmount() != null ? formatBigDecimal(singlePayrunData.getSpentFlueAmount()) : "");
            header.put(BASIC_SALARY, formatBigDecimal(basicSalary));
            header.put(GROSS_COMMISSION, formatBigDecimal(basicSalary));
            header.put("MONTHLY_INCOME_AFTER_SALIK_AND_BONUS", singlePayrunData.getMonthlyCollection() != null ? formatBigDecimal(singlePayrunData.getMonthlyCollection()) : "");
            header.put(SALIK_CHARGE, formatBigDecimal(salikChargeTotal));
            header.put(PETROL_CHARGE, formatBigDecimal(petrolChargeTotal));
            header.put("SALIK_CHARGE_AND_PETROL_CHARGE_TOTAL", formatBigDecimal(petrolChargeTotal.add(salikChargeTotal)));
            header.put("NET_COMMISSION", formatBigDecimal(basicSalary.subtract((petrolChargeTotal.add(salikChargeTotal)))));
            BigDecimal monthlyIncome = new BigDecimal(BigInteger.ZERO);
            monthlyIncome = monthlyIncome.add(singlePayrunData.getMonthlyCollection() != null ? singlePayrunData.getMonthlyCollection() : new BigDecimal(BigInteger.ZERO));
            monthlyIncome = monthlyIncome.add(singlePayrunData.getMonthlySalik() != null ? singlePayrunData.getMonthlySalik() : new BigDecimal(BigInteger.ZERO));
            monthlyIncome = monthlyIncome.add(singlePayrunData.getAdditionalPay() != null ? singlePayrunData.getAdditionalPay() : new BigDecimal(BigInteger.ZERO));
            header.put(MONTHLY_INCOME, formatBigDecimal(monthlyIncome));

            EdsEmployee employee = employeeManager.get(singlePayrunData.getEmployeeId());
            List<EdsPaymentDeduction> categories = employee.getCategories();
            if (categories != null && categories.size() > 0) {
                PaymentDeductionObject deduction;
                HashMap<String, List<PaymentDeductionObject>> loanDetailsMap = new HashMap<>();
                for (EdsPaymentDeduction category : categories) {
                    deduction = category.getRPC();
                    if (deduction.isLoan() && !category.isFullPayed() && !deduction.isPaymentCategory()) {
                        deduction.setRemainingAmount(category.getRemainingAmount());
                        List<PaymentDeductionObject> deductionObjects;
                        if (loanDetailsMap.get(deduction.getCategoryItem().getCode()) == null) {
                            deductionObjects = new ArrayList<>();
                        } else {
                            deductionObjects = loanDetailsMap.get(deduction.getCategoryItem().getCode());
                        }
                        deductionObjects.add(deduction);
                        loanDetailsMap.put(deduction.getCategoryItem().getCode(), deductionObjects);
                    }
                }

                CustomisedITextTable deductionDetailsTable = new CustomisedITextTable();
                deductionDetailsTable.addColumnOrder("DETAILS", "OPENING", "THIS_MONTH_ADD", "TOTAL", "THIS_MONTH_DED", "CLOSING");
                deductionDetailsTable.addHeaderColumns("Details", "Opening", "This Month Add.", "Total", "This Month Ded.", "Closing");
                BigDecimal openingTotal = new BigDecimal(BigInteger.ZERO);
                BigDecimal thisMonthAddTotal = new BigDecimal(BigInteger.ZERO);
                BigDecimal totalAmount = new BigDecimal(BigInteger.ZERO);
                BigDecimal thisMonthDedTotal = new BigDecimal(BigInteger.ZERO);
                BigDecimal closingTotal = new BigDecimal(BigInteger.ZERO);
                for (String code : loanDetailsMap.keySet()) {
                    if (code.equals(SALIK_CHARGE) || code.equals(PETROL_CHARGE)) {
                        continue;
                    }
                    BigDecimal opening = new BigDecimal(BigInteger.ZERO);
                    BigDecimal thisMonthAdd = new BigDecimal(BigInteger.ZERO);
                    BigDecimal total = new BigDecimal(BigInteger.ZERO);
                    BigDecimal thisMonthDed = new BigDecimal(BigInteger.ZERO);
                    BigDecimal closing = new BigDecimal(BigInteger.ZERO);

                    List<PaymentDeductionObject> loanList = loanDetailsMap.get(code);
                    String title = "";
                    for (PaymentDeductionObject loan : loanList) {
                        title = loan.getCategoryItem().getName();
                        if (loan.getStarttDate() != null) {
                            if (loan.getStarttDate().getDate().before(singlePayrunData.getToDate())) {
                                opening = opening.add(loan.getTotalAmount());
                            }
                            if (loan.getStarttDate().getDate().before(singlePayrunData.getToDate()) &&
                                    loan.getStarttDate().getDate().after(singlePayrunData.getToDate())) {
                                thisMonthAdd = thisMonthAdd.add(loan.getTotalAmount());
                            }
                        }
                        total = total.add((opening.add(thisMonthAdd)));
                        if (deductionCategoryMap.get(code) != null) {
                            List<PaymentDeductionObject> deductions = deductionCategoryMap.get(code);
                            for (PaymentDeductionObject deductionObject : deductions) {
                                thisMonthDed = thisMonthDed.add(deductionObject.getTotalAmount());
                                /*if (deductionObject.getStarttDate().getDate() != null && deductionObject.getStarttDate().getDate().before(singlePayrunData.getFromDate())
                                        && deductionObject.getStarttDate().getDate().after(singlePayrunData.getToDate())) {
                                }*/
                            }
                        }
                    }

                    closing = total.subtract(thisMonthDed);
                    openingTotal = openingTotal.add(opening);
                    thisMonthAddTotal = thisMonthAddTotal.add(thisMonthAdd);
                    totalAmount = totalAmount.add(total);
                    thisMonthDedTotal = thisMonthDedTotal.add(thisMonthDed);
                    closingTotal = closingTotal.add(closing);

                    deductionDetailsTable.addRow(title, formatBigDecimal(opening), formatBigDecimal(thisMonthAdd), formatBigDecimal(total), formatBigDecimal(thisMonthDed), formatBigDecimal(closing));
                }

                deductionDetailsTable.addRow("Total", formatBigDecimal(openingTotal), formatBigDecimal(thisMonthAddTotal), formatBigDecimal(totalAmount), formatBigDecimal(thisMonthDedTotal), formatBigDecimal(closingTotal));
                baseInvoice.setCustomGroupTaxRateTable(deductionDetailsTable);
                header.put("DEDUCTIONS", formatBigDecimal(thisMonthDedTotal));
                header.put("COMMISSION_PAYABLE", formatBigDecimal((basicSalary.subtract((petrolChargeTotal.add(salikChargeTotal)))).subtract(thisMonthDedTotal)));
            }
        }

        if (singlePayrunData.getEmployeeExpenses() != null && singlePayrunData.getEmployeeExpenses().getExpenses() != null && singlePayrunData.getEmployeeExpenses().getExpenses().length > 0) {
            CustomisedITextTable expensesTable = new CustomisedITextTable();
            expensesTable.addColumnOrder(EXPENSES, PAID, AMOUNTS, AMOUNTS_NUMERIC, TYPE);
            expensesTable.addHeaderColumns(commonLocalizer.localize("expense"), "Paid From", "Amount", wfmLocalizer.localize("amount"), "Type");
            String amount, type;
            BigDecimal amountsNumeric;
            for (ExpenseData expenseData : singlePayrunData.getEmployeeExpenses().getExpenses()) {
                amountsNumeric = expenseData.getAmount() != null ? BigDecimal.valueOf(expenseData.getAmount())
                        : BigDecimal.ZERO;
                amount = formatCurrency(amountsNumeric, curSymbol);
                type = expenseData.getPaymentType() != null
                        && expenseData.getPaymentType() != 0 ? "Payment"
                        : expenseData.getPaymentType() != null
                        ? "Deduction"
                        : "";
                expensesTable.addRow(expenseData.getTitle(), expenseData.getAccount(), amount, formatCurrency(amountsNumeric, null), type);
            }
            baseInvoice.setCustomExpenseTable(expensesTable);
        }

        if (singlePayrunData.getDailyOvertimeData() != null && singlePayrunData.getDailyOvertimeData().size() > 0) {
            BigDecimal regularOvertime = null, holidayOvertime = null, weekendOvertime = null;
            DailyOvertimeData dailyOvertimeData = null;
            CustomisedITextTable overtimeData = new CustomisedITextTable();
            overtimeData.addColumnOrder(DATE, NOT_AVAILABLE, REGULAR_OVERTIME, HOLIDAY_OVERTIME, WEEKEND_OVERTIME);
            overtimeData.addHeaderColumns("Date", "Site", "O.T", "H/O.T", "H/O.T");
            Date day = DateUtil.getMonthFirstDay(singlePayrunData.getFromDate());
            day = DateUtil.resetTime(day);
            if (singlePayrunData.getDailyOvertimeData().get(day) != null) {
                dailyOvertimeData = singlePayrunData.getDailyOvertimeData().get(day);
                if (dailyOvertimeData.isHoliday()) {
                    holidayOvertime = dailyOvertimeData.getOvertimeHour();
                } else if (dailyOvertimeData.isDayOff()) {
                    weekendOvertime = dailyOvertimeData.getOvertimeHour();
                } else {
                    regularOvertime = dailyOvertimeData.getOvertimeHour();
                }
                overtimeData.addRow(dateFormat.format(dailyOvertimeData.getDate()), "", regularOvertime != null && regularOvertime.compareTo(BigDecimal.ZERO) > 0
                        ? formatCurrency(regularOvertime, null) : "", holidayOvertime != null && holidayOvertime.compareTo(BigDecimal.ZERO) > 0
                        ? formatCurrency(holidayOvertime, null) : "", weekendOvertime != null && weekendOvertime.compareTo(BigDecimal.ZERO) > 0
                        ? formatCurrency(weekendOvertime, null) : ""
                );
            } else {
                overtimeData.addRow(dateFormat.format(day), "", "", "");
            }
            for (int i = 2; i <= DateUtil.getDateInMonth(singlePayrunData.getYear(), singlePayrunData.getMonthId()); i++) {
                day = DateUtil.addDays(day, 1);
                holidayOvertime = BigDecimal.ZERO;
                regularOvertime = BigDecimal.ZERO;
                weekendOvertime = BigDecimal.ZERO;
                if (singlePayrunData.getDailyOvertimeData().get(day) != null) {
                    dailyOvertimeData = singlePayrunData.getDailyOvertimeData().get(day);
                    if (dailyOvertimeData.isHoliday()) {
                        holidayOvertime = dailyOvertimeData.getOvertimeHour();
                    } else if (dailyOvertimeData.isDayOff()) {
                        weekendOvertime = dailyOvertimeData.getOvertimeHour();
                    } else {
                        regularOvertime = dailyOvertimeData.getOvertimeHour();
                    }
                    overtimeData.addRow(dateFormat.format(dailyOvertimeData.getDate()), "", regularOvertime != null && regularOvertime.compareTo(BigDecimal.ZERO) > 0
                            ? formatCurrency(regularOvertime, null) : "", holidayOvertime != null && holidayOvertime.compareTo(BigDecimal.ZERO) > 0
                            ? formatCurrency(holidayOvertime, null) : "", weekendOvertime != null && weekendOvertime.compareTo(BigDecimal.ZERO) > 0
                            ? formatCurrency(weekendOvertime, null) : ""
                    );
                } else {
                    overtimeData.addRow(dateFormat.format(day), "", "", "");
                }
            }
            baseInvoice.setCustomPOTable(overtimeData);
        }

        CustomisedITextTable totalTable = new CustomisedITextTable();
        Long fraction = singlePayrunData.getTotal().setScale(0, RoundingMode.DOWN).longValue();
        Integer cents = singlePayrunData.getTotal().setScale(2, RoundingMode.HALF_UP).remainder(BigDecimal.ONE).multiply(BigDecimal.valueOf(100)).intValue();

        String fractionWord;
        String centsWord;
        String totalInWords;
        NumberToWord numberToWord;
        EdsUserEmailSettings userSettings = userEmailSettingsManager.getUserSettings(user);
        if (userSettings != null && "ru".equals(userSettings.getInternationalization())) {
            numberToWord = new NumberToWord_ru();
            fractionWord = WordUtils.capitalize(numberToWord.toWord(fraction).replaceAll("\\d*/\\d*", ""));
            centsWord = WordUtils.capitalize(numberToWord.toWord(cents).replaceAll("\\d*/\\d*", ""));
            totalInWords = fractionWord + " " + curFullName + (cents > 0 ? " и " + centsWord + " " + getCompanyCurrencyFrname() : "");
        } else if (userSettings != null && "uz".equals(userSettings.getInternationalization())) {
            numberToWord = new NumberToWord_uz_lotin();
            fractionWord = WordUtils.capitalize(numberToWord.toWord(fraction).replaceAll("\\d*/\\d*", ""));
            centsWord = WordUtils.capitalize(numberToWord.toWord(cents).replaceAll("\\d*/\\d*", ""));
            totalInWords = fractionWord + " " + curFullName + (cents > 0 ? " va " + centsWord + " " + getCompanyCurrencyFrname() : "");
        } else {
            numberToWord = new NumberToWord_en();
            fractionWord = WordUtils.capitalize(numberToWord.toWord(fraction).replaceAll("\\d*/\\d*", ""));
            centsWord = WordUtils.capitalize(numberToWord.toWord(cents).replaceAll("\\d*/\\d*", ""));
            totalInWords = fractionWord + " " + curFullName + (cents > 0 ? " and " + centsWord + " " + getCompanyCurrencyFrname() : "");
        }

        totalTable.addColumnOrder(
                ADDITIONAL_PAYMENT,
                TOTAL_GROSS_PAY,
                DEDUCTIONS,
                TOTAL,
                TOTAL_YTD,
                DEDUCTIONS_NUMERIC,
                TOTAL_NUMERIC,
                GROSS_SALARY,
                TOTAL_ADDITIONAL,
                TOTAL_LIVING,
                TOTAL_OVERTIME,
                TOTAL_IN_BASE,
                TOTAL_IN_WORDS,
                EXP_TOTAL);
        totalTable.addRow(
                formatCurrency(singlePayrunData.getAllowance(), null),
                formatCurrency(totalPayment, null),
                formatCurrency(singlePayrunData.getDeduction(), null),
                formatCurrency(singlePayrunData.getTotal(), null),
                formatCurrency(singlePayrunData.getTotalPayToDate(), null),
                totalDeductions.compareTo(BigDecimal.ZERO) > 0 ? formatCurrency(totalDeductions, null) : "",
                formatCurrency(singlePayrunData.getTotal(), null),
                grossSalary.compareTo(BigDecimal.ZERO) > 0 ? formatCurrency(grossSalary, null) : "",
                totalAdditional.compareTo(BigDecimal.ZERO) > 0 ? formatCurrency(totalAdditional, null) : "",
                totalLiving.compareTo(BigDecimal.ZERO) > 0 ? formatCurrency(totalLiving, null) : "",
                totalOvertime.compareTo(BigDecimal.ZERO) > 0 ? formatCurrency(totalOvertime, null) : "",
                formatCurrency(singlePayrunData.getTotal().divide(BigDecimal.valueOf(exchangeRate), 4, RoundingMode.HALF_UP), null),
                totalInWords,
                singlePayrunData.getEmployeeExpenses() != null ? formatCurrency(singlePayrunData.getEmployeeExpenses().getPaymentAmount(), null) : "");
        baseInvoice.setCustomTotalTable(totalTable);

        CustomisedITextTable projectTable = new CustomisedITextTable();
        projectTable.setName("Project Table");
        projectTable.addColumnOrder(PROJECT_NUMBER, PROJECT_NAME, CATEGORY_CODE, SALARY, SITE, "FOOD_AMOUNT", "DAYS", "BA", "OT1", "OT2", "OT3", "OT1A", "OT2A", "OT3A", TOTAL, PERIOD, "CONTRACT_NUMBER", "SITE_ALLOWANCE_DAILY");
        int size = projectStaff != null ? projectStaff.size() : 0;
        double total = 0.0;
        for (int i = 0; i < size; i++) {
            Object[] o = (Object[]) projectStaff.get(i);
            String projectnumber = (String) o[0];
            String projectname = (String) o[1];
            String category = (String) o[2];
            double basiCSalary = (double) o[3];
            double site = (double) o[4];
            double foodmount = (double) o[5];
            double days = (double) o[6];
            String monthyear = (String) o[7];
            double overtimeHours = (double) o[8];
            double weekendovertimehours = (double) o[9];
            double holidayovertimehours = (double) o[10];
            double regularovertimerate = (double) o[11];
            double weekendovertimerate = (double) o[12];
            double holidayovertimerate = (double) o[13];
            String contractNumber = (String) o[14];
            if (site < 0) {
                site = 0.00;
            }
            double ot1A = ((basiCSalary + site) * overtimeHours * regularovertimerate) / (30 * 8);

            double ot2A = ((basiCSalary + site) * weekendovertimehours * weekendovertimerate) / (30 * 8);

            double ot3A = ((basiCSalary + site) * holidayovertimehours * holidayovertimerate) / (30 * 8);

            double siteAllowanceDaily = site * days / 30;

            Calendar c = Calendar.getInstance();
            if (monthyear != null && !"".equals(monthyear)) {
                SimpleDateFormat formatter = new SimpleDateFormat("MM/yyyy");
                Date date = new Date();
                try {
                    date = formatter.parse(monthyear);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                c.setTime(date);
            }
            int monthMaxDays = c.getActualMaximum(Calendar.DAY_OF_MONTH);

            double ba = (basiCSalary + site + foodmount) * days / monthMaxDays;
            double totalamount = ba + ot1A + ot2A;
            total = total + totalamount;

            projectTable.addRow(projectnumber, escapeHtml(projectname), escapeHtml(category), getMoneyFormat(basiCSalary), getMoneyFormat(site), getMoneyFormat(foodmount),
                    getMoneyFormat(days), getMoneyFormat(ba), getMoneyFormat(overtimeHours), getMoneyFormat(weekendovertimehours), getMoneyFormat(holidayovertimehours), getMoneyFormat(ot1A), getMoneyFormat(ot2A), getMoneyFormat(ot3A), getMoneyFormat(totalamount), monthyear, contractNumber, getMoneyFormat(siteAllowanceDaily));
        }
        header.put("BYPROJECT_TOTAL", getMoneyFormat(total));
        baseInvoice.setCustomFooterData(projectTable);

        baseInvoice.setTermsConditionsName("Payment is reported for the period of " + singlePayrunData.getFromDate().getDate() + " to " + singlePayrunData.getToDate().getDate() + " " + singlePayrunData.getMonth() + ", " + singlePayrunData.getYear());

        CustomisedITextTable customTable = new CustomisedITextTable();
        Map<String, LinkedHashMap<String, Map<String, String>>> singlePayrunCustomFields = new HashMap<>();
        if (payslipTableItem != null && payslipTableItem.getCustomFields() != null) {
            List<CompanyCustomFieldItem> customFieldItems = CustomFieldsUtils.setRPCCustomFieldItems(payslipTableItem.getCustomFields(),
                    commonService.getCompanyCustomFields(ViewName.SinglePayrun));
            if (customFieldItems != null && customFieldItems.size() > 0) {
                LinkedHashMap<String, Map<String, String>> itemCusFields = new LinkedHashMap<>();
                for (CompanyCustomFieldItem item : customFieldItems) {
                    if (item != null) {
                        Map<String, String> cols = new HashMap<>();
                        cols.put(COLUMN_NAME, escapeHtml(item.getFieldName()));
                        if (CompanyCustomFieldItem.DATE.equals(item.getDataType())) {
                            SimpleDateFormat compShortDateFormat = getCompanyShortDateFormat(userManager.getUser().getCompany());
                            cols.put(COLUMN_VALUE, item.getFieldDateNonConvertedValue() != null ? escapeHtml(compShortDateFormat.format(item.getFieldDateNonConvertedValue().getNonConvertedDate())) : "");
                        } else if (CompanyCustomFieldItem.NUMBER.equals(item.getDataType())) {
                            cols.put(COLUMN_VALUE, item.getFieldStringValue() != null && !"".equals(item.getFieldStringValue()) ? escapeHtml(numberFormat.format(Double.valueOf(item.getFieldStringValue()))) : "");
                        } else {
                            cols.put(COLUMN_VALUE, escapeHtml(item.getFieldStringValue()));
                            SelectItem entityType = item.getEntityType();
                            if (entityType != null && entityType.getReferenceCode() != null) {
                                String code = entityType.getReferenceCode();
                                if (code.equals(EMPLOYEE)) {
                                }
                            }
                        }
                        if (item.getFieldName() != null) {
                            itemCusFields.put(item.getFieldName(), cols);
                        }
                    }
                }
                singlePayrunCustomFields.put("SINGLE_PAYRUN", itemCusFields);
                customTable.setCustomFields(singlePayrunCustomFields);
            }
        }
        customData.put("CUSTOM_FIELD", customTable);
        customData.put("EMPLOYEE_CUSTOM_FIELD", getEmployeeCustomField(singlePayrunData));
        customData.put("CATEGORIES_GROUP_DATA", getCategoriesGroupData(singlePayrunData.getAllPaymentCategories()));
        customData.put("ADDITIONAL_PAYMENT_CATEGORIES", getAddtionalPaymentCategoriesData(singlePayrunData.getAllPaymentCategories()));
        pdfData.setCustomData(customData);

        return pdfData;
    }

    private CustomisedITextTable getEmployeeCustomField(PayslipTableRequestObject singlePayrunData) {
        CustomisedITextTable customFieldTable = new CustomisedITextTable();

        EdsEmployee employee = employeeManager.get(singlePayrunData.getEmployeeId());
        ArrayList<CompanyCustomFieldItem> employeeCustomFields = CustomFieldsUtils.setRPCCustomFieldItems(employee.getCustomFields(),
                this.commonService.getCompanyCustomFields(ViewName.Employee));
        EdsUser user = uploadManager.getUser();
        DecimalFormat decimalFormat = new DecimalFormat(",##0.00");
        Map<String, LinkedHashMap<String, Map<String, String>>> customFields = new HashMap<>();

        if (employeeCustomFields != null && employeeCustomFields.size() > 0) {
            LinkedHashMap<String, Map<String, String>> itemCusFields = new LinkedHashMap<>();
            for (CompanyCustomFieldItem field : employeeCustomFields) {
                if (field != null) {
                    Map<String, String> cols = new HashMap<>();
                    cols.put(COLUMN_NAME, escapeHtml(field.getFieldName()));
                    if (CompanyCustomFieldItem.DATE.equals(field.getDataType())) {
                        String dateValue = "";
                        EdsCompany company = userManager.getUser().getCompany();
                        SimpleDateFormat shortDateFormat = getCompanyShortDateFormat(company);
                        if (field.getFieldDateNonConvertedValue() != null) {
                            if (company.getLocale() != null && "ru".equals(company.getLocale())) {
                                Locale ruLocale = new Locale("ru", "RU");
                                SimpleDateFormat ruDateFormat = new SimpleDateFormat(shortDateFormat.toPattern(), ruLocale);
                                dateValue = field.getFieldDateNonConvertedValue().getNonConvertedDate() != null ? ruDateFormat.format(field.getFieldDateNonConvertedValue().getNonConvertedDate()) : "";
                            } else {
                                dateValue = field.getFieldDateNonConvertedValue().getNonConvertedDate() != null ? shortDateFormat.format(field.getFieldDateNonConvertedValue().getNonConvertedDate()) : "";
                            }
                        }
                        cols.put(COLUMN_VALUE, dateValue);
                    } else if (CompanyCustomFieldItem.NUMBER.equals(field.getDataType())) {
                        cols.put(COLUMN_VALUE, StringUtils.isNotEmpty(field.getFieldStringValue()) ? escapeHtml(decimalFormat.format(Double.valueOf(field.getFieldStringValue()))) : "—");
                    } else if (UI_TYPE_HTML_TEXTAREA.equals(field.getUiType())) {
                        if (field.getFieldStringValue() != null && !field.getFieldStringValue().isEmpty()) {
                            String html = field.getFieldStringValue();
                            org.jsoup.nodes.Document doc = Jsoup.parse(html);
                            StringBuilder textValue = new StringBuilder();
                            if (Objects.equals(user.getCompany().getObjectID(), 90826)) {
                                Elements pElements = doc.getElementsByTag("p");
                                for (Element element : pElements) {
                                    textValue.append(element.text()).append("<br/>");
                                }
                            } else {
                                textValue.append(doc.body().text());
                            }
                            cols.put(COLUMN_VALUE, textValue.toString());
                        } else {
                            cols.put(COLUMN_VALUE, "");
                        }
                    } else {
                        cols.put(COLUMN_VALUE, StringUtils.isNotEmpty(field.getFieldStringValue()) ? escapeHtml(field.getFieldStringValue()) : "—");
                    }
                    if (field.getFieldName() != null) {
                        itemCusFields.put(field.getFieldName(), cols);
                    }
                }
            }
            customFields.put("EMPLOYEE", itemCusFields);
            customFieldTable.setCustomFields(customFields);
        }

        return customFieldTable;
    }

    private CustomisedITextTable getCategoriesGroupData(List<PaymentDeductionObject> allPaymentCategories) {
        CustomisedITextTable categoriesGroup = new CustomisedITextTable();
        Map<String, ArrayList<PaymentDeductionObject>> itemMap = new LinkedHashMap<>();
        categoriesGroup.addColumnOrder(CATEGORY, "COUNT", AMOUNTS);

        for (PaymentDeductionObject item : allPaymentCategories) {
            if (item.getCategoryItem() != null) {
                if (itemMap.containsKey(item.getCategoryItem().getName())) {
                    itemMap.get(item.getCategoryItem().getName()).add(item);
                } else {
                    itemMap.put(item.getCategoryItem().getName(), new ArrayList<>(Collections.singletonList(item)));
                }
            }
        }

        for (Map.Entry<String, ArrayList<PaymentDeductionObject>> entry : itemMap.entrySet()) {
            BigDecimal totalAmount = BigDecimal.ZERO;
            int count = 0;
            boolean first = true;
            String countAmount = "";
            for (PaymentDeductionObject valueItem : entry.getValue()) {
                totalAmount = totalAmount.add(valueItem.getTotalAmount());
                count = count + 1;
                if (first) {
                    countAmount = numberFormat.format(totalAmount.setScale(2, RoundingMode.HALF_UP));
                    first = false;
                }
            }
            String categoryName = entry.getKey();
            String countRow = count + " x " + countAmount;
            String amount = numberFormat.format(totalAmount.setScale(2, RoundingMode.HALF_UP));
            categoriesGroup.addRow(categoryName, countRow, amount);
        }

        return categoriesGroup;
    }

    private CustomisedITextTable getAddtionalPaymentCategoriesData(List<PaymentDeductionObject> allPaymentCategories) {
        CustomisedITextTable paymentCategories = new CustomisedITextTable();
        paymentCategories.addColumnOrder(DATE, CATEGORY, AMOUNTS, REFERENCE);

        String dateString = "";
        String category = "";
        String amount = "";
        String reference = "";

        Collections.sort(allPaymentCategories, (pd1, pd2) -> {
            if (pd1.getAdditionalPaymentDate() == null) return 1;
            if (pd2.getAdditionalPaymentDate() == null) return -1;
            return pd1.getAdditionalPaymentDate().getNonConvertedDate().compareTo(pd2.getAdditionalPaymentDate().getNonConvertedDate());
        });

        for (PaymentDeductionObject allowance : allPaymentCategories) {
            dateString = allowance.getAdditionalPaymentDate() != null ? dateFormat(allowance.getAdditionalPaymentDate().getNonConvertedDate(), false) : "";
            category = allowance.getCategoryItem() != null ? escapeHtml(allowance.getCategoryItem().getName()) : "";
            amount = allowance.getPaymentAmount() != null ? numberFormat.format(allowance.getPaymentAmount().setScale(2, RoundingMode.HALF_UP)) : "";
            reference = escapeHtml(allowance.getReference());
            paymentCategories.addRow(dateString, category, amount, reference);
        }

        return paymentCategories;
    }

    protected boolean getPagingOnTop() {
        return true;
    }

    @Override
    protected Object getDataClass(HttpServletRequest request) {
        PayslipTableRequestObject requestObject = new PayslipTableRequestObject();
        requestObject.setObjectID(Integer.valueOf(request.getParameter("objectID")));
        if (!"".equals(request.getParameter("pdfTemplateID"))) {
            requestObject.setPdfTemplateID(Integer.valueOf(request.getParameter("pdfTemplateID")));
        }
        String processDateString = request.getParameter("processDate");
        if (processDateString != null) {
            processDate = new Date(Long.valueOf(processDateString) + (long) new Date().getTimezoneOffset() * 60 * 1000);
        } else {
            processDate = null;
        }
        return new PayslipTableRequestObject();
    }

    private String getFileName(Object dataClass) {
        PayslipTableRequestObject payslipRequestObject = (PayslipTableRequestObject) dataClass;
        PayslipTableRequestObject singlePayrunData = payrollService.getSinglePayrunPdfData(payslipRequestObject.getObjectID());
        StringBuilder fileName = new StringBuilder(commonLocalizer.localize("payslip"));
        if (singlePayrunData != null) {
            if (singlePayrunData.getYear() != null) {
                fileName.append(singlePayrunData.getYear()).append("_");
            }
            if (singlePayrunData.getMonth() != null) {
                fileName.append(singlePayrunData.getMonth()).append("_");
            }
            if (singlePayrunData.getEmployeeName() != null) {
                fileName.append(singlePayrunData.getEmployeeName().replaceAll("^.*-> ", ""));
            }
        }
        return fileName.toString();
    }

    @Override
    protected void setFileName(EdsUser user, Object dataClass) {
        setFileName(getFileName(dataClass));
    }

    @Override
    protected String getTableName(Object dataClass) {
        if (!(dataClass instanceof PayslipTableRequestObject)) {
            return commonLocalizer.localize("payslip");
        }
        PayslipTableRequestObject object = (PayslipTableRequestObject) dataClass;
        String[] fullMonths = new String[]{"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
        String[] fullMonthsInUzb = new String[]{"Yanvar", "Fevral", "Mart", "Aprel", "May", "Iyun", "Iyul", "Avgust", "Sentabr", "Oktyabr", "Noyabr", "Dekabr"};
        String[] shortMonths = new String[]{"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
        String[] shortMonthsInUzb = new String[]{"Yan", "Fev", "Mar", "Apr", "May", "Iyun", "Iyul", "Avg", "Sen", "Okt", "Noy", "Dek"};

        String tableName = "";
        if (StringUtils.isNotEmpty(object.getEmployeeCode())) {
            tableName = object.getEmployeeCode() + " ";
        }
        if (StringUtils.isNotEmpty(object.getMonth())) {
            if (ServerUtils.getUserLocale().getLanguage().equals("uz")) {
                if (object.getMonth().length() > 2) {
                    for (int i = 0; i < fullMonths.length; i++) {
                        if (fullMonths[i].equals(object.getMonth())) {
                            tableName += fullMonthsInUzb[i] + ", ";
                        }
                    }
                } else if (object.getMonth().length() == 2) {
                    for (int i = 0; i < shortMonths.length; i++) {
                        if (shortMonths[i].equals(object.getMonth())) {
                            tableName += shortMonthsInUzb[i] + ", ";
                        }
                    }
                }
            } else {
                tableName += object.getMonth() + ", ";
            }
        }
        if (object.getYear() != null) {
            tableName += object.getYear();
        }
        return tableName;
    }

    private String getCompanyCurrencySymbol() {
        String symbol = "$";
        EdsFinancialSettings financialSettings = financialSettingsManager.getFinancialSettings();
        EdsCurrency currency = null;
        if (financialSettings != null) {
            currency = financialSettings.getCurrency();
        }
        if (currency != null) {
            symbol = currency.getSymbol() != null ? currency.getSymbol() : currency.getName();
        }
        return symbol;
    }

    private String getCompanyCurrencyName() {
        String name = "USD";
        EdsFinancialSettings financialSettings = financialSettingsManager.getFinancialSettings();
        EdsCurrency currency = null;
        if (financialSettings != null) {
            currency = financialSettings.getCurrency();
        }
        if (currency != null) {
            name = currency.getName();
        }
        return name;
    }


    private String getCompanyCurrencyFullName() {
        String name = "US Dollar";
        EdsFinancialSettings financialSettings = financialSettingsManager.getFinancialSettings();
        EdsCurrency currency = null;
        if (financialSettings != null) {
            currency = financialSettings.getCurrency();
        }
        if (currency != null) {
            name = currency.getFullName();
        }
        return name;
    }

    private String getCompanyCurrencyFrname() {
        String frname = "Cent";
        EdsFinancialSettings financialSettings = financialSettingsManager.getFinancialSettings();
        EdsCurrency currency = null;
        if (financialSettings != null) {
            currency = financialSettings.getCurrency();
        }
        if (currency != null) {
            frname = currency.getFrname();
        }
        return frname;
    }


    @Override
    protected Integer getCustomisedPDFTemplateId(Object object) {
        if (object instanceof PayslipTableRequestObject) {
            return ((PayslipTableRequestObject) object).getPdfTemplateID();
        }
        return null;
    }

    private String formatCurrency(BigDecimal amount, String currencySymbol) {
        if (amount == null) return "";
        return (currencySymbol != null ? currencySymbol + " " : "") + numberFormat.format(amount.setScale(2, RoundingMode.HALF_UP));
    }

    private String formatBigDecimal(BigDecimal amount) {
        if (amount.remainder(BigDecimal.ONE).compareTo(BigDecimal.ZERO) != 0) {
            return amount.setScale(1, RoundingMode.HALF_UP).toString();
        } else {
            return amount.setScale(0, RoundingMode.HALF_UP).toString();
        }
    }

    @Override
    protected PdfReferenceCodeNameEnum getPdfCodeName(Object dataClass) {
        return PdfReferenceCodeNameEnum.SINGLE_PAYRUN;
    }
}
