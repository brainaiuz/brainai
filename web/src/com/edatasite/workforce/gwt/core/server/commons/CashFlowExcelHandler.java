package com.edatasite.workforce.gwt.core.server.commons;

import com.edatasite.shared.poiutils.WorkBook;
import com.edatasite.workforce.core.domain.EdsProperty;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.accounting.EdsFinancialSettings;
import com.edatasite.workforce.gwt.accounting.client.rpc.AccountingService;
import com.edatasite.workforce.gwt.accounting.client.rpc.CashFlow;
import com.edatasite.workforce.gwt.accounting.client.rpc.CashFlowItem;
import com.edatasite.workforce.gwt.core.client.rpc.accounting.AccountItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.db.PropertManager;
import com.edatasite.workforce.gwt.core.server.db.UploadManager;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.localization.PdfLocalizationName;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by Sherzod on 1/14/2016.
 */
public class CashFlowExcelHandler extends BaseExcelHandler {
    @Autowired
    private AccountingService accountingService;
    @Autowired
    private PropertManager propertManager;
    private String sheetName;
    @Autowired
    private UploadManager uploadManager;
    private List<ExcelData[]> list;

    private final int aCellSize = 110;
    private final int bCellSize = 36;

    private final BigDecimal beginningBalance = BigDecimal.ZERO;
    private CashFlow cashFlow;

    @Override
    protected void setFileName() {
        filename = "CashFlow_" + dateFormat(uploadManager.getUser().getUserDate());
    }

    @Override
    protected HSSFWorkbook getWorkBook(Object object) {
        EdsUser user = uploadManager.getUser();
        String shortDateFormat = user.getCompany().getCompanySettings().getShortDateFormat();
        ListingFilterParameter filterParametrs = (ListingFilterParameter) object;
        EdsProperty property = propertManager.findByCode(filterParametrs.getPropertyCode());
        sheetName = property != null ? property.getPlural() : commonLocalizer.localize(PdfLocalizationName.cashFlowStatement);
        SimpleDateFormat format = new SimpleDateFormat(shortDateFormat != null ? shortDateFormat : "MMM dd yyyy", Locale.ENGLISH);
        Date startDate = parseFilterParameterDate(filterParametrs.getStartDateNC());
        Date endDate = parseFilterParameterDate(filterParametrs.getEndDateNC());
        Integer showAccounts = filterParametrs.getType();

        EdsFinancialSettings fs = financialSettingsManager.getFinancialSettings();
        String currencySymbol = fs.getCurrency().getSymbol();
        String currencyCode = fs.getCurrency().getName();
        currencySymbol = currencySymbol != null ? currencySymbol : "";

        CashFlow cashFlow = accountingService.getCashFlow(filterParametrs);

        SimpleDateFormat fpDateFormat = new SimpleDateFormat("ddMMyyyy HH:mm:ss");
        filterParametrs.setStartDateNC(fpDateFormat.format(ServerUtils.getDayStartTime(new Date(0))));
        filterParametrs.setEndDateNC(fpDateFormat.format(ServerUtils.getDayEndTime(ServerUtils.addDays(startDate, -1))));
        CashFlow beginningBalance = accountingService.getCashFlow(filterParametrs);

        cashFlow.setCashAtTheBeginningOfPeriod(beginningBalance.getNetIncreaseDecreaseForPeriod());
        cashFlow.setCashAtTheEndOfPeriod(cashFlow.getCashAtTheBeginningOfPeriod().add(cashFlow.getNetIncreaseDecreaseForPeriod()));
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

        list = new LinkedList<>();
        list.add(new ExcelData[]{
                new ExcelData("", ExcelData.STRING, aCellSize, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL)
        });

        int lastColumnIndex = 6;
        ExcelData titleData = ExcelData.getReportNameData(sheetName, aCellSize, lastColumnIndex);

        ExcelData companyData = ExcelData.getReportNameChildData(user.getCompany().getName(), aCellSize, lastColumnIndex);

        ExcelData dateData = ExcelData.getReportNameChildData(date, aCellSize, lastColumnIndex);

        ExcelData currencyData = ExcelData.getReportNameChildData(commonLocalizer.localize(PdfLocalizationName.figuresIn) + " " + currencySymbol + "(" + currencyCode + ")", aCellSize, lastColumnIndex);

        list.add( new ExcelData[]{
                titleData
        });
        list.add( new ExcelData[]{
                companyData
        });
        list.add( new ExcelData[]{
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

        ExcelData[] cellHeader;

        ExcelData accountData = new ExcelData(commonLocalizer.localize(PdfLocalizationName.account), ExcelData.STRING, aCellSize, false, true, ExcelData.NO_BORDER, ExcelData.HEADER_LEFT);
        ExcelData balanceData = new ExcelData(commonLocalizer.localize(PdfLocalizationName.balance), ExcelData.STRING, bCellSize, false, true, ExcelData.NO_BORDER, ExcelData.HEADER_RIGHT);
        accountData.setBold(true);
        balanceData.setBold(true);
        cellHeader = new ExcelData[]{accountData, balanceData};
        list.add(cellHeader);

        Integer calculationScale = getCalculationScale(fs);

        addEmptyLine(list);
        createGroupHeader(list, commonLocalizer.localize(PdfLocalizationName.operatingActivities));
        createGroupNetTotal(list, commonLocalizer.localize(PdfLocalizationName.netProfit), cashFlow.getNetProfit(), calculationScale);
        createInnerGroup(list, accountingLocalizer.localize(PdfLocalizationName.currentAsset), cashFlow.getCurrentAssets(), calculationScale);
        createInnerGroup(list, commonLocalizer.localize(PdfLocalizationName.prepayments), cashFlow.getPrepayments(), calculationScale);
        createInnerGroup(list, commonLocalizer.localize(PdfLocalizationName.currentLiability), cashFlow.getCurrentLiabilities(), calculationScale);
        createGroupNetTotal(list, commonLocalizer.localize(PdfLocalizationName.netCashFromOperatingActivities), cashFlow.getNetOperatingActivities(), calculationScale);

        addEmptyLine(list);
        createGroupHeader(list, commonLocalizer.localize(PdfLocalizationName.investingActivities));
        createInnerGroup(list, commonLocalizer.localize(PdfLocalizationName.accumulatedDepreciation), cashFlow.getAccumulatedDepreciations(), calculationScale);
        createInnerGroup(list, commonLocalizer.localize(PdfLocalizationName.fixedAssetAccounts), cashFlow.getFixedAssets(), calculationScale);
        createInnerGroup(list, accountingLocalizer.localize(PdfLocalizationName.nonCurrentAssets), cashFlow.getNonCurrentAssets(), calculationScale);
        createInnerGroup(list, commonLocalizer.localize(PdfLocalizationName.liabilities), cashFlow.getLiabilities(), calculationScale);
        createGroupNetTotal(list, commonLocalizer.localize(PdfLocalizationName.netCashFromInvestingActivities), cashFlow.getNetInvestingActivities(), calculationScale);

        addEmptyLine(list);
        createGroupHeader(list, commonLocalizer.localize(PdfLocalizationName.financingActivities));
        createInnerGroup(list, commonLocalizer.localize(PdfLocalizationName.longTermLiabilityAccounts), cashFlow.getLongTermLiabilities(), calculationScale);
        createInnerGroup(list, commonLocalizer.localize(PdfLocalizationName.equityAccounts), cashFlow.getEquities(), calculationScale);
        createGroupNetTotal(list, commonLocalizer.localize(PdfLocalizationName.netCashFromFinancingActivities), cashFlow.getNetFinancingActivities(), calculationScale);

        addEmptyLine(list);
        createGrandTotal(list, commonLocalizer.localize(PdfLocalizationName.netIncreaseDecreaseForPeriod), cashFlow.getNetIncreaseDecreaseForPeriod(), calculationScale);
        createGrandTotal(list, commonLocalizer.localize(PdfLocalizationName.cashAtTheBeginningOfPeriod), cashFlow.getCashAtTheBeginningOfPeriod(), calculationScale);
        createGrandTotal(list, commonLocalizer.localize(PdfLocalizationName.cashAtTheEndOfPeriod), cashFlow.getCashAtTheEndOfPeriod(), calculationScale);


        HSSFWorkbook wb = new WorkBook(list).getWorkBook(filename, 0, 0, 0, 3);
        // Set the columns to repeat from column 0 to 2 on the first sheet
        wb.setRepeatingRowsAndColumns(0, 0, 4, 0, 5);

        return wb;
    }

    private void addEmptyLine(List<ExcelData[]> list) {
        ExcelData emptyLine = new ExcelData("", ExcelData.STRING, aCellSize, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL);
        list.add(new ExcelData[]{emptyLine});
    }

    private void createGroupHeader(List<ExcelData[]> list, String text) {
        ExcelData cellName = new ExcelData(text, ExcelData.STRING, aCellSize, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL);
        cellName.setBold(true);
        list.add(new ExcelData[]{cellName});
    }

    private void createGroupNetTotal(List<ExcelData[]> list, String text, BigDecimal netTotal, Integer calculationScale) {
        ExcelData cellName = new ExcelData(text, ExcelData.STRING, aCellSize, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL);
        ExcelData cellBalance = new ExcelData(createCell(netTotal, calculationScale), ExcelData.BIG_DECIMAL, bCellSize, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL);
        cellName.setBold(true);
        cellBalance.setBold(true);
        list.add(new ExcelData[]{cellName, cellBalance});
    }

    private void createGrandTotal(List<ExcelData[]> list, String text, BigDecimal total, Integer calculationScale) {
        ExcelData cellName = new ExcelData(text, ExcelData.STRING, aCellSize, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL);
        ExcelData cellBalance = new ExcelData(createCell(total, calculationScale), ExcelData.BIG_DECIMAL, bCellSize, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL);
        cellName.setBold(true);
        cellBalance.setBold(true);
        list.add(new ExcelData[]{cellName, cellBalance});
    }

    private void createInnerGroup(List<ExcelData[]> list, String header, List<CashFlowItem> items, Integer calculationScale) {
        if (items != null && items.size() > 0) {
            createInnerGroupHeader(list, header);

            Map<String, CashFlowItem> map1 = new HashMap<>(items.size());
            ArrayList<CashFlowItem> map2 = new ArrayList<>();
            BigDecimal totalInnerGroupBalance = BigDecimal.ZERO;
            for (CashFlowItem item : items) {
                if (item.getBalance() != null) {
                    totalInnerGroupBalance = totalInnerGroupBalance.add(item.getBalance());
                }
                map1.put(item.getCode(), item);
            }
            for (CashFlowItem item : items) {
                if (item.getParentCode() != null) {
                    if (map1.get(item.getParentCode()) == null) {
                        AccountItem accountCodeUnique = new AccountItem(item.getParentId(), item.getParentCode(), item.getParentName());
                        CashFlowItem cashFlowItem = new CashFlowItem(accountCodeUnique.getId(), accountCodeUnique.getCode(), accountCodeUnique.getName(), BigDecimal.ZERO);
                        cashFlowItem.getChilds().add(item);
                        map1.put(item.getParentCode(), cashFlowItem);
                        map2.add(cashFlowItem);
                    } else {
                        map1.get(item.getParentCode()).getChilds().add(item);
                    }
                } else {
                    map2.add(item);
                }
            }
            map2.forEach(key -> {
                BigDecimal childTotal = new BigDecimal(0);
                addItem(list, calculationScale, key, childTotal, 2);
            });
            drawTolatsRow(list, calculationScale, totalInnerGroupBalance, 1, commonLocalizer.localize(PdfLocalizationName.total) + " " + header);
        }
    }

    private void addItem(List<ExcelData[]> list, Integer calculationScale, CashFlowItem item, BigDecimal childTotal, int level) {
        addItemToList(list, calculationScale, level, item,  !item.getChilds().isEmpty());
        if (!item.getChilds().isEmpty()) {
            for (CashFlowItem child : item.getChilds()) {
                addItem(list, calculationScale, child, childTotal, level + 1);
                if (!child.isCalculated()) {
                    childTotal = childTotal.add(child.getBalance());
                    child.setCalculated(true);
                }
            }
            if (!item.isCalculated()) {
                childTotal = childTotal.add(item.getBalance());
                item.setCalculated(true);
            }
            if (item.getAccount() != null) {
                drawTolatsRow(list, calculationScale, childTotal, level,
                        commonLocalizer.localize(PdfLocalizationName.total) + " " + item.getAccount().getName() + " (" + item.getCode() + ")");
            }
        }
    }

    private void addItemToList(List<ExcelData[]> list, Integer calculationScale, int level, CashFlowItem item, boolean hasChilds) {
        String intent = getTabString(level);

        ExcelData cellName = new ExcelData(intent + item.getAccount().getName() + " (" + item.getCode() + ")", ExcelData.STRING, aCellSize, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL);
        ExcelData cellBalance = new ExcelData(createCell(item.getBalance(), calculationScale), ExcelData.BIG_DECIMAL, bCellSize, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL);
        if (hasChilds) {
            cellName.setBold(true);
            cellBalance.setBold(true);
        }
        list.add(new ExcelData[]{cellName, cellBalance});
    }

    private void drawTolatsRow(List<ExcelData[]> list, Integer calculationScale, BigDecimal childTotal, int level, String groupName) {
        String intent = getTabString(level);

        ExcelData cellName = new ExcelData(intent + groupName, ExcelData.STRING, aCellSize, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL);
        cellName.setBold(true);
        ExcelData cellBalance = new ExcelData(createCell(childTotal, calculationScale), ExcelData.BIG_DECIMAL, bCellSize, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL);
        cellBalance.setBold(true);
        list.add(new ExcelData[]{cellName, cellBalance});
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

    private void createInnerGroupHeader(List<ExcelData[]> list, String text) {
        ExcelData cellName = new ExcelData(text, ExcelData.STRING, aCellSize, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL);
        cellName.setBold(true);
        list.add(new ExcelData[]{cellName});
    }

    private BigDecimal createCell(BigDecimal value, Integer calculationScale) {
        return (value != null ? value.setScale(calculationScale, BigDecimal.ROUND_HALF_UP) : BigDecimal.ZERO);
    }
}
