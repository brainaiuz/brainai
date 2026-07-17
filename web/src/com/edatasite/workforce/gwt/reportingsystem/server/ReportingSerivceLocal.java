package com.edatasite.workforce.gwt.reportingsystem.server;

import com.edatasite.workforce.gwt.chart.client.rpc.ChartData;
import com.edatasite.workforce.gwt.chart.client.rpc.KpiWidgetData;
import com.edatasite.workforce.gwt.core.client.rpc.dashboard.DashboardComponentItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.reportingsystem.client.rpc.ReportRpc;
import com.edatasite.workforce.gwt.reportingsystem.client.rpc.SelectListRpc;

public interface ReportingSerivceLocal {

    ChartData getReportChartData(DashboardComponentItem gridItemConfig);

    KpiWidgetData getKpiWidgetData(DashboardComponentItem gridItemConfig);

    ReportRpc getReport(Integer id);

    ListResult<SelectListRpc> getReports(ListingFilterParameter parameter);

    byte[] getReportFile(Integer reportId,String type);

}
