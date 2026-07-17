package com.edatasite.workforce.gwt.core.client.ui;

import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Widget;

import java.util.LinkedHashMap;

/**
 * Created by IntelliJ IDEA.
 * User: Virus
 * Date: 10.01.13
 * Time: 22:08
 * To change this template use File | Settings | File Templates.
 */
public class LoadingControl extends Widget {
    private static LinkedHashMap<String, LoadingControl> instance = new LinkedHashMap<>();
    private String code;
    private Element span;

    public LoadingControl(String code) {
        span = DOM.createSpan();
        span.setClassName("loader");
        this.code = code;
    }

    public static LoadingControl get(String code) {
        instance.computeIfAbsent(code, LoadingControl::new);
        return instance.get(code);
    }

    public void hide() {
        span.getStyle().setVisibility(Style.Visibility.HIDDEN);
        span.getStyle().setDisplay(Style.Display.NONE);
        instance.put(code, null);
        instance.remove(code);
        span.removeFromParent();
    }

    public void show() {
        DOM.getElementById(code).appendChild(span);
    }

}
