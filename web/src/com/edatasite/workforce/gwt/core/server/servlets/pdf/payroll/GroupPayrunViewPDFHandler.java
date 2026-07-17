package com.edatasite.workforce.gwt.core.server.servlets.pdf.payroll;

import com.edatasite.workforce.core.domain.EdsCompany;
import com.edatasite.workforce.core.domain.EdsCurrency;
import com.edatasite.workforce.core.domain.EdsExpenseReport;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.accounting.EdsAccount;
import com.edatasite.workforce.core.domain.payrolluk.EdsCompanyPayrollSettings;
import com.edatasite.workforce.core.domain.payrolluk.EdsPaymentDeduction;
import com.edatasite.workforce.core.domain.payrolluk.EdsPayrollCategory;
import com.edatasite.workforce.core.tools.StringUtil;
import com.edatasite.workforce.gwt.core.client.enums.GenericSettingsEnum;
import com.edatasite.workforce.gwt.core.client.rpc.ExpenseData;
import com.edatasite.workforce.gwt.core.client.rpc.payroll.EmployerSettingsObject;
import com.edatasite.workforce.gwt.core.client.ui.Frequency;
import com.edatasite.workforce.gwt.core.client.ui.view.PaymentDeductionObject;
import com.edatasite.workforce.gwt.core.client.ui.view.PaymentDeductionSelectItem;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.db.AccountingManager;
import com.edatasite.workforce.gwt.core.server.db.ExpenseReportManager;
import com.edatasite.workforce.gwt.core.server.db.payroll.PayrollCategoryManager;
import com.edatasite.workforce.gwt.core.server.db.payroll.PayslipPaymentsManager;
import com.edatasite.workforce.gwt.core.server.db.payroll.PayslipTableItemManager;
import com.edatasite.workforce.gwt.core.server.db.settings.GenericSettingsManager;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.AbstractITextPostPdfHandler;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.PDFConstants;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.PdfReferenceCodeNameEnum;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.localization.PdfLocalizationName;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.CustomisedITextTable;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.CustomisedProductCategoriesITextTable;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.ITextBaseInvoice;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.ITextGenericPdfData;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.PdfParams;
import com.edatasite.workforce.gwt.core.server.utils.NumberToWord;
import com.edatasite.workforce.gwt.core.server.utils.NumberToWord_en;
import com.edatasite.workforce.gwt.core.server.utils.NumberToWord_uz_lotin;
import com.edatasite.workforce.gwt.payroll.client.rpc.GroupPayrunData;
import com.edatasite.workforce.gwt.payroll.client.rpc.GroupPayrunRequestObject;
import com.edatasite.workforce.gwt.payroll.client.rpc.PayrollService;
import com.edatasite.workforce.gwt.payroll.client.rpc.PayslipFilter;
import com.edatasite.workforce.gwt.payroll.client.rpc.SinglePayrunItem;
import com.google.common.collect.Lists;
import com.lowagie.text.Document;
import com.lowagie.text.PageSize;
import com.lowagie.text.pdf.PdfWriter;
import org.apache.commons.lang.WordUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.support.WfmMessageSource;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

/**
 * Created by Abror Abdukadirov on 26.05.2016.
 */
public class GroupPayrunViewPDFHandler extends AbstractITextPostPdfHandler implements PDFConstants {

    @Autowired
    private PayrollService payrollService;
    @Autowired
    @Qualifier("payrollLocalizer")
    protected WfmMessageSource payrollLocalizer;
    @Autowired
    private GenericSettingsManager genericSettings;
    @Autowired
    private PayrollCategoryManager categoryManager;
    @Autowired
    private ExpenseReportManager expenseReportManager;
    @Autowired
    private AccountingManager accountingManager;
    @Autowired
    private PayslipTableItemManager payslipTableItemManager;
    @Autowired
    private PayslipPaymentsManager payslipPaymentsManager;

    final static SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy");
    boolean fromSummary;

    private String period;

    @Override
    protected Document newDocument(EdsCompany edsCompany, Object dataClass) {
        return new Document(PageSize.A4.rotate(), 20, 40, 120, 50);
    }

    @Override
    protected PdfParams.Orientation getOrientation(Object dataClass) {
        GroupPayrunRequestObject requestObject = (GroupPayrunRequestObject) dataClass;
        return requestObject.getIS_LANDSCAPE() ? PdfParams.Orientation.landscape : null;
    }

    @Override
    public ITextGenericPdfData buildPdfDocument(Object dataClass, Document document, PdfWriter writer) throws IOException {
        return null;
    }

    @Override
    protected ITextGenericPdfData buildPdfDocumentCustomise(Object dataClass, EdsCompany company, boolean hasPhantom) {
        ITextGenericPdfData pdfData = new ITextGenericPdfData();
        ITextBaseInvoice baseInvoice = new ITextBaseInvoice();
        pdfData.setBaseInvoice(baseInvoice);

        GroupPayrunRequestObject requestObject = (GroupPayrunRequestObject) dataClass;
        PayslipFilter filter = new PayslipFilter();
        filter.setObjectID(requestObject.getObjectID());
        Date currentDate = new Date();
        final int currentYear = Integer.valueOf(yearFormat.format(currentDate));
        filter.setFromGroupTaxi(genericSettings.isSettingsEnabled(GenericSettingsEnum.TAXI_PAYRUN_ENABLED));
        filter.setYear(currentYear);
        filter.setFromExcelHandler(true);
        filter.setEmpCodeAdjoined(false);
        GroupPayrunData groupPayrunData = payrollService.getPayslipTable(filter);
        EmployerSettingsObject employerSettings = payrollService.getCompanyPayrollSettingsForGroupPayrunPDF();
        EdsCompany edsCompany = userManager.getUser().getCompany();
        SimpleDateFormat shortDateFormat = getCompanyShortDateFormat(edsCompany);
        DecimalFormat defaultScaleFormat = new DecimalFormat(",##0.00");

        final EdsPayrollCategory expenseCategory = categoryManager.getCategoryByCode(EXPENSE_REPORT);
        PaymentDeductionSelectItem expenseCategoryItem = null;
        if (expenseCategory != null) {
            expenseCategoryItem = expenseCategory.createPaymentDeductionSelectItem();
        }
        final EdsCurrency baseCurrency = this.financialSettingsManager.getFinancialSettings().getCurrency();
        final boolean empInBase = groupPayrunData.getCurrency() == null || groupPayrunData.getCurrency().getId().equals(baseCurrency.getObjectID());
        final String expensePaidFromAccount = this.getCompanyPayrollSettings(EXPENSE_PAID_ACCOUNT);
        final EdsAccount paidFromAccount = !StringUtil.isEmpty(expensePaidFromAccount) ? accountingManager.get(Integer.valueOf(expensePaidFromAccount)) : null;

        String frequency;
        if (groupPayrunData.getFrequency() != null) {
            frequency = Frequency.getByID(groupPayrunData.getFrequency()).getName();
        } else {
            frequency = Frequency.getByID(1).getName();
        }
        if (groupPayrunData.getYear() != null) {
            period = groupPayrunData.getMonth() + ", " + groupPayrunData.getYear();
        } else {
            period = groupPayrunData.getMonth();
        }
        String processDate;
        if (groupPayrunData.getProcessDate() != null) {
            processDate = shortDateFormat.format(groupPayrunData.getProcessDate().getNonConvertedDate());
        } else {
            processDate = shortDateFormat.format(getMonthLastDate(new Date(groupPayrunData.getYear() - 1900, groupPayrunData.getMonthID(), 1), edsCompany));
        }

        CustomisedITextTable headerTable = new CustomisedITextTable();
        headerTable.addColumnOrder(COLUMN_NAME, COLUMN_VALUE);
        headerTable.addRowWithCode(CREATOR, commonLocalizer.localize(PdfLocalizationName.createdBy), groupPayrunData.getCreator() != null ? escapeHtml(groupPayrunData.getCreator().getName()) : "");
        headerTable.addRowWithCode("FREQUENCY", commonLocalizer.localize(PdfLocalizationName.frequency, "Frequency"), frequency);
        headerTable.addRowWithCode(GROUP, commonLocalizer.localize(PdfLocalizationName.payrollGroup), groupPayrunData.getPayrollBatchItem() != null ? escapeHtml(groupPayrunData.getPayrollBatchItem().getName()) : "");
        headerTable.addRowWithCode(APPROVER, commonLocalizer.localize(PdfLocalizationName.approver), groupPayrunData.getApprover() != null ? escapeHtml(groupPayrunData.getApprover().getName()) : "");
        headerTable.addRowWithCode(PERIOD, commonLocalizer.localize(PdfLocalizationName.period), ServerUtils.getUserLocale().getLanguage().equals("uz") ? ServerUtils.convertToUzbDateFormat(period) : period);
        headerTable.addRowWithCode(PROCESSED_DATE, commonLocalizer.localize(PdfLocalizationName.processDate), processDate);
        headerTable.addRowWithCode("CODE_LABEL", commonLocalizer.localize(PdfLocalizationName.code));
        headerTable.addRowWithCode("EMPLOYEE_LABEL", commonLocalizer.localize(PdfLocalizationName.employee));
        headerTable.addRowWithCode("BASIC_LABEL", commonLocalizer.localize(PdfLocalizationName.basic));
        headerTable.addRowWithCode("ALLOWANCE_LABEL", commonLocalizer.localize(PdfLocalizationName.allowance));
        headerTable.addRowWithCode("DEDUCTION_LABEL", commonLocalizer.localize(PdfLocalizationName.deduction));
        headerTable.addRowWithCode("EXPENSE_LABEL", commonLocalizer.localize(PdfLocalizationName.expense));
        headerTable.addRowWithCode("PAY_TOTAL_LABEL", commonLocalizer.localize(PdfLocalizationName.paymentTotal));
        headerTable.addRowWithCode("TOTAL_LABEL", commonLocalizer.localize(PdfLocalizationName.total));

        CustomisedITextTable employeeTable = new CustomisedITextTable();
        employeeTable.addColumnOrder(EMPLOYEE_CODE);
        employeeTable.addColumnOrder(EMPLOYEE_NAME);
        employeeTable.addColumnOrder(FROM_DATE);
        employeeTable.addColumnOrder(TO_DATE);
        employeeTable.addColumnOrder(BASIC_SALARY);
        employeeTable.addColumnOrder(ADDITIONAL_PAYMENT);
        employeeTable.addColumnOrder(PENSION_DEDUCTION);
        employeeTable.addColumnOrder(DEDUCTION_AMOUNT);
        employeeTable.addColumnOrder(EXPENSE_AMOUNT);
        employeeTable.addColumnOrder(COMMENTS);
        employeeTable.addColumnOrder(TOTAL_PAY);
        employeeTable.addColumnOrder(ACCOUNT_NUMBER);
        employeeTable.addColumnOrder(BANK_NAME);

        BigDecimal dhofarTotal = BigDecimal.ZERO;
        CustomisedITextTable dhofarTable = new CustomisedITextTable();
        dhofarTable.addColumnOrder(EMPLOYEE_CODE);
        dhofarTable.addColumnOrder(EMPLOYEE_NAME);
        dhofarTable.addColumnOrder(FROM_DATE);
        dhofarTable.addColumnOrder(TO_DATE);
        dhofarTable.addColumnOrder(BASIC_SALARY);
        dhofarTable.addColumnOrder(ADDITIONAL_PAYMENT);
        dhofarTable.addColumnOrder(PENSION_DEDUCTION);
        dhofarTable.addColumnOrder(DEDUCTION_AMOUNT);
        dhofarTable.addColumnOrder(EXPENSE_AMOUNT);
        dhofarTable.addColumnOrder(COMMENTS);
        dhofarTable.addColumnOrder(TOTAL_PAY);
        dhofarTable.addColumnOrder(ACCOUNT_NUMBER);
        dhofarTable.addColumnOrder(BANK_NAME);

        BigDecimal nonDhofarTotal = BigDecimal.ZERO;
        CustomisedITextTable nonDhofarTable = new CustomisedITextTable();
        nonDhofarTable.addColumnOrder(EMPLOYEE_CODE);
        nonDhofarTable.addColumnOrder(EMPLOYEE_NAME);
        nonDhofarTable.addColumnOrder(FROM_DATE);
        nonDhofarTable.addColumnOrder(TO_DATE);
        nonDhofarTable.addColumnOrder(BASIC_SALARY);
        nonDhofarTable.addColumnOrder(ADDITIONAL_PAYMENT);
        nonDhofarTable.addColumnOrder(PENSION_DEDUCTION);
        nonDhofarTable.addColumnOrder(DEDUCTION_AMOUNT);
        nonDhofarTable.addColumnOrder(EXPENSE_AMOUNT);
        nonDhofarTable.addColumnOrder(COMMENTS);
        nonDhofarTable.addColumnOrder(TOTAL_PAY);
        nonDhofarTable.addColumnOrder(ACCOUNT_NUMBER);
        nonDhofarTable.addColumnOrder(BANK_NAME);

        List<String> columnsValue = new ArrayList<>();

        Arrays.sort(groupPayrunData.getTableItems(), (o1, o2) -> {
            String b1 = o1.getBankName() != null && !o1.getBankName().isEmpty() ? o1.getBankName() : "~";
            String b2 = o2.getBankName() != null && !o2.getBankName().isEmpty() ? o2.getBankName() : "~";
            return b1.compareTo(b2);
        });

        baseInvoice.setCustomProductCategoriesITextTables(getEmployeeWithPaymentAndDeductionData(groupPayrunData.getTableItems()));

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

            columnsValue.clear();

            String employeeName = item.getEmployee() != null ? item.getEmployee() : "";

            String employeeCode = item.getEmployeeCode() != null ? item.getEmployeeCode() : "";

            String fromDate = item.getFromDate() != null && item.getFromDate().getNonConvertedDate() != null ? shortDateFormat.format(item.getFromDate().getNonConvertedDate()) : "";

            String toDate = item.getToDate() != null && item.getToDate().getNonConvertedDate() != null ? shortDateFormat.format(item.getToDate().getNonConvertedDate()) : "";

            String basicSalaryAmount = defaultScaleFormat.format(item.getBasicSalary() != null ? item.getBasicSalary() : BigDecimal.ZERO);

            String AllowanceAmount = defaultScaleFormat.format(item.getAllowance() != null ? item.getAllowance() : BigDecimal.ZERO);

            String pensionAmount = defaultScaleFormat.format(item.getPensionAmount() != null ? item.getPensionAmount() : BigDecimal.ZERO);

            String deductionAmount = defaultScaleFormat.format(item.getDeduction() != null ? item.getDeduction() : BigDecimal.ZERO);

            String expenseAmount = defaultScaleFormat.format(item.getEmployeeExpenses() != null ? item.getEmployeeExpenses().getPaymentAmount() : BigDecimal.ZERO);

            String comment = item.getDescription() != null ? item.getDescription() : "";

            BigDecimal tot = item.getTotal();
            if (item.getPrevMonthItem() != null) tot = tot.add(item.getPrevMonthItem().getTotal());
            String totalAmount = defaultScaleFormat.format(tot);

            String accountNumber = item.getAccountNumber() != null ? item.getAccountNumber() : "";

            String bankName = item.getBankName() != null ? item.getBankName() : "";


            if (employeeTable.containsColumn(PDFConstants.EMPLOYEE_CODE)) {
                columnsValue.add(escapeHtml(employeeCode));
            }
            if (employeeTable.containsColumn(EMPLOYEE_NAME)) {
                columnsValue.add(escapeHtml(employeeName));
            }
            if (employeeTable.containsColumn(FROM_DATE)) {
                columnsValue.add(escapeHtml(fromDate));
            }
            if (employeeTable.containsColumn(TO_DATE)) {
                columnsValue.add(escapeHtml(toDate));
            }
            if (employeeTable.containsColumn(BASIC_SALARY)) {
                columnsValue.add(escapeHtml(basicSalaryAmount));
            }
            if (employeeTable.containsColumn(ADDITIONAL_PAYMENT)) {
                columnsValue.add(escapeHtml(AllowanceAmount));
            }
            if (employeeTable.containsColumn(PENSION_DEDUCTION)) {
                columnsValue.add(escapeHtml(pensionAmount));
            }
            if (employeeTable.containsColumn(DEDUCTION_AMOUNT)) {
                columnsValue.add(escapeHtml(deductionAmount));
            }
            if (employeeTable.containsColumn(EXPENSE_AMOUNT)) {
                columnsValue.add(escapeHtml(expenseAmount));
            }
            if (employeeTable.containsColumn(COMMENTS)) {
                columnsValue.add(escapeHtml(comment));
            }
            if (employeeTable.containsColumn(TOTAL_PAY)) {
                columnsValue.add(escapeHtml(totalAmount));
            }
            if (employeeTable.containsColumn(ACCOUNT_NUMBER)) {
                columnsValue.add(escapeHtml(accountNumber));
            }
            if (employeeTable.containsColumn(BANK_NAME)) {
                columnsValue.add(escapeHtml(bankName));
            }
            employeeTable.addRow(columnsValue.toArray(new String[]{}));
            if ("Bank Dhofar".equals(bankName)) {
                dhofarTotal = dhofarTotal.add(tot);
                dhofarTable.addRow(columnsValue.toArray(new String[]{}));
            } else {
                nonDhofarTotal = nonDhofarTotal.add(tot);
                nonDhofarTable.addRow(columnsValue.toArray(new String[]{}));
            }
        }
        CustomisedITextTable totalTable = new CustomisedITextTable();
        totalTable.addColumnOrder(TOTAL);
        totalTable.addColumnOrder(BASE_CURRENCY);
        totalTable.addColumnOrder(TOTAL_IN_WORDS);
        totalTable.addColumnOrder(TOTAL_IN_BASE);
        totalTable.addColumnOrder(PROCESSED_DATE);
        totalTable.addColumnOrder(PERIOD);
        totalTable.addColumnOrder("DHOFAR_TOTAL");
        totalTable.addColumnOrder("DHOFAR_TOTAL_IN_WORD");
        totalTable.addColumnOrder("NON_DHOFAR");
        totalTable.addColumnOrder("NON_DHOFAR_IN_WORD");
        List<String> totalColumnsValue = new ArrayList<>();

        NumberToWord numberToWordConverter = new NumberToWord_en();
        NumberToWord numberToWordUzConverter = new NumberToWord_uz_lotin();

        BigDecimal totalAmount = groupPayrunData.getTotalAmount() != null ? groupPayrunData.getTotalAmount() : BigDecimal.ZERO;
        BigDecimal totalAmountInBase = groupPayrunData.getTotalInBase() != null ? groupPayrunData.getTotalInBase() : BigDecimal.ZERO;

        String totalAmountString = defaultScaleFormat.format(totalAmount);

        String currencyBase = groupPayrunData.getCurrency() != null ? groupPayrunData.getCurrency().getName() : groupPayrunData.getCurrencyName() != null ? groupPayrunData.getCurrencyName() : "";

        String totalAmountWord;
        if (ServerUtils.getUserLocale().getLanguage().equals("uz")) {
            totalAmountWord = numberToWordUzConverter.convert(totalAmount.abs().setScale(2, RoundingMode.HALF_UP));
            totalAmountWord = totalAmountWord != null ? WordUtils.capitalizeFully(totalAmountWord) : "";
        } else {
            totalAmountWord = numberToWordConverter.convert(totalAmount.abs().setScale(2, RoundingMode.HALF_UP));
            totalAmountWord = totalAmountWord != null ? WordUtils.capitalizeFully(totalAmountWord) : "";
        }
        String totalAmountInBaseString = defaultScaleFormat.format(totalAmountInBase);


        if (totalTable.containsColumn(TOTAL)) {
            totalColumnsValue.add(escapeHtml(totalAmountString));
        }
        if (totalTable.containsColumn(BASE_CURRENCY)) {
            totalColumnsValue.add(escapeHtml(currencyBase));
        }
        if (totalTable.containsColumn(TOTAL_IN_WORDS)) {
            totalColumnsValue.add(escapeHtml(totalAmountWord));
        }
        if (totalTable.containsColumn(TOTAL_IN_BASE)) {
            totalColumnsValue.add(escapeHtml(totalAmountInBaseString));
        }
        if (totalTable.containsColumn(PROCESSED_DATE)) {
            totalColumnsValue.add(escapeHtml(processDate));
        }
        if (totalTable.containsColumn(PERIOD)) {
            totalColumnsValue.add(escapeHtml(period));
        }
        if (totalTable.containsColumn("DHOFAR_TOTAL")){
            totalColumnsValue.add(escapeHtml(defaultScaleFormat.format(dhofarTotal)));
        }
        if (totalTable.containsColumn("DHOFAR_TOTAL_IN_WORD")){
            String dhofarTotalWord = numberToWordConverter.convert(dhofarTotal.abs().setScale(2, RoundingMode.HALF_UP));
            dhofarTotalWord = dhofarTotalWord != null ? WordUtils.capitalizeFully(dhofarTotalWord) : "";
            totalColumnsValue.add(escapeHtml(dhofarTotalWord));
        }
        if (totalTable.containsColumn("NON_DHOFAR")){
            totalColumnsValue.add(escapeHtml(defaultScaleFormat.format(nonDhofarTotal)));
        }
        if (totalTable.containsColumn("NON_DHOFAR_IN_WORD")){
            String nonDhofarTotalWord = numberToWordConverter.convert(nonDhofarTotal.abs().setScale(2, RoundingMode.HALF_UP));
            nonDhofarTotalWord = nonDhofarTotalWord != null ? WordUtils.capitalizeFully(nonDhofarTotalWord) : "";
            totalColumnsValue.add(escapeHtml(nonDhofarTotalWord));
        }

        totalTable.addRow(totalColumnsValue.toArray(new String[]{}));

        CustomisedITextTable employerSettingsTable = new CustomisedITextTable();
        employerSettingsTable.addColumnOrder(REFERENCE);
        employerSettingsTable.addColumnOrder(COMPANY_CODE);
        employerSettingsTable.addColumnOrder(BANK_NAME);
        employerSettingsTable.addColumnOrder(BILL_ADDRESS);
        employerSettingsTable.addColumnOrder(BANK_ACCOUNT_NUMBER);
        employerSettingsTable.addColumnOrder(BANK_ACCOUNT_NAME);
        employerSettingsTable.addColumnOrder(SWIFT_BIC);
        employerSettingsTable.addColumnOrder(IBAN_CODE);

        List<String> employerColumnsValue = new ArrayList<>();
        employerColumnsValue.add(escapeHtml(employerSettings.getReferenceNumber()));
        employerColumnsValue.add(escapeHtml(employerSettings.getCompanyCode()));
        employerColumnsValue.add(escapeHtml(employerSettings.getBankName()));
        employerColumnsValue.add(escapeHtml(employerSettings.getBankAddress()));
        employerColumnsValue.add(escapeHtml(employerSettings.getAccountNumber()));
        employerColumnsValue.add(escapeHtml(employerSettings.getAccountName()));
        employerColumnsValue.add(escapeHtml(employerSettings.getSwiftCode()));
        employerColumnsValue.add(escapeHtml(employerSettings.getiBANCode()));
        employerSettingsTable.addRow(employerColumnsValue.toArray(new String[]{}));

        HashMap<String, CustomisedITextTable> customData = new HashMap<>();
        customData.put(HEADER, headerTable);
        customData.put(EMPLOYEE_TABLE, employeeTable);
        customData.put(TOTAL_TABLE, totalTable);
        customData.put(EMPLOYER_SETTINGS_TABLE, employerSettingsTable);
        customData.put("DHOFAR_TABLE", dhofarTable);
        customData.put("NON_DHOFAR_TABLE", nonDhofarTable);
        pdfData.setCustomData(customData);

        return pdfData;
    }

    private List<CustomisedProductCategoriesITextTable> getEmployeeWithPaymentAndDeductionData(SinglePayrunItem[] items) {
        EdsCompany edsCompany = userManager.getUser().getCompany();
        SimpleDateFormat shortDateFormat = getCompanyShortDateFormat(edsCompany);
        DecimalFormat defaultScaleFormat = new DecimalFormat(",##0.00");

        List<CustomisedProductCategoriesITextTable> employeeCategoriesITextTable = new ArrayList<>();

        List<String> employeeTableValues = Lists.newLinkedList();
        for (SinglePayrunItem item : items) {
            CustomisedProductCategoriesITextTable categoriesTable = new CustomisedProductCategoriesITextTable();

            CustomisedITextTable employeeTable = new CustomisedITextTable();
            employeeTable.addColumn(EMPLOYEE_CODE, "");
            employeeTable.addColumn(EMPLOYEE_NAME, "");
            employeeTable.addColumn(FROM_DATE, "");
            employeeTable.addColumn(TO_DATE, "");
            employeeTable.addColumn(BASIC_SALARY, "");
            employeeTable.addColumn(ADDITIONAL_PAYMENT, "");
            employeeTable.addColumn(PENSION_DEDUCTION, "");
            employeeTable.addColumn(DEDUCTION_AMOUNT, "");
            employeeTable.addColumn(EXPENSE_AMOUNT, "");
            employeeTable.addColumn(COMMENTS, "");
            employeeTable.addColumn(TOTAL_PAY, "");
            employeeTable.addColumn(ACCOUNT_NUMBER, "");
            employeeTable.addColumn(BANK_NAME, "");

            CustomisedITextTable paymentAndDeductionTable = new CustomisedITextTable();
            paymentAndDeductionTable.addColumn(CATEGORY, "");
            paymentAndDeductionTable.addColumn(TYPE, "");
            paymentAndDeductionTable.addColumn(AMOUNTS, "");

            String employeeCode = escapeHtml(item.getEmployeeCode());
            String employeeName = escapeHtml(item.getEmployee());
            String fromDate = item.getFromDate() != null && item.getFromDate().getNonConvertedDate() != null ? shortDateFormat.format(item.getFromDate().getNonConvertedDate()) : "";
            String toDate = item.getToDate() != null && item.getToDate().getNonConvertedDate() != null ? shortDateFormat.format(item.getToDate().getNonConvertedDate()) : "";
            String basicSalaryAmount = defaultScaleFormat.format(item.getBasicSalary() != null ? item.getBasicSalary() : BigDecimal.ZERO);
            String allowanceAmount = defaultScaleFormat.format(item.getAllowance() != null ? item.getAllowance() : BigDecimal.ZERO);
            String pensionAmount = defaultScaleFormat.format(item.getPensionAmount() != null ? item.getPensionAmount() : BigDecimal.ZERO);
            String deductionAmount = defaultScaleFormat.format(item.getDeduction() != null ? item.getDeduction() : BigDecimal.ZERO);
            String expenseAmount = defaultScaleFormat.format(item.getEmployeeExpenses() != null ? item.getEmployeeExpenses().getPaymentAmount() : BigDecimal.ZERO);
            String comment = escapeHtml(item.getDescription());

            BigDecimal tot = item.getTotal();
            if (item.getPrevMonthItem() != null) tot = tot.add(item.getPrevMonthItem().getTotal());
            String totalAmount = defaultScaleFormat.format(tot);
            String accountNumber = escapeHtml(item.getAccountNumber());
            String bankName = escapeHtml(item.getBankName());

            employeeTableValues.add(employeeCode);
            employeeTableValues.add(employeeName);
            employeeTableValues.add(fromDate);
            employeeTableValues.add(toDate);
            employeeTableValues.add(basicSalaryAmount);
            employeeTableValues.add(allowanceAmount);
            employeeTableValues.add(pensionAmount);
            employeeTableValues.add(deductionAmount);
            employeeTableValues.add(expenseAmount);
            employeeTableValues.add(comment);
            employeeTableValues.add(totalAmount);
            employeeTableValues.add(accountNumber);
            employeeTableValues.add(bankName);

            employeeTable.addRow(employeeTableValues.toArray(new String[]{}));
            employeeTableValues.clear();
            categoriesTable.setTable(employeeTable);

            List<EdsPaymentDeduction> categories = payslipTableItemManager.getItemCategories(item.getObjectID());

            List<String> paymentAndDeductionTableValues = Lists.newLinkedList();
            for (EdsPaymentDeduction paymentAndDeduction : categories) {
                PaymentDeductionObject object = paymentAndDeduction.getRPC();
                String code = object != null && object.getCategoryItem() != null ? object.getCategoryItem().getCode() : "";
                if (!ServerUtils.isNullOrEmpty(code) && !Objects.equals("BASIC_SALARY", code)) {

                    BigDecimal paymentAmount = payslipPaymentsManager.getPaymentAmount(paymentAndDeduction.getObjectID(), item.getObjectID());
                    paymentAndDeductionTableValues.add(object.getCategoryItem() != null ? object.getCategoryItem().getName() : "");
                    paymentAndDeductionTableValues.add(object.isPaymentCategory() ? "Payment" : "Deduction");
                    paymentAndDeductionTableValues.add(defaultScaleFormat.format(paymentAmount != null ? paymentAmount : BigDecimal.ZERO));

                    paymentAndDeductionTable.addRow(paymentAndDeductionTableValues.toArray(new String[]{}));
                    paymentAndDeductionTableValues.clear();
                    categoriesTable.setInnerTable(paymentAndDeductionTable);
                }
            }
            employeeCategoriesITextTable.add(categoriesTable);
        }
        return employeeCategoriesITextTable;
    }

    public String getCompanyPayrollSettings(String key) {
        final EdsCompanyPayrollSettings settings = companyPayrollSettingsManager.getCompanySettingValue(key);
        return settings != null ? settings.getValue() : null;
    }

    @Override
    protected Object getDataClass(HttpServletRequest request) {
        return new GroupPayrunRequestObject();
    }

    @Override
    protected PdfReferenceCodeNameEnum getPdfCodeName(Object dataClass) {
        return PdfReferenceCodeNameEnum.GROUP_PAYRUN;
    }

    @Override
    protected Integer getCustomisedPDFTemplateId(Object object) {
        if (object instanceof GroupPayrunRequestObject) {
            return ((GroupPayrunRequestObject) object).getTemplateID();
        }
        return null;
    }

    @Override
    protected String getTableName(Object dataClass) {
        return period != null ? (ServerUtils.getUserLocale().getLanguage().equals("uz") ? ServerUtils.convertToUzbDateFormat(period) : period).concat(" - ").concat(payrollLocalizer.localize(PdfLocalizationName.groupPayrun, "Group Payrun")) : payrollLocalizer.localize(PdfLocalizationName.groupPayrun, "Group Payrun");
    }

    @Override
    protected void setFileName(EdsUser user, Object dataClass) {
        setFileName("Group_Payrun" + "_" + dateFormat(user.getUserDate()));
    }

    public static Date getMonthLastDate(Date date, EdsCompany company) {
        int month = date.getMonth();
        Date current = ServerUtils.getCompanyDate((Date) date.clone(), company);
        while (current.getMonth() == month) {
            current.setDate(current.getDate() + 1);
        }
        current.setDate(current.getDate() - 1);
        return current;
    }
}
