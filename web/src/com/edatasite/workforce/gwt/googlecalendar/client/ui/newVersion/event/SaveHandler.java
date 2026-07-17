package com.edatasite.workforce.gwt.googlecalendar.client.ui.newVersion.event;

import com.google.gwt.event.shared.EventHandler;

/**
 * Created by IntelliJ IDEA.
 * User: Ruslan Muhammadov
 * Date: Feb 6, 2010
 * Time: 2:01:57 PM
 * To change this template use File | Settings | File Templates.
 */
public interface SaveHandler<T> extends EventHandler {

    void onSave(SaveEvent<T> event);
}
