package com.edatasite.workforce.gwt.profile.client.rpc;

import com.edatasite.workforce.gwt.profile.client.rpc.locking.TransactionLocking;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface TransactionLockingServiceAsync {

    void getLock(AsyncCallback<TransactionLocking> callback);

    void lock(TransactionLocking lock, AsyncCallback<String> callback);
}
