package com.edatasite.workforce.gwt.chart.client.rpc;

import com.edatasite.workforce.gwt.reportingsystem.client.enumerable.ChartSizeType;
import com.google.gwt.user.client.rpc.IsSerializable;

import java.io.Serializable;

public class ChartSizeItem implements IsSerializable, Serializable {

    private ChartSizeType type;

    private Integer width;
    private Integer height;

    public ChartSizeItem() {

    }

    public ChartSizeType getType() {
        return type;
    }

    public void setType(ChartSizeType type) {
        this.type = type;
    }

    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }
}
