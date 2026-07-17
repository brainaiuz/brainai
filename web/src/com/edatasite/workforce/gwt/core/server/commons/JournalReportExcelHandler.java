package com.edatasite.workforce.gwt.core.server.commons;

import com.edatasite.shared.poiutils.WorkBook;
import com.edatasite.workforce.core.domain.EdsProperty;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.accounting.EdsFinancialSettings;
import com.edatasite.workforce.gwt.accounting.client.rpc.AccountingService;
import com.edatasite.workforce.gwt.accounting.client.rpc.ListingResult;
import com.edatasite.workforce.gwt.accounting.client.rpc.Transaction;
import com.edatasite.workforce.gwt.accounting.client.rpc.TransactionItem;
import com.edatasite.workforce.gwt.accounting.client.ui.AccountingConstants;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.db.PropertManager;
import com.edatasite.workforce.gwt.core.server.db.TransactionManager;
import com.edatasite.workforce.gwt.core.server.db.UploadManager;
import com.edatasite.workforce.gwt.core.server.db.UserManager;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.localization.PdfLocalizationName;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.Locale;

/**
 * Created by IntelliJ IDEA.
 * User: Atabek Boboyev
 * Date: 15.03.12
 * Time: 13:59
 * To change this template use File | Settings | File Templates.
 */
public class JournalReportExcelHandler extends BaseExcelHandler implements AccountingConstants {
    private static final Logger log = LoggerFactory.getLogger(LocationListExcelHandler.class);

    @Autowired
    private AccountingService accountingService;
    @Autowired
    private UserManager userManager;
    @Autowired
    private UploadManager uploadManager;
    @Autowired
    private PropertManager propertManager;
    private String sheetName;
    SimpleDateFormat format3 = new SimpleDateFormat("dd MMM yyyy", Locale.ENGLISH);

    @Autowired
    private TransactionManager transactionManager;

    private final int aCellSize = 79;
    private final int bCellSize = 20;
    private final int cCellSize = 20;

    @Override
    protected void setFileName() {
        filename = userManager.getUser().getCompany().getName() + "_Journal Report_" + dateFormat(uploadManager.getUser().getUserDate());
    }

    @Override
    protected HSSFWorkbook getWorkBook(Object object) {
        EdsUser user = userManager.getUser();
        String shortDateFormat = user.getCompany().getCompanySettings().getShortDateFormat();
        SimpleDateFormat format = new SimpleDateFormat(shortDateFormat != null
                                                       ? shortDateFormat
                                                       : "MMM dd yyyy", Locale.ENGLISH);
        ListingFilterParameter filterParametrs = (ListingFilterParameter) object;
        EdsProperty property = propertManager.findByCode(filterParametrs.getPropertyCode());
        Date startDate = parseFilterParameterDate(filterParametrs.getStartDateNC());
        Date endDate = parseFilterParameterDate(filterParametrs.getEndDateNC());
        filterParametrs.setFromExcelPDF(true);
        filterParametrs.setStartDate(startDate);
        filterParametrs.setEndDate(endDate);
        String orderby = filterParametrs.getSortField();

        EdsFinancialSettings fs = financialSettingsManager.getFinancialSettings();
        Integer calculationScale = getCalculationScale(fs);
        String currencySymbol = fs.getCurrency().getSymbol();
        String currencyCode = fs.getCurrency().getName();
        currencySymbol = currencySymbol != null ? currencySymbol : "";

        String debit = accountingLocalizer.localize(PdfLocalizationName.debit);
        String credit = accountingLocalizer.localize(PdfLocalizationName.credit);
        String from = commonLocalizer.localize(PdfLocalizationName.from);
        String to = commonLocalizer.localize(PdfLocalizationName.to);
        String journalReport = property != null ? property.getPlural() : commonLocalizer.localize(PdfLocalizationName.journalReport);
        String figureIn = accountingLocalizer.localize(PdfLocalizationName.figuresIn);
        String account = accountingLocalizer.localize(PdfLocalizationName.account);
        String department = commonLocalizer.localize(PdfLocalizationName.department);

        LinkedList<ExcelData[]> list = new LinkedList<>();
        filterParametrs.setLimit(5000);
        int rowCount = 0;
        int transactionItemsCount = 0;
        int limit = 15000;
        filterParametrs.setStart(0 * 5000);
        ListingResult<Transaction> transactions = accountingService.getJournalReportWithPaging(new DateNonConvertable(startDate), new DateNonConvertable(endDate), orderby, filterParametrs.getJournalID(), filterParametrs);

        if (transactions.getList() != null) {
            ExcelData[] cellDatas;
            try {
                //header
                String startDateValue = ServerUtils.getUserLocale().getLanguage().equals("uz") ? ServerUtils.convertToUzbDateFormat(format.format(startDate)) : format.format(startDate);
                String endDateValue = ServerUtils.getUserLocale().getLanguage().equals("uz") ? ServerUtils.convertToUzbDateFormat(format.format(endDate)) : format.format(endDate);
                String date = from + " " + startDateValue + " - " + to + " " + endDateValue;

                ExcelData emptyData = new ExcelData("", ExcelData.STRING, 0, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL);
                ExcelData[] cellEmptyHeader = new ExcelData[]{
                        emptyData
                };
                if (0 == 0) {
                    list.add(cellEmptyHeader);
                    rowCount++;
                    int lastColumnIndex = 3;
                    ExcelData titleData = ExcelData.getReportNameData(journalReport, aCellSize, lastColumnIndex);
                    ExcelData companyData = ExcelData.getReportNameChildData(user.getCompany().getName(), aCellSize, lastColumnIndex);
                    ExcelData dateData = ExcelData.getReportNameChildData(date, aCellSize, lastColumnIndex);
                    ExcelData currencyData = ExcelData.getReportNameChildData(figureIn + " " + currencySymbol + "(" + currencyCode + ")", aCellSize, lastColumnIndex);

                    list.add(new ExcelData[]{
                            titleData
                    });
                    rowCount++;
                    list.add(new ExcelData[]{
                            companyData
                    });
                    rowCount++;
                    list.add(new ExcelData[]{
                            dateData
                    });
                    rowCount++;
                    list.add(new ExcelData[]{
                            currencyData
                    });
                    rowCount++;
                    list.add(cellEmptyHeader);
                    rowCount++;
                }

                for (Transaction transaction : transactions.getList()) {
                    String id = transaction.getJournalId() != null
                            ? "ID " + transaction.getJournalId()
                            : "";
                    String type = " " + transaction.getJournalName();
                    String postedDate = transaction.getPostedDate() != null
                            ? ServerUtils.getUserLocale().getLanguage().equals("uz") ? ServerUtils.convertToUzbDateFormat(format3.format(transaction.getPostedDate().getNonConvertedDate())) : format3.format(transaction.getPostedDate().getNonConvertedDate())
                            : "";
                    String poster = accountingLocalizer.localizeWithParamAccounting(PdfLocalizationName.postedByOn, transaction.getPostedBy(), postedDate);
                    String journalDate = transaction.getJournalDate() != null
                            ? ServerUtils.getUserLocale().getLanguage().equals("uz") ? ServerUtils.convertToUzbDateFormat(format3.format(transaction.getJournalDate().getNonConvertedDate())) : format3.format(transaction.getJournalDate().getNonConvertedDate())
                            : "";
                    String tableName = id + type + poster;

                    ExcelData title1 = new ExcelData(tableName, ExcelData.STRING, aCellSize, false, true, ExcelData.NO_BORDER, ExcelData.HEADER_LEFT);
                    ExcelData title2 = new ExcelData(journalDate, ExcelData.STRING, cCellSize, false, true, ExcelData.NO_BORDER, ExcelData.HEADER_RIGHT);
                    ExcelData[] headers = new ExcelData[]{
                            title1, emptyData, title2
                    };
                    list.add(headers);
                    rowCount++;
                    ExcelData[] headersTitle = new ExcelData[]{
                            new ExcelData(account, ExcelData.STRING, aCellSize, false, true, ExcelData.NO_BORDER, ExcelData.HEADER_LEFT),
                            new ExcelData(department, ExcelData.STRING, bCellSize, false, true, ExcelData.NO_BORDER, ExcelData.HEADER_RIGHT),
                            new ExcelData(debit, ExcelData.STRING, bCellSize, false, true, ExcelData.NO_BORDER, ExcelData.HEADER_RIGHT),
                            new ExcelData(credit, ExcelData.STRING, cCellSize, false, true, ExcelData.NO_BORDER, ExcelData.HEADER_RIGHT)
                    };
                    list.add(headersTitle);
                    rowCount++;
                    transactionItemsCount = transaction.getTransactionItems().length;
                    if (rowCount + transactionItemsCount + 3 > limit) {
                        list.remove(rowCount - 1);
                        list.remove(rowCount - 2);
                        rowCount = limit + 1;
                        break;
                    }
                    for (TransactionItem transactionItem : transaction.getTransactionItems()) {
                        String name = transactionItem.getAccountName() + "(" + transactionItem.getAccountCode() + ")";
                        ExcelData debitCellData, creditCellData;
                        if (transactionItem.getDebit() != null) {
                            debitCellData = new ExcelData(transactionItem.getDebit().setScale(calculationScale, BigDecimal.ROUND_HALF_UP),
                                                          ExcelData.CURRENCY, bCellSize, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL_RIGHT);
                        } else {
                            debitCellData = new ExcelData("", ExcelData.STRING, aCellSize, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL_RIGHT);
                        }
                        if (transactionItem.getCredit() != null) {
                            creditCellData = new ExcelData(transactionItem.getCredit().setScale(calculationScale, BigDecimal.ROUND_HALF_UP),
                                                           ExcelData.CURRENCY, cCellSize, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL_RIGHT);
                        } else {
                            creditCellData = new ExcelData("", ExcelData.STRING, aCellSize, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL_RIGHT);
                        }
                        cellDatas = new ExcelData[]{new ExcelData(name != null
                                                                  ? name
                                                                  : "", ExcelData.STRING, aCellSize, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL_LEFT),
                                                    new ExcelData(transactionItem.getDepartment() != null
                                                                  ? transactionItem.getDepartment()
                                                                  : "", ExcelData.STRING, aCellSize, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL_LEFT),
                                                    debitCellData, creditCellData};
                        list.add(cellDatas);
                        rowCount++;
                    }

                    BigDecimal total_debit = transaction.getTotalDebit() != null
                                             ? transaction.getTotalDebit().setScale(calculationScale, BigDecimal.ROUND_HALF_UP)
                                             : ZERO;
                    BigDecimal total_credit = transaction.getTotalCredit() != null
                                              ? transaction.getTotalCredit().setScale(calculationScale, BigDecimal.ROUND_HALF_UP)
                                              : ZERO;

                    cellDatas = new ExcelData[]{
                            createTotalCellData("", ExcelData.STRING, aCellSize, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL, false),
                            createTotalCellData("", ExcelData.STRING, bCellSize, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL, false),
                            createTotalCellData(total_debit != null
                                                ? total_debit
                                                : "", ExcelData.CURRENCY, bCellSize, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL, true),
                            createTotalCellData(total_credit != null
                                                ? total_credit
                                                : "", ExcelData.CURRENCY, cCellSize, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL, true)
                    };
                    list.add(cellDatas);
                    rowCount++;
                    list.add(cellEmptyHeader);
                    rowCount++;
                    list.add(cellEmptyHeader);
                    rowCount++;
                }

            } catch (Exception ex) {
                ex.printStackTrace();
                log.error("Cannot generate Journal Report List excel report, exception: " + ex);
            }
        }
        HSSFWorkbook wb = new WorkBook(list).getWorkBook(filename, 0, 0, 0, 3);
        // Set the columns to repeat from column 0 to 2 on the first sheet
        wb.setRepeatingRowsAndColumns(0, 0, 2, 0, 4);

        return wb;
    }

    private ExcelData createTotalCellData(Object value, int dataType, int cellSize,
                                          boolean autSize, boolean wrapped,
                                          int borderPosiontion, int dataPosiontionInPage, boolean bold) {
        ExcelData excelData = new ExcelData(value, dataType, cellSize, autSize, wrapped, borderPosiontion, dataPosiontionInPage);
        if (bold) {
            excelData.setBold(true);
        }
        excelData.setHorizontalAlignment(HSSFCellStyle.ALIGN_RIGHT);
        return excelData;
    }

}
