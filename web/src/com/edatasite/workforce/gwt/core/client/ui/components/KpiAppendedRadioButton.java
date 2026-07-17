package com.edatasite.workforce.gwt.core.client.ui.components;

import com.edatasite.workforce.gwt.core.client.UUID;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.google.gwt.event.logical.shared.HasSelectionHandlers;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Composite;
import gwt.material.design.client.ui.html.Div;

/**
 * @author Djuraev on 4/2/2019
 */
public class KpiAppendedRadioButton extends Composite implements HasSelectionHandlers<Boolean> {

    private static final WfmStrings wfmStrings = WfmStrings.App.get();
    private KpiRadioButton radio1;
    private KpiRadioButton radio2;

    public KpiAppendedRadioButton() {
        this(wfmStrings.yes(), wfmStrings.no());
    }

    public KpiAppendedRadioButton(String onLabel, String offLabel) {

        initialize(onLabel, offLabel);
    }

    private void initialize(String onLabel, String offLabel) {
        String name = UUID.uuid();

        Div inputGroup = new Div("input-group");
        Div prepend = new Div("input-group-prepend");
        inputGroup.add(prepend);

        Div prependedContent1 = new Div("input-group-text");
        radio1 = new KpiRadioButton(name, onLabel);
        radio1.setValue(true);
        radio1.addValueChangeHandler(valueChangeEvent -> SelectionEvent.fire(this, true));
        prependedContent1.add(radio1);
        prepend.add(prependedContent1);

        Div prependedContent2 = new Div("input-group-text");
        radio2 = new KpiRadioButton(name, offLabel);
        radio2.addValueChangeHandler(valueChangeEvent -> SelectionEvent.fire(this, false));
        prependedContent2.add(radio2);
        prependedContent2.getElement().setAttribute("style", "border-top-right-radius:5px;border-bottom-right-radius:5px;");
        prepend.add(prependedContent2);

        initWidget(inputGroup);
    }

    @Override
    public HandlerRegistration addSelectionHandler(SelectionHandler<Boolean> selectionHandler) {
        return addHandler(selectionHandler, SelectionEvent.getType());
    }

    public void setActive(boolean active) {
        if (active) {
            radio1.setValue(true);
        } else {
            radio2.setValue(true);
        }
    }

    public void setActive() {
        radio1.setValue(true);
    }

    public boolean isActive() {
        return radio1.getValue();
    }

    public void setEnabled(boolean enabled) {
        radio1.setEnabled(enabled);
        radio2.setEnabled(enabled);
    }
}
