package com.edatasite.workforce.gwt.core.server.db.impl.eventdispatcher;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.workforce.core.domain.businessevent.EdsBusinessEvent;
import com.edatasite.workforce.gwt.core.client.CommandConstants;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.db.UserSessionManager;
import com.edatasite.workforce.gwt.core.server.db.eventdispatcher.BusinessEventDispatcherManager;
import com.edatasite.workforce.gwt.core.server.db.impl.BaseManager;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.impl.Inducer;
import com.edatasite.workforce.gwt.core.server.rabbitmq.enums.EventStatus;
import com.google.gwt.user.server.rpc.security.ServerSecurityContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * User: Abdulaziz
 * Date: Dec 14, 2009
 * Time: 5:36:08 PM
 */
@Repository("businessEventDispatcherManager")
public class BusinessEventDispatcherManagerImpl extends BaseManager<EdsBusinessEvent> implements BusinessEventDispatcherManager, CommandConstants {
    public BusinessEventDispatcherManagerImpl() {
        super(EdsBusinessEvent.class);
    }

    @Autowired
    private UserSessionManager userSessionManager;

    public <P extends Inducer> EdsBusinessEvent registerEvent(P sourceID, Integer entityID, String entityType, String eventType, String processorName, Boolean sendNotification) {
        return registerEvent(sourceID, null, entityID, entityType, eventType, processorName, sendNotification);
    }

    public <P extends Inducer, F extends EdsObject> EdsBusinessEvent registerEvent(P sourceID, F additionaleSourceID, Integer entityID, String entityType, String eventType, String processorName, Boolean sendNotification) {
        EdsBusinessEvent event = new EdsBusinessEvent();
        event.setSourceID(sourceID != null ? sourceID.getObjectID() : 0);
        event.setAdditionalSourceID(additionaleSourceID != null ? additionaleSourceID.getObjectID() : 0);
        event.setEntityID(entityID);
        event.setEntityType(entityType);
        event.setEventType(eventType);
        event.setProcessorName(processorName);
        //event.setProcessed(false);
        event.setTime(new Date());
        event.setSendEmailNotification(sendNotification);
        event.setCompanyId(Integer.parseInt(ServerSecurityContext.getInstance().getCompanyId()));
        try {
            create(event);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return event;
    }

    public EdsBusinessEvent registerCustomEvent(Integer sourceID, Integer additionaleSourceID, Integer entityID, String entityType, String eventType, String processorName, Boolean sendNotification) {
        EdsBusinessEvent event = new EdsBusinessEvent();
        event.setSourceID(sourceID);
        event.setAdditionalSourceID(additionaleSourceID);
        event.setEntityID(entityID);
        event.setEntityType(entityType);
        event.setEventType(eventType);
        event.setProcessorName(processorName);
        //event.setProcessed(false);
        event.setTime(new Date());
        event.setSendEmailNotification(sendNotification);
        event.setCompanyId(Integer.parseInt(ServerSecurityContext.getInstance().getCompanyId()));
        try {
            create(event);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return event;
    }

    @Override
    public void create(EdsBusinessEvent obj) {
        if (obj == null) {
            return;
        }
        if (ServerSecurityContext.getInstance() != null && ServerSecurityContext.getInstance().getSessionId() != null) {
            obj.setSuperUser(userSessionManager.isSuperUser(ServerSecurityContext.getInstance().getSessionId()));
        }
        if (obj.getCreatedByID() == null) {
            obj.setCreatedByID(ServerSecurityContext.getInstance().getUser() != null ? ((EdsObject) ServerSecurityContext.getInstance().getUser()).getObjectID() : ServerSecurityContext.getInstance().getStaticUserID());
        }
        super.create(obj);
    }

    public List<EdsBusinessEvent> getUnprocessedEvents() {
        return findLimited("SELECT event FROM EdsBusinessEvent event WHERE (event.status is null or event.status not in ('FAILED', 'COMPLETED'))  and event.time <= ? ORDER BY event.sorder, event.objectID ", 50, new Date());
    }

    public <P extends Inducer> EdsBusinessEvent getUnprocessedEvent(P sourceID, Integer entityID, String eventType, String processorName) {
        return (EdsBusinessEvent) findSingle("SELECT event FROM EdsBusinessEvent event WHERE event.processed = false and (event.status is null or event.status <> '" + FAIL + "') and event.sourceID = " + sourceID.getObjectID() + " and event.entityID = " + entityID + " and event.eventType = '" + eventType + "' and event.processorName = '" + processorName + "' and event.companyId = " + ServerSecurityContext.getInstance().getCompanyId());
    }

    public List<EdsBusinessEvent> getUserSyntGoogleContactEvents(String eventType, Integer userID) {
        Map params = new HashMap();
        params.put("eventType", eventType);
        params.put("userID", userID);
        return findByNamedParams("SELECT events FROM EdsBusinessEvent events WHERE events.entityID=:userID AND events.eventType=:eventType", params);
    }

    public void removeEventNative(EdsBusinessEvent event) {
        if (!event.isProcessFailed() || event.isProcessed()) {
            try {
                updateNative("delete from " + getCompanyId() + ".businessevent where id = " + event.getObjectID());
            } catch (DataAccessException e) {
                e.printStackTrace();
            }
        } else {
            if (event.getAttempts() >= ATTEMPTS) {
                updateNative("update " + getCompanyId() + ".businessevent set status='" + EventStatus.FAILED + "' where id = " + event.getObjectID());
            } else {
                updateNative("update " + getCompanyId() + ".businessevent set attempts=" + (event.getAttempts() + 1) + ", sorder=999 where id = " + event.getObjectID());
            }
        }
    }

    public void removeEvent(EdsBusinessEvent event) {
        if (event.isProcessed()) {
            try {
                jpaTemplate.delete(event);
            } catch (DataAccessException e) {
                e.printStackTrace();
            }
        } else {
            if (event.getAttempts() > ATTEMPTS) {
                event.setStatus(FAIL);
                update(event);
            }
        }
    }

    public void updateEvent(EdsBusinessEvent event) {
        update(event);
    }

    public EdsBusinessEvent getEvent(Integer eventID) {
        return get(eventID);
    }

    @Override
    public List<EdsBusinessEvent> getEvents(List<Integer> Ids) {
        return find("SELECT e FROM EdsBusinessEvent e WHERE e.objectID in (" + ServerUtils.getAsCommoDelimited(Ids, "0", ",") + ") order by e.objectID");
    }
}
