package com.edatasite.workforce.gwt.core.server.rabbitmq.receiver;

import com.edatasite.workforce.core.domain.accounting.EdsTransaction;
import com.edatasite.workforce.gwt.core.server.rabbitmq.data.FIFOItemMQ;

public class BatchFifoOutDeleteListener extends BatchFifoOutListener {

    @Override
    protected void doAction(FIFOItemMQ item, EdsTransaction transaction) {
        reCreateCogsTransactions(item, transaction);
    }

    @Override
    protected String getTransactionType() {
        return "DELETE OUT TRANSACTION";
    }

    @Override
    boolean isValid(FIFOItemMQ item, EdsTransaction transaction) {
        return true;
    }
}
