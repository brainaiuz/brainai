package com.edatasite.workforce.gwt.reportingsystem.client.enumerable.chart;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Created by IntelliJ IDEA.
 * User: Dilshod
 * Date: 17-Feb-2010
 * Time: 16:31:53
 * To change this template use File | Settings | File Templates.
 */
public enum ScatterChartTypeEnum implements IsSerializable {
    SCATTER("scatter"),
    SCATTER_LINE("scatter_line");

    ScatterChartTypeEnum(String scatterName) {
        this.scatterName = scatterName;
    }

    private String scatterName;

    ScatterChartTypeEnum() {
    }

    public String getScatterName() {
        return scatterName;
    }
}
