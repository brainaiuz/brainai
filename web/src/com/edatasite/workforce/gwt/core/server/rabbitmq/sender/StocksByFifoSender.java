package com.edatasite.workforce.gwt.core.server.rabbitmq.sender;

import com.edatasite.workforce.gwt.core.server.rabbitmq.data.FIFODataMQ;
import com.edatasite.workforce.gwt.core.server.rabbitmq.enums.InOutQueue;
import com.edatasite.workforce.gwt.core.server.rabbitmq.enums.QueueListener;
import org.springframework.stereotype.Component;

@Component
public class StocksByFifoSender extends BaseAmqpSender<FIFODataMQ> {

    public void sendToQueue(FIFODataMQ data, Integer companyId, String clusterType, InOutQueue queue, QueueListener listener) {
        sendToDynamic(data, companyId, clusterType, queue.getQueue(), listener.getListener());
    }

    @Override
    public void sendMessage(FIFODataMQ data, Integer companyId, String clusterType) {
    }
}
