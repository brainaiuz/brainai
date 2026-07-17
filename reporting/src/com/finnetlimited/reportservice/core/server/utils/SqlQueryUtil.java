package com.finnetlimited.reportservice.core.server.utils;

import com.edatasite.workforce.gwt.chart.client.enums.ChartTypeEnum;
import com.edatasite.workforce.gwt.chart.client.enums.SerieAggrTypeEnum;
import com.edatasite.workforce.gwt.chart.client.rpc.ChartConfItem;
import com.edatasite.workforce.gwt.chart.client.rpc.GaugeChartConfig;
import com.edatasite.workforce.gwt.chart.client.rpc.SerieColumn;
import com.edatasite.workforce.gwt.chart.client.rpc.SerieConfItem;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.reportingsystem.client.enumerable.DateRangeType;
import com.edatasite.workforce.gwt.reportingsystem.client.enumerable.DurationType;
import com.edatasite.workforce.gwt.reportingsystem.client.enumerable.OperationType;
import com.edatasite.workforce.gwt.reportingsystem.client.enumerable.ReportType;
import com.edatasite.workforce.gwt.reportingsystem.client.enumerable.SqlColumnType;
import com.edatasite.workforce.gwt.reportingsystem.client.rpc.ColumnRpc;
import com.edatasite.workforce.gwt.reportingsystem.client.rpc.PatternUtils;
import com.edatasite.workforce.gwt.reportingsystem.client.rpc.ReportRpc;
import com.edatasite.workforce.gwt.reportingsystem.client.rpc.ReportingCustomizeFilter;
import com.edatasite.workforce.gwt.reportingsystem.client.rpc.ReportingRolePermissionItem;
import com.finnetlimited.reportservice.core.client.gwtrpc.ViewRpc;
import com.finnetlimited.reportservice.core.server.parser.XmlParser;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import static com.edatasite.workforce.gwt.reportingsystem.client.enumerable.DurationType.SamePeriodLastYear;

/**
 * User: ${Dilsh0d}
 * Date: 03-Apr-2010
 * Time: 13:07:12
 */
public final class SqlQueryUtil {

    public static final String ColumnFormat_WITHOUT_TIME_ZONE_AND_TIME = "without_time_zone_and_time";
    public static final String ColumnFormat_WITHOUT_TIME_ZONE = "without_time_zone";
    public static final String ColumnFormat_DATE_WITHOUT_TIME_ZONE = "date_without_time_zone";
    public static final String ColumnFormat_DATE_WITHOUT_TIME = "date_without_time";
    public static final String ColumnFormat_DATE = "date";
    public static final String ColumnFormat_PHONE = "phone";
    public static final String ColumnFormat_MONEY = "money";
    public static final String ColumnFormat_STRING = "string";
    public static final String ColumnFormat_ROUNDED = "rounded";
    public static final String ColumnFormat_TIME = "time";
    public static final String ColumnFormat_NUMBER = "number";
    public static final String ColumnFormat_DOUBLE = "double";
    public static final String ColumnFormat_PERCENT = "percent";
    public static final String ColumnFormat_IMAGE = "image";

    public static ViewRpc getViewParser(String viewCode) {
        return new XmlParser().getViewStructure(viewCode);
    }

    public static String getTotalCountQuery(Integer companyId, ReportRpc reportRpc, ViewRpc viewRpc) {
        if (reportRpc.getGroupColumns() == null || reportRpc.getGroupColumns().size() == 0) {
            reportRpc.setTableType(ReportType.TABULAR.name());
        }
        StringBuilder reportQuery = new StringBuilder();
        if (reportRpc.getTableType().equals(ReportType.TABULAR.name())) {
            String tempTableName = getTempTableName();
            reportQuery.append(getCoreQueryPart(companyId, reportRpc, tempTableName, viewRpc));
            reportQuery.append(" SELECT COUNT(*) ");
            reportQuery.append(", '0' :: text as gorder, '0' :: text as sorder FROM " + tempTableName);
        } else {
            reportQuery.append(" SELECT COUNT(*)-1, '0' :: text as gorder, '0' :: text as sorder FROM ( ");
            reportRpc.setLimit(0);
            reportQuery.append(SqlQueryUtil.getSummaryReportQuery(companyId, reportRpc, null, false));
            reportQuery.append(" ) tempTableName ");
            return reportQuery.toString();
        }
        return reportQuery.toString();
    }

    public static String getTabularReportQuery(Integer companyId, ReportRpc reportRpc, ViewRpc viewRpc, boolean isForExport) {
        StringBuilder reportQuery = new StringBuilder();
        String tempTableName = getTempTableName();
        reportQuery.append(getCoreQueryPart(companyId, reportRpc, tempTableName, viewRpc));
        reportQuery.append(getTotalFooterRowQueryPart(viewRpc, reportRpc, tempTableName));
        boolean sym = false;
        reportQuery.append(" UNION ALL  ");
        reportQuery.append(" select * from ( ");
        reportQuery.append(" select * from ( ");
        reportQuery.append(" SELECT ");
        reportQuery.append(getHiddenColumn(viewRpc));
        for (int i = 0; i < reportRpc.getSelectedColumns().size(); i++) {
            ColumnRpc column = reportRpc.getSelectedColumns().get(i);
            if (sym) {
                reportQuery.append(",");
            } else {
                sym = true;
            }
            String columnByFormat = getFormattedColumns("", column, null, reportRpc.getBrowserTimeZone(), isForExport);
            reportQuery.append(columnByFormat);
        }
        return getOrderByForPSQL(reportRpc, viewRpc, reportQuery, tempTableName);
    }

    private static String getOrderByForPSQL(ReportRpc reportRpc, ViewRpc viewRpc, StringBuilder reportQuery, String tempTableName) {
        List<ColumnRpc> selectedColumnForOrder = new LinkedList<>(reportRpc.getColumnMap().values());
        if (selectedColumnForOrder.size() == 0) {
            selectedColumnForOrder = reportRpc.getSelectedColumns();
        }
        ColumnRpc sortedColumn = getColumnByName(reportRpc.getSortTableByColumn(), selectedColumnForOrder);
        ColumnRpc sorterColumnHasSelected = getColumnByName(reportRpc.getSortTableByColumn(), reportRpc.getSelectedColumns());
        if (sorterColumnHasSelected != null && sortedColumn != null && sortedColumn.getAlias() != null && !sortedColumn.getAlias().equals("")) {
            reportQuery.append(", (\"").append(sortedColumn.getAlias()).append("\") as \"sorderColumn").append(sortedColumn.getAlias()).append("\"");
        }
        reportQuery.append(", '1' :: text as gorder");
        reportQuery.append(", '2' :: text as groupsorder0 ");
        reportQuery.append(", '3' :: text as groupsorder1 ");
        reportQuery.append(", '4' :: text as groupsorder2 ");
        reportQuery.append(", '0' :: text as sorder FROM ").append(tempTableName);
        reportQuery.append(") groupdata ");
        reportQuery.append(" ORDER BY sorder ASC, ");

        if (sorterColumnHasSelected != null && sortedColumn != null && (reportRpc.getSelectedColumns().contains(sortedColumn) || reportRpc.getGroupColumns().contains(sortedColumn))) {
            reportQuery.append("\"sorderColumn").append(sortedColumn.getAlias()).append("\"");
            if (StrUtils.isEmpty(reportRpc.getSortTableByColumnType())) {
                reportQuery.append(" ASC , ");
            } else {
                reportQuery.append(" ").append(reportRpc.getSortTableByColumnType()).append(" , ");
            }
        }
        reportQuery.append(" gorder ");

        getLimitClauseNewLogic(reportRpc, reportQuery);

        reportQuery.append(" ) finalTemp  ");

        return reportQuery.toString();
    }

    public static synchronized String getSummaryReportQuery(Integer companyId, ReportRpc reportRpc, ViewRpc viewRpc, boolean isForExport) {
        StringBuilder reportQuery = new StringBuilder();
        String tempTableName = getTempTableName();
        clearNotGroupingAndNotSummarizedColumns(reportRpc);
        reportQuery.append(getCoreQueryPart(companyId, reportRpc, tempTableName, viewRpc));
        reportQuery.append(firstGroupingData(reportRpc, tempTableName, isForExport));
        reportQuery.append(getTotalFooterRowQueryPart(viewRpc, reportRpc, tempTableName));
        boolean needComma;
        reportQuery.append("\n UNION ALL ");
        reportQuery.append("\n select * from   (");
        reportQuery.append("\n select * from   (");
        // 1-grouped column tepada yasanlgan
        reportQuery.append("\n select * from " + getFirsGroupingTableName());
        // xar bir grouped column uchun alohida union all querylar
        String groupingTableAlias;
        ColumnRpc column0 = reportRpc.getSelectedColumns().get(0);
        for (int i = 1; i < reportRpc.getGroupColumns().size(); i++) {
            groupingTableAlias = "_gtb_" + i;
            reportQuery.append("\n UNION ALL ");
            reportQuery.append(" SELECT ");
            reportQuery.append(getGroupedColumnQueryPart(i, reportRpc, groupingTableAlias, isForExport));
            reportQuery.append(" FROM " + tempTableName + " as " + groupingTableAlias + " ");
            String subTableJoinColumn;
            if (SqlColumnType.DATE.getName().equals(column0.getType())) {
                String customDateFormat = "YYYY-MM-DD";
                if (column0.getCustomDateFormat() != null && !column0.getCustomDateFormat().equals("long") && !column0.getCustomDateFormat().equals("short")) {
                    customDateFormat = column0.getCustomDateFormat();
                }
                subTableJoinColumn = getGroupByDateQueryPart(column0.getAlias(), customDateFormat, reportRpc.getRangeType().get(0), groupingTableAlias).split(" as ")[0];
            } else {
                subTableJoinColumn = getFormattedColumns("", column0, groupingTableAlias, reportRpc.getBrowserTimeZone(), isForExport);
            }
            reportQuery.append(" join ").append(getFirsGroupingTableName()).append(" as _gtb_00  on  ").append(subTableJoinColumn).append(" = _gtb_00.\"").append(column0.getAlias()).append("\" ");
            reportQuery.append(getGroupByQueryPart(i, reportRpc, groupingTableAlias));
        }
        // detailed view uchun kerak buladiganlari
        needComma = false;
        List<ColumnRpc> selectedColumnForOrder = new LinkedList<>(reportRpc.getColumnMap().values());
        if (selectedColumnForOrder.size() == 0) {
            selectedColumnForOrder = reportRpc.getSelectedColumns();
        }
        ColumnRpc sortedColumn = getColumnByName(reportRpc.getSortTableByColumn(), selectedColumnForOrder);
        ColumnRpc sorterColumnHasSelected = getColumnByName(reportRpc.getSortTableByColumn(), reportRpc.getSelectedColumns());
        if (reportRpc.getIsDetailed()) {
            if (reportRpc.getGroupColumns().size() > 0) {
                reportQuery.append("\n UNION ALL SELECT ");
            } else {
                reportQuery.append("\n SELECT ");
            }
            reportQuery.append(getHiddenColumn(viewRpc));
            for (int i = 0; i < reportRpc.getSelectedColumns().size(); i++) {
                ColumnRpc selectedColumnRpc = reportRpc.getSelectedColumns().get(i);
                if (needComma) {
                    reportQuery.append(",");
                }
                if (reportRpc.getGroupColumns().contains(selectedColumnRpc) && selectedColumnRpc.getType().equals(SqlColumnType.DATE.getName())) {
                    ColumnRpc column = reportRpc.getGroupColumns().get(i);
                    String customDateFormat = "YYYY-MM-DD";
                    if (column.getCustomDateFormat() != null && !column.getCustomDateFormat().equals("long") && !column.getCustomDateFormat().equals("short")) {
                        customDateFormat = column.getCustomDateFormat();
                    }
                    reportQuery.append(getGroupByDateQueryPart(column.getAlias(), customDateFormat, reportRpc.getRangeType().get(i), getDetailedTableName()));
                } else {
                    String columnByFormat = getFormattedColumns("", selectedColumnRpc, getDetailedTableName(), reportRpc.getBrowserTimeZone(), isForExport);
                    reportQuery.append(columnByFormat);
                }
                needComma = true;
            }
            if (sorterColumnHasSelected != null && sortedColumn != null && sortedColumn.getAlias() != null && !sortedColumn.getAlias().equals("")) {
                reportQuery.append(", (" + getDetailedTableName() + ".\"" + sortedColumn.getAlias() + "\") as \"sorderColumn" + sortedColumn.getAlias()).append("\"");
            }

            reportQuery.append(", '" + reportRpc.getGroupColumns().size() + "' :: text as gorder ");

            reportQuery.append(", '2' :: text as groupsorder0 ");
            reportQuery.append(", '3' :: text as groupsorder1 ");
            reportQuery.append(", '4' :: text as groupsorder2 ");

            reportQuery.append(", '0' :: text as sorder FROM " + tempTableName + " as " + getDetailedTableName() + " ");
            String subTableJoinColumn;
            if (SqlColumnType.DATE.getName().equals(column0.getType())) {
                String customDateFormat = "YYYY-MM-DD";
                if (column0.getCustomDateFormat() != null && !column0.getCustomDateFormat().equals("long") && !column0.getCustomDateFormat().equals("short")) {
                    customDateFormat = column0.getCustomDateFormat();
                }
                subTableJoinColumn = getGroupByDateQueryPart(column0.getAlias(), customDateFormat, reportRpc.getRangeType().get(0), getDetailedTableName()).split(" as ")[0];
            } else {
                subTableJoinColumn = getFormattedColumns("", column0, getDetailedTableName(), reportRpc.getBrowserTimeZone(), isForExport);
            }
            reportQuery.append(" join ").append(getFirsGroupingTableName()).append(" as _gtb_00  on  ").append(subTableJoinColumn).append(" = _gtb_00.\"").append(column0.getAlias()).append("\" ");
        }


        reportQuery.append(" ) groupdata ");
        // order by
        needComma = false;
        reportQuery.append(" ORDER BY sorder, ");
        String sortType = " ASC ";
        if (sorterColumnHasSelected != null && sortedColumn != null) {
            if (StrUtils.isEmpty(reportRpc.getSortTableByColumnType())) {
                sortType = " ASC ";
            } else {
                sortType = " " + reportRpc.getSortTableByColumnType() + " ";
            }
        }

        for (int i = 0; i < reportRpc.getGroupColumns().size(); i++) {
            ColumnRpc column = reportRpc.getGroupColumns().get(i);
            if (needComma) {
                reportQuery.append(",");
            }
            reportQuery.append("\"" + column.getAlias() + "\"");
            if (sorterColumnHasSelected != null && column.equals(sortedColumn)) {
                reportQuery.append(sortType);
            } else {
                if (column.getType().equals(SqlColumnType.DATE.getName())) {
                    reportQuery.append(" DESC ");
                } else {
                    reportQuery.append("Ascending".equals(reportRpc.getSortTypes().get(i)) ? " ASC " : " DESC ");
                }
            }
            reportQuery.append(", groupsorder" + i + " DESC ");
            needComma = true;
        }
        if (needComma) {
            reportQuery.append(", ");
        }
        reportQuery.append(" gorder ");

        if (sorterColumnHasSelected != null && sortedColumn != null && sortedColumn.getAlias() != null && !sortedColumn.getAlias().equals("")) {
            reportQuery.append(", " + " \"sorderColumn" + sortedColumn.getAlias() + "\"");
            if (StrUtils.isEmpty(reportRpc.getSortTableByColumnType())) {
                reportQuery.append(" ASC ");
            } else {
                reportQuery.append(" " + reportRpc.getSortTableByColumnType() + " ");
            }
        }


        reportQuery.append(" ) finalTemp ");


        return reportQuery.toString();
    }

    private static String getDetailedTableName() {
        return "_dttb_";
    }

    private static String firstGroupingData(ReportRpc reportRpc, String tempTableName, boolean isForExport) {
        if (reportRpc.getGroupColumns() == null || reportRpc.getGroupColumns().size() == 0) {
            return "";
        }
        ColumnRpc column = reportRpc.getGroupColumns().get(0);
        StringBuilder firstGrouping = new StringBuilder();
        firstGrouping.append(", \n ");
        firstGrouping.append(getFirsGroupingTableName() + " as ( SELECT ");
        firstGrouping.append(getGroupedColumnQueryPart(0, reportRpc, "_gtb_0", isForExport));
        firstGrouping.append(" FROM " + tempTableName + " as _gtb_0 ");
        firstGrouping.append(getGroupByQueryPart(0, reportRpc, "_gtb_0"));
        firstGrouping.append(" ORDER BY ");

        if (SqlColumnType.DATE.getName().equals(column.getType())) {
            String customDateFormat = "YYYY-MM-DD";
            if (column.getCustomDateFormat() != null && !column.getCustomDateFormat().equals("long") && !column.getCustomDateFormat().equals("short")) {
                customDateFormat = column.getCustomDateFormat();
            }
            firstGrouping.append(getGroupByDateQueryPart(column.getAlias(), customDateFormat, reportRpc.getRangeType().get(0), "_gtb_0").split(" as ")[0]);
        } else {
            firstGrouping.append("_gtb_0." + "\"" + column.getAlias() + "\"");
        }

        boolean thisColumnSortedByUser = false;
        if (!StrUtils.isEmpty(reportRpc.getSortTableByColumn())) {
            if (reportRpc.getSortTableByColumn().equals(column.getName())
                    || column.getName().equals(column.getPrefix() + "." + reportRpc.getSortTableByColumn())
                    || reportRpc.getSortTableByColumn().replace("_", ".").equals(column.getName().replace("_", "."))) {
                thisColumnSortedByUser = true;
            }
        }

        if (thisColumnSortedByUser) {
            if (!StrUtils.isEmpty(reportRpc.getSortTableByColumnType())) {
                firstGrouping.append(" " + reportRpc.getSortTableByColumnType() + " ");
            } else {
                firstGrouping.append(" ASC ");

            }
        } else {
            if (SqlColumnType.DATE.getName().equals(column.getType())) {
                firstGrouping.append(" DESC ");
            } else {
                firstGrouping.append("Ascending".equals(reportRpc.getSortTypes().get(0)) ? " ASC " : " DESC ");
            }
        }
        getLimitClauseNewLogic(reportRpc, firstGrouping);

        firstGrouping.append(" ) ");

        return firstGrouping.toString();
    }

    private static void clearNotGroupingAndNotSummarizedColumns(ReportRpc reportRpc) {
        if (reportRpc.getIsDetailed() != null && !reportRpc.getIsDetailed()) {
            LinkedList<ColumnRpc> clearedSelectedColumns = new LinkedList<>();
            boolean addedColumn;
            for (ColumnRpc selectedcolumn : reportRpc.getSelectedColumns()) {
                addedColumn = false;
                for (ColumnRpc groupingColumn : reportRpc.getGroupColumns()) {
                    if (groupingColumn.getName().equals(selectedcolumn.getName())) {
                        clearedSelectedColumns.add(selectedcolumn);
                        addedColumn = true;
                        break;
                    }
                }
                if (!addedColumn) {
                    for (ColumnRpc summirizedcolumn : reportRpc.getSumaries()) {
                        if (summirizedcolumn.getName().equals(selectedcolumn.getName())) {
                            clearedSelectedColumns.add(selectedcolumn);
                            addedColumn = true;
                            break;
                        }
                    }
                }
                if (!addedColumn && reportRpc.getChartConf() != null) {
                    if (reportRpc.getChartConf().getxAxis() != null && selectedcolumn.getName().equals(reportRpc.getChartConf().getxAxis().getColumn())) {
                        clearedSelectedColumns.add(selectedcolumn);
                        continue;
                    }
                    if (reportRpc.getChartConf().getSeries() != null) {
                        for (SerieConfItem series : reportRpc.getChartConf().getSeries()) {
                            if (series.getSerieColumn() != null && selectedcolumn.getName().equals(series.getSerieColumn().getColumn())) {
                                clearedSelectedColumns.add(selectedcolumn);
                            }
                        }
                    }
                }

            }
            reportRpc.setSelectedColumns(clearedSelectedColumns);
        }
    }

    /**
     * Генерация основного теле запроса
     *
     * @param companyId      индификатор компании
     * @param reportRpc      структура отчета с настройками что нужно получить
     * @param tempTableName  имя темповой таблицы
     * @param viewRpc        Настройки отчета, чтобы не заправшивать и не парсить все заново
     * @return
     */
    private static String getCoreQueryPart(Integer companyId, ReportRpc reportRpc, String tempTableName, ViewRpc viewRpc) {

        String longDateFormat = "YYYY-MM-DD HH24:MI";
        String shortDateFormat = "YYYY-MM-DD";

        StringBuilder coreQuery = new StringBuilder();
        if (viewRpc == null) {
            viewRpc = getViewParser(reportRpc.getViewCode());
        }
        //Генерация выборки, список колонок
        coreQuery.append(" WITH " + tempTableName + " AS (");
        coreQuery.append(" SELECT ");
        if (!StrUtils.isEmpty(viewRpc.getAgregateFunction())) {
            coreQuery.append("DISTINCT ");
        }
        coreQuery.append(getHiddenColumn(viewRpc));

        boolean needComma = false;
        for (int i = 0; i < reportRpc.getSelectedColumns().size(); i++) {
            ColumnRpc columnRpc = reportRpc.getSelectedColumns().get(i);
            if (needComma) {
                coreQuery.append(",");
            }
            if (SqlColumnType.DATE.getName().equals(columnRpc.getType())) {
                String columnName = columnRpc.getName();
                String str = "";
                String timeZone = ServerUtils.isNullOrEmpty(reportRpc.getBrowserTimeZone()) ? "" : (" AT TIME ZONE '" + reportRpc.getBrowserTimeZone() + "'");
                switch ("" + columnRpc.getColumnFormat()) {
                    case ColumnFormat_DATE -> {
                        if (columnRpc.getCustomDateFormat() == null || "".equals(columnRpc.getCustomDateFormat()) || columnRpc.getCustomDateFormat().equals("long")) {
                            str = "(to_char(" + columnName + timeZone + ",'" + longDateFormat + "'))";
                        } else {
                            if (columnRpc.getCustomDateFormat().equals("short")) {
                                str = ("to_char(" + columnName + timeZone + ",'" + shortDateFormat + "')");
                            } else {
                                str = ("to_char(" + columnName + timeZone + ",'" + columnRpc.getCustomDateFormat() + "')");
                            }
                        }
                    }
                    case ColumnFormat_DATE_WITHOUT_TIME_ZONE -> {
                        if (columnRpc.getCustomDateFormat() == null || "".equals(columnRpc.getCustomDateFormat()) || columnRpc.getCustomDateFormat().equals("long")) {
                            str = "(to_char(" + columnName + ",'" + longDateFormat + "'))";
                        } else {
                            if (columnRpc.getCustomDateFormat().equals("short")) {
                                str = "(to_char(" + columnName + ",'" + shortDateFormat + "'))";
                            } else {
                                str = "(to_char(" + columnName + ",'" + columnRpc.getCustomDateFormat() + "'))";
                            }
                        }
                    }
                    case ColumnFormat_DATE_WITHOUT_TIME -> {
                        str = "to_char(" + columnName + ",'YYYY/MM/DD')";
                    }
                    case ColumnFormat_WITHOUT_TIME_ZONE, ColumnFormat_WITHOUT_TIME_ZONE_AND_TIME -> {
                        if (columnRpc.getCustomDateFormat() == null || "".equals(columnRpc.getCustomDateFormat())) {
                            str = columnName;
                        } else {
                            str = ("to_char(" + columnName + timeZone + ",'" + columnRpc.getCustomDateFormat() + "')");
                        }
                    }
                }
                coreQuery.append(str);
            } else {
                coreQuery.append(columnRpc.getName());
            }
            coreQuery.append(" as \"")
                    .append(columnRpc.getAlias())
                    .append("\"");
            if (columnRpc.getReletedColumn() != null) {
                coreQuery.append(", ")
                        .append(columnRpc.getReletedColumn())
                        .append(" as \"").append((new Date()).getTime())
                        .append(columnRpc.getReletedColumn())
                        .append("\"");
            }
            needComma = true;
        }
        coreQuery.append(" FROM ");

        //add main query from report settings
        if (StringUtils.isNotEmpty(viewRpc.getQueries())) {
            String query = viewRpc.getQueries();
            query = getCustomizeWhere(reportRpc, query);
            query = query.replace("$", companyId.toString());
            coreQuery.append(query);
        }

        joinCustomFieldTable(coreQuery, reportRpc, companyId);
        getFilterOptions(reportRpc.getUserID(), viewRpc, coreQuery);
        getWhereClauseNewLogic(reportRpc, coreQuery, companyId);
        String releledToProject = null;
        if (viewRpc.getFilterColumns() != null) {
            for (ColumnRpc selectedColumn : viewRpc.getFilterColumns()) {
                if (selectedColumn.getReletedProjectColumn() != null) {
                    releledToProject = selectedColumn.getReletedProjectColumn();
                    break;
                }
            }
        }
        if (reportRpc.getReletedProjectID() != null && releledToProject != null) {
            reletedProjectFilter(coreQuery, releledToProject, reportRpc.getReletedProjectID());
        }
        coreQuery.append(" ) ");

        return coreQuery.toString();
    }

    private static void getLimitClauseNewLogic(ReportRpc reportRpc, StringBuilder coreQuery) {
        if (reportRpc.getLimit() > 0) {
            coreQuery.append(" limit ").append(reportRpc.getLimit());
            coreQuery.append(" offset ").append(reportRpc.getPosition() - 1);
        }
    }

    private static String getHiddenColumn(ViewRpc viewRpc) {
        StringBuilder builder = new StringBuilder();
        builder.append((!(viewRpc == null || viewRpc.getId() == null || "".equals(viewRpc.getId()))) ? " " + viewRpc.getId() + ", " : "");
        if (viewRpc != null && viewRpc.getHiddenColumnCount() > 0) {
            for (ColumnRpc columnRpc : viewRpc.getHiddenColumns()) {
                builder.append(columnRpc.getName() + " \"" + columnRpc.getName() + "\",");
            }
        }
        return builder.toString();
    }

    private static String getTempTableName() {
        String tempTableName = "table_" + UUID.randomUUID();
        tempTableName = tempTableName.replace("-", "");
        return tempTableName;
    }

    private static String getFirsGroupingTableName() {
        return "_fgtb_ ";
    }

    private static String getGroupedColumnQueryPart(int depth, ReportRpc reportRpc, String groupingTableAlias, boolean isForExport) {
        StringBuilder columnQuery = new StringBuilder();
        boolean needComma = false;
        for (int i = 0; i <= depth; i++) {

            ColumnRpc column = reportRpc.getSelectedColumns().get(i);
            if (needComma) {
                columnQuery.append(",");
            }
            // agar summary qilinayotgan column DATE tipida bulsa
            if (SqlColumnType.DATE.getName().equals(column.getType())) {
                String customDateFormat = "YYYY-MM-DD";
                if (column.getCustomDateFormat() != null && !column.getCustomDateFormat().equals("long") && !column.getCustomDateFormat().equals("short")) {
                    customDateFormat = column.getCustomDateFormat();
                }
                columnQuery.append(getGroupByDateQueryPart(column.getAlias(), customDateFormat, reportRpc.getRangeType().get(i), groupingTableAlias));
            } else {
                String columnByFormat = getFormattedColumns("", column, groupingTableAlias, false, reportRpc.getBrowserTimeZone(), isForExport) + " as \"" + column.getAlias() + "\"";
                columnQuery.append(columnByFormat);
            }
            needComma = true;
        }
        // qolgan columnlarni aggreregat funcsiyalar buyicha qushib chiqiladi
        for (int i = depth + 1; i < reportRpc.getSelectedColumns().size(); i++) {
            ColumnRpc column = reportRpc.getSelectedColumns().get(i);
            if (needComma) {
                columnQuery.append(",");
            }
            columnQuery.append(getAggregateQueryPart(column, reportRpc, groupingTableAlias));
            needComma = true;
        }

        // query ishlagan vaqtda kerakli kurinishga kelishi uchun kerak buladigan belgi
        ColumnRpc sortedColumn = getColumnByName(reportRpc.getSortTableByColumn(), new LinkedList<>(reportRpc.getColumnMap().values()));
        ColumnRpc sorterColumnHasSelected = getColumnByName(reportRpc.getSortTableByColumn(), reportRpc.getSelectedColumns());
        if (sorterColumnHasSelected != null && sortedColumn != null && sortedColumn.getAlias() != null && !sortedColumn.getAlias().equals("")) {
            columnQuery.append(", max(" + groupingTableAlias + ".\"" + sortedColumn.getAlias() + "\") as \"sorderColumn" + sortedColumn.getAlias()).append("\"");
        }
        columnQuery.append(", " + "'" + depth + "' :: text as gorder ");

        if (depth == 0) {
            columnQuery.append(", '1' :: text as groupsorder0 ");
            columnQuery.append(", '1' :: text as groupsorder1 ");
            columnQuery.append(", '1' :: text as groupsorder2 ");
        }
        if (depth == 1) {
            columnQuery.append(", '2' :: text as groupsorder0 ");
            columnQuery.append(", '2' :: text as groupsorder1 ");
            columnQuery.append(", '2' :: text as groupsorder2 ");
        }
        if (depth == 2) {
            columnQuery.append(", '2' :: text as groupsorder0 ");
            columnQuery.append(", '3' :: text as groupsorder1 ");
            columnQuery.append(", '3' :: text as groupsorder2 ");
        }

        columnQuery.append(", '0' :: text as sorder ");
        return columnQuery.toString();
    }

    private static String getGroupByQueryPart(int depth, ReportRpc reportRpc, String tableAlias) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < depth + 1; i++) {
            ColumnRpc column = reportRpc.getGroupColumns().get(i);
            if (builder.length() > 0) {
                builder.append(",");
            } else {
                builder.append("GROUP BY ");
            }
            if (SqlColumnType.DATE.getName().equals(column.getType())) {
                String customDateFormat = "YYYY-MM-DD";
                if (column.getCustomDateFormat() != null && !column.getCustomDateFormat().equals("long") && !column.getCustomDateFormat().equals("short")) {
                    customDateFormat = column.getCustomDateFormat();
                }
                builder.append(getGroupByDateQueryPart(column.getAlias(), customDateFormat, reportRpc.getRangeType().get(i), tableAlias).split(" as ")[0]);
            } else {
                builder.append((tableAlias != null ? tableAlias + "." : "") + "\"" + column.getAlias() + "\"");
            }
        }
        return builder.toString();
    }

    private static String getGroupByDateQueryPart(String defaultColumnName, String customDateFormat, String type, String tableAlias) {
        customDateFormat = (customDateFormat == null || customDateFormat.isEmpty()) ? "yyyy-MM-DD HH24:MI" : customDateFormat;
        String columnName = tableAlias != null ? tableAlias + "." : "";
        if (!defaultColumnName.startsWith("\"")) {
            columnName += "\"" + defaultColumnName + "\"";
            defaultColumnName = "\"" + defaultColumnName + "\"";
        } else {
            columnName += defaultColumnName;
        }
        columnName = "to_date(" + columnName + ", '" + customDateFormat + "')";
        if (DateRangeType.Daily.name().equals(type)) {
            return "coalesce(to_char(" + columnName + ",'yyyy-MM-dd'),'') as " + defaultColumnName;
        } else {
            if (DateRangeType.Weekly.name().equals(type)) {
                return "coalesce(to_char(" + columnName + ",'IYYY IW')||'-Week','') as " + defaultColumnName;
            } else {
                if (DateRangeType.Monthly.name().equals(type)) {
                    return "coalesce(to_char(" + columnName + ",'yyyy-MM'),'') as " + defaultColumnName;
                } else {
                    if (DateRangeType.Quarterly.name().equals(type)) {
                        return "coalesce(to_char(" + columnName + ",'yyyy Q')||'-Quarter','') as " + defaultColumnName;
                    } else {
                        return "coalesce(to_char(" + columnName + ",'yyyy'),'') as " + defaultColumnName;
                    }
                }
            }
        }
    }

    private static String getGroupByDateForChart(String columnName, String customDateFormat, String type) {
        customDateFormat = (customDateFormat == null || customDateFormat.isEmpty()) ? "yyyy-MM-DD HH24:MI" : customDateFormat;
        if (!columnName.startsWith("\"")) {
            columnName = "\"" + columnName + "\"";
        }
        columnName = "to_date(" + columnName + ", '" + customDateFormat + "')";
        if (DateRangeType.Daily.name().equals(type)) {
            return "to_char(" + columnName + ",'yyyy-MM-dd') ";
        } else {
            if (DateRangeType.Weekly.name().equals(type)) {
                return "to_char("+columnName+",'IYYY IW') ";
            } else {
                if (DateRangeType.Monthly.name().equals(type)) {
                    return "to_char(" + columnName + ",'yyyy-MM') ";
                } else {
                    if (DateRangeType.Quarterly.name().equals(type)) {
                        return "to_char(" + columnName + ",'yyyy Q') ";
                    } else {
                        return "to_char(" + columnName + ",'yyyy') ";
                    }
                }
            }
        }
    }

    /**
     * Агрегирование числовых колонок
     *
     * @param column    колонка по которй по которой производиться агрегация дданных
     * @param reportRpc отчет по которому производиться агрешациия
     * @param groupingTableAlias
     * @return строка запрос с агрегацией
     */
    private static String getAggregateQueryPart(ColumnRpc column, ReportRpc reportRpc, String groupingTableAlias) {
        String separatorString = "||";

        boolean noTimezone = reportRpc.isNoTimeZone();
        String browserTimeZone = reportRpc.getBrowserTimeZone();
        browserTimeZone = noTimezone ? browserTimeZone : "";

        int maxDepth = reportRpc.getGroupColumns().indexOf(column);
        String prefix = "";
        if (maxDepth > -1) {
            prefix = "''" + separatorString;
        }

        //agar bu colonka buyicha xech qanday agregat funksiya belgilanmagan bulsa
        int summaryColumnIndex = reportRpc.getSumaries().indexOf(column);
        if (summaryColumnIndex < 0) {
            return prefix + "to_char(count(" + getFormattedColumns("", column, groupingTableAlias, browserTimeZone)
                    + "),'999,999,999') as \"" + column.getAlias() + "\"";
        }

        // ushbu column buyicha agregat funksiya mavjud bulsa , ularni queryga aylantirib chiqamiz
        StringBuilder agregateBuilder = new StringBuilder();
        column.setAvg(reportRpc.getSumaries().get(summaryColumnIndex).isAvg());
        column.setSmallest(reportRpc.getSumaries().get(summaryColumnIndex).isSmallest());
        column.setLargest(reportRpc.getSumaries().get(summaryColumnIndex).isLargest());
        column.setSum(reportRpc.getSumaries().get(summaryColumnIndex).isSum());
        column.setCount(reportRpc.getSumaries().get(summaryColumnIndex).isCount());

        agregateBuilder.append(prefix);
        boolean needComma = false;
        if (column.onlySum()) {
            String temp = getFormattedColumns("sum", column, groupingTableAlias, browserTimeZone, false);
            agregateBuilder.append(temp);
            needComma = true;
        } else if (column.isSum()) {
            String temp = getFormattedColumns("sum", column, groupingTableAlias, browserTimeZone, false);
            agregateBuilder.append(temp);
            needComma = true;
        }
        if (column.isCount()) {
            String temp = getFormattedColumns("count", column, groupingTableAlias, browserTimeZone, false);
            if (needComma) {
                agregateBuilder.append(separatorString + "'/'" + separatorString);
            }
            agregateBuilder.append(temp);
            needComma = true;
        }
        if (column.isAvg()) {
            String temp = getFormattedColumns("avg", column, groupingTableAlias, browserTimeZone, false);
            if (needComma) {
                agregateBuilder.append(separatorString + "'/'" + separatorString);
            }
            agregateBuilder.append(temp);
            needComma = true;
        }
        if (column.isLargest()) {
            String temp = getFormattedColumns("max", column, groupingTableAlias, browserTimeZone, false);
            if (needComma) {
                agregateBuilder.append(separatorString + "'/'" + separatorString);
            }
            agregateBuilder.append(temp);
            needComma = true;
        }
        if (column.isSmallest()) {
            String temp = getFormattedColumns("min", column, groupingTableAlias, browserTimeZone, false);
            if (needComma) {
                agregateBuilder.append(separatorString + "'/'" + separatorString);
            }
            agregateBuilder.append(temp);
            needComma = true;
        }
        if (needComma) {
            agregateBuilder.append(" as \"" + column.getAlias() + "\"");
        } else {
            agregateBuilder.append(" to_char(count(\"" + column.getAlias() + "\"),'999,999,999') as \"" + column.getAlias() + "\"");
        }
        return agregateBuilder.toString();
    }

    private static String getTotalFooterRowQueryPart(ViewRpc viewRpc, ReportRpc reportRpc, String tempTableName) {
        StringBuilder reportQuery = new StringBuilder();
        String id = getHiddenColumn(viewRpc).trim();
        StringBuilder temp = new StringBuilder();
        for (int i = 0; i < id.split(",").length; i++) {
            if (!"".equals(id.split(",")[i].trim())) {
                temp.append(" '").append(i).append("' hidden").append(i).append(", ");
            }
        }
        String separatorString = "||";
        reportQuery.append("SELECT " + temp + " to_char(COUNT(*),'999,999,999') ");
        if (reportRpc.getSumaries().contains(reportRpc.getSelectedColumns().get(0))) {
            reportQuery.append(separatorString + "'</br>'" + separatorString);
            reportQuery.append(getAggregateQueryPart(reportRpc.getSelectedColumns().get(0), reportRpc, null));
            reportQuery.append(",");
        } else {
            reportQuery.append(" as \"" + reportRpc.getSelectedColumns().get(0).getAlias() + "\",");
        }
        boolean sym = false;

        for (int i = 1; i < reportRpc.getSelectedColumns().size(); i++) {
            ColumnRpc column = reportRpc.getSelectedColumns().get(i);
            if (sym) {
                reportQuery.append(",");
            } else {
                sym = true;
            }
            reportQuery.append(getAggregateQueryPart(column, reportRpc, null));
        }
        if (reportRpc.getSelectedColumns().size() > 1) {
            reportQuery.append(",");
        }
        ColumnRpc sortedColumn = getColumnByName(reportRpc.getSortTableByColumn(), new LinkedList<>(reportRpc.getColumnMap().values()));
        ColumnRpc sorterColumnHasSelected = getColumnByName(reportRpc.getSortTableByColumn(), reportRpc.getSelectedColumns());
        if (sorterColumnHasSelected != null && sortedColumn != null && sortedColumn.getAlias() != null && !sortedColumn.getAlias().equals("")) {
            reportQuery.append("max(\"" + sortedColumn.getAlias() + "\") as \"sorderColumn" + sortedColumn.getAlias() + "\",");
        }
        reportQuery.append(" '-1' :: text as gorder , ");

        reportQuery.append(" '-1' :: text as groupsorder0 , ");
        reportQuery.append(" '-1' :: text as groupsorder1 , ");
        reportQuery.append(" '-1' :: text as groupsorder2 , ");

        reportQuery.append(" '-1' :: text as sorder FROM " + tempTableName);

        return reportQuery.toString();
    }

    public static String getFormattedColumns(String summaryType, ColumnRpc column, String grouTableAlias, String userTimeZone) {
        return getFormattedColumns(summaryType, column, grouTableAlias, false, userTimeZone, false);
    }

    public static String getFormattedColumns(String summaryType, ColumnRpc column, String grouTableAlias, String userTimeZone, boolean isForExport) {
        return getFormattedColumns(summaryType, column, grouTableAlias, false, userTimeZone, isForExport);
    }

    public static String getFormattedColumns(String summaryType, ColumnRpc column, String grouTableAlias, boolean isForChart, String userTimeZone) {
        return getFormattedColumns(summaryType, column, grouTableAlias, isForChart, userTimeZone, false);
    }

    public static String getFormattedColumns(String summaryType, ColumnRpc column, String grouTableAlias, boolean isForChart, String userTimeZone, boolean isForExport) {

        String columnName = grouTableAlias != null ? grouTableAlias + "." : "";
        if (isForChart) {
            columnName += column.getName();
        } else {
            columnName += "\"" + column.getAlias() + "\"";
        }
        String str = "";

        if (ColumnFormat_STRING.equals(column.getColumnFormat())) {
            if (summaryType.equals("count")) {
                str = ("''||count( distinct " + columnName + " )");
            } else {
                str = columnName;
            }
        } else {
            if (ColumnFormat_PERCENT.equals(column.getColumnFormat())) {
                if (isForChart) {
                    str = ("to_char(" + summaryType + "(" + columnName + "),'990.99')");
                } else {
                    str = ("to_char(" + summaryType + "(" + columnName + "),'990%')");
                }

            } else {
                switch ("" + column.getColumnFormat()) {
                    case ColumnFormat_NUMBER, ColumnFormat_IMAGE -> {
                        if (!isForChart) {
                            str = ("to_char(round(cast(" + summaryType + "(" + columnName + ") as numeric),0),'999,999,999,999')");
                        } else {
                            str = ("to_char(round(cast(" + summaryType + "(" + columnName + ") as numeric),0),'999,999,999,999')");
                        }
                    }
                    case ColumnFormat_DOUBLE -> {
                        if (!isForChart) {
                            str = ("to_char(round(cast(" + summaryType + "(" + columnName + ") as numeric),3),'999,999,999,999.999')");
                        } else {
                            str = ("to_char(round(cast(" + summaryType + "(" + columnName + ") as numeric),3),'999,999,999,999.999')");
                        }
                    }
                    case ColumnFormat_TIME -> {
                        if (!isForChart) {
                            str = ("to_char(trunc(" + summaryType + "(" + columnName + ")/60),'999999999:')||trim(to_char(" + summaryType + "(" + columnName + ")%60,'09'))");
                        } else {
                            str = ("to_char(trunc(" + summaryType + "(" + columnName + ")/60),'999999999.')||trim(to_char(" + summaryType + "(" + columnName + ")%60,'09'))");
                        }
                    }
                    case ColumnFormat_ROUNDED -> {
                        str = ("round( cast(" + summaryType + "(" + columnName + ") as numeric),2)");
                    }
                    case ColumnFormat_MONEY -> {
                        if ("".equals(summaryType) || null == summaryType) {
                            str = ("" + summaryType + "(" + columnName + ")::varchar");
                        } else if (!isForChart) {
                            str = ("to_char(round(cast(" + summaryType + "(" + columnName + ")as numeric),2),'999,999,999,990.00')");
                        } else {
                            str = ("to_char(round(cast(" + summaryType + "(" + columnName + ")as numeric),2),'999999999990.00')");
                        }
                    }
                    case ColumnFormat_PHONE -> {
                        str = summaryType + "(" + "replace(" + columnName + ",'|',' ')" + ")";
                    }
                    default -> {
                        if (isForExport) {
                            str = (summaryType + "(" + columnName + ")");
                        } else {
                            switch ("" + column.getColumnFormat()) {
                                case ColumnFormat_DATE, ColumnFormat_DATE_WITHOUT_TIME_ZONE, ColumnFormat_DATE_WITHOUT_TIME, ColumnFormat_WITHOUT_TIME_ZONE, ColumnFormat_WITHOUT_TIME_ZONE_AND_TIME -> {
                                    str = summaryType + "(" + columnName + ")||'' ";
                                }
                                default -> {
                                    if (!"".equals(summaryType)) {
                                        if (summaryType.equals("count")) {
                                            str = ("count( distinct " + columnName + " )||'' ");
                                        }
                                    } else {
                                        str = columnName;
                                    }
                                }
                            }
                        }
                    }
                }

            }
        }
        return "coalesce(" + str + ",'') ";
    }

    /**
     * Добавление условий фильтрация определенных в самом отчете
     *
     * @param userId
     * @param viewRpc
     * @param sqlQuery
     */
    private static void getFilterOptions(Integer userId, ViewRpc viewRpc, StringBuilder sqlQuery) {
        if (viewRpc.getReplacementUserId() != null && !"".equals(viewRpc.getReplacementUserId())) {
            sqlQuery.replace(0, sqlQuery.length(), sqlQuery.toString().replace(viewRpc.getReplacementUserId(), userId.toString()));
        }
        Set<String> keySet = viewRpc.getCustomReplacements() != null ? viewRpc.getCustomReplacements().keySet() : new HashSet<>();
        for (String key : keySet) {
            sqlQuery.replace(0, sqlQuery.length(), sqlQuery.toString().replace(key, viewRpc.getCustomReplacements().get(key)));
        }
        StringBuilder where = new StringBuilder();
        where.append("WHERE " + " \n");
        if (viewRpc.getWhereBase() != null) {
            where.append(viewRpc.getWhereBase() + " \n");
        }
        String WHERE_CODE = "{where}";
        whereTagReplacement(sqlQuery, where, WHERE_CODE);
    }

    private static void whereTagReplacement(StringBuilder sqlQuery, StringBuilder where, String WHERE_CODE) {
        if (!sqlQuery.toString().contains(WHERE_CODE)) {
            sqlQuery.append(where);
        }
        int wherePosition;
        while (sqlQuery.toString().contains(WHERE_CODE)) {
            wherePosition = sqlQuery.toString().indexOf(WHERE_CODE);
            sqlQuery.replace(wherePosition, wherePosition + WHERE_CODE.length(), where.toString());
        }
    }

    private static void getWhereClauseNewLogic(ReportRpc report, StringBuilder sqlQuery, Integer companyId) {
        PatternUtils patternUtils = new PatternUtils(report);
        patternUtils.generate();
        String generated = patternUtils.getGenerated();
        System.out.println(generated);

        if (report.getValues().size() != 0) {
            if (report.getValues().size() % 2 == 0) {
                generated = " AND (" + generated;
            } else {
                generated = " AND " + generated;
            }
            for (int i = 0; i < report.getValues().size(); i++) {
                if (report.getFieldd().size() <= i) {
                    continue;
                }
                ColumnRpc column = report.getFieldd().get(i);
                final String columnType = column.getType();
                final String columnFormatType = column.getColumnFormat();
                final String columnFilterWidgetType = column.getFilterWidgetType() != null ? column.getFilterWidgetType() : "";
                String columnName = SqlColumnType.STRING.getName().equals(columnType) ? " lower(trim(" + column.getName().toLowerCase() + ")) " : column.getName().toLowerCase();
                columnName = column.isTreeSelect() ? " " + column.getLookUpField().trim() + " " : columnName;

                if (columnType == null) {
                    continue;
                }

                String clause = "";
                if ("".equals(columnFilterWidgetType)) {
                    clause = getWhereClauseFilterOptions(report, i, columnType, columnFormatType, columnName);
                } else {
                    clause = getWhereClauseFilterWidgetOptions(report, i, columnFilterWidgetType, columnName, companyId);
                }
                generated = generated.replace("$(" + (i + 1) + ")", clause);
            }
        }

        if (report.getValues().size() > 0 && report.getValues().size() % 2 == 0) {
            generated += " )";
        }
        String WHERE_CODE = "{where2}";
        whereTagReplacement(sqlQuery, new StringBuilder(generated), WHERE_CODE);
    }

    private static String getCustomizeWhere(ReportRpc report, String query) {
        ReportingCustomizeFilter filter = report.getCustomizeFilter();
        if (!(filter == null || filter.getViewAs().size() < 1)) {
            if (ServerUtils.isNullOrEmpty(filter.getSelectedViewAsName())) {
                filter.setSelectedViewAsName(filter.getViewAs().get(0).getCode());
            }
            if (!ServerUtils.isNullOrEmpty(filter.getSelectedViewAsName())) {
                for (ReportingRolePermissionItem selectItem : filter.getViewAs()) {
                    if (filter.getSelectedViewAsName() != null && filter.getSelectedViewAsName().equals(selectItem.getCode())) {
                        String sqlQuery = selectItem.getValue();
                        query = query.replace("#?replacement?#", sqlQuery);
                        break;
                    }
                }
            }
        }
        return query.replace("#?replacement?#", " 1=1 ");
    }

    private static String getWhereClauseFilterOptions(ReportRpc report, int index, String columnType, String columnFormatType, String columnName) {
        String clause = "";
        String value = report.getValues().get(index).toLowerCase();
        if (columnType.equals(SqlColumnType.DATE.getName())) {
            boolean noTimezone = report.isNoTimeZone();
            String userTimeZone = report.getBrowserTimeZone();
            if (userTimeZone == null) {
                userTimeZone = "GMT+00";
            }
            userTimeZone = !noTimezone ? userTimeZone : "";
            String timeZone = ServerUtils.isNullOrEmpty(userTimeZone) ? "" : (" AT TIME ZONE '" + userTimeZone + "'");
            DurationType durationType = DurationType.valueOf(report.getOperators().get(index));

            if (durationType.getName().equals(SamePeriodLastYear.toString())) {
                durationType = DurationType.valueOf(report.getKpiWidgetItem().getKpiWidgetFilterItemOne().getOperators().get(index));
            }
            if (durationType.equals(DurationType.After)) {

                if (columnFormatType.equals("date_without_time_zone")) {
                    clause = columnName + " >    '" + value + "'";
                } else {
                    clause = "((to_char(" + columnName + " ,'Mon DD YYYY HH24:MI'))::TIMESTAMP " + timeZone + ")" + " >= '" + value + "'";
                }

            } else {
                if (durationType.equals(DurationType.Before)) {

                    if (columnFormatType.equals("date_without_time_zone")) {
                        clause = columnName + " < '" + value + "'";
                    } else {
                        clause = "((to_char(" + columnName + " ,'Mon DD YYYY HH24:MI'))::TIMESTAMP " + timeZone + ")" + " <= '" + value + "'";
                    }

                } else {
                    if (durationType.equals(DurationType.Before1Week) || durationType.equals(DurationType.Before3Week) || durationType.equals(DurationType.BeforeMonth)) {
                        if (columnFormatType.equals("date_without_time_zone")) {
                            clause = columnName + " < " + value;
                        } else {
                            clause = columnName + " < " + value;
                        }
                    } else {
                        if (durationType.equals(DurationType.Equals)) {
                            if (columnFormatType.equals("date_without_time_zone")) {
                                clause = "date(" + columnName + ")" + " = '" + value + "'";
                            } else {
                                clause = "date(" + columnName + timeZone + ")" + " = '" + value + "'";
                            }
                        } else {
                            if (durationType.equals(DurationType.Between)) {
                                String[] tokens = value.split("_");
                                if (columnFormatType.equals("date_without_time_zone")) {
                                    clause = columnName + "::TIMESTAMP BETWEEN '" + tokens[0] + "' AND ('" + tokens[1] + "'::TIMESTAMP + interval '1 days - 1 ms') ";
                                } else {
                                    clause = "((to_char(" + columnName + ",'Mon DD YYYY HH24:MI'))::TIMESTAMP " + timeZone + ")" + " BETWEEN '" + tokens[0] + "' AND ('" + tokens[1] + "'::TIMESTAMP + interval '1 days - 1 ms') ";
                                }
                            } else if (durationType.equals(DurationType.AgeInDays)) {
                                String oper = value.split("_")[0];
                                Integer days = Integer.valueOf(value.split("_")[1]);
                                //">", "<", ">=", "=<", "="
                                if (">".equals(oper)) {
                                    String aend = durationType.getPlusDate(durationType.getStartNow(), days + 1, "days");
                                    if (columnFormatType.equals("date_without_time_zone")) {
                                        clause = columnName + " > " + aend + " ";
                                    } else {
                                        clause = "((to_char(" + columnName + " ,'Mon DD YYYY HH24:MI'))::TIMESTAMP " + timeZone + ")" + " > " + aend + " ";
                                    }
                                } else if (">=".equals(oper)) {
                                    String aend = durationType.getPlusDate(durationType.getStartNow(), days, "days");
                                    if (columnFormatType.equals("date_without_time_zone")) {
                                        clause = columnName + " >= " + aend + " ";
                                    } else {
                                        clause = "((to_char(" + columnName + " ,'Mon DD YYYY HH24:MI'))::TIMESTAMP " + timeZone + ")" + " >= " + aend + " ";
                                    }
                                } else if ("<".equals(oper)) {
                                    String astart = durationType.getMinusDate(durationType.getStartNow(), days + 1, "days");
                                    if (columnFormatType.equals("date_without_time_zone")) {
                                        clause = columnName + " < " + astart + " ";
                                    } else {
                                        clause = "((to_char(" + columnName + " ,'Mon DD YYYY HH24:MI'))::TIMESTAMP " + timeZone + ")" + " < " + astart + " ";
                                    }
                                } else if ("<=".equals(oper)) {
                                    String astart = durationType.getMinusDate(durationType.getStartNow(), days, "days");
                                    if (columnFormatType.equals("date_without_time_zone")) {
                                        clause = columnName + " <= " + astart + " ";
                                    } else {
                                        clause = "((to_char(" + columnName + " ,'Mon DD YYYY HH24:MI'))::TIMESTAMP " + timeZone + ")" + " <= " + astart + "";
                                    }
                                }

                            } else {
                                String[] tokens = value.split("_");
                                if (value.contains("'")) {
                                    if (columnFormatType.equals("date_without_time_zone")) {
                                        clause = columnName + "::TIMESTAMP BETWEEN " + tokens[0] + " AND " + tokens[1];
                                    } else {
                                        clause = "(to_char(" + columnName + " ,'Mon DD YYYY HH24:MI')::TIMESTAMP " + timeZone + ")" + " BETWEEN " + tokens[0] + " AND " + tokens[1];
                                    }
                                } else {
                                    if (columnFormatType.equals("date_without_time_zone")) {
                                        clause = columnName + "::TIMESTAMP BETWEEN '" + tokens[0] + "' AND ('" + tokens[1] + "'::TIMESTAMP + interval '1 days - 1 ms')";
                                    } else {
                                        clause = columnName + "::TIMESTAMP " + timeZone + " BETWEEN '" + tokens[0] + "' AND ('" + tokens[1] + "'::TIMESTAMP + interval '1 days - 1 ms')";
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } else {
            if (SqlColumnType.NUMBER.getName().equals(columnType) || SqlColumnType.MONEY.getName().equals(columnType)) {
                final OperationType operationType = OperationType.getByCode(report.getOperators().get(index));
                if (operationType != null) {
                    clause = columnName + operationType.getOperator() + " ? ";
                }
            } else if (SqlColumnType.TIME.getName().equals(columnType)) {
                final OperationType operationType = OperationType.getByCode(report.getOperators().get(index));
                if (operationType != null) {
                    clause = columnName + operationType.getOperator() + "?*60";
                }
            } else {
                final OperationType operationType = OperationType.getByCode(report.getOperators().get(index));
                if (OperationType.IsEqualTo.equals(operationType) || OperationType.IsNotEqualTo.equals(operationType)) {
                    clause = columnName + operationType.getOperator() + " ? ";
                } else {
                    if (OperationType.DoesNoTContain.equals(operationType)) {
                        clause = "(" + columnName + " is null or " + columnName + operationType.getOperator() + "'||?||'%' ESCAPE '@' )  ";
                    } else if (OperationType.StartsWith.equals(operationType) || OperationType.Contains.equals(operationType)) {
                        clause = columnName + operationType.getOperator() + "'||?||'%' ESCAPE '@'";
                    } else {
                        if (OperationType.EndsWith.equals(operationType)) {
                            clause = columnName + operationType.getOperator() + "'||? ESCAPE '@'";
                        } else if (operationType != null) {
                            clause = columnName + operationType.getOperator() + "'||?";
                        }
                    }
                }
                if (operationType != null && ("n/a".equals(value.trim()) || "".equals(value.trim()))) {
                    String equals1 = "is ", equals2 = "=", condition = "OR";
                    if ("<>".equals(operationType.getOperator())) {
                        equals1 += "not ";
                        equals2 = "<>";
                        condition = "AND";
                    }
                    clause = " (" + columnName + equals1 + " NULL " + condition + " trim(" + columnName + ") " + equals2 + " '') ";
                }
            }
        }
        return clause;
    }

    private static String getWhereClauseFilterWidgetOptions(ReportRpc report, int index, String columnFilterWidgetType, String columnName, int companyId) {
        String clause = "";
        if (columnFilterWidgetType.equals("checkbox")) {
            String[] selectedValues = report.getValues().get(index).split("<->");
            StringBuilder tempBuffer = new StringBuilder();
            for (String selectedValue : selectedValues) {
                if (tempBuffer.length() > 0) {
                    tempBuffer.append(" OR ");
                }
                tempBuffer.append(columnName + " LIKE '%" + selectedValue.toLowerCase() + "%'");
            }
            clause = " ( " + tempBuffer + " ) ";
        } else {
            Boolean isTree = report.getFieldd().get(index).isTreeSelect();

            if (columnFilterWidgetType.equals("lookup") && isTree) {
                String replacement = report.getFieldd().get(index).getTable();
                replacement = replacement.replace("$", "\"" + companyId + "\"");
                clause = "','||" + columnName + "||',' like " + "'%,'||(select getchildrenid('" + report.getValues().get(index).replace("'", "''") + "','" + replacement
                        + "','" + report.getFieldd().get(index).getColumn() + "','" + report.getFieldd().get(index).getId() + "','" + report.getFieldd().get(index).getParent()
                        + "','" + report.getFieldd().get(index).getWhere() + "','" + report.getFieldd().get(index).getSplitter() + "',0))||',%'";
            } else {

                if (OperationType.IsNotEqualTo.name().equals(report.getOperators().get(index))) {
                    clause = columnName + " != " + report.getValues().get(index).toLowerCase() + "";
                } else {
                    clause = columnName + " = " + report.getValues().get(index).toLowerCase() + "";
                }
            }
        }
        return clause;
    }

    public static String getChartQuery(Integer companyId, ReportRpc report) {
        ViewRpc viewRpc = getViewParser(report.getViewCode());

        StringBuilder sqlQuery = new StringBuilder();
        String tempTableName = getTempTableName();
        sqlQuery.append(getCoreQueryPart(companyId, report, tempTableName, viewRpc));
        sqlQuery.append("   \n");
        sqlQuery.append("SELECT ");

        ChartConfItem chartSetting = report.getChartConf();

        if (chartSetting == null || chartSetting.getType() == null) {
            return "";
        }

        /* build custom query  */
        SerieColumn xAxisColumn = chartSetting.getxAxis();
        ColumnRpc columnRpc = new ColumnRpc(xAxisColumn.getColumn());
        columnRpc.setType(xAxisColumn.getColumnType());
        columnRpc.setColumnFormat(xAxisColumn.getColumnFormat());

        SerieColumn splitByColumn = chartSetting.getSplitBy();
        ColumnRpc splitByRpc = new ColumnRpc();
        if (splitByColumn != null) {
            splitByRpc = new ColumnRpc(splitByColumn.getColumn());
            splitByRpc.setType(splitByColumn.getColumnType());
            splitByRpc.setColumnFormat(splitByColumn.getColumnFormat());
        }
        SerieColumn customSorderColumn = chartSetting.getCustomSortColumn();
        ColumnRpc customSorderColumnRpc = new ColumnRpc();
        if (customSorderColumn != null) {
            customSorderColumnRpc = new ColumnRpc(customSorderColumn.getColumn());
            customSorderColumnRpc.setType(customSorderColumn.getColumnType());
            customSorderColumnRpc.setColumnFormat(customSorderColumn.getColumnFormat());
        }

        String xAxisColumnInQuery = "\"" + xAxisColumn.getColumn().replace(".", "_") + "\"";

        String xAxisColumnGrouping = xAxisColumnInQuery;

        if (xAxisColumn.getColumnType().equals(SqlColumnType.DATE.getName())) {
            String customDateFormat = "YYYY-MM-DD";
            for (ColumnRpc rpc : report.getSelectedColumns()) {
                if (rpc.getName().equals(xAxisColumn.getColumn())) {
                    if (rpc.getCustomDateFormat() != null && !rpc.getCustomDateFormat().equals("long") && !rpc.getCustomDateFormat().equals("short")) {
                        customDateFormat = rpc.getCustomDateFormat();
                    }
                }
            }
            xAxisColumnGrouping = getGroupByDateForChart(xAxisColumnGrouping, customDateFormat, chartSetting.getDateSortPeriodType());
        }
        sqlQuery.append(" ").append(xAxisColumnGrouping).append(" as ").append(xAxisColumnInQuery).append(" ");

        String splitColumn = null;
        if (splitByColumn != null) {
            splitColumn = "\"" + splitByColumn.getColumn().replace(".", "_") + "\"";
            sqlQuery.append(", ").append(splitColumn).append(" as ").append(splitColumn).append(" ");
        }

        String customSorderBy = null;
        if (customSorderColumn != null) {
            customSorderBy = "\"" + customSorderColumn.getColumn().replace(".", "_") + "\"";
            sqlQuery.append(", ").append(customSorderBy).append(" as ").append(customSorderBy).append(" ");
        }

        StringBuilder orderingClause = new StringBuilder();
        StringBuilder sortingClause = new StringBuilder();
        StringBuilder seriesColumns = new StringBuilder();

        if (chartSetting.getSeries() != null) {
            for (SerieConfItem serieConf : chartSetting.getSeries()) {
                sqlQuery.append(",");


                if (StringUtils.isNotEmpty(orderingClause.toString())) {
                    orderingClause.append(" + ");
                    sortingClause.append(" + ");
                    seriesColumns.append(", ");
                }
                orderingClause.append(createTempColumn(sqlQuery, serieConf));
                sqlQuery.append(" as \"" + serieConf.getAlias() + "\" ");

                sortingClause.append("\"" + serieConf.getAlias() + "\"");
                seriesColumns.append("\"" + serieConf.getAlias() + "\" ");
            }
        }

        sqlQuery.append(" FROM " + tempTableName + " \n");

        sqlQuery.append(" GROUP BY ").append(xAxisColumnGrouping);
        if (splitByColumn != null) {
            sqlQuery.append(", ").append(splitColumn);
        }
        if (customSorderBy != null) {
            sqlQuery.append(", ").append(customSorderBy);
        }

        sqlQuery.append(" \n");

        if (StringUtils.isNotEmpty(orderingClause.toString())) {
            sqlQuery.append(" ORDER BY ").append(orderingClause).append(" DESC ");
        }

        if (chartSetting.getPageSize() != null && chartSetting.getPageSize() != 0) {
            sqlQuery.append(" offset 0 ");
            sqlQuery.append(" limit " + chartSetting.getPageSize());
        }

        //this one works for sorting things
        if (StringUtils.isNotEmpty(chartSetting.getSortBy()) && StringUtils.isNotEmpty(chartSetting.getSortType())) {

            StringBuilder sortedSql = new StringBuilder("SELECT ");
            sortedSql.append(getFormattedColumns("", columnRpc, "t", false, report.getBrowserTimeZone())).append(" as \"" + xAxisColumn.getColumn() + "\", ");
            if (splitByColumn != null) {
                sortedSql.append(getFormattedColumns("", splitByRpc, "t", false, report.getBrowserTimeZone())).append(" as \"" + splitByColumn.getColumn() + "\", ");
            }
            sortedSql.append(seriesColumns);
            if (customSorderColumn != null) {
                sortedSql.append(", ");
                sortedSql.append(getFormattedColumns("", customSorderColumnRpc, "t", false, report.getBrowserTimeZone())).append(" as \"" + customSorderColumn.getColumn() + "\" ");
//                sortedSql.append(customSorderBy).append(" as \"" + customSorderColumn.getColumn() + "\" ");
            }

            sortedSql.append(" FROM (").append(sqlQuery).append(") t ORDER BY ");

            if (ChartConfItem.BY_CUSTOM.equals(chartSetting.getSortBy()) && customSorderColumn != null) {
                sortedSql.append(customSorderBy + " ").append(chartSetting.getSortType());
            } else if (ChartConfItem.BY_CATEGORY.equals(chartSetting.getSortBy())) {
                sortedSql.append(xAxisColumnInQuery + " ").append(chartSetting.getSortType());
            } else {
                sortedSql.append(sortingClause).append(" ").append(chartSetting.getSortType());
            }
            if (splitByColumn != null) {
                sortedSql.append(", ");
                sortedSql.append(splitColumn).append(" DESC ");
            }

            return sortedSql.toString();
        }

        return sqlQuery.toString();
    }

    public static String getDrillChartQuery(Integer companyId, ReportRpc report, String parentQuery) {
        ViewRpc viewRpc = getViewParser(report.getViewCode());

        StringBuilder sqlQuery = new StringBuilder();
        String tempTableName = getTempTableName();
        sqlQuery.append(getCoreQueryPart(companyId, report, tempTableName, viewRpc));
        sqlQuery.append("   \n");
        sqlQuery.append("SELECT ");

        ChartConfItem chartSetting = report.getChartConf();

        if (chartSetting == null || chartSetting.getType() == null) {
            return "";
        }

        /* build custom query  */
        SerieColumn xAxisColumn = chartSetting.getxAxis();
        ColumnRpc columnRpc = new ColumnRpc(xAxisColumn.getColumn());
        columnRpc.setType(xAxisColumn.getColumnType());
        columnRpc.setColumnFormat(xAxisColumn.getColumnFormat());

        SerieColumn drillxAxisColumn = chartSetting.getDrillxAxis();
        ColumnRpc drillColumnRpc = new ColumnRpc(drillxAxisColumn.getColumn());
        drillColumnRpc.setType(drillxAxisColumn.getColumnType());
        drillColumnRpc.setColumnFormat(drillxAxisColumn.getColumnFormat());


        SerieColumn splitByColumn = chartSetting.getSplitBy();
        ColumnRpc splitByRpc = new ColumnRpc();
        if (splitByColumn != null) {
            splitByRpc = new ColumnRpc(splitByColumn.getColumn());
            splitByRpc.setType(splitByColumn.getColumnType());
            splitByRpc.setColumnFormat(splitByColumn.getColumnFormat());
        }
        SerieColumn customSorderColumn = chartSetting.getCustomSortColumn();
        ColumnRpc customSorderColumnRpc = new ColumnRpc();
        if (customSorderColumn != null) {
            customSorderColumnRpc = new ColumnRpc(customSorderColumn.getColumn());
            customSorderColumnRpc.setType(customSorderColumn.getColumnType());
            customSorderColumnRpc.setColumnFormat(customSorderColumn.getColumnFormat());
        }

        String xAxisColumnInQuery = "\"" + xAxisColumn.getColumn().replace(".", "_") + "\"";
        String drillxAxisColumnInQuery = "\"" + drillxAxisColumn.getColumn().replace(".", "_") + "\"";

        String xAxisColumnGrouping = xAxisColumnInQuery;
        String drillxAxisColumnGrouping = drillxAxisColumnInQuery;

        if (xAxisColumn.getColumnType().equals(SqlColumnType.DATE.getName())) {
            String customDateFormat = "YYYY-MM-DD";
            for (ColumnRpc rpc : report.getSelectedColumns()) {
                if (rpc.getName().equals(xAxisColumn.getColumn())) {
                    if (rpc.getCustomDateFormat() != null && !rpc.getCustomDateFormat().equals("long") && !rpc.getCustomDateFormat().equals("short")) {
                        customDateFormat = rpc.getCustomDateFormat();
                    }
                }
            }
            xAxisColumnGrouping = getGroupByDateForChart(xAxisColumnGrouping, customDateFormat, chartSetting.getDateSortPeriodType());
        }
        if (drillxAxisColumn.getColumnType().equals(SqlColumnType.DATE.getName())) {
            String customDateFormat = "YYYY-MM-DD";
            for (ColumnRpc rpc : report.getSelectedColumns()) {
                if (rpc.getName().equals(drillxAxisColumn.getColumn())) {
                    if (rpc.getCustomDateFormat() != null && !rpc.getCustomDateFormat().equals("long") && !rpc.getCustomDateFormat().equals("short")) {
                        customDateFormat = rpc.getCustomDateFormat();
                    }
                }
            }
            drillxAxisColumnGrouping = getGroupByDateForChart(drillxAxisColumnGrouping, customDateFormat, chartSetting.getDateSortPeriodType());
        }
        sqlQuery.append(" ").append(xAxisColumnGrouping).append(" as ").append(xAxisColumnInQuery).append(" ");
        sqlQuery.append(", ").append(drillxAxisColumnGrouping).append(" as ").append(drillxAxisColumnInQuery).append(" ");

        String splitColumn = null;
        if (splitByColumn != null) {
            splitColumn = "\"" + splitByColumn.getColumn().replace(".", "_") + "\"";
            sqlQuery.append(", ").append(splitColumn).append(" as ").append(splitColumn).append(" ");
        }

        String customSorderBy = null;
        if (customSorderColumn != null) {
            customSorderBy = "\"" + customSorderColumn.getColumn().replace(".", "_") + "\"";
            sqlQuery.append(", ").append(customSorderBy).append(" as ").append(customSorderBy).append(" ");
        }

        StringBuilder orderingClause = new StringBuilder();
        StringBuilder sortingClause = new StringBuilder();
        StringBuilder seriesColumns = new StringBuilder();

        if (chartSetting.getSeries() != null) {
            for (SerieConfItem serieConf : chartSetting.getSeries()) {
                sqlQuery.append(",");


                if (StringUtils.isNotEmpty(orderingClause.toString())) {
                    orderingClause.append(" + ");
                    sortingClause.append(" + ");
                    seriesColumns.append(", ");
                }
                orderingClause.append(createTempColumn(sqlQuery, serieConf));
                sqlQuery.append(" as \"" + serieConf.getAlias() + "\" ");

                sortingClause.append("\"" + serieConf.getAlias() + "\"");
                seriesColumns.append("\"" + serieConf.getAlias() + "\" ");
            }
        }

        sqlQuery.append(" FROM " + tempTableName + " \n");

        sqlQuery.append(" GROUP BY ").append(xAxisColumnGrouping).append(", ").append(drillxAxisColumnGrouping);
        if (splitByColumn != null) {
            sqlQuery.append(", ").append(splitColumn);
        }
        if (customSorderBy != null) {
            sqlQuery.append(", ").append(customSorderBy);
        }

        sqlQuery.append(" \n");

        if (StringUtils.isNotEmpty(orderingClause.toString())) {
            sqlQuery.append(" ORDER BY ").append(orderingClause).append(" DESC ");
        }

//        if (chartSetting.getPageSize() != null && chartSetting.getPageSize() != 0) {
//            sqlQuery.append(" offset 0 ");
//            sqlQuery.append(" limit " + chartSetting.getPageSize());
//        }

        //this one works for sorting things
        if (StringUtils.isNotEmpty(chartSetting.getSortBy()) && StringUtils.isNotEmpty(chartSetting.getSortType())) {

            StringBuilder sortedSql = new StringBuilder("SELECT ");
            sortedSql.append(getFormattedColumns("", columnRpc, "t", false, report.getBrowserTimeZone())).append(" as \"" + xAxisColumn.getColumn() + "\", ");
            sortedSql.append(getFormattedColumns("", drillColumnRpc, "t", false, report.getBrowserTimeZone())).append(" as \"" + drillxAxisColumn.getColumn() + "\", ");
            if (splitByColumn != null) {
                sortedSql.append(getFormattedColumns("", splitByRpc, "t", false, report.getBrowserTimeZone())).append(" as \"" + splitByColumn.getColumn() + "\", ");
            }
            sortedSql.append(seriesColumns);
            if (customSorderColumn != null) {
                sortedSql.append(", ");
                sortedSql.append(getFormattedColumns("", customSorderColumnRpc, "t", false, report.getBrowserTimeZone())).append(" as \"" + customSorderColumn.getColumn() + "\" ");
//                sortedSql.append(customSorderBy).append(" as \"" + customSorderColumn.getColumn() + "\" ");
            }

            sortedSql.append(" FROM (").append(sqlQuery).append(") t ORDER BY ");

            if (ChartConfItem.BY_CUSTOM.equals(chartSetting.getSortBy()) && customSorderColumn != null) {
                sortedSql.append(customSorderBy + " ").append(chartSetting.getSortType());
            } else if (ChartConfItem.BY_CATEGORY.equals(chartSetting.getSortBy())) {
                sortedSql.append(xAxisColumnInQuery + " ").append(chartSetting.getSortType());
            } else {
                sortedSql.append(sortingClause).append(" ").append(chartSetting.getSortType());
            }
            if (splitByColumn != null) {
                sortedSql.append(", ");
                sortedSql.append(splitColumn).append(" DESC ");
            }

            return " with tab1 as (" + parentQuery + ")\n " +
                    " , tab2 as (" + sortedSql + ")\n " +
                    " select t2.* from tab2 t2 join tab1 t1 on t2.\"" + xAxisColumn.getColumn() + "\" = t1.\"" + xAxisColumn.getColumn() + "\"";
        }

        return sqlQuery.toString();
    }

    public static String getKpiWidgetQuery(Integer companyId, ViewRpc viewRpc, ReportRpc report) {

        StringBuilder sqlQuery = new StringBuilder();
        String tempTableName = getTempTableName();
        sqlQuery.append(getCoreQueryPart(companyId, report, tempTableName, viewRpc));
        sqlQuery.append("   \n");
        sqlQuery.append("SELECT ");

        SerieConfItem kpiWidgetMetric = report.getKpiWidgetItem().getKpiWidgetMetric();
        String xAxisColumnGrouping = "";
        if (ChartTypeEnum.RANKING_KPI.equals(report.getKpiWidgetItem().getType()) && report.getKpiWidgetItem().getGroupingColumn() != null) {
            SerieColumn groupingColumn = report.getKpiWidgetItem().getGroupingColumn();
            ColumnRpc columnRpc = new ColumnRpc(groupingColumn.getColumn());
            columnRpc.setType(groupingColumn.getColumnType());
            columnRpc.setColumnFormat(groupingColumn.getColumnFormat());

            String xAxisColumnInQuery = "\"" + groupingColumn.getColumn().replace(".", "_") + "\"";

            xAxisColumnGrouping = xAxisColumnInQuery;

            if (groupingColumn.getColumnType().equals(SqlColumnType.DATE.getName())) {
                String customDateFormat = "YYYY-MM-DD";
                for (ColumnRpc rpc : report.getSelectedColumns()) {
                    if (rpc.getName().equals(groupingColumn.getColumn())) {
                        if (rpc.getCustomDateFormat() != null && !rpc.getCustomDateFormat().equals("long") && !rpc.getCustomDateFormat().equals("short")) {
                            customDateFormat = rpc.getCustomDateFormat();
                        }
                    }
                }
                xAxisColumnGrouping = getGroupByDateForChart(xAxisColumnGrouping, customDateFormat, report.getKpiWidgetItem().getDateSortPeriodType());
            }
            sqlQuery.append(" ").append(xAxisColumnGrouping).append(" as ").append(xAxisColumnInQuery);
            if (kpiWidgetMetric != null && kpiWidgetMetric.getSerieColumn().getColumn() != null) {
                sqlQuery.append(", ");
            }
        }


        if (kpiWidgetMetric != null && kpiWidgetMetric.getSerieColumn().getColumn() != null) {
            createTempColumn(sqlQuery, kpiWidgetMetric);
            sqlQuery.append(" as \"").append(kpiWidgetMetric.getAlias()).append("\" ");
        }

        sqlQuery.append(" FROM ").append(tempTableName).append(" \n");

        if (ChartTypeEnum.RANKING_KPI.equals(report.getKpiWidgetItem().getType()) && report.getKpiWidgetItem().getGroupingColumn() != null) {
            sqlQuery.append(" GROUP BY ").append(xAxisColumnGrouping);
            if (kpiWidgetMetric != null && kpiWidgetMetric.getSerieColumn().getColumn() != null) {
                sqlQuery.append(" ORDER BY ");
                String topOrLow = report.getKpiWidgetItem().getPageSizeType().equals(0) ? " desc " : " asc ";
                sqlQuery.append("\"").append(kpiWidgetMetric.getAlias()).append("\" ").append(topOrLow);
            }
            if (!report.getKpiWidgetItem().getPageSizeWithCustom().equals(0) && !report.getKpiWidgetItem().isOtherItems()) {
                sqlQuery.append(" limit ").append(report.getKpiWidgetItem().getPageSizeWithCustom());
            }

            StringBuilder wrapperQuery = new StringBuilder();
            wrapperQuery.append(" select ");
            wrapperQuery.append(xAxisColumnGrouping);
            if (kpiWidgetMetric != null && kpiWidgetMetric.getSerieColumn().getColumn() != null) {
                wrapperQuery.append(",");
                wrapperQuery.append(" \"").append(kpiWidgetMetric.getAlias()).append("\" ");
            }
            wrapperQuery.append(" from (").append(sqlQuery).append(") t ");
            if (ChartConfItem.BY_CATEGORY.equals(report.getKpiWidgetItem().getSortBy())) {
                wrapperQuery.append(" ORDER BY ");
                wrapperQuery.append(xAxisColumnGrouping).append(" ").append(report.getKpiWidgetItem().getSortType());
            } else if (kpiWidgetMetric != null && kpiWidgetMetric.getSerieColumn().getColumn() != null) {
                wrapperQuery.append(" ORDER BY ");
                wrapperQuery.append("\"").append(kpiWidgetMetric.getAlias()).append("\" ").append(report.getKpiWidgetItem().getSortType());
            }
            wrapperQuery.append(" ");
            return wrapperQuery.toString();
        }

        return sqlQuery.toString();
    }

    public static String getChartQueryForGauge(Integer companyId, ReportRpc report) {
        ViewRpc viewRpc = getViewParser(report.getViewCode());

        StringBuilder sqlQuery = new StringBuilder();
        String tempTableName = getTempTableName();
        sqlQuery.append(getCoreQueryPart(companyId, report, tempTableName, viewRpc));
        sqlQuery.append("   \n");
        sqlQuery.append("SELECT ");

        ChartConfItem chartSetting = report.getChartConf();

        if (chartSetting == null || chartSetting.getGaugeConfig() == null) {
            return "";
        }

        GaugeChartConfig gaugeConfig = chartSetting.getGaugeConfig();
        createTempColumn(sqlQuery, gaugeConfig.getGaugeSerie());
        sqlQuery.append(" as \"" + gaugeConfig.getGaugeSerie().getAlias() + "\" ");

        if (gaugeConfig.getGaugeMinColumn() != null) {
            SerieConfItem minSerie = new SerieConfItem();
            minSerie.setSerieColumn(gaugeConfig.getGaugeMinColumn());
            minSerie.setAggrType(SerieAggrTypeEnum.MIN);

            sqlQuery.append(", ");
            createTempColumn(sqlQuery, minSerie);
            sqlQuery.append(" as \"" + minSerie.getAlias() + "\" ");
        }

        if (gaugeConfig.getGaugeMaxColumn() != null) {
            SerieConfItem maxSerie = new SerieConfItem();
            maxSerie.setSerieColumn(gaugeConfig.getGaugeMaxColumn());
            maxSerie.setAggrType(SerieAggrTypeEnum.MAX);

            sqlQuery.append(", ");
            createTempColumn(sqlQuery, maxSerie);
            sqlQuery.append(" as \"" + maxSerie.getAlias() + "\" ");
        }
        sqlQuery.append(" FROM " + tempTableName + " \n");

        return sqlQuery.toString();
    }

    private static void reletedProjectFilter(StringBuilder sqlQuery, String reletedToProjectColumn, Integer projectId) {
        sqlQuery.append(" and ");
        sqlQuery.append(reletedToProjectColumn + "=" + projectId);
    }

    /**
     * Добавляеем join для подключения кастом таблиц
     *
     * @param sqlQuery  repodt sql  query
     * @param report    report
     * @param companyId company ID
     */
    private static void joinCustomFieldTable(StringBuilder sqlQuery, ReportRpc report, Integer companyId) {
        Set<String> useCustomColumn = new HashSet<>();
        for (ColumnRpc column : report.getSelectedColumns()) {
            if (column.getIsCustomField() && !useCustomColumn.contains(column.getPrefix())) {
                if (useCustomColumn.contains(column.getPrefix())) {
                    continue;
                }
                sqlQuery.append(" ").append(column.getCustomFieldJoin().replace("$", "\"" + companyId.toString() + "\"")).append(" ");
                useCustomColumn.add(column.getPrefix());
            }
        }
        for (ColumnRpc column : report.getFieldd()) {
            if (column.getIsCustomField() && !useCustomColumn.contains(column.getPrefix())) {
                if (useCustomColumn.contains(column.getPrefix())) {
                    continue;
                }
                sqlQuery.append(" ").append(column.getCustomFieldJoin().replace("$", "\"" + companyId.toString() + "\"")).append(" ");
                useCustomColumn.add(column.getPrefix());
            }
        }
    }

    public static String getReportFilterLists(Integer userId, Integer companyId, String searchKey, ReportRpc report, ColumnRpc column, boolean fullSearchKey) {
        ViewRpc viewRpc = getViewParser(report.getViewCode());
        report.setPosition(1);
        report.setLimit(20);
        StringBuilder sqlQuery = new StringBuilder();
        String columnname;
        String columnType;
        if (column.isTreeSelect() && !(column.getTable() == null || "".equals(column.getTable()) || column.getId() == null || "".equals(column.getId()) || column.getParent() == null || "".equals(column.getParent()) || column.getWhere() == null || "".equals(column.getWhere()) || column.getLookUpField() == null || "".equals(column.getLookUpField()))) {
            String replacement = column.getTable().replace("$", "\"" + companyId.toString() + "\"");
            sqlQuery.append("select getcascadeParentsnames('','" + replacement + "','" + column.getColumn() + "', '" + column.getId() + "','" + column.getParent() + "',' " + column.getWhere() + " ', -1,'" + (null != searchKey ? searchKey : "") + "','" + column.getSplitter() + "') ");
            sqlQuery.append("\"" + column.getColumn() + "\"");
            sqlQuery.append(" ORDER BY ");
            sqlQuery.append("\"" + column.getColumn() + "\"");
            sqlQuery.append(" ASC ");
            if (!report.isShowMailingList()) {
                getLimitClauseNewLogic(report, sqlQuery);
            }
            return sqlQuery.toString();
        } else {
            if (column.getLookupSql() != null && !"".equals(column.getLookupSql()) && column.getLookUpField() != null && !"".equals(column.getLookUpField())) {
                String replacement = column.getLookupSql().replace("$", "\"" + companyId.toString() + "\"");
                columnname = column.getLookUpField();
                columnType = column.getType();
                sqlQuery.append(replacement);

            } else {

                sqlQuery.append("SELECT");
                sqlQuery.append(" DISTINCT " + column.getName());
                String replacement = getQueriesWithJoins(viewRpc.getQueries(), viewRpc.getJoins(), report.getSelectedColumns());
                replacement = getCustomizeWhere(report, replacement);
                replacement = replacement.replace("$", companyId.toString());
                sqlQuery.append(" FROM " + replacement + " \n");
                joinCustomFieldTable(sqlQuery, report, companyId);
                getFilterOptions(userId, viewRpc, sqlQuery);
                if (report.isShowMailingList()) {
                    getWhereClauseNewLogic(report, sqlQuery, companyId);
                }

                columnname = column.getName();
                columnType = column.getType();
            }

            StringBuilder whereStatement = new StringBuilder(100);

            String WHERE_CODE = "{where}";
            whereTagReplacement(sqlQuery, new StringBuilder(), WHERE_CODE);
            WHERE_CODE = "{where2}";
            if (searchKey != null && !"".equals(searchKey)) {
                whereStatement.append(" AND ");
                if (!fullSearchKey) {
                    if (columnType.equals(SqlColumnType.NUMBER.getName()) || columnType.equals(SqlColumnType.MONEY.getName())) {
                        whereStatement.append(" to_char(").append(columnname).append(",'99999999999.0') LIKE '%'||?||'%'");
                    } else {
                        whereStatement.append("lower(").append(columnname).append(") ilike ?||'%'");
                    }
                } else {
                    whereStatement.append(" ? ");
                }
            }

            whereTagReplacement(sqlQuery, whereStatement, WHERE_CODE);

            sqlQuery.append(" ORDER BY ");
            sqlQuery.append(columnname);
            sqlQuery.append(" ASC ");
            if (!report.isShowMailingList()) {
                getLimitClauseNewLogic(report, sqlQuery);
            }
            String result = getCustomFilterApplied(sqlQuery.toString(), report);
            if (result != null && !"".equals(result)) {
                return result;
            } else {
                return sqlQuery.toString();
            }
        }
    }

    private static String createTempColumn(StringBuilder sqlQuery, SerieConfItem serieConf) {
        String selectColumn = "";
        String distinctString = serieConf.getUnique() ? "DISTINCT" : "";
        if (serieConf.getAggrType() != null) {
            selectColumn = " " + serieConf.getAggrType().getFunction() + "( " + distinctString + " \"" + serieConf.getSerieColumn().getColumn().replace(".", "_") + "\") ";
        } else {
            selectColumn = " \"" + serieConf.getSerieColumn().getColumn().replace(".", "_") + "\" ";
        }
        sqlQuery.append(selectColumn);

        return selectColumn;
    }

    private static ColumnRpc getColumnByName(String columnName, List<ColumnRpc> columns) {
        if (!StrUtils.isEmpty(columnName)) {
            for (ColumnRpc columnRpc : columns) {
                if (columnName.equals(columnRpc.getName())
                        || columnRpc.getName().equals(columnRpc.getPrefix() + "." + columnName)
                        || columnName.replace("_", ".").equals(columnRpc.getName().replace("_", "."))) {
                    return columnRpc;
                }
            }
        }
        return null;
    }

    private static String getQueriesWithJoins(String query, Map<String, String> joins, List<ColumnRpc> selectedColumns) {
        if (joins != null) {
            StringBuilder buffer = new StringBuilder(query);
            String joinQuery = "";
            String value;
            for (ColumnRpc rpc : selectedColumns) {
                value = joins.get(rpc.getTitle());
                if (joins.containsKey(rpc.getTitle()) && !joinQuery.contains(value)) {
                    joinQuery = value;
                    buffer.append(" ");
                    buffer.append(joinQuery);
                }
            }
            return buffer.toString();
        }
        return query;
    }

    /**
     * Replace custom filter variables to known values, kept in hash map from ReportRpc.getCustomFilter().
     *
     * @param query     Sql Query.
     * @param reportRpc Transer Object {@link ReportRpc}
     * @return
     */
    private static String getCustomFilterApplied(String query, ReportRpc reportRpc) {
        String result = query;
        String firstDate = "null";
        if (reportRpc.getCustomFilter() != null && !reportRpc.getCustomFilter().isEmpty()) {
            for (Map.Entry<String, String> entrySet : reportRpc.getCustomFilter().entrySet()) {
                if (DurationType.Equals.getName().equals(entrySet.getValue())) {
                    result = result.replace(entrySet.getKey().trim(), "'" + entrySet.getValue().trim() + "'");
                } else {
                    if (entrySet.getValue() != null && oneOfTheDurationType(entrySet.getValue())) {
                        for (DurationType durationType : DurationType.values()) {
                            if (durationType.getName().equals(entrySet.getValue())) {
                                if (!entrySet.getKey().equals("#!custom_end_date!#")) {
                                    result = result.replace(entrySet.getKey().trim(), DurationType.valueOf(durationType.toString()).getStartDate());
                                    result = result.replace("#!custom_end_date!#", DurationType.valueOf(durationType.toString()).getEndDate());
                                }

                            }

                        }
                    } else {
                        if (entrySet.getKey().equals("#!custom_start_date!#")) {
                            firstDate = entrySet.getValue();
                        }
                        if (entrySet.getValue() != null && "null".equals(entrySet.getValue().trim())) {
                            result = result.replace(entrySet.getKey().trim(), entrySet.getValue() != null ? entrySet.getValue().trim() : "null");
                        } else {
                            if ("''".equals(entrySet.getValue())) {
                                result = result.replace(entrySet.getKey().trim(), entrySet.getValue().trim());
                            } else {
                                result = result.replace(entrySet.getKey().trim(), entrySet.getValue() != null ? "'" + entrySet.getValue().trim() + "'" : "null");
                            }
                        }
                    }
                }

            }
        }
        result = result.replaceAll("\\#\\!(.*)\\!\\#", "''");
        result = result.replace("\\(date \\(''\\)\\)", "'" + firstDate + "'");
        return result;
    }

    public static boolean oneOfTheDurationType(String durationType) {
        boolean oneOfTheDurationType = false;
        for (DurationType type : DurationType.values()) {
            if (type.getName().equals(durationType)) {
                oneOfTheDurationType = true;
                break;
            }
        }
        return oneOfTheDurationType;
    }
}
