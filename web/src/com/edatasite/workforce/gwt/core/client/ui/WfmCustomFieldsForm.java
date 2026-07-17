package com.edatasite.workforce.gwt.core.client.ui;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.ui.html.Label;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * User: Dilsh0d
 * Email: dilshod.toj@gmail.com
 * Date: 18-Nov-2010
 * Time: 17:33:46
 */
public class WfmCustomFieldsForm extends WfmForm {

    private Set<Integer> isRowEmpty = new TreeSet<>();

    public WfmCustomFieldsForm() {
        super();
    }
    
    public Field addField(int index, String label, Widget widget, boolean required) {
        return addField(index, label, widget, required, null, 0);
    }

    public Field addField(int index, String label, Widget widget, boolean required, Label... labelWidget) {
        return addField(index, label, widget, required, null, 0, null, 0, labelWidget);
    }

    public Field addField(int index, String label, Widget widget,boolean required, String toolTip) {
        return addField(index, label, widget, required, toolTip, 0);
    }

    public Field addField(int index,String label, Widget widget, String helpMessage, boolean required) {
        return addField(index, label, widget, required, null, 0, helpMessage, 0);
    }

    public Field addField(int index, String label, Widget widget) {
        return addField(index, label, widget, false, null, 0);
    }

    public Field addField(int index,String label, Widget widget, String helpMessage, int helpTextScope, boolean required) {
        return addField(index, label, widget, required, null, 0, helpMessage, helpTextScope);
    }

    public Field addField(int index, String label, Widget widget, boolean required, String toolTip, int colspan) {
        return addField(index, label, widget, required, toolTip, colspan, null, 0);
    }

    public Field addField(int index, String label, Widget widget, boolean required, String toolTip, int colspan, String helpText, int helpTextScope, Label... labelWidget) {
        Field field = new Field(label, widget, required, null, helpText, helpTextScope, labelWidget);
        if (widget instanceof HorizontalPanel) {
            throw new IllegalArgumentException("While adding a widget to the WfmForm avoid use of HorizontalPanels, please try to use the widget that implemented HasText or Clearable interfaces. To get more details please refer to Anvarbek or Abdulaziz");
        }
        if (colspan > 0) {
            field.setColspan(colspan);
        }

        if (isRowEmpty.contains(index)) {
            fillRows(index);
//            getFlex().insertRow(index);
        } else {
            isRowEmpty.add(index);
        }
        field.setIndex(index);
        getFields().add(field);
        internalAddField(field);
        return field;
    }

    private void fillRows(int index) {
        List<Integer> rows = new ArrayList<>();
        for (Integer row : isRowEmpty) {
            if (row >= index) {
                row++;
                rows.add(row);
            }
        }
        isRowEmpty.clear();
        isRowEmpty.addAll(rows);
        isRowEmpty.add(index);
    }

    public Field addField(int index, String label, Widget[] widgets) {
        return addField(index, label, widgets, false, 0, null, 0);
    }

    public Field addField(int index, String label, Widget[] widgets, boolean required) {
        return addField(index, label, widgets, required, 0, null, 0);
    }

    public Field addField(int index, String label, Widget[] widgets, boolean required, int widgetSpacing) {
        return addField(index, label, widgets, required, widgetSpacing, null, 0);
    }

    public Field addField(int index, String label, Widget[] widgets, boolean required, int widgetSpacing, String helpText) {
        return addField(index, label, widgets, required, widgetSpacing, helpText, 0);
    }


    private Field addField(int index, String label, Widget[] widgets,boolean required, int widgetSpacing, String helpText, int helpTextScope) {
        Field field = new Field(label, widgets, widgetSpacing, required, helpText, helpTextScope);
        field.setIndex(index);
        if (isRowEmpty.contains(index)) {
            fillRows(index);
//            getFlex().insertRow(index);
        } else {
            isRowEmpty.add(index);
        }
        getFields().add(field);
        internalAddField(field);
        return field;
    }


    public Field addImageField(Image image) {
        Field field = new Field("", image, false);
        field.setIndex(getFields().size());
        getFields().add(field);
//        getFlex().setWidget(field.getIndex(), 0, field.widget);
//        getFlex().getFlexCellFormatter().setColSpan(field.getIndex(), 0, allColumns);
        getContainer().add(field.widget);
        return field;
    }

    public Field addTitleField(String text) {
        return addTitleField(text, false);
    }

    public Field addTitleField(String text, boolean mandatory) {
        return addTitleField(text, null, mandatory);
    }

    public Field addTitleField(String text, Widget widget, boolean mandatory) {
        Field field = new Field(text, widget, mandatory);
        field.setIndex(getFields().size());
        String label = "<div class=customTitle>" + text + "</div>";
        if (mandatory) {
            label = "<div class=customTitle>" + text + "<span class=txt-elem--required>*</span></div>";
        }
        HorizontalPanel titlePanel = new HorizontalPanel();
        HTML titleLabel = new HTML(label);
        if (isRowEmpty.contains(field.getIndex())) {
            fillRows(field.getIndex());
            //getFlex().insertRow(field.getIndex());
        } else {
            isRowEmpty.add(field.getIndex());
        }
        titlePanel.add(titleLabel);
        getFields().add(field.getIndex(), field);
        if (widget != null) {
            titlePanel.add(widget);
        }
//        getFlex().setWidget(field.getIndex(), 0, titlePanel);
//        getFlex().getFlexCellFormatter().setColSpan(field.getIndex(), 0, allColumns);
        getContainer().add(titlePanel);
        return field;
    }

    public Field addHorizontalLine() {
        HTML line = new HTML("<div class=line></div>");
        Field field = new Field("", line, false);
        field.setIndex(getFields().size());
        if (isRowEmpty.contains(field.getIndex())) {
            fillRows(field.getIndex());
//            getFlex().insertRow(field.getIndex());
        } else {
            isRowEmpty.add(field.getIndex());
        }
        getFields().add(field);
//        getFlex().setWidget(field.getIndex(), 0, line);
//        getFlex().getFlexCellFormatter().setColSpan(field.getIndex(), 0, allColumns);
        getContainer().add(line);
        return field;
    }

    public void addButton(Widget button, int column) {
        if (firstAdd) {
//            getFlex().setWidget(getFields().size(), column, buttons);
            getContainer().add(buttons);
            firstAdd = false;
        }
        buttons.add(button);
        isRowEmpty.add(getFields().size());
        if (Utils.isDemoAccount()) {
            if (button instanceof WfmButton2) {
                WfmButton2 bu2 = (WfmButton2) button;
                bu2.setEnabled(false);
            }
        }
    }
}
