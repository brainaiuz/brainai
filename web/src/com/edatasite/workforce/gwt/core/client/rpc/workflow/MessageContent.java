package com.edatasite.workforce.gwt.core.client.rpc.workflow;

import java.util.ArrayList;
import java.util.HashMap;

public class MessageContent {
    private String message;
    private byte[] chart;
    private String kpiWidget;
    private String xlsCaption;
    private String csvCaption;
    private String pdfCaption;
    private String chartCaption;
    private String pdfError;
    private String xlsError;
    private String csvError;
    private ArrayList<String> summary = new ArrayList<>();
    private ArrayList<HashMap<String, byte[]>> files = new ArrayList<>();

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public byte[] getChart() {
        return chart;
    }

    public void setChart(byte[] chart) {
        this.chart = chart;
    }

    public String getKpiWidget() {
        return kpiWidget;
    }

    public void setKpiWidget(String kpiWidget) {
        this.kpiWidget = kpiWidget;
    }

    public ArrayList<HashMap<String, byte[]>> getFiles() {
        return files;
    }

    public void addToFiles(HashMap<String, byte[]> file) {
        files.add(file);
    }

    public String getXlsCaption() {
        return xlsCaption;
    }

    public void setXlsCaption(String xlsCaption) {
        this.xlsCaption = xlsCaption;
    }

    public String getCsvCaption() {
        return csvCaption;
    }

    public void setCsvCaption(String csvCaption) {
        this.csvCaption = csvCaption;
    }

    public String getPdfCaption() {
        return pdfCaption;
    }

    public void setPdfCaption(String pdfCaption) {
        this.pdfCaption = pdfCaption;
    }

    public String getChartCaption() {
        return chartCaption;
    }

    public void setChartCaption(String chartCaption) {
        this.chartCaption = chartCaption;
    }

    public String getPdfError() {
        return pdfError;
    }

    public void setPdfError(String pdfError) {
        this.pdfError = pdfError;
    }

    public String getXlsError() {
        return xlsError;
    }

    public void setXlsError(String xlsError) {
        this.xlsError = xlsError;
    }

    public String getCsvError() {
        return csvError;
    }

    public void setCsvError(String csvError) {
        this.csvError = csvError;
    }

    public ArrayList<String> getSummary() {
        return summary;
    }
}
