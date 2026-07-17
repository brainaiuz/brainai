package com.edatasite.workforce.gwt.core.server.servlets.pdf.invoice;

import com.edatasite.workforce.core.domain.EdsCompany;
import com.edatasite.workforce.core.domain.EdsProperty;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.accounting.EdsFinancialSettings;
import com.edatasite.workforce.gwt.accounting.client.rpc.AccountingService;
import com.edatasite.workforce.gwt.accounting.client.rpc.AddAccountItem;
import com.edatasite.workforce.gwt.accounting.client.rpc.Transaction;
import com.edatasite.workforce.gwt.accounting.client.rpc.TransactionsBetweenDatesInAccount;
import com.edatasite.workforce.gwt.accounting.client.ui.AccountingConstants;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.AbstractITextPostPdfHandler;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.PdfReferenceCodeNameEnum;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.localization.PdfLocalizationName;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.CellData;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.CustomisedITextTable;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.ITextGenericPdfData;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.ITextTableList;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.PdfParams;
import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.pdf.PdfWriter;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Locale;

/**
 * Created by IntelliJ IDEA.
 * User: xushnud
 * Date: 14-May-2010
 * Time: 16:18:23
 * To change this template use File | Settings | File Templates.
 */
public class TransactionByPeriodPdfHandler extends AbstractITextPostPdfHandler {

    @Autowired
    private AccountingService accountingService;

    @Override
    public ITextGenericPdfData buildPdfDocument(Object dataClass, Document document, PdfWriter writer) {
        return null;
    }

    @Override
    protected ITextGenericPdfData buildPdfDocumentCustomise(Object dataClass, EdsCompany company, boolean hasPhantom) {
        ListingFilterParameter fp = (ListingFilterParameter) dataClass;
        Date startDate = parseFilterParameterDate(fp.getStartDateNC());
        Date endDate = parseFilterParameterDate(fp.getEndDateNC());

        SimpleDateFormat format;
        if (company.getCompanySettings() != null && StringUtils.isNotEmpty(company.getCompanySettings().getShortDateFormat())) {
            format = new SimpleDateFormat(company.getCompanySettings().getShortDateFormat(), Locale.ENGLISH);
        } else {
            format = new SimpleDateFormat("MMM d yyyy", Locale.ENGLISH);
        }

        int limit = 3000;
        EdsFinancialSettings fs = financialSettingsManager.getFinancialSettings();
        String currencySymbol = fs.getCurrency().getSymbol();
        String currencyCode = fs.getCurrency().getName();
        currencySymbol = currencySymbol != null ? currencySymbol : "";

        ITextGenericPdfData pdfData = new ITextGenericPdfData();
        if (ServerUtils.getUserLocale().getLanguage().equals("uz")) {
            pdfData.setCurrentDate(StringUtils.join(commonLocalizer.localize(PdfLocalizationName.from), " ", ServerUtils.convertToUzbDateFormat(format.format(startDate)), " ",
                    commonLocalizer.localize(PdfLocalizationName.to), " ", ServerUtils.convertToUzbDateFormat(format.format(endDate))));
        } else {
            pdfData.setCurrentDate(StringUtils.join(commonLocalizer.localize(PdfLocalizationName.from), " ", format.format(startDate), " ",
                    commonLocalizer.localize(PdfLocalizationName.to), " ", format.format(endDate)));
        }
        HashMap<String , String> localizePageLabel = new HashMap<>();
        localizePageLabel.put("PAGE_LABEL", pdfWfmMessageSource.localize("page"));
        localizePageLabel.put("OF_LABEL", pdfWfmMessageSource.localize("of"));
        pdfData.setLocalizeMap(localizePageLabel);

        pdfData.setExtraData(StringUtils.join(commonLocalizer.localize(PdfLocalizationName.figuresIn), " ", currencySymbol, "(", currencyCode, ")"));

        ITextTableList table = new ITextTableList(8);
        table.addPdfTableHeader(
                new CellData(commonLocalizer.localize(PdfLocalizationName.date), Element.ALIGN_LEFT, 8),
                new CellData(accountingLocalizer.localize(PdfLocalizationName.transactionDescription), Element.ALIGN_LEFT, 24),
                new CellData(commonLocalizer.localize(PdfLocalizationName.number), Element.ALIGN_LEFT, 8),
                new CellData(commonLocalizer.localize(PdfLocalizationName.journalID), Element.ALIGN_LEFT, 8),
                new CellData(commonLocalizer.localize(PdfLocalizationName.reference), Element.ALIGN_LEFT, 16),
                new CellData(commonLocalizer.localize(PdfLocalizationName.debit), Element.ALIGN_RIGHT, 12),
                new CellData(accountingLocalizer.localize(PdfLocalizationName.credit), Element.ALIGN_RIGHT, 12),
                new CellData(accountingLocalizer.localize(PdfLocalizationName.balance), Element.ALIGN_RIGHT, 12)
        );
        pdfData.setListTable(table);

        fp.setStartDate(startDate);
        fp.setEndDate(endDate);
        fp.setLimit(limit);
        TransactionsBetweenDatesInAccount transactions = accountingService.findTransactionsByAccountAndJournalDate(fp, startDate != null ? new DateNonConvertable(startDate) : null, endDate != null ? new DateNonConvertable(endDate) : null);
        DecimalFormat priceScaleNumberFormat = getPriceScaleNumberFormat(fs);

        AddAccountItem account = accountingService.getAccount(fp.getAccountID());

        HashMap<String, CustomisedITextTable> customData = new LinkedHashMap<>();
        customData.put("BEGINNING_BALANCE", getBegEndBalance(transactions, priceScaleNumberFormat, true));
        customData.put("TRANSACTIONS", getTransactions(transactions, priceScaleNumberFormat, format));
        customData.put("TOTAL", getTotalBalance(transactions, priceScaleNumberFormat));
        customData.put("ENDING_BALANCE", getBegEndBalance(transactions, priceScaleNumberFormat, false));
        customData.put("ACCOUNT_TYPE", getAccountType(transactions));
        customData.put("ACCOUNT_NAME", getAccountName(account));
        customData.put("ACCOUNT_CODE", getAccountCode(account));
        pdfData.setCustomData(customData);
        return pdfData;
    }

    private CustomisedITextTable getTransactions(TransactionsBetweenDatesInAccount transactions, DecimalFormat priceScaleNumberFormat, SimpleDateFormat format) {
        if (CollectionUtils.isNotEmpty(transactions.getTransactions())) {
            CustomisedITextTable table = new CustomisedITextTable();
            String accountType = transactions.getAccountType();
            BigDecimal beginning = transactions.getTotalBeginningBalance() != null ? transactions.getTotalBeginningBalance() : BigDecimal.ZERO;

            int i = 0;
            for (Transaction item : transactions.getTransactions()) {
                LinkedList<String> list = new LinkedList<>();

                String journalName = item.getJournalName();
                String number = item.getNumber();
                if (INVOICE_TRANSACTION.equals(item.getTransactionType())) {
                    if (item.getKeyId() != null) {
                        number = item.getNumber();
                        journalName = item.getJournalName();
                    }
                } else if (GOODS_RECEIVED_TRANSACTION.equals(item.getTransactionType())) {
                    number = item.getNumber();
                    journalName = accountingLocalizer.localizeAccounting(PdfLocalizationName.purchaseOrder) + ": " + item.getJournalName();
                } else if (INVOICEPAYMENT_TRANSACTION.equals(item.getTransactionType())) {
                    number = item.getInvoicePaymentNumber();
                    journalName = item.getJournalName();
                    if (item.getPaymentType() != null && AccountingConstants.RECEIVABLE_PREPAYMENT.equals(item.getPaymentType())) {
                        journalName = "Prepayment: " + journalName + " Client:" + item.getClientName();
                    } else if (item.getPaymentType() != null && AccountingConstants.PAYABLE_SUPPLIER_CREDIT.equals(item.getPaymentType())) {
                        journalName = "Supplier Credit: " + journalName + " Supplier: " + item.getSupplierName();

                    }
                } else if (INVENTORY_TRANSACTION.equals(item.getTransactionType())) {
                    number = item.getProductNumber();
                    journalName = item.getJournalName();
                } else if (MANUAL_TRANSACTION.equals(item.getTransactionType())) {
                    number = item.getManualJournalNumber();
                    journalName = commonLocalizer.localizeAccounting(PdfLocalizationName.manualEntry, "Manual Entry") + ": " + item.getNarration();
                } else if (EXPENSE_TRANSACTION.equals(item.getTransactionType())) {
                    number = item.getExpenseNumber();
                    String descripton = StringUtils.isNotEmpty(item.getDescription()) ? item.getDescription() : item.getExpenseTitle();
                    journalName = accountingLocalizer.localizeAccounting(PdfLocalizationName.expenseClaims) + ": " + escapeHtml(descripton);
                } else if (EXPENSEPAYMENT_TRANSACTION.equals(item.getTransactionType())) {
                    journalName = accountingLocalizer.localizeAccounting(PdfLocalizationName.expensePaymentTransaction);
                } else if (FIXED_ASSET_TRANSACTION.equals(item.getTransactionType())) {
                    number = item.getFixedAssetNumber();
                    journalName = commonLocalizer.localizeAccounting(PdfLocalizationName.fixedAsset) + ": " + item.getFixedAssetName();
                } else if (DISPOSAL_TRANSACTION.equals(item.getTransactionType())) {
                    number = item.getFixedAssetNumber();
                    journalName = commonLocalizer.localizeAccounting(PdfLocalizationName.fixedAsset) + " Disposal: " + item.getFixedAssetName();
                } else if (CUSTOMER_SUPPLIER_PAYMENT_TRANSACTION.equals(item.getTransactionType())) {
                    journalName = item.getJournalName();
                } else if (BANK_CHECK_TRANSACTION.equals(item.getTransactionType())) {
                    number = item.getCheckNumber();
                    journalName = accountingLocalizer.localizeAccounting(PdfLocalizationName.check);
                } else if (BANK_TRANSFER_TRANSACTION.equals(item.getTransactionType())) {
                    String type = "", name = "";
                    if (AccountingConstants.RECEIVE_MONEY.equals(item.getSpendReceiveMoneyType())) {
                        name = accountingLocalizer.localizeAccounting(PdfLocalizationName.bankReceipts);
                    } else if (AccountingConstants.SPEND_MONEY.equals(item.getSpendReceiveMoneyType())) {
                        name = accountingLocalizer.localizeAccounting(PdfLocalizationName.bankPayments);
                    } else if (AccountingConstants.CASH_RECEIPT.equals(item.getSpendReceiveMoneyType())) {
                        name = accountingLocalizer.localizeAccounting(PdfLocalizationName.cashReceipt);
                    } else if (AccountingConstants.CASH_PAYMENT.equals(item.getSpendReceiveMoneyType())) {
                        name = accountingLocalizer.localizeAccounting(PdfLocalizationName.cashPayment);
                    }
                    journalName = name + ": " + item.getSpendReceiveMoneyNarration();
                    number = item.getSpendReceiveMoneyNumber();
                } else if (BANK_MONEY_TRANSFER_TRANSACTION.equals(item.getTransactionType())) {
                    journalName = item.getJournalName();
                } else if (ADJUSTMENT_TRANSACTION.equals(item.getTransactionType())) {
                    journalName = accountingLocalizer.localizeAccounting(PdfLocalizationName.stockAdjustments);
                    number = item.getStockAdjustmentNumber();
                } else if (STOCK_TRANSFER_TRANSACTION.equals(item.getTransactionType())) {
                    journalName = accountingLocalizer.localizeAccounting(PdfLocalizationName.stockTransfer);
                    number = item.getReference();
                } else if (RETAINED_EARNINGS_TRANSACTION.equals(item.getTransactionType())) {
                    journalName = item.getJournalName();
                }

                String journalDate = format.format(item.getJournalDate().getNonConvertedDate());
                String journalId = String.valueOf(item.getJournalId() != null ? item.getJournalId() : "");
                String debit = item.getTotalDebit() != null ? priceScaleNumberFormat.format(item.getTotalDebit()) : "";
                String credit = item.getTotalCredit() != null ? priceScaleNumberFormat.format(item.getTotalCredit()) : "";
                String reference = "";
                if (MANUAL_TRANSACTION.equals(item.getTransactionType())) {
                    reference = !ServerUtils.isNullOrEmpty(item.getDescription()) ? item.getDescription() : "";
                } else {
                    reference = !ServerUtils.isNullOrEmpty(item.getReference()) ? item.getReference() : "";
                }
                BigDecimal totalDebit = item.getTotalDebit() != null ? item.getTotalDebit() : BigDecimal.ZERO;
                BigDecimal totalCredit = item.getTotalCredit() != null ? item.getTotalCredit() : BigDecimal.ZERO;

                if (ASSETS.equals(accountType) || EXPENSES.equals(accountType)) {
                    beginning = beginning.add(totalDebit.subtract(totalCredit));
                } else {
                    beginning = beginning.add(totalCredit.subtract(totalDebit));
                }

                list.add(journalDate);
                list.add(journalName);
                list.add(number != null ? number : "");
                list.add(journalId);
                list.add(reference);
                list.add(debit);
                list.add(credit);
                list.add(getValueAsString(beginning, priceScaleNumberFormat));
                table.addTotalRow("" + i++, list);
            }
            return table;
        }
        return null;
    }
    private CustomisedITextTable getAccountName(AddAccountItem account){
        CustomisedITextTable data = new CustomisedITextTable();
        if (account != null){
            data.setName(account.getName());
        }
        return data;
    }
    private CustomisedITextTable getAccountCode(AddAccountItem account){
        CustomisedITextTable data = new CustomisedITextTable();
        if (account != null){
            data.setName(account.getCode());
        }
        return data;
    }

    private CustomisedITextTable getBegEndBalance(TransactionsBetweenDatesInAccount transactions, DecimalFormat priceScaleNumberFormat, boolean isBeginning) {
        CustomisedITextTable table = new CustomisedITextTable();
        table.setName(commonLocalizer.localize(isBeginning ? PdfLocalizationName.beginningBalance : PdfLocalizationName.endingBalance));
        table.addColumn("TOTAL", getValueAsString(isBeginning ? transactions.getTotalBeginningBalance() : transactions.getTotalBalance(), priceScaleNumberFormat));
        return table;
    }

    private CustomisedITextTable getAccountType(TransactionsBetweenDatesInAccount transactions) {
        CustomisedITextTable accountTypeTable = new CustomisedITextTable();
        String accountType = transactions != null && transactions.getAccountType() != null ? transactions.getAccountType() : "";
        accountTypeTable.addColumn("ACCOUNT_TYPE", accountType);
        return accountTypeTable;
    }

    private CustomisedITextTable getTotalBalance(TransactionsBetweenDatesInAccount transactions, DecimalFormat priceScaleNumberFormat) {
        CustomisedITextTable table = new CustomisedITextTable();
        table.setName(commonLocalizer.localize(PdfLocalizationName.total));
        table.addColumn("DEBIT", getValueAsString(transactions.getTotalDebit(), priceScaleNumberFormat));
        table.addColumn("CREDIT", getValueAsString(transactions.getTotalCredit(), priceScaleNumberFormat));
        table.addColumn("TOTAL", getValueAsString(transactions.getTotalDebit().subtract(transactions.getTotalCredit()), priceScaleNumberFormat));
        return table;
    }

    private String getValueAsString(BigDecimal value, DecimalFormat priceScaleNumberFormat) {
        if (value.compareTo(BigDecimal.ZERO) >= 0) {
            return priceScaleNumberFormat.format(value);
        } else {
            return "(" + priceScaleNumberFormat.format(value.abs()) + ")";
        }
    }

    @Override
    protected PdfReferenceCodeNameEnum getPdfCodeName(Object dataClass) {
        return PdfReferenceCodeNameEnum.ACCOUNT_TRANSACTIONS;
    }

    @Override
    protected String getTableName(Object dataClass) {
        ListingFilterParameter filterParameter = (ListingFilterParameter) dataClass;
        EdsProperty property = propertManager.findByCode(filterParameter.getPropertyCode());
        return property != null ? property.getPlural() : commonLocalizer.localize(PdfLocalizationName.accountTransactions);
    }

    @Override
    protected void setFileName(EdsUser user, Object dataClass) {
        ListingFilterParameter fp = (ListingFilterParameter) dataClass;
        setFileName(user.getFirstName() + "_" + user.getLastName() + "_" + fp.getName() + "_" + dateFormat(user.getUserDate()));
    }

    @Override
    protected PdfParams.Orientation getOrientation(Object dataClass) {
        ListingFilterParameter filterParametrs = (ListingFilterParameter) dataClass;
        return filterParametrs.isLandscape() ? PdfParams.Orientation.landscape : null;
    }
}
