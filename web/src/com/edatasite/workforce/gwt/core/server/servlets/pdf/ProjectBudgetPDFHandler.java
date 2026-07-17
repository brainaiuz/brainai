package com.edatasite.workforce.gwt.core.server.servlets.pdf;

import com.edatasite.workforce.core.domain.EdsCompany;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.accounting.EdsFinancialSettings;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.ITextFontTypeEnum;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.ITextPdfViewTypeEnum;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.CellData;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.ITextGenericPdfData;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.ITextSummaryView;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.ITextTableList;
import com.edatasite.workforce.gwt.project.client.rpc.NewProjectBudgetCellItem;
import com.edatasite.workforce.gwt.project.client.rpc.NewProjectBudgetData;
import com.edatasite.workforce.gwt.project.client.rpc.NewProjectBudgetRowItem;
import com.edatasite.workforce.gwt.project.client.rpc.ProjectService;
import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.PageSize;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfWriter;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import java.awt.*;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Atabek Boboyev
 * Date: 30.05.12
 * Time: 15:29
 * To change this template use File | Settings | File Templates.
 */
public class ProjectBudgetPDFHandler extends AbstractITextPostPdfHandler {
    private final SimpleDateFormat keyDateFormat = new SimpleDateFormat("yyyy_MM");
    private ArrayList<Date[]> monthIntervalList;

    @Autowired
    private ProjectService projectService;


    @Override
    public ITextGenericPdfData buildPdfDocument(Object dataClass, Document document, PdfWriter writer) throws IOException {

        return null;
    }

    @Override
    protected ITextGenericPdfData buildPdfDocumentCustomise(Object dataClass, EdsCompany company, boolean hasPhantom) {
        ListingFilterParameter filterParameter = (ListingFilterParameter) dataClass;
        ITextGenericPdfData pdfData = new ITextGenericPdfData();

        Date startDate = parseFilterParameterDate(filterParameter.getStartDateNC());
        Date endDate = parseFilterParameterDate(filterParameter.getEndDateNC());

        NewProjectBudgetData budgetData = projectService.getNewProjectBudgetData(filterParameter.getObjectId(), new DateNonConvertable(startDate), new DateNonConvertable(endDate));
        SimpleDateFormat headesAsMonthDateFormat = new SimpleDateFormat("MMM yyyy");
        ITextTableList monthHeaderTable = createMonthHeaderTable(monthIntervalList, headesAsMonthDateFormat);
        ITextTableList monthBudgetActualHeaderTable = createMonthBudgetActualHeaderTable(monthIntervalList);
        EdsFinancialSettings fs = financialSettingsManager.getFinancialSettings();
        DecimalFormat priceScaleNumberFormat = getPriceScaleNumberFormat(fs);

        LinkedList<String> monthKeys = new LinkedList<>();
        for (Date[] monthInterval : monthIntervalList) {
            monthKeys.add(keyDateFormat.format(monthInterval[0]));
        }

        pdfData.setPdfViewType(ITextPdfViewTypeEnum.SUMMARYVIEW);
        ITextSummaryView summaryView = new ITextSummaryView();

        pdfData.setTableName(budgetData.getProjectName());
        monthHeaderTable.addTableWidthPercentage(0.16f, 0.36f, 0.30f, 0.30f);
        monthBudgetActualHeaderTable.addTableWidthPercentage(0.05f, 0.09f, 0.14f, 0.14f, 0.14f, 0.14f, 0.14f, 0.14f);
        summaryView.addTable(monthHeaderTable);
        summaryView.addTable(monthBudgetActualHeaderTable);

//        ProjectBudgetTableData revenueTable = createTable("Revenue", monthKeys, budgetData.getRevenues(), priceScaleNumberFormat);
//        summaryView.addTable(revenueTable.getTableData());

        int expensesCount = budgetData.getExpenses() != null ? budgetData.getExpenses().length + 1 : 1;
        NewProjectBudgetRowItem[] employeeCostAndExpenses = new NewProjectBudgetRowItem[expensesCount];
        employeeCostAndExpenses[0] = budgetData.getEmployeeCost();
        if (budgetData.getExpenses() != null && employeeCostAndExpenses != null) {
            System.arraycopy(budgetData.getExpenses(), 0, employeeCostAndExpenses, 1, expensesCount - 1);
        }
        ProjectBudgetTableData expensesTable = createTable("Expenses And Current Assets", monthKeys, employeeCostAndExpenses, priceScaleNumberFormat);
        summaryView.addTable(expensesTable.getTableData());
        ProjectBudgetTableData purchasesTable;
        if (budgetData.isDetailedPurchasesEnabled()) {
            purchasesTable = createTable("Purchases", monthKeys, budgetData.getDetailedPurchases(), priceScaleNumberFormat);
        } else {
            purchasesTable = createTable("Purchases", monthKeys, new NewProjectBudgetRowItem[]{budgetData.getPurchases()}, priceScaleNumberFormat);
        }
        summaryView.addTable(purchasesTable.getTableData());

        BigDecimal[] profitTotals = new BigDecimal[monthKeys.size() * 2 + 4];
        for (int i = 0; i < profitTotals.length; i++) {
            profitTotals[i] = BigDecimal.ZERO;
        }
//        BigDecimal[] revenueTotals = revenueTable.getTotals();
        BigDecimal[] expensesTotals = expensesTable.getTotals();
        BigDecimal[] purchasesTotals = purchasesTable.getTotals();
        for (int i = 0; i < profitTotals.length; i++) {
//            profitTotals[i] = revenueTotals[i].subtract(expensesTotals[i]).subtract(purchasesTotals[i]);
        }

        if (profitTotals[profitTotals.length - 4].compareTo(BigDecimal.ZERO) != 0) {
            profitTotals[profitTotals.length - 1] = profitTotals[profitTotals.length - 2].multiply(new BigDecimal(100))
                    .divide(profitTotals[profitTotals.length - 4], 2, BigDecimal.ROUND_HALF_UP);
        }

        summaryView.addTable(ProjectProfitTable("PROJECT PROFIT", monthKeys, profitTotals, priceScaleNumberFormat));
        pdfData.setSummaryView(summaryView);
        return pdfData;
    }

    private ITextTableList createMonthHeaderTable(ArrayList<Date[]> monthIntervalList, SimpleDateFormat headesAsMonthDateFormat) {
        ITextTableList tableList = new ITextTableList(monthIntervalList.size() + 3);
        CellData[] cells = new CellData[monthIntervalList.size() + 3];
        int i = 0;
        cells[i] = new CellData("");
        cells[i].setAlignment(Element.ALIGN_CENTER);
        cells[i].setBgColor(new Color(255, 255, 255));
        cells[i].setBorderColor(new Color(255, 255, 255));
        i++;
        for (Date[] monthInterval : monthIntervalList) {
            cells[i] = new CellData(headesAsMonthDateFormat.format(monthInterval[0]));

            cells[i].setAlignment(Element.ALIGN_CENTER);
            cells[i].setBgColor(new Color(240, 240, 240));
            cells[i].setBorderColor(new Color(255, 255, 255));
            i++;

        }

        cells[i] = new CellData("TOTAL");
        cells[i + 1] = new CellData("VARIANCE");

        cells[i].setAlignment(Element.ALIGN_CENTER);
        cells[i + 1].setAlignment(Element.ALIGN_CENTER);


        cells[i].setBgColor(new Color(208, 237, 204));
        cells[i + 1].setBgColor(new Color(208, 237, 204));


        cells[i].setBorderColor(new Color(255, 255, 255));
        cells[i + 1].setBorderColor(new Color(255, 255, 255));
        tableList.addPdfTableRows(cells);

        return tableList;
    }

    private ITextTableList createMonthBudgetActualHeaderTable(ArrayList<Date[]> monthIntervalList) {
        ITextTableList tableList = new ITextTableList(monthIntervalList.size() * 2 + 6);
        CellData[] cells = new CellData[monthIntervalList.size() * 2 + 6];
        cells[0] = new CellData("");
        cells[0].setBgColor(new Color(255, 255, 255));
        cells[0].setBorderColor(new Color(255, 255, 255));

        cells[1] = new CellData("");
        cells[1].setBgColor(new Color(255, 255, 255));
        cells[1].setBorderColor(new Color(255, 255, 255));

        for (int i = 1; i < monthIntervalList.size() + 1; i++) {
            cells[2 * i] = new CellData("Budget");
            cells[2 * i + 1] = new CellData("Actual");

            cells[i * 2].setBgColor(new Color(36, 79, 109));
            cells[i * 2 + 1].setBgColor(new Color(36, 79, 109));
            cells[i * 2].setAlignment(Element.ALIGN_CENTER);
            cells[i * 2 + 1].setAlignment(Element.ALIGN_CENTER);
            cells[i * 2].setFont(createFont(8, false, Color.WHITE));
            cells[i * 2 + 1].setFont(createFont(8, false, Color.WHITE));
            cells[i * 2].setBorderColor(new Color(255, 255, 255));
            cells[i * 2 + 1].setBorderColor(new Color(255, 255, 255));
        }

        int i = monthIntervalList.size() + 1;
        //Total
        cells[i * 2] = new CellData("Budget");
        cells[i * 2 + 1] = new CellData("Actual");

        //Variance
        cells[(i + 1) * 2] = new CellData("Difference");
        cells[(i + 1) * 2 + 1] = new CellData("Difference %");

        //Background Color
        cells[i * 2].setBgColor(new Color(62, 133, 31));
        cells[i * 2 + 1].setBgColor(new Color(62, 133, 31));
        cells[(i + 1) * 2].setBgColor(new Color(62, 133, 31));
        cells[(i + 1) * 2 + 1].setBgColor(new Color(62, 133, 31));
        //Center
        cells[i * 2].setAlignment(Element.ALIGN_CENTER);
        cells[i * 2 + 1].setAlignment(Element.ALIGN_CENTER);
        cells[(i + 1) * 2].setAlignment(Element.ALIGN_CENTER);
        cells[(i + 1) * 2 + 1].setAlignment(Element.ALIGN_CENTER);
        //Text Color
        cells[i * 2].setFont(createFont(8, false, Color.WHITE));
        cells[i * 2 + 1].setFont(createFont(8, false, Color.WHITE));
        cells[(i + 1) * 2].setFont(createFont(8, false, Color.WHITE));
        cells[(i + 1) * 2 + 1].setFont(createFont(8, false, Color.WHITE));
        //Border Color
        cells[i * 2].setBorderColor(new Color(255, 255, 255));
        cells[i * 2 + 1].setBorderColor(new Color(255, 255, 255));
        cells[(i + 1) * 2].setBorderColor(new Color(255, 255, 255));
        cells[(i + 1) * 2 + 1].setBorderColor(new Color(255, 255, 255));

        tableList.addPdfTableRows(cells);
        return tableList;
    }

    private ITextTableList ProjectProfitTable(String tableName, List<String> monthKeys, BigDecimal[] total, DecimalFormat priceScaleNumberFormat) {
        Integer columnCount = monthKeys.size() * 2 + 6;
        CellData[] cells = new CellData[columnCount];
        ITextTableList tableList = new ITextTableList(columnCount);
        cells[0] = toEmptyCell(new CellData(""));
        cells[1] = new CellData(tableName);
        for (int i = 2; i < columnCount; i++) {
            cells[i] = new CellData(priceScaleNumberFormat.format(total[i - 2]));
            cells[i].setAlignment(Element.ALIGN_RIGHT);
            cells[i].setBgColor(new Color(208, 237, 204));
            cells[i].setFont(createFont(8, false, Color.BLACK));
        }

        tableList.addPdfTableRows(cells);

        return tableList;
    }

    @Override
    protected String getTableName(Object dataClass) {
        ListingFilterParameter filterParameter = (ListingFilterParameter) dataClass;
        Date startDate = parseFilterParameterDate(filterParameter.getStartDateNC());
        Date endDate = parseFilterParameterDate(filterParameter.getEndDateNC());
        NewProjectBudgetData budgetData = projectService.getNewProjectBudgetData(filterParameter.getObjectId(), new DateNonConvertable(startDate), new DateNonConvertable(endDate));
        return "Project Budget -" + budgetData.getProjectName();
    }

    private ProjectBudgetTableData createTable(String tableName, List<String> monthKeys, NewProjectBudgetRowItem[]
            rowItems, DecimalFormat priceScaleNumberFormat) {
        Integer columnCount = monthKeys.size() * 2 + 6;
        CellData[] cells = new CellData[columnCount];
        ITextTableList tableList = new ITextTableList(columnCount);
        cells[0] = toEmptyCell(new CellData(""));
        for (int i = 1; i < columnCount; i++) {
            if (i == 1) {
                cells[i] = new CellData(tableName);
            } else {
                cells[i] = new CellData("");
            }
            cells[i].setBgColor(new Color(18, 134, 195));
            cells[i].setFont(createFont(8, false, Color.WHITE));
        }

        tableList.addPdfTableHeader(cells);

        ArrayList<CellData> cellsBody = null;
        ArrayList<CellData[]> tableCellsData = new ArrayList<>();
        BigDecimal[] tableTotals = new BigDecimal[monthKeys.size() * 2 + 4];
        for (int i = 0; i < tableTotals.length; i++) {
            tableTotals[i] = BigDecimal.ZERO;
        }
        if (rowItems != null && rowItems.length > 0) {
            for (NewProjectBudgetRowItem rowItem : rowItems) {
                cellsBody = new ArrayList<>();
                cellsBody.add(toEmptyCell(new CellData("")));
                Integer accountID = rowItem.getAccount().getId();
                if (accountID == -1) {
                    cellsBody.add(new CellData("Employee Cost"));
                } else if (accountID == -2) {
                    cellsBody.add(new CellData("Purchases"));
                } else {
                    cellsBody.add(new CellData(rowItem.getAccount().getName()));
                }

                LinkedHashMap<String, NewProjectBudgetCellItem> cellDataMap = rowItem.getCellDataMap();

                int totalColPosition = 0;
                BigDecimal rowBudgetAmount = BigDecimal.ZERO, rowActualAmount = BigDecimal.ZERO;
                for (String monthKey : monthKeys) {
                    //Budget
                    BigDecimal cellBudgetAmount = (cellDataMap.get(monthKey) != null && cellDataMap.get(monthKey).getBudget() != null) ? cellDataMap.get(monthKey).getBudget() : BigDecimal.ZERO;
                    cellBudgetAmount = cellBudgetAmount.setScale(priceScaleNumberFormat.getMaximumFractionDigits(), BigDecimal.ROUND_HALF_UP);
                    rowBudgetAmount = rowBudgetAmount.add(cellBudgetAmount);
                    tableTotals[totalColPosition] = tableTotals[totalColPosition].add(cellBudgetAmount);
                    totalColPosition++;
                    cellsBody.add(new CellData(priceScaleNumberFormat.format(cellBudgetAmount), Element.ALIGN_RIGHT));
                    //Actual
                    BigDecimal cellActualAmount = (cellDataMap.get(monthKey) != null && cellDataMap.get(monthKey).getActual() != null) ? cellDataMap.get(monthKey).getActual() : BigDecimal.ZERO;
                    cellActualAmount = cellActualAmount.setScale(priceScaleNumberFormat.getMaximumFractionDigits());
                    rowActualAmount = rowActualAmount.add(cellActualAmount);
                    tableTotals[totalColPosition] = tableTotals[totalColPosition].add(cellActualAmount);
                    totalColPosition++;
                    cellsBody.add(new CellData(priceScaleNumberFormat.format(cellActualAmount), Element.ALIGN_RIGHT));
                }

                //Total  Budget
                cellsBody.add(new CellData(priceScaleNumberFormat.format(rowBudgetAmount), Element.ALIGN_RIGHT));
                tableTotals[totalColPosition] = tableTotals[totalColPosition].add(rowBudgetAmount);
                totalColPosition++;
                //Total Actual
                cellsBody.add(new CellData(priceScaleNumberFormat.format(rowActualAmount), Element.ALIGN_RIGHT));
                tableTotals[totalColPosition] = tableTotals[totalColPosition].add(rowActualAmount);
                totalColPosition++;

                //VARIANCE  Difference
                BigDecimal rowDifference = rowActualAmount.subtract(rowBudgetAmount);
                cellsBody.add(new CellData(priceScaleNumberFormat.format(rowDifference), Element.ALIGN_RIGHT));
                tableTotals[totalColPosition] = tableTotals[totalColPosition].add(rowDifference);
                totalColPosition++;


                //VARIANCE Difference % = rowDifference*100 / rowBudgetAmount
                BigDecimal rowDifferencePercentage = rowBudgetAmount.compareTo(BigDecimal.ZERO) != 0 ?
                        rowDifference.multiply(new BigDecimal(100)).divide(rowBudgetAmount, 2, BigDecimal.ROUND_HALF_UP) : BigDecimal.ZERO;
                cellsBody.add(new CellData(priceScaleNumberFormat.format(rowDifferencePercentage), Element.ALIGN_RIGHT));

                tableList.addPdfTableRows(cellsBody.toArray(new CellData[]{}));
                tableCellsData.add(cellsBody.toArray(new CellData[]{}));
            }
        }

        if (tableTotals[tableTotals.length - 4].compareTo(BigDecimal.ZERO) != 0) {
            tableTotals[tableTotals.length - 1] = tableTotals[tableTotals.length - 2].multiply(new BigDecimal(100))
                    .divide(tableTotals[tableTotals.length - 4], 2, BigDecimal.ROUND_HALF_UP);
        }

        CellData[] cellsTotal = new CellData[columnCount];
        cellsTotal[0] = toEmptyCell(new CellData(""));
        cellsTotal[1] = new CellData("Total " + tableName);
        for (int i = 2; i < columnCount; i++) {
            cellsTotal[i] = new CellData(priceScaleNumberFormat.format(tableTotals[i - 2]));
            cellsTotal[i].setAlignment(Element.ALIGN_RIGHT);
            cellsTotal[i].setFont(createFont(10, true, Color.BLACK));
        }
        tableList.addPdfTableRows(cellsTotal);

        return new ProjectBudgetTableData(tableTotals, tableList);
    }

    private CellData toEmptyCell(CellData cellData) {
        cellData.setBorderColor(new Color(255, 255, 255));
        cellData.setBgColor(new Color(255, 255, 255));
        return cellData;
    }

    private Font createFont(Integer fontSize, boolean bold, Color color) {
        return FontFactory.getFont(ITextFontTypeEnum.TIMES_NEW_ROMAN.getName(), BaseFont.IDENTITY_H, false, fontSize, bold ? Font.BOLD : Font.NORMAL, color);
    }

    @Override
    protected Object getDataClass(HttpServletRequest request) {
        monthIntervalList = getMonthIntervalsList(parseFilterParameterDate(request.getParameter("startDate_nc")), parseFilterParameterDate(request.getParameter("endDate_nc")));
        return super.getDataClass(request);
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

    @Override
    protected PdfReferenceCodeNameEnum getPdfCodeName(Object dataClass) {
        return PdfReferenceCodeNameEnum.BUDGET_BETA;
    }

    @Override
    protected void setFileName(EdsUser user, Object dataClass) {
        setFileName("ProjectBudget_" + dateFormat(user.getUserDate()));
    }

    @Override
    protected Document newDocument(EdsCompany edsCompany, Object dataClass) {
        if (monthIntervalList.size() > 1) {
            int pageWidth = monthIntervalList.size() * 85;
            Rectangle pagesize = new Rectangle(595 + pageWidth, 842);
            return new Document(pagesize, 20, 20, 120, 50);
        } else {
            return new Document(PageSize.A4, 20, 20, 120, 50);
        }
    }

    public static class ProjectBudgetTableData {
        private final BigDecimal[] totals;
        private final ITextTableList tableData;

        public ProjectBudgetTableData(BigDecimal[] totals, ITextTableList tableData) {
            this.totals = totals;
            this.tableData = tableData;
        }

        public BigDecimal[] getTotals() {
            return totals;
        }

        public ITextTableList getTableData() {
            return tableData;
        }
    }
}
