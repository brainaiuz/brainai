package com.edatasite.workforce.gwt.accounting.client.rpc.target;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * Created by Shohruh on 27-Jan-17.
 */
public interface TargetErpServiceAsync {

    void sendClientToTarget(Integer id, Boolean isClient, AsyncCallback<String> callback);

    void sendInvoiceToTarget(Integer id, AsyncCallback<String> callback);

}
