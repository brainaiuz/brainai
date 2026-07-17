package com.edatasite.workforce.gwt.core.server.commons;

import com.edatasite.shared.poiutils.WorkBook;
import com.edatasite.workforce.core.domain.EdsCurrency;
import com.edatasite.workforce.core.domain.EdsProperty;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.accounting.EdsFinancialSettings;
import com.edatasite.workforce.gwt.accounting.client.rpc.AccountingService;
import com.edatasite.workforce.gwt.accounting.client.rpc.BalanceSheet;
import com.edatasite.workforce.gwt.accounting.client.rpc.BalanceSheetInnerItem;
import com.edatasite.workforce.gwt.accounting.client.rpc.BalanceSheetSummary;
import com.edatasite.workforce.gwt.accounting.client.ui.AccountingConstants;
import com.edatasite.workforce.gwt.accounting.client.ui.view.balancesheet.BalancesheetSettingsItem;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.app.Utils;
import com.edatasite.workforce.gwt.core.server.db.CurrencyManager;
import com.edatasite.workforce.gwt.core.server.db.PropertManager;
import com.edatasite.workforce.gwt.core.server.db.UploadManager;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.localization.PdfLocalizationName;
import com.google.gwt.user.server.rpc.security.ServerSecurityContext;
import org.apache.poi.hssf.usermodel.HSSFPalette;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.Locale;
import java.util.Objects;

/**
 * Created by IntelliJ IDEA.
 * User: Azazello
 * Date: 06.06.18
 * Time: 17:11
 * To change this template use File | Settings | File Templates.
 */
public class BalanceSheetExcelHandler extends BaseExcelHandler implements AccountingConstants {
    @Autowired
    private AccountingService accountingService;
    @Autowired
    private UploadManager uploadManager;
    @Autowired
    private PropertManager propertManager;
    private String sheetName;
    @Autowired
    private CurrencyManager currencyManager;

    private LinkedList<ExcelData[]> list;
    private final int aCellSize = 60;
    private final int bCellSize = 25;

    @Override
    protected void setFileName() {
        filename = "Balance_Sheet_" + dateFormat(uploadManager.getUser().getUserDate());
    }

    @Override
    protected HSSFWorkbook getWorkBook(Object object) {
        EdsUser user = uploadManager.getUser();
        EdsFinancialSettings financialSettings = financialSettingsManager.getFinancialSettings();
        Integer calculationScale = getCalculationScale(financialSettings);
        String shortDateFormat = user.getCompany().getCompanySettings().getShortDateFormat();
        SimpleDateFormat format = new SimpleDateFormat(shortDateFormat != null ? shortDateFormat : "MMM dd yyyy", Locale.ENGLISH);

        ListingFilterParameter filterParametrs = (ListingFilterParameter) object;
        EdsProperty property = propertManager.findByCode(filterParametrs.getPropertyCode());
        sheetName = property != null ? property.getPlural() : commonLocalizer.localize(PdfLocalizationName.balanceSheet);
        Date startDate = parseFilterParameterDate(filterParametrs.getStartDateNC());
        Date endDate = parseFilterParameterDate(filterParametrs.getEndDateNC());

        //boolean totalNetAssetEnabled = false;
        list = new LinkedList<>();

        Integer currencyId = filterParametrs.getCurrencyID();
        EdsCurrency currency = currencyManager.getCurrency(currencyId);
        String currencySymbol = currency.getSymbol() != null ? currency.getSymbol() : "";
        String currencyCode = currency.getName();

        ExcelData td = ExcelData.getReportNameData("", 170, 4);
        td.setGroupCellBorder(true);
        list.add(new ExcelData[]{td});
        list.add(new ExcelData[]{ExcelData.getReportNameData(sheetName, 170, 4)});
        list.add(new ExcelData[]{ExcelData.getReportNameData(user.getCompany().getName(), 170, 4)});
        if (ServerUtils.getUserLocale().getLanguage().equals("uz")) {
            list.add(new ExcelData[]{ExcelData.getReportNameChildData(commonLocalizer.localize(PdfLocalizationName.asAt) + " " + ServerUtils.convertToUzbDateFormat(format.format(endDate)), 170, 4)});
        } else {
            list.add(new ExcelData[]{ExcelData.getReportNameChildData(commonLocalizer.localize(PdfLocalizationName.asAt) + " " + format.format(endDate), 170, 4)});
        }
//        list.add(new ExcelData[]{ExcelData.getReportNameChildData(commonLocalizer.localize(PdfLocalizationName.asAt) + " " + format.format(endDate), 170, 4)});
        list.add(new ExcelData[]{ExcelData.getReportNameChildData(commonLocalizer.localize(PdfLocalizationName.figuresIn) + " " + currencySymbol + "(" + currencyCode + ")", 170, 4)});
        list.add(new ExcelData[]{ExcelData.getReportNameData("", 170, 4)});

        if (filterParametrs.isShowBudget()) {
            BalanceSheetSummary sheetSummary = accountingService.getBalanceSheetSummary(new DateNonConvertable(startDate), new DateNonConvertable(endDate), filterParametrs.isActualDue(), filterParametrs.getDepartmentId(), filterParametrs.getProjectId(), currency.getObjectID());
            drawSummary(sheetSummary, calculationScale, filterParametrs.isZeroAvoided());
        } else {
            BalanceSheet sheet = accountingService.getBalanceSheet(new DateNonConvertable(startDate), new DateNonConvertable(endDate), filterParametrs.isActualDue(), filterParametrs.getDepartmentId(), filterParametrs.getProjectId(), currency.getObjectID());
            if (Objects.equals("86560", ServerSecurityContext.getInstance().getCompanyId())) {
                drawSheetForShipox(sheet, calculationScale, filterParametrs.isZeroAvoided());
            } else {
                drawSheet(sheet, calculationScale, filterParametrs.isZeroAvoided());
            }
        }
        WorkBook wb = new WorkBook(list);

        HSSFWorkbook hsswb = wb.getWorkBook(filename, 0, 0, 0, 3);
        HSSFSheet sheet = wb.getSheet();
        if (Objects.equals("86560", ServerSecurityContext.getInstance().getCompanyId())) {
            sheet.setDisplayGridlines(false);
        }
        sheet.getPrintSetup().setLandscape(true);
        // Set the columns to repeat from column 0 to 2 on the first sheet
        hsswb.setRepeatingRowsAndColumns(0, 0, 1, 0, 5);

        return hsswb;
    }

    private void drawSheet(BalanceSheet sheet, Integer calculationScale, boolean zeroAvoided) {
        String blank1 = "      ";
        String blank2 = "            ";
        for (BalancesheetSettingsItem setting : sheet.getSettings().getSettings()) {
            ExcelData[] header = new ExcelData[4];
            header[0] = new ExcelData(setting.getTitle().toUpperCase(), ExcelData.STRING, aCellSize, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL);
            header[0].setBold(true);
            header[1] = new ExcelData("", ExcelData.STRING, bCellSize, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL);
            list.add(header);
            for (BalancesheetSettingsItem item : setting.getItems()) {
                if (sheet.getItemByKey(item.getCode()) != null && sheet.getItemByKey(item.getCode()).getItems().length > 0) {
                    ExcelData[] header1 = new ExcelData[4];
                    header1[0] = new ExcelData(blank1 + sheet.getItemByKey(item.getCode()).getName(), ExcelData.STRING, aCellSize, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL);
                    header1[0].setBold(true);
                    header1[1] = new ExcelData("", ExcelData.STRING, bCellSize, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL);
                    list.add(header1);
                    for (BalanceSheetInnerItem innerItem : sheet.getItemByKey(item.getCode()).getItems()) {
                        ExcelData[] cellBody = new ExcelData[4];
                        if (zeroAvoided) {
                            if (innerItem.getValue() != null && BigDecimal.ZERO.compareTo(innerItem.getValue()) == 0) {
                                continue;
                            }
                        }
                        BigDecimal innerValue = innerItem.getValue();
                        cellBody[0] = new ExcelData(blank2 + (innerItem.getName() != null ? innerItem.getName() : ""), ExcelData.STRING, aCellSize, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL);
                        cellBody[1] = new ExcelData(innerValue != null ? (innerValue.setScale(calculationScale, BigDecimal.ROUND_HALF_UP).doubleValue()) : null, ExcelData.CURRENCY, bCellSize, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL);
                        list.add(cellBody);
                    }
                    ExcelData bodyDataItem = new ExcelData(blank1 + (sheet.getItemByKey(item.getCode()).getTotal().getName() != null ? sheet.getItemByKey(item.getCode()).getTotal().getName() : ""), ExcelData.STRING, 0, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL);
                    bodyDataItem.setBold(true);
                    ExcelData bodyDataValue = new ExcelData(sheet.getItemByKey(item.getCode()).getTotal().getValue() != null ? sheet.getItemByKey(item.getCode()).getTotal().getValue().setScale(calculationScale, BigDecimal.ROUND_HALF_UP) : ZERO, ExcelData.CURRENCY, 0, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL);
                    bodyDataValue.setBold(true);
                    ExcelData[] cellFooter = new ExcelData[4];
                    cellFooter[0] = bodyDataItem;
                    cellFooter[1] = bodyDataValue;
                    list.add(cellFooter);
                }
            }
            if (Constants.ASSETS.equals(setting.getCode())) {
                drawInnerTotal(sheet.getTotalAsset(), calculationScale);
            } else {
                drawInnerTotal(sheet.getTotalLiability(), calculationScale);
            }
        }
    }

    private void drawSheetForShipox(BalanceSheet sheet, Integer calculationScale, boolean zeroAvoided) {
        String blank1 = "      ";
        String blank2 = "            ";
        for (BalancesheetSettingsItem setting : sheet.getSettings().getSettings()) {
            ExcelData[] headerEmpty = new ExcelData[4];
            String assetsColor = "D6DCE4";
            short assetsBg = getColor(assetsColor);

            String nameChanges = setting.getTitle();
            if (Objects.equals("Equity and Liabilities", setting.getTitle()))
                nameChanges = "Liabilities";
            ExcelData header = new ExcelData(nameChanges.toUpperCase(), ExcelData.STRING, 0, false, true, ExcelData.NO_BORDER, ExcelData.HEADER);
            header.setMerged(true);
            header.setFromRow(0);
            header.setToRow(0);
            header.setFromCell(0);
            header.setToCell(3);
            header.setBgcolor(assetsBg);

            list.add(headerEmpty);
            list.add(new ExcelData[]{header});

            for (BalancesheetSettingsItem item : setting.getItems()) {
                if (sheet.getItemByKey(item.getCode()) != null && sheet.getItemByKey(item.getCode()).getItems().length > 0) {
                    ExcelData[] parentHeaderEmpty = new ExcelData[4];
                    String nameChange = "";
                    if (Objects.equals("Bank", sheet.getItemByKey(item.getCode()).getName())) {
                        nameChange = "Cash & Cash equivalents";
                    } else {
                        nameChange = sheet.getItemByKey(item.getCode()).getName();
                    }

                    list.add(parentHeaderEmpty);

                    ExcelData parentHeader = new ExcelData(blank2 + blank1 + nameChange, ExcelData.STRING, 0, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL);
                    parentHeader.setBold(true);

                    parentHeader.setMerged(true);
                    parentHeader.setFromRow(0);
                    parentHeader.setToRow(0);
                    parentHeader.setFromCell(0);
                    parentHeader.setToCell(3);

                    list.add(new ExcelData[]{parentHeader});
                    for (BalanceSheetInnerItem innerItem : sheet.getItemByKey(item.getCode()).getItems()) {
                        ExcelData[] cellBody = new ExcelData[4];
                        if (zeroAvoided) {
                            if (innerItem.getValue() != null && BigDecimal.ZERO.compareTo(innerItem.getValue()) == 0) {
                                continue;
                            }
                        }
                        BigDecimal innerValue = innerItem.getValue();
                        cellBody[0] = new ExcelData(innerItem.getCode(), ExcelData.STRING, 7, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL);
                        cellBody[1] = new ExcelData(blank2 + (innerItem.getName() != null ? innerItem.getName() : ""), ExcelData.STRING, aCellSize, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL);
                        cellBody[2] = new ExcelData(innerValue != null ? (innerValue.setScale(calculationScale, BigDecimal.ROUND_HALF_UP).doubleValue()) : null, ExcelData.CURRENCY, bCellSize, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL);
                        list.add(cellBody);
                    }
                    String totalBankNameChange = sheet.getItemByKey(item.getCode()).getTotal().getName();
                    if (Objects.equals("Total Bank", sheet.getItemByKey(item.getCode()).getTotal().getName())) {
                        totalBankNameChange = "Total Cash & Cash equivalents";
                    }
                    String totalColor = "D9D9D9";
                    short totalBg = getColor(totalColor);
                    ExcelData bodyDataItem = new ExcelData(blank2 + blank1 + totalBankNameChange, ExcelData.STRING, 0, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL);
                    bodyDataItem.setBold(true);
                    bodyDataItem.setBgcolor(totalBg);

                    bodyDataItem.setMerged(true);
                    bodyDataItem.setFromRow(0);
                    bodyDataItem.setToRow(0);
                    bodyDataItem.setFromCell(0);
                    bodyDataItem.setToCell(2);

                    ExcelData bodyDataValue = new ExcelData(sheet.getItemByKey(item.getCode()).getTotal().getValue() != null ? sheet.getItemByKey(item.getCode()).getTotal().getValue().setScale(calculationScale, BigDecimal.ROUND_HALF_UP) : ZERO, ExcelData.CURRENCY, 0, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL);
                    bodyDataValue.setBold(true);
                    bodyDataValue.setBgcolor(totalBg);

                    ExcelData[] cellFooter = new ExcelData[]{
                            bodyDataItem,
                            bodyDataValue,
                            bodyDataValue
                    };
                    list.add(cellFooter);
                }
            }
            if (Constants.ASSETS.equals(setting.getCode())) {
                drawInnerTotalForShipox(sheet.getTotalAsset(), calculationScale);
            } else {
                drawInnerTotalForShipox(sheet.getTotalLiability(), calculationScale);
            }
        }
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

    private void drawInnerTotalForShipox(BalanceSheetInnerItem total, Integer calculationScale) {
        String color = "BFBFBF";
        short totalBg = getColor(color);

        ExcelData bodyDataItem = new ExcelData(total.getName() != null ? total.getName() : "", ExcelData.STRING, 0, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL);
        bodyDataItem.setBold(true);
        bodyDataItem.setBgcolor(totalBg);

        bodyDataItem.setMerged(true);
        bodyDataItem.setFromRow(0);
        bodyDataItem.setToRow(0);
        bodyDataItem.setFromCell(0);
        bodyDataItem.setToCell(2);

        ExcelData bodyDataValue = new ExcelData(total.getValue() != null ? total.getValue().setScale(calculationScale, BigDecimal.ROUND_HALF_UP) : ZERO, ExcelData.CURRENCY, 0, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL);
        bodyDataValue.setBold(true);
        bodyDataValue.setBgcolor(totalBg);

        ExcelData[] cellFooter = new ExcelData[]{
                bodyDataItem,
                bodyDataValue,
                bodyDataValue
        };
        ExcelData[] cellFooterEmpty = new ExcelData[4];
        list.add(cellFooterEmpty);
        list.add(cellFooter);
    }

    private void drawSummary(BalanceSheetSummary sheetSummary, Integer calculationScale, boolean avoidZero) {
        String blank = "         ";
        if (sheetSummary.getAssets().getItems().length > 0) {
            ExcelData[] header = new ExcelData[4];
            header[0] = new ExcelData(commonLocalizer.localize(PdfLocalizationName.assets), ExcelData.STRING, aCellSize, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL);
            header[0].setBold(true);
            header[1] = new ExcelData("", ExcelData.STRING, bCellSize, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL);
            list.add(header);
            for (BalanceSheetInnerItem innerItem : sheetSummary.getAssets().getItems()) {
                ExcelData[] cellBody = new ExcelData[4];
                if (avoidZero) {
                    if (innerItem.getValue() != null && BigDecimal.ZERO.compareTo(innerItem.getValue()) == 0) {
                        continue;
                    }
                }
                BigDecimal innerValue = innerItem.getValue();
                cellBody[0] = new ExcelData(blank + (innerItem.getName() != null ? innerItem.getName() : ""), ExcelData.STRING, aCellSize, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL);
                cellBody[1] = new ExcelData(innerValue != null ? (innerValue.setScale(calculationScale, BigDecimal.ROUND_HALF_UP).doubleValue()) : null, ExcelData.CURRENCY, bCellSize, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL);
                list.add(cellBody);
            }
            drawInnerTotal(sheetSummary.getAssets().getTotal(), calculationScale);
        }
        if (sheetSummary.getLiabilities().getItems().length > 0) {
            int index = 6;
            ExcelData[] header = list.size() > index ? list.get(index) : new ExcelData[4];
            header[2] = new ExcelData(accountingLocalizer.localizeAccounting(PdfLocalizationName.equityLiabilities), ExcelData.STRING, aCellSize, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL);
            header[2].setBold(true);
            header[3] = new ExcelData("", ExcelData.STRING, aCellSize, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL);
            if (list.size() > index) {
                list.set(index++, header);
            } else {
                list.add(header);
            }
            for (BalanceSheetInnerItem innerItem : sheetSummary.getLiabilities().getItems()) {
                ExcelData[] cellBody = list.size() > index ? list.get(index) : new ExcelData[4];
                if (avoidZero) {
                    if (innerItem.getValue() != null && BigDecimal.ZERO.compareTo(innerItem.getValue()) == 0) {
                        continue;
                    }
                }
                BigDecimal innerValue = innerItem.getValue();
                cellBody[2] = new ExcelData(blank + (innerItem.getName() != null ? innerItem.getName() : ""), ExcelData.STRING, aCellSize, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL);
                cellBody[3] = new ExcelData(innerValue != null ? (innerValue.setScale(calculationScale, BigDecimal.ROUND_HALF_UP).doubleValue()) : null, ExcelData.CURRENCY, bCellSize, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL);
                if (list.size() > index) {
                    list.set(index++, cellBody);
                } else {
                    list.add(cellBody);
                }
            }
            ExcelData[] footer = list.size() > index ? list.get(index) : new ExcelData[4];
            ExcelData bodyDataItem = new ExcelData(sheetSummary.getLiabilities().getTotal().getName() != null ? sheetSummary.getLiabilities().getTotal().getName() : "", ExcelData.STRING, aCellSize, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL);
            bodyDataItem.setBold(true);
            BigDecimal totalValue = sheetSummary.getLiabilities().getTotal().getValue();
            ExcelData bodyDataValue = new ExcelData(totalValue != null ? totalValue.setScale(calculationScale, BigDecimal.ROUND_HALF_UP) : ZERO, ExcelData.CURRENCY, bCellSize, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL);
            bodyDataValue.setBold(true);
            footer[2] = bodyDataItem;
            footer[3] = bodyDataValue;
            if (list.size() > index) {
                list.set(index, footer);
            } else {
                list.add(footer);
            }
        }
    }

    private void drawInnerTotal(BalanceSheetInnerItem total, Integer calculationScale) {
        ExcelData bodyDataItem = new ExcelData(total.getName() != null ? total.getName() : "", ExcelData.STRING, 0, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL);
        bodyDataItem.setBold(true);
        ExcelData bodyDataValue = new ExcelData(total.getValue() != null ? total.getValue().setScale(calculationScale, BigDecimal.ROUND_HALF_UP) : ZERO, ExcelData.CURRENCY, 0, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL);
        bodyDataValue.setBold(true);
        ExcelData[] cellFooter = new ExcelData[4];
        cellFooter[0] = bodyDataItem;
        cellFooter[1] = bodyDataValue;
        list.add(cellFooter);
    }
}
