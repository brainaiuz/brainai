package com.edatasite.workforce.gwt.core.server.eventdispatcher;

import com.edatasite.workforce.core.domain.businessevent.EdsBusinessEvent;

/**
 * User: Abdulaziz
 * Date: Jan 5, 2010
 * Time: 11:15:25 AM
 */
public interface BusinessEventDisposer {
    void disposeEvent(EdsBusinessEvent event);

    void disposeEventNative(EdsBusinessEvent event);

    void disposeEventNative(Integer eventID);
}
