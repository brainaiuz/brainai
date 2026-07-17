package com.edatasite.workforce.gwt.accounting.client.rpc;

import com.google.gwt.user.client.rpc.IsSerializable;

public class DeviceScreen implements IsSerializable {
    private Integer width;
    private Integer height;
    private Double scalingFactor;
    private Integer colourDepth;

    public static DeviceScreen newInstance(Integer width, Integer height, Double scalingFactor, Integer colourDepth) {
        return new DeviceScreen(width, height, scalingFactor, colourDepth);
    }

    public DeviceScreen(Integer width, Integer height, Double scalingFactor, Integer colourDepth) {
        this.width = width;
        this.height = height;
        this.scalingFactor = scalingFactor;
        this.colourDepth = colourDepth;
    }

    public Integer getWidth() {
        return width;
    }

    public Integer getHeight() {
        return height;
    }

    public Double getScalingFactor() {
        return scalingFactor;
    }

    public Integer getColourDepth() {
        return colourDepth;
    }
}
