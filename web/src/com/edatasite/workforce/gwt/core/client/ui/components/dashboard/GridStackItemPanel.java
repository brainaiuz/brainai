package com.edatasite.workforce.gwt.core.client.ui.components.dashboard;

import com.edatasite.workforce.gwt.core.client.rpc.dashboard.DashboardComponentItem;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.ui.MaterialLink;
import gwt.material.design.client.ui.html.Icon;

/**
 * User: Abror Abdukadirov
 * Date: 05.04.2018 20:33
 */
public class GridStackItemPanel extends Composite {
    interface GridStackItemPanelUiBinder extends UiBinder<Widget, GridStackItemPanel> {
    }

    private static GridStackItemPanelUiBinder ourUiBinder = GWT.create(GridStackItemPanelUiBinder.class);

    @UiField
    HTMLPanel gridItem;
    @UiField
    HTMLPanel header;
    @UiField
    HTMLPanel content;

    private Widget itemWidget;

    private MaterialLink hideLink;

    private Integer objectId;
    private Integer reportId;
    private String componentCode;
    private String componentName;
    private Integer reportWidgetId;

    public GridStackItemPanel() {
        initWidget(ourUiBinder.createAndBindUi(this));

        initialize();
    }

    private void initialize() {
        hideLink = new MaterialLink();
        hideLink.setStyleName("widget-delete");
        Icon icon = new Icon();
        icon.setStyleName(WfmButton2.ICON_CLOSE);
        hideLink.add(icon);

        header.add(hideLink);
    }

    public void initConfig(DashboardComponentItem item) {
        if (item != null) {
            this.setObjectId(item.getId());
            this.setReportId(item.getReportId());
            this.setReportWidgetId(item.getReportWidgetId());
            this.setComponentCode(item.getComponentCode());
            this.setComponentName(item.getName());
            gridItem.getElement().setAttribute("data-gs-x", String.valueOf(item.getX()));
            gridItem.getElement().setAttribute("data-gs-y", String.valueOf(item.getY()));
            gridItem.getElement().setAttribute("data-gs-width", String.valueOf(item.getWidth()));
            gridItem.getElement().setAttribute("data-gs-height", String.valueOf(item.getHeight()));
            gridItem.getElement().setAttribute("data-gs-min-width", String.valueOf(item.getMinWidth())); // additional prop
            gridItem.getElement().setAttribute("data-gs-min-height", String.valueOf(item.getMinHeight())); //additional prop
        }
    }

    public int getX() {
        return parseAttribute("data-gs-x");
    }

    public int getY() {
        return parseAttribute("data-gs-y");
    }

    public int getWidth() {
        int width = parseAttribute("data-gs-width");
        return width > 0 ? width : 4;
    }

    public void setWidth(int width) {
        gridItem.getElement().setAttribute("data-gs-width", String.valueOf(width));
    }

    public int getHeight() {
        int height = parseAttribute("data-gs-height");
        return height > 0 ? height : 4;
    }

    public void setHeight(int height) {
        gridItem.getElement().setAttribute("data-gs-height", String.valueOf(height));
    }

    public int getMinHeight() {
        int height = parseAttribute("data-gs-min-height");
        return height > 0 ? height : 2;
    }

    public void setMinHeight(int minHeight) {
        gridItem.getElement().setAttribute("data-gs-min-height", String.valueOf(minHeight));
    }

    public int getMinWidth() {
        int width = parseAttribute("data-gs-min-width");
        return width > 0 ? width : 2;
    }

    public void setMinWidth(int minWidth) {
        gridItem.getElement().setAttribute("data-gs-min-width", String.valueOf(minWidth));
    }

    public int parseAttribute(String attribute) {
        int result = 0;
        try {
            result = Integer.parseInt(gridItem.getElement().getAttribute(attribute));
        } catch (NumberFormatException e) {
        }
        return result;
    }

    public HTMLPanel getContent() {
        return content;
    }

    public MaterialLink getHideLink() {
        return hideLink;
    }

    public void setItemWidget(Widget itemWidget) {
        this.itemWidget = itemWidget;
    }

    public Widget getItemWidget() {
        return itemWidget;
    }

    public Integer getObjectId() {
        return objectId;
    }

    public void setObjectId(Integer objectId) {
        this.objectId = objectId;
    }

    public Integer getReportId() {
        return reportId;
    }

    public void setReportId(Integer reportId) {
        this.reportId = reportId;
    }

    public String getComponentCode() {
        return componentCode;
    }

    public void setComponentCode(String componentCode) {
        this.componentCode = componentCode;
    }

    public String getComponentName() {
        if (componentName == null) {
            return getComponentCode();
        }
        return componentName;
    }

    public void setComponentName(String componentName) {
        this.componentName = componentName;
    }

    public Integer getReportWidgetId() {
        return reportWidgetId;
    }

    public void setReportWidgetId(Integer reportWidgetId) {
        this.reportWidgetId = reportWidgetId;
    }
}