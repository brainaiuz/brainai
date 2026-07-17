package com.edatasite.workforce.gwt.core.server.servlets.pdf;

import com.edatasite.workforce.core.domain.EdsCompany;
import com.edatasite.workforce.core.domain.EdsEmployee;
import com.edatasite.workforce.core.domain.EdsExpense;
import com.edatasite.workforce.core.domain.EdsExpensePayment;
import com.edatasite.workforce.core.domain.EdsExpenseReport;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.accounting.EdsFinancialSettings;
import com.edatasite.workforce.core.domain.accounting.EdsInvoicePayment;
import com.edatasite.workforce.core.domain.approving.EdsApprover;
import com.edatasite.workforce.gwt.accounting.client.ui.AccountingConstants;
import com.edatasite.workforce.gwt.core.client.CommandConstants;
import com.edatasite.workforce.gwt.core.client.rpc.CommonService;
import com.edatasite.workforce.gwt.core.client.rpc.CompanyCustomFieldItem;
import com.edatasite.workforce.gwt.core.client.rpc.FileItem;
import com.edatasite.workforce.gwt.core.client.rpc.HistoryListItem;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.customfields.ViewName;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.db.ExpenseManager;
import com.edatasite.workforce.gwt.core.server.db.ExpensePaymentManager;
import com.edatasite.workforce.gwt.core.server.db.ExpenseReportManager;
import com.edatasite.workforce.gwt.core.server.db.VatManager;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.localization.PdfLocalizationName;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.qrcode.QRCodeGenerator;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.ITextPdfTemplateEvent;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.ITextPdfViewTypeEnum;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.CustomisedITextTable;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.ITextBaseInvoice;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.ITextGenericPdfData;
import com.edatasite.workforce.gwt.core.server.utils.CustomFieldsUtils;
import com.edatasite.workforce.gwt.core.server.utils.NumberToWord;
import com.edatasite.workforce.gwt.core.server.utils.NumberToWord_en;
import com.edatasite.workforce.gwt.expenses.client.rpc.ExpenseRequestObject;
import com.edatasite.workforce.gwt.expenses.client.rpc.ExpenseServiceLocal;
import com.edatasite.workforce.utils.EdsContextParams;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfStamper;
import com.lowagie.text.pdf.PdfWriter;
import org.apache.commons.lang.WordUtils;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: Admin
 * Date: 20.11.2008
 * Time: 19:51:29
 * To change this template use File | Settings | File Templates.
 */
public class ExpensesViewPDFHandler extends AbstractITextPostPdfHandler implements IPostPDFHandler, AccountingConstants, PDFConstants {

    @Autowired
    private ExpenseReportManager reportManager;
    @Autowired
    private ExpenseManager expenseManager;
    @Autowired
    private ExpensePaymentManager expensePaymentManager;
    @Autowired
    protected CommonService commonService;
    @Autowired
    protected ExpenseServiceLocal expenseServiceLocal;
    @Autowired
    private VatManager vatManager;

    protected SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM dd, yyyy");
    protected SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");

    @Override
    public ITextGenericPdfData buildPdfDocument(Object dataClass, Document document, PdfWriter writer) throws IOException {
        return null;
    }

    private StringBuilder getPaymentAccountNames(Integer reportId) {
        List<String> expensePaymentAccountNames = expensePaymentManager.getExpensePaymentAccountNamesByExpenseReportId(reportId);
        StringBuilder accountsName = new StringBuilder();
        if (expensePaymentAccountNames == null || expensePaymentAccountNames.isEmpty()) {
            return accountsName;
        }
        Iterator<String> iterator = expensePaymentAccountNames.iterator();
        while (iterator.hasNext()) {
            String expensePaymentAccountName = iterator.next();
            if (expensePaymentAccountName == null) {
                continue;
            }
            if (accountsName.length() > 0) {
                accountsName.append(", ");
            }
            accountsName.append(expensePaymentAccountName);
        }
        return accountsName;
    }

    @Override
    protected ITextGenericPdfData buildPdfDocumentCustomise(Object dataClass, EdsCompany company, boolean hasPhantom) {
        ITextGenericPdfData pdfData = new ITextGenericPdfData();
        pdfData.setPdfViewType(ITextPdfViewTypeEnum.BASEINVOICE);
        ITextBaseInvoice baseInvoice = new ITextBaseInvoice();
        pdfData.setBaseInvoice(baseInvoice);
        ExpenseRequestObject requestObject = (ExpenseRequestObject) dataClass;
        Integer reportId = requestObject.getObjectID();
        EdsUser user = uploadManager.getUser();
        EdsExpenseReport report = reportManager.getExpenseReport(reportId);
        List<EdsExpense> expenseList = expenseManager.getExpenseByReport(reportId);
        EdsEmployee reporter = report.getReporter();
        pdfData.setUserId(user.getObjectID().toString());
        baseInvoice.setObjectId(reportId);
        //Company Data
        pdfData.setCompanyData(getCompanyData(user.getCompany(), true, hasPhantom));

        SimpleDateFormat shortDateFormat = getCompanyShortDateFormat(user.getCompany());

        //Report node
        CustomisedITextTable reportTitle = new CustomisedITextTable();
        reportTitle.addColumnOrder(COLUMN_NAME, COLUMN_VALUE);
        reportTitle.addRowWithCode(TITLE, commonLocalizer.localizeAccounting(PdfLocalizationName.title), escapeHtml(report.getTitle()));
        reportTitle.addRowWithCode(PDFConstants.REPORTER_DATE, commonLocalizer.localizeAccounting(PdfLocalizationName.date), escapeHtml(reporter == null ? "" : String.valueOf(report.getStartDate())));
        reportTitle.addRowWithCode(REPORTER, commonLocalizer.localizeAccounting(PdfLocalizationName.reportedBy), (reporter == null ? "" : escapeHtml(reporter.getName())));
        reportTitle.addRowWithCode(REPORTER_NUMBER, commonLocalizer.localizeAccounting(PdfLocalizationName.reportedNumber, "Reporter Number") + " ", (reporter != null && reporter.getProfile() != null) ? escapeHtml(reporter.getProfile().getEmployeeCode()) : "");
        reportTitle.addRowWithCode(REPORTER_EMAIL, commonLocalizer.localizeAccounting(PdfLocalizationName.reportedEmail, "Reporter Email") + " ", reporter != null ? escapeHtml(reporter.getEmail()) : "");
        reportTitle.addRowWithCode(REPORTER_DEPARTMENT, commonLocalizer.localizeAccounting(PdfLocalizationName.department, "Reporter Department") + " ", (reporter != null && reporter.getEmployeeDepartment() != null) ? escapeHtml(reporter.getEmployeeDepartment().getTeam().getName()) : "");
        reportTitle.addRowWithCode(REPORTER_JOB_TITLE, commonLocalizer.localizeAccounting(PdfLocalizationName.position, "Reporter Position") + " ", reporter.getPosition() != null ? escapeHtml(reporter.getPosition().getName()) : "");
        reportTitle.addRowWithCode(REPORTER_PHONE, commonLocalizer.localizeAccounting(PdfLocalizationName.phone, "Reporter Phone") + " ", reporter.getPrimaryPhone() != null ? escapeHtml(reporter.getPrimaryPhone()) : "");
        reportTitle.addRowWithCode(REPORTER_MOBILE_PHONE, commonLocalizer.localizeAccounting(PdfLocalizationName.mobilePhone, "Reporter Mobile Phone") + " ", reporter.getMobilePhoneFirst() != null ? escapeHtml(reporter.getMobilePhoneFirst()) : "");
        reportTitle.addRowWithCode(APPROVER, commonLocalizer.localizeAccounting(PdfLocalizationName.approver), (report.getCurrentApprover() != null && report.getCurrentApprover().getExactEmployee() != null ? escapeHtml(report.getCurrentApprover().getExactEmployee().getFullName()) : ""));
        reportTitle.addRowWithCode(SUPPLIER, pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.supplier), (report.getSupplier() != null ? escapeHtml(report.getSupplier().getName()) : ""));
        reportTitle.addRowWithCode(REPORTER_QRCODE, pdfWfmMessageSource.localize(PdfLocalizationName.qrcode), (reporter != null && reporter.getFullName() != null ? escapeHtml(QRCodeGenerator.generate(reporter.getFullName(), 150, 150)) : ""));
        baseInvoice.setCustomBillToAddress(reportTitle);

        CustomisedITextTable approverTable = new CustomisedITextTable();
        List<EdsApprover> approvers = report.getApprovers();
        if (approvers != null && !approvers.isEmpty()) {
            approverTable.addColumnOrder(PDFConstants.APPROVERS, PDFConstants.APPROVERS_DATES, "QR", "APPROVERS_DEPARTMENT", "APPROVERS_POSITION", "APPROVERS_PHONE", "APPROVERS_EMAIL", "STATUS");

            for (EdsApprover approver : approvers) {
                EdsUser approverExactEmployee = approver.getExactEmployee();
                String fullName = "";
                Date approveDate = null;
                String teamName = "";
                String positionName = "";
                String primaryPhone = "";
                String email = "";
                String status = approver.getStatus()!=null ?escapeHtml( approver.getStatus().getCode()):"";
                if (approverExactEmployee != null) {
                    EdsEmployee employee = approverExactEmployee.getEmployee();
                    fullName = escapeHtml(approverExactEmployee.getFullName());
                    if (approver.getApproverHistory().isEmpty() && approver.getApproverHistory().iterator().next().getApproveDate() == null) {
                        new Date(System.currentTimeMillis());
                    }
                    approveDate = ServerUtils.convertServerDateToUserDate(approver.getApproverHistory().iterator().next().getApproveDate(), user.getUserTimezone());
                    teamName = employee.getTeam() != null ? employee.getTeam().getName() : "";
                    positionName = employee.getPosition() != null ? employee.getPosition().getName() : "";
                    primaryPhone = employee.getPrimaryPhone() != null ? employee.getPrimaryPhone() : "";
                    email = employee.getEmail() != null ? employee.getEmail() : "";
                }
                HistoryListItem[] reportsHistory = expenseServiceLocal.getReportsHistory(reportId);
                for (HistoryListItem historyListItem : reportsHistory) {
                    if (historyListItem != null && historyListItem.getEmployeeID().equals(approver.getExactEmployee().getObjectID())) {
                        report.setRejectionNote(escapeHtml(historyListItem.getComment(false)));
                    }
                }
                approverTable.addRow(
                        fullName,
                        " " + ServerUtils.dateFormat(approveDate, "yyyy-MM-dd HH:mm:ss"),
                        escapeHtml(QRCodeGenerator.generate(fullName, 150, 150)),
                        teamName,
                        positionName,
                        primaryPhone,
                        email,
                        status
                );
            }
        }
        baseInvoice.setPaymentHistoryTable(approverTable);
        CustomisedITextTable reportDate = new CustomisedITextTable();
        reportDate.addColumnOrder(COLUMN_NAME, COLUMN_VALUE);
        reportDate.addRowWithCode(EXP_NUMBER, pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.expenseNumber), (escapeHtml(report.getNumber())));
        reportDate.addRowWithCode(EXP_DESCRIPTION, pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.reportDescription), (escapeHtml(report.getDescription())));
        reportDate.addRowWithCode(EXP_START_DATE, commonLocalizer.localizeAccounting(PdfLocalizationName.date), report.getStartDate() == null ? "" : shortDateFormat.format(report.getStartDate()));
        reportDate.addRowWithCode(PERIOD_START_DATE, "Period Start", report.getPeriodStartDate() == null ? "" : shortDateFormat.format(report.getPeriodStartDate()));
        reportDate.addRowWithCode(PERIOD_END_DATE, "Period End", report.getPeriodEndDate() == null ? "" : shortDateFormat.format(report.getPeriodEndDate()));
        reportDate.addRowWithCode(RELATED_PROJECT, pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.relatedProject), ((report.getProject() == null ? "" : escapeHtml(report.getProject().getNumber() + " - " + report.getProject().getName()))));
        reportDate.addRowWithCode(STATUS, commonLocalizer.localizeAccounting(PdfLocalizationName.status), (report.getStatus() == null ? "" : escapeHtml(report.getStatus().getName())));
        reportDate.addRowWithCode(AMOUNTS, commonLocalizer.localizeAccounting(PdfLocalizationName.taxCalculationType), (report.getTaxCalculationType() == null ? "" : report.getTaxCalculationType().toString()));
        reportDate.addRowWithCode(CURRENCY, commonLocalizer.localizeAccounting(PdfLocalizationName.currency), (report.getCurrency() != null ? escapeHtml(report.getCurrency().getName()) : ""));
        reportDate.addRowWithCode("REJECT_NOTE", pdfWfmMessageSource.localize(PdfLocalizationName.rejectionReason), escapeHtml(report.getRejectionNote()));
        reportDate.addRowWithCode(CURRENCY_SYMBOL, "", (report.getCurrency() != null ? escapeHtml(report.getCurrency().getSymbol()) : ""));
        reportDate.addRowWithCode(BASE_CURRENCY, "", (report.getBaseCurrency() != null ? escapeHtml(report.getBaseCurrency().getName()) : ""));
        StringBuilder accountsName = getPaymentAccountNames(reportId);
        reportDate.addRowWithCode(PAID_FROM, commonLocalizer.localizeAccounting(PdfLocalizationName.paidFrom), accountsName.toString());
        String taxTypeName = "";
        if (report.getTaxCalculationType() == null) {
            taxTypeName = PdfLocalizationName.taxExclusive;
        } else if (report.getTaxCalculationType().equals(0)) {
            taxTypeName = PdfLocalizationName.noTax;
        } else if (report.getTaxCalculationType().equals(1)) {
            taxTypeName = PdfLocalizationName.taxInclusive;
        } else {
            taxTypeName = PdfLocalizationName.taxExclusive;
        }
        reportDate.addRowWithCode(TAX_TYPE_NAME, commonLocalizer.localize(PdfLocalizationName.taxCalculationType), commonLocalizer.localize(taxTypeName));

        baseInvoice.setCustomNumberAndDatesTable(reportDate);

        //Expenses Node
        CustomisedITextTable recieptTable = new CustomisedITextTable();
        recieptTable.addColumnOrder(ITEM_NO, "FILE_NAME", DESCRIPTION, TYPE, "FORM_URL");
        recieptTable.addHeaderColumns(pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.number),
                commonLocalizer.localize(PdfLocalizationName.fileName),
                pdfWfmMessageSource.localize(PdfLocalizationName.description),
                accountingLocalizer.localizeAccounting(PdfLocalizationName.type),
                commonLocalizer.localize(PdfLocalizationName.formURL));
        recieptTable.setName(pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.expenseRecieptTableHeader));
        baseInvoice.setTermsConditionsName(pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.receipts));
        baseInvoice.setCustomTermsConditions(recieptTable);

        //Exchange Rate
        baseInvoice.setExchangeRate(getExchangeRate(report));

        CustomisedITextTable expenseTable = new CustomisedITextTable();
        baseInvoice.setProductTableName(pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.expenseReport));
        expenseTable.addColumnOrder(ITEM_NO,
                ITEM_NAME,
                ITEM_DESCRIPTION,
                ITEM_QTY_HRS,
                ITEM_UNIT_PRICE,
                CURRENCY,
                ITEM_DATE,
                ITEM_TOTAL_AMOUNT,
                ITEM_BASE_TOTAL,
                EXP_BILL_TO,
                PROJECT_BASE_ITEM,
                ITEM_DEPARTMENT,
                ACCOUNT_CODE,
                PARENT_CODE,
                ITEM_TAX_AMOUNT);
        expenseTable.addHeaderColumns(pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.number),
                commonLocalizer.localizeAccounting(PdfLocalizationName.category),
                commonLocalizer.localizeAccounting(PdfLocalizationName.description),
                commonLocalizer.localizeAccounting(PdfLocalizationName.units),
                commonLocalizer.localizeAccounting(PdfLocalizationName.costPerUnit),
                commonLocalizer.localizeAccounting(PdfLocalizationName.currency),
                commonLocalizer.localizeAccounting(PdfLocalizationName.date),
                commonLocalizer.localizeAccounting(PdfLocalizationName.total),
                commonLocalizer.localizeAccounting(PdfLocalizationName.total),
                pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.billTo),
                "Project Base Item",
                commonLocalizer.localizeAccounting(PdfLocalizationName.department),
                commonLocalizer.localizeAccounting(PdfLocalizationName.accountNumber),
                commonLocalizer.localizeAccounting(PdfLocalizationName.parentaccount),
                commonLocalizer.localizeAccounting(PdfLocalizationName.tax));
        baseInvoice.setCustomProductTable(expenseTable);

        DecimalFormat defaultScaleFormat = new DecimalFormat(",##0.00");
        DecimalFormat unitPriceNumberFormat = getUnitPriceNumberFormat(user.getCompany(), null);
        EdsFinancialSettings fs = financialSettingsManager.getFinancialSettings();
        DecimalFormat priceScaleNumberFormat = getPriceScaleNumberFormat(fs);

        for (int i = 0; i < expenseList.size(); i++) {
            EdsExpense expenses = expenseList.get(i);
            String no = String.valueOf(i + 1);
            String category = expenses.getAccount() == null ? "" : escapeHtml(expenses.getAccount().getName());
            String description = escapeHtml(expenses.getDescription());
            String units = expenses.getUnits() == null ? "" : defaultScaleFormat.format(expenses.getUnits());
            String cost = expenses.getCostPerUnit() == null ? "" : unitPriceNumberFormat.format(expenses.getCostPerUnit());
            String currency = expenses.getCurrency() == null ? "" : expenses.getCurrency().getName();
            String date = expenses.getDate() == null ? "" : shortDateFormat.format(expenses.getDate());
            String total = expenses.getSubtotal() == null ? "" : priceScaleNumberFormat.format(expenses.getSubtotal());
            String baseTotal = expenses.getBaseSubtotal() == null ? "" : priceScaleNumberFormat.format(expenses.getBaseSubtotal());
            String billTo = expenses.getClient() == null ? "" : escapeHtml(expenses.getClient().getName());
            String projectBaseItem = expenses.getIsProjectBase() != null && expenses.getIsProjectBase() ? "YES" : "NO";
            String department = expenses.getDepartment() != null ? escapeHtml(expenses.getDepartment().getName()) : "";
            String accountCode = expenses.getAccount() != null ? escapeHtml(expenses.getAccount().getAccountCode()) : "";
            String parentCode = expenses.getAccount() != null && expenses.getAccount().getAccountRootParent() != null ? escapeHtml(expenses.getAccount().getAccountRootParent().getAccountCode()) : "";
            String taxAmount = expenses.getTaxAmount() != null ? priceScaleNumberFormat.format(expenses.getTaxAmount()) : "";

            expenseTable.addRow(no, category, description, units, cost, currency, date, total, baseTotal, billTo, projectBaseItem, department, accountCode, parentCode, taxAmount);

            //Receipts NODE
            FileItem[] fileItems = expenseServiceLocal.getAttachments(expenses.getObjectID());
            String[] values = new String[5];
            if (fileItems != null && fileItems.length>0) {
                for (FileItem fileItem : fileItems) {
                    values[0] = no;
                    values[1] = fileItem.getFileName() != null ? fileItem.getFileName() : "";
                    values[2] = fileItem.getDescription() != null ? fileItem.getDescription() : "";
                    values[3] = fileItem.getUploadType() != null ? fileItem.getUploadType() : "";
                    values[4] = fileItem.getDownloadUrl() != null ? fileItem.getDownloadUrl() : "";
                    recieptTable.addRow(values);
                }
            }else {
                values[0] = no;
                values[1] = null;
                recieptTable.addRow(values);
            }
        }

        CustomisedITextTable totalTable = new CustomisedITextTable();
        totalTable.addColumnOrder(COLUMN_NAME, CURRENCY, COLUMN_VALUE);
        String currency = (report.getBaseCurrency() == null ? " " : report.getBaseCurrency().getName() + " ");
        String currencySymbol = (report.getBaseCurrency() == null ? "" : report.getBaseCurrency().getSymbol());
        baseInvoice.setCurrencyName(currency != null ? escapeHtml(currency.trim()) : "");
        baseInvoice.setCurrency(currencySymbol);
        BigDecimal subtotal = report.getTotal();
        if (report.getTaxTotal() != null && report.getTaxTotal().compareTo(ZERO) > 0) {
            subtotal = report.getTotal().subtract(report.getTaxTotal());
        }
        totalTable.addRowWithCode(SUBTOTAL, pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.subTotal), currency, priceScaleNumberFormat.format(subtotal));
        totalTable.addRowWithCode(TAX_TOTAL, pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.vat), currency, priceScaleNumberFormat.format(report.getTaxTotal()));
        totalTable.addRowWithCode(EXP_TOTAL, commonLocalizer.localizeAccounting(PdfLocalizationName.total), currency, report.getTotal() == null ? "" : priceScaleNumberFormat.format(report.getTotal()));
        totalTable.addRowWithCode(TOTAL, commonLocalizer.localizeAccounting(PdfLocalizationName.total), currency, report.getBaseTotal() == null ? "" : priceScaleNumberFormat.format(report.getBaseTotal()));

        NumberToWord numberToWordConverter = new NumberToWord_en();
        String total_word = report.getBaseTotal() != null ? numberToWordConverter.toWord(report.getBaseTotal().abs()) : "";
        String expTotal_word = report.getTotal() != null ? numberToWordConverter.toWord(report.getTotal().abs()) : "";

        totalTable.addRowWithCode(TOTAL_WORD, accountingLocalizer.localizeAccounting(PdfLocalizationName.totalToWord), currency, WordUtils.capitalizeFully(total_word));
        totalTable.addRowWithCode(EXP_TOTAL_WORD, accountingLocalizer.localizeAccounting(PdfLocalizationName.totalToWord), currency, WordUtils.capitalizeFully(expTotal_word));

        BigDecimal totalPayments = BigDecimal.ZERO;
        BigDecimal exchangeRate = report.getTotal().divide(report.getBaseTotal(), 12, RoundingMode.HALF_UP);
        if (report.getPayments() != null && !report.getPayments().isEmpty()) {
            for (EdsExpensePayment payment : report.getPayments()) {
                BigDecimal paymentAmount = payment.getAmount();
                //For handling old expenses
                if (payment.getExchangeRate() == null) {
                    paymentAmount = paymentAmount.multiply(exchangeRate);
                }
                totalPayments = totalPayments.add(paymentAmount);
            }
        }
        if (report.getPrePayments() != null && !report.getPrePayments().isEmpty()) {
            for (EdsInvoicePayment payment : report.getPrePayments()) {
                BigDecimal paymentAmount = payment.getAmount();
                //For handling old expenses
                if (payment.getExchangeRate() == null) {
                    paymentAmount = paymentAmount.multiply(exchangeRate);
                }
                totalPayments = totalPayments.add(paymentAmount);
            }
        }
        totalTable.addRowWithCode(EXP_LESS_PAYMENT, pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.lessPayment), currency, priceScaleNumberFormat.format(totalPayments));
        totalTable.addRowWithCode(DUE_AMOUNT, commonLocalizer.localizeAccounting(PdfLocalizationName.dueAmount), currency, priceScaleNumberFormat.format(report.getTotal().subtract(totalPayments).setScale(2, RoundingMode.HALF_UP)));

        baseInvoice.setClientSupplierData(getClientSupplierData(report));
        expenseTable.setCustomFields(getCustomFields(report));
        baseInvoice.setCustomTotalTable(totalTable);
        baseInvoice.setCustomPrepaymentTable(getPaymentHistory(report, priceScaleNumberFormat,shortDateFormat));
        pdfData.setCustomData(getCustomData());
        return pdfData;
    }

    private Map<String, String> getClientSupplierData(EdsExpenseReport report) {
        Map<String, String> values = new HashMap<>();
        values.put("SUPPLIER_TRN_NUMBER", report.getSupplier() != null && report.getSupplier().getTrn() != null ? report.getSupplier().getTrn() : "");
        return values;
    }

    private CustomisedITextTable getPaymentHistory(EdsExpenseReport report, DecimalFormat priceScaleNumberFormat,SimpleDateFormat shortDateFormat) {
        CustomisedITextTable innerTable = new CustomisedITextTable();
        innerTable.addColumnOrder( "AMOUNT", "DATE");
        int i = 0;
        for (EdsExpensePayment payment : report.getPayments()) {
            innerTable.addRowWithCode(i++ + "", payment.getAmount() != null ? priceScaleNumberFormat.format(payment.getAmount()) : null, shortDateFormat.format(payment.getPaymentDate()));
        }
        return innerTable;
    }

    public HashMap<String, CustomisedITextTable> getCustomData() {
        CustomisedITextTable customTable = new CustomisedITextTable();
        customTable.addColumnOrder(COLUMN_NAME, COLUMN_VALUE);
        Date currentDate = new Date();
        int totalCount = vatManager.getTaxRatesListCount();
        customTable.addRow(CURRENT_DATE, dateFormat.format(currentDate));
        customTable.addRow("CURRENT_YEAR", Calendar.getInstance().get(Calendar.YEAR) + "");
        customTable.addRow("CURRENT_TIME", timeFormat.format(userManager.getUser().getUserDate()));
        customTable.addRow("TAX_RATE_COUNT", totalCount + "");
        HashMap<String, CustomisedITextTable> result = new HashMap<>();
        result.put(CUSTOM_DATA, customTable);
        return result;
    }

    private Map<String, LinkedHashMap<String, Map<String, String>>> getCustomFields(EdsExpenseReport report) {
        Map<String, LinkedHashMap<String, Map<String, String>>> customFields = new HashMap<>();

        if (report == null || report.getCustomFields() == null) {
            return customFields;
        }
        List<CompanyCustomFieldItem> customFieldItems = CustomFieldsUtils.setRPCCustomFieldItems(report.getCustomFields(), commonService.getCompanyCustomFields(ViewName.ExpenceReportView));
        if (customFieldItems == null || customFieldItems.isEmpty()) {
            return customFields;
        }
        LinkedHashMap<String, Map<String, String>> itemCusFields = new LinkedHashMap<>();
        for (CompanyCustomFieldItem item : customFieldItems) {
            if (item == null) {
                continue;
            }
            Map<String, String> cols = new HashMap<>();
            cols.put(COLUMN_NAME, item.getFieldName() != null ? escapeHtml(item.getFieldName()) : "");
            if (CompanyCustomFieldItem.DATE.equals(item.getDataType())) {
                SimpleDateFormat shortDateFormat = getCompanyShortDateFormat(userManager.getUser().getCompany());
                cols.put(COLUMN_VALUE, item.getFieldDateNonConvertedValue() != null && item.getFieldDateNonConvertedValue().getNonConvertedDate() != null ?
                        escapeHtml(shortDateFormat.format(item.getFieldDateNonConvertedValue().getNonConvertedDate())) :
                        "");
            } else {
                cols.put(COLUMN_VALUE, item.getFieldStringValue() != null ? escapeHtml(item.getFieldStringValue()) : "");
            }
            if (item.getFieldName() != null) {
                itemCusFields.put(escapeHtml(item.getFieldName()), cols);
            }
        }
        customFields.put(EXPENSE, itemCusFields);
        return customFields;
    }

    private String getExchangeRate(EdsExpenseReport report) {
        BigDecimal exchangeRate = report.getExchangeRate() != null && report.getExchangeRate().compareTo(ZERO) != 0 ? report.getExchangeRate() : new BigDecimal("1.00");
        DecimalFormat priceScaleNumberFormat = new DecimalFormat(",#0.00000");
        return priceScaleNumberFormat.format(exchangeRate);
    }

    @Override
    protected Object getDataClass(HttpServletRequest request) {
        return new ExpenseRequestObject();
    }

    @Override
    protected Integer getCustomisedPDFTemplateId(Object object) {
        if (object != null) {
            return ((ExpenseRequestObject) object).getTemplateId();
        }
        return null;
    }

    @Override
    protected void setFileName(EdsUser user, Object dataClass) {
        ExpenseRequestObject requestObject = (ExpenseRequestObject) dataClass;
        Integer reportId = requestObject.getObjectID();
        EdsExpenseReport report = reportManager.getExpenseReport(reportId);

        SimpleDateFormat shortDateFormat = getCompanyShortDateFormat(user.getCompany());
        setFileName("ExpenseReport-" + (report.getTitle().length() > 24 ? report.getTitle().substring(0, 24) : report.getTitle()) + "-" + shortDateFormat.format(user.getUserDate()));
    }

    @Override
    protected PdfReferenceCodeNameEnum getPdfCodeName(Object dataClass) {
        return PdfReferenceCodeNameEnum.EXPENSE_REPORT;
    }

    public String getDownloadURL(FileItem fileItem) {
        if (Constants.GOOGLE.equals(fileItem.getUploadType())) {
            return fileItem.getGoogleDocumentLink();
        } else if (Constants.OFFICE_365.equals(fileItem.getUploadType()) || Constants.OFFICE_365_SHARE_POINT.equals(fileItem.getUploadType())) {
            return fileItem.getOfficeDocumentLink();
        }
        return EdsContextParams.getFullHost() + CommandConstants.COMMON_URL + "/downloadFile?id=" + fileItem.getAttachmentId().toString();
    }

    @Override
    protected void initPagingAndStamper(PdfReader pdfReader, PdfStamper pdfStamper, Document document, ITextPdfTemplateEvent iTextPdfTemplateEvent, Object dataClass) throws DocumentException {
        audingPdfFooterSignature(pdfReader, pdfStamper, document);
        super.initPagingAndStamper(pdfReader, pdfStamper, document, iTextPdfTemplateEvent, dataClass);
    }

    @Override
    protected String getTableName(Object dataClass) {
        return pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.expenseClaim);
    }
}
