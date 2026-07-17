package com.edatasite.workforce.gwt.profile.client.ui.view.pm;

import com.allen_sauer.gwt.dnd.client.DragEndEvent;
import com.allen_sauer.gwt.dnd.client.DragHandler;
import com.allen_sauer.gwt.dnd.client.DragStartEvent;
import com.allen_sauer.gwt.dnd.client.VetoDragException;
import com.google.gwt.user.client.ui.HTML;

/**
 * Created by IntelliJ IDEA.
 * User: JavaZone
 * Date: 5/15/12
 * Time: 2:38 PM
 * To change this template use File | Settings | File Templates.
 */

public class DemoDragHandler implements DragHandler {
    private final HTML eventTextArea;

    DemoDragHandler(HTML dragHandlerHTML) {
        eventTextArea = dragHandlerHTML;
    }

    public void onDragEnd(DragEndEvent event) {
    }

    public void onDragStart(DragStartEvent event) {
    }

    public void onPreviewDragEnd(DragEndEvent event) throws VetoDragException {
    }

    public void onPreviewDragStart(DragStartEvent event) throws VetoDragException {
    }

    private void clear() {
        eventTextArea.setHTML("");
    }

    private void log(String text, String color) {
        eventTextArea.setHTML(eventTextArea.getHTML() + (eventTextArea.getHTML().length() == 0 ? "" : "<br>") + "<span style='color: " + color + "'>" + text + "</span>");
    }
}
