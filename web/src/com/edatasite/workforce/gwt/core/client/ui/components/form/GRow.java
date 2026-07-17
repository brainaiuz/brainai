package com.edatasite.workforce.gwt.core.client.ui.components.form;

import gwt.material.design.client.ui.html.Div;

import java.util.stream.Stream;

public class GRow extends Div {

    public GRow() {
        super("grid-row");
    }


    public GRow(GColumn... columns) {
        this();

        if (columns != null && columns.length > 0) {
            for (GColumn column : columns)
                add(column);
        }
    }

    public void add(GColumn column) {
        super.add(column);
    }

    public void addAll(GColumn... columns) {
        if (columns != null && columns.length > 0) {
            Stream.of(columns).forEachOrdered(column -> add(column));
        }
    }
}
