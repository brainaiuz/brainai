package com.edatasite.workforce.gwt.chart.client.enums;

import com.google.gwt.user.client.rpc.IsSerializable;

import java.io.Serializable;

public enum SerieAggrTypeEnum implements Serializable, IsSerializable {
    SUM(1, "Sum", "sum"),
    COUNT(2, "Count", "count"),
    AVG(3, "Average", "avg"),
    MAX(4, "Max.", "max"),
    MIN(5, "Min.", "min");

    private int Id;
    private String title;
    private String function;

    SerieAggrTypeEnum(int Id, String title, String function) {
        this.Id = Id;
        this.title = title;
        this.function = function;
    }

    public int getId() {
        return Id;
    }

    public String getTitle() {
        return title;
    }

    public String getFunction() {
        return function;
    }

    public static SerieAggrTypeEnum getById(int Id) {

        for (SerieAggrTypeEnum type : SerieAggrTypeEnum.values()) {

            if (type.getId() == Id) {
                return type;
            }
        }
        return null;
    }
}
