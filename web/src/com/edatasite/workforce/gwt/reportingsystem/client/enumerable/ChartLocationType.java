package com.edatasite.workforce.gwt.reportingsystem.client.enumerable;

import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;

/**
 * Created by IntelliJ IDEA.
 * User: Normurod Buriev
 * Date: Jan 4, 2011
 * Time: 2:14:54 PM
 * To change this template use File | Settings | File Templates.
 */
public enum ChartLocationType {

    TOP_OF_REPORT("Top Of Report", 1),
    BOTTOM_OF_REPORT("Bottom Of Report", 2);

    ChartLocationType(String text, Integer value) {
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

    public static SelectItem[] getLocations() {
        SelectItem[] items = new SelectItem[values().length];
        int i = 0;
        for (ChartLocationType type : values()) {
            items[i++] = new SelectItem(type.getValue(), type.getText(), type.name());
        }
        return items;
    }
}
