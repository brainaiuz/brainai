package com.edatasite.workforce.gwt.core.server.db.fifo;

import com.edatasite.workforce.core.domain.fifo.EdsFifoFailure;
import com.edatasite.workforce.gwt.core.server.db.Manager;
import com.edatasite.workforce.gwt.core.server.rabbitmq.data.FIFODataMQ;
import com.edatasite.workforce.gwt.core.server.rabbitmq.enums.EntityType;

import java.util.List;

public interface FifoFailureManager extends Manager<EdsFifoFailure> {

    EdsFifoFailure getByEntityId(Integer entityId, EntityType entityType, String companyId);

    EdsFifoFailure getAnyByEntityId(Integer entityId, EntityType entityType, String companyId);

    List<FIFODataMQ> getPendingFailures();

    void deleteFifoFailure(Integer id);

    void updateFifoFailure(Integer id);

}
