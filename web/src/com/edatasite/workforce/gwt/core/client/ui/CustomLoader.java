package com.edatasite.workforce.gwt.core.client.ui;

import com.edatasite.workforce.gwt.core.client.ui.entryPoints.carcass.MainLayout;
import com.google.gwt.user.client.ui.Panel;
import gwt.material.design.client.constants.CssName;
import gwt.material.design.client.ui.html.Div;

/**
 * Created by Hurshid on 12/15/2017.
 */
public class CustomLoader {

    String LOADER_WAVE = "lds-wave";
    private static CustomLoader loader = new CustomLoader();
    private Div preLoader = new Div();

    private Div lds = new Div(LOADER_WAVE);
    private Div div = new Div();

    private Div circle1 = new Div();
    private Div circle2 = new Div();
    private Div circle3 = new Div();

    private Panel container = MainLayout.get().getContentBody();

    private CustomLoader() {
        div.setStyleName(CssName.VALIGN_WRAPPER + " " + CssName.LOADER_WRAPPER);
        preLoader.getElement().getStyle().setProperty("margin", "auto");
        lds.add(circle1);
        lds.add(circle2);
        lds.add(circle3);
        preLoader.add(lds);
    }

    public static void loading(boolean visible) {
        loading(visible, MainLayout.get().getContentBody());
    }

    public static void loading(boolean visible, Panel container) {
        loader.setContainer(container);
        if (visible) {
            loader.show();
        } else {
            loader.hide();
        }
    }

    public void show() {
        div.add(preLoader);
        container.add(div);
    }

    /**
     * Hides the Loader component
     */
    public void hide() {
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
