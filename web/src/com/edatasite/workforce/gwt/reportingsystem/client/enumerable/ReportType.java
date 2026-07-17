package com.edatasite.workforce.gwt.reportingsystem.client.enumerable;

/**
 * User: ${Dilsh0d}
 * Date: 09-Mar-2010
 * Time: 20:48:43
 * <p/>
 * <br/> This Enum uses for Report type
 */
public enum ReportType {
    TABULAR(0),
    SUMMARY(1);

    ReportType(int type) {
        this.type = type;
    }

    private int type;

    public int getType() {
        return type;
    }

    public static ReportType get(String name) {
        for (ReportType item : values()) {
            if (item.name().equals(name)) {
                return item;
            }
        }
        return null;
    }
}
