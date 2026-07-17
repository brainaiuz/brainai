package com.edatasite.workforce.gwt.core.server.commons;

import com.edatasite.shared.poiutils.WorkBook;
import com.edatasite.workforce.core.domain.EdsProperty;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.accounting.EdsFinancialSettings;
import com.edatasite.workforce.gwt.accounting.client.rpc.*;
import com.edatasite.workforce.gwt.accounting.client.rpc.enums.CompareCategoryEnum;
import com.edatasite.workforce.gwt.accounting.client.rpc.enums.CompareWithEnum;
import com.edatasite.workforce.gwt.accounting.client.ui.AccountingConstants;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.FromToDate;
import com.edatasite.workforce.gwt.core.client.rpc.accounting.AccountItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.app.Utils;
import com.edatasite.workforce.gwt.core.server.db.PropertManager;
import com.edatasite.workforce.gwt.core.server.db.UploadManager;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.localization.PdfLocalizationName;
import com.google.gwt.user.server.rpc.security.ServerSecurityContext;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFPalette;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: Atabek Boboyev
 * Date: 17.03.12
 * Time: 14:55
 * To change this template use File | Settings | File Templates.
 */
public class ProfitLostViewExcelHandler extends BaseExcelHandler implements AccountingConstants {

    @Autowired
    private AccountingService accountingService;
    @Autowired
    private PropertManager propertManager;
    @Autowired
    private UploadManager uploadManager;
    private List<ExcelData[]> list;

    private final int aCellSize = 18;
    private final int bCellSize = 36;
    private final int cCellSize = 18;
    private final int dCellSize = 14;
    private final int eCellSize = 16;
    private final int fCellSize = 16;
    private Integer tableColsCount;
    private String sheetName;
    private final ArrayList<String> shownObjects = new ArrayList<>();

    @Override
    protected void setFileName() {
        filename = "Profit&Loss_" + dateFormat(uploadManager.getUser().getUserDate());
    }

    @Override
    protected HSSFWorkbook getWorkBook(Object object) {

        ListingFilterParameter filterParametrs = (ListingFilterParameter) object;
        EdsProperty property = propertManager.findByCode(filterParametrs.getPropertyCode());
        sheetName = property != null ? property.getPlural() : commonLocalizer.localize(PdfLocalizationName.profitAndLoss);
        Date startDate = parseFilterParameterDate(filterParametrs.getStartDateNC());
        Date endDate = parseFilterParameterDate(filterParametrs.getEndDateNC());
        Integer currencyId = filterParametrs.getCurrencyID();
        FromToDate main = new FromToDate(new DateNonConvertable(startDate), new DateNonConvertable(endDate));
        FromToDate[] compareDates = getCompareDates(startDate, Integer.valueOf(filterParametrs.getDataType()));
        if (filterParametrs.getShownObjects() != null) {
            shownObjects.clear();
            shownObjects.addAll(Arrays.asList(filterParametrs.getShownObjects().split(",")));
        }

        boolean showBudget = filterParametrs.isShowBudget();
        boolean showYTD = filterParametrs.isShowYTD();
        String sortDirection = 1 == filterParametrs.getSortDir() ? Constants.ASC_STR : Constants.DESC_STR;
        int comparisonYears = compareDates.length;
        tableColsCount = compareDates.length + 1;
        PnLFilter filter = new PnLFilter();
        filter.setMain(main);
        filter.setCompareTo(compareDates);
        filter.setShowBudget(showBudget);
        filter.setShowYearToDate(showYTD);
        filter.setSortField(filterParametrs.getSortField() != null ? filterParametrs.getSortField() : Constants.ACC_NAME);
        filter.setSortDirection(sortDirection);
        filter.setCosolidation(filterParametrs.isActualDue());
        filter.setDepartmentID(filterParametrs.getDepartmentId());
        filter.setProjectID(filterParametrs.getProjectId());
        filter.setCurrencyId(currencyId);
        BudgetManagerItems profLoss = accountingService.getProfitAndLoss(filter);
        SimpleDateFormat format = new SimpleDateFormat("MMM d yyyy", Locale.ENGLISH);
        list = new LinkedList<>();
        String date;
        if (comparisonYears > 0) {
            if (ServerUtils.getUserLocale().getLanguage().equals("uz")) {
                date = ServerUtils.convertToUzbDateFormat(format.format(endDate)) + " Xolatiga ko'ra";
            } else {
                date = accountingLocalizer.localizeAccounting(PdfLocalizationName.asOF) + " " + ServerUtils.convertToUzbDateFormat(format.format(endDate));
            }
//            date = accountingLocalizer.localizeAccounting(PdfLocalizationName.asOF) + " " + format.format(endDate);
        } else {
            if (ServerUtils.getUserLocale().getLanguage().equals("uz")) {
                date = ServerUtils.convertToUzbDateFormat(format.format(startDate)) + " - " + ServerUtils.convertToUzbDateFormat(format.format(endDate));
            } else {
                date = format.format(startDate) + " - " + format.format(endDate);
            }
//            date = format.format(startDate) + " - " + format.format(endDate);
        }

        int lastColumnIndex;

        EdsFinancialSettings fs = financialSettingsManager.getFinancialSettings();
        Integer calculationScale = getCalculationScale(fs);

        if (profLoss != null) {
            ExcelData[] headers;
            String GROSS_PROFIT = commonLocalizer.localize(PdfLocalizationName.grossProfit);
            String NET_PROFIT = commonLocalizer.localize(PdfLocalizationName.netProfit);
            if (showBudget) {
                if (showYTD) {
                    ExcelData accountCode = new ExcelData(accountingLocalizer.localize(PdfLocalizationName.code), ExcelData.STRING, aCellSize, false, true, ExcelData.NO_BORDER, ExcelData.HEADER);
                    ExcelData accountData = new ExcelData(accountingLocalizer.localize(PdfLocalizationName.account), ExcelData.STRING, aCellSize, false, true, ExcelData.NO_BORDER, ExcelData.HEADER);
                    ExcelData actualData = new ExcelData(accountingLocalizer.localize(PdfLocalizationName.actual), ExcelData.STRING, bCellSize, false, true, ExcelData.NO_BORDER, ExcelData.HEADER_RIGHT);
                    ExcelData budgetData = new ExcelData(accountingLocalizer.localize(PdfLocalizationName.budget), ExcelData.STRING, cCellSize, false, true, ExcelData.NO_BORDER, ExcelData.HEADER_RIGHT);
                    ExcelData varianceData = new ExcelData(commonLocalizer.localize(PdfLocalizationName.variance), ExcelData.STRING, dCellSize, false, true, ExcelData.NO_BORDER, ExcelData.HEADER_RIGHT);
                    ExcelData YTDActualData = new ExcelData(accountingLocalizer.localize(PdfLocalizationName.ytdActual), ExcelData.STRING, eCellSize, false, true, ExcelData.NO_BORDER, ExcelData.HEADER_RIGHT);
                    ExcelData YTDBudgetData = new ExcelData(accountingLocalizer.localize(PdfLocalizationName.ytdBudget), ExcelData.STRING, fCellSize, false, true, ExcelData.NO_BORDER, ExcelData.HEADER_RIGHT);
                    ExcelData YTDVarianceData = new ExcelData(accountingLocalizer.localize(PdfLocalizationName.ytdVariance), ExcelData.STRING, aCellSize, false, true, ExcelData.NO_BORDER, ExcelData.HEADER_RIGHT);
                    headers = new ExcelData[]{
                            accountCode, accountData, actualData, budgetData, varianceData, YTDActualData, YTDBudgetData, YTDVarianceData
                    };
                    lastColumnIndex = applyTitle(date, headers.length, fs);
                    list.add(headers);
                } else {
                    ExcelData accountCode = new ExcelData(accountingLocalizer.localize(PdfLocalizationName.code), ExcelData.STRING, aCellSize, false, true, ExcelData.NO_BORDER, ExcelData.HEADER);
                    ExcelData accountData = new ExcelData(accountingLocalizer.localize(PdfLocalizationName.account), ExcelData.STRING, 25, false, true, ExcelData.NO_BORDER, ExcelData.HEADER);
                    ExcelData actualData = new ExcelData(accountingLocalizer.localize(PdfLocalizationName.actual), ExcelData.STRING, 25, false, true, ExcelData.NO_BORDER, ExcelData.HEADER_RIGHT);
                    ExcelData budgetData = new ExcelData(accountingLocalizer.localize(PdfLocalizationName.budget), ExcelData.STRING, 25, false, true, ExcelData.NO_BORDER, ExcelData.HEADER_RIGHT);
                    ExcelData varianceData = new ExcelData(commonLocalizer.localize(PdfLocalizationName.variance), ExcelData.STRING, 25, false, true, ExcelData.NO_BORDER, ExcelData.HEADER_RIGHT);
                    headers = new ExcelData[]{
                            accountCode, accountData, actualData, budgetData, varianceData};
                    lastColumnIndex = applyTitle(date, headers.length, fs);
                    list.add(headers);

                }
                // Gross items
                setBudgetData(profLoss.getRevenue(), showYTD, calculationScale);
                setBudgetData(profLoss.getSale(), showYTD, calculationScale);
                setBudgetData(profLoss.getExpense(), showYTD, calculationScale);
                setTotal(profLoss.getGrossVariance(), profLoss.getGrossYTDvariance(), showYTD, GROSS_PROFIT, calculationScale);
                // Net items
                setBudgetData(profLoss.getDirectCosts(), showYTD, calculationScale);
                setBudgetData(profLoss.getDepreciation(), showYTD, calculationScale);
                setBudgetData(profLoss.getOverhead(), showYTD, calculationScale);
                setBudgetData(profLoss.getOtherIncome(), showYTD, calculationScale);
                setTotal(profLoss.getNetVariance(), profLoss.getNetYTDvariance(), showYTD, NET_PROFIT, calculationScale);

            } else if (Objects.equals("86560", ServerSecurityContext.getInstance().getCompanyId())) {
                comparisonYears++;
                String headerIntent = getTabString(1);
                headers = new ExcelData[3];
                headers[0] = new ExcelData("", ExcelData.STRING, 10, false, true, ExcelData.NO_BORDER, ExcelData.HEADER);
                headers[1] = new ExcelData("", ExcelData.STRING, 60, false, true, ExcelData.NO_BORDER, ExcelData.HEADER);
                headers[2] = new ExcelData("", ExcelData.STRING, 25, false, true, ExcelData.NO_BORDER, ExcelData.HEADER_RIGHT);
                lastColumnIndex = applyTitle(date, headers.length, fs);
                list.add(headers);

                setComparisonContentGroup(profLoss.getRevenue(), calculationScale, false);
                setComparisonContentGroup(profLoss.getSale(), calculationScale, false);
                setComparisonContentGroup(profLoss.getExpense(), calculationScale, false);
                setComparisonContentGroup(profLoss.getDirectCosts(), calculationScale, false);
                setComparisonContentTotalForShipox(profLoss.getGrossProfit(), headerIntent + "CM2 - NET MARGIN", calculationScale);
                setComparisonContentGroup(profLoss.getOtherIncome(), calculationScale, false);
                setComparisonContentGroup(profLoss.getOverhead(), calculationScale, false);
                setComparisonContentGroup(profLoss.getDepreciation(), calculationScale, false);
                setComparisonContentTotalForShipox(profLoss.getNetProfit(), headerIntent + "NET INCOME / LOSS", calculationScale);
            } else {
                comparisonYears++;
                headers = new ExcelData[comparisonYears + 2];
                headers[0] = new ExcelData(accountingLocalizer.localize(PdfLocalizationName.accountName), ExcelData.STRING, 25, false, true, ExcelData.NO_BORDER, ExcelData.HEADER);
                headers[1] = new ExcelData(accountingLocalizer.localize(PdfLocalizationName.code), ExcelData.STRING, aCellSize, false, true, ExcelData.NO_BORDER, ExcelData.HEADER);

                for (int i = compareDates.length - 1; i >= 0; i--) {
                    headers[compareDates.length - i + 1] = new ExcelData(format.format(compareDates[i].getTo() != null ? compareDates[i].getTo().getDate() : ""), ExcelData.STRING, 25, false, true, ExcelData.NO_BORDER, ExcelData.HEADER_RIGHT);

                }
                headers[headers.length - 1] = new ExcelData(format.format(endDate), ExcelData.STRING, 25, false, true, ExcelData.NO_BORDER, ExcelData.HEADER_RIGHT);
                lastColumnIndex = applyTitle(date, headers.length, fs);
                list.add(headers);

                setComparisonContentGroup(profLoss.getRevenue(), calculationScale, filterParametrs.isSummaryView());
                setComparisonContentGroup(profLoss.getSale(), calculationScale, filterParametrs.isSummaryView());
                setComparisonContentGroup(profLoss.getExpense(), calculationScale, filterParametrs.isSummaryView());
                setComparisonContentGroup(profLoss.getDirectCosts(), calculationScale, filterParametrs.isSummaryView());
                setComparisonContentTotal(profLoss.getGrossProfit(), GROSS_PROFIT, calculationScale);
                setComparisonContentGroup(profLoss.getOtherIncome(), calculationScale, filterParametrs.isSummaryView());
                setComparisonContentGroup(profLoss.getOverhead(), calculationScale, filterParametrs.isSummaryView());
                setComparisonContentGroup(profLoss.getDepreciation(), calculationScale, filterParametrs.isSummaryView());
                setComparisonContentTotal(profLoss.getNetProfit(), NET_PROFIT, calculationScale);
            }
            WorkBook wb = new WorkBook(list);
            HSSFWorkbook hssfWorkbook = wb.getWorkBook(filename, 0, 0, 0, 8);
            hssfWorkbook.setRepeatingRowsAndColumns(0, 0, lastColumnIndex, 0, 7);

            if (Objects.equals("86560", ServerSecurityContext.getInstance().getCompanyId())) {
                HSSFSheet sheet = wb.getSheet();
                sheet.setDisplayGridlines(false);
            }
            hssfWorkbook.getSheetAt(0).createFreezePane(1, 0);
            return hssfWorkbook;
        }
        return null;
    }

    private int applyTitle(String date, int lastColumnIndex, EdsFinancialSettings fs) {
        EdsUser user = uploadManager.getUser();
        String currencySymbol = fs.getCurrency().getSymbol();
        currencySymbol = currencySymbol != null ? currencySymbol : "";
        String currencyCode = fs.getCurrency().getName();


        ExcelData titleData = ExcelData.getReportNameData(sheetName, aCellSize, lastColumnIndex);

        ExcelData companyData = ExcelData.getReportNameChildData(user.getCompany().getName(), aCellSize, lastColumnIndex);

        ExcelData dateData = ExcelData.getReportNameChildData(date, aCellSize, lastColumnIndex);

        ExcelData currencyData = ExcelData.getReportNameChildData(accountingLocalizer.localize(PdfLocalizationName.figuresIn) + " " + currencySymbol + "(" + currencyCode + ")", aCellSize, lastColumnIndex);

        list.add(new ExcelData[]{
                new ExcelData("", ExcelData.STRING, aCellSize, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL)
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

        ExcelData emptyData = new ExcelData("", ExcelData.STRING, aCellSize, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL);
        ExcelData[] cellEmptyHeader = new ExcelData[]{
                emptyData
        };
        list.add(cellEmptyHeader);
        return lastColumnIndex;
    }

    private void setComparisonContentGroup(AccountItemsByAccountType item, Integer calculationScale, boolean isSummary) {
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
                            if (accountCodeUnique != null) {
                                AccountItemWithBudgetDate budgetDate = new AccountItemWithBudgetDate(accountCodeUnique.getId(), accountCodeUnique.getCode(), accountCodeUnique.getName());
                                budgetDate.getChilds().add(acc);
                                map1.put(acc.getParentCode(), budgetDate);
                                map2.add(budgetDate);
                            } else {
                                map2.add(acc);
                            }
                        } else {
                            map1.get(acc.getParentCode()).getChilds().add(acc);
                        }
                    } else {
                        map2.add(acc);
                    }
                });

                String headerIntent = getTabString(1);
                if (Objects.equals("86560", ServerSecurityContext.getInstance().getCompanyId())) {
                    if (item.getActualTotal() != null) {
                        String parentName = !ServerUtils.isNullOrEmpty(item.getGroupName()) ? item.getGroupName() : "";
                        if (Objects.equals("Sales", parentName)) {
                            parentName = "Revenue";
                        } else if (Objects.equals("Cost of Sales", parentName)) {
                            parentName = "Cost of Revenue";
                        } else if (Objects.equals("Direct Expenses", parentName)) {
                            parentName = "CM1 - Gross Margin";
                        } else if (Objects.equals("Overhead", parentName)) {
                            parentName = "Operating expenses";
                        } else if (Objects.equals("Other Income", parentName)) {
                            parentName = "Other Income / Loss";
                        }
                        String parentColor = "D9D9D9";
                        short parentBg = getColor(parentColor);
                        ExcelData[] emptyHeader = new ExcelData[3];

                        ExcelData parentHeader = new ExcelData(headerIntent + parentName, ExcelData.STRING, 0, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL);
                        parentHeader.setBold(true);
                        parentHeader.setBgcolor(parentBg);

                        parentHeader.setMerged(true);
                        parentHeader.setFromRow(0);
                        parentHeader.setToRow(0);
                        parentHeader.setFromCell(0);
                        parentHeader.setToCell(2);

                        ExcelData parentHeaderTotal = null;
                        ArrayList<BudgetInDate> totals = item.getActualTotal();
                        if (totals != null) {
                            for (BudgetInDate total : totals) {
                                parentHeaderTotal = new ExcelData(total.getValue() != null ? total.getValue().setScale(calculationScale, RoundingMode.HALF_UP) : ZERO, ExcelData.CURRENCY, 0, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL);
                                parentHeaderTotal.setBold(true);
                                parentHeaderTotal.setBgcolor(parentBg);
                            }
                        }
                        if (parentHeaderTotal != null) {
                            ExcelData[] cellFooter = new ExcelData[]{
                                    parentHeader,
                                    parentHeaderTotal,
                                    parentHeaderTotal
                            };
                            list.add(emptyHeader);
                            list.add(cellFooter);
                            list.add(emptyHeader);
                        }
                    }
                } else {
                    if (shownObjects.contains(item.getGroupName())) {
                        list.add(drawBookMark(headerIntent + item.getGroupName()));
                    }
                }

                if (shownObjects.contains(item.getGroupName())) {
                    map2.forEach(key -> {
                        ArrayList<BigDecimal> childTotal = new ArrayList<>(tableColsCount - 1);
                        for (int i = 0; i < tableColsCount; i++) {
                            childTotal.add(i, BigDecimal.ZERO);
                        }
                        /*if (!shownObjects.contains(key.getCode())) {
                            calculateSummaryView(key, calculationScale, childTotal);
                            addItemToListSummary(calculationScale, 2, key, childTotal);
                        } else {
                            addItem(key, calculationScale, 2, childTotal);
                        }*/
                        addItem(key, calculationScale, 2, childTotal);
                    });
                }
                if (!Objects.equals("86560", ServerSecurityContext.getInstance().getCompanyId())) {
                    if (item.getActualTotal() != null) {
                        ExcelData[] rows = new ExcelData[item.getActualTotal().size() + 2];
                        String totalString = accountingLocalizer.localize(PdfLocalizationName.total) + " ";
                        if (!shownObjects.contains(item.getGroupName())) {
                            totalString = "";
                        }
                        rows[0] = new ExcelData(headerIntent + totalString + item.getGroupName(), ExcelData.STRING, 40, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL);
                        rows[0].setBold(true);
                        rows[1] = new ExcelData("", ExcelData.STRING, 40, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL);
                        ArrayList<BudgetInDate> totals = item.getActualTotal();
                        if (totals != null) {
                            int i = rows.length - 1;
                            for (BudgetInDate total : totals) {
                                rows[i] = new ExcelData(total.getValue() != null ? total.getValue().setScale(calculationScale, RoundingMode.HALF_UP) : ZERO, ExcelData.CURRENCY, 25, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL);
                                rows[i].setBold(true);
                                i--;
                            }
                        }
                        list.add(rows);
                    }
                }
            }
        }

    }

    private void addItemToListSummary(Integer calculationScale, int level, AccountItemWithBudgetDate budgetDate, ArrayList<BigDecimal> total) {
        String intent = getTabString(level);
        ArrayList<BudgetInDate> bItems = budgetDate.getRowCells() != null ? budgetDate.getRowCells() : new ArrayList<>();
        ExcelData[] items = new ExcelData[total.size() + 2];
        items[0] = new ExcelData(intent + budgetDate.getName(), ExcelData.STRING, 40, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL);
        items[1] = new ExcelData(budgetDate.getCode(), ExcelData.STRING, 40, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL);
        int j = items.length;
        for (BigDecimal bItem : total) {
            items[j - 1] = new ExcelData(bItem.setScale(calculationScale, RoundingMode.HALF_UP), ExcelData.CURRENCY, 25, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL);
            j--;
        }
        list.add(items);
    }

    private void calculateSummaryView(AccountItemWithBudgetDate budgetDate, Integer calculationScale, ArrayList<BigDecimal> childTotal) {
        if (!budgetDate.getChilds().isEmpty()) {
            budgetDate.getChilds().forEach(child -> {
                calculateSummaryView(child, calculationScale, childTotal);
                List<BudgetInDate> budgetInOneAccount = child.getRowCells();
                if (budgetInOneAccount != null && !child.isCalculated()) {
                    child.setCalculated(true);
                    calculate(childTotal, budgetInOneAccount);
                }
            });
            if (budgetDate.getRowCells() != null) {
                List<BudgetInDate> budgetInOneAccount = budgetDate.getRowCells();
                if (budgetInOneAccount != null && !budgetDate.isCalculated()) {
                    budgetDate.setCalculated(true);
                    calculate(childTotal, budgetInOneAccount);
                }
            }
        }
    }

    private void addItem(AccountItemWithBudgetDate budgetDate, Integer calculationScale, int level, ArrayList<BigDecimal> childTotal) {
        if (Objects.equals("86560", ServerSecurityContext.getInstance().getCompanyId())) {
            addItemToListForShipox(calculationScale, level, budgetDate, !budgetDate.getChilds().isEmpty());
        } else {
            addItemToList(calculationScale, level, budgetDate, !budgetDate.getChilds().isEmpty());
        }

        if (!budgetDate.getChilds().isEmpty()) {
            budgetDate.getChilds().forEach(child -> {
                if (shownObjects.contains(budgetDate.getCode())) {
                    addItem(child, calculationScale, level + 1, childTotal);
                }
                List<BudgetInDate> budgetInOneAccount = child.getRowCells();
                if (budgetInOneAccount != null && !child.isCalculated()) {
                    child.setCalculated(true);
                    calculate(childTotal, budgetInOneAccount);
                }
            });
            if (budgetDate.getRowCells() != null) {
                List<BudgetInDate> budgetInOneAccount = budgetDate.getRowCells();
                if (budgetInOneAccount != null && !budgetDate.isCalculated()) {
                    budgetDate.setCalculated(true);
                    calculate(childTotal, budgetInOneAccount);
                }
            }

            boolean used = false;
            if (!budgetDate.getChilds().isEmpty() && budgetDate.getRowCells() != null && !budgetDate.getRowCells().isEmpty()) {
                List<BudgetInDate> budgetInDates = budgetDate.getRowCells();
                Collections.reverse(budgetInDates);
                for (BudgetInDate bItem : budgetInDates) {
                    if (bItem.getValue() != null) {
                        used = true;
                    }
                }
            }

            if (budgetDate.getName() != null && !used) {
                List<BigDecimal> total = new ArrayList<>(childTotal);
                if (Objects.equals("86560", ServerSecurityContext.getInstance().getCompanyId())) {
                    drawChildTotalForShipox(budgetDate, calculationScale, level, total);
                } else {
                    drawChildTotal(budgetDate, calculationScale, level, total);
                }
            } else {
                ArrayList<BigDecimal> accountAndChildTotal = new ArrayList<>(tableColsCount - 1);
                for (int i = 0; i < tableColsCount; i++) {
                    accountAndChildTotal.add(i, BigDecimal.ZERO);
                }
                calculate(accountAndChildTotal, budgetDate.getRowCells());
                for (AccountItemWithBudgetDate child : budgetDate.getChilds()) {
                    List<BudgetInDate> budgetInOneAccount = child.getRowCells();
                    Collections.reverse(budgetInOneAccount);
                    if (budgetInOneAccount != null) {
                        calculate(accountAndChildTotal, budgetInOneAccount);
                    }
                }

                List<BigDecimal> total = new ArrayList<>(accountAndChildTotal);
                Collections.reverse(total);
                if (Objects.equals("86560", ServerSecurityContext.getInstance().getCompanyId())) {
                    drawChildTotalForShipox(budgetDate, calculationScale, level, total);
                } else {
                    drawChildTotal(budgetDate, calculationScale, level, total);
                }

            }
        }
    }

    private void drawChildTotal(AccountItemWithBudgetDate budgetDate, Integer calculationScale, int level, List<BigDecimal> total) {
        String intent = getTabString(level);
        ArrayList<BudgetInDate> bItems = budgetDate.getRowCells() != null ? budgetDate.getRowCells() : new ArrayList<>();
        ExcelData[] items = new ExcelData[total.size() + 2];
        items[0] = new ExcelData(intent + accountingLocalizer.localize(PdfLocalizationName.total) + " " + budgetDate.getName(), ExcelData.STRING, 40, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL);
        items[0].setBold(true);
        items[1] = new ExcelData("", ExcelData.STRING, 40, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL);
        items[1].setBold(true);
        int j = items.length;
        for (BigDecimal bItem : total) {
            items[j - 1] = new ExcelData(bItem.setScale(calculationScale, RoundingMode.HALF_UP), ExcelData.CURRENCY, 25, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL);
            items[j - 1].setBold(true);
            j--;
        }
        list.add(items);
    }

    private void drawChildTotalForShipox(AccountItemWithBudgetDate budgetDate, Integer calculationScale, int level, List<BigDecimal> total) {
        String intent = getTabString(level);
        ExcelData[] items = new ExcelData[3];
        items[0] = new ExcelData("", ExcelData.STRING, 0, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL);
        items[1] = new ExcelData(intent + accountingLocalizer.localize(PdfLocalizationName.total) + " " + budgetDate.getName(), ExcelData.STRING, 0, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL);
        items[1].setBold(true);
        int j = items.length;
        for (BigDecimal bItem : total) {
            items[j - 1] = new ExcelData(bItem.setScale(calculationScale, RoundingMode.HALF_UP), ExcelData.CURRENCY, 0, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL);
            items[j - 1].setBold(true);
            j--;
        }
        list.add(items);
    }

    private void calculate(ArrayList<BigDecimal> childTotal, List<BudgetInDate> budgetInOneAccount) {
        int i = 0;
        for (BigDecimal cellTotal : childTotal) {
            BigDecimal amount = budgetInOneAccount.get(i).getValue() != null
                    ? budgetInOneAccount.get(i).getValue()
                    : BigDecimal.ZERO;
            if (cellTotal == null) {
                childTotal.set(i, amount);
            } else {
                childTotal.set(i, cellTotal.add(amount));
            }
            i++;
        }
    }

    private void addItemToList(Integer calculationScale, int level, AccountItemWithBudgetDate budgetDate, boolean hasChilds) {
        if (budgetDate != null) {
            boolean addRow = false;
            String intent = getTabString(level);
            ArrayList<BudgetInDate> bItems = budgetDate.getRowCells() != null ? budgetDate.getRowCells() : new ArrayList<>();
            ExcelData[] items = new ExcelData[bItems.size() + 2];
            items[0] = new ExcelData(intent + budgetDate.getName(), ExcelData.STRING, 40, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL);
            items[1] = new ExcelData(budgetDate.getCode(), ExcelData.STRING, 40, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL);
            if (hasChilds) {
                items[0].setBold(true);
                items[1].setBold(true);
            }
            int j = items.length;
            for (BudgetInDate bItem : bItems) {
                if (bItem.getValue() != null && bItem.getValue().compareTo(BigDecimal.ZERO) != 0) {
                    items[j - 1] = new ExcelData(bItem.getValue().setScale(calculationScale, RoundingMode.HALF_UP), ExcelData.CURRENCY, 25, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL);
                    addRow = true;
                }
                j--;
            }
            if (addRow || hasChilds) {
                list.add(items);
            }
        }
    }

    private void addItemToListForShipox(Integer calculationScale, int level, AccountItemWithBudgetDate budgetDate, boolean hasChilds) {
        if (budgetDate != null) {
            boolean addRow = false;
            String intent = getTabString(level);
            ArrayList<BudgetInDate> bItems = budgetDate.getRowCells() != null ? budgetDate.getRowCells() : new ArrayList<>();
            ExcelData[] items = new ExcelData[3];
            items[0] = new ExcelData(budgetDate.getCode(), ExcelData.STRING, 0, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL);
            items[1] = new ExcelData(intent + budgetDate.getName(), ExcelData.STRING, 0, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL);
            if (hasChilds) {
                items[0].setBold(true);
                items[1].setBold(true);
            }
            int j = items.length;
            for (BudgetInDate bItem : bItems) {
                if (bItem.getValue() != null && bItem.getValue().compareTo(BigDecimal.ZERO) != 0) {
                    items[j - 1] = new ExcelData(bItem.getValue().setScale(calculationScale, RoundingMode.HALF_UP), ExcelData.CURRENCY, 0, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL);
                    addRow = true;
                }
                j--;
            }
            if (addRow || hasChilds) {
                list.add(items);
            }
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

    private FromToDate[] getCompareDates(Date from, Integer compareWithId) {
        CompareWithEnum compareWithEnum = CompareWithEnum.getEnumyById(compareWithId);
        int length = compareWithEnum.getLength();
        FromToDate[] compareDates = new FromToDate[length];
        if (CompareCategoryEnum.Day.equals(compareWithEnum.getCompareCategoryEnum())) {
            Calendar fromCalendar = new GregorianCalendar();
            fromCalendar.setTime(from);
            fromCalendar.add(Calendar.DATE, -1);

            Calendar toCalendar = new GregorianCalendar();
            toCalendar.setTime(from);
            toCalendar.add(Calendar.DATE, -1);

            ServerUtils.setBeginningOfTheDay(fromCalendar);
            ServerUtils.setEndOfTheDay(toCalendar);

            compareDates[0] = new FromToDate(new DateNonConvertable(fromCalendar.getTime()), new DateNonConvertable(toCalendar.getTime()));
        } else if (CompareCategoryEnum.Week.equals(compareWithEnum.getCompareCategoryEnum())) {
            Calendar fromCalendar = new GregorianCalendar();
            fromCalendar.setTime(from);
            fromCalendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
            fromCalendar.add(Calendar.DATE, -7);

            Calendar toCalendar = new GregorianCalendar();
            toCalendar.setTime(from);
            toCalendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
            toCalendar.add(Calendar.DATE, -7);

            ServerUtils.setBeginningOfTheDay(fromCalendar);
            ServerUtils.setEndOfTheDay(toCalendar);

            compareDates[0] = new FromToDate(new DateNonConvertable(fromCalendar.getTime()), new DateNonConvertable(toCalendar.getTime()));
        } else if (CompareCategoryEnum.Month.equals(compareWithEnum.getCompareCategoryEnum())) {
            for (int i = 0; i < compareWithEnum.getLength(); i++) {
                Calendar fromCalendar = new GregorianCalendar();
                fromCalendar.setTime(from);
                fromCalendar.add(Calendar.MONTH, -(i + 1));

                Calendar toCalendar = new GregorianCalendar();
                toCalendar.setTime(from);
                toCalendar.add(Calendar.MONTH, -(i + 1));
                toCalendar.set(Calendar.DAY_OF_MONTH, toCalendar.getActualMaximum(Calendar.DAY_OF_MONTH));

                ServerUtils.setBeginningOfTheDay(fromCalendar);
                ServerUtils.setEndOfTheDay(toCalendar);

                compareDates[i] = new FromToDate(new DateNonConvertable(fromCalendar.getTime()), new DateNonConvertable(toCalendar.getTime()));
            }
        } else if (CompareCategoryEnum.Year.equals(compareWithEnum.getCompareCategoryEnum())) {
            for (int i = 0; i < compareWithEnum.getLength(); i++) {
                Calendar fromCalendar = new GregorianCalendar();
                fromCalendar.setTime(from);
                fromCalendar.add(Calendar.YEAR, -(i + 1));

                Calendar toCalendar = new GregorianCalendar();
                toCalendar.setTime(fromCalendar.getTime());
                toCalendar.add(Calendar.YEAR, 1);
                toCalendar.add(Calendar.DATE, -1);

                ServerUtils.setBeginningOfTheDay(fromCalendar);
                ServerUtils.setEndOfTheDay(toCalendar);

                compareDates[i] = new FromToDate(new DateNonConvertable(fromCalendar.getTime()), new DateNonConvertable(toCalendar.getTime()));
            }
        }

        return compareDates;
    }

    private void setBudgetData(AccountItemsByAccountType item, boolean showYTD, Integer calculationScale) {
        if (item != null) {
            AccountItemWithBudgetDate[] accItems = item.getAccountItems();
            if (accItems != null && accItems.length > 0) {
                if (showYTD) {
                    list.add(drawBookMark(item.getGroupName()));
                    for (AccountItemWithBudgetDate accItem : accItems) {
                        if (accItem != null) {
                            ActBudVar var = accItem.getVariance();
                            ExcelData[] cellBodyData;
                            ExcelData codeCell = new ExcelData(accItem.getCode(), ExcelData.STRING, 40, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL);
                            ExcelData nameCell = new ExcelData(accItem.getName(), ExcelData.STRING, 40, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL);

                            ExcelData actualBodyCellData = new ExcelData(var != null && var.getActual() != null ? var.getActual().setScale(calculationScale, RoundingMode.HALF_UP) : ZERO, ExcelData.CURRENCY, 25, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL);
                            ExcelData bujectlBodyCellData = new ExcelData(var != null && var.getBudget() != null ? var.getBudget().setScale(calculationScale, RoundingMode.HALF_UP) : ZERO, ExcelData.CURRENCY, 25, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL);
                            ExcelData varianceBodyCellData = new ExcelData(var != null && var.getVariance() != null ? var.getVariance().setScale(calculationScale, RoundingMode.HALF_UP).doubleValue() / 100 : ZERO.doubleValue(), ExcelData.NUMBER_FORMAT_PERCENTAGE, 25, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL);

                            ActBudVar ytdVar = accItem.getYTDvariance();
                            if (ytdVar != null) {
                                ExcelData actualBodyCellData2 = new ExcelData(ytdVar.getActual() != null ? ytdVar.getActual().setScale(calculationScale, RoundingMode.HALF_UP) : null, ExcelData.CURRENCY, 25, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL);
                                ExcelData bujectlBodyCellData2 = new ExcelData(ytdVar.getBudget() != null ? ytdVar.getBudget().setScale(calculationScale, RoundingMode.HALF_UP) : null, ExcelData.CURRENCY, 25, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL);
                                ExcelData varianceBodyCellData2 = new ExcelData(ytdVar.getVariance() != null ? ytdVar.getVariance().setScale(calculationScale, RoundingMode.HALF_UP).doubleValue() / 100 : null, ExcelData.NUMBER_FORMAT_PERCENTAGE, 25, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL);
                                cellBodyData = new ExcelData[]{
                                        codeCell, nameCell,
                                        actualBodyCellData, bujectlBodyCellData, varianceBodyCellData,
                                        actualBodyCellData2, bujectlBodyCellData2, varianceBodyCellData2

                                };
                            } else {
                                cellBodyData = new ExcelData[]{
                                        codeCell, nameCell,
                                        actualBodyCellData, bujectlBodyCellData, varianceBodyCellData
                                };

                            }
                            list.add(cellBodyData);
                        }
                    }
                } else {
                    list.add(drawBookMark(item.getGroupName()));
                    ExcelData[] cellBodyData2;
                    for (AccountItemWithBudgetDate accItem : accItems) {
                        if (accItem != null) {
                            ActBudVar var = accItem.getVariance();
                            ExcelData codeCell = new ExcelData(accItem.getCode(), ExcelData.STRING, 40, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL);
                            ExcelData nameCell2 = new ExcelData(accItem.getName(), ExcelData.STRING, 40, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL);
                            if (var != null) {

                                ExcelData actualBodyCell = new ExcelData(var.getActual() != null ? var.getActual().setScale(calculationScale, RoundingMode.HALF_UP) : ZERO, ExcelData.CURRENCY, 25, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL);
                                ExcelData bujectlBodyCell = new ExcelData(var.getBudget() != null ? var.getBudget().setScale(calculationScale, RoundingMode.HALF_UP) : ZERO, ExcelData.CURRENCY, 25, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL);
                                ExcelData varianceBodyCell = new ExcelData(var.getVariance() != null ? var.getVariance().setScale(calculationScale, RoundingMode.HALF_UP).doubleValue() / 100 : ZERO.doubleValue() / 100, ExcelData.NUMBER_FORMAT_PERCENTAGE, 25, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL);
                                cellBodyData2 = new ExcelData[]{
                                        codeCell, nameCell2,
                                        actualBodyCell, bujectlBodyCell, varianceBodyCell
                                };
                                list.add(cellBodyData2);
                            }
                        }
                    }
                    setTotalForGroup(item.getTotalWithVariance(), item.getGroupName(), calculationScale);
                }
            }
        }
    }

    private void setTotalForGroup(ActBudVar var, String name, Integer calculationScale) {
        ExcelData nameCell = new ExcelData(name != null ? accountingLocalizer.localize(PdfLocalizationName.total) + " " + name : "", ExcelData.STRING, 40, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL);
        nameCell.setBold(true);

        ExcelData actualBodyCell = new ExcelData(var != null && var.getActual() != null ? var.getActual().setScale(calculationScale, RoundingMode.HALF_UP) : ZERO, ExcelData.CURRENCY, 25, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL);
        ExcelData bujectlBodyCell = new ExcelData(var != null && var.getBudget() != null ? var.getBudget().setScale(calculationScale, RoundingMode.HALF_UP) : ZERO, ExcelData.CURRENCY, 25, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL);
        ExcelData varianceBodyCell = new ExcelData(var != null && var.getVariance() != null ? var.getVariance().setScale(calculationScale, RoundingMode.HALF_UP).doubleValue() / 100 : ZERO.doubleValue(), ExcelData.NUMBER_FORMAT_PERCENTAGE, 25, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL);
        ExcelData[] cellTotal = new ExcelData[]{
                nameCell,
                actualBodyCell, bujectlBodyCell, varianceBodyCell
        };
        list.add(cellTotal);
    }


    private void setTotal(ActBudVar variance, ActBudVar ytdVariance, boolean showYTD, String totalHeader, Integer calculationScale) {
        ExcelData nameCell = new ExcelData(totalHeader != null ? totalHeader : "", ExcelData.STRING, 40, false, true, ExcelData.NO_BORDER, ExcelData.HEADER);
        ExcelData emptyCell = new ExcelData("", ExcelData.STRING, 40, false, true, ExcelData.NO_BORDER, ExcelData.HEADER);
        nameCell.setBold(true);
        ExcelData[] cellTotal;
        ExcelData actualBodyCellData = new ExcelData(variance != null && variance.getActual() != null ? variance.getActual().setScale(calculationScale, RoundingMode.HALF_UP) : ZERO, ExcelData.CURRENCY, 25, false, true, ExcelData.NO_BORDER, ExcelData.HEADER);
        ExcelData bujectlBodyCellData = new ExcelData(variance != null && variance.getBudget() != null ? variance.getBudget().setScale(calculationScale, RoundingMode.HALF_UP) : ZERO, ExcelData.CURRENCY, 25, false, true, ExcelData.NO_BORDER, ExcelData.HEADER);
        ExcelData varianceBodyCellData = new ExcelData(variance != null && variance.getVariance() != null ? variance.getVariance().setScale(calculationScale, RoundingMode.HALF_UP).doubleValue() / 100 : ZERO.doubleValue(), ExcelData.NUMBER_FORMAT_PERCENTAGE, 25, false, true, ExcelData.NO_BORDER, ExcelData.HEADER);

        if (showYTD && variance != null) {
            ExcelData actualBodyCellData2 = new ExcelData(variance.getActual() != null ? variance.getActual().setScale(calculationScale, RoundingMode.HALF_UP) : ZERO, ExcelData.CURRENCY, 25, false, true, ExcelData.NO_BORDER, ExcelData.HEADER);
            ExcelData bujectlBodyCellData2 = new ExcelData(variance.getBudget() != null ? variance.getBudget().setScale(calculationScale, RoundingMode.HALF_UP) : ZERO, ExcelData.CURRENCY, 25, false, true, ExcelData.NO_BORDER, ExcelData.HEADER);
            ExcelData varianceBodyCellData2 = new ExcelData(variance.getVariance() != null ? variance.getVariance().setScale(calculationScale, RoundingMode.HALF_UP).doubleValue() / 100 : ZERO.doubleValue(), ExcelData.NUMBER_FORMAT_PERCENTAGE, 25, false, true, ExcelData.NO_BORDER, ExcelData.HEADER);
            cellTotal = new ExcelData[]{
                    nameCell, emptyCell,
                    actualBodyCellData, bujectlBodyCellData, varianceBodyCellData,
                    actualBodyCellData2, bujectlBodyCellData2, varianceBodyCellData2
            };
            list.add(cellTotal);
        } else {
            cellTotal = new ExcelData[]{
                    nameCell, emptyCell,
                    actualBodyCellData, bujectlBodyCellData, varianceBodyCellData
            };
            list.add(cellTotal);
        }
    }

    private ExcelData[] drawBookMark(String name) {
        ExcelData headerData = new ExcelData(name != null ? name : "", ExcelData.STRING, 60, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL);
        headerData.setBold(true);
        ExcelData emptyData = new ExcelData("", ExcelData.STRING, 25, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL);
        return new ExcelData[]{
                headerData,
                emptyData
        };
    }

    private void setComparisonContentTotal(ArrayList<BudgetInDate> totals, String totalName, Integer calculationScale) {
        ExcelData[] rows = new ExcelData[totals.size() + 2];


        rows[0] = new ExcelData(totalName != null ? totalName : "", ExcelData.STRING, 40, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL);
        rows[0].setBold(true);

        int i = rows.length - 1;
        for (BudgetInDate total : totals) {
            ExcelData cellData = new ExcelData(total.getValue() != null ? total.getValue().setScale(calculationScale, RoundingMode.HALF_UP) : ZERO, ExcelData.CURRENCY, 25, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL);
            cellData.setBold(true);
            rows[i] = cellData;
            i--;
        }

        list.add(rows);
    }

    private void setComparisonContentTotalForShipox(ArrayList<BudgetInDate> totals, String totalName, Integer calculationScale) {
        ExcelData[] amountTopEmpty = new ExcelData[3];
        ExcelData[] amountBottomEmpty = new ExcelData[3];

        String profitColor = "BFBFBF";
        short profitBg = getColor(profitColor);

        ExcelData netTotalFooterTitle = new ExcelData(totalName != null ? totalName : "", ExcelData.STRING, 0, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL);
        netTotalFooterTitle.setBold(true);
        netTotalFooterTitle.setBgcolor(profitBg);

        netTotalFooterTitle.setMerged(true);
        netTotalFooterTitle.setFromRow(0);
        netTotalFooterTitle.setToRow(0);
        netTotalFooterTitle.setFromCell(0);
        netTotalFooterTitle.setToCell(2);

        ExcelData netTotalFooterValue = null;
        for (BudgetInDate total : totals) {
            netTotalFooterValue = new ExcelData(total.getValue() != null ? total.getValue().setScale(calculationScale, RoundingMode.HALF_UP) : ZERO, ExcelData.CURRENCY, 0, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL);
            netTotalFooterValue.setBold(true);
            netTotalFooterValue.setBgcolor(profitBg);
        }

        list.add(amountTopEmpty);
        if (netTotalFooterValue != null) {
            ExcelData[] totalFooter = new ExcelData[]{
                    netTotalFooterTitle,
                    netTotalFooterValue,
                    netTotalFooterValue
            };
            list.add(totalFooter);
        }
        list.add(amountBottomEmpty);
    }

    private short getColor(String hexColor) {
        if (hexColor != null && !"".equals(hexColor)) {
            int[] colors = Utils.convertHexToRGB(hexColor);
            HSSFWorkbook hwb = new HSSFWorkbook();
            HSSFPalette palette = hwb.getCustomPalette();
            HSSFColor myColor = palette.findSimilarColor(colors[0], colors[1], colors[2]);
            return myColor.getIndex();
        }
        return 0;
    }

    @Override
    protected boolean prepareRequest(HttpServletRequest request) {
        return false;
    }
}
