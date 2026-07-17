/*
 * MyGWT Widget Library
 * Copyright(c) 2007, MyGWT.
 * licensing@mygwt.net
 * 
 * http://mygwt.net/license
 */
package com.google.gwt.user.client.ui;

/**
 * Provides public access to widget package protected methods.
 */
public class WidgetHelper {

    public static void doAttach(Widget widget) {
        widget.onAttach();
    }

    public static void doDetach(Widget widget) {
        widget.onDetach();
    }

    public static void setParent(Widget widget, Widget parent) {
        widget.setParent(parent);
    }
}
