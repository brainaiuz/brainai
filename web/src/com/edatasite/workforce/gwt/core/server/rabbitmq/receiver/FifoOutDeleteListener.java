package com.edatasite.workforce.gwt.core.server.rabbitmq.receiver;

import com.edatasite.workforce.gwt.core.server.rabbitmq.data.FifoItem;

public class FifoOutDeleteListener extends FifoOutListener {

    @Override
    protected void doAction(FifoItem fifoItem) {
        reCreateCogsTransactions(fifoItem);
    }

    @Override
    protected String getTransactionType() {
        return "DELETE OUT TRANSACTION";
    }

    @Override
    boolean isValid(FifoItem fifoItem) {
        return true;
    }
}
