package com.edatasite.workforce.gwt.reportingsystem.client.enumerable.chart;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Created by IntelliJ IDEA.
 * User: Dilshod
 * Date: 16.12.2009
 * Time: 22:35:18
 * To change this template use File | Settings | File Templates.
 */
public enum LineChartTypeEnum  implements IsSerializable {
    LINE("line"),
    LINE_HOLLOW("line_hollow"),
    LINE_DOT("line_dot"),

    // For Area Chart
    AREA_LINE("area_line"),
    AREA_HOLLOW("area_hollow");

    private String type;

    LineChartTypeEnum(String type) {
        this.type = type;
    }

    LineChartTypeEnum() {
    }

    public String getType() {
        return type;
    }
}