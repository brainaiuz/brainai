package com.edatasite.workforce.gwt.core.client.ui.clickablePanel;

import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.*;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.CellPanel;
import com.google.gwt.user.client.ui.HasAlignment;
import com.google.gwt.user.client.ui.Widget;

/**
 * Created by IntelliJ IDEA.
 * User: Ruslan Muhammadov
 * Date: 27.04.2009
 * Time: 18:40:38
 * To change this template use File | Settings | File Templates.
 */

/**
 * This class draws panel with its clickListener and all types of mouse listener.
 * ClickablePanel includes all VerticalPanel and HorizontalPanel options and accumulates them into one area.
 * addHoriontally() and insertHorizontally() methods are responsible for adding or inserting a widget horizontally.
 * addVertically() and insertVertically() methods are responsible for adding or inserting a widget vertically.
 */
public class ClickablePanel extends CellPanel implements HasAlignment, HasClickHandlers, HasAllMouseHandlers {

    private HorizontalAlignmentConstant horizontalAlign = ALIGN_LEFT;
    private VerticalAlignmentConstant verticalAlign = ALIGN_TOP;

    private Element tr;

    /**
     * Constructor that initializes necessary events and variables.
     */
    public ClickablePanel() {
        tr = DOM.createTR();

        DOM.appendChild(getBody(), tr);

        DOM.setElementProperty(getTable(), "cellSpacing", "0");
        DOM.setElementProperty(getTable(), "cellPadding", "0");
    }

    /**
     * This method is not usable.
     *
     * @param widget
     */
    public void add(Widget widget) {
        throw new IllegalArgumentException("\nIllegal Argument");
    }

    /**
     * Adds a child widget to the panel horizontally. If the Widget is already attached,
     * it will be moved to the end of the panel.
     *
     * @param w the widget to be added
     */
    public void addHorizontally(Widget widget) {
        Element td = createAlignedTD();
        DOM.appendChild(tr, td);
        super.add(widget, td);
    }

    public void addHorizontally(Element elem) {
        Element td = createAlignedTD();
        tr.appendChild(td);
        td.appendChild(elem);
    }

    /**
     * Inserts a widget horizontally before the specified index. If the Widget is already
     * attached panel, it will be moved to the specified index.
     *
     * @param w           the widget to be inserted
     * @param beforeIndex the index before which it will be inserted
     * @throws IndexOutOfBoundsException if <code>beforeIndex</code> is out of range
     */
    public void insertHorizontally(Widget widget, int beforeIndex) {
        checkIndexBoundsForInsertion(beforeIndex);

        /*
         * We have been reinserting already existing child with trick.
         *
         * For the WIDGET, it ultimately removes first and inserts second, so we
         * have to adjust the index within ComplexPanel.insert(). But for the DOM,
         * we insert first and remove second, which means we DON'T need to adjust
         * the index.
         */
        Element td = createAlignedTD();
        DOM.insertChild(tr, td, beforeIndex);
        super.insert(widget, td, beforeIndex, false);
    }

    /**
     * Adds a child widget to the panel vertically. If the Widget is already attached,
     * it will be moved to the end of the panel.
     *
     * @param w the widget to be added
     */
    public void addVertically(Widget widget) {
        tr = DOM.createTR();
        Element td = DOM.createTD();
        DOM.appendChild(getBody(), tr);
        DOM.appendChild(tr, td);
        super.add(widget, td);
    }

    /**
     * Inserts a widget vertically before the specified index. If the Widget is already
     * attached to the VerticalPanel, it will be moved to the specified index.
     *
     * @param w           the widget to be inserted
     * @param beforeIndex the index before which it will be inserted
     * @throws IndexOutOfBoundsException if <code>beforeIndex</code> is out of range
     */
    public void insertVertically(Widget widget, int beforeIndex) {
        checkIndexBoundsForInsertion(beforeIndex);

        tr = DOM.createTR();
        Element td = createAlignedTD();
        DOM.appendChild(tr, td);
        /**
         * The case where we reinsert an already existing child is tricky.
         *
         * For the WIDGET, it ultimately removes first and inserts second, so we
         * have to adjust the index within ComplexPanel.insert(). But for the DOM,
         * we insert first and remove second, which means we DON'T need to adjust
         * the index.
         */
        DOM.insertChild(getBody(), tr, beforeIndex);
        super.insert(widget, td, beforeIndex, false);
    }

    /**
     * Removes the horizontal widget from the table.
     *
     * @param widget
     * @return
     */
    public boolean removeHorizontally(Widget widget) {
        // Get the TD to be removed, before calling super.remove(), because
        // super.remove() will detach the child widget's element from its parent.
        Element td = DOM.getParent(widget.getElement());
        boolean removed = super.remove(widget);
        if (removed) {
            DOM.removeChild(tr, td);
        }
        return removed;
    }

    /**
     * Removes the vertical widget from the table.
     *
     * @param widget
     * @return
     */
    public boolean removeVertically(Widget widget) {
        /*
         * Get the TR to be removed before calling super.remove() because
         * super.remove() will detach the child widget's element from its parent.
         */
        Element td = DOM.getParent(widget.getElement());
        boolean removed = super.remove(widget);
        if (removed) {
            DOM.removeChild(getBody(), DOM.getParent(td));
        }
        return removed;
    }

    private Element createAlignedTD() {
        Element td = DOM.createTD();
        setCellHorizontalAlignment(td, horizontalAlign);
        setCellVerticalAlignment(td, verticalAlign);
        return td;
    }

    /**
     * Returns horizontal alignment of the panel.
     *
     * @return
     */
    public HorizontalAlignmentConstant getHorizontalAlignment() {
        return horizontalAlign;
    }

    /**
     * Sets the align horizontally to the panel.
     *
     * @param align
     */
    public void setHorizontalAlignment(HorizontalAlignmentConstant align) {
        horizontalAlign = align;
    }

    /**
     * Returns vertical alignment of the panel.
     *
     * @return
     */
    public VerticalAlignmentConstant getVerticalAlignment() {
        return verticalAlign;
    }

    /**
     * Sets the align vertically to the panel.
     *
     * @param align
     */
    public void setVerticalAlignment(VerticalAlignmentConstant align) {
        verticalAlign = align;
    }

    /**
     * Sets the scroll value to the panel.
     *
     * @param scroll
     */
    public void setScrollEnabled(boolean scroll) {
        String style = scroll ? "auto" : "hidden";
        DOM.setStyleAttribute(getElement(), "overflow", style);
    }

    private Element div;
    private Element b;
    private Element b1;
    private Element b2;
    private Element b3;
    private Element b4;

    private boolean render;
    private Widget widget;

    private void drawOvoidWidget() {
        div = DOM.createDiv();
        div.addClassName("menu-item-main-figure");

        Element td = createAlignedTD();
        DOM.appendChild(td, drawUp());
        DOM.appendChild(tr, td);
        DOM.appendChild(getBody(), tr);

        DOM.appendChild(td = createAlignedTD(), div);
        DOM.appendChild(tr = DOM.createTR(), td);
        DOM.appendChild(getBody(), tr);

        DOM.appendChild(td = createAlignedTD(), drawDown());
        DOM.appendChild(tr = DOM.createTR(), td);
        DOM.appendChild(getBody(), tr);
    }

    public void setOvoidWidget(Widget widget) {
        this.widget = widget;

        if (!render) {
            drawOvoidWidget();
            render = true;
        } else {
            while (DOM.getChildCount(div) > 0) {
                DOM.removeChild(div, DOM.getChild(div, 0));
            }

        }

        DOM.appendChild(div, widget.getElement());
    }

    public Widget getOvoidWidget() {
        return widget;
    }

    private Element drawUp() {
        init();

        DOM.appendChild(b, b1);
        DOM.appendChild(b, b2);
        DOM.appendChild(b, b3);
        DOM.appendChild(b, b4);

        return b;
    }

    private Element drawDown() {
        init();

        DOM.appendChild(b, b4);
        DOM.appendChild(b, b3);
        DOM.appendChild(b, b2);
        DOM.appendChild(b, b1);

        return b;
    }

    private void init() {
        b = DOM.createElement("b");
        b.addClassName("menu-item-main");

        DOM.appendChild(b1 = DOM.createElement("b"), DOM.createElement("b"));
        b1.addClassName("menu-item-side-1");

        DOM.appendChild(b2 = DOM.createElement("b"), DOM.createElement("b"));
        b2.addClassName("menu-item-side-2");

        b3 = DOM.createElement("b");
        b3.addClassName("menu-item-side-3");

        b4 = DOM.createElement("b");
        b4.addClassName("menu-item-side-4");
    }

    public HandlerRegistration addClickHandler(ClickHandler handler) {
        return addDomHandler(handler, ClickEvent.getType());
    }

    public HandlerRegistration addMouseDownHandler(MouseDownHandler handler) {
        return addDomHandler(handler, MouseDownEvent.getType());
    }

    public HandlerRegistration addMouseUpHandler(MouseUpHandler handler) {
        return addDomHandler(handler, MouseUpEvent.getType());
    }

    public HandlerRegistration addMouseOutHandler(MouseOutHandler handler) {
        return addDomHandler(handler, MouseOutEvent.getType());
    }

    public HandlerRegistration addMouseOverHandler(MouseOverHandler handler) {
        return addDomHandler(handler, MouseOverEvent.getType());
    }

    public HandlerRegistration addMouseMoveHandler(MouseMoveHandler handler) {
        return addDomHandler(handler, MouseMoveEvent.getType());
    }

    public HandlerRegistration addMouseWheelHandler(MouseWheelHandler handler) {
        return addDomHandler(handler, MouseWheelEvent.getType());
    }
}
