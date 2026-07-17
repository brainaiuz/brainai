package com.edatasite.workforce.gwt.core.client.ui.components.panelStack;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.ui.html.Div;

/**
 * User: Abror Abdukadirov
 * Date: 17.12.2018 17:02
 */
public class PanelStack extends Composite {
    interface PanelStackUiBinder extends UiBinder<HTMLPanel, PanelStack> {
    }

    private static PanelStackUiBinder ourUiBinder = GWT.create(PanelStackUiBinder.class);

    @UiField
    HTMLPanel headerTitle;
    @UiField
    HTMLPanel headerWidget;
    @UiField
    HTMLPanel bodyRow;

    public PanelStack() {
        initWidget(ourUiBinder.createAndBindUi(this));
    }

    public void setHeaderTitle(String title) {
        this.headerTitle.getElement().setInnerText(title);
    }

    public void setHeaderTitle(Widget title) {
        this.headerTitle.add(title);
    }

    public void setHeaderWidget(Widget widget) {
        this.headerWidget.add(widget);
    }

    public void addBodyRow(String title, Widget widget) {
        Div rowDiv = new Div("panel-row");

        Div dtDiv = new Div("panel-row__dt");
        dtDiv.getElement().setInnerText(title);

        Div ddDiv = null;
        if (widget != null) {
            ddDiv = new Div("panel-row__dd");
            ddDiv.add(widget);
        }
        rowDiv.add(dtDiv);
        if (ddDiv != null) {
            rowDiv.add(ddDiv);
        }
        this.bodyRow.add(rowDiv);
    }

    public void addBodyRow(Widget title, Widget widget) {
        Div rowDiv = new Div("panel-row");

        Div dtDiv = new Div("panel-row__dt");
        dtDiv.add(title);

        Div ddDiv = null;
        if (widget != null) {
            ddDiv = new Div("panel-row__dd");
            ddDiv.add(widget);
        }
        rowDiv.add(dtDiv);
        if (ddDiv != null) {
            rowDiv.add(ddDiv);
        }
        this.bodyRow.add(rowDiv);
    }

    public void removeBodyRowPanel() {
        this.bodyRow.removeFromParent();
    }
}
