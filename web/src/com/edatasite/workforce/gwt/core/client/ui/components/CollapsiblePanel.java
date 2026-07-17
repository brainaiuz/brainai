package com.edatasite.workforce.gwt.core.client.ui.components;

import com.edatasite.workforce.gwt.core.client.ui.components.form.GColumn;
import com.edatasite.workforce.gwt.core.client.ui.components.form.GColumnEnum;
import com.edatasite.workforce.gwt.core.client.ui.components.form.GRow;
import com.edatasite.workforce.gwt.core.client.ui.components.svgIcon.SvgEnum;
import com.edatasite.workforce.gwt.core.client.ui.components.svgIcon.SvgIcon;
import com.edatasite.workforce.gwt.core.client.ui.multiwidget.WidgetsMap;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.ui.html.Div;
import gwt.material.design.client.ui.html.ListItem;

import java.util.LinkedList;

public class CollapsiblePanel extends Composite {
    interface CollapsiblePanelUiBinder extends UiBinder<Widget, CollapsiblePanel> {
    }

    private static CollapsiblePanelUiBinder ourUiBinder = GWT.create(CollapsiblePanelUiBinder.class);

    @UiField
    ListItem div;
    @UiField
    HTMLPanel header;
    @UiField
    HTML title;
    @UiField
    GRow customizeHeaderPanel;
    @UiField
    HTMLPanel body;
    @UiField
    HTMLPanel customizePanel;
    @UiField
    GRow defaultRow;

    private SvgIcon checkIcon;
    LinkedList<GRow> rows = new LinkedList<>();
    WidgetsMap widgetsMap = new WidgetsMap();

    public CollapsiblePanel() {
        initWidget(ourUiBinder.createAndBindUi(this));
        header.getElement().setAttribute("onclick", "var myclick=this.parentElement;if(myclick && myclick.className);{var cls=myclick.className;if(cls.indexOf('active')>=0){cls=cls.replace('active','');}else{cls+=' active';}myclick.className=cls; } var myclick=this;if(myclick && myclick.className);{var cls=myclick.className;if(cls.indexOf('active')>=0){cls=cls.replace('active','');}else{cls+=' active';}myclick.className=cls; }");
    }

    public CollapsiblePanel(String title, GColumn... columns) {
        this();
        this.title.setText(title);
        for (GColumn column : columns) {
            defaultRow.add(column);
        }
    }

    public void addRow(GRow row) {
        rows.add(row);
        body.add(row);
    }

    public void addWidget(Widget widget) {
        customizePanel.setVisible(true);
        body.setVisible(false);
        customizePanel.add(widget);
    }

    public void setCustomizeHeader(LinkedList<GColumn> columns) {
        customizeHeaderPanel.setVisible(true);
//        customizeHeaderPanel.getElement().setAttribute("style", "display: -webkit-box;");
        customizeHeaderPanel.setClass("tileDragTarget__header");
        header.setVisible(false);

        checkIcon = new SvgIcon(SvgEnum.chevronRight);
        checkIcon.getElement().setAttribute("style", "transform: rotate(90deg);");

        GColumn check = new GColumn(GColumnEnum.COL_AUTO, checkIcon);
//        check.getElement().setAttribute("style", "display: flex; align-items: center;");
        check.getElement().addClassName("customizeHeaderPanel-check");
        check.addClickHandler(click -> {
            boolean active = !customizeHeaderPanel.getStyleName().contains("active");
            setActive(active);
        });
        customizeHeaderPanel.add(check);

        for (GColumn column : columns) {
            customizeHeaderPanel.add(column);
        }
    }

    public void setCustomizeHeaderBudget(LinkedList<GColumn> columns) {
        customizeHeaderPanel.setVisible(true);
//        customizeHeaderPanel.getElement().setAttribute("style", "display: -webkit-box;");
        customizeHeaderPanel.setClass("java--customizeHeaderPanel__setBudget"); //ToDo check this class
        header.setVisible(false);

        SvgIcon checkIcon = new SvgIcon(SvgEnum.chevronRight);
        checkIcon.getElement().setAttribute("style", "transform: rotate(90deg);");

        GColumn check = new GColumn(GColumnEnum.COL_2, checkIcon);
        check.addClickHandler(click -> {
            boolean active = !customizeHeaderPanel.getStyleName().contains("active");
            setActive(active);
        });
        customizeHeaderPanel.add(check);

        int i = 0;
        for (GColumn column : columns) {
            if (i == 0) {
                column.addClickHandler(click -> {
                    boolean active = !customizeHeaderPanel.getStyleName().contains("active");
                    setActive(active);
                });
            }
            customizeHeaderPanel.add(column);
        }
    }

    public void setTitleName(String titleName) {
        title.setText(titleName);
    }

    public void addColumn(GColumn... columns) {
        for (GColumn column : columns) {
            defaultRow.add(column);
        }
    }

    public void setActive(boolean active) {
        if (active) {
            div.addStyleName("active");
            header.addStyleName("active");
            customizeHeaderPanel.addStyleName("active");
            if (checkIcon != null) {
                checkIcon.getElement().setAttribute("style", "transform: rotate(90deg);");
            }
        } else {
            div.removeStyleName("active");
            header.removeStyleName("active");
            customizeHeaderPanel.removeStyleName("active");
            if (checkIcon != null) {
                checkIcon.getElement().setAttribute("style", "transform: rotate(0deg);");
            }
        }
    }

    public void removeAllRows() {
        defaultRow.clear();
        for (GRow row : rows) {
            row.removeFromParent();
        }
    }

    public void addHeaderButtons(WfmButton2... buttons) {
        header.getElement().removeAttribute("onclick");
        Div btnPanel = new Div("collapsible-header-buttons");
        for (Widget button : buttons) {
            btnPanel.add(button);
        }
        header.add(btnPanel);
    }

    public GRow getDefaultRow() {
        return defaultRow;
    }

    public void setWidgetsMap(WidgetsMap widgetsMap) {
        this.widgetsMap = widgetsMap;
    }

    public WidgetsMap getWidgetsMap() {
        return widgetsMap;
    }
}
