package com.edatasite.workforce.gwt.chart.client.charts;

import com.edatasite.workforce.gwt.chart.client.rpc.ChartData;
import com.edatasite.workforce.gwt.chart.client.rpc.GanttSeriesDataWithChild;
import com.edatasite.workforce.gwt.chart.client.rpc.SerieData;
import com.edatasite.workforce.gwt.chart.client.utils.ChartUtils;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.ui.entryPoints.carcass.MainLayout;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Timer;
import org.moxieapps.gwt.highcharts.client.*;
import org.moxieapps.gwt.highcharts.client.labels.DataLabels;
import org.moxieapps.gwt.highcharts.client.plotOptions.PlotOptions;

import java.util.Random;

public class KpiGanttChartLeave extends BaseChart<KpiGanttChartLeave> {

    private static final WfmStrings wfmStrings = WfmStrings.App.get();


    public KpiGanttChartLeave(ChartData chartData) {
        super();

        MainLayout.get().setSideNavResizeCommand(() -> {
            if (isAttached()) {
                Timer timer = new Timer() {
                    @Override
                    public void run() {
                        reflow();
                    }
                };
                timer.schedule(320);
            }
        });

        //default color libraries
        setColors(ChartUtils.getDefaultColors());

        //configure chart title
        setChartTitle(new ChartTitle()
                .setText("")
                .setStyle(new Style().setColor(ChartUtils.DEFAULT_TEXT_COLOR))
                .setX(-20)); //set title on center
        setStyle(new Style().setFontFamily("Roboto"));

        //configure subtitle of the chart


        setChartSubtitle(new ChartSubtitle()
                .setText(chartData.getConf().getTitle())
                .setStyle(new Style().setColor(ChartUtils.DEFAULT_TEXT_COLOR))
                .setStyle(new Style().setOption("display", "none"))
                .setX(-20)); //set subtitle on center

        setHeight((chartData.getSeries().size() * 50 + 120) + "px");

        //do not show chart manufacture {in this case hide "Highchart.com"}
        setCredits(new Credits().setEnabled(false));

        Exporting exporting = new Exporting();
        exporting.setEnabled(true);
        exporting.setSourceWidth(100);
        setExporting(exporting);

        initialize(chartData);


    }

    protected void initialize(ChartData chartData) {
        Point[] points = new Point[chartData.getSeries().size()];
        String departmentChart = null;
        int length = chartData.getSeries().size();
        int colorIndex = 0;
        for (int i = 0; i < length; i++) {
            SerieData serieData = chartData.getSeries().get(i);
            Point point = new Point();
            if (serieData.getId() != null) {
                point = new Point(Integer.valueOf(serieData.getId()));
            }

            if (serieData.getId() != null) point.setOption("id", serieData.getId());
            if (serieData.getName() != null) {
                String employeeName = serieData.getName().length() > 40 ? serieData.getName().substring(0, 40).concat("...") : serieData.getName();
                if (serieData.getParent() == null) {
                    if (serieData instanceof GanttSeriesDataWithChild) {
                        Integer childCount = ((GanttSeriesDataWithChild) serieData).getChildCount();
                        Integer member = ((GanttSeriesDataWithChild) serieData).getMember();
                        Double aDouble = Double.valueOf(childCount) / Double.valueOf(member);
                        childCount = childCount + 1;
                        departmentChart = wfmStrings.discount() + " : " + childCount + "   /    " + wfmStrings.headCount() + "  :  " + member + "   /    " + wfmStrings.percentage() + "  :  " + (Math.ceil(aDouble)) + "%";
                    }
                    point.setOption("name", employeeName);
                } else {
                    point.setOption("name", "<a " +
                            " href=\"" + GWT.getHostPageBaseURL() + "Hrms.html#leaverequest/" +
                            serieData.getId() + "/\">" + employeeName + "</a>");
                }
            }
            if (serieData.getParent() != null) {
                point.setOption("parent", serieData.getParent());
                point.setColor(ChartUtils.getDefaultColorsWithLowOpacity()[colorIndex]);
            } else {
                Random random = new Random();
                colorIndex = random.nextInt(ChartUtils.getDefaultColors().length);
                point.setColor(ChartUtils.getDefaultColors()[colorIndex]);

            }
            if (serieData.getDependency() != null) point.setOption("dependency", serieData.getDependency());
            if (serieData.getStart() != null) point.setOption("start", serieData.getStart().getTime());
            if (serieData.getEnd() != null) point.setOption("end", serieData.getEnd().getTime());
            if (serieData.getMilestone() != null) point.setOption("milestone", serieData.getMilestone());
            if (serieData.getStart() != null && serieData.getEnd() != null) {

                double difference = Double.parseDouble(serieData.getDependency());

                point.setOption("difference", difference);
            } else if (serieData.getParent() == null && departmentChart != null) {
                point.setOption("difference", departmentChart);
            }
//            if (serieData.getPercent() != null) point.setOption("partialFill/amount", serieData.getName());

            points[i] = point;
        }

        PlotOptions plot = new PlotOptions() {
        };

        plot = plot.setDataLabels(new DataLabels().setFormat("{point.difference}"));

        ToolTip toolTip = new ToolTip();
        toolTip.setOption("dateTimeLabelFormat", "%d-%m-%Y");
        addSeries(createSeries()
                .setPlotOptions(plot)
                .setToolTip(toolTip)
                .setOption("showInLegend", false)
                .setName("")
                .setPoints(points));
        getXAxis().setOption("max", chartData.getGanttMaxDate().getTime());
        getYAxis().setOption("name", 100);

    }


    @Override
    protected String getChartTypeName() {
        return "GanttChart";
    }
}
