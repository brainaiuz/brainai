package com.edatasite.workforce.gwt.core.server.commons;

import com.edatasite.shared.poiutils.WorkBook;
import com.edatasite.workforce.core.domain.EdsProperty;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.accounting.EdsFinancialSettings;
import com.edatasite.workforce.core.domain.settings.EdsCompanySettings;
import com.edatasite.workforce.gwt.accounting.client.rpc.AccountingService;
import com.edatasite.workforce.gwt.accounting.client.rpc.Transaction;
import com.edatasite.workforce.gwt.accounting.client.rpc.TransactionsBetweenDatesInAccount;
import com.edatasite.workforce.gwt.accounting.client.ui.AccountingConstants;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.db.PropertManager;
import com.edatasite.workforce.gwt.core.server.db.TransactionManager;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.localization.PdfLocalizationName;
import com.edatasite.workforce.gwt.team.client.rpc.DepartmentService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.jsoup.Jsoup;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

/**
 * Created by IntelliJ IDEA.
 * User: Atabek Boboyev
 * Date: 16.03.12
 * Time: 14:04
 * To change this template use File | Settings | File Templates.
 */
public class TransactionByPeriodExcelHandler extends BaseExcelHandler implements Constants {
    @Autowired
    private AccountingService accountingService;
    @Autowired
    private TransactionManager transactionManager;
    @Autowired
    private DepartmentService departmentService;
    @Autowired
    private PropertManager propertManager;
    private String sheetName;

    final BigDecimal ZERO = new BigDecimal("0.00");
    private static final Logger log = LoggerFactory.getLogger(LocationListExcelHandler.class);

    @Override
    protected void setFileName() {
        filename = "";
    }

    @Override
    protected HSSFWorkbook getWorkBook(Object object) {
        ListingFilterParameter fp = (ListingFilterParameter) object;
        EdsProperty property = propertManager.findByCode(fp.getPropertyCode());
        sheetName = property != null ? property.getPlural() : commonLocalizer.localize(PdfLocalizationName.accountTransactions);
        EdsUser user = userManager.getUser();
        List<ExcelData[]> list = new LinkedList<>();

        String shortDateFormat = "MMM dd, yyyy";
        EdsCompanySettings companySettings = user.getCompany().getCompanySettings();
        if (companySettings != null && companySettings.getShortDateFormat() != null && !"".equals(companySettings.getShortDateFormat())) {
            shortDateFormat = companySettings.getShortDateFormat();
        }
        SimpleDateFormat format = new SimpleDateFormat(shortDateFormat, Locale.ENGLISH);

        Date fromDate = parseFilterParameterDate(fp.getStartDateNC());
        Date toDate = parseFilterParameterDate(fp.getEndDateNC());
        String accountName = fp.getName();
        String[] codeAndName = fp.getName().split(" -> ");
        if (codeAndName != null && codeAndName.length > 1)
            accountName = codeAndName[1];

        EdsFinancialSettings fs = financialSettingsManager.getFinancialSettings();
        String currencySymbol = fs.getCurrency().getSymbol();
        String currencyCode = fs.getCurrency().getName();
        currencySymbol = currencySymbol != null ? currencySymbol : "";

        fp.setStartDate(fromDate);
        fp.setEndDate(toDate);

        Integer count;
        Integer limit = 64000;

        String teamIDs = getParentAndChildDepartmentTreeIDs(fp.getDepartmentId());

        if (fp.isShowBudget()) {
            BigDecimal[] totalsCashOnly = transactionManager.getListCashOnlyInAccountInPeriodCount(fp, teamIDs);
            count = totalsCashOnly[0].intValue();
        } else {
            BigDecimal[] totals = transactionManager.getListAttendedInAccountInPeriodCount(fp, teamIDs);
            count = totals[0].intValue();
        }

        int step = (int) Math.ceil(count / limit.doubleValue());


        fp.setForExportOnly(true);
        fp.setLimit(limit);
        fp.setShowActive(true);

        ExcelData[] cellHeader;

        int aCellSize = 15;
        String str = "";
        int lastColumnIndex = 5;

        ExcelData probelExc = ExcelData.getReportNameChildDataWithOutBorderInStart(str, aCellSize, lastColumnIndex);

        ExcelData titleData = ExcelData.getReportNameData(sheetName, aCellSize, lastColumnIndex);

        ExcelData accountData = ExcelData.getReportNameChildData(accountName, aCellSize, lastColumnIndex);

        ExcelData dateData;
        ExcelData companyData = ExcelData.getReportNameChildData(user.getCompany().getName(), aCellSize, lastColumnIndex);
        if (ServerUtils.getUserLocale().getLanguage().equals("uz")) {
            dateData = ExcelData.getReportNameChildData(commonLocalizer.localize(PdfLocalizationName.from) + " "
                    + ServerUtils.convertToUzbDateFormat(format.format(fromDate)) + " " + commonLocalizer.localize(PdfLocalizationName.to) + " "
                    + ServerUtils.convertToUzbDateFormat(format.format(toDate)), aCellSize, lastColumnIndex);
        } else {
            dateData = ExcelData.getReportNameChildData(commonLocalizer.localize(PdfLocalizationName.from) + " "
                    + format.format(fromDate) + " " + commonLocalizer.localize(PdfLocalizationName.to) + " "
                    + format.format(toDate), aCellSize, lastColumnIndex);
        }

        ExcelData currencyData = ExcelData.getReportNameChildData(commonLocalizer.localizeAccounting(PdfLocalizationName.figuresIn) + " " + currencySymbol + "(" + currencyCode + ")", aCellSize, lastColumnIndex);
        list.add(new ExcelData[]{
                probelExc
        });
        list.add(new ExcelData[]{titleData});
        list.add(new ExcelData[]{accountData});
        list.add(new ExcelData[]{companyData});
        list.add(new ExcelData[]{dateData});
        list.add(new ExcelData[]{currencyData});
        list.add(new ExcelData[]{
                probelExc
        });

        ExcelData dataCell = new ExcelData(accountingLocalizer.localize(PdfLocalizationName.date), ExcelData.STRING, aCellSize, false, true, ExcelData.NO_BORDER, ExcelData.HEADER_LEFT);
        int bCellSize = 37;
        ExcelData transactionCell = new ExcelData(accountingLocalizer.localize(PdfLocalizationName.transaction), ExcelData.STRING, bCellSize, false, true, ExcelData.NO_BORDER, ExcelData.HEADER_LEFT);
        ExcelData numberCell = new ExcelData(commonLocalizer.localize(PdfLocalizationName.number), ExcelData.STRING, bCellSize, false, true, ExcelData.NO_BORDER, ExcelData.HEADER_LEFT);
        int eCellSize = 16;
        ExcelData journalIdCell = new ExcelData(accountingLocalizer.localize(PdfLocalizationName.journalID), ExcelData.STRING, eCellSize, false, true, ExcelData.NO_BORDER, ExcelData.HEADER_LEFT);
        int cCellSize = 20;
        ExcelData referenceCell = new ExcelData(accountingLocalizer.localize(PdfLocalizationName.reference), ExcelData.STRING, cCellSize, false, true, ExcelData.NO_BORDER, ExcelData.HEADER_LEFT);
        int dCellSize = 16;
        ExcelData debitCell = new ExcelData(accountingLocalizer.localize(PdfLocalizationName.debit), ExcelData.STRING, dCellSize, false, true, ExcelData.NO_BORDER, ExcelData.HEADER_RIGHT);
        ExcelData creditCell = new ExcelData(accountingLocalizer.localize(PdfLocalizationName.credit), ExcelData.STRING, eCellSize, false, true, ExcelData.NO_BORDER, ExcelData.HEADER_RIGHT);
        ExcelData balanceCell = new ExcelData(accountingLocalizer.localize(PdfLocalizationName.balance), ExcelData.STRING, eCellSize, false, true, ExcelData.NO_BORDER, ExcelData.HEADER_RIGHT);

        dataCell.setBold(true);
        transactionCell.setBold(true);
        numberCell.setBold(true);
        journalIdCell.setBold(true);
        referenceCell.setBold(true);
        debitCell.setBold(true);
        creditCell.setBold(true);
        balanceCell.setBold(true);

        cellHeader = new ExcelData[]{dataCell, transactionCell, numberCell, journalIdCell, referenceCell, debitCell, creditCell, balanceCell};
        list.add(cellHeader);

        if (step == 0) {
            fp.setForExportOnly(false);
            TransactionsBetweenDatesInAccount transactions = accountingService.findTransactionsByAccountAndJournalDate(fp, fromDate != null ? new DateNonConvertable(fromDate) : null, toDate != null ? new DateNonConvertable(toDate) : null);
            ExcelData beginCell = new ExcelData(accountingLocalizer.localize(PdfLocalizationName.beginningBalance), ExcelData.STRING, eCellSize, false, true, ExcelData.NO_BORDER, ExcelData.HEADER_RIGHT);
            String accountType = transactions != null && transactions.getAccountType() != null ? transactions.getAccountType() : "";
            if (!EXPENSES.equalsIgnoreCase(accountType) && !REVENUE.equalsIgnoreCase(accountType)) {
                list.add(new ExcelData[]{beginCell, null, null, null, null, null, null, new ExcelData(transactions.getTotalBeginningBalance(), ExcelData.NUMBER_FORMAT_0_00, eCellSize, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL)});
            }

            String number, journalName;
            BigDecimal beginning = transactions.getTotalBeginningBalance() != null ? transactions.getTotalBeginningBalance() : BigDecimal.ZERO;
            for (Transaction item : transactions.getTransactions()) {
                journalName = item.getJournalName();
                number = item.getNumber();
                if (Constants.INVOICE_TRANSACTION.equals(item.getTransactionType())) {
                    if (item.getKeyId() != null) {
                        number = item.getNumber();
                        journalName = item.getJournalName();
                    }
                } else if (GOODS_RECEIVED_TRANSACTION.equals(item.getTransactionType())) {
                    number = item.getNumber();
                    journalName = accountingLocalizer.localize(PdfLocalizationName.purchaseOrder) + ": " + item.getJournalName();
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
                    journalName = commonLocalizer.localize(PdfLocalizationName.manualEntry, "Manual Entry") + ": " + item.getNarration();
                } else if (EXPENSE_TRANSACTION.equals(item.getTransactionType())) {
                    number = item.getExpenseNumber();
                    String descripton = StringUtils.isNotEmpty(item.getDescription()) ? item.getDescription() : item.getExpenseTitle();
                    journalName = accountingLocalizer.localize(PdfLocalizationName.expenseClaims) + ": " + descripton;
                } else if (EXPENSEPAYMENT_TRANSACTION.equals(item.getTransactionType())) {
                    journalName = accountingLocalizer.localize(PdfLocalizationName.expensePaymentTransaction);
                } else if (FIXED_ASSET_TRANSACTION.equals(item.getTransactionType())) {
                    number = item.getFixedAssetNumber();
                    journalName = commonLocalizer.localize(PdfLocalizationName.fixedAsset) + ": " + item.getFixedAssetName();
                } else if (DISPOSAL_TRANSACTION.equals(item.getTransactionType())) {
                    number = item.getFixedAssetNumber();
                    journalName = commonLocalizer.localize(PdfLocalizationName.fixedAsset) + " Disposal: " + item.getFixedAssetName();
                } else if (CUSTOMER_SUPPLIER_PAYMENT_TRANSACTION.equals(item.getTransactionType())) {
                    journalName = item.getJournalName();
                } else if (BANK_CHECK_TRANSACTION.equals(item.getTransactionType())) {
                    number = item.getCheckNumber();
                    journalName = accountingLocalizer.localize(PdfLocalizationName.check);
                } else if (BANK_TRANSFER_TRANSACTION.equals(item.getTransactionType())) {
                    String name = "";
                    if (AccountingConstants.RECEIVE_MONEY.equals(item.getSpendReceiveMoneyType())) {
                        name = accountingLocalizer.localize(PdfLocalizationName.bankReceipts);
                    } else if (AccountingConstants.SPEND_MONEY.equals(item.getSpendReceiveMoneyType())) {
                        name = accountingLocalizer.localize(PdfLocalizationName.bankPayments);
                    } else if (AccountingConstants.CASH_RECEIPT.equals(item.getSpendReceiveMoneyType())) {
                        name = accountingLocalizer.localize(PdfLocalizationName.cashReceipt);
                    } else if (AccountingConstants.CASH_PAYMENT.equals(item.getSpendReceiveMoneyType())) {
                        name = accountingLocalizer.localize(PdfLocalizationName.cashPayment);
                    }
                    journalName = name + ": " + item.getSpendReceiveMoneyNarration();
                    number = item.getSpendReceiveMoneyNumber();
                } else if (BANK_MONEY_TRANSFER_TRANSACTION.equals(item.getTransactionType())) {
                    journalName = item.getJournalName();
                } else if (ADJUSTMENT_TRANSACTION.equals(item.getTransactionType())) {
                    journalName = accountingLocalizer.localize(PdfLocalizationName.stockAdjustments);
                    number = item.getStockAdjustmentNumber();
                } else if (STOCK_TRANSFER_TRANSACTION.equals(item.getTransactionType())) {
                    journalName = commonLocalizer.localize(PdfLocalizationName.stockTransfer);
                    number = item.getReference();
                } else if (RETAINED_EARNINGS_TRANSACTION.equals(item.getTransactionType())) {
                    journalName = item.getJournalName();
                }
                ExcelData dateDataCell = new ExcelData(item.getJournalDate() != null ? item.getJournalDate().getNonConvertedDate() : null, ExcelData.DATE, 0, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL_RIGHT);
                ExcelData journalNameDataCell = new ExcelData(journalName, ExcelData.STRING, 0, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL_RIGHT);
                ExcelData numberDataCell = new ExcelData(number != null ? number : "", ExcelData.STRING, 0, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL_RIGHT);
                ExcelData journalIdDataCell = new ExcelData(String.valueOf(item.getJournalId() != null ? item.getJournalId() : ""), ExcelData.STRING, 0, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL_RIGHT);
                BigDecimal debit = item.getTotalDebit();
                BigDecimal credit = item.getTotalCredit();
                ExcelData debitDataCell = new ExcelData(debit, ExcelData.NUMBER_FORMAT_0_00, dCellSize, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL);
                ExcelData creditDataCell = new ExcelData(credit, ExcelData.NUMBER_FORMAT_0_00, eCellSize, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL);
                String referenceString = Jsoup.parse(item.getReference() != null ? item.getReference() : "").text();
                ExcelData referenceDataCell = new ExcelData(referenceString, ExcelData.STRING, 0, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL_LEFT);
                BigDecimal totalDebit = item.getTotalDebit() != null ? item.getTotalDebit() : BigDecimal.ZERO;
                BigDecimal totalCredit = item.getTotalCredit() != null ? item.getTotalCredit() : BigDecimal.ZERO;

                if (Constants.ASSETS.equals(accountType) || Constants.EXPENSES.equals(accountType)) {
                    beginning = beginning.add(totalDebit.subtract(totalCredit));
                } else {
                    beginning = beginning.add(totalCredit.subtract(totalDebit));
                }
                ExcelData beginningDataCell = new ExcelData(beginning, ExcelData.NUMBER_FORMAT_0_00, eCellSize, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL);

                ExcelData[] cellBody = new ExcelData[]{
                        dateDataCell,
                        journalNameDataCell,
                        numberDataCell,
                        journalIdDataCell,
                        referenceDataCell,
                        debitDataCell,
                        creditDataCell,
                        beginningDataCell
                };
                list.add(cellBody);

            }


            BigDecimal debit = transactions.getTotalDebit();
            BigDecimal credit = transactions.getTotalCredit();
            BigDecimal balance = transactions.getTotalBalance();
            ExcelData[] cellFooter;
            ExcelData totalTotalData = new ExcelData(accountingLocalizer.localize(PdfLocalizationName.total), ExcelData.STRING, 0, false, true, ExcelData.NO_BORDER, ExcelData.HEADER_LEFT);
            totalTotalData.setBold(true);
            ExcelData emptyTotalData = new ExcelData("", ExcelData.STRING, 0, false, true, ExcelData.NO_BORDER, ExcelData.HEADER_LEFT);
            ExcelData creditTotalData = new ExcelData(credit, ExcelData.NUMBER_FORMAT_0_00, 0, false, true, ExcelData.NO_BORDER, ExcelData.HEADER_RIGHT);
            ExcelData debitTotalData = new ExcelData(debit, ExcelData.NUMBER_FORMAT_0_00, 0, false, true, ExcelData.NO_BORDER, ExcelData.HEADER_RIGHT);
            ExcelData balanceTotalData = new ExcelData(debit.subtract(credit), ExcelData.NUMBER_FORMAT_0_00, 0, false, true, ExcelData.NO_BORDER, ExcelData.HEADER_RIGHT);

            cellFooter = new ExcelData[]{totalTotalData, emptyTotalData, emptyTotalData, emptyTotalData, emptyTotalData, debitTotalData, creditTotalData, balanceTotalData};
            list.add(cellFooter);

            ExcelData[] cellFooter2;
            ExcelData balanceCellVal = new ExcelData(accountingLocalizer.localize(PdfLocalizationName.endingBalance), ExcelData.STRING, 0, false, true, ExcelData.NO_BORDER, ExcelData.HEADER_LEFT);
            balanceCellVal.setBold(true);
            ExcelData balanceTotalData2 = new ExcelData(balance, ExcelData.NUMBER_FORMAT_0_00, 0, false, true, ExcelData.NO_BORDER, ExcelData.HEADER_RIGHT);
            cellFooter2 = new ExcelData[]{balanceCellVal, emptyTotalData, emptyTotalData, emptyTotalData, emptyTotalData, emptyTotalData, emptyTotalData, balanceTotalData2};
            list.add(cellFooter2);
        } else {
            for (int i = 0; i < step; i++) {
                fp.setStart(i * limit);
                if (i == step - 1) {
                    fp.setForExportOnly(false);
                }
                TransactionsBetweenDatesInAccount transactions = accountingService.findTransactionsByAccountAndJournalDate(fp, fromDate != null ? new DateNonConvertable(fromDate) : null, toDate != null ? new DateNonConvertable(toDate) : null);
                if (transactions != null && transactions.getTransactions().size() != 0) {
                    BigDecimal beginning = transactions.getTotalBeginningBalance() != null ? transactions.getTotalBeginningBalance() : BigDecimal.ZERO;
                    try {
                        //header
                        if (i == 0) {
                            ExcelData beginCell = new ExcelData(accountingLocalizer.localize(PdfLocalizationName.beginningBalance), ExcelData.STRING, eCellSize, false, true, ExcelData.NO_BORDER, ExcelData.HEADER_RIGHT);
                            beginCell.setBold(true);
                            String accountType = transactions != null && transactions.getAccountType() != null ? transactions.getAccountType() : "";
                            if (!EXPENSES.equalsIgnoreCase(accountType) && !REVENUE.equalsIgnoreCase(accountType)) {
                                list.add(new ExcelData[]{beginCell, null, null, null, null, new ExcelData(transactions.getTotalBeginningBalance(), ExcelData.NUMBER_FORMAT_0_00, eCellSize, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL)});
                            }
                        }
                        //BodyCell
                        String accountType = transactions.getAccountType();
                        String number, journalName;
                        for (Transaction item : transactions.getTransactions()) {
                            journalName = item.getJournalName();
                            number = item.getNumber();
                            if (Constants.INVOICE_TRANSACTION.equals(item.getTransactionType())) {
                                if (item.getKeyId() != null) {
                                    number = item.getNumber();
                                    journalName = item.getJournalName();
                                }
                            } else if (GOODS_RECEIVED_TRANSACTION.equals(item.getTransactionType())) {
                                number = item.getNumber();
                                journalName = accountingLocalizer.localize(PdfLocalizationName.purchaseOrder) + ": " + item.getJournalName();
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
                                journalName = commonLocalizer.localize(PdfLocalizationName.manualEntry, "Manual Entry") + ": " + item.getNarration();
                            } else if (EXPENSE_TRANSACTION.equals(item.getTransactionType())) {
                                number = item.getExpenseNumber();
                                String descripton = StringUtils.isNotEmpty(item.getDescription()) ? item.getDescription() : item.getExpenseTitle();
                                journalName = accountingLocalizer.localize(PdfLocalizationName.expenseClaims) + ": " + descripton;
                            } else if (EXPENSEPAYMENT_TRANSACTION.equals(item.getTransactionType())) {
                                journalName = accountingLocalizer.localize(PdfLocalizationName.expensePaymentTransaction);
                            } else if (FIXED_ASSET_TRANSACTION.equals(item.getTransactionType())) {
                                number = item.getFixedAssetNumber();
                                journalName = commonLocalizer.localize(PdfLocalizationName.fixedAsset) + ": " + item.getFixedAssetName();
                            } else if (DISPOSAL_TRANSACTION.equals(item.getTransactionType())) {
                                number = item.getFixedAssetNumber();
                                journalName = commonLocalizer.localize(PdfLocalizationName.fixedAsset) + " Disposal: " + item.getFixedAssetName();
                            } else if (CUSTOMER_SUPPLIER_PAYMENT_TRANSACTION.equals(item.getTransactionType())) {
                                journalName = item.getJournalName();
                            } else if (BANK_CHECK_TRANSACTION.equals(item.getTransactionType())) {
                                number = item.getCheckNumber();
                                journalName = accountingLocalizer.localize(PdfLocalizationName.check);
                            } else if (BANK_TRANSFER_TRANSACTION.equals(item.getTransactionType())) {
                                String name = "";
                                if (AccountingConstants.RECEIVE_MONEY.equals(item.getSpendReceiveMoneyType())) {
                                    name = accountingLocalizer.localize(PdfLocalizationName.bankReceipts);
                                } else if (AccountingConstants.SPEND_MONEY.equals(item.getSpendReceiveMoneyType())) {
                                    name = accountingLocalizer.localize(PdfLocalizationName.bankPayments);
                                } else if (AccountingConstants.CASH_RECEIPT.equals(item.getSpendReceiveMoneyType())) {
                                    name = accountingLocalizer.localize(PdfLocalizationName.cashReceipt);
                                } else if (AccountingConstants.CASH_PAYMENT.equals(item.getSpendReceiveMoneyType())) {
                                    name = accountingLocalizer.localize(PdfLocalizationName.cashPayment);
                                }
                                journalName = name + ": " + item.getSpendReceiveMoneyNarration();
                                number = item.getSpendReceiveMoneyNumber();
                            } else if (BANK_MONEY_TRANSFER_TRANSACTION.equals(item.getTransactionType())) {
                                journalName = item.getJournalName();
                            } else if (ADJUSTMENT_TRANSACTION.equals(item.getTransactionType())) {
                                journalName = accountingLocalizer.localize(PdfLocalizationName.stockAdjustments);
                                number = item.getStockAdjustmentNumber();
                            } else if (STOCK_TRANSFER_TRANSACTION.equals(item.getTransactionType())) {
                                journalName = commonLocalizer.localize(PdfLocalizationName.stockTransfer);
                                number = item.getReference();
                            } else if (RETAINED_EARNINGS_TRANSACTION.equals(item.getTransactionType())) {
                                journalName = item.getJournalName();
                            }
                            ExcelData dateDataCell = new ExcelData(item.getJournalDate() != null ? item.getJournalDate().getNonConvertedDate() : null, ExcelData.DATE, 0, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL_RIGHT);
                            ExcelData journalNameDataCell = new ExcelData(journalName, ExcelData.STRING, 0, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL_RIGHT);
                            ExcelData numberDataCell = new ExcelData(number != null ? number : "", ExcelData.STRING, 0, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL_RIGHT);
                            ExcelData journalIdDataCell = new ExcelData(String.valueOf(item.getJournalId() != null ? item.getJournalId() : ""), ExcelData.STRING, 0, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL_RIGHT);
                            BigDecimal debit = item.getTotalDebit();
                            BigDecimal credit = item.getTotalCredit();
                            ExcelData debitDataCell = new ExcelData(debit, ExcelData.NUMBER_FORMAT_0_00, dCellSize, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL);
                            ExcelData creditDataCell = new ExcelData(credit, ExcelData.NUMBER_FORMAT_0_00, eCellSize, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL);
                            String referenceString = "";
                            if (MANUAL_TRANSACTION.equals(item.getTransactionType())) {
                                referenceString = Jsoup.parse(item.getDescription() != null ? item.getDescription() : "").text();
                            } else {
                                referenceString = Jsoup.parse(item.getReference() != null ? item.getReference() : "").text();
                            }
                            ExcelData referenceDataCell = new ExcelData(referenceString, ExcelData.STRING, 0, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL_LEFT);
                            BigDecimal totalDebit = item.getTotalDebit() != null ? item.getTotalDebit() : BigDecimal.ZERO;
                            BigDecimal totalCredit = item.getTotalCredit() != null ? item.getTotalCredit() : BigDecimal.ZERO;

                            if (Constants.ASSETS.equals(accountType) || Constants.EXPENSES.equals(accountType)) {
                                beginning = beginning.add(totalDebit.subtract(totalCredit));
                            } else {
                                beginning = beginning.add(totalCredit.subtract(totalDebit));
                            }
                            ExcelData beginningDataCell = new ExcelData(beginning, ExcelData.NUMBER_FORMAT_0_00, eCellSize, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL);

                            ExcelData[] cellBody = new ExcelData[]{
                                    dateDataCell,
                                    journalNameDataCell,
                                    numberDataCell,
                                    journalIdDataCell,
                                    referenceDataCell,
                                    debitDataCell,
                                    creditDataCell,
                                    beginningDataCell
                            };
                            list.add(cellBody);

                        }

                        //total
                        if (i == step - 1) {
                            BigDecimal debit = transactions.getTotalDebit();
                            BigDecimal credit = transactions.getTotalCredit();
                            BigDecimal balance = transactions.getTotalBalance();
                            ExcelData[] cellFooter;
                            ExcelData totalTotalData = new ExcelData(accountingLocalizer.localize(PdfLocalizationName.total), ExcelData.STRING, 0, false, true, ExcelData.NO_BORDER, ExcelData.HEADER_LEFT);
                            totalTotalData.setBold(true);
                            ExcelData emptyTotalData = new ExcelData("", ExcelData.STRING, 0, false, true, ExcelData.NO_BORDER, ExcelData.HEADER_LEFT);
                            ExcelData creditTotalData = new ExcelData(credit, ExcelData.NUMBER_FORMAT_0_00, 0, false, true, ExcelData.NO_BORDER, ExcelData.HEADER_RIGHT);
                            ExcelData debitTotalData = new ExcelData(debit, ExcelData.NUMBER_FORMAT_0_00, 0, false, true, ExcelData.NO_BORDER, ExcelData.HEADER_RIGHT);
                            ExcelData balanceTotalData = new ExcelData(debit.subtract(credit), ExcelData.NUMBER_FORMAT_0_00, 0, false, true, ExcelData.NO_BORDER, ExcelData.HEADER_RIGHT);

                            cellFooter = new ExcelData[]{
                                    totalTotalData,
                                    emptyTotalData,
                                    emptyTotalData,
                                    emptyTotalData,
                                    emptyTotalData,
                                    debitTotalData,
                                    creditTotalData,
                                    balanceTotalData
                            };
                            list.add(cellFooter);
                            ExcelData[] cellFooter2;
                            ExcelData balanceCellVal = new ExcelData(accountingLocalizer.localize(PdfLocalizationName.endingBalance), ExcelData.STRING, 0, false, true, ExcelData.NO_BORDER, ExcelData.HEADER_LEFT);
                            balanceCellVal.setBold(true);
                            ExcelData balanceTotalData2 = new ExcelData(balance, ExcelData.NUMBER_FORMAT_0_00, 0, false, true, ExcelData.NO_BORDER, ExcelData.HEADER_RIGHT);
                            if (!EXPENSES.equalsIgnoreCase(accountType) && !REVENUE.equalsIgnoreCase(accountType)) {
                                cellFooter2 = new ExcelData[]{
                                        balanceCellVal,
                                        emptyTotalData,
                                        emptyTotalData,
                                        emptyTotalData,
                                        emptyTotalData,
                                        emptyTotalData,
                                        emptyTotalData,
                                        balanceTotalData2
                                };
                            } else {
                                cellFooter2 = new ExcelData[]{
                                        emptyTotalData,
                                        emptyTotalData,
                                        emptyTotalData,
                                        emptyTotalData,
                                        emptyTotalData,
                                        emptyTotalData,
                                        emptyTotalData,
                                        emptyTotalData
                                };
                            }
                            list.add(cellFooter2);
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        log.error("Cannot generate Account Transactions Report List excel report, exception: " + ex);
                    }
                }
            }
        }

        // System.out.println((System.currentTimeMillis() - current1)/1000.0);
        HSSFWorkbook wb = new WorkBook(list).getWorkBook(filename, 0, 0, 0, list.size());
        // Set the columns to repeat from column 0 to 2 on the first sheet
        wb.setRepeatingRowsAndColumns(0, 0, 4, 0, 5);
        return wb;
    }

    private String getParentAndChildDepartmentTreeIDs(Integer departmentID) {
        StringBuilder teamIDs = new StringBuilder();
        if (departmentID != null) {
            teamIDs.append(departmentID);
            teamIDs.append(departmentService.getChildDepartmentIds(departmentID, true));
        }
        return teamIDs.toString();
    }

    @Override
    protected void setFileName(Object object) {
        ListingFilterParameter fp = (ListingFilterParameter) object;
        filename = fp.getName() + "_" + "Account Transactions Report List_" + dateFormat(userManager.getUser().getUserDate());
    }
}
