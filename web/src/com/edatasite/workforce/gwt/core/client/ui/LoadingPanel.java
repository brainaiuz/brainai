package com.edatasite.workforce.gwt.core.client.ui;

import com.edatasite.workforce.gwt.core.client.ui.entryPoints.carcass.MainLayout;
import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.constants.CssName;
import gwt.material.design.client.ui.html.Div;

public class LoadingPanel {

    String LOADER = "lds-message";
    private static LoadingPanel instance = new LoadingPanel();
    private Div preLoader = new Div();

    private Div lds = new Div(LOADER);
    private Div div = new Div();

    private Div circle1 = new Div();
    private Div circle2 = new Div();
    private Div circle3 = new Div();

    private Panel container = MainLayout.get().getContentBody();

    private LoadingPanel() {
        div.setStyleName(CssName.VALIGN_WRAPPER + " " + CssName.LOADER_WRAPPER);
        preLoader.getElement().getStyle().setProperty("margin", "auto");
        preLoader.setStyleName("material-preloader");
        lds.add(circle1);
        lds.add(circle2);
        lds.add(circle3);
        preLoader.add(lds);
    }

    public static void loading(boolean visible) {
        instance.div.addStyleName("content-back-drop");
        loading(visible, MainLayout.get().getContentBody(), false);
    }

    public static void loading(boolean visible, Panel container) {
        loading(visible, container, true);
    }

    public static void loading(boolean visible, Panel container, boolean isCustomPanel) {
        if (isCustomPanel) {
            instance.div.setWidth("100%");
            instance.div.setLayoutPosition(Style.Position.ABSOLUTE);
        } else {
            instance.div.setWidth("auto");
            instance.div.setLayoutPosition(Style.Position.FIXED);
        }

        instance.setContainer(container);
        if (visible) {
            instance.showLoader();
        } else {
            instance.hideLoader();
        }
    }

    private void showLoader() {
        div.add(preLoader);
        container.add(div);
    }

    public void hideLoader() {
        div.removeStyleName("content-back-drop");
        div.removeFromParent();
        preLoader.removeFromParent();
    }

    public Panel getContainer() {
        return container;
    }

    public void setContainer(Panel container) {
        this.container = container;
    }
}
