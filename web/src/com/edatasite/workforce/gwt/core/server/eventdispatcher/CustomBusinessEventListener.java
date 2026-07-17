package com.edatasite.workforce.gwt.core.server.eventdispatcher;

import com.edatasite.workforce.core.domain.businessevent.EdsBusinessEvent;

/**
 * User: Abdulaziz
 * Date: Aug 5, 2010
 * Time: 5:12:47 PM
 */
public interface CustomBusinessEventListener extends BusinessEventListener {
    void onCustomEvent(EdsBusinessEvent event);
}
