package com.edatasite.workforce.gwt.core.client.ui.components;

import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import gwt.material.design.incubator.client.toggle.GroupToggleButton;

/**
 * @author Hurshid on 2/20/2019
 */
public class KpiToggleButton extends GroupToggleButton {
    private static final WfmStrings wfmStrings = WfmStrings.App.get();
    private static String YES = "YES";
    private static String NO = "NO";

    public KpiToggleButton() {
        this(wfmStrings.yes(), wfmStrings.no());
    }

    public KpiToggleButton(String onLabel, String offLabel) {
        setStyleName("kpi-toggle-button");
        getWrapper().setStyleName("toggle-wrapper");

        addItem(onLabel, YES);
        addItem(offLabel, NO);
    }

    public KpiToggleButton(SelectItem... values) {

        setStyleName("kpi-toggle-button");
        getWrapper().setStyleName("toggle-wrapper");
        setValues(values);
    }

    private void setValues(SelectItem[] values) {
        getWrapper().setStyleName("toggle-wrapper");
        if (values != null && values.length > 0) {
            for (int i = 0; i < values.length; i++) {
                addItem(values[i].getName(), values[i].getDescription());
            }
        }
    }

    //for multiple buttons
    public String getSelected() {
        return (String) getSingleValue();
    }

    public void setActive(boolean active) {
        super.clearAll();
        if (active) {
            get(0).toggle();
        } else {
            get(1).toggle();
        }
    }

    public void setActive() {
        super.clearAll();
        get(0).toggle();
    }

    public boolean isActive() {
        return YES.equalsIgnoreCase(getSelected());
    }

    public void setOffLabel(String offLabel) {
        get(1).setText(offLabel);
    }

    public void setOnLabel(String onLabel) {
        get(0).setText(onLabel);
    }
}
