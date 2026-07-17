package com.edatasite.workforce.gwt.core.server.db.fifo;

import com.edatasite.workforce.core.domain.fifo.EdsProcessedEvent;
import com.edatasite.workforce.gwt.core.server.db.Manager;

public interface ProcessedEventManager extends Manager<EdsProcessedEvent> {

    EdsProcessedEvent findByEventID(String eventId);
}
