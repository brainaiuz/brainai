package com.edatasite.workforce.gwt.reportingsystem.client.enumerable.chart;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Created by IntelliJ IDEA.
 * User: Dilshod
 * Date: 15.12.2009
 * Time: 15:53:14
 * To change this template use File | Settings | File Templates.
 */
public enum BarChartTypeEnum implements IsSerializable {
    NORMAL("bar"),
    THREED("bar_3d"),
    GLASS("bar_glass"),
    CYLINDER("bar_cylinder"),
    CYLINDER_OUT_LINE("bar_cylinder_outline"),
    ROUND_GLASS("bar_round_glass"),
    ROUND("bar_round"),
    DOME("bar_dome");

    private String style;



    BarChartTypeEnum(String style) {
        this.style = style;
    }

    BarChartTypeEnum() {
    }

    public String getStyle() {
        return style;
    }
}
