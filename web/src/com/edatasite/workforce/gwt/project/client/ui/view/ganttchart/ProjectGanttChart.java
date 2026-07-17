package com.edatasite.workforce.gwt.project.client.ui.view.ganttchart;

import com.edatasite.workforce.gwt.chart.client.rpc.ChartData;
import com.edatasite.workforce.gwt.chart.client.rpc.GanttSerieData;
import com.edatasite.workforce.gwt.chart.client.utils.ChartUtils;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.task.client.rpc.TaskListItem;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.json.client.*;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import org.moxieapps.gwt.highcharts.client.Axis;

import java.util.List;
import java.util.Random;

/**
 * User: Abror Abdukadirov
 * Date: 03.05.2019 14:48
 */
public class ProjectGanttChart extends Composite {
    interface ProjectGanttChartUiBinder extends UiBinder<HTMLPanel, ProjectGanttChart> {
    }

    private static final ProjectGanttChartUiBinder ourUiBinder = GWT.create(ProjectGanttChartUiBinder.class);

    private final ChartData chartData;
    private List<SelectItem> columns;

    private final HTMLPanel rootPanel;
    private JavaScriptObject chart;

    public ProjectGanttChart(ChartData chartData, List<SelectItem> columns) {
        this.chartData = chartData;
        this.columns = columns;
        rootPanel = ourUiBinder.createAndBindUi(this);
        rootPanel.getElement().setId("gantt-container");
        initWidget(rootPanel);

        rootPanel.addAttachHandler(event -> {
            initialize();
        });
    }

    private void initialize() {
        this.chart = this.nativeInitialize(this.createNativeOptions());
    }

    private JavaScriptObject createNativeOptions() {
        JSONObject options = new JSONObject();

        options.put("subtitle", new JSONString(""));

        options.put("title", this.getTitleJSON());

        options.put("chart", this.getChartJSON());

        options.put("colors", this.getColorJSON());

        options.put("xAxis", this.getXAxisJSON());

        options.put("yAxis", this.getYAxisJSON());

        options.put("series", this.getSeriesJSON());

        options.put("credits", this.getCreditsJSON());

        options.put("exporting", this.getExportingJSON());

        return options.getJavaScriptObject();
    }

    private JSONObject getChartJSON() {
        JSONObject fontObject = new JSONObject();
        fontObject.put("fontFamily", new JSONString("Roboto"));

        JSONObject styleObject = new JSONObject();
        styleObject.put("style", fontObject);
        return styleObject;
    }

    private JSONArray getColorJSON() {
        JSONArray colorArray = new JSONArray();
        String[] colors = ChartUtils.getDefaultColors();
        for (int i = 0; i < colors.length; i++) {
            colorArray.set(i, new JSONString(colors[i]));
        }
        return colorArray;
    }

    private JSONObject getTitleJSON() {
        JSONObject titelObject = new JSONObject();
        titelObject.put("text", new JSONString(""));
        return titelObject;
    }

    private JSONObject getXAxisJSON() {
        JSONObject xAxisObject = new JSONObject();

        JSONObject formatObject = new JSONObject();
        formatObject.put("format", new JSONString("%a, %b %d %Y"));

        JSONObject labelObject = new JSONObject();
        labelObject.put("label", formatObject);

        xAxisObject.put("currentDateIndicator", labelObject);
        xAxisObject.put("tickPixelInterval", new JSONNumber(70));
        xAxisObject.put("max", new JSONNumber(chartData.getGanttMaxDate().getTime()));
        xAxisObject.put("max", new JSONNumber(chartData.getGanttMaxDate().getTime()));
        return xAxisObject;
    }

    private JSONObject getYAxisJSON() {
        JSONObject yAxisObject = new JSONObject();
        yAxisObject.put("type", new JSONString(Axis.Type.CATEGORY.toString()));

        JSONObject gridObject = new JSONObject();
        gridObject.put("enabled", JSONBoolean.getInstance(true));
        gridObject.put("borderWidth", new JSONNumber(1));

        JSONArray columnsArray = new JSONArray();
        columnsArray.set(0, this.getColumnItemObject("Task", "{point.name}"));
        if (this.columns != null && this.columns.size() > 0) {
            int columnY = 1;
            for (SelectItem column : this.columns) {
                String labelKey = null;
                switch (column.getDescription()) {
                    case TaskListItem.START_DATE:
                        labelKey = "{point.start:%e. %b. %Y}";
                        break;
                    case TaskListItem.END_DATE:
                        labelKey = "{point.end:%e. %b. %Y}";
                        break;
                    case TaskListItem.ESTIMATED:
                        labelKey = "{point.estimated}";
                        break;
                    case TaskListItem.HOUR_SPENT:
                        labelKey = "{point.hourSpent}";
                        break;
                    case TaskListItem.ACTUAL_HOURS_SPENT:
                        labelKey = "{point.actualHoursSpent}";
                        break;
                    case TaskListItem.OVERALL_STATUS_NAME:
                        labelKey = "{point.overallStatusName}";
                        break;
                    case TaskListItem.ACTUAL_START_DATE:
                        labelKey = "{point.actualStartDate:%e. %b. %Y}";
                        break;
                    case TaskListItem.ACTUAL_END_DATE:
                        labelKey = "{point.actualEndDate:%e. %b. %Y}";
                        break;
                    default:
                        break;
                }
                columnsArray.set(columnY++, this.getColumnItemObject(column.getName(), labelKey));
            }
        }
        gridObject.put("columns", columnsArray);

        yAxisObject.put("grid", gridObject);
        return yAxisObject;
    }

    private JSONObject getColumnItemObject(String title, String labels) {
        JSONObject columnItemObject = new JSONObject();

        JSONObject titleObject = new JSONObject();
        titleObject.put("text", new JSONString(title));
        titleObject.put("rotation", new JSONNumber(60));
        double y = -60;
        double x = -10;
        if (title.length() <= 5) {
            y = -15;
        } else if (title.length() <= 10) {
            y = -20;
        } else if (title.length() <= 15) {
            y = -35;
            x = -20;
        } else if (title.length() <= 20) {
            y = -45;
            x = -30;
        } else if (title.length() <= 25) {
            y = -55;
            x = -35;
        }
        titleObject.put("y", new JSONNumber(y));
        titleObject.put("x", new JSONNumber(x));

        JSONObject labelObject = new JSONObject();
        labelObject.put("format", new JSONString(labels));

        columnItemObject.put("title", titleObject);
        columnItemObject.put("labels", labelObject);
        return columnItemObject;
    }

    private JSONArray getSeriesJSON() {
        JSONArray seriesArray = new JSONArray();
        JSONObject seriesObject = new JSONObject();
        seriesObject.put("tooltip", this.getSeriesToolTipJSON());
        seriesObject.put("name", new JSONString(chartData.getConf().getTitle() == null ? "" : chartData.getConf().getTitle()));
        seriesObject.put("showInLegend", JSONBoolean.getInstance(false));
        seriesObject.put("data", new JSONArray());

        JSONArray dataArray = (JSONArray) seriesObject.get("data");
        int y = chartData.getGanttSerieData().size() - 1;
        int colorIndex = 0;
        for (int i = 0; i < chartData.getGanttSerieData().size(); i++) {
            GanttSerieData serieData = chartData.getGanttSerieData().get(i);
            JSONObject pointObject = new JSONObject();
            if (serieData.getId() != null) {
                pointObject.put("id", new JSONString(serieData.getId()));
            }
            if (serieData.getName() != null) {
                String name = serieData.getName();
                if (name.length() > 30) {
                    name = name.substring(0, 30) + "...";
                }
                pointObject.put("name", new JSONString("<a " +
                        " href=\"" + GWT.getHostPageBaseURL() + "ProjectManagement.html#task%7Csummary/" +
                        serieData.getId() + "/true\">" + name + "</a>"));
            }
            if (serieData.getEstimated() != null) {
                pointObject.put("estimated", new JSONString(Utils.formatMinutes(serieData.getEstimated())));
            }
            if (serieData.getHourSpent() != null) {
                pointObject.put("hourSpent", new JSONString(serieData.getHourSpent()));
            }
            if (serieData.getActualHoursSpent() != null) {
                pointObject.put("actualHoursSpent", new JSONString(serieData.getActualHoursSpent()));
            }
            if (serieData.getOverallStatusName() != null) {
                pointObject.put("overallStatusName", new JSONString(serieData.getOverallStatusName()));
            }
            if (serieData.getActualStartDate() != null) {
                pointObject.put("actualStartDate", new JSONNumber(serieData.getStart().getTime()));
            }
            if (serieData.getActualEndDate() != null) {
                pointObject.put("actualEndDate", new JSONNumber(serieData.getActualEndDate().getTime()));
            }
            if (serieData.getParent() != null) {
                pointObject.put("parent", new JSONString(serieData.getParent()));
                pointObject.put("color", new JSONString(ChartUtils.getDefaultColorsWithLowOpacity()[colorIndex]));
            } else {
                Random random = new Random();
                colorIndex = random.nextInt(ChartUtils.getDefaultColors().length);
                pointObject.put("color", new JSONString(ChartUtils.getDefaultColors()[colorIndex]));
            }
            if (serieData.getDependency() != null) {
                pointObject.put("dependency", new JSONString(serieData.getDependency()));
            }
            if (serieData.getStart() != null) {
                pointObject.put("start", new JSONNumber(serieData.getStart().getTime()));
            }
            if (serieData.getEnd() != null) {
                pointObject.put("end", new JSONNumber(serieData.getEnd().getTime()));
            }
            if (serieData.getMilestone() != null) {
                pointObject.put("milestone", JSONBoolean.getInstance(serieData.getMilestone().booleanValue()));
            }
            if (serieData.getPercent() != null) {
                JSONObject amountObject = new JSONObject();
                amountObject.put("amount", new JSONNumber(serieData.getPercent().doubleValue()));
                pointObject.put("completed", amountObject);
            }
            pointObject.put("y", new JSONNumber(y));
            y--;

            dataArray.set(i, pointObject);
        }
        seriesArray.set(0, seriesObject);
        return seriesArray;
    }

    private JSONObject getSeriesToolTipJSON() {
        JSONObject toolTipObject = new JSONObject();
        toolTipObject.put("dateTimeLabelFormat", new JSONString("%d-%m-%Y"));
        return toolTipObject;
    }

    private JSONObject getCreditsJSON() {
        JSONObject creditObject = new JSONObject();
        creditObject.put("enabled", JSONBoolean.getInstance(false));
        return creditObject;
    }

    private JSONObject getExportingJSON() {
        JSONObject exportingObject = new JSONObject();
        exportingObject.put("enabled", JSONBoolean.getInstance(true));
        exportingObject.put("sourceWidth", new JSONNumber(1500));
        return exportingObject;
    }

    public void setColumns(List<SelectItem> columns) {
        this.columns = columns;
    }

    private void setChart(JavaScriptObject chart) {
        this.chart = chart;
    }

    public HTMLPanel getRootPanel() {
        return rootPanel;
    }

    private native JavaScriptObject nativeInitialize(JavaScriptObject options) /*-{
        if ($wnd.$('#gantt-container')) {
            var that = this;
            setTimeout(function () {
                var chart = new $wnd.Highcharts.ganttChart('gantt-container', options);
                that.@com.edatasite.workforce.gwt.project.client.ui.view.ganttchart.ProjectGanttChart::setChart(Lcom/google/gwt/core/client/JavaScriptObject;)(chart);
            }, 1000);
        } else {
            return new $wnd.Highcharts.ganttChart('gantt-container', options);
        }
    }-*/;
}
