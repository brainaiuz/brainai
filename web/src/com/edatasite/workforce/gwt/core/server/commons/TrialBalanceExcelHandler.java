package com.edatasite.workforce.gwt.core.server.commons;

import com.edatasite.shared.poiutils.WorkBook;
import com.edatasite.workforce.core.domain.EdsCurrency;
import com.edatasite.workforce.core.domain.EdsProperty;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.accounting.EdsFinancialSettings;
import com.edatasite.workforce.gwt.accounting.client.rpc.AccountingService;
import com.edatasite.workforce.gwt.accounting.client.rpc.TrialBalance;
import com.edatasite.workforce.gwt.accounting.client.rpc.TrialBalanceItem;
import com.edatasite.workforce.gwt.accounting.client.rpc.consignment.TrialBalanceFilter;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.accounting.AccountItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.db.CurrencyManager;
import com.edatasite.workforce.gwt.core.server.db.PropertManager;
import com.edatasite.workforce.gwt.core.server.db.UploadManager;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.localization.PdfLocalizationName;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: Atabek Boboyev
 * Date: 19.03.12
 * Time: 10:30
 * To change this template use File | Settings | File Templates.
 */
public class TrialBalanceExcelHandler extends BaseExcelHandler {
    @Autowired
    private AccountingService accountingService;
    @Autowired
    private UploadManager uploadManager;
    @Autowired
    private CurrencyManager currencyManager;
    @Autowired
    private PropertManager propertManager;
    private String sheetName;
    private List<ExcelData[]> list;

    private final int aCellSize = 18;
    private final int bCellSize = 36;
    private final int cCellSize = 18;
    private final int dCellSize = 14;
    private final int eCellSize = 16;
    private final int fCellSize = 16;

    @Override
    protected void setFileName() {
        filename = "Trial Balance_" + dateFormat(uploadManager.getUser().getUserDate());
    }

    @Override
    protected HSSFWorkbook getWorkBook(Object object) {
        EdsUser user = uploadManager.getUser();
        String shortDateFormat = user.getCompany().getCompanySettings().getShortDateFormat();
        ListingFilterParameter filterParametrs = (ListingFilterParameter) object;
        EdsProperty property = propertManager.findByCode(filterParametrs.getPropertyCode());
        sheetName = property != null ? property.getPlural() : commonLocalizer.localize(PdfLocalizationName.trialBalance);
        SimpleDateFormat format = new SimpleDateFormat(shortDateFormat != null ? shortDateFormat : "MMM dd yyyy", Locale.ENGLISH);
        Date startDate = parseFilterParameterDate(filterParametrs.getStartDateNC());
        Date endDate = parseFilterParameterDate(filterParametrs.getEndDateNC());
        Integer showAccounts = filterParametrs.getType();

        EdsFinancialSettings fs = financialSettingsManager.getFinancialSettings();
        Integer currencyId = filterParametrs.getCurrencyID();
        EdsCurrency currency = currencyManager.getCurrency(currencyId);
        String currencySymbol = currency.getSymbol();
        String currencyCode = currency.getName();
        currencySymbol = currencySymbol != null ? currencySymbol : "";
        TrialBalanceFilter tbf = new TrialBalanceFilter();
        tbf.setStartDate(startDate != null ? new DateNonConvertable(startDate) : null);
        tbf.setToDate((endDate != null ? new DateNonConvertable(endDate) : null));
        tbf.setSortField(filterParametrs.getSortField());
        tbf.setConsolidation(filterParametrs.isActualDue());
        tbf.setShowValues(showAccounts);
        tbf.setDepartmentID(filterParametrs.getDepartmentId());
        tbf.setSortDirection(filterParametrs.isAscending() ? "ASC" : "DESC");
        tbf.setCurrencyId(currencyId);
        tbf.setSummary(filterParametrs.isShowBudget());
        TrialBalance trialBalance = accountingService.getTrialBalance(tbf);

        String date;
        if (ServerUtils.getUserLocale().getLanguage().equals("uz")) {
            date = commonLocalizer.localize(PdfLocalizationName.from) + " "
                    + ServerUtils.convertToUzbDateFormat(format.format(startDate)) + " "
                    + commonLocalizer.localize(PdfLocalizationName.to) + " "
                    + ServerUtils.convertToUzbDateFormat(format.format(endDate));
        } else {
            date = commonLocalizer.localize(PdfLocalizationName.from) + " "
                    + format.format(startDate) + " "
                    + commonLocalizer.localize(PdfLocalizationName.to) + " "
                    + format.format(endDate);
        }
        int lastColumnIndex = 6;
        list = new LinkedList<>();
        String str = "";

        ExcelData probelExc = ExcelData.getReportNameChildDataWithOutBorderInStart(str, aCellSize, lastColumnIndex);

        ExcelData titleData = ExcelData.getReportNameData(sheetName, aCellSize, lastColumnIndex);

        ExcelData companyData = ExcelData.getReportNameChildData(user.getCompany().getName(), aCellSize, lastColumnIndex);

        ExcelData dateData = ExcelData.getReportNameChildData(date, aCellSize, lastColumnIndex);

        ExcelData currencyData = ExcelData.getReportNameChildData(commonLocalizer.localize(PdfLocalizationName.figuresIn) + " " + currencySymbol + "(" + currencyCode + ")", aCellSize, lastColumnIndex);
        list.add(new ExcelData[]{
                probelExc
        });

        list.add(new ExcelData[]{
                titleData
        });
        list.add(new ExcelData[]{
                companyData
        });
        list.add(new ExcelData[]{
                dateData
        });
        list.add(new ExcelData[]{
                currencyData
        });

        list.add(new ExcelData[]{
                probelExc
        });


        ExcelData[] cellHeader;

        ExcelData accountData = new ExcelData(commonLocalizer.localize(PdfLocalizationName.account), ExcelData.STRING, bCellSize, false, true, ExcelData.NO_BORDER, ExcelData.HEADER_LEFT);
        accountData.setBold(true);
        ExcelData code = new ExcelData(commonLocalizer.localize(PdfLocalizationName.code), ExcelData.STRING, cCellSize, false, true, ExcelData.NO_BORDER, ExcelData.HEADER_LEFT);
        code.setBold(true);
        ExcelData beginningBalanceData = new ExcelData(commonLocalizer.localize(PdfLocalizationName.beginningBalance), ExcelData.STRING, cCellSize, false, true, ExcelData.NO_BORDER, ExcelData.HEADER_RIGHT);
        ExcelData debitData = new ExcelData(commonLocalizer.localize(PdfLocalizationName.debit), ExcelData.STRING, dCellSize, false, true, ExcelData.NO_BORDER, ExcelData.HEADER_RIGHT);
        ExcelData creditData = new ExcelData(commonLocalizer.localize(PdfLocalizationName.credit), ExcelData.STRING, eCellSize, false, true, ExcelData.NO_BORDER, ExcelData.HEADER_RIGHT);
        ExcelData endingBalanceData = new ExcelData(commonLocalizer.localize(PdfLocalizationName.endingBalance), ExcelData.STRING, fCellSize, false, true, ExcelData.NO_BORDER, ExcelData.HEADER_RIGHT);
        cellHeader = new ExcelData[]{accountData, code, beginningBalanceData, debitData, creditData, endingBalanceData};
        list.add(cellHeader);

        Integer calculationScale = getCalculationScale(fs);

        setData(trialBalance.getAssets(), commonLocalizer.localize(PdfLocalizationName.assets), calculationScale, true);
        setData(trialBalance.getLiabilities(), commonLocalizer.localize(PdfLocalizationName.liabilities), calculationScale, false);
        setData(trialBalance.getEquity(), commonLocalizer.localize(PdfLocalizationName.equities), calculationScale, false);
        setData(trialBalance.getRevenue(), commonLocalizer.localize(PdfLocalizationName.revenue), calculationScale, false);
        setData(trialBalance.getExpenses(), commonLocalizer.localize(PdfLocalizationName.expenses), calculationScale, true);

        ExcelData[] cellTotal;
        ExcelData totalNameData = new ExcelData(commonLocalizer.localize(PdfLocalizationName.total), ExcelData.STRING, 0, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL);
        ExcelData emptyAccount = new ExcelData();
        totalNameData.setBold(true);
        cellTotal = new ExcelData[]{totalNameData, emptyAccount,
                createTotalCell(trialBalance.getTotalBeginningBalance(), calculationScale),
                createTotalCell(trialBalance.getTotalDebit(), calculationScale),
                createTotalCell(trialBalance.getTotalCredit(), calculationScale),
                createTotalCell(trialBalance.getTotalEndingBalance(), calculationScale)
        };
        list.add(cellTotal);

        HSSFWorkbook wb = new WorkBook(list).getWorkBook(filename, 0, 0, 0, 3);
        // Set the columns to repeat from column 0 to 2 on the first sheet
        wb.setRepeatingRowsAndColumns(0, 0, 4, 0, 5);

        return wb;
    }

    private ExcelData createTotalCell(BigDecimal value, Integer calculationScale) {
        ExcelData totalValue = new ExcelData(value != null ? createCell(value, calculationScale) : "", ExcelData.CURRENCY, 0, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL);
        totalValue.setBold(true);
        return totalValue;
    }

    private void setData(TrialBalanceItem[] items, String header, Integer calculationScale, boolean isDebitBalance) {
        TrialBalanceItem itemsTotal = new TrialBalanceItem();
        itemsTotal.setBeginningBalance(BigDecimal.ZERO);
        itemsTotal.setDebit(BigDecimal.ZERO);
        itemsTotal.setCredit(BigDecimal.ZERO);
        itemsTotal.setEndingBalance(BigDecimal.ZERO);
        if (items != null && items.length > 0) {
            list.add(drawBookMark(header));
            ExcelData[] cellBody;
            if (items != null && items.length > 0) {
                Map<String, TrialBalanceItem> map1 = new HashMap<>(items.length);
                ArrayList<TrialBalanceItem> map2 = new ArrayList<>();
                Arrays.stream(items).forEach(acc -> map1.put(acc.getCode(), acc));
                Arrays.stream(items).forEach(acc -> {
                    itemsTotal.setBeginningBalance(itemsTotal.getBeginningBalance().add(acc.getBeginningBalance()));
                    itemsTotal.setDebit(itemsTotal.getDebit().add(acc.getDebit()));
                    itemsTotal.setCredit(itemsTotal.getCredit().add(acc.getCredit()));
                    itemsTotal.setEndingBalance(itemsTotal.getEndingBalance().add(acc.getEndingBalance()));
                    if (acc.getParentCode() != null) {
                        if (map1.get(acc.getParentCode()) == null) {
                            AccountItem accountCodeUnique = new AccountItem(acc.getParentId(), acc.getParentCode(), acc.getParentName());
                            TrialBalanceItem balanceItem = new TrialBalanceItem(accountCodeUnique.getId(), accountCodeUnique.getCode(), accountCodeUnique.getName(),
                                                                                BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO);
                            balanceItem.getChilds().add(acc);
                            map1.put(acc.getParentCode(), balanceItem);
                            map2.add(balanceItem);
                        } else {
                            map1.get(acc.getParentCode()).getChilds().add(acc);
                        }
                    } else {
                        map2.add(acc);
                    }
                });

                for (TrialBalanceItem item : map2) {
                    TrialBalanceItem childTotal = new TrialBalanceItem();
                    childTotal.setBeginningBalance(BigDecimal.ZERO);
                    childTotal.setDebit(BigDecimal.ZERO);
                    childTotal.setCredit(BigDecimal.ZERO);
                    childTotal.setEndingBalance(BigDecimal.ZERO);
                    addItem(item, calculationScale, isDebitBalance, childTotal, 2);
                }
                drawTolatsRow(calculationScale, isDebitBalance, itemsTotal, 1, commonLocalizer.localize(PdfLocalizationName.total) + " " + header);
            }
            list.add(new ExcelData[]{
                    new ExcelData("", ExcelData.STRING, 0, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL)
            });
        }
    }

    private String getTabString(int level) {
        StringBuilder intent = new StringBuilder();
        if (level > 0) {
            for (int i = 1; i<=level; i++) {
                intent.append("     ");
            }
        }
        return intent.toString();
    }

    private void addItem(TrialBalanceItem item, Integer calculationScale, boolean isDebitBalance, TrialBalanceItem childTotal, int level) {
        addItemToList(calculationScale, isDebitBalance, item, level, !item.getChilds().isEmpty());
        if (!item.getChilds().isEmpty()) {
            item.getChilds().forEach(child->{
                addItem(child, calculationScale, isDebitBalance, childTotal, level + 1);
                if (!child.isCalculated()){
                    childTotal.setBeginningBalance(childTotal.getBeginningBalance().add(child.getBeginningBalance()));
                    childTotal.setDebit(childTotal.getDebit().add(child.getDebit()));
                    childTotal.setCredit(childTotal.getCredit().add(child.getCredit()));
                    childTotal.setEndingBalance(childTotal.getEndingBalance().add(child.getEndingBalance()));
                    child.setCalculated(true);
                }
            });
            if (!item.isCalculated()) {
                childTotal.setBeginningBalance(childTotal.getBeginningBalance().add(item.getBeginningBalance() != null ? item.getBeginningBalance() : BigDecimal.ZERO));
                childTotal.setDebit(childTotal.getDebit().add(item.getDebit() != null ? item.getDebit() : BigDecimal.ZERO));
                childTotal.setCredit(childTotal.getCredit().add(item.getCredit() != null ? item.getCredit() : BigDecimal.ZERO));
                childTotal.setEndingBalance(childTotal.getEndingBalance().add(item.getEndingBalance() != null ? item.getEndingBalance() : BigDecimal.ZERO));
                item.setCalculated(true);
            }
            boolean used = !item.getChilds().isEmpty() &&
                    item.getBeginningBalance() != null &&
                    item.getDebit() != null &&
                    item.getCredit() != null &&
                    item.getEndingBalance() != null;
            if (item.getName() != null && !used) {
                drawTolatsRow(calculationScale, isDebitBalance, childTotal, level, commonLocalizer.localize(PdfLocalizationName.total) + " " + item.getName());
            } else {
                TrialBalanceItem accountAndChildTotal = new TrialBalanceItem();
                accountAndChildTotal.setBeginningBalance(item.getBeginningBalance() != null ? item.getBeginningBalance() : BigDecimal.ZERO);
                accountAndChildTotal.setDebit(item.getDebit() != null ? item.getDebit() : BigDecimal.ZERO);
                accountAndChildTotal.setCredit(item.getCredit() != null ? item.getCredit() : BigDecimal.ZERO);
                accountAndChildTotal.setEndingBalance(item.getEndingBalance() != null ? item.getEndingBalance() : BigDecimal.ZERO);

                Set<TrialBalanceItem> visited = new HashSet<>();


                // Calculate totals for all nested children
                calculatTotal(accountAndChildTotal,item,visited);


                drawTolatsRow(calculationScale, isDebitBalance, accountAndChildTotal, level, commonLocalizer.localize(PdfLocalizationName.total) + " " + item.getName());
            }
        }
    }

    // Recursive method to calculate totals for all nested children
    private void calculatTotal(TrialBalanceItem accountAndChildTotal, TrialBalanceItem item,Set<TrialBalanceItem> visited) {

        if (visited.contains(item)) {
            return;
        }
        visited.add(item);

        for (TrialBalanceItem child : item.getChilds()) {
            // Recursively calculate totals for sub-children
            if (!child.getChilds().isEmpty()) {
                calculatTotal(accountAndChildTotal, child,visited);
            }
            accountAndChildTotal.setBeginningBalance(accountAndChildTotal.getBeginningBalance().add(child.getBeginningBalance() != null
                    ? child.getBeginningBalance()
                    : BigDecimal.ZERO));
            accountAndChildTotal.setDebit(accountAndChildTotal.getDebit().add(child.getDebit() != null
                    ? child.getDebit()
                    : BigDecimal.ZERO));
            accountAndChildTotal.setCredit(accountAndChildTotal.getCredit().add(child.getCredit() != null
                    ? child.getCredit()
                    : BigDecimal.ZERO));
            accountAndChildTotal.setEndingBalance(accountAndChildTotal.getEndingBalance().add(child.getEndingBalance() != null
                    ? child.getEndingBalance()
                    : BigDecimal.ZERO));
        }
    }
    private void addItemToList(Integer calculationScale, boolean isDebitBalance, TrialBalanceItem item, int level, boolean hasChilds) {
        String intent = getTabString(level);

        ExcelData[] cellBody;
        System.out.println("Print Is Debit Balance:");
        System.out.println(isDebitBalance);
        System.out.println("Print Beginning Balance:");
        System.out.println(item.getBeginningBalance());
        System.out.println("Print Ending Balance:");
        System.out.println(item.getEndingBalance());

        BigDecimal begBalanceVal = isDebitBalance
                                   ? item.getBeginningBalance()
                                   : item.getBeginningBalance() == null
                                     ? BigDecimal.ZERO
                                     : item.getBeginningBalance().multiply(new BigDecimal(-1));
        BigDecimal endBalanceVal = isDebitBalance
                                   ? item.getEndingBalance()
                                   : item.getEndingBalance() == null
                                     ? BigDecimal.ZERO
                                     : item.getEndingBalance().multiply(new BigDecimal(-1));
        ExcelData cellName = new ExcelData(intent + item.getName(), ExcelData.STRING, bCellSize, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL);
        ExcelData cellCode = new ExcelData(item.getCode(), ExcelData.STRING, bCellSize, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL);
        if (hasChilds) {
            cellName.setBold(true);
            cellCode.setBold(true);
        }
        ExcelData cellBeginningBalance = new ExcelData(createCell(begBalanceVal, calculationScale), ExcelData.CURRENCY, cCellSize, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL);
        ExcelData cellDebit = new ExcelData(createCell(item.getDebit(), calculationScale), ExcelData.CURRENCY, dCellSize, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL);
        ExcelData cellCredit = new ExcelData(createCell(item.getCredit(), calculationScale), ExcelData.CURRENCY, eCellSize, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL);
        ExcelData cellEndingBalance = new ExcelData(createCell(endBalanceVal, calculationScale), ExcelData.CURRENCY, fCellSize, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL);
        cellBody = new ExcelData[]{cellName, cellCode, cellBeginningBalance, cellDebit, cellCredit, cellEndingBalance};
        list.add(cellBody);
    }

    private void drawTolatsRow(Integer calculationScale, boolean isDebitBalance, TrialBalanceItem item, int level, String groupName) {
        String intent = getTabString(level);

        ExcelData[] cellBody;
        BigDecimal begBalanceVal = isDebitBalance
                                   ? item.getBeginningBalance()
                                   : item.getBeginningBalance() == null
                                     ? BigDecimal.ZERO
                                     : item.getBeginningBalance().multiply(new BigDecimal(-1));
        BigDecimal endBalanceVal = isDebitBalance
                                   ? item.getEndingBalance()
                                   : item.getEndingBalance() == null
                                     ? BigDecimal.ZERO
                                     : item.getEndingBalance().multiply(new BigDecimal(-1));
        ExcelData cellName = new ExcelData(intent + groupName, ExcelData.STRING, bCellSize, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL);
        ExcelData cellCode = new ExcelData("", ExcelData.STRING, bCellSize, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL);
        ExcelData cellBeginningBalance = new ExcelData(createCell(begBalanceVal, calculationScale), ExcelData.CURRENCY, cCellSize, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL);
        ExcelData cellDebit = new ExcelData(createCell(item.getDebit(), calculationScale), ExcelData.CURRENCY, dCellSize, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL);
        ExcelData cellCredit = new ExcelData(createCell(item.getCredit(), calculationScale), ExcelData.CURRENCY, eCellSize, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL);
        ExcelData cellEndingBalance = new ExcelData(createCell(endBalanceVal, calculationScale), ExcelData.CURRENCY, fCellSize, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL);
        cellName.setBold(true);
        cellCode.setBold(true);
        cellBeginningBalance.setBold(true);
        cellDebit.setBold(true);
        cellCredit.setBold(true);
        cellEndingBalance.setBold(true);
        cellBody = new ExcelData[]{cellName, cellCode, cellBeginningBalance, cellDebit, cellCredit, cellEndingBalance};
        list.add(cellBody);
    }

    private ExcelData[] drawBookMark(String name) {
        ExcelData headerData = new ExcelData(name != null ? name : "", ExcelData.STRING, aCellSize, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL);
        headerData.setBold(true);
        ExcelData emptyData = new ExcelData("", ExcelData.STRING, 0, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL);
        return new ExcelData[]{
                headerData, emptyData, emptyData, emptyData, emptyData
        };
    }

    private BigDecimal createCell(BigDecimal value, Integer calculationScale) {
        return (value != null ? value.setScale(calculationScale, BigDecimal.ROUND_HALF_UP) : BigDecimal.ZERO);
    }

}
