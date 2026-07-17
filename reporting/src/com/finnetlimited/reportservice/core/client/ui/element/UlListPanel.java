/*
package com.finnetlimited.reportservice.core.client.ui.element;


import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.ComplexPanel;
import com.google.gwt.user.client.ui.InsertPanel;
import com.google.gwt.user.client.ui.Widget;


*/
/**
 * A panel that uses the HTML UL element. All children will be wrapped into LI elements.
 * <p/>
 * Using UL lists is a modern pattern to layout web pages, as it is easy to style them
 * with CSS. Moreover, they have several advantages over tables (changing the layout
 * requires changing the code, accessibility, etc).
 *
 * @author Markus
 *//*

public class UlListPanel extends ComplexPanel implements InsertPanel {

    */
/**
 * Creates an empty flow panel.
 *//*

    public UlListPanel() {
        setElement(DOM.createElement("UL"));
    }

    private LiPanel wrapWidget(Widget w) {
        LiPanel li = new LiPanel();
        li.add(w);
        return li;
    }

    private LiPanel wrapWidget(Widget w, String styleName) {
        LiPanel li = new LiPanel();
        li.addStyleName(styleName);
        li.add(w);
        return li;
    }

    */
/**
 * Adds a new child widget to the panel.
 *
 * @param w the widget to be added
 *//*

    @Override
    public void add(Widget w) {
        add(wrapWidget(w), getElement());
    }

    public void add(Widget w, String styleName) {
        add(wrapWidget(w, styleName), getElement());
    }

    */
/**
 * Inserts a widget before the specified index.
 *
 * @param w           the widget to be inserted
 * @param beforeIndex the index before which it will be inserted
 * @throws IndexOutOfBoundsException if <code>beforeIndex</code> is out of range
 *//*

    public void insert(Widget w, int beforeIndex) {
        insert(wrapWidget(w), getElement(), beforeIndex, true);
    }

    */
/**
 * The LI element for use in {@link UlListPanel}s.
 *
 * @author Markus
 *//*

    private static class LiPanel extends ComplexPanel implements InsertPanel {

        protected LiPanel() {
            setElement(DOM.createElement("LI"));
        }

        */
/**
 * Adds a new child widget to the panel.
 *
 * @param w the widget to be added
 *//*

        @Override
        public void add(Widget w) {
            add(w, getElement());
        }


        */
/**
 * Inserts a widget before the specified index.
 *
 * @param w           the widget to be inserted
 * @param beforeIndex the index before which it will be inserted
 * @throws IndexOutOfBoundsException if <code>beforeIndex</code> is out of range
 *//*

        public void insert(Widget w, int beforeIndex) {
            insert(w, getElement(), beforeIndex, true);
        }
    }
}*/
