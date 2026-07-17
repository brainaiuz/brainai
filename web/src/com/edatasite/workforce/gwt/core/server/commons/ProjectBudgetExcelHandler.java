package com.edatasite.workforce.gwt.core.server.commons;

import com.edatasite.workforce.core.domain.accounting.EdsFinancialSettings;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.project.client.rpc.NewProjectBudgetCellItem;
import com.edatasite.workforce.gwt.project.client.rpc.NewProjectBudgetData;
import com.edatasite.workforce.gwt.project.client.rpc.NewProjectBudgetRowItem;
import com.edatasite.workforce.gwt.project.client.rpc.ProjectService;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.Font;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by Dilshod Madrahimov on 17-April-16.
 */
public class ProjectBudgetExcelHandler extends BaseExcelHandler {

    @Autowired
    private ProjectService projectService;

    HSSFWorkbook workbook;
    HSSFSheet sheet;
    ArrayList<Date[]> monthIntervalList;
    LinkedList<String> monthKeys;
    SimpleDateFormat dateFormat = new SimpleDateFormat("MMM yyyy");
    SimpleDateFormat keyDateFormat = new SimpleDateFormat("yyyy_MM");
    SimpleDateFormat shortMonthFormat = new SimpleDateFormat("MMM");
    SimpleDateFormat shortYearFormat = new SimpleDateFormat("yyyy");
    DecimalFormat priceScaleNumberFormat;
    LinkedHashMap<String, BigDecimal[]> totalAmountByCategoryMap;

    private CellStyle titleStyleCell = null;

    static String expenseAndCurrentAsset = "Expenses And Current Assets";
    static String revenue = "Revenue";
    static String purchases = "Purchases";

    int rowIndex = 0;
    int cellCount = 0;

    @Override
    protected boolean prepareRequest(HttpServletRequest request) {
        return false;
    }

    @Override
    protected void setFileName() {
        filename = "ProjectBudget";
    }

    protected HSSFWorkbook getWorkBook(Object object) {
        ListingFilterParameter filterParametr = (ListingFilterParameter) object;
        EdsFinancialSettings fs = financialSettingsManager.getFinancialSettings();
        priceScaleNumberFormat = getPriceScaleNumberFormat(fs);
        workbook = new HSSFWorkbook();
        titleStyleCell = workbook.createCellStyle();
        sheet = workbook.createSheet("Project Budget");
        sheet.setDefaultColumnWidth(20);
        sheet.autoSizeColumn(0);
        sheet.setColumnWidth(0, 10000);

        generateProjectBudget(filterParametr);

        return workbook;
    }

    private void generateProjectBudget(ListingFilterParameter filterParameter) {
        Date startDate = parseFilterParameterDate(filterParameter.getStartDateNC());
        Date endDate = parseFilterParameterDate(filterParameter.getEndDateNC());
        monthIntervalList = getMonthIntervalsList(startDate, endDate);
        cellCount = monthIntervalList.size() * 2 + 1 + 4;
        rowIndex = 0;
        monthKeys = new LinkedList<>();
        totalAmountByCategoryMap = new LinkedHashMap<>();
        for (Date[] monthInterval : monthIntervalList) {
            monthKeys.add(keyDateFormat.format(monthInterval[0]));
        }
        NewProjectBudgetData projectBudget = projectService.getNewProjectBudgetData(filterParameter.getObjectId(), new DateNonConvertable(startDate), new DateNonConvertable(endDate));

        createHeaderTable(projectBudget);

        drawDatePeriodTable();

        createItemTable("Revenue", projectBudget.getRevenues());

        int expensesCount = projectBudget.getExpenses() != null ? projectBudget.getExpenses().length + 1 : 1;
        NewProjectBudgetRowItem[] employeeCostAndExpenses = new NewProjectBudgetRowItem[expensesCount];
        employeeCostAndExpenses[0] = projectBudget.getEmployeeCost();
        if (projectBudget.getExpenses() != null && employeeCostAndExpenses != null) {
            System.arraycopy(projectBudget.getExpenses(), 0, employeeCostAndExpenses, 1, expensesCount - 1);
        }
        createItemTable(expenseAndCurrentAsset, employeeCostAndExpenses);

        if (projectBudget.isDetailedPurchasesEnabled()) {
            createItemTable(purchases, projectBudget.getDetailedPurchases());
        } else {
            createItemTable(purchases, new NewProjectBudgetRowItem[]{projectBudget.getPurchases()});
        }

        createFooterTable();
    }

    private void createHeaderTable(NewProjectBudgetData projectBudget) {
        int cellIndex = 2;
        HSSFRow row = generateOneRowWithEmptyCell(rowIndex, cellIndex);
        row.setHeight((short) 400);
        sheet.getRow(rowIndex).getCell(cellIndex).setCellValue(projectBudget.getProjectName() + " Budget Sheet");
        sheet.getRow(rowIndex).getCell(cellIndex).setCellStyle(getHeaderTitleStyleCell());
        if (projectBudget.getCustomerName() != null) {
            rowIndex++;
            row = generateOneRowWithEmptyCell(rowIndex, cellIndex);
            sheet.getRow(rowIndex).getCell(cellIndex).setCellValue("Assigned To " + projectBudget.getCustomerName());
            sheet.getRow(rowIndex).getCell(cellIndex).setCellStyle(getHeaderTitleStyleCell());
        }

        //header date period
        rowIndex++;
        row = generateOneRowWithEmptyCell(rowIndex, cellIndex);
        sheet.getRow(rowIndex).getCell(cellIndex).setCellValue("For Period " + dateFormat(monthIntervalList.get(0)[0]) + " - " + dateFormat(monthIntervalList.get(monthIntervalList.size() - 1)[1]));
        sheet.getRow(rowIndex).getCell(cellIndex).setCellStyle(getHeaderDateStyleCell());

        //Just empty row
        rowIndex++;
        row = generateOneRowWithEmptyCell(rowIndex, cellIndex);
    }

    private void drawDatePeriodTable() {
        int datePeriodInterval = monthIntervalList.size();
        rowIndex++;
        HSSFRow row = generateOneRowWithEmptyCell(rowIndex, cellCount);
        row.setHeight((short) 400);
        int cellIndex = 0;
        sheet.getRow(rowIndex).getCell(cellIndex).setCellValue("");
        cellIndex++;
        for (Date[] aMonthIntervalList : monthIntervalList) {
            sheet.getRow(rowIndex).getCell(cellIndex).setCellValue(shortMonthFormat.format(aMonthIntervalList[0]));
            sheet.getRow(rowIndex).getCell(cellIndex).setCellStyle(getGrayStyleCell(true));
            cellIndex++;
            sheet.getRow(rowIndex).getCell(cellIndex).setCellValue(shortYearFormat.format(aMonthIntervalList[1]));
            sheet.getRow(rowIndex).getCell(cellIndex).setCellStyle(getGrayStyleCell(false));
            cellIndex++;
        }
        sheet.getRow(rowIndex).getCell(cellIndex).setCellValue("");
        sheet.getRow(rowIndex).getCell(cellIndex).setCellStyle(getEmptyGreenStyleCell());

        sheet.getRow(rowIndex).getCell(cellIndex + 1).setCellValue("TOTAL");
        sheet.getRow(rowIndex).getCell(cellIndex + 1).setCellStyle(getGreenStyleCell());

        sheet.getRow(rowIndex).getCell(cellIndex + 2).setCellValue("");
        sheet.getRow(rowIndex).getCell(cellIndex + 2).setCellStyle(getEmptyGreenStyleCell());

        sheet.getRow(rowIndex).getCell(cellIndex + 3).setCellValue("% VARIANCE");
        sheet.getRow(rowIndex).getCell(cellIndex + 3).setCellStyle(getGreenStyleCell());

        //header second row
        cellIndex = 0;
        rowIndex++;
        row = generateOneRowWithEmptyCell(rowIndex, cellCount);
        row.setHeight((short) 400);
        sheet.getRow(rowIndex).getCell(cellIndex).setCellValue("");
        cellIndex++;
        for (int i = 0; i < datePeriodInterval; i++) {
            sheet.getRow(rowIndex).getCell(cellIndex).setCellValue("Budget");
            sheet.getRow(rowIndex).getCell(cellIndex).setCellStyle(getDarkBlueStyleCell());
            cellIndex++;

            sheet.getRow(rowIndex).getCell(cellIndex).setCellValue("Actual");
            sheet.getRow(rowIndex).getCell(cellIndex).setCellStyle(getDarkBlueStyleCell());
            cellIndex++;
        }
        sheet.getRow(rowIndex).getCell(cellIndex).setCellValue("Budget");
        sheet.getRow(rowIndex).getCell(cellIndex).setCellStyle(getDarkGreenStyleCell());

        sheet.getRow(rowIndex).getCell(cellIndex + 1).setCellValue("Actual");
        sheet.getRow(rowIndex).getCell(cellIndex + 1).setCellStyle(getDarkGreenStyleCell());

        sheet.getRow(rowIndex).getCell(cellIndex + 2).setCellValue("Difference");
        sheet.getRow(rowIndex).getCell(cellIndex + 2).setCellStyle(getDarkGreenStyleCell());

        sheet.getRow(rowIndex).getCell(cellIndex + 3).setCellValue("Difference (%)");
        sheet.getRow(rowIndex).getCell(cellIndex + 3).setCellStyle(getDarkGreenStyleCell());
    }

    private void createItemTable(String tableName, NewProjectBudgetRowItem[] items) {
        int cellIndex = 0;
        rowIndex++;
        HSSFRow row = generateOneRowWithEmptyCell(rowIndex, cellCount);
        row.setHeight((short) 400);
        //table title
        for (int i = 0; i < cellCount; i++) {
            if (i == 0) {
                sheet.getRow(rowIndex).getCell(i).setCellValue(tableName);
            }
            sheet.getRow(rowIndex).getCell(i).setCellStyle(getBlueStyleCell());
        }

        BigDecimal[] tableTotals = new BigDecimal[monthKeys.size() * 2 + 4];
        for (int i = 0; i < tableTotals.length; i++) {
            tableTotals[i] = BigDecimal.ZERO;
        }

        rowIndex++;
        if (items != null) {
            for (NewProjectBudgetRowItem rowItem : items) {
                row = generateOneRowWithEmptyCell(rowIndex, cellCount);
                String accountName = rowItem.getAccount().getName();
                Integer accountID = rowItem.getAccount().getId();
                if (accountID == -1) {
                    accountName = "Employee Cost";
                } else if (accountID == -2) {
                    accountName = "Purchases";
                }
                cellIndex = 0;
                sheet.getRow(rowIndex).getCell(cellIndex).setCellValue(accountName);
                sheet.getRow(rowIndex).getCell(cellIndex).setCellStyle(getBoldAlignmentStyleCell(false, HSSFCellStyle.ALIGN_LEFT));

                cellIndex++;
                LinkedHashMap<String, NewProjectBudgetCellItem> cellDataMap = rowItem.getCellDataMap();
                BigDecimal rowBudgetAmount = BigDecimal.ZERO, rowActualAmount = BigDecimal.ZERO;
                int totalColPosition = 0;
                for (String monthKey : monthKeys) {
                    //Budget
                    BigDecimal cellBudgetAmount = (cellDataMap.get(monthKey) != null && cellDataMap.get(monthKey).getBudget() != null) ? cellDataMap.get(monthKey).getBudget() : BigDecimal.ZERO;
                    cellBudgetAmount = cellBudgetAmount.setScale(priceScaleNumberFormat.getMaximumFractionDigits(), RoundingMode.HALF_UP);
                    sheet.getRow(rowIndex).getCell(cellIndex).setCellValue(priceScaleNumberFormat.format(cellBudgetAmount));
                    sheet.getRow(rowIndex).getCell(cellIndex).setCellStyle(getFloatValueStyleCell(false, HSSFCellStyle.ALIGN_RIGHT));

                    rowBudgetAmount = rowBudgetAmount.add(cellBudgetAmount);
                    tableTotals[totalColPosition] = tableTotals[totalColPosition].add(cellBudgetAmount);
                    totalColPosition++;
                    cellIndex++;

                    //Actual
                    BigDecimal cellActualAmount = (cellDataMap.get(monthKey) != null && cellDataMap.get(monthKey).getActual() != null) ? cellDataMap.get(monthKey).getActual() : BigDecimal.ZERO;
                    cellActualAmount = cellActualAmount.setScale(priceScaleNumberFormat.getMaximumFractionDigits(), RoundingMode.HALF_UP);
                    sheet.getRow(rowIndex).getCell(cellIndex).setCellValue(priceScaleNumberFormat.format(cellActualAmount));
                    sheet.getRow(rowIndex).getCell(cellIndex).setCellStyle(getFloatValueStyleCell(false, HSSFCellStyle.ALIGN_RIGHT));

                    rowActualAmount = rowActualAmount.add(cellActualAmount);
                    tableTotals[totalColPosition] = tableTotals[totalColPosition].add(cellActualAmount);
                    totalColPosition++;
                    cellIndex++;
                }

                //Total  Budget
                sheet.getRow(rowIndex).getCell(cellIndex).setCellValue(priceScaleNumberFormat.format(rowBudgetAmount));
                sheet.getRow(rowIndex).getCell(cellIndex).setCellStyle(getFloatValueStyleCell(false, HSSFCellStyle.ALIGN_RIGHT));
                tableTotals[totalColPosition] = tableTotals[totalColPosition].add(rowBudgetAmount);
                totalColPosition++;
                cellIndex++;

                //Total Actual
                sheet.getRow(rowIndex).getCell(cellIndex).setCellValue(priceScaleNumberFormat.format(rowActualAmount));
                sheet.getRow(rowIndex).getCell(cellIndex).setCellStyle(getFloatValueStyleCell(false, HSSFCellStyle.ALIGN_RIGHT));
                tableTotals[totalColPosition] = tableTotals[totalColPosition].add(rowActualAmount);
                totalColPosition++;
                cellIndex++;

                //Variance Difference
                BigDecimal rowDifference = rowActualAmount.subtract(rowBudgetAmount);
                sheet.getRow(rowIndex).getCell(cellIndex).setCellValue(priceScaleNumberFormat.format(rowDifference));
                sheet.getRow(rowIndex).getCell(cellIndex).setCellStyle(getFloatValueStyleCell(false, HSSFCellStyle.ALIGN_RIGHT));
                tableTotals[totalColPosition] = tableTotals[totalColPosition].add(rowDifference);
                totalColPosition++;
                cellIndex++;

                //Variance Difference % = rowDifference*100 / rowBudgetAmount
                BigDecimal rowDifferencePercentage = rowBudgetAmount.compareTo(BigDecimal.ZERO) != 0 ?
                        rowDifference.multiply(new BigDecimal(100)).divide(rowBudgetAmount, 2, RoundingMode.HALF_UP) : BigDecimal.ZERO;
                sheet.getRow(rowIndex).getCell(cellIndex).setCellValue(priceScaleNumberFormat.format(rowDifferencePercentage));
                sheet.getRow(rowIndex).getCell(cellIndex).setCellStyle(getFloatValueStyleCell(false, HSSFCellStyle.ALIGN_RIGHT));
                cellIndex++;

                rowIndex++;
            }
        }

        if (tableTotals[tableTotals.length - 4].compareTo(BigDecimal.ZERO) != 0) {
            tableTotals[tableTotals.length - 1] = tableTotals[tableTotals.length - 2].multiply(new BigDecimal(100))
                    .divide(tableTotals[tableTotals.length - 4], 2, RoundingMode.HALF_UP);
        }

        row = generateOneRowWithEmptyCell(rowIndex, cellCount);
        cellIndex = 0;
        sheet.getRow(rowIndex).getCell(cellIndex).setCellValue("Total " + tableName);
        sheet.getRow(rowIndex).getCell(cellIndex).setCellStyle(getBoldAlignmentStyleCell(true, HSSFCellStyle.ALIGN_LEFT));
        cellIndex++;
        for (int i = 1; i < cellCount; i++) {
            sheet.getRow(rowIndex).getCell(cellIndex).setCellValue(priceScaleNumberFormat.format(tableTotals[i - 1]));
            sheet.getRow(rowIndex).getCell(cellIndex).setCellStyle(getFloatValueStyleCell(true, HSSFCellStyle.ALIGN_RIGHT));
            cellIndex++;
        }

        totalAmountByCategoryMap.put(tableName, tableTotals);
    }

    private void createFooterTable() {

        BigDecimal[] profitTotals = new BigDecimal[monthKeys.size() * 2 + 4];
        for (int i = 0; i < profitTotals.length; i++) {
            profitTotals[i] = BigDecimal.ZERO;
        }
        BigDecimal[] revenueTotals = totalAmountByCategoryMap.get(revenue);
        BigDecimal[] expensesTotals = totalAmountByCategoryMap.get(expenseAndCurrentAsset);
        BigDecimal[] purchasesTotals = totalAmountByCategoryMap.get(purchases);

        for (int i = 0; i < profitTotals.length; i++) {
            profitTotals[i] = revenueTotals[i].subtract(expensesTotals[i]).subtract(purchasesTotals[i]);
        }
        if (profitTotals[profitTotals.length - 4].compareTo(BigDecimal.ZERO) != 0) {
            profitTotals[profitTotals.length - 1] = profitTotals[profitTotals.length - 2].multiply(new BigDecimal(100))
                    .divide(profitTotals[profitTotals.length - 4], 2, RoundingMode.HALF_UP);
        }

        rowIndex++;
        int cellIndex = 0;
        HSSFRow row = generateOneRowWithEmptyCell(rowIndex, cellCount);
        row.setHeight((short) 400);
        sheet.getRow(rowIndex).getCell(cellIndex).setCellValue("PROJECT PROFIT");

        CellStyle cellStyle = getProfitStyleCell();
        cellStyle.setAlignment(HSSFCellStyle.ALIGN_LEFT);
        sheet.getRow(rowIndex).getCell(cellIndex).setCellStyle(cellStyle);

        cellIndex++;
        for (int i = 1; i < cellCount; i++) {
            sheet.getRow(rowIndex).getCell(cellIndex).setCellValue(priceScaleNumberFormat.format(profitTotals[i - 1]));
            sheet.getRow(rowIndex).getCell(cellIndex).setCellStyle(getFloatValueStyleCell(false, HSSFCellStyle.ALIGN_RIGHT));
            cellIndex++;
        }
    }

    private HSSFRow generateOneRowWithEmptyCell(int rowNumber, int cells) {
        HSSFRow row = sheet.createRow(rowNumber);
        for (int i = 0; i <= cells; i++) {
            Cell cell = row.createCell(i);
        }
        return row;
    }

    private CellStyle getBoldAlignmentStyleCell(boolean isBold, short alignment) {
        CellStyle titleStyleCell = workbook.createCellStyle();
        titleStyleCell.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        titleStyleCell.setAlignment(alignment);
        if (isBold) {
            Font font = workbook.createFont();
            font.setBoldweight(Font.BOLDWEIGHT_BOLD);
            titleStyleCell.setFont(font);
        }
        return titleStyleCell;
    }

    private CellStyle getFloatValueStyleCell(boolean isBold, short alignment) {
        titleStyleCell.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        titleStyleCell.setAlignment(alignment);
        if (isBold) {
            Font font = workbook.createFont();
            font.setBoldweight(Font.BOLDWEIGHT_BOLD);
            titleStyleCell.setFont(font);
        }
        titleStyleCell.setDataFormat(HSSFDataFormat.getBuiltinFormat("0.00"));
        return titleStyleCell;
    }

    private CellStyle getHeaderTitleStyleCell() {
        CellStyle titleStyleCell = workbook.createCellStyle();
        titleStyleCell.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        titleStyleCell.setAlignment(HSSFCellStyle.ALIGN_CENTER);

        Font font = workbook.createFont();
        font.setBoldweight(Font.BOLDWEIGHT_BOLD);
        titleStyleCell.setFont(font);
        return titleStyleCell;
    }

    private CellStyle getHeaderDateStyleCell() {
        CellStyle dateStyleCell = workbook.createCellStyle();
        dateStyleCell.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        dateStyleCell.setAlignment(HSSFCellStyle.ALIGN_CENTER);

        Font font = workbook.createFont();
        font.setColor(HSSFColor.GREY_50_PERCENT.index);
        dateStyleCell.setFont(font);
        return dateStyleCell;
    }

    private CellStyle getGrayStyleCell(boolean odd) {
        CellStyle grayStyleCell = workbook.createCellStyle();
        grayStyleCell.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
        grayStyleCell.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        grayStyleCell.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        if (odd) {
            grayStyleCell.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
        } else {
            grayStyleCell.setAlignment(HSSFCellStyle.ALIGN_LEFT);
            grayStyleCell.setBorderRight(CellStyle.BORDER_THIN);
            grayStyleCell.setRightBorderColor(HSSFColor.WHITE.index);
        }

        Font font = workbook.createFont();
        grayStyleCell.setFont(font);
        return grayStyleCell;
    }


    private CellStyle getGreenStyleCell() {
        CellStyle greenStyleCell = workbook.createCellStyle();
        greenStyleCell.setFillForegroundColor(HSSFColor.LIGHT_GREEN.index);
        greenStyleCell.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        greenStyleCell.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        greenStyleCell.setAlignment(HSSFCellStyle.ALIGN_LEFT);
        greenStyleCell.setBorderRight(CellStyle.BORDER_THIN);
        greenStyleCell.setRightBorderColor(HSSFColor.WHITE.index);

        Font font = workbook.createFont();
        font.setBoldweight(Font.BOLDWEIGHT_BOLD);
        font.setColor(HSSFColor.DARK_GREEN.index);
        greenStyleCell.setFont(font);
        return greenStyleCell;
    }

    private CellStyle getEmptyGreenStyleCell() {
        CellStyle greenStyleCell = workbook.createCellStyle();
        greenStyleCell.setFillForegroundColor(HSSFColor.LIGHT_GREEN.index);
        greenStyleCell.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        return greenStyleCell;
    }

    private CellStyle getProfitStyleCell() {
        CellStyle greenStyleCell = workbook.createCellStyle();
        greenStyleCell.setFillForegroundColor(HSSFColor.LIGHT_GREEN.index);
        greenStyleCell.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        greenStyleCell.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        greenStyleCell.setAlignment(HSSFCellStyle.ALIGN_RIGHT);

        Font font = workbook.createFont();
        font.setBoldweight(Font.BOLDWEIGHT_BOLD);
        font.setColor(HSSFColor.GREEN.index);
        DataFormat format = workbook.createDataFormat();
        greenStyleCell.setDataFormat(HSSFDataFormat.getBuiltinFormat("0.00"));
        greenStyleCell.setFont(font);
        return greenStyleCell;
    }

    private CellStyle getDarkGreenStyleCell() {
        CellStyle greenStyleCell = workbook.createCellStyle();
        greenStyleCell.setFillForegroundColor(HSSFColor.DARK_GREEN.index);
        greenStyleCell.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        greenStyleCell.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        greenStyleCell.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        greenStyleCell.setBorderRight(CellStyle.BORDER_THIN);
        greenStyleCell.setRightBorderColor(HSSFColor.WHITE.index);

        Font font = workbook.createFont();
        font.setColor(HSSFColor.WHITE.index);
        greenStyleCell.setFont(font);
        return greenStyleCell;
    }

    private CellStyle getBlueStyleCell() {
        CellStyle blueStyleCell = workbook.createCellStyle();
        blueStyleCell.setFillForegroundColor(HSSFColor.ROYAL_BLUE.index);
        blueStyleCell.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        blueStyleCell.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        blueStyleCell.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        blueStyleCell.setBorderRight(CellStyle.BORDER_THIN);
        blueStyleCell.setRightBorderColor(HSSFColor.BLACK.index);

        Font font = workbook.createFont();
        font.setBoldweight(Font.BOLDWEIGHT_BOLD);
        font.setColor(HSSFColor.WHITE.index);
        blueStyleCell.setFont(font);
        return blueStyleCell;
    }

    private CellStyle getDarkBlueStyleCell() {
        CellStyle darkBlueStyleCell = workbook.createCellStyle();
        darkBlueStyleCell.setFillForegroundColor(HSSFColor.DARK_BLUE.index);
        darkBlueStyleCell.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        darkBlueStyleCell.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        darkBlueStyleCell.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        darkBlueStyleCell.setBorderRight(CellStyle.BORDER_THIN);
        darkBlueStyleCell.setRightBorderColor(HSSFColor.WHITE.index);

        Font font = workbook.createFont();
        font.setColor(HSSFColor.WHITE.index);
        darkBlueStyleCell.setFont(font);
        return darkBlueStyleCell;
    }

    private ArrayList<Date[]> getMonthIntervalsList(Date startDate, Date endDate) {
        Calendar startDateCal = new GregorianCalendar();
        startDateCal.setTime(startDate);

        Calendar endDateCal = new GregorianCalendar();
        endDateCal.setTime(endDate);

        startDateCal.set(Calendar.DATE, 1);
        endDateCal.set(Calendar.DATE, endDateCal.getActualMaximum(Calendar.DATE));
        ServerUtils.setBeginningOfTheDay(startDateCal);
        ServerUtils.setEndOfTheDay(endDateCal);

        ArrayList<Date[]> monthIntervalList = new ArrayList<>();

        while (startDateCal.getTime().before(endDateCal.getTime())) {
            GregorianCalendar monthEndCal = new GregorianCalendar();
            monthEndCal.setTime(startDateCal.getTime());
            monthEndCal.set(Calendar.DATE, monthEndCal.getActualMaximum(Calendar.DATE));
            ServerUtils.setEndOfTheDay(monthEndCal);

            Date[] dates = new Date[2];
            dates[0] = startDateCal.getTime();
            dates[1] = monthEndCal.getTime();
            monthIntervalList.add(dates);

            startDateCal.set(Calendar.MONTH, startDateCal.get(Calendar.MONTH) + 1);
        }

        return monthIntervalList;
    }


}
