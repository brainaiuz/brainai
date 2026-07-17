package com.edatasite.workforce.gwt.core.server.rabbitmq.sender;

import com.edatasite.workforce.gwt.core.server.rabbitmq.data.FifoItem;
import org.springframework.stereotype.Component;

@Component
public class HandleStocksByFifoSender extends BaseAmqpSender<FifoItem> {

    private final String OUT_ITEM_KEY = "out_item_fifo_key";
    private final String OUT_ITEM_DELETE_KEY = "out_item_delete_fifo_key";
    private final String IN_ITEM_KEY = "in_item_by_fifo_key";
    private final String IN_ITEM_DELETE_KEY = "in_item_delete_fifo_key";

    private final String OUT_ITEM_LISTENER = "fifoOutListenerId";
    private final String OUT_ITEM_DELETE_LISTENER = "fifoOutDeleteListenerId";
    private final String IN_ITEM_LISTENER = "fifoInListenerId";
    private final String IN_ITEM_DELETE_LISTENER = "fifoInDeleteListenerId";

    @Override
    public void sendMessage(FifoItem data, Integer companyId, String clusterType) {
    }

    public void sendOutItemMessage(FifoItem data, Integer companyId, String clusterType) {
        sendToDynamic(data, companyId, clusterType, OUT_ITEM_KEY, OUT_ITEM_LISTENER);
    }
    public void sendOutItemDeleteMessage(FifoItem data, Integer companyId, String clusterType) {
        sendToDynamic(data, companyId, clusterType, OUT_ITEM_DELETE_KEY, OUT_ITEM_DELETE_LISTENER);
    }
    public void sendInItemMessage(FifoItem data, Integer companyId, String clusterType) {
        sendToDynamic(data, companyId, clusterType, IN_ITEM_KEY, IN_ITEM_LISTENER);
    }
    public void sendInItemDeleteMessage(FifoItem data, Integer companyId, String clusterType) {
        sendToDynamic(data, companyId, clusterType, IN_ITEM_DELETE_KEY, IN_ITEM_DELETE_LISTENER);
    }
}
