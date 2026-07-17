package com.edatasite.workforce.gwt.core.server.rabbitmq.enums;

public enum InOutQueue {
    BATCH_OUT_QUEUE("batch_out_queue"),
    BATCH_IN_QUEUE("batch_in_queue"),
    BATCH_OUT_DELETE_QUEUE("batch_out_delete_queue"),
    BATCH_IN_DELETE_QUEUE("batch_in_delete_queue");

    String queue;

    InOutQueue(String queue) {
        this.queue = queue;
    }

    public String getQueue() {
        return queue;
    }
}
