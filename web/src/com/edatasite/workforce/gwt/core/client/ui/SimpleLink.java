package com.edatasite.workforce.gwt.core.client.ui;

import com.edatasite.workforce.gwt.core.client.ui.factory.SinksContainerFactory;
import com.google.gwt.uibinder.client.UiConstructor;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Image;

import java.util.Optional;

public class SimpleLink extends HTML {

    public static int ADD_ICON = 1;
    public static int REMOVE_ICON = 2;
    public static int EDIT_ICON = 3;
    public static int MESSAGE_ICON = 4;
    public static int REFRESH_ICON = 5;

    private String action;
    private String name;
    private String title;
    private String tabName;

    //For PDF EXCEL and others
    public SimpleLink(String name) {
        this(name, null);
    }

    //For Sinks containers
    public SimpleLink(String name, String action) {
        this(name, action, "");
    }

    public SimpleLink(String name, String action, String title) {
        this(name, action, title, null);
    }

    public SimpleLink(String name, String action, String title, String tabName) {
        this(name, action, title, tabName, "");
    }

    /**
     * Base Simple link constructor
     *
     * @param name    link name
     * @param action  link action
     * @param title   link title that use as tab title
     * @param tabName tab name
     * @param style   custom style
     */
    public SimpleLink(String name, String action, String title, String tabName, String style) {
        super("<a class='" + style + "' href='javascript:;' title='" + title + "'>" + name + "</a>");

        DOM.setStyleAttribute(getElement(), "display", "inline");
        DOM.setStyleAttribute(getElement(), "cursor", "pointer");
        this.name = name;
        this.action = action;
        this.title = title;
        this.tabName = tabName;
        if (Optional.ofNullable(action).isPresent()) {
            runAction();
        }
    }

    public SimpleLink(String linkName, int iconStyle) {
        setHTML("<div><span>" + getIcon(iconStyle) + "</span>&nbsp;<span><a href='javascript:;'>" + linkName + "</a></span>" + "</div>");
    }

    public SimpleLink(Image icon, String linkName) {
        setHTML("<div><span style='vertical-align:middle;'>" + icon + "</span>&nbsp;<span><a href='javascript:;'>" + linkName + "</a></span>" + "</div>");
    }

    @UiConstructor
    public SimpleLink(int iconStyle) {
        setHTML("<div><span>" + getIcon(iconStyle) + "</span></div>");
    }

    public SimpleLink(String linkName, int iconStyle, String action) {
        this(linkName, iconStyle);

        if (Optional.ofNullable(action).isPresent()) {
            runAction();
        }
    }

    private void runAction() {
        addClickHandler(event -> SinksContainerFactory.entryPoint.onHistoryChanged(action, Optional.ofNullable(tabName).orElse(name), title));
    }

    @Override
    public String toString() {
        return name;
    }

    private Image getIcon(int iconStyle) {
        return new Image();
    }

    public String getAction() {
        return action;
    }
}