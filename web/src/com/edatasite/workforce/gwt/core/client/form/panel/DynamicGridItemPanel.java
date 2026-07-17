package com.edatasite.workforce.gwt.core.client.form.panel;

import com.edatasite.workforce.gwt.core.client.form.CustomizeFormItem;
import com.edatasite.workforce.gwt.core.client.form.DynamicField2;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * User: Abror Abdukadirov
 * Date: 09.08.2019 20:22
 */
public class DynamicGridItemPanel extends Composite {
    interface DynamicItemPanelUiBinder extends UiBinder<Widget, DynamicGridItemPanel> {
    }

    private static DynamicItemPanelUiBinder ourUiBinder = GWT.create(DynamicItemPanelUiBinder.class);

    @UiField
    HTMLPanel gridItem;
    @UiField
    HTMLPanel content;

    private String elementId;
    private Integer objectId;
    private String fieldId;
    private DynamicField2 field;
    private Command addedCommand;

    public DynamicGridItemPanel() {
        initWidget(ourUiBinder.createAndBindUi(this));
        this.elementId = DOM.createUniqueId();
        gridItem.getElement().setId(this.elementId);
    }

    public void initConfig(CustomizeFormItem item) {
        if (item != null) {
            this.setObjectId(item.getId());
            this.setFieldId(item.getName());
            gridItem.getElement().setAttribute("data-gs-x", String.valueOf(item.getX()));
            gridItem.getElement().setAttribute("data-gs-y", String.valueOf(item.getY()));
            gridItem.getElement().setAttribute("data-gs-width", String.valueOf(item.getWidth()));
            gridItem.getElement().setAttribute("data-gs-height", "1");
            gridItem.getElement().setAttribute("data-gs-max-height", "1");
//            gridItem.getElement().setAttribute("data-gs-min-width", "2");
        }
    }

    public int getX() {
        return parseAttribute("data-gs-x");
    }

    public void setX(int x) {
        gridItem.getElement().setAttribute("data-gs-x",  String.valueOf(x));
    }

    public void setY(int y) {
        gridItem.getElement().setAttribute("data-gs-y",  String.valueOf(y));
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
        } catch (NumberFormatException ignored) {
        }
        return result;
    }

    public Integer getObjectId() {
        return objectId;
    }

    public void setObjectId(Integer objectId) {
        this.objectId = objectId;
    }

    public void setContent(DynamicField2 field) {
        if (field == null) {
            return;
        }
        field.setAddedCommand(() -> addedCommand.execute());
        this.content.add(field);
    }

    public String getFieldId() {
        return fieldId;
    }

    public void setFieldId(String fieldId) {
        this.fieldId = fieldId;
    }

    public DynamicField2 getField() {
        return field;
    }

    public void setField(DynamicField2 field) {
        this.field = field;
    }

    public String getElementId() {
        return elementId;
    }

    public void setAddedCommand(Command addedCommand) {
        this.addedCommand = addedCommand;
    }
}
