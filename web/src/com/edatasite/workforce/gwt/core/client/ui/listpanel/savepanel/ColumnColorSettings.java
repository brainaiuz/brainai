package com.edatasite.workforce.gwt.core.client.ui.listpanel.savepanel;

import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ColumnColor;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.ui.html.Div;

import java.util.ArrayList;
import java.util.List;

public class ColumnColorSettings extends Div {
    private static final WfmStrings wfmStrings = WfmStrings.App.get();
    public static ColumnColorSettings selected;
    private final WfmButton2 addMore;

    public ColumnColorSettings() {
        super();
        addMore = new WfmButton2(wfmStrings.addRule(), WfmButton2.BTN_LIGHTGREY);
        addMore.addStyleName("btn-block mt-1");
        addMore.addClickHandler(event -> {
            add(new ColumnColorPanel(null));
        });
        insert(addMore, 0);
        activate(false);
    }

    public ColumnColorSettings(boolean isOrgChart) {
        super();

        addMore = new WfmButton2(wfmStrings.addRule(), WfmButton2.BTN_LIGHTGREY);
        addMore.addStyleName("btn-block mt-1");
        addMore.addClickHandler(event -> {
            add(new ColumnColorForOrgChart(null, new ArrayList<>()));
        });
        insert(addMore, 0);
        activateForOrgChart(false);
    }

    public void activate(boolean active) {
        setVisible(active);
        if (getChildrenList().size() < 2) {
            add(new ColumnColorPanel(null));
        }
    }
    public void activateForOrgChart(boolean active) {
        setVisible(active);
        if (getChildrenList().size() < 2) {
            add(new ColumnColorForOrgChart(null, new ArrayList<>()));
        }
    }

    public static void handlePopup(ColumnColorSettings sibling) {
        if (sibling.equals(selected) && sibling.isVisible()) {
            sibling.activate(false);
        } else {
            if (selected != null) {
                selected.activate(false);
            }
            selected = sibling;
            selected.activate(true);
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

    public void setData(List<ColumnColor> colors) {
        clear();
        for (ColumnColor color : colors) {
            add(new ColumnColorPanel(color));
        }
    }

    public void setDataForOrgChart(List<ColumnColor> colors) {
        clear();
        for (ColumnColor color : colors) {
            add(new ColumnColorForOrgChart(color, new ArrayList<>()));
        }
    }

    public List<ColumnColor> getData() {
        List<ColumnColor> result = new ArrayList<>();
        for (Widget widget : getChildrenList()) {
            if (widget instanceof ColumnColorPanel) {
                ColumnColorPanel panel = (ColumnColorPanel) widget;
                if (panel.getData() != null) {
                    result.add(panel.getData());
                }
            }
        }
        return result;
    }

    public List<ColumnColor> getDataForOrgChart() {
        GWT.log("getDataForColor1");
        List<ColumnColor> result = new ArrayList<>();
        GWT.log("getDataForColor2");
        for (Widget widget : getChildrenList()) {
            GWT.log("getDataForColor3");
            if (widget instanceof ColumnColorForOrgChart) {
                GWT.log("getDataForColor4");
                ColumnColorForOrgChart panel = (ColumnColorForOrgChart) widget;
                if (panel.getData() != null) {
                    GWT.log("getDataForColor5");
                    result.add(panel.getData());
                }
            }
        }
        return result;
    }

}
