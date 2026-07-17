package com.edatasite.workforce.gwt.core.client.ui.viewFooter;

import com.edatasite.workforce.gwt.core.client.ui.entryPoints.carcass.MainLayout;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.ui.html.Div;

import java.util.List;

/**
 * <div class="frame__info fixed-content">
 * leftSideWidgets
 * rigthSideWidgets
 * </div>
 */
public class ViewFooter extends Div {
    private Div leftSideWidgets;
    private Div rightSideWidgets;
    private IFooteredView footerProvider;

    public ViewFooter(IFooteredView footerProvider) {
        super("frame__info fixed-content");
        leftSideWidgets = new Div("frame__info__informers");
        add(leftSideWidgets);
        rightSideWidgets = new Div("frame__info__actions");
        add(rightSideWidgets);
        setFooterProvider(footerProvider);
    }

    public void setFooterProvider(IFooteredView footerProvider) {
        this.footerProvider = footerProvider;
        setLeftSideWidgets(footerProvider.getFooterLeftSideWidgets());
        setRightSideWidgets(footerProvider.getFooterRightSideWidgets());
    }

    private void setLeftSideWidgets(List<Widget> widgetList) {
        leftSideWidgets.clear();
        if (widgetList == null) {
            return;
        }
        widgetList.forEach(w -> addToLeftSide(w));
    }

    public List<Widget> getRightSideWidgets() {
        return rightSideWidgets.getChildrenList();
    }

    public void addToLeftSide(Widget w) {
        leftSideWidgets.add(w);
    }

    private void setRightSideWidgets(List<Widget> widgetList) {
        rightSideWidgets.clear();
        if (widgetList == null) {
            return;
        }
        widgetList.forEach(w -> addToRightSide(w));
    }

    public void addToRightSide(Widget w) {
        rightSideWidgets.add(w);
    }

    @Override
    public void onBrowserEvent(Event event) {
        super.onBrowserEvent(event);

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
