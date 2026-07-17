package com.edatasite.workforce.gwt.core.server.eventdispatcher;

import com.edatasite.workforce.core.domain.businessevent.EdsBusinessEvent;

/**
 * User: Abdulaziz
 * Date: Jul 29, 2010
 * Time: 10:00:23 PM
 */
public abstract class CustomBusinessEventListenerAdapter implements CustomBusinessEventListener{
    @Override
    public void onAddEvent(EdsBusinessEvent event) {

    }

    @Override
    public void onEditEvent(EdsBusinessEvent event) {

    }

    @Override
    public void onDeleteEvent(EdsBusinessEvent event) {
        
    }
}
