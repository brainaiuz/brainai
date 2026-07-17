package com.edatasite.workforce.gwt.core.server.commons;

import com.edatasite.workforce.core.domain.*;
import com.edatasite.workforce.core.domain.accounting.EdsAccount;
import com.edatasite.workforce.core.domain.payrolluk.EdsCompanyPayrollSettings;
import com.edatasite.workforce.core.domain.payrolluk.EdsPayrollCategory;
import com.edatasite.workforce.core.tools.StringUtil;
import com.edatasite.workforce.gwt.core.client.enums.GenericSettingsEnum;
import com.edatasite.workforce.gwt.core.client.rpc.CommonService;
import com.edatasite.workforce.gwt.core.client.rpc.CompanyCustomFieldItem;
import com.edatasite.workforce.gwt.core.client.rpc.ExpenseData;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.Frequency;
import com.edatasite.workforce.gwt.core.client.ui.customfields.ViewName;
import com.edatasite.workforce.gwt.core.client.ui.view.PaymentDeductionObject;
import com.edatasite.workforce.gwt.core.client.ui.view.PaymentDeductionSelectItem;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.db.AccountingManager;
import com.edatasite.workforce.gwt.core.server.db.EmployeeManager;
import com.edatasite.workforce.gwt.core.server.db.ExpenseReportManager;
import com.edatasite.workforce.gwt.core.server.db.SickRequestDurationManager;
import com.edatasite.workforce.gwt.core.server.db.payroll.CompanyPayrollSettingsManager;
import com.edatasite.workforce.gwt.core.server.db.payroll.PayrollCategoryManager;
import com.edatasite.workforce.gwt.core.server.db.settings.GenericSettingsManager;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.localization.PdfLocalizationName;
import com.edatasite.workforce.gwt.core.server.utils.CustomFieldsUtils;
import com.edatasite.workforce.gwt.core.server.utils.NumberToWord;
import com.edatasite.workforce.gwt.core.server.utils.NumberToWord_en;
import com.edatasite.workforce.gwt.core.server.utils.NumberToWord_uz_lotin;
import com.edatasite.workforce.gwt.payroll.client.rpc.GroupPayrunData;
import com.edatasite.workforce.gwt.payroll.client.rpc.PayrollService;
import com.edatasite.workforce.gwt.payroll.client.rpc.PayslipFilter;
import com.edatasite.workforce.gwt.payroll.client.rpc.SinglePayrunItem;
import com.google.common.collect.Lists;
import org.apache.commons.lang.WordUtils;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.support.WfmMessageSource;
import org.springframework.context.support.WfmResourceBundleMessageSource;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

/**
 * Created by Dilsh0d Madrahimov on 8/23/2016 1:56 PM.
 */
public class GroupPayrunViewExcelHandler extends BaseExcelHandler implements Constants {

    @Autowired
    private PayrollService payrollService;
    @Autowired
    private GenericSettingsManager genericSettings;
    @Autowired
    protected CompanyPayrollSettingsManager companyPayrollSettingsManager;
    @Autowired
    private PayrollCategoryManager categoryManager;
    @Autowired
    private ExpenseReportManager expenseReportManager;
    @Autowired
    private AccountingManager accountingManager;
    @Autowired
    private EmployeeManager employeeManager;
    @Autowired
    private CommonService commonService;
    @Autowired
    private SickRequestDurationManager sickRequestDurationManager;

    @Autowired
    @Qualifier("commonLocalizer")
    private WfmMessageSource commonLocalizer;

    private static final Logger logger = LoggerFactory.getLogger(GroupPayrunViewExcelHandler.class);
    @Autowired
    @Qualifier("allReferenceWfmMessageSource")
    protected WfmResourceBundleMessageSource excelReferenceMessageSource;

    HSSFWorkbook workbook;
    HSSFSheet sheet;
    int rowIndex = 0;
    SimpleDateFormat shortDateFormat;
    DecimalFormat defaultScaleFormat;
    final static SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy");
    boolean fromSummary;
    DataFormat format;

    protected HSSFWorkbook getWorkBook(Object object) {
        rowIndex = 0;
        ListingFilterParameter filterParameter = (ListingFilterParameter) object;
        PayslipFilter filter = new PayslipFilter();
        filter.setObjectID(filterParameter.getObjectId());
        fromSummary = Constants.VIEW.equals(filterParameter.getViewType());
        Date currentDate = new Date();
        final int currentYear = Integer.valueOf(yearFormat.format(currentDate));
        filter.setFromGroupTaxi(genericSettings.isSettingsEnabled(GenericSettingsEnum.TAXI_PAYRUN_ENABLED));
        filter.setYear(currentYear);
        filter.setEmpCodeAdjoined(false);
        filter.setFromExcelHandler(true);
        GroupPayrunData groupPayrunData = payrollService.getPayslipTable(filter);
        EdsUser user = userManager.getUser();
        String shortDateFormatStr = user.getCompany().getCompanySettings().getShortDateFormat();
        shortDateFormat = new SimpleDateFormat(shortDateFormatStr != null ? shortDateFormatStr : "MMMM dd, yyyy", Locale.ENGLISH);
        defaultScaleFormat = new DecimalFormat(",##0.00");

        try {
            workbook = new HSSFWorkbook();
            sheet = workbook.createSheet(commonLocalizer.localize("groupPayrun", "Group Payrun"));
            sheet.setDefaultColumnWidth(20);
            sheet.autoSizeColumn(0);
            sheet.setColumnWidth(0, 10000);

            format = workbook.createDataFormat();

            if (user.getCompany().getObjectID().equals(312959)) {
                createItemTableForDMDC(groupPayrunData);
            } else {
                createHeaderTable(user, groupPayrunData);
                createItemTable(groupPayrunData);
                createTotalTable(groupPayrunData);
            }
            return workbook;


        } catch (Exception exp) {
            exp.printStackTrace();
            logger.error("Cannot generate " + filename + " excel report, exception: " + exp);
        }


        return null;
    }

    private void createHeaderTable(EdsUser user, GroupPayrunData groupPayrunData) {
        int cellIndex = 3;
        HSSFRow row = generateOneRowWithEmptyCell(rowIndex, cellIndex);
        sheet.getRow(rowIndex).getCell(cellIndex).setCellValue(user.getCompany().getName());
        sheet.getRow(rowIndex).getCell(cellIndex).setCellStyle(getHeaderTitleStyleCell());

        rowIndex++;
        row = generateOneRowWithEmptyCell(rowIndex, cellIndex);
        sheet.getRow(rowIndex).getCell(cellIndex).setCellValue(commonLocalizer.localize("groupPayrun", "Group Payrun"));
        sheet.getRow(rowIndex).getCell(cellIndex).setCellStyle(getHeaderTitleStyleCell());

        rowIndex++;
        row = generateOneRowWithEmptyCell(rowIndex, cellIndex);
        if (ServerUtils.getUserLocale().getLanguage().equals("uz")) {
            sheet.getRow(rowIndex).getCell(cellIndex).setCellValue(ServerUtils.convertToUzbDateFormat(ServerUtils.shortDateFormat(user.getUserDate(new Date()), user)) + " holatiga");
        } else {
            sheet.getRow(rowIndex).getCell(cellIndex).setCellValue("  " + excelReferenceMessageSource.localize("EPAsOf", " As Of") + "  " + ServerUtils.shortDateFormat(user.getUserDate(new Date()), user));
        }
        sheet.getRow(rowIndex).getCell(cellIndex).setCellStyle(getHeaderTitleStyleCell());
        rowIndex++;

        //Just empty row
        cellIndex = 0;
        rowIndex++;
        row = generateOneRowWithEmptyCell(rowIndex, cellIndex);

        //Created By
        rowIndex++;
        row = generateOneRowWithEmptyCell(rowIndex, cellIndex);
        sheet.getRow(rowIndex).getCell(cellIndex).setCellValue(commonLocalizer.localize(PdfLocalizationName.createdBy, "Created By") + ":");
        sheet.getRow(rowIndex).getCell(cellIndex).setCellStyle(getHeaderFieldStyleCell());

        HSSFCell createdByCell = sheet.getRow(rowIndex).createCell(cellIndex + 1);
        createdByCell.setCellValue(groupPayrunData.getCreator() != null ? groupPayrunData.getCreator().getName() : "");

        //Approver
        rowIndex++;
        row = generateOneRowWithEmptyCell(rowIndex, cellIndex);
        sheet.getRow(rowIndex).getCell(cellIndex).setCellValue(commonLocalizer.localize(PdfLocalizationName.approver, "Approver") + ":");
        sheet.getRow(rowIndex).getCell(cellIndex).setCellStyle(getHeaderFieldStyleCell());

        HSSFCell approverCell = sheet.getRow(rowIndex).createCell(cellIndex + 1);
        approverCell.setCellValue(groupPayrunData.getApprover() != null ? groupPayrunData.getApprover().getName() : "");

        //Frequency
        rowIndex++;
        row = generateOneRowWithEmptyCell(rowIndex, cellIndex);
        sheet.getRow(rowIndex).getCell(cellIndex).setCellValue(commonLocalizer.localize(PdfLocalizationName.frequency, "Frequency") + ":");
        sheet.getRow(rowIndex).getCell(cellIndex).setCellStyle(getHeaderFieldStyleCell());

        HSSFCell frequencyCell = sheet.getRow(rowIndex).createCell(cellIndex + 1);
        String frequency = "";
        if (groupPayrunData.getFrequency() != null) {
            frequency = commonLocalizer.localize(Objects.requireNonNull(Frequency.getByID(groupPayrunData.getFrequency())).getCode(), Objects.requireNonNull(Frequency.getByID(groupPayrunData.getFrequency())).getName());
        } else {
            frequency = commonLocalizer.localize(Objects.requireNonNull(Frequency.getByID(1)).getName(), Objects.requireNonNull(Frequency.getByID(1)).getName());
        }
        frequencyCell.setCellValue(frequency);

        //Period
        rowIndex++;
        row = generateOneRowWithEmptyCell(rowIndex, cellIndex);
        sheet.getRow(rowIndex).getCell(cellIndex).setCellValue(commonLocalizer.localize(PdfLocalizationName.period, "Period") + ":");
        sheet.getRow(rowIndex).getCell(cellIndex).setCellStyle(getHeaderFieldStyleCell());

        HSSFCell periodCell = sheet.getRow(rowIndex).createCell(cellIndex + 1);
        String period = "";
        if (groupPayrunData.getYear() != null) {
            if (ServerUtils.getUserLocale().getLanguage().equals("uz")) {
                period = ServerUtils.convertToUzbDateFormat(groupPayrunData.getMonth() + ", " + groupPayrunData.getYear());
            } else {
                period = groupPayrunData.getMonth() + ", " + groupPayrunData.getYear();
            }
        } else {
            period = groupPayrunData.getMonth();
        }
        periodCell.setCellValue(period);

        //Process Date
        rowIndex++;
        row = generateOneRowWithEmptyCell(rowIndex, cellIndex);
        sheet.getRow(rowIndex).getCell(cellIndex).setCellValue(commonLocalizer.localize(PdfLocalizationName.processDate, "Process Date") + ":");
        sheet.getRow(rowIndex).getCell(cellIndex).setCellStyle(getHeaderFieldStyleCell());

        HSSFCell processDateCell = sheet.getRow(rowIndex).createCell(cellIndex + 1);
        String processDate = "";
        if (groupPayrunData.getProcessDate() != null) {
            processDate = shortDateFormat.format(groupPayrunData.getProcessDate().getNonConvertedDate());
        } else {
            processDate = shortDateFormat.format(getMonthLastDate(new Date(groupPayrunData.getYear() - 1900, groupPayrunData.getMonthID(), 1), user.getCompany()));
        }
        processDateCell.setCellValue(processDate);
    }

    private void createItemTable(GroupPayrunData groupPayrunData) {
        int cellIndex = 0;
        int additionalColumns = 0;
        if (groupPayrunData.getPensionType() != null && groupPayrunData.getPensionValue() != null && BigDecimal.ZERO.compareTo(groupPayrunData.getPensionValue()) < 0) {
            additionalColumns++;
        }
        final EdsPayrollCategory expenseCategory = categoryManager.getCategoryByCode(EXPENSE_REPORT);
        PaymentDeductionSelectItem expenseCategoryItem = null;
        if (expenseCategory != null) {
            expenseCategoryItem = expenseCategory.createPaymentDeductionSelectItem();
        }
        final EdsCurrency baseCurrency = this.financialSettingsManager.getFinancialSettings().getCurrency();
        final boolean empInBase = groupPayrunData.getCurrency() == null || groupPayrunData.getCurrency().getId().equals(baseCurrency.getObjectID());
        final String expensePaidFromAccount = this.getCompanyPayrollSettings(EXPENSE_PAID_ACCOUNT);
        final EdsAccount paidFromAccount = !StringUtil.isEmpty(expensePaidFromAccount) ? accountingManager.get(Integer.valueOf(expensePaidFromAccount)) : null;

        int cellCount = 12 + additionalColumns;

        //Just empty row
        rowIndex++;
        HSSFRow emptyCell = generateOneRowWithEmptyCell(rowIndex, cellIndex);

        rowIndex++;
        HSSFRow row = generateOneRowWithEmptyCell(rowIndex, cellCount);
        row.setHeight((short) 500);
        int colCounter = 0;
        sheet.getRow(rowIndex).getCell(colCounter).setCellValue(commonLocalizer.localize(PdfLocalizationName.employeeCode, "Employee Code"));
        sheet.getRow(rowIndex).getCell(colCounter).setCellStyle(getBlueStyleCell());
        colCounter++;
        sheet.getRow(rowIndex).getCell(colCounter).setCellValue(commonLocalizer.localize(PdfLocalizationName.firstName, "FirstName"));
        sheet.getRow(rowIndex).getCell(colCounter).setCellStyle(getBlueStyleCell());
        colCounter++;
        sheet.getRow(rowIndex).getCell(colCounter).setCellValue(commonLocalizer.localize(PdfLocalizationName.lastName, "LastName"));
        sheet.getRow(rowIndex).getCell(colCounter).setCellStyle(getBlueStyleCell());
        colCounter++;
        /*sheet.getRow(rowIndex).getCell(colCounter).setCellValue(commonLocalizer.localize(PdfLocalizationName.from, "From"));
        sheet.getRow(rowIndex).getCell(colCounter).setCellStyle(getBlueStyleCell());
        colCounter++;
        sheet.getRow(rowIndex).getCell(colCounter).setCellValue(commonLocalizer.localize(PdfLocalizationName.to, "To"));
        sheet.getRow(rowIndex).getCell(colCounter).setCellStyle(getBlueStyleCell());
        colCounter++;*/
        sheet.getRow(rowIndex).getCell(colCounter).setCellValue(commonLocalizer.localize(PdfLocalizationName.basicSalary, "Basic Salary"));
        sheet.getRow(rowIndex).getCell(colCounter).setCellStyle(getBlueStyleCell());
        colCounter++;
        sheet.getRow(rowIndex).getCell(colCounter).setCellValue(commonLocalizer.localize(PdfLocalizationName.allowance, "Allowance"));
        sheet.getRow(rowIndex).getCell(colCounter).setCellStyle(getBlueStyleCell());
        colCounter++;
        if (groupPayrunData.getPensionType() != null && groupPayrunData.getPensionValue() != null && BigDecimal.ZERO.compareTo(groupPayrunData.getPensionValue()) < 0) {
            sheet.getRow(rowIndex).getCell(colCounter).setCellValue(commonLocalizer.localize(PdfLocalizationName.pension, "Gov/Pension"));
            sheet.getRow(rowIndex).getCell(colCounter).setCellStyle(getBlueStyleCell());
            colCounter++;
        }
        sheet.getRow(rowIndex).getCell(colCounter).setCellValue(commonLocalizer.localize(PdfLocalizationName.deduction, "Deduction"));
        sheet.getRow(rowIndex).getCell(colCounter).setCellStyle(getBlueStyleCell());
        colCounter++;
        sheet.getRow(rowIndex).getCell(colCounter).setCellValue(commonLocalizer.localize(PdfLocalizationName.expense, "Expense"));
        sheet.getRow(rowIndex).getCell(colCounter).setCellStyle(getBlueStyleCell());
        colCounter++;
        /*sheet.getRow(rowIndex).getCell(colCounter).setCellValue(commonLocalizer.localize(PdfLocalizationName.comments, "Comments"));
        sheet.getRow(rowIndex).getCell(colCounter).setCellStyle(getBlueStyleCell());
        colCounter++;*/
        sheet.getRow(rowIndex).getCell(colCounter).setCellValue(commonLocalizer.localize(PdfLocalizationName.totalPay, "Total Pay"));
        sheet.getRow(rowIndex).getCell(colCounter).setCellStyle(getBlueStyleCell());
        colCounter++;
        if (fromSummary) {
            sheet.getRow(rowIndex).getCell(colCounter).setCellValue(commonLocalizer.localize(PdfLocalizationName.status, "Status"));
            sheet.getRow(rowIndex).getCell(colCounter).setCellStyle(getBlueStyleCell());
            colCounter++;
            /*sheet.getRow(rowIndex).getCell(colCounter).setCellValue(commonLocalizer.localize(PdfLocalizationName.rejectionReason, "Rejection Reason"));
            sheet.getRow(rowIndex).getCell(colCounter).setCellStyle(getBlueStyleCell());
            colCounter++;*/
        }

        rowIndex++;
        CellStyle blueStyleCell = workbook.createCellStyle();
        blueStyleCell.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
        blueStyleCell.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        blueStyleCell.setDataFormat(format.getFormat("#,##0.00"));
        for (SinglePayrunItem item : groupPayrunData.getTableItems()) {
            BigDecimal amount = BigDecimal.ZERO;
            final List<ExpenseData> expenses = Lists.newLinkedList();
            final List<EdsExpenseReport> linkedExpenses = expenseReportManager.getPayslipTableItemRelatedExpenseClaims(item.getObjectID());

            for (EdsExpenseReport exp : linkedExpenses) {
                final boolean expInBase = empInBase || exp.getCurrency() == null || exp.getCurrency().getObjectID().equals(baseCurrency.getObjectID());
                ExpenseData expData;
                double totalExp = expInBase ? exp.getBaseTotal().doubleValue() : exp.getTotal().doubleValue();
                if (PARTIALLY_PAID.equals(exp.getStatus().getCode())) {
                    double paid = exp.getPaidTotal(expInBase).doubleValue();
                    totalExp -= paid;
                } else if (EXPENSE_PAID.equals(exp.getStatus().getCode())) {
                    totalExp = exp.getPaidTotalByPayslip(item.getObjectID(), expInBase).doubleValue();
                }
                if (exp.getAccount() != null) {
                    expData = new ExpenseData(exp.getObjectID(),
                            exp.getTitle(),
                            totalExp,
                            expInBase,
                            exp.getAccount().getObjectID(),
                            exp.getAccount().getName());
                } else {
                    if (paidFromAccount == null) {
                        expData = new ExpenseData(exp.getObjectID(),
                                exp.getTitle(),
                                totalExp,
                                expInBase,
                                null,
                                "");
                    } else {
                        expData = new ExpenseData(exp.getObjectID(),
                                exp.getTitle(),
                                totalExp,
                                expInBase,
                                paidFromAccount.getObjectID(),
                                paidFromAccount.getName());
                    }
                }
                if (expData.isInBaseCurrency() && groupPayrunData.getExchangeRate() != null) {
                    expData.setAmount(expData.getAmount() * groupPayrunData.getExchangeRate().doubleValue());
                    expData.setInBaseCurrency(false);
                }
                expenses.add(expData);
                amount = amount.add(BigDecimal.valueOf(expData.getAmount()));
            }
            if (!expenses.isEmpty()) {
                expenses.sort((o1, o2) -> o2.getObjectID().compareTo(o1.getObjectID()));
                final PaymentDeductionObject expensePayment = new PaymentDeductionObject();

                expensePayment.setPaymentAmount(amount);
                expensePayment.setExpenses(expenses.toArray(new ExpenseData[]{}));
                expensePayment.setCategoryItem(expenseCategoryItem);
                item.setEmployeeExpenses(expensePayment);
            }

            row = generateOneRowWithEmptyCell(rowIndex, cellCount);
            cellIndex = 0;
            sheet.getRow(rowIndex).getCell(cellIndex).setCellValue(item.getEmployeeCode());
            cellIndex++;
            sheet.getRow(rowIndex).getCell(cellIndex).setCellValue(item.getFirstName());
            cellIndex++;
            sheet.getRow(rowIndex).getCell(cellIndex).setCellValue(item.getLastName());
            cellIndex++;
            /*sheet.getRow(rowIndex).getCell(cellIndex).setCellValue((item.getFromDate() != null && item.getFromDate().getNonConvertedDate() != null) ? shortDateFormat.format(item.getFromDate().getNonConvertedDate()) : "");
            sheet.getRow(rowIndex).getCell(cellIndex).setCellStyle(blueStyleCell);
            cellIndex++;
            sheet.getRow(rowIndex).getCell(cellIndex).setCellValue((item.getToDate() != null && item.getToDate().getNonConvertedDate() != null) ? shortDateFormat.format(item.getToDate().getNonConvertedDate()) : "");
            sheet.getRow(rowIndex).getCell(cellIndex).setCellStyle(blueStyleCell);
            cellIndex++;*/
            sheet.getRow(rowIndex).getCell(cellIndex).setCellValue(item.getBasicSalary() != null ? item.getBasicSalary().doubleValue() : BigDecimal.ZERO.doubleValue());
            sheet.getRow(rowIndex).getCell(cellIndex).setCellStyle(blueStyleCell);
            cellIndex++;
            sheet.getRow(rowIndex).getCell(cellIndex).setCellValue(item.getAllowance() != null ? item.getAllowance().doubleValue() : BigDecimal.ZERO.doubleValue());
            sheet.getRow(rowIndex).getCell(cellIndex).setCellStyle(blueStyleCell);
            cellIndex++;
            if (groupPayrunData.getPensionType() != null && groupPayrunData.getPensionValue() != null && BigDecimal.ZERO.compareTo(groupPayrunData.getPensionValue()) < 0) {
                sheet.getRow(rowIndex).getCell(cellIndex).setCellValue(item.getPensionAmount() != null ? item.getPensionAmount().doubleValue() : BigDecimal.ZERO.doubleValue());
                sheet.getRow(rowIndex).getCell(cellIndex).setCellStyle(blueStyleCell);
                cellIndex++;
            }
            sheet.getRow(rowIndex).getCell(cellIndex).setCellValue(item.getDeduction() != null ? item.getDeduction().doubleValue() : BigDecimal.ZERO.doubleValue());
            sheet.getRow(rowIndex).getCell(cellIndex).setCellStyle(blueStyleCell);
            cellIndex++;
            sheet.getRow(rowIndex).getCell(cellIndex).setCellValue(item.getEmployeeExpenses() != null ? item.getEmployeeExpenses().getPaymentAmount().doubleValue() : BigDecimal.ZERO.doubleValue());
            sheet.getRow(rowIndex).getCell(cellIndex).setCellStyle(blueStyleCell);
            cellIndex++;
            /*sheet.getRow(rowIndex).getCell(cellIndex).setCellValue(item.getDescription());
            cellIndex++;*/
            sheet.getRow(rowIndex).getCell(cellIndex).setCellValue(item.getTotal() != null ? item.getTotal().doubleValue() : BigDecimal.ZERO.doubleValue());
            sheet.getRow(rowIndex).getCell(cellIndex).setCellStyle(blueStyleCell);
            if (fromSummary) {
                cellIndex++;
                if (item.isApproved()) {
                    item.setStatus(commonLocalizer.localize(PdfLocalizationName.approved, "Approved"));
                } else {
                    if (item.getApprovalRejectionStatus() == null) {
                        item.setStatus(commonLocalizer.localize(PdfLocalizationName.pending, "Pending"));
                    } else if (Constants.PAYRUN_STATUS_REJECTED.equals(item.getApprovalRejectionStatus())) {
                        item.setStatus("Rejected");
                    }
                }
                sheet.getRow(rowIndex).getCell(cellIndex).setCellValue(item.getStatus());
                sheet.getRow(rowIndex).getCell(cellIndex).setCellStyle(blueStyleCell);
                cellIndex++;
                /*sheet.getRow(rowIndex).getCell(cellIndex).setCellValue(!item.isApproved() ? commonLocalizer.localize(PdfLocalizationName.rejectionReason, "Rejection Reason") : "");
                sheet.getRow(rowIndex).getCell(cellIndex).setCellStyle(blueStyleCell);*/
            }

            rowIndex++;
        }

    }

    //companyId: 312959
    private void createItemTableForDMDC(GroupPayrunData groupPayrunData) {
        int cellIndex = 0;
        int cellCount = 18;

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH);

        HSSFRow row = generateOneRowWithEmptyCell(rowIndex, cellCount);
        row.setHeight((short) 500);
        int colCounter = 0;
        sheet.getRow(rowIndex).getCell(colCounter).setCellValue("Record Type");
        sheet.getRow(rowIndex).getCell(colCounter).setCellStyle(getBlueStyleCell());
        colCounter++;
        sheet.getRow(rowIndex).getCell(colCounter).setCellValue("Employee Unique ID");
        sheet.getRow(rowIndex).getCell(colCounter).setCellStyle(getBlueStyleCell());
        colCounter++;
        sheet.getRow(rowIndex).getCell(colCounter).setCellValue("Employee Name");
        sheet.getRow(rowIndex).getCell(colCounter).setCellStyle(getBlueStyleCell());
        colCounter++;
        sheet.getRow(rowIndex).getCell(colCounter).setCellValue("Routing Code");
        sheet.getRow(rowIndex).getCell(colCounter).setCellStyle(getBlueStyleCell());
        colCounter++;
        sheet.getRow(rowIndex).getCell(colCounter).setCellValue("Employee Account with Agent");
        sheet.getRow(rowIndex).getCell(colCounter).setCellStyle(getBlueStyleCell());
        colCounter++;
        sheet.getRow(rowIndex).getCell(colCounter).setCellValue("Pay Start Date");
        sheet.getRow(rowIndex).getCell(colCounter).setCellStyle(getBlueStyleCell());
        colCounter++;
        sheet.getRow(rowIndex).getCell(colCounter).setCellValue("Pay End Date");
        sheet.getRow(rowIndex).getCell(colCounter).setCellStyle(getBlueStyleCell());
        colCounter++;
        sheet.getRow(rowIndex).getCell(colCounter).setCellValue("Days in Period");
        sheet.getRow(rowIndex).getCell(colCounter).setCellStyle(getBlueStyleCell());
        colCounter++;
        sheet.getRow(rowIndex).getCell(colCounter).setCellValue("Income Fixed Component");
        sheet.getRow(rowIndex).getCell(colCounter).setCellStyle(getBlueStyleCell());
        colCounter++;
        sheet.getRow(rowIndex).getCell(colCounter).setCellValue("Income Variable Component");
        sheet.getRow(rowIndex).getCell(colCounter).setCellStyle(getBlueStyleCell());
        colCounter++;
        sheet.getRow(rowIndex).getCell(colCounter).setCellValue("Days on leave for period");
        sheet.getRow(rowIndex).getCell(colCounter).setCellStyle(getBlueStyleCell());
        colCounter++;
        sheet.getRow(rowIndex).getCell(colCounter).setCellValue("Housing Allowance");
        sheet.getRow(rowIndex).getCell(colCounter).setCellStyle(getBlueStyleCell());
        colCounter++;
        sheet.getRow(rowIndex).getCell(colCounter).setCellValue("Conveyance Allowance");
        sheet.getRow(rowIndex).getCell(colCounter).setCellStyle(getBlueStyleCell());
        colCounter++;
        sheet.getRow(rowIndex).getCell(colCounter).setCellValue("Medical Allowance");
        sheet.getRow(rowIndex).getCell(colCounter).setCellStyle(getBlueStyleCell());
        colCounter++;
        sheet.getRow(rowIndex).getCell(colCounter).setCellValue("Annual Passage Allowance");
        sheet.getRow(rowIndex).getCell(colCounter).setCellStyle(getBlueStyleCell());
        colCounter++;
        sheet.getRow(rowIndex).getCell(colCounter).setCellValue("Overtime Allowance");
        sheet.getRow(rowIndex).getCell(colCounter).setCellStyle(getBlueStyleCell());
        colCounter++;
        sheet.getRow(rowIndex).getCell(colCounter).setCellValue("Other Allowance");
        sheet.getRow(rowIndex).getCell(colCounter).setCellStyle(getBlueStyleCell());
        colCounter++;
        sheet.getRow(rowIndex).getCell(colCounter).setCellValue("Leave Encashment");
        sheet.getRow(rowIndex).getCell(colCounter).setCellStyle(getBlueStyleCell());

        rowIndex++;
        CellStyle blueStyleCell = workbook.createCellStyle();
        blueStyleCell.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
        blueStyleCell.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        blueStyleCell.setDataFormat(format.getFormat("#,##0.00"));
        for (SinglePayrunItem item : groupPayrunData.getTableItems()) {

            EdsEmployee edsEmployee = employeeManager.get(item.getEmployeeID());
            List<CompanyCustomFieldItem> customFieldItems = CustomFieldsUtils.setRPCCustomFieldItems(edsEmployee.getCustomFields(),
                    commonService.getCompanyCustomFields(ViewName.Employee));
            String routingCode = "";
            String employeeUniqueId = "";
            String recordType = "";
            String employeeAccountWithAgent = "";
            ListingFilterParameter filterParameter = new ListingFilterParameter();
            filterParameter.setStatusCode(Constants.LR_STATUS_SS_APPROVED);
            filterParameter.setReasonCode(EdsSickRequest.LR_TYPE_ANNUAL_LEAVE);
            filterParameter.setEmployeeId(item.getEmployeeID());
            filterParameter.setYear(groupPayrunData.getYear());
            Integer monthId = 1;
            if (groupPayrunData.getMonth().equals("January")) {
                monthId = 1;
            } else if (groupPayrunData.getMonth().equals("February")) {
                monthId = 2;
            } else if (groupPayrunData.getMonth().equals("March")) {
                monthId = 3;
            } else if (groupPayrunData.getMonth().equals("April")) {
                monthId = 4;
            } else if (groupPayrunData.getMonth().equals("May")) {
                monthId = 5;
            } else if (groupPayrunData.getMonth().equals("June")) {
                monthId = 6;
            } else if (groupPayrunData.getMonth().equals("July")) {
                monthId = 7;
            } else if (groupPayrunData.getMonth().equals("August")) {
                monthId = 8;
            } else if (groupPayrunData.getMonth().equals("September")) {
                monthId = 9;
            } else if (groupPayrunData.getMonth().equals("October")) {
                monthId = 10;
            } else if (groupPayrunData.getMonth().equals("November")) {
                monthId = 11;
            } else if (groupPayrunData.getMonth().equals("December")) {
                monthId = 12;
            }
            filterParameter.setMonthId(monthId);

            Double daysOnLeaveForPeriod = sickRequestDurationManager.getEmployeeLeaveDurationsByMonthAndYear(filterParameter);

            for (CompanyCustomFieldItem customField : customFieldItems) {
                if (customField.getFieldName().equals("Routing Code")) {
                    routingCode = customField.getFieldStringValue();
                } else if (customField.getFieldName().equals("Employee Unique ID")) {
                    employeeUniqueId = customField.getFieldStringValue();
                } else if (customField.getFieldName().equals("Record Type")) {
                    recordType = customField.getFieldStringValue();
                } else if (customField.getFieldName().equals("Employee Account with Agent")) {
                    employeeAccountWithAgent = customField.getFieldStringValue();
                }
            }

            String housingAllowance = "";
            String otherAllowance = "";
            String airTicketAllowance = "";
            String regularOvertime = "";
            String basicSalary = "";
            for (PaymentDeductionObject allowance : item.getPaymentCategories()){
                if (allowance.getCategoryItem() != null) {
                    if ("HOUSING_ALLOWANCE".equals(allowance.getCategoryItem().getCode())) {
                        housingAllowance = defaultScaleFormat.format(allowance.getPaymentAmount() != null ? allowance.getPaymentAmount() : BigDecimal.ZERO);
                    } else if ("OTHER_ALLOWANCES".equals(allowance.getCategoryItem().getCode())) {
                        otherAllowance = defaultScaleFormat.format(allowance.getPaymentAmount() != null ? allowance.getPaymentAmount() : BigDecimal.ZERO);
                    } else if ("AIR_TICKET_ALLOWANCE".equals(allowance.getCategoryItem().getCode())) {
                        airTicketAllowance = defaultScaleFormat.format(allowance.getPaymentAmount() != null ? allowance.getPaymentAmount() : BigDecimal.ZERO);
                    } else if ("REGULAR_OVERTIME".equals(allowance.getCategoryItem().getCode())) {
                        regularOvertime = defaultScaleFormat.format(allowance.getPaymentAmount() != null ? allowance.getPaymentAmount() : BigDecimal.ZERO);
                    } else if ("BASIC_SALARY".equals(allowance.getCategoryItem().getCode())) {
                        basicSalary = defaultScaleFormat.format(allowance.getPaymentAmount() != null ? allowance.getPaymentAmount() : BigDecimal.ZERO);
                    }
                }
            }

            row = generateOneRowWithEmptyCell(rowIndex, cellCount);
            cellIndex = 0;
            sheet.getRow(rowIndex).getCell(cellIndex).setCellValue(recordType);
            cellIndex++;
            sheet.getRow(rowIndex).getCell(cellIndex).setCellValue(employeeUniqueId);
            cellIndex++;
            sheet.getRow(rowIndex).getCell(cellIndex).setCellValue(item.getFirstName() + " " + item.getLastName());
            cellIndex++;
            sheet.getRow(rowIndex).getCell(cellIndex).setCellValue(routingCode);
            cellIndex++;
            sheet.getRow(rowIndex).getCell(cellIndex).setCellValue(employeeAccountWithAgent);
            cellIndex++;
            sheet.getRow(rowIndex).getCell(cellIndex).setCellValue((item.getFromDate() != null && item.getFromDate().getNonConvertedDate() != null) ? simpleDateFormat.format(item.getFromDate().getNonConvertedDate()) : "");
            sheet.getRow(rowIndex).getCell(cellIndex).setCellStyle(blueStyleCell);
            cellIndex++;
            sheet.getRow(rowIndex).getCell(cellIndex).setCellValue((item.getToDate() != null && item.getToDate().getNonConvertedDate() != null) ? simpleDateFormat.format(item.getToDate().getNonConvertedDate()) : "");
            sheet.getRow(rowIndex).getCell(cellIndex).setCellStyle(blueStyleCell);
            cellIndex++;
            sheet.getRow(rowIndex).getCell(cellIndex).setCellValue(String.valueOf(ServerUtils.getMonthDaysCountInYear(groupPayrunData.getMonthID(), groupPayrunData.getYear())));
            sheet.getRow(rowIndex).getCell(cellIndex).setCellStyle(blueStyleCell);
            cellIndex++;
            sheet.getRow(rowIndex).getCell(cellIndex).setCellValue(item.getBasicSalary() != null ? getMoneyFormat(item.getBasicSalary().add(item.getAllowance())) : getMoneyFormat(BigDecimal.ZERO));
            sheet.getRow(rowIndex).getCell(cellIndex).setCellStyle(blueStyleCell);
            cellIndex++;
            sheet.getRow(rowIndex).getCell(cellIndex).setCellValue(getMoneyFormat(BigDecimal.ZERO));
            sheet.getRow(rowIndex).getCell(cellIndex).setCellStyle(blueStyleCell);
            cellIndex++;
            sheet.getRow(rowIndex).getCell(cellIndex).setCellValue(daysOnLeaveForPeriod);
            sheet.getRow(rowIndex).getCell(cellIndex).setCellStyle(blueStyleCell);
            cellIndex++;
            sheet.getRow(rowIndex).getCell(cellIndex).setCellValue(housingAllowance);
            sheet.getRow(rowIndex).getCell(cellIndex).setCellStyle(blueStyleCell);
            cellIndex++;
            sheet.getRow(rowIndex).getCell(cellIndex).setCellValue("");
            sheet.getRow(rowIndex).getCell(cellIndex).setCellStyle(blueStyleCell);
            cellIndex++;
            sheet.getRow(rowIndex).getCell(cellIndex).setCellValue("");
            sheet.getRow(rowIndex).getCell(cellIndex).setCellStyle(blueStyleCell);
            cellIndex++;
            sheet.getRow(rowIndex).getCell(cellIndex).setCellValue("");
            sheet.getRow(rowIndex).getCell(cellIndex).setCellStyle(blueStyleCell);
            cellIndex++;
            sheet.getRow(rowIndex).getCell(cellIndex).setCellValue(regularOvertime);
            sheet.getRow(rowIndex).getCell(cellIndex).setCellStyle(blueStyleCell);
            cellIndex++;
            sheet.getRow(rowIndex).getCell(cellIndex).setCellValue(otherAllowance);
            sheet.getRow(rowIndex).getCell(cellIndex).setCellStyle(blueStyleCell);
            cellIndex++;
            sheet.getRow(rowIndex).getCell(cellIndex).setCellValue("");
            sheet.getRow(rowIndex).getCell(cellIndex).setCellStyle(blueStyleCell);
            rowIndex++;
        }

        for (int i = 0; i <= cellIndex; i++) {
            sheet.autoSizeColumn(i);
        }


        row = generateOneRowWithEmptyCell(rowIndex, cellCount);
        row.setHeight((short) 500);
        colCounter = 0;
        sheet.getRow(rowIndex).getCell(colCounter).setCellValue("Record Type");
        sheet.getRow(rowIndex).getCell(colCounter).setCellStyle(getBlueStyleCell());
        colCounter++;
        sheet.getRow(rowIndex).getCell(colCounter).setCellValue("MOL Company Number");
        sheet.getRow(rowIndex).getCell(colCounter).setCellStyle(getBlueStyleCell());
        colCounter++;
        sheet.getRow(rowIndex).getCell(colCounter).setCellValue("Routing Bank Code");
        sheet.getRow(rowIndex).getCell(colCounter).setCellStyle(getBlueStyleCell());
        colCounter++;
        sheet.getRow(rowIndex).getCell(colCounter).setCellValue("File Creation Date");
        sheet.getRow(rowIndex).getCell(colCounter).setCellStyle(getBlueStyleCell());
        colCounter++;
        sheet.getRow(rowIndex).getCell(colCounter).setCellValue("File Creation Time");
        sheet.getRow(rowIndex).getCell(colCounter).setCellStyle(getBlueStyleCell());
        colCounter++;
        sheet.getRow(rowIndex).getCell(colCounter).setCellValue("Salary Month");
        sheet.getRow(rowIndex).getCell(colCounter).setCellStyle(getBlueStyleCell());
        colCounter++;
        sheet.getRow(rowIndex).getCell(colCounter).setCellValue("EDR Count");
        sheet.getRow(rowIndex).getCell(colCounter).setCellStyle(getBlueStyleCell());
        colCounter++;
        sheet.getRow(rowIndex).getCell(colCounter).setCellValue("Total Salary");
        sheet.getRow(rowIndex).getCell(colCounter).setCellStyle(getBlueStyleCell());
        colCounter++;
        sheet.getRow(rowIndex).getCell(colCounter).setCellValue("Payment Currency");
        sheet.getRow(rowIndex).getCell(colCounter).setCellStyle(getBlueStyleCell());
        colCounter++;
        sheet.getRow(rowIndex).getCell(colCounter).setCellValue("Employer Reference");
        sheet.getRow(rowIndex).getCell(colCounter).setCellStyle(getBlueStyleCell());
        colCounter++;
        sheet.getRow(rowIndex).getCell(colCounter).setCellValue("");
        colCounter++;
        sheet.getRow(rowIndex).getCell(colCounter).setCellValue("");
        colCounter++;
        sheet.getRow(rowIndex).getCell(colCounter).setCellValue("");
        colCounter++;
        sheet.getRow(rowIndex).getCell(colCounter).setCellValue("");
        colCounter++;
        sheet.getRow(rowIndex).getCell(colCounter).setCellValue("");
        colCounter++;
        sheet.getRow(rowIndex).getCell(colCounter).setCellValue("");
        colCounter++;
        sheet.getRow(rowIndex).getCell(colCounter).setCellValue("");
        colCounter++;
        sheet.getRow(rowIndex).getCell(colCounter).setCellValue("");
        rowIndex++;

        for (int i = 0; i <= colCounter; i++) {
            sheet.autoSizeColumn(i);
        }

        row = generateOneRowWithEmptyCell(rowIndex, cellCount);
        row.setHeight((short) 500);
        cellIndex = 0;
        sheet.getRow(rowIndex).getCell(cellIndex).setCellValue("SCR");
        sheet.getRow(rowIndex).getCell(cellIndex).setCellStyle(blueStyleCell);
        cellIndex++;
        sheet.getRow(rowIndex).getCell(cellIndex).setCellValue("0000001354886");
        sheet.getRow(rowIndex).getCell(cellIndex).setCellStyle(blueStyleCell);
        cellIndex++;
        sheet.getRow(rowIndex).getCell(cellIndex).setCellValue("720610101");
        sheet.getRow(rowIndex).getCell(cellIndex).setCellStyle(blueStyleCell);
        cellIndex++;
        SimpleDateFormat timeFormat = new SimpleDateFormat("hhmm");
        timeFormat.setTimeZone(userManager.getUser().getUserTimezone());
        sheet.getRow(rowIndex).getCell(cellIndex).setCellValue(simpleDateFormat.format(new Date()));
        sheet.getRow(rowIndex).getCell(cellIndex).setCellStyle(blueStyleCell);
        cellIndex++;
        sheet.getRow(rowIndex).getCell(cellIndex).setCellValue(timeFormat.format(new Date()));
        sheet.getRow(rowIndex).getCell(cellIndex).setCellStyle(blueStyleCell);
        cellIndex++;
        String month = "";
        if (groupPayrunData.getMonth().equals("January")) {
            month = "Jan";
        } else if (groupPayrunData.getMonth().equals("February")) {
            month = "Feb";
        } else if (groupPayrunData.getMonth().equals("March")) {
            month = "Mar";
        } else if (groupPayrunData.getMonth().equals("April")) {
            month = "Apr";
        } else if (groupPayrunData.getMonth().equals("May")) {
            month = "May";
        } else if (groupPayrunData.getMonth().equals("June")) {
            month = "Jun";
        } else if (groupPayrunData.getMonth().equals("July")) {
            month = "Jul";
        } else if (groupPayrunData.getMonth().equals("August")) {
            month = "Aug";
        } else if (groupPayrunData.getMonth().equals("September")) {
            month = "Sep";
        } else if (groupPayrunData.getMonth().equals("October")) {
            month = "Oct";
        } else if (groupPayrunData.getMonth().equals("November")) {
            month = "Nov";
        } else if (groupPayrunData.getMonth().equals("December")) {
            month = "Dec";
        }
        sheet.getRow(rowIndex).getCell(cellIndex).setCellValue(month);
        sheet.getRow(rowIndex).getCell(cellIndex).setCellStyle(blueStyleCell);
        cellIndex++;
        sheet.getRow(rowIndex).getCell(cellIndex).setCellValue(groupPayrunData.getTableItems() != null ? String.valueOf(groupPayrunData.getTableItems().length) : "");
        sheet.getRow(rowIndex).getCell(cellIndex).setCellStyle(blueStyleCell);
        cellIndex++;
        sheet.getRow(rowIndex).getCell(cellIndex).setCellValue(getMoneyFormat(groupPayrunData.getTotalAmount()));
        sheet.getRow(rowIndex).getCell(cellIndex).setCellStyle(blueStyleCell);
        cellIndex++;
        sheet.getRow(rowIndex).getCell(cellIndex).setCellValue(groupPayrunData.getCurrencyName());
        sheet.getRow(rowIndex).getCell(cellIndex).setCellStyle(blueStyleCell);
        cellIndex++;
        sheet.getRow(rowIndex).getCell(cellIndex).setCellValue("");
        cellIndex++;
        sheet.getRow(rowIndex).getCell(cellIndex).setCellValue("");
        cellIndex++;
        sheet.getRow(rowIndex).getCell(cellIndex).setCellValue("");
        cellIndex++;
        sheet.getRow(rowIndex).getCell(cellIndex).setCellValue("");
        cellIndex++;
        sheet.getRow(rowIndex).getCell(cellIndex).setCellValue("");
        cellIndex++;
        sheet.getRow(rowIndex).getCell(cellIndex).setCellValue("");
        cellIndex++;
        sheet.getRow(rowIndex).getCell(cellIndex).setCellValue("");
        cellIndex++;
        sheet.getRow(rowIndex).getCell(cellIndex).setCellValue("");
        cellIndex++;
        sheet.getRow(rowIndex).getCell(cellIndex).setCellValue("");
        rowIndex++;
    }

    public String getCompanyPayrollSettings(String key) {
        final EdsCompanyPayrollSettings settings = companyPayrollSettingsManager.getCompanySettingValue(key);
        return settings != null ? settings.getValue() : null;
    }

    private void createTotalTable(GroupPayrunData groupPayrunData) {
        int cellIndex = 0;
        int additionalColumns = 0;
        if (groupPayrunData.getPensionType() != null && groupPayrunData.getPensionValue() != null && BigDecimal.ZERO.compareTo(groupPayrunData.getPensionValue()) < 0) {
            additionalColumns++;
        }
        int cellCount = 12 + additionalColumns;
        //Just empty row
        generateOneRowWithEmptyCell(++rowIndex, cellIndex);
        generateOneRowWithEmptyCell(++rowIndex, cellCount);
        generateOneRowWithEmptyCell(rowIndex + 1, cellCount);
        BigDecimal totalAmount = groupPayrunData.getTotalAmount() != null ? groupPayrunData.getTotalAmount() : BigDecimal.ZERO;
        BigDecimal totalInBaseAmount = groupPayrunData.getTotalInBase() != null ? groupPayrunData.getTotalInBase() : BigDecimal.ZERO;
        NumberToWord numberToWordConverter = new NumberToWord_en();
        NumberToWord numberToWordUzConverter = new NumberToWord_uz_lotin();
        String totalAmountWord;
        if (ServerUtils.getUserLocale().getLanguage().equals("uz")) {
            totalAmountWord = numberToWordUzConverter.convert(totalAmount.abs().setScale(2, BigDecimal.ROUND_HALF_UP));
        } else {
            totalAmountWord = numberToWordConverter.convert(totalAmount.abs().setScale(2, BigDecimal.ROUND_HALF_UP));
        }
        sheet.addMergedRegion(new CellRangeAddress(rowIndex, rowIndex, cellCount - 5, cellCount - 3));
        sheet.getRow(rowIndex).getCell(cellCount - 5).setCellValue(totalAmountWord != null ? WordUtils.capitalizeFully(totalAmountWord) : "");
        if (groupPayrunData.getCurrency() != null) {
            sheet.getRow(rowIndex).getCell(cellCount - 2).setCellValue(commonLocalizer.localize(PdfLocalizationName.total, "Total") + " (" + groupPayrunData.getCurrency().getName() + ")");
            sheet.getRow(rowIndex + 1).getCell(cellCount - 2).setCellValue(commonLocalizer.localize(PdfLocalizationName.total, "Total") + " (" + groupPayrunData.getCurrencyName() + ")");
            sheet.getRow(rowIndex + 1).getCell(cellCount - 1).setCellValue(defaultScaleFormat.format(totalInBaseAmount));
            sheet.getRow(rowIndex + 1).getCell(cellCount - 1).setCellStyle(getBlueStyleCell());
        }
        sheet.getRow(rowIndex).getCell(cellCount - 2).setCellStyle(getHeaderFieldStyleCell());
        sheet.getRow(rowIndex + 1).getCell(cellCount - 2).setCellStyle(getHeaderFieldStyleCell());
        sheet.getRow(rowIndex).getCell(cellCount - 1).setCellValue(defaultScaleFormat.format(totalAmount));
        sheet.getRow(rowIndex).getCell(cellCount - 1).setCellStyle(getBlueStyleCell());

    }

    private CellStyle getBlueStyleCell() {
        CellStyle blueStyleCell = workbook.createCellStyle();
        blueStyleCell.setFillForegroundColor(HSSFColor.ROYAL_BLUE.index);
        blueStyleCell.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        blueStyleCell.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        blueStyleCell.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        blueStyleCell.setBorderRight(CellStyle.BORDER_THIN);
        blueStyleCell.setRightBorderColor(HSSFColor.BLACK.index);

        Font font = workbook.createFont();
        font.setBoldweight(Font.BOLDWEIGHT_BOLD);
        font.setColor(HSSFColor.WHITE.index);
        blueStyleCell.setFont(font);
        return blueStyleCell;
    }

    private static Date getMonthLastDate(Date date, EdsCompany company) {
        int month = date.getMonth();
        Date current = ServerUtils.getCompanyDate((Date) date.clone(), company);
        while (current.getMonth() == month) {
            current.setDate(current.getDate() + 1);
        }
        current.setDate(current.getDate() - 1);
        return current;
    }

    private HSSFRow generateOneRowWithEmptyCell(int rowNumber, int cells) {
        HSSFRow row = sheet.createRow(rowNumber);
        for (int i = 0; i <= cells; i++) {
            Cell cell = row.createCell(i);
        }
        return row;
    }

    private CellStyle getHeaderTitleStyleCell() {
        CellStyle titleStyleCell = workbook.createCellStyle();
        titleStyleCell.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        titleStyleCell.setAlignment(HSSFCellStyle.ALIGN_CENTER);

        Font font = workbook.createFont();
        font.setBoldweight(Font.BOLDWEIGHT_BOLD);
        titleStyleCell.setFont(font);
        return titleStyleCell;
    }

    private CellStyle getHeaderFieldStyleCell() {
        CellStyle titleStyleCell = workbook.createCellStyle();
        titleStyleCell.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        titleStyleCell.setAlignment(HSSFCellStyle.ALIGN_LEFT);

        Font font = workbook.createFont();
        font.setBoldweight(Font.BOLDWEIGHT_BOLD);
        titleStyleCell.setFont(font);
        return titleStyleCell;
    }

    private CellStyle getHeaderStyleCell() {
        CellStyle blueStyleCell = workbook.createCellStyle();
        blueStyleCell.setFillForegroundColor(HSSFColor.ROYAL_BLUE.index);
        blueStyleCell.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        blueStyleCell.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        blueStyleCell.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        blueStyleCell.setBorderRight(CellStyle.BORDER_THIN);
        blueStyleCell.setRightBorderColor(HSSFColor.BLACK.index);

        Font font = workbook.createFont();
        font.setBoldweight(Font.BOLDWEIGHT_BOLD);
        font.setColor(HSSFColor.WHITE.index);
        blueStyleCell.setFont(font);
        return blueStyleCell;
    }


    @Override
    protected void setFileName() {
        filename = "Group Payrun";
    }
}
