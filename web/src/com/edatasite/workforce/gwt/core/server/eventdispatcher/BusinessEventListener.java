package com.edatasite.workforce.gwt.core.server.eventdispatcher;

import com.edatasite.workforce.core.domain.businessevent.EdsBusinessEvent;

/**
 * User: Anvarbek
 * Date: Dec 27, 2009
 * Time: 5:10:11 PM
 */
public interface BusinessEventListener {

    void onAddEvent(EdsBusinessEvent event);

    void onDeleteEvent(EdsBusinessEvent event);

    void onEditEvent(EdsBusinessEvent event);

    void onCustomEvent(EdsBusinessEvent event);

}
