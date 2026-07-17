package com.edatasite.workforce.gwt.core.server.app;

import de.novanic.eventservice.client.event.Event;
import de.novanic.eventservice.client.event.domain.Domain;
import de.novanic.eventservice.client.event.filter.EventFilter;
import de.novanic.eventservice.service.EventExecutorService;
import de.novanic.eventservice.service.EventExecutorServiceFactory;
import org.gwtwidgets.server.spring.ServletUtils;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by dilsh0d on 01.08.15.
 */
@Service("serversenteventservice")
public class ServerSentEventServiceImpl implements ServerSentEventService,EventExecutorService {

    /**
     * Checks if the user is registered for event listening.
     * @return true when the user is listening, otherwise false
     */
    public boolean isUserRegistered() {
        return getEventExecutorService().isUserRegistered();
    }

    /**
     * Checks if the user is registered for event listening.
     * @param aDomain domain to check the registration for the user
     * @return true when the user is listening, otherwise false
     */
    public boolean isUserRegistered(Domain aDomain) {
        return getEventExecutorService().isUserRegistered(aDomain);
    }

    /**
     * Adds an event for all users
     * @param aDomain the domain to add the event
     * @param anEvent event to add
     */
    public void addEvent(Domain aDomain, Event anEvent) {
        getEventExecutorService().addEvent(aDomain, anEvent);
    }

    /**
     * Adds an event for a specific user
     * @param anEvent event to add
     */
    public void addEventUserSpecific(Event anEvent) {
        getEventExecutorService().addEventUserSpecific(anEvent);
    }

    /**
     * Changes the {@link de.novanic.eventservice.client.event.filter.EventFilter} for the user-domain combination.
     * The {@link de.novanic.eventservice.client.event.filter.EventFilter} can be removed with the method
     * {@link de.novanic.eventservice.service.EventExecutorService#removeEventFilter(de.novanic.eventservice.client.event.domain.Domain)}
     * or when that method is called with NULL as the {@link de.novanic.eventservice.client.event.filter.EventFilter}
     * parameter value.
     * @param aDomain domain to set the {@link de.novanic.eventservice.client.event.filter.EventFilter}
     * @param anEventFilter new {@link de.novanic.eventservice.client.event.filter.EventFilter}
     */
    public void setEventFilter(Domain aDomain, EventFilter anEventFilter) {
        getEventExecutorService().setEventFilter(aDomain, anEventFilter);
    }

    /**
     * Returns the EventFilter for the user domain combination.
     * @param aDomain domain
     * @return EventFilter for the domain
     */
    public EventFilter getEventFilter(Domain aDomain) {
        return getEventExecutorService().getEventFilter(aDomain);
    }

    /**
     * Removes the {@link de.novanic.eventservice.client.event.filter.EventFilter} of the domain.
     * @param aDomain domain to drop the {@link de.novanic.eventservice.client.event.filter.EventFilter} from
     */
    public void removeEventFilter(Domain aDomain) {
        getEventExecutorService().removeEventFilter(aDomain);
    }

    /**
     * Creates an instance of {@link de.novanic.eventservice.service.EventExecutorService}.
     * @return a new instance of {@link de.novanic.eventservice.service.EventExecutorService}
     */
    private EventExecutorService getEventExecutorService() {
        final EventExecutorServiceFactory theEventExecutorServiceFactory = EventExecutorServiceFactory.getInstance();
        return theEventExecutorServiceFactory.getEventExecutorService(getRequest());
    }

    /**
     * Returns the current request.
     *
     * @return current request
     */
    protected HttpServletRequest getRequest() {
        return ServletUtils.getRequest();
    }
}