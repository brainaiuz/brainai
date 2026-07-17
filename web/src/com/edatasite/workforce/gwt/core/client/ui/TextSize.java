package com.edatasite.workforce.gwt.core.client.ui;

import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.DOM;

/**
 * User: Jamshid Asatillayev
 * Date: 6/18/11
 * Time: 10:00 PM
 */
public class TextSize {
    public int height;
    public int width;

    public TextSize(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public TextSize(Element element) {
        width = DOM.getElementPropertyInt(element, "offsetWidth");
        height = DOM.getElementPropertyInt(element, "offsetHeight");
    }

    public static TextSize newInstance(int width, int height) {
        return new TextSize(width, height);
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }
}
