package com.edatasite.workforce.gwt.profile.client.rpc;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.profile.client.rpc.locking.TransactionLocking;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.ServiceDefTarget;

public interface TransactionLockingService extends RemoteService {

    TransactionLocking getLock();
    String lock(TransactionLocking lock);
    class App {
        public static TransactionLockingServiceAsync get() {
            ServiceDefTarget target = GWT.create(TransactionLockingService.class);
            target.setServiceEntryPoint(Utils.getRpcBaseUrl() + "/transactionlock");
            return (TransactionLockingServiceAsync) target;
        }
    }
}
