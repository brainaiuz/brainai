package com.edatasite.workforce.gwt.core.server.rabbitmq.service;

import com.edatasite.workforce.gwt.core.server.rabbitmq.data.FIFODataMQ;

import java.util.List;

public interface FifoFailureService {

    List<FIFODataMQ> getPendingFailures();

    void deleteFailure(FIFODataMQ fifoItem);

    void updateFifoFailure(Integer failureId);

    void trackFailur(FIFODataMQ item, String key, String failMessage);

}
