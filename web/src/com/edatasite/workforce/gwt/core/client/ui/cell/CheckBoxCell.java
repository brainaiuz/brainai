package com.edatasite.workforce.gwt.core.client.ui.cell;

import com.google.gwt.cell.client.CheckboxCell;
import com.google.gwt.cell.client.ValueUpdater;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.InputElement;
import com.google.gwt.dom.client.LabelElement;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;

/**
 * User: Abror Abdukadirov
 * Date: 10.01.2018 16:25
 */
public class CheckBoxCell extends CheckboxCell {
    private static final SafeHtml INPUT_CHECKED = SafeHtmlUtils.fromSafeConstant("<label class=\"control control--checkbox\">" +
                                                                                        "<input type=\"checkbox\" tabindex=\"-1\" checked/>" +
                                                                                        "<span class=\"control__indicator\"></span>" +
                                                                                    "</label>");

    private static final SafeHtml INPUT_UNCHECKED = SafeHtmlUtils.fromSafeConstant("<label class=\"control control--checkbox\">" +
                                                                                         "<input type=\"checkbox\" tabindex=\"-1\"/>" +
                                                                                         "<span class=\"control__indicator\"></span>" +
                                                                                      "</label>");
    public CheckBoxCell() {
        super();
    }

    public CheckBoxCell(boolean dependsOnSelection, boolean handlesSelection) {
        super(dependsOnSelection, handlesSelection);
    }

    @Override
    public void render(Context context, Boolean value, SafeHtmlBuilder sb) {
        Object key = context.getKey();
        Boolean viewData = (Boolean) this.getViewData(key);
        if (viewData != null && viewData.equals(value)) {
            this.clearViewData(key);
            viewData = null;
        }

        if (value != null && (viewData != null ? viewData : value).booleanValue()) {
            sb.append(INPUT_CHECKED);
        } else {
            sb.append(INPUT_UNCHECKED);
        }
    }

    @Override
    public void onBrowserEvent(Context context, Element parent, Boolean value, NativeEvent event, ValueUpdater<Boolean> valueUpdater) {
        String type = event.getType();
        boolean enterPressed = "keydown".equals(type) && event.getKeyCode() == 13;
        if ("change".equals(type) || enterPressed) {
            LabelElement label = (LabelElement) parent.getFirstChild().cast();
            InputElement input = (InputElement) label.getFirstChild().cast();
            Boolean isChecked = input.isChecked();
            if (enterPressed && (this.handlesSelection() || !this.dependsOnSelection())) {
                isChecked = !isChecked.booleanValue();
                input.setChecked(isChecked.booleanValue());
            }
            if (value != isChecked && !this.dependsOnSelection()) {
                this.setViewData(context.getKey(), isChecked);
            } else {
                this.clearViewData(context.getKey());
            }
            if (valueUpdater != null) {
                valueUpdater.update(isChecked);
            }
        }
    }
}
