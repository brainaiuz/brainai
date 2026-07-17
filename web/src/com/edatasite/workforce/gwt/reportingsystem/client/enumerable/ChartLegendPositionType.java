package com.edatasite.workforce.gwt.reportingsystem.client.enumerable;

import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;

/**
 * Created by IntelliJ IDEA.
 * User: Normurod Buriev
 * Date: Jan 4, 2011
 * Time: 2:19:31 PM
 * To change this template use File | Settings | File Templates.
 */
public enum ChartLegendPositionType {
    LEFT("Left", 1),
    RIGHT("Right", 2),
    CENTER("Center", 3);

    ChartLegendPositionType(String text, Integer value) {
        this.text = text;
        this.value = value;
    }

    private String text;
    private Integer value;

    public String getText() {
        return text;
    }

    public Integer getValue() {
        return value;
    }

    public static SelectItem[] getLegendPositions() {
        SelectItem[] items = new SelectItem[values().length];
        int i = 0;
        for (ChartLegendPositionType type : values()) {
            items[i++] = new SelectItem(type.getValue(), type.getText(), type.name());
        }
        return items;
    }
}
