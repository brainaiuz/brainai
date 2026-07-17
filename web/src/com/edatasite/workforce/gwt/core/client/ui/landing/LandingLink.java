package com.edatasite.workforce.gwt.core.client.ui.landing;

import com.edatasite.workforce.gwt.core.client.ui.factory.SinksContainerFactory;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.AnchorElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.AbstractImagePrototype;
import com.google.gwt.user.client.ui.Widget;

public class LandingLink extends Widget implements HasClickHandlers {

    private Element div;
    private String action;

    public LandingLink(String linkName, String style, String action) {
        this(style);
        this.action = action;
        addLink(linkName);
    }

    public LandingLink(String linkName, String style, String url, boolean simpleLink) {
        this(style);
        this.action = url;
        if (simpleLink) {
            addSimpleLink(linkName, url);
        } else {
            addLink(linkName);
        }
    }

    public LandingLink(String linkName, String style, String section, String action) {
        this(style);

        String url = GWT.getHostPageBaseURL() + section + "#" + action;

        addSimpleLink(linkName, url);
    }

//    public LandingLink(String linkName, AbstractImagePrototype style, String action) {
//        this(style);
//        String url = GWT.getHostPageBaseURL() + section + "#" + action;
//        addSimpleLink(linkName, url);
//    }

    public LandingLink(String linkName, AbstractImagePrototype style, String positionStyle, String action) {
        this(style);
        this.action = action;
        addBundleLink(linkName, positionStyle);
    }

    private LandingLink(AbstractImagePrototype style) {

        div = DOM.createDiv();
        div.addClassName("img-container");


        DOM.insertChild(div, style.createImage().getElement(), 1);
        setElement(div);
    }


    private void addBundleLink(String name, String positionStyle) {
        Element a = DOM.createAnchor();
        clickListener(a);
        a.addClassName("abs-link");

        a.addClassName(positionStyle);
        DOM.setElementAttribute(a, "href", "javascript:;");
        DOM.setInnerText(a, name);
        DOM.insertChild(div, a, 2);
    }


    private LandingLink(String style) {
        div = DOM.createDiv();
        div.addClassName("bid-block");
        DOM.setElementAttribute(div, "id", style);
        setElement(div);
    }

    private void addLink(String name) {
        Element a = DOM.createAnchor();
        clickListener(a);
        a.addClassName("abs-link");
        DOM.setElementAttribute(a, "href", "javascript:;");
        DOM.setInnerHTML(a, name);
        DOM.insertChild(div, a, 1);
    }

    private void addSimpleLink(String name, String url) {
        Element a = DOM.createAnchor();
        a.addClassName("abs-link");
        DOM.setElementAttribute(a, "href", url);
        DOM.setElementAttribute(a, "target", "_blank");
        DOM.setInnerText(a, name);
        DOM.insertChild(div, a, 1);
    }

    private void clickListener(Element elem) {
        DOM.sinkEvents(elem, Event.ONCLICK);
        DOM.setEventListener(elem, event -> clickProcessor(event));
    }

//    private void onBaseEvent(BaseEvent be) {
//
//    }

    private void clickProcessor(Event event) {
//        BaseEvent be = new BaseEvent();
//        be.type = DOM.eventGetType(event);

        if (Event.ONCLICK == DOM.eventGetType(event)) {
            SinksContainerFactory.entryPoint.onHistoryChanged(action);
        }
//        else {
//            onBaseEvent(be);
//        }
    }

    public void setSimpleLinkUrl(String url) {
        div.getFirstChildElement().getStyle().setProperty("href", url);
    }

    public void setElement(Element elem) {
        super.setElement(elem);
    }

    public HandlerRegistration addClickHandler(ClickHandler handler) {
        return addDomHandler(handler, ClickEvent.getType());
    }
}
