package com.edatasite.workforce.gwt.reportingsystem.client.ui.widget;

import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.ui.html.Span;

public class ViewAllButton extends Composite {
    private WfmStrings wfmStrings = WfmStrings.App.get();
    @UiField
    HTMLPanel viewAllButton;
    @UiField
    Span viewAllLabel;
    interface ViewAllButtonUiBinder extends UiBinder<Widget, ViewAllButton> {
    }

    private static ViewAllButtonUiBinder ourUiBinder = GWT.create(ViewAllButtonUiBinder.class);
    public ViewAllButton(Command clickCommand) {
        Widget widget = ourUiBinder.createAndBindUi(this);
        initWidget(widget);
        widget.addHandler((e) -> {
            Window.alert("clicked");
        }, ClickEvent.getType());
        viewAllLabel.setText(wfmStrings.viewAll());
    }
}
