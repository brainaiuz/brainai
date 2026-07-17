package com.edatasite.workforce.gwt.core.client.ui.components.form;

public enum GColumnEnum {
    COL("col"),
    COL_1("col-1"),
    COL_2("col-2"),
    COL_3("col-3"),
    COL_4("col-4"),
    COL_5("col-5"),
    COL_6("col-6"),
    COL_7("col-7"),
    COL_8("col-8"),
    COL_9("col-9"),
    COL_10("col-10"),
    COL_11("col-11"),
    COL_12("col-12"),
    COL_AUTO("col-auto");

    private String className;

    GColumnEnum(String className) {
        this.className = className;
    }

    public String getClassName() {
        return className;
    }
}
