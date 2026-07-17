package com.edatasite.workforce.gwt.dashboard.client.rpc;

import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: ${Dilsh0d}
 * Date: 29.10.2009
 * Time: 2:07:27
 * To change this template use File | Settings | File Templates.
 */
public class TimeSheetSummaryRps implements IsSerializable {

    private String label;
    private int max;
    private ArrayList<String> names;
    private double[][] timeSheetSummary;
    private double[] timesBy;

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
    }

    public ArrayList<String> getNames() {
        return names;
    }

    public void setNames(ArrayList<String> names) {
        this.names = names;
    }

    public double[][] getTimeSheetSummary() {
        return timeSheetSummary;
    }

    public void setTimeSheetSummary(double[][] timeSheetSummary) {
        this.timeSheetSummary = timeSheetSummary;
    }

    public double[] getTimesBy() {
        return timesBy;
    }

    public void setTimesBy(double[] timesBy) {
        this.timesBy = timesBy;
    }
}
