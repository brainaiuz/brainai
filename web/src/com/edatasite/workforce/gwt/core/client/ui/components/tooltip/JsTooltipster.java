package com.edatasite.workforce.gwt.core.client.ui.components.tooltip;

import com.google.gwt.dom.client.Element;
import gwt.material.design.jquery.client.api.JQueryElement;
import jsinterop.annotations.JsMethod;
import jsinterop.annotations.JsPackage;
import jsinterop.annotations.JsType;

@JsType(isNative = true)
public class JsTooltipster extends JQueryElement {

    @JsMethod(name = "$", namespace = JsPackage.GLOBAL)
    public static native JsTooltipster $(JQueryElement element);

    @JsMethod(name = "$", namespace = JsPackage.GLOBAL)
    public static native JsTooltipster $(Element element);

    @JsMethod(name = "$", namespace = JsPackage.GLOBAL)
    public static native JsTooltipster $(String selector);

    @JsMethod
    public native JsTooltipster tooltipster(JsTooltipsterOptions options);
}

