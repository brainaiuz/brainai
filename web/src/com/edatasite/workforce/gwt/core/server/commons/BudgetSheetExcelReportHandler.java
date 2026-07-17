package com.edatasite.workforce.gwt.core.server.commons;

import com.edatasite.shared.poiutils.WorkBook;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.accounting.EdsFinancialSettings;
import com.edatasite.workforce.gwt.accounting.client.rpc.*;
import com.edatasite.workforce.gwt.accounting.client.ui.AccountingConstants;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.accounting.AccountItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.server.db.UploadManager;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.localization.PdfLocalizationName;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.support.WfmResourceBundleMessageSource;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: DELL
 * Date: 04-Jun-2009
 * Time: 12:35:50
 * To change this template use File | Settings | File Templates.
 */
public class BudgetSheetExcelReportHandler extends BaseExcelHandler {

    @Autowired
    @Qualifier("pdfWfmMessageSource")
    private WfmResourceBundleMessageSource wfmMessageSource;

    private final int aCellSize = 26;
    private final int bCellSize = 13;

    DateFormat headerFormat = new SimpleDateFormat("MMM-yy");
    @Autowired
    AccountingService accountingService;
    @Autowired
    private UploadManager uploadManager;

    @Override
    protected void setFileName() {
        filename = "Budget Sheet";
    }

    @Override
    protected HSSFWorkbook getWorkBook(Object object) {

        ListingFilterParameter filterParametrs = (ListingFilterParameter) object;

        Date startDate = parseFilterParameterDate(filterParametrs.getStartDateNC());
        Date endDate = parseFilterParameterDate(filterParametrs.getEndDateNC());
        int columnCount = filterParametrs.getCaseID();


        ExpensesAndRevenue result = accountingService.getBudgetedAccounts(
                (startDate != null ? new DateNonConvertable(startDate) : null),
                (endDate != null ? new DateNonConvertable(endDate) : null), filterParametrs.getDepartmentId(), filterParametrs.isAscending());
        ExcelData[] headers;
        Map<String, List<BigDecimal>> totalProfits;
        Map<String, List<BigDecimal>> subtractTotalProfits;
        if (result != null) {
            EdsUser user = uploadManager.getUser();
            String shortDateFormat = user.getCompany().getCompanySettings().getShortDateFormat();
            SimpleDateFormat format = new SimpleDateFormat(shortDateFormat != null ? shortDateFormat : "MMM dd yyyy", Locale.ENGLISH);

            EdsFinancialSettings fs = financialSettingsManager.getFinancialSettings();
            String currencySymbol = fs.getCurrency().getSymbol();
            String currencyCode = fs.getCurrency().getName();
            currencySymbol = currencySymbol != null ? currencySymbol : "";

            String date = accountingLocalizer.localizeAccounting(PdfLocalizationName.from) + " "
                    + format.format(startDate) + " "
                    + accountingLocalizer.localizeAccounting(PdfLocalizationName.to) + " "
                    + format.format(endDate);

            List<ExcelData[]> list = new LinkedList<>();

            list.add(new ExcelData[]{
                    new ExcelData("", ExcelData.STRING, aCellSize, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL)
            });

            int lastColumnIndex = columnCount;
            ExcelData titleData = ExcelData.getReportNameData("Overall Budget", aCellSize, lastColumnIndex);

            ExcelData companyData = ExcelData.getReportNameChildData(user.getCompany().getName(), aCellSize, lastColumnIndex);

            ExcelData dateData = ExcelData.getReportNameChildData(date, aCellSize, lastColumnIndex);

            ExcelData currencyData = ExcelData.getReportNameChildData(accountingLocalizer.localizeAccounting(PdfLocalizationName.figuresIn) + " " + currencySymbol + "(" + currencyCode + ")", aCellSize, lastColumnIndex);

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

            ExcelData emptyData = new ExcelData("", ExcelData.STRING, aCellSize, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL);
            ExcelData[] cellEmptyHeader = new ExcelData[]{
                    emptyData
            };
            list.add(cellEmptyHeader);

            // HEADER
            headers = new ExcelData[columnCount + 2];
            headers[0] = new ExcelData(" ", ExcelData.STRING, aCellSize, false, true, ExcelData.NO_BORDER, ExcelData.HEADER3);

            for (int i = 1; i < columnCount - 1; i++) {
                headers[i] = new ExcelData(headerFormat.format(startDate), ExcelData.STRING, bCellSize, false, true, ExcelData.NO_BORDER, ExcelData.HEADER_RIGHT);
                startDate = addMonths(startDate, 1, 1);
            }
            columnCount = columnCount - 1;
            headers[columnCount] = new ExcelData("Total", ExcelData.STRING, bCellSize, false, true, ExcelData.NO_BORDER, ExcelData.HEADER_RIGHT);
            list.add(headers);

            totalProfits = new HashMap<>();
            subtractTotalProfits = new HashMap<>();
            for (int i = 1; i < columnCount; i++) {
                totalProfits.put((String) headers[i].getValue(), new LinkedList<>());
                subtractTotalProfits.put((String) headers[i].getValue(), new LinkedList<>());
            }

            setComparisonContentGroup(list, result.getRevenue(), columnCount, headers, totalProfits);
            setComparisonContentGroup(list, result.getSale(), columnCount, headers, totalProfits);
            setComparisonContentGroup(list, result.getDirectCosts(), columnCount, headers, subtractTotalProfits);
            list.add(cellEmptyHeader);
            drawProfit(wfmMessageSource.localizeAccounting(PdfLocalizationName.grossProfit), list, columnCount, headers, totalProfits, subtractTotalProfits);

            setComparisonContentGroup(list, result.getOtherIncome(), columnCount, headers, totalProfits);
            setComparisonContentGroup(list, result.getExpense(), columnCount, headers, subtractTotalProfits);
            setComparisonContentGroup(list, result.getDepreciation(), columnCount, headers, subtractTotalProfits);
            setComparisonContentGroup(list, result.getOverhead(), columnCount, headers, subtractTotalProfits);
            list.add(cellEmptyHeader);
            drawProfit(wfmMessageSource.localizeAccounting(PdfLocalizationName.netProfit), list, columnCount, headers, totalProfits, subtractTotalProfits);

            HSSFWorkbook wb = new WorkBook(list).getWorkBook(filename, 0, 0, 0, 8);
            // Set the columns to repeat from column 0 to 2 on the first sheet
            wb.setRepeatingRowsAndColumns(0, 0, lastColumnIndex, 0, 7);
            wb.getSheetAt(0).createFreezePane(1, 0);
            return wb;
        }

        return null;
    }

    private void setComparisonContentGroup(List list, AccountItemsByAccountType item, int columnCount, ExcelData[] headers, Map<String, List<BigDecimal>> totalProfits) {
        if (item != null) {
            AccountItemWithBudgetDate[] accItems = item.getAccountItems();
            if (accItems != null && accItems.length > 0) {
                Map<String, AccountItemWithBudgetDate> map1 = new HashMap<>(accItems.length);
                ArrayList<AccountItemWithBudgetDate> map2 = new ArrayList<>();

                Arrays.stream(accItems).forEach(acc -> map1.put(acc.getCode(), acc));
                Arrays.stream(accItems).forEach(acc -> {
                    if (StringUtils.isNotBlank(acc.getParentCode())) {
                        if (map1.get(acc.getParentCode()) == null) {
                            AccountItem accountCodeUnique = accountingService.getAccountCodeUnique(acc.getParentCode(), null);
                            AccountItemWithBudgetDate budgetDate = new AccountItemWithBudgetDate(accountCodeUnique.getId(), accountCodeUnique.getCode(), accountCodeUnique.getName());
                            budgetDate.getChilds().add(acc);
                            map1.put(acc.getParentCode(), budgetDate);
                            map2.add(budgetDate);
                        } else {
                            map1.get(acc.getParentCode()).getChilds().add(acc);
                        }
                    } else {
                        map2.add(acc);
                    }
                });

                Map<String, List<BigDecimal>> groupValueByColumn;
                groupValueByColumn = new HashMap<>();
                for (int i = 1; i < columnCount; i++) {
                    groupValueByColumn.put((String) headers[i].getValue(), new LinkedList<>());
                }
                list.add(new ExcelData[]{new ExcelData("", ExcelData.STRING, 40, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL)});

                String headerIntent = getTabString(1);
                ExcelData[] title = new ExcelData[]{new ExcelData(item.getGroupName(), ExcelData.STRING, 0, false, true, ExcelData.NO_BORDER, ExcelData.HEADER3)};
                list.add(title);

                map2.forEach(key -> addItem(list, key, columnCount, headers, 2, groupValueByColumn));

                drawGroupTotal(item.getGroupName(), list, columnCount, headers, totalProfits, groupValueByColumn);
            }
        }

    }

    private void addItem(List list, AccountItemWithBudgetDate budgetDate, int columnCount, ExcelData[] headers,
                         int level, Map<String, List<BigDecimal>> groupValueByColumn) {
        addItemToList(list, level, budgetDate, !budgetDate.getChilds().isEmpty(), columnCount, groupValueByColumn, headers);

        if (!budgetDate.getChilds().isEmpty()) {
            budgetDate.getChilds().forEach(child -> {
                addItem(list, child, columnCount, headers, level + 1, groupValueByColumn);
            });
        }
    }

    private void addItemToList(List list, int level, AccountItemWithBudgetDate budgetDate, boolean hasChilds, int columnCount, Map<String,
            List<BigDecimal>> groupValueByColumn, ExcelData[] headers) {
        if (budgetDate != null) {
            String intent = getTabString(level);
            ArrayList<BudgetInDate> bItems = budgetDate.getRowCells() != null ? budgetDate.getRowCells() : new ArrayList<>();
            ExcelData[] items = new ExcelData[bItems.size() + 2];
            items[0] = new ExcelData(intent + budgetDate.getName(), ExcelData.STRING, 40, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL);
            items[1] = new ExcelData(budgetDate.getCode(), ExcelData.STRING, 0, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL);
            if (hasChilds) {
                items[0].setBold(true);
                items[1].setBold(true);
            }

            ExcelData[] cellDatas;
            List<ExcelData[]> rowExcelData = new LinkedList<>();
            AccountItemWithBudgetDate row = budgetDate;
            if (row != null) {
                BigDecimal total = new BigDecimal("0.00");
                cellDatas = new ExcelData[columnCount + 1];
                cellDatas[0] = new ExcelData(row.getCode() != null
                        ? intent + row.getName() + " (" + row.getCode() + ")"
                        : row.getName(), ExcelData.STRING, 50, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL);
                if (hasChilds) {
                    cellDatas[0].setBold(true);
                }
                for (int j = 1; j < columnCount; j++) {
                    BigDecimal cellData = getCellAmount(row.getRowCells(), j, headers, groupValueByColumn);
                    total = total.add(cellData);
                    cellDatas[j] = new ExcelData(cellData, ExcelData.CURRENCY, 0, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL);
                    if (hasChilds) {
                        cellDatas[j].setBold(true);
                    }
                }
                cellDatas[columnCount] = new ExcelData(total, ExcelData.CURRENCY, 0, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL);
                if (hasChilds) {
                    cellDatas[columnCount].setBold(true);
                }
                rowExcelData.add(cellDatas);
            }

            list.addAll(rowExcelData);
        }
    }

    private String getTabString(int level) {
        StringBuilder intent = new StringBuilder();
        if (level > 0) {
            for (int i = 1; i <= level; i++) {
                intent.append("     ");
            }
        }
        return intent.toString();
    }

    private void drawGroup(List list, AccountItemsByAccountType accountType, int columnCount, ExcelData[] headers, Map<String, List<BigDecimal>> totalProfits) {
        if (accountType != null && accountType.getAccountItems() != null && accountType.getAccountItems().length > 0) {

            // Map for collecting total values by column
            Map<String, List<BigDecimal>> groupValueByColumn;
            groupValueByColumn = new HashMap<>();
            for (int i = 1; i < columnCount; i++) {
                groupValueByColumn.put((String) headers[i].getValue(), new LinkedList<>());
            }
            list.add(new ExcelData[]{new ExcelData("", ExcelData.STRING, aCellSize, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL)});

            ExcelData[] title = new ExcelData[]{new ExcelData(accountType.getGroupName(), ExcelData.STRING, 0, false, true, ExcelData.NO_BORDER, ExcelData.HEADER3)};
            list.add(title);

            ExcelData[] cellDatas;
            List<ExcelData[]> rowExcelData = new LinkedList<>();
            for (int i = 0; i < accountType.getAccountItems().length; i++) {
                AccountItemWithBudgetDate row = accountType.getAccountItems()[i];
                if (row != null) {
                    BigDecimal total = new BigDecimal("0.00");
                    cellDatas = new ExcelData[columnCount + 1];
                    cellDatas[0] = new ExcelData(row.getCode() != null ? row.getName() + " (" + row.getCode() + ")" : row.getName(), ExcelData.STRING, 0, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL);
                    for (int j = 1; j < columnCount; j++) {
                        BigDecimal cellData = getCellAmount(row.getRowCells(), j, headers, groupValueByColumn);
                        total = total.add(cellData);
                        cellDatas[j] = new ExcelData(cellData, ExcelData.CURRENCY, 0, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL);
                    }
                    cellDatas[columnCount] = new ExcelData(total, ExcelData.CURRENCY, 0, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL);
                    rowExcelData.add(cellDatas);
                }

            }
            list.addAll(rowExcelData);
            drawGroupTotal(accountType.getGroupName(), list, columnCount, headers, totalProfits, groupValueByColumn);
        }
    }

    private void drawGroupTotal(String groupName, List list, int columnCount, ExcelData[] headers, Map<String, List<BigDecimal>> totalProfits, Map<String, List<BigDecimal>> groupValueByColumn) {
        ExcelData[] totalCells = new ExcelData[columnCount + 1];
        totalCells[0] = new ExcelData("Total " + groupName, ExcelData.STRING, 0, false, true, ExcelData.NO_BORDER, ExcelData.HEADER3);
        int cols = 0;
        Double total;
        Double allTotal = 0d;
        List<BigDecimal> totals;
        for (int i = 1; i < columnCount; i++) {
            total = 0d;
            totals = groupValueByColumn.get(headers[i].getValue());
            for (BigDecimal total1 : totals) {
                total += total1.doubleValue();
            }
            allTotal += total;
            totalCells[++cols] = new ExcelData(total, ExcelData.CURRENCY, 0, false, true, ExcelData.NO_BORDER, ExcelData.HEADER3);
            totalProfits.get(headers[i].getValue()).addAll(totals);
        }
        totalCells[columnCount] = new ExcelData(allTotal, ExcelData.CURRENCY, 0, false, true, ExcelData.NO_BORDER, ExcelData.HEADER3);

        list.add(totalCells);
    }

    private BigDecimal getCellAmount(ArrayList<BudgetInDate> budgets, int column, ExcelData[] headers, Map<String, List<BigDecimal>> groupValueByColumn) {
        String columnDate = (String) headers[column].getValue();
        String budgetDate;
        if (budgets != null && budgets.size() > 0) {
            for (BudgetInDate budget : budgets) {
                budgetDate = budget != null ? headerFormat.format(budget.getDate()) : "";
                if (columnDate.equals(budgetDate)) {
                    groupValueByColumn.get(columnDate).add(budget.getValue());
                    return budget.getValue();
                }
            }
        }

        return AccountingConstants.ZERO;
    }

    private void drawProfit(String title, List list, int columnCount, ExcelData[] headers, Map<String, List<BigDecimal>> totalProfits, Map<String, List<BigDecimal>> subtractTotalProfits) {
        ExcelData[] profits = new ExcelData[columnCount + 1];
        profits[0] = new ExcelData(title, ExcelData.STRING, 50, false, true, ExcelData.NO_BORDER, ExcelData.HEADER3);
        int cols = 0;
        BigDecimal totalProfit = new BigDecimal("0.00");
        for (int i = 1; i < columnCount; i++) {
            List<BigDecimal> totals = totalProfits.get(headers[i].getValue());
            List<BigDecimal> subtractTotals = subtractTotalProfits.get(headers[i].getValue());
            BigDecimal total = new BigDecimal("0.00");
            for (BigDecimal total1 : totals) {
                total = total.add(total1);
            }
            BigDecimal subtractTotal = new BigDecimal("0.00");
            for (BigDecimal subtractTotal1 : subtractTotals) {
                subtractTotal = subtractTotal.add(subtractTotal1);
            }
            total = total.subtract(subtractTotal);
            totalProfit = totalProfit.add(total);
            profits[++cols] = new ExcelData(total, ExcelData.CURRENCY, 0, false, true, ExcelData.NO_BORDER, ExcelData.HEADER3);
        }
        profits[columnCount] = new ExcelData(totalProfit, ExcelData.CURRENCY, 0, false, true, ExcelData.NO_BORDER, ExcelData.HEADER3);
        list.add(profits);
    }

    private Date addMonths(Date date, int months, int beginningDay) {
        return new Date(date.getYear(), date.getMonth() + months, beginningDay <= date.getDate() ? beginningDay : 0);
    }

    public void setAccountingService(AccountingService accountingService) {
        this.accountingService = accountingService;
    }

    public void setWfmMessageSource(WfmResourceBundleMessageSource wfmMessageSource) {
        this.wfmMessageSource = wfmMessageSource;
    }

    public void setUploadManager(UploadManager uploadManager) {
        this.uploadManager = uploadManager;
    }
}
