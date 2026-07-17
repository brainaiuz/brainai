package com.edatasite.workforce.gwt.core.client.ui.formWidgets.multilookup;

import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.google.gwt.event.dom.client.*;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.ComplexPanel;
import com.google.gwt.user.client.ui.HasHTML;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.Widget;

public class UL extends ComplexPanel {
    public UL() {
        setElement(DOM.createElement("UL"));
    }

    public void add(Widget w) {
        super.add(w, getElement());
    }

    public void insert(Widget w, int beforeIndex) {
        super.insert(w, getElement(), beforeIndex, true);
    }

    public static class LI extends ComplexPanel implements HasText, HasHTML, HasClickHandlers, HasKeyDownHandlers, HasBlurHandlers {
        HandlerRegistration clickHandler;
        private SelectItem selectItem;

        public LI() {
            setElement(DOM.createElement("LI"));
        }

        public LI(SelectItem item) {
            this();
            this.selectItem = item;
            setId(String.valueOf(item.getId()));
        }

        public void add(Widget w) {
            super.add(w, getElement());
        }

        public void insert(Widget w, int beforeIndex) {
            super.insert(w, getElement(), beforeIndex, true);
        }

        public String getText() {
            return DOM.getInnerText(getElement());
        }

        public void setText(String text) {
            DOM.setInnerText(getElement(), (text == null) ? "" : text);
        }

        public void setId(String id) {
            DOM.setElementAttribute(getElement(), "id", id);
        }

        public String getHTML() {
            return DOM.getInnerHTML(getElement());
        }

        public void setHTML(String html) {
            DOM.setInnerHTML(getElement(), (html == null) ? "" : html);
        }

        public HandlerRegistration addClickHandler(ClickHandler handler) {
            return addDomHandler(handler, ClickEvent.getType());
        }

        public HandlerRegistration addKeyDownHandler(KeyDownHandler handler) {
            return addDomHandler(handler, KeyDownEvent.getType());
        }

        public HandlerRegistration addBlurHandler(BlurHandler handler) {
            return addDomHandler(handler, BlurEvent.getType());
        }

        public SelectItem getSelectItem() {
            return selectItem;
        }
    }
}

