package com.edatasite.workforce.gwt.core.client.ui.components.form;

import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.ui.html.Div;

public class GColumn extends Div {

    public GColumn(FormGroup... fgourps) {
        super("col");
        add(fgourps);
    }

    public GColumn(GColumnEnum columnCount, FormGroup... fgroups) {
        super(columnCount != null ? columnCount.getClassName() : "col");

        add(fgroups);
    }

    public GColumn(GColumnEnum columnCount, Widget... fgroups) {
        super(columnCount != null ? columnCount.getClassName() : "col");

        addWidget(fgroups);
    }

    public void add(FormGroup... fgourps) {

        if (fgourps != null && fgourps.length > 0) {
            for (FormGroup fgroup : fgourps)
                add(fgroup);
        }
    }

    public void addWidget(Widget... fgourps) {

        if (fgourps != null && fgourps.length > 0) {
            for (Widget fgroup : fgourps)
                add(fgroup);
        }
    }

    public void setOffset(GColumnOffsetEnum offset) {
        addStyleName(offset.getClassName());
    }
}
