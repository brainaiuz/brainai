package com.edatasite.workforce.gwt.documents.client;

import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.ui.html.Div;

public class CheckBoxList extends Composite {
    private WfmStrings wfmStrings = WfmStrings.App.get();
    private Div self;
    private Div noFile = new Div("kpi-upload__checkbox-list-empty");
    public CheckBoxList() {
        self = new Div("kpi-upload__checkbox-list");
        initWidget(self);
        setNoFileText(wfmStrings.noResultsFoundForTheProvidedSearchCriteria());
        self.add(noFile);
    }

    public void setNoFileVisible(boolean visible) {
        if (visible) {
            self.add(noFile);
        } else {
            self.remove(noFile);
        }
    }

    public void setNoFileText(String text) {
        if (text != null) {
            noFile.getElement().setInnerText(text);
        }
    }
    public Div getSelf() {
        return self;
    }

    public void add(CustomCheckBox checkBox) {
        self.add(checkBox);
    }

    public void clear() {
        self.clear();
    }

    public int getWidgetCount() {
        return self.getWidgetCount();
    }

    public CustomCheckBox getWidget(int i) {
        return (CustomCheckBox) self.getWidget(i);
    }

}
