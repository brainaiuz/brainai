package com.edatasite.workforce.gwt.core.client.ui.components.groupBox;

import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.ui.MaterialLabel;

@Deprecated
public class GBoxItem extends Composite implements Constants {

    interface GBoxItemUiBinder extends UiBinder<Widget, GBoxItem> {
    }

    private static GBoxItemUiBinder ourUiBinder = GWT.create(GBoxItemUiBinder.class);

    public static final String STYLE_SPLIT_RIGHT = "group-box__item--split-right";
    public static final String STYLE_WIDTH_FREE = "group-box__item--width-free";
    public static final String STYLE_NO_BORDER = "group-box__item-content--no-border";
    public static final String STYLE_DOUBLE_BOTTOM_GAP = "group-box__item--double-bottom-gap";
    public static final String STYLE_GBOX_ITEM = "group-box__item";
    public static final String STYLE_NO_OVERFLOW = "group-box__item--no-overflow";
    @UiField
    GBoxLabel gBoxItemLabel;
    @UiField
    MaterialLabel label;
    @UiField
    GBoxComponent component;
    @Deprecated
    public GBoxItem() {
        initWidget(ourUiBinder.createAndBindUi(this));
        this.addStyleName(STYLE_GBOX_ITEM);
    }
    @Deprecated
    public GBoxItem(Widget widget) {
        initWidget(ourUiBinder.createAndBindUi(this));
        label.getElement().setInnerHTML("&nbsp;");
        this.setComponent(widget);
        this.addStyleName(STYLE_GBOX_ITEM);
    }

    @Deprecated
    public GBoxItem(Widget widget, String label) {
        this(label, widget);
    }
    @Deprecated
    public GBoxItem(String label, Widget widget, String boxItemStyleName) {
        this(label, widget);
        this.addStyleName(boxItemStyleName);
    }
    @Deprecated
    public GBoxItem(String label, Widget widget) {
        initWidget(ourUiBinder.createAndBindUi(this));
        this.setLabel(label);
        this.setComponent(widget);
        this.addStyleName(STYLE_GBOX_ITEM);
    }

    public void setLabel(String text) {
        label.getElement().setInnerHTML(text);
    }

    public void setComponent(Widget widget) {
        component.clear();
        component.add(widget);
    }

    public void addComponent(Widget widget) {
        component.add(widget);
    }

    public void addStyleToComponent(String styleName) {
        this.component.addStyleName(styleName);
    }

    public void setStyleSplitRight(boolean split) {
        if (split) {
            this.addStyleName(STYLE_SPLIT_RIGHT);
        } else {
            this.removeStyleName(STYLE_SPLIT_RIGHT);
        }
    }

    public GBoxItem setStyleWidthFree(boolean widthFree) {
        if (widthFree) {
            this.addStyleName(STYLE_WIDTH_FREE);
        } else {
            this.removeStyleName(STYLE_WIDTH_FREE);
        }
        return this;
    }

    public GBoxItem setWidthLinear(String width) {
        super.setWidth(width);
        return this;
    }

    public GBoxItem setStyleNoBorder(boolean noBorder) {
        if (noBorder) {
            component.addStyleName(STYLE_NO_BORDER);
        } else {
            component.removeStyleName(STYLE_NO_BORDER);
        }
        return GBoxItem.this;
    }

    public GBoxItem setStyleNoOverFlow(boolean noOverflow) {
        if (noOverflow) {
            addStyleName(STYLE_NO_OVERFLOW);
        } else {
            removeStyleName(STYLE_NO_OVERFLOW);
        }
        return GBoxItem.this;
    }

    public GBoxLabel getgBoxItemLabel() {
        return gBoxItemLabel;
    }

    public void removeBoxItemLabel() {
        this.gBoxItemLabel.removeFromParent();
    }

    public MaterialLabel getLabel() {
        return label;
    }

    public GBoxComponent getComponent() {
        return component;
    }
}
