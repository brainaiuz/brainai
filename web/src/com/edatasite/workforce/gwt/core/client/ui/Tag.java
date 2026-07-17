package com.edatasite.workforce.gwt.core.client.ui;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Widget;


/**
 * User: Jamshid Asatillayev
 * Date: Mar 24, 2010
 * Time: 8:17:35 PM
 */
public class Tag extends Widget implements HasClickHandlers {

    public Tag() {
    }

    public Tag(String tag) {
        setTag(tag);
    }

    public Tag(String tag, String styleName) {
        this(tag, styleName, "", false);
    }

    public Tag(String tag, String styleName, String text) {
        this(tag, styleName, text, false);
    }

    public Tag(String tag, String text, Boolean asHTML) {
        this(tag, "", text, asHTML);
    }

    public Tag(String tag, String tagStyleName, String text, Boolean asHTML) {
        setTag(tag);
        if (asHTML) {
            setHTML(text);
        } else {
            setText(text);
        }
        if (!Utils.isNullOrEmpty(tagStyleName)) {
            setStyleName(tagStyleName);
        }
    }

    public void setHTML(String html) {
        getElement().setInnerHTML(html);
    }

    public void setText(String text) {
        getElement().setInnerText(text);
    }

    public void setInnerHTML(String innerHTML) {
        DOM.setInnerHTML(getElement(), innerHTML);
    }

    public void setInnerText(String innerText) {
        DOM.setInnerText(getElement(), innerText);
    }

    public String getInnetHTML() {
        return DOM.getInnerHTML(getElement());
    }

    public String getInnerText() {
        return DOM.getInnerText(getElement());
    }

    private void setTag(String t) {
        setElement(DOM.createElement(t));
    }

    public void setId(String id) {
        setAttribute("id", id);
    }

//    public void setStyleName(String name){
//        setStyleName(name);
//    }

    public void setAttribute(String attr, String value) {
        DOM.setElementAttribute(getElement(), attr, value);
    }


    public void setProperty(String prop, String value) {
        DOM.setElementProperty(getElement(), prop, value);
    }


    public String setProperty(String prop) {
        return DOM.getElementProperty(getElement(), prop);
    }

    @Override
    public HandlerRegistration addClickHandler(ClickHandler handler) {
        return addDomHandler(handler, ClickEvent.getType());
    }
}
