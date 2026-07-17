package com.edatasite.workforce.gwt.ganttchart.client;

import com.google.gwt.i18n.client.NumberFormat;

/**
 * Created with IntelliJ IDEA.
 * User: Ilxom Lutfullaev
 * Date: 21.02.13
 * Time: 16:53
 * To change this template use File | Settings | File Templates.
 */
public interface Constants {
    NumberFormat defaultNumberFormat = NumberFormat.getFormat(",##0.00");

    String ganttTypeDaily = "DAILY";
    String ganttTypeWeekly = "WEEKLY";
    String ganttTypeMonthly = "MONTHLY";
    String SET_PREDECESSOR = "SET_PREDECESSOR";
    String SET_SUCCESSOR = "SET_SUCCESSOR";

    String rowsHeight = "24px";
    int cellSize = 24;
    int headerHeight = 40;
    int taskWidgetHeight = 13;
}
