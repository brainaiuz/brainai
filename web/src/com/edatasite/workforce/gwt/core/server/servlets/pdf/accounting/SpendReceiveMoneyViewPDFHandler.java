package com.edatasite.workforce.gwt.core.server.servlets.pdf.accounting;

import com.edatasite.workforce.core.domain.EdsCompany;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.accounting.EdsFinancialSettings;
import com.edatasite.workforce.core.domain.settings.EdsCompanySettings;
import com.edatasite.workforce.gwt.accounting.client.rpc.AccountingService;
import com.edatasite.workforce.gwt.accounting.client.rpc.NewManualTransaction;
import com.edatasite.workforce.gwt.accounting.client.rpc.NewManualTransactionItem;
import com.edatasite.workforce.gwt.accounting.client.rpc.TransactionPDFObject;
import com.edatasite.workforce.gwt.accounting.client.ui.AccountingConstants;
import com.edatasite.workforce.gwt.core.client.rpc.CompanyCustomFieldItem;
import com.edatasite.workforce.gwt.core.client.rpc.currency.CurrencyItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.db.accounting.SpendReceiveMoneyManager;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.AbstractITextPostPdfHandler;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.IPostPDFHandler;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.PDFConstants;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.PdfReferenceCodeNameEnum;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.localization.PdfLocalizationName;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.ITextPdfTemplateEvent;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.ITextPdfViewTypeEnum;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.CellData;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.CustomisedITextTable;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.ITextBaseInvoice;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.ITextGenericPdfData;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.ITextSummaryView;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.ITextTableList;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.ITextUserData;
import com.edatasite.workforce.gwt.core.server.utils.NumberToWord;
import com.edatasite.workforce.gwt.core.server.utils.NumberToWord_ar;
import com.edatasite.workforce.gwt.core.server.utils.NumberToWord_en;
import com.edatasite.workforce.gwt.core.server.utils.NumberToWord_ru;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfStamper;
import com.lowagie.text.pdf.PdfWriter;
import org.apache.commons.lang.WordUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import java.awt.*;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Ilxom Lutfullaev
 * Date: 24.09.13
 * Time: 18:16
 * To change this template use File | Settings | File Templates.
 */

public class SpendReceiveMoneyViewPDFHandler extends AbstractITextPostPdfHandler implements PDFConstants, AccountingConstants, IPostPDFHandler {

    @Autowired
    private SpendReceiveMoneyManager spendReceiveMoneyManager;

    @Autowired
    private AccountingService accountingService;

    private Integer transferType;
    private boolean isBankAccount;
    private String shortDateFormat = "";
    private boolean enabledDepartmentRelation;
    private boolean enabledProjectInLineItem;

    @Override
    public ITextGenericPdfData buildPdfDocument(Object dataClass, Document document, PdfWriter writer) throws IOException {
        int columnSize = 6;
        TransactionPDFObject requestObject = (TransactionPDFObject) dataClass;
        ITextGenericPdfData pdfData = new ITextGenericPdfData();
        pdfData.setPdfViewType(ITextPdfViewTypeEnum.SUMMARYVIEW);
        ITextSummaryView summaryView = new ITextSummaryView();

        EdsUser user = spendReceiveMoneyManager.getUser();
        DecimalFormat decimalFormat = getPriceScaleNumberFormat(user.getCompany(), requestObject.getTemplateID());
        pdfData.setCompanyData(getCompanyData(user.getCompany(), true, false));

        EdsCompanySettings companySettings = user.getCompany().getCompanySettings();
        shortDateFormat = (companySettings != null && companySettings.getShortDateFormat() != null) ? companySettings.getShortDateFormat() : "MM/dd/yyyy";
        ListingFilterParameter fp = new ListingFilterParameter();
        fp.setObjectId(requestObject.getObjectID());
        NewManualTransaction manualTransaction = accountingService.getBankTransferData(fp);
        transferType = manualTransaction.getTransferType();
        isBankAccount = (transferType == SPEND_MONEY || transferType == RECEIVE_MONEY);
        pdfData.setTableName(requestObject.getViewName());
        enabledDepartmentRelation = manualTransaction.isEnabledDepartmentRelation();
        enabledProjectInLineItem = manualTransaction.isEnabledProjectInLineItem();
        if (enabledDepartmentRelation) {
            columnSize = columnSize + 1;
        }
        if (enabledProjectInLineItem) {
            columnSize = columnSize + 1;
        }
        ITextTableList center = new ITextTableList(3);
        ITextTableList bottom = new ITextTableList(columnSize);
        ITextTableList bottom1 = new ITextTableList(3);

        centerPanel(center, manualTransaction, transferType);
        bottomPanel(bottom, bottom1, manualTransaction, decimalFormat);

        summaryView.addTable(center);
        summaryView.addTable(bottom);
        summaryView.addTable(bottom1);

        pdfData.setSummaryView(summaryView);
        return pdfData;
    }

    private void centerPanel(ITextTableList center, NewManualTransaction manualTransaction, Integer transferType) {
        CellData[] centerPayTo = new CellData[3];

        Font defaultFont = new Font(Font.UNDEFINED, 8, Font.BOLD);

        centerPayTo[0] = new CellData(isBankAccount ? pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.bankAccount, "Bank Account") : pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.cashAccount, "Cash Account"));
        centerPayTo[0].setFont(defaultFont);
        centerPayTo[1] = new CellData(pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.currency));
        centerPayTo[1].setFont(defaultFont);
        centerPayTo[2] = new CellData(pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.exchangeRate));
        centerPayTo[2].setFont(defaultFont);

        setBgColor(centerPayTo);
        center.addTableWidthPercentage(33, 33, 34);
        center.addPdfTableRows(centerPayTo);

        CellData[] centerAmount = new CellData[3];
        centerAmount[0] = new CellData(isBankAccount ? manualTransaction.getBankAccountItem().getName() : manualTransaction.getCashAccount().getName());
        centerAmount[1] = new CellData(manualTransaction.getCurrency().getFullName());
        centerAmount[2] = new CellData(manualTransaction.getExchangeRate() != null ? manualTransaction.getExchangeRate().setScale(getCalculationScale(), RoundingMode.HALF_UP).toString() : "N/A");


        setBgColor(centerAmount);
        center.setBorderWidth(0);
        center.addPdfTableRows(centerAmount);

        CellData[] amountString = new CellData[3];
        amountString[0] = new CellData(pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.project, "Project"));
        amountString[0].setFont(defaultFont);
        amountString[1] = new CellData(pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.date));
        amountString[1].setFont(defaultFont);
        amountString[2] = new CellData(pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.reference));
        amountString[2].setFont(defaultFont);
        setBgColor(amountString);
        center.addPdfTableRows(amountString);

        CellData[] centerAddress = new CellData[3];
        centerAddress[0] = new CellData(manualTransaction.getProject() != null ? manualTransaction.getProject().getName() : "N/A");
        centerAddress[1] = new CellData(ServerUtils.dateFormat(manualTransaction.getDate().getNonConvertedDate(), shortDateFormat));
        centerAddress[2] = new CellData(manualTransaction.getReference() != null ? manualTransaction.getReference() : "N/A");
        setBgColor(centerAddress);
        center.addPdfTableRows(centerAddress);

        CellData[] extra = new CellData[3];

        extra[0] = new CellData(commonLocalizer.localizeAccounting(PdfLocalizationName.narration, "Narration"));
        extra[0].setFont(defaultFont);
        extra[1] = new CellData(commonLocalizer.localizeAccounting(PdfLocalizationName.checkNumber, "CHQ/TRF Number"));
        extra[1].setFont(defaultFont);
        extra[2] = new CellData(commonLocalizer.localizeAccounting(PdfLocalizationName.number, "Number"));
        extra[2].setFont(defaultFont);
        setBgColor(extra);
        center.addPdfTableRows(extra);

        CellData[] extraValues = new CellData[3];
        extraValues[0] = new CellData(manualTransaction.getNarration() != null ? manualTransaction.getNarration() : "N/A");
        extraValues[1] = new CellData(manualTransaction.getCheckNumber() != null ? manualTransaction.getCheckNumber() : "N/A");
        extraValues[2] = new CellData(manualTransaction.getNumber() != null ? manualTransaction.getNumber() : "N/A");
        setBgColor(extraValues);
        center.addPdfTableRows(extraValues);

        /*CellData[] naration = new CellData[3];

        extraValues[1] = new CellData(manualTransaction.getCheckNumber() != null ? manualTransaction.getCheckNumber() : "N/A");
        extraValues[2] = new CellData(manualTransaction.getNumber() != null ? manualTransaction.getNumber() : "");

        center.addPdfTableRows(extraValues);*/

    }

    private void bottomPanel(ITextTableList bottom, ITextTableList bottom1, NewManualTransaction manualTransaction, DecimalFormat decimalFormat) {
        ArrayList<String> headers = new ArrayList<>();
        headers.add(pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.account));
        headers.add(pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.taxRate));
        headers.add(pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.description));
        headers.add(pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.reference));
        headers.add(pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.amount));
        headers.add(commonLocalizer.localizeAccounting(PdfLocalizationName.name));
        if (enabledProjectInLineItem) {
            headers.add(commonLocalizer.localizeAccounting(PdfLocalizationName.project));
        }
        if (enabledDepartmentRelation) {
            headers.add(commonLocalizer.localize(PdfLocalizationName.department));
        }
        bottom.addPdfTableHeader(headers.toArray(new String[0]));
        if (headers.size() == 6) {
            bottom.addTableWidthPercentage(20, 15, 20, 15, 15, 15);
        }
        if (headers.size() == 7) {
            bottom.addTableWidthPercentage(15, 15, 15, 15, 10, 15, 15);
        }
        if (headers.size() == 8) {
            bottom.addTableWidthPercentage(15, 10, 15, 10, 10, 10, 15, 15);
        }
        BigDecimal subTotalAmount = BigDecimal.ZERO;

        ArrayList<CellData> cellDatas = new ArrayList<>();
        for (NewManualTransactionItem item : manualTransaction.getItems()) {
            cellDatas.clear();
            cellDatas.add(new CellData(item.getAccountItem().getName()));
            cellDatas.add(new CellData((item.getTaxItem() != null && item.getTaxItem().getName() != null) ? item.getTaxItem().getName() : ""));
            cellDatas.add(new CellData(item.getDescription()));
            cellDatas.add(new CellData(item.getReference()));
            cellDatas.add(new CellData(decimalFormat.format(item.getAmount())));
            cellDatas.add(new CellData(item.getCustomerOrSupplier() != null ? item.getCustomerOrSupplier().getName() : ""));
            if (enabledProjectInLineItem) {
                cellDatas.add(new CellData(item.getProject() != null ? item.getProject().getName() : ""));
            }
            if (enabledDepartmentRelation) {
                cellDatas.add(new CellData(item.getDepartment() != null ? item.getDepartment().getName() : ""));
            }
            bottom.addPdfTableRows(cellDatas.toArray(new CellData[0]));
            subTotalAmount = subTotalAmount.add(item.getAmount().setScale(getCalculationScale(), RoundingMode.HALF_UP));


        }

        boolean inBaseCurrency = manualTransaction.getBaseCurrency().getName().equals(manualTransaction.getCurrency().getName());

        bottom1.addTableWidthPercentage(70, 20, 10);
        bottom1.setBorderWidth(0);
        bottom1.setBeforSpacing(20);

        CellData[] baseSubTotalData = new CellData[3];
        baseSubTotalData[0] = new CellData("");
        baseSubTotalData[1] = new CellData(pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.subTotal) + " (" + manualTransaction.getBaseCurrency().getName() + ")");
        baseSubTotalData[2] = new CellData(decimalFormat.format(manualTransaction.getSubtotal().divide(manualTransaction.getExchangeRate(), RoundingMode.HALF_UP)));
        baseSubTotalData[2].setAlignment(Element.ALIGN_RIGHT);
        bottom1.addPdfTableRows(baseSubTotalData);

        if (!inBaseCurrency) {
            CellData[] subTotalData = new CellData[3];
            subTotalData[0] = new CellData("");
            subTotalData[1] = new CellData(pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.subTotal) + " (" + manualTransaction.getCurrency().getName() + ")");
            subTotalData[2] = new CellData(decimalFormat.format(manualTransaction.getSubtotal()));
            subTotalData[2].setAlignment(Element.ALIGN_RIGHT);
            bottom1.addPdfTableRows(subTotalData);
        }

        CellData[] baseTaxTotalData = new CellData[3];
        baseTaxTotalData[0] = new CellData("");
        baseTaxTotalData[1] = new CellData(pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.vat) + ": (" + manualTransaction.getBaseCurrency().getName() + ")");
        baseTaxTotalData[2] = new CellData(decimalFormat.format(manualTransaction.getTaxTotal()));
        baseTaxTotalData[2].setAlignment(Element.ALIGN_RIGHT);
        bottom1.addPdfTableRows(baseTaxTotalData);

        if (!inBaseCurrency) {
            CellData[] taxTotalData = new CellData[3];
            taxTotalData[0] = new CellData("");
            taxTotalData[1] = new CellData(pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.vat) + ": (" + manualTransaction.getCurrency().getName() + ")");
            taxTotalData[2] = new CellData(decimalFormat.format(manualTransaction.getTaxForeignTotal()));
            taxTotalData[2].setAlignment(Element.ALIGN_RIGHT);
            bottom1.addPdfTableRows(taxTotalData);
        }

        CellData[] baseTotalData = new CellData[3];
        baseTotalData[0] = new CellData("");
        baseTotalData[1] = new CellData(pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.total) + " (" + manualTransaction.getBaseCurrency().getName() + ")");
        baseTotalData[2] = new CellData(decimalFormat.format(manualTransaction.getTotal().divide(manualTransaction.getExchangeRate(), RoundingMode.HALF_UP)));
        baseTotalData[2].setAlignment(Element.ALIGN_RIGHT);
        bottom1.addPdfTableRows(baseTotalData);

        if (!inBaseCurrency) {
            CellData[] totalData = new CellData[3];
            totalData[0] = new CellData("");
            totalData[1] = new CellData(pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.total) + " (" + manualTransaction.getCurrency().getName() + ")");
            totalData[2] = new CellData(decimalFormat.format(manualTransaction.getTotal()));
            totalData[2].setAlignment(Element.ALIGN_RIGHT);
            bottom1.addPdfTableRows(totalData);
        }

    }

    private void setBgColor(CellData[] cells) {
        for (CellData items : cells) {
            items.setBgColor(new Color(224, 239, 224));
        }
    }

    @Override
    protected ITextGenericPdfData buildPdfDocumentCustomise(Object dataClass, EdsCompany company, boolean hasPhantom) {
        ITextGenericPdfData pdf = new ITextGenericPdfData();
        pdf.setPdfViewType(ITextPdfViewTypeEnum.BASEINVOICE);
        ITextBaseInvoice baseInvoice = new ITextBaseInvoice();
        pdf.setBaseInvoice(baseInvoice);
        final EdsFinancialSettings fs = financialSettingsManager.getFinancialSettings();

        TransactionPDFObject requestObject = (TransactionPDFObject) dataClass;
        EdsUser user = uploadManager.getUser();
        pdf.setCompanyData(getCompanyData(user.getCompany(), true, hasPhantom));
        DecimalFormat decimalFormat = getPriceScaleNumberFormat(user.getCompany(), requestObject.getTemplateID());

        ListingFilterParameter fp = new ListingFilterParameter();
        fp.setObjectId(requestObject.getObjectID());
        NewManualTransaction manualTransaction = accountingService.getBankTransferData(fp);
        transferType = manualTransaction.getTransferType();
        isBankAccount = (transferType == SPEND_MONEY || transferType == RECEIVE_MONEY);
        CurrencyItem baseCurrencyItem = manualTransaction.getBaseCurrency();
        CurrencyItem bankAccountCurrencyItem = manualTransaction.getBankAccountItem() != null ? manualTransaction.getBankAccountItem().getCurrency() : baseCurrencyItem;
        enabledDepartmentRelation = manualTransaction.isEnabledDepartmentRelation();

        //draw details table
        SimpleDateFormat shortDateFormat = getCompanyShortDateFormat(user.getCompany());
        CustomisedITextTable detailsTable = new CustomisedITextTable();
        detailsTable.addColumnOrder(COLUMN_NAME, COLUMN_VALUE);
        detailsTable.addRowWithCode(TRANSACTION_NUMBER, "No.", escapeHtml(manualTransaction.getNumber()));
        detailsTable.addRowWithCode(ITEM_DESCRIPTION, "Description", escapeHtml(manualTransaction.getNarration()));
        detailsTable.addRowWithCode(NARRATION, "Narration", escapeHtml(manualTransaction.getNarration()));
        detailsTable.addRowWithCode(REFERENCE, "Reference", escapeHtml(manualTransaction.getReference()));
        detailsTable.addRowWithCode(ITEM_DATE, "Date", shortDateFormat.format(manualTransaction.getDate().getNonConvertedDate()));
        detailsTable.addRowWithCode(CHECK_NUMBER, "Cheque No.", escapeHtml(manualTransaction.getCheckNumber()));
        detailsTable.addRowWithCode(PROJECT, "Project", manualTransaction.getProject() != null ? escapeHtml(manualTransaction.getProject().getName()) : "N/A");
        detailsTable.addRowWithCode(ENABLED_DEPARTMENT, "", enabledDepartmentRelation ? "YES" : "NO");
        if (isBankAccount) {
            detailsTable.addRowWithCode(BANK_ACCOUNT, "Bank Account", escapeHtml(manualTransaction.getBankAccountItem().getName()));
            detailsTable.addRowWithCode(BANK_ACCOUNT_CODE, "Bank Account Code", manualTransaction.getBankAccountItem().getBankAccountCode() != null ? escapeHtml(manualTransaction.getBankAccountItem().getBankAccountCode()) : "");
            detailsTable.addRowWithCode(BANK_ACCOUNT_NUMBER, "Bank Account Number", manualTransaction.getBankAccountItem().getAccountNumber() != null ? escapeHtml(manualTransaction.getBankAccountItem().getAccountNumber()) : "");
            detailsTable.addRowWithCode(PDFConstants.DEBIT, "Debit Amount", decimalFormat.format(manualTransaction.getBankAccountItem().getBalance() != null ? manualTransaction.getBankAccountItem().getBalance() : BigDecimal.ZERO));
        } else {
            detailsTable.addRowWithCode(CASH_ACCOUNT, "Cash Account", manualTransaction.getCashAccount().getName() != null ? escapeHtml(manualTransaction.getCashAccount().getName()) : "");
            detailsTable.addRowWithCode(CASH_ACCOUNT_CODE, "Cash Account Code", manualTransaction.getCashAccount().getDescription() != null ? escapeHtml(manualTransaction.getCashAccount().getDescription()) : "");
        }

        //draw item table
        if (manualTransaction.getItems() != null && manualTransaction.getItems().length > 0) {
            String sub_project = manualTransaction.getProject() != null ? escapeHtml(manualTransaction.getProject().getName()) : "";
            BigDecimal exchangeRate = manualTransaction.getExchangeRate();
            StringBuilder nameList = new StringBuilder();
            StringBuilder descList = new StringBuilder();
            NumberToWord numberToWordConverter;
            if (user.getCompany().getLocale() != null && "ru".equals(user.getCompany().getLocale())) {
                numberToWordConverter = new NumberToWord_ru();
            } else if (user.getCompany().getLocale() != null && "ar".equals(user.getCompany().getLocale())) {
                numberToWordConverter = new NumberToWord_ar();
            } else {
                numberToWordConverter = new NumberToWord_en();
            }

            CustomisedITextTable itemTable = new CustomisedITextTable();
            baseInvoice.setProductTableName(requestObject.getViewName().toUpperCase());
            itemTable.addColumnOrder(ACCOUNT_CODE,
                    ACCOUNT_NAME,
                    ITEM_AMOUNT,
                    ITEM_BASE_AMOUNT,
                    RELATED_PROJECT,
                    ITEM_TAX_RATE,
                    ITEM_DESCRIPTION,
                    REFERENCE,
                    ITEM_SUB_PROJECT,
                    PARENT_PROJECT,
                    ITEM_NAME,
                    ITEM_DEPARTMENT,
                    ITEM_TAX_AMOUNT);
            itemTable.addHeaderColumns("Account Code",
                    "Account",
                    "Amount",
                    "Currency",
                    "Project",
                    "Tax",
                    "Description",
                    "Reference",
                    "Sub Project",
                    "Project Main",
                    "Name",
                    "Department",
                    "Tax");

            BigDecimal subTotalAmount = BigDecimal.ZERO;
            for (NewManualTransactionItem item : manualTransaction.getItems()) {
                nameList.append(item.getCustomerOrSupplier() != null ? item.getCustomerOrSupplier().getName() + "," : "");
                descList.append(item.getDescription() != null ? item.getDescription() + ". " : "");
                String description = item.getDescription() != null ? escapeHtml(item.getDescription()) : "";
                String accountCode = escapeHtml(item.getAccountItem().getCode());
                String accountName = escapeHtml(item.getAccountItem().getName());
                String foreignAmount = decimalFormat.format(item.getAmount() != null ? item.getAmount() : BigDecimal.ZERO);
                String baseAmount = decimalFormat.format(item.getAmount() != null ? item.getAmount().divide(exchangeRate, RoundingMode.HALF_DOWN) : BigDecimal.ZERO);
                String taxRate = (item.getTaxItem() != null && item.getTaxItem().getName() != null) ? item.getTaxItem().getName() : "";
                String reference = item.getReference() != null ? escapeHtml(item.getReference()) : "";
                String project = item.getProject() != null ? escapeHtml(item.getProject().getName()) : "";
                String parentProject = item.getParentProject() != null ? escapeHtml(item.getParentProject().getName()) : "";
                String name = item.getCustomerOrSupplier() != null ? escapeHtml(item.getCustomerOrSupplier().getName()) : "";
                String department = item.getDepartment() != null ? escapeHtml(item.getDepartment().getName()) : "";
                String taxAmount = item.getTaxAmount() != null ? decimalFormat.format(item.getTaxAmount()) : "";

                itemTable.addRow(accountCode,
                        accountName,
                        foreignAmount,
                        baseAmount,
                        project,
                        taxRate,
                        description,
                        reference,
                        sub_project,
                        parentProject,
                        name,
                        department,
                        taxAmount);
                subTotalAmount = subTotalAmount.add(item.getAmount().setScale(getCalculationScale(), RoundingMode.HALF_UP));

            }

            boolean isExclusiveTax = manualTransaction.getTaxCalculationType() != null && manualTransaction.getTaxCalculationType() == 2;
            BigDecimal total = manualTransaction.getTaxForeignTotal() != null && isExclusiveTax ? subTotalAmount.add(manualTransaction.getTaxForeignTotal()) : manualTransaction.getTotal();

            if (baseCurrencyItem.getName().equals(bankAccountCurrencyItem.getName())) {
                detailsTable.addRowWithCode(CURRENCY, "Currency", baseCurrencyItem.getName());
                detailsTable.addRowWithCode(BASE_CURRENCY, "Currency Full Name", baseCurrencyItem.getFullName());
            } else {
                detailsTable.addRowWithCode(CURRENCY, "Currency", bankAccountCurrencyItem.getName());
                detailsTable.addRowWithCode(BASE_CURRENCY, "Currency Full Name", escapeHtml(bankAccountCurrencyItem.getFullName()));
            }
            detailsTable.addRowWithCode(TOTAL, "Total", decimalFormat.format(total));
            detailsTable.addRowWithCode(TAX_TOTAL, "Vat", decimalFormat.format(manualTransaction.getTaxForeignTotal() != null ? manualTransaction.getTaxForeignTotal() : manualTransaction.getTaxTotal()));
            detailsTable.addRowWithCode(SUBTOTAL, "Subtotal", decimalFormat.format(manualTransaction.getTaxForeignTotal() != null ? subTotalAmount : manualTransaction.getSubtotal()));
            detailsTable.addRowWithCode(TOTAL_WORD, "Total Word", WordUtils.capitalizeFully(numberToWordConverter.convert(total)));
            detailsTable.addRowWithCode(SUBTOTAL_IN_BASE, "SUBTOTAL_IN_BASE", decimalFormat.format(manualTransaction.getTaxForeignTotal() != null ? subTotalAmount.divide(exchangeRate, 5, RoundingMode.HALF_UP) : manualTransaction.getSubtotal().divide(exchangeRate, 5, RoundingMode.HALF_UP)));
            detailsTable.addRowWithCode(TAX_TOTAL_IN_BASE, "TAX_TOTAL_IN_BASE", decimalFormat.format(manualTransaction.getTaxForeignTotal() != null ? manualTransaction.getTaxForeignTotal().divide(exchangeRate, 5, RoundingMode.HALF_UP) : manualTransaction.getTaxTotal().divide(exchangeRate, 5, RoundingMode.HALF_UP)));
            detailsTable.addRowWithCode(TOTAL_IN_BASE, "Total", decimalFormat.format(total.divide(exchangeRate, 5, RoundingMode.HALF_UP)));

            detailsTable.addRowWithCode(NAME_LIST, "Names", !"".equals(nameList.toString()) ? nameList.substring(0, nameList.toString().length() - 1) : "");
            detailsTable.addRowWithCode(DESCRIPTION_LIST, "Descriptions", !"".equals(descList.toString()) ? descList.toString() : "");
            detailsTable.addRowWithCode(FOREIGN_TOTAL, "Foreign Total", decimalFormat.format(manualTransaction.getTaxForeignTotal() != null ? subTotalAmount.add(manualTransaction.getTaxForeignTotal()) : manualTransaction.getTotal()));
            detailsTable.addRowWithCode(FOREIGN_CURRENCY, "Foreign Currency", manualTransaction.getCurrency() != null ? manualTransaction.getCurrency().getName() : "");
            detailsTable.addRowWithCode(FOREIGN_TOTAL_WORD, "Foreign Total Word", WordUtils.capitalizeFully(numberToWordConverter.convert(manualTransaction.getTaxForeignTotal() != null ? subTotalAmount.add(manualTransaction.getTaxForeignTotal()) : manualTransaction.getTotal())));
            detailsTable.addRowWithCode(EXCHANGE_RATE, "Exchange rate", manualTransaction.getExchangeRate() != null ? decimalFormat.format(manualTransaction.getExchangeRate()) : "");
            detailsTable.addRowWithCode(COMP_VAT_NUMBER, fs != null && fs.getTaxIdNumber() != null ? fs.getTaxIdNumber() : "");
            itemTable.setCustomFields(getCustomFields(manualTransaction.getCustomFieldItems()));
            baseInvoice.setCustomProductTable(itemTable);
        }

        baseInvoice.setCustomBillToAddress(detailsTable);
        pdf.setCreatorData(getCreatorData(uploadManager.getUser()));

        return pdf;
    }

    private ITextUserData getCreatorData(EdsUser user) {
        ITextUserData result = new ITextUserData();
        if (user != null) {
            result.setFullName(escapeHtml(user.getFullName()));
            result.setEmail(escapeHtml(user.getEmail()));
        }
        return result;
    }

    public Map<String, LinkedHashMap<String, Map<String, String>>> getCustomFields(java.util.List<CompanyCustomFieldItem> bankCustomFields) {
        Map<String, LinkedHashMap<String, Map<String, String>>> customFields = new HashMap<>();
        if (bankCustomFields == null || bankCustomFields.isEmpty()) {
            return customFields;
        }
        LinkedHashMap<String, Map<String, String>> itemCusFields = new LinkedHashMap<>();
        SimpleDateFormat dateFormat = getCompanyShortDateFormat(userManager.getUser().getCompany());
        for (CompanyCustomFieldItem item : bankCustomFields) {
            if (item == null) {
                continue;
            }
            Map<String, String> cols = new HashMap<>();
            cols.put(COLUMN_NAME, escapeHtml(item.getFieldName()));
            if (CompanyCustomFieldItem.DATE.equals(item.getDataType())) {
                cols.put(COLUMN_VALUE, item.getFieldDateNonConvertedValue() != null ? escapeHtml(dateFormat.format(item.getFieldDateNonConvertedValue().getNonConvertedDate())) : "");
            } else {
                cols.put(COLUMN_VALUE, escapeHtml(item.getFieldStringValue()));
            }
            if (item.getFieldName() != null) {
                itemCusFields.put(item.getFieldName(), cols);
            }
        }
        customFields.put(PDFConstants.INVOICE, itemCusFields);
        return customFields;
    }

    @Override
    protected Object getDataClass(HttpServletRequest request) {
        TransactionPDFObject requestObject = new TransactionPDFObject();
        requestObject.setObjectID(Integer.valueOf(request.getParameter("objectID")));
        if (!"".equals(request.getParameter("templateID"))) {
            requestObject.setTemplateID(Integer.valueOf(request.getParameter("templateID")));
        }
        requestObject.setViewName(request.getParameter("viewName"));
        return requestObject;
    }

    @Override
    protected Integer getCustomisedPDFTemplateId(Object object) {
        if (object instanceof TransactionPDFObject) {
            return ((TransactionPDFObject) object).getTemplateID();
        }
        return null;
    }

    @Override
    protected String getPdfLogoUrl(EdsCompany edsCompany, boolean hasPhantom) throws IOException {
        String url = super.getPdfAccountingLogoUrl(edsCompany);
        if (StringUtils.isNotEmpty(url)) {
            return url;
        }
        return super.getPdfLogoUrl(edsCompany, hasPhantom);
    }

    @Override
    protected void setFileName(EdsUser user, Object dataClass) {
        TransactionPDFObject requestObject = (TransactionPDFObject) dataClass;
        setFileName(requestObject.getViewName() + "_" + dateFormat(user.getUserDate()));
    }

    @Override
    protected void initPagingAndStamper(PdfReader pdfReader, PdfStamper pdfStamper, Document document, ITextPdfTemplateEvent iTextPdfTemplateEvent, Object dataClass) throws DocumentException {
        audingPdfFooterSignature(pdfReader, pdfStamper, document);
        super.initPagingAndStamper(pdfReader, pdfStamper, document, iTextPdfTemplateEvent, dataClass);
    }

    @Override
    protected PdfReferenceCodeNameEnum getPdfCodeName(Object dataClass) {
        if (dataClass == null) {
            return null;
        }
        if (dataClass instanceof TransactionPDFObject) {
            TransactionPDFObject params = (TransactionPDFObject) dataClass;
            if (params.getTransferType() == null) {
                return null;
            }
            return switch (params.getTransferType()) {
                case 0 -> PdfReferenceCodeNameEnum.BANK_RECEIPT;
                case 1 -> PdfReferenceCodeNameEnum.BANK_PAYMENT;
                case 2 -> PdfReferenceCodeNameEnum.CASH_RECEIPT;
                case 3 -> PdfReferenceCodeNameEnum.CASH_PAYMENT;
                default -> null;
            };
        }
        return null;
    }

    @Override
    protected String getTableName(Object dataClass) {
        if (dataClass == null) {
            return super.getTableName(dataClass);
        }
        if (dataClass instanceof TransactionPDFObject) {
            TransactionPDFObject params = (TransactionPDFObject) dataClass;
            if (params.getTransferType() == null) {
                return super.getTableName(dataClass);
            }
            return switch (params.getTransferType()) {
                case 0 -> commonLocalizer.localizeAccounting(PdfLocalizationName.bankReceipts);
                case 1 -> commonLocalizer.localizeAccounting(PdfLocalizationName.bankPayments);
                case 2 -> commonLocalizer.localizeAccounting(PdfLocalizationName.cashReceipt);
                case 3 -> commonLocalizer.localizeAccounting(PdfLocalizationName.cashPayment);
                default -> null;
            };
        }
        return super.getTableName(dataClass);
    }
}
