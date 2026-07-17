package com.edatasite.workforce.gwt.core.client.ui.splitButton;

import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.google.gwt.user.client.ui.Composite;
import gwt.material.design.client.base.MaterialWidget;
import gwt.material.design.client.ui.MaterialDropDown;
import gwt.material.design.client.ui.MaterialLink;
import gwt.material.design.client.ui.html.Div;
import gwt.material.design.client.ui.html.Icon;

/**
 * Created by Hurshid on 1/9/2018.
 */
public class MaterialSplitButton extends Composite {

    private MaterialDropDown menuContainer;

    public MaterialSplitButton(MaterialLink materialWidget) {
        this(materialWidget, Constants.BTN_PRIMARY);
    }

    public MaterialSplitButton(MaterialLink materialWidget, String buttonStyle) {
        this(materialWidget, buttonStyle, false);
    }

    public MaterialSplitButton(MaterialLink materialWidget, String buttonStyle, boolean directionTop) {
        initButton(materialWidget, buttonStyle, directionTop);
    }

    private Div menubar;
    private String buttonStyle;
    private MaterialLink mainButton;
    private MaterialLink more;

    private void initButton(MaterialLink ieLink, String buttonStyle, boolean directionTop) {
        this.mainButton = ieLink;
        this.buttonStyle = buttonStyle;
        if (!directionTop) {
            menubar = new Div("btn-group dropdown-split dropdown-split--top");
        } else {
            menubar = new Div("btn-group dropdown-split");
        }
        mainButton.addStyleName(buttonStyle);

        more = new MaterialLink();
        menuContainer = new MaterialDropDown(more);
        menuContainer.setClass("dropdown-content");
        menuContainer.setBelowOrigin(true);

        initWidget(menubar);
    }

    private void drawButton() {

        more.setHref("javaScript:void(0)");
        more.setStyleName("dropdown-button " + buttonStyle);
        more.setDataAttribute("alignment", "right");
        more.addBlurHandler(bh -> {
            menubar.removeStyleName("dropdown-split--open");
            menubar.addStyleName("dropdown-split");
        });
        more.addClickHandler(ch -> {
            if (menubar.getStyleName().contains("dropdown-split--open")) {
                menubar.removeStyleName("dropdown-split--open");
                menubar.addStyleName("dropdown-split");
            } else {
                menubar.removeStyleName("dropdown-split");
                menubar.addStyleName("dropdown-split--open");
            }
        });

        Icon moreIcon = new Icon();
        moreIcon.setClass("ficon--more-horiz");
        more.add(moreIcon);
        Div div = new Div("btn-group dropdown-split__toggle");
        div.add(more);
        div.add(menuContainer);
        menubar.add(div);
    }

    public void addItem(MaterialWidget widget) {
        menuContainer.add(widget);
    }

    boolean isAttached;
    @Override
    protected void onLoad() {
        super.onLoad();

        if (!isAttached && !menuContainer.getItems().isEmpty()) {
            menubar.clear();
            menubar.add(mainButton);

            drawButton();

            isAttached = true;
        }
    }

    public void setEnabled(boolean enabled) {
        more.setEnabled(enabled);
        mainButton.setEnabled(enabled);
        menubar.setEnabled(enabled);
    }
}
