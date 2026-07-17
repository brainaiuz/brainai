package com.edatasite.workforce.gwt.chart.client.enums;

import com.google.gwt.user.client.rpc.IsSerializable;

import java.io.Serializable;

public enum LegendPositionEnum implements IsSerializable, Serializable {

    TOP(1, "Top"),
    BOTTOM(2, "Bottom"),
    RIGHT(3, "Right");

    private int Id;
    private String title;

    LegendPositionEnum(int Id, String title) {
        this.Id = Id;
        this.title = title;
    }

    public int getId() {
        return Id;
    }

    public String getTitle() {
        return title;
    }

    public static LegendPositionEnum getById(Integer Id) {

        if (Id == null) {
            return BOTTOM;
        }

        for (LegendPositionEnum item : values()) {

            if (item.getId() == Id) {
                return item;
            }
        }
        return null;
    }
}
