package com.edatasite.workforce.gwt.core.client.ui.listpanel.savepanel;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.ui.components.form.FormGroup;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ColumnColor;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.google.gwt.core.client.GWT;
import gwt.material.design.client.ui.html.Div;

import java.util.List;

public class ColumnColorForOrgChart extends Div {
    private static final WfmStrings wfmStrings = WfmStrings.App.get();
    private final ColorWidget colorWidget;

    public ColumnColorForOrgChart(ColumnColor data, List<SelectItem> selectItems) {
        super("panel mt-1");

        Div body = new Div("panel__body");

        colorWidget = new ColorWidget();
        if (data != null) {
            colorWidget.setColor(data.getColor());
        }
        FormGroup colorWidgetFormGroup = new FormGroup(wfmStrings.color(), colorWidget);
        body.add(colorWidgetFormGroup);

        WfmButton2 removeButton = new WfmButton2(wfmStrings.delete(), WfmButton2.BTN_LIGHTGREY, WfmButton2.ICON_TRASH);
        removeButton.addStyleName("btn-block");
        removeButton.addClickHandler(event -> {
            ColumnColorForOrgChart.this.removeFromParent();
        });
        body.add(removeButton);
        add(body);
    }

    public ColumnColor getData() {
        ColumnColor result = null;
        try {
            if (!Utils.isNullOrEmpty(String.valueOf(colorWidget.getColor() != null))) {
                result = new ColumnColor(colorWidget.getColor());
                GWT.log("result ni color widgetga getColor");
            }
        } catch (Exception e) {
            GWT.log(e.getMessage());
        }
        return result;
    }
}
