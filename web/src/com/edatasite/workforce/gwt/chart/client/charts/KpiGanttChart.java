package com.edatasite.workforce.gwt.chart.client.charts;

import com.edatasite.workforce.gwt.chart.client.rpc.ChartData;
import com.edatasite.workforce.gwt.chart.client.rpc.SerieData;
import com.edatasite.workforce.gwt.chart.client.utils.ChartUtils;
import com.edatasite.workforce.gwt.core.client.ui.entryPoints.carcass.MainLayout;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Timer;
import org.moxieapps.gwt.highcharts.client.*;

import java.util.Random;

public class KpiGanttChart extends BaseChart<KpiGanttChart> {

    public KpiGanttChart(ChartData chartData) {
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
        exporting.setSourceWidth(1500);
        setExporting(exporting);

        initialize(chartData);
    }

    protected void initialize(ChartData chartData) {
        Point[] points = new Point[chartData.getSeries().size()];

        int length = chartData.getSeries().size();
        int colorIndex = 0;
        for (int i = 0; i < length; i++) {
            Point point = new Point();
            SerieData serieData = chartData.getSeries().get(i);
            if (serieData.getId() != null) point.setOption("id", serieData.getId());
            if (serieData.getName() != null) {
                String taskName = serieData.getName().length() > 40 ? serieData.getName().substring(0,40).concat("...") : serieData.getName();
                if (serieData.getParent() == null) point.setOption("name", taskName);
                else {
                    point.setOption("name", "<a " +
                            " href=\"" + GWT.getHostPageBaseURL() + "ProjectManagement.html#task%7Csummary/" +
                            serieData.getId() + "/true\">" + taskName + "</a>");
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
            if (serieData.getPercent() != null) point.setOption("completed/amount", serieData.getPercent());

            points[i] = point;
        }

        ToolTip toolTip = new ToolTip();
        toolTip.setOption("dateTimeLabelFormat", "%d-%m-%Y");

        addSeries(createSeries()
                .setToolTip(toolTip)
                .setOption("showInLegend", false)
                .setName("")
                .setPoints(points));
        getXAxis().setOption("max", chartData.getGanttMaxDate().getTime());
    }


    @Override
    protected String getChartTypeName() {
        return "GanttChart";
    }
}
