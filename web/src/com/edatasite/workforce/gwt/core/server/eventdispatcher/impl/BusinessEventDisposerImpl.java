package com.edatasite.workforce.gwt.core.server.eventdispatcher.impl;

import com.edatasite.workforce.core.domain.businessevent.EdsBusinessEvent;
import com.edatasite.workforce.gwt.core.server.db.eventdispatcher.BusinessEventDispatcherManager;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.BusinessEventDisposer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

/**
 * User: Abdulaziz
 * Date: Jan 5, 2010
 * Time: 11:13:49 AM
 */
@Transactional
public class BusinessEventDisposerImpl implements BusinessEventDisposer {
    public static String TYPE = "businessEventDisposerString";
    @Autowired
    private BusinessEventDispatcherManager businessEventDispatcherManager;

    /**
     * Deletes processed events, calls when transaction session exists
     *
     * @param event
     */
    public void disposeEvent(EdsBusinessEvent event) {
        businessEventDispatcherManager.removeEvent(event);
    }

    /**
     * Deletes processed events, calls when transaction session does not exists by using native query
     *
     * @param eventID
     */
    public void disposeEventNative(EdsBusinessEvent event) {
        businessEventDispatcherManager.removeEventNative(event);
    }

    /**
     * Disposes event by given id
     * @param eventID
     */
    public void disposeEventNative(Integer eventID) {
       EdsBusinessEvent event = businessEventDispatcherManager.getEvent(eventID);
       disposeEventNative(event);
    }
}
