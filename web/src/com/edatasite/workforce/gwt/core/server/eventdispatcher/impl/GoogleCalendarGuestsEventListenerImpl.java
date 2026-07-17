package com.edatasite.workforce.gwt.core.server.eventdispatcher.impl;

import com.edatasite.workforce.core.domain.businessevent.EdsBusinessEvent;
import com.edatasite.workforce.core.domain.crm.EdsEvent;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.BusinessEventListener;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by IntelliJ IDEA.
 * User: Ilxom Lutfullaev
 * Date: Feb 24, 2011
 * Time: 10:47:58 PM
 * To change this template use File | Settings | File Templates.
 */

@Transactional
public class GoogleCalendarGuestsEventListenerImpl implements BusinessEventListener {

    public static WfmType<EdsEvent> TYPE = new WfmType<>(EventTypes.googleCalendarGuestsEventListener);

    @Override
    public void onCustomEvent(EdsBusinessEvent event) {

    }

    @Override
    public void onAddEvent(EdsBusinessEvent event) {
    }

    @Override
    public void onDeleteEvent(EdsBusinessEvent event) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void onEditEvent(EdsBusinessEvent event) {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
