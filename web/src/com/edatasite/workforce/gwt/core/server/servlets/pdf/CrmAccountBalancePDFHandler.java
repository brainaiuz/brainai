package com.edatasite.workforce.gwt.core.server.servlets.pdf;

import com.edatasite.workforce.core.domain.EdsAddress;
import com.edatasite.workforce.core.domain.EdsCompany;
import com.edatasite.workforce.core.domain.EdsCurrency;
import com.edatasite.workforce.core.domain.EdsInvoicingSettings;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.accounting.EdsFinancialSettings;
import com.edatasite.workforce.core.domain.accounting.EdsInvoice;
import com.edatasite.workforce.core.domain.crm.EdsCrmAccount;
import com.edatasite.workforce.gwt.accounting.client.ui.AccountingConstants;
import com.edatasite.workforce.gwt.client.client.rpc.ClientService;
import com.edatasite.workforce.gwt.client.client.rpc.CrmAccountBalance;
import com.edatasite.workforce.gwt.client.client.rpc.CrmAccountBalanceItem;
import com.edatasite.workforce.gwt.client.client.rpc.CrmAccountCurrencyBalance;
import com.edatasite.workforce.gwt.contact.client.rpc.CrmAccountItem;
import com.edatasite.workforce.gwt.core.client.rpc.CommonService;
import com.edatasite.workforce.gwt.core.client.rpc.CompanyCustomFieldItem;
import com.edatasite.workforce.gwt.core.client.rpc.CurrencyService;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.currency.CurrencyItem;
import com.edatasite.workforce.gwt.core.client.rpc.currency.CurrencyListItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.customfields.ViewName;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.db.*;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.localization.PdfLocalizationName;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.ITextPdfViewTypeEnum;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.CustomisedITextTable;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.ITextGenericPdfData;
import com.edatasite.workforce.gwt.core.server.utils.CustomFieldsUtils;
import com.edatasite.workforce.gwt.core.server.utils.NumberToWord;
import com.edatasite.workforce.gwt.core.server.utils.NumberToWord_ar;
import com.edatasite.workforce.gwt.core.server.utils.NumberToWord_en;
import com.edatasite.workforce.gwt.core.server.utils.NumberToWord_ru;
import com.edatasite.workforce.gwt.invoice.client.rpc.AgingSummaryInvoiceItem;
import com.edatasite.workforce.gwt.invoice.client.rpc.NewInvoice;
import com.edatasite.workforce.gwt.invoice.client.rpc.NewInvoiceItem;
import com.edatasite.workforce.gwt.invoice.client.rpc.service.InvoiceService;
import com.edatasite.workforce.gwt.invoice.server.app.InvoiceAPIService;
import com.lowagie.text.Document;
import com.lowagie.text.pdf.PdfWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.support.WfmResourceBundleMessageSource;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: 12/28/11
 * Time: 5:45 PM
 * To change this template use File | Settings | File Templates.
 */
public class CrmAccountBalancePDFHandler extends AbstractITextPostPdfHandler implements IPostPDFHandler, PDFConstants {

    @Autowired
    private ClientService clientService;
    @Autowired
    private CrmAccountManager crmAccountManager;
    @Autowired
    private InvoiceManager invoiceManager;
    @Autowired
    private ItemManager itemManager;
    @Autowired
    protected CommonService commonService;
    @Autowired
    private InvoicingSettingsManager invoicingSettingsManager;
    @Autowired
    private CurrencyService currencyService;

    @Qualifier("pdfWfmMessageSource")
    @Autowired
    private WfmResourceBundleMessageSource pdfWfmMessageSource;
    @Autowired
    private CurrencyManager currencyManager;
    @Autowired
    private AccountingManager accountingManager;

    private final String FROM_DATE = "FROM_DATE";
    private final String TO_DATE = "TO_DATE";
    private final String EXCEL_SEPARATE_COLUMN = "EXCEL_SEPARATE_COLUMN";
    private final String NO = "NO";
    private final String BEGINNING_BALANCE = "BEGINNING_BALANCE";
    private final String ENDING_BALANCE = "ENDING_BALANCE";
    private final String ITEM_TRANSACTION = "ITEM_TRANSACTION";
    private final String ITEM_DUE_DATE = "ITEM_DUE_DATE";
    private final String ITEM_REFERENCE = "ITEM_REFERENCE";
    private final String TRANSACTION_TYPE = "TRANSACTION_TYPE";
    private final String ITEM_PAYMENTS = "ITEM_PAYMENTS";
    private final String ITEM_BALANCE = "ITEM_BALANCE";
    private final String ITEM_DEBIT = "ITEM_DEBIT";
    private final String ITEM_CREDIT = "ITEM_CREDIT";
    private final String PAYMENT_INVOICE_NUMBER = "PAYMENT_INVOICE_NUMBER";
    private final String ROW_AMOUNT = "ROW_AMOUNT";
    private final String ITEM_DEBIT_FOREIGN = "ITEM_DEBIT_FOREIGN";
    private final String ITEM_CREDIT_FOREIGN = "ITEM_CREDIT_FOREIGN";
    private final String ITEM_BALANCE_FOREIGN = "ITEM_BALANCE_FOREIGN";
    private final String BEGINNING_BALANCE_FOREIGN = "BEGINNING_BALANCE_FOREIGN";
    private final String ENDING_BALANCE_FOREIGN = "ENDING_BALANCE_FOREIGN";
    private final String ITEM_DEBIT_USD = "ITEM_DEBIT_USD";
    private final String ITEM_CREDIT_USD = "ITEM_CREDIT_USD";
    private final String ITEM_BALANCE_USD = "ITEM_BALANCE_USD";
    private final String BEGINNING_BALANCE_USD = "BEGINNING_BALANCE_USD";
    private final String ENDING_BALANCE_USD = "ENDING_BALANCE_USD";
    private final String ITEM_DEBIT_EUR = "ITEM_DEBIT_EUR";
    private final String ITEM_CREDIT_EUR = "ITEM_CREDIT_EUR";
    private final String ITEM_BALANCE_EUR = "ITEM_BALANCE_EUR";
    private final String BEGINNING_BALANCE_EUR = "BEGINNING_BALANCE_EUR";
    private final String ENDING_BALANCE_EUR = "ENDING_BALANCE_EUR";
    private final String CURRENT_DATE = "CURRENT_DATE";
    private final String FIGURES = "FIGURES";
    private final String TOTAL_WORD = "TOTAL_WORD";
    private final String TOTAL_WORD_ARABIC = "TOTAL_WORD_ARABIC";
    private final String CURRENCY_SYMBOL = "CURRENCY_SYMBOL";

    private final String BALANCE_DUE = "BALANCE_DUE";

    private final String STATEMENT_OF_ACCOUNTS = "STATEMENT_OF_ACCOUNTS";

    private final String TO_ACCOUNTING = "TO_ACCOUNTING";

    private final String ADDRESS_ACCOUNTING = "ADRESS_ACCOUNTING";

    private final String ACCOUNT_SUMMARY = "ACCOUNT_SUMMARY";

    private final String OPENNING_BALANCE = "OPENNING_BALANCE";

    private final String INVOICE_AMOUNT = "INVOICE_AMOUNT";

    private final String AMOUNT_RECEIVED = "AMOUNT_RECEIVED";

    private final String DATE_PDF = "DATE_PDF";

    private final String TRANSACTIONS = "TRANSACTIONS";

    private final String AMOUNT_PDF = "AMOUNT_PDF";

    private final String REFERENCE_PDF = "REFERENCE_PDF";

    private final String PAYMENTS_PDF = "PAYMENTS_PDF";

    private final String BALANCE_PDF = "BALANCE_PDF";

    private final String ADDRESS_PDF = "ADDRESS_PDF";

    @Override
    public ITextGenericPdfData buildPdfDocument(Object dataClass, Document document, PdfWriter writer) throws IOException {
        return null;
    }

    @Override
    protected ITextGenericPdfData buildPdfDocumentCustomise(Object dataClass, EdsCompany company, boolean hasPhantom) {
        ListingFilterParameter fp = (ListingFilterParameter) dataClass;
        Date fromDate = parseFilterParameterDate(fp.getStartDateNC());
        Date toDate = parseFilterParameterDate(fp.getEndDateNC());
        EdsCrmAccount customerOrSuppler = crmAccountManager.get(fp.getCrmAccountId());
        CrmAccountBalance balance = clientService.getCrmAccountBalanceReport(new DateNonConvertable(fromDate), new DateNonConvertable(toDate), fp);
        CurrencyListItem currencyListItem = currencyService.getCurrencyRateByDate(customerOrSuppler.getCurrency().getObjectID(), new DateNonConvertable(new Date()));
        BigDecimal exchangeRate = currencyListItem != null ? BigDecimal.valueOf(currencyListItem.getExchangeRate()) : BigDecimal.ONE;

        EdsCurrency currencyUsd = currencyManager.getCurrency(CurrencyManager.USD);
        CurrencyListItem currencyUSD = currencyService.getCurrencyRateByDate(currencyUsd.getObjectID(), new DateNonConvertable(new Date()));
        BigDecimal exchangeUSD = BigDecimal.valueOf(currencyUSD.getExchangeRate());
        EdsCurrency currencyEur = currencyManager.getCurrency(CurrencyManager.EUR);
        CurrencyListItem currencyEUR = currencyService.getCurrencyRateByDate(currencyEur.getObjectID(), new DateNonConvertable(new Date()));
        BigDecimal exchangeEUR = BigDecimal.valueOf(currencyEUR.getExchangeRate());

        EdsUser user = crmAccountManager.getUser();
        DateFormat dateFormat = getCompanyShortDateFormat(company);
        EdsFinancialSettings fs = financialSettingsManager.getFinancialSettings();
        DecimalFormat priceScaleNumberFormat = getPriceScaleNumberFormat(fs);

        int scale = 2;
        if (fs != null && fs.getCalculationScale() != null) {
            scale = fs.getCalculationScale();
        }

        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setDecimalSeparator('.');
        symbols.setGroupingSeparator(',');
        DecimalFormat customPriceScaleNumberFormat = new DecimalFormat("##0.00", symbols);

        NumberToWord numberToWordConverter = null;
        if (user.getCompany().getLocale() != null && "ru".equals(user.getCompany().getLocale())) {
            numberToWordConverter = new NumberToWord_ru();
        } else {
            numberToWordConverter = new NumberToWord_en();
        }

        NumberToWord numberToWordConverterArabic = null;
        if (isArabicCompany(user)) {
            numberToWordConverterArabic = new NumberToWord_ar();
        }

        ITextGenericPdfData pdfData = new ITextGenericPdfData();
        pdfData.setPdfViewType(ITextPdfViewTypeEnum.LISTTABLE);
        pdfData.setCompanyData(getCompanyData(user.getCompany(), true, hasPhantom));
        Integer calculationScale = fs.getAccountingCalculationScale();
        String currencySymbol = fs.getCurrency().getSymbol();
        LinkedHashMap<String, LinkedList<HashMap<String, CustomisedITextTable>>> customListData = new LinkedHashMap<>();
        LinkedList<HashMap<String, CustomisedITextTable>> customListData2 = new LinkedList<>();
        for (int t = 0; t < balance.getCurrencyBalances().size(); t++) {

            CrmAccountCurrencyBalance currencyBalance = balance.getCurrencyBalances().get(t);

            Map<String, String> header = new HashMap<>();
            header.put(NAME, CrmAccountItem.CUSTOMER.equals(fp.getAccountType()) ? pdfWfmMessageSource.localize("customerBalance", "Customer Balance") : pdfWfmMessageSource.localize("supplierBalance", "Supplier Balance"));
            header.put(USER_NAME, balance.getCrmAccountItem().getName());
            header.put(FROM_DATE, ServerUtils.getUserLocale().getLanguage().equals("uz") ? ServerUtils.convertDateFormatFromEngToUzb(ServerUtils.dateFormat(fromDate, "dd MMM, yyyy")) : ServerUtils.dateFormat(fromDate, "dd MMM, yyyy"));
            header.put(TO_DATE, ServerUtils.getUserLocale().getLanguage().equals("uz") ? ServerUtils.convertDateFormatFromEngToUzb(ServerUtils.dateFormat(toDate, "dd MMM, yyyy")) : ServerUtils.dateFormat(toDate, "dd MMM, yyyy"));
            header.put(EXCEL_SEPARATE_COLUMN, NO);
            header.put(BEGINNING_BALANCE, priceScaleNumberFormat.format(currencyBalance.getEarlyBalance()));
            header.put(ENDING_BALANCE, priceScaleNumberFormat.format(currencyBalance.getEndingBalance()));
            header.put(BEGINNING_BALANCE_FOREIGN, priceScaleNumberFormat.format(currencyBalance.getEarlyBalance().multiply(exchangeRate)));
            header.put(ENDING_BALANCE_FOREIGN, priceScaleNumberFormat.format(currencyBalance.getEndingBalance().multiply(exchangeRate)));
            header.put(BEGINNING_BALANCE_USD, priceScaleNumberFormat.format(currencyBalance.getEarlyBalance().multiply(exchangeUSD)));
            header.put(ENDING_BALANCE_USD, priceScaleNumberFormat.format(currencyBalance.getEndingBalance().multiply(exchangeUSD)));
            header.put(BEGINNING_BALANCE_EUR, priceScaleNumberFormat.format(currencyBalance.getEarlyBalance().multiply(exchangeEUR)));
            header.put(ENDING_BALANCE_EUR, priceScaleNumberFormat.format(currencyBalance.getEndingBalance().multiply(exchangeEUR)));
            header.put(CURRENT_DATE, dateFormat.format(new Date()));
            header.put(FIGURES, accountingLocalizer.localizeAccounting(PdfLocalizationName.figuresIn) + " " + (currencySymbol != null ? currencySymbol : "") + "(" + currencyBalance.getCurrency().getName() + ")");
            CurrencyItem currency = currencyBalance.getCurrency();
            if (currency != null) {
                // Handle Symbol
                String symbol = currency.getSymbol();
                String finalSymbol = (symbol != null && !symbol.isEmpty()) ? symbol : currency.getName();
                header.put(CURRENCY_SYMBOL, finalSymbol);

                // Handle Name
                String name = currency.getName();
                String finalName = (name != null && !name.isEmpty()) ? name : currency.getFullName();
                header.put(CURRENCY_NAME, finalName);
            } else {
                header.put(CURRENCY_SYMBOL, null);
                header.put(CURRENCY_NAME, null);
            }

            header.put(TOTAL_WORD, numberToWordConverter.convert(currencyBalance.getEndingBalance().abs().setScale(scale, BigDecimal.ROUND_HALF_UP)));
            header.put(TOTAL_WORD_ARABIC, numberToWordConverterArabic != null ? numberToWordConverterArabic.convert(currencyBalance.getEndingBalance().abs().setScale(2, BigDecimal.ROUND_HALF_UP)) : "");
            header.put(BALANCE_DUE, pdfWfmMessageSource.localize("balanceDue", "Balance Due"));
            header.put(STATEMENT_OF_ACCOUNTS, pdfWfmMessageSource.localize("statementofAccounts", "Statement of Accounts"));
            header.put(TO_ACCOUNTING, pdfWfmMessageSource.localize("To", "To"));
            header.put(ACCOUNT_SUMMARY, pdfWfmMessageSource.localize("accountSummary", "Account Summary"));
            header.put(OPENNING_BALANCE, pdfWfmMessageSource.localize("openingBalance", "Openning Balance"));
            header.put(INVOICE_AMOUNT, pdfWfmMessageSource.localize("invoicedAmount", "Invoiced Amount"));
            header.put(AMOUNT_RECEIVED, pdfWfmMessageSource.localize("amountReceived", "Amount Received"));
            header.put(DATE_PDF, pdfWfmMessageSource.localize("date", "Date"));
            header.put(TRANSACTIONS, pdfWfmMessageSource.localize("transactions", "Transactions"));
            header.put(AMOUNT_PDF, pdfWfmMessageSource.localize("amount", "Amount"));
            header.put(REFERENCE_PDF, pdfWfmMessageSource.localize("reference", "Reference"));
            header.put(PAYMENTS_PDF, pdfWfmMessageSource.localize("payments", "Payments"));
            header.put(BALANCE_PDF, pdfWfmMessageSource.localize("balance", "Balance"));
            header.put(ADDRESS_PDF, pdfWfmMessageSource.localize("address", "Address"));
            CustomisedITextTable balanceCustomData = new CustomisedITextTable();
            CustomisedITextTable agingSummaryData = new CustomisedITextTable();
            balanceCustomData.setHeader(header);

            balanceCustomData.addColumnOrder(INV_NUMBER, ITEM_DATE, ITEM_TRANSACTION, ITEM_REFERENCE, ITEM_DUE_DATE, TRANSACTION_TYPE,
                    ITEM_AMOUNT, ITEM_PAYMENTS, ITEM_BALANCE, ITEM_DEBIT, ITEM_CREDIT, ROW_AMOUNT,
                    ITEM_NAME, ITEM_DESCRIPTION, ITEM_UNIT_PRICE, ITEM_NET_AMOUNT, PDFConstants.CUSTOMER,
                    ITEM_CUSTOM_AMOUNT, PAYMENT_INVOICE_NUMBER, ITEM_DEBIT_FOREIGN, ITEM_CREDIT_FOREIGN, ITEM_BALANCE_FOREIGN,
                    ITEM_DEBIT_USD, ITEM_CREDIT_USD, ITEM_BALANCE_USD, ITEM_DEBIT_EUR, ITEM_CREDIT_EUR, ITEM_BALANCE_EUR);
            BigDecimal totalAmount = new BigDecimal(0);
            BigDecimal totalPayments = new BigDecimal(0);

            BigDecimal rowAmount = currencyBalance.getEarlyBalance() != null ? currencyBalance.getEarlyBalance() : BigDecimal.ZERO;
            Map<Date, Double> openBalance = new HashMap<>();
            Map<String, LinkedHashMap<String, Map<String, String>>> customFields = new HashMap<>();
            SimpleDateFormat shortDateFormat = new SimpleDateFormat(ServerUtils.getShortDateFormat(user));
            List<Integer> invIds = Arrays.stream(currencyBalance.getItems()).filter(f -> f.getInvoiceID() != null).map(i -> i.getInvoiceID()).toList();
            Map<Integer, EdsInvoice> invoiceListByIds = invoiceManager.getInvoiceListByIds(invIds);
            for (CrmAccountBalanceItem item : currencyBalance.getItems()) {
                currencyListItem = currencyService.getCurrencyRateByDate(customerOrSuppler.getCurrency().getObjectID(), item.getDate_nc());
                BigDecimal exchange = new BigDecimal(currencyListItem.getExchangeRate()).setScale(fs.getExchangeRateScale(), BigDecimal.ROUND_HALF_UP);
                exchangeRate = currencyListItem != null ? exchange : BigDecimal.ONE;
                if (item.getAmount().compareTo(BigDecimal.ZERO) > 0) {
                    totalAmount = totalAmount.add(item.getAmount());
                } else {
                    totalPayments = totalPayments.add(item.getAmount());
                }
                rowAmount = rowAmount.add(item.getAmount());
                String invNumber = item.getNumber();
                String itemDate = ServerUtils.getUserLocale().getLanguage().equals("uz") ? ServerUtils.convertDateFormatFromEngToUzb(ServerUtils.dateFormat(item.getDate_nc().getNonConvertedDate(), shortDateFormat.toPattern())) : ServerUtils.dateFormat(item.getDate_nc().getNonConvertedDate(), shortDateFormat.toPattern());
                Date dateOpenBal = item.getDate_nc().getNonConvertedDate();
                String itemTransaction = item.getTransactionLabel();
                String itemDueDate = item.getDueDate() != null ? dateFormat.format(item.getDueDate()) : "";
                String itemReference = escapeHtml(item.getReference());
                String transactionType = item.getTransactionType() != null ? localizeTransactionType(item.getTransactionType()) : "";
                String itemPayments = "";
                String itemDebit = (item.getDebit() != null ? priceScaleNumberFormat.format(item.getDebit()) : "");
                String itemCredit = (item.getCredit() != null ? priceScaleNumberFormat.format(item.getCredit()) : "");
                String itemBalance = priceScaleNumberFormat.format(item.getBalance());
                String itemDebitForeign = item.getDebit() != null ? priceScaleNumberFormat.format(item.getDebit().multiply(exchangeRate)) : "";
                String itemCreditForeign = item.getCredit() != null ? priceScaleNumberFormat.format(item.getCredit().multiply(exchangeRate)) : "";
                String itemBalanceForeign = priceScaleNumberFormat.format(item.getBalance().multiply(exchangeRate));
                String itemDebitUSD = item.getDebit() != null ? priceScaleNumberFormat.format(item.getDebit().multiply(exchangeRate)) : "";
                String itemCreditUSD = item.getCredit() != null ? priceScaleNumberFormat.format(item.getCredit().multiply(exchangeRate)) : "";
                String itemBalanceUSD = priceScaleNumberFormat.format(item.getBalance().multiply(exchangeRate));
                String itemDebitEUR = item.getDebit() != null ? priceScaleNumberFormat.format(item.getDebit().multiply(exchangeEUR)) : "";
                String itemCreditEUR = item.getCredit() != null ? priceScaleNumberFormat.format(item.getCredit().multiply(exchangeEUR)) : "";
                String itemBalanceEUR = priceScaleNumberFormat.format(item.getBalance().multiply(exchangeEUR));
                String itemAmount = priceScaleNumberFormat.format(item.getAmount());
                String itemAmountFormatted = customPriceScaleNumberFormat.format(item.getAmount());
                Double valueOpenBal = item.getAmount() != null ? item.getAmount().doubleValue() : 0d;
                String rowAmountString = priceScaleNumberFormat.format(rowAmount);
                String itemName = "";
                String itemDescription = "";
                String itemPrice = "";
                String netWithoutDiscount = "";

                if (item.getInvoiceID() != null) {
                    EdsInvoice invoice = invoiceListByIds.get(item.getInvoiceID());
                    if (invoice != null) {
                        NewInvoice newInvoice = EdsInvoice.getInvoiceData(invoice);
                        if (newInvoice.getItems() != null && newInvoice.getItems().length > 0) {
                            NewInvoiceItem invoiceItem = newInvoice.getItems()[0];
                            if (invoiceItem.getItemID() != null && invoiceItem.getItemID() > 0) {
                                itemName = itemManager.get(invoiceItem.getItemID()).getName();
                            } else if (invoiceItem.getItemName() != null) {
                                itemName = invoiceItem.getItemName();
                            } else {
                                itemName = "";
                            }
                            itemDescription = invoiceItem.getDescription() != null ? invoiceItem.getDescription() : NOT_AVAILABLE;
                            itemPrice = invoiceItem.getUnitPrice() != null ? priceScaleNumberFormat.format(invoiceItem.getUnitPrice()) : "";
                            BigDecimal netAmount = invoiceItem.getQuantity().multiply(invoiceItem.getUnitPrice());
                            netWithoutDiscount = netAmount != null ? priceScaleNumberFormat.format(netAmount) : "";
                        }
                        newInvoice.setCustomFieldItems(CustomFieldsUtils.setRPCCustomFieldItems(invoice.getCustomFields(), commonService.getCompanyCustomFields(ViewName.SaleInvoice)));
                        customFields = getCustomFields(null, newInvoice, customFields);
                    }
                }
                if ("Customer opening balance".equals(transactionType) || "Supplier opening balance".equals(transactionType) ||
                        "Customer Supplier opening balance".equals(transactionType) || "Manual".equals(transactionType)) {
                    openBalance.put(dateOpenBal, valueOpenBal);
                }
                String customerOrSuplierName = escapeHtml(item.getClientSupplierName());
                String itemPaymentInvNumber = escapeHtml(item.getPaymentInvoiceNumber());
                balanceCustomData.addRow(invNumber, itemDate, itemTransaction, itemReference, itemDueDate, transactionType, itemAmount,
                        itemPayments, itemBalance, itemDebit, itemCredit, rowAmountString, itemName,
                        itemDescription, itemPrice, netWithoutDiscount, customerOrSuplierName,
                        itemAmountFormatted, itemPaymentInvNumber, itemDebitForeign, itemCreditForeign, itemBalanceForeign,
                        itemDebitUSD, itemCreditUSD, itemBalanceUSD, itemDebitEUR, itemCreditEUR, itemBalanceEUR);
            }
            balanceCustomData.setCustomFields(customFields);
            header.put(ITEM_TOTAL_AMOUNT, priceScaleNumberFormat.format(totalAmount));
            header.put(PAYMENT_TOTAL, priceScaleNumberFormat.format(totalPayments));

            HashMap<String, CustomisedITextTable> customData = new HashMap<>();
            HashMap<String, CustomisedITextTable> customData2 = new HashMap<>();

            customData2.put(CUSTOM_DATA, balanceCustomData);
            customData.put(CUSTOM_DATA, balanceCustomData);

            customListData2.add(customData2);

            if (customerOrSuppler != null) {
                CustomisedITextTable billAddresses = getBillToAddressTable(customerOrSuppler);
                customData.put(CUSTOMER_ADDRESS, billAddresses);
            }

            agingSummaryData.addColumnOrder(COLUMN_VALUE);

            ListingFilterParameter agingFilter = new ListingFilterParameter();
            agingFilter.setAccountType(fp.getAccountType());
            agingFilter.setClientId(fp.getCrmAccountId());
            agingFilter.setDate(new Date());
            agingFilter.setInterval(30);
            agingFilter.setExcludePrePayments(false);

            LinkedHashMap<Integer, ArrayList<AgingSummaryInvoiceItem>> agingMap = accountingManager.getClientSupplierBalanceForAging(agingFilter);

            ArrayList<AgingSummaryInvoiceItem> agingItems = agingMap.getOrDefault(fp.getCrmAccountId(), new ArrayList<>());

            Map<Integer, BigDecimal> balanceByColumn = new HashMap<>();
            for (int i = 0; i < 5; i++) balanceByColumn.put(i, BigDecimal.ZERO);
            BigDecimal curTotal = BigDecimal.ZERO;
            BigDecimal total = BigDecimal.ZERO;

            for (AgingSummaryInvoiceItem item : agingItems) {
                BigDecimal itemAmount = item.getAmount();
                int aging = item.getAging();

                if (aging <= 0) {
                    curTotal = curTotal.add(itemAmount);
                } else if (aging <= 30) {
                    balanceByColumn.merge(0, itemAmount, BigDecimal::add);
                } else if (aging <= 60) {
                    balanceByColumn.merge(1, itemAmount, BigDecimal::add);
                } else if (aging <= 90) {
                    balanceByColumn.merge(2, itemAmount, BigDecimal::add);
                } else if (aging <= 120) {
                    balanceByColumn.merge(3, itemAmount, BigDecimal::add);
                } else {
                    balanceByColumn.merge(4, itemAmount, BigDecimal::add);
                }
            }

            for (int i = 0; i < 5; i++) {
                total = total.add(balanceByColumn.get(i));
            }
            total = total.add(curTotal);
            agingSummaryData.addRowWithCode(WEEKLY_AMOUNT, priceScaleNumberFormat.format(balanceByColumn.get(0)));
            agingSummaryData.addRowWithCode(MONTHLY_AMOUNT, priceScaleNumberFormat.format(balanceByColumn.get(1)));
            agingSummaryData.addRowWithCode(TWO_MONTHLY_AMOUNT, priceScaleNumberFormat.format(balanceByColumn.get(2)));
            agingSummaryData.addRowWithCode(THREE_MONTHLY_AMOUNT, priceScaleNumberFormat.format(balanceByColumn.get(3)));
            agingSummaryData.addRowWithCode(FOUR_MONTHLY_AND_OVER_AMOUNT, priceScaleNumberFormat.format(balanceByColumn.get(4)));
            agingSummaryData.addRowWithCode(CURRENT_AMOUNT, priceScaleNumberFormat.format(curTotal));
            agingSummaryData.addRowWithCode(PDFConstants.TOTAL_AMOUNT, priceScaleNumberFormat.format(total));
            customData.put(AGING_SUMMARY, agingSummaryData);
            customData.put("CURRENCY_DATA", getCurrencyData(currencyListItem));
            pdfData.setCustomData(customData);
        }
        customListData.put("CUSTOM_DATA_LIST", customListData2);

        pdfData.setCustomListData(customListData);
        return pdfData;
    }

    public boolean isArabicCompany(EdsUser edsUser) {
        if (edsUser.getCompany().getCountryZone() != null && edsUser.getCompany().getCountryZone().getCountry() != null) {
            return ("AE".equals(edsUser.getCompany().getCountryZone().getCountry().getCode())
                    || "SA".equals(edsUser.getCompany().getCountryZone().getCountry().getCode())
                    || "OM".equals(edsUser.getCompany().getCountryZone().getCountry().getCode())
                    || "QA".equals(edsUser.getCompany().getCountryZone().getCountry().getCode()));
        }
        return false;
    }

    private CustomisedITextTable getCurrencyData(CurrencyListItem currencyListItem) {
        CustomisedITextTable currencyData = new CustomisedITextTable();
        currencyData.addColumnOrder(COLUMN_NAME, COLUMN_VALUE);
        currencyData.addRowWithCode("FOREIGN_CURRENCY_NAME", "", currencyListItem.getCurrency() != null ? escapeHtml(currencyListItem.getCurrency().getName()) : "");
        currencyData.addRowWithCode("FOREIGN_CURRENCY_SYMBOL", "", currencyListItem.getCurrency() != null ? escapeHtml(currencyListItem.getCurrency().getSymbol()) : "");
        currencyData.addRowWithCode("BASE_CURRENCY_NAME", "", currencyListItem.getBaseCurrency() != null ? escapeHtml(currencyListItem.getBaseCurrency().getName()) : "");
        currencyData.addRowWithCode("BASE_CURRENCY_SYMBOL", "", currencyListItem.getBaseCurrency() != null ? escapeHtml(currencyListItem.getBaseCurrency().getSymbol()) : "");

        return currencyData;
    }

    private String localizeTransactionType(String transactionType) {
        if (INVOICE_TRANSACTION.equals(transactionType)) {
            return "Invoice";
        } else if (INVOICEPAYMENT_TRANSACTION.equals(transactionType)) {
            return "Invoice Payment";
        } else if (CUSTOMER_TRANSACTION.equals(transactionType)) {
            return "Customer opening balance";
        } else if (SUPPLIER_TRANSACTION.equals(transactionType)) {
            return "Supplier opening balance";
        } else if (CUSTOMER_SUPPLIER_PAYMENT_TRANSACTION.equals(transactionType)) {
            return "Customer Supplier opening balance";
        } else if (MANUAL_TRANSACTION.equals(transactionType)) {
            return "Manual";
        } else if (BANK_CHECK_TRANSACTION.equals(transactionType)) {
            return "Bank Check";
        }
        return transactionType;
    }

    private CustomisedITextTable getBillToAddressTable(EdsCrmAccount customerOrSuppler) {
        Map<String, String> values = getBillToAddressMap(customerOrSuppler);
        CustomisedITextTable addressTable = new CustomisedITextTable();
        addressTable.addColumnOrder(COLUMN_VALUE);
        addressTable.addRowWithCode(NAME, escapeHtml(values.get(NAME)));        // Client Name
        addressTable.addRowWithCode(CLIENT_PHONE, escapeHtml(values.get(CLIENT_PHONE)));        // Client Phone
        addressTable.addRowWithCode(CLIENT_FAX, escapeHtml(values.get(CLIENT_FAX)));        // Client Fax
        addressTable.addRowWithCode(CLIENT_CONTACT, escapeHtml(values.get(CLIENT_CONTACT)));  // Contact Name
        addressTable.addRowWithCode(CONTACT_PHONE, escapeHtml(values.get(CONTACT_PHONE)));  // Contact phone
        addressTable.addRowWithCode(CONTACT_EMAIL, escapeHtml(values.get(CONTACT_EMAIL)));  // Contact email
        //Client/Supplier Bill Address
        if (values.get(BILL_ADDRESS_NAME) != null) {
            addressTable.addRowWithCode(BILL_ADDRESS_NAME, escapeHtml(values.get(BILL_ADDRESS_NAME)));      // Address Name
        }
        if (values.get(BILL_ADDRESS) != null) {
            addressTable.addRowWithCode(BILL_ADDRESS, escapeHtml(values.get(BILL_ADDRESS)));      // Address
        }
        if (values.get(BILL_ADDRESS2) != null) {
            addressTable.addRowWithCode(BILL_ADDRESS2, escapeHtml(values.get(BILL_ADDRESS2)));    // Address 2
        }
        if (values.get(BILL_CITY) != null) {
            addressTable.addRowWithCode(BILL_CITY, escapeHtml(values.get(BILL_CITY)));
        }
        if (values.get(BILL_STATE) != null) {
            addressTable.addRowWithCode(BILL_STATE, escapeHtml(values.get(BILL_STATE)));
        }
        if (values.get(BILL_ZIPCODE) != null) {
            addressTable.addRowWithCode(BILL_ZIPCODE, escapeHtml(values.get(BILL_ZIPCODE)));
        }
        if (values.get(BILL_COUNTRY) != null) {
            addressTable.addRowWithCode(BILL_COUNTRY, escapeHtml(values.get(BILL_COUNTRY)));
        }
        //Client/Supplier Mail Address
        if (values.get(MAIL_ADDRESS_NAME) != null) {
            addressTable.addRowWithCode(MAIL_ADDRESS_NAME, escapeHtml(values.get(MAIL_ADDRESS_NAME)));      // Address Name
        }
        if (values.get(MAIL_ADDRESS) != null) {
            addressTable.addRowWithCode(MAIL_ADDRESS, escapeHtml(values.get(MAIL_ADDRESS)));      // Address
        }
        if (values.get(MAIL_ADDRESS2) != null) {
            addressTable.addRowWithCode(MAIL_ADDRESS2, escapeHtml(values.get(MAIL_ADDRESS2)));    // Address 2
        }
        if (values.get(MAIL_CITY) != null) {
            addressTable.addRowWithCode(MAIL_CITY, escapeHtml(values.get(MAIL_CITY)));
        }
        if (values.get(MAIL_STATE) != null) {
            addressTable.addRowWithCode(MAIL_STATE, escapeHtml(values.get(MAIL_STATE)));
        }
        if (values.get(MAIL_ZIPCODE) != null) {
            addressTable.addRowWithCode(MAIL_ZIPCODE, escapeHtml(values.get(MAIL_ZIPCODE)));
        }
        if (values.get(MAIL_COUNTRY) != null) {
            addressTable.addRowWithCode(MAIL_COUNTRY, escapeHtml(values.get(MAIL_COUNTRY)));
        }
        if (values.get(PAYMENT_TERMS) != null) {
            addressTable.addRowWithCode(PAYMENT_TERMS, escapeHtml(values.get(PAYMENT_TERMS)));
        }
        addressTable.addRowWithCode(CLIENT_VAT_NUMBER, escapeHtml(values.get(CLIENT_VAT_NUMBER)));
        addressTable.addRowWithCode(PDFConstants.CLIENT_CODE, escapeHtml(values.get(PDFConstants.CLIENT_CODE)));
        addressTable.setCustomFields(getCustomFields(customerOrSuppler, null, null));
        return addressTable;
    }

    private Map<String, String> getBillToAddressMap(EdsCrmAccount customerOrSuppler) {
        Map<String, String> values = new HashMap<>();
        EdsAddress billAddress = customerOrSuppler.getBillingAddress();
        EdsAddress mailAddress = customerOrSuppler.getMailingAddress();
        values.put(NAME, customerOrSuppler.getName());
        values.put(CLIENT_PHONE, customerOrSuppler.getPhone() != null ? customerOrSuppler.getPhone().replace("|", "") : "");
        values.put(CLIENT_FAX, customerOrSuppler.getFax() != null ? customerOrSuppler.getFax().replace("|", "") : "");
        String title = "";
        String name = "";
        if (customerOrSuppler.getPrimaryContact() != null) {
            title = customerOrSuppler.getPrimaryContact().getTitle() != null
                    ? customerOrSuppler.getPrimaryContact().getTitle() + " "
                    : "";
            name = customerOrSuppler.getPrimaryContact().getName() != null
                    ? title + customerOrSuppler.getPrimaryContact().getName()
                    : "";
            values.put(CLIENT_CONTACT, name);
            values.put(CONTACT_PHONE, customerOrSuppler.getPrimaryContact().getPrimaryPhone() != null ? customerOrSuppler.getPrimaryContact().getPrimaryPhone().replace("|", "") : "");
            values.put(CONTACT_EMAIL, customerOrSuppler.getPrimaryContact().getPrimaryEmail() != null ? customerOrSuppler.getPrimaryContact().getPrimaryEmail() : "");
            customerOrSuppler.getPrimaryContact().getPrimaryContact();
        }

        if (billAddress != null) {
            values.put(BILL_ADDRESS_NAME, billAddress.getName() != null ? billAddress.getName() : "");
            values.put(BILL_ADDRESS, billAddress.getAddress() != null ? billAddress.getAddress() : "");
            values.put(BILL_ADDRESS2, billAddress.getAddressb() != null ? billAddress.getAddressb() : "");
            values.put(BILL_COUNTRY, billAddress.getCountry() != null ? billAddress.getCountry().getName() : "");
            values.put(BILL_CITY, billAddress.getCity() != null ? billAddress.getCity() : "");
            values.put(BILL_STATE, (billAddress.getState() != null && billAddress.getState().getName() != null) ? billAddress.getState().getName() : "");
            values.put(BILL_ZIPCODE, billAddress.getZipCode() != null ? billAddress.getZipCode() : "");
        }
        if (mailAddress != null) {
            values.put(MAIL_ADDRESS_NAME, mailAddress.getName() != null ? mailAddress.getName() : "");
            values.put(MAIL_ADDRESS, mailAddress.getAddress() != null ? mailAddress.getAddress() : "");
            values.put(MAIL_ADDRESS2, mailAddress.getAddressb() != null ? mailAddress.getAddressb() : "");
            values.put(MAIL_COUNTRY, mailAddress.getCountry() != null ? mailAddress.getCountry().getName() : "");
            values.put(MAIL_CITY, mailAddress.getCity() != null ? mailAddress.getCity() : "");
            values.put(MAIL_STATE, (mailAddress.getState() != null && mailAddress.getState().getName() != null) ? mailAddress.getState().getName() : "");
            values.put(MAIL_ZIPCODE, mailAddress.getZipCode() != null ? mailAddress.getZipCode() : "");
        }
        values.put(PAYMENT_TERMS, customerOrSuppler.getTerms() != null ? customerOrSuppler.getTerms().getName() : "");
        values.put(CLIENT_VAT_NUMBER, customerOrSuppler.getVatNumber() != null ? customerOrSuppler.getVatNumber() : "");
        values.put(PDFConstants.CLIENT_CODE, customerOrSuppler.getNumber() != null ? escapeHtml(customerOrSuppler.getNumber()) : "");
        return values;
    }

    public Map<String, LinkedHashMap<String, Map<String, String>>> getCustomFields(EdsCrmAccount customerOrSuppler, NewInvoice invoiceData, Map<String, LinkedHashMap<String, Map<String, String>>> customFields) {
        if (customFields == null) {
            customFields = new HashMap<>();
        }
        if (customerOrSuppler != null && customerOrSuppler.getCustomFields() != null) {
            List<CompanyCustomFieldItem> customFieldItems = CustomFieldsUtils.setRPCCustomFieldItems(customerOrSuppler.getCustomFields(), commonService.getCompanyCustomFields(ViewName.CrmAccount));
            if (customFieldItems != null && customFieldItems.size() > 0) {
                LinkedHashMap<String, Map<String, String>> itemCusFields = new LinkedHashMap<>();
                SimpleDateFormat shortDateFormat = getCompanyShortDateFormat(userManager.getUser().getCompany());
                for (CompanyCustomFieldItem item : customFieldItems) {
                    if (item != null) {
                        Map<String, String> cols = new HashMap<>();
                        cols.put(COLUMN_NAME, item.getFieldName() != null ? escapeHtml(item.getFieldName()) : null);
                        if (CompanyCustomFieldItem.DATE.equals(item.getDataType())) {
                            cols.put(COLUMN_VALUE, item.getFieldDateNonConvertedValue() != null ? escapeHtml(shortDateFormat.format(item.getFieldDateNonConvertedValue().getNonConvertedDate())) : null);
                        } else {
                            cols.put(COLUMN_VALUE, item.getFieldStringValue() != null ? escapeHtml(item.getFieldStringValue()) : null);
                        }
                        if (item.getFieldName() != null) {
                            itemCusFields.put(escapeHtml(item.getFieldName()), cols);
                        }
                    }
                }
                customFields.put(ACCOUNT, itemCusFields);
            }
        }
        if (invoiceData != null) {
            if (invoiceData.getCustomFieldItems() != null && invoiceData.getCustomFieldItems().size() > 0) {
                LinkedHashMap<String, Map<String, String>> itemCusFields = new LinkedHashMap<>();
                SimpleDateFormat shortDateFormat = getCompanyShortDateFormat(userManager.getUser().getCompany());
                for (CompanyCustomFieldItem item : invoiceData.getCustomFieldItems()) {
                    if (item != null) {
                        Map<String, String> cols = new HashMap<>();
                        cols.put(COLUMN_NAME, item.getFieldName() != null ? escapeHtml(item.getFieldName()) : null);
                        if (CompanyCustomFieldItem.DATE.equals(item.getDataType())) {
                            cols.put(COLUMN_VALUE, item.getFieldDateNonConvertedValue() != null ? escapeHtml(shortDateFormat.format(item.getFieldDateNonConvertedValue().getNonConvertedDate())) : null);
                        } else {
                            cols.put(COLUMN_VALUE, item.getFieldStringValue() != null ? escapeHtml(item.getFieldStringValue()) : null);
                        }
                        if (item.getFieldName() != null) {
                            itemCusFields.put(item.getFieldName(), cols);
                        }
                    }
                }
                customFields.put(invoiceData.getInvoiceNumber(), itemCusFields);
            }
        }
        return customFields;
    }

    @Override
    protected void setFileName(EdsUser user, Object dataClass) {
        ListingFilterParameter fp = (ListingFilterParameter) dataClass;
        boolean isCustomer = CrmAccountItem.CUSTOMER.equals(fp.getAccountType());
        DateFormat dateFormat = getCompanyShortDateFormat(user.getCompany());
        EdsInvoicingSettings invoicingSettings = invoicingSettingsManager.getInvoiceSettings(user.getCompany());
        if (invoicingSettings != null) {
            Map<String, String> params = new HashMap<>();
            String pdfFormat = isCustomer ? invoicingSettings.getCustomerPdfNamingFormat() : invoicingSettings.getSupplierPdfNamingFormat();
            String pdfPrefix = isCustomer ? invoicingSettings.getCustomerPdfNamingPrefix() : invoicingSettings.getSupplierPdfNamingPrefix();
            if (pdfPrefix != null && !"".equals(pdfPrefix)) {
                params.put(AccountingConstants.PDF_PREFIX, pdfPrefix);
            }
            EdsCrmAccount customerOrSuppler = crmAccountManager.get(fp.getCrmAccountId());
            if (customerOrSuppler != null) {
                params.put(AccountingConstants.PDF_CLIENT, customerOrSuppler.getName());
                params.put(AccountingConstants.PDF_CLIENT_CODE, customerOrSuppler.getNumber());
            }
            params.put(AccountingConstants.PDF_COMPANY_NAME, user.getCompany().getName());
            params.put(AccountingConstants.PDF_GENERATED_DATE, dateFormat.format(user.getUserDate()));
            params.put(AccountingConstants.PDF_USER_NAME, user.getName());

            String[] format = (pdfFormat != null && !"".equals(pdfFormat)) ? pdfFormat.split("_") : new String[0];
            StringBuilder fileName = new StringBuilder();
            if (!params.isEmpty()) {
                for (String aFormat : format) {
                    String value = params.get(aFormat);
                    if (value != null && !"".equals(value.trim())) {
                        fileName.append(fileName.length() > 0 ? ("-" + value) : value);
                    }
                }
            }
            if (fileName.length() > 0) {
                setFileName(fileName.toString());
            } else {
                setFileName(isCustomer ? "Customer_Balance" : "Supplier_Balance");
            }

        } else {
            setFileName(isCustomer ? "Customer_Balance" : "Supplier_Balance");
        }
    }

    @Override
    protected PdfReferenceCodeNameEnum getPdfCodeName(Object dataClass) {
        return PdfReferenceCodeNameEnum.CUSTOMER_SUPPLIER_BALANCE;
    }

    @Override
    protected String getTableName(Object dataClass) {
//        ListingFilterParameter fp = (ListingFilterParameter) dataClass;
//        boolean isCustomer = CrmAccountItem.CUSTOMER.equals(fp.getAccountType());

        return "";
    }

    @Override
    protected String getPdfLogoUrl(EdsCompany edsCompany, boolean hasPhantom) throws IOException {
        return super.getPdfAccountingLogoUrl(edsCompany);
    }

    @Override
    protected Integer getCustomisedPDFTemplateId(Object object) {
        ListingFilterParameter fp = (ListingFilterParameter) object;
        return fp.getTemplateID();
    }
}
