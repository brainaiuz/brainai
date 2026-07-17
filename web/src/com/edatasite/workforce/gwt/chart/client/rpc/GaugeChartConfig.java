package com.edatasite.workforce.gwt.chart.client.rpc;

import com.google.gwt.user.client.rpc.IsSerializable;

import java.io.Serializable;

public class GaugeChartConfig implements IsSerializable, Serializable {

    private SerieColumn gaugeMinColumn;
    private Double gaugeMinValue;

    private SerieColumn gaugeMaxColumn;
    private Double gaugeMaxValue;

    private SerieConfItem gaugeSerie;
    private String chartColor;

    public String getChartColor() {
        return chartColor;
    }

    public void setChartColor(String chartColor) {
        this.chartColor = chartColor;
    }

    public SerieColumn getGaugeMinColumn() {
        return gaugeMinColumn;
    }

    public void setGaugeMinColumn(SerieColumn gaugeMinColumn) {
        this.gaugeMinColumn = gaugeMinColumn;
    }

    public Double getGaugeMinValue() {
        return gaugeMinValue;
    }

    public void setGaugeMinValue(Double gaugeMinValue) {
        this.gaugeMinValue = gaugeMinValue;
    }

    public SerieColumn getGaugeMaxColumn() {
        return gaugeMaxColumn;
    }

    public void setGaugeMaxColumn(SerieColumn gaugeMaxColumn) {
        this.gaugeMaxColumn = gaugeMaxColumn;
    }

    public Double getGaugeMaxValue() {
        return gaugeMaxValue;
    }

    public void setGaugeMaxValue(Double gaugeMaxValue) {
        this.gaugeMaxValue = gaugeMaxValue;
    }

    public SerieConfItem getGaugeSerie() {
        return gaugeSerie;
    }

    public void setGaugeSerie(SerieConfItem gaugeSerie) {
        this.gaugeSerie = gaugeSerie;
    }
}
