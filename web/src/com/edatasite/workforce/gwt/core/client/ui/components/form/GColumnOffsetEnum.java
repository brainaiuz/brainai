package com.edatasite.workforce.gwt.core.client.ui.components.form;

public enum  GColumnOffsetEnum {

    OFFSET_1("offset-1"),
    OFFSET_2("offset-2"),
    OFFSET_3("offset-3"),
    OFFSET_4("offset-4"),
    OFFSET_5("offset-5"),
    OFFSET_6("offset-6"),
    OFFSET_7("offset-7"),
    OFFSET_8("offset-8"),
    OFFSET_9("offset-9"),
    OFFSET_10("offset-10"),
    OFFSET_11("offset-11");

    private String className;

    GColumnOffsetEnum(String className) {
        this.className = className;
    }

    public String getClassName() {
        return className;
    }
}
