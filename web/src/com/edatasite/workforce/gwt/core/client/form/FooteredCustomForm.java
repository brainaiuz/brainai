package com.edatasite.workforce.gwt.core.client.form;

import com.edatasite.workforce.gwt.core.client.rpc.form.LayoutInterface;
import com.edatasite.workforce.gwt.core.client.ui.viewFooter.IFooteredView;
import com.edatasite.workforce.gwt.core.client.ui.viewFooter.ViewFooter;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;

import java.util.List;

public abstract class FooteredCustomForm extends CustomForm {

    public FooteredCustomForm(String name) {
        super(name);
    }

    public FooteredCustomForm(String name, String description) {
        super(name, description);
    }

    @Override
    protected void addPanel(LayoutInterface layoutInterface) {
        super.addPanel(layoutInterface);
        panel.add(createFooter());
        panel.setStyleName("add-form");
    }

    private ViewFooter createFooter() {
        return new ViewFooter(new IFooteredView() {
            @Override
            public List<Widget> getFooterLeftSideWidgets() {
                return FooteredCustomForm.this.getFooterLeftSideWidgets();
            }

            @Override
            public List<Widget> getFooterRightSideWidgets() {
                return FooteredCustomForm.this.getFooterRightSideWidgets();
            }
        });
    }

    protected abstract List<Widget> getFooterLeftSideWidgets();

    protected abstract List<Widget> getFooterRightSideWidgets();

    protected Widget getWidgetAsFormControl(String value) {
        HTML formControl = new HTML();
        formControl.setStyleName("form-control");

        if (value != null && !value.isEmpty()) {
            formControl.setHTML(value);
        } else {
            formControl.setHTML("");
        }
        return formControl;
    }

    protected Widget wrapWidgetToFormControl(Widget widget) {

        if (widget != null) {
            widget.addStyleName("form-control");
        }
        return widget;
    }

    @Override
    protected void onAttach() {
        super.onAttach();
        RootPanel.get().addStyleName("has-frame__info");
    }

    @Override
    protected void onDetach() {
        super.onDetach();
        RootPanel.get().removeStyleName("has-frame__info");
    }

}
