package com.edatasite.workforce.gwt.core.client.ui.custompanel;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * Created by dilsh0d on 15.09.15.
 */
public class KpiCustomPanel extends Composite {
    interface KpiCustomPanelUiBinder extends UiBinder<HTMLPanel, KpiCustomPanel> {
    }

    private static KpiCustomPanelUiBinder ourUiBinder = GWT.create(KpiCustomPanelUiBinder.class);

    @UiField
    Element titleElem;
    @UiField
    HTMLPanel panelHeaderRight;
    @UiField
    HTMLPanel panelBody;

    public KpiCustomPanel() {
        this("","");
    }

    public KpiCustomPanel(String width, String height) {
        HTMLPanel rootWidget = ourUiBinder.createAndBindUi(this);
        initWidget(rootWidget);
        if (!"".equals(width) && !"".equals(height)) {
            setSize(width, height);
            rootWidget.setSize(width, height);
        }
    }

    public void setHeaderTitle(String title) {
        titleElem.setInnerHTML(title);
    }

    public void addHeaderRightWidget(Widget widget) {
        widget.setStyleName("pull-right");
        panelHeaderRight.add(widget);
    }

    public void setBodyWidget(Widget widget) {
        panelBody.add(widget);
    }
}