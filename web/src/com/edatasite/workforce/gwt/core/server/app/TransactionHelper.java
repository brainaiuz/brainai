package com.edatasite.workforce.gwt.core.server.app;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Component
public class TransactionHelper {
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void runInANewTransaction(Runnable runnable){
        runnable.run();
    }
}
