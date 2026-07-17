package com.edatasite.workforce.gwt.core.server.app;

import com.edatasite.workforce.gwt.core.client.rpc.EventServiceRemoteService;
import de.novanic.eventservice.client.config.EventServiceConfigurationTransferable;
import de.novanic.eventservice.client.config.RemoteEventServiceConfigurationTransferable;
import de.novanic.eventservice.client.event.DomainEvent;
import de.novanic.eventservice.client.event.Event;
import de.novanic.eventservice.client.event.domain.Domain;
import de.novanic.eventservice.client.event.filter.EventFilter;
import de.novanic.eventservice.client.event.listener.unlisten.UnlistenEvent;
import de.novanic.eventservice.client.event.listener.unlisten.UnlistenEventListener;
import de.novanic.eventservice.config.ConfigurationDependentFactory;
import de.novanic.eventservice.config.EventServiceConfiguration;
import de.novanic.eventservice.config.EventServiceConfigurationFactory;
import de.novanic.eventservice.config.level.ConfigLevelFactory;
import de.novanic.eventservice.config.loader.WebDescriptorConfigurationLoader;
import de.novanic.eventservice.logger.ServerLogger;
import de.novanic.eventservice.logger.ServerLoggerFactory;
import de.novanic.eventservice.service.connection.id.SessionConnectionIdGenerator;
import de.novanic.eventservice.service.connection.strategy.connector.ConnectionStrategyServerConnector;
import de.novanic.eventservice.service.registry.EventRegistry;
import de.novanic.eventservice.service.registry.EventRegistryFactory;
import org.gwtwidgets.server.spring.ServletUtils;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.servlet.ServletConfig;
import java.util.List;
import java.util.Set;

/**
 * Created by dilsh0d on 24.07.15.
 */
@Service("gwteventservice")
public class EventServiceRemoteServiceImpl implements EventServiceRemoteService {

    private static final ServerLogger LOG = ServerLoggerFactory.getServerLogger(EventServiceRemoteServiceImpl.class.getName());


    private EventRegistry myEventRegistry;
    private ConfigurationDependentFactory myConfigurationDependentFactory;


    @Override
    public EventServiceConfigurationTransferable initEventService() {
        final String theClientId = getClientId();
        final EventServiceConfiguration theConfiguration = myEventRegistry.getConfiguration();

        String theClientIdTransferable = null;
        if (SessionConnectionIdGenerator.class.getName().equals(theConfiguration.getConnectionIdGeneratorClassName())) {
            theClientIdTransferable = null;
        } else {
            theClientIdTransferable = theClientId;
        }
        return new RemoteEventServiceConfigurationTransferable(theConfiguration.getMinWaitingTime(), theConfiguration.getMaxWaitingTime(),
                theConfiguration.getTimeoutTime(), theConfiguration.getReconnectAttemptCount(), theClientIdTransferable, theConfiguration.getConnectionStrategyClientConnectorClassName());
    }

    /**
     * Register listen for a domain.
     *
     * @param aDomain domain to listen to
     */
    public void register(Domain aDomain) {
        register(aDomain, null);
    }

    /**
     * Register listen for a domain.
     *
     * @param aDomain       domain to listen to
     * @param anEventFilter EventFilter to filter events
     */
    public void register(Domain aDomain, EventFilter anEventFilter) {
        final String theClientId = getClientId();

        myEventRegistry.registerUser(aDomain, theClientId, anEventFilter);
    }

    /**
     * Register listen for a domain.
     *
     * @param aDomains domains to listen to
     */
    public void register(Set<Domain> aDomains) {
        for (Domain aDomain : aDomains) {
            register(aDomain);
        }
    }

    /**
     * Register listen for domains.
     *
     * @param aDomains      domains to listen to
     * @param anEventFilter EventFilter to filter events (applied to all domains)
     */
    public void register(Set<Domain> aDomains, EventFilter anEventFilter) {
        for (Domain aDomain : aDomains) {
            register(aDomain, anEventFilter);
        }
    }

    /**
     * Registers an {@link de.novanic.eventservice.client.event.listener.unlisten.UnlistenEvent} which is triggered on a
     * timeout or when a user/client leaves a {@link de.novanic.eventservice.client.event.domain.Domain}. An
     * {@link de.novanic.eventservice.client.event.listener.unlisten.UnlistenEvent} is hold at the server side and can
     * contain custom data. Other users/clients can use the custom data when the event is for example triggered by a timeout.
     *
     * @param anUnlistenScope scope of the unlisten events to receive
     * @param anUnlistenEvent {@link de.novanic.eventservice.client.event.listener.unlisten.UnlistenEvent} which should
     *                        be transferred to other users/clients when a timeout occurs or a domain is leaved.
     */
    public void registerUnlistenEvent(UnlistenEventListener.Scope anUnlistenScope, UnlistenEvent anUnlistenEvent) {
        final String theClientId = getClientId();
        myEventRegistry.registerUnlistenEvent(theClientId, anUnlistenScope, anUnlistenEvent);
    }

    /**
     * Registers an {@link EventFilter} for the domain.
     *
     * @param aDomain       domain to register the EventFilter to
     * @param anEventFilter EventFilter to filter events for the domain
     */
    public void registerEventFilter(Domain aDomain, EventFilter anEventFilter) {
        final String theClientId = getClientId();
        myEventRegistry.setEventFilter(aDomain, theClientId, anEventFilter);
    }

    /**
     * Deregisters the {@link EventFilter} of the domain.
     *
     * @param aDomain domain to drop the EventFilters from
     */
    public void deregisterEventFilter(Domain aDomain) {
        final String theClientId = getClientId();
        myEventRegistry.removeEventFilter(aDomain, theClientId);
    }

    /**
     * Returns the EventFilter for the user domain combination.
     *
     * @param aDomain domain
     * @return EventFilter for the domain
     */
    public EventFilter getEventFilter(Domain aDomain) {
        final String theClientId = getClientId();
        return myEventRegistry.getEventFilter(aDomain, theClientId);
    }

    /**
     * The listen method returns all events for the user (events for all domains where the user is registered and user
     * specific events). If no events are available, the method waits a defined time before the events are returned.
     * The client side calls the method with a defined interval to receive all events. If the client don't call the
     * method in the interval, the user will be removed from the EventRegistry. The timeout time and the waiting time
     * can be configured with {@link de.novanic.eventservice.config.EventServiceConfiguration}.
     *
     * @return list of events
     */
    public List<DomainEvent> listen() {
        final String theClientId = getClientId();
        ConnectionStrategyServerConnector theConnectionStrategyServerConnector = this.myConfigurationDependentFactory.getConnectionStrategyServerConnector();
        return myEventRegistry.listen(theConnectionStrategyServerConnector, theClientId);
    }

    /**
     * Unlisten for events (for the current user) in all domains (deregisters the user from all domains).
     */
    public void unlisten() {
        final String theClientId = getClientId();
        myEventRegistry.unlisten(theClientId);
    }

    /**
     * Unlisten for events (for the current user) in the domain and deregisters the user from the domain.
     *
     * @param aDomain domain to unlisten
     */
    public void unlisten(Domain aDomain) {
        final String theClientId = getClientId();
        myEventRegistry.unlisten(aDomain, theClientId);
    }

    /**
     * Unlisten for events (for the current user) in the domains and deregisters the user from the domains.
     *
     * @param aDomains set of domains to unlisten
     */
    public void unlisten(Set<Domain> aDomains) {
        for (Domain theDomain : aDomains) {
            unlisten(theDomain);
        }
    }

    /**
     * Checks if the user is registered for event listening.
     *
     * @param aDomain domain to check
     * @return true when the user is registered for listening, otherwise false
     */
    public boolean isUserRegistered(Domain aDomain) {
        final String theClientId = getClientId();
        return myEventRegistry.isUserRegistered(aDomain, theClientId);
    }

    /**
     * Adds an event for all users in the domain.
     *
     * @param aDomain domain to add the event
     * @param anEvent event to add
     */
    public void addEvent(Domain aDomain, Event anEvent) {
        myEventRegistry.addEvent(aDomain, anEvent);
    }

    /**
     * Adds an event only for the current user.
     *
     * @param anEvent event to add to the user
     */
    public void addEventUserSpecific(Event anEvent) {
        final String theClientId = getClientId();
        myEventRegistry.addEventUserSpecific(theClientId, anEvent);
    }

    /**
     * Returns the domain names, where the user is listening to
     *
     * @return collection of domain names
     */
    public Set<Domain> getActiveListenDomains() {
        final String theClientId = getClientId();
        return myEventRegistry.getListenDomains(theClientId);
    }

    /**
     * Registers the {@link de.novanic.eventservice.config.loader.WebDescriptorConfigurationLoader},
     * loads the first available configuration (with {@link de.novanic.eventservice.config.EventServiceConfigurationFactory})
     * and initializes the {@link de.novanic.eventservice.service.registry.EventRegistry}.
     *
     * @param aConfig servlet configuration
     * @return initialized {@link de.novanic.eventservice.service.registry.EventRegistry}
     */
    private EventRegistry initEventRegistry(ServletConfig aConfig) {
        final WebDescriptorConfigurationLoader theWebDescriptorConfigurationLoader = new WebDescriptorConfigurationLoader(aConfig);
        final EventServiceConfigurationFactory theEventServiceConfigurationFactory = EventServiceConfigurationFactory.getInstance();
        theEventServiceConfigurationFactory.addConfigurationLoader(ConfigLevelFactory.DEFAULT, theWebDescriptorConfigurationLoader);

        final EventRegistryFactory theEventRegistryFactory = EventRegistryFactory.getInstance();
        EventRegistry theEventRegistry = theEventRegistryFactory.getEventRegistry();

        if (theWebDescriptorConfigurationLoader.isAvailable()) {
            theEventServiceConfigurationFactory.loadEventServiceConfiguration();
        }
        return theEventRegistry;
    }

    private EventRegistry initEventRegistry() {
        //final WebDescriptorConfigurationLoader theWebDescriptorConfigurationLoader = new WebDescriptorConfigurationLoader(aConfig);
        final EventServiceConfigurationFactory theEventServiceConfigurationFactory = EventServiceConfigurationFactory.getInstance();
        //theEventServiceConfigurationFactory.addConfigurationLoader(ConfigLevelFactory.DEFAULT, theWebDescriptorConfigurationLoader);

        final EventRegistryFactory theEventRegistryFactory = EventRegistryFactory.getInstance();

        //if (theWebDescriptorConfigurationLoader.isAvailable()) {
        //  theEventServiceConfigurationFactory.loadEventServiceConfiguration();
        //}
        return theEventRegistryFactory.getEventRegistry();
    }

    /**
     * Returns the client id.
     *
     * @return client id
     */
    protected String getClientId() {
        return ServletUtils.getRequest().getSession().getId();
    }

    @PostConstruct
    public void initIt() throws Exception {
        myEventRegistry = initEventRegistry();
        EventServiceConfiguration theConfiguration = this.myEventRegistry.getConfiguration();
        this.myConfigurationDependentFactory = ConfigurationDependentFactory.getInstance(theConfiguration);
    }

    @PreDestroy
    public void cleanUp() throws Exception {
        EventRegistryFactory.getInstance().resetEventRegistry();
    }
}
