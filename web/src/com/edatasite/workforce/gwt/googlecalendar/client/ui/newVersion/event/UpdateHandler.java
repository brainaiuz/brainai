package com.edatasite.workforce.gwt.googlecalendar.client.ui.newVersion.event;

import com.google.gwt.event.shared.EventHandler;

/**
 * Handler interface for {@link DeleteEvent} events.
 *
 * @param <T> the type being opened
 */
public interface UpdateHandler<T> extends EventHandler {

    /**
     * Called when {@link DeleteEvent} is fired.
     *
     * @param event the {@link DeleteEvent} that was fired
     */
    void onUpdate(UpdateEvent<T> event);
}