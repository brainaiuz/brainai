package com.edatasite.workforce.gwt.googlecalendar.client.ui.newVersion.event;

import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.event.shared.HasHandlers;

/**
 * Created by IntelliJ IDEA.
 * User: Ruslan Muhammadov
 * Date: Feb 6, 2010
 * Time: 2:16:12 PM
 * To change this template use File | Settings | File Templates.
 */
public interface HasSaveHandlers<T> extends HasHandlers {

    HandlerRegistration addSaveHandler(SaveHandler<T> handler);
}
