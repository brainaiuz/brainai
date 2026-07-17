package com.edatasite.workforce.gwt.core.server.db.impl.fifo;

import com.edatasite.workforce.core.domain.fifo.EdsProcessedEvent;
import com.edatasite.workforce.gwt.core.server.db.fifo.ProcessedEventManager;
import com.edatasite.workforce.gwt.core.server.db.impl.BaseManager;
import org.springframework.stereotype.Repository;

@Repository("processedEventManager")
public class ProcessedEventManagerImpl extends BaseManager<EdsProcessedEvent> implements ProcessedEventManager {

    public ProcessedEventManagerImpl() {
        super(EdsProcessedEvent.class);
    }

    @Override
    public EdsProcessedEvent findByEventID(String eventId) {
        return (EdsProcessedEvent) findSingle("SELECT e FROM EdsProcessedEvent e WHERE e.eventId = ?", eventId);
    }
}
