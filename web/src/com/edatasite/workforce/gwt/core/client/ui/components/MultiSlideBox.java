package com.edatasite.workforce.gwt.core.client.ui.components;

import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.ui.components.form.FormGroup;
import com.edatasite.workforce.gwt.core.client.ui.components.form.GColumn;
import com.edatasite.workforce.gwt.core.client.ui.components.form.GRow;
import com.edatasite.workforce.gwt.core.client.ui.components.tooltip.KpiToolTip;
import com.edatasite.workforce.gwt.core.client.ui.multiwidget.MultiTableWidgets;
import com.edatasite.workforce.gwt.core.client.ui.multiwidget.WidgetsMap;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.ui.MaterialPanel;

import java.util.ArrayList;
import java.util.List;

public class MultiSlideBox extends MaterialPanel {
    private static final WfmStrings wfmStrings = WfmStrings.App.get();

    private final MultiTableWidgets multiTableWidgets;
    private final List<Widget> widgetList = new ArrayList<>();
    private final String title;

    public MultiSlideBox(String title, MultiTableWidgets multiTableWidgets) {
        this.multiTableWidgets = multiTableWidgets;
        this.title = title;
        addWidgets(multiTableWidgets.getWidgetsMaps());
        setClass("collapsible--panels collapsible");
    }

    public void addWidgets(final WidgetsMap widgetsMap) {
        if (widgetsMap == null) {
            return;
        }
        CollapsiblePanel slideBox = initPanel();
        slideBox.setWidgetsMap(widgetsMap);
        for (Widget widget : widgetsMap.getWidgets()) {
            if (widget instanceof GRow) {
                slideBox.addRow((GRow) widget);
            } else if (widget instanceof GColumn) {
                slideBox.addColumn((GColumn) widget);
            } else if (widget instanceof FormGroup){
                slideBox.addColumn(new GColumn((FormGroup) widget));
            } else {
                FormGroup formGroup = new FormGroup(widget);
                slideBox.addColumn(new GColumn(formGroup));
            }
        }
        widgetList.add(slideBox);
        add(slideBox);
    }

    private CollapsiblePanel initPanel() {
        final CollapsiblePanel colPanel = new CollapsiblePanel(title);
        colPanel.getDefaultRow().removeFromParent();
        colPanel.setActive(true);
        WfmButton2 minusButton = new WfmButton2();
        minusButton.addStyleName("ficon--minus no-border");
        new KpiToolTip(minusButton, wfmStrings.delete());
        minusButton.addClickHandler(c -> {
            colPanel.removeFromParent();
            widgetList.remove(colPanel);
            if (getWidgetCount() == 0) {
                addWidgets(multiTableWidgets.getWidgetsMaps());
            }
        });

        WfmButton2 plusButton = new WfmButton2();
        plusButton.addStyleName("ficon--plus no-border");
        new KpiToolTip(plusButton, wfmStrings.add());
        plusButton.addClickHandler(c -> addWidgets(multiTableWidgets.getWidgetsMaps()));

        colPanel.addHeaderButtons(minusButton, plusButton);
        return colPanel;
    }

    public List<WidgetsMap> getWidgetsMap() {
        List<WidgetsMap> widgetsMaps = new ArrayList<>();
        for (Widget widget : widgetList) {
            if (widget instanceof CollapsiblePanel) {
                widgetsMaps.add(((CollapsiblePanel) widget).getWidgetsMap());
            }
        }
        return widgetsMaps;
    }

    public void removeAllRows() {
        widgetList.clear();
        clear();
    }

    public int size() {
        return widgetList.size();
    }
}
