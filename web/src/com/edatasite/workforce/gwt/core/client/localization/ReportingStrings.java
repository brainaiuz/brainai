package com.edatasite.workforce.gwt.core.client.localization;

import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.Constants;

/**
 * Created by Ozodbek on 1/4/2018.
 */
public interface ReportingStrings extends Constants {
    String addAsDashboard();
    String addMoreSeries();
    String addReport();
    String advancedFilter();
    String areaChartTitle();
    String areYouSureWantoDeleteThisReport();
    String areYouSureWantoDeleteThisReportAndWidget();
    String basicKpi();
    String before3Weeks();
    String beforeThisMonth();
    String beforeThisWeek();
    String benchmarkforyaxis();
    String bySeries();
    String chartCategoryAndSerieRequired();
    String chooseDate();
    String comparisonFilter();
    String comparisonText();
    String conditionalFormating();
    String connectYouData();
    String createChart();
    String createWidget();
    String currentlyYouDontHaveData();
    String cylinderStacking();
    String dimensions();
    String DonutChartTitle();
    String drillDown();
    String errorOcuredPleaseTryAgain();
    String errorOcuredPleaseTryAgainOrRefreshYouBrouzer();
    String favourite();
    String folderAlreadyExists();
    String funnelChartTitle();
    String grouping();
    String growthKpi();
    String hideDetails();
    String horizontalBarChartTitle();
    String increaseColor();
    String kpiWidgetTitleAndSerieRequired();
    String last1Hours();
    String last2Hours();
    String last2Years();
    String last3Hours();
    String last3Weeks();
    String lineChartTitle();
    String lineStacking();
    String low();
    String makeFavourite();
    String moduleType();
    String newReport();
    String nextQuarter();
    String nextYear();
    String noChartTitle();
    String noWidgetTitle();
    String PieChartTitle();
    String rankingKpi();
    String removeFromFavourites();
    String removeRow();
    String removeSerie();
    String reportType();
    String runReport();
    String saveAs();
    String saveCustomReport();
    String scheduleReport();
    String searchEmptyMessage();
    String searchFolderDescription();
    String searchResults();
    String selecrReportingGrouping();
    String selectColumnToIncludeInYourReport();
    String selectedColumns();
    String selectGrouping();
    String selectReportColumns();
    String selectReportFilter();
    String selectReportSummarize();
    String selectReportType();
    String selectTheTypeOfReportToCreate();
    String semiCircleDonutChartTitle();
    String serieName();
    String shareResults();
    String showItems();
    String showLabel();
    String showLegend();
    String showSerie();
    String stacked();
    String summaries();
    String summaryReport();
    String summaryReportsListYourDataInformation();
    String tabularReport();
    String tabularReportsInfo();
    String thisAndLastMonth();
    String thisAndLastQuarter();
    String thisAndLastTwoYears();
    String thisAndLastYear();
    String thisAndNextMonth();
    String thisAndNextQuarter();
    String thisAndNextYear();
    String twoYearsAgo();
    String verticalBarChartTitle();
    String widgetMetric();
    String widgetTitle();


    class App {
        private static ReportingStrings instance;

        public static ReportingStrings get() {
            if (instance == null) {
                instance = GWT.create(ReportingStrings.class);
            }
            return instance;
        }
    }
}
