package com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.Button;

/**
 * The button that additionally supperts MOUSEEVENTS
 * <p/>
 * <p/>
 * Created by IntelliJ IDEA.
 * User: Anvarbek
 * Date: 29.01.2009
 * Time: 17:21:56
 * To change this template use File | Settings | File Templates.
 */
public class ButtonExt extends Button {

    private MouseEventsListener listener;

    public void onBrowserEvent(Event event) {
        super.onBrowserEvent(event);
        switch (DOM.eventGetType(event)) {
            case Event.ONMOUSEOVER:
                if (listener != null) {
                    listener.onMouseOver();
                }
                break;
            case Event.ONMOUSEOUT:
                if (listener != null) {
                    listener.onMouseOut();
                }
                break;
        }
    }

    protected void setElement(Element elem) {
        super.setElement(elem);
        sinkEvents(Event.MOUSEEVENTS);
    }

    public void setMouseEventListener(MouseEventsListener listener) {
        this.listener = listener;
    }
    //More universal approach like "click listener" may be chousen

    public interface MouseEventsListener {
        void onMouseOver();

        void onMouseOut();
    }

}
