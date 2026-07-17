package com.edatasite.workforce.gwt.dashboardwidget.client.view.dynamicwidget;

import com.edatasite.workforce.gwt.chart.client.rpc.KpiWidgetData;

public class WidgetRequest {

    private KpiWidgetData kpiWidgetData;
    private Integer id;

    public WidgetRequest(KpiWidgetData kpiWidgetData, Integer id) {
        this.kpiWidgetData = kpiWidgetData;
        this.id = id;
    }

    public KpiWidgetData getKpiWidgetData() {
        return kpiWidgetData;
    }


    public Integer getId() {
        return id;
    }

}
