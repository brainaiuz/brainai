/*
 * This file is part of gwt-cal
 * Copyright (C) 2009  Scottsdale Software LLC
 * 
 * gwt-cal is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.

 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/
 */

package com.edatasite.workforce.gwt.googlecalendar.client.ui.newVersion;

import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.*;

/**
 * Abstract class for widgets that react to keystrokes and mouse gestures
 * providing a centralized place for the association between user inputs and the
 * logic to perform. Subclasses will implement the <code>onXXXKeyPressed</code>,
 * <code>onMouseDown</code> and <code>onDoubleClick</code> methods to provide
 * the custom event processing logic.
 */
public abstract class InteractiveWidget extends Composite {

    /**
     * Focus widget used to add keyboard and mouse focus to a calendar.
     */
    private FocusPanel focusPanel = new FocusPanel();

    /**
     * Main panel hold all other components.
     */
    protected FlowPanel rootPanel = new FlowPanel();

    /**
     * Used by focus widget to make sure a key stroke is only handled once by
     * the calendar.
     */
    private boolean lastWasKeyDown = false;

    public InteractiveWidget() {

        initWidget(rootPanel);

        //Sink events, mouse and keyboard for now
        sinkEvents(Event.ONMOUSEDOWN | Event.ONDBLCLICK | Event.KEYEVENTS | Event.ONCLICK);

        hideFocusPanel();

        //Add key handler events to the focus panel
        focusPanel.addKeyPressHandler(event -> {
            if (!lastWasKeyDown) {
                keyboardNavigation(event.getNativeEvent().getKeyCode());
            }
            lastWasKeyDown = false;
        });

        focusPanel.addKeyUpHandler(event -> lastWasKeyDown = false);

        focusPanel.addKeyDownHandler(event -> {
            keyboardNavigation(event.getNativeEvent().getKeyCode());
            lastWasKeyDown = true;
        });
    }

    /**
     * Makes the widget's focus panel invisible.
     */
    private void hideFocusPanel() {
        RootPanel.get().add(focusPanel);
        DOM.setStyleAttribute(focusPanel.getElement(), "position", "absolute");
        DOM.setStyleAttribute(focusPanel.getElement(), "top", "-10");
        DOM.setStyleAttribute(focusPanel.getElement(), "left", "-10");
        DOM.setStyleAttribute(focusPanel.getElement(), "height", "0px");
        DOM.setStyleAttribute(focusPanel.getElement(), "width", "0px");
    }

    public ComplexPanel getRootPanel() {
        return rootPanel;
    }

    /**
     * Processes mouse double-click events. Concrete interactive widgets should
     * provide the component's specific logic.
     *
     * @param element The HTML DOM element that originated the event
     */
    public abstract void onDoubleClick(Element element, Event event);

    /**
     * Processes mouse button pressing events. Concrete interactive widgets
     * should provide the component's specific logic.
     *
     * @param element The HTML DOM element that originated the event
     */
    public abstract void onMouseDown(Element element, Event event);

    /**
     * Processes {@link com.google.gwt.event.dom.client.KeyCodes.KEY_DELETE}
     * keystrokes. Concrete interactive widgets should provide the component's
     * specific logic.
     */
    public abstract void onDeleteKeyPressed();

    /**
     * Processes {@link com.google.gwt.event.dom.client.KeyCodes.KEY_UP}
     * keystrokes. Concrete interactive widgets should provide the component's
     * specific logic.
     */
    public abstract void onUpArrowKeyPressed();

    /**
     * Processes {@link com.google.gwt.event.dom.client.KeyCodes.KEY_DOWN}
     * keystrokes. Concrete interactive widgets should provide the component's
     * specific logic.
     */
    public abstract void onDownArrowKeyPressed();

    /**
     * Processes {@link com.google.gwt.event.dom.client.KeyCodes.KEY_LEFT}
     * keystrokes. Concrete interactive widgets should provide the component's
     * specific logic.
     */
    public abstract void onLeftArrowKeyPressed();

    /**
     * Processes {@link com.google.gwt.event.dom.client.KeyCodes.KEY_RIGHT}
     * keystrokes. Concrete interactive widgets should provide the component's
     * specific logic.
     */
    public abstract void onRightArrowKeyPressed();

    @Override
    public void onBrowserEvent(Event event) {
        int eventType = DOM.eventGetType(event);
        Element element = DOM.eventGetTarget(event);

        switch (eventType) {
            case Event.ONCLICK: {
                onDoubleClick(element, event);
                focusPanel.setFocus(true);
                break;
            }
            case Event.ONMOUSEDOWN: {
                if (DOM.eventGetCurrentTarget(event).equals(getElement())) {
                    onMouseDown(element, event);
                    focusPanel.setFocus(true);
                    //Cancel events so Firefox / Chrome don't
                    //give child widgets with scrollbars focus.
                    DOM.eventCancelBubble(event, true);
                    DOM.eventPreventDefault(event);
                    return;
                }
            }
        }

        super.onBrowserEvent(event);
    }

    /**
     * Dispatches the processing of a key being pressed to the this widget
     * <code>onXXXXKeyPressed</code> methods.
     *
     * @param key Pressed key code
     */
    protected void keyboardNavigation(int key) {
        switch (key) {
            case KeyCodes.KEY_DELETE: {
                onDeleteKeyPressed();
                break;
            }
            case KeyCodes.KEY_LEFT: {
                onLeftArrowKeyPressed();
                break;
            }
            case KeyCodes.KEY_UP: {
                onUpArrowKeyPressed();
                break;
            }
            case KeyCodes.KEY_RIGHT: {
                onRightArrowKeyPressed();
                break;
            }
            case KeyCodes.KEY_DOWN: {
                onDownArrowKeyPressed();
                break;
            }
        }
    }
}