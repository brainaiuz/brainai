package com.edatasite.workforce.gwt.core.client.ui;

import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.*;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.ComplexPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * Created by IntelliJ IDEA.
 * User: Ruslan Muhammadov
 * Date: Nov 10, 2009
 * Time: 9:32:33 PM
 * To change this template use File | Settings | File Templates.
 */

/**
 * This panel supports adding widgets vertically and horizontally.
 * Furthermore, it enables scroll when its size exceeds allowed.
 */
public class FlexPanel extends ComplexPanel implements HasClickHandlers {

    public static final String LEFT = "left";
    public static final String CENTER = "center";
    public static final String RIGHT = "right";

    public static final String TOP = "top";
    public static final String MIDDLE = "middle";
    public static final String BOTTOM = "bottom";

    private String panel = "panel-";
    private String panelMain = panel + "main";
    private String panelBorder = panel + "border";
    private String panelBorderTop = panelBorder + "-top";
    private String panelBorderBottom = panelBorder + "-bottom";
    private String panelBackground = panel + "background";

    private String styleName;

    private int biasValue;

    private Element parent;
    private Element middle;
    private ColorSet style;

    private int padding;

    /**
     * This element accepts whether parent or middle element's properties.
     * If panel should be oval, then parent shouldn't accept any widget or texts.
     */
    private Element tempElem;

    public FlexPanel() {
        this(0);
    }

    public FlexPanel(int biasValue) {
        this.biasValue = biasValue;

        setElement(parent = DOM.createDiv());
        setVerticalAlignment(TOP);

        if (biasValue > 0) {
            drawOvalPanel();
            tempElem = middle;
        } else {
            tempElem = parent;
        }
    }

    public FlexPanel(int biasValue, ColorSet color) {
        this.biasValue = biasValue;
        style = color;
        switch (style) {
            case SocialNetworksPanelColor:
                panelMain = panel + "main" + "-sn";
                panelBorder = panel + "border" + "-sn";
                panelBorderTop = panelBorder + "-top" + "-sn";
                panelBorderBottom = panelBorder + "-bottom" + "-sn";
                panelBackground = panel + "background" + "-sn";
                break;
            case BLUE:
                panelMain = panel + "main" + "-blue";
                panelBorder = panel + "border" + "-blue";
                panelBorderTop = panelBorder + "-top" + "-blue";
                panelBorderBottom = panelBorder + "-bottom" + "-blue";
                panelBackground = panel + "background" + "-blue";
                break;
        }

        setElement(parent = DOM.createDiv());
        setVerticalAlignment(TOP);

        if (biasValue > 0) {
            drawOvalPanel();
            tempElem = middle;
        } else {
            tempElem = parent;
        }
    }

    public FlexPanel(Boolean collapsable) {
        setElement(parent = DOM.createDiv());
        setVerticalAlignment(TOP);
        middle = DOM.createDiv();
        DOM.appendChild(parent, middle);
        tempElem = middle;
    }

    public void refreshing(String text) {
        LoadingPanel.loading(true);
    }

    public void refreshing() {
        LoadingPanel.loading(true);
    }

    public void stopRefreshing() {
        LoadingPanel.loading(false);
    }

    private void drawOvalPanel() {
        middle = DOM.createDiv();
        setStyleName(middle, panelBackground);

        DOM.appendChild(parent, drawUp());
        DOM.appendChild(parent, middle);
        DOM.appendChild(parent, drawDown());
    }

    private Element drawUp() {
        Element b = DOM.createElement("b");
        setStyleName(b, panelMain);

        for (int i = 0; i < biasValue; i++) {
            Element childB = setElementStyle(b, biasValue - i);
            if (i == 0) {
                setStyleName(childB, panelBorderTop);
            }
        }

        return b;
    }

    private Element drawDown() {
        Element b = DOM.createElement("b");
        setStyleName(b, panelMain);

        for (int i = biasValue - 1; i >= 0; i--) {
            Element childB = setElementStyle(b, biasValue - i);
            if (i == 0) {
                setStyleName(childB, panelBorderBottom);
            }
        }

        return b;
    }

    private Element setElementStyle(Element parentElem, int margin) {
        Element b = DOM.createElement("b");
        DOM.setIntStyleAttribute(b, "marginLeft", margin);
        DOM.setIntStyleAttribute(b, "marginRight", margin);
        DOM.appendChild(parentElem, b);

        setStyleName(b, panelBorder);

        return b;
    }

    /**
     * Adds a child widget to this panel.
     *
     * @param widget the child widget to be added
     */
    @Override
    public void add(Widget widget) {
        add(widget, tempElem);
    }

    public void add(Element elem) {
        tempElem.appendChild(elem);
    }

    public void setWidget(Widget widget) {
        DOM.insertChild(tempElem, widget.getElement(), 0);
    }

    public HandlerRegistration addClickHandler(ClickHandler handler) {
        return addDomHandler(handler, ClickEvent.getType());
    }

    public HandlerRegistration addMouseOverHandler(MouseOverHandler handler) {
        return addDomHandler(handler, MouseOverEvent.getType());
    }

    public HandlerRegistration addMouseOutHandler(MouseOutHandler handler) {
        return addDomHandler(handler, MouseOutEvent.getType());
    }

    public void setText(String text) {
        tempElem.setInnerText(text);
    }

    public void setHTML(String html) {
        tempElem.setInnerHTML(html);
    }

    public void setScrollEnabled(boolean scroll) {
        DOM.setStyleAttribute(tempElem, "overflow", scroll ? "auto" : "hidden");
    }

    public void setTitle(String text) {
        Element h3 = DOM.createElement("h3");
        DOM.setInnerHTML(h3, text);
        DOM.setElementAttribute(h3, "class", panel + "title");
        DOM.appendChild(parent, h3);
    }

    /**
     * Scroll to the bottom of this panel.
     */
    public void scrollToBottom() {
        setScrollPosition(DOM.getElementPropertyInt(tempElem, "scrollHeight"));
    }

    /**
     * Scroll to the far left of this panel.
     */
    public void scrollToLeft() {
        setHorizontalScrollPosition(0);
    }

    /**
     * Scroll to the far right of this panel.
     */
    public void scrollToRight() {
        setHorizontalScrollPosition(DOM.getElementPropertyInt(tempElem, "scrollWidth"));
    }

    /**
     * Scroll to the top of this panel.
     */
    public void scrollToTop() {
        setScrollPosition(0);
    }

    /**
     * Sets the horizontal scroll position.
     *
     * @param position the new horizontal scroll position, in pixels
     */
    public void setHorizontalScrollPosition(int position) {
        DOM.setElementPropertyInt(tempElem, "scrollLeft", position);
    }

    /**
     * Sets the vertical scroll position.
     *
     * @param position the new vertical scroll position, in pixels
     */
    public void setScrollPosition(int position) {
        DOM.setElementPropertyInt(tempElem, "scrollTop", position);
    }

    public void setBorders(boolean show) {
        if (show) {
            setStyleName(styleName = "tree-border-style");
        } else if (styleName != null) {
            removeStyleName(styleName);
        }
    }


    public void setBorderLeft(String color) {
        setBorderLeft(color, 1, "solid");
    }

    public void setBorderLeft(String color, int borderWidth) {
        setBorderLeft(color, borderWidth, "solid");
    }

    public void setBorderLeft(String color, int borderWidth, String borderStyle) {
        DOM.setStyleAttribute(getElement(), "borderLeft", borderWidth + "px " + borderStyle + " " + color);
    }

    public void setBorderTop(String color) {
        setBorderTop(color, 1, "solid");
    }

    public void setBorderTop(String color, int borderWidth) {
        setBorderTop(color, borderWidth, "solid");
    }

    public void setBorderTop(String color, int borderWidth, String borderStyle) {
        DOM.setStyleAttribute(getElement(), "borderTop", borderWidth + "px " + borderStyle + " " + color);
    }

    public void setBorderRight(String color) {
        setBorderRight(color, 1, "solid");
    }

    public void setBorderRight(String color, int borderWidth) {
        setBorderRight(color, borderWidth, "solid");
    }

    public void setBorderRight(String color, int borderWidth, String borderStyle) {
        DOM.setStyleAttribute(getElement(), "borderRight", borderWidth + "px " + borderStyle + " " + color);
    }

    public void setBorderBottom(String color) {
        setBorderBottom(color, 1, "solid");
    }

    public void setBorderBottom(String color, int borderWidth) {
        setBorderBottom(color, borderWidth, "solid");
    }

    public void setBorderBottom(String color, int borderWidth, String borderStyle) {
        DOM.setStyleAttribute(getElement(), "borderBottom", borderWidth + "px " + borderStyle + " " + color);
    }

    public int getPadding() {
        return padding;
    }

    public void setPadding(int padding) {
        this.padding = padding;

        DOM.setIntStyleAttribute(getElement(), "padding", padding);
    }

    public void setPaddingLeft(int padding) {
        DOM.setIntStyleAttribute(getElement(), "paddingLeft", padding);
    }

    public void setPaddingRight(int padding) {
        DOM.setIntStyleAttribute(getElement(), "paddingRight", padding);
    }

    public void setPaddingTop(int padding) {
        DOM.setIntStyleAttribute(getElement(), "paddingTop", padding);
    }

    public void setPaddingBottom(int padding) {
        DOM.setIntStyleAttribute(getElement(), "paddingBottom", padding);
    }

    public void setHorizontalAlignment(String alignment) {
        DOM.setElementProperty(getElement(), "align", alignment);
    }

    public void setVerticalAlignment(String alignment) {
        DOM.setElementProperty(getElement(), "verticalAlign", alignment);
    }

    public void removeAll() {
        while (DOM.getChildCount(getElement()) > 0) {
            DOM.removeChild(getElement(), DOM.getChild(getElement(), 0));
        }
    }

    public enum ColorSet {
        SocialNetworksPanelColor, WHITE, BLACK, BLUE
    }
}
