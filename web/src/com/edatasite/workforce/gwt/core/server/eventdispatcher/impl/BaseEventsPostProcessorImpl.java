package com.edatasite.workforce.gwt.core.server.eventdispatcher.impl;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.workforce.core.config.datasource.TenantContextHolder;
import com.edatasite.workforce.core.domain.businessevent.EdsBusinessEvent;
import com.edatasite.workforce.core.domain.crm.EdsEvent;
import com.edatasite.workforce.core.kafka.data.EventData;
import com.edatasite.workforce.core.kafka.producer.EventPostProcessorProducer;
import com.edatasite.workforce.core.service.SessionContextService;
import com.edatasite.workforce.gwt.core.server.db.eventdispatcher.BusinessEventDispatcherManager;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.BaseEventsPostProcessor;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.BusinessEventDisposer;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.BusinessEventListener;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.CustomBusinessEventListener;
import com.google.gwt.user.server.rpc.security.ServerSecurityContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;

/**
 * User: Anvarbek
 * Date: Dec 24, 2009
 * Time: 4:29:33 PM
 */
public class BaseEventsPostProcessorImpl implements BaseEventsPostProcessor {
    public static final String EVENT_TYPE_ADD = "ADD";
    public static final String EVENT_TYPE_EDIT = "EDIT";
    public static final String EVENT_TYPE_DELETE = "DELETE";
    public static final String EVENT_TYPE_CUSTOM = "CUSTOM";
    private static final Logger log = LoggerFactory.getLogger(BaseEventsPostProcessorImpl.class);

    private static final ThreadLocal<HashMap<String, ArrayList<Integer>>> map = new ThreadLocal<>();
    private static final ThreadLocal<String> currentDatabase = new ThreadLocal<>();
    private static final ThreadLocal<String> currentSchema = new ThreadLocal<>();
    private static final ThreadLocal<String> currentSessionId = new ThreadLocal<>();

    private Map<String, BusinessEventListener> mapping;
    private Map<String, CustomBusinessEventListener> custommapping;
    private BusinessEventDispatcherManager businessEventDispatcherManager;
    private EventPostProcessorProducer eventPostProcessorProducer;
    @Autowired
    private SessionContextService sessionContextService;
    @Autowired
    private ExecutorService executor;


    public void setMapping(Map<String, BusinessEventListener> mapping) {
        this.mapping = mapping;
    }

    public void setCustommapping(Map<String, CustomBusinessEventListener> custommapping) {
        this.custommapping = custommapping;
    }

    public void setBusinessEventDisposer(BusinessEventDisposer businessEventDisposer) {
    }

    public void setBusinessEventDispatcherManager(BusinessEventDispatcherManager businessEventDispatcherManager) {
        this.businessEventDispatcherManager = businessEventDispatcherManager;
    }

    public void setEventPostProcessorProducer(EventPostProcessorProducer eventPostProcessorProducer) {
        this.eventPostProcessorProducer = eventPostProcessorProducer;
    }

    public <E extends EdsObject, P extends Inducer> EdsBusinessEvent registerEvent(WfmType<E> type, String eventType, E entity, P sourceID) {
        fillOutLocalThreadConfig();

        boolean sendNotification = true;
        if (entity instanceof EdsEvent event) {
            sendNotification = event.isSendEmailNotification();
        }
        EdsBusinessEvent event = businessEventDispatcherManager.registerEvent(
                sourceID,
                entity != null ? entity.getObjectID() : null,
                entity != null ? entity.getDomainType() : null,
                eventType,
                type.getStringValue(),
                sendNotification
        );
        addToMap(type.getStringValue(), event.getObjectID());
        return event;
    }

    public <E extends EdsObject, P extends Inducer, F extends EdsObject> EdsBusinessEvent registerEvent(WfmType<E> type, String eventType, E entity, P sourceID, F additionalSourceID) {
        fillOutLocalThreadConfig();

        EdsBusinessEvent event = businessEventDispatcherManager.registerEvent(
                sourceID,
                additionalSourceID,
                entity != null ? entity.getObjectID() : null,
                entity != null ? entity.getDomainType() : null,
                eventType,
                type.getStringValue(),
                true
        );
        addToMap(type.getStringValue(), event.getObjectID());
        return event;
    }

    @Override
    public <E extends EdsObject> EdsBusinessEvent registerCustomEvent(WfmType<E> type, String eventType, E entity, Integer sourceID, Integer additionalSourceID) {
        fillOutLocalThreadConfig();

        EdsBusinessEvent event = businessEventDispatcherManager.registerCustomEvent(
                sourceID,
                additionalSourceID,
                entity != null ? entity.getObjectID() : null,
                entity != null ? entity.getDomainType() : null,
                eventType,
                type.getStringValue(),
                true
        );
        addToMap(type.getStringValue(), event.getObjectID());
        return event;
    }

    public <E extends EdsObject, P extends Inducer> EdsBusinessEvent registerEvent(WfmType<E> type, String eventType, E entity, P sourceID, boolean avoidDuplicates) {
        fillOutLocalThreadConfig(); // ← har doim chaqiriladi

        if (avoidDuplicates) {
            EdsBusinessEvent existing = businessEventDispatcherManager.getUnprocessedEvent(
                    sourceID,
                    entity != null ? entity.getObjectID() : null,
                    eventType,
                    type.getStringValue()
            );
            if (existing != null) {
                return null; // duplicate — register qilmaymiz
            }
        }

        EdsBusinessEvent event = businessEventDispatcherManager.registerEvent(
                sourceID,
                entity != null ? entity.getObjectID() : null,
                entity != null ? entity.getDomainType() : null,
                eventType,
                type.getStringValue(),
                true
        );
        addToMap(type.getStringValue(), event.getObjectID());
        return event;
    }

    public void handleActivity() {
        HashMap<String, ArrayList<Integer>> currentMap = map.get();
        final String currentDatabaseName = currentDatabase.get();
        final String currentSchemaName = currentSchema.get();
        final String currentSessionID = currentSessionId.get();

        map.remove();
        currentDatabase.remove();
        currentSchema.remove();
        currentSessionId.remove();

        if (CollectionUtils.isEmpty(currentMap)) {
            return;
        }

        try {
            currentMap.forEach((type, eventList) -> eventList.forEach(event -> {
                if (!EventTypes.importEmployeeAttandenceCustomEventListener.equals(type)) {
                    executor.execute(() -> {
                        try {
                            TenantContextHolder.setCustomerType(currentDatabaseName);
                            ServerSecurityContext.getInstance().setSessionId(currentSessionID);
                            ServerSecurityContext.getInstance().setDatabase(currentDatabaseName);
                            ServerSecurityContext.getInstance().setCompanyId(currentSchemaName);
                            sessionContextService.getInNewTransaction(() -> {
                                dispatchEvent(event);
                                return null;
                            });
                        } catch (Exception e) {
                            log.error("Event dispatch failed. type={}, eventId={}", type, event, e);
                        } finally {
                            TenantContextHolder.clearCustomerType();
                            ServerSecurityContext.setServerSecurityContext(null);
                        }
                    });
                } else {
                    eventPostProcessorProducer.sendMessage(new EventData(type, event), currentSessionID, currentSchemaName, currentDatabaseName);
                }
            }));
        } catch (Exception e) {
            log.error("handleActivity failed", e);
        }
    }

    public void dispatchEvent(Integer eventId) {
        EdsBusinessEvent event = businessEventDispatcherManager.getEvent(eventId);
        dispatchEvent(event,"BaseEventsPostProcessorImpl");
    }

    public void dispatchEvent(EdsBusinessEvent event, String from) {
        if (event == null) {
            log.error("|||||||||||  Event object is null in dispatchEvent  |||||||||||");
            return;
        }
        String processorName = event.getProcessorName();
        if (processorName == null) {
            log.error("Event {} has null processorName", event.getObjectID());
            return;
        }
        log.info("+++++++++++++++++++++++++++++++++++++++++ Listen Event ProcessorName: {} Event ID: {} Event Type: {} Company ID : {} FROM : {}",
                processorName, event.getObjectID(), event.getEventType(), ServerSecurityContext.getInstance().getCompanyId(), from);

        BusinessEventListener listener = mapping.get(processorName);
        if (listener != null) {
            switch (event.getEventType()) {
                case EVENT_TYPE_ADD -> listener.onAddEvent(event);
                case EVENT_TYPE_EDIT -> listener.onEditEvent(event);
                case EVENT_TYPE_DELETE -> listener.onDeleteEvent(event);
                default -> listener.onCustomEvent(event);
            }
        } else {
            CustomBusinessEventListener customListener = custommapping.get(processorName);
            if (customListener != null) {
                customListener.onCustomEvent(event);
            } else {
                log.info("+++++++++++++++++++++++++++++++++++++++++ Listener not found :( ");
            }
        }
    }

    private void addToMap(String typeValue, Integer eventId) {
        if (map.get().get(typeValue) != null) {
            map.get().get(typeValue).add(eventId);
        } else {
            map.get().put(typeValue, new ArrayList<>(Collections.singletonList(eventId)));
        }
    }

    private void fillOutLocalThreadConfig() {
        if (currentDatabase.get() == null) {
            currentDatabase.set(ServerSecurityContext.getInstance().getDatabase());
        }
        if (currentSchema.get() == null) {
            currentSchema.set(ServerSecurityContext.getInstance().getCompanyId());
        }
        if (currentSessionId.get() == null) {
            currentSessionId.set(ServerSecurityContext.getInstance().getSessionId());
        }
        if (map.get() == null) {
            map.set(new HashMap<>());
        }
    }
}
