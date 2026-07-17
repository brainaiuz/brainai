package com.edatasite.workforce.gwt.chart.client.rpc;

import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;

public class ChartData implements IsSerializable {

    /**
     * configuration item for drawing chart
     */
    private ChartConfItem conf;

    /**
     * xAxis values {categories}
     */
    private LinkedList<String> categories;

    public HashMap<String, HashMap<String, Double>> getDrillSeries() {
        return drillSeries;
    }

    public void setDrillSeries(HashMap<String, HashMap<String, Double>> drillSeries) {
        this.drillSeries = drillSeries;
    }

    private HashMap<String, HashMap<String, Double>> drillSeries;

    /**
     * values of the selected series
     */
    private LinkedList<SerieData> series;
    private LinkedList<GanttSerieData> ganttSerieData;

    /**
     * generated title in server side
     */
    private Long title;

    private String textInCenter;

    private Number gaugeMinValue;
    private Number gaugeMaxValue;
    private Number gaugeActValue;

    private Number total;

    private Date ganttMinDate;
    private Date ganttMaxDate;

    public ChartConfItem getConf() {
        return conf;
    }

    public void setConf(ChartConfItem conf) {
        this.conf = conf;
    }

    public LinkedList<String> getCategories() {
        return categories;
    }

    public void setCategories(LinkedList<String> categories) {
        this.categories = categories;
    }

    public LinkedList<SerieData> getSeries() {

        if (series == null) {
            series = new LinkedList<>();
        }
        return series;
    }

    public void setSeries(LinkedList<SerieData> series) {
        this.series = series;
    }

    public LinkedList<GanttSerieData> getGanttSerieData() {
        if (ganttSerieData == null) {
            ganttSerieData = new LinkedList<>();
        }
        return ganttSerieData;
    }

    public void setGanttSerieData(LinkedList<GanttSerieData> ganttSerieData) {
        this.ganttSerieData = ganttSerieData;
    }

    public Long getTitle() {
        return title;
    }

    public void setTitle(Long title) {
        this.title = title;
    }

    public String getTextInCenter() {
        return textInCenter;
    }

    public void setTextInCenter(String textInCenter) {
        this.textInCenter = textInCenter;
    }

    public Number getGaugeMinValue() {
        return gaugeMinValue;
    }

    public void setGaugeMinValue(Number gaugeMinValue) {
        this.gaugeMinValue = gaugeMinValue;
    }

    public Number getGaugeMaxValue() {
        return gaugeMaxValue;
    }

    public void setGaugeMaxValue(Number gaugeMaxValue) {
        this.gaugeMaxValue = gaugeMaxValue;
    }

    public Number getGaugeActValue() {
        return gaugeActValue;
    }

    public void setGaugeActValue(Number gaugeActValue) {
        this.gaugeActValue = gaugeActValue;
    }

    public Date getGanttMinDate() {
        return ganttMinDate;
    }

    public void setGanttMinDate(Date ganttMinDate) {
        this.ganttMinDate = ganttMinDate;
    }

    public Date getGanttMaxDate() {
        return ganttMaxDate;
    }

    public void setGanttMaxDate(Date ganttMaxDate) {
        this.ganttMaxDate = ganttMaxDate;
    }

    public Number getTotal() {
        return total;
    }

    public void setTotal(Number total) {
        this.total = total;
    }
}
