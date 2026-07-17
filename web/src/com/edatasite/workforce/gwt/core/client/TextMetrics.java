package com.edatasite.workforce.gwt.core.client;

/**
 * User: Aziz
 * Date: 11/16/12
 * Copied from MyGWT
 */

import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.DOM;

/**
 * Provides precise pixel measurements for blocks of text so that you can
 * determine exactly how high and wide, in pixels, a given block of text will
 * be.
 */
public class TextMetrics {

    private static TextMetrics instance;

    static {
        instance = new TextMetrics();
    }

    /**
     * Returns the singleton instance.
     *
     * @return the text metrics instance
     */
    public static TextMetrics get() {
        return instance;
    }

    private Element elem;

    private TextMetrics() {
        elem = DOM.createDiv();
        DOM.appendChild(PanelUtils.getBody(), elem);
        DOM.setStyleAttribute(elem, "position", "absolute");
        DOM.setStyleAttribute(elem, "left", -10000 + "px");
        DOM.setStyleAttribute(elem, "top", -1000 + "px");
        PanelUtils.setVisibility(elem, false);
    }

    /**
     * Binds this TextMetrics instance to an element from which to copy existing
     * CSS styles that can affect the size of the rendered text.
     *
     * @param el the element
     */
    public void bind(Element el) {
        DOM.setStyleAttribute(elem, "fontSize", DOM.getStyleAttribute(el, "fontSize"));
        DOM.setStyleAttribute(elem, "fontStyle", DOM.getStyleAttribute(el, "fontStyle"));
        DOM.setStyleAttribute(elem, "fontWeight", DOM.getStyleAttribute(el, "fontWeight"));
    }

    /**
     * Returns the measured width of the specified text.
     *
     * @param text the text to measure
     * @return the width in pixels
     */
    public int getWidth(String text) {
        DOM.setStyleAttribute(elem, "width", "auto");
        return DOM.getElementPropertyInt(elem, "offsetWidth");
    }

    /**
     * Sets a fixed width on the internal measurement element. If the text will be
     * multiline, you have to set a fixed width in order to accurately measure the
     * text height.
     *
     * @param width the width to set on the element
     */
    public void setFixedWidth(int width) {
        DOM.setIntStyleAttribute(elem, "width", width);
    }

}
