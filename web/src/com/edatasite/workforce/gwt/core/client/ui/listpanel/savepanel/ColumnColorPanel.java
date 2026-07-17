package com.edatasite.workforce.gwt.core.client.ui.listpanel.savepanel;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.edatasite.workforce.gwt.core.client.ui.components.form.FormGroup;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ColumnColor;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.constants.TextAlign;
import gwt.material.design.client.ui.html.Div;
import gwt.material.design.client.ui.html.Icon;
import gwt.material.design.client.ui.html.Span;

public class ColumnColorPanel extends Div {
    private static final WfmStrings wfmStrings = WfmStrings.App.get();
    private final DataListBox brushType;
    private final TextBox conditionTextBox;
    private final ColorWidget colorWidget;
    private final Div preview;
    private final Div previewWrapper;

    public ColumnColorPanel(ColumnColor data) {
        super("panel mt-1");

        Div body = new Div("panel__body");

        conditionTextBox = new TextBox();

        FormGroup match = new FormGroup(wfmStrings.match(), conditionTextBox);

        preview = new Div("color-panel-preview");
        previewWrapper = new Div();
        previewWrapper.setTextAlign(TextAlign.CENTER);
        previewWrapper.add(preview);
        body.add(previewWrapper);
        brushType = new DataListBox();
        brushType.setWithoutNullLabel(true);
        brushType.setItems(getBrushTypes());
        brushType.addValueChangeHandler((event) -> setColumnText());

        brushType.setItems(getBrushTypes());

        FormGroup brush = new FormGroup(wfmStrings.targetToBrush(), brushType);
        body.add(wrapIntoFormRow(match, brush));

        colorWidget = new ColorWidget();
        colorWidget.setChangeHandler(() -> setColumnText());
        if (data != null) {
            if (data.getCondition() != null) {
                conditionTextBox.setText(data.getCondition());
            }
            if (data != null && data.getTarget() != null) {
                brushType.setSelectedByDescription(data.getTarget());
            }
            colorWidget.setColor(data.getColor());
        }
        FormGroup colorWidgetFormGroup = new FormGroup(wfmStrings.color(), colorWidget);
        body.add(colorWidgetFormGroup);

        WfmButton2 removeButton = new WfmButton2(wfmStrings.delete(), WfmButton2.BTN_LIGHTGREY, WfmButton2.ICON_TRASH);
        removeButton.addStyleName("btn-block");
        removeButton.addClickHandler(event -> {
            ColumnColorPanel.this.removeFromParent();
        });
        body.add(removeButton);

        add(body);
        setColumnText();
    }

    public ColumnColor getData() {
        ColumnColor result = null;
        if (!Utils.isNullOrEmpty(conditionTextBox.getValue()) && brushType.getSelectedItem(true) != null && colorWidget.getColor() != null) {
            result = new ColumnColor(conditionTextBox.getValue(), brushType.getSelectedItem(true).getDescription(), colorWidget.getColor());
        }
        return result;
    }

    private void setColumnText() {
        preview.clear();
        Div row = new Div("row-decorator");
        Div tickDiv = new Div();
        tickDiv.getElement().setInnerHTML("<label class=\"gwt-control control control--checkbox\"><input type=\"checkbox\" disabled=\"disabled\"><span class=\"control__indicator\"></span></label>");
        Div anchDiv = new Div();
        anchDiv.getElement().setInnerHTML("<a class=\"action-listing ficon--more-horiz\" href=\"javascript:;\"></a>");
        Div previewDiv = new Div();
        Div previewValue = new Div("cell-decorator");
        previewDiv.add(previewValue);
        if (!Utils.isNullOrEmpty(colorWidget.getColor())) {
            String color = colorWidget.getColor();
            color = color.replace("#", "");
            if (brushType.getSelectedItem(true).getId().equals(1)) {
                int[] colors = Utils.convertHexToRGB(color);
                row.getElement().getStyle().setProperty("background", "linear-gradient(to right, rgba(" + colors[0] + ", " + colors[1] + ", " + colors[2] + ", 0.4), rgba(" + colors[0] + ", " + colors[1] + ", " + colors[2] + ", 0))");
                row.getElement().getStyle().setColor("#" + color);
            } else if (brushType.getSelectedItem(true).getId().equals(2)) {
                Icon iColor = new Icon();
                iColor.getElement().getStyle().setBackgroundColor("#" + color);
                previewValue.add(iColor);
                row.getElement().getStyle().setColor("transparent");
            } else if (brushType.getSelectedItem(true).getId().equals(3)) {
                previewValue.getElement().getStyle().setColor("#" + color);
                row.getElement().getStyle().setColor("transparent");
            }
        }
        Span cellValue = new Span(wfmStrings.preview());
        cellValue.addStyleName("text-holder");
        previewValue.add(cellValue);
        preview.add(row);
        preview.add(tickDiv);
        preview.add(anchDiv);
        preview.add(previewDiv);
    }

    private SelectItem[] getBrushTypes() {
        SelectItem[] colorizeItems = new SelectItem[3];
        colorizeItems[0] = new SelectItem(1, wfmStrings.row(), "r");
        colorizeItems[1] = new SelectItem(2, wfmStrings.cell(), "c");
        colorizeItems[2] = new SelectItem(3, wfmStrings.text(), "w");
        return colorizeItems;
    }

    private Div wrapIntoFormRow(Widget... widgets) {
        Div row = new Div("form-row");
        for (Widget widget : widgets) {
            Div col6 = new Div("col-6");
            col6.add(widget);
            row.add(col6);
        }
        return row;
    }
}
