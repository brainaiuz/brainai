package com.edatasite.workforce.gwt.core.server.eventdispatcher;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.workforce.core.domain.businessevent.EdsBusinessEvent;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.impl.Inducer;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.impl.WfmType;

/**
 * User: Anvarbek
 * Date: Dec 26, 2009
 * Time: 7:29:04 PM
 */
public interface BaseEventsPostProcessor {
    <E extends EdsObject, P extends Inducer> EdsBusinessEvent registerEvent(WfmType<E> listenerName, String eventType, E entityID, P sourceID);

//    <E extends EdsObject, P extends Inducer> EdsBusinessEvent registerEvent(WfmType<E> listenerName, String eventType, E entityID, P sourceID, P additionalSourceID);

    <E extends EdsObject, P extends Inducer, F extends EdsObject> EdsBusinessEvent registerEvent(WfmType<E> listenerName, String eventType, E entityID, P sourceID, F additionalSourceID);

    <E extends EdsObject> EdsBusinessEvent registerCustomEvent(WfmType<E> listenerName, String eventType, E entity, Integer sourceID, Integer additionalSourceID);

    <E extends EdsObject, P extends Inducer> EdsBusinessEvent registerEvent(WfmType<E> listenerName, String eventType, E entityID, P sourceID, boolean avoidDuplicates);

    void handleActivity();

    void dispatchEvent(Integer eventId);

    void dispatchEvent(EdsBusinessEvent event, String from);
}
