package com.edatasite.workforce.gwt.core.client.enums;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Created by Hurshid on 2/16/2018.
 */
public enum ColumnType implements IsSerializable {

    COL_1("col-12"),
    COL_2("col-6"),
    COL_3("col-4"),
    COL_4("col-3");

    String style;

    ColumnType(String style) {
        this.style = style;
    }

    public String getStyle() {
        return style;
    }
}
