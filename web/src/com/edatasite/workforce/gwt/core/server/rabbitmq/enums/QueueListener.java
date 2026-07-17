package com.edatasite.workforce.gwt.core.server.rabbitmq.enums;

public enum QueueListener {
    BATCH_OUT_LISTENER("batchOutListener"),
    BATCH_OUT_DELETE_LISTENER("batchOutDeleteListener"),
    BATCH_IN_LISTENER("batchInListener"),
    BATCH_IN_DELETE_LISTENER("batchInDeleteListener");

    String listener;

    QueueListener(String listener) {
        this.listener = listener;
    }

    public String getListener() {
        return listener;
    }
}
