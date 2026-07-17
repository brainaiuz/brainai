package com.edatasite.workforce.gwt.core.client.ui;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.DOM;

/**
 * User: Jamshid Asatillayev
 * Date: 6/18/11
 * Time: 9:58 PM
 */
public class TextUtils {
    private static TextUtils instance;

    static {
        instance = new TextUtils();
    }

    /**
     * Returns the singleton instance.
     *
     * @return the text TextUtil instance
     */
    public static TextUtils get() {
        return instance;
    }

    private Element elem;

    private TextUtils() {
        elem = DOM.createDiv();
        Document.get().getBody().appendChild(elem);
        DOM.setStyleAttribute(elem, "position", "absolute");
        elem.getStyle().setTop(-3000, Style.Unit.PX);
        elem.getStyle().setLeft(-4000, Style.Unit.PX);
        elem.getStyle().setVisibility(Style.Visibility.HIDDEN);
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
     * Returns the measured height of the specified text. For multiline text, be
     * sure to call {@link #setFixedWidth} if necessary.
     *
     * @param text the text to be measured
     * @return the height in pixels
     */
    public int getHeight(String text) {
        return getSize(text).height;
    }

    /**
     * Returns the size of the specified text based on the internal element's
     * style and width properties.
     *
     * @param text the text to measure
     * @return the size
     */
    public TextSize getSize(String text) {
        DOM.setInnerHTML(elem, text);
        final TextSize size = new TextSize(elem);
        DOM.setInnerHTML(elem, "");
        return size;
    }

    /**
     * Returns the measured width of the specified text.
     *
     * @param text the text to measure
     * @return the width in pixels
     */
    public int getWidth(String text) {
        DOM.setStyleAttribute(elem, "width", "auto");
        return getSize(text).width;
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