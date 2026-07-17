package com.edatasite.workforce.gwt.core.client.ui.listpanel.savepanel;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.edatasite.workforce.gwt.core.client.ui.components.form.FormGroup;
import com.edatasite.workforce.gwt.core.client.ui.components.form.InputGroup;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ColumnColor;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.ui.html.Div;

public class ReportingColumnColorPanel extends Div {
    private static final WfmStrings wfmStrings = WfmStrings.App.get();
    /// condition types
    private static final String GREATER_THEN = "GREATER_THEN";
    private static final String EQUAL_TO = "EQUAL_TO";
    private static final String BETWEEN = "BETWEEN";
    private static final String LESS_THAN = "LESS_THAN";
    ///  color type
    private static final String AUTOMATIC = "AUTOMATIC";
    private static final String CUSTOM_COLOR = "CUSTOM_COLOR";
    private final FormGroup colorWidgetFormGroup;
    private final DataListBox conditions;
    private final TextBox conditionTextBox;
    private final TextBox betweenSecondValueTextBox;
    private final ColorWidget colorWidget;
    private FormGroup colorTypeFormGroup;
    private DataListBox colorType;
//    private final Div preview;
//    private final Div previewWrapper;


    public ReportingColumnColorPanel() {
        this(null);
    }

    public ReportingColumnColorPanel(ColumnColor data) {
        super("panel mt-1");

        Div body = new Div("panel__body");

        conditionTextBox = new TextBox();
        betweenSecondValueTextBox = new TextBox();
        Validation.addNumericKeyboardListener(conditionTextBox, 4, true);
        Validation.addNumericKeyboardListener(betweenSecondValueTextBox, 4, true);
        betweenSecondValueTextBox.setVisible(false);
        InputGroup valueInputBox = new InputGroup(conditionTextBox, betweenSecondValueTextBox);
        FormGroup match = new FormGroup(wfmStrings.value(), valueInputBox);

        conditions = new DataListBox();
        conditions.setWithoutNullLabel(true);
        conditions.setItems(getConditionTypes());
        conditions.addValueChangeHandler(handler -> {
            if (conditions.getSelectedItem(true) != null && BETWEEN.equalsIgnoreCase(conditions.getSelectedItem(true).getDescription())) {
                betweenSecondValueTextBox.setVisible(true);
                betweenSecondValueTextBox.setPlaceHolder(wfmStrings.to() + " ...");
                conditionTextBox.setPlaceHolder(wfmStrings.from() + " ...");
            } else {
                betweenSecondValueTextBox.setVisible(false);
                conditionTextBox.setPlaceHolder("");
            }
        });

        FormGroup brush = new FormGroup(wfmStrings.condition(), conditions);
        body.add(wrapIntoFormRow(brush, match));
        setColorTypeWidget(body, data);
        colorWidget = new ColorWidget();

        colorWidgetFormGroup = new FormGroup(wfmStrings.color(), colorWidget);
        colorWidgetFormGroup.setVisible(true);
        body.add(colorWidgetFormGroup);
        if (data != null) {
            if (data.getCondition() != null) {
                conditionTextBox.setText(data.getCondition());
            }
            if (data.getTarget() != null) {
                conditions.setSelectedByDescription(data.getTarget());
            }
            colorWidget.setColor(data.getColor());
            colorWidgetFormGroup.setVisible(true);
            if (BETWEEN.equalsIgnoreCase(data.getTarget())) {
                betweenSecondValueTextBox.setPlaceHolder(wfmStrings.to() + " ...");
                conditionTextBox.setPlaceHolder(wfmStrings.from() + " ...");
                betweenSecondValueTextBox.setVisible(true);
                betweenSecondValueTextBox.setText(data.getBetweenSecondValue());
            }
        }

        WfmButton2 removeButton = new WfmButton2(wfmStrings.delete(), WfmButton2.BTN_LIGHTGREY, WfmButton2.ICON_TRASH);
        removeButton.addStyleName("btn-block");
        removeButton.addClickHandler(event -> {
            ReportingColumnColorPanel.this.removeFromParent();
        });
        body.add(removeButton);

        add(body);
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

    public ColumnColor getData() {
        ColumnColor result = null;
        if (!Utils.isNullOrEmpty(conditionTextBox.getValue()) && conditions.getSelectedItem(true) != null && colorWidget.getColor() != null) {
            result = new ColumnColor(conditionTextBox.getValue(), conditions.getSelectedItem(true).getDescription(), colorWidget.getColor());
            if (colorType.getSelectedItem() != null) {
                result.setType(colorType.getSelectedItem().getDescription());
            } else {
                result.setType(CUSTOM_COLOR);
            }
            if (BETWEEN.equalsIgnoreCase(conditions.getSelectedItem(true).getDescription())
                    && betweenSecondValueTextBox.getValue() != null
                    && !betweenSecondValueTextBox.getValue().isEmpty()) {
                betweenSecondValueTextBox.setVisible(true);
                result.setBetweenSecondValue(betweenSecondValueTextBox.getValue());
            }
        }
        return result;

    }

    public void setColorTypeWidget(Div body, ColumnColor data) {

        colorType = new DataListBox();
        colorType.setItems(getColorTypeItems());
        colorType.setSelectedByDescription(CUSTOM_COLOR);
        colorType.setEnabled(false);
        colorTypeFormGroup = new FormGroup(wfmStrings.type(), colorType);
        body.add(wrapIntoFormRow(colorTypeFormGroup));
    }


    public SelectItem[] getConditionTypes() {
        return new SelectItem[]{
                new SelectItem(1, wfmStrings.moreThan(), GREATER_THEN),
                new SelectItem(2, wfmStrings.lessThan(), LESS_THAN),
                new SelectItem(3, wfmStrings.equal(), EQUAL_TO),
                new SelectItem(4, wfmStrings.between(), BETWEEN)
        };
    }

    public SelectItem[] getColorTypeItems() {
        return new SelectItem[]{
                new SelectItem(1, wfmStrings.custom(), CUSTOM_COLOR)
        };
    }
}
