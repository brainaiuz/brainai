package com.edatasite.workforce.gwt.core.client.ui.facetfilter;

import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * Created by IntelliJ IDEA.
 * User: dilsh0d
 * Date: 21/03/12
 * Time: 14:40
 * To change this template use File | Settings | File Templates.
 */
public class VerticalPanelWithSpacer extends VerticalPanel {
    private int cell;

    public VerticalPanelWithSpacer(int cell) {
        this.cell = cell;
        Label spacerLabel = new Label("X");
        spacerLabel.setVisible(false);
        super.add(spacerLabel);
//        setCellHeight(spacerLabel, "200px;");
    }

    @Override
    public void add(Widget w) {
        super.insert(w, getWidgetCount() - 1);
    }

    @Override
    public void insert(Widget w, int beforeIndex) {
        if (beforeIndex >= getWidgetCount()) {
            beforeIndex = getWidgetCount() - 1;
        }
        super.insert(w, beforeIndex);
        updateChildPosition();
    }

    private void updateChildPosition() {
        int k = 1;
        for (int i = 0; i < getWidgetCount(); i++) {
            Widget w = getWidget(i);
            if (w instanceof FacetContentPanel) {
                ((FacetContentPanel) w).setPanelPosition(k++, cell);
            }
        }
    }

    public void regenerateContent() {
        for (int i = 0; i < getWidgetCount(); i++) {
            Widget w = getWidget(i);
            if (w instanceof FacetContentPanel) {
                FacetContentPanel focusPanel = ((FacetContentPanel) w);
                insert(w, focusPanel.getSetting().getOriginalRow());
            }
        }
    }
}
