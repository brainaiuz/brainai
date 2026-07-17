package com.edatasite.workforce.gwt.core.client.ui.components.tooltip;

import gwt.material.design.jquery.client.api.JQueryElement;
import jsinterop.annotations.JsPackage;
import jsinterop.annotations.JsProperty;
import jsinterop.annotations.JsType;

@JsType(isNative = true, name = "Object", namespace = JsPackage.GLOBAL)
public class JsTooltipsterOptions {
    @JsProperty
    public boolean contentAsHTML;
    @JsProperty
    public boolean interactive;
    @JsProperty
    public JQueryElement content;
    @JsProperty
    public int maxWidth;
    @JsProperty
    public int minWidth;
    @JsProperty
    public String side;
    @JsProperty
    public boolean contentCloning;
    @JsProperty
    public int[] delay;
    @JsProperty
    public int[] delayTouch;
    @JsProperty
    public String trigger;
}
