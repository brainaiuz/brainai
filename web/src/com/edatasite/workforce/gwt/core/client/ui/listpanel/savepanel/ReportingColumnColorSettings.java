package com.edatasite.workforce.gwt.core.client.ui.listpanel.savepanel;

import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ColumnColor;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.ui.html.Div;

import java.util.LinkedList;

public class ReportingColumnColorSettings extends Div {

    private static final WfmStrings wfmStrings = WfmStrings.App.get();
    public static ReportingColumnColorSettings selected;
    private final WfmButton2 addMore;


    public ReportingColumnColorSettings() {
        super();
        addMore = new WfmButton2(wfmStrings.addRule(), WfmButton2.BTN_LIGHTGREY);
        addMore.addStyleName("btn-block mt-1");
        addMore.addClickHandler(event -> {
            add(new ReportingColumnColorPanel(null));
        });
        insert(addMore, 0);
        activate(false);
    }

    public void activate(boolean active) {
        setVisible(active);
        if (getChildrenList().size() < 2) {
            add(new ReportingColumnColorPanel(null));
        }
    }

    @Override
    public void add(Widget child) {
        if (child != null) {
            insert(child, 1);
        }
    }

    @Override
    public void clear() {
        super.clear();
        insert(addMore, 0);
    }

    public LinkedList<ColumnColor> getData() {
        LinkedList<ColumnColor> result = new LinkedList<>();
        for (Widget widget : getChildrenList()) {
            if (widget instanceof ReportingColumnColorPanel) {
                ReportingColumnColorPanel panel = (ReportingColumnColorPanel) widget;
                if (panel.getData() != null) {
                    result.add(panel.getData());
                }
            }
        }
        return result;
    }

    public void setData(LinkedList<ColumnColor> colors) {
        if (colors == null || colors.size() == 0) {
            colors = new LinkedList<>();
        }
        clear();
        for (int i = colors.size() - 1; i >= 0; i--) {
            add(new ReportingColumnColorPanel(colors.get(i)));

        }
    }

}
