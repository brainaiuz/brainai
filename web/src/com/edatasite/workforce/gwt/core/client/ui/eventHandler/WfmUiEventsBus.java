package com.edatasite.workforce.gwt.core.client.ui.eventHandler;

import com.edatasite.workforce.gwt.core.client.View;
import com.google.gwt.user.client.ui.Widget;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: Abdulaziz
 * Date: Sep 26, 2009
 * Time: 4:52:56 PM
 * To change this template use File | Settings | File Templates.
 */
public class WfmUiEventsBus {
    private Map<Integer, List<WfmUiEvent>> eventsListenersMap;
    private static WfmUiEventsBus instance = new WfmUiEventsBus();

    private WfmUiEventsBus() {
        eventsListenersMap = new HashMap<>();
    }

    public static WfmUiEventsBus getInstance() {
        return instance;
    }

    public Map<Integer, List<WfmUiEvent>> getEventsListenersMap() {
        return eventsListenersMap;
    }

    public void addUiEventListener(int eventType, WfmUiEvent wfmUiEvent, Widget widget) {
        if (mapActivityByEventType.containsKey(eventType)) {
            if (mapActivityByEventType.get(eventType)) {
                return;
            }
        }
        List<WfmUiEvent> eventsSubscribersList;
        Map<Integer, List<WfmUiEvent>> eventsListenersMap = getEventsListenersMap();
        if (eventsListenersMap.get(eventType) == null) {
            eventsSubscribersList = new ArrayList<>();
            eventsListenersMap.put(eventType, eventsSubscribersList);
        } else {
            eventsSubscribersList = eventsListenersMap.get(eventType);
        }
        eventsSubscribersList.add(wfmUiEvent);

        if (widget != null && widget instanceof View) {
            View view = (View) widget;
            view.addWfmUiEvent(wfmUiEvent);
        }
    }

    //In Progress
    static Map<Integer, Boolean> mapActivityByEventType = new HashMap<>();


    public static void fireWfmUiEvent(int eventType, Object args, Widget sender) {
        List<WfmUiEvent> eventsSubscribersList = getInstance().getEventsListenersMap().get(eventType);
        mapActivityByEventType.put(eventType, true);
        if (eventsSubscribersList != null) {
            for (WfmUiEvent listener : eventsSubscribersList) {
                if (listener != null) {
                    listener.onWfmUiEvent(sender, args);
                }
            }
        }
        mapActivityByEventType.remove(eventType);
    }

    public static void unSubscribeWfmUiEvent(int eventType, WfmUiEvent event) {
        ArrayList eventsSubscribersList = (ArrayList) getInstance().getEventsListenersMap().get(eventType);
        if (eventsSubscribersList != null) {
            eventsSubscribersList.remove(event);
        }
    }

    public static void
    addWfmUiListener(int eventType, Widget widget, WfmUiEvent event) throws IllegalArgumentException {
        if (widget == null) {
            throw new IllegalArgumentException("Widget that subscribing to event notification  must not be null. Please refer to Abdulaziz or Anvarbek for more information");
        }
        getInstance().addUiEventListener(eventType, event, widget);
    }

    public static void addWfmUiListener(int eventType, WfmUiEvent event) throws IllegalArgumentException {
        getInstance().addUiEventListener(eventType, event, null);
    }

    public static void addWfmUiListenerWithoutWidget(int eventType, WfmUiEvent event) throws IllegalArgumentException {
        getInstance().addUiEventListener(eventType, event, null);
    }

    public static void addWfmUiListener(Widget widget, WfmUiEvent event, int... eventTypes) throws IllegalArgumentException {
        if (widget == null) {
            throw new IllegalArgumentException("Widget that subscribing to event notification  must not be null. Please refer to Abdulaziz or Anvarbek for more information");
        }
        if (eventTypes != null && eventTypes.length > 0) {
            List<Integer> s = new ArrayList<>();
            for (int eventType : eventTypes) {
                if (!s.contains(eventType)) {
                    s.add(eventType);
                    getInstance().addUiEventListener(eventType, event, widget);
                }
            }
        }
    }

    public static void unSubscribeWfmUiEvents(List<WfmUiEvent> uiEvents) {

        if (uiEvents == null || uiEvents.isEmpty()) {
            return;
        }
        Map<Integer, List<WfmUiEvent>> listMap = getInstance().getEventsListenersMap();
        for (List<WfmUiEvent> events : listMap.values()) {
            events.removeIf(uiEvents::contains);
        }
        uiEvents.clear();
    }
}
