package com.edatasite.workforce.gwt.chart.client.enums;

import com.google.gwt.user.client.rpc.IsSerializable;

import java.io.Serializable;

public enum StackedEnum implements Serializable, IsSerializable {
    BY_VALUE(1, "Value"),
    BY_PERCENT(2, "Percentage"),
    BY_PERCENTANDVALUE(3, "Value/Percentage");

    private int Id;
    private String title;

    StackedEnum(int Id, String title) {
        this.Id = Id;
        this.title = title;
    }

    public int getId() {
        return Id;
    }

    public String getTitle() {
        return title;
    }

    public static StackedEnum getById(Integer Id) {

        if (Id == null) {
            return BY_VALUE;
        }

        for (StackedEnum item : values()) {

            if (item.getId() == Id) {
                return item;
            }
        }

        return null;
    }
}
