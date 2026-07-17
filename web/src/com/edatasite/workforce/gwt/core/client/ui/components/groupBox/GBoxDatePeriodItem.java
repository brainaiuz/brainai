package com.edatasite.workforce.gwt.core.client.ui.components.groupBox;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.ui.MaterialLabel;

/**
 * User: Abror Abdukadirov
 * Date: 07.02.2018 18:31
 */
@Deprecated
public class GBoxDatePeriodItem extends Composite {
    interface GBoxDatePeriodItemUiBinder extends UiBinder<Widget, GBoxDatePeriodItem> {
    }

    private static GBoxDatePeriodItemUiBinder ourUiBinder = GWT.create(GBoxDatePeriodItemUiBinder.class);

    @UiField
    GBoxLabel startGBoxItemLabel;
    @UiField
    MaterialLabel startLabel;
    @UiField
    GBoxComponent startComponent;
    @UiField
    GBoxLabel dueGBoxItemLabel;
    @UiField
    MaterialLabel dueLabel;
    @UiField
    GBoxComponent dueComponent;
    @Deprecated
    public GBoxDatePeriodItem() {
        initWidget(ourUiBinder.createAndBindUi(this));
        setStyleName("group-box__item invoice__date-due-date");
    }

    public void setStartBoxItem(String label, Widget widget) {
        this.setStartLabel(label);
        this.setStartComponent(widget);
    }

    public void setDueBoxItem(String label, Widget widget) {
        this.setDueLabel(label);
        this.setDueComponent(widget);
    }

    public void setStyleSplitRight(boolean split) {
        if (split) {
            this.addStyleName(GBoxItem.STYLE_SPLIT_RIGHT);
        } else {
            this.removeStyleName(GBoxItem.STYLE_SPLIT_RIGHT);
        }
    }

    public void setStartLabel(String startLabel) {
        this.startLabel.getElement().setInnerHTML(startLabel);
    }

    public void setStartComponent(Widget startComponent) {
        this.startComponent.add(startComponent);
    }

    public void setDueLabel(String dueLabel) {
        this.dueLabel.getElement().setInnerHTML(dueLabel);
    }

    public void setDueComponent(Widget dueComponent) {
        this.dueComponent.add(dueComponent);
    }
}