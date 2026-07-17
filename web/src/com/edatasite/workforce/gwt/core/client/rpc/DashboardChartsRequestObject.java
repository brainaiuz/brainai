package com.edatasite.workforce.gwt.core.client.rpc;

import java.util.HashMap;

public class DashboardChartsRequestObject extends RequestObject {
    private HashMap<String, String[]> charts;

    public DashboardChartsRequestObject(HashMap<String, String[]> charts) {
        this.charts = charts;
    }

    public DashboardChartsRequestObject(boolean isLandscape) {
        setIS_LANDSCAPE(isLandscape);
    }

    public DashboardChartsRequestObject() {
    }

    public HashMap<String, String[]> getCharts() {
        return charts;
    }

    public void setCharts(HashMap<String, String[]> charts) {
        this.charts = charts;
    }
}
