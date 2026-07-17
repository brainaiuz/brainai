package com.edatasite.workforce.gwt.hrms.client.ui.orgchart.boardchart;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.InputElement;
import com.google.gwt.event.logical.shared.HasValueChangeHandlers;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.Widget;

public class RangeSlider extends Widget implements HasValueChangeHandlers<Integer>, HasValue<Integer> {

    private final InputElement input;
    private int minValue = 0;
    private int maxValue = 100;
    private int stepSize = 1;

    public RangeSlider() {
        Element element = Document.get().createElement("input");
        input = InputElement.as(element);

        input.setAttribute("type", "range");
        input.setAttribute("step", "1");

        setElement(input);
        hookInputEvent(input);
        hookDoubleClick(input); // Double click option
    }

    private native void hookDoubleClick(Element el) /*-{
        var self = this;
        el.addEventListener("dblclick", $entry(function () {
            // Double click on Thumg Sets 100% via Java, to make all updates work
            self.@com.edatasite.workforce.gwt.hrms.client.ui.orgchart.boardchart.RangeSlider::setValue(Ljava/lang/Integer;Z)(@java.lang.Integer::valueOf(I)(100), true);
        }));
    }-*/;

    private void onNativeInput() {
        updateFillPercent(); // refresh the indicator line
        ValueChangeEvent.fire(this, getValue());
    }

    // for update the CSS var --fill-percent
    private void updateFillPercent() {
        int val = getValue();
        double percent = (maxValue > minValue) ? (double) (val - minValue) / (maxValue - minValue) * 100 : 0;
        setFillVariable(input, percent + "%");

        setFillVariable(input, percent + "%");
    }

    private native void setFillVariable(Element el, String value) /*-{
        el.style.setProperty('--fill-percent', value);
    }-*/;


    private native void hookInputEvent(Element el) /*-{
        var self = this;
        el.addEventListener("input", $entry(function () {
            self.@com.edatasite.workforce.gwt.hrms.client.ui.orgchart.boardchart.RangeSlider::onNativeInput()();
        }));
    }-*/;

    public void setMin(int min) {
        input.setAttribute("min", String.valueOf(min));
    }

    public void setMax(int max) {
        input.setAttribute("max", String.valueOf(max));
    }

    public void setValue(int value) {
        input.setAttribute("value", String.valueOf(value));
    }

    public Integer getValue() {
        String v = input.getValue();
        return v == null || v.isEmpty() ? 0 : Integer.parseInt(v);
    }

    public void setMinValue(String minValue) {
        try {
            this.minValue = Integer.parseInt(minValue);
        } catch (NumberFormatException e) {
            this.minValue = 0;
        }
        input.setAttribute("min", String.valueOf(this.minValue));
    }

    public void setMaxValue(String maxValue) {
        try {
            this.maxValue = Integer.parseInt(maxValue);
        } catch (NumberFormatException e) {
            this.maxValue = 100;
        }
        input.setAttribute("max", String.valueOf(this.maxValue));
    }

    public void setStepSize(String stepSize) {
        try {
            this.stepSize = Integer.parseInt(stepSize);
        } catch (NumberFormatException e) {
            this.stepSize = 1;
        }
        input.setAttribute("step", String.valueOf(this.stepSize));
    }

    @Override
    public void setValue(Integer value) {
        setValue(value, false);
    }

    @Override
    public void setValue(Integer value, boolean fireEvents) {
        if (value == null) value = 0;
        // input.setValue changes current status of indicator line
        input.setValue(String.valueOf(value));
        // input.setAttribute changes value in DOM (for debug)
        input.setAttribute("value", String.valueOf(value));

        updateFillPercent();

        if (fireEvents) {
            ValueChangeEvent.fire(this, value);
        }
    }

    @Override
    public HandlerRegistration addValueChangeHandler(ValueChangeHandler handler) {
        return addHandler(handler, ValueChangeEvent.getType());
    }

    @Override
    protected void onLoad() {
        super.onLoad();
        updateFillPercent();
    }
}
